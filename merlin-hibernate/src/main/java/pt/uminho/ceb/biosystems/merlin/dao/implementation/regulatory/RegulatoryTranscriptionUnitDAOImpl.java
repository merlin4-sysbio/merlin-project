package pt.uminho.ceb.biosystems.merlin.dao.implementation.regulatory;

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
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory.IRegulatoryTranscriptionUnitDAO;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryPromoter;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryTranscriptionUnit;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryTranscriptionUnitPromoter;

public class RegulatoryTranscriptionUnitDAOImpl extends GenericDaoImpl<RegulatoryTranscriptionUnit> implements IRegulatoryTranscriptionUnitDAO{

	public RegulatoryTranscriptionUnitDAOImpl(SessionFactory sessionFactory, Class<RegulatoryTranscriptionUnit> klass) {
		super(sessionFactory, RegulatoryTranscriptionUnit.class);

	}

	@Override 
	public void addRegulatoryTranscriptionUnit(RegulatoryTranscriptionUnit RegulatoryTranscriptionUnit) {
		super.save(RegulatoryTranscriptionUnit);

	}
	
	@Override
	public Integer insertTranscriptionUnitName(String gene){
		RegulatoryTranscriptionUnit transcriptionUnit = new RegulatoryTranscriptionUnit();
		transcriptionUnit.setName(gene);
	
		return (Integer) this.save(transcriptionUnit);	
	}
	
	@Override
	public boolean checkTranscriptionUnitNameExists(String name) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("name", name);

		List<RegulatoryTranscriptionUnit> list =  this.findByAttributes(map);

		if(list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override 
	public void addRegulatoryTranscriptionUnitList(List<RegulatoryTranscriptionUnit> RegulatoryTranscriptionUnitList) {
		for (RegulatoryTranscriptionUnit RegulatoryTranscriptionUnit: RegulatoryTranscriptionUnitList) {
			this.addRegulatoryTranscriptionUnit(RegulatoryTranscriptionUnit);
		}

	}

	@Override 
	public List<RegulatoryTranscriptionUnit> getAllRegulatoryTranscriptionUnit() {
		return super.findAll();
	}

	@Override 
	public RegulatoryTranscriptionUnit getRegulatoryTranscriptionUnit(Integer id) {
		return super.findById(id);
	}

	@Override 
	public void removeRegulatoryTranscriptionUnit(RegulatoryTranscriptionUnit RegulatoryTranscriptionUnit) {
		super.delete(RegulatoryTranscriptionUnit);

	}

	@Override 
	public void removeRegulatoryTranscriptionUnitList(List<RegulatoryTranscriptionUnit> RegulatoryTranscriptionUnitList) {
		for (RegulatoryTranscriptionUnit RegulatoryTranscriptionUnit: RegulatoryTranscriptionUnitList) {
			this.removeRegulatoryTranscriptionUnit(RegulatoryTranscriptionUnit);
		}

	}

	@Override 
	public void updateRegulatoryTranscriptionUnitList(List<RegulatoryTranscriptionUnit> RegulatoryTranscriptionUnitList) {
		for (RegulatoryTranscriptionUnit RegulatoryTranscriptionUnit: RegulatoryTranscriptionUnitList) {
			this.update(RegulatoryTranscriptionUnit);
		}

	}

	@Override 
	public void updateRegulatoryTranscriptionUnit(RegulatoryTranscriptionUnit RegulatoryTranscriptionUnit) {
		super.update(RegulatoryTranscriptionUnit);
	}



	@Override 
	public List<String> getRegulatoryTranscriptionUnitPromoterNameById(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<RegulatoryTranscriptionUnit> c = cb.createQuery(RegulatoryTranscriptionUnit.class);
		Root<RegulatoryTranscriptionUnitPromoter> promoter = c.from(RegulatoryTranscriptionUnitPromoter.class);
		Root<RegulatoryTranscriptionUnit> unit = c.from(RegulatoryTranscriptionUnit.class);
		Root<RegulatoryPromoter> prom = c.from(RegulatoryPromoter.class);

		c.multiselect(unit.get("name")); 
		Predicate filter1 = cb.equal(unit.get("idtranscriptionUnit"), promoter.get("id").get("transcriptionUnitIdtranscriptionUnit"));
		Predicate filter2 = cb.equal(prom.get("idpromoter"), promoter.get("id").get("promoterIdpromoter"));
		Predicate filter3 = cb.equal(promoter.get("id").get("transcriptionUnitIdtranscriptionUnit"), id);

		c.where(filter1, filter2, filter3);

		Query<RegulatoryTranscriptionUnit> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<RegulatoryTranscriptionUnit> list = q.getResultList();
		List<String> resultList = null;
		if(list.size() > 0) {
			resultList = new ArrayList<String>();
			for(RegulatoryTranscriptionUnit result: list) {

				resultList.add(result.getName());
			}	    
		}
		return null;
	}

}



