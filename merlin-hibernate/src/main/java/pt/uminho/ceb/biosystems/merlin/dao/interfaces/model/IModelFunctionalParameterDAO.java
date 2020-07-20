package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelFunctionalParameter;



public interface IModelFunctionalParameterDAO extends IGenericDao<ModelFunctionalParameter>{

	public void addModelFunctionalParameter(ModelFunctionalParameter modelFunctionalParameter); 
	
	public void addModelFunctionalParameterList(List<ModelFunctionalParameter> modelFunctionalParameterList); 
	
	public List<ModelFunctionalParameter> getAllModelFunctionalParameter(); 
	
	public ModelFunctionalParameter getModelFunctionalParameter(Integer id); 
	
	public void removeModelFunctionalParameter(ModelFunctionalParameter modelFunctionalParameter); 
	
	public void removeModelFunctionalParameterList(List<ModelFunctionalParameter> modelFunctionalParameterList); 
	
	public void updateModelFunctionalParameterList(List<ModelFunctionalParameter> modelFunctionalParameterList); 
	
	public void updateModelFunctionalParameter(ModelFunctionalParameter modelFunctionalParameter);
}
