package pt.uminho.ceb.biosystems.merlin.entities.model;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ModelPathwayHasModuleId generated by hbm2java
 */
@Embeddable
public class ModelPathwayHasModuleId implements java.io.Serializable {

	private int modelPathwayIdpathway;
	private int modelModuleId;

	public ModelPathwayHasModuleId() {
	}

	public ModelPathwayHasModuleId(int modelPathwayIdpathway, int modelModuleId) {
		this.modelPathwayIdpathway = modelPathwayIdpathway;
		this.modelModuleId = modelModuleId;
	}

	@Column(name = "model_pathway_idpathway", nullable = false)
	public int getModelPathwayIdpathway() {
		return this.modelPathwayIdpathway;
	}

	public void setModelPathwayIdpathway(int modelPathwayIdpathway) {
		this.modelPathwayIdpathway = modelPathwayIdpathway;
	}

	@Column(name = "model_module_id", nullable = false)
	public int getModelModuleId() {
		return this.modelModuleId;
	}

	public void setModelModuleId(int modelModuleId) {
		this.modelModuleId = modelModuleId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ModelPathwayHasModuleId))
			return false;
		ModelPathwayHasModuleId castOther = (ModelPathwayHasModuleId) other;

		return (this.getModelPathwayIdpathway() == castOther.getModelPathwayIdpathway())
				&& (this.getModelModuleId() == castOther.getModelModuleId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getModelPathwayIdpathway();
		result = 37 * result + this.getModelModuleId();
		return result;
	}

}
