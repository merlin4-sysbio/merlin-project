package pt.uminho.ceb.biosystems.merlin.entities.model;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ModelPathwayHasReactionId generated by hbm2java
 */
@Embeddable
public class ModelPathwayHasReactionId implements java.io.Serializable {

	private int pathwayIdpathway;
	private int reactionIdreaction;

	public ModelPathwayHasReactionId() {
	}

	public ModelPathwayHasReactionId(int pathwayIdpathway, int reactionIdreaction) {
		this.pathwayIdpathway = pathwayIdpathway;
		this.reactionIdreaction = reactionIdreaction;
	}

	@Column(name = "pathway_idpathway", nullable = false)
	public int getPathwayIdpathway() {
		return this.pathwayIdpathway;
	}

	public void setPathwayIdpathway(int pathwayIdpathway) {
		this.pathwayIdpathway = pathwayIdpathway;
	}

	@Column(name = "reaction_idreaction", nullable = false)
	public int getReactionIdreaction() {
		return this.reactionIdreaction;
	}

	public void setReactionIdreaction(int reactionIdreaction) {
		this.reactionIdreaction = reactionIdreaction;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ModelPathwayHasReactionId))
			return false;
		ModelPathwayHasReactionId castOther = (ModelPathwayHasReactionId) other;

		return (this.getPathwayIdpathway() == castOther.getPathwayIdpathway())
				&& (this.getReactionIdreaction() == castOther.getReactionIdreaction());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getPathwayIdpathway();
		result = 37 * result + this.getReactionIdreaction();
		return result;
	}

}
