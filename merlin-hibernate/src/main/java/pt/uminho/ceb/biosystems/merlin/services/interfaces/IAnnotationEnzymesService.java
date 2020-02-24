package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologyStatus;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResults;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public interface IAnnotationEnzymesService {

	public List<String[]> getProgram(String query) throws Exception;

	public List<String[]> getProductRankData(String locus) throws Exception;

	public String getLastestUsedBlastDatabase() throws Exception;

	public List<String[]> getCommittedHomologyData() throws Exception;

	public boolean productListHasKey(Integer s_key) throws Exception;

	public boolean ecNumberListHasKey(Integer s_key) throws Exception;

	public List<String[]> getDataFromecNumberRank() throws Exception;

	public List<String[]> getEcRank() throws Exception;

	public Integer getMaxTaxRank() throws Exception;

	public List<String[]> getProductRank() throws Exception;

	public List<String[]> getTaxRank() throws Exception;

	//	public List<String[]> getProductRank2() throws Exception;

	public boolean checkHomologuesHasEcNumber(int homologues_s_key, int ecnumber_s_key) throws Exception;

	public int getSKeyFromOrganism(String organism) throws Exception;

	public int getecNumberSkey(String ecNumber) throws Exception;

	public int getProductRankSkey(int geneHomology_s_key, String name, Integer rank) throws Exception;

	public int getProductRankHasOrganismSkey(int prodKey, int orgKey) throws Exception;

	public String[] getAllOrganisms() throws Exception;

	public String[] getAllGenus() throws Exception;

	public Double getBlastEValue(String database) throws Exception;

	public void resetDatabaseScorer(String blastDatabase) throws Exception;

	public List<String> getCommitedScorerData(String blastDatabase) throws Exception;

	public void setBestAlphaFound(String blastDatabase) throws Exception;

	public List<String> bestAlphasFound(boolean bestAlpha) throws Exception;

	public void setLastestUsedBlastDatabase(String latestDB) throws Exception;

	public int getEcNumberRankSkey(int geneHomology_s_key, String concatEC, int ecnumber) throws Exception;

	public void loadProductRank(Map<String, Integer> pd, Integer geneHomology_s_key, Map<String, List<Integer>> prodOrg) throws Exception;

	public List<String[]> getEnzymesAnnotationEcNumberrankAttributesByLocusTag(String query) throws Exception;

	public Integer insertEcNumberEntry(String ecNumber) throws Exception;

	public Integer insertEcNumberRank(Integer geneHomology_s_key, String concatEC, Integer ecnumber) throws Exception;

	public Boolean FindEnzymesAnnotationEcNumberRankHasOrganismByIds(Integer sKey, Integer orgKey) throws Exception;
	
	public void InsertEnzymesAnnotationEcNumberRankHasOrganism(Integer sKey, Integer orgKey) throws Exception;

	public Integer getGeneHomologySkey(String query, Integer homologySetupID) throws Exception;

	public Integer insertEnzymesAnnotationOrganism(String organism, String taxonomy, Integer taxrank) throws Exception;

	public Integer getHomologuesSkey(String referenceID) throws Exception;

	public void insertEnzymesAnnotationHomologuesHasEcNumber(int homologues_s_key, int ecnumber_s_key) throws Exception;

	public int getHomologySetupSkeyByAttributes(String databaseID, String program, double eVal, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version) throws Exception;

	public Integer insertHomologySetup(String databaseID, String program, double eVal, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version) throws Exception;

	public int getHomologySetupSkeyByAttributes2(String databaseID, String program, double eVal, int maxNumberOfAlignments,
			String version) throws Exception;

	public Integer insertHomologySetup2(String databaseID, String program, double eVal, int maxNumberOfAlignments,
			String version) throws Exception;

	public void removeGeneHomologyBySKey(Integer skey) throws Exception;

	public void removeGeneHomologyByQuery(String query) throws Exception;

	public Map<Integer, String> getQueriesBySKey() throws Exception;

	public Map<String, Integer> getQueries() throws Exception;

	public Map<String, String> loadGeneLocusFromHomologyData(String query) throws Exception;

	public String getLocusTagFromHomologyData(String sequence_id) throws Exception;

	public List<List<String>> getHomologySetup(Integer skey) throws Exception;

	public String getHomologySequence(Integer skey) throws Exception;

	public List<InterproResults> getInterproAvailability(Integer skey) throws Exception;

	public Map<String, Boolean> getHomologyAvailabilities(Integer skey) throws Exception;

	public Map<Integer, String> getLocusKeys() throws Exception;

	public void deleteDuplicatedQuerys(String query) throws Exception;

	public Map<Integer, String> getDatabaseLocus() throws Exception;

	public void deleteSetOfGenes(Set<Integer> deleteGeneslist) throws Exception;

	public boolean loadGeneHomologyData(String query, String program) throws Exception;

	public Integer getGeneHomologySkey(Integer skey, String query) throws Exception;

	public void load_geneHomology_has_homologues(String referenceID, String gene, Float eValue, Float bits,
			Integer geneHomology_s_key, Integer homologues_s_key, Float identity, Float positives, Float queryCoverage, Float targetCoverage) throws Exception;

	public GeneContainer getGeneHomologyEntryByQuery(String query) throws Exception;

	public List<String> getDuplicatedQuerys() throws Exception;

	public void resetAllScorers() throws Exception;

	public void updateScorerConfigSetLatest(boolean latest) throws Exception;

	public void updateScorerConfigSetLatestByBlastDatabase(boolean latest, String blastDatabase) throws Exception;

	public Set<Integer> getHomologyGenesSKeyByStatus(HomologyStatus status, String program) throws Exception;

	public Set<String> getHomologyGenesQueryByStatus(HomologyStatus status, String program) throws Exception;

	public Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByAttributes(HomologyStatus status, String program,
			double evalue, String matrix, String wordSize, Integer maxNumberOfAlignments) throws Exception;

	public Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByStatusAndProgramAndDatabaseId(HomologyStatus status,
			String program, String databaseId) throws Exception;

	public void deleteFromHomologyDataByDatabaseID(String database) throws Exception;

	public Integer insertHomologyData(Integer geneHomologySKey, String locusTag, String geneName,
			String product, String ecnumber, Boolean selected, String chromossome, String notes) throws Exception;

	public Set<Integer> getSKeyForAutomaticAnnotation(String blastDatabase) throws Exception;

	public Set<String> getAllBlastDatabases() throws Exception;

	public boolean deleteGenesWithoutECRankAndProdRank() throws Exception;

}
