package pt.uminho.ceb.biosystems.merlin.dao.interfaces.experimental;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalInhibitor;



public interface IExperimentalInhibitorDAO extends IGenericDao<ExperimentalInhibitor>{

	public void addExperimentalInhibitor(ExperimentalInhibitor ExperimentalInhibitor); 
	
	public void addExperimentalInhibitorList(List<ExperimentalInhibitor> ExperimentalInhibitorList); 
	
	public List<ExperimentalInhibitor> getAllExperimentalInhibitor(); 
	
	public ExperimentalInhibitor getExperimentalInhibitor(Integer id); 
	
	public void removeExperimentalInhibitor(ExperimentalInhibitor ExperimentalInhibitor); 
	
	public void removeExperimentalInhibitorList(List<ExperimentalInhibitor> ExperimentalInhibitorList); 
	
	public void updateExperimentalInhibitorList(List<ExperimentalInhibitor> ExperimentalInhibitorList); 
	
	public void updateExperimentalInhibitor(ExperimentalInhibitor ExperimentalInhibitor);
}
