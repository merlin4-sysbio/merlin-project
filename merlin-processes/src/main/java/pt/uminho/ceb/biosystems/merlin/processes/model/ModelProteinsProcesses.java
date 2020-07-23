package pt.uminho.ceb.biosystems.merlin.processes.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;

public class ModelProteinsProcesses {

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public static String[][] getStats(List<Integer> values) {

		String[][] res = new String[values.size()][];

		int i = 0;
		res[i] = new String[] {"Number of proteins", ""+values.get(i++)};

		res[i]= new String[] {"Number of proteins with no name associated", ""+values.get(i++)};

		res[i]= new String[] {"Number of proteins synonyms", values.get(i++)+""};

		res[i] = new String[] {"Average number synonyms by protein", ""+values.get(i++)};

		res[i] = new String[] {"Number of proteins that are enzymes", ""+values.get(i++)};

		res[i]  = new String[] {"Number of proteins that are transporters", ""+values.get(i++)};

		res[i] = new String[] {"Number of proteins that are complexes", ""+values.get(i++)};

		res[i]  = new String[] {"Number of proteins associated to genes", ""+values.get(i++)+""};

		return res;
	}

	/**
	 * @param proteins
	 * @param result
	 * @param namesIndex
	 * @param ids
	 * @return
	 */
	public static WorkspaceGenericDataTable getMainTableData(boolean encoded, List<String[]> result, Map<Integer, String> namesIndex, Map<Integer, Integer> ids ) {

		namesIndex = new HashMap<>();
		List<String> columnsNames = new ArrayList<String>();

		columnsNames.add("info");
		columnsNames.add("names");
		columnsNames.add("identifier");
		if(encoded)
			columnsNames.add("number of reactions in model");
		else
			columnsNames.add("number of reactions");
		columnsNames.add("encoding genes");
//		columnsNames.add("encoded in Genome");
//		columnsNames.add("catalysing reactions in Model");

		WorkspaceGenericDataTable enzymeDataTable = new WorkspaceGenericDataTable(columnsNames, "proteins",""){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col){
				if (col==0){return true;}
				else {return false;}
			}
		};


		for(int index=0; index<result.size(); index++){
			String[] list = result.get(index);

			ArrayList<Object> ql = new ArrayList<Object>();
			ql.add(""); //info

			for(int i=0;i<4;i++) {

//				if(i>3 && i<5) {
//
//					if(i==4) {
//
//						if(Boolean.valueOf(list[i])==false && Integer.parseInt(list[6])==1)
//							ql.add(false); 
//						else 
//							ql.add(true); 
//					}
//					else {
//
//						ql.add(Boolean.valueOf(list[i]));
//					}
//				}
//				else {
					if (i==3) {
						
//						if(proteins.containsKey(list[5]))
//							ql.add(proteins.get(list[5])+"");
//						else
//							ql.add("0");
						ql.add(list[7]);
					}
					else {

						String aux = list[i];

						if(aux!=null) 
							ql.add(aux);
						else 
							ql.add("");	
					}
//				}
			}
			enzymeDataTable.addLine(ql,Integer.parseInt(list[6]));

			namesIndex.put(index, list[0]);
			ids.put(index, Integer.parseInt(list[6]));
		}

