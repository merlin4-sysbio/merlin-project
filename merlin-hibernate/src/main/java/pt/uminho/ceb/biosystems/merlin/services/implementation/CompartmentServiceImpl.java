package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.compartments.ICompartmentsAnnotationReportsHasCompartmentsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelCompartmentDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneHasCompartmentDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelStoichiometryDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartmentId;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.ICompartmentService;


public class CompartmentServiceImpl implements ICompartmentService{


	private IModelCompartmentDAO modelcompartment;
	private ICompartmentsAnnotationReportsHasCompartmentsDAO reportsHasCompartment; 
	private IModelGeneHasCompartmentDAO modelGeneHasCompartmentDAO;
	private IModelReactionDAO modelReactionDAO;
	private IModelStoichiometryDAO modelStoichiometryDAO;

	public CompartmentServiceImpl(IModelCompartmentDAO modelcompartment,
			ICompartmentsAnnotationReportsHasCompartmentsDAO reportsHasCompartment, 
			IModelGeneHasCompartmentDAO modelGeneHasCompartmentDAO, IModelReactionDAO modelReactionDAO,
			IModelStoichiometryDAO modelStoichiometryDAO) {
		
		this.modelcompartment = modelcompartment;
		this.reportsHasCompartment = reportsHasCompartment;
		this.modelGeneHasCompartmentDAO = modelGeneHasCompartmentDAO;
		this.modelReactionDAO = modelReactionDAO;
		this.modelStoichiometryDAO = modelStoichiometryDAO;
	}

	private static CompartmentContainer buildContainer(ModelCompartment compartment) {

		CompartmentContainer container = null;
		if(compartment != null) {
			container = new CompartmentContainer(compartment.getIdcompartment());

			container.setAbbreviation(compartment.getAbbreviation());
			container.setName(compartment.getName());
		}
		return container;
	}

	@Override
	public Map<Integer, String> getIdCompartmentAbbMap() throws Exception {
		return this.modelcompartment.getModelCompartmentIdAndAbbreviation();
	}

	@Override
	public Map<Integer, String> getModelCompartmentIdAndName() throws Exception {
		return this.modelcompartment.getModelCompartmentIdAndName();
	}

	@Override
	public Set<String> getCompartments() throws Exception {
		return this.modelcompartment.getModelCompartmentNames();
	}

	@Override
	public boolean areCompartmentsPredicted() throws Exception {
		return this.reportsHasCompartment.checkIfIsFilled();
	}

	@Override
	public List<String[]> getCompartmentsAnnotationReportsHasCompartmentsNameAndScore(Integer id) {
		return this.reportsHasCompartment.getCompartmentsAnnotationReportsHasCompartmentsNameAndScore(id);
	}

	@Override
	public void initCompartments(Map<String, String> compartments) throws Exception {
		for(String abbrev : compartments.keySet()) {
			List<ModelCompartment> list = this.modelcompartment.getAllModelCompartmentByAbbreviationName(compartments.get(abbrev)+"'");

			if (list == null) {
				this.modelcompartment.insertNameAndAbbreviation(compartments.get(abbrev), abbrev.toUpperCase()+"')");
			}
		}
	}

	@Override
	public CompartmentContainer findCompartmentByName(String name) throws Exception {

		ModelCompartment compartment = this.modelcompartment.getCompartmentByCompartmentName(name);

		return buildContainer(compartment);
	}

	@Override
	public CompartmentContainer findCompartmentByAbbreviation(String abbreviation) throws Exception {

		ModelCompartment compartment = this.modelcompartment.getCompartmentByCompartmentAbbreviation(abbreviation);

		return buildContainer(compartment);
	}

	@Override
	public CompartmentContainer findCompartmentById(Integer id) throws Exception {

		ModelCompartment compartment = this.modelcompartment.getModelCompartment(id);

		return buildContainer(compartment);
	}

	@Override
	public int getCompartmentID(String name) throws Exception {
		int idCompartment = -1;
		ModelCompartment res = this.modelcompartment.getCompartmentByCompartmentName(name);

		if (res != null)
			idCompartment = res.getIdcompartment();
		return idCompartment;
	}


	@Override
	public ModelCompartment getCompartment(String localisation) throws Exception {

		return this.modelcompartment.getCompartmentByCompartmentName(localisation);
	}

