package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasModelProtein;


public interface IModelModuleHasModelProteinDAO  extends IGenericDao<ModelModuleHasModelProtein>{

	public void addModelModuleHasModelProtein(ModelModuleHasModelProtein modelModuleHasModelProtein);

	public void addModelModuleHasModelProteinList(List<ModelModuleHasModelProtein> modelModuleHasModelProteinList);

	public List<ModelModuleHasModelProtein> getAllModelModuleHasModelProtein();

	public ModelModuleHasModelProtein getModelModuleHasModelProtein(Integer id);

	public void removeModelModuleHasModelProtein(ModelModuleHasModelProtein modelModuleHasModelProtein);

	public void removeModelModuleHasModelProteinList(List<ModelModuleHasModelProtein> modelModuleHasModelProteinList);

	public void updateModelModuleHasModelProteinList(List<ModelModuleHasModelProtein> modelModuleHasModelProteinList);

	public void updateModelModuleHasModelProtein(ModelModuleHasModelProtein modelModuleHasModelProtein);


}
