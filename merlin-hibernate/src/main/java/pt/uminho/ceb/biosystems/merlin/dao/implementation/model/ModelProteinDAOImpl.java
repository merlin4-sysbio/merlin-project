package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologyStatus;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModule;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryEvent;

public class ModelProteinDAOImpl extends GenericDaoImpl<ModelProtein> implements IModelProteinDAO{

	public ModelProteinDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelProtein.class);

	}

	@Override
	public void addModelProtein(ModelProtein modelProtein) {
		super.save(modelProtein);

	}

	@Override
	public void addModelProteinList(List<ModelProtein> modelProteinList) {
		for (ModelProtein modelProtein: modelProteinList) {
			this.addModelProtein(modelProtein);
		}

	}

	@Override
	public List<ModelProtein> getAllModelProtein() {
		return super.findAll();
	}

	@Override
	public ModelProtein getModelProtein(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelProtein(ModelProtein modelProtein) {
		super.delete(modelProtein);

	}

	@Override
	public void removeModelProteinList(List<ModelProtein> modelProteinList) {
		for (ModelProtein modelProtein: modelProteinList) {
			this.removeModelProtein(modelProtein);
		}

	}

	@Override
	public void updateModelProteinList(List<ModelProtein> modelProteinList) {
		for (ModelProtein modelProtein: modelProteinList) {
			this.update(modelProtein);
		}

	}

	@Override
	public void updateModelProtein(ModelProtein modelProtein) {
		super.update(modelProtein);

	}

	@Override
	public Integer getProteinIdByName(String name) {

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.select(prot.get("idprotein"));

		Predicate filter = cb.equal(prot.get("name"), name);

		c.where(filter);

		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);

		return q.uniqueResult();

	}

	@Override
	public Integer getProteinIdByNameAndClass(String name, String class_) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("name", name);
		dic.put("class_", class_);
		List<ModelProtein> list =  this.findByAttributes(dic);
		Integer res = null;
		if(list.size() > 0) {
			ModelProtein id = list.get(0);
			res =  id.getIdprotein();
		}
		return res;	
	}

	@Override
	public Integer insertModelProtein(String name, String classe, String inchi, Float molecularWeight, Float molecularWeightExp, Float molecularWeightKd,
			Float molecularWeightSeq, Float pi, String ecnumber, String source) {
		ModelProtein modelProtein = new ModelProtein();
		modelProtein.setName(name);
		modelProtein.setClass_(classe);
		modelProtein.setInchi(inchi);
		modelProtein.setMolecularWeight(molecularWeightSeq);
		modelProtein.setMolecularWeightExp(molecularWeightExp);
		modelProtein.setMolecularWeightSeq(molecularWeightSeq);
		modelProtein.setPi(pi);
		modelProtein.setEcnumber(ecnumber);
		modelProtein.setSource(source);
//		modelProtein.setInModel(inModel);
		return (Integer) this.save(modelProtein);
	}

	@Override
	public List<ModelProtein> getDistinctModelProteinIdAndNameAndClass(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);
		prot.join("modelSubunits", JoinType.INNER); 	//this filters the query to return only encoded proteins

		c.multiselect(prot.get("idprotein"), prot.get("name"), prot.get("class_")).distinct(true); 

		c.orderBy(cb.asc(prot.get("name")));
		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelProtein> list = q.getResultList();

		return list;
	}

	@Override
	//	"SELECT distinct(idprotein), name, inchi " +
	//	"FROM protein JOIN enzyme ON enzyme_protein_idprotein=idprotein ORDER BY name");
	public List<ModelProtein> getDistinctModelProteinIdAndNameAndInchi(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.multiselect(prot.get("idprotein"), prot.get("name"), prot.get("inchi")).distinct(true); //distinct so no idprot

		c.orderBy(cb.asc(prot.get("name")));
		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelProtein> list = q.getResultList();

		return list;
	}

	@Override
	public List<ModelProtein> getAllModelProteinById(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.select(prot);

		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelProtein> list = q.getResultList();

		return list;
	}

	@Override
	public ProteinContainer getProteinIdByEcNumber(String ecNumber){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.select(prot);

		Predicate filter = cb.equal(prot.get("ecnumber"), ecNumber);

		c.where(filter);

		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelProtein> list = q.getResultList();


		for(ModelProtein item : list) {

			ProteinContainer container = new ProteinContainer(item.getEcnumber());

//			container.setInModel(item.getInModel());
			container.setIdProtein(item.getIdprotein());
			container.setName(item.getName());

			return container;

		}
		return null;

	}

	@Override
	public List<ModelProtein> getDistinctModelProteinAttributes(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);
		Root<RegulatoryEvent> reg = c.from(RegulatoryEvent.class);

		c.multiselect(prot.get("idprotein"), prot.get("name"), prot.get("inchi")).distinct(true); 
		Predicate filter1 = cb.equal(reg.get("id").get("proteinIdprotein"), prot.get("idprotein"));

		c.where(cb.and(filter1));
		c.orderBy(cb.asc(prot.get("name")));
		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelProtein> list = q.getResultList();

		return list;
	}

	@Override
	public List<ModelProtein> getDistincModelProteinAttributesByAtt(boolean distinct){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);
		Root<RegulatoryEvent> reg = c.from(RegulatoryEvent.class);

		c.multiselect(prot.get("idprotein"), prot.get("name"), prot.get("inchi")).distinct(distinct); 
		Predicate filter1 = cb.equal(reg.get("id").get("proteinIdprotein"), prot.get("idprotein"));

		c.where(cb.and(filter1));
		c.orderBy(cb.asc(prot.get("name")));
		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelProtein> list = q.getResultList();

		return list;
	}

	//	ResultSet rs = stmt.executeQuery("SELECT distinct(idprotein), name, iupac_name, inchi, " +
	//			"cas_registry_name FROM protein " +
	//			"JOIN sigma_promoter ON protein_protein_idprotein=idprotein ORDER BY name");
	//	public List<ModelProtein> getDistinctModelProteinAttributes2(){
	//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
	//		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
	//		Root<ModelProtein> prot = c.from(ModelProtein.class);
	//		Root<ModelPromoter> reg = c.from(ModelRegulatoryEvent.class); //é o sigma promoter??
	//
	//	    c.multiselect(prot.get("idprotein"), prot.get("name"), prot.get("inchi")); 
	//	    Predicate filter1 = cb.equal(reg.get("id").get("proteinIdprotein"), prot.get("idprotein"));
	//	    
	//	    c.where(cb.and(filter1));
	//	    c.orderBy(cb.asc(prot.get("name")));
	//	    Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
	//		List<ModelProtein> list = q.getResultList();
	//		
	//		return list;
	//	}

	@Override
	public List<ModelProtein> getAllModelProteinByNameAndClass(String name, String class_) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("name", name);
		map.put("class_", class_);

		List<ModelProtein> res = this.findByAttributes(map);

		if (res!=null && res.size()>0) {
			return res;
		}
		return null;
	}

	@Override
	public List<String[]> getModelProteinAttributesOrderByEcNumber(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);

		c.multiselect(protein.get("idprotein"), protein.get("name"), protein.get("ecnumber")); 
		Order[] orderList = {cb.asc(protein.get("ecnumber"))}; 

		c.orderBy(orderList);

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
	public List<String[]> getDistinctModelProteinIdAndNameAndClass2(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.multiselect(prot.get("idprotein"), prot.get("name"), prot.get("class_")).distinct(true); 

		c.orderBy(cb.asc(prot.get("name")));
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
	public List<ProteinContainer> getModelProteinAttributes(Integer pathId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathwayHasModelProtein> c = cb.createQuery(ModelPathwayHasModelProtein.class);
		Root<ModelPathwayHasModelProtein> pathHasProt = c.from(ModelPathwayHasModelProtein.class);

		c.select(pathHasProt); 

		Predicate filter1 = cb.equal(pathHasProt.get("modelPathway").get("idpathway"),pathId);

		c.where(cb.and(filter1));
		c.orderBy(cb.asc(pathHasProt.get("modelProtein").get("ecnumber")));

		Query<ModelPathwayHasModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);

		List<ModelPathwayHasModelProtein> resultList = q.getResultList();

		List<ProteinContainer> list1 = new ArrayList<ProteinContainer>();

		if(resultList.size() > 0) {

			for(ModelPathwayHasModelProtein item: resultList) {

				ProteinContainer container = new ProteinContainer(item.getModelProtein().getEcnumber());

				container.setExternalIdentifier(item.getModelProtein().getEcnumber());
				container.setName(item.getModelProtein().getName());
				container.setClass_(item.getModelProtein().getClass_());
//				container.setInModel(item.getModelProtein().getInModel());

				list1.add(container);
			}
		}
		return list1;

	}

	@Override
	public String[][] getProteins(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.select(prot); 

		Order[] orderList = {cb.asc(prot.get("ecnumber"))}; 
		c.orderBy(orderList);

		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelProtein> resultList = q.getResultList();

		String[][] res = new String[resultList.size()][3];

		if(resultList.size() > 0) {

			for(int row = 0; row < resultList.size(); row++) {

				ModelProtein item = resultList.get(row);

				res[row][0] = item.getIdprotein() + "__" + item.getEcnumber();
				res[row][1] = item.getEcnumber() +"	-	"+ item.getName();

			}
		}
		return res;
	}

	@Override
	public List<String[]> getAllEncodedEnzymes() {

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);
		Join<ModelProtein, ModelReactionHasModelProtein> reactHasProtein = protein.join("modelReactionHasModelProteins", JoinType.INNER);
		Join<ModelReaction, ModelReactionHasModelProtein> reaction = reactHasProtein.join("modelReaction", JoinType.INNER);
		Join<ModelProtein, ModelSubunit> subunit = protein.join("modelSubunits", JoinType.INNER);		//the only difference between this method and the one bellow is the joinType here

		c.multiselect(
				protein.get("name"), 
				protein.get("ecnumber"), 
				cb.countDistinct(reactHasProtein.get("id").get("modelReactionIdreaction")),
				protein.get("source"), 
				subunit.get("id").get("modelProteinIdprotein"), 
				reaction.get("inModel"), 
				protein.get("idprotein"),
				cb.countDistinct(subunit.get("id").get("modelGeneIdgene"))).distinct(true);

		Predicate filter1 = cb.equal(reaction.get("inModel"), true);
		c.where(filter1);

		Order[] orderList = {cb.asc(protein.get("ecnumber")), cb.desc(reaction.get("inModel"))};
		c.orderBy(orderList);

		c.groupBy(protein.get("idprotein"), protein.get("ecnumber"), reaction.get("inModel"));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);

		List<Object[]> resultList = q.getResultList();

		List<String[]> parsedList = new ArrayList<>();

		if(resultList != null && resultList.size() > 0) {

			for(Object[] item: resultList) {
				String[] list = new String[8];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				list[3] = String.valueOf(item[3]);
				list[4] = null;
				list[5] = String.valueOf(item[5]);
				list[6] = String.valueOf(item[6]);
				list[7] = String.valueOf(item[7]);
				parsedList.add(list);
			}
		}

		return parsedList;
	}
	
	@Override
	public List<String[]> getAllEnzymes() {

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);
		Join<ModelProtein, ModelReactionHasModelProtein> reactHasProtein = protein.join("modelReactionHasModelProteins", JoinType.INNER);
		Join<ModelReaction, ModelReactionHasModelProtein> reaction = reactHasProtein.join("modelReaction", JoinType.INNER);
		Join<ModelProtein, ModelSubunit> subunit = protein.join("modelSubunits", JoinType.LEFT);

		c.multiselect(
				protein.get("name"), 
				protein.get("ecnumber"), 
				cb.countDistinct(reactHasProtein.get("id").get("modelReactionIdreaction")),
				protein.get("source"), 
				subunit.get("id").get("modelProteinIdprotein"), 
				reaction.get("inModel"), 
				protein.get("idprotein"),
				cb.countDistinct(subunit.get("id").get("modelGeneIdgene"))).distinct(true);

		Predicate filter1 = cb.equal(reaction.get("inModel"), true);
		c.where(filter1);

		Order[] orderList = {cb.asc(protein.get("ecnumber")), cb.desc(reaction.get("inModel"))};
		c.orderBy(orderList);

		c.groupBy(protein.get("idprotein"), protein.get("ecnumber"), reaction.get("inModel"));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);

		List<Object[]> resultList = q.getResultList();

		List<String[]> parsedList = new ArrayList<>();

		if(resultList != null && resultList.size() > 0) {

			for(Object[] item: resultList) {
				String[] list = new String[8];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				list[3] = String.valueOf(item[3]);
				list[4] = null;
				list[5] = String.valueOf(item[5]);
				list[6] = String.valueOf(item[6]);
				list[7] = String.valueOf(item[7]);
				parsedList.add(list);
			}
		}

		return parsedList;
	}

	@Override
	public Map<Integer, Long> getProteinsData2(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		c.multiselect(this.getPath("idprotein", protein), cb.count(this.getPath("id.geneIdgene",subunit)));

		Predicate filter1 = cb.equal(this.getPath("idprotein", protein), this.getPath("id.modelProteinIdprotein",subunit));

		c.where(cb.and(filter1));

		c.groupBy(this.getPath("idprotein", protein));
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);

		List<Object[]> list = q.getResultList();
		Map<Integer, Long> res = null;
		if(list.size() > 0) {
			res = new HashMap<Integer,Long>();
			for(Object[] result: list) {

				res.put((Integer)result[0], (Long)result[1]);
			}
		}
		return res;
	}

