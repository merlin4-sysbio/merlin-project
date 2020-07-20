package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelOrthologyDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelOrthology;

public class ModelOrthologyDAOImpl extends GenericDaoImpl<ModelOrthology> implements IModelOrthologyDAO{

	public ModelOrthologyDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelOrthology.class);
		
	}

	@Override
	public void addModelOrthology(ModelOrthology modelOrthology) {
		super.save(modelOrthology);
		
	}

	@Override
	public void addModelOrthologyList(List<ModelOrthology> modelOrthologyList) {
		for (ModelOrthology modelOrthology: modelOrthologyList) {
			this.addModelOrthology(modelOrthology);
		}
		
	}

	@Override
	public List<ModelOrthology> getAllModelOrthology() {
		return super.findAll();
	}

	@Override
	public ModelOrthology getModelOrthology(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelOrthology(ModelOrthology modelOrthology) {
		super.delete(modelOrthology);
		
	}

	@Override
	public void removeModelOrthologyList(List<ModelOrthology> modelOrthologyList) {
		for (ModelOrthology modelOrthology: modelOrthologyList) {
			this.removeModelOrthology(modelOrthology);
		}
		
	}

	@Override
	public void updateModelOrthologyList(List<ModelOrthology> modelOrthologyList) {
		for (ModelOrthology modelOrthology: modelOrthologyList) {
			this.update(modelOrthology);
		}
		
	}

	@Override
	public void updateModelOrthology(ModelOrthology modelOrthology) {
		super.update(modelOrthology);
		
	}
	
	@Override
	public List<String> getModelOrthologyLocusIdbyEntryId(String entryId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("entryId", entryId);
		List<ModelOrthology> res = this.findByAttributes(map);
		List<String> result = new ArrayList<String>();
		if (res!=null && res.size()>0) {
			result.add(res.get(0).getLocusId());
		}
		return result;
	}
	
	@Override
	public List<ModelOrthology> getAllModelOrthologyByEntryIdAndLocusId(String entyID, String locusID) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("entryId", entyID);
		dic.put("locusId", locusID);
		List<ModelOrthology> list =  this.findByAttributes(dic);

		return list;
	}
	
	@Override
	public List<ModelOrthology> getAllModelOrthologyByEntryId(Integer gene) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id", gene);
		List<ModelOrthology> list =  this.findByAttributes(dic);

		return list;
	}

	@Override
	public Integer insertModelOrthologyEntryId(String gene){
		ModelOrthology modelOrthology = new ModelOrthology();
		modelOrthology.setEntryId(gene);;
	
		return (Integer) this.save(modelOrthology);	
	}

	@Override
	public void updateModelOrthologyLocusIdByEntryId(String entryid, String locusid) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("entryId", entryid);
		
		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("locusId", locusid);
		
		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public Integer getModelOrthologyIdByEntryIdAndLocusId(String entryId, String locusId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("entryId", entryId);
		map.put("locusId", locusId);
		List<ModelOrthology> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return res.get(0).getId();
		}
		return null;
	}

	@Override
	public Integer insertModelOrthologyEntryIdAndLocusId(String entryid, String locusId){
		ModelOrthology modelOrthology = new ModelOrthology();
		modelOrthology.setEntryId(entryid);
		modelOrthology.setLocusId(locusId);
	
		return (Integer) this.save(modelOrthology);	
	}
	
	@Override
	public Map<String, Integer> getEntryIdAndOrthologyId(){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelOrthology> c = cb.createQuery(ModelOrthology.class);
		Root<ModelOrthology> orth = c.from(ModelOrthology.class);
		c.select(orth);
		
		Predicate filter1 = cb.isNotNull(orth.get("entryId"));
		c.where(cb.and(filter1));
		Query<ModelOrthology> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelOrthology> list = q.getResultList();
		
		Map<String, Integer> result = new HashMap<>();
		
		if(list.size() > 0) {
			for(ModelOrthology orthItem: list) {
				result.put(orthItem.getEntryId(), orthItem.getId());
			}
		}
		return result;
	}
	
}
