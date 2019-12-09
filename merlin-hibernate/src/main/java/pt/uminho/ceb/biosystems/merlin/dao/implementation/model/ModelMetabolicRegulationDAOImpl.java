package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelMetabolicRegulationDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelMetabolicRegulation;

public class ModelMetabolicRegulationDAOImpl extends GenericDaoImpl<ModelMetabolicRegulation> implements IModelMetabolicRegulationDAO {

	public ModelMetabolicRegulationDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelMetabolicRegulation.class);
		
	}

	@Override
	public void addModelMetabolicRegulation(ModelMetabolicRegulation modelMetabolicRegulation) {
		super.save(modelMetabolicRegulation);
		
	}

	@Override
	public void addModelMetabolicRegulationList(List<ModelMetabolicRegulation> modelMetabolicRegulationList) {
		for (ModelMetabolicRegulation modelMetabolicRegulation: modelMetabolicRegulationList) {
			this.addModelMetabolicRegulation(modelMetabolicRegulation);
		}
		
	}

	@Override
	public List<ModelMetabolicRegulation> getAllModelMetabolicRegulation() {
		return super.findAll();
	}

	@Override
	public ModelMetabolicRegulation getModelMetabolicRegulation(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelMetabolicRegulation(ModelMetabolicRegulation modelMetabolicRegulation) {
		super.delete(modelMetabolicRegulation);
		
	}

	@Override
	public void removeModelMetabolicRegulationList(List<ModelMetabolicRegulation> modelMetabolicRegulationList) {
		for (ModelMetabolicRegulation modelMetabolicRegulation: modelMetabolicRegulationList) {
			this.removeModelMetabolicRegulation(modelMetabolicRegulation);
		}
		
	}

	@Override
	public void updateModelMetabolicRegulationList(List<ModelMetabolicRegulation> modelMetabolicRegulationList) {
		for (ModelMetabolicRegulation modelMetabolicRegulation: modelMetabolicRegulationList) {
			this.update(modelMetabolicRegulation);
		}
		
	}

	@Override
	public void updateModelMetabolicRegulation(ModelMetabolicRegulation modelMetabolicRegulation) {
		super.update(modelMetabolicRegulation);
		
	}

}
