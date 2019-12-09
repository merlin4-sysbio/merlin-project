package pt.uminho.ceb.biosystems.merlin.core.containers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProteinContainer implements EntityContainer {

	private String externalIdentifier;
	private int idProtein;
	private List<Integer> reactionsIdentifiers;
	private String name;
	private List<String> names;
	private	List<String> dblinks;
	private String class_;
	private List<String> orthologues;
	private	List<String> cofactors;
	private	Map<String, String> pathways;
	private	List<String> genes;
	private List<String> reactionsExternalIdentifiers;
	private Boolean inModel;
	private String source;
	private String query;
	private String inchi;
	private Float molecularWeight;
	private Float molecularWeightExp;
	private Float molecularWeightKd;
	private Float molecularWeightSeq;
	private Float pi;
	/**
	 * @param entry
	 */
	public ProteinContainer(String externalIdentifier) {
		this.setExternalIdentifier(externalIdentifier);
		this.reactionsIdentifiers=new ArrayList<>();
	}
	
	/**
	 * @param entry
	 */
	public ProteinContainer(int idProtein, String externalIdentifier) {
		
		this.setIdProtein(idProtein);
		this.setExternalIdentifier(externalIdentifier);
	}
	
	/**
	 * @param reactionIdentifier
	 */
	public void addReactionIdentifier(int reactionIdentifier) {
		
		this.reactionsIdentifiers.add(reactionIdentifier);
	}
	
	/**
	 * @param reactionExternalIdentifier
	 */
	public void addReactionExternalIdentifier(String reactionExternalIdentifier) {

		this.reactionsExternalIdentifiers.add(reactionExternalIdentifier);
	}
	
	/**
	 * @param names the names to set
	 */
	public void setNames(List<String> names) {
		this.names = names;
	}
	/**
	 * @return the names
	 */
	public List<String> getNames() {
		return names;
	}
	/**
	 * @param dblinks the dblinks to set
	 */
	public void setDblinks(List<String> dblinks) {
		this.dblinks = dblinks;
	}
	/**
	 * @return the dblinks
	 */
	public List<String> getDblinks() {
		return dblinks;
	}
	/**
	 * @param entry the entry to set
	 */
	public void setExternalIdentifier(String entry) {
		this.externalIdentifier = entry;
	}
	/**
	 * @return the entry
	 */
	public String getExternalIdentifier() {
		return externalIdentifier;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}



	/**
	 * @param cofactors the cofactors to set
	 */
	public void setCofactors(List<String> cofactors) {
		this.cofactors = cofactors;
	}

	/**
	 * @return the cofactors
	 */
	public List<String> getCofactors() {
		return cofactors;
	}

	/**
	 * @param pathways the pathways to set
	 */
	public void setPathways(Map<String, String> pathways) {
		this.pathways = pathways;
	}

	/**
	 * @return the pathways
	 */
	public Map<String, String> getPathways() {
		return pathways;
	}

	/**
	 * @param genes the genes to set
	 */
	public void setGenes(List<String> genes) {
		this.genes = genes;
	}

	/**
	 * @return the genes
	 */
	public List<String> getGenes() {
		return genes;
	}

	/**
	 * @return the orthologues
	 */
	public List<String> getOrthologues() {
		return orthologues;
	}

	/**
	 * @param orthologues the orthologues to set
	 */
	public void setOrthologues(List<String> orthologues) {
		this.orthologues = orthologues;
	}

	public int getIdProtein() {
		return idProtein;
	}

	public void setIdProtein(int idProtein) {
		this.idProtein = idProtein;
	}

	/**
	 * @return
	 */
	public List<Integer> getReactionsIdentifier() {
		return reactionsIdentifiers;
	}

	/**
	 * @param reactionsIdentifiers
	 */
	public void setReactionsIdentifier(List<Integer> reactionsIdentifiers) {
		this.reactionsIdentifiers = reactionsIdentifiers;
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
	 * @return the reactionsExternalIdentifiers
	 */
	public List<String> getReactionsExternalIdentifiers() {
		return reactionsExternalIdentifiers;
	}

	/**
	 * @param reactionsExternalIdentifiers the reactionsExternalIdentifiers to set
	 */
	public void setReactionsExternalIdentifiers(List<String> reactionsExternalIdentifiers) {
		this.reactionsExternalIdentifiers = reactionsExternalIdentifiers;
	}

	/**
	 * @return the class_
	 */
	public String getClass_() {
		return class_;
	}

	/**
	 * @param class_ the class_ to set
	 */
	public void setClass_(String class_) {
		this.class_ = class_;
	}

	/**
	 * @return the inModel
	 */
	public Boolean getInModel() {
		return inModel;
	}

	/**
	 * @param inModel the inModel to set
	 */
	public void setInModel(Boolean inModel) {
		this.inModel = inModel;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	public String getInchi() {
		return inchi;
	}

	public void setInchi(String inchi) {
		this.inchi = inchi;
	}

	public Float getMolecularWeight() {
		return molecularWeight;
	}

	public void setMolecularWeight(Float molecularWeight) {
		this.molecularWeight = molecularWeight;
	}

	public Float getMolecularWeightExp() {
		return molecularWeightExp;
	}

	public void setMolecularWeightExp(Float molecularWeightExp) {
		this.molecularWeightExp = molecularWeightExp;
	}

	public Float getMolecularWeightKd() {
		return molecularWeightKd;
	}

	public void setMolecularWeightKd(Float molecularWeightKd) {
		this.molecularWeightKd = molecularWeightKd;
	}

	public Float getMolecularWeightSeq() {
		return molecularWeightSeq;
	}

	public void setMolecularWeightSeq(Float molecularWeightSeq) {
		this.molecularWeightSeq = molecularWeightSeq;
	}

	public Float getPi() {
		return pi;
	}

	public void setPi(Float pi) {
		this.pi = pi;
	}
}
