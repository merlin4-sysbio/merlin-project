package pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * EnzymesAnnotationScorerConfigId generated by hbm2java
 */
@Embeddable
public class EnzymesAnnotationScorerConfigId implements java.io.Serializable {

	private float threshold;
	private float upperThreshold;
	private float balanceBh;
	private float alpha;
	private float beta;
	private int minHomologies;
	private String databaseName;
	private boolean latest;
	private boolean bestAlpha;

	public EnzymesAnnotationScorerConfigId() {
	}

	public EnzymesAnnotationScorerConfigId(float threshold, float upperThreshold, float balanceBh, float alpha,
			float beta, int minHomologies, String databaseName, boolean latest, boolean bestAlpha) {
		this.threshold = threshold;
		this.upperThreshold = upperThreshold;
		this.balanceBh = balanceBh;
		this.alpha = alpha;
		this.beta = beta;
		this.minHomologies = minHomologies;
		this.databaseName = databaseName;
		this.latest = latest;
		this.bestAlpha = bestAlpha;
	}

	@Column(name = "threshold", nullable = false, precision = 12, scale = 0)
	public float getThreshold() {
		return this.threshold;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

	@Column(name = "upperThreshold", nullable = false, precision = 12, scale = 0)
	public float getUpperThreshold() {
		return this.upperThreshold;
	}

	public void setUpperThreshold(float upperThreshold) {
		this.upperThreshold = upperThreshold;
	}

	@Column(name = "balanceBH", nullable = false, precision = 12, scale = 0)
	public float getBalanceBh() {
		return this.balanceBh;
	}

	public void setBalanceBh(float balanceBh) {
		this.balanceBh = balanceBh;
	}

	@Column(name = "alpha", nullable = false, precision = 12, scale = 0)
	public float getAlpha() {
		return this.alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	@Column(name = "beta", nullable = false, precision = 12, scale = 0)
	public float getBeta() {
		return this.beta;
	}

	public void setBeta(float beta) {
		this.beta = beta;
	}

	@Column(name = "minHomologies", nullable = false)
	public int getMinHomologies() {
		return this.minHomologies;
	}

	public void setMinHomologies(int minHomologies) {
		this.minHomologies = minHomologies;
	}

	@Column(name = "databaseName", nullable = false, length = 45)
	public String getDatabaseName() {
		return this.databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Column(name = "latest", nullable = false)
	public boolean isLatest() {
		return this.latest;
	}

	public void setLatest(boolean latest) {
		this.latest = latest;
	}

	@Column(name = "bestAlpha", nullable = false)
	public boolean isBestAlpha() {
		return this.bestAlpha;
	}

	public void setBestAlpha(boolean bestAlpha) {
		this.bestAlpha = bestAlpha;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EnzymesAnnotationScorerConfigId))
			return false;
		EnzymesAnnotationScorerConfigId castOther = (EnzymesAnnotationScorerConfigId) other;

		return (this.getThreshold() == castOther.getThreshold())
				&& (this.getUpperThreshold() == castOther.getUpperThreshold())
				&& (this.getBalanceBh() == castOther.getBalanceBh()) && (this.getAlpha() == castOther.getAlpha())
				&& (this.getBeta() == castOther.getBeta()) && (this.getMinHomologies() == castOther.getMinHomologies())
				&& ((this.getDatabaseName() == castOther.getDatabaseName())
						|| (this.getDatabaseName() != null && castOther.getDatabaseName() != null
								&& this.getDatabaseName().equals(castOther.getDatabaseName())))
				&& (this.isLatest() == castOther.isLatest()) && (this.isBestAlpha() == castOther.isBestAlpha());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getThreshold();
		result = 37 * result + (int) this.getUpperThreshold();
		result = 37 * result + (int) this.getBalanceBh();
		result = 37 * result + (int) this.getAlpha();
		result = 37 * result + (int) this.getBeta();
		result = 37 * result + this.getMinHomologies();
		result = 37 * result + (getDatabaseName() == null ? 0 : this.getDatabaseName().hashCode());
		result = 37 * result + (this.isLatest() ? 1 : 0);
		result = 37 * result + (this.isBestAlpha() ? 1 : 0);
		return result;
	}

}
