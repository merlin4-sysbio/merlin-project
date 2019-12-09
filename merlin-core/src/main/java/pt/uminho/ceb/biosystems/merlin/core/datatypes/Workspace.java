package pt.uminho.ceb.biosystems.merlin.core.datatypes;

import java.util.Map;
import java.util.Observable;

/**
 * @author Oscar Dias
 *
 */
public class Workspace extends Observable  {

	/**
	 * 
	 */
	private WorkspaceDatabase database;
	private String fileName;
	private boolean initialiseHomologyData;
	private long taxonomyID;
	private String organismName;
	private String organismLineage;
	private Map<String,String> genomeLocusTag;
	protected String workspaceName;
	private String homologyDatabase;

	/**
	 * @param database
	 * @param name
	 */
	public Workspace(WorkspaceDatabase database) {

		this.database = database;
		this.fileName = "";
		this.taxonomyID = -1;
	}

	public WorkspaceDatabase getDatabase() {
		return database;
	}

	public void setDatabase(WorkspaceDatabase db) {
		this.database = db;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getName() {
		return this.workspaceName;
	}

	public void setName(String workspaceName) {
		this.workspaceName = workspaceName;
	}

	/**
	 * @return the taxonomyID
	 */
	public long getTaxonomyID() {
		return taxonomyID;
	}

	/**
	 * @param taxonomyID the taxonomyID to set
	 */
	public void setTaxonomyID(long taxonomyID) {

		this.taxonomyID = taxonomyID;
	}

	/**
	 * @return
	 */
	public boolean isInitialiseHomologyData() {
		return initialiseHomologyData;
	}

	/**
	 * @param initialiseHomologyData
	 */
	public void setInitialiseHomologyData(boolean initialiseHomologyData) {
		this.initialiseHomologyData = initialiseHomologyData;
	}

	/**
	 * @return the organismName
	 */
	public String getOrganismName() {
		return organismName;
	}

	/**
	 * @param organismName the organismName to set
	 */
	public void setOrganismName(String organismName) {
		this.organismName = organismName;
	}

	/**
	 * @return the organismLineage
	 */
	public String getOrganismLineage() {
		return organismLineage;
	}

	/**
	 * @param organismLineage the organismLineage to set
	 */
	public void setOrganismLineage(String organismLineage) {
		this.organismLineage = organismLineage;
	}

	public Map<String,String> getGenomeLocusTag() {
		return genomeLocusTag;
	}

	public void setGenomeLocusTag(Map<String,String> genomeLocusTag) {
		this.genomeLocusTag = genomeLocusTag;
	}
	
	public void setHomologyDatabase(String homologyDatabase) {
		
		this.homologyDatabase = homologyDatabase;
	}
	
	public String getHomologyDatabase() {
		
		return this.homologyDatabase;
	}
	
	public boolean isEukaryoticOrganism() {
		
		return this.getOrganismLineage().startsWith("Eukaryota");
	}
}
