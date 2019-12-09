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
 * ModelGeneHasCompartment generated by hbm2java
 */
@Entity
@Table(name = "model_gene_has_compartment")
public class ModelGeneHasCompartment implements java.io.Serializable {

	private ModelGeneHasCompartmentId id;
	private ModelCompartment modelCompartment;
	private ModelGene modelGene;
	private Boolean primaryLocation;
	private String score;

	public ModelGeneHasCompartment() {
	}

	public ModelGeneHasCompartment(ModelGeneHasCompartmentId id, ModelCompartment modelCompartment,
			ModelGene modelGene) {
		this.id = id;
		this.modelCompartment = modelCompartment;
		this.modelGene = modelGene;
	}

	public ModelGeneHasCompartment(ModelGeneHasCompartmentId id, ModelCompartment modelCompartment, ModelGene modelGene,
			Boolean primaryLocation, String score) {
		this.id = id;
		this.modelCompartment = modelCompartment;
		this.modelGene = modelGene;
		this.primaryLocation = primaryLocation;
		this.score = score;
	}
	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "modelCompartmentIdcompartment", column = @Column(name = "model_compartment_idcompartment", nullable = false)),
			@AttributeOverride(name = "modelGeneIdgene", column = @Column(name = "model_gene_idgene", nullable = false)) })
	public ModelGeneHasCompartmentId getId() {
		return this.id;
	}

	public void setId(ModelGeneHasCompartmentId id) {
		this.id = id;
	}
    @XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_compartment_idcompartment", nullable = false, insertable = false, updatable = false)
	public ModelCompartment getModelCompartment() {
		return this.modelCompartment;
	}

	public void setModelCompartment(ModelCompartment modelCompartment) {
		this.modelCompartment = modelCompartment;
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

	@Column(name = "primaryLocation")
	public Boolean getPrimaryLocation() {
		return this.primaryLocation;
	}
	public void setPrimaryLocation(Boolean primaryLocation) {
		this.primaryLocation = primaryLocation;
	}

	@Column(name = "score", length = 20)
	public String getScore() {
		return this.score;
	}
	public void setScore(String score) {
		this.score = score;
	}

}