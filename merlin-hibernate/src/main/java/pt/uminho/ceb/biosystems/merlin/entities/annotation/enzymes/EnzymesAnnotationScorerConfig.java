package pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * EnzymesAnnotationScorerConfig generated by hbm2java
 */
@Entity
@Table(name = "enzymes_annotation_scorerConfig")
public class EnzymesAnnotationScorerConfig implements java.io.Serializable {

	private EnzymesAnnotationScorerConfigId id;

	public EnzymesAnnotationScorerConfig() {
	}

	public EnzymesAnnotationScorerConfig(EnzymesAnnotationScorerConfigId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "threshold", column = @Column(name = "threshold", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "upperThreshold", column = @Column(name = "upperThreshold", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "balanceBh", column = @Column(name = "balanceBH", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "alpha", column = @Column(name = "alpha", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "beta", column = @Column(name = "beta", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "minHomologies", column = @Column(name = "minHomologies", nullable = false)),
			@AttributeOverride(name = "databaseName", column = @Column(name = "databaseName", nullable = false, length = 45)),
			@AttributeOverride(name = "latest", column = @Column(name = "latest", nullable = false)),
			@AttributeOverride(name = "bestAlpha", column = @Column(name = "bestAlpha", nullable = false)) })
	public EnzymesAnnotationScorerConfigId getId() {
		return this.id;
	}

	public void setId(EnzymesAnnotationScorerConfigId id) {
		this.id = id;
	}

}
