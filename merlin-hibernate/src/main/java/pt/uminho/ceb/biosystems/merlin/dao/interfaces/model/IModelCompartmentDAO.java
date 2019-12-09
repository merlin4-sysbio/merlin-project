package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;



public interface IModelCompartmentDAO extends IGenericDao<ModelCompartment>{
	
	public void addModelCompartment(ModelCompartment modelCompartment); 
	
	public void addModelCompartmentList(List<ModelCompartment> modelCompartmentList); 
	
	public ModelCompartment getModelCompartment(Integer id); 
	
	public void removeModelCompartment(ModelCompartment modelCompartment); 
	
	public void removeModelCompartmentList(List<ModelCompartment> modelCompartmentList); 
	
	public void updateModelCompartmentList(List<ModelCompartment> modelCompartmentList); 
	
	public void updateModelCompartment(ModelCompartment modelCompartment);

	public Map<Integer, String> getModelCompartmentIdAndAbbreviation();

	public Set<String> getModelCompartmentNames();

	public List<ModelCompartment> getAllModelCompartmentByAbbreviationName(String string);

	public Integer insertNameAndAbbreviation(String name, String abb);

	public ModelCompartment getCompartmentByCompartmentName(String name);

	public List<CompartmentContainer> getModelCompartmentIdAndNameAndAbbreviation();

	public Integer getCompartmentIdByCompartmentNameAndAbbreviation(String compartment, String abbreviation);

	public ModelCompartment getModelCompartmentByCompartmentId(Integer compartmentId);

	public Map<Integer, String> getModelCompartmentIdAndName();

	public List<ModelCompartment> getAllModelCompartment();

	public List<Object[]> getReactantsOrProductsInCompartment(Boolean isCompartimentalized, Boolean Reactants, Boolean Products);

	public ArrayList<String[]> getCompartmentDataByName(String name);

	public Map<String, Set<Integer>> getCompartIdAndEcNumbAndProtId();

	public ModelCompartment getCompartmentByCompartmentAbbreviation(String abbreviation);

	
}
