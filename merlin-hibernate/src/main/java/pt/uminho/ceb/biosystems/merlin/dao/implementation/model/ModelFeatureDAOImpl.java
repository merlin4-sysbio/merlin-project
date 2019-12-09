package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelFeatureDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelFeature;


public class ModelFeatureDAOImpl extends GenericDaoImpl<ModelFeature> implements IModelFeatureDAO {

	public ModelFeatureDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelFeature.class);
		
	}

	@Override
	public void addModelFeature(ModelFeature modelFeature) {
		super.save(modelFeature);
		
	}

	@Override
	public void addModelFeatureList(List<ModelFeature> modelFeatureList) {
		for (ModelFeature modelFeature: modelFeatureList) {
			this.addModelFeature(modelFeature);
		}
	}

	@Override
	public List<ModelFeature> getAllModelFeature() {
		return super.findAll();
	}

	@Override
	public ModelFeature getModelFeature(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelFeature(ModelFeature modelFeature) {
		super.delete(modelFeature);
	}

	@Override
	public void removeModelFeatureList(List<ModelFeature> modelFeatureList) {
		for (ModelFeature modelFeature: modelFeatureList) {
			this.removeModelFeature(modelFeature);
		}
	}

	@Override
	public void updateModelFeatureList(List<ModelFeature> modelFeatureList) {
		for (ModelFeature modelFeature: modelFeatureList) {
			this.update(modelFeature);
		}
	}

	@Override
	public void updateModelFeature(ModelFeature modelFeature) {
		super.update(modelFeature);	
	}

}
