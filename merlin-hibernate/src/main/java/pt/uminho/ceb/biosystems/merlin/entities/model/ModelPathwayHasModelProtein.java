package pt.uminho.ceb.biosystems.merlin.entities.model;
// Generated Sep 11, 2019 1:12:29 PM by Hibernate Tools 5.2.12.Final

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
 * ModelPathwayHasModelProtein generated by hbm2java
 */
@Entity
@Table(name = "model_pathway_has_model_protein")
public class ModelPathwayHasModelProtein implements java.io.Serializable {

	private ModelPathwayHasModelProteinId id;
	private ModelPathway modelPathway;
	private ModelProtein modelProtein;

	public ModelPathwayHasModelProtein() {
	}

	public ModelPathwayHasModelProtein(ModelPathwayHasModelProteinId id, ModelPathway modelPathway,
			ModelProtein modelProtein) {
		this.id = id;
		this.modelPathway = modelPathway;
		this.modelProtein = modelProtein;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "modelPathwayIdpathway", column = @Column(name = "model_pathway_idpathway", nullable = false)),
			@AttributeOverride(name = "modelProteinIdprotein", column = @Column(name = "model_protein_idprotein", nullable = false)) })
	public ModelPathwayHasModelProteinId getId() {
		return this.id;
	}

	public void setId(ModelPathwayHasModelProteinId id) {
		this.id = id;
	}

	 @XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_pathway_idpathway", nullable = false, insertable = false, updatable = false)
	public ModelPathway getModelPathway() {
		return this.modelPathway;
	}

	public void setModelPathway(ModelPathway modelPathway) {
		this.modelPathway = modelPathway;
	}

	 @XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_protein_idprotein", nullable = false, insertable = false, updatable = false)
	public ModelProtein getModelProtein() {
		return this.modelProtein;
	}

	public void setModelProtein(ModelProtein modelProtein) {
		this.modelProtein = modelProtein;
	}

}