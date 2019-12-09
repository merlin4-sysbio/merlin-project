package pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasReactionDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathway;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasReactionId;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;

public class ModelPathwayHasReactionDAOImpl extends GenericDaoImpl<ModelPathwayHasReaction> implements IModelPathwayHasReactionDAO{

	public ModelPathwayHasReactionDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelPathwayHasReaction.class);
		
	}

	public void addModelPathwayHasReaction(ModelPathwayHasReaction modelPathwayHasReaction) {
		super.save(modelPathwayHasReaction);
		
	}

	public void addModelPathwayHasReactionList(List<ModelPathwayHasReaction> modelPathwayHasReactionList) {
		for (ModelPathwayHasReaction modelPathwayHasReaction: modelPathwayHasReactionList) {
			this.addModelPathwayHasReaction(modelPathwayHasReaction);
		}
		
	}

	public List<ModelPathwayHasReaction> getAllModelPathwayHasReaction() {
		return super.findAll();
	}

	public ModelPathwayHasReaction getModelPathwayHasReaction(Integer id) {
		return super.findById(id);
	}

	public void removeModelPathwayHasReaction(ModelPathwayHasReaction modelPathwayHasReaction) {
		super.delete(modelPathwayHasReaction);
		
	}

	public void removeModelPathwayHasReactionList(List<ModelPathwayHasReaction> modelPathwayHasReactionList) {
		for (ModelPathwayHasReaction modelPathwayHasReaction: modelPathwayHasReactionList) {
			this.removeModelPathwayHasReaction(modelPathwayHasReaction);
		}
		
	}

	public void updateModelPathwayHasReactionList(List<ModelPathwayHasReaction> modelPathwayHasReactionList) {
		for (ModelPathwayHasReaction modelPathwayHasReaction: modelPathwayHasReactionList) {
			this.update(modelPathwayHasReaction);
		}
		
	}

	public void updateModelPathwayHasReaction(ModelPathwayHasReaction modelPathwayHasReaction) {
		super.update(modelPathwayHasReaction);
	}

	public List<ModelPathwayHasReaction> getAllModelPathwayHasReactionByAttributes(Integer reactionId, Integer pathwayId){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		
		dic.put("id.pathwayIdpathway", pathwayId);
		dic.put("id.reactionIdreaction", reactionId);

		List<ModelPathwayHasReaction> list =  this.findByAttributes(dic);

		return list;
	}
	
	public List<ModelPathwayHasReaction> getAllModelPathwayHasReactionByReactionId(Integer idReaction){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.reactionIdreaction", idReaction);
		List<ModelPathwayHasReaction> list =  this.findByAttributes(dic);

		return list;
	}
	
	public List<PathwayContainer> getModelPathwayHasReactionIdByReactionId(Integer idReaction){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.reactionIdreaction", idReaction);
		List<ModelPathwayHasReaction> list =  this.findByAttributes(dic);
		List<PathwayContainer> result = new ArrayList<>();
		
		for (ModelPathwayHasReaction x : list) {
			result.add(new PathwayContainer(x.getModelPathway().getIdpathway(), x.getModelPathway().getCode(), x.getModelPathway().getName()));
		}
		return result;
	}
	
	public Integer getModelPathwayHasReactionIdByReactionIdAndPathId(Integer idReaction, Integer pathId){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.reactionIdreaction", idReaction);
		dic.put("id.pathwayIdpathway", pathId);
		List<ModelPathwayHasReaction> list =  this.findByAttributes(dic);
		if (list !=null && list.size()>0) {
			return list.get(0).getId().getReactionIdreaction();
		}
		return null;
	}
	
	
	public void insertModelPathwayHasReaction(Integer reactionId, Integer pathwayId) {
		ModelPathwayHasReaction model = new ModelPathwayHasReaction();
		ModelPathwayHasReactionId id = new ModelPathwayHasReactionId();
		id.setReactionIdreaction(reactionId);
		id.setPathwayIdpathway(pathwayId);
		model.setId(id);
		this.save(model);
	}

	
	public List<String> getModelPathwayHasReactionAttributes() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String> c = cb.createQuery(String.class);
		Root<ModelPathwayHasReaction> pathHasReact = c.from(ModelPathwayHasReaction.class);
		Root<ModelPathway> path = c.from(ModelPathway.class);
		
		c.multiselect(path.get("name"), path.get("idpathway"), pathHasReact.get("id").get("pathwayIdpathway"));
		Predicate filter1 = cb.equal(path.get("idpathway"), pathHasReact.get("id").get("reactionIdreaction"));
	    Order[] orderList = {cb.asc(pathHasReact.get("id").get("reactionIdreaction"))};
	    c.where(cb.and(filter1)).orderBy(orderList);
	    
	    Query<String> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<String> list = q.getResultList();
		return list;
	}
	
	
	public List<String[]> getModelPathwayHasReactionData(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelPathwayHasReaction> pathReac = c.from(ModelPathwayHasReaction.class);
		Root<ModelPathway> path = c.from(ModelPathway.class);

	    c.multiselect(pathReac.get("id").get("reactionIdreaction"), path.get("idpathway"), path.get("name"));
		Predicate filter1 = cb.equal(path.get("idpathway"), pathReac.get("id").get("pathwayIdpathway"));
	    Order[] orderList = {cb.asc(pathReac.get("id").get("reactionIdreaction"))}; 

	    c.where(cb.and(filter1)).orderBy(orderList);
	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				String[] list = new String[3];
				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				list[2] = (String) item[2];
				
				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}
	
	@Override
	public Map<Integer, ReactionContainer> getModelPathwayHasReactionIdAndPathIdByAttributes(String source, Boolean isCompartimentalized){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelPathwayHasReaction> pathReac = c.from(ModelPathwayHasReaction.class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		
	    c.multiselect(reaction.get("idreaction"),  pathReac.get("id").get("pathwayIdpathway"),
	    		pathReac.get("modelPathway").get("code"), pathReac.get("modelPathway").get("name"));
	    
		Predicate filter1 = cb.equal(reaction.get("idreaction"), pathReac.get("id").get("reactionIdreaction"));
		Predicate filter2 = cb.equal(reaction.get("modelReactionLabels").get("source"), source);
		Predicate filter3 = cb.isNull(reaction.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter3 = cb.isNotNull(reaction.get("modelCompartment").get("idcompartment"));
	    
		c.where(cb.and(filter1, filter2, filter3));
	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		
		Map<Integer, ReactionContainer> containers = new HashMap<>();
		
		if(resultList != null) {
			for(Object[] item: resultList) {

				Integer reactionId = Integer.valueOf(item[0].toString());
				
				if(!containers.containsKey(reactionId))
					containers.put(reactionId, new ReactionContainer(reactionId));
				
				containers.get(reactionId).addPathway(Integer.valueOf(item[1].toString()), item[2].toString(), item[3].toString());
			}
		}
		return containers;
	}
	
	@Override
	public List<Integer> getAllModelPathwayHasReactionIdReactionByPathId(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathwayHasReaction> c = cb.createQuery(ModelPathwayHasReaction.class);
		Root<ModelPathwayHasReaction> pathReact = c.from(ModelPathwayHasReaction.class);
		
		c.select(pathReact).distinct(true); 

	    Predicate filter1 = cb.equal(pathReact.get("id").get("pathwayIdpathway"), id);
	    c.where(cb.and(filter1));
	    
	    Query<ModelPathwayHasReaction> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelPathwayHasReaction> list = q.getResultList();

		List<Integer> res = new ArrayList<Integer>();
		
		if (list != null) {
			for (ModelPathwayHasReaction x : list) {
				res.add(x.getId().getReactionIdreaction());
			}
		}
		return res;
	}
	
	public List<String[]> getPathwayHasReactionAttributes(Integer id, boolean isCompartimentalized) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		Root<ModelPathwayHasReaction> pathHasReaction = c.from(ModelPathwayHasReaction.class);
		Root<ModelReactionLabels> label = c.from(ModelReactionLabels.class);

		c.multiselect(reaction.get("idreaction"), label.get("name"), label.get("equation")).distinct(true); //distinct so no idreaction
		Predicate filter1 = cb.equal(reaction.get("idreaction"), pathHasReaction.get("id").get("reactionIdreaction"));
		Predicate filter2 = cb.equal(pathHasReaction.get("id").get("pathwayIdpathway"), id);
		Predicate filter3 = cb.isNull(reaction.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter3 = cb.isNotNull(reaction.get("modelCompartment").get("idcompartment"));
		
		Predicate filter4= cb.equal(reaction.get("modelReactionLabels").get("idreactionLabel"), label.get("idreactionLabel"));
	
		c.where(cb.and(filter1, filter2, filter3,filter4));
	
		c.orderBy(cb.asc(label.get("name")));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		
		ArrayList<String[]> parsedList = new ArrayList<String[]>();
		
		if(list.size() > 0) {
			
			for(Object[] item: list) {
				
				String[] result = new String[3];
				result[0] = String.valueOf(item[0]);
				result[1] = (String) item[1];
				result[2] = (String) item[2];
				
				parsedList.add(result);
			}
			return parsedList;
		}
		return parsedList;
	}
	
	public boolean deleteModelPathwayHasReactionByReactionIdAndPathwayId(Integer reactionId, Integer pathId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.reactionIdreaction", reactionId);
		map.put("id.pathwayIdpathway", pathId);
	
		List<ModelPathwayHasReaction> list =  this.findByAttributes(map);
		this.removeModelPathwayHasReactionList(list);
		return true;
	}
	
	public boolean deleteModelPathwayHasReactionByPathwayId(Integer id) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.pathwayIdpathway", id);
	
		List<ModelPathwayHasReaction> list =  this.findByAttributes(map);
		this.removeModelPathwayHasReactionList(list);
		return true;
	}
	
	public boolean deleteModelPathwayHasReactionByReactionId(Integer id) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.reactionIdreaction", id);
	
		List<ModelPathwayHasReaction> list =  this.findByAttributes(map);
		this.removeModelPathwayHasReactionList(list);
		return true;
	}
	

}
