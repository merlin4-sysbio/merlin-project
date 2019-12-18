package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSubunitDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModule;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunitId;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryEvent;

public class ModelSubunitDAOImpl extends GenericDaoImpl<ModelSubunit> implements IModelSubunitDAO{

	public ModelSubunitDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelSubunit.class);

	}

	@Override 
	public void addSubunit(ModelSubunit subunit) {
		super.save(subunit);

	}

	@Override 
	public void addSubunitList(List<ModelSubunit> subunitList) {
		for (ModelSubunit subunit: subunitList) {
			this.addSubunit(subunit);
		}

	}

	@Override 
	public List<ModelSubunit> getAllSubunitList() {
		return super.findAll();
	}

	@Override 
	public ModelSubunit getSubunit(Integer id) {
		return super.findById(id);
	}

	@Override 
	public void removeSubunit(ModelSubunit subunit) {
		super.delete(subunit);

	}

	@Override 
	public void removeSubunitList(List<ModelSubunit> subunitList) {
		for (ModelSubunit subunit: subunitList) {
			this.removeSubunit(subunit);
		}

	}

	@Override 
	public void updateSubunitList(List<ModelSubunit> subunitList) {
		for (ModelSubunit subunit: subunitList) {
			this.update(subunit);
		}

	}

	@Override 
	public void updateSubunit(ModelSubunit subunit) {
		super.update(subunit);

	}


	@Override 
	public List<String> getEcNumberByGeneId(Integer geneId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelGeneIdgene", geneId);
		List<ModelSubunit> list =  this.findByAttributes(dic);
		List<String> res = new ArrayList<String>();
		if(list.size() > 0) {
			ModelSubunit model = list.get(0);
			res.add(model.getModelProtein().getEcnumber());
		}
		return res;
	}

	@Override 
	public void removeSubunitByGeneIdAndProteinId(Integer geneId, Integer protId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelGeneIdgene", geneId);
		dic.put("id.modelProteinIdprotein", protId);
		List<ModelSubunit> list = this.findByAttributes(dic);
		if (list.size() > 0) {
			this.removeSubunitList(list);
		}
	}

	@Override 
	public List<ModelSubunit> getAllSubunitByGeneId(Integer geneId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelGeneIdgene", geneId);
		List<ModelSubunit> list =  this.findByAttributes(dic);

		return list;
	}

	@Override 
	public List<ModelSubunit> getAllSubunitByProteinId(Integer protId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelProteinIdprotein", protId);
		List<ModelSubunit> list =  this.findByAttributes(dic);

		return list;
	}


	@Override 
	public Integer checkProteinIdByGeneIdAndProteinId(Integer geneId, Integer proteinId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelGeneIdgene", geneId);
		dic.put("id.modelProteinIdprotein", proteinId);
		List<ModelSubunit> list =  this.findByAttributes(dic);
		Integer res = null;
		if(list.size() > 0) {
			ModelSubunit model = list.get(0);
			res =  model.getModelProtein().getIdprotein();
		}
		return res;
	}

	@Override 
	public Integer checkProteinByGeneIdAndProteinIdAndModuleId(Integer geneId, Integer proteinId, Integer moduleId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelGeneIdgene", geneId);
		dic.put("id.modelProteinIdprotein", proteinId);
		dic.put("modelModule.id", moduleId);
		List<ModelSubunit> list =  this.findByAttributes(dic);
		Integer res = null;
		if(list.size() > 0) {
			ModelSubunit model = list.get(0);
			res =  model.getModelProtein().getIdprotein();
		}
		return res;
	}


	@Override 
	public ModelSubunitId insertModelSubunit(Integer geneId, Integer proteinId) {
		
		ModelSubunit modelSubunit = new ModelSubunit();
		ModelSubunitId id = new ModelSubunitId();
		id.setModelGeneIdgene(geneId);
		id.setModelProteinIdprotein(proteinId);
		modelSubunit.setId(id);
		
		return (ModelSubunitId) this.save(modelSubunit);	
	}



	@Override 
	public List<Integer> getDistinctCompartmentIdByEcNumber(String ecNumber){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);
		Root<ModelGeneHasCompartment> geneComp = c.from(ModelGeneHasCompartment.class);

		c.multiselect(geneComp.get("id").get("modelCompartmentIdcompartment")).distinct(true); 
		Predicate filter1 = cb.equal(sub.get("id").get("modelGeneIdgene"), geneComp.get("id").get("modelGeneIdgene"));
		Predicate filter2 = cb.equal(sub.get("id").get("modelEnzymeEcnumber"), ecNumber);
		c.where(cb.and(filter1,filter2));

		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer> list = q.getResultList();

		return list;
	}

	@Override 
	public void updateSubunitGeneIdWithLocusTagId(Integer locusTagId, Integer geneId) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("id.modelGeneIdgene", geneId);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("id.modelGeneIdgene", locusTagId);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}


	@Override 
	public Map<Integer, Integer> getModelSubunitGeneIdAndEnzymeProteinIdByEcNumber(String ecNumber) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);

		c.multiselect(sub.get("id").get("modelGeneIdgene"), sub.get("id").get("modelProteinIdprotein")).distinct(true);

		Predicate filter1 = cb.equal(sub.get("id").get("modelEnzymeEcnumber"), ecNumber);
		c.where(cb.and(filter1));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		Map<Integer, Integer> dics = null;
		if(list.size() > 0) {
			dics = new HashMap<Integer, Integer>();
			for (Object[] x: list){
				dics.put((Integer) x[0], (Integer) x[1]);
			}
		}
		return dics;	
	}


	@Override 
	public List<String[]> getModelSubunitAttributesOrderByName(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<RegulatoryEvent> reg = c.from(RegulatoryEvent.class);

		/*CriteriaQuery<Integer> c2 = cb.createQuery(Integer.class);
		Root<ModelRegulatoryEvent> reg = c2.from(ModelRegulatoryEvent.class);
		c.multiselect(reg.get("id").get("proteinIdprotein"));
		Query<Integer> q2 = super.sessionFactory.getCurrentSession().createQuery(c2);
		List<Integer> resultList2 = q2.getResultList();*/

		c.multiselect(gene.get("name"), gene.get("locusTag"), gene.get("idgene")); 
		Predicate filter1 = cb.equal(sub.get("id").get("modelGeneIdgene"), gene.get("idgene"));


		Predicate filter2 = sub.get("id").get("modelProteinIdprotein").in(reg.get("id").get("proteinIdprotein"));

		Order[] orderList = {cb.asc(gene.get("name"))};
		c.where(cb.and(filter1,filter2)).orderBy(orderList);

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
	public List<String[]> getGPRsECNumbers(boolean isCompartimentalized) {
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String[]> c = cb.createQuery(String[].class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);
//		Root<ModelGene> gene = c.from(ModelGene.class);
//		Root<ModelProtein> protein = c.from(ModelProtein.class);
		Root<ModelReaction> reac = c.from(ModelReaction.class);
		Root<ModelReactionHasModelProtein> reacHasEnz = c.from(ModelReactionHasModelProtein.class);
		
		Root<ModelProtein> protein = c.from(ModelProtein.class);
		Join<ModelProtein, ModelReactionHasModelProtein> reactHasProtein = protein.join("modelReactionHasModelProteins", JoinType.INNER);
		Join<ModelReaction, ModelReactionHasModelProtein> reaction = reactHasProtein.join("modelReaction", JoinType.INNER);
		Join<ModelProtein, ModelSubunit> subunit = protein.join("modelSubunits", JoinType.INNER);

		c.multiselect(subunit.get("modelGene").get("locusTag"), protein.get("ecnumber"), subunit.get("modelGene").get("query")).distinct(true);

//		Predicate filter1 = cb.equal(gene.get("idgene"), sub.get("id").get("modelGeneIdgene"));
		Predicate filter2 = cb.equal(sub.get("id").get("modelProteinIdprotein"), protein.get("idprotein"));
		Predicate filter5 = cb.equal(reacHasEnz.get("id").get("modelProteinIdprotein"), protein.get("idprotein"));
		Predicate filter6 = cb.equal(reacHasEnz.get("id").get("modelReactionIdreaction"), reac.get("idreaction"));
		Predicate filter8 = cb.equal(reac.get("inModel"), true);

		Predicate filter9= cb.isNull(reac.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter9 = cb.isNotNull(reac.get("modelCompartment").get("idcompartment"));

		c.where(cb.and(filter2, filter5, filter6, filter8, filter9));

		Query<String[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		
		return q.getResultList();


	}

	@Override 
	public List<String[]> getModelSubunitDistinctData() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);
		Root<ModelGeneHasCompartment> gene = c.from(ModelGeneHasCompartment.class);

		c.multiselect(gene.get("id").get("modelCompartmentIdcompartment"), sub.get("id").get("modelEnzymeEcnumber"), sub.get("id").get("modelProteinIdprotein")).distinct(true);

		Predicate filter1 = cb.equal(sub.get("id").get("modelGeneIdgene"), gene.get("id").get("modelGeneIdgene"));
		Order[] orderList = {cb.asc(sub.get("id").get("modelEnzymeEcnumber"))};
		c.where(cb.and(filter1)).orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[3];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}


	@Override 
	public Map<Integer, String> getModelSubunitProteinIdAndEcNumberByGeneId(Integer geneid) {

		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelGeneIdgene", geneid);
		List<ModelSubunit> list =  this.findByAttributes(dic);

		Map<Integer, String> dict = null;

		if(list.size() > 0) {
			dict = new HashMap<Integer, String>();
			for (ModelSubunit x: list){
				dict.put(x.getModelProtein().getIdprotein(), x.getModelProtein().getEcnumber());
			}
		}
		return dict;
	}

	//	"SELECT module_id, note FROM subunit WHERE gene_idgene='"+idGene+"' AND enzyme_ecnumber = '"+ecnumber+"';");
	//	@Override 
	//	public Map<Integer, String> getModelSubunitModuleIdAndNoteByGeneIdAndEcNumber(int geneid, String ecnumber) {
	//		Map<String, Serializable> dic = new HashMap<String, Serializable>();
	//		dic.put("id.modelGeneIdgene", geneid);
	//		dic.put("id.modelEnzymeEcnumber", ecnumber);
	//		List<ModelSubunit> list =  this.findByAttributes(dic);
	//		
	//		Map<Integer, String> dict = null;
	//		
	//		if(list.size() > 0) {
	//			dict = new HashMap<Integer, String>();
	//			for (ModelSubunit x: list){
	//			
	//			//dict.put(x.get); //ONDE ESTÁ O MODULE E O NOTE??!
	//			}
	//		}
	//		return dict;
	//	}		


	@Override 
	public Long countModelSubunitDistinctGeneId() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);

		c.select(cb.count(sub.get("id").get("modelGeneIdgene"))).distinct(true);

		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Object result = q.uniqueResult();

		return (Long) result;
	}	

	@Override 
	public Map<Integer, String> getModelSubunitDistinctGeneIdAndSource() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);

		c.multiselect(sub.get("id").get("modelGeneIdgene"), protein.get("source")).distinct(true); //por o distinct só no idgene

		Predicate filter1 = cb.equal(sub.get("id").get("modelProteinIdprotein"), protein.get("idprotein"));

		c.where(cb.and(filter1)).groupBy(protein.get("source"), sub.get("id").get("modelGeneIdgene"));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		Map<Integer, String> res = new HashMap<Integer, String>();
		if(resultList.size() > 0) {
			for (Object[] x : resultList) {
				if(x[1] == null)
					System.out.println(x[0]);
				res.put((Integer) x[0], (String) x[1]);
			}
		}
		return res;
	}

	@Override 
	public Long countGenesInModel() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
