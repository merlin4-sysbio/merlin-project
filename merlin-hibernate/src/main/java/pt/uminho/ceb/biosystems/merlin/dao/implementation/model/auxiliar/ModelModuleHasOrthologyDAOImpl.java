package pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelModuleHasOrthologyDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasOrthology;

public class ModelModuleHasOrthologyDAOImpl  extends GenericDaoImpl<ModelModuleHasOrthology> implements IModelModuleHasOrthologyDAO {

	public ModelModuleHasOrthologyDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelModuleHasOrthology.class);
		
	}

	@Override
	public void addModelModuleHasOrthology(ModelModuleHasOrthology modelModuleHasOrthology) {
		super.save(modelModuleHasOrthology);
		
	}

	@Override
	public void addModelModuleHasOrthologyList(List<ModelModuleHasOrthology> modelGeneHasOrthologyList) {
		for (ModelModuleHasOrthology modelGeneHasOrthology: modelGeneHasOrthologyList) {
			this.addModelModuleHasOrthology(modelGeneHasOrthology);
		}
		
	}

	@Override
	public List<ModelModuleHasOrthology> getAllModelModuleHasOrthology() {
		return super.findAll();
	}

	@Override
	public ModelModuleHasOrthology getModelModuleHasOrthology(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelModuleHasOrthology(ModelModuleHasOrthology modelModuleHasOrthology) {
		super.delete(modelModuleHasOrthology);
		
	}

	@Override
	public void removeModelModuleHasOrthologyList(List<ModelModuleHasOrthology> modelModuleHasOrthologyList) {
		for (ModelModuleHasOrthology modelModuleHasOrthology: modelModuleHasOrthologyList) {
			this.removeModelModuleHasOrthology(modelModuleHasOrthology);
		}
		
	}

	@Override
	public void updateModelModuleHasOrthologyList(List<ModelModuleHasOrthology> modelModuleHasOrthologyList) {
		for (ModelModuleHasOrthology modelModuleHasOrthology: modelModuleHasOrthologyList) {
			this.update(modelModuleHasOrthology);
		}
		
	}

	@Override
	public void updateModelModuleHasOrthology(ModelModuleHasOrthology modelModuleHasOrthology) {
		super.update(modelModuleHasOrthology);
		
	}
	
}
