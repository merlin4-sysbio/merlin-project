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
 * EnzymesAnnotationEcNumberRank generated by hbm2java
 */
@Entity
@Table(name = "enzymes_annotation_ecNumberRank")
public class EnzymesAnnotationEcNumberRank implements java.io.Serializable {

	private int SKey;
	private EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology;
	private String ecNumber;
	private Integer rank;
	private Set<EnzymesAnnotationEcNumberRankHasOrganism> enzymesAnnotationEcNumberRankHasOrganisms = new HashSet<EnzymesAnnotationEcNumberRankHasOrganism>(
			0);

	public EnzymesAnnotationEcNumberRank() {
	}

	public EnzymesAnnotationEcNumberRank(int SKey, EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology) {
		this.SKey = SKey;
		this.enzymesAnnotationGeneHomology = enzymesAnnotationGeneHomology;
	}

	public EnzymesAnnotationEcNumberRank(int SKey, EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology,
			String ecNumber, Integer rank,
			Set<EnzymesAnnotationEcNumberRankHasOrganism> enzymesAnnotationEcNumberRankHasOrganisms) {
		this.SKey = SKey;
		this.enzymesAnnotationGeneHomology = enzymesAnnotationGeneHomology;
		this.ecNumber = ecNumber;
		this.rank = rank;
		this.enzymesAnnotationEcNumberRankHasOrganisms = enzymesAnnotationEcNumberRankHasOrganisms;
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
	@JoinColumn(name = "enzymes_annotation_geneHomology_s_key", nullable = false)
	public EnzymesAnnotationGeneHomology getEnzymesAnnotationGeneHomology() {
		return this.enzymesAnnotationGeneHomology;
	}

	public void setEnzymesAnnotationGeneHomology(EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology) {
		this.enzymesAnnotationGeneHomology = enzymesAnnotationGeneHomology;
	}

	@Column(name = "ecNumber", length = 16777215)
	public String getEcNumber() {
		return this.ecNumber;
	}

	public void setEcNumber(String ecNumber) {
		this.ecNumber = ecNumber;
	}

	@Column(name = "rank")
	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "enzymesAnnotationEcNumberRank")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<EnzymesAnnotationEcNumberRankHasOrganism> getEnzymesAnnotationEcNumberRankHasOrganisms() {
		return this.enzymesAnnotationEcNumberRankHasOrganisms;
	}

	public void setEnzymesAnnotationEcNumberRankHasOrganisms(
			Set<EnzymesAnnotationEcNumberRankHasOrganism> enzymesAnnotationEcNumberRankHasOrganisms) {
		this.enzymesAnnotationEcNumberRankHasOrganisms = enzymesAnnotationEcNumberRankHasOrganisms;
	}

}
