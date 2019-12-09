package pt.uminho.ceb.biosystems.merlin.core.containers.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


/**
 * @author odias
 *
 */
/**
 * @author BioSystems
 *
 */
public class ReactionContainer implements EntityContainer {

	private int reactionID;
	private boolean inModel;
	private Double lowerBound, upperBound;
	private String externalIdentifier, notes;
	private List<String> names;
	private List<String> dblinks;
	private String equation;
	private	List<MetaboliteContainer> reactantsStoichiometry;
	private	List<MetaboliteContainer> productsStoichiometry;
	private List<String> comments;
	private List<PathwayContainer> pathways;
	private Set<Pair<String, String>> genes;
	private String geneRule;
	private List<ProteinContainer> proteins;
	private CompartmentContainer localisation;
	private boolean isGeneric, non_enzymatic, spontaneous;
	private Set<String> pathwayNames;
	private SourceType source;
	private List<Pair<Integer, String>> proteinsPairs;

	/**
	 * @param entryID
	 */
	public ReactionContainer(String externalIdentifier) {
		this.setExternalIdentifier(externalIdentifier);
		this.proteins = new ArrayList<>();
		this.reactantsStoichiometry = new ArrayList<>();
		this.productsStoichiometry = new ArrayList<>();
		this.pathways = new ArrayList<>();
		this.pathwayNames = new HashSet<>();
	}
	
	/**
	 * @param reactionId
	 */
	public ReactionContainer(Integer reactionId) {
		this.setReactionID(reactionId);
		this.proteins = new ArrayList<>();
		this.reactantsStoichiometry = new ArrayList<>();
		this.productsStoichiometry = new ArrayList<>();
		this.pathways = new ArrayList<>();
		this.pathwayNames = new HashSet<>();
	}


	/**
	 * 
	 * Constructor to build container using the following parameters
	 * Null for all others
	 * 
	 * @param reactionID
	 * @param inModel
	 * @param lowerBound
	 * @param upperBound
	 */
	public ReactionContainer(int reactionID, boolean inModel, double lowerBound, double upperBound) {
		super();
		this.setReactionID(reactionID);
		this.setLowerBound(lowerBound);
		this.setUpperBound(upperBound);
		this.proteins = new ArrayList<>();
		this.reactantsStoichiometry = new ArrayList<>();
		this.productsStoichiometry = new ArrayList<>();
		this.pathways = new ArrayList<>();
		this.names = new ArrayList<>();
		this.pathwayNames = new HashSet<>();
	}

	/**
	 * Constructor to build container using the following parameters
	 * Null for all others
	 * 
	 * @param reactionID
	 * @param externalIdentifier
	 * @param equation
	 * @param localisation
	 * @param entryID
	 * @param inModel
	 * @param isGeneric
	 * @param isSpontaneous
	 * @param isNonEnzymatic
	 */
	public ReactionContainer(int reactionID, String externalIdentifier, String equation, String source,
			boolean inModel, boolean isGeneric, boolean isSpontaneous,
			boolean isNonEnzymatic) {
		super();
		this.setReactionID(reactionID);
		this.setExternalIdentifier(externalIdentifier);
		this.setEquation(equation);
		this.setLowerBound(null);
		this.setUpperBound(null);
		this.proteins = new ArrayList<>();
		this.reactantsStoichiometry = new ArrayList<>();
		this.productsStoichiometry = new ArrayList<>();
		this.pathways = new ArrayList<>();
		this.pathwayNames = new HashSet<>();

	}
	/**
	 * Constructor to build container using the following parameters
	 * Null for all others
	 * 
	 * @param externalIdentifier
	 * @param equation
	 * @param source
	 * @param inModel
	 * @param isGeneric
	 * @param isSpontaneous
	 * @param isNonEnzymatic
	 */
	public ReactionContainer(String externalIdentifier, String equation, String source,
			boolean inModel, boolean isGeneric, boolean isSpontaneous,
			boolean isNonEnzymatic) {
		super();
		this.setReactionID(reactionID);
		this.setExternalIdentifier(externalIdentifier);
		this.setEquation(equation);
		this.setLowerBound(null);
		this.setUpperBound(null);
		this.proteins = new ArrayList<>();
		this.reactantsStoichiometry = new ArrayList<>();
		this.productsStoichiometry = new ArrayList<>();
		this.pathways = new ArrayList<>();
		this.names = new ArrayList<>();
		this.pathwayNames = new HashSet<>();

	}

