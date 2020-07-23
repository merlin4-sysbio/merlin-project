package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelAliases;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequence;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;


public class ModelGeneDAOImpl extends GenericDaoImpl<ModelGene> implements IModelGeneDAO {

	public ModelGeneDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelGene.class);	
	}

	@Override
	public void addModelGene(ModelGene modelGene) {
		super.save(modelGene);		
	}

	@Override
	public void addModelGeneList(List<ModelGene> modelGeneList) {
		for (ModelGene modelGene: modelGeneList) {
			this.addModelGene(modelGene);
		}		
	}

	@Override
	public List<ModelGene> getAllModelGene() {
		return super.findAll();
	}

	@Override
	public Boolean isModelGeneFilled() {
		return super.checkIfIsFilled();
	}

	@Override
	public ModelGene getModelGene(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelGene(ModelGene modelGene) {
		super.delete(modelGene);

	}

	@Override
	public void removeModelGeneList(List<ModelGene> modelGeneList) {
		for (ModelGene modelGene: modelGeneList) {
			this.removeModelGene(modelGene);
		}
	}

	@Override
	public void updateModelGeneList(List<ModelGene> modelGeneList) {
		for (ModelGene modelGene: modelGeneList) {
			this.update(modelGene);
		}
	}

	@Override
	public void updateModelGene(ModelGene modelGene) {
		super.update(modelGene);

	}

	@Override
	public Integer getGeneIdBySequenceIdAndLocusTag(String sequenceId, String locusTag) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("query", sequenceId);
		map.put("locusTag", locusTag);
		List<ModelGene> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return res.get(0).getIdgene();
		}
		return null;
	}

	@Override
	public Integer getGeneIdBySequenceId(String sequenceId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("query", sequenceId);
		List<ModelGene> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return res.get(0).getIdgene();
		}
		return null;
	}


	@Override
	public String getModelSequenceByQueryAndSequenceType(String sequenceId, SequenceType sequenceType) {

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("query", sequenceId);
		List<ModelGene> res = this.findByAttributes(map);

		if (res!=null && res.size()>0) {

			for(ModelSequence seq : res.get(0).getModelSequences())
				if(seq.getSequenceType().equalsIgnoreCase(sequenceType.toString()))
					return seq.getSequence();
		}
		return null;
	}

	@Override
	public String getLocusTagBySequenceId(String sequenceId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("query", sequenceId);
		List<ModelGene> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return res.get(0).getLocusTag();
		}
		return null;
	}

	@Override
	public Map<Integer, String> getModelGeneIdGeneAndSequenceId() {	
		List<ModelGene> list =  this.findAll();
		Map<Integer, String> dic = new HashMap<>();

		if(list.size() > 0) {
			for (ModelGene x: list){
				dic.put(x.getIdgene(), x.getQuery());
			}
		}
		return dic;
	}

	@Override
	public Map<String, Integer> getModelSequenceIdAndGeneId() {	
		List<ModelGene> list =  this.findAll();
		Map<String, Integer> dic = new HashMap<String, Integer>();

		if(list.size() > 0) {
			for (ModelGene x: list){
				dic.put(x.getQuery(), x.getIdgene());
			}
		}
		return dic;
	}
	
	@Override
	public Map<Integer, String> getGeneIdAndGeneQuery() {	
		List<ModelGene> list =  this.findAll();
		Map<Integer, String> dic = new HashMap<Integer, String>();

		if(list.size() > 0) {
			for (ModelGene x: list){
				dic.put(x.getIdgene(), x.getQuery());
			}
		}
		return dic;
	}


	@Override
	public Boolean checkModelSequenceType(String sequenceType) {

		List<ModelGene> list =  this.findAll();
		if(list.size() > 0) {
			for(ModelSequence seq : list.get(0).getModelSequences())
				if(seq.getSequenceType() == sequenceType)
					return true;
		}
		return false;
	}

	@Override
	public Map<Integer, String> getModelGeneIdGeneAndLocusTag() {	
		List<ModelGene> list =  this.findAll();
		Map<Integer, String> dic = null;

		if(list.size() > 0) {
			dic = new HashMap<Integer, String>();
			for (ModelGene x: list){
				dic.put(x.getIdgene(), x.getLocusTag());
			}
		}
		return dic;
	}

	@Override
	public Set<String> getAllDatabaseGenesSeqId(){
		Set<String> allGenes = new TreeSet<String>();
		List<ModelGene> list = this.findAll();
		for (ModelGene x: list){
			allGenes.add(x.getQuery());	
		}
		return allGenes;
	}

	@Override
	public Long countEntriesInGene() throws Exception{

		return this.countAll();

	}

	@Override
	public Map<String, Set<String>> getAllSeqIdAndAliasByClassName(String className){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String[]> c = cb.createQuery(String[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelAliases> modelAlias = c.from(ModelAliases.class);

		c.multiselect(gene.get("idgene"),modelAlias.get("alias")); 
		Predicate filter1 = cb.equal(gene.get("idgene"), modelAlias.get("entity"));
		Predicate filter2 = cb.equal(modelAlias.get("class_"), className);
		c.where(cb.and(filter1,filter2));

		Query<String[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<String[]> list = q.getResultList();
		
		Map<String, Set<String>> existingGeneNamesAlias = new HashMap<>();
		
		if(list != null) {
			
			for(String[] item: list) {
				
				Set<String> aliases = new TreeSet<>();
				
				String query = item[0];
				String alias = item[1];
				
				if(existingGeneNamesAlias.containsKey(query))
					aliases = existingGeneNamesAlias.get(query);
				
				aliases.add(alias);
				
				existingGeneNamesAlias.put(query, aliases);
			}
		}
		return existingGeneNamesAlias;
	}

	@Override
	public Map<String,String> getLocusTagAndECNumber(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		c.multiselect(gene.get("locusTag"),subunit.get("id").get("modelEnzymeEcnumber")); 
		Predicate filter1 = cb.equal(gene.get("idgene"), subunit.get("id").get("modelGeneIdgene"));

		c.where(filter1);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<String, String> res = null;
		if(list.size() > 0) {
			res = new HashMap<String,String>();
			for(Object[] result: list) {

				res.put((String)result[0], (String)result[1]);
			}
		}
		return res;
	}

	@Override
	public Map<String,List<String>> getSeqIdAndECNumber(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		c.multiselect(gene.get("query"),subunit.get("modelProtein").get("ecnumber")); 
		Predicate filter1 = cb.equal(gene.get("idgene"), subunit.get("id").get("modelGeneIdgene"));

		c.where(filter1);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<String, List<String>> res = new HashMap<>();
		
		if(list.size() > 0) {
			for(Object[] result: list) {
				
				List<String> enzymeEcnumberList = res.get((String)result[0]);
				
				if(enzymeEcnumberList == null) {
					enzymeEcnumberList = new ArrayList<String>();
					res.put((String)result[0], enzymeEcnumberList);
				}
				enzymeEcnumberList.add((String)result[1]);
			}
		}
		return res;
	}

	@Override
	public Integer insertModelGene(String name, String locusTag, String origin, String query, 
			String transcriptionDirection, String leftEndPosition, String rightEndPosition, String booleanRule){
		ModelGene modelGene = new ModelGene();

		if(name!=null)
			modelGene.setName(name);

		modelGene.setLocusTag(locusTag);
		modelGene.setOrigin(origin);
		modelGene.setQuery(query);
		modelGene.setBooleanRule(booleanRule);

		if(transcriptionDirection!=null) {

			modelGene.setTranscriptionDirection(transcriptionDirection);
			modelGene.setLeftEndPosition(leftEndPosition);
			modelGene.setRightEndPosition(rightEndPosition);
		}

		return (Integer) this.save(modelGene);	
	}

	@Override
	public void updateModelGeneNameBySeqId(String name, String sequenceId) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("query", sequenceId);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("name", name);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public void updateAllModelGeneNameByGeneId(String name, Integer geneId, String direction, String left, String right,
			String locusTag) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("idgene", geneId);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("name", name);
		updateAttributes.put("transcriptionDirection", direction);
		updateAttributes.put("leftEndPosition", left);
		updateAttributes.put("rightEndPosition", right);
		updateAttributes.put("locusTag", locusTag);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public void updateModelGeneNameAndLocusTagByGeneId(String name, String locusTag, Integer geneId) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("idgene", geneId);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("name", name);
		updateAttributes.put("locusTag", locusTag);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public Map<String,String> getSeqIdAndProteinName(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);

		c.multiselect(gene.get("query"), protein.get("name")); 
		Predicate filter1 = cb.equal(gene.get("idgene"), subunit.get("id").get("modelGeneIdgene"));
		Predicate filter2 = cb.equal(subunit.get("id").get("modelProteinIdprotein"), protein.get("idprotein"));

		c.where(cb.and(filter1, filter2));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<String, String> res = null;
		if(list.size() > 0) {
			res = new HashMap<String,String>();
			for(Object[] result: list) {
				res.put((String)result[0], (String)result[1]);
			}
		}
		return res;
	}

	@Override
	public Map<String, Set<String>> getSeqIdAndAlias(String className){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);
		Root<ModelAliases> alias = c.from(ModelAliases.class);

		c.multiselect(gene.get("query"), alias.get("alias")); 
		Predicate filter1 = cb.equal(gene.get("idgene"), subunit.get("id").get("modelGeneIdgene"));
		Predicate filter2 = cb.equal(subunit.get("id").get("modelProteinIdprotein"), alias.get("id").get("entity"));
		Predicate filter3 = cb.equal(alias.get("id").get("class_"), className);

		c.where(cb.and(filter1, filter2, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<String, Set<String>> res = null;
		if(list.size() > 0) {
			res = new HashMap<String,Set<String>>();
			for(Object[] result: list) {
				Set<String> aliasList = res.get((String)result[0]);
				if(aliasList == null) {
					aliasList = new TreeSet<String>();
					res.put((String)result[0], aliasList);
				}
				aliasList.add((String)result[1]);
			}
		}
		return res;
	}



	@Override
	public Map<Integer, String> getModelGeneIdGeneAndOriginBySequenceId(String seqId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("query", seqId);
		List<ModelGene> list =  this.findByAttributes(map);
		Map<Integer, String> dic = null;

		if(list.size() > 0) {
			dic = new HashMap<Integer, String>();
			for (ModelGene x: list){
				dic.put(x.getIdgene(), x.getOrigin());
			}
		}
		return dic;
	}

	@Override
	public void removeModelGeneByGeneId(Integer geneId){
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("idgene", geneId);
		List<ModelGene> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {

			this.removeModelGeneList(res);
		}
	}

	@Override
	public void updateModelGeneLocusTagByGeneId(String locusTag, Integer geneId) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("idgene", geneId);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("locusTag", locusTag);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public Integer getGeneIdByLocusTagAndSequenceId(String locusTag, String seqId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("locusTag", locusTag);
		map.put("query", seqId);
		List<ModelGene> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return res.get(0).getIdgene();
		}
		return null;
	}

	@Override
	public void updateModelGeneOriginBySequenceId(String origin, String seqId) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("query", seqId);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("origin", origin);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public void updateModelGeneLocusTagBySequenceId(String locusTag, String seqId) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("query", seqId);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("locusTag", locusTag);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public Map<String, Set<String>> getEntryIdAndSequenceId(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
	
		Root<ModelSequence> sequence = c.from(ModelSequence.class);
		
		c.multiselect(sequence.get("idsequence"), sequence.get("sequence")); 
		
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		
		Map<String, Set<String>> res = new HashMap<String,Set<String>>();
		
		if(list.size() > 0 && list != null) {
		
			for(Object[] item : list) {
				
				Set<String> lst = new HashSet<>();
				
				lst.add(String.valueOf(item[1]));
				
				res.put(item[0].toString(), lst);
			}		
			
		}
		return res;
	}

	@Override
	public Integer getGeneIdByLocusTag(String locusTag) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("locusTag", locusTag);
		List<ModelGene> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return res.get(0).getIdgene();
		}
		return -1;
	}

	@Override
	public List<String[]> getModelGeneAttributesByEcNumberAndProteinId(String ecNumber, Integer proteinId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelGeneHasCompartment> gene_comp = c.from(ModelGeneHasCompartment.class);
		Root<ModelCompartment> comp = c.from(ModelCompartment.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		c.multiselect(gene.get("idgene"), comp.get("name"), gene_comp.get("primaryLocation"), gene_comp.get("score"), gene.get("locusTag")); 
		Predicate filter1 = cb.equal(gene_comp.get("id").get("modelGeneIdgene"), gene.get("idgene"));
		Predicate filter2 = cb.equal(comp.get("idcompartment"), gene_comp.get("id").get("modelCompartmentIdcompartment"));
		Predicate filter3 = cb.equal(gene.get("idgene"), subunit.get("id").get("modelGeneIdgene"));
		Predicate filter4 = cb.equal(subunit.get("id").get("modelEnzymeEcnumber"), ecNumber);
		Predicate filter5 = cb.equal(subunit.get("id").get("modelProteinIdprotein"), proteinId);

		c.where(cb.and(filter1, filter2, filter3, filter4, filter5));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> parsedList = new ArrayList<>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				String[] list = new String[5];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				list[3] = (String) item[3];
				list[4] = (String) item[4];
				parsedList.add(list);
			}
		}
		return parsedList;
	}

	@Override
	public List<String> getModelGeneLocusTag() {
		List<ModelGene> res = this.findAll();
		List<String> locusTagList = new ArrayList<String>();
		if (res!=null && res.size()>0) {
			for (ModelGene x: res){
				locusTagList.add(x.getLocusTag());
			}
		}
		return locusTagList;
	}

	@Override
	public Map<String,String> getModelGeneNameAndLocusTag() {
		List<ModelGene> list = this.findAll();
		Map<String, String> res = null;
		if(list.size() > 0) {
			res = new HashMap<String,String>();
			for(ModelGene result: list) {
				res.put((String) result.getName(), (String) result.getLocusTag());
			}
		}
		return res;
	}

	@Override
	public Map<String,String> getModelGeneNameAndLocusTagAByProteinId(Integer protId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		c.multiselect(gene.get("name"),gene.get("locusTag")); 
		Predicate filter1 = cb.equal(gene.get("idgene"), subunit.get("id").get("modelGeneIdgene"));
		Predicate filter2 = cb.equal(subunit.get("id").get("modelProteinIdprotein"), protId);

		c.where(filter1, filter2);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<String, String> res = null;
		if(list.size() > 0) {
			res = new HashMap<String,String>();
			for(Object[] result: list) {

				res.put((String)result[0], (String)result[1]);
			}
		}
		return res;
	}

	@Override
	public List<String[]> getModelGeneAttributesByGeneId(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelGeneHasCompartment> gene_comp = c.from(ModelGeneHasCompartment.class);
		Root<ModelCompartment> comp = c.from(ModelCompartment.class);

		c.multiselect(gene.get("idgene"), comp.get("name"), gene_comp.get("primaryLocation"), gene_comp.get("score")); 
		Predicate filter1 = cb.equal(gene_comp.get("id").get("modelGeneIdgene"), gene.get("idgene"));
		Predicate filter2 = cb.equal(comp.get("idcompartment"), gene_comp.get("id").get("modelCompartmentIdcompartment"));
		Predicate filter3 = cb.equal(gene.get("idgene"), id);

		c.where(cb.and(filter1, filter2, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> parsedList = new ArrayList<String[]>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				list[3] = (String) item[3];
				parsedList.add(list);
			}
		}
		return parsedList;
	}

	@Override
	public List<Integer> getModelGeneProteinId(Integer protId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelSubunit> c = cb.createQuery(ModelSubunit.class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		c.select(subunit); 
		Predicate filter1 = cb.equal(gene.get("idgene"), subunit.get("id").get("modelGeneIdgene"));

		c.where(filter1);

		Query<ModelSubunit> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelSubunit> list = q.getResultList();

		if(list != null && list.size() > 0) {
			List<Integer> res = new ArrayList<Integer>();
			for(ModelSubunit x: list) {
				res.add(x.getId().getModelProteinIdprotein());
			}
			return res;
		}
		return null;
	}

	@Override
	public Integer getModelGeneCountIdGene(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<ModelGene> gene = c.from(ModelGene.class);

		c.multiselect(cb.count(gene.get("idgene"))); 

		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);

		Object result = q.uniqueResult();

		return (Integer) result;
	}

	@Override
	public List<String[]> getModelGeneAttributesGroupedByGeneIdAndLocusTag(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Join<ModelGene, ModelSubunit> join2 = gene.join("modelSubunit", JoinType.LEFT);
		Join<ModelSubunit, ModelProtein> join3 = gene.join("modelProtein", JoinType.LEFT); //falta ir buscar module id

		c.multiselect(gene.get("idgene"), gene.get("locusTag"), gene.get("name"), cb.countDistinct(join3.get("modelProtein").get("ecnumber"))); 

		Predicate filter1 = cb.equal(join2.get("id").get("modelGeneIdgene"), gene.get("idgene"));
		Predicate filter2 = cb.equal(join3.get("idprotein"), join2.get("id").get("modelProteinIdprotein"));

		c.where(cb.and(filter1, filter2)).groupBy(gene.get("idgene"), gene.get("locusTag"));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> parsedList = new ArrayList<>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = String.valueOf(item[3]);

				parsedList.add(list);
			}
		}
		return parsedList;

	}

	@Override
	public List<String[]> getModelGeneAttributesGroupedLocusTag(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);
		//falta ir buscar module id

		c.multiselect(gene.get("idgene"), gene.get("locusTag"), gene.get("name"), cb.count(protein.get("ecnumber"))); 

		Predicate filter1 = cb.equal(subunit.get("id").get("modelGeneIdgene"), gene.get("idgene"));
		Predicate filter2 = cb.equal(protein.get("idprotein"), subunit.get("id").get("modelProteinIdprotein"));

		c.where(cb.and(filter1, filter2)).groupBy(gene.get("locusTag"));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> parsedList = new ArrayList<String[]>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = String.valueOf(item[3]);

				parsedList.add(list);
			}
		}
		return parsedList;
	}

	@Override
	public Long countGenesWithName(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		Root<ModelGene> gene = c.from(ModelGene.class);

		c.multiselect(cb.count(gene.get("name")));

		Predicate filter1 = cb.notEqual(gene.get("name"),"");
		Predicate filter2 = cb.isNotNull(gene.get("name"));

		c.where(filter1, filter2);

		Query<Long> q = super.sessionFactory.getCurrentSession().createQuery(c);

		return q.uniqueResult();

	}
	
	
	@Override
	public Map<String, Integer> getGeneLocusTagAndIdgene(){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelGene> c = cb.createQuery(ModelGene.class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		c.select(gene);
		
		Predicate filter1 = cb.isNotNull(gene.get("locusTag"));
		c.where(cb.and(filter1));
		Query<ModelGene> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelGene> list = q.getResultList();
		
		Map<String, Integer> result = new HashMap<>();
		
		if(list.size() > 0) {
			for(ModelGene geneItem: list) {
				result.put(geneItem.getLocusTag(), geneItem.getIdgene());
			}
		}
		return result;
	}
	
	@Override
	public List<String[]> getGeneData2(String ecnumber, Integer id){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		
		Join<ModelGene, ModelGeneHasCompartment> geneHasCompart = gene.join("modelGeneHasCompartments", JoinType.INNER);
		Join<ModelGene, ModelSubunit> sub = gene.join("modelSubunits", JoinType.INNER); 
		Join<ModelGeneHasCompartment, ModelCompartment> compart = geneHasCompart.join("modelCompartment", JoinType.INNER); 
		Join<ModelSubunit, ModelProtein> prot = sub.join("modelProtein", JoinType.INNER); 


		c.multiselect(gene.get("idgene"),compart.get("name"),geneHasCompart.get("primaryLocation"),
				geneHasCompart.get("score"),gene.get("locusTag"));
		
		Predicate filter1 = cb.equal(prot.get("ecnumber"),ecnumber);
		Predicate filter2 = cb.equal(prot.get("idprotein"),id);
		
		
		c.where(cb.and(filter1,filter2));
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		
		List<String[]> result = new ArrayList<String[]>();
		
		if(list.size() > 0) {
			for(Object[] item: list) {
				
				String[] sublist = new String[5];
						
				sublist[0]= String.valueOf(item[0]);
				sublist[1]= (String) item[1];
				sublist[2]= String.valueOf(item[2].toString());
				sublist[3]= (String) item[3];
				sublist[4]= (String) item[4];
				
				result.add(sublist);
	
			}
		}
		return result;
	}
	
	@Override
	public ArrayList<String[]> getAllGenes(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Join<ModelGene, ModelSubunit> subunit = gene.join("modelSubunits", JoinType.LEFT);
		Join<ModelSubunit, ModelProtein> protein = subunit.join("modelProtein", JoinType.LEFT);
		Join<ModelGene, ModelGeneHasOrthology> geneHasOrth = gene.join("modelGeneHasOrthologies", JoinType.LEFT);
		Join<ModelGeneHasOrthology, ModelOrthology > orth = geneHasOrth.join("modelOrthology", JoinType.LEFT);
		Join<ModelOrthology, ModelModuleHasOrthology > moduleHasOrth = orth.join("modelModuleHasOrthologies", JoinType.LEFT);

		c.multiselect(gene.get("idgene"), gene.get("locusTag"), gene.get("name"),
				cb.countDistinct(moduleHasOrth.get("id").get("modelModuleId")), cb.countDistinct(protein.get("ecnumber")),
				 gene.get("query"));
		
		c.groupBy(gene.get("locusTag"), gene.get("idgene"));
		c.orderBy(cb.asc(gene.get("locusTag")));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		ArrayList<String[]> parsedList = new ArrayList<String[]>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				String[] list = new String[5];
				list[0] = String.valueOf(item[0]);
				if(item[1] == null)
					list[1] = (String) item[5];
				else
					list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = String.valueOf(item[3]);
				list[4] = String.valueOf(item[4]);
				parsedList.add(list);
			}
		}
		return parsedList;
	}

	@Override
	public Map<String, Set<String>> getQueryAndAliasFromProducts(){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);
		Root<ModelAliases> alias = c.from(ModelAliases.class);
		
		c.multiselect(subunit.get("modelGene").get("query"),alias.get("alias"));
		
		Predicate filter1 = cb.equal(subunit.get("modelProtein").get("idprotein"), alias.get("entity"));
		Predicate filter2 = cb.equal(alias.get("class_"),"p");
		
		c.where(cb.and(filter1,filter2));
		
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		
		Map<String, Set<String>> result = new HashMap<>();
		
		Set<String> aliases = new TreeSet<>();
		
		if(list.size() > 0) {
			
			for(Object[] geneItem: list) {
				
				if(result.containsKey(geneItem[0])){
					aliases = result.get(geneItem[0]);
				}
				
				aliases.add((String) geneItem[1]);
				result.put((String) geneItem[0],aliases);
			}
		}
		
		return result;
	}
	
	@Override
	public List<String[]> getEncodingGenes(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Join<ModelGene, ModelSubunit> subunit = gene.join("modelSubunits", JoinType.LEFT);
		Join<ModelSubunit, ModelProtein> protein = subunit.join("modelProtein", JoinType.INNER);
		Join<ModelGene, ModelGeneHasOrthology> geneHasOrth = gene.join("modelGeneHasOrthologies", JoinType.LEFT);
		Join<ModelGeneHasOrthology, ModelOrthology> orthology = geneHasOrth.join("modelOrthology", JoinType.LEFT);
		Join<ModelOrthology, ModelModuleHasOrthology> moduleHasOrth = orthology.join("modelModuleHasOrthologies", JoinType.LEFT);


		c.multiselect(gene.get("idgene"), gene.get("locusTag"), gene.get("name"), cb.countDistinct(moduleHasOrth.get("modelModule").get("id")), 
				cb.count(protein.get("ecnumber")), gene.get("query")); 


		c.groupBy(gene.get("locusTag"), gene.get("idgene"));
		c.orderBy(cb.asc(gene.get("locusTag")));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> parsedList = new ArrayList<String[]>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				String[] list = new String[5];
				list[0] = String.valueOf(item[0]);
				if(item[1] == null)
					list[1] = (String) item[5];
				else
					list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = String.valueOf(item[3]);
				list[4] = String.valueOf(item[4]);
				
				parsedList.add(list);
			}
		}
		return parsedList;
		
	}
	
	@Override
	public List<Integer> getModelGenesIDs(boolean encoded){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelGene> c = cb.createQuery(ModelGene.class);
		Root<ModelGene> gene = c.from(ModelGene.class);

		c.multiselect(gene); 
		
		if(encoded) {
			Join<ModelGene, ModelSubunit> subunit = gene.join("modelSubunits", JoinType.INNER);
			subunit.join("modelProtein", JoinType.INNER);
		}

		Query<ModelGene> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelGene> resultList = q.getResultList();

		List<Integer> ids = new ArrayList<>();

		if(resultList != null) {

			for(ModelGene item: resultList) {

				ids.add(item.getIdgene());
			}
		}
		return ids;
	}

	@Override
	public Integer countInitialMetabolicGenes(){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		Root<EnzymesAnnotationGeneHomology> enzymeAnnot = c.from(EnzymesAnnotationGeneHomology.class);
	
		c.multiselect(cb.count(enzymeAnnot.get("SKey")));
		
		Predicate filter1 = cb.isNotNull(enzymeAnnot.get("uniprotEcnumber"));
		Predicate filter2 = cb.notEqual(enzymeAnnot.get("uniprotEcnumber"), ";");
		
		c.where(cb.and(filter1, filter2));
		
		Query<Long> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Long> list = q.getResultList();
		
		if(list != null && list.size()>0)
			return list.get(0).intValue();
		
		return null;
	}
	
}

