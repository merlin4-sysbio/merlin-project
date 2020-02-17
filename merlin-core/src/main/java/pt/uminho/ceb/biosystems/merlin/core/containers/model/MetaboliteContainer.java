package pt.uminho.ceb.biosystems.merlin.core.containers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Oscar
 *
 */
public class MetaboliteContainer implements EntityContainer {
	
	private int metaboliteID;
	private int compartmentID;
	private String externalIdentifier;
	private String name;
	private String formula;
	private Integer charge;
	private String entryType;
	private Double stoichiometric_coefficient;
	private String compartment_name;
	private String compartment_abbreviation;
	private String molecular_weight;
	private List<String> names;
	private List<String> enzymes;
	private List<String> reactions;
	private Map<String, String> pathways;
	private List<String> dblinks;
	private List<String> same_as;
	
	
	/**
	 * @param entry
	 */
	public MetaboliteContainer(String externalIdentifier) {
		this.setExternalIdentifier(externalIdentifier);
		this.setSame_as(new ArrayList<String>());
	}


	/**
	 * @param metaboliteID
	 * @param name
	 * @param formula
	 * @param stoichiometric_coefficient
	 * @param compartment_name
	 */
	public MetaboliteContainer(int metaboliteID, String name, String formula, double stoichiometric_coefficient,
			String compartment_name) {
		
		this.setMetaboliteID(metaboliteID);
		this.setFormula(formula);
		this.setName(name);
		this.setStoichiometric_coefficient(stoichiometric_coefficient);
		this.setCompartment_name(compartment_name);

		

	}


	/**
	 * @param metaboliteID
	 * @param name
	 * @param formula
	 * @param stoichiometric_coefficient
	 * @param compartment_name
	 * @param compartmentID
	 * @param compartmentAbbreviation
	 * @param metaboliteExternalIdentifier
	 */
	public MetaboliteContainer(int metaboliteID, String name, String formula, double stoichiometric_coefficient,
			String compartment_name, int compartmentID, String compartmentAbbreviation, String metaboliteExternalIdentifier) {
		
		this.setMetaboliteID(metaboliteID);
		this.setFormula(formula);
		this.setName(name);
		this.setStoichiometric_coefficient(stoichiometric_coefficient);
		this.setCompartment_name(compartment_name);
		this.setCompartmentID(compartmentID);
		this.setAbbreviation(compartmentAbbreviation);
		this.setExternalIdentifier(metaboliteExternalIdentifier);
	}
	
	
	

	/**
	 * @param metaboliteID
	 * @param formula
	 * @param name
	 * @param externalIdentifier
	 */
	public MetaboliteContainer(int metaboliteID, String formula, String name, String externalIdentifier) {

		this.setExternalIdentifier(externalIdentifier);
		this.setMetaboliteID(metaboliteID);
		this.setFormula(formula);
		this.setName(name);
	}
	
	/**
	 * @param metaboliteID
	 * @param stoichiometric_coefficient
	 * @param compartmentID
	 */
	public MetaboliteContainer(int metaboliteID, double stoichiometric_coefficient, int compartmentID) {

		this.setMetaboliteID(metaboliteID);
		this.setStoichiometric_coefficient(stoichiometric_coefficient);
		this.setCompartmentID(compartmentID);
	}

	/**
	 * @param externalIdentifier
	 * @param stoichiometric_coefficient
	 * @param compartmentID
	 */
	public MetaboliteContainer(String externalIdentifier, double stoichiometric_coefficient) {

		this.setExternalIdentifier(externalIdentifier);
		this.setStoichiometric_coefficient(stoichiometric_coefficient);
	}
	
	public MetaboliteContainer(String externalIdentifier, double stoichiometric_coefficient, String compartmentName) {

		this.setExternalIdentifier(externalIdentifier);
		this.setStoichiometric_coefficient(stoichiometric_coefficient);
		this.setCompartment_name(compartmentName);

	}


