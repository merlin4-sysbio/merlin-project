package pt.uminho.ceb.biosystems.merlin.entities.regulatory;
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
 * RegulatoryRiFunction generated by hbm2java
 */
@Entity
@Table(name = "regulatory_ri_function")
public class RegulatoryRiFunction implements java.io.Serializable {

	private int idriFunction;
	private String symbol;
	private String riFunction;
	private Set<RegulatoryEvent> regulatoryEvents = new HashSet<RegulatoryEvent>(0);

	public RegulatoryRiFunction() {
	}

	public RegulatoryRiFunction(int idriFunction) {
		this.idriFunction = idriFunction;
	}

	public RegulatoryRiFunction(int idriFunction, String symbol, String riFunction,
			Set<RegulatoryEvent> regulatoryEvents) {
		this.idriFunction = idriFunction;
		this.symbol = symbol;
		this.riFunction = riFunction;
		this.regulatoryEvents = regulatoryEvents;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="UseExistingIdOtherwiseGenerateUsingIdentity")
	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "pt.uminho.ceb.biosystems.merlin.auxiliary.UseExistingIdOtherwiseGenerateUsingIdentity")	
	@Column(name = "idri_function", unique = true, nullable = false)
	public int getIdriFunction() {
		return this.idriFunction;
	}

	public void setIdriFunction(int idriFunction) {
		this.idriFunction = idriFunction;
	}

	@Column(name = "symbol", length = 2)
	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Column(name = "ri_function", length = 20)
	public String getRiFunction() {
		return this.riFunction;
	}

	public void setRiFunction(String riFunction) {
		this.riFunction = riFunction;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "regulatoryRiFunction")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<RegulatoryEvent> getRegulatoryEvents() {
		return this.regulatoryEvents;
	}

	public void setRegulatoryEvents(Set<RegulatoryEvent> regulatoryEvents) {
		this.regulatoryEvents = regulatoryEvents;
	}

}
