package pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * EnzymesAnnotationProductList generated by hbm2java
 */
@Entity
@Table(name = "enzymes_annotation_productList")
public class EnzymesAnnotationProductList implements java.io.Serializable {

	private int SKey;
	private EnzymesAnnotationHomologyData enzymesAnnotationHomologyData;
	private String otherNames;

	public EnzymesAnnotationProductList() {
	}

	public EnzymesAnnotationProductList(int SKey, EnzymesAnnotationHomologyData enzymesAnnotationHomologyData) {
		this.SKey = SKey;
		this.enzymesAnnotationHomologyData = enzymesAnnotationHomologyData;
	}

	public EnzymesAnnotationProductList(int SKey, EnzymesAnnotationHomologyData enzymesAnnotationHomologyData,
			String otherNames) {
		this.SKey = SKey;
		this.enzymesAnnotationHomologyData = enzymesAnnotationHomologyData;
		this.otherNames = otherNames;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="UseExistingIdOtherwiseGenerateUsingIdentity")
	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "pt.uminho.ceb.biosystems.merlin.auxiliary.UseExistingIdOtherwiseGenerateUsingIdentity")	
	@Column(name = "s_key", unique = true, nullable = false)
	public int getSKey() {
		return this.SKey;
	}

	public void setSKey(int SKey) {
		this.SKey = SKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "enzymes_annotation_homologyData_s_key", nullable = false)
	public EnzymesAnnotationHomologyData getEnzymesAnnotationHomologyData() {
		return this.enzymesAnnotationHomologyData;
	}

	public void setEnzymesAnnotationHomologyData(EnzymesAnnotationHomologyData enzymesAnnotationHomologyData) {
		this.enzymesAnnotationHomologyData = enzymesAnnotationHomologyData;
	}

	@Column(name = "otherNames", length = 65535, columnDefinition="Text")
	public String getOtherNames() {
		return this.otherNames;
	}

	public void setOtherNames(String otherNames) {
		this.otherNames = otherNames;
	}

}
