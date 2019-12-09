package pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasModuleDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModule;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModuleId;

public class ModelPathwayHasModuleDAO extends GenericDaoImpl<ModelPathwayHasModule> implements IModelPathwayHasModuleDAO {

	public ModelPathwayHasModuleDAO(SessionFactory sessionFactory) {
		super(sessionFactory, ModelPathwayHasModule.class);
		
	}
	
	@Override
	public void addModelPathwayHasModule(ModelPathwayHasModule modelPathwayHasModule) {
		super.save(modelPathwayHasModule);
		
	}

	@Override
	public void addModelPathwayHasModuleList(List<ModelPathwayHasModule> modelPathwayHasModuleList) {
		for (ModelPathwayHasModule modelPathwayHasModule: modelPathwayHasModuleList) {
			this.addModelPathwayHasModule(modelPathwayHasModule);
		}
		
	}

	@Override
	public List<ModelPathwayHasModule> getAllModelPathwayHasModule() {
		return super.findAll();
	}

	@Override
	public ModelPathwayHasModule getModelPathwayHasModule(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelPathwayHasModule(ModelPathwayHasModule modelPathwayHasModule) {
		super.delete(modelPathwayHasModule);
		
	}

	@Override
	public void removeModelPathwayHasModuleList(List<ModelPathwayHasModule> modelPathwayHasModuleList) {
		for (ModelPathwayHasModule modelPathwayHasModule: modelPathwayHasModuleList) {
			this.removeModelPathwayHasModule(modelPathwayHasModule);
		}
		
	}

	@Override
	public void updateModelPathwayHasModuleList(List<ModelPathwayHasModule> modelPathwayHasModuleList) {
		for (ModelPathwayHasModule modelPathwayHasModule: modelPathwayHasModuleList) {
			this.update(modelPathwayHasModule);
		}
		
	}

	@Override
	public void updateModelPathwayHasModule(ModelPathwayHasModule modelPathwayHasModule) {
		super.update(modelPathwayHasModule);
		
	}
	
	@Override
	public void insertModelPathwayHasModule(Integer pathId, Integer moduleId) {
		ModelPathwayHasModule model = new ModelPathwayHasModule();
		ModelPathwayHasModuleId id = new ModelPathwayHasModuleId();
		id.setModelModuleId(moduleId);
		id.setModelPathwayIdpathway(pathId);
		model.setId(id);
		
		this.save(model);
	}
	
	@Override
	public boolean checkModelPathwayHasModuleEntry(Integer pathId, Integer moduleId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelPathwayIdpathway", pathId);
		dic.put("id.modelModuleId", moduleId);
		ModelPathwayHasModule has = this.findUniqueByAttributes(dic);
		
		return (has != null);
	}
}
