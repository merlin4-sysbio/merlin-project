package pt.uminho.ceb.biosystems.merlin.entities.experimental;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * ExperimentalFactor generated by hbm2java
 */
@Entity
@Table(name = "experimental_factor")
public class ExperimentalFactor implements java.io.Serializable {

	private int idexperimentalFactor;
	private String factor;
	private Set<ExperimentalDescription> experimentalDescriptions = new HashSet<ExperimentalDescription>(0);

	public ExperimentalFactor() {
	}

	public ExperimentalFactor(int idexperimentalFactor) {
		this.idexperimentalFactor = idexperimentalFactor;
	}

	public ExperimentalFactor(int idexperimentalFactor, String factor,
			Set<ExperimentalDescription> experimentalDescriptions) {
		this.idexperimentalFactor = idexperimentalFactor;
		this.factor = factor;
		this.experimentalDescriptions = experimentalDescriptions;
	}

	@Id

	@Column(name = "idexperimental_factor", unique = true, nullable = false)
	public int getIdexperimentalFactor() {
		return this.idexperimentalFactor;
	}

	public void setIdexperimentalFactor(int idexperimentalFactor) {
		this.idexperimentalFactor = idexperimentalFactor;
	}

	@Column(name = "factor")
	public String getFactor() {
		return this.factor;
	}

	public void setFactor(String factor) {
		this.factor = factor;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "experimentalFactor")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ExperimentalDescription> getExperimentalDescriptions() {
		return this.experimentalDescriptions;
	}

	public void setExperimentalDescriptions(Set<ExperimentalDescription> experimentalDescriptions) {
		this.experimentalDescriptions = experimentalDescriptions;
	}

}
