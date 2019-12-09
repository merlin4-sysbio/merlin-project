package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberRankDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberRank;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberRankHasOrganism;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologySetup;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationOrganism;

public class EnzymesAnnotationEcNumberRankDAOImpl extends GenericDaoImpl<EnzymesAnnotationEcNumberRank> implements IEnzymesAnnotationEcNumberRankDAO {

	public EnzymesAnnotationEcNumberRankDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationEcNumberRank.class);
		
	}

	@Override
	public void addEnzymesAnnotationEcNumberRank(EnzymesAnnotationEcNumberRank EnzymesAnnotationEcNumberRank) {
		super.save(EnzymesAnnotationEcNumberRank);
		
	}

	@Override
	public void addEnzymesAnnotationEcNumberRankList(List<EnzymesAnnotationEcNumberRank> EnzymesAnnotationEcNumberRankList) {
		for (EnzymesAnnotationEcNumberRank EnzymesAnnotationEcNumberRank: EnzymesAnnotationEcNumberRankList) {
			this.addEnzymesAnnotationEcNumberRank(EnzymesAnnotationEcNumberRank);
		}		
	}

	@Override
	public List<EnzymesAnnotationEcNumberRank> getAllEnzymesAnnotationEcNumberRank() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationEcNumberRank getEnzymesAnnotationEcNumberRank(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationEcNumberRank(EnzymesAnnotationEcNumberRank EnzymesAnnotationEcNumberRank) {
		super.delete(EnzymesAnnotationEcNumberRank);
		
	}

	@Override
	public void removeEnzymesAnnotationEcNumberRankList(List<EnzymesAnnotationEcNumberRank> EnzymesAnnotationEcNumberRankList) {
		for (EnzymesAnnotationEcNumberRank EnzymesAnnotationEcNumberRank: EnzymesAnnotationEcNumberRankList) {
			this.removeEnzymesAnnotationEcNumberRank(EnzymesAnnotationEcNumberRank);
		}
	}

	@Override
	public void updateEnzymesAnnotationEcNumberRankList(List<EnzymesAnnotationEcNumberRank> EnzymesAnnotationEcNumberRankList) {
		for (EnzymesAnnotationEcNumberRank EnzymesAnnotationEcNumberRank: EnzymesAnnotationEcNumberRankList) {
			this.update(EnzymesAnnotationEcNumberRank);
		}
	}

	@Override
	public void updateEnzymesAnnotationEcNumberRank(EnzymesAnnotationEcNumberRank EnzymesAnnotationEcNumberRank) {
		super.update(EnzymesAnnotationEcNumberRank);
		
	}

	@Override
	public List<String[]> getEnzymesAnnotationEcNumberrankAttributesByQuery(String query){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationEcNumberRank> rank = c.from(EnzymesAnnotationEcNumberRank.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

	    c.multiselect(rank.get("SKey"), rank.get("ecNumber"), rank.get("rank"),setup.get("program")); 
	    Predicate filter1 = cb.equal(rank.get("enzymesAnnotationGeneHomology").get("SKey"), homology.get("SKey"));
	    Predicate filter2 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
	    Predicate filter3 = cb.equal(homology.get("query"), query);
	    Order[] orderList = {cb.desc(setup.get("program")), cb.desc(rank.get("rank"))};
	
	    c.where(cb.and(filter1, filter2, filter3)).orderBy(orderList);
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				list[3] = (String) item[3];
				parsedList.add(list);
			}			
			return parsedList;
		}
		return null;
	}
	
	@Override
	public List<String[]> getEnzymesAnnotationEcNumberrankAttributes(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationEcNumberRank> rank = c.from(EnzymesAnnotationEcNumberRank.class);
		
	    c.multiselect(rank.get("SKey"), rank.get("enzymesAnnotationGeneHomology").get("SKey"), rank.get("ecNumber"),rank.get("rank")); 
	    
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				list[2] = (String) item[2]; 
				list[3] = String.valueOf(item[3]);
				
				parsedList.add(list);
			}			
			return parsedList;
		}
		return null;
	}

	@Override
	public List<String[]> getEnzymesAnnotationEcNumberrankSKeyAndTaxRank(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationEcNumberRank> rank = c.from(EnzymesAnnotationEcNumberRank.class);
		Root<EnzymesAnnotationEcNumberRankHasOrganism> hasOrganism = c.from(EnzymesAnnotationEcNumberRankHasOrganism.class);
		Root<EnzymesAnnotationOrganism> organism = c.from(EnzymesAnnotationOrganism.class);
		
		c.multiselect(rank.get("SKey"), organism.get("taxRank"));
	    Predicate filter1 = cb.equal(rank.get("SKey"), hasOrganism.get("id").get("ecNumberRankSKey"));
	    Predicate filter2 = cb.equal(hasOrganism.get("id").get("organismSKey"), organism.get("SKey"));
	    c.where(cb.and(filter1,filter2));

	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				
				String[] list = new String[2];
				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				parsedList.add(list);
			}			
			return parsedList;
		}
		return null;
	}
	
	@Override
	public List<String[]> getEnzymesAnnotationEcNumberrankAttributesByLocusTag(String query){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationEcNumberRank> rank = c.from(EnzymesAnnotationEcNumberRank.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

	    c.multiselect(rank.get("SKey"), rank.get("ecNumber"), rank.get("rank"),setup.get("program")); 
	    Predicate filter1 = cb.equal(rank.get("enzymesAnnotationGeneHomology").get("SKey"), homology.get("SKey"));
	    Predicate filter2 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
	    Predicate filter3 = cb.equal(homology.get("query"), query);
	    Order[] orderList = {cb.desc(setup.get("program")), cb.desc(rank.get("rank"))};

	    c.where(cb.and(filter1, filter2, filter3)).orderBy(orderList);
	   
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				list[3] = (String) item[3];
				parsedList.add(list);
			}			
			return parsedList;
		}
		return null;
	}
	
	@Override
	public List<EnzymesAnnotationEcNumberRank> getAllEnzymesAnnotationEcNumberRankAttributes(Integer geneHomology_s_key,
			String concatEC, int ecnumber){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationEcNumberRank> c = cb.createQuery(EnzymesAnnotationEcNumberRank.class);
		Root<EnzymesAnnotationEcNumberRank> rank = c.from(EnzymesAnnotationEcNumberRank.class);
		

	    c.select(rank); 
	    
	    Predicate filter1 = cb.equal(rank.get("enzymesAnnotationGeneHomology").get("SKey"),geneHomology_s_key);
	    Predicate filter2 = cb.equal(rank.get("ecNumber"), concatEC);
	    Predicate filter3 = cb.equal(rank.get("rank"), ecnumber);

	    c.where(cb.and(filter1, filter2, filter3));
	   
		Query<EnzymesAnnotationEcNumberRank> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationEcNumberRank> resultList = q.getResultList();
		
		return resultList;
	}

	
	
	@Override
	public Integer insertEcNumberRank(String concatEC, Integer ecnumber, EnzymesAnnotationGeneHomology geneHomology){
		
		EnzymesAnnotationEcNumberRank enzAnnEcNRank = new EnzymesAnnotationEcNumberRank();
		enzAnnEcNRank.setEnzymesAnnotationGeneHomology(geneHomology);
		enzAnnEcNRank.setEcNumber(concatEC);
		enzAnnEcNRank.setRank(ecnumber);
		
		return (Integer) this.save(enzAnnEcNRank);
		
	}
	

}
