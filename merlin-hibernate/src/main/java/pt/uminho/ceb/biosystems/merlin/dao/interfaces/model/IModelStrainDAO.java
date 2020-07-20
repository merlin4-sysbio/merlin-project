package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStrain;



public interface IModelStrainDAO extends IGenericDao<ModelStrain>{

	public void addModelStrain(ModelStrain modelStrain); 
	
	public void addModelStrainList(List<ModelStrain> modelStrainList); 
	
	public List<ModelStrain> getAllModelStrain(); 
	
	public ModelStrain getModelStrain(Integer id); 
	
	public void removeModelStrain(ModelStrain modelStrain); 
	
	public void removeModelStrainList(List<ModelStrain> modelStrainList); 
	
	public void updateModelStrainList(List<ModelStrain> modelStrainList); 
	
	public void updateModelStrain(ModelStrain modelStrain);
}
