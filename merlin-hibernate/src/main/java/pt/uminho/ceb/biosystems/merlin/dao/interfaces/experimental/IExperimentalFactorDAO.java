package pt.uminho.ceb.biosystems.merlin.dao.interfaces.experimental;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalFactor;


public interface IExperimentalFactorDAO extends IGenericDao<ExperimentalFactor>{
	
	public void addExperimentalFactor(ExperimentalFactor ExperimentalFactor); 
	
	public void addExperimentalFactorList(List<ExperimentalFactor> ExperimentalFactorList); 
	
	public List<ExperimentalFactor> getAllExperimentalFactor(); 
	
	public ExperimentalFactor getExperimentalFactor(Integer id); 
	
	public void removeExperimentalFactor(ExperimentalFactor ExperimentalFactor); 
	
	public void removeExperimentalFactorList(List<ExperimentalFactor> ExperimentalFactorList); 
	
	public void updateExperimentalFactorList(List<ExperimentalFactor> ExperimentalFactorList); 
	
	public void updateExperimentalFactor(ExperimentalFactor ExperimentalFactor);
}
