package pt.uminho.ceb.biosystems.merlin.core.containers.alignment;

import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.AlignmentScoreType;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Method;

/**
 * @author Oscar
 *
 */
public class AlignmentContainer {

	private String query, queryLocus;
	private String tcdbID;
	private String target, targetLocus;
	private AlignmentScoreType alignmentScoreType;
	private String matrix;
	private String ko;
	private double score, maxScore, minScore, evalue, bitScore;
	private Method method;
	private int[][][] scoreMatrix;
	private int numIdenticals, numSimilars, queryLength, targetLength, alignmentLength;
	private double coverageQuery;
	private double coverageTarget;
	private int gapsQuery;
	private int gapsTarget;
	private String ecNumber;
	private Map<String, Set<String>> closestOrthologues;
	private Map<String, Set<Integer >> modules;
	
	
	/**
	 * Alignment information container.
	 * 
	 * @param query
	 * @param target
	 * @param ko
	 * @param maxScore
	 * @param minScore
	 * @param alignedScore
	 * @param numIdenticals
	 * @param numSimilars
	 * @param alignmentLength
	 * @param queryLength
	 * @param targetLength
	 * @param matrix
	 * @param method
	 * @param alignmentScoreType
	 */
	public AlignmentContainer(String query, String target, String ko, double maxScore, double minScore, double score,
			int numIdenticals, int numSimilars, int alignmentLength, int queryLength, int targetLength, 
			String matrix, Method method, AlignmentScoreType alignmentScoreType) {

		super();
		this.setQuery(query);
		this.setTarget(target);
		this.setKo(ko);
		this.setMaxScore(maxScore);
		this.setMinScore(minScore);
		this.setScore(score);
		this.setNumIdenticals(numIdenticals);
		this.setNumSimilars(numSimilars);
		this.setAlignmentLength(alignmentLength);
		this.setQueryLength(queryLength);
		this.setTargetLength(targetLength);
		this.setMethod(method);
		this.setMatrix(matrix);
		this.setAlignmentScoreType(alignmentScoreType);
		
	}
	
	/**
	 * @param query
	 * @param target
	 * @param ko
	 * @param calculatedScore
	 * @param maxScore
	 * @param minScore
	 * @param alignedScore
	 * @param numIdenticals
	 * @param numSimilars
	 * @param alignmentLength
	 * @param queryLength
	 * @param targetLength
	 * @param coverageQuery
	 * @param coverageTarget
	 * @param gapsQuery
	 * @param gapsTarget
	 * @param matrix
	 * @param method
	 * @param alignmentScoreType
	 */
	public AlignmentContainer(String query, String target, String ko, double maxScore, double minScore, double score,
			int numIdenticals, int numSimilars, int alignmentLength, int queryLength, int targetLength, double coverageQuery, double coverageTarget, int gapsQuery, int gapsTarget, 
			String matrix, Method method, AlignmentScoreType alignmentScoreType) {

		super();
		this.setQuery(query);
		this.setTarget(target);
		this.setKo(ko);
		this.setScore(score);
		this.setMaxScore(maxScore);
		this.setMinScore(minScore);
		this.setNumIdenticals(numIdenticals);
		this.setNumSimilars(numSimilars);
		this.setAlignmentLength(alignmentLength);
		this.setQueryLength(queryLength);
		this.setTargetLength(targetLength);
		this.setCoverageQuery(coverageQuery);
		this.setCoverageTarget(coverageTarget);
		this.setGapsQuery(gapsQuery);
		this.setGapsTarget(gapsTarget); 
		this.setMethod(method);
		this.setMatrix(matrix);
		this.setAlignmentScoreType(alignmentScoreType);
	}
	
