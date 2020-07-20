package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubstrateAffinity;



public interface IModelSubstrateAffinityDAO extends IGenericDao<ModelSubstrateAffinity>{

	public void addModelSubstrateAffinity(ModelSubstrateAffinity modelSubstrateAffinity); 
	
	public void addModelSubstrateAffinityList(List<ModelSubstrateAffinity> modelSubstrateAffinityList); 
	
	public List<ModelSubstrateAffinity> getAllModelSubstrateAffinity(); 
	
	public ModelSubstrateAffinity getModelSubstrateAffinity(Integer id); 
	
	public void removeModelSubstrateAffinity(ModelSubstrateAffinity modelSubstrateAffinity); 
	
	public void removeModelSubstrateAffinityList(List<ModelSubstrateAffinity> modelSubstrateAffinityList); 
	
	public void updateModelSubstrateAffinityList(List<ModelSubstrateAffinity> modelSubstrateAffinityList); 
	
	public void updateModelSubstrateAffinity(ModelSubstrateAffinity modelSubstrateAffinity);
}
