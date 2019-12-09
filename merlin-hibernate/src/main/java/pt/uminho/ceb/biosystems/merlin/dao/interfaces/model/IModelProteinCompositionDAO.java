package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProteinComposition;


public interface IModelProteinCompositionDAO  extends IGenericDao<ModelProteinComposition>{
	
	public void addModelProteinComposition(ModelProteinComposition modelProteinComposition); 
	
	public void addModelProteinCompositionList(List<ModelProteinComposition> modelProteinCompositionList); 
	
	public List<ModelProteinComposition> getAllModelProteinComposition(); 
	
	public ModelProteinComposition getModelProteinComposition(Integer id); 
	
	public void removeModelProteinComposition(ModelProteinComposition modelProteinComposition); 
	
	public void removeModelProteinCompositionList(List<ModelProteinComposition> modelProteinCompositionList); 
	
	public void updateModelProteinCompositionList(List<ModelProteinComposition> modelProteinCompositionList); 
	
	public void updateModelProteinComposition(ModelProteinComposition modelProteinComposition);

	public Long getModelCompositionDistinctProteinId();

	public List<String[]> getProteinComposition();

}
