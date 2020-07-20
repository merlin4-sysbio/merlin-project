package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.compartments;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.CompartmentsAnnotationReportsHasCompartments;


public interface ICompartmentsAnnotationReportsHasCompartmentsDAO extends IGenericDao<CompartmentsAnnotationReportsHasCompartments>{
	
	public void addCompartmentsAnnotationReportsHasCompartment(CompartmentsAnnotationReportsHasCompartments compartmentsAnnotationReportsHasCompartment); 
	
	public void addCompartmentsAnnotationReportsHasCompartments(List<CompartmentsAnnotationReportsHasCompartments> compartmentsAnnotationReportsHasCompartments); 
	
	public List<CompartmentsAnnotationReportsHasCompartments> getAllCompartmentsAnnotationReportsHasCompartments(); 
	
	public CompartmentsAnnotationReportsHasCompartments getCompartmentsAnnotationReportsHasCompartment(Integer id); 
	
	public void removeCompartmentsAnnotationReportsHasCompartment(CompartmentsAnnotationReportsHasCompartments compartmentsAnnotationReportsHasCompartment); 
	
	public void removeCompartmentsAnnotationReportsHasCompartments(List<CompartmentsAnnotationReportsHasCompartments> compartmentsAnnotationReportsHasCompartments); 
	
	public void updateCompartmentsAnnotationReportsHasCompartments(List<CompartmentsAnnotationReportsHasCompartments> compartmentsAnnotationReportsHasCompartments); 
	
	public void updateCompartmentsAnnotationReportsHasCompartment(CompartmentsAnnotationReportsHasCompartments compartmentsAnnotationReportsHasCompartment);

	public Long getDistinctCompartmentsAnnotationReportsHasCompartmentsIds();

	public Map<String, String> getDistinctCompartmentsAnnotationReportsHasCompartmentsNameAndAbbreviation();

	public List<String[]> getCompartmentsAnnotationReportsHasCompartmentsNameAndScore(Integer id);

	public Integer countCompartmentsAnnotationReports();

	public List<CompartmentContainer> getBestCompartmenForGene();

}
