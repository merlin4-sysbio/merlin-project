package pt.uminho.ceb.biosystems.merlin.core.containers.proteomeComparisons;

import java.util.List;

public class GeneAlignmentsResult {
	
	private Long id;
	private String locusTag;
	private	List<HomologousGeneMatch> homologousGenes;

	
	public GeneAlignmentsResult(Long id){
		
		this.setId(id);
	}

	public GeneAlignmentsResult(String locusTag){

		this.setLocusTag(locusTag);
	}	


	public GeneAlignmentsResult(Long id, String locusTag){

		this(id);
		this.setLocusTag(locusTag);
	}
	
	public GeneAlignmentsResult(Long id, String locusTag, List<HomologousGeneMatch> homologousList){

		this(id,locusTag);
		this.setHomologousGenes(homologousList);
	}
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the locusTag
	 */
	public String getLocusTag() {
		return locusTag;
	}


	/**
	 * @param locusTag the locusTag to set
	 */
	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}


	/**
	 * @return the homologousGenes
	 */
	public List<HomologousGeneMatch> getHomologousGenes() {
		return homologousGenes;
	}


	/**
	 * @param homologousGenes the homologousGenes to set
	 */
	public void setHomologousGenes(List<HomologousGeneMatch> homologousGenes) {
		this.homologousGenes = homologousGenes;
	}
	
	
	@Override
	public String toString() {
		
		return "GeneAligmentsResult [id = " + this.id + ", locusTag = "+this.locusTag
				+", homologousGenes = "+this.homologousGenes+"]";
	}
	
}
