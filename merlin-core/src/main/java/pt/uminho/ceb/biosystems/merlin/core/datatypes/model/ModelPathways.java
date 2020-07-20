package pt.uminho.ceb.biosystems.merlin.core.datatypes.model;

import java.util.HashMap;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTable;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.IEntity;


/**
 * @author ODias
 *
 */
public class ModelPathways extends WorkspaceEntity implements IEntity {

	private HashMap<String, String> names;
	private WorkspaceGenericDataTable data;
	private HashMap<Integer, Integer[]> searchData;
	
	/**
	 * @param dbt
	 * @param name
	 */
	public ModelPathways(WorkspaceTable dbt, String name) {
		
		super(dbt, name);
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getData()
	 */
	public WorkspaceGenericDataTable getData() {
		
		return this.data;
	}

	public HashMap<Integer,Integer[]> getSearchData() {
		
		return this.searchData;
	}

	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#hasWindow()
	 */
	public boolean hasWindow() {
		return true;
	}

	public String getName(String id) {
		
		return this.names.get(id);
	}

	public String getSingular() {
		
		return "pathway: ";
	}

	/**
	 * @param data the data to set
	 */
	public void setData(WorkspaceGenericDataTable data) {
		this.data = data;
	}

	/**
	 * @param searchData the searchData to set
	 */
	public void setSearchData(HashMap<Integer, Integer[]> searchData) {
		this.searchData = searchData;
	}

}
