package pt.uminho.ceb.biosystems.merlin.core.containers.gpr;

import java.util.HashMap;
import java.util.Map;

public class ReactionProteinGeneAssociation implements Comparable<ReactionProteinGeneAssociation>{

	private String reaction;
	private Map<String, ProteinGeneAssociation> proteinGeneAssociation;

	/**
	 * @param reaction
	 */
	public ReactionProteinGeneAssociation(String reaction) {

		this.setReaction(reaction);
		this.proteinGeneAssociation = new HashMap<>();
	}

	/**
	 * @param proteinGeneAssociation
	 */
	public void addProteinGeneAssociation(ProteinGeneAssociation proteinGeneAssociation) {

		if(this.proteinGeneAssociation.containsKey(proteinGeneAssociation.getProtein())){
		
			for(GeneAssociation ga : proteinGeneAssociation.getGenes()) {
				
				this.proteinGeneAssociation.get(proteinGeneAssociation.getProtein()).addGeneAssociation(ga);
			}
		}
		else {
			this.proteinGeneAssociation.put(proteinGeneAssociation.getProtein(), proteinGeneAssociation);
		}
	}

	/**
	 * @return the reaction
	 */
	public String getReaction() {
		return reaction;
	}

	/**
	 * @param reaction the reaction to set
	 */
	public void setReaction(String reaction) {
		this.reaction = reaction;
	}

	/**
	 * @return the rules_list
	 */
	public Map<String, ProteinGeneAssociation> getProteinGeneAssociation() {
		return proteinGeneAssociation;
	}

	/**
	 * @param rules_list the rules_list to set
	 */
	public void setProteinGeneAssociation(Map<String, ProteinGeneAssociation> rules_list) {
		this.proteinGeneAssociation = rules_list;
	}


	/**
	 * @param reactionProteinGeneAssociation
	 */
	public boolean mergeRules(ReactionProteinGeneAssociation reactionProteinGeneAssociation) {

		boolean ret = false;
		
		if(this.reaction.equalsIgnoreCase(reactionProteinGeneAssociation.getReaction())) {
			
			for(ProteinGeneAssociation protein : reactionProteinGeneAssociation.getProteinGeneAssociation().values()) {
				
				if(this.getProteinGeneAssociation().containsKey(protein.getProtein())){
					
					for(GeneAssociation genes : protein.getGenes()) {
						
						this.getProteinGeneAssociation().get(protein.getProtein()).addGeneAssociation(genes);
					}
				}
				else {
					
					this.addProteinGeneAssociation(protein);
				}
			}
		}

		return ret;
	}
	
	@Override
	public boolean equals(Object rpga){
		
		if(ReactionProteinGeneAssociation.class.isInstance(rpga)) {
			ReactionProteinGeneAssociation r = (ReactionProteinGeneAssociation) rpga;
			
			if(this.reaction.equalsIgnoreCase(r.getReaction())){
				
				if(this.proteinGeneAssociation.keySet().equals(r.getProteinGeneAssociation().keySet())) {
					
					if(this.proteinGeneAssociation.values().equals(r.getProteinGeneAssociation().values())) {
						
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReactionProteinGeneAssociation ["
				+ (reaction != null ? "reaction=" + reaction + ", " : "")
				+ (proteinGeneAssociation != null ? "proteinGeneAssociation="
						+ proteinGeneAssociation : "") + "]";
	}

	@Override
	public int compareTo(ReactionProteinGeneAssociation rpga) {
		
		if(this.equals(rpga))
			return 0;
		
		int nameDiff = this.reaction.compareToIgnoreCase(rpga.getReaction());
        
		//if(nameDiff != 0)
		{
			
            return nameDiff;
        }
        
        //reactions are equals compare list
       // return this.proteinGeneAssociation.keySet().compareTo(rpga.getProteinGeneAssociation().keySet());
	}

}
