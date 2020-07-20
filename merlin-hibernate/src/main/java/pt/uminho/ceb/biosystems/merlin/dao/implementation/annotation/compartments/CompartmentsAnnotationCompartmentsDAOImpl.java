package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.compartments;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.compartments.ICompartmentsAnnotationCompartmentsDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.CompartmentsAnnotationCompartments;


public class CompartmentsAnnotationCompartmentsDAOImpl extends GenericDaoImpl<CompartmentsAnnotationCompartments> implements ICompartmentsAnnotationCompartmentsDAO{

	public CompartmentsAnnotationCompartmentsDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, CompartmentsAnnotationCompartments.class);
		
	}
	
	@Override
	public void addCompartmentsAnnotationCompartment(CompartmentsAnnotationCompartments CompartmentsAnnotationCompartment) {
		super.save(CompartmentsAnnotationCompartment);
		
	}

	@Override
	public void addCompartmentsAnnotationCompartments(List<CompartmentsAnnotationCompartments> compartmentsAnnotationCompartments) {
		for (CompartmentsAnnotationCompartments compartmentsAnnotationCompartment: compartmentsAnnotationCompartments) {
			this.addCompartmentsAnnotationCompartment(compartmentsAnnotationCompartment);
		}
		
	}

	@Override
	public List<CompartmentsAnnotationCompartments> getAllCompartmentsAnnotationCompartments() {
		return super.findAll();
	}

	@Override
	public CompartmentsAnnotationCompartments getCompartmentsAnnotationCompartment(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeCompartmentsAnnotationCompartment(CompartmentsAnnotationCompartments compartmentsAnnotationCompartment) {
		super.delete(compartmentsAnnotationCompartment);
		
	}

	@Override
	public void removeCompartmentsAnnotationCompartments(List<CompartmentsAnnotationCompartments> compartmentsAnnotationCompartments) {
		for (CompartmentsAnnotationCompartments compartmentsAnnotationCompartment: compartmentsAnnotationCompartments) {
			this.removeCompartmentsAnnotationCompartment(compartmentsAnnotationCompartment);
		}
		
	}

	@Override
	public void updateCompartmentsAnnotationCompartments(List<CompartmentsAnnotationCompartments> compartmentsAnnotationCompartments) {
		for (CompartmentsAnnotationCompartments compartmentsAnnotationCompartment: compartmentsAnnotationCompartments) {
			this.update(compartmentsAnnotationCompartment);
		}
		
	}

	@Override
	public void updateCompartmentsAnnotationCompartment(CompartmentsAnnotationCompartments compartmentsAnnotationCompartment) {
		super.update(compartmentsAnnotationCompartment);
		
	}

}
