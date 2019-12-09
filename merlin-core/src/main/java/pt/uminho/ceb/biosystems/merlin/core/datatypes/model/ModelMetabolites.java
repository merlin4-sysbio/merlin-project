package pt.uminho.ceb.biosystems.merlin.core.datatypes.model;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTable;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.IEntity;

public class ModelMetabolites extends WorkspaceEntity implements IEntity {

	private WorkspaceGenericDataTable dataReagentProduct;
	private WorkspaceDataTable[] reaction;
	private int metaboliteOccurrence;
	private String keggOccurence;
	private MetaboliteContainer metaboliteData;
	private List<String> relatedReactions;

	/**
	 * @param dbt
	 * @param name
	 */
	public ModelMetabolites(WorkspaceTable dbt, String name) {
		super(dbt, name);
		relatedReactions = new ArrayList<String>();
	}

	/* (non-Javadoc)
	 * @see pt.uminho.ceb.biosystems.merlin.core.datatypes.metabolic_regulatory.Entity#getName(java.lang.String)
	 */
	public String getName(String id) {
		
		return "metabolites";
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getSingular()
	 */
	public String getSingular() {
		
		return "metabolite: ";
	}

	/**
	 * @return the dataReagentProduct
	 */
	public WorkspaceGenericDataTable getDataReagentProduct() {
		return dataReagentProduct;
	}

	/**
	 * @param dataReagentProduct the dataReagentProduct to set
	 */
	public void setDataReagentProduct(WorkspaceGenericDataTable dataReagentProduct) {
		this.dataReagentProduct = dataReagentProduct;
	}

	/**
	 * @return the reaction
	 */
	public WorkspaceDataTable[] getReaction() {
		return reaction;
	}

	/**
	 * @param reaction the reaction to set
	 */
	public void setReaction(WorkspaceDataTable[] reaction) {
		this.reaction = reaction;
	}

	/**
	 * @return the metaboliteOccurrence
	 */
	public int getMetaboliteOccurrence() {
		return metaboliteOccurrence;
	}

	/**
	 * @param metaboliteOccurrence the metaboliteOccurrence to set
	 */
	public void setMetaboliteOccurrence(int metaboliteOccurrence) {
		this.metaboliteOccurrence = metaboliteOccurrence;
	}

	/**
	 * @return the keggOccurence
	 */
	public String getKeggOccurence() {
		return keggOccurence;
	}

	/**
	 * @param keggOccurence the keggOccurence to set
	 */
	public void setKeggOccurence(String keggOccurence) {
		this.keggOccurence = keggOccurence;
	}

	/**
	 * @return the metaboliteData
	 */
	public MetaboliteContainer getMetaboliteData() {
		return metaboliteData;
	}

	/**
	 * @param metaboliteData the metaboliteData to set
	 */
	public void setMetaboliteData(MetaboliteContainer metaboliteData) {
		this.metaboliteData = metaboliteData;
	}

	/**
	 * @return the relatedReactions
	 */
	public List<String> getRelatedReactions() {
		return relatedReactions;
	}

	/**
	 * @param relatedReactions the relatedReactions to set
	 */
	public void setRelatedReactions(List<String> relatedReactions) {
		this.relatedReactions = relatedReactions;
	}
}
