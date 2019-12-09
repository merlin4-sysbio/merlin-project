package pt.uminho.ceb.biosystems.merlin.processes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class ModelMetabolitesProcesses {


	/**
	 * @param data
	 * @return
	 */
	public static String[][] getStats(List<Pair<Integer, String>> data) {

		String[][] res = null;

		
		if(data != null && !data.isEmpty()) {
			res = new String[data.size()][];
	
			int tab = 0;
	
			String[] tabData = new String[] {"Number of reactants", ""+data.get(tab).getA()};
			res[tab] = tabData;
			tab++;
			tabData =new String[] {"Number of products",""+ data.get(tab).getA()};
			res[tab] = tabData;
			tab++;
			tabData =new String[] {"Number of reactions with reactants associated",""+ data.get(tab).getA()};
			res[tab] = tabData;
			tab++;
			tabData =new String[] {"Number of reactions with products associated",""+ data.get(tab).getA()};
			res[tab] = tabData;
			tab++;
			tabData =new String[] {"Metabolites that are reactants and products",""+ data.get(tab).getA()};
			res[tab] = tabData;
			tab++;
			if(data.size()>5) {
				tabData =new String[] {"Number of reactants in compartment" + data.get(tab).getB(),""+ data.get(tab).getA()};
				res[tab] = tabData;
				tab++;
				tabData =new String[] {"Number of products in compartment" + data.get(tab).getB(),""+ data.get(tab).getA()};
				res[tab] = tabData;
				tab++;
			}
		}

		return res;
	}

	/**
	 * @param name
	 * @param data
	 * @param typeMap
	 * @return
	 */
	public static WorkspaceGenericDataTable getMainTableData(String name, Map<Integer, List<Object>> data, Map<Integer, String> typeMap) {

		ArrayList<String> columnsNames = new ArrayList<String>();

		columnsNames.add("info");
		columnsNames.add("metabolite");
		columnsNames.add("compartment");
		columnsNames.add("formula");
		columnsNames.add("external id");
		columnsNames.add("biochemical reactions");
		columnsNames.add("transport reactions");

		WorkspaceGenericDataTable qrt = new WorkspaceGenericDataTable(columnsNames, name, "metabolites") {

			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col) {

				if (col==0) {

					return true;
				}
				else return false;
			}};

			for(int speciesIdentifier : data.keySet())
				qrt.addLine(data.get(speciesIdentifier), typeMap.get(speciesIdentifier), speciesIdentifier);

			return qrt;
	}	

	/**
	 * @param rec
	 * @return
	 */
	public static WorkspaceDataTable[] getRowInfo(Map<String, List<ArrayList<String>>> data) {

		WorkspaceDataTable[] res = new WorkspaceDataTable[data.size()];

		ArrayList<String> columnsNames = new ArrayList<String>();

		int tab = 0;

		columnsNames = new ArrayList<String>();
		columnsNames.add("reaction name");
		columnsNames.add("equations");
		columnsNames.add("source");
		columnsNames.add("in model");
		columnsNames.add("reversible");
		res[tab] = new WorkspaceDataTable(columnsNames, "reactions");

		for(ArrayList<String> line : data.get("reactions"))
			res[tab].addLine(line);

		tab ++;
		
		columnsNames = new ArrayList<String>();
		columnsNames.add("names");
		res[tab]  = new WorkspaceDataTable(columnsNames, "synonyms");

		for(ArrayList<String> line : data.get("synonyms"))
			res[tab].addLine(line);

		tab ++;

		columnsNames = new ArrayList<String>();
		columnsNames.add("entry type");
		res[tab] = new WorkspaceDataTable(columnsNames, "entry type");

		for(ArrayList<String> line : data.get("entry type"))
			res[tab].addLine(line);

		return res;

	}
}
