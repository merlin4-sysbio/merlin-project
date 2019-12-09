package pt.uminho.ceb.biosystems.merlin.entities.experimental;
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

import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;

/**
 * ExperimentalInhibitor generated by hbm2java
 */
@Entity
@Table(name = "experimental_inhibitor")
public class ExperimentalInhibitor implements java.io.Serializable {

	private ExperimentalInhibitorId id;
	private ModelCompound modelCompound;
	private ModelProtein modelProtein;
	private Float ki;

	public ExperimentalInhibitor() {
	}

	public ExperimentalInhibitor(ExperimentalInhibitorId id, ModelCompound modelCompound, ModelProtein modelProtein) {
		this.id = id;
		this.modelCompound = modelCompound;
		this.modelProtein = modelProtein;
	}

	public ExperimentalInhibitor(ExperimentalInhibitorId id, ModelCompound modelCompound, ModelProtein modelProtein,
			Float ki) {
		this.id = id;
		this.modelCompound = modelCompound;
		this.modelProtein = modelProtein;
		this.ki = ki;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "experimentDescription", column = @Column(name = "experiment_description", nullable = false)),
			@AttributeOverride(name = "modelCompoundIdcompound", column = @Column(name = "model_compound_idcompound", nullable = false)),
			@AttributeOverride(name = "modelProteinIdprotein", column = @Column(name = "model_protein_idprotein", nullable = false)) })
	public ExperimentalInhibitorId getId() {
		return this.id;
	}

	public void setId(ExperimentalInhibitorId id) {
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

	@Column(name = "ki", precision = 12, scale = 0)
	public Float getKi() {
		return this.ki;
	}

	public void setKi(Float ki) {
		this.ki = ki;
	}

}