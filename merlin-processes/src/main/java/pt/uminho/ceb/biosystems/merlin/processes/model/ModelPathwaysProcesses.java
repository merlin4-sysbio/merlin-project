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
public class ModelPathwaysProcesses {

	/**
	 * @param result
	 * @return
	 */
	public static String[][] getStats(List<Integer> result) {

		String[][] res = new String[14][];

		try {
			int counter = 0;

			res[counter] = new String[] {"Number of pathways", ""+result.get(0)};
			res[counter] = new String[] {"Number of pathways with no name associated", ""+result.get(1)};
			res[counter] = new String[] {"Number of pathways with no SBML file associated",""+result.get(2)};
		}
		catch(Exception e){e.printStackTrace();}
		return res;
	}


	

	/**
	 * @param dataList
	 * @return
	 */
	public static WorkspaceDataTable[] getRowInfo(Map<String,  List<List<String>>> dataList) {

		WorkspaceDataTable[] results = new WorkspaceDataTable[dataList.size()];
		ArrayList<String> columnsNames = new ArrayList<String>();
		int tabs=0;

		if(dataList.containsKey("reactions")) {

			columnsNames.add("reactions");
			columnsNames.add("equation");
			results[tabs] = new WorkspaceDataTable(columnsNames, "reactions");


			for(List<String> list : dataList.get("reactions"))				
				results[tabs].addLine((ArrayList<String>) list);
			tabs++;

		}

		if(dataList.containsKey("enzymes")) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("enzymes");
			columnsNames.add("protein name");
			columnsNames.add("class");
			results[tabs] = new WorkspaceDataTable(columnsNames, "enzymes");


			for(List<String> list : dataList.get("enzymes"))				
				results[tabs].addLine((ArrayList<String>) list);
			tabs++;
		}

		return results;
	}

	/**
	 * @param dataTable
	 * @return
	 */
	public static WorkspaceGenericDataTable getMainTableData(Map<Integer, List<Object>> dataTable ) {

		List<String> columnsNames = new ArrayList<String>();

		columnsNames.add("info");
		columnsNames.add("code");
		columnsNames.add("name");
		columnsNames.add("number of reactions");
		columnsNames.add("number of enzymes");

		WorkspaceGenericDataTable res = new WorkspaceGenericDataTable(columnsNames, "promoter", "pathway"){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int col){
				if (col==0)
				{
					return true;
				}
				else return false;
			}};

			for(int key : dataTable.keySet()){

				List<Object> list = dataTable.get(key);
				res.addLine(list, key);
			}

			return res;
	}
	
	/**
	 * @return
	 */
	public static HashMap<Integer,Integer[]> getSearchData() {

		HashMap<Integer,Integer[]> res = new HashMap<Integer,Integer[]>();

		res.put(Integer.valueOf(0), new Integer[]{Integer.valueOf(0)});

		return res;
	}

	/**
	 * @return
	 */
	public static String[] getSearchDataIds() {

		String[] res = new String[]{"Name"};


		return res;
	}

}
