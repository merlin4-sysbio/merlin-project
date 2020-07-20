package pt.uminho.ceb.biosystems.merlin.core.datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

public class WorkspaceTables extends Observable implements Serializable {

	private static final long serialVersionUID = -3494349774553720072L;
	private ArrayList<WorkspaceTable> list = null;

	public WorkspaceTables() {
		list = new ArrayList<WorkspaceTable>();
	}

	public void addToList(WorkspaceTable b){
		list.add(b);
		setChanged();
		notifyObservers();
	}
	
	public void removeAllTables() {
		
		list.clear();
	}
	
	public String[][] getTableNumbers() throws Exception {
		
		String[][] res = new String[list.size()][];
		
		for(int i=0;i<res.length;i++) {
			WorkspaceTable dbt = list.get(i);
			
			res[i] = new String[2];
			res[i][0] = dbt.getName();
			res[i][1] = dbt.getSize();
		}
		
		return res;
	}
	
	public String getName() {
		return "tables";
	}
	
}
