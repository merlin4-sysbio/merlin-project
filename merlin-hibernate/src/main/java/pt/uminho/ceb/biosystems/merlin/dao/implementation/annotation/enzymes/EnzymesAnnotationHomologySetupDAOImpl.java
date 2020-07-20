package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
import java.sql.Timestamp;
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
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologysetupDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomologyHasHomologues;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologues;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologySetup;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationOrganism;


public class EnzymesAnnotationHomologySetupDAOImpl extends GenericDaoImpl<EnzymesAnnotationHomologySetup> implements IEnzymesAnnotationHomologysetupDAO  {

	public EnzymesAnnotationHomologySetupDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationHomologySetup.class);
		
	}

	@Override
	public void addEnzymesAnnotationHomologySetup(EnzymesAnnotationHomologySetup enzymesAnnotationHomologySetup) {
		super.save(enzymesAnnotationHomologySetup);
		
	}

	@Override
	public void addEnzymesAnnotationHomologySetupList(List<EnzymesAnnotationHomologySetup> enzymesAnnotationHomologySetupList) {
		for (EnzymesAnnotationHomologySetup enzymesAnnotationHomologysetup: enzymesAnnotationHomologySetupList) {
			this.addEnzymesAnnotationHomologySetup(enzymesAnnotationHomologysetup);
		}
	}

	@Override
	public List<EnzymesAnnotationHomologySetup> getAllEnzymesAnnotationHomologysetup() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationHomologySetup getEnzymesAnnotationHomologysetup(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationHomologysetup(EnzymesAnnotationHomologySetup enzymesAnnotationHomologySetup) {
		super.delete(enzymesAnnotationHomologySetup);
		
	}

	@Override
	public void removeEnzymesAnnotationHomologySetupList(List<EnzymesAnnotationHomologySetup> enzymesAnnotationHomologySetupList) {
		for (EnzymesAnnotationHomologySetup enzymesAnnotationHomologysetup: enzymesAnnotationHomologySetupList) {
			this.removeEnzymesAnnotationHomologysetup(enzymesAnnotationHomologysetup);
		}
	}

	@Override
	public void updateEnzymesAnnotationHomologySetupList(List<EnzymesAnnotationHomologySetup> enzymesAnnotationHomologySetupList) {
		for (EnzymesAnnotationHomologySetup enzymesAnnotationHomologySetup: enzymesAnnotationHomologySetupList) {
			this.update(enzymesAnnotationHomologySetup);
		}
	}

	@Override
	public void updateEnzymesAnnotationHomologysetup(EnzymesAnnotationHomologySetup enzymesAnnotationHomologysetup) {
		super.update(enzymesAnnotationHomologysetup);
		
	}

	@Override
	public Map<String, String> getModelHomologySetupProgramAndDatabaseId() {
		
		List<EnzymesAnnotationHomologySetup> list =  this.findAll();
		Map<String, String> dic = null;
		
		if(list.size() > 0) {
			dic = new HashMap<String, String>();
			for (EnzymesAnnotationHomologySetup x: list){
			
			dic.put(x.getProgram(), x.getDatabaseId());
			}
		}
		return dic;
	}
	
	@Override
	public List<String> getModelHomologySetupEvalue() {
		
		List<EnzymesAnnotationHomologySetup> list =  this.findAll();
		List<String> result = new ArrayList<String>();
		if(list.size() > 0) {
			for (EnzymesAnnotationHomologySetup x: list){
				result.add(x.getEvalue());
			}
		}
		return result;
	}
	
	@Override
	public List<String> getModelHomologySetupEvalueByDatabaseId(String databaseId){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("databaseId", databaseId);
		List<EnzymesAnnotationHomologySetup> list =  this.findByAttributes(dic);
		List<String> result = new ArrayList<String>();
		if (list != null) {
			for (EnzymesAnnotationHomologySetup x : list) {
				result.add(x.getEvalue());
			}
		}
		return result;
	}
	
	@Override
	public List<String> getEnzymesAnnotationHomologysetupProgramByQuery(String query) { 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationHomologySetup> c = cb.createQuery(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationGeneHomology> gene = c.from(EnzymesAnnotationGeneHomology.class);

	    c.select(setup); 
	    
	    Predicate filter1 = cb.equal(setup.get("SKey"), gene.get("enzymesAnnotationHomologySetup").get("SKey"));
	    Predicate filter2 = cb.equal(gene.get("query"), query);
	    c.where(cb.and(filter1, filter2));
	    
	    Query<EnzymesAnnotationHomologySetup> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationHomologySetup> resultList = q.getResultList();
		List<String> res = new ArrayList<String>();
		if(resultList.size() > 0) {
			res.add(resultList.get(0).getProgram());
			}
		return res;
	}
	
	@Override
	public List<EnzymesAnnotationHomologySetup> getEnzymesAnnotationHomologysetupDataByQuery(String query) { 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationHomologySetup> c = cb.createQuery(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationGeneHomology> gene = c.from(EnzymesAnnotationGeneHomology.class);

	    c.select(setup); 
	    
	    Predicate filter1 = cb.equal(setup.get("SKey"), gene.get("enzymesAnnotationHomologySetup").get("SKey"));
	    Predicate filter2 = cb.equal(gene.get("query"), query);
	    c.where(cb.and(filter1, filter2));
	    
	    Query<EnzymesAnnotationHomologySetup> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationHomologySetup> resultList = q.getResultList();
		
		return resultList;
		
	}
	
	@Override
	public List<String[]> getGeneHomologySetupAttributesByAttributes(Float evalue, String status, String matrix, String wordSize){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomologyHasHomologues> homologues = c.from(EnzymesAnnotationGeneHomologyHasHomologues.class);
		
	    c.multiselect(homology.get("SKey"), cb.count(homologues.get("referenceId")), homology.get("query"), setup.get("program")); 
	   
	    Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
	    Predicate filter2 = cb.equal(homology.get("SKey"), homologues.get("id").get("geneHomologySKey"));
	    Predicate filter3 = cb.equal(homology.get("status"), status);
	    Predicate filter4 = cb.gt(homology.<Number>get("evalue"), evalue);
	    Predicate filter5 = cb.equal(setup.get("matrix"), matrix);
	    Predicate filter6 = cb.equal(setup.get("wordSize"), wordSize);
	    
	    c.where(cb.and(filter1,filter2, filter3, filter4, filter5, filter6)).groupBy(homology.get("SKey"));

	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[2] = (String) item[3];
				
				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}
	
	@Override
	public List<String> getEnzymesAnnotationHomologysetupProgramByStatus(String status) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationHomologySetup> c = cb.createQuery(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationGeneHomology> gene = c.from(EnzymesAnnotationGeneHomology.class);

	    c.select(setup); 
	    
	    Predicate filter1 = cb.equal(setup.get("SKey"), gene.get("enzymesAnnotationHomologySetup").get("SKey"));
	    
	    if(status!=null) {
	    	
	    	Predicate filter2 = cb.equal(gene.get("status"), status);
	    	c.where(cb.and(filter1, filter2));
	    }
	    else {
	    	
	    	c.where(cb.and(filter1));
	    }
	    
	    Query<EnzymesAnnotationHomologySetup> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationHomologySetup> resultList = q.getResultList();
		
		List<String> res = new ArrayList<String>();
		if(resultList.size() > 0) {
			for (EnzymesAnnotationHomologySetup x : resultList) {
				res.add(x.getProgram());
			}
			return res;
		}
		return null;
	}
		
	@Override
	public Long getAllEnzymesAnnotationHomologysetupByProgram(String program){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomologyHasHomologues> homologues = c.from(EnzymesAnnotationGeneHomologyHasHomologues.class);

	    c.multiselect(cb.count(setup)); 
	    Predicate filter1 = cb.equal(setup.get("SKey"), homology.get("enzymesAnnotationHomologySetup").get("SKey"));
	    Predicate filter2 = cb.equal(homologues.get("id").get("geneHomologySKey"), homology.get("SKey"));
	    Predicate filter3 = cb.like(setup.get("program".toLowerCase()), program);
	    c.where(cb.and(filter1,filter2, filter3));

	    Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		
		Object result = q.uniqueResult();
		
		return (Long) result;
	}
		
	@Override
	public Map<String,String> getDistinctTaxonomyAndOrganismByProgram(String program){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomologyHasHomologues> has = c.from(EnzymesAnnotationGeneHomologyHasHomologues.class);
		Root<EnzymesAnnotationHomologues> homologues = c.from(EnzymesAnnotationHomologues.class);
		Root<EnzymesAnnotationOrganism> organism = c.from(EnzymesAnnotationOrganism.class);

	    c.multiselect(organism.get("taxonomy"), organism.get("organism")).distinct(true); 
	    Predicate filter1 = cb.equal(homology.get("enzymesAnnotationHomologySetup").get("SKey"), setup.get("SKey"));
	    Predicate filter2 = cb.equal(homology.get("SKey"), has.get("id").get("geneHomologySKey"));
	    Predicate filter3 = cb.equal(homologues.get("SKey"), has.get("id").get("homologuesSKey"));
	    Predicate filter4 = cb.equal(homologues.get("enzymesAnnotationOrganism").get("SKey"), organism.get("SKey"));
	    Predicate filter5 = cb.like(setup.get("program".toLowerCase()), program);
	    
	    c.where(cb.and(filter1,filter2, filter3, filter4, filter5));

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
	public ArrayList<String[]> getOrganismAndTaxonomyAndEvalue(String query) {
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		Root<EnzymesAnnotationGeneHomologyHasHomologues> has = c.from(EnzymesAnnotationGeneHomologyHasHomologues.class);
		Root<EnzymesAnnotationHomologues> homologues = c.from(EnzymesAnnotationHomologues.class);
		Root<EnzymesAnnotationOrganism> organism = c.from(EnzymesAnnotationOrganism.class);

	    c.multiselect(organism.get("organism"), organism.get("taxonomy"), has.get("evalue")); 
	    
	    Predicate filter1 = cb.equal(homology.get("enzymesAnnotationHomologySetup").get("SKey"), setup.get("SKey"));
	    Predicate filter2 = cb.equal(homology.get("SKey"), has.get("id").get("geneHomologySKey"));
	    Predicate filter3 = cb.equal(homologues.get("SKey"), has.get("id").get("homologuesSKey"));
	    Predicate filter4 = cb.equal(homologues.get("enzymesAnnotationOrganism").get("SKey"), organism.get("SKey"));
	    Predicate filter5 = cb.equal(homology.get("query"), query);
	    
	    Order[] orderList = {cb.desc(has.get("bits")), cb.asc(has.get("evalue"))};
	    
	    c.where(cb.and(filter1,filter2, filter3, filter4, filter5)).orderBy(orderList);
	   
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				String[] list = new String[3];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				
				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}
	
	@Override
	public List<String[]> getHomologyAttributes(String query){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationGeneHomology> homology = c.from(EnzymesAnnotationGeneHomology.class);
		
	    c.multiselect(setup.get("program"), setup.get("version"), setup.get("databaseId"), setup.get("evalue"), setup.get("matrix"),
	    		setup.get("wordSize"), setup.get("gapCosts"), setup.get("maxNumberOfAlignments") ); 
	    
	    Predicate filter1 = cb.equal(homology.get("enzymesAnnotationHomologySetup").get("SKey"), setup.get("SKey"));
	    Predicate filter2 = cb.equal(homology.get("query"), query);
	    
	    c.where(cb.and(filter1,filter2));
	   
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				String[] list = new String[8];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];
				list[4] = (String) item[4];
				list[5] = (String) item[5];
				list[6] = (String) item[6];
				list[7] = String.valueOf(item[7]);
				
				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}
	
	@Override
	public Long getEnzymesAnnotationHomologysetupCountDistinctDatabaseId(String program){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
	
	    c.multiselect(cb.countDistinct(setup.get("databaseId"))); 
	    Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		
		Object result = q.uniqueResult();
		
		return (Long) result;
	}
	
	@Override
	public List<String> getEnzymesAnnotationHomologysetupDistinctDatabaseId(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String> c = cb.createQuery(String.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
	
	    c.multiselect(setup.get("databaseId")).distinct(true); 
		Query<String> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<String> list = q.getResultList();
		return list;
	}
	
	@Override
	public String getEnzymesAnnotationHomologysetupDistinctProgram(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<EnzymesAnnotationHomologySetup> c = cb.createQuery(EnzymesAnnotationHomologySetup.class);
		Root<EnzymesAnnotationHomologySetup> setup = c.from(EnzymesAnnotationHomologySetup.class);
	
	    c.multiselect(setup.get("program")).distinct(true); 
		Query<EnzymesAnnotationHomologySetup> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<EnzymesAnnotationHomologySetup> list = q.getResultList();
		
		if (list != null) {
			for (EnzymesAnnotationHomologySetup x : list) {
				return x.getProgram();
			}
		}
		return null;
	}


//			
//		" geneHomology.s_key NOT IN (SELECT distinct (geneHomology.s_key) FROM homologySetup " +
//			" LEFT JOIN geneHomology ON (homologySetup.s_key = homologySetup_s_key) " +
//			" LEFT JOIN geneHomology_has_homologues ON (geneHomology.s_key = geneHomology_s_key) " +
//			" WHERE status='PROCESSED' AND geneHomology_has_homologues.eValue <" + eVal + ") " +
//			" GROUP BY geneHomology.s_key;");

	
	public List<String[]> getHomologySetupAttributesGroupBySkey(Integer rowID) {
//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
//		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
//		Root<EnzymesAnnotationHomologysetup> setup = c.from(EnzymesAnnotationHomologysetup.class);	
//		Join<EnzymesAnnotationHomologysetup, EnzymesAnnotationGeneHomology> gene_homology = setup.join("enzymesAnnotationGenehomologies",JoinType.LEFT);
//		Join<EnzymesAnnotationGeneHomology, EnzymesAnnotationGeneHomologyHasHomologues> hasHomologues = gene_homology.join("EnzymesAnnotationGeneHomologyHasHomologueses",JoinType.LEFT);
//		
//		
//		
//	    c.multiselect(gene_homology.get("SKey"), cb.count(hasHomologues.get("referenceId")), gene_homology.get("query"),
//	    		setup.get("databaseId"));
//
//	    Predicate filter1 = cb.equal(setup.get("SKey"), gene_homology.get("SKey"));
//	    Predicate filter2 = cb.equal(hasHomologues.get("id").get("geneHomologySKey"), gene_homology.get("SKey"));
//	    Predicate filter3 = cb.equal(gene_homology.get("status"), "PROCESSED");
//	    
//	    erro - acabar;
//	    
//	    c.where(cb.and(filter1,filter2));
//	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
//		List<Object[]> resultList = q.getResultList();
//		if(resultList.size() > 0) {
//			ArrayList<String[]> parsedList = new ArrayList<String[]>();
//			
//			for(Object[] item: resultList) {
//				String[] list = new String[12];
//				list[0] = (String) item[0];
//				list[1] = (String) item[1];
//				list[2] = String.valueOf(item[2]);
//				list[3] = (String) item[3];
//				list[4] = String.valueOf(item[4]);
//				list[5] = (String) item[5];
//				list[6] = String.valueOf(item[6]);
//				list[7] = String.valueOf(item[7]);
//				list[8] = String.valueOf(item[8]);
//				list[9] = String.valueOf(item[9]);
//				list[10] = String.valueOf(item[10]);
//				list[11] = String.valueOf(item[11]);
//				parsedList.add(list);
//			}
//			return parsedList;
//		}
		return null;
	}

	
	@Override
	public int getHomologySetupSkeyByAttributes(String databaseID, String program, double eVal, Float lowerIdentity, Float positives, Float queryCoverage, Float targetCoverage, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("databaseId", databaseID);
		dic.put("program", program);
		dic.put("evalue", eVal);
		dic.put("lowerIdentity", lowerIdentity);
		dic.put("positives", positives);
		dic.put("queryCoverage", queryCoverage);
		dic.put("targetCoverage", targetCoverage);
		dic.put("matrix", matrix);
		dic.put("wordSize", wordSize);
		dic.put("gapCosts", gapCosts);
		dic.put("maxNumberOfAlignments", maxNumberOfAlignments);
		if(version != null)
			dic.put("programVersion", version);
		List<EnzymesAnnotationHomologySetup> list =  this.findByAttributes(dic);
		int result = -1;
		if (list != null) {
			for (EnzymesAnnotationHomologySetup x : list) {
				result = x.getSKey();
			}
		}
		return result;
	}
	
	
	@Override
	public Integer insertHomologySetup(String databaseID, String program, double eVal, Float lowerIdentity,
			Float positives, Float queryCoverage, Float targetCoverage, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version) {
		
		long time = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(time);
		
		EnzymesAnnotationHomologySetup homologySetup = new EnzymesAnnotationHomologySetup();
		homologySetup.setDatabaseId(databaseID);
		homologySetup.setProgram(program);
		homologySetup.setEvalue(eVal+"");
		homologySetup.setLowerIdentity(lowerIdentity+"");
		homologySetup.setPositives(positives+"");
		homologySetup.setQueryCoverage(queryCoverage+"");
		homologySetup.setTargetCoverage(targetCoverage+"");
		homologySetup.setMatrix(matrix);
		homologySetup.setWordSize(wordSize+"");
		homologySetup.setGapCosts(gapCosts);
		homologySetup.setMaxNumberOfAlignments(maxNumberOfAlignments);
		homologySetup.setProgramVersion(version);
		homologySetup.setDate(timestamp);
		
		return (Integer) this.save(homologySetup);	
	}
	
	
	@Override
	public int getHomologySetupSkeyByAttributes2(String databaseID, String program, double eVal, 
			int maxNumberOfAlignments, String version){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("databaseId", databaseID);
		dic.put("program", program);
		dic.put("evalue", eVal);
		dic.put("maxNumberOfAlignments", maxNumberOfAlignments);
		dic.put("programVersion", version);
		List<EnzymesAnnotationHomologySetup> list =  this.findByAttributes(dic);
		int result = -1;
		if (list != null) {
			for (EnzymesAnnotationHomologySetup x : list) {
				result = x.getSKey();
			}
		}
		return result;
	}
	
	@Override
	public Integer insertHomologySetup2(String databaseID, String program, double eVal, int maxNumberOfAlignments, String version) {
		
		EnzymesAnnotationHomologySetup homologySetup = new EnzymesAnnotationHomologySetup();
		homologySetup.setDatabaseId(databaseID);
		homologySetup.setProgram(program);
		homologySetup.setEvalue(eVal+"");
		homologySetup.setMaxNumberOfAlignments(maxNumberOfAlignments);
		homologySetup.setProgramVersion(version);
		
		return (Integer) this.save(homologySetup);	
	}
}

