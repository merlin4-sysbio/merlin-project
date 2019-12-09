package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSubstrateAffinityDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubstrateAffinity;

public class ModelSubstrateAffinityDAOImpl extends GenericDaoImpl<ModelSubstrateAffinity> implements IModelSubstrateAffinityDAO{

	public ModelSubstrateAffinityDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelSubstrateAffinity.class);

	}

	@Override 
	public void addModelSubstrateAffinity(ModelSubstrateAffinity modelSubstrateAffinity) {
		super.save(modelSubstrateAffinity);

	}

	@Override 
	public void addModelSubstrateAffinityList(List<ModelSubstrateAffinity> modelSubstrateAffinityList) {
		for (ModelSubstrateAffinity modelSubstrateAffinity: modelSubstrateAffinityList) {
			this.addModelSubstrateAffinity(modelSubstrateAffinity);
		}

	}

	@Override 
	public List<ModelSubstrateAffinity> getAllModelSubstrateAffinity() {
		return super.findAll();
	}

	@Override 
	public ModelSubstrateAffinity getModelSubstrateAffinity(Integer id) {
		return super.findById(id);
	}

	@Override 
	public void removeModelSubstrateAffinity(ModelSubstrateAffinity modelSubstrateAffinity) {
		super.delete(modelSubstrateAffinity);

	}

	@Override 
	public void removeModelSubstrateAffinityList(List<ModelSubstrateAffinity> modelSubstrateAffinityList) {
		for (ModelSubstrateAffinity modelSubstrateAffinity: modelSubstrateAffinityList) {
			this.removeModelSubstrateAffinity(modelSubstrateAffinity);
		}

	}

	@Override 
	public void updateModelSubstrateAffinityList(List<ModelSubstrateAffinity> modelSubstrateAffinityList) {
		for (ModelSubstrateAffinity modelSubstrateAffinity: modelSubstrateAffinityList) {
			this.update(modelSubstrateAffinity);
		}

	}

	@Override 
	public void updateModelSubstrateAffinity(ModelSubstrateAffinity modelSubstrateAffinity) {
		super.update(modelSubstrateAffinity);

	}

}
