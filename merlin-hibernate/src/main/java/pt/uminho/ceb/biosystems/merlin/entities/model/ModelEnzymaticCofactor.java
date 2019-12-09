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
 * ModelEnzymaticCofactor generated by hbm2java
 */
@Entity
@Table(name = "model_enzymatic_cofactor")
public class ModelEnzymaticCofactor implements java.io.Serializable {

	private ModelEnzymaticCofactorId id;
	private ModelCompound modelCompound;
	private ModelProtein modelProtein;
	private Boolean prosthetic;

	public ModelEnzymaticCofactor() {
	}

	public ModelEnzymaticCofactor(ModelEnzymaticCofactorId id, ModelCompound modelCompound, ModelProtein modelProtein) {
		this.id = id;
		this.modelCompound = modelCompound;
		this.modelProtein = modelProtein;
	}

	public ModelEnzymaticCofactor(ModelEnzymaticCofactorId id, ModelCompound modelCompound, ModelProtein modelProtein,
			Boolean prosthetic) {
		this.id = id;
		this.modelCompound = modelCompound;
		this.modelProtein = modelProtein;
		this.prosthetic = prosthetic;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "modelProteinIdprotein", column = @Column(name = "model_protein_idprotein", nullable = false)),
			@AttributeOverride(name = "modelCompoundIdcompound", column = @Column(name = "model_compound_idcompound", nullable = false)) })
	public ModelEnzymaticCofactorId getId() {
		return this.id;
	}

	public void setId(ModelEnzymaticCofactorId id) {
		this.id = id;
	}
	
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_compound_idcompound", nullable = false, insertable = false, updatable = false)
	public ModelCompound getModelCompound() {
		return this.modelCompound;
	}

	public void setModelCompound(ModelCompound modelCompound) {
		this.modelCompound = modelCompound;
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

	@Column(name = "prosthetic")
	public Boolean getProsthetic() {
		return this.prosthetic;
	}

	public void setProsthetic(Boolean prosthetic) {
		this.prosthetic = prosthetic;
	}

}
