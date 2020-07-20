package pt.uminho.ceb.biosystems.merlin.entities.model;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ModelEffectorId generated by hbm2java
 */
@Embeddable
public class ModelEffectorId implements java.io.Serializable {

	private int modelProteinIdprotein;
	private int modelCompoundIdcompound;

	public ModelEffectorId() {
	}

	public ModelEffectorId(int modelProteinIdprotein, int modelCompoundIdcompound) {
		this.modelProteinIdprotein = modelProteinIdprotein;
		this.modelCompoundIdcompound = modelCompoundIdcompound;
	}

	@Column(name = "model_protein_idprotein", nullable = false)
	public int getModelProteinIdprotein() {
		return this.modelProteinIdprotein;
	}

	public void setModelProteinIdprotein(int modelProteinIdprotein) {
		this.modelProteinIdprotein = modelProteinIdprotein;
	}

	@Column(name = "model_compound_idcompound", nullable = false)
	public int getModelCompoundIdcompound() {
		return this.modelCompoundIdcompound;
	}

	public void setModelCompoundIdcompound(int modelCompoundIdcompound) {
		this.modelCompoundIdcompound = modelCompoundIdcompound;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ModelEffectorId))
			return false;
		ModelEffectorId castOther = (ModelEffectorId) other;

		return (this.getModelProteinIdprotein() == castOther.getModelProteinIdprotein())
				&& (this.getModelCompoundIdcompound() == castOther.getModelCompoundIdcompound());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getModelProteinIdprotein();
		result = 37 * result + this.getModelCompoundIdcompound();
		return result;
	}

}
