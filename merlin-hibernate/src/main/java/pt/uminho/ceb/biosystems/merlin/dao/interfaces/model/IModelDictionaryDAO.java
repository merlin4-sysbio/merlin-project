package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelDictionary;


public interface IModelDictionaryDAO extends IGenericDao<ModelDictionary> {
	
	public void addModelDictionary(ModelDictionary modelDictionary); 
	
	public void addModelDictionaryList(List<ModelDictionary> modelDictionaryList); 
	
	public List<ModelDictionary> getAllModelDictionary(); 
	
	public ModelDictionary getModelDictionary(Integer id); 
	
	public void removeModelDictionary(ModelDictionary modelDictionary); 
	
	public void removeModelDictionaryList(List<ModelDictionary> modelDictionaryList); 
	
	public void updateModelDictionaryList(List<ModelDictionary> modelDictionaryList); 
	
	public void updateModelDictionary(ModelDictionary modelDictionary);
}
