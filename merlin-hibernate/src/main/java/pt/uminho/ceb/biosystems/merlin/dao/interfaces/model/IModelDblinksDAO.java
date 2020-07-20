package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelDblinks;



public interface IModelDblinksDAO extends IGenericDao<ModelDblinks>{
	
	public void addModelDblinks(ModelDblinks modelDblinks); 
	
	public void addModelDblinksList(List<ModelDblinks> modelDblinksList); 
	
	public List<ModelDblinks> getAllModelDblinks(); 
	
	public ModelDblinks getModelDblinks(Integer id); 
	
	public void removeModelDblinks(ModelDblinks modelDblinks); 
	
	public void removeModelDblinksList(List<ModelDblinks> modelDblinksList); 
	
	public void updateModelDblinksList(List<ModelDblinks> modelDblinksList); 
	
	public void updateModelDblinks(ModelDblinks modelDblinks);

	public List<ModelDblinks> getAllModelDblinksByAttributes(String class_, Integer int_id, String ext_db);

}