	/**
	 * Constructor to build container using the following parameters
	 * Null for all others
	 * 
	 * @param reactionID
	 * @param name
	 * @param equation
	 * @param localisation
	 */
	public ReactionContainer(int reactionID, String externalIdentifier, String equation) {
		super();
		this.setReactionID(reactionID);
		this.setExternalIdentifier(externalIdentifier);;
		this.setEquation(equation);
		this.setLowerBound(null);
		this.setUpperBound(null);
		this.proteins = new ArrayList<>();
		this.reactantsStoichiometry = new ArrayList<>();
		this.productsStoichiometry = new ArrayList<>();
		this.pathways = new ArrayList<>();
		this.names = new ArrayList<>();
		this.pathwayNames = new HashSet<>();

	}

	/**
	 * @param reactionContainer
	 */
	public ReactionContainer(ReactionContainer reactionContainer, boolean copyReactionId) {

		if(copyReactionId)
			this.reactionID = reactionContainer.getReactionID();
		this.inModel = reactionContainer.isInModel();
		this.lowerBound = reactionContainer.getLowerBound();
		this.upperBound = reactionContainer.getUpperBound();
		this.externalIdentifier = reactionContainer.getExternalIdentifier();
		this.localisation = reactionContainer.getLocalisation();
		this.notes = reactionContainer.getNotes();
		this.names = reactionContainer.getNames();
		this.dblinks = reactionContainer.getDblinks();
		this.equation = reactionContainer. getEquation();
		this.reactantsStoichiometry = reactionContainer.getReactantsStoichiometry();
		this.productsStoichiometry = reactionContainer.getProductsStoichiometry();
		this.comments = reactionContainer.getComments();
		this.genes = reactionContainer.getGenes();
		this.geneRule = reactionContainer.getGeneRule();
		this.proteins = reactionContainer.getEnzymes();
		this.pathways = reactionContainer.getPathways();
		this.pathwayNames = reactionContainer.getPathwayNames();
		this.source = reactionContainer.getSource();
		this.isGeneric = reactionContainer.isGeneric();
		this.non_enzymatic = reactionContainer.isNon_enzymatic();
		this.spontaneous = reactionContainer.isSpontaneous();
		this.proteinsPairs = reactionContainer.getProteinsPairs();
				
		
	}
	
	/**
	 * @param pathwayCode
	 * @param pathwayName
	 */
	public void addName(String name) {

		this.names.add(name);
	}
	
	/**
	 * @param pathwayCode
	 * @param pathwayName
	 */
	public void addPathway(String pathwayCode, String pathwayName) {

		this.pathways.add(new PathwayContainer(pathwayCode, pathwayName));
	}
	
	/**
	 * @param pathwayName
	 */
	public void addPathway(String pathwayName) {

		this.pathwayNames.add(pathwayName);
		this.pathways.add(new PathwayContainer(pathwayName));
	}
	
	public void addPathway(PathwayContainer container) {
		
		this.pathwayNames.add(container.getName());
		this.pathways.add(container);
	}
	/**
	 * @param idPathway
	 * @param pathwayName
	 */
	public void addPathway(int idPathway, String pathwayName) {

		this.pathways.add(new PathwayContainer(idPathway, pathwayName));
	}
	
	/**
	 * @param idPathway
	 * @param pathwayName
	 */
	public void addPathway(int idPathway, String pathwayName, String pathwayCode) {

		this.pathways.add(new PathwayContainer(idPathway, pathwayCode, pathwayName));
	}

	/**
	 * @param metaboliteID
	 * @param stoichiometric_coefficient
	 * @param compartmentID
	 */
	public void addReactant(int metaboliteID, double stoichiometric_coefficient, int compartmentID) {

		this.reactantsStoichiometry.add(new MetaboliteContainer(metaboliteID, stoichiometric_coefficient, compartmentID));
	}
	
	
	/**
	 * @param metaboliteID
	 * @param stoichiometric_coefficient
	 * @param compartmentID
	 */
	public void addReactant(String externalIdentifier, double stoichiometric_coefficient, String compartmentName) {

		this.reactantsStoichiometry.add(new MetaboliteContainer(externalIdentifier, stoichiometric_coefficient, compartmentName));
	}
	
