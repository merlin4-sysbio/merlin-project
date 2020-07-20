package pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes;

import java.util.List;

public class AnnotationEnzymesHomologyScorer {


	private Double score1;
	private Double score2;
	private List<Integer> list;
	private Double alpha, beta;
	private Double score;
	private int maxRank, minimumNumberofHits;

	/**
	 * @param score1
	 * @param score2
	 * @param maxRank
	 * @param alpha
	 * @param beta
	 */
	public AnnotationEnzymesHomologyScorer(Double score1, List<Integer> list, int maxRank, Double alpha, Double beta, int minimumNumberofHits) {
		
		this.score1=score1;
		this.list=list;
		this.alpha = alpha;
		this.beta = beta;
		this.setMaxRank(maxRank);
		this.setMinimumNumberofHits(minimumNumberofHits);
		this.taxonomyAverage();
		this.scoreCalculation();
	}
	
	/**
	 * performs the score calculation
	 * 
	 */
	public void scoreCalculation(){
		
		if(this.getScore1()>0) {
			
			this.setScore(this.getAlpha() * this.getScore1() +(1-this.getAlpha())*this.getScore2());

		}
		else {
			
			this.setScore(1.0);
		}
		
	}

	/**
	 * @return the s1
	 */
	public Double getScore1() {
		return score1;
	}

	/**
	 * @param s1 the s1 to set
	 */
	public void setScore1(Double score1) {
		this.score1 = score1;
	}

	/**
	 * @return
	 */
	public Double getScore2() {
		return score2;
	}

	/**
	 * @param s2
	 */
	public void setScore2(Double s2) {
		this.score2 = s2;
	}

	/**
	 * @return the s2
	 */
	public List<Integer> getList() {
		return list;
	}

	/**
	 * @param score2 the s2 to set
	 */
	public void setList(List<Integer> list, int maxRank) {
		
		this.list = list;
		this.setMaxRank(maxRank);
	}

	/**
	 * @return the alpha
	 */
	public Double getAlpha() {
		
		return alpha;
	}

	/**
	 * @param d the alpha to set
	 */
	public void setAlpha(Double alpha) {
		
		this.alpha = alpha;
	}

	/**
	 * @return the s
	 */
	public Double getScore() {
		
		return score;
	}

	/**
	 * @param s the s to set
	 */
	public void setScore(Double score) {
		
		this.score = score;
	}

	/**
	 * @param numberOfOrganisms
	 * @return
	 */
	public void taxonomyAverage() {
		
		List<Integer> taxScore;
		double r=0;
		
		if(this.minimumNumberofHits>=this.getList().size()) {
			
			taxScore = this.getList();
		}
		else {
			
			taxScore = this.getList().subList(0, this.minimumNumberofHits);
			
			for(int i = this.minimumNumberofHits; i<this.getList().size(); i++) {
				
				for(int j = 0; j<taxScore.size(); j++) {
					
					if(taxScore.get(j) < this.getList().get(i)) {
						
						taxScore.set(j, this.getList().get(i));
						j = taxScore.size();
					}
				}
			}
		}

		for(int rank:taxScore) {
			r+=rank;
		}
		
		//o penalty aplicado ao ranking maximo vai baixar o divisor, por isso o ranking vai ser mais alto...
		//r=(r/this.getMaxRank())/(numberOfOrganisms*(1-(numberOfOrganisms-ranks.size())*penaltyCost));
		
		//r=(r*(1-(numberOfOrganisms-ranks.size())*this.beta)/(numberOfOrganisms*this.getMaxRank()));
		
		r=(r*(1-(this.minimumNumberofHits - taxScore.size()) * this.beta)/(taxScore.size()*this.getMaxTaxonomy()));
		this.score2 = r;
	}

	/**
	 * @param maxRank the maxRank to set
	 */
	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}

	/**
	 * @return the maxRank
	 */
	public int getMaxTaxonomy() {
		return maxRank;
	}

	/**
	 * @return the beta
	 */
	public Double getBeta() {
		return beta;
	}

	/**
	 * @param beta the beta to set
	 */
	public void setBeta(Double beta) {
		this.beta = beta;
	}

	/**
	 * @return the minimumNumberofHits
	 */
	public int getMinimumNumberofHits() {
		return minimumNumberofHits;
	}

	/**
	 * @param minimumNumberofHits the minimumNumberofHits to set
	 */
	public void setMinimumNumberofHits(int minimumNumberofHits) {
		this.minimumNumberofHits = minimumNumberofHits;
	}

	@Override
	public String toString() {
		return "BlastScorer [s1=" + score1 + ", s2=" + score2 + ", alpha=" + alpha + ", beta=" + beta + ", s=" + score
				+ ", maxRank=" + maxRank + ", minimumNumberofHits=" + minimumNumberofHits + "]";
	}
	
}
