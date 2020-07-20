package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequenceFeature;



public interface IModelSequenceFeatureDAO extends IGenericDao<ModelSequenceFeature>{

	public void addModelSequenceFeature(ModelSequenceFeature modelSequenceFeature); 
	
	public void addModelSequenceFeatureList(List<ModelSequenceFeature> modelSequenceFeatureList); 
	
	public List<ModelSequenceFeature> getAllModelSequenceFeature(); 
	
	public ModelSequenceFeature getModelSequenceFeature(Integer id); 
	
	public void removeModelSequenceFeature(ModelSequenceFeature modelSequenceFeature); 
	
	public void removeModelSequenceFeatureList(List<ModelSequenceFeature> modelSequenceFeatureList); 
	
	public void updateModelSequenceFeatureList(List<ModelSequenceFeature> modelSequenceFeatureList); 
	
	public void updateModelSequenceFeature(ModelSequenceFeature modelSequenceFeature);
}
