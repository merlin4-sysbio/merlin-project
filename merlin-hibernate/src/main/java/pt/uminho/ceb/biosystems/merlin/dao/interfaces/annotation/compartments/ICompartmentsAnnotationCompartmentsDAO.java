package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.compartments;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.CompartmentsAnnotationCompartments;


public interface ICompartmentsAnnotationCompartmentsDAO extends IGenericDao<CompartmentsAnnotationCompartments>{
	
	public void addCompartmentsAnnotationCompartment(CompartmentsAnnotationCompartments compartmentsAnnotationCompartment); 
	
	public void addCompartmentsAnnotationCompartments(List<CompartmentsAnnotationCompartments> compartmentsAnnotationCompartments); 
	
	public List<CompartmentsAnnotationCompartments> getAllCompartmentsAnnotationCompartments(); 
	
	public CompartmentsAnnotationCompartments getCompartmentsAnnotationCompartment(Integer id); 
	
	public void removeCompartmentsAnnotationCompartment(CompartmentsAnnotationCompartments compartmentsAnnotationCompartment); 
	
	public void removeCompartmentsAnnotationCompartments(List<CompartmentsAnnotationCompartments> compartmentsAnnotationCompartments); 
	
	public void updateCompartmentsAnnotationCompartments(List<CompartmentsAnnotationCompartments> compartmentsAnnotationCompartments); 
	
	public void updateCompartmentsAnnotationCompartment(CompartmentsAnnotationCompartments compartmentsAnnotationCompartment);
}
