package pt.uminho.ceb.biosystems.merlin.core.datatypes.model;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTable;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.IEntity;

/**
 * @author ODias
 *
 */
public class ModelEnzymes extends WorkspaceEntity implements IEntity {

	private Map<Integer, Integer[]> searchData;
	private WorkspaceGenericDataTable enzymeDataTable;
	private WorkspaceGenericDataTable trancriptionUnitData;

	/**
	 * @param table
	 * @param name
	 * @param ultimlyComplexComposedBy
	 */
	public ModelEnzymes(WorkspaceTable table, String name, TreeMap<String,LinkedList<String>> ultimlyComplexComposedBy) {

		super(table, name);
	}

	/**
	 * @param encoded
	 * @return
	 */
	public WorkspaceGenericDataTable getAllEnzymes() {
		
		return enzymeDataTable;
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getData()
	 */
	public WorkspaceGenericDataTable getTrancriptionUnitData() {
		return trancriptionUnitData;
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getSearchData()
	 */
	public Map<Integer,Integer[]> getSearchData() {
		
		return searchData;
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#hasWindow()
	 */
	public boolean hasWindow() {

		return true;
	}


	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getSingular()
	 */
	public String getSingular() {

		return "enzyme: ";
	}

	public WorkspaceGenericDataTable getEnzymeDataTable() {
		return enzymeDataTable;
	}

	public void setEnzymeDataTable(WorkspaceGenericDataTable enzymeDataTable) {
		this.enzymeDataTable = enzymeDataTable;
	}

	public void setTrancriptionUnitData(WorkspaceGenericDataTable trancriptionUnitData) {
		this.trancriptionUnitData = trancriptionUnitData;
	}
}
