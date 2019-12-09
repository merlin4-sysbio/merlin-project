package pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTable;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.IEntity;

public class AnnotationCompartments extends WorkspaceEntity implements IEntity {

	protected double threshold;

	/**
	 * @param dbt
	 * @param name
	 */
	public AnnotationCompartments(WorkspaceTable dbt, String name) {

		super(dbt, name);
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getName()
	 */
	public String getName() {

		return "compartments";
	}

	/**
	 * @param thold
	 */
	public void setThreshold(Double thold) {

		this.threshold = thold;
	}

	/**
	 * @param 
	 * @return
	 */
	public Double getThreshold() {

		return this.threshold;
	}
}
