package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSequenceFeatureDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequenceFeature;

public class ModelSequenceFeatureDAOImpl extends GenericDaoImpl<ModelSequenceFeature> implements IModelSequenceFeatureDAO{

	public ModelSequenceFeatureDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelSequenceFeature.class);

	}

	@Override 
	public void addModelSequenceFeature(ModelSequenceFeature modelSequenceFeature) {
		super.save(modelSequenceFeature);

	}

	@Override 
	public void addModelSequenceFeatureList(List<ModelSequenceFeature> modelSequenceFeatureList) {
		for (ModelSequenceFeature modelSequenceFeature: modelSequenceFeatureList) {
			this.addModelSequenceFeature(modelSequenceFeature);
		}
	}	


	@Override 
	public List<ModelSequenceFeature> getAllModelSequenceFeature() {
		return super.findAll();
	}

	@Override 
	public ModelSequenceFeature getModelSequenceFeature(Integer id) {
		return super.findById(id);

	}


	@Override 
	public void removeModelSequenceFeature(ModelSequenceFeature modelSequenceFeature) {
		super.delete(modelSequenceFeature);
	}


	@Override 
	public void removeModelSequenceFeatureList(List<ModelSequenceFeature> modelSequenceFeatureList) {
		for (ModelSequenceFeature modelSequenceFeature: modelSequenceFeatureList) {
			this.removeModelSequenceFeature(modelSequenceFeature);
		}


	}

	@Override 
	public void updateModelSequenceFeatureList(List<ModelSequenceFeature> modelSequenceFeatureList) {
		for (ModelSequenceFeature modelSequenceFeature: modelSequenceFeatureList) {
			this.update(modelSequenceFeature);
		}
	}

	@Override 
	public void updateModelSequenceFeature(ModelSequenceFeature modelSequenceFeature) {
		super.update(modelSequenceFeature);


	}

}
