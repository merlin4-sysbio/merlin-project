package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelEntityisfromDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEntityisfrom;


public class ModelEntityisfromDAOImpl extends GenericDaoImpl<ModelEntityisfrom> implements IModelEntityisfromDAO{

	public ModelEntityisfromDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelEntityisfrom.class);
		
	}

	@Override
	public void addModelEntityisfrom(ModelEntityisfrom modelEntityisfrom) {
		super.save(modelEntityisfrom);
		
	}

	@Override
	public void addModelEntityisfromList(List<ModelEntityisfrom> modelEntityisfromList) {
		for (ModelEntityisfrom modelEntityisfrom: modelEntityisfromList) {
			this.addModelEntityisfrom(modelEntityisfrom);
		}
	}

	@Override
	public List<ModelEntityisfrom> getAllModelEntityisfrom() {
		return super.findAll();
	}

	@Override
	public ModelEntityisfrom getModelEntityisfrom(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelEntityisfrom(ModelEntityisfrom modelEntityisfrom) {
		super.delete(modelEntityisfrom);
		
	}

	@Override
	public void removeModelEntityisfromList(List<ModelEntityisfrom> modelEntityisfromList) {
		for (ModelEntityisfrom modelEntityisfrom: modelEntityisfromList) {
			this.removeModelEntityisfrom(modelEntityisfrom);
		}
	}

	@Override
	public void updateModelEntityisfromist(List<ModelEntityisfrom> modelEntityisfromList) {
		for (ModelEntityisfrom modelEntityisfrom: modelEntityisfromList) {
			this.update(modelEntityisfrom);
		}
	}

	@Override
	public void updateModelEntityisfrom(ModelEntityisfrom ModelEntityisfrom) {
		super.update(ModelEntityisfrom);
	}

}
