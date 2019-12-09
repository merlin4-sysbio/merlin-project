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
 * ModelReactionHasModelProtein generated by hbm2java
 */
@Entity
@Table(name = "model_reaction_has_model_protein")
public class ModelReactionHasModelProtein implements java.io.Serializable {

	private ModelReactionHasModelProteinId id;
	private ModelProtein modelProtein;
	private ModelReaction modelReaction;

	public ModelReactionHasModelProtein() {
	}

	public ModelReactionHasModelProtein(ModelReactionHasModelProteinId id, ModelProtein modelProtein,
			ModelReaction modelReaction) {
		this.id = id;
		this.modelProtein = modelProtein;
		this.modelReaction = modelReaction;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "modelReactionIdreaction", column = @Column(name = "model_reaction_idreaction", nullable = false)),
			@AttributeOverride(name = "modelProteinIdprotein", column = @Column(name = "model_protein_idprotein", nullable = false)) })
	public ModelReactionHasModelProteinId getId() {
		return this.id;
	}

	public void setId(ModelReactionHasModelProteinId id) {
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

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model_reaction_idreaction", nullable = false, insertable = false, updatable = false)
	public ModelReaction getModelReaction() {
		return this.modelReaction;
	}

	public void setModelReaction(ModelReaction modelReaction) {
		this.modelReaction = modelReaction;
	}

}