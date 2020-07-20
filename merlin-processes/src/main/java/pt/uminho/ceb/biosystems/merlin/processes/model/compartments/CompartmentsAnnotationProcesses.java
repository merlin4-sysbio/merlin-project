package pt.uminho.ceb.biosystems.merlin.processes.model.compartments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;

public class CompartmentsAnnotationProcesses {


	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public static String[][] getStats(List<Integer> result) {
		
		String[][] res = new String[2][];

		res[0] = new String[] {"Number of genes with predicted compartments", ""+result.get(0)};
		res[1] = new String[] {"Number of distinct compartments ", ""+result.get(1)};
			
		return res;
	}

	/**
	 * 
	 * @param result
	 * @return
	 */
	public static WorkspaceGenericDataTable getMainTableData(Map<Integer, ArrayList<Object>> dataTable) {
		
		ArrayList<String> columnsNames = new ArrayList<String>();

		columnsNames.add("info");
		columnsNames.add("genes");
		columnsNames.add("primary compartment");
		columnsNames.add("score");
		columnsNames.add("secondary compartments");
		columnsNames.add("scores");

		WorkspaceGenericDataTable qrt = new WorkspaceGenericDataTable(columnsNames, "genes", "gene data") {
			
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {

				if (col==0 || col==4) {

					return true;
				}
				else return false;
			}
		};
		
		for(int key : dataTable.keySet()){

			ArrayList<Object> list = dataTable.get(key);
			qrt.addLine(list, key);
		}

		return qrt;
	}

	/**
	 * @param dataList
	 * @return
	 */
	public static WorkspaceDataTable[] getRowInfo(Map<String,List<List<String>>> dataList) {

		WorkspaceDataTable[] results = new WorkspaceDataTable[1];

		List<String> columnsNames = new ArrayList<String>();
		columnsNames.add("compartment");
		columnsNames.add("score");
		results[0] = new WorkspaceDataTable(columnsNames, "compartments");

		for(List<String> list : dataList.get("compartments"))				
			results[0].addLine((List<String>) list);
			
		return results;
	}
}
