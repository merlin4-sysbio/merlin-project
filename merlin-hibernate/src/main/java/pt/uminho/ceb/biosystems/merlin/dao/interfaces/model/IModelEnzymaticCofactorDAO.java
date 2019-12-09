package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEnzymaticCofactor;


public interface IModelEnzymaticCofactorDAO extends IGenericDao<ModelEnzymaticCofactor>{

	public void addModelEnzymaticCofactor(ModelEnzymaticCofactor modelEnzymaticCofactor); 
	
	public void addModelEnzymaticCofactorList(List<ModelEnzymaticCofactor> modelEnzymaticCofactorList); 
	
	public List<ModelEnzymaticCofactor> getAllModelEnzymaticCofactor(); 
	
	public ModelEnzymaticCofactor getModelEnzymaticCofactor(Integer id); 
	
	public void removeModelEnzymaticCofactor(ModelEnzymaticCofactor modelEnzymaticCofactor); 
	
	public void removeModelEnzymaticCofactorList(List<ModelEnzymaticCofactor> modelEnzymaticCofactorList); 
	
	public void updateModelEnzymaticCofactorList(List<ModelEnzymaticCofactor> modelEnzymaticCofactorList); 
	
	public void updateModelEnzymaticCofactor(ModelEnzymaticCofactor modelEnzymaticCofactor);

	public boolean getModelEnzymaticCofactorProteinIdByattributes(Integer protID, Integer compoundID);
}
