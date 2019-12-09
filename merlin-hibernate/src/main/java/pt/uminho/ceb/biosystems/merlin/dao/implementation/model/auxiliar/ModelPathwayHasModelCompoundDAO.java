package pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasModelCompoundDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasCompoundId;

public class ModelPathwayHasModelCompoundDAO extends GenericDaoImpl<ModelPathwayHasCompound> implements IModelPathwayHasModelCompoundDAO {

	public ModelPathwayHasModelCompoundDAO(SessionFactory sessionFactory) {
		super(sessionFactory, ModelPathwayHasCompound.class);
		
	}
	
	@Override
	public void addModelPathwayHasModelCompound(ModelPathwayHasCompound modelPathwayHasCompound) {
		super.save(modelPathwayHasCompound);
		
	}

	@Override
	public void addModelPathwayHasModelCompoundList(List<ModelPathwayHasCompound> modelPathwayHasCompoundList) {
		for (ModelPathwayHasCompound modelPathwayHasCompound: modelPathwayHasCompoundList) {
			this.addModelPathwayHasModelCompound(modelPathwayHasCompound);
		}
		
	}

	@Override
	public List<ModelPathwayHasCompound> getAllModelPathwayHasModelCompound() {
		return super.findAll();
	}

	@Override
	public ModelPathwayHasCompound getModelPathwayHasModelCompound(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelPathwayHasModelCompound(ModelPathwayHasCompound modelPathwayHasCompound) {
		super.delete(modelPathwayHasCompound);
		
	}

	@Override
	public void removeModelPathwayHasModelCompoundList(List<ModelPathwayHasCompound> modelPathwayHasCompoundList) {
		for (ModelPathwayHasCompound modelPathwayHasModelCompound: modelPathwayHasCompoundList) {
			this.removeModelPathwayHasModelCompound(modelPathwayHasModelCompound);
		}
		
	}

	@Override
	public void updateModelPathwayHasModelCompoundList(List<ModelPathwayHasCompound> modelPathwayHasCompoundList) {
		for (ModelPathwayHasCompound modelPathwayHasCompound: modelPathwayHasCompoundList) {
			this.update(modelPathwayHasCompound);
		}
		
	}

	@Override
	public void updateModelPathwayHasModelCompound(ModelPathwayHasCompound modelPathwayHasCompound) {
		super.update(modelPathwayHasCompound);
		
	}
	
	@Override
	public void insertModelPathwayHasModelCompound(Integer pathId, Integer compoundId) {
		ModelPathwayHasCompound model = new ModelPathwayHasCompound();
		ModelPathwayHasCompoundId id = new ModelPathwayHasCompoundId();
		id.setModelCompoundIdcompound(compoundId);
		id.setModelPathwayIdpathway(pathId);
		model.setId(id);
		
		this.save(model);
	}

			
}
