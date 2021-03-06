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
 * ModelSuperpathway generated by hbm2java
 */
@Entity
@Table(name = "model_superpathway")
public class ModelSuperpathway implements java.io.Serializable {

	private ModelSuperpathwayId id;
	private ModelPathway modelPathwayByPathwayIdpathway;
	private ModelPathway modelPathwayBySuperpathway;

	public ModelSuperpathway() {
	}

	public ModelSuperpathway(ModelSuperpathwayId id, ModelPathway modelPathwayByPathwayIdpathway,
			ModelPathway modelPathwayBySuperpathway) {
		this.id = id;
		this.modelPathwayByPathwayIdpathway = modelPathwayByPathwayIdpathway;
		this.modelPathwayBySuperpathway = modelPathwayBySuperpathway;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "pathwayIdpathway", column = @Column(name = "pathway_idpathway", nullable = false)),
			@AttributeOverride(name = "superpathway", column = @Column(name = "superpathway", nullable = false)) })
	public ModelSuperpathwayId getId() {
		return this.id;
	}

	public void setId(ModelSuperpathwayId id) {
		this.id = id;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pathway_idpathway", nullable = false, insertable = false, updatable = false)
	public ModelPathway getModelPathwayByPathwayIdpathway() {
		return this.modelPathwayByPathwayIdpathway;
	}

	public void setModelPathwayByPathwayIdpathway(ModelPathway modelPathwayByPathwayIdpathway) {
		this.modelPathwayByPathwayIdpathway = modelPathwayByPathwayIdpathway;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "superpathway", nullable = false, insertable = false, updatable = false)
	public ModelPathway getModelPathwayBySuperpathway() {
		return this.modelPathwayBySuperpathway;
	}

	public void setModelPathwayBySuperpathway(ModelPathway modelPathwayBySuperpathway) {
		this.modelPathwayBySuperpathway = modelPathwayBySuperpathway;
	}

}
