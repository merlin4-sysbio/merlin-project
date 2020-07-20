package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasCompound;

public interface IModelPathwayHasModelCompoundDAO extends IGenericDao<ModelPathwayHasCompound>{

	public void addModelPathwayHasModelCompound(ModelPathwayHasCompound modelPathwayHasCompound);

	public void addModelPathwayHasModelCompoundList(List<ModelPathwayHasCompound> modelPathwayHasCompoundList);

	public List<ModelPathwayHasCompound> getAllModelPathwayHasModelCompound();

	public ModelPathwayHasCompound getModelPathwayHasModelCompound(Integer id);

	public void removeModelPathwayHasModelCompound(ModelPathwayHasCompound modelPathwayHasCompound);

	public void removeModelPathwayHasModelCompoundList(List<ModelPathwayHasCompound> modelPathwayHasCompoundList);

	public void updateModelPathwayHasModelCompoundList(List<ModelPathwayHasCompound> modelPathwayHasCompoundList);

	public void updateModelPathwayHasModelCompound(ModelPathwayHasCompound modelPathwayHasCompound);

	public void insertModelPathwayHasModelCompound(Integer pathId, Integer compoundId);

}
