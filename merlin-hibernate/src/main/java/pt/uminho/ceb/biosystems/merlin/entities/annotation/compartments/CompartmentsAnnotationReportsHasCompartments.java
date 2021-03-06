package pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments;
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
 * CompartmentsAnnotationReportsHasCompartments generated by hbm2java
 */
@Entity
@Table(name = "compartments_annotation_reports_has_compartments")
public class CompartmentsAnnotationReportsHasCompartments implements java.io.Serializable {

	private CompartmentsAnnotationReportsHasCompartmentsId id;
	private CompartmentsAnnotationCompartments compartmentsAnnotationCompartments;
	private ModelGene modelGene;
	private Float score;

	public CompartmentsAnnotationReportsHasCompartments() {
	}

	public CompartmentsAnnotationReportsHasCompartments(CompartmentsAnnotationReportsHasCompartmentsId id,
			CompartmentsAnnotationCompartments compartmentsAnnotationCompartments, ModelGene modelGene) {
		this.id = id;
		this.compartmentsAnnotationCompartments = compartmentsAnnotationCompartments;
		this.modelGene = modelGene;
	}

	public CompartmentsAnnotationReportsHasCompartments(CompartmentsAnnotationReportsHasCompartmentsId id,
			CompartmentsAnnotationCompartments compartmentsAnnotationCompartments, ModelGene modelGene, Float score) {
		this.id = id;
		this.compartmentsAnnotationCompartments = compartmentsAnnotationCompartments;
		this.modelGene = modelGene;
		this.score = score;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "modelGeneIdgene", column = @Column(name = "model_gene_idgene", nullable = false)),
			@AttributeOverride(name = "compartmentsAnnotationCompartmentsId", column = @Column(name = "compartments_annotation_compartments_id", nullable = false)) })
	public CompartmentsAnnotationReportsHasCompartmentsId getId() {
		return this.id;
	}

	public void setId(CompartmentsAnnotationReportsHasCompartmentsId id) {
		this.id = id;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "compartments_annotation_compartments_id", nullable = false, insertable = false, updatable = false)
	public CompartmentsAnnotationCompartments getCompartmentsAnnotationCompartments() {
		return this.compartmentsAnnotationCompartments;
	}

	public void setCompartmentsAnnotationCompartments(
			CompartmentsAnnotationCompartments compartmentsAnnotationCompartments) {
		this.compartmentsAnnotationCompartments = compartmentsAnnotationCompartments;
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

	@Column(name = "score", precision = 12, scale = 0)
	public Float getScore() {
		return this.score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

}
