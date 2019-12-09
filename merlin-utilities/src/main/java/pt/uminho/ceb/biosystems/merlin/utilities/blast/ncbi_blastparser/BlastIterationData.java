package pt.uminho.ceb.biosystems.merlin.utilities.blast.ncbi_blastparser;

import java.util.List;

/**
 * @author amaromorais
 *
 */
public class BlastIterationData {
	
	private String qseq;
	private String seqDB;
	private Parameters param;
	private String queryID;
	private String queryDef;
	private String querylen;
	private List<Hit> hits;
	private Iteration iteration;
	
	
	public BlastIterationData(Iteration iteration, String queryID, String queryDef) {
		this.setIteration(iteration);
		this.queryID = queryID;
		this.queryDef = queryDef;
	}
	
	public BlastIterationData(Iteration iteration, String queryID, String queryDef, String queryLen) {
		this(iteration,queryID,queryDef);
		this.querylen = queryLen;
	}
	

	public BlastIterationData(Iteration iteration, String queryID, String queryDef, String queryLen, List<Hit> listHits) {
		this(iteration,queryID,queryDef,queryLen);
		this.hits = listHits;
	}
	
	public BlastIterationData(Iteration iteration, String queryID, String queryDef, String queryLen, List<Hit> listHits, String blastDB) {
		this(iteration,queryID,queryDef,queryLen,listHits);
		this.seqDB = blastDB;
	}
	
	public BlastIterationData(Iteration iteration, String queryID, String queryDef, String queryLen, List<Hit> listHits, Parameters params) {
		this(iteration,queryID,queryDef,queryLen,listHits);
		this.param = params;
	}
	
	
	/**
	 * @return the iteration
	 */
	public Iteration getIteration() {
		return iteration;
	}

	/**
	 * @param iteration the iteration to set
	 */
	public void setIteration(Iteration iteration) {
		this.iteration = iteration;
	}
	
	/**
	 * @return
	 */
	public String getQuerySequence () {
		return this.qseq;
	}

	/**
	 * @param qseq
	 */
	public void setQuerySequence (String qseq) {
		this.qseq = qseq;
	}
	
	/**
	 * @return
	 */
	public String getSeqDb () {
		return this.seqDB;
		
	}
	
	/**
	 * @param seqDB
	 */
	public void setSeqDb (String seqDB) {
		this.seqDB = seqDB;
	}
	
	
	/**
	 * @return
	 */
	public Parameters getParameters() {
		return this.param;
	}
	
	/**
	 * @param parameters
	 */
	public void setParameters (Parameters parameters) {
		this.param = parameters;
	}
	
	/**
	 * @return
	 */
	public String getQueryID() {
		return this.queryID;
	}
	
	/**
	 * 
	 * @param queryID
	 */
	public void setQueryID (String queryID) {
		this.queryID = queryID;
	}
	
	
	/**
	 * @return
	 */
	public String getQueryDef() {
		return this.queryDef;
	}
	
	/**
	 * @param querydef
	 */
	public void setQueryDef (String querydef) {
		this.queryDef = querydef;
	}
	
	/**
	 * @return
	 */
	public Integer getQueryLen() {
		return Integer.parseInt(this.querylen);
	}
	
	/**
	 * @param querylen
	 */
	public void setQueryLen (String querylen) {
		this.querylen = querylen;
	}
	
	
	/**
	 * @return
	 */
	public List <Hit> getHits() {
		return this.hits;
	}
	
	
	/**
	 * @param hits
	 */
	public void setHits (List <Hit> hits) {
		this.hits = hits;
	}
	
	
	/**
	 * @param hitNum
	 * @return
	 */
	public String getHitID(String hitNum){
		return this.hits.get(Integer.parseInt(hitNum)-1).getHitId();
	}
	
	/**
	 * @param hitNum
	 * @return
	 */
	public String getHitDefenition(String hitNum){
		return this.hits.get(Integer.parseInt(hitNum)-1).getHitDef();
	}
	
	/**
	 * @param hitNum
	 * @return
	 */
	public String getHitAccession(String hitNum){
		return this.hits.get(Integer.parseInt(hitNum)-1).getHitAccession();
	}
	
	/**
	 * @param hitNum
	 * @return
	 */
	public Integer getHitLength(String hitNum){
		return Integer.parseInt(this.hits.get(Integer.parseInt(hitNum)-1).getHitLen());
	}
	
	/**
	 * @param hit
	 * @return hitBestScore
	 */
	public Double getHitScore(Hit hit){
		
		return Double.parseDouble(hit.getHitHsps().getHsp().get(0).getHspScore());
	}
	
	/**
	 * @param hit
	 * @return
	 */
	public Double getHitBitScore(Hit hit){
		
		return Double.parseDouble(hit.getHitHsps().getHsp().get(0).getHspBitScore());
	}
	
	/**
	 * @param hit
	 * @return
	 */
	public Double getHitEvalue(Hit hit){
		
		return Double.parseDouble(hit.getHitHsps().getHsp().get(0).getHspEvalue());
	}
	
	/**
	 * @param hit
	 * @return hitBestScore
	 */
	public Integer getHitIdentity(String hitNum){
		
		return Integer.parseInt(this.hits.get(Integer.parseInt(hitNum)-1).getHitHsps().getHsp().get(0).getHspIdentity());
	}
	
	/**
	 * @param hit
	 * @return hitBestScore
	 * 
	 * return the Alignment identity percent
	 */
	public Double getIdentityScore(String hitNum){
		
		Hsp hsPair = this.hits.get(Integer.parseInt(hitNum)-1).getHitHsps().getHsp().get(0);
		
		return Double.parseDouble(hsPair.getHspIdentity())/Double.parseDouble(hsPair.getHspAlignLen());
	}
	
	/**
	 * @param hit
	 * @return hitBestScore
	 */
	public Integer getHitPositive(String hitNum){
		
		return Integer.parseInt(this.hits.get(Integer.parseInt(hitNum)-1).getHitHsps().getHsp().get(0).getHspPositive());
	}
	
	/**
	 * @param hitNum
	 * @return
	 */
	public Double getPositivesScore(String hitNum){
		
		Hsp hsPair = this.hits.get(Integer.parseInt(hitNum)-1).getHitHsps().getHsp().get(0);
		
		return Double.parseDouble(hsPair.getHspPositive())/Double.parseDouble(hsPair.getHspAlignLen());
	}	
	
	/**
	 * @param hit
	 * @return
	 */
	public Integer getHitAlignmentLength(String hitNum){
		
		return Integer.parseInt(this.hits.get(Integer.parseInt(hitNum)-1).getHitHsps().getHsp().get(0).getHspAlignLen());
	}
	
	/**
	 * @param hitNum
	 * @return
	 */
	public Integer getHitAlignmentGaps(String hitNum){
		
		return Integer.parseInt(this.hits.get(Integer.parseInt(hitNum)-1).getHitHsps().getHsp().get(0).getHspGaps());
	}
	
	
	/**
	 * @param hitNum
	 * @return
	 */
	public double getHitQueryCoverage(String hitNum){
		
		return (double)(getHitAlignmentLength(hitNum)- getHitAlignmentGaps(hitNum))/(double)getQueryLen();
	}
	
	/**
	 * @param hitNum
	 * @return
	 */
	public double getHiTargetCoverage(String hitNum){
		
		return (double)(getHitAlignmentLength(hitNum)- getHitAlignmentGaps(hitNum))/(double)getHitLength(hitNum);
	}
}
