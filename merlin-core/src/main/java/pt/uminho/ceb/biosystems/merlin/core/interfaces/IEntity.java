package pt.uminho.ceb.biosystems.merlin.core.interfaces;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;

public interface IEntity {
	
	public void resetDataStuctures();

	public WorkspaceDataTable[] getRowInfo();
	
	public void setRowInfo(WorkspaceDataTable[] rowInfo);
	
	public String[] getSearchDataIdentifiers();
	
	public void setSearchDataIdentifiers(String[] searchDataIdentifiers);
	
	public String getNameFromIndex(int identifier);
	
	public String[][] getStats() throws Exception;	
	
	public void setStats(String[][] stats);	
	
	public void setMainTableData(WorkspaceGenericDataTable data);
	
	public WorkspaceGenericDataTable getMainTableData();
	
}
