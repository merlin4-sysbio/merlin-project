package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEffector;



public interface IModelEffectorDAO extends IGenericDao<ModelEffector>{
	
	public void addModelEffector(ModelEffector modelEffector); 
	
	public void addModelEffectorList(List<ModelEffector> modelEffectorList); 
	
	public List<ModelEffector> getAllModelEffector(); 
	
	public ModelEffector getModelEffector(Integer id); 
	
	public void removeModelEffector(ModelEffector modelEffector); 
	
	public void removeModelEffectorList(List<ModelEffector> modelEffectorList); 
	
	public void updateModelEffectorList(List<ModelEffector> modelEffectorList); 
	
	public void updateModelEffector(ModelEffector modelEffector);
}