	@Override
	public int selectCompartmentID(String compartment, String abbreviation) throws Exception {
		Integer res = this.modelcompartment.getCompartmentIdByCompartmentNameAndAbbreviation(compartment, abbreviation);

		if (res == null)
			res = this.modelcompartment.insertNameAndAbbreviation(compartment, abbreviation);

		return res;
	}

	@Override
	public List<CompartmentContainer> getCompartmentsInfo() throws Exception {
		return this.modelcompartment.getModelCompartmentIdAndNameAndAbbreviation();
	}

	@Override
	public Map<String, Integer> getCompartmentAbbIdMap() throws Exception {
		Map<String,Integer> idCompartmentAbbIdMap = new HashMap<String,Integer>();

		Map<Integer, String> res = this.modelcompartment.getModelCompartmentIdAndAbbreviation();

		if (res != null)
			for (Integer x : res.keySet())
				idCompartmentAbbIdMap.put(res.get(x).toLowerCase(), x);

		return idCompartmentAbbIdMap;
	}

	@Override
	public Integer insertNameAndAbbreviation(String name, String abb) throws Exception{
		return this.modelcompartment.insertNameAndAbbreviation(name, abb);
	}

	@Override
	public List<ModelCompartment> getAllEntityModelCompartments() throws JAXBException{
		ModelCompartment res = this.modelcompartment.getAllModelCompartment().get(0);
		JAXBContext jaxbcontext = JAXBContext.newInstance(res.getClass());
		Marshaller jaxbMarshaller = jaxbcontext.createMarshaller();
		jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
		jaxbMarshaller.marshal( res, System.out );
		return null;
	}

	@Override
	public List<Object[]> getReactantsOrProductsInCompartment(Boolean isCompartimentalized, Boolean Reactants, Boolean Products) throws Exception{
		return this.modelcompartment.getReactantsOrProductsInCompartment(isCompartimentalized, Reactants, Products);
	}

	@Override
	public ArrayList<String[]> getCompartmentDataByName(String name) throws Exception{
		return this.modelcompartment.getCompartmentDataByName(name);
	}

	@Override
	public Map<String, Set<Integer>> getCompartIdAndEcNumbAndProtId() throws Exception{
		return this.modelcompartment.getCompartIdAndEcNumbAndProtId();
	}

	@Override
	public Integer getCompartmentIdByNameAndAbbreviation(String name, String abb) throws Exception{

		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("name", name);
		dic.put("abbreviation", abb);

		ModelCompartment comp = this.modelcompartment.findUniqueByAttributes(dic);

		if(comp == null)
			return null;

		return comp.getIdcompartment();
	}

	@Override
	public void deleteCompartment(Integer compartmentId) {
		
		ModelCompartment compartment = this.modelcompartment.getModelCompartment(compartmentId);
		
		
		if(compartment != null)
			this.modelcompartment.removeModelCompartment(compartment);
		
	}

	@Override
	public void replaceCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace) throws Exception {
		
		
		List<ModelGeneHasCompartmentId> equalGeneId = this.modelGeneHasCompartmentDAO.getEqualGeneCompartments(
				compartmentIdToKeep, compartmentIdToReplace);
		
		for (ModelGeneHasCompartmentId geneId: equalGeneId) {
			
			ModelGeneHasCompartment geneToReplace= modelGeneHasCompartmentDAO.getModelGeneHasCompartment(geneId);
			this.modelGeneHasCompartmentDAO.delete(geneToReplace);
		}
		
		modelGeneHasCompartmentDAO.replaceCompartment(compartmentIdToKeep, compartmentIdToReplace);
		
	}
	
	public void removeCompartmentsNotInUse() throws Exception{

		List<ModelCompartment> compartments = this.modelcompartment.getAllModelCompartment();

		for(ModelCompartment compartment : compartments) {

			if(!compartment.getAbbreviation().equalsIgnoreCase("in") 
				&& !compartment.getAbbreviation().equalsIgnoreCase("out") 
				&& compartment.getModelGeneHasCompartments().isEmpty() 
				&& compartment.getModelStoichiometries().isEmpty()
				&& compartment.getModelReactions().isEmpty())
				this.modelcompartment.removeModelCompartment(compartment);
		}

	}
}

