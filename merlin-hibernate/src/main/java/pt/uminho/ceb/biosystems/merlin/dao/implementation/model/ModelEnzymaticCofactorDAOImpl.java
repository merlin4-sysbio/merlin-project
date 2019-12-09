package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelEnzymaticCofactorDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEnzymaticCofactor;


public class ModelEnzymaticCofactorDAOImpl extends GenericDaoImpl<ModelEnzymaticCofactor> implements IModelEnzymaticCofactorDAO {

	public ModelEnzymaticCofactorDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelEnzymaticCofactor.class);
		
	}

	@Override
	public void addModelEnzymaticCofactor(ModelEnzymaticCofactor modelEnzymaticCofactor) {
		super.save(modelEnzymaticCofactor);
		
	}

	@Override
	public void addModelEnzymaticCofactorList(List<ModelEnzymaticCofactor> modelEnzymaticCofactorList) {
		for (ModelEnzymaticCofactor modelEnzymaticCofactor: modelEnzymaticCofactorList) {
			this.addModelEnzymaticCofactor(modelEnzymaticCofactor);
		}
	}

	@Override
	public List<ModelEnzymaticCofactor> getAllModelEnzymaticCofactor() {
		return super.findAll();
	}

	@Override
	public ModelEnzymaticCofactor getModelEnzymaticCofactor(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelEnzymaticCofactor(ModelEnzymaticCofactor modelEnzymaticCofactor) {
		super.delete(modelEnzymaticCofactor);
		
	}

	@Override
	public void removeModelEnzymaticCofactorList(List<ModelEnzymaticCofactor> modelEnzymaticCofactorList) {
		for (ModelEnzymaticCofactor modelEnzymaticCofactor: modelEnzymaticCofactorList) {
			this.removeModelEnzymaticCofactor(modelEnzymaticCofactor);
		}
	}

	@Override
	public void updateModelEnzymaticCofactorList(List<ModelEnzymaticCofactor> modelEnzymaticCofactorList) {
		for (ModelEnzymaticCofactor modelEnzymaticCofactor: modelEnzymaticCofactorList) {
			this.update(modelEnzymaticCofactor);
		}
	}

	@Override
	public void updateModelEnzymaticCofactor(ModelEnzymaticCofactor modelEnzymaticCofactor) {
		super.update(modelEnzymaticCofactor);
		
	}
	
	@Override
	public boolean getModelEnzymaticCofactorProteinIdByattributes(Integer protID, Integer compoundID) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelProteinIdprotein", protID);
		dic.put("id.modelCompoundIdcompound", compoundID);
		ModelEnzymaticCofactor cofactor =  this.findUniqueByAttributes(dic);
		
		return (cofactor != null);
	}



}
