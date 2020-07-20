package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;

public interface ICompartmentService {
	
	public Map<Integer,String> getIdCompartmentAbbMap() throws Exception;
	
	public Set<String> getCompartments() throws Exception;
	
	public boolean areCompartmentsPredicted() throws Exception;
	
	public List<String[]> getCompartmentsAnnotationReportsHasCompartmentsNameAndScore(Integer id) throws Exception;
	
	public void initCompartments(Map<String, String> compartments) throws Exception;
	
	public int getCompartmentID(String localisation) throws Exception;
	
	public ModelCompartment getCompartment(String localisation) throws Exception;
	
	public int selectCompartmentID(String compartment, String abbreviation) throws Exception;
	
	public List<CompartmentContainer> getCompartmentsInfo () throws Exception;
	
	public Map<String,Integer> getCompartmentAbbIdMap() throws Exception;
	
	public Integer insertNameAndAbbreviation(String name, String abb) throws Exception;

	public List<ModelCompartment> getAllEntityModelCompartments() throws JAXBException, Exception;

	public Map<Integer, String> getModelCompartmentIdAndName() throws Exception;

	public CompartmentContainer findCompartmentByName(String name) throws Exception;

	public List<Object[]> getReactantsOrProductsInCompartment(Boolean isCompartimentalized, Boolean Reactants, Boolean Products) throws Exception;

	public ArrayList<String[]> getCompartmentDataByName(String name) throws Exception;
	
	public Map<String, Set<Integer>> getCompartIdAndEcNumbAndProtId() throws Exception;

	public Integer getCompartmentIdByNameAndAbbreviation(String name, String abb) throws Exception;

	public CompartmentContainer findCompartmentByAbbreviation(String abbreviation) throws Exception;

	public CompartmentContainer findCompartmentById(Integer id) throws Exception;

	public void deleteCompartment(Integer compartmentId) throws Exception;

	public void replaceCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace) throws Exception;
	
	public void removeCompartmentsNotInUse() throws Exception;
	
}
