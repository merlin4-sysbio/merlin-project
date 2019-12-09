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
 * ModelProteinComposition generated by hbm2java
 */
@Entity
@Table(name = "model_protein_composition")
public class ModelProteinComposition implements java.io.Serializable {

	private ModelProteinCompositionId id;
	private ModelProtein modelProtein;

	public ModelProteinComposition() {
	}

	public ModelProteinComposition(ModelProteinCompositionId id, ModelProtein modelProtein) {
		this.id = id;
		this.modelProtein = modelProtein;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "subunit", column = @Column(name = "subunit", nullable = false)),
			@AttributeOverride(name = "modelProteinIdprotein", column = @Column(name = "model_protein_idprotein", nullable = false)) })
	public ModelProteinCompositionId getId() {
		return this.id;
	}

	public void setId(ModelProteinCompositionId id) {
		this.id = id;
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