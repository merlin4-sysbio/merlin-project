package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.utilities.DatabaseProgressStatus;

public interface IHomologyService {
	
	public String getNcbiBlastDatabase() throws Exception;
	
	public String getHmmerDatabase() throws Exception;
	
	public List<String> getProgramFromHomologySetup(String status) throws Exception;
	
	public List<String[]> getSpecificStats(String program) throws Exception;
	
	public List<Integer> getAllFromGeneHomology(String program) throws Exception;
	
	public List<String> getTaxonomy(String program) throws Exception;
	
	public String geneHomologyHasHomologues(String locus) throws Exception;
	
	public boolean homologyDataHasKey(int key) throws Exception;
	
	public Integer getHomologyDataKey(int key) throws Exception;
	
	public String getSetupProgram() throws Exception;
	
	public List<List<String>> getHomologyResults(int key) throws Exception; 
	
	public List<List<String>> getHomologyTaxonomy(int key) throws Exception;
	
	public List<List<String>> getInterProResult(int key) throws Exception;
	
	public Integer loadInterProResult(String tool, float eValue, float score, String family, String accession,
			String name, String ec, String goName, String localization, String database, int resultsID) throws Exception;
	
	public Integer loadInterProEntry(String accession, String description, String name, String type) throws Exception;
	
	public void loadXrefs(String category, String database, String name, String id, int entryID) throws Exception;
	
	public Integer loadInterProModel(String accession, String name, String description) throws Exception;
	
	public Set<String> getGenesFromDatabase(String eVal, String matrix, Integer numberOfAlignments, String wordSize,
			String program, String databaseID, boolean deleteProcessing) throws Exception;
	
	public Set<String> getGenesFromDatabase(String program, boolean deleteProcessing) throws Exception;
	
	public Map<String, List<String>> getUniprotEcNumbers() throws Exception;
	
	public List<String[]> getCommittedHomologyData2() throws Exception;
	
	public List<String[]> getCommittedHomologyData3() throws Exception;
	
	public Map<Integer,Long> getHomologuesCountByEcNumber() throws Exception;
	
	public Map<Integer,Long> getHomologuesCountByProductRank() throws Exception;
	
	public List<String[]> getGenesInformation() throws Exception;
	
	public Map<String, Set<Integer>>  getGenesPerDatabase() throws Exception;
	
	public String[] getBlastDatabases(String program) throws Exception;
	
	public void deleteHomologyData(String database) throws Exception;
	
	public void insertAutomaticEnzymeAnnotation(Integer homologySkey, String locusTag, String geneName, String product, String ecnumber, boolean selected,
			String chromossome, String notes, Map<Integer, String> locusTag2, Map<Integer, String> geneName2, 
			Map<Integer, String> ecMap, Map<Integer, String> confLevelMap) throws Exception;
	
	public Long getNumberOfHomologueGenes(String program) throws Exception;
	
	public String getEbiBlastDatabase() throws Exception;

	public long countEntriesInGeneHomology() throws Exception;

	public Integer insertGeneHomologyEntry(String locusTag, String uniprotEcnumber, Boolean uniprotStar, int setupId,
			String query, DatabaseProgressStatus status, int seqId, String chromosome, String organelle) throws Exception;

	public void updateGeneHomologyEntry(String locusTag, String uniprotEcnumber, Boolean uniprotStar, int setupId,
			String query, DatabaseProgressStatus status, int seqId, String chromosome, String organelle, int sKey) throws Exception;

	public void updateGeneHomologyStatus(String databaseName, String locusTag, DatabaseProgressStatus status) throws Exception;

	public Integer insertGeneHomologues(int organismsKey, String locusID, String definition, Float calculatedMw, String product,
			String organelle, Boolean uniprotStar) throws Exception;

	public boolean checkCommitedData() throws Exception;
	
	public List<String[]> getHomologyProductsByGeneHomologySkey(Integer skey) throws Exception;
	
	public Integer getAnnotationHomologySkeyBySequenceId(int sequenceId) throws Exception;

}
