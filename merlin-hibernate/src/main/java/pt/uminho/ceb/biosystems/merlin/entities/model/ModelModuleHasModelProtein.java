package pt.uminho.ceb.biosystems.merlin.entities.model;
// Generated Oct 29, 2019 3:55:35 PM by Hibernate Tools 5.2.12.Final

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
 * ModelModuleHasModelProtein generated by hbm2java
 */
@Entity
@Table(name = "model_module_has_model_protein")
public class ModelModuleHasModelProtein implements java.io.Serializable {

	private ModelModuleHasModelProteinId id;
	private ModelModule modelModule;
	private ModelProtein modelProtein;

	public ModelModuleHasModelProtein() {
	}

	public ModelModuleHasModelProtein(ModelModuleHasModelProteinId id, ModelModule modelModule,
			ModelProtein modelProtein) {
		this.id = id;
		this.modelModule = modelModule;
		this.modelProtein = modelProtein;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "modelModuleId", column = @Column(name = "model_module_id", nullable = false)),
			@AttributeOverride(name = "modelProteinIdprotein", column = @Column(name = "model_protein_idprotein", nullable = false)) })
	public ModelModuleHasModelProteinId getId() {
		return this.id;
	}

	public void setId(ModelModuleHasModelProteinId id) {
		this.id = id;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_module_id", nullable = false, insertable = false, updatable = false)
	public ModelModule getModelModule() {
		return this.modelModule;
	}

	public void setModelModule(ModelModule modelModule) {
		this.modelModule = modelModule;
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