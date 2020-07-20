package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasOrthology;

public interface IModelModuleHasOrthologyDAO  extends IGenericDao<ModelModuleHasOrthology>{

	public void addModelModuleHasOrthology(ModelModuleHasOrthology modelModuleHasOrthology);

	public void addModelModuleHasOrthologyList(List<ModelModuleHasOrthology> modelGeneHasOrthologyList);

	public List<ModelModuleHasOrthology> getAllModelModuleHasOrthology();

	public ModelModuleHasOrthology getModelModuleHasOrthology(Integer id);

	public void removeModelModuleHasOrthology(ModelModuleHasOrthology modelModuleHasOrthology);

	public void removeModelModuleHasOrthologyList(List<ModelModuleHasOrthology> modelModuleHasOrthologyList);

	public void updateModelModuleHasOrthologyList(List<ModelModuleHasOrthology> modelModuleHasOrthologyList);

	public void updateModelModuleHasOrthology(ModelModuleHasOrthology modelModuleHasOrthology);

}