	/**
	 * @param query
	 * @param coverageQuery
	 * @param coverageTarget
	 * @param target
	 * @param score
	 * @param ecNumber
	 * @param closestOrthologues
	 * @param modules
	 */
	public AlignmentContainer(String query, double coverageQuery, double coverageTarget, String target, double score, String ecNumber, 
			Map<String, Set<String>> closestOrthologues, Map<String, Set<Integer >> modules){
		
		super();
		this.setQuery(query);
		this.setScore(score);
		this.setCoverageQuery(coverageQuery);
		this.setCoverageTarget(coverageTarget);
		this.setTarget(target);
		this.setEcNumber(ecNumber);
		this.setClosestOrthologues(closestOrthologues);
		this.setModules(modules);
	}
	
	
	/**
	 * @param query
	 * @param queryLocus
	 * @param target
	 * @param tcdbID
	 * @param matrix
	 * @param score
	 */
	public AlignmentContainer(String query, String target, String tcdbID, String matrix, double score){
		
		super();
		this.setQuery(query);
		this.setQueryLocus(queryLocus);
		this.setTarget(target);
		this.setTcdbID(tcdbID);
		this.setMatrix(matrix);
		this.setScore(score);
	}
	
	/**
	 * @return the alignmentScoreType
	 */
	public AlignmentScoreType getAlignmentScoreType() {
		return alignmentScoreType;
	}

