package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelAliasesDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelAliases;


public class ModelAliasesDAOImpl extends GenericDaoImpl<ModelAliases> implements IModelAliasesDAO {

	public ModelAliasesDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelAliases.class);
		
	}

	@Override
	public void addModelAliases(ModelAliases modelAliases) {
		super.save(modelAliases);
		
	}

	@Override
	public void addModelAliasesList(List<ModelAliases> modelAliasesList) {
		for (ModelAliases modelAliases: modelAliasesList) {
			this.addModelAliases(modelAliases);
		}
	}

	@Override
	public List<ModelAliases> getAllModelAliases() {
		return super.findAll();
	}

	@Override
	public ModelAliases getModelAliases(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelAliases(ModelAliases modelAliases) {
		super.delete(modelAliases);
		
	}

	@Override
	public void removeModelAliasesList(List<ModelAliases> modelAliasesList) {
		for (ModelAliases modelAliases: modelAliasesList) {
			this.removeModelAliases(modelAliases);
		}
	}

	@Override
	public void updateModelAliasesList(List<ModelAliases> modelAliasesList) {
		for (ModelAliases modelAliases: modelAliasesList) {
			this.update(modelAliases);
		}
	}

	@Override
	public void updateModelAliases(ModelAliases modelAliases) {
		super.update(modelAliases);
		
	}
	
	@Override
	public Integer insertModelAlias(String class_, Integer proteinId, String aliasName, Integer aliasId) {
		ModelAliases alias = new ModelAliases();
		
		alias.setClass_(class_);
		alias.setEntity(proteinId);
		alias.setAlias(aliasName);
		
		return (Integer) this.save(alias);
	}
	
	@Override
	public Integer insertModelAliasClassAndAliasAndEntity(String class_, String aliasName, Integer entity) {
		ModelAliases alias = new ModelAliases();
		alias.setClass_(class_);
		alias.setEntity(entity);;
		alias.setAlias(aliasName);
		
		return (Integer) this.save(alias);
	}
	
	@Override
	public List<ModelAliases> getModelAliasesAliasByClassAndEntity(String class_, int entity) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("class_", class_);
		dic.put("entity", entity);
		return this.findByAttributes(dic);
	}
	
	@Override
	public List<String> getModelAliasesAliasByClassAndEntity2(String class_, Integer entity) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		List<String> res = new ArrayList<String>();
		dic.put("class_", class_);
		dic.put("entity", entity);
		
		List<ModelAliases> list =  this.findByAttributes(dic);
		if(list.size() > 0) {
			for (ModelAliases x : list) {
				res.add(x.getAlias());
			}
		}
		return res;
	}
	
	@Override
	public List<Integer> getModelAliasesEntityByAttributes(String class_, int entity, String alias) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		List<Integer> res = new ArrayList<>();
		dic.put("class_", class_);
		dic.put("entity", entity);
		dic.put("alias", alias);
		
		List<ModelAliases> list =  this.findByAttributes(dic);
		if(list.size() > 0) {
			for (ModelAliases x : list) {
				res.add(x.getIdalias());
			}
		}
		return res;
	}
	
	@Override
	public List<ModelAliases> getAllModelAliasesByClassdAndEntityAndName(String class_, Integer entity, String name) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("class_", class_);
		map.put("entity", entity);
		map.put("alias", name);
		
		List<ModelAliases> res = this.findByAttributes(map);
		
		if (res!=null && res.size()>0) {
			return res;
		}
		return null;
	}
	
	@Override
	public Long getAllModelAliasesByClass(String aliasClass) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<ModelAliases> alias = c.from(ModelAliases.class);

	    c.multiselect(cb.count(alias)); 
	    
	    c.where(cb.equal(alias.get("class_"), aliasClass));
	    
	    Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Object result = q.uniqueResult();
		
		return (Long) result;
	}

	@Override
	public void updateModelAliasesAliasByClassAndEntityAndAlias(String class_, Integer entity, String alias, String alias_old) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("class_", class_);
		filterAttributes.put("entity", entity);
		filterAttributes.put("alias", alias_old);
		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("alias", alias);
		
		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}
	
	@Override
	public void removeAllModelAliasesByEntityAndAliases(Integer entity, String alias) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("entity", entity);
		dic.put("alias", alias);
		List<ModelAliases> list = this.findByAttributes(dic);
		if (list.size() > 0) {
			this.removeModelAliasesList(list);
		}
	}

}
