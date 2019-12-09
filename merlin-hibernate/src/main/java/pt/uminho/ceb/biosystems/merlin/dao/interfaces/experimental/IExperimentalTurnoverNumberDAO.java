package pt.uminho.ceb.biosystems.merlin.dao.interfaces.experimental;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalTurnoverNumber;


public interface IExperimentalTurnoverNumberDAO extends IGenericDao<ExperimentalTurnoverNumber>{
	
	public void addExperimentalTurnoverNumber(ExperimentalTurnoverNumber ExperimentalTurnoverNumber); 
	
	public void addExperimentalTurnoverNumberList(List<ExperimentalTurnoverNumber> ExperimentalTurnoverNumberList); 
	
	public List<ExperimentalTurnoverNumber> getAllExperimentalTurnoverNumber(); 
	
	public ExperimentalTurnoverNumber getExperimentalTurnoverNumber(Integer id); 
	
	public void removeExperimentalTurnoverNumber(ExperimentalTurnoverNumber ExperimentalTurnoverNumber); 
	
	public void removeExperimentalTurnoverNumberList(List<ExperimentalTurnoverNumber> ExperimentalTurnoverNumberList); 
	
	public void updateExperimentalTurnoverNumberList(List<ExperimentalTurnoverNumber> ExperimentalTurnoverNumberList); 
	
	public void updateExperimentalTurnoverNumber(ExperimentalTurnoverNumber ExperimentalTurnoverNumber);
}
