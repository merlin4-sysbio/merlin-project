package pt.uminho.ceb.biosystems.merlin.entities.interpro;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * InterproXRef generated by hbm2java
 */
@Entity
@Table(name = "interpro_xRef")
public class InterproXRef implements java.io.Serializable {

	private int id;
	private InterproEntry interproEntry;
	private String category;
	private String databaseName;
	private String name;
	private String externalId;

	public InterproXRef() {
	}

	public InterproXRef(int id, InterproEntry interproEntry, String databaseName, String name) {
		this.id = id;
		this.interproEntry = interproEntry;
		this.databaseName = databaseName;
		this.name = name;
	}

	public InterproXRef(int id, InterproEntry interproEntry, String category, String databaseName, String name,
			String externalId) {
		this.id = id;
		this.interproEntry = interproEntry;
		this.category = category;
		this.databaseName = databaseName;
		this.name = name;
		this.externalId = externalId;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interpro_entry_id", nullable = false)
	public InterproEntry getInterproEntry() {
		return this.interproEntry;
	}

	public void setInterproEntry(InterproEntry interproEntry) {
		this.interproEntry = interproEntry;
	}

	@Column(name = "category", length = 45)
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "databaseName", nullable = false, length = 45)
	public String getDatabaseName() {
		return this.databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Column(name = "name", nullable = false, length = 250)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "external_id", length = 45)
	public String getExternalId() {
		return this.externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

}
