package pt.uminho.ceb.biosystems.merlin.core.datatypes.model;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTable;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.IEntity;

public class ModelProteins extends WorkspaceEntity implements IEntity {

	/**
	 * 
	 */
	private WorkspaceGenericDataTable allProteins;
	private String[] proteinData;
	private String[] synonyms;;

	/**
	 * @param dbt
	 * @param name
	 */
	public ModelProteins(WorkspaceTable dbt, String name) {

		super(dbt, name);
	}
	
	/* (non-Javadoc)
	 * @see pt.uminho.ceb.biosystems.merlin.core.datatypes.metabolic_regulatory.Entity#getName(java.lang.String)
	 */
	public String getName(String id) {
		
		return "proteins";
	}

	/**
	 * @return the allProteins
	 */
	public WorkspaceGenericDataTable getAllProteins() {
		return allProteins;
	}

	/**
	 * @param allProteins the allProteins to set
	 */
	public void setAllProteins(WorkspaceGenericDataTable allProteins) {
		this.allProteins = allProteins;
	}

	/**
	 * @param stats the stats to set
	 */
	public void setStats(String[][] stats) {
		this.stats = stats;
	}

	/**
	 * @return the proteinData
	 */
	public String[] getProteinData() {
		return proteinData;
	}

	/**
	 * @param proteinData the proteinData to set
	 */
	public void setProteinData(String[] proteinData) {
		this.proteinData = proteinData;
	}

	/**
	 * @return the synonyms
	 */
	public String[] getSynonyms() {
		return synonyms;
	}

	/**
	 * @param synonyms the synonyms to set
	 */
	public void setSynonyms(String[] synonyms) {
		this.synonyms = synonyms;
	}
	
}
