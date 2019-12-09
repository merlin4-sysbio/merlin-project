package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelFunctionalParameterDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelFunctionalParameter;


public class ModelFunctionalParameterDAOImpl extends GenericDaoImpl<ModelFunctionalParameter> implements IModelFunctionalParameterDAO {

	public ModelFunctionalParameterDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelFunctionalParameter.class);
		
	}

	@Override
	public void addModelFunctionalParameter(ModelFunctionalParameter modelFunctionalParameter) {
		super.save(modelFunctionalParameter);
		
	}

	@Override
	public void addModelFunctionalParameterList(List<ModelFunctionalParameter> modelFunctionalParameterList) {
		for (ModelFunctionalParameter modelFunctionalParameter: modelFunctionalParameterList) {
			this.addModelFunctionalParameter(modelFunctionalParameter);
		}
		
	}

	@Override
	public List<ModelFunctionalParameter> getAllModelFunctionalParameter() {
		return super.findAll();
	}

	@Override
	public ModelFunctionalParameter getModelFunctionalParameter(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelFunctionalParameter(ModelFunctionalParameter modelFunctionalParameter) {
		super.delete(modelFunctionalParameter);
		
	}

	@Override
	public void removeModelFunctionalParameterList(List<ModelFunctionalParameter> modelFunctionalParameterList) {
		for (ModelFunctionalParameter modelFunctionalParameter: modelFunctionalParameterList) {
			this.removeModelFunctionalParameter(modelFunctionalParameter);
		}
		
	}

	@Override
	public void updateModelFunctionalParameterList(List<ModelFunctionalParameter> modelFunctionalParameterList) {
		for (ModelFunctionalParameter modelFunctionalParameter: modelFunctionalParameterList) {
			this.update(modelFunctionalParameter);
		}
		
	}

	@Override
	public void updateModelFunctionalParameter(ModelFunctionalParameter modelFunctionalParameter) {
		super.update(modelFunctionalParameter);
		
	}

}
