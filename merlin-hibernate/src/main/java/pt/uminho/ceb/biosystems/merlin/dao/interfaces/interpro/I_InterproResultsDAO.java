package pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResults;



public interface I_InterproResultsDAO extends IGenericDao<InterproResults>{
	
	public void addInterproResults(InterproResults interproInterproResults); 
	
	public void addInterproResults(List<InterproResults> interproInterproResultsLists); 
	
	public List<InterproResults> getAllInterproResults(); 
	
	public InterproResults getInterproResults(Integer id); 
	
	public void removeInterproResults(InterproResults interproInterproResults); 
	
	public void removeInterproResultsList(List<InterproResults> interproInterproResultsList); 
	
	public void updateInterproResultsList(List<InterproResults> interproInterproResultsList); 
	
	public void updateInterproResults(InterproResults interproInterproResults);

	public List<InterproResults> getAllInterproResultsByQuery(String query);

	public List<String[]> getInterproResultsDataByQuery(String query);

	public List<InterproResults> getAllInterproResultsByStatus(String string);

	public Integer insertInterproResults(String query, String querySequence, String mostLikelyEc,
			String mostLikelyLocalization, String mostLikelyName, String status);

	public void updateInterproResultsStatusById(String status, Integer id);

	public void removeInterproResultsByStatus(String status);

	public List<String> getInterproResultsQueryByStatus(String status);

}
