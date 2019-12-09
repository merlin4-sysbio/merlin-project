package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelAliases;


public interface IModelAliasesDAO extends IGenericDao<ModelAliases>{
	
	public void addModelAliases(ModelAliases modelAliases); 
	
	public void addModelAliasesList(List<ModelAliases> modelAliasesList); 
	
	public List<ModelAliases> getAllModelAliases(); 
	
	public ModelAliases getModelAliases(Integer id); 
	
	public void removeModelAliases(ModelAliases modelAliases); 
	
	public void removeModelAliasesList(List<ModelAliases> modelAliasesList); 
	
	public void updateModelAliasesList(List<ModelAliases> modelAliasesList); 
	
	public void updateModelAliases(ModelAliases modelAliases);

	public List<ModelAliases> getModelAliasesAliasByClassAndEntity(String string, int entity);

	public List<ModelAliases> getAllModelAliasesByClassdAndEntityAndName(String class_, Integer entity, String name);

	public List<String> getModelAliasesAliasByClassAndEntity2(String class_, Integer entity);

	public Long getAllModelAliasesByClass(String class_);

	public List<Integer> getModelAliasesEntityByAttributes(String class_, int entity, String alias);

	public Integer insertModelAlias(String class_, Integer proteinId, String aliasName, Integer aliasId);
	
	public Integer insertModelAliasClassAndAliasAndEntity(String class_, String aliasName, Integer entity);
	
	public void removeAllModelAliasesByEntityAndAliases(Integer entity, String alias);
	
	public void updateModelAliasesAliasByClassAndEntityAndAlias(String class_, Integer entity, String alias, String alias_old);
	
}
