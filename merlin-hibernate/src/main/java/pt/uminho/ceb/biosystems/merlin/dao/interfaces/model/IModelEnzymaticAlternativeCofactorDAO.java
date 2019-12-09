package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEnzymaticAlternativeCofactor;


public interface IModelEnzymaticAlternativeCofactorDAO extends IGenericDao<ModelEnzymaticAlternativeCofactor>{
	
	public void addModelEnzymaticAlternativeCofactor(ModelEnzymaticAlternativeCofactor modelEnzymaticAlternativeCofactor); 
	
	public void addModelEnzymaticAlternativeCofactorList(List<ModelEnzymaticAlternativeCofactor> modelEnzymaticAlternativeCofactorList); 
	
	public List<ModelEnzymaticAlternativeCofactor> getAllModelEnzymaticAlternativeCofactor(); 
	
	public ModelEnzymaticAlternativeCofactor getModelEnzymaticAlternativeCofactor(Integer id); 
	
	public void removeModelEnzymaticAlternativeCofactor(ModelEnzymaticAlternativeCofactor modelEnzymaticAlternativeCofactor); 
	
	public void removeModelEnzymaticAlternativeCofactorList(List<ModelEnzymaticAlternativeCofactor> modelEnzymaticAlternativeCofactorList); 
	
	public void updateModelEnzymaticAlternativeCofactorList(List<ModelEnzymaticAlternativeCofactor> modelEnzymaticAlternativeCofactorList); 
	
	public void updateModelEnzymaticAlternativeCofactor(ModelEnzymaticAlternativeCofactor modelEnzymaticAlternativeCofactor);

}
