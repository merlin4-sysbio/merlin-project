package pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelModuleHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasModelProtein;

public class ModelModuleHasModelProteinDAOImpl extends GenericDaoImpl<ModelModuleHasModelProtein> implements IModelModuleHasModelProteinDAO {

	public ModelModuleHasModelProteinDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelModuleHasModelProtein.class);
		
	}
	
	@Override
	public void addModelModuleHasModelProtein(ModelModuleHasModelProtein modelModuleHasModelProtein) {
		super.save(modelModuleHasModelProtein);
		
	}

	@Override
	public void addModelModuleHasModelProteinList(List<ModelModuleHasModelProtein> modelModuleHasModelProteinList) {
		for (ModelModuleHasModelProtein modelModuleHasModelProtein: modelModuleHasModelProteinList) {
			this.addModelModuleHasModelProtein(modelModuleHasModelProtein);
		}
		
	}

	@Override
	public List<ModelModuleHasModelProtein> getAllModelModuleHasModelProtein() {
		return super.findAll();
	}

	@Override
	public ModelModuleHasModelProtein getModelModuleHasModelProtein(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelModuleHasModelProtein(ModelModuleHasModelProtein modelModuleHasModelProtein) {
		super.delete(modelModuleHasModelProtein);
		
	}

	@Override
	public void removeModelModuleHasModelProteinList(List<ModelModuleHasModelProtein> modelModuleHasModelProteinList) {
		for (ModelModuleHasModelProtein modelPathwayHasModelCompound: modelModuleHasModelProteinList) {
			this.removeModelModuleHasModelProtein(modelPathwayHasModelCompound);
		}
		
	}

	@Override
	public void updateModelModuleHasModelProteinList(List<ModelModuleHasModelProtein> modelModuleHasModelProteinList) {
		for (ModelModuleHasModelProtein modelModuleHasModelProtein: modelModuleHasModelProteinList) {
			this.update(modelModuleHasModelProtein);
		}
		
	}

	@Override
	public void updateModelModuleHasModelProtein(ModelModuleHasModelProtein modelModuleHasModelProtein) {
		super.update(modelModuleHasModelProtein);
		
	}
}
