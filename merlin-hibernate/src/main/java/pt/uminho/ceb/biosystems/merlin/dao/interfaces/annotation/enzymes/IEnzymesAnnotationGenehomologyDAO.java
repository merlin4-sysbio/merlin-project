package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologyStatus;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


public interface IEnzymesAnnotationGenehomologyDAO extends IGenericDao<EnzymesAnnotationGeneHomology>{

	public void addEnzymesAnnotationGeneHomology(EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology); 
	
	public void addEnzymesAnnotationGeneHomologyList(List<EnzymesAnnotationGeneHomology> enzymesAnnotationGeneHomologyList);
	
	public List<EnzymesAnnotationGeneHomology> getAllEnzymesAnnotationGeneHomology(); 
	
	public EnzymesAnnotationGeneHomology getEnzymesAnnotationGeneHomology(Integer id); 
	
	public void removeEnzymesAnnotationGeneHomology(EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology); 
	
	public void removeEnzymesAnnotationGeneHomologyList(List<EnzymesAnnotationGeneHomology> enzymesAnnotationGeneHomologyList); 
	
	public void updateEnzymesAnnotationGeneHomologyList(List<EnzymesAnnotationGeneHomology> enzymesAnnotationGeneHomologyList); 
	
	public void updateEnzymesAnnotationGeneHomology(EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology);

	public Map<Integer, String> getSKeyAndQuery();

	public Map<String, String> getLocusTagAndGeneByQuery(String query);

	public String getLocusTagbyQuery(String sequence_id);

	public void removeModelGeneHomologyByQuery(String query);

	public Map<Integer, String> getSequenceIds();

	public List<String> getsequenceIdBySKey(Integer skey);

	public void removeGeneHomologyBySKey(Integer skey);

	public Map<Integer, String> getEnzymesAnnotationGenehomologySKeyAndLocusTag();

	public List<EnzymesAnnotationGeneHomology> getAllEnzymesAnnotationGeneHomologyByQueryAndHomologySetupId(
			Integer skey, String query);

	public List<EnzymesAnnotationGeneHomology> getAllEnzymesAnnotationGeneHomologyByQueryAndProgram(String query,
			String program);

	public List<String[]> getHomologyAttributes(String program, String string);

	public List<EnzymesAnnotationGeneHomology> getAllEnzymesAnnotationGeneHomologyByStatusAndProgram(String string,
			String program);

	public Map<String, String> getGeneHomologyQueryAndProgramByStatus(String status);

	public List<String[]> getGeneHomologySKeyANdQueryAndProgramByAttributes(String status, String evalue, String matrix,
			String wordSize, Integer maxNumberOfAlignments);

	public List<String[]> getGeneHomologySKeyANdQueryAndProgramByStatusAndDatabaseId(String string, String databaseID);

	public Map<String, String> getDistinctGeneHomologyLocusTagAndUniprotEcNumber();

	public List<String[]> getGeneHomologyAttributesByStatus2();

	public Map<String, Set<Integer>>  getGeneHomologySkeyAndDatabaseId();

	public Map<Integer, String> getGeneHomologySKeyAndProgramByStatus(String string);

	public Map<String, Integer> getQueryAndSkey();

	Set<Integer> getS_Key();

//	List<String[]> getEnzymesAnnotationGenehomologyData();
//
//	List<String[]> getEnzymesAnnotationGenehomologyData2();

	public List<String[]> getGeneHomologySKeyANdQueryAndProgramByAttributes2(String status, String databaseId);

	public List<String[]> getGeneHomologyAttributesByStatus(String status);

	public List<String[]> getGeneHomologyAttributes();

	public ArrayList<String[]> getHomologyAttributes2(String program, String status);

	public List<String[]> getGeneHomologyAttributesByDifferentAttributes(String status, Float evalue, String matrix,
			String word);

	public Integer getGeneHomologySkey(String query, Integer homologySetupID);

	public Set<Integer> getHomologyGenesSKeyByStatus(HomologyStatus status, String program);

	public Set<String> getHomologyGenesQueryByStatus(HomologyStatus status, String program);

	public Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByAttributes(HomologyStatus status, String program, double evalue,
			String matrix, String wordSize, Integer maxNumberOfAlignments);

	public Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByStatusAndProgramAndDatabaseId(HomologyStatus status,
			String program, String databaseId);
	
	public Set<Integer> getSKeyForAutomaticAnnotation(String blastDatabase);
	
	public List<String[]> getHomologySetupAttributes(String query);

	public Integer getAnnotationHomologySkeyBySequenceId(int sequenceId);


}
