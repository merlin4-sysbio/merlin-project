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
 * EnzymesAnnotationHomologyData generated by hbm2java
 */
@Entity
@Table(name = "enzymes_annotation_homologyData")
public class EnzymesAnnotationHomologyData implements java.io.Serializable {

	private int SKey;
	private EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology;
	private String locusTag;
	private String geneName;
	private String product;
	private String ecNumber;
	private Boolean selected;
	private String chromosome;
	private String notes;
	private Set<EnzymesAnnotationEcNumberList> enzymesAnnotationEcNumberLists = new HashSet<EnzymesAnnotationEcNumberList>(
			0);
	private Set<EnzymesAnnotationProductList> enzymesAnnotationProductLists = new HashSet<EnzymesAnnotationProductList>(
			0);

	public EnzymesAnnotationHomologyData() {
	}

	public EnzymesAnnotationHomologyData(int SKey, EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology, String locusTag, String product) {
		this.SKey = SKey;
		this.enzymesAnnotationGeneHomology = enzymesAnnotationGeneHomology;
		this.locusTag = locusTag;
		this.product = product;
	}

	public EnzymesAnnotationHomologyData(int SKey, EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology, String locusTag, String geneName,
			String product, String ecNumber, Boolean selected, String chromosome, String notes,
			Set<EnzymesAnnotationEcNumberList> enzymesAnnotationEcNumberLists,
			Set<EnzymesAnnotationProductList> enzymesAnnotationProductLists) {
		this.SKey = SKey;
		this.enzymesAnnotationGeneHomology = enzymesAnnotationGeneHomology;
		this.locusTag = locusTag;
		this.geneName = geneName;
		this.product = product;
		this.ecNumber = ecNumber;
		this.selected = selected;
		this.chromosome = chromosome;
		this.notes = notes;
		this.enzymesAnnotationEcNumberLists = enzymesAnnotationEcNumberLists;
		this.enzymesAnnotationProductLists = enzymesAnnotationProductLists;
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
	@JoinColumn(name = "geneHomology_s_key", nullable = false)
	public EnzymesAnnotationGeneHomology getEnzymesAnnotationGeneHomology() {
		return this.enzymesAnnotationGeneHomology;
	}

	public void setEnzymesAnnotationGeneHomology(EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology) {
		this.enzymesAnnotationGeneHomology = enzymesAnnotationGeneHomology;
	}

	@Column(name = "locusTag", nullable = false, length = 100)
	public String getLocusTag() {
		return this.locusTag;
	}

	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}

	@Column(name = "geneName", length = 100)
	public String getGeneName() {
		return this.geneName;
	}

	public void setGeneName(String geneName) {
		this.geneName = geneName;
	}

	@Column(name = "product", length = 65535, columnDefinition="Text")
	public String getProduct() {
		return this.product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	@Column(name = "ecNumber", length = 16777215)
	public String getEcNumber() {
		return this.ecNumber;
	}

	public void setEcNumber(String ecNumber) {
		this.ecNumber = ecNumber;
	}

	@Column(name = "selected")
	public Boolean getSelected() {
		return this.selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@Column(name = "chromosome", length = 20)
	public String getChromosome() {
		return this.chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	@Column(name = "notes", length = 65535, columnDefinition="Text")
	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "enzymesAnnotationHomologyData")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<EnzymesAnnotationEcNumberList> getEnzymesAnnotationEcNumberLists() {
		return this.enzymesAnnotationEcNumberLists;
	}

	public void setEnzymesAnnotationEcNumberLists(Set<EnzymesAnnotationEcNumberList> enzymesAnnotationEcNumberLists) {
		this.enzymesAnnotationEcNumberLists = enzymesAnnotationEcNumberLists;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "enzymesAnnotationHomologyData")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<EnzymesAnnotationProductList> getEnzymesAnnotationProductLists() {
		return this.enzymesAnnotationProductLists;
	}

	public void setEnzymesAnnotationProductLists(Set<EnzymesAnnotationProductList> enzymesAnnotationProductLists) {
		this.enzymesAnnotationProductLists = enzymesAnnotationProductLists;
	}

}
