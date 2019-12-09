package pt.uminho.ceb.biosystems.merlin.entities.model;
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

/**
 * ModelGeneHasOrthology generated by hbm2java
 */
@Entity
@Table(name = "model_gene_has_orthology")
public class ModelGeneHasOrthology implements java.io.Serializable {

	private ModelGeneHasOrthologyId id;
	private ModelGene modelGene;
	private ModelOrthology modelOrthology;
	private Float similarity;

	public ModelGeneHasOrthology() {
	}

	public ModelGeneHasOrthology(ModelGeneHasOrthologyId id, ModelGene modelGene, ModelOrthology modelOrthology) {
		this.id = id;
		this.modelGene = modelGene;
		this.modelOrthology = modelOrthology;
	}

	public ModelGeneHasOrthology(ModelGeneHasOrthologyId id, ModelGene modelGene, ModelOrthology modelOrthology,
			Float similarity) {
		this.id = id;
		this.modelGene = modelGene;
		this.modelOrthology = modelOrthology;
		this.similarity = similarity;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "modelOrthologyId", column = @Column(name = "model_orthology_id", nullable = false)),
			@AttributeOverride(name = "modelGeneIdgene", column = @Column(name = "model_gene_idgene", nullable = false)) })
	public ModelGeneHasOrthologyId getId() {
		return this.id;
	}

	public void setId(ModelGeneHasOrthologyId id) {
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
	@JoinColumn(name = "model_orthology_id", nullable = false, insertable = false, updatable = false)
	public ModelOrthology getModelOrthology() {
		return this.modelOrthology;
	}

	public void setModelOrthology(ModelOrthology modelOrthology) {
		this.modelOrthology = modelOrthology;
	}

	@Column(name = "similarity", precision = 12, scale = 0)
	public Float getSimilarity() {
		return this.similarity;
	}

	public void setSimilarity(Float similarity) {
		this.similarity = similarity;
	}

}