	/**
	 * @param container
	 */
	public void addReactant(MetaboliteContainer container) {

		this.reactantsStoichiometry.add(container);
	}

	/**
	 * @param metaboliteID
	 * @param stoichiometric_coefficient
	 * @param compartmentID
	 */
	public void addProduct(int metaboliteID, double stoichiometric_coefficient, int compartmentID) {

		this.productsStoichiometry.add(new MetaboliteContainer(metaboliteID, stoichiometric_coefficient, compartmentID));
	}
	
	
	/**
	 * @param metaboliteID
	 * @param stoichiometric_coefficient
	 * @param compartmentID
	 */
	public void addProduct(String externalIdentifier, double stoichiometric_coefficient, String compartmentName) {

		this.productsStoichiometry.add(new MetaboliteContainer(externalIdentifier, stoichiometric_coefficient, compartmentName));
	}
	
	/**
	 * @param container
	 */
	public void addProduct(MetaboliteContainer container) {

		this.productsStoichiometry.add(container);
	}

	/**
	 * @param ecNumber
	 */
	public void addProteins(String ecNumber) {

		proteins.add(new ProteinContainer(ecNumber));
	}

	/**
	 * @param protein_id
	 * @param ec_number
	 */
	public void addProteins(int protein_id, String ecNumber) {

		proteins.add(new ProteinContainer(protein_id, ecNumber));		
	}

	/**
	 * @param reactantsStoichiometry the reactantsStoichiometry to set
	 */
	public void setReactantsStoichiometry(List<MetaboliteContainer> reactantsStoichiometry) {
		this.reactantsStoichiometry = reactantsStoichiometry;
	}

	/**
	 * @return the reactantsStoichiometry
	 */
	public List<MetaboliteContainer> getReactantsStoichiometry() {
		return reactantsStoichiometry;
	}

	/**
	 * @param enzymes the enzymes to set
	 */
	public void setEnzymes(List<ProteinContainer> proteins) {
		this.proteins = proteins;
	}
	/**
	 * @return the enzymes
	 */
	public List<ProteinContainer> getEnzymes() {
		return proteins;
	}
	/**
	 * @param productsStoichiometry the productsStoichiometry to set
	 */
	public void setProductsStoichiometry(List<MetaboliteContainer> productsStoichiometry) {
		this.productsStoichiometry = productsStoichiometry;
	}
	/**
	 * @return the productsStoichiometry
	 */
	public List<MetaboliteContainer> getProductsStoichiometry() {
		return productsStoichiometry;
	}
	/**
	 * @param equation the equation to set
	 */
	public void setEquation(String equation) {
		this.equation = equation;
	}
	/**
	 * @return the equation
	 */
	public String getEquation() {
		return equation;
	}

	/**
	 * @param isGeneric the isGeneric to set
	 */
	public void setGeneric(boolean isGeneric) {
		this.isGeneric = isGeneric;
	}

	/**
	 * @return the isGeneric
	 */
	public boolean isGeneric() {
		return isGeneric;
	}

	/**
	 * @param spontaneous the spontaneous to set
	 */
	public void setSpontaneous(boolean spontaneous) {
		this.spontaneous = spontaneous;
	}

	/**
	 * @return the spontaneous
	 */
	public boolean isSpontaneous() {
		return spontaneous;
	}

	/**
	 * @param non_enzymatic the non_enzymatic to set
	 */
	public void setNon_enzymatic(boolean non_enzymatic) {
		this.non_enzymatic = non_enzymatic;
	}

