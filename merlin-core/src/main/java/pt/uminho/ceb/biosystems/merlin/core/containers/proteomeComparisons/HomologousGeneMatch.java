package pt.uminho.ceb.biosystems.merlin.core.containers.proteomeComparisons;

public class HomologousGeneMatch {
	
	private Long id;
	private String locusTag;
	private	Long bitScore;
	private Long bbhBitScorePercent;

	
	public HomologousGeneMatch(Long id){
		
		this.setId(id);
	}

	public HomologousGeneMatch(String locusTag){

		this.setLocusTag(locusTag);
	}	


	public HomologousGeneMatch(Long id, String locusTag){

		this(id);
		this.setLocusTag(locusTag);
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
	 * @return the bitScore
	 */
	public Long getBitScore() {
		return bitScore;
	}


	/**
	 * @param bitScore the bitScore to set
	 */
	public void setBitScore(Long bitScore) {
		this.bitScore = bitScore;
	}


	/**
	 * @return the hbbBitScorePercent
	 */
	public Long getHbbBitScorePercent() {
		return bbhBitScorePercent;
	}


	/**
	 * @param hbbBitScorePercent the hbbBitScorePercent to set
	 */
	public void setHbbBitScorePercent(Long hbbBitScorePercent) {
		this.bbhBitScorePercent = hbbBitScorePercent;
	} 
	
	@Override
	public String toString() {
		
		return "HomologousGeneMatch [locusTag = "+this.locusTag+", bitScore = "+this.bitScore
				+", bbhBitScorePercent = "+this.bbhBitScorePercent+"]";
	}

}
