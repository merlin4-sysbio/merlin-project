package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.Entry;

/**
 * @author João Sequeira
 *
 */
public class InterProResult {


	private String tool;
	private double eValue;
	private double score;
	private String family;
	private String accession;					//database identifier
	private String name;						//protein name
	private Entry entry;
	private String database;					//Library/database referent to the domain, abbreviated
	private String databaseVersion;				//Barely useful
	private List<Location> locations;
	private String ec;							//EC obtained from GO annotation
	private String goname;						//also protein name, distinguished for comparison purposes
	private String localization;				//subcellular localization, someday will be useful
	private List<Model> models;					//List of databases models - accession, (name), (description) - in parentheses, parts not always present, but position is always the same

	/**
	 * 
	 */
	public InterProResult() {
		this.locations = new ArrayList<>();
		this.models = new ArrayList<>();
	}
	/**
	 * @return the tool
	 */
	public String getTool() {
		return tool;
	}
	/**
	 * @param tool the tool to set
	 */
	public void setTool(String tool) {
		this.tool = tool;
	}
	/**
	 * @return the eValue
	 */
	public double geteValue() {
		return eValue;
	}
	/**
	 * @param eValue the eValue to set
	 */
	public void seteValue(double eValue) {
		this.eValue = eValue;
	}
	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}
	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}
	/**
	 * @return the family
	 */
	public String getFamily() {
		return family;
	}
	/**
	 * @param family the family to set
	 */
	public void setFamily(String family) {
		this.family = family;
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
	 * @return the entry
	 */
	public Entry getEntry() {
		return entry;
	}
	/**
	 * @param entry the entry to set
	 */
	public void setEntry(Entry entry) {
		this.entry = entry;
	}
	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}
	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}
	/**
	 * @return the database_version
	 */
	public String getDatabaseVersion() {
		return databaseVersion;
	}
	/**
	 * @param database_version the database_version to set
	 */
	public void setDatabaseVersion(String database_version) {
		this.databaseVersion = database_version;
	}
	/**
	 * @return the location
	 */
	public List<Location> getLocation() {
		return locations;
	}
	/**
	 * @param location the location to set
	 */
	public void addLocation(Location location) {
		this.locations.add(location);
	}
	/**
	 * @return the eC
	 */
	public String getEC() {
		return ec;
	}
	/**
	 * @param eC the eC to set
	 */
	public void setEC(String ec) {
		this.ec = ec;
	}
	/**
	 * @return the gOName
	 */
	public String getGOName() {
		return goname;
	}
	/**
	 * @param gOName the gOName to set
	 */
	public void setGOName(String goname) {
		this.goname = goname;
	}
	/**
	 * @return the localization
	 */
	public String getLocalization() {
		return localization;
	}
	/**
	 * @param localization the localization to set
	 */
	public void setLocalization(String localization) {
		this.localization = localization;
	}
	/**
	 * @return the models
	 */
	public List<Model> getModels() {
		return models;
	}
	/**
	 * @param model to add
	 */
	public void addModel(Model model) {
		this.models.add(model);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InterProResult [tool=" + tool + ", eValue=" + eValue + ", score=" + score + ", family=" + family
				+ ", accession=" + accession + ", name=" + name + ", entry=" + entry + ", database=" + database
				+ ", databaseVersion=" + databaseVersion + ", locations=" + locations + ", ec=" + ec + ", goname="
				+ goname + ", localization=" + localization + ", models=" + models + "]";
	}
	
	
}