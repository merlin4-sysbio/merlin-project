package pt.uminho.ceb.biosystems.merlin.entities.model;
// Generated Sep 11, 2019 1:12:29 PM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ModelReactionHasModelProteinId generated by hbm2java
 */
@Embeddable
public class ModelReactionHasModelProteinId implements java.io.Serializable {

	private int modelReactionIdreaction;
	private int modelProteinIdprotein;

	public ModelReactionHasModelProteinId() {
	}

	public ModelReactionHasModelProteinId(int modelReactionIdreaction, int modelProteinIdprotein) {
		this.modelReactionIdreaction = modelReactionIdreaction;
		this.modelProteinIdprotein = modelProteinIdprotein;
	}

	@Column(name = "model_reaction_idreaction", nullable = false)
	public int getModelReactionIdreaction() {
		return this.modelReactionIdreaction;
	}

	public void setModelReactionIdreaction(int modelReactionIdreaction) {
		this.modelReactionIdreaction = modelReactionIdreaction;
	}

	@Column(name = "model_protein_idprotein", nullable = false)
	public int getModelProteinIdprotein() {
		return this.modelProteinIdprotein;
	}

	public void setModelProteinIdprotein(int modelProteinIdprotein) {
		this.modelProteinIdprotein = modelProteinIdprotein;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ModelReactionHasModelProteinId))
			return false;
		ModelReactionHasModelProteinId castOther = (ModelReactionHasModelProteinId) other;

		return (this.getModelReactionIdreaction() == castOther.getModelReactionIdreaction())
				&& (this.getModelProteinIdprotein() == castOther.getModelProteinIdprotein());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getModelReactionIdreaction();
		result = 37 * result + this.getModelProteinIdprotein();
		return result;
	}

}
