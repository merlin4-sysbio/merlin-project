package pt.uminho.ceb.biosystems.merlin.entities.model;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * ModelStrain generated by hbm2java
 */
@Entity
@Table(name = "model_strain")
public class ModelStrain implements java.io.Serializable {

	private int idstrain;
	private String name;
	private ModelEntityisfrom modelEntityisfrom;

	public ModelStrain() {
	}

	public ModelStrain(int idstrain) {
		this.idstrain = idstrain;
	}

	public ModelStrain(int idstrain, String name, ModelEntityisfrom modelEntityisfrom) {
		this.idstrain = idstrain;
		this.name = name;
		this.modelEntityisfrom = modelEntityisfrom;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="UseExistingIdOtherwiseGenerateUsingIdentity")
	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "pt.uminho.ceb.biosystems.merlin.auxiliary.UseExistingIdOtherwiseGenerateUsingIdentity")	
	@Column(name = "idstrain", unique = true, nullable = false)
	public int getIdstrain() {
		return this.idstrain;
	}

	public void setIdstrain(int idstrain) {
		this.idstrain = idstrain;
	}

	@Column(name = "name", length = 60)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlTransient
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "modelStrain")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public ModelEntityisfrom getModelEntityisfrom() {
		return this.modelEntityisfrom;
	}

	public void setModelEntityisfrom(ModelEntityisfrom modelEntityisfrom) {
		this.modelEntityisfrom = modelEntityisfrom;
	}

}
