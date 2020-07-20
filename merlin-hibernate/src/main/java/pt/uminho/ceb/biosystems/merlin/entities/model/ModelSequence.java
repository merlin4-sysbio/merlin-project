package pt.uminho.ceb.biosystems.merlin.entities.model;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;

/**
 * ModelSequence generated by hbm2java
 */
@Entity
@Table(name = "model_sequence")
public class ModelSequence implements java.io.Serializable {

	private int idsequence;
	private ModelGene modelGene;
	private String sequenceType;
	private String sequence;
	private Integer sequenceLength;
	private Set<EnzymesAnnotationGeneHomology> enzymesAnnotationGeneHomologies = new HashSet<EnzymesAnnotationGeneHomology>(
			0);
	private Set<ModelSequenceFeature> modelSequenceFeatures = new HashSet<ModelSequenceFeature>(0);

	public ModelSequence() {
	}

	public ModelSequence(int idsequence) {
		this.idsequence = idsequence;
	}

	public ModelSequence(int idsequence, ModelGene modelGene, String sequenceType, String sequence,
			Integer sequenceLength, Set<EnzymesAnnotationGeneHomology> enzymesAnnotationGeneHomologies,
			Set<ModelSequenceFeature> modelSequenceFeatures) {
		this.idsequence = idsequence;
		this.modelGene = modelGene;
		this.sequenceType = sequenceType;
		this.sequence = sequence;
		this.sequenceLength = sequenceLength;
		this.enzymesAnnotationGeneHomologies = enzymesAnnotationGeneHomologies;
		this.modelSequenceFeatures = modelSequenceFeatures;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="UseExistingIdOtherwiseGenerateUsingIdentity")
	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "pt.uminho.ceb.biosystems.merlin.auxiliary.UseExistingIdOtherwiseGenerateUsingIdentity")	
	@Column(name = "idsequence", unique = true, nullable = false)
	public int getIdsequence() {
		return this.idsequence;
	}
	
	public void setIdsequence(int idsequence) {
		this.idsequence = idsequence;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_gene_idgene")
	public ModelGene getModelGene() {
		return this.modelGene;
	}
	
	public void setModelGene(ModelGene modelGene) {
		this.modelGene = modelGene;
	}

	@Column(name = "sequence_type", length = 20)
	public String getSequenceType() {
		return this.sequenceType;
	}

	public void setSequenceType(String sequenceType) {
		this.sequenceType = sequenceType;
	}

	@Column(name = "sequence", length = 65535, columnDefinition="Text")
	public String getSequence() {
		return this.sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Column(name = "sequence_length")
	public Integer getSequenceLength() {
		return this.sequenceLength;
	}

	public void setSequenceLength(Integer sequenceLength) {
		this.sequenceLength = sequenceLength;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelSequence")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<EnzymesAnnotationGeneHomology> getEnzymesAnnotationGeneHomologies() {
		return this.enzymesAnnotationGeneHomologies;
	}

	public void setEnzymesAnnotationGeneHomologies(Set<EnzymesAnnotationGeneHomology> enzymesAnnotationGeneHomologies) {
		this.enzymesAnnotationGeneHomologies = enzymesAnnotationGeneHomologies;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelSequence")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelSequenceFeature> getModelSequenceFeatures() {
		return this.modelSequenceFeatures;
	}

	public void setModelSequenceFeatures(Set<ModelSequenceFeature> modelSequenceFeatures) {
		this.modelSequenceFeatures = modelSequenceFeatures;
	}

}
