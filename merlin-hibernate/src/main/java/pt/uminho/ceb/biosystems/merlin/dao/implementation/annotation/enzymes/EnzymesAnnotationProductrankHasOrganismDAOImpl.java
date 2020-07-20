package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

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

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationProductrankHasOrganismDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberRankHasOrganism;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationOrganism;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductRankHasOrganism;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductRankHasOrganismId;



public class EnzymesAnnotationProductrankHasOrganismDAOImpl extends GenericDaoImpl<EnzymesAnnotationProductRankHasOrganism> implements IEnzymesAnnotationProductrankHasOrganismDAO {

	public EnzymesAnnotationProductrankHasOrganismDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationProductRankHasOrganism.class);
		
	}

	@Override
	public void addEnzymesAnnotationProductRankHasOrganism(EnzymesAnnotationProductRankHasOrganism enzymesAnnotationProductRankHasOrganism) {
		super.save(enzymesAnnotationProductRankHasOrganism);
		
	}

	@Override
	public void addEnzymesAnnotationProductRankHasOrganismList(List<EnzymesAnnotationProductRankHasOrganism> enzymesAnnotationProductRankHasOrganismList) {
		for (EnzymesAnnotationProductRankHasOrganism enzymesAnnotationProductRankHasOrganism: enzymesAnnotationProductRankHasOrganismList) {
			this.addEnzymesAnnotationProductRankHasOrganism(enzymesAnnotationProductRankHasOrganism);
		}
	}

	@Override
	public List<EnzymesAnnotationProductRankHasOrganism> getAllEnzymesAnnotationProductRankHasOrganism() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationProductRankHasOrganism getEnzymesAnnotationProductRankHasOrganism(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationProductRankHasOrganism(EnzymesAnnotationProductRankHasOrganism enzymesAnnotationProductRankHasOrganism) {
		super.delete(enzymesAnnotationProductRankHasOrganism);
		
	}

	@Override
	public void removeEnzymesAnnotationProductRankHasOrganismList(List<EnzymesAnnotationProductRankHasOrganism> enzymesAnnotationProductRankHasOrganismList) {
		for (EnzymesAnnotationProductRankHasOrganism enzymesAnnotationProductRankHasOrganism: enzymesAnnotationProductRankHasOrganismList) {
			this.removeEnzymesAnnotationProductRankHasOrganism(enzymesAnnotationProductRankHasOrganism);
		}
	}

	@Override
	public void updateEnzymesAnnotationProductRankHasOrganismList(List<EnzymesAnnotationProductRankHasOrganism> enzymesAnnotationProductRankHasOrganismList) {
		for (EnzymesAnnotationProductRankHasOrganism enzymesAnnotationProductRankHasOrganism: enzymesAnnotationProductRankHasOrganismList) {
			this.update(enzymesAnnotationProductRankHasOrganism);
		}
	}

	@Override
	public void updateEnzymesAnnotationProductRankHasOrganism(EnzymesAnnotationProductRankHasOrganism enzymesAnnotationProductRankHasOrganism) {
		super.update(enzymesAnnotationProductRankHasOrganism);
		
	}

	@Override
	public List<String[]> getEnzymesAnnotationProductrankHasOrganismSKeyAndTaxRank() {
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationProductRankHasOrganism> product = c.from(EnzymesAnnotationProductRankHasOrganism.class);
		Root<EnzymesAnnotationOrganism> organism = c.from(EnzymesAnnotationOrganism.class);

	    c.multiselect(product.get("id").get("enzymesAnnotationProductRankSKey"), organism.get("taxRank")); 
	    Predicate filter1 = cb.equal(organism.get("SKey"), product.get("id").get("enzymesAnnotationOrganismSKey"));
	    Order[] orderList = {cb.asc(product.get("id").get("enzymesAnnotationProductRankSKey"))};
	    c.where(filter1).orderBy(orderList);
	    
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		if(list.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: list) {
				String[] res = new String[2];
				res[0] = String.valueOf(item[0]);
				res[1] = String.valueOf(item[1]);
				
				parsedList.add(res);
			}			
			return parsedList;
		}
		return null;
	}
	
	@Override
	public List<EnzymesAnnotationProductRankHasOrganism> getAllEnzymesAnnotationProductRankHasOrganismByAttributes(Integer skey, Integer orgSkey){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.enzymesAnnotationProductRankSKey", skey);
		dic.put("id.enzymesAnnotationOrganismSKey", orgSkey);
		List<EnzymesAnnotationProductRankHasOrganism> list =  this.findByAttributes(dic);

		return list;
	}
	
	@Override
	public void insertEnzymesAnnotationProductrankHasOrganismAttributes(Integer productRankSKey, Integer organismSKey){
		EnzymesAnnotationProductRankHasOrganism org = new EnzymesAnnotationProductRankHasOrganism();
		EnzymesAnnotationProductRankHasOrganismId id = new EnzymesAnnotationProductRankHasOrganismId();
		id.setEnzymesAnnotationOrganismSKey(organismSKey);
		id.setEnzymesAnnotationProductRankSKey(productRankSKey);
		org.setId(id);
		this.save(org);
	}

	@Override
	public List<Integer> getAllProdRankSKeyInProdRankHasOrganism(){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);
		Root<EnzymesAnnotationProductRankHasOrganism> rankHasOrg = c.from(EnzymesAnnotationProductRankHasOrganism.class);
		
	    c.select(rankHasOrg.get("id").get("enzymesAnnotationProductRankSKey")).distinct(true); 
	    
		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer> resultList = q.getResultList();
		
		return resultList;
	}
	
	
}
