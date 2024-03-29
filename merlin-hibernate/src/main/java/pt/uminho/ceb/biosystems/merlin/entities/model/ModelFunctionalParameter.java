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
 * ModelFunctionalParameter generated by hbm2java
 */
@Entity
@Table(name = "model_functional_parameter")
public class ModelFunctionalParameter implements java.io.Serializable {

	private ModelFunctionalParameterId id;
	private ModelCompound modelCompound;
	private ModelProtein modelProtein;
	private Float parameterValue;

	public ModelFunctionalParameter() {
	}

	public ModelFunctionalParameter(ModelFunctionalParameterId id, ModelCompound modelCompound,
			ModelProtein modelProtein) {
		this.id = id;
		this.modelCompound = modelCompound;
		this.modelProtein = modelProtein;
	}

	public ModelFunctionalParameter(ModelFunctionalParameterId id, ModelCompound modelCompound,
			ModelProtein modelProtein, Float parameterValue) {
		this.id = id;
		this.modelCompound = modelCompound;
		this.modelProtein = modelProtein;
		this.parameterValue = parameterValue;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "parameterType", column = @Column(name = "parameter_type", nullable = false, length = 50)),
			@AttributeOverride(name = "modelProteinIdprotein", column = @Column(name = "model_protein_idprotein", nullable = false)),
			@AttributeOverride(name = "modelCompoundIdcompound", column = @Column(name = "model_compound_idcompound", nullable = false)) })
	public ModelFunctionalParameterId getId() {
		return this.id;
	}

	public void setId(ModelFunctionalParameterId id) {
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

	@Column(name = "parameter_value", precision = 12, scale = 0)
	public Float getParameterValue() {
		return this.parameterValue;
	}

	public void setParameterValue(Float parameterValue) {
		this.parameterValue = parameterValue;
	}

}
