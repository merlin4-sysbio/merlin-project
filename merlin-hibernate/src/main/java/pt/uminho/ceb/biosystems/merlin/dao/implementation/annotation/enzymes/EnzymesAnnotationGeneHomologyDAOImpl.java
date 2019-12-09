package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologyStatus;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationGenehomologyDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumber;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomologyHasHomologues;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologues;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologuesHasEcNumber;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologySetup;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationOrganism;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


public class EnzymesAnnotationGeneHomologyDAOImpl extends GenericDaoImpl<EnzymesAnnotationGeneHomology> implements IEnzymesAnnotationGenehomologyDAO {

	public EnzymesAnnotationGeneHomologyDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationGeneHomology.class);

	}

	@Override
	public void addEnzymesAnnotationGeneHomology(EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology) {
		super.save(enzymesAnnotationGeneHomology);

	}

	@Override
	public void addEnzymesAnnotationGeneHomologyList(List<EnzymesAnnotationGeneHomology> enzymesAnnotationGeneHomologyList) {
		for (EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology: enzymesAnnotationGeneHomologyList) {
			this.addEnzymesAnnotationGeneHomology(enzymesAnnotationGeneHomology);
		}
	}

	@Override
	public List<EnzymesAnnotationGeneHomology> getAllEnzymesAnnotationGeneHomology() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationGeneHomology getEnzymesAnnotationGeneHomology(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationGeneHomology(EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology) {
		super.delete(enzymesAnnotationGeneHomology);

	}

	@Override
	public void removeEnzymesAnnotationGeneHomologyList(List<EnzymesAnnotationGeneHomology> enzymesAnnotationGeneHomologyList) {
		for (EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology: enzymesAnnotationGeneHomologyList) {
			this.removeEnzymesAnnotationGeneHomology(enzymesAnnotationGeneHomology);
		}
	}

	@Override
	public void updateEnzymesAnnotationGeneHomologyList(List<EnzymesAnnotationGeneHomology> enzymesAnnotationGeneHomologyList) {
		for (EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology: enzymesAnnotationGeneHomologyList) {
			this.update(enzymesAnnotationGeneHomology);
		}
	}

	@Override
	public void updateEnzymesAnnotationGeneHomology(EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology) {
		super.update(enzymesAnnotationGeneHomology);

	}

	@Override
	public Map<Integer, String> getSequenceIds(){
		Map<Integer, String> ret = new HashMap<Integer, String>();
		List<EnzymesAnnotationGeneHomology> list = this.findAll();
		for (EnzymesAnnotationGeneHomology x: list) {
			ret.put(x.getSKey(), x.getQuery());
		}

		return ret;
	}

	@Override
	public Set<Integer> getS_Key(){
		Set<Integer> res= new TreeSet<Integer>();
		List<EnzymesAnnotationGeneHomology> list = this.findAll();
		for (EnzymesAnnotationGeneHomology x: list) {
			res.add(x.getSKey());
		}
		return res;
	}

	@Override
	public Map<String, String> getLocusTagAndGeneByQuery(String query) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("query", query);
		List<EnzymesAnnotationGeneHomology> list =  this.findByAttributes(dic);

		Map<String, String> dics = null;
		if(list.size() > 0) {
			dics = new HashMap<String, String>();
			for (EnzymesAnnotationGeneHomology x: list){
				dics.put(x.getLocusTag(), x.getGene());
			}
		}
		return dics;
	}

	@Override
	public String getLocusTagbyQuery(String query) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("query", query);
		List<EnzymesAnnotationGeneHomology> list =  this.findByAttributes(dic);

		if(list.size() > 0) {
			return list.get(0).getLocusTag();
		}
		return null;
	}

	@Override
	public List<String> getsequenceIdBySKey(Integer skey) {

		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("SKey", skey);
		List<EnzymesAnnotationGeneHomology> list =  this.findByAttributes(dic);
		List<String> res = new ArrayList<String>();
		if(list.size() > 0) {
			for (EnzymesAnnotationGeneHomology x : list)
				res.add(x.getQuery());
		}
		return res;
	}

	@Override
	public Map<Integer, String> getSKeyAndQuery() {
		List<EnzymesAnnotationGeneHomology> list =  this.findAll();

		Map<Integer, String> dics = null;
		if(list.size() > 0) {
			dics = new HashMap<Integer, String>();
			for (EnzymesAnnotationGeneHomology x: list){
				dics.put(x.getSKey(), x.getQuery());
			}
		}
		return dics;
	}

	@Override
	public Map<String, Integer> getQueryAndSkey() {
		List<EnzymesAnnotationGeneHomology> list =  this.findAll();

		Map<String, Integer> dics = new HashMap<>();
		if(list.size() > 0) {
			for (EnzymesAnnotationGeneHomology x: list){
				dics.put(x.getQuery(), x.getSKey());
			}
		}
		return dics;
	}

	@Override
	public Map<Integer, String> getEnzymesAnnotationGenehomologySKeyAndLocusTag() {
		List<EnzymesAnnotationGeneHomology> list =  this.findAll();

		Map<Integer, String> dics = null;
		if(list.size() > 0) {
			dics = new HashMap<Integer, String>();
			for (EnzymesAnnotationGeneHomology x: list){
				dics.put(x.getSKey(), x.getLocusTag());
			}
		}
		return dics;
	}


	@Override
	public void removeModelGeneHomologyByQuery(String query){
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("query", query);
		List<EnzymesAnnotationGeneHomology> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			this.removeEnzymesAnnotationGeneHomologyList(res);
		}
	}

	@Override
	public void removeGeneHomologyBySKey(Integer skey){
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("SKey", skey);
		List<EnzymesAnnotationGeneHomology> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			this.removeEnzymesAnnotationGeneHomologyList(res);
		}
	}

	//	@Override
	//	public List<String[]> getEnzymesAnnotationGenehomologyData() { 
	//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
	//		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
	//		Root<EnzymesAnnotationGeneHomology> gene = c.from(EnzymesAnnotationGeneHomology.class);
	////		Root<CompartmentsAnnotationReports> rep = c.from(CompartmentsAnnotationReports.class);
	//
	//	    c.multiselect(gene.get("SKey"), gene.get("query"), gene.get("locusTag"));
	//	    
	//	    Predicate filter1 = cb.equal(rep.get("locusTag"), gene.get("gene"));
	//	    c.where(cb.and(filter1));
	//	    
	//	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
	//		List<Object[]> resultList = q.getResultList();
	//		if(resultList.size() > 0) {
	//			ArrayList<String[]> parsedList = new ArrayList<String[]>();
	//			
	//			for(Object[] item: resultList) {
	//				String[] list = new String[3];
	//				list[0] = String.valueOf(item[0]);
	//				list[1] = (String) item[1];
	//				list[2] = (String) item[2];
	//				
	//				parsedList.add(list);	
	//			}
	//			return parsedList;
	//		}
	//		return null;
	//	}
	//	
	//	@Override
	//	public List<String[]> getEnzymesAnnotationGenehomologyData2() { 
	//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
	//		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
	//		Root<EnzymesAnnotationGeneHomology> gene = c.from(EnzymesAnnotationGeneHomology.class);
	//		Root<CompartmentsAnnotationPsortReports> rep = c.from(CompartmentsAnnotationPsortReports.class);
	//
	//	    c.multiselect(gene.get("SKey"), gene.get("query"), gene.get("locusTag")); 
	//	    
	//	    Predicate filter1 = cb.equal(rep.get("locusTag"), gene.get("query"));
	//	    c.where(cb.and(filter1));
	//	    
	//	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
	//		List<Object[]> resultList = q.getResultList();
	//		if(resultList.size() > 0) {
	//			ArrayList<String[]> parsedList = new ArrayList<String[]>();
	//			
	//			for(Object[] item: resultList) {
	//				String[] list = new String[3];
	//				list[0] = String.valueOf(item[0]);
	//				list[1] = (String) item[1];
	//				list[2] = (String) item[2];
	//				
	//				parsedList.add(list);	
	//			}
	//			return parsedList;
	//		}
	//		return null;
	//	}

	@Override
	public Map<String,String> getGeneHomologyQueryAndProgramByStatus(String status){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.multiselect(setup.get("program"), homology.get("query")); 
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter2 = cb.equal(homology.get("status"), status);
		c.where(cb.and(filter1,filter2));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<String,String> res = null;
		if(list.size() > 0) {
			res = new HashMap<String,String>();
			for(Object[] result: list) {
				res.put((String)result[0], (String)result[1]);
			}
		}
		return res;
	}

	@Override
	public List<String[]> getGeneHomologySKeyANdQueryAndProgramByAttributes(String status, String evalue, String matrix,
			String wordSize, Integer maxNumberOfAlignments){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.multiselect(homology.get("SKey"), homology.get("query"), setup.get("program")); 
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter2 = cb.equal(homology.get("status"), status);
		Predicate filter3 = cb.equal(setup.get("evalue"), evalue);
		Predicate filter4 = cb.equal(setup.get("matrix"), matrix);
		Predicate filter5 = cb.equal(setup.get("wordSize"), wordSize);
		Predicate filter6 = cb.equal(setup.get("maxNumberOfAlignments"), maxNumberOfAlignments);

		c.where(cb.and(filter1,filter2, filter3, filter4, filter5, filter6));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[3];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}

	@Override
	public List<String[]> getGeneHomologySKeyANdQueryAndProgramByStatusAndDatabaseId(String status, String databaseid){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.multiselect(homology.get("SKey"), homology.get("query"), setup.get("program")); 
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter2 = cb.equal(homology.get("status"), status);
		Predicate filter3 = cb.notEqual(setup.get("databaseId"), databaseid);

		c.where(cb.and(filter1,filter2, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[3];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}

	@Override
	public List<String[]> getGeneHomologySKeyANdQueryAndProgramByAttributes2(String status,String databaseId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.multiselect(homology.get("SKey"), homology.get("query"), setup.get("program")); 
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("SKey"));
		Predicate filter2 = cb.equal(homology.get("status"), status);
		Predicate filter3 = cb.equal(setup.get("databaseId"), databaseId);

		c.where(cb.and(filter1,filter2, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[3];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}

	@Override
	public Map<Integer,String> getGeneHomologySKeyAndProgramByStatus(String status){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.multiselect(homology.get("SKey"), setup.get("program")); 
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter2 = cb.equal(homology.get("status"), status);
		c.where(cb.and(filter1,filter2));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<Integer,String> res = null;
		if(list.size() > 0) {
			res = new HashMap<Integer,String>();
			for(Object[] result: list) {
				res.put((Integer) result[0], (String)result[1]);
			}
		}
		return res;
	}

	@Override
	public List<EnzymesAnnotationGeneHomology> getAllEnzymesAnnotationGeneHomologyByStatusAndProgram(String status, String program){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationGeneHomology> c = cb.createQuery(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.select(homology); 
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter2 = cb.equal(homology.get("status"), status);
		Predicate filter3 = cb.like(setup.get("program".toLowerCase()), program); 
		c.where(cb.and(filter1,filter2, filter3));

		Query<EnzymesAnnotationGeneHomology> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationGeneHomology> list = q.getResultList();
		return list;
	}

	@Override
	public Map<String,String> getDistinctGeneHomologyLocusTagAndUniprotEcNumber(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationGeneHomology> c = cb.createQuery(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);

		c.multiselect(homology.get("locusTag"), homology.get("uniprotEcnumber")).distinct(true); 

		Query<EnzymesAnnotationGeneHomology> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationGeneHomology> list = q.getResultList();
		Map<String,String> res = new HashMap<String, String>();

		if(list.size() > 0) {

			for (EnzymesAnnotationGeneHomology x: list){
				res.put(x.getLocusTag(), x.getUniprotEcnumber());
			}
		}
		return res;
	}

	@Override
	public List<String[]> getGeneHomologyAttributesByStatus(String status){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.multiselect(homology.get("SKey"), homology.get("locusTag"), homology.get("gene"), homology.get("chromosome"), homology.get("organelle"), homology.get("uniprotStar"), setup.get("program"), homology.get("query")); 
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter2 = cb.equal(homology.get("status"), status); 
		Order[] orderList = {cb.asc(homology.get("locusTag")), cb.desc(homology.get("status"))};

		c.where(cb.and(filter1,filter2)).orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[8];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];
				list[4] = (String) item[4];
				list[5] = String.valueOf(item[5]);
				list[6] = (String) item[6];
				list[7] = (String) item[7];

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}

	@Override
	public List<String[]> getGeneHomologyAttributesByStatus2(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.multiselect(homology.get("SKey"), homology.get("locusTag"), homology.get("gene"),
				//	    		homology.get("chromosome"), homology.get("organelle"), 
				homology.get("uniprotStar"), setup.get("program"), homology.get("query")); 
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter2 = cb.equal(homology.get("status"), "PROCESSED"); 
		Predicate filter3 = cb.equal(homology.get("status"), "NO_SIMILARITY"); 
		Order[] orderList = {cb.asc(homology.get("locusTag")), cb.desc(homology.get("status"))};

		c.where(cb.and(filter1), cb.or(filter2, filter3)).orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[8];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = "";//(String) item[3];
				list[4] = "";//(String) item[4];
				list[5] = String.valueOf(item[3]);
				list[6] = (String) item[4];
				list[7] = (String) item[5];

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}

	@Override
	public List<String[]> getGeneHomologyAttributes(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.multiselect(homology.get("SKey"), homology.get("locusTag"), homology.get("gene"), homology.get("chromosome"), homology.get("organelle"), homology.get("uniprotStar"), setup.get("program"), homology.get("query")); 
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter2 = cb.equal(homology.get("status"), "PROCESSED");
		Predicate filter3 = cb.equal(homology.get("status"), "NO_SIMILARITY");

		Order[] orderList = {cb.asc(homology.get("locusTag")), cb.desc(homology.get("status"))};

		c.where(cb.and(filter1, cb.or(filter2, filter3)));
		c.orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[8];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];
				list[4] = (String) item[4];
				list[5] = String.valueOf(item[5]);
				list[6] = (String) item[6];
				list[7] = (String) item[7];

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}

	@Override
	public Map<String, Set<Integer>> getGeneHomologySkeyAndDatabaseId(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.multiselect(homology.get("SKey"), setup.get("databaseId")); 
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter2 = cb.equal(homology.get("status"), "PROCESSED"); 
		Predicate filter3 = cb.equal(homology.get("status"), "NO_SIMILARITY");
		Order[] orderList = {cb.asc(homology.get("locusTag")), cb.desc(homology.get("status"))};

		c.where(cb.and(filter1, cb.or(filter2, filter3)));
		c.orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		Map<String, Set<Integer>> res = new HashMap<>();

		if(resultList.size() > 0) {
			for (Object[] x: resultList){
				Integer key = (Integer) x[0];
				String value = x[1].toString();
				if(!res.containsKey(value))
					res.put(value, new HashSet<Integer>());
				res.get(value).add(key);
			}
		}
		return res;
	}

	@Override
	public List<EnzymesAnnotationGeneHomology> getAllEnzymesAnnotationGeneHomologyByQueryAndHomologySetupId(Integer skey, String query) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("enzymesAnnotationHomologysetup.sKey", skey);
		dic.put("query", query);

		List<EnzymesAnnotationGeneHomology> list =  this.findByAttributes(dic);

		return list;
	}

	@Override
	public List<EnzymesAnnotationGeneHomology> getAllEnzymesAnnotationGeneHomologyByQueryAndProgram(String query, String program){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationGeneHomology> c = cb.createQuery(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);

		c.select(homology); 
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter2 = cb.equal(homology.get("query"), query);
		Predicate filter3 = cb.equal(setup.get("program"), program);
		c.where(cb.and(filter1,filter2, filter3));

		Query<EnzymesAnnotationGeneHomology> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationGeneHomology> list = q.getResultList();
		return list;
	}

	@Override
	public List<String[]> getGeneHomologyAttributesByDifferentAttributes(String status, Float evalue, String matrix, String word){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationGeneHomologyHasHomologues> homologyHashomologues = c.from(EnzymesAnnotationGeneHomologyHasHomologues.class);

		c.multiselect(homology.get("SKey"), homologyHashomologues.get("id").get("referenceId"), homology.get("query"), setup.get("program"), setup.get("maxNumberOfAlignments"));  //falta o referenceID
		Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
		Predicate filter2 = cb.equal(homology.get("SKey"), homologyHashomologues.get("id").get("geneHomologySKey"));
		Predicate filter3 = cb.equal(homology.get("status"), status);
		Predicate filter4 = cb.le(homologyHashomologues.<Number>get("evalue"), evalue);
		Predicate filter5 = cb.gt(setup.<Number>get("evalue"), evalue); 
		Predicate filter6 = cb.equal(setup.get("matrix"), matrix);
		Predicate filter7 = cb.equal(setup.get("wordSize"), word); 

		c.where(cb.and(filter1,filter2, filter3, filter4, filter5, filter6, filter7)).groupBy(homology.get("SKey"));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[8];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];
				list[4] = (String) item[4];

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}

	@Override
	public ArrayList<String[]> getHomologyAttributes(String program, String status){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);

		c.multiselect(homology.get("SKey"), homology.get("locusTag"), homology.get("query"), homology.get("gene")
				//,homology.get("chromossome"), homology.get("organelle")
				); 

		Predicate filter1 = cb.equal(homology.get("enzymesAnnotationHomologySetup").get("SKey"), setup.get("SKey"));
		Predicate filter2 = cb.like(setup.get("program"), program); 
		Predicate filter3 = cb.notEqual(homology.get("status"), status); 

		c.where(cb.and(filter1,filter2, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[6];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];
				list[4] = "";//(String) item[4];
				list[5] = "";//(String) item[5];

				parsedList.add(list);	
			}
			return parsedList;
		}
		return new ArrayList<String[]>();
	}

	@Override
	public ArrayList<String[]> getHomologyAttributes2(String program, String status){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);

		c.multiselect(homology.get("SKey"), homology.get("locusTag"), homology.get("query"), homology.get("gene"), 
				homology.get("chromossome"), homology.get("organelle")); 

		Predicate filter1 = cb.equal(homology.get("enzymesAnnotationHomologySetup").get("SKey"), setup.get("SKey"));
		Predicate filter2 = cb.equal(setup.get("program".toLowerCase()), program); 
		Predicate filter3 = cb.notEqual(homology.get("status"), "NO_SIMILARITY"); 

		c.where(cb.and(filter1,filter2, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[6];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];
				list[4] = (String) item[4];
				list[5] = (String) item[5];

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}


	@Override
	public Integer getGeneHomologySkey(String query, Integer homologySetupID) {

		Integer res = -1;

		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("query", query);
		dic.put("enzymesAnnotationHomologySetup.SKey", homologySetupID);
		List<EnzymesAnnotationGeneHomology> list =  this.findByAttributes(dic);

		if(list != null && list.size()>0)
			res = list.get(0).getSKey();

		return res;
	}

	@Override
	public Set<Integer> getHomologyGenesSKeyByStatus(HomologyStatus status, String program) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationGeneHomology> c = cb.createQuery(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);

		c.select(homology);

		Predicate filter = cb.equal(homology.get("status"), status.toString());

		c.where(filter);

		Query<EnzymesAnnotationGeneHomology> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationGeneHomology> resultList = q.getResultList();

		Set<Integer> genes = new HashSet<>();

		for(EnzymesAnnotationGeneHomology entry : resultList) {
			if(entry.getEnzymesAnnotationHomologySetup().getProgram().contains(program))
				genes.add(entry.getSKey());
		}
		return genes;
	}

	@Override
	public Set<String> getHomologyGenesQueryByStatus(HomologyStatus status, String program) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationGeneHomology> c = cb.createQuery(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);

		c.select(homology);

		Predicate filter = cb.equal(homology.get("status"), status.toString());

		c.where(filter);

		Query<EnzymesAnnotationGeneHomology> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationGeneHomology> resultList = q.getResultList();

		Set<String> genes = new HashSet<>();

		for(EnzymesAnnotationGeneHomology entry : resultList) {
			if(entry.getEnzymesAnnotationHomologySetup().getProgram().contains(program))
				genes.add(entry.getQuery());
		}
		return genes;
	}

	@Override
	public Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByAttributes(HomologyStatus status, String program, double evalue, String matrix, String wordSize, Integer maxNumberOfAlignments) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationGeneHomology> c = cb.createQuery(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);

		c.select(homology);

		Predicate filter1= cb.equal(homology.get("status"), status.toString());
		Predicate filter2 = cb.equal(homology.get("enzymesAnnotationHomologySetup").get("wordSize"), wordSize);
		Predicate filter3 = cb.equal(homology.get("enzymesAnnotationHomologySetup").get("maxNumberOfAlignments"), maxNumberOfAlignments.toString());
		Predicate filter4 = cb.equal(homology.get("enzymesAnnotationHomologySetup").get("matrix"), matrix);

		c.where(cb.and(filter1, filter2, filter3, filter4));

		Query<EnzymesAnnotationGeneHomology> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationGeneHomology> resultList = q.getResultList();

		Set<Integer> keys = new HashSet<>();
		Set<String> queries = new HashSet<>();

		for(EnzymesAnnotationGeneHomology entry : resultList) {
			if(entry.getEnzymesAnnotationHomologySetup().getProgram().contains(program) &&
					Double.valueOf(entry.getEnzymesAnnotationHomologySetup().getEvalue()) < evalue) {
				keys.add(entry.getSKey());
				queries.add(entry.getQuery());
			}
		}
		return new Pair<Set<Integer>, Set<String>>(keys, queries);
	}

	@Override
	public Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByStatusAndProgramAndDatabaseId(HomologyStatus status, String program, String databaseId) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationGeneHomology> c = cb.createQuery(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);

		c.select(homology);

		Predicate filter1= cb.equal(homology.get("status"), status.toString());
		Predicate filter2 = cb.notEqual(homology.get("enzymesAnnotationHomologySetup").get("databaseId"), databaseId);

		c.where(cb.and(filter1, filter2));

		Query<EnzymesAnnotationGeneHomology> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationGeneHomology> resultList = q.getResultList();

		Set<Integer> keys = new HashSet<>();
		Set<String> queries = new HashSet<>();

		for(EnzymesAnnotationGeneHomology entry : resultList) {
			if(entry.getEnzymesAnnotationHomologySetup().getProgram().contains(program)) {
				keys.add(entry.getSKey());
				queries.add(entry.getQuery());
			}
		}
		return new Pair<Set<Integer>, Set<String>>(keys, queries);
	}

	@Override
	public Set<Integer> getSKeyForAutomaticAnnotation(String blastDatabase){ 

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationGeneHomology> c = cb.createQuery(EnzymesAnnotationGeneHomology.class);

		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);

		Join<EnzymesAnnotationGeneHomologyHasHomologues,EnzymesAnnotationGeneHomology> geneHasHomologues = homology.join("enzymesAnnotationGeneHomologyHasHomologueses", 
				JoinType.INNER);

		Join<EnzymesAnnotationGeneHomologyHasHomologues,EnzymesAnnotationHomologues> homologues = geneHasHomologues.join("enzymesAnnotationHomologues", 
				JoinType.INNER);

		homologues.join("enzymesAnnotationHomologuesHasEcNumbers", 
				JoinType.INNER);

		c.select(homology).distinct(true); 

		Query<EnzymesAnnotationGeneHomology> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationGeneHomology> resultList = q.getResultList();

		Set<Integer> list = new HashSet<>();

		for(EnzymesAnnotationGeneHomology geneHomology : resultList) {

			if(blastDatabase == null) {
				list.add(geneHomology.getSKey());
			}
			else {
				String databaseId  = geneHomology.getEnzymesAnnotationHomologySetup().getDatabaseId();

				if(databaseId != null && databaseId.equals(blastDatabase))
					list.add(geneHomology.getSKey());
			}
		}

		return list;
	}

	@Override
	public List<String[]> getHomologySetupAttributes(String query) {


		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationGeneHomology> geneHomology = c.from(EnzymesAnnotationGeneHomology.class);
		Join<EnzymesAnnotationGeneHomology, EnzymesAnnotationGeneHomologyHasHomologues> geneHomologyHasHomologues = 
				geneHomology.join("enzymesAnnotationGeneHomologyHasHomologueses", JoinType.INNER);
		Join<EnzymesAnnotationGeneHomologyHasHomologues, EnzymesAnnotationHomologues> homologues = 
				geneHomologyHasHomologues.join("enzymesAnnotationHomologues", JoinType.INNER);
		Join<EnzymesAnnotationHomologues, EnzymesAnnotationOrganism> organism = 
				homologues.join("enzymesAnnotationOrganism", JoinType.INNER);
		Join<EnzymesAnnotationHomologues, EnzymesAnnotationHomologuesHasEcNumber> homologuesHasEcNumber = 
				homologues.join("enzymesAnnotationHomologuesHasEcNumbers", JoinType.LEFT);
		Join<EnzymesAnnotationHomologuesHasEcNumber, EnzymesAnnotationEcNumber> ecNumber = 
				homologuesHasEcNumber.join("enzymesAnnotationEcNumber", JoinType.LEFT);

		c.multiselect(geneHomologyHasHomologues.get("referenceId"), homologues.get("locusId"), organism.get("organism"), 
				geneHomologyHasHomologues.get("evalue"), geneHomologyHasHomologues.get("bits"), homologues.get("product"),
				homologues.get("SKey"), ecNumber.get("ecNumber"),
				homologues.get("uniprotStar")); 

		Predicate filter1 = cb.equal(geneHomology.get("query"), query);

		Order[] orderList = {cb.desc(geneHomologyHasHomologues.get("bits")), cb.asc(geneHomologyHasHomologues.get("evalue"))};

		c.where(cb.and(filter1)).orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[9];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = String.valueOf(item[3]);
				list[4] = String.valueOf(item[4]);
				list[5] = (String) item[5];
				list[6] = String.valueOf(item[6]);
				list[7] = (String) item[7];
				list[8] = String.valueOf(item[8]);

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}


	@Override
	public Integer getAnnotationHomologySkeyBySequenceId(int sequenceId) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationGeneHomology> c = cb.createQuery(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);

		c.select(homology.get("SKey"));

		Predicate filter = cb.equal(homology.get("modelSequence").get("idsequence"), sequenceId);

		c.where(filter);

		Query<EnzymesAnnotationGeneHomology> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationGeneHomology> resultList = q.getResultList();

		Integer sKey = null;

		if(resultList != null && resultList.size()>0) {
			for(EnzymesAnnotationGeneHomology item : resultList) {
				sKey = Integer.valueOf(item.toString());
			}
		}

		return sKey;
	}
}