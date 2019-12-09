package pt.uminho.ceb.biosystems.merlin.processes.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers.ContainerBuilder;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.GeneCI;

public class ModelGenesProcesses {


	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public static String[][] getStats(double[] results) {

		String[][] res = new String[9][];

		int position = 0;

		res[position] = new String[] {"Number of genes", ""+results[position++]};
		res[position] = new String[] {"Number of genes with no name associated", ""+results[position++]};
		res[position] = new String[] {"Number of genes' synonyms", ""+results[position++]};
		res[position] = new String[] {"Average synonym number by gene", ""+results[position++]};
		res[position] = new String[] {"Number of genes that encode proteins", ""+results[position++]};
		res[position] = new String[] {"       Number of genes that only encode enzymes", ""+results[position++]};
		res[position] = new String[] {"       Number of genes that only encode transporters", ""+results[position++]};
		res[position] = new String[] {"       Number of genes that encode both", ""+results[position++]};
		res[position] = new String[] {"      Number of genes in model", ""+results[position++]};

		return res;
	}




	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getRowInfo(java.lang.String)
	 */
	public static WorkspaceDataTable[] getRowInfo(Map<String,List<List<String>>> dataList) {

		WorkspaceDataTable[] results = new WorkspaceDataTable[dataList.size()];

		int tabs=0;

		List<String> columnsNames = new ArrayList<String>();

		if(dataList.containsKey("synonyms")) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("synonyms");
			results[tabs] = new WorkspaceDataTable(columnsNames, "synonyms");

			for(List<String> list : dataList.get("synonyms"))				
				results[tabs].addLine((ArrayList<String>) list);
			tabs++;
		}
		//		if(dataList.containsKey("regulations")) {
		//
		//			columnsNames = new ArrayList<String>();
		//			columnsNames.add("regulations");
		//			results[tabs] = new WorkspaceDataTable(columnsNames, "regulations");
		//			
		//			for(List<String> list : dataList.get("regulations"))				
		//				results[tabs].addLine((ArrayList<String>) list);
		//			tabs++;
		//		}
		if(dataList.containsKey("orthologs")) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("ortholog");
			columnsNames.add("homologue identifier");
			columnsNames.add("similarity");
			results[tabs] = new WorkspaceDataTable(columnsNames, "orthologs");

			for(List<String> list : dataList.get("orthologs"))				
				results[tabs].addLine((ArrayList<String>) list);
			tabs++;
		}
		if(dataList.containsKey("encoded proteins")) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("encoded proteins");
			columnsNames.add("class");
			columnsNames.add("identifier");
			results[tabs] = new WorkspaceDataTable(columnsNames, "encoded proteins");

			for(List<String> list : dataList.get("encoded proteins"))				
				results[tabs].addLine((ArrayList<String>) list);
			tabs++;
		}
		if(dataList.containsKey("compartments")) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("compartment");
			columnsNames.add("score");
			columnsNames.add("primary Location");

			results[tabs] = new WorkspaceDataTable(columnsNames, "compartments");

			for(List<String> list : dataList.get("compartments"))				
				results[tabs].addLine((ArrayList<String>) list);
			tabs++;
		}
		if(dataList.containsKey("sequence")) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("type");
			columnsNames.add("sequence");
			results[tabs] = new WorkspaceDataTable(columnsNames, "sequence");

			for(List<String> list : dataList.get("sequence"))				
				results[tabs].addLine((ArrayList<String>) list);
			tabs++;
		}		

		return results;
	}

	/**
	 * @param selectedRow
	 * @return
	 */
	public static String[] getSubunits(String[][] res) {

		String[] sub;

		if(res != null && res.length>0) {

			sub = new String[res.length];

			for(int i=0; i<res.length;i++)
				sub[i]=res[i][0];
		}
		else 
			sub = new String[0];

		return sub;
	}

	/**
	 * 
	 * @param res
	 * @return
	 */
	public static String[][] getProteins(String[][] res) {

		String[][] prt = new String[2][res.length+1];
		prt[0][0]="dummy";
		prt[1][0]="";

		for(int i = 0; i<res.length;i++) {

			prt[0][i+1]= res[i][0];
			prt[1][i+1]= res[i][1];
		}
		return prt;
	} 

	/**
	 * @param encoded
	 * @param names
	 * @param identifiers
	 * @param connection
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static WorkspaceGenericDataTable getMainTableData(String databaseName, boolean encoded, Map<Integer,String> names, Map<Integer,Integer> identifiers) throws IOException, Exception {

		ArrayList<String[]> result = null;

		if(encoded)
			result = (ArrayList<String[]>) ModelGenesServices.getEncodingGenes(databaseName);
		else
			result = ModelGenesServices.getAllGenes2(databaseName);

		Container cont = new Container(new ContainerBuilder(databaseName,
				databaseName, ProjectServices.isCompartmentalisedModel(databaseName), false, null,
				null));

		cont.verifyDepBetweenClass();

		Map<String, Integer> reactionsCount = new HashMap<>();

		for(Entry<String, GeneCI> geneEntry : cont.getGenes().entrySet()) {

			reactionsCount.put(geneEntry.getKey(), geneEntry.getValue().getReactionIds().size());
		
		}
		
		ArrayList<String> columnsNames = new ArrayList<String>();
		
		columnsNames.add("info");
		columnsNames.add("locus tag");
		columnsNames.add("names");
		columnsNames.add("number of encoding subunits");
		columnsNames.add("number of encoded proteins");
		columnsNames.add("number of associated reactions");

		WorkspaceGenericDataTable qrt = new WorkspaceGenericDataTable(columnsNames, "genes", "gene data"){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {

				if (col==0) {

					return true;
				}
				else return false;
			}
		};

		for(int i=0; i<result.size(); i++) {

			String[] list = result.get(i);
			
			Integer idGene = Integer.parseInt(list[0]);

			ArrayList<Object> ql = new ArrayList<Object>();
			ql.add("");
			identifiers.put(i,idGene);
			ql.add(list[1]);
			ql.add(list[2]);
			ql.add(list[3]);
			ql.add(list[4]);

			if(reactionsCount.get("G_" + list[1])!=null && reactionsCount.get("G_" + list[1])>0)
				ql.add(Integer.toString(reactionsCount.get("G_" + list[1])));
			else
				ql.add("0");

			names.put(idGene, list[1]);
			
			qrt.addLine(ql, idGene);;
		
		}

		return qrt;
	}
}