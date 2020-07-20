package pt.uminho.ceb.biosystems.merlin.core.containers.gpr;

import java.util.HashMap;
import java.util.Map;

public class ReactionsGPR_CI {
	
	private String reaction;
	private Map<String, ProteinsGPR_CI> proteins;
	
	/**
	 * @param reaction
	 * @param proteins
	 */
	public ReactionsGPR_CI(String reaction) {
		super();
		this.reaction = reaction;
		this.proteins = new HashMap<>();
	}
	
	/**
	 * @param reaction
	 * @param proteins
	 */
	public ReactionsGPR_CI(String reaction, Map<String, ProteinsGPR_CI> proteins) {
		super();
		this.reaction = reaction;
		this.proteins = proteins;
	}
	
	/**
	 * @param proteinsGPR_CI
	 */
	public void addProteinGPR_CI (ProteinsGPR_CI proteinsGPR_CI) {
		
		if(this.proteins==null)
			this.proteins = new HashMap<>();
			
		this.proteins.put(proteinsGPR_CI.getEcnumber(), proteinsGPR_CI);
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
	 * @return the proteins
	 */
	public Map<String, ProteinsGPR_CI> getProteins() {
		return proteins;
	}
	/**
	 * @param proteins the proteins to set
	 */
	public void setProteins(Map<String, ProteinsGPR_CI> proteins) {
		this.proteins = proteins;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReactionsGPR_CI ["
				+ (reaction != null ? "reaction=" + reaction + ", " : "")
				+ (proteins != null ? "proteins=" + proteins : "") + "]";
	}

	

}