	/**
	 * @return the metaboliteID
	 */
	public int getMetaboliteID() {
		return metaboliteID;
	}

	/**
	 * @param metaboliteID the metaboliteID to set
	 */
	public void setMetaboliteID(int metaboliteID) {
		this.metaboliteID = metaboliteID;
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
	 * @return the stoichiometric_coefficient
	 */
	public Double getStoichiometric_coefficient() {
		return stoichiometric_coefficient;
	}

	/**
	 * @param stoichiometric_coefficient the stoichiometric_coefficient to set
	 */
	public void setStoichiometric_coefficient(double stoichiometric_coefficient) {
		this.stoichiometric_coefficient = stoichiometric_coefficient;
	}

//	/**
//	 * @return the numberofchains
//	 */
//	public String getNumberofchains() {
//		return numberofchains;
//	}
//
//	/**
//	 * @param numberofchains the numberofchains to set
//	 */
//	public void setNumberofchains(String numberofchains) {
//		this.numberofchains = numberofchains;
//	}

	/**
	 * @return the compartment_name
	 */
	public String getCompartment_name() {
		return compartment_name;
	}

	/**
	 * @param compartment_name the compartment_name to set
	 */
	public void setCompartment_name(String compartment_name) {
		this.compartment_name = compartment_name;
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @return the same_as
	 */
	public List<String> getSame_as() {
		return same_as;
	}


	/**
	 * @param same_as the same_as to set
	 */
	public void setSame_as(List<String> same_as) {
		this.same_as = same_as;
	}
	
	
	/**
	 * @param same_as
	 */
	public void setSame_as(String same_as) {
		this.same_as.add(same_as);		
	}

	/**
	 * @return the molecular_weight
	 */
	public String getMolecular_weight() {
		return molecular_weight;
	}


	/**
	 * @param molecular_weight the molecular_weight to set
	 */
	public void setMolecular_weight(String molecular_weight) {
		this.molecular_weight = molecular_weight;
	}


	/**
	 * @return the names
	 */
	public List<String> getNames() {
		return names;
	}


	/**
	 * @param names the names to set
	 */
	public void setNames(List<String> names) {
		this.names = names;
	}


	/**
	 * @return the enzymes
	 */
	public List<String> getEnzymes() {
		return enzymes;
	}


	/**
	 * @param enzymes the enzymes to set
	 */
	public void setEnzymes(List<String> enzymes) {
		this.enzymes = enzymes;
	}


	/**
	 * @return the reactions
	 */
	public List<String> getReactions() {
		return reactions;
	}


	/**
	 * @param reactions the reactions to set
	 */
	public void setReactions(List<String> reactions) {
		this.reactions = reactions;
	}


	/**
	 * @return the pathways
	 */
	public Map<String, String> getPathways() {
		return pathways;
	}


	/**
	 * @param pathways the pathways to set
	 */
	public void setPathways(Map<String, String> pathways) {
		this.pathways = pathways;
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


	public int getCompartmentID() {
		return compartmentID;
	}


	public void setCompartmentID(int compartmentID) {
		this.compartmentID = compartmentID;
	}


	@Override
	public void setExternalIdentifier(String externalIdentifier) {

		this.externalIdentifier = externalIdentifier;
	}


	@Override
	public String getExternalIdentifier() {

		return externalIdentifier;
	}


	public String getAbbreviation() {
		return compartment_abbreviation;
	}


	public void setAbbreviation(String abbreviation) {
		this.compartment_abbreviation = abbreviation;
	}


	/**
	 * @return the charge
	 */
	public Integer getCharge() {
		return charge;
	}


	/**
	 * @param charge the charge to set
	 */
	public void setCharge(Integer charge) {
		this.charge = charge;
	}


	/**
	 * @return the entryType
	 */
	public String getEntryType() {
		return entryType;
	}


	/**
	 * @param entryType the entryType to set
	 */
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
}
