package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelModuleDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModule;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;

public class ModelModuleDAOImpl extends GenericDaoImpl<ModelModule> implements IModelModuleDAO{

	public ModelModuleDAOImpl(SessionFactory sessionFactory, Class<ModelModule> klass) {
		super(sessionFactory, ModelModule.class);
		
	}

	@Override
	public void addModelModule(ModelModule modelModule) {
		super.save(modelModule);
		
	}

	@Override
	public void addModelModuleList(List<ModelModule> modelModuleList) {
		for (ModelModule modelModule: modelModuleList) {
			this.addModelModule(modelModule);
		}
		
	}

	@Override
	public List<ModelModule> getAllModelModule() {
		return super.findAll();
	}

	@Override
	public ModelModule getModelModule(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelModule(ModelModule modelModule) {
		super.delete(modelModule);
		
	}

	@Override
	public void removeModelModuleList(List<ModelModule> modelModuleList) {
		for (ModelModule modelModule: modelModuleList) {
			this.removeModelModule(modelModule);
		}
		
	}

	@Override
	public void updateModelModuleList(List<ModelModule> modelModuleList) {
		for (ModelModule modelModule: modelModuleList) {
			this.update(modelModule);
		}
		
	}

	@Override
	public void updateModelModule(ModelModule modelModule) {
		super.update(modelModule);
		
	}
	
	@Override
	public Map<Integer, String> getModelModuleIdAndDefinitionByEntryIdAndReactionAndDefinition(String entryId, String reaction, String definition) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("entryId", entryId);
		map.put("reaction", reaction);
		map.put("definition", definition);
		List<ModelModule> list =  this.findByAttributes(map);
		Map<Integer, String> dic = null;
		
		if(list.size() > 0) {
			dic = new HashMap<Integer, String>();
			for (ModelModule x: list){
			
			dic.put(x.getId(), x.getDefinition());
			}
		}
		return dic;
	}

	@Override
	public Integer insertModelModule(String reaction, String entryId, String name, String definition, 
			String type){
		ModelModule modelModule = new ModelModule();
		modelModule.setReaction(reaction);
		modelModule.setEntryId(entryId);
		modelModule.setName(name);
		modelModule.setDefinition(definition);
		modelModule.setType(type);
		return (Integer) this.save(modelModule);	
	}
	
	@Override
	public Map<String, Integer> getModuleEntryidAndId(){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelModule> c = cb.createQuery(ModelModule.class);
		Root<ModelModule> module = c.from(ModelModule.class);
		c.select(module);
		
		Predicate filter1 = cb.isNotNull(module.get("entryId"));
		c.where(cb.and(filter1));
		Query<ModelModule> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelModule> list = q.getResultList();
		
		Map<String, Integer> result = new HashMap<>();
		
		if(list.size() > 0) {
			for(ModelModule moduleItem: list) {
				result.put(moduleItem.getEntryId(), moduleItem.getId());
			}
		}
		return result;
	}

	@Override
	public Map<String, Integer> getModelModuleEntryIdAndId(){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelModule> c = cb.createQuery(ModelModule.class);
		Root<ModelModule> module = c.from(ModelModule.class);
		c.select(module);
		
		Predicate filter1 = cb.isNotNull(module.get("entryId"));
		c.where(cb.and(filter1));
		Query<ModelModule> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelModule> list = q.getResultList();
		
		Map<String, Integer> result = new HashMap<>();
		
		if(list.size() > 0) {
			for(ModelModule moduleItem: list) {
				result.put(moduleItem.getEntryId(), moduleItem.getId());
			}
		}
		return result;
	}
	
	@Override
	public List<ModelModule> getModulesIdsByEcNumber(String ecNumber){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelModule> c = cb.createQuery(ModelModule.class);
		Root<ModelModule> module = c.from(ModelModule.class);
		
		Join<ModelModuleHasModelProtein, ModelModule> moduleHasProt = module.join("modelModuleHasModelProteins", JoinType.INNER);
		Join<ModelModuleHasModelProtein, ModelProtein> protein = moduleHasProt.join("modelProtein", JoinType.INNER);
		
		c.select(module);
		
		Predicate filter1 = cb.equal(protein.get("ecnumber"), ecNumber);
		
		c.where(filter1);
		
		Query<ModelModule> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelModule> list = q.getResultList();
		
		List<ModelModule> modulesIds = new ArrayList<>();
		
		if(list.size() > 0) {
			
			for(ModelModule moduleItem: list) {
		
				modulesIds.add(moduleItem);
				
				}
		}
		
		return modulesIds;
	}
	
	@Override 
	public Map<Integer,String> getModuleIdAndNoteByGeneIdAndProteinId(Integer geneId, Integer protId) {

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		
		Root<ModelProtein> protein = c.from(ModelProtein.class);	
		
		Join<ModelProtein, ModelSubunit> subunit = protein.join("modelSubunits", JoinType.INNER);
		Join<ModelProtein, ModelModuleHasModelProtein> moduleHasProtein = protein.join("modelModuleHasModelProteins", JoinType.INNER);
		Join<ModelModuleHasModelProtein, ModelModule> module = moduleHasProtein.join("modelModule", JoinType.INNER);
		Join<ModelSubunit, ModelGene> gene = subunit.join("modelGene", JoinType.INNER);
		
		c.multiselect(module.get("id"), module.get("note"));

		Predicate filter2 = cb.equal(protein.get("idprotein"), protId);
		Predicate filter3 = cb.equal(gene.get("idgene"), geneId);
		
		c.where(cb.and(filter2, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		Map<Integer,String> res = new HashMap<>();

		if(list.size() > 0 && list != null) {

			for(Object[] item : list) {
				
				if(item[0] != null && item[1] != null)
					
					res.put(Integer.valueOf(item[0].toString()), item[1].toString());
			}
		}
		
		return res;	
	}
	

}
