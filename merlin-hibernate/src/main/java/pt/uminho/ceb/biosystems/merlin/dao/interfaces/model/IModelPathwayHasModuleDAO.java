package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModule;

public interface IModelPathwayHasModuleDAO extends IGenericDao<ModelPathwayHasModule>{

	public void addModelPathwayHasModule(ModelPathwayHasModule modelPathwayHasModule);

	public void addModelPathwayHasModuleList(List<ModelPathwayHasModule> modelPathwayHasModuleList);

	public List<ModelPathwayHasModule> getAllModelPathwayHasModule();

	public void removeModelPathwayHasModule(ModelPathwayHasModule modelPathwayHasModule);

	public ModelPathwayHasModule getModelPathwayHasModule(Integer id);

	public void removeModelPathwayHasModuleList(List<ModelPathwayHasModule> modelPathwayHasModuleList);

	public void updateModelPathwayHasModuleList(List<ModelPathwayHasModule> modelPathwayHasModuleList);

	public void updateModelPathwayHasModule(ModelPathwayHasModule modelPathwayHasModule);

	public void insertModelPathwayHasModule(Integer pathId, Integer moduleId);

	public boolean checkModelPathwayHasModuleEntry(Integer pathId, Integer moduleId);

}
