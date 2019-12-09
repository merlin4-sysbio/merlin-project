package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
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
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationGeneHomologyHasHomologuesDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberRank;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomologyHasHomologues;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomologyHasHomologuesId;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologySetup;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductRank;



public class EnzymesAnnotationGeneHomologyHasHomologuesDAOImpl extends GenericDaoImpl<EnzymesAnnotationGeneHomologyHasHomologues> implements IEnzymesAnnotationGeneHomologyHasHomologuesDAO {

	public EnzymesAnnotationGeneHomologyHasHomologuesDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationGeneHomologyHasHomologues.class);
		
	}

	@Override
	public void addEnzymesAnnotationGeneHomologyHasHomologues(
			EnzymesAnnotationGeneHomologyHasHomologues EnzymesAnnotationGeneHomologyHasHomologues) {
		super.save(EnzymesAnnotationGeneHomologyHasHomologues);

	}

	@Override
	public void addEnzymesAnnotationGeneHomologyHasHomologuesList(List<EnzymesAnnotationGeneHomologyHasHomologues> EnzymesAnnotationGeneHomologyHasHomologuesList) {
		for (EnzymesAnnotationGeneHomologyHasHomologues EnzymesAnnotationGeneHomologyHasHomologues: EnzymesAnnotationGeneHomologyHasHomologuesList) {
			this.addEnzymesAnnotationGeneHomologyHasHomologues(EnzymesAnnotationGeneHomologyHasHomologues);
		}
	}

	@Override
	public List<EnzymesAnnotationGeneHomologyHasHomologues> getAllEnzymesAnnotationGeneHomologyHasHomologues() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationGeneHomologyHasHomologues getEnzymesAnnotationGeneHomologyHasHomologues(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationGeneHomologyHasHomologues(EnzymesAnnotationGeneHomologyHasHomologues EnzymesAnnotationGeneHomologyHasHomologues) {
		super.delete(EnzymesAnnotationGeneHomologyHasHomologues);
		
	}

	@Override
	public void removeEnzymesAnnotationGeneHomologyHasHomologuesList(List<EnzymesAnnotationGeneHomologyHasHomologues> EnzymesAnnotationGeneHomologyHasHomologuesList) {
		for (EnzymesAnnotationGeneHomologyHasHomologues EnzymesAnnotationGeneHomologyHasHomologues: EnzymesAnnotationGeneHomologyHasHomologuesList) {
			this.removeEnzymesAnnotationGeneHomologyHasHomologues(EnzymesAnnotationGeneHomologyHasHomologues);
		}
	}

	@Override
	public void updateEnzymesAnnotationGeneHomologyHasHomologuesList(List<EnzymesAnnotationGeneHomologyHasHomologues> EnzymesAnnotationGeneHomologyHasHomologuesList) {
		for (EnzymesAnnotationGeneHomologyHasHomologues EnzymesAnnotationGeneHomologyHasHomologues: EnzymesAnnotationGeneHomologyHasHomologuesList) {
			this.update(EnzymesAnnotationGeneHomologyHasHomologues);
		}
	}

	@Override
	public void updateEnzymesAnnotationGeneHomologyHasHomologues(EnzymesAnnotationGeneHomologyHasHomologues EnzymesAnnotationGeneHomologyHasHomologues) {
		super.update(EnzymesAnnotationGeneHomologyHasHomologues);
		
	}
	
	@Override
	public String getEnzymesAnnotationGeneHomologyHasHomologuesQueryByLocusTag(String locus) {
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String> c = cb.createQuery(String.class);
		Root<EnzymesAnnotationGeneHomologyHasHomologues> homologues = c.from(EnzymesAnnotationGeneHomologyHasHomologues.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		
		c.multiselect(homology.get("query")); 
	    Predicate filter1 = cb.equal(homologues.get("id").get("geneHomologySKey"), homology.get("SKey"));
	    Predicate filter2 = cb.equal(homology.get("enzymesAnnotationHomologySetup").get("SKey"), setup.get("SKey"));
	    Predicate filter3 = cb.equal(homology.get("locusTag"), locus);
	    
	    c.where(cb.and(filter1, filter2, filter3)).groupBy(setup.get("program"));
	    Query<String> q = super.sessionFactory.getCurrentSession().createQuery(c);
		String resultList = q.getSingleResult();
		return resultList;
	}
	
	@Override
	public Map<Integer, Long> getEnzymesAnnotationGeneHomologyHasHomologuesAttributes(){  
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomologyHasHomologues> homologues = c.from(EnzymesAnnotationGeneHomologyHasHomologues.class);
		Root<EnzymesAnnotationEcNumberRank> ec = c.from(EnzymesAnnotationEcNumberRank.class);
		
		c.multiselect(ec.get("SKey"), cb.count(homologues.get("id").get("homologuesSKey")));
		
	    Predicate filter1 = cb.equal(homologues.get("id").get("geneHomologySKey"), ec.get("enzymesAnnotationGeneHomology").get("SKey"));
	    
	    c.where(cb.and(filter1)).groupBy(ec.get("SKey"));
	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		
	    List<Object[]> list = q.getResultList();
			    
		Map<Integer,Long> res = null;
		if(list.size() > 0) {
			res = new HashMap<Integer,Long>();
			for(Object[] result: list) {
				res.put((Integer)result[0], (Long)result[1]);
			}
		}
		return res;
	}

	@Override
	public Map<Integer,Long> getEnzymesAnnotationGeneHomologyHasHomologuesAttributes2(){  
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomologyHasHomologues> homologues = c.from(EnzymesAnnotationGeneHomologyHasHomologues.class);
		Root<EnzymesAnnotationProductRank> product = c.from(EnzymesAnnotationProductRank.class);
		
		c.multiselect(product.get("SKey"), cb.count(homologues.get("id").get("homologuesSKey"))); 
	    Predicate filter1 = cb.equal(product.get("enzymesAnnotationGeneHomology").get("SKey"), homologues.get("enzymesAnnotationGeneHomology").get("SKey"));
	    
	    c.where(cb.and(filter1)).groupBy(product.get("SKey"));
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<Integer,Long> res = null;
		if(list.size() > 0) {
			res = new HashMap<Integer,Long>();
			for(Object[] result: list) {
				res.put((Integer)result[0], (Long)result[1]);
			}
		}
		return res;
	}
	
	@Override
	public List<EnzymesAnnotationGeneHomologyHasHomologues> getAllEnzymesAnnotationGeneHomologyHasHomologuesByAttributes(Integer homologyskey, Integer homologuesSkey, String referenceId, Float evalue, Float bits, String gene) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("evalue", evalue);
		dic.put("bits", bits);
		dic.put("referenceId", referenceId);
		dic.put("enzymesAnnotationGeneHomology.SKey", homologyskey);
		dic.put("enzymesAnnotationHomologues.SKey", homologuesSkey);
		dic.put("gene", gene);
		List<EnzymesAnnotationGeneHomologyHasHomologues> list =  this.findByAttributes(dic);

		return list;
	}

	@Override
	public void insertEnzymesAnnotationGeneHomologyHasHomologues(Integer geneHomology_s_key, String referenceID, String gene, Float eValue, Float bits, Integer homologues_s_key){
		EnzymesAnnotationGeneHomologyHasHomologues hasHomologues = new EnzymesAnnotationGeneHomologyHasHomologues();
		EnzymesAnnotationGeneHomologyHasHomologuesId id = new EnzymesAnnotationGeneHomologyHasHomologuesId();
		id.setGeneHomologySKey(geneHomology_s_key);
		id.setHomologuesSKey(homologues_s_key);
		hasHomologues.setBits(bits);
		hasHomologues.setEvalue(eValue);
		hasHomologues.setReferenceId(referenceID);
		hasHomologues.setId(id);
		this.save(hasHomologues);	
	}
}
