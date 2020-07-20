package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelMetabolicRegulation;

public interface IModelMetabolicRegulationDAO extends IGenericDao<ModelMetabolicRegulation>{

	public void addModelMetabolicRegulation(ModelMetabolicRegulation modelMetabolicRegulation); 
	
	public void addModelMetabolicRegulationList(List<ModelMetabolicRegulation> modelMetabolicRegulation); 
	
	public List<ModelMetabolicRegulation> getAllModelMetabolicRegulation(); 
	
	public ModelMetabolicRegulation getModelMetabolicRegulation(Integer id); 
	
	public void removeModelMetabolicRegulation(ModelMetabolicRegulation modelMetabolicRegulation); 
	
	public void removeModelMetabolicRegulationList(List<ModelMetabolicRegulation> modelMetabolicRegulation); 
	
	public void updateModelMetabolicRegulationList(List<ModelMetabolicRegulation> modelMetabolicRegulation); 
	
	public void updateModelMetabolicRegulation(ModelMetabolicRegulation modelMetabolicRegulation);

}
