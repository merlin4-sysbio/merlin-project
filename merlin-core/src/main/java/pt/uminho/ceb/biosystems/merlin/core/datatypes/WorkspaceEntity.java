package pt.uminho.ceb.biosystems.merlin.core.datatypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import pt.uminho.ceb.biosystems.merlin.core.interfaces.IEntity;


/**
 * @author ODias
 *
 */

public abstract class WorkspaceEntity extends Observable implements IEntity {


	protected Map<Integer, String> namesIndex;
	protected ArrayList<WorkspaceEntity> subenties = null;
	protected WorkspaceTable table;
	protected String name;

	private Workspace workspace;
	protected WorkspaceDataTable[] rowInfo;
	protected String[][] stats;
	private String[] searchDataIdentifiers;
	protected WorkspaceGenericDataTable mainTableData;
	protected Map<Integer, Integer> identifiers;

	/**
	 * @param dbt
	 * @param name
	 */
	public WorkspaceEntity(WorkspaceTable dbt, String name) {
		
		this.subenties = new ArrayList<WorkspaceEntity>();
		this.table = dbt;
		this.name = name;
	}
	
	/**
	 *
	 */
	public void resetDataStuctures() {
		
		this.namesIndex = new HashMap<>();
		this.identifiers = new HashMap<>();
		this.subenties = null;
		this.rowInfo = null;
		this.stats = null;
		this.mainTableData=null;
	}

	public WorkspaceTable getDbt() {
		return table;
	}

	public void setDbt(WorkspaceTable dbt) {
		this.table = dbt;
	}

	public String getName() {
		return name.toLowerCase();
	}

	public String toString() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<WorkspaceEntity> getSubenties() {
		return subenties;
	}

	public void setSubenties(ArrayList<WorkspaceEntity> subenties) {
		this.subenties = subenties;
	}

	public void addEl(WorkspaceEntity ent){
		subenties.add(ent);
		setChanged();
		notifyObservers();
	}
	
	
	public WorkspaceGenericDataTable getData() {
		
		ArrayList<String> columnsNames = new ArrayList<String>();
		WorkspaceGenericDataTable res = new WorkspaceGenericDataTable(columnsNames, "", "");
		
		return res;
	}
	
	/**
	 * @return
	 */
	public Map<Integer,Integer[]> getSearchData() {
		
		Map<Integer,Integer[]> res = new HashMap<Integer,Integer[]>();
		return res;
	}
	
	public String[] getSearchDataIds() {
		
		String[] res = new String[0];
		
		return res;
	}
	
	public boolean hasWindow()
	{
		return false;
	}
	
	public String getSingular()
	{
		return "";
	}
	
	/**
	 * @param workspace
	 */
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;	
		
	}

	/**
	 * @return the project
	 */
	public Workspace getWorkspace() {
		return workspace;
	}

	public WorkspaceDataTable[] getRowInfo() {
		
		return rowInfo;
	}
	
	public void setRowInfo(WorkspaceDataTable[] rowInfo) {
		
		this.rowInfo = rowInfo;
	}
	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public String[][] getStats() {
		
		return this.stats;
	}
	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getSearchDataIds()
	 */
	public String[] getSearchDataIdentifiers() {

		return searchDataIdentifiers;
	}
	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getName(java.lang.String)
	 */
	public String getNameFromIndex(int identifier) {

		return this.namesIndex.get(identifier);
	}
	
	public void setSearchDataIdentifiers(String[] searchDataIdentifiers) {
		
		this.searchDataIdentifiers = searchDataIdentifiers;
	}
	
	@Override
	public void setStats(String[][] stats) {

		this.stats = stats;
	}

	@Override
	public void setMainTableData(WorkspaceGenericDataTable mainTableData) {
		
		this.mainTableData = mainTableData;
	}

	@Override
	public WorkspaceGenericDataTable getMainTableData() {

		return mainTableData;
	}

	public Map<Integer, String> getNamesIndex() {
		return namesIndex;
	}

	public void setNamesIndex(Map<Integer, String> namesIndex) {
		this.namesIndex = namesIndex;
	}
	
	/**
	 * @return
	 */
	public Map<Integer,Integer> getIdentifiers() {

		return identifiers;
	}

	public void setIdentifiers(Map<Integer, Integer> identifiers) {
		this.identifiers = identifiers;
	}

}
