package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelDblinksDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelDblinks;


public class ModelDblinksDAOImpl extends GenericDaoImpl<ModelDblinks> implements IModelDblinksDAO{

	public ModelDblinksDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelDblinks.class);
		
	}
	
	@Override
	public void addModelDblinks(ModelDblinks modelDblinks) {
		super.save(modelDblinks);
		
	}

	@Override
	public void addModelDblinksList(List<ModelDblinks> modelDblinksList) {
		for (ModelDblinks modelDblinks: modelDblinksList) {
			this.addModelDblinks(modelDblinks);
		}
	}

	@Override
	public List<ModelDblinks> getAllModelDblinks() {
		return super.findAll();
	}

	@Override
	public ModelDblinks getModelDblinks(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelDblinks(ModelDblinks modelDblinks) {
		super.delete(modelDblinks);
	}

	@Override
	public void removeModelDblinksList(List<ModelDblinks> modelDblinksList) {
		for (ModelDblinks modelDblinks: modelDblinksList) {
			this.removeModelDblinks(modelDblinks);
		}	
	}

	@Override
	public void updateModelDblinksList(List<ModelDblinks> modelDblinksList) {
		for (ModelDblinks modelDblinks: modelDblinksList) {
			this.update(modelDblinks);
		}
	}

	@Override
	public void updateModelDblinks(ModelDblinks modelDblinks) {
		super.update(modelDblinks);
		
	}
	
	@Override
	public List<ModelDblinks> getAllModelDblinksByAttributes(String class_, Integer int_id, String ext_db){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.class_", class_);
		dic.put("id.internalId", int_id);
		dic.put("id.externalDatabase", ext_db);
		List<ModelDblinks> list =  this.findByAttributes(dic);

		return list;
	}
	



}


