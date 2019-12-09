/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.biocomponents.container;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.StoichiometryValueCI;

/**
 * @author ODias
 *
 */
public class TransportReactionCI extends ReactionCI {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String transportType;
	protected Map<String, Boolean> reversibilityConfirmed_byGene;
	protected Map<String, Map<String, String>> general_equation;
	
	
	/**
	 * @param shortName
	 * @param name
	 * @param reversible
	 * @param reactants
	 * @param products
	 */
	public TransportReactionCI(String shortName, String name, Boolean reversible, Map<String, StoichiometryValueCI> reactants,Map<String, StoichiometryValueCI> products) {

		super(shortName, name, reversible, reactants, products);

	}

	/**
	 * @return the transportType
	 */
	public String getTransportType() {
		return transportType;
	}

	/**
	 * @param transportType the transportType to set
	 */
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}


	/**
	 * @return the general_equation
	 */
	public Map<String, Map<String, String>> getGeneral_equation() {
		return general_equation;
	}

	/**
	 * @param general_equation the general_equation to set
	 */
	public void setGeneral_equation(Map<String, Map<String, String>> general_equation) {
		this.general_equation = general_equation;
	}

	/**
	 * @return the reversibilityConfirmed_byGene
	 */
	public Map<String, Boolean> getReversibilityConfirmed_byGene() {
		return reversibilityConfirmed_byGene;
	}

	/**
	 * @param reversibilityConfirmed_byGene the reversibilityConfirmed_byGene to set
	 */
	public void setReversibilityConfirmed_byGene(
			Map<String, Boolean> reversibilityConfirmed_byGene) {
		this.reversibilityConfirmed_byGene = reversibilityConfirmed_byGene;
	}
	
	
	public TransportReactionCI clone() {
		
		Map<String, StoichiometryValueCI> newReactants = new HashMap<String, StoichiometryValueCI>();
		for (StoichiometryValueCI value : reactants.values()) {
			StoichiometryValueCI newValue = value.clone();
			newReactants.put(newValue.getMetaboliteId(), newValue);
		}

		Map<String, StoichiometryValueCI> newProducts = new HashMap<String, StoichiometryValueCI>();
		for (StoichiometryValueCI value : products.values()) {
			StoichiometryValueCI newValue = value.clone();
			newProducts.put(newValue.getMetaboliteId(), newValue);
		}

		TransportReactionCI cloneReaction = new TransportReactionCI(id, name, reversible, newReactants, newProducts);

		cloneReaction.setGeneRule(this.getGeneRule());
		cloneReaction.setProteinRule(this.proteinRule);
		cloneReaction.setGenesIDs(this.genesIDs);
		cloneReaction.setProteinIds(this.proteinIds);
		cloneReaction.setType(this.type);
		cloneReaction.setAllMetabolitesHaveKEGGId(this.isAllMetabolitesHaveKEGGId());
		cloneReaction.setSubsystems(this.subsystems);
		cloneReaction.setEc_number(this.ecNumber);
		
		cloneReaction.setTransportType(transportType);
		cloneReaction.setReversibilityConfirmed_byGene(reversibilityConfirmed_byGene);
		cloneReaction.setGeneral_equation(general_equation);
		
		return cloneReaction;
	}

}
