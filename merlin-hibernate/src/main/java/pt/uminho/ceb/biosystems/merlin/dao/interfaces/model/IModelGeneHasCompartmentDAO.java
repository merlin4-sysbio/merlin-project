package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartmentId;



public interface IModelGeneHasCompartmentDAO extends IGenericDao<ModelGeneHasCompartment>{
	
	public void addModelGeneHasCompartment(ModelGeneHasCompartment modelGeneHasCompartment); 
	
	public void addModelGeneHasCompartmentList(List<ModelGeneHasCompartment> modelGeneHasCompartmentList); 
	
	public List<ModelGeneHasCompartment> getAllModelGeneHasCompartment(); 
	
	public ModelGeneHasCompartment getModelGeneHasCompartment(Integer id); 
	
	public void removeModelGeneHasCompartment(ModelGeneHasCompartment modelGeneHasCompartment); 
	
	public void removeModelGeneHasCompartmentList(List<ModelGeneHasCompartment> modelGeneHasCompartmentList); 
	
	public void updateModelGeneHasCompartmentList(List<ModelGeneHasCompartment> modelGeneHasCompartmentList); 
	
	public void updateModelGeneHasCompartment(ModelGeneHasCompartment modelGeneHasCompartment);

	public List<ModelGeneHasCompartment> getAllModelGeneHasCompartmentByGeneId(Integer idGene);

	public Long getModelGeneHasCompartmentDistinctGeneId();

	public List<Integer> getIdByGeneIdAndPrimaryLocation(Integer idGene, boolean primLocation);

	public void insertModelGeneHasCompartment(Integer geneId, Integer compartmentId, boolean primaryLocation, String score);

	public Integer getIdByGeneIdAndCompartmentId(Integer geneId, Integer compartmentId);

	public void removeAllFromModelGeneHasCompartment();

	public void replaceCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace);

	public List<ModelGeneHasCompartmentId> getEqualGeneCompartments(Integer compartmentIdToKeep, Integer compartmentIdToReplace);

	public ModelGeneHasCompartment getModelGeneHasCompartment(ModelGeneHasCompartmentId id);


}
