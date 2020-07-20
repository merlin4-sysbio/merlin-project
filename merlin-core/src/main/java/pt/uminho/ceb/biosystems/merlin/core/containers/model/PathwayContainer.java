package pt.uminho.ceb.biosystems.merlin.core.containers.model;

import java.util.List;

public class PathwayContainer {

	private String externalIdentifier;
	private int idPathway;
	private List<Integer> reactionsIdentifiers;
	private String name, code;
	private	List<String> dblinks;
	private String sbmlPath;
	
	/**
	 * @param idPathway
	 */
	public PathwayContainer(int idPathway) {
		super();
		this.idPathway = idPathway;
	}
	
	/**
	 * @param idPathway
	 */
	public PathwayContainer(String name) {
		super();
		this.name = name;
	}
	
	/**
	 * @param idPathway
	 */
	public PathwayContainer(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	
	/**
	 * @param idPathway
	 */
	public PathwayContainer(int idPathway, String code, String name) {
		super();
		this.idPathway = idPathway;
		this.code = code;
		this.name = name;
	}
	
	/**
	 * @param idPathway
	 * @param name
	 */
	public PathwayContainer(int idPathway, String name) {
		super();
		this.idPathway = idPathway;
		this.name = name;
	}
	
	/**
	 * @return the externalIdentifier
	 */
	public String getExternalIdentifier() {
		return externalIdentifier;
	}
	/**
	 * @param externalIdentifier the externalIdentifier to set
	 */
	public void setExternalIdentifier(String externalIdentifier) {
		this.externalIdentifier = externalIdentifier;
	}
	/**
	 * @return the idPathway
	 */
	public int getIdPathway() {
		return idPathway;
	}
	/**
	 * @param idPathway the idPathway to set
	 */
	public void setIdPathway(int idPathway) {
		this.idPathway = idPathway;
	}
	/**
	 * @return the reactionsIdentifiers
	 */
	public List<Integer> getReactionsIdentifiers() {
		return reactionsIdentifiers;
	}
	/**
	 * @param reactionsIdentifiers the reactionsIdentifiers to set
	 */
	public void setReactionsIdentifiers(List<Integer> reactionsIdentifiers) {
		this.reactionsIdentifiers = reactionsIdentifiers;
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
	 * @return the dblinks
	 */
	public List<String> getDblinks() {
		return dblinks;
	}
	/**
	 * @param dblinks the dblinks to set
	 */
	public void setDblinks(List<String> dblinks) {
		this.dblinks = dblinks;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the sbmlPath
	 */
	public String getSbmlPath() {
		return sbmlPath;
	}

	/**
	 * @param sbmlPath the sbmlPath to set
	 */
	public void setSbmlPath(String sbmlPath) {
		this.sbmlPath = sbmlPath;
	}
}
