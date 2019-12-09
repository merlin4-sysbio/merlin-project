package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathway;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;

public class ModelPathwayDAOImpl extends GenericDaoImpl<ModelPathway> implements IModelPathwayDAO {

	public ModelPathwayDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelPathway.class);

	}

	@Override
	public void addModelPathway(ModelPathway modelPathway) {
		super.save(modelPathway);

	}

	@Override
	public void addModelPathwayList(List<ModelPathway> modelPathwayList) {
		for (ModelPathway modelPathway : modelPathwayList) {
			this.addModelPathway(modelPathway);
		}

	}

	@Override
	public List<ModelPathway> getAllModelPathway() {
		return super.findAll();
	}

	@Override
	public ModelPathway getModelPathway(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelPathway(ModelPathway modelPathway) {
		super.delete(modelPathway);

	}

	@Override
	public void removeModelPathwayList(List<ModelPathway> modelPathwayList) {
		for (ModelPathway modelPathway : modelPathwayList) {
			this.removeModelPathway(modelPathway);
		}

	}

	@Override
	public void updateModelPathwayList(List<ModelPathway> modelPathwayList) {
		for (ModelPathway modelPathway : modelPathwayList) {
			this.update(modelPathway);
		}
	}

	@Override
	public void updateModelPathway(ModelPathway modelPathway) {
		super.update(modelPathway);

	}

	@Override
	public Map<Integer, Set<String>> getAllPathwayIdAndNameAndEcNumber() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelPathway> path = c.from(ModelPathway.class);
		Root<ModelPathwayHasModelProtein> pathHasProt = c.from(ModelPathwayHasModelProtein.class);

		c.multiselect(path.get("idpathway"), path.get("name"), pathHasProt.get("modelProtein").get("ecnumber"));
		
		Predicate filter1 = cb.equal(path.get("idpathway"), pathHasProt.get("id").get("modelPathwayIdpathway"));

		c.where((filter1));
		c.orderBy(cb.asc(path.get("idpathway")));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<Integer, Set<String>> res = null;
		if (list.size() > 0) {
			res = new HashMap<Integer, Set<String>>();
			for (Object[] result : list) {
				Set<String> aliasList = res.get((Integer) result[0]);
				if (aliasList == null) {
					aliasList = new TreeSet<String>();
					res.put((Integer) result[0], aliasList);
				}
				aliasList.add((String) result[1]);
			}
		}
		return res;
	}

	@Override
	public Map<String, Set<String>> getEnzymesPathwaysEcNumber() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathwayHasModelProtein> c = cb.createQuery(ModelPathwayHasModelProtein.class);
		Root<ModelPathwayHasModelProtein> pathwayHasProtein = c.from(ModelPathwayHasModelProtein.class);

		c.select(pathwayHasProtein);

		Predicate filter1 = cb.equal(pathwayHasProtein.get("modelProtein").get("inModel"),true);
		
		c.where(filter1);
		c.orderBy(cb.asc(pathwayHasProtein.get("modelPathway").get("idpathway")));

		Query<ModelPathwayHasModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelPathwayHasModelProtein> list = q.getResultList();
		Map<String, Set<String>> res = new HashMap<String, Set<String>>();
		
		if (list.size() > 0) {
			
			for (ModelPathwayHasModelProtein result : list) {
				
				Set<String> ecnumberList = res.get(result.getModelPathway().getName());
				
				if (ecnumberList == null) {
					ecnumberList = new TreeSet<String>();
					res.put(result.getModelPathway().getName(), ecnumberList);
				}
				ecnumberList.add(result.getModelProtein().getEcnumber());
			}
		}
		return res;
	}

	@Override
	public Map<String, Set<String>> getPathwayIdAndNameAndEcNumber() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathwayHasModelProtein> c = cb.createQuery(ModelPathwayHasModelProtein.class);
		Root<ModelPathwayHasModelProtein> pathwayHasProtein = c.from(ModelPathwayHasModelProtein.class);

		c.select(pathwayHasProtein);

		c.orderBy(cb.asc(pathwayHasProtein.get("modelPathway").get("idpathway")));

		Query<ModelPathwayHasModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelPathwayHasModelProtein> list = q.getResultList();
		
		Map<String, Set<String>> res = new HashMap<String, Set<String>>();
		
		if (list.size() > 0) {
			
			for (ModelPathwayHasModelProtein result : list) {
				Set<String> ecnumberList = res.get(result.getModelPathway().getName());
				if (ecnumberList == null) {
					ecnumberList = new TreeSet<String>();
					res.put(result.getModelPathway().getName(), ecnumberList);
				}
				ecnumberList.add((String) result.getModelProtein().getEcnumber());
			}
		}
		return res;
	}

	@Override
	public boolean checkIfModelPathwayNameExists(String name) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("name", name);

		List<ModelPathway> list =  this.findByAttributes(map);

		if(list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Integer insertModelPathwayCodeAndName(String code, String name){
		ModelPathway modelPathway = new ModelPathway();
		modelPathway.setCode(code);
		modelPathway.setName(name);

		return (Integer) this.save(modelPathway);	
	}

	@Override
	public Integer getPathwayIdByName(String name) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("name", name);
		List<ModelPathway> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return res.get(0).getIdpathway();
		}
		return null;
	}

	@Override
	public Map<Integer, String> getPathwayIdAndNameByName(String name) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("name", name);
		List<ModelPathway> res = this.findByAttributes(map);
		Map<Integer, String> dic = new HashMap<Integer, String>();

		if (res!=null && res.size()>0) {
			dic.put(res.get(0).getIdpathway(), res.get(0).getName());
		}
		return dic;
	}

	@Override
	public String getPathwayCodeByName(String name) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("name", name);
		List<ModelPathway> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return res.get(0).getCode();
		}
		return null;
	}

	@Override
	public List<Integer> getPathwayIdByReactionId(Integer id ) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("idpathway", id);
		List<ModelPathway> res = this.findByAttributes(map);
		List<Integer> result = new ArrayList<Integer>();
		if (res!=null && res.size()>0) {
			for (ModelPathway x : res) {	
				result.add(x.getIdpathway());
			}
		}
		return result;
	}

	@Override
	public List<ModelPathway> getAllModelPathwayByCode(String code) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("code", code);
		List<ModelPathway> res = this.findByAttributes(map);

		return res;
	}

	@Override
	public List<ModelPathway> getAllModelPathwayByName(String name) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("name", name);
		List<ModelPathway> res = this.findByAttributes(map);

		return res;
	}

	@Override
	public List<ModelPathway> getAllModelPathwayByNameAndCode(String name, String code) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("name", name);
		map.put("code", code);
		List<ModelPathway> res = this.findByAttributes(map);

		return res;
	}

	@Override
	public String getModelPathwayNameByReactionId(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathway> c = cb.createQuery(ModelPathway.class);
		Root<ModelPathway> path = c.from(ModelPathway.class);
		Root<ModelPathwayHasReaction> pathHasReact = c.from(ModelPathwayHasReaction.class);

		c.multiselect(path.get("name"));

		Predicate filter1 = cb.equal(path.get("idpathway"), pathHasReact.get("id").get("modelPathwayIdpathway"));
		Predicate filter2 = cb.equal(pathHasReact.get("id").get("reactionIdreaction"), id);
		c.where(cb.and(filter1, filter2));

		Query<ModelPathway> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelPathway> resultList = q.getResultList();

		String res = null;
		if(resultList.size() > 0) {
			ModelPathway model = resultList.get(0);
			res =  model.getName();
		}
		return res;
	}

	@Override
	public List<String[]> getModelPathwayIdAndName(){ //sem aux
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathway> c = cb.createQuery(ModelPathway.class);
		Root<ModelPathway> path = c.from(ModelPathway.class);

		c.multiselect(path.get("idpathway"), path.get("name")).distinct(true);
		c.orderBy(cb.asc(path.get("name")));

		Query<ModelPathway> q = super.sessionFactory.getCurrentSession().createQuery(c);

		List<ModelPathway> resultList = q.getResultList();
		List<String[]> res = new ArrayList<String[]>();
		if (resultList != null && resultList.size()>0) {
			for (ModelPathway x: resultList){
				String[] result = new String[2];
				result[0] = String.valueOf(x.getIdpathway());
				result[1] = (String) x.getName();
				res.add(result);
			}
			return res;
		}
		return null;
	}

	@Override
	public List<PathwayContainer> getAllFromPathwaySortedByName() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathway> c = cb.createQuery(ModelPathway.class);
		Root<ModelPathway> path = c.from(ModelPathway.class);

		c.select(path);

		c.orderBy(cb.asc(path.get("name")));

		Query<ModelPathway> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelPathway> list = q.getResultList();
		
		List<PathwayContainer> containers = new ArrayList<>();
		
		if(list != null) {

			for(ModelPathway item: list) {
				PathwayContainer container = new PathwayContainer(item.getIdpathway(), item.getCode(), item.getName());
				container.setSbmlPath(item.getPathSbmlFile());
				
				containers.add(container);
			}
		}
		return containers;
	}

	@Override
	public Map<Integer, String> getAllModelPathwayIdAndName() {	
		List<ModelPathway> list =  this.findAll();
		Map<Integer, String> dic = new TreeMap<>();

		if(list.size() > 0) 
			for (ModelPathway x: list)
				dic.put(x.getIdpathway(), x.getName());

		return dic;
	}

	@Override
	public List<Object[]> getPathwayIdAndReactionId(boolean isCompartimentalized) {
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelPathway> path = c.from(ModelPathway.class);
		Root<ModelPathwayHasReaction> pathHasReact = c.from(ModelPathwayHasReaction.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.multiselect(path.get("idpathway"), cb.count(react.get("idreaction")));

		Predicate filter1 = cb.equal(path.get("idpathway"), pathHasReact.get("id").get("pathwayIdpathway"));
		Predicate filter2 = cb.equal(pathHasReact.get("id").get("reactionIdreaction"), react.get("idreaction"));
		
		Predicate filter3 = cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter3 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));
		
		c.where(cb.and(filter1, filter2, filter3)).groupBy(path.get("idpathway")).orderBy(cb.asc(path.get("name")));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		return q.getResultList();
	}
	
	@Override
	public List<String> getAllPathwaysOrderedByNameInModelWithReaction(Boolean inModel) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String[]> c = cb.createQuery(String[].class);
		Root<ModelPathway> path = c.from(ModelPathway.class);
		Root<ModelPathwayHasReaction> pathHasReact = c.from(ModelPathwayHasReaction.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.multiselect(path.get("idpathway"),path.get("name")).distinct(true);

		Predicate filter1 = cb.equal(path.get("idpathway"), pathHasReact.get("id").get("pathwayIdpathway"));
		Predicate filter2 = cb.equal(react.get("inModel"), inModel);
		c.where(cb.and(filter1, filter2));
		c.orderBy(cb.asc(path.get("name")));

		Query<String[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<String[]> result = q.getResultList();
		List<String> res1 = new ArrayList<String>();
		for(Object[] res: result)
			res1.add(String.valueOf(res[1]));
		return res1;
	}

	@Override
	public List<String> getAllPathwaysNamesOrdered() {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathway> c = cb.createQuery(ModelPathway.class);
		Root<ModelPathway> root = c.from(ModelPathway.class);
		c.select(root);
		c.orderBy(cb.asc(root.get("name")));
		Query<ModelPathway> q = sessionFactory.getCurrentSession().createQuery(c);
		List<ModelPathway> result = q.getResultList();
		List<String> l= new ArrayList<>();
		for(ModelPathway model : result )
			l.add(model.getName());
		return l;
	}

	@Override
	public List<String[]> getUpdatedPathways(boolean isCompartimentalized, boolean encodedOnly){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelPathway> path = c.from(ModelPathway.class);

		if(encodedOnly) {
			
			Root<ModelPathwayHasReaction> pathHasReact = c.from(ModelPathwayHasReaction.class);
			Root<ModelReaction> react = c.from(ModelReaction.class);
			Root<ModelReactionLabels> reactLabels = c.from(ModelReactionLabels.class);

			c.multiselect(path.get("idpathway"), path.get("name"), reactLabels.get("name")).distinct(true);

			Predicate filter1 = cb.equal(path.get("idpathway"), pathHasReact.get("id").get("pathwayIdpathway"));
			Predicate filter2 = cb.equal(react.get("idreaction"), pathHasReact.get("id").get("reactionIdreaction"));
			Predicate filter3 = cb.equal(react.get("modelReactionLabels").get("idreactionLabel"), reactLabels.get("idreactionLabel"));

			Predicate filter4 = cb.isNull(react.get("modelCompartment").get("idcompartment"));

			if(isCompartimentalized) 
				filter4 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

			Order[] orderList = {cb.asc(path.get("name")), cb.asc(reactLabels.get("name"))};

			Predicate filter5 = cb.equal(react.get("inModel"), true);
			
			c.where(cb.and(filter1, filter2, filter3, filter4, filter5)).orderBy(orderList);

		}
		else {
			c.multiselect(path.get("idpathway"), path.get("name"));

			c.orderBy(cb.asc(path.get("name")));
		}


		Query<Object[]> q = sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();


		List<String[]> result = new ArrayList<>();
		Set<Integer> existingPaths = new HashSet<>(); 

		if(resultList != null && !resultList.isEmpty()) {

			for(Object[] item : resultList) {
				String[] list = new String[2];

				if(!existingPaths.contains(Integer.valueOf(item[0]+""))) {

					existingPaths.add(Integer.valueOf(item[0]+""));
					list[0]=item[0]+"";
					list[1]=item[1]+"";

					result.add(list);
				}
			}
		}
		return result;
	}

	//	public Map<Integer, Integer> getPathwayIdAndReactionId() {
	//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
	//		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
	//		Root<ModelPathway> pathway = c.from(ModelPathway.class);
	//		Root<ModelPathwayHasReaction> pathwayHasReaction = c.from(ModelPathwayHasReaction.class);
	//		Root<ModelReaction> reaction = c.from(ModelReaction.class);
	//		
	//		if(ProjectAPI.isCompartmentalisedModel(stmt))
	//			aux = " NOT originalReaction ";
	//		
	//
	//		c.multiselect(pathway.get("idpathway"), cb.count(reaction.get("idreaction")));
	//		
	//		Predicate filter1 = cb.equal(pathway.get("idpathway"), pathwayHasReaction.get("id").get("modelPathwayIdpathway"));
	//		Predicate filter2 = cb.equal(reaction.get("idreaction"), pathwayHasReaction.get("id").get("reactionIdreaction"));
	//		Predicate filter3 = cb.equal(reaction.get("originalReaction"), aux);
	//		c.where(filter1, filter2, filter3).groupBy(pathway.get("idpathway"));
	//		c.orderBy(cb.asc(pathway.get("name")));
	//
	//		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
	//		List<Object[]> list = q.getResultList();
	//		Map<Integer, Integer> res = null;
	//		if (list.size() > 0) {
	//			for (Object[] x : list)
	//				res.put((Integer) x[0], (Integer) x[1]);
	//			}
	//		
	//		return res;
	//		return null;
	//	}
	//
	//	
	//	public Integer insertModelPathwayIdAndSuperPathway(Integer id, String name){
	//		ModelPathway modelPathway = new ModelPathway();
	//		modelPathway.setCode(code);
	//		modelPathway.setName(name);
	//	
	//		return (Integer) this.save(modelPathway);	
	//	}
	//	
	@Override
	public List<Object[]> countProteinIdByPathwayID() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelPathwayHasModelProtein> pathEnz = c.from(ModelPathwayHasModelProtein.class);	
		Join<ModelPathway,ModelPathwayHasModelProtein> joinEnzyme = pathEnz.join("modelPathway",JoinType.LEFT);
		

		c.multiselect(joinEnzyme.get("idpathway"),cb.count(pathEnz.get("id").get("modelProteinIdprotein")));
		
		Predicate filter1 = cb.equal(joinEnzyme.get("idpathway"), pathEnz.get("id").get("modelPathwayIdpathway"));
		
		c.where(filter1).groupBy(joinEnzyme.get("idpathway")).orderBy(cb.asc(joinEnzyme.get("name")));


		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();


		return resultList;
	}
	
	@Override
	public Map<String, Integer> getPathwayCodeAndIdpathway(){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathway> c = cb.createQuery(ModelPathway.class);
		Root<ModelPathway> pathway = c.from(ModelPathway.class);
		c.select(pathway);
		
		Predicate filter1 = cb.isNotNull(pathway.get("code"));
		c.where(cb.and(filter1));
		Query<ModelPathway> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelPathway> list = q.getResultList();
		
		Map<String, Integer> result = new HashMap<>();
		
		if(list.size() > 0) {
			for(ModelPathway pathwayItem: list) {
				result.put(pathwayItem.getCode(), pathwayItem.getIdpathway());
			}
		}
		return result;
	}
	
	@Override
	public Long countPathwayHasReaction(boolean isCompartimentalized){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Join<ModelReaction,ModelPathwayHasReaction> pathHasReaction = react.join("modelPathwayHasReactions",JoinType.INNER);

		c.select(cb.countDistinct(react.get("idreaction")));

		Predicate filter= cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		c.where(cb.and(filter));

		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Object result = q.uniqueResult();

		return (Long) result;
	}
	
}
