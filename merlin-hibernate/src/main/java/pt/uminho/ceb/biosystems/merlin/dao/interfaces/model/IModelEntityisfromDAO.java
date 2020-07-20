package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEntityisfrom;

public interface IModelEntityisfromDAO extends IGenericDao<ModelEntityisfrom>{
	
	public void addModelEntityisfrom(ModelEntityisfrom modelEntityisfrom); 
	
	public void addModelEntityisfromList(List<ModelEntityisfrom> modelEntityisfromList); 
	
	public List<ModelEntityisfrom> getAllModelEntityisfrom(); 
	
	public ModelEntityisfrom getModelEntityisfrom(Integer id); 
	
	public void removeModelEntityisfrom(ModelEntityisfrom modelEntityisfrom); 
	
	public void removeModelEntityisfromList(List<ModelEntityisfrom> modelEntityisfromList); 
	
	public void updateModelEntityisfromist(List<ModelEntityisfrom> modelEntityisfromList); 
	
	public void updateModelEntityisfrom(ModelEntityisfrom modelEntityisfrom);
}
