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
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionLabelsDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;

public class ModelReactionLabelsDAOImpl extends GenericDaoImpl<ModelReactionLabels> implements IModelReactionLabelsDAO {

	public ModelReactionLabelsDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelReactionLabels.class);
		
	}

	@Override
	public void addModelReactionLabels(ModelReactionLabels modelReaction) {
		super.save(modelReaction);
		
	}

	@Override
	public void addModelReactionLabelsList(List<ModelReactionLabels> modelReactionList) {
		for (ModelReactionLabels modelReaction: modelReactionList) {
			this.addModelReactionLabels(modelReaction);
		}
		
	}

	@Override
	public List<ModelReactionLabels> getAllModelReactionLabels() {
		return super.findAll();
	}

	@Override
	public ModelReactionLabels getModelReactionLabels(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelReactionLabels(ModelReactionLabels modelReaction) {
		super.delete(modelReaction);
		
	}

	@Override
	public void removeModelReactionLabelsList(List<ModelReactionLabels> modelReactionList) {
		for (ModelReactionLabels modelReaction: modelReactionList) {
			this.removeModelReactionLabels(modelReaction);
		}
		
	}

	@Override
	public void updateModelReactionLabelsList(List<ModelReactionLabels> modelReactionList) {
		for (ModelReactionLabels modelReaction: modelReactionList) {
			this.update(modelReaction);
		}
		
	}

	@Override
	public void updateModelReactionLabels(ModelReactionLabels modelReaction) {
		super.update(modelReaction);
		
	}
	
	@Override
	public Map<Integer, String> getLabelsByExternalIdentifiers(String name) {
		
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);

		Root<ModelReaction> table = c.from(ModelReaction.class); 
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class); 

		c.multiselect(table.get("idreaction"), reactionLabels.get("equation")); 

		Predicate filter1 = cb.equal(table.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter2 = cb.equal(reactionLabels.get("name"), name);
		Predicate filter3 = cb.like(reactionLabels.get("name"), name+"%_%");
		
		c.where(filter1, cb.or(filter2,filter3));
		
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		Map<Integer, String> reactions = new HashMap<>();
		
		
		if(resultList != null) {
			
			for(Object[] reaction : resultList) {

				reactions.put(Integer.valueOf(reaction[0].toString()), reaction[1].toString());
			}
		}
		
		return reactions;
	}
	
	@Override
	public List<Integer> getModelReactionsIdByName(String name, boolean isCompartimentalized){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);

		c.select(reactionLabels.get("idreactionLabel"));

		Predicate filter1= cb.equal(reactionLabels.get("name"), name);
		Predicate filter2= cb.isNull(react.get("modelCompartment").get("idcompartment"));
		
		if(isCompartimentalized) 
			filter2 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));
		
		Predicate filter3 = cb.equal(react.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));

		c.where(cb.and(filter1, filter2, filter3));

		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer> list = q.getResultList();
		

		return list;
	}
		
	@Override
	public List<ModelReactionLabels> getAllModelReactionLabelssByAttributes(String name, String equation, boolean generic, boolean spontaneous, 
			boolean nonEnzymatic, String reactionSource){ 
		
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("equation", equation);
		map.put("isGeneric", generic);
		map.put("isSpontaneous", spontaneous);
		map.put("isNonEnzymatic", nonEnzymatic);
		map.put("source", reactionSource);

		List<ModelReactionLabels> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return res;
		}
		return null;
	}
	
	@Override
	public void updateSourceByReactionLabelId(Integer reactionLabelId, String source) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("idreactionLabel", reactionLabelId);
		
		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("source", source);
		
		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}
	
	@Override
	public void updateEquationByReactionLabelId(Integer reactionLabelId, String equation) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("idreactionLabel", reactionLabelId);
		
		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("equation", equation);
		
		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}
	
	@Override
	public void updateAllModelReactionLabels(String name, String equation,
			Boolean isSpontaneous, Boolean isNonEnzymatic, Boolean isGeneric, Integer reactionLabelId) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("idreactionLabel", reactionLabelId);
		
		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("name", name);
		updateAttributes.put("equation", equation);
		updateAttributes.put("isSpontaneous", isSpontaneous);
		updateAttributes.put("isNonEnzymatic", isNonEnzymatic);
		updateAttributes.put("isGeneric", isGeneric);
		
		
		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}
	
	@Override
	public Integer insertModelReactionLabels(String name, String equation, boolean isSpontaneous, 
			boolean isGeneric, boolean isNonEnzymatic, String source) {
		ModelReactionLabels model = new ModelReactionLabels();
		model.setName(name);
		model.setEquation(equation);
		model.setIsGeneric(isGeneric);
		model.setIsSpontaneous(isSpontaneous);
		model.setIsGeneric(isGeneric);
		model.setIsNonEnzymatic(isNonEnzymatic);
		model.setSource(source);
		
		return (Integer) this.save(model);
	}
	
	@Override
	public ModelReactionLabels getModelReactionLabelByName(String name){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("name", name);

		return this.findUniqueByAttributes(dic);

	}
	
	@Override
	public boolean deleteModelReactionLabelsById(Integer reactionLabelId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("idreactionLabel", reactionLabelId);
	
		List<ModelReactionLabels> list =  this.findByAttributes(map);
		this.removeModelReactionLabelsList(list);
		return true;
	}
	
	@Override
	public String getModelReactionLabelsNameByReactionId(Integer id) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("idreactionLabel", id);
		List<ModelReactionLabels> list =  this.findByAttributes(map);
		return list.get(0).getName();
	}
	
	@Override
	public List<String[]> getModelReactionLabelsDataBySource(String source) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReactionLabels> react = c.from(ModelReactionLabels.class);

	    c.multiselect(react.get("idreactionLabel"));
	    
	    Predicate filter1 = cb.notEqual(react.get("source"), source);
	    
	    c.where(cb.and(filter1));
	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				String[] list = new String[1];
				list[0] = String.valueOf(item[0]);
				
				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}
	
	@Override
	public Integer getIdReactionLabelFromReactionId(Integer reactionId){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);
		
		Root<ModelReaction> react = c.from(ModelReaction.class);
		
		Join<ModelReaction, ModelReactionLabels> idReactionLabels = react.join("modelReactionLabels",JoinType.INNER);

		c.select(idReactionLabels.get("idreactionLabel"));

		Predicate filter1= cb.equal(react.get("idreaction"), reactionId);
		
		c.where(cb.and(filter1));

		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer> list = q.getResultList();
		
		Integer idReactionLabel = null;
		
		for(Integer i : list) {
			idReactionLabel = i;
			return idReactionLabel;
		}
			
		return idReactionLabel;
	}

}
