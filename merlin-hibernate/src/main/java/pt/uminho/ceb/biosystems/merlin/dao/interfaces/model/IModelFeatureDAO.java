package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelFeature;



public interface IModelFeatureDAO extends IGenericDao<ModelFeature>{
	
	public void addModelFeature(ModelFeature modelFeature); 
	
	public void addModelFeatureList(List<ModelFeature> modelFeatureList); 
	
	public List<ModelFeature> getAllModelFeature(); 
	
	public ModelFeature getModelFeature(Integer id); 
	
	public void removeModelFeature(ModelFeature modelFeature); 
	
	public void removeModelFeatureList(List<ModelFeature> modelFeatureList); 
	
	public void updateModelFeatureList(List<ModelFeature> modelFeatureList); 
	
	public void updateModelFeature(ModelFeature modelFeature);

}
