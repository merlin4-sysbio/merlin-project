package pt.uminho.ceb.biosystems.merlin.services.implementation;


import java.util.ArrayList;
import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelAliasesDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelAliases;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IAliasesService;

public class AliasesServiceImpl implements IAliasesService{


	private IModelAliasesDAO modelaliasesDAO;



	public AliasesServiceImpl(IModelAliasesDAO modelaliasesDAO) {
		this.modelaliasesDAO  = modelaliasesDAO;

	}

	@Override
	public List<String> getSynonyms(String class_, int entity) throws Exception {

		List<ModelAliases> aliases = this.modelaliasesDAO.getModelAliasesAliasByClassAndEntity(class_, entity);

		if(aliases == null)
			return null;

		List<String> res = new ArrayList<>();

		for(ModelAliases alias : aliases)
			res.add(alias.getAlias());

		return res;
	}


	public boolean checkAliasExistence(String class_, Integer entity, String name) throws Exception {
		List<ModelAliases> res = this.modelaliasesDAO.getAllModelAliasesByClassdAndEntityAndName(class_, entity, name);
		if (res != null && res.size()>0) {
			return true;
		}
		return false;

	}

	public List<String> getAliasClassTU(Integer id) throws Exception {
		return this.modelaliasesDAO.getModelAliasesAliasByClassAndEntity2("tu", id);
	}

	public List<String> getAliasClassG(Integer id) throws Exception {
		return this.modelaliasesDAO.getModelAliasesAliasByClassAndEntity2("g", id);
	}

	public List<String> getAliasClassR(Integer id) throws Exception {
		return this.modelaliasesDAO.getModelAliasesAliasByClassAndEntity2("r", id);
	}

	public List<String[]> getAliasClassP(Integer id) throws Exception {
		List<String[]> res = new ArrayList<>();
		List<String> result = this.modelaliasesDAO.getModelAliasesAliasByClassAndEntity2("p", id);

		if (result != null)
			for (String x : result)
				if(x != null && !x.equalsIgnoreCase("null"))
					res.add(new String[] {x});
		return res;

	}

	public long countGenesSynonyms(String aliasClass) throws Exception {
		return this.modelaliasesDAO.getAllModelAliasesByClass(aliasClass);
	}

	public long countProteinsSynonyms(String aliasClass) throws Exception {

		return this.modelaliasesDAO.getAllModelAliasesByClass(aliasClass);

	}

	//	public boolean checkEntityFromAliases(String class_, int entity, String alias) throws Exception {
	//		boolean exists = false;
	//		
	//		List<Integer> list = this.modelaliasesDAO.getModelAliasesEntityByAttributes(class_, entity, alias);
	//		if (list != null)
	//			exists = true;
	//		return exists;
	//	}

	public List<String> getAliasClassC(Integer id) throws Exception {
		List<String> res = new ArrayList<String>();

		List<String> result = this.modelaliasesDAO.getModelAliasesAliasByClassAndEntity2("c", id);

		if (result != null) {
			for (String x : result)
				res.add(x);

			if (result.size() == 0)
				res.add("");
		}
		return res;	
	}

	@Override
	public boolean checkEntityFromAliases(String cl, int entity, String alias) throws Exception {

		boolean exists = false;

		List<Integer> list = this.modelaliasesDAO.getModelAliasesEntityByAttributes(cl, entity, alias);

		if (!list.isEmpty())
			exists = true;

		return exists;
	}


	@Override
	public void insertNewModelAliasEntry(String cl, int entity, String alias) throws Exception {

		ModelAliases modelAlias = new ModelAliases();

		modelAlias.setAlias(alias);
		modelAlias.setClass_(cl);
		modelAlias.setEntity(entity);

		this.modelaliasesDAO.addModelAliases(modelAlias);
	}


	@Override
	public void updateModelAlias(int modelAliasId, String cl, int entity, String alias) throws Exception {

		ModelAliases modelAlias = this.modelaliasesDAO.getModelAliases(modelAliasId);

		if(modelAlias != null) {

			modelAlias.setAlias(alias);
			modelAlias.setClass_(cl);
			modelAlias.setEntity(entity);
		}

		this.modelaliasesDAO.updateModelAliases(modelAlias);
	}
	
	@Override
	public void removeModelAlias(int modelAliasId) throws Exception {

		ModelAliases modelAlias = this.modelaliasesDAO.getModelAliases(modelAliasId);
		this.modelaliasesDAO.removeModelAliases(modelAlias);
	}

}
