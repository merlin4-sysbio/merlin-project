package pt.uminho.ceb.biosystems.merlin.entities.regulatory;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;

/**
 * RegulatoryTranscriptionUnitGene generated by hbm2java
 */
@Entity
@Table(name = "regulatory_transcription_unit_gene")
public class RegulatoryTranscriptionUnitGene implements java.io.Serializable {

	private RegulatoryTranscriptionUnitGeneId id;
	private ModelGene modelGene;
	private RegulatoryTranscriptionUnit regulatoryTranscriptionUnit;

	public RegulatoryTranscriptionUnitGene() {
	}

	public RegulatoryTranscriptionUnitGene(RegulatoryTranscriptionUnitGeneId id, ModelGene modelGene,
			RegulatoryTranscriptionUnit regulatoryTranscriptionUnit) {
		this.id = id;
		this.modelGene = modelGene;
		this.regulatoryTranscriptionUnit = regulatoryTranscriptionUnit;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "regulatoryTranscriptionUnitIdtranscriptionUnit", column = @Column(name = "regulatory_transcription_unit_idtranscription_unit", nullable = false)),
			@AttributeOverride(name = "modelGeneIdgene", column = @Column(name = "model_gene_idgene", nullable = false)) })
	public RegulatoryTranscriptionUnitGeneId getId() {
		return this.id;
	}

	public void setId(RegulatoryTranscriptionUnitGeneId id) {
		this.id = id;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_gene_idgene", nullable = false, insertable = false, updatable = false)
	public ModelGene getModelGene() {
		return this.modelGene;
	}

	public void setModelGene(ModelGene modelGene) {
		this.modelGene = modelGene;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "regulatory_transcription_unit_idtranscription_unit", nullable = false, insertable = false, updatable = false)
	public RegulatoryTranscriptionUnit getRegulatoryTranscriptionUnit() {
		return this.regulatoryTranscriptionUnit;
	}

	public void setRegulatoryTranscriptionUnit(RegulatoryTranscriptionUnit regulatoryTranscriptionUnit) {
		this.regulatoryTranscriptionUnit = regulatoryTranscriptionUnit;
	}

}