	/**
	 * @param alignmentScoreType the alignmentScoreType to set
	 */
	public void setAlignmentScoreType(AlignmentScoreType alignmentScoreType) {
		this.alignmentScoreType = alignmentScoreType;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	
	/**
	 * @return the tcdb ID
	 */
	public String getTcdbID() {
		return tcdbID;
	}

	/**
	 * @param tcdb ID the tcdb ID to set
	 */
	public void setTcdbID(String tcdbID) {
		this.tcdbID = tcdbID;
	}

	/**
	 * @return the locusTag
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param locusTag the locusTag to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}


	/**
	 * @return the matrix
	 */
	public String getMatrix() {
		return matrix;
	}

	/**
	 * @param matrix the matrix to set
	 */
	public void setMatrix(String matrix) {
		this.matrix = matrix;
	}

	/**
	 * @return the ko
	 */
	public String getKo() {
		return ko;
	}



	/**
	 * @param ko the ko to set
	 */
	public void setKo(String ko) {
		this.ko = ko;
	}


	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * @return the maxScore
	 */
	public double getMaxScore() {
		return maxScore;
	}

	/**
	 * @param maxScore the maxScore to set
	 */
	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

	/**
	 * @return the minscore
	 */
	public double getMinScore() {
		return minScore;
	}

	/**
	 * @param minscore the minscore to set
	 */
	public void setMinScore(double minscore) {
		this.minScore = minscore;
	}

	/**
	 * @return
	 */
	public double getEvalue() {
		return evalue;
	}

	/**
	 * @param evalue
	 */
	public void setEvalue(double evalue) {
		this.evalue = evalue;
	}

	/**
	 * @return the bitScore
	 */
	public double getBitScore() {
		return bitScore;
	}

	/**
	 * @param bitScore the bitScore to set
	 */
	public void setBitScore(double bitScore) {
		this.bitScore = bitScore;
	}

	/**
	 * @return the queryLenght
	 */
	public int getQueryLength() {
		return queryLength;
	}

	/**
	 * @param queryLenght the queryLenght to set
	 */
	public void setQueryLength(int queryLength) {
		this.queryLength = queryLength;
	}

	/**
	 * @return the targetLength
	 */
	public int getTargetLength() {
		return targetLength;
	}

	/**
	 * @param targetLength the targetLength to set
	 */
	public void setTargetLength(int targetLength) {
		this.targetLength = targetLength;
	}

	/**
	 * @param scoreMatrix
	 */
	public void setScoreMatrix(int[][][] scoreMatrix) {

		this.scoreMatrix = scoreMatrix;
	}

	/**
	 * @return
	 */
	public int[][][] getScoreMatrix() {

		return this.scoreMatrix;
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(Method method) {
		this.method = method;
	}

	/**
	 * @return the queryLocus
	 */
	public String getQueryLocus() {
		return queryLocus;
	}

	/**
	 * @param queryLocus the queryLocus to set
	 */
	public void setQueryLocus(String queryLocus) {
		this.queryLocus = queryLocus;
	}

	/**
	 * @return the targetLocus
	 */
	public String getTargetLocus() {
		return targetLocus;
	}

	/**
	 * @param targetLocus the targetLocus to set
	 */
	public void setTargetLocus(String targetLocus) {
		this.targetLocus = targetLocus;
	}

	/**
	 * @return the numIdenticals
	 */
	public int getNumIdenticals() {
		return numIdenticals;
	}

	/**
	 * @param numIdenticals the numIdenticals to set
	 */
	public void setNumIdenticals(int numIdenticals) {
		this.numIdenticals = numIdenticals;
	}

	/**
	 * @return the numSimilars
	 */
	public int getNumSimilars() {
		return numSimilars;
	}

	/**
	 * @param numSimilars the numSimilars to set
	 */
	public void setNumSimilars(int numSimilars) {
		this.numSimilars = numSimilars;
	}

	/**
	 * @return the alignmentLength
	 */
	public int getAlignmentLength() {
		return alignmentLength;
	}

	/**
	 * @param alignmentLength the alignmentLength to set
	 */
	public void setAlignmentLength(int alignmentLength) {
		this.alignmentLength = alignmentLength;
	}

	/**
	 * Get overall alignment score.
	 * 
	 * @return double alignment score
	 */
	public double getAlignmentScore(){

		return (this.getScore()-this.getMinScore())/(this.getMaxScore()-this.getMinScore());
	}

	/**
	 * Get overall similarity score.
	 * 
	 * @return double similarity score
	 */
	public double getSimilarityScore(){

		return ((double)this.getNumSimilars()/(double)this.getAlignmentLength());
	}

	/**
	 * Get overall identity score.
	 * 
	 * @return double identity score
	 */
	public double getIdentityScore(){

		return ((double)this.getNumIdenticals()/(double)this.getAlignmentLength());
	}

	/**
	 * @return the coverageQuery
	 */
	public double getCoverageQuery() {
		return coverageQuery;
	}

	/**
	 * @param coverageQuery the coverageQuery to set
	 */
	public void setCoverageQuery(double coverageQuery) {
		this.coverageQuery = coverageQuery;
	}

	/**
	 * @return the coverageTarget
	 */
	public double getCoverageTarget() {
		return coverageTarget;
	}

	/**
	 * @param coverageTarget the coverageTarget to set
	 */
	public void setCoverageTarget(double coverageTarget) {
		this.coverageTarget = coverageTarget;
	}

	/**
	 * @return the gapsQuery
	 */
	public int getGapsQuery() {
		return gapsQuery;
	}

	/**
	 * @param gapsQuery the gapsQuery to set
	 */
	public void setGapsQuery(int gapsQuery) {
		this.gapsQuery = gapsQuery;
	}

	/**
	 * @return the gapsTarget
	 */
	public int getGapsTarget() {
		return gapsTarget;
	}

	/**
	 * @param gapsTarget the gapsTarget to set
	 */
	public void setGapsTarget(int gapsTarget) {
		this.gapsTarget = gapsTarget;
	}

	/**
	 * @return the ecNumber
	 */
	public String getEcNumber() {
		return ecNumber;
	}

	/**
	 * @param ecNumber the ecNumber to set
	 */
	public void setEcNumber(String ecNumber) {
		this.ecNumber = ecNumber;
	}

	/**
	 * @return the closestOrthologues
	 */
	public Map<String, Set<String>> getClosestOrthologues() {
		return closestOrthologues;
	}

	/**
	 * @param closestOrthologues the closestOrthologues to set
	 */
	public void setClosestOrthologues(Map<String, Set<String>> closestOrthologues) {
		this.closestOrthologues = closestOrthologues;
	}

	/**
	 * @return the modules
	 */
	public Map<String, Set<Integer>> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(Map<String, Set<Integer >> modules) {
		this.modules = modules;
	}
}
