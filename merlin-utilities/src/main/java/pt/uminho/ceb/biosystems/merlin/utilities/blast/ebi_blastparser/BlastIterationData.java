package pt.uminho.ceb.biosystems.merlin.utilities.blast.ebi_blastparser;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.utilities.blast.ebi_blastparser.BlastIterationData;

/**
 * @author diogolima
 *
 */
public class BlastIterationData {
	
	private String qseq;
	private String seqDB;
	private TParameters param;
	private String queryID;
	private String queryDef;
	private String querylen;
	private List<THit> hits;
	private TIteration iteration;
	private String program;
	private String version;
	
	
	
	public BlastIterationData(String queryID, String queryLen, List<THit> listHits,  String blastDB, String program, String version) {
		this.setQueryID(queryID);
		this.setQueryLen(queryLen);
		this.setHits(listHits);
		this.setSeqDb(blastDB);
		this.setProgram(program);
		this.setVersion(version);
		
	}
	
	public BlastIterationData(TIteration iteration, String queryID, String queryDef) {
		this.setIteration(iteration);
		this.queryID = queryID;
		this.queryDef = queryDef;
	}
	
	public BlastIterationData(TIteration iteration, String queryID, String queryDef, String queryLen) {
		this(iteration,queryID,queryDef);
		this.querylen = queryLen;
	}
	

	public BlastIterationData(TIteration iteration, String queryID, String queryDef, String queryLen, List<THit> listHits) {
		this(iteration,queryID,queryDef,queryLen);
		this.hits = listHits;
	}
	
	public BlastIterationData(TIteration iteration, String queryID, String queryDef, String queryLen, List<THit> listHits, String blastDB) {
		this(iteration,queryID,queryDef,queryLen,listHits);
		this.seqDB = blastDB;
	}
	
	public BlastIterationData(TIteration iteration, String queryID, String queryDef, String queryLen, List<THit> listHits, TParameters params) {
		this(iteration,queryID,queryDef,queryLen,listHits);
		this.param = params;
	}
	
	
	/**
	 * @return the iteration
	 */
	public TIteration getIteration() {
		return iteration;
	}

	/**
	 * @param iteration the iteration to set
	 */
	public void setIteration(TIteration iteration) {
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
	public TParameters getParameters() {
		return this.param;
	}
	
	/**
	 * @param parameters
	 */
	public void setParameters (TParameters parameters) {
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
	public List <THit> getHits() {
		return this.hits;
	}
	
	
	/**
	 * @param hits
	 */
	public void setHits (List <THit> hits) {
		this.hits = hits;
	}
	
	
	/**
	 * @param hitNum
	 * @return
	 */
	public String getHitID(String hitNum){
		return this.hits.get(Integer.parseInt(hitNum)-1).getId();
	}
	
	
	/**
	 * @param hitNum
	 * @return
	 */
	public String getHitAccession(String hitNum){
		return this.hits.get(Integer.parseInt(hitNum)-1).getAc();
	}
	
	/**
	 * @param hitNum
	 * @return
	 */
	public Integer getHitLength(String hitNum){
		return Integer.parseInt(this.hits.get(Integer.parseInt(hitNum)-1).getLength() +"");
	}
	
		
	/**
	 * @param hit
	 * @return hitBestScore
	 */
	public Double getHitScore(THit hit){
		
		return Double.parseDouble(hit.getAlignments().getAlignment().get(0).getScore() +"");
	}
	
	/**
	 * @param hit
	 * @return
	 */
	public Double getHitBitScore(THit hit){
		
		return Double.parseDouble(hit.getAlignments().getAlignment().get(0).getBits() +"");
	}
	
	/**
	 * @param hit
	 * @return
	 */
	public Double getHitEvalue(THit hit){
		
		return Double.parseDouble(hit.getAlignments().getAlignment().get(0).getExpectation() +"");
	}
	
	/**
	 * @param hit
	 * @return hitBestScore
	 */
	public Integer getHitIdentity(String hitNum){
		
		return Integer.parseInt(this.hits.get(Integer.parseInt(hitNum)-1).getAlignments().getAlignment().get(0).getIdentity() + "");
	}
	
	/**
	 * @param hit
	 * @return hitIdentity
	 */
	public Integer getHitIdentity(THit hit){
		
		return hit.getAlignments().getAlignment().get(0).getIdentity().intValue();
	}
	
	public Integer getHitPositive(THit hit){
		
		return hit.getAlignments().getAlignment().get(0).getPositives().intValue();
	}
	
	public Integer getHitQuerySeqLength(THit hit){
		
		return hit.getAlignments().getAlignment().get(0).getQuerySeq().getEnd() - hit.getAlignments().getAlignment().get(0).getQuerySeq().getStart() + 1;
	}
	
	public Integer getHitMatchSeqLength(THit hit){
		
		return hit.getAlignments().getAlignment().get(0).getMatchSeq().getEnd() - hit.getAlignments().getAlignment().get(0).getMatchSeq().getStart() + 1;
	}
	
	/**
	 * @param hit
	 * @return hitBestScore
	 */
	public Integer getHitPositive(String hitNum){
		
		return this.hits.get(Integer.parseInt(hitNum)-1).getAlignments().getAlignment().get(0).getPositives().intValue();
	}
	
	/**
	 * @param hitNum
	 * @return
	 */
	public Integer getHitAlignmentGaps(String hitNum){
		
		return Integer.parseInt(this.hits.get(Integer.parseInt(hitNum)-1).getAlignments().getAlignment().get(0).getGaps() + "");
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}
	
	
}
