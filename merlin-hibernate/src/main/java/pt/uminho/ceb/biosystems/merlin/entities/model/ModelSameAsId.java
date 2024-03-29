package pt.uminho.ceb.biosystems.merlin.entities.model;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ModelSameAsId generated by hbm2java
 */
@Embeddable
public class ModelSameAsId implements java.io.Serializable {

	private int metaboliteid;
	private int similarMetaboliteid;

	public ModelSameAsId() {
	}

	public ModelSameAsId(int metaboliteid, int similarMetaboliteid) {
		this.metaboliteid = metaboliteid;
		this.similarMetaboliteid = similarMetaboliteid;
	}

	@Column(name = "metaboliteid", nullable = false)
	public int getMetaboliteid() {
		return this.metaboliteid;
	}

	public void setMetaboliteid(int metaboliteid) {
		this.metaboliteid = metaboliteid;
	}

	@Column(name = "similar_metaboliteid", nullable = false)
	public int getSimilarMetaboliteid() {
		return this.similarMetaboliteid;
	}

	public void setSimilarMetaboliteid(int similarMetaboliteid) {
		this.similarMetaboliteid = similarMetaboliteid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ModelSameAsId))
			return false;
		ModelSameAsId castOther = (ModelSameAsId) other;

		return (this.getMetaboliteid() == castOther.getMetaboliteid())
				&& (this.getSimilarMetaboliteid() == castOther.getSimilarMetaboliteid());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getMetaboliteid();
		result = 37 * result + this.getSimilarMetaboliteid();
		return result;
	}

}
