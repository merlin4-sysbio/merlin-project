package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelEnzymaticAlternativeCofactorDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEnzymaticAlternativeCofactor;

public class ModelEnzymaticAlternativeCofactorDAOImpl extends GenericDaoImpl<ModelEnzymaticAlternativeCofactor> implements IModelEnzymaticAlternativeCofactorDAO{

	public ModelEnzymaticAlternativeCofactorDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelEnzymaticAlternativeCofactor.class);
		
	}

	@Override
	public void addModelEnzymaticAlternativeCofactor(ModelEnzymaticAlternativeCofactor ModelEnzymaticAlternativeCofactor) {
		super.save(ModelEnzymaticAlternativeCofactor);
		
	}

	@Override
	public void addModelEnzymaticAlternativeCofactorList(List<ModelEnzymaticAlternativeCofactor> ModelEnzymaticAlternativeCofactorList) {
		for (ModelEnzymaticAlternativeCofactor ModelEnzymaticAlternativeCofactor: ModelEnzymaticAlternativeCofactorList) {
			this.addModelEnzymaticAlternativeCofactor(ModelEnzymaticAlternativeCofactor);
		}
	}

	@Override
	public List<ModelEnzymaticAlternativeCofactor> getAllModelEnzymaticAlternativeCofactor() {
		return super.findAll();
	}
	

	@Override
	public ModelEnzymaticAlternativeCofactor getModelEnzymaticAlternativeCofactor(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelEnzymaticAlternativeCofactor(ModelEnzymaticAlternativeCofactor ModelEnzymaticAlternativeCofactor) {
		super.delete(ModelEnzymaticAlternativeCofactor);
		
	}

	@Override
	public void removeModelEnzymaticAlternativeCofactorList(List<ModelEnzymaticAlternativeCofactor> ModelEnzymaticAlternativeCofactorList) {
		for (ModelEnzymaticAlternativeCofactor ModelEnzymaticAlternativeCofactor: ModelEnzymaticAlternativeCofactorList) {
			this.removeModelEnzymaticAlternativeCofactor(ModelEnzymaticAlternativeCofactor);
		}	
	}

	@Override
	public void updateModelEnzymaticAlternativeCofactorList(List<ModelEnzymaticAlternativeCofactor> ModelEnzymaticAlternativeCofactorList) {
		for (ModelEnzymaticAlternativeCofactor ModelEnzymaticAlternativeCofactor: ModelEnzymaticAlternativeCofactorList) {
			this.update(ModelEnzymaticAlternativeCofactor);
		}
	}

	@Override
	public void updateModelEnzymaticAlternativeCofactor(ModelEnzymaticAlternativeCofactor ModelEnzymaticAlternativeCofactor) {
		super.update(ModelEnzymaticAlternativeCofactor);
		
	}

}

