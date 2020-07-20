
package pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation;

import java.util.HashMap;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTable;


/**
 * @author ODias
 *
 */
public class AnnotationTransporters extends WorkspaceEntity {

	private HashMap<String, String> names;
	private WorkspaceGenericDataTable genericDataTable;
	private HashMap<Integer, Integer[]> searchData;

	/**
	 * @param table
	 * @param name
	 */
	public AnnotationTransporters(WorkspaceTable table, String name) {
		
		super(table, name);
	}
	
	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public String[][] getStats() {

		return this.stats;
	}
	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public void setStats(String[][] stats) {

		this.stats = stats;
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getData()
	 */
	public WorkspaceGenericDataTable getData() {

		return this.genericDataTable;
	}
	
	/**
	 * @param genericDataTable
	 */
	public void setData(WorkspaceGenericDataTable genericDataTable){
		
		this.genericDataTable = genericDataTable;
	}
	
	public HashMap<Integer, Integer[]> getSearchData() {
		
		return searchData;
	}
	
	public String[] getSearchDataIds() {
		
		String[] res = new String[]{"name" , "InChi", "formula",
		};

		return res;
	}
	
	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getRowInfo(java.lang.String)
	 */
	public WorkspaceDataTable[] getRowInfo(String id) {
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#hasWindow()
	 */
	public boolean hasWindow() {
		
		return true;
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getName(java.lang.String)
	 */
	public String getName(String id) {
		
		return this.names.get(id);
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getSingular()
	 */
	public String getSingular() {
		
		return "transport protein: ";
	}
}
