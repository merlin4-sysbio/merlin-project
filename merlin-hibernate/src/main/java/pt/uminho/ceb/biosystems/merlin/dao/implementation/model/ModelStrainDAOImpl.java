package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelStrainDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStrain;


public class ModelStrainDAOImpl extends GenericDaoImpl<ModelStrain> implements IModelStrainDAO{

	public ModelStrainDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelStrain.class);

	}

	@Override 
	public void addModelStrain(ModelStrain modelStrain) {
		super.save(modelStrain);

	}

	@Override 
	public void addModelStrainList(List<ModelStrain> modelStrainList) {
		for (ModelStrain modelStrain: modelStrainList) {
			this.addModelStrain(modelStrain);
		}

	}

	@Override 
	public List<ModelStrain> getAllModelStrain() {
		return super.findAll();
	}

	@Override 
	public ModelStrain getModelStrain(Integer id) {
		return super.findById(id);
	}

	@Override 
	public void removeModelStrain(ModelStrain modelStrain) {
		super.delete(modelStrain);

	}

	@Override 
	public void removeModelStrainList(List<ModelStrain> modelStrainList) {
		for (ModelStrain modelStrain: modelStrainList) {
			this.removeModelStrain(modelStrain);
		}

	}

	@Override 
	public void updateModelStrainList(List<ModelStrain> modelStrainList) {
		for (ModelStrain modelStrain: modelStrainList) {
			this.update(modelStrain);
		}

	}

	@Override 
	public void updateModelStrain(ModelStrain modelStrain) {
		super.update(modelStrain);

	}

}
