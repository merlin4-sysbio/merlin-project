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
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationProductrankDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologySetup;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductRank;


public class EnzymesAnnotationProductrankDAOImpl extends GenericDaoImpl<EnzymesAnnotationProductRank> implements IEnzymesAnnotationProductrankDAO {

	public EnzymesAnnotationProductrankDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationProductRank.class);

	}

	@Override
	public void addEnzymesAnnotationProductRank(EnzymesAnnotationProductRank enzymesAnnotationProductRank) {
		super.save(enzymesAnnotationProductRank);

	}

	@Override
	public void addEnzymesAnnotationProductRankList(List<EnzymesAnnotationProductRank> enzymesAnnotationProductRankList) {
		for (EnzymesAnnotationProductRank enzymesAnnotationProductRank: enzymesAnnotationProductRankList) {
			this.addEnzymesAnnotationProductRank(enzymesAnnotationProductRank);
		}
	}

	@Override
	public List<EnzymesAnnotationProductRank> getAllEnzymesAnnotationProductRank() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationProductRank getEnzymesAnnotationProductRank(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationProductRank(EnzymesAnnotationProductRank enzymesAnnotationProductRank) {
		super.delete(enzymesAnnotationProductRank);
	}

	@Override
	public void removeEnzymesAnnotationProductRankList(List<EnzymesAnnotationProductRank> enzymesAnnotationProductRankList) {
		for (EnzymesAnnotationProductRank enzymesAnnotationProductRank: enzymesAnnotationProductRankList) {
			this.removeEnzymesAnnotationProductRank(enzymesAnnotationProductRank);
		}
	}

	@Override
	public void updateEnzymesAnnotationProductRankList(List<EnzymesAnnotationProductRank> enzymesAnnotationProductRankList) {
		for (EnzymesAnnotationProductRank enzymesAnnotationProductRank: enzymesAnnotationProductRankList) {
			this.update(enzymesAnnotationProductRank);
		}	
	}

	@Override
	public void updateEnzymesAnnotationProductRank(EnzymesAnnotationProductRank enzymesAnnotationProductRank) {
		super.update(enzymesAnnotationProductRank);

	}

	@Override
	public List<String[]> getEnzymesAnnotationProductrankAttributesByLocusTag(String locus){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationProductRank> product = c.from(EnzymesAnnotationProductRank.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.multiselect(product.get("SKey"), product.get("productName"), product.get("rank"),setup.get("program")); 
		
		Predicate filter1 = cb.equal(product.get("enzymesAnnotationGeneHomology").get("SKey"), homology.get("SKey"));
		Predicate filter2 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter3 = cb.equal(homology.get("locusTag"), locus);
		Order[] orderList = {cb.desc(setup.get("program")), cb.desc(product.get("rank"))};

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
	public List<String[]> getEnzymesAnnotationProductrankAttributes(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationProductRank> product = c.from(EnzymesAnnotationProductRank.class);
//		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);

//		Predicate filter1 = cb.equal(product.get("enzymesAnnotationGeneHomology").get("SKey"), homology.get("SKey"));
//
//		c.where(filter1);

		c.multiselect(product.get("SKey"), product.get("enzymesAnnotationGeneHomology").get("SKey"), product.get("productName"), product.get("rank")); 

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				list[2] = (String) item[2];
				list[3] =  String.valueOf(item[3]);
				parsedList.add(list);

			}			
			return parsedList;
		}
		return null;
	}

	@Override
	public List<EnzymesAnnotationProductRank> getAllEnzymesAnnotationProductRankByAttributes(Integer skey, String name, Integer rank){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("enzymesAnnotationGeneHomology.SKey", skey);
		dic.put("productName", name);
		dic.put("rank", rank);
		List<EnzymesAnnotationProductRank> list =  this.findByAttributes(dic);

		return list;
	}
	
	@Override
	public Integer getEnzymesAnnotationProductRankIDByAttributes(Integer skey, String name, Integer rank){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("enzymesAnnotationGeneHomology.SKey", skey);
		dic.put("productName", name);
		dic.put("rank", rank);
		EnzymesAnnotationProductRank res =  this.findUniqueByAttributes(dic);
		if(res != null)
			return res.getSKey();
		return null;
	}

	@Override
	public Integer insertEnzymesAnnotationProductrankSkeyAndProductNameAndRank(EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology, String name, Integer rank_){
		EnzymesAnnotationProductRank rank = new EnzymesAnnotationProductRank();
		rank.setEnzymesAnnotationGeneHomology(enzymesAnnotationGeneHomology);
		rank.setProductName(name);
		rank.setRank(rank_);

		return (Integer) this.save(rank);	
	}


}
