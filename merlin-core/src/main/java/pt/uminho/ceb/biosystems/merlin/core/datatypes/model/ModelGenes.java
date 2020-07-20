package pt.uminho.ceb.biosystems.merlin.core.datatypes.model;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTable;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.IEntity;

public class ModelGenes extends WorkspaceEntity implements IEntity {

	private String[][] proteins;
	private GeneContainer geneData;
	private String[] subunits;

	/**
	 * @param dbt
	 * @param name
	 * @param ultimlyComplexComposedBy
	 */
	public ModelGenes(WorkspaceTable dbt, String name) {

		super(dbt, name);
	}

	/**
	 * @return
	 */
	public String[][] getProteins() {
		
		return this.proteins;
	} 

	/**
	 * @param selectedRow
	 * @return
	 */
	public GeneContainer getGeneData() {
		
		return this.geneData;
	}

	/**
	 * @param selectedRow
	 * @return
	 */
	public String[] getSubunits() {
		
		return this.subunits;
	}

	/**
	 * @param proteins the proteins to set
	 */
	public void setProteins(String[][] proteins) {
		this.proteins = proteins;
	}

	/**
	 * @param geneData the geneData to set
	 */
	public void setGeneData(GeneContainer geneData) {
		this.geneData = geneData;
	}

	/**
	 * @param subunits the subunits to set
	 */
	public void setSubunits(String[] subunits) {
		this.subunits = subunits;
	}
}