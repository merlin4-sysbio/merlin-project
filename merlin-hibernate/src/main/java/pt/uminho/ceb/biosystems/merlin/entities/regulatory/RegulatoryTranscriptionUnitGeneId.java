package pt.uminho.ceb.biosystems.merlin.entities.regulatory;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RegulatoryTranscriptionUnitGeneId generated by hbm2java
 */
@Embeddable
public class RegulatoryTranscriptionUnitGeneId implements java.io.Serializable {

	private int regulatoryTranscriptionUnitIdtranscriptionUnit;
	private int modelGeneIdgene;

	public RegulatoryTranscriptionUnitGeneId() {
	}

	public RegulatoryTranscriptionUnitGeneId(int regulatoryTranscriptionUnitIdtranscriptionUnit, int modelGeneIdgene) {
		this.regulatoryTranscriptionUnitIdtranscriptionUnit = regulatoryTranscriptionUnitIdtranscriptionUnit;
		this.modelGeneIdgene = modelGeneIdgene;
	}

	@Column(name = "regulatory_transcription_unit_idtranscription_unit", nullable = false)
	public int getRegulatoryTranscriptionUnitIdtranscriptionUnit() {
		return this.regulatoryTranscriptionUnitIdtranscriptionUnit;
	}

	public void setRegulatoryTranscriptionUnitIdtranscriptionUnit(int regulatoryTranscriptionUnitIdtranscriptionUnit) {
		this.regulatoryTranscriptionUnitIdtranscriptionUnit = regulatoryTranscriptionUnitIdtranscriptionUnit;
	}

	@Column(name = "model_gene_idgene", nullable = false)
	public int getModelGeneIdgene() {
		return this.modelGeneIdgene;
	}

	public void setModelGeneIdgene(int modelGeneIdgene) {
		this.modelGeneIdgene = modelGeneIdgene;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RegulatoryTranscriptionUnitGeneId))
			return false;
		RegulatoryTranscriptionUnitGeneId castOther = (RegulatoryTranscriptionUnitGeneId) other;

		return (this.getRegulatoryTranscriptionUnitIdtranscriptionUnit() == castOther
				.getRegulatoryTranscriptionUnitIdtranscriptionUnit())
				&& (this.getModelGeneIdgene() == castOther.getModelGeneIdgene());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRegulatoryTranscriptionUnitIdtranscriptionUnit();
		result = 37 * result + this.getModelGeneIdgene();
		return result;
	}

}
