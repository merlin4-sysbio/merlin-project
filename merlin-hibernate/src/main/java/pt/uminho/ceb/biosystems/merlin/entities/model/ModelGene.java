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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.CompartmentsAnnotationReportsHasCompartments;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryTranscriptionUnitGene;


/**
 * ModelGene generated by hbm2java
 */

@Entity
@Table(name = "model_gene")
public class ModelGene implements java.io.Serializable {

	private int idgene;
	private String name;
	private String locusTag;
	private String transcriptionDirection;
	private String leftEndPosition;
	private String rightEndPosition;
	private String booleanRule;
	private String origin;
	private String query;
	private Set<CompartmentsAnnotationReportsHasCompartments> compartmentsAnnotationReportsHasCompartmentses = new HashSet<CompartmentsAnnotationReportsHasCompartments>(
			0);
	private Set<ModelSequence> modelSequences = new HashSet<ModelSequence>(0);
	private Set<ModelGeneHasOrthology> modelGeneHasOrthologies = new HashSet<ModelGeneHasOrthology>(0);
	private Set<ModelSubunit> modelSubunits = new HashSet<ModelSubunit>(0);
	private Set<ModelGeneHasCompartment> modelGeneHasCompartments = new HashSet<ModelGeneHasCompartment>(0);
	private Set<RegulatoryTranscriptionUnitGene> regulatoryTranscriptionUnitGenes = new HashSet<RegulatoryTranscriptionUnitGene>(
			0);

	public ModelGene() {
	}

	public ModelGene(int idgene, String origin, String query) {
		this.idgene = idgene;
		this.origin = origin;
		this.query = query;
	}

	public ModelGene(int idgene, String name, String locusTag, String transcriptionDirection, String leftEndPosition,
			String rightEndPosition, String booleanRule, String origin, String query,
			Set<CompartmentsAnnotationReportsHasCompartments> compartmentsAnnotationReportsHasCompartmentses,
			Set<ModelSequence> modelSequences, Set<ModelGeneHasOrthology> modelGeneHasOrthologies,
			Set<ModelSubunit> modelSubunits, Set<ModelGeneHasCompartment> modelGeneHasCompartments,
			Set<RegulatoryTranscriptionUnitGene> regulatoryTranscriptionUnitGenes) {
		this.idgene = idgene;
		this.name = name;
		this.locusTag = locusTag;
		this.transcriptionDirection = transcriptionDirection;
		this.leftEndPosition = leftEndPosition;
		this.rightEndPosition = rightEndPosition;
		this.booleanRule = booleanRule;
		this.origin = origin;
		this.query = query;
		this.compartmentsAnnotationReportsHasCompartmentses = compartmentsAnnotationReportsHasCompartmentses;
		this.modelSequences = modelSequences;
		this.modelGeneHasOrthologies = modelGeneHasOrthologies;
		this.modelSubunits = modelSubunits;
		this.modelGeneHasCompartments = modelGeneHasCompartments;
		this.regulatoryTranscriptionUnitGenes = regulatoryTranscriptionUnitGenes;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="UseExistingIdOtherwiseGenerateUsingIdentity")
	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "pt.uminho.ceb.biosystems.merlin.auxiliary.UseExistingIdOtherwiseGenerateUsingIdentity")	
	@Column(name = "idgene", unique = true, nullable = false)
	public int getIdgene() {
		return this.idgene;
	}

	public void setIdgene(int idgene) {
		this.idgene = idgene;
	}

	@Column(name = "name", length = 120)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "locusTag", length = 45)
	public String getLocusTag() {
		return this.locusTag;
	}

	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}

	@Column(name = "transcription_direction", length = 3)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public String getTranscriptionDirection() {
		return this.transcriptionDirection;
	}

	public void setTranscriptionDirection(String transcriptionDirection) {
		this.transcriptionDirection = transcriptionDirection;
	}

	@Column(name = "left_end_position", length = 45)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public String getLeftEndPosition() {
		return this.leftEndPosition;
	}

	public void setLeftEndPosition(String leftEndPosition) {
		this.leftEndPosition = leftEndPosition;
	}

	@Column(name = "right_end_position", length = 100)
	public String getRightEndPosition() {
		return this.rightEndPosition;
	}

	public void setRightEndPosition(String rightEndPosition) {
		this.rightEndPosition = rightEndPosition;
	}

	@Column(name = "boolean_rule", length = 200)
	public String getBooleanRule() {
		return this.booleanRule;
	}

	public void setBooleanRule(String booleanRule) {
		this.booleanRule = booleanRule;
	}

	@Column(name = "origin", nullable = false, length = 40)
	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Column(name = "query", nullable = false, length = 45)
	public String getQuery() {
		return this.query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelGene")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<CompartmentsAnnotationReportsHasCompartments> getCompartmentsAnnotationReportsHasCompartmentses() {
		return this.compartmentsAnnotationReportsHasCompartmentses;
	}

	public void setCompartmentsAnnotationReportsHasCompartmentses(
			Set<CompartmentsAnnotationReportsHasCompartments> compartmentsAnnotationReportsHasCompartmentses) {
		this.compartmentsAnnotationReportsHasCompartmentses = compartmentsAnnotationReportsHasCompartmentses;
	}

    @XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelGene")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelSequence> getModelSequences() {
		return this.modelSequences;
	}

	public void setModelSequences(Set<ModelSequence> modelSequences) {
		this.modelSequences = modelSequences;
	}

    @XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelGene")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelGeneHasOrthology> getModelGeneHasOrthologies() {
		return this.modelGeneHasOrthologies;
	}

	public void setModelGeneHasOrthologies(Set<ModelGeneHasOrthology> modelGeneHasOrthologies) {
		this.modelGeneHasOrthologies = modelGeneHasOrthologies;
	}
    @XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelGene")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelSubunit> getModelSubunits() {
		return this.modelSubunits;
	}

	public void setModelSubunits(Set<ModelSubunit> modelSubunits) {
		this.modelSubunits = modelSubunits;
	}
    @XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelGene")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelGeneHasCompartment> getModelGeneHasCompartments() {
		return this.modelGeneHasCompartments;
	}

	public void setModelGeneHasCompartments(Set<ModelGeneHasCompartment> modelGeneHasCompartments) {
		this.modelGeneHasCompartments = modelGeneHasCompartments;
	}
    @XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelGene")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<RegulatoryTranscriptionUnitGene> getRegulatoryTranscriptionUnitGenes() {
		return this.regulatoryTranscriptionUnitGenes;
	}

	public void setRegulatoryTranscriptionUnitGenes(
			Set<RegulatoryTranscriptionUnitGene> regulatoryTranscriptionUnitGenes) {
		this.regulatoryTranscriptionUnitGenes = regulatoryTranscriptionUnitGenes;
	}

}
