package pt.uminho.ceb.biosystems.merlin.processes.regulatory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;

public class RegulatoryProcesses {

	
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
		
		if(dataList.containsKey("sequence")) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("type");
			columnsNames.add("sequence");
			results[tabs] = new WorkspaceDataTable(columnsNames, "sequence");

			for(List<String> list : dataList.get("sequence"))				
				results[tabs].addLine((ArrayList<String>) list);
			tabs++;
		}		
		
		if(dataList.containsKey("annotation")) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("regulation");
			results[tabs] = new WorkspaceDataTable(columnsNames, "regulation");

			for(List<String> list : dataList.get("regulation"))				
				results[tabs].addLine((ArrayList<String>) list);
			tabs++;
		}	
		
		return results;
		
	}
	
}