//		Root<ModelSubunit> sub = c.from(ModelSubunit.class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);
		Join<ModelProtein, ModelSubunit> subunit = protein.join("modelSubunits", JoinType.INNER);

		c.multiselect(cb.count(subunit.get("id").get("modelGeneIdgene"))).distinct(true);

		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);

		Object result = q.uniqueResult();

		return (Long) result;
	}	

	@Override 
	public List<ProteinContainer> getModelSubunitAttributes(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelSubunit> c = cb.createQuery(ModelSubunit.class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);

		c.select(sub); 

		Predicate filter1 = cb.equal(sub.get("id").get("modelGeneIdgene"), id);

		c.where(cb.and(filter1));

		Query<ModelSubunit> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelSubunit> resultList = q.getResultList();

		List<ProteinContainer> parsedList = new ArrayList<>();

		if(resultList.size() > 0) {

			for(ModelSubunit item : resultList) {

				ProteinContainer container = new ProteinContainer(item.getModelProtein().getEcnumber());

				container.setName(item.getModelProtein().getName());
				container.setClass_(item.getModelProtein().getClass_());
				container.setExternalIdentifier(item.getModelProtein().getEcnumber());

				parsedList.add(container);
			}


		}
		return parsedList;
	}

	@Override 
	public Map<Integer, String> getModelSubunitDistinctEnzymeProteinIdAndEnzymeEcNumber() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.select(prot);

		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelProtein> list = q.getResultList();

		Map<Integer, String> dics = new HashMap<Integer, String>();

		if(list.size() > 0) {

			for (ModelProtein x: list){
				if(x.getEcnumber() != null)
					dics.put(x.getIdprotein(), x.getEcnumber());
			}
		}
		return dics;
	}

	@Override
	public boolean checkSubunitData(Integer id) throws Exception {

		boolean exists = false;

		List<ModelSubunit> res = this.getAllSubunitByProteinId(id);
		if(res == null)
			exists = true;

		return exists;
	}

	@Override 
	public Map<String,Integer> countGenesReactionsBySubunit() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);
		Root<ModelGene> gene = c.from(ModelGene.class);
		Root<ModelReactionHasModelProtein> reactHasProt = c.from(ModelReactionHasModelProtein.class);

		c.multiselect(gene.get("query"), cb.countDistinct(reactHasProt.get("id").get("modelReactionIdreaction")));

		Predicate filter1 = cb.equal(sub.get("id").get("modelProteinIdprotein"), reactHasProt.get("id").get("modelProteinIdprotein"));
		Predicate filter2 = cb.equal(gene.get("idgene"), sub.get("id").get("modelGeneIdgene"));

		c.where(cb.and(filter1, filter2)).groupBy(gene.get("query")).orderBy(cb.asc(gene.get("query")));

		Map<String, Integer> res = new HashMap<>();

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {

			for(Object[] item: resultList) {

				res.put(String.valueOf(item[0]), Integer.valueOf(item[1].toString()));
			}
		}
		return null;
	}

	@Override 
	public String[][] getSubunitsByGeneId(Integer idGene) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);

		c.multiselect(sub.get("id").get("modelProteinIdprotein"), sub.get("modelProtein").get("ecnumber"));

		Predicate filter1 = cb.equal(sub.get("id").get("modelGeneIdgene"), idGene);
		c.where(cb.and(filter1));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		String[][] res = null;

		if(list != null && !list.isEmpty()) {
			res = new String[list.size()][list.get(0).length];

			for(int row = 0; row < list.size(); row++) {
				Object[] item = list.get(row);
				res[row][0] = item[0]+"__"+item[1];
			}

		}
		return res;	
	}

	@Override 
	public Map<String,Integer> getProteinsCount() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);

		c.multiselect(sub.get("id").get("modelProteinIdprotein"));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		Map<String,Integer> counts = new HashMap<>();

		if(list != null && !list.isEmpty()) {

			for(Object item : list) {

				String protein = item.toString();

				if(counts.containsKey(protein))
					counts.put(protein, counts.get(protein)+1);
				else
					counts.put(protein, 1);

			}
		}
		return counts;	
	}

	@Override
	public List<String[]> getModelEnzymeByEcNumberAndProteinId(Integer proteinId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelProtein> enzyme = c.from(ModelProtein.class);	

		Join<ModelProtein, ModelSubunit> join = enzyme.join("modelSubunits",JoinType.INNER);
		Join<ModelSubunit, ModelGene> join2 = join.join("modelGene", JoinType.INNER);
		Join<ModelGene, ModelGeneHasOrthology> join3 = join2.join("modelGeneHasOrthologies", JoinType.LEFT);
		Join<ModelGeneHasOrthology, ModelOrthology> join4 = join3.join("modelOrthology", JoinType.LEFT);

		c.multiselect(this.getPath("name", join2), this.getPath("locusTag", join2),
				this.getPath("entryId", join4), this.getPath("origin", join2),
				this.getPath("similarity", join3), this.getPath("locusId", join4)).distinct(true);

		Predicate filter1 = cb.equal(this.getPath("id.modelProteinIdprotein", join), proteinId);
		//	    Line commented in SQL query -. "ModelAPi.getGeneData"
		//	    Predicate filter2 = cb.equal(this.getPath("id.enzymeEcnumber", join), ecNumber);

		c.where(cb.and(filter1));
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> parsedList = new ArrayList<>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				String[] list = new String[6];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];
				list[4] = String.valueOf(item[4]);
				list[5] = (String) item[5];
				parsedList.add(list);
			}
		}
		return parsedList;
	}

	@Override
	public List<String[]> getGPRstatusAndReactionAndDefinition(Integer proteinId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);	
		
		Join<ModelProtein, ModelModuleHasModelProtein> moduleHasProtein = protein.join("modelModuleHasModelProteins", JoinType.INNER);
		Join<ModelModuleHasModelProtein, ModelModule> module = moduleHasProtein.join("modelModule", JoinType.INNER);
		

		c.multiselect(module.get("gprStatus"), module.get("reaction"), 
				module.get("definition"), module.get("name")).distinct(true);

		Predicate filter1 = cb.equal(protein.get("idprotein"), proteinId);

		c.where(filter1);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> parsedList = new ArrayList<>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];

				parsedList.add(list);
			}
		}
		return parsedList;
	}

	@Override
	public boolean isProteinEncodedByGenes(Integer proteinId) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		c.select(cb.countDistinct(subunit.get("id").get("modelGeneIdgene"))); 

		Predicate filter1 = cb.equal(subunit.get("id").get("modelProteinIdprotein"),proteinId);

		c.where(cb.and(filter1));
		
		Query<Long> q = super.sessionFactory.getCurrentSession().createQuery(c);

		Long count = q.uniqueResult();
		
		if(count > 0)
			return true;
		
		return false;
	}
	
}

