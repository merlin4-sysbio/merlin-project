package pt.uminho.ceb.biosystems.merlin.entities.model;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * ModelOrthology generated by hbm2java
 */
@Entity
@Table(name = "model_orthology")
public class ModelOrthology implements java.io.Serializable {

	private int id;
	private String entryId;
	private String locusId;
	private Set<ModelGeneHasOrthology> modelGeneHasOrthologies = new HashSet<ModelGeneHasOrthology>(0);
	private Set<ModelModuleHasOrthology> modelModuleHasOrthologies = new HashSet<ModelModuleHasOrthology>(0);

	public ModelOrthology() {
	}

	public ModelOrthology(int id, String entryId) {
		this.id = id;
		this.entryId = entryId;
	}

	public ModelOrthology(int id, String entryId, String locusId, Set<ModelGeneHasOrthology> modelGeneHasOrthologies,
			Set<ModelModuleHasOrthology> modelModuleHasOrthologies) {
		this.id = id;
		this.entryId = entryId;
		this.locusId = locusId;
		this.modelGeneHasOrthologies = modelGeneHasOrthologies;
		this.modelModuleHasOrthologies = modelModuleHasOrthologies;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="UseExistingIdOtherwiseGenerateUsingIdentity")
	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "pt.uminho.ceb.biosystems.merlin.auxiliary.UseExistingIdOtherwiseGenerateUsingIdentity")	
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "entry_id", nullable = false, length = 20)
	public String getEntryId() {
		return this.entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	@Column(name = "locus_id", length = 45)
	public String getLocusId() {
		return this.locusId;
	}

	public void setLocusId(String locusId) {
		this.locusId = locusId;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelOrthology")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelGeneHasOrthology> getModelGeneHasOrthologies() {
		return this.modelGeneHasOrthologies;
	}

	public void setModelGeneHasOrthologies(Set<ModelGeneHasOrthology> modelGeneHasOrthologies) {
		this.modelGeneHasOrthologies = modelGeneHasOrthologies;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelOrthology")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelModuleHasOrthology> getModelModuleHasOrthologies() {
		return this.modelModuleHasOrthologies;
	}

	public void setModelModuleHasOrthologies(Set<ModelModuleHasOrthology> modelModuleHasOrthologies) {
		this.modelModuleHasOrthologies = modelModuleHasOrthologies;
	}

}