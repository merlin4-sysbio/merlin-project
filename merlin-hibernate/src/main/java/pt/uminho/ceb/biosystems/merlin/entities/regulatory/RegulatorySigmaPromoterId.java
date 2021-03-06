package pt.uminho.ceb.biosystems.merlin.entities.regulatory;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RegulatorySigmaPromoterId generated by hbm2java
 */
@Embeddable
public class RegulatorySigmaPromoterId implements java.io.Serializable {

	private int modelProteinIdprotein;
	private int regulatoryPromoterIdpromoter;

	public RegulatorySigmaPromoterId() {
	}

	public RegulatorySigmaPromoterId(int modelProteinIdprotein, int regulatoryPromoterIdpromoter) {
		this.modelProteinIdprotein = modelProteinIdprotein;
		this.regulatoryPromoterIdpromoter = regulatoryPromoterIdpromoter;
	}

	@Column(name = "model_protein_idprotein", nullable = false)
	public int getModelProteinIdprotein() {
		return this.modelProteinIdprotein;
	}

	public void setModelProteinIdprotein(int modelProteinIdprotein) {
		this.modelProteinIdprotein = modelProteinIdprotein;
	}

	@Column(name = "regulatory_promoter_idpromoter", nullable = false)
	public int getRegulatoryPromoterIdpromoter() {
		return this.regulatoryPromoterIdpromoter;
	}

	public void setRegulatoryPromoterIdpromoter(int regulatoryPromoterIdpromoter) {
		this.regulatoryPromoterIdpromoter = regulatoryPromoterIdpromoter;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RegulatorySigmaPromoterId))
			return false;
		RegulatorySigmaPromoterId castOther = (RegulatorySigmaPromoterId) other;

		return (this.getModelProteinIdprotein() == castOther.getModelProteinIdprotein())
				&& (this.getRegulatoryPromoterIdpromoter() == castOther.getRegulatoryPromoterIdpromoter());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getModelProteinIdprotein();
		result = 37 * result + this.getRegulatoryPromoterIdpromoter();
		return result;
	}

}