//	@Override
//	public Map<String, Boolean> getModelProteinEcNumberAndInModelByProteinId(Integer protId) {
//		Map<String, Serializable> map = new HashMap<String, Serializable>();
//		map.put("idprotein", protId);
//		List<ModelProtein> res = this.findByAttributes(map);
//		Map<String, Boolean> dic = null;
//
//		if(res.size() > 0) {
//			dic = new HashMap<String, Boolean>();
//			for (ModelProtein x: res){
//				dic.put(x.getEcnumber(), x.getInModel());
//			}
//		}
//		return dic;
//	}

	@Override
	public List<ModelProtein> getAllModelProteinByAttributes(Integer protId, String source){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);	
		protein.join("modelSubunits", JoinType.INNER);		//this filters the query to return only encoded proteins
		
		c.select(protein);

		Predicate filter2 = cb.equal(protein.get("source"), source);
		Predicate filter3 = cb.equal(protein.get("idprotein"), protId);

		c.where(cb.and(filter2, filter3));
		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelProtein> resultList = q.getResultList();

		if(resultList.size() > 0) {
			return resultList;
		}
		return resultList;
	}


	@Override
	public Long countModelProteinDistinctProteinIdNotLikeSource(String source) {

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);

		c.multiselect(cb.countDistinct(protein.get("idprotein")));

		Predicate filter1 = cb.notEqual(protein.get("source"), source);
		c.where(cb.and(filter1));

		Query<Long> q = super.sessionFactory.getCurrentSession().createQuery(c);

		return q.getSingleResult();

	}

	@Override
	public Long getModelProteinDistinctProteinIdBySource(String source) {

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);

		c.multiselect(cb.countDistinct(protein.get("idprotein")));

		Predicate filter1 = cb.equal(protein.get("source"), source);
		c.where(cb.and(filter1));

		Query<Long> q = super.sessionFactory.getCurrentSession().createQuery(c);

		return q.getSingleResult();
	}

	@Override
	public void removeAllEnzymeByProteinId(Integer protId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("idprotein", protId);
		List<ModelProtein> list = this.findByAttributes(dic);
		if (list.size() > 0) {
			this.removeModelProteinList(list);
		}
	}

	@Override
	public Boolean checkEnzymeInModelExistence(Integer protId, String source) {

		List<ModelProtein> res = this.getAllModelProteinByAttributes(protId, source);

		if (res != null && res.size()>0) {
			return true;
		}
		return false;
	}

	@Override
	public void updateProteinSetEcNumberSourceAndInModel(Integer model_protein_idprotein, String ecnumber, String source) throws Exception {

		ModelProtein protein = this.getModelProtein(model_protein_idprotein);	

		if(protein != null) {
			protein.setEcnumber(ecnumber);

//			protein.setInModel(inModel);

			if(source != null)
				protein.setSource(source);

			this.update(protein);
		}
	}	

	public List<String> getECNumbersWithModules(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelModuleHasModelProtein> c = cb.createQuery(ModelModuleHasModelProtein.class);

		Root<ModelModuleHasModelProtein> moduleHasProts = c.from(ModelModuleHasModelProtein.class);
		
		Join<ModelModuleHasModelProtein, ModelModule> modules = moduleHasProts.join("modelModule", JoinType.INNER);
		
		c.select(moduleHasProts);
		
		Predicate filter = cb.equal(modules.get("gprStatus"), HomologyStatus.PROCESSED.toString());
		
		c.where(filter);
		
		Query<ModelModuleHasModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelModuleHasModelProtein> res = q.getResultList();

		List<String> ecNumbers = new ArrayList<>();

		if(res != null) {
			for(ModelModuleHasModelProtein moduleHasProt : res) {

				if(moduleHasProt.getModelProtein().getEcnumber() != null)
					ecNumbers.add(moduleHasProt.getModelProtein().getEcnumber());
			}
		}

		return  ecNumbers;
	}

	@Override
	public List<ModelSubunit> getSubunitByEcNumber(String ecNumber){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelSubunit> c = cb.createQuery(ModelSubunit.class);

		Root<ModelProtein> enzyme = c.from(ModelProtein.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		c.select(subunit); 
		Predicate filter1 = cb.equal(enzyme.get("ecnumber"), ecNumber);
		Predicate filter2 = cb.equal(enzyme.get("idprotein"), subunit.get("id").get("modelProteinIdprotein"));

		c.where(cb.and(filter1,filter2));

		Query<ModelSubunit> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelSubunit> list = q.getResultList();

		return list;

	}

	@Override
	public List<ProteinContainer> getEnzymesModel(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);

		Root<ModelProtein> protein = c.from(ModelProtein.class);

		c.select(protein); 
		c.orderBy(cb.asc(protein.get("ecnumber")));

		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelProtein> resultlist = q.getResultList();
		List<ProteinContainer> containers = new ArrayList<ProteinContainer>();

		if(resultlist.size() > 0) {

			for(ModelProtein modelprotein : resultlist) {

				ProteinContainer container = new ProteinContainer(modelprotein.getIdprotein(),modelprotein.getEcnumber());
				container.setName(modelprotein.getName());
				containers.add(container);
			}

		}

		return containers;

	}

	public Map<Integer, List<Integer>> getEnzymesCompartments(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);

		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);
		
		Join<ModelGene,ModelSubunit> gene = subunit.join("modelGene", JoinType.INNER);
		Join<ModelGeneHasCompartment,ModelGene> geneHasCompart = gene.join("modelGeneHasCompartments", JoinType.INNER);
		
		c.multiselect(geneHasCompart.get("id").get("modelCompartmentIdcompartment"),
						subunit.get("modelProtein").get("idprotein")).distinct(true); 

		c.orderBy(cb.asc(subunit.get("modelProtein").get("idprotein")));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		Map<Integer, List<Integer>> enzymesCompartments = new HashMap<>();

		if(list.size() > 0) {

			for(Object[] item: list) {
				
				List<Integer> compartments = new ArrayList<>();

				Integer proteinID = Integer.valueOf(item[1].toString());

				if (enzymesCompartments.containsKey(proteinID))
					compartments = enzymesCompartments.get(proteinID);
				
				Integer idc = Integer.valueOf(item[0].toString());
				if(!compartments.contains(idc))
					compartments.add(idc);
				
				enzymesCompartments.put(proteinID, compartments);
			}
			return enzymesCompartments;
		}
		return enzymesCompartments;

	}


	@Override
	public Map<String, Integer> getEnzymeEcNumberAndProteinID(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);
		c.multiselect(protein.get("ecnumber"), protein.get("idprotein"));

		Predicate filter1 = cb.isNotNull(protein.get("ecnumber"));
		c.where(cb.and(filter1));
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		Map<String, Integer> result = new HashMap<>();

		if(list.size() > 0) {
			for(Object[] item: list) {
				result.put(item[0].toString(), Integer.valueOf(item[1].toString()));
			}
		}
		return result;
	}

	/**
	 * @param proteinId
	 * @return
	 */
	@Override
	public String getEnzymeEcNumberByProteinID(Integer proteinId){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String> c = cb.createQuery(String.class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);
		c.select(protein.get("ecnumber"));

		Predicate filter1 = cb.isNotNull(protein.get("ecnumber"));
		Predicate filter2 = cb.equal(protein.get("idprotein"),proteinId);
		c.where(cb.and(filter1,filter2));

		Query<String> q = super.sessionFactory.getCurrentSession().createQuery(c);


		return q.uniqueResult();
	}

	@Override
	public List<ProteinContainer> getGeneQueryAndProteinName(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelSubunit> c = cb.createQuery(ModelSubunit.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		c.select(subunit);

		Query<ModelSubunit> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelSubunit> list = q.getResultList();

		List<ProteinContainer> existingProducts = new ArrayList<ProteinContainer>();

		if(list.size() > 0) {
			for(ModelSubunit item: list) {

				ProteinContainer container = new ProteinContainer(item.getModelProtein().getEcnumber());

				container.setQuery(item.getModelGene().getQuery());
				container.setName(item.getModelProtein().getName());

				existingProducts.add(container);
			}

		}

		return existingProducts;
	}


	@Override
	public List<ProteinContainer> getProteinIdByIdgene(Integer idGene) {

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelSubunit> c = cb.createQuery(ModelSubunit.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		c.select(subunit);

		Predicate filter1 = cb.equal(subunit.get("modelGene").get("idgene"), idGene);

		c.where(filter1);

		Query<ModelSubunit> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelSubunit> list = q.getResultList();

		List<ProteinContainer> proteins = new ArrayList<>();
		
		for(ModelSubunit item : list) {

			ProteinContainer container = new ProteinContainer(item.getModelProtein().getEcnumber());

			container.setIdProtein(item.getModelProtein().getIdprotein());

			proteins.add(container);
		}

		return proteins;
	}

	/**
	 * @param proteinId
	 * @return
	 */
	@Override
	public ModelProtein getProteinWithEcnumberNotNullByProteinID(Integer proteinId){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);
		c.select(protein);

		Predicate filter1 = cb.isNotNull(protein.get("ecnumber"));
		Predicate filter2 = cb.equal(protein.get("idprotein"), proteinId);
		c.where(cb.and(filter1,filter2));
		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		return q.uniqueResult();

	}


	@Override
	public List<ProteinContainer> getEnzymeHasReaction(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReactionHasModelProtein> c = cb.createQuery(ModelReactionHasModelProtein.class);
		Root<ModelReactionHasModelProtein> reacHasProtein = c.from(ModelReactionHasModelProtein.class);
		c.select(reacHasProtein);

		c.orderBy(cb.asc(reacHasProtein.get("modelReaction").get("idreaction")));

		Query<ModelReactionHasModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReactionHasModelProtein> resultList = q.getResultList();

		List<ProteinContainer> newList = new ArrayList<ProteinContainer>();

		for(ModelReactionHasModelProtein item : resultList) {

			ProteinContainer container = new ProteinContainer(item.getModelProtein().getEcnumber());

			container.setIdProtein(item.getModelProtein().getIdprotein());
			container.addReactionIdentifier(item.getModelReaction().getIdreaction());

			newList.add(container);

		}

		return newList;
	}

	@Override 
	public List<CompartmentContainer> getProteinCompartmentsByProteinId(Integer proteinId) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelGeneHasCompartment> c = cb.createQuery(ModelGeneHasCompartment.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelGeneHasCompartment> geneHasComp = c.from(ModelGeneHasCompartment.class);

		c.select(geneHasComp);

		Predicate filter1 = cb.equal(subunit.get("id").get("modelProteinIdprotein"), proteinId);
		Predicate filter2 = cb.equal(subunit.get("id").get("modelGeneIdgene"), gene.get("idgene"));
		Predicate filter3 = cb.equal(geneHasComp.get("id").get("modelGeneIdgene"), gene.get("idgene"));

		c.where(cb.and(filter1, filter2, filter3));

		Query<ModelGeneHasCompartment> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelGeneHasCompartment> resultList = q.getResultList();

		List<CompartmentContainer> containers = new ArrayList<>();
		
		Set<Integer> alreadySaved = new HashSet<>();

		if(resultList != null) {
			for(ModelGeneHasCompartment res : resultList) {

				ModelCompartment compartment = res.getModelCompartment();
				
				if(!alreadySaved.contains(compartment.getIdcompartment()))
					containers.add(new CompartmentContainer(compartment.getIdcompartment(), compartment.getName(), compartment.getAbbreviation()));
			}
		}
		return containers;
	}
}