	/**
	 * @return the non_enzymatic
	 */
	public boolean isNon_enzymatic() {
		return non_enzymatic;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	/**
	 * @return the comments
	 */
	public List<String> getComments() {
		return comments;
	}


	/**
	 * @return the proteinsPairs
	 */
	public List<Pair<Integer, String>> getProteinsPairs() {
		return proteinsPairs;
	}

	/**
	 * @param proteinsPairs the proteinsPairs to set
	 */
	public void addProteinPair(Pair<Integer, String> proteinsPairs) {
		
		if(this.proteinsPairs == null)
			this.proteinsPairs = new ArrayList<>();
		
		this.proteinsPairs.add(proteinsPairs);
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
	 * @return the reversible
	 */
	public boolean isReversible() {

		return (lowerBound < 0 && upperBound > 0);
	}

	/**
	 * @return the inModel
	 */
	public boolean isInModel() {
		return inModel;
	}

	/**
	 * @param inModel the inModel to set
	 */
	public void setInModel(boolean inModel) {
		this.inModel = inModel;
	}

	/**
	 * @return the localisation
	 */
	public CompartmentContainer getLocalisation() {
		return localisation;
	}

	/**
	 * @param localisation the localisation to set
	 */
	public void setLocalisation(CompartmentContainer compartment) {
		this.localisation = compartment;
	}
	
	/**
	 * @param localisation the localisation to set
	 */
	public void setLocalisation(int idCompartment) {
		this.localisation = new CompartmentContainer(idCompartment);
	}
	
	/**
	 * @param localisation the localisation to set
	 */
	public void setLocalisation(String compartmentName) {
		this.localisation = new CompartmentContainer(compartmentName);
	}

	/**
	 * @return the lowerBound
	 */
	public Double getLowerBound() {
		return lowerBound;
	}

	/**
	 * @param lowerBound the lowerBound to set
	 */
	public void setLowerBound(Double lowerBound) {
		this.lowerBound = lowerBound;
	}

	/**
	 * @return the upperBound
	 */
	public Double getUpperBound() {
		return upperBound;
	}

	/**
	 * @param upperBound the upperBound to set
	 */
	public void setUpperBound(Double upperBound) {
		this.upperBound = upperBound;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the genes
	 */
	public Set<Pair<String, String>> getGenes() {
		return genes;
	}

	/**
	 * @param genes the genes to set
	 */
	public void setGenes(Set<Pair<String, String>> genes) {
		this.genes = genes;
	}

	/**
	 * @return the reactionID
	 */
	public int getReactionID() {
		return reactionID;
	}

	/**
	 * @param reactionID the reactionID to set
	 */
	public void setReactionID(int reactionID) {
		this.reactionID = reactionID;
	}

	/**
	 * @return the pathways
	 */
	public Set<String> getPathwayNames() {
		return pathwayNames;
	}

	/**
	 * @param pathways the pathways to set
	 */
	public void setPathways(Set<String> pathwayNames) {
		this.pathwayNames = pathwayNames;
	}

	/**
	 * @return the geneRule
	 */
	public String getGeneRule() {
		return geneRule;
	}

	/**
	 * @param geneRule the geneRule to set
	 */
	public void setGeneRule(String geneRule) {
		this.geneRule = geneRule;
	}


	/**
	 * @param geneLocus
	 * @param geneName
	 */
	public void addGene(String geneLocus, String geneName){

		if(this.genes==null)
			this.genes = new HashSet<>();

		this.genes.add(new Pair<String, String> (geneLocus, geneName));
	}

	public String getExternalIdentifier() {
		return externalIdentifier;
	}

	public void setExternalIdentifier(String externalIdentifier) {
		this.externalIdentifier = externalIdentifier;
	}

	/**
	 * @return
	 */
	public SourceType getSource() {

		return this.source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(SourceType source) {
		this.source = source;
	}

	/**
	 * @return the pathways
	 */
	public List<PathwayContainer> getPathways() {
		return pathways;
	}

	/**
	 * @param pathways the pathways to set
	 */
	public void setPathways(List<PathwayContainer> pathways) {
		this.pathways = pathways;
	}
	
	public Set<String> getAllPathwaysExternalIdentifiers(){
		
		Set<String> paths = new HashSet<>();
		
		for(PathwayContainer path : pathways)
			paths.add(path.getExternalIdentifier());
		
		return paths;
	}
	
	public Set<String> getAllPathwaysCodes(){
		
		Set<String> paths = new HashSet<>();
		
		for(PathwayContainer path : pathways)
			paths.add(path.getCode());
		
		return paths;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((dblinks == null) ? 0 : dblinks.hashCode());
		result = prime * result + ((equation == null) ? 0 : equation.hashCode());
		result = prime * result + ((externalIdentifier == null) ? 0 : externalIdentifier.hashCode());
		result = prime * result + ((geneRule == null) ? 0 : geneRule.hashCode());
		result = prime * result + ((genes == null) ? 0 : genes.hashCode());
		result = prime * result + (inModel ? 1231 : 1237);
		result = prime * result + (isGeneric ? 1231 : 1237);
		result = prime * result + ((localisation == null) ? 0 : localisation.hashCode());
		result = prime * result + ((lowerBound == null) ? 0 : lowerBound.hashCode());
		result = prime * result + ((names == null) ? 0 : names.hashCode());
		result = prime * result + (non_enzymatic ? 1231 : 1237);
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((pathwayNames == null) ? 0 : pathwayNames.hashCode());
		result = prime * result + ((pathways == null) ? 0 : pathways.hashCode());
		result = prime * result + ((productsStoichiometry == null) ? 0 : productsStoichiometry.hashCode());
		result = prime * result + ((proteins == null) ? 0 : proteins.hashCode());
		result = prime * result + ((proteinsPairs == null) ? 0 : proteinsPairs.hashCode());
		result = prime * result + ((reactantsStoichiometry == null) ? 0 : reactantsStoichiometry.hashCode());
		result = prime * result + reactionID;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + (spontaneous ? 1231 : 1237);
		result = prime * result + ((upperBound == null) ? 0 : upperBound.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReactionContainer other = (ReactionContainer) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (dblinks == null) {
			if (other.dblinks != null)
				return false;
		} else if (!dblinks.equals(other.dblinks))
			return false;
		if (equation == null) {
			if (other.equation != null)
				return false;
		} else if (!equation.equals(other.equation))
			return false;
		if (externalIdentifier == null) {
			if (other.externalIdentifier != null)
				return false;
		} else if (!externalIdentifier.equals(other.externalIdentifier))
			return false;
		if (geneRule == null) {
			if (other.geneRule != null)
				return false;
		} else if (!geneRule.equals(other.geneRule))
			return false;
		if (genes == null) {
			if (other.genes != null)
				return false;
		} else if (!genes.equals(other.genes))
			return false;
		if (inModel != other.inModel)
			return false;
		if (isGeneric != other.isGeneric)
			return false;
		if (localisation == null) {
			if (other.localisation != null)
				return false;
		} else if (!localisation.equals(other.localisation))
			return false;
		if (lowerBound == null) {
			if (other.lowerBound != null)
				return false;
		} else if (!lowerBound.equals(other.lowerBound))
			return false;
		if (names == null) {
			if (other.names != null)
				return false;
		} else if (!names.equals(other.names))
			return false;
		if (non_enzymatic != other.non_enzymatic)
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (pathwayNames == null) {
			if (other.pathwayNames != null)
				return false;
		} else if (!pathwayNames.equals(other.pathwayNames))
			return false;
		if (pathways == null) {
			if (other.pathways != null)
				return false;
		} else if (!pathways.equals(other.pathways))
			return false;
		if (productsStoichiometry == null) {
			if (other.productsStoichiometry != null)
				return false;
		} else if (!productsStoichiometry.equals(other.productsStoichiometry))
			return false;
		if (proteins == null) {
			if (other.proteins != null)
				return false;
		} else if (!proteins.equals(other.proteins))
			return false;
		if (proteinsPairs == null) {
			if (other.proteinsPairs != null)
				return false;
		} else if (!proteinsPairs.equals(other.proteinsPairs))
			return false;
		if (reactantsStoichiometry == null) {
			if (other.reactantsStoichiometry != null)
				return false;
		} else if (!reactantsStoichiometry.equals(other.reactantsStoichiometry))
			return false;
		if (reactionID != other.reactionID)
			return false;
		if (source != other.source)
			return false;
		if (spontaneous != other.spontaneous)
			return false;
		if (upperBound == null) {
			if (other.upperBound != null)
				return false;
		} else if (!upperBound.equals(other.upperBound))
			return false;
		return true;
	}
	
	
	
	

}
