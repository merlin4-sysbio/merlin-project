package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi;

import java.util.ArrayList;
import java.util.List;

public class Entry {
	
	private String accession;
	private String description;
	private String name;
	private String type;
	private List<Xref> xrefs;
	/**
	 * 
	 */
	public Entry() {
		this.xrefs = new ArrayList<>();
	}
	/**
	 * @return the accession
	 */
	public String getAccession() {
		return accession;
	}
	/**
	 * @param accession the accession to set
	 */
	public void setAccession(String accession) {
		this.accession = accession;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the xrefs
	 */
	public List<Xref> getXrefs() {
		return xrefs;
	}
	/**
	 * @param xref to add
	 */
	public void addXref(Xref xref) {
		this.xrefs.add(xref);
	}
}
