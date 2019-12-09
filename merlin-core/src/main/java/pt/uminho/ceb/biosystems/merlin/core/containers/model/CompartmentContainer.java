package pt.uminho.ceb.biosystems.merlin.core.containers.model;

public class CompartmentContainer {
	
	
	private int compartmentID;
	private int reportID;
	private String name;
	private String abbreviation;
	private boolean primaryLocation;
	private String locusTag;
	private Double score;
	
	/**
	 * @param compartmentID
	 * @param name
	 * @param abbreviation
	 */
	public CompartmentContainer(int compartmentID, String name,
			String abbreviation) {
		super();
		this.setCompartmentID(compartmentID);
		this.setName(name);
		this.setAbbreviation(abbreviation);
	}

	/**
	 * @param idCompartment
	 */
	public CompartmentContainer(int idCompartment) {
		this.compartmentID = idCompartment;
	}

	/**
	 * @param compartmentName
	 */
	public CompartmentContainer(String compartmentName) {
		this.name = compartmentName;
	}

	/**
	 * @param name
	 * @param abbreviation
	 */
	public CompartmentContainer(String name, String abbreviation) {
		this.name = name;
		this.abbreviation = abbreviation; 
	}

	/**
	 * @return the compartmentID
	 */
	public int getCompartmentID() {
		return compartmentID;
	}

	/**
	 * @param compartmentID the compartmentID to set
	 */
	public void setCompartmentID(int compartmentID) {
		this.compartmentID = compartmentID;
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
	 * @return the abbreviation
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * @param abbreviation the abbreviation to set
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 * @return the primaryLocation
	 */
	public boolean isPrimaryLocation() {
		return primaryLocation;
	}

	/**
	 * @param primaryLocation the primaryLocation to set
	 */
	public void setPrimaryLocation(boolean primaryLocation) {
		this.primaryLocation = primaryLocation;
	}

	public String getLocusTag() {
		return locusTag;
	}

	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	/**
	 * @return the reportID
	 */
	public int getReportID() {
		return reportID;
	}

	/**
	 * @param reportID the reportID to set
	 */
	public void setReportID(int reportID) {
		this.reportID = reportID;
	}
	
	
}