package pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes;
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

/**
 * EnzymesAnnotationHomologues generated by hbm2java
 */
@Entity
@Table(name = "enzymes_annotation_homologues")
public class EnzymesAnnotationHomologues implements java.io.Serializable {

	private int SKey;
	private EnzymesAnnotationOrganism enzymesAnnotationOrganism;
	private String locusId;
	private String definition;
	private Float calculatedMw;
	private String product;
	private String organelle;
	private Integer uniprotStar;
	private Set<EnzymesAnnotationHomologuesHasEcNumber> enzymesAnnotationHomologuesHasEcNumbers = new HashSet<EnzymesAnnotationHomologuesHasEcNumber>(
			0);
	private Set<EnzymesAnnotationGeneHomologyHasHomologues> enzymesAnnotationGeneHomologyHasHomologueses = new HashSet<EnzymesAnnotationGeneHomologyHasHomologues>(
			0);

	public EnzymesAnnotationHomologues() {
	}

	public EnzymesAnnotationHomologues(int SKey, EnzymesAnnotationOrganism enzymesAnnotationOrganism) {
		this.SKey = SKey;
		this.enzymesAnnotationOrganism = enzymesAnnotationOrganism;
	}

	public EnzymesAnnotationHomologues(int SKey, EnzymesAnnotationOrganism enzymesAnnotationOrganism, String locusId,
			String definition, Float calculatedMw, String product, String organelle, Integer uniprotStar,
			Set<EnzymesAnnotationHomologuesHasEcNumber> enzymesAnnotationHomologuesHasEcNumbers,
			Set<EnzymesAnnotationGeneHomologyHasHomologues> enzymesAnnotationGeneHomologyHasHomologueses) {
		this.SKey = SKey;
		this.enzymesAnnotationOrganism = enzymesAnnotationOrganism;
		this.locusId = locusId;
		this.definition = definition;
		this.calculatedMw = calculatedMw;
		this.product = product;
		this.organelle = organelle;
		this.uniprotStar = uniprotStar;
		this.enzymesAnnotationHomologuesHasEcNumbers = enzymesAnnotationHomologuesHasEcNumbers;
		this.enzymesAnnotationGeneHomologyHasHomologueses = enzymesAnnotationGeneHomologyHasHomologueses;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="UseExistingIdOtherwiseGenerateUsingIdentity")
	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "pt.uminho.ceb.biosystems.merlin.auxiliary.UseExistingIdOtherwiseGenerateUsingIdentity")	
	@Column(name = "s_key", unique = true, nullable = false)
	public int getSKey() {
		return this.SKey;
	}

	public void setSKey(int SKey) {
		this.SKey = SKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "enzymes_annotation_organism_s_key", nullable = false)
	public EnzymesAnnotationOrganism getEnzymesAnnotationOrganism() {
		return this.enzymesAnnotationOrganism;
	}

	public void setEnzymesAnnotationOrganism(EnzymesAnnotationOrganism enzymesAnnotationOrganism) {
		this.enzymesAnnotationOrganism = enzymesAnnotationOrganism;
	}

	@Column(name = "locusID", length = 100)
	public String getLocusId() {
		return this.locusId;
	}

	public void setLocusId(String locusId) {
		this.locusId = locusId;
	}

	@Column(name = "definition", length = 65535, columnDefinition="Text")
	public String getDefinition() {
		return this.definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	@Column(name = "calculated_mw", precision = 12, scale = 0)
	public Float getCalculatedMw() {
		return this.calculatedMw;
	}

	public void setCalculatedMw(Float calculatedMw) {
		this.calculatedMw = calculatedMw;
	}

	@Column(name = "product", length = 65535, columnDefinition="Text")
	public String getProduct() {
		return this.product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	@Column(name = "organelle", length = 100)
	public String getOrganelle() {
		return this.organelle;
	}

	public void setOrganelle(String organelle) {
		this.organelle = organelle;
	}

	@Column(name = "uniprot_star")
	public Integer getUniprotStar() {
		return this.uniprotStar;
	}

	public void setUniprotStar(Integer uniprotStar) {
		this.uniprotStar = uniprotStar;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "enzymesAnnotationHomologues")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<EnzymesAnnotationHomologuesHasEcNumber> getEnzymesAnnotationHomologuesHasEcNumbers() {
		return this.enzymesAnnotationHomologuesHasEcNumbers;
	}

	public void setEnzymesAnnotationHomologuesHasEcNumbers(
			Set<EnzymesAnnotationHomologuesHasEcNumber> enzymesAnnotationHomologuesHasEcNumbers) {
		this.enzymesAnnotationHomologuesHasEcNumbers = enzymesAnnotationHomologuesHasEcNumbers;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "enzymesAnnotationHomologues")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<EnzymesAnnotationGeneHomologyHasHomologues> getEnzymesAnnotationGeneHomologyHasHomologueses() {
		return this.enzymesAnnotationGeneHomologyHasHomologueses;
	}

	public void setEnzymesAnnotationGeneHomologyHasHomologueses(
			Set<EnzymesAnnotationGeneHomologyHasHomologues> enzymesAnnotationGeneHomologyHasHomologueses) {
		this.enzymesAnnotationGeneHomologyHasHomologueses = enzymesAnnotationGeneHomologyHasHomologueses;
	}

}