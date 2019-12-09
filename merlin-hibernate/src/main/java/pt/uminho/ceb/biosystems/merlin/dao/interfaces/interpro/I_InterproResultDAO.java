package pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResult;



public interface I_InterproResultDAO extends IGenericDao<InterproResult>{

	public void addInterproResult(InterproResult interproInterproResult); 
	
	public void addInterproResultList(List<InterproResult> interproInterproResultList); 
	
	public List<InterproResult> getAllInterproResult(); 
	
	public InterproResult getInterproResult(Integer id); 
	
	public void removeInterproResult(InterproResult interproInterproResult); 
	
	public void removeInterproResultList(List<InterproResult> interproInterproResultList); 
	
	public void updateInterproResultList(List<InterproResult> interproInterproResultList); 
	
	public void updateInterproResult(InterproResult interproInterproResult);

	public List<InterproResult> getAllInterproResultByDatabaseAndAccessionAndResultsId(String database, String accession, Integer results);

	public Integer insertInterproResultData(String tool, Float eValue, Float score, String familyName, 
			String accession, String name, String ec, String goName, String localization, String database, Integer resultId);
}
