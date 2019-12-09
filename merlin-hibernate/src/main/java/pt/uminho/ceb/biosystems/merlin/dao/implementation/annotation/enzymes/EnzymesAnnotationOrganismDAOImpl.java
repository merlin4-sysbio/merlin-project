package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationOrganismDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationOrganism;


public class EnzymesAnnotationOrganismDAOImpl extends GenericDaoImpl<EnzymesAnnotationOrganism> implements IEnzymesAnnotationOrganismDAO {

	public EnzymesAnnotationOrganismDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationOrganism.class);
	}

	@Override
	public void addEnzymesAnnotationOrganism(EnzymesAnnotationOrganism enzymesAnnotationOrganism) {
		super.save(enzymesAnnotationOrganism);
	}

	@Override
	public void addEnzymesAnnotationOrganismList(List<EnzymesAnnotationOrganism> enzymesAnnotationOrganismList) {
		for (EnzymesAnnotationOrganism enzymesAnnotationOrganism: enzymesAnnotationOrganismList) {
			this.addEnzymesAnnotationOrganism(enzymesAnnotationOrganism);
		}
	}
	
	@Override
	public List<EnzymesAnnotationOrganism> getAllEnzymesAnnotationOrganism() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationOrganism getEnzymesAnnotationOrganism(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationOrganism(EnzymesAnnotationOrganism enzymesAnnotationOrganism) {
		super.delete(enzymesAnnotationOrganism);
		
	}

	@Override
	public void removeEnzymesAnnotationOrganismList(List<EnzymesAnnotationOrganism> enzymesAnnotationOrganismList) {
		for (EnzymesAnnotationOrganism enzymesAnnotationOrganism: enzymesAnnotationOrganismList) {
			this.removeEnzymesAnnotationOrganism(enzymesAnnotationOrganism);
		}
	}

	@Override
	public void updateEnzymesAnnotationOrganismList(List<EnzymesAnnotationOrganism> enzymesAnnotationOrganismList) {
		for (EnzymesAnnotationOrganism enzymesAnnotationOrganism: enzymesAnnotationOrganismList) {
			this.update(enzymesAnnotationOrganism);
		}
	}

	@Override
	public void updateEnzymesAnnotationOrganism(EnzymesAnnotationOrganism enzymesAnnotationOrganism) {
		super.update(enzymesAnnotationOrganism);
		
	}
	
	@Override
	public Integer getEnzymesAnnotationOrganismMaxTaxRank() {
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);
		Root<EnzymesAnnotationOrganism> org = c.from(EnzymesAnnotationOrganism.class);
	
	    c.select(cb.max(org.get("taxRank")));
	    
		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		
		int res = -1;
		
		if(q.getSingleResult()!=null)
			res = q.getSingleResult(); 
		
		return res;
	}
	
	@Override
	public List<EnzymesAnnotationOrganism> getAllEnzymesAnnotationOrganismByOrganism(String organism){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("organism", organism);
		List<EnzymesAnnotationOrganism> list =  this.findByAttributes(dic);

		return list;
	}
	
	@Override
	public Long getEnzymesAnnotationOrganismCountDistinctOrganism(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<EnzymesAnnotationOrganism> org = c.from(EnzymesAnnotationOrganism.class);
		
		c.multiselect(cb.count(org.get("organism"))).distinct(true);
	
		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Object result = q.uniqueResult();
		
		return (Long) result;
	}
	
	@Override
	public List<String> getEnzymesAnnotationOrganismDistinctOrganism(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String> c = cb.createQuery(String.class);
		Root<EnzymesAnnotationOrganism> org = c.from(EnzymesAnnotationOrganism.class);
		
		c.multiselect(org.get("organism")).distinct(true);
		
		Query<String> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<String> resultList = q.getResultList();
		
		return resultList;
	}
	
	@Override
	public List<String> getEnzymesAnnotationOrganismTaxonomy(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String> c = cb.createQuery(String.class);
		Root<EnzymesAnnotationOrganism> org = c.from(EnzymesAnnotationOrganism.class);
		
		c.multiselect(org.get("taxonomy"));
		
		Query<String> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<String> resultList = q.getResultList();
		
		return resultList;
	}
	
	
	@Override
	public Integer insertEnzymesAnnotationOrganism(String organism, String taxonomy, Integer taxrank) {

		EnzymesAnnotationOrganism org = new EnzymesAnnotationOrganism();
		org.setOrganism(organism);
		org.setTaxonomy(taxonomy);
		org.setTaxRank(taxrank);
		
		return (Integer) this.save(org);
	}
}

