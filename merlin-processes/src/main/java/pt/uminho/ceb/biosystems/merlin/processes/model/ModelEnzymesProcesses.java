package pt.uminho.ceb.biosystems.merlin.processes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;

/**
 * @author ODias
 *
 */
public class ModelEnzymesProcesses {

	/**
	 * @param result
	 * @return
	 */
	public static String[][] getStats(ArrayList<Integer> result) {

		String[][] res = new String[result.size()][];

		try {

			res[0] = new String[] {"Total number of enzymes", ""+result.get(0)};
			res[1] = new String[] {"      From homology", ""+result.get(1)};
			res[2] = new String[] {"      From KEGG", ""+result.get(2)};
			res[3] = new String[] {"      Added manually", ""+result.get(3)};
			res[4] = new String[] {"Total number of encoded enzymes", ""+result.get(5)};
			res[5] = new String[] {"      From homology", ""+result.get(6)};
			res[6] = new String[] {"      From KEGG", ""+result.get(7)};
			res[7] = new String[] {"       Added manually", ""+result.get(8)};
			res[9] = new String[] {"Total number of transporters", ""+result.get(4)};
			res[10] = new String[] {"Total number of encoded transporters", ""+result.get(9)};
			res[12] = new String[] {"Total number of proteins", ""+(result.get(0)+result.get(4))};
			res[13] = new String[] {"Total number of encoded proteins", ""+(result.get(5)+result.get(9))};
		}
		catch(Exception e){e.printStackTrace();}
		return res;
	}

	/**
	 * @param result
	 * @return
	 */
	public static WorkspaceGenericDataTable getMainTableData(ArrayList<String[]> result) {

		List<String> columnsNames = new ArrayList<String>();

		columnsNames.add("info");
		columnsNames.add("names");
		columnsNames.add("identifier");
		columnsNames.add("number of reactions");
		columnsNames.add("source");
		columnsNames.add("encoded in Genome");
		columnsNames.add("catalysing reactions in model");


		WorkspaceGenericDataTable enzymeDataTable = new WorkspaceGenericDataTable(columnsNames, "Enzymes",""){
			/**
			 * 
			 */
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
			ql.add("");

			for(int i=0;i<6;i++) {

				if(i>3 && i<6) {

					if(i==5) {

						if(Boolean.valueOf(list[i])==false && Integer.parseInt(list[7])==1) {

							ql.add(false);
						}
						else {

							ql.add(true);
						}

					}
					else {

						ql.add(Boolean.valueOf(list[i]));
					}
				}
				else {

					String aux = list[i];

					if(aux!=null) 
						ql.add(aux);
					else 
						ql.add("");	
				}
			}
			enzymeDataTable.addLine(ql,Integer.parseInt(list[6]));
		}
		return enzymeDataTable;
	}
	

	/**
	 * @param ecnumber
	 * @param id
	 * @param connection 
	 * @return
	 */
	public static WorkspaceDataTable[] getRowInfo(List<List<String[]>> results) {

		WorkspaceDataTable[] datatables = new WorkspaceDataTable[6];
		ArrayList<String> columnsNames = new ArrayList<String>();

		columnsNames.add("Reaction");
		columnsNames.add("Equation");
		columnsNames.add("Source");
		columnsNames.add("in Model");
		columnsNames.add("Reversible");
		datatables[0] = new WorkspaceDataTable(columnsNames, "Encoded Reactions");

		columnsNames = new ArrayList<String>();
		columnsNames.add("Name");
		columnsNames.add("Locus tag");
		columnsNames.add("KO");
		columnsNames.add("Origin");
		columnsNames.add("Notes");
		columnsNames.add("Similarity");
		columnsNames.add("Orthologue");
		datatables[1] = new WorkspaceDataTable(columnsNames, "Encoding genes");	

		columnsNames = new ArrayList<String>();
		columnsNames.add("GPR status");
		columnsNames.add("Reaction");
		columnsNames.add("Rule");
		columnsNames.add("Module Name");
		datatables[2] = new WorkspaceDataTable(columnsNames, "Gene-Protein-Reaction");	

		columnsNames = new ArrayList<String>();
		columnsNames.add("Pathway ID");
		columnsNames.add("Pathway Name");
		datatables[3] = new WorkspaceDataTable(columnsNames, "Pathways");	

		columnsNames = new ArrayList<String>();
		columnsNames.add("Synonyms");
		datatables[4] = new WorkspaceDataTable(columnsNames, "Synonyms");

		columnsNames = new ArrayList<String>();
		columnsNames.add("Locus tag");
		columnsNames.add("Compartment");
		columnsNames.add("Score");
		columnsNames.add("Primary Location");
		datatables[5] = new WorkspaceDataTable(columnsNames, "Compartments");

			int r = 0;

			for(int i=0; i<results.get(r++).size(); i++){
				String[] list = results.get(r).get(i);

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

			for(int i=0; i<results.get(r++).size(); i++){
				String[] list = results.get(r).get(i);

				ArrayList<String> ql = new ArrayList<String>();
				ql.add(list[0]);
				ql.add(list[1]);
				ql.add(list[2]);
				ql.add(list[3]);
				ql.add(list[4]);
				ql.add(list[5]);
				ql.add(list[6]);
				datatables[1].addLine(ql);
			}

			for(int i=0; i<results.get(r++).size(); i++){
				String[] list = results.get(r).get(i);

				ArrayList<String> ql = new ArrayList<String>();
				ql.add(list[0]);
				ql.add(list[1]);
				datatables[3].addLine(ql);
			}

			for(int i=0; i<results.get(r++).size(); i++){
				String[] list = results.get(r).get(i);

				ArrayList<String> ql = new ArrayList<String>();
				ql.add(list[0]);
				ql.add(list[1]);
				ql.add(list[2]);
				ql.add(list[3]);

				datatables[2].addLine(ql);
			}

			for(int i=0; i<results.get(r++).size(); i++) {
				String[] list = results.get(r).get(i);

				ArrayList<String> ql = new ArrayList<String>();
				ql.add(list[0]);
				datatables[4].addLine(ql);
			}

			for(int i=0; i<results.get(r++).size(); i++){
				String[] list = results.get(r).get(i);

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

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getData()
	 */
	public static WorkspaceGenericDataTable getTrancriptionUnitData(Map<Integer, List<Object>> map) {

		ArrayList<String> columnsNames = new ArrayList<String>();

		columnsNames.add("Names");
		columnsNames.add("ECnumber");
		columnsNames.add("Optimal pH");
		columnsNames.add("Post translational modification");
		columnsNames.add("Number of coding genes");

		WorkspaceGenericDataTable res = new WorkspaceGenericDataTable(columnsNames, "TUs", "TU");

		for(int key : map.keySet())
			res.addLine(map.get(key), key);

		return res;
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getSearchData()
	 */
	public static HashMap<Integer,Integer[]> getSearchData() {

		HashMap<Integer,Integer[]> res = new HashMap<Integer,Integer[]>();
		res.put(0, new Integer[]{0});
		res.put(1, new Integer[]{1});

		return res;
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getSearchDataIds()
	 */
	public static String[] getSearchDataIds() {

		String[] res = new String[]{"Name", "ECnumber"};

		return res;
	}

}
