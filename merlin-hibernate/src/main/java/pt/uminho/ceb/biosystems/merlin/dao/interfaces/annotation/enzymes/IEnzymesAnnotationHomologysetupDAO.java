package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologySetup;


public interface IEnzymesAnnotationHomologysetupDAO extends IGenericDao<EnzymesAnnotationHomologySetup>{

	public void addEnzymesAnnotationHomologySetup(EnzymesAnnotationHomologySetup enzymesAnnotationHomologysetup); 
	
	public void addEnzymesAnnotationHomologySetupList(List<EnzymesAnnotationHomologySetup> enzymesAnnotationHomologySetupList); 
	
	public List<EnzymesAnnotationHomologySetup> getAllEnzymesAnnotationHomologysetup(); 
	
	public EnzymesAnnotationHomologySetup getEnzymesAnnotationHomologysetup(Integer id); 
	
	public void removeEnzymesAnnotationHomologysetup(EnzymesAnnotationHomologySetup enzymesAnnotationHomologysetup); 
	
	public void removeEnzymesAnnotationHomologySetupList(List<EnzymesAnnotationHomologySetup> enzymesAnnotationHomologySetupList); 
	
	public void updateEnzymesAnnotationHomologySetupList(List<EnzymesAnnotationHomologySetup> enzymesAnnotationHomologySetupList); 
	
	public void updateEnzymesAnnotationHomologysetup(EnzymesAnnotationHomologySetup enzymesAnnotationHomologysetup);

	public List<String> getModelHomologySetupEvalue();

	public List<String> getModelHomologySetupEvalueByDatabaseId(String database);

	public List<String> getEnzymesAnnotationHomologysetupProgramByQuery(String query);

	public Map<String, String> getModelHomologySetupProgramAndDatabaseId();

	public List<String> getEnzymesAnnotationHomologysetupProgramByStatus(String status);

	public Map<String, String> getDistinctTaxonomyAndOrganismByProgram(String program);

	public String getEnzymesAnnotationHomologysetupDistinctProgram();

	public ArrayList<String[]> getOrganismAndTaxonomyAndEvalue(String query);

	public List<String[]> getGeneHomologySetupAttributesByAttributes(Float evalue, String status, String matrix, String wordSize);

	public Long getEnzymesAnnotationHomologysetupCountDistinctDatabaseId(String program);

	public List<String> getEnzymesAnnotationHomologysetupDistinctDatabaseId();

	public Long getAllEnzymesAnnotationHomologysetupByProgram(String program);

	List<String[]> getHomologyAttributes(String query);

	List<EnzymesAnnotationHomologySetup> getEnzymesAnnotationHomologysetupDataByQuery(String query);

	public int getHomologySetupSkeyByAttributes(String databaseID, String program, double eVal, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version);

	public Integer insertHomologySetup(String databaseID, String program, double eVal, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version);

	public int getHomologySetupSkeyByAttributes2(String databaseID, String program, double eVal, int maxNumberOfAlignments,
			String version);

	public Integer insertHomologySetup2(String databaseID, String program, double eVal, int maxNumberOfAlignments,
			String version);


}