package pt.uminho.ceb.biosystems.merlin.dao.implementation.regulatory;


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

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory.IRegulatoryTranscriptionUnitPromoterDAO;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryEvent;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryTranscriptionUnit;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryTranscriptionUnitPromoter;

public class RegulatoryTranscriptionUnitPromoterDAOImpl extends GenericDaoImpl<RegulatoryTranscriptionUnitPromoter> implements IRegulatoryTranscriptionUnitPromoterDAO{

	public RegulatoryTranscriptionUnitPromoterDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, RegulatoryTranscriptionUnitPromoter.class);
		
	}

	@Override
	public void addRegulatoryTranscriptionUnitPromoter(RegulatoryTranscriptionUnitPromoter model) {
		super.save(model);
		
	}

	@Override
	public void addRegulatoryTranscriptionUnitPromoterList(List<RegulatoryTranscriptionUnitPromoter> modelList) {
		for (RegulatoryTranscriptionUnitPromoter model: modelList) {
			this.addRegulatoryTranscriptionUnitPromoter(model);
		}
		
	}

	@Override
	public List<RegulatoryTranscriptionUnitPromoter> getAllRegulatoryTranscriptionUnitPromoter() {
		return super.findAll();
	}

	@Override
	public RegulatoryTranscriptionUnitPromoter getRegulatoryTranscriptionUnitPromoter(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeRegulatoryTranscriptionUnitPromoter(RegulatoryTranscriptionUnitPromoter Model) {
		super.delete(Model);
		
	}

	@Override
	public void removeRegulatoryTranscriptionUnitPromoterList(List<RegulatoryTranscriptionUnitPromoter> modelList) {
		for (RegulatoryTranscriptionUnitPromoter model: modelList) {
			this.removeRegulatoryTranscriptionUnitPromoter(model);
		}
		
	}

	@Override
	public void updateRegulatoryTranscriptionUnitPromoterList(List<RegulatoryTranscriptionUnitPromoter> modelList) {
		for (RegulatoryTranscriptionUnitPromoter model: modelList) {
			this.update(model);
		}
		
	}

	@Override
	public void updateRegulatoryTranscriptionUnitPromoter(RegulatoryTranscriptionUnitPromoter model) {
		super.update(model);
		
	}
	
	@Override
	public Map<Integer, Integer> getRegulatoryTranscriptionUnitIdAndPromoterId(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<RegulatoryTranscriptionUnit> unit = c.from(RegulatoryTranscriptionUnit.class);
		Root<RegulatoryTranscriptionUnitPromoter> promoter = c.from(RegulatoryTranscriptionUnitPromoter.class);

	    c.multiselect(unit.get("idtranscriptionUnit"), cb.count(promoter.get("id").get("promoterIdpromoter")));
	    
		Predicate filter1 = cb.equal(unit.get("idtranscriptionUnit"), promoter.get("id").get("transcriptionUnitIdtranscriptionUnit")); 

	    c.where(cb.and(filter1)).groupBy(unit.get("idtranscriptionUnit"));
	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<Integer, Integer> res = null;
		if(list.size() > 0) {
			res = new HashMap<Integer, Integer>();
			for(Object[] result: list) {
				
				res.put((Integer)result[0], (Integer)result[1]);
			}
		}
		return res;
	}
	
	@Override
	public Map<Integer, Integer> getRegulatoryTranscriptionUnitPromoterIdAndTranscriptionId(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<RegulatoryTranscriptionUnitPromoter> promoter = c.from(RegulatoryTranscriptionUnitPromoter.class);

	    c.multiselect(promoter.get("id").get("promoterIdpromoter"), cb.count(promoter.get("id").get("transcriptionUnitIdtranscriptionUnit")));
	    Order[] orderList = {cb.asc(promoter.get("id").get("promoterIdpromoter"))};
	    c.groupBy(promoter.get("id").get("promoterIdpromoter"));
	    c.orderBy(orderList);
	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<Integer, Integer> res = null;
		if(list.size() > 0) {
			res = new HashMap<Integer, Integer>();
			for(Object[] result: list) {
				
				res.put((Integer)result[0], (Integer)result[1]);
			}
		}
		return res;
	}
	
	@Override
	public Long getRegulatoryTranscriptionUnitPromoterDistinctIdAndTranscriptionId(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<RegulatoryTranscriptionUnitPromoter> promoter = c.from(RegulatoryTranscriptionUnitPromoter.class);
		Root<RegulatoryEvent> event = c.from(RegulatoryEvent.class);

	    c.multiselect(cb.countDistinct(promoter.get("id").get("promoterIdpromoter")));
	    Predicate filter1 = cb.equal(promoter.get("id").get("promoterIdpromoter"),event.get("id").get("promoterIdpromoter")); 

	    c.where(cb.and(filter1));
	    
	    Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		
		Object result = q.uniqueResult();
		
		return (Long) result;
	}
	
	@Override
	public List<String> getRegulatoryTranscriptionUnitNameByPromoterId(Integer id){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<RegulatoryTranscriptionUnit> c = cb.createQuery(RegulatoryTranscriptionUnit.class);
		Root<RegulatoryTranscriptionUnit> unit = c.from(RegulatoryTranscriptionUnit.class);
		Root<RegulatoryTranscriptionUnitPromoter> promoter = c.from(RegulatoryTranscriptionUnitPromoter.class);

	    c.multiselect(unit.get("name"));
	    
		Predicate filter1 = cb.equal(unit.get("idtranscriptionUnit"), promoter.get("id").get("transcriptionUnitIdtranscriptionUnit")); 
		Predicate filter2 = cb.equal(promoter.get("id").get("promoterIdpromoter"), id);

	    c.where(cb.and(filter1, filter2));
	    
	    Query<RegulatoryTranscriptionUnit> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<RegulatoryTranscriptionUnit> list = q.getResultList();
		List<String> res = new ArrayList<String>();
		
		if(list.size() > 0) {
			for(RegulatoryTranscriptionUnit result: list) {
				res.add(result.getName());
			}
		}
		return res;
	}

	@Override
	public Long getRegulatoryTranscriptionUnitPromoterDistinctId(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<RegulatoryTranscriptionUnitPromoter> promoter = c.from(RegulatoryTranscriptionUnitPromoter.class);

	    c.multiselect(cb.count(promoter.get("id").get("promoterIdpromoter"))).distinct(true);
	   
	    Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		
		Object result = q.uniqueResult();
		
		return (Long) result;
	}

}
