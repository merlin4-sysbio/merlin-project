package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelOrthology;



public interface IModelOrthologyDAO extends IGenericDao<ModelOrthology>{

	public void addModelOrthology(ModelOrthology modelOrthology); 
	
	public void addModelOrthologyList(List<ModelOrthology> modelOrthologyList); 
	
	public List<ModelOrthology> getAllModelOrthology(); 
	
	public ModelOrthology getModelOrthology(Integer id); 
	
	public void removeModelOrthology(ModelOrthology modelOrthology); 
	
	public void removeModelOrthologyList(List<ModelOrthology> modelOrthologyList); 
	
	public void updateModelOrthologyList(List<ModelOrthology> modelOrthologyList); 
	
	public void updateModelOrthology(ModelOrthology modelOrthology);

	public List<String> getModelOrthologyLocusIdbyEntryId(String entryId);

	public List<ModelOrthology> getAllModelOrthologyByEntryIdAndLocusId(String entyID, String locusID);

	public List<ModelOrthology> getAllModelOrthologyByEntryId(Integer gene);

	public Integer insertModelOrthologyEntryId(String gene);

	public void updateModelOrthologyLocusIdByEntryId(String entryid, String locusid);

	public Integer getModelOrthologyIdByEntryIdAndLocusId(String entryId, String locusId);

	public Integer insertModelOrthologyEntryIdAndLocusId(String entryid, String locusId);

	public Map<String, Integer> getEntryIdAndOrthologyId();
}
