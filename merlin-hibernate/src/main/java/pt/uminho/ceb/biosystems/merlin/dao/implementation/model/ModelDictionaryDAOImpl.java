package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelDictionaryDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelDictionary;


public class ModelDictionaryDAOImpl extends GenericDaoImpl<ModelDictionary> implements IModelDictionaryDAO {

	public ModelDictionaryDAOImpl(SessionFactory sessionFactory, Class<ModelDictionary> klass) {
		super(sessionFactory, ModelDictionary.class);
		
	}

	@Override
	public void addModelDictionary(ModelDictionary modelDictionary) {
		super.save(modelDictionary);
		
	}

	@Override
	public void addModelDictionaryList(List<ModelDictionary> modelDictionaryList) {
		for (ModelDictionary modelDictionary: modelDictionaryList) {
			this.addModelDictionary(modelDictionary);
		}
	}

	@Override
	public List<ModelDictionary> getAllModelDictionary() {
		return super.findAll();
	}

	@Override
	public ModelDictionary getModelDictionary(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelDictionary(ModelDictionary modelDictionary) {
		super.delete(modelDictionary);
		
	}

	@Override
	public void removeModelDictionaryList(List<ModelDictionary> modelDictionaryList) {
		for (ModelDictionary modelDictionary: modelDictionaryList) {
			this.removeModelDictionary(modelDictionary);
		}
	}

	@Override
	public void updateModelDictionaryList(List<ModelDictionary> modelDictionaryList) {
		for (ModelDictionary modelDictionary: modelDictionaryList) {
			this.update(modelDictionary);
		}
	}

	@Override
	public void updateModelDictionary(ModelDictionary modelDictionary) {
		super.update(modelDictionary);
	}

}