		return enzymeDataTable;
	}

	/**
	 * @param ecnumber
	 * @param id
	 * @param resultPathways 
	 * @param resultSubunit 
	 * @param resultAlias 
	 * @param resultGeneData 
	 * @return
	 */
	public static WorkspaceDataTable[] getRowInfo(List<List<String[]>> data) {

		int index = 0;
		List<String[]> resultReactions = data.get(index++);
		List<String[]> resultGenes = data.get(index++);
		List<String[]> resultPathways = data.get(index++);
		List<String[]> resultSubunit = data.get(index++);
		List<String[]> resultAlias = data.get(index++);
		List<String[]> resultGeneData = data.get(index++);
		
		WorkspaceDataTable[] datatables = new WorkspaceDataTable[6];
		ArrayList<String> columnsNames = new ArrayList<String>();

		columnsNames.add("reaction");
		columnsNames.add("equation");
		columnsNames.add("source");
		columnsNames.add("in model");
		columnsNames.add("reversible");
		datatables[0] = new WorkspaceDataTable(columnsNames, "Encoded Reactions");

		columnsNames = new ArrayList<String>();
		columnsNames.add("name");
		columnsNames.add("locus tag");
		columnsNames.add("KO");
		columnsNames.add("origin");
		columnsNames.add("similarity");
		columnsNames.add("orthologue");
		datatables[1] = new WorkspaceDataTable(columnsNames, "Encoding genes");	

		columnsNames = new ArrayList<String>();
		columnsNames.add("gpr status");
		columnsNames.add("reaction");
		columnsNames.add("rule");
		columnsNames.add("module name");
		datatables[2] = new WorkspaceDataTable(columnsNames, "Gene-Protein-Reaction");	

		columnsNames = new ArrayList<String>();
		columnsNames.add("pathway ID");
		columnsNames.add("pathway name");
		datatables[3] = new WorkspaceDataTable(columnsNames, "Pathways");	

		columnsNames = new ArrayList<String>();
		columnsNames.add("synonyms");
		datatables[4] = new WorkspaceDataTable(columnsNames, "Synonyms");

		columnsNames = new ArrayList<String>();
		columnsNames.add("locus tag");
		columnsNames.add("compartment");
		columnsNames.add("score");
		columnsNames.add("primary location");
		datatables[5] = new WorkspaceDataTable(columnsNames, "Compartments");
		
		
		for(int i=0; i<resultReactions.size(); i++){
			String[] list = resultReactions.get(i);

			ArrayList<String> ql = new ArrayList<String>();
			ql.add(list[0]);
			ql.add(list[1]);
			ql.add(list[2]);

			if(Boolean.valueOf(list[3]))					
				ql.add("true");
			else					
				ql.add("-");

			if(Boolean.valueOf(list[4]))					
				ql.add("true");
			else					
				ql.add("-");

			datatables[0].addLine(ql);
		}
		
		
		for(int i=0; i<resultGenes.size(); i++){
			String[] list = resultGenes.get(i);
			
			ArrayList<String> ql = new ArrayList<String>();
			ql.add(list[0]);
			ql.add(list[1]);
			ql.add(list[2]);
			ql.add(list[3]);
			ql.add(list[4]);
			ql.add(list[5]);

			datatables[1].addLine(ql);
		}


		for(int i=0; i<resultPathways.size(); i++){
			String[] list = resultPathways.get(i);

			ArrayList<String> ql = new ArrayList<String>();
			ql.add(list[0]);
			ql.add(list[1]);
			datatables[3].addLine(ql);
		}

		for(int i=0; i<resultSubunit.size(); i++){
			String[] list = resultSubunit.get(i);

			ArrayList<String> ql = new ArrayList<String>();
			ql.add(list[0]);
			ql.add(list[1]);
			ql.add(list[2]);
			ql.add(list[3]);

			datatables[2].addLine(ql);
		}

		for(int i=0; i<resultAlias.size(); i++) {
			for(int j=0; j<resultAlias.get(i).length; j++) {

				ArrayList<String> ql = new ArrayList<String>();
				ql.add(resultAlias.get(i)[j]);
				datatables[4].addLine(ql);
			}
		}

		for(int i=0; i<resultGeneData.size(); i++){
			String[] list = resultGeneData.get(i);

			ArrayList<String> ql = new ArrayList<String>();
			ql.add(list[4]);
			ql.add(list[1]);
			ql.add(list[3]);

			if(Boolean.valueOf(list[2]))
				ql.add(list[2]);
			else
				ql.add("");

			datatables[5].addLine(ql);
		}

		return datatables;
	}

	/**
	 * @param selectedRow
	 * @return
	 */
	public static String[] getSynonyms(String[][] synonyms) {


		String[] data = new String[synonyms.length];

		int i=0;
		while(i<synonyms.length) {
		
			data[i]=synonyms[i][0];
			i++;
		}
		
		return data;
	}

}
