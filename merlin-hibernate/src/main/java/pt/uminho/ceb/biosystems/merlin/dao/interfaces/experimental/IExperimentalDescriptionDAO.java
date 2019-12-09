package pt.uminho.ceb.biosystems.merlin.dao.interfaces.experimental;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalDescription;



public interface IExperimentalDescriptionDAO extends IGenericDao<ExperimentalDescription>{

	public void addExperimentalDescription(ExperimentalDescription ExperimentalDescription); 
	
	public void addExperimentalDescriptionList(List<ExperimentalDescription> ExperimentalDescriptionList); 
	
	public List<ExperimentalDescription> getAllExperimentalDescription(); 
	
	public ExperimentalDescription getExperimentalDescription(Integer id); 
	
	public void removeExperimentalDescription(ExperimentalDescription ExperimentalDescription); 
	
	public void removeExperimentalDescriptionList(List<ExperimentalDescription> ExperimentalDescriptionList); 
	
	public void updateExperimentalDescriptionList(List<ExperimentalDescription> ExperimentalDescriptionList); 
	
	public void updateExperimentalDescription(ExperimentalDescription ExperimentalDescription);
}
