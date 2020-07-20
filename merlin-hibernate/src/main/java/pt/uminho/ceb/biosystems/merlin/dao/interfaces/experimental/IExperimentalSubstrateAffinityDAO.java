package pt.uminho.ceb.biosystems.merlin.dao.interfaces.experimental;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalSubstrateAffinity;



public interface IExperimentalSubstrateAffinityDAO extends IGenericDao<ExperimentalSubstrateAffinity>{

	public void addExperimentalSubstrateAffinity(ExperimentalSubstrateAffinity ExperimentalSubstrateAffinity); 
	
	public void addEnzymesAnnotationEcNumberList(List<ExperimentalSubstrateAffinity> ExperimentalSubstrateAffinityList); 
	
	public List<ExperimentalSubstrateAffinity> getAllExperimentalSubstrateAffinity(); 
	
	public ExperimentalSubstrateAffinity getExperimentalSubstrateAffinity(Integer id); 
	
	public void removeExperimentalSubstrateAffinity(ExperimentalSubstrateAffinity ExperimentalSubstrateAffinity); 
	
	public void removeExperimentalSubstrateAffinityList(List<ExperimentalSubstrateAffinity> ExperimentalSubstrateAffinityList); 
	
	public void updateExperimentalSubstrateAffinityList(List<ExperimentalSubstrateAffinity> ExperimentalSubstrateAffinityList); 
	
	public void updateExperimentalSubstrateAffinity(ExperimentalSubstrateAffinity ExperimentalSubstrateAffinity);
}
