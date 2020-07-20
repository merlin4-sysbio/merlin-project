package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationScorerConfig;



public interface IEnzymesAnnotationScorerconfigDAO extends IGenericDao<EnzymesAnnotationScorerConfig>{

	public void addEnzymesAnnotationScorerConfig(EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig); 
	
	public void addEnzymesAnnotationScorerConfig(List<EnzymesAnnotationScorerConfig> enzymesAnnotationScorerConfigList); 
	
	public List<EnzymesAnnotationScorerConfig> getAllEnzymesAnnotationScorerConfig(); 
	
	public EnzymesAnnotationScorerConfig getEnzymesAnnotationScorerConfig(Integer id); 
	
	public void removeEnzymesAnnotationScorerConfig(EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig); 
	
	public void removeEnzymesAnnotationScorerConfigList(List<EnzymesAnnotationScorerConfig> enzymesAnnotationScorerConfigList); 
	
	public void updateEnzymesAnnotationScorerConfigList(List<EnzymesAnnotationScorerConfig> enzymesAnnotationScorerConfigList); 
	
	public void updateEnzymesAnnotationScorerConfig(EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig);

	public String getScorerConfigBlastDB();

	public void removeEnzymesAnnotationScorerconfigByDatabase(String blastDatabase);

	public List<EnzymesAnnotationScorerConfig> getAllScorerConfigByBlastDB(String blastDatabase);

	public void updateEnzymesAnnotationScorerconfigByDB(String db);

	public List<String> getDatabaseByBestAlpha(boolean bestAlpha);

	public void updateEnzymesAnnotationLatestByDB(String db);
	
	
	
//	public void updateEnzymesAnnotationAttributesByDB(String db, Float threshold, Float balanceBh, Float beta, Float alpha,
//			Integer minHomologies);
	
	public void insertEnzymesAnnotationScorerConfigAttributes(Float threshold, Float upperThreshold, Float balanceBH, Float alpha, Float beta, Integer minHomologies, String blastDB, Boolean latest, boolean bestAlpha);

	public void updateEnzymesAnnotationAttributesByDB(String db, Float threshold, Float upperThreshold, Float balanceBh,
			Float beta, Float alpha, Integer minHomologies);

	public List<EnzymesAnnotationScorerConfig> getAllEnzymesAnnotationScorerConfigAttributes();

	public void EnzymesAnnotationScorerConfigList(List<EnzymesAnnotationScorerConfig> enzymesAnnotationScorerConfigList);

	public void EnzymesAnnotationScorerConfig(EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig);

	public void deleteAllScorerConfig();
}
