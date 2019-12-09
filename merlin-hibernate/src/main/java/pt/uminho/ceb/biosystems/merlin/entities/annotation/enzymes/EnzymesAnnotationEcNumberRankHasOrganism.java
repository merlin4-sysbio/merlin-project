package pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes;
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
 * EnzymesAnnotationEcNumberRankHasOrganism generated by hbm2java
 */
@Entity
@Table(name = "enzymes_annotation_ecNumberRank_has_organism")
public class EnzymesAnnotationEcNumberRankHasOrganism implements java.io.Serializable {

	private EnzymesAnnotationEcNumberRankHasOrganismId id;
	private EnzymesAnnotationEcNumberRank enzymesAnnotationEcNumberRank;
	private EnzymesAnnotationOrganism enzymesAnnotationOrganism;

	public EnzymesAnnotationEcNumberRankHasOrganism() {
	}

	public EnzymesAnnotationEcNumberRankHasOrganism(EnzymesAnnotationEcNumberRankHasOrganismId id,
			EnzymesAnnotationEcNumberRank enzymesAnnotationEcNumberRank,
			EnzymesAnnotationOrganism enzymesAnnotationOrganism) {
		this.id = id;
		this.enzymesAnnotationEcNumberRank = enzymesAnnotationEcNumberRank;
		this.enzymesAnnotationOrganism = enzymesAnnotationOrganism;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "organismSKey", column = @Column(name = "organism_s_key", nullable = false)),
			@AttributeOverride(name = "ecNumberRankSKey", column = @Column(name = "ecNumberRank_s_key", nullable = false)) })
	public EnzymesAnnotationEcNumberRankHasOrganismId getId() {
		return this.id;
	}

	public void setId(EnzymesAnnotationEcNumberRankHasOrganismId id) {
		this.id = id;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ecNumberRank_s_key", nullable = false, insertable = false, updatable = false)
	public EnzymesAnnotationEcNumberRank getEnzymesAnnotationEcNumberRank() {
		return this.enzymesAnnotationEcNumberRank;
	}

	public void setEnzymesAnnotationEcNumberRank(EnzymesAnnotationEcNumberRank enzymesAnnotationEcNumberRank) {
		this.enzymesAnnotationEcNumberRank = enzymesAnnotationEcNumberRank;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organism_s_key", nullable = false, insertable = false, updatable = false)
	public EnzymesAnnotationOrganism getEnzymesAnnotationOrganism() {
		return this.enzymesAnnotationOrganism;
	}

	public void setEnzymesAnnotationOrganism(EnzymesAnnotationOrganism enzymesAnnotationOrganism) {
		this.enzymesAnnotationOrganism = enzymesAnnotationOrganism;
	}

}