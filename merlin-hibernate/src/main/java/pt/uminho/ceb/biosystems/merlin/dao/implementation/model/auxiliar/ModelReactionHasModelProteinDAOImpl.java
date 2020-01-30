package pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar;

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

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathway;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProteinId;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


public class ModelReactionHasModelProteinDAOImpl extends GenericDaoImpl<ModelReactionHasModelProtein> implements IModelReactionHasModelProteinDAO{

	public ModelReactionHasModelProteinDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelReactionHasModelProtein.class);

	}

	public void addModelReactionHasModelProtein(ModelReactionHasModelProtein modelReactionHasEnzyme) {
		super.save(modelReactionHasEnzyme);

	}

	public void addModelReactionHasModelProteinList(List<ModelReactionHasModelProtein> modelReactionHasEnzymeList) {
		for (ModelReactionHasModelProtein modelReactionHasEnzyme: modelReactionHasEnzymeList) {
			this.addModelReactionHasModelProtein(modelReactionHasEnzyme);
		}

	}

	public List<ModelReactionHasModelProtein> getAllModelReactionHasModelProtein() {
		return super.findAll();
	}

	public ModelReactionHasModelProtein getModelReactionHasModelProtein(Integer id) {
		return super.findById(id);
	}

	public void removeModelReactionHasModelProtein(ModelReactionHasModelProtein modelReactionHasEnzyme) {
		super.delete(modelReactionHasEnzyme);

	}

	public void removeModelReactionHasModelProteinList(List<ModelReactionHasModelProtein> modelReactionHasEnzymeList) {
		for (ModelReactionHasModelProtein modelReactionHasEnzyme: modelReactionHasEnzymeList) {
			this.removeModelReactionHasModelProtein(modelReactionHasEnzyme);
		}

	}

	public void updateModelReactionHasModelProteinList(List<ModelReactionHasModelProtein> modelReactionHasEnzymeList) {
		for (ModelReactionHasModelProtein modelReactionHasEnzyme: modelReactionHasEnzymeList) {
			this.update(modelReactionHasEnzyme);
		}

	}

	public void updateModelReactionHasModelProtein(ModelReactionHasModelProtein modelReactionHasEnzyme) {
		super.update(modelReactionHasEnzyme);

	}

	public ModelReactionHasModelProteinId insertModelReactionHasModelProtein(Integer reactionId, Integer proteinId){
		ModelReactionHasModelProtein model = new ModelReactionHasModelProtein();
		ModelReactionHasModelProteinId id = new ModelReactionHasModelProteinId();
		id.setModelReactionIdreaction(reactionId);
		id.setModelProteinIdprotein(proteinId);
		model.setId(id);
		return (ModelReactionHasModelProteinId) this.save(model);
	}

	@Override
	public List<ModelReactionHasModelProtein> getAllModelReactionHasModelProteinByAttributes(Integer idprotein, Integer idReaction){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelProteinIdprotein", idprotein);
		dic.put("id.modelReactionIdreaction", idReaction);
		List<ModelReactionHasModelProtein> list =  this.findByAttributes(dic);

		return list;
	}

	public List<ModelReactionHasModelProtein> getAllModelReactionHasModelProteinByReactionId(Integer idReaction){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelReactionIdreaction", idReaction);
		List<ModelReactionHasModelProtein> list =  this.findByAttributes(dic);

		return list;
	}


	public List<ModelReactionHasModelProtein> getAllModelReactionHasModelProteinOrderByReactionId(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReactionHasModelProtein> c = cb.createQuery(ModelReactionHasModelProtein.class);
		Root<ModelReactionHasModelProtein> react = c.from(ModelReactionHasModelProtein.class);

		Order[] orderList = {cb.asc(react.get("id").get("modelReactionIdreaction"))};
		c.orderBy(orderList);

		Query<ModelReactionHasModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);

		List<ModelReactionHasModelProtein> resultList = q.getResultList();

		return resultList; 
	}

	public List<String[]> getAllModelReactionHasModelProteinByreactionId(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReactionHasModelProtein> react = c.from(ModelReactionHasModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);
		//Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		c.multiselect(prot.get("ecnumber"),
				prot.get("idprotein"), prot.get("name"), react.get("modelReaction").get("inModel"));

		Predicate filter1 = cb.equal(react.get("id").get("modelProteinIdprotein"), prot.get("idprotein"));
		Predicate filter2 = cb.equal(react.get("id").get("modelReactionIdreaction"), id);
		//Predicate filter3 = cb.equal(prot.get("idprotein"), subunit.get("modelProtein").get("idprotein"));

		c.where(cb.and(filter1, filter2));
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);

		List<Object[]> resultList = q.getResultList();
		ArrayList<String[]> parsedList = new ArrayList<String[]>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {

				String[] list = new String[4];

				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				list[2] = (String) item[2];
				list[3] = item[3].toString();

				parsedList.add(list);

			}
			return parsedList;
		}
		return parsedList;
	}

	public List<String[]> getAllModelReactionHasModelProteinByreactionId2(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReactionHasModelProtein> react = c.from(ModelReactionHasModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.multiselect(react.get("id").get("modelEnzymeEcnumber"), prot.get("name"), 
				react.get("modelReaction").get("inModel"), react.get("id").get("modelEnzymeModelProteinIdprotein"));

		Predicate filter = cb.equal(react.get("id").get("modelEnzymeModelProteinIdprotein"), prot.get("idprotein"));
		//Predicate filter4 = cb.equal(react.get("id").get("modelEnzymeEcnumber"), enzyme.get("id").get("ecnumber")); REPETIDA

		c.where(filter);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);

		List<Object[]> resultList = q.getResultList();

		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				list[3] = String.valueOf(item[3]);
				parsedList.add(list);

			}
			return parsedList;
		}
		return null;
	}


	public List<String[]> getReactionHasEnzyme (){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReactionHasModelProtein> reactionHasEnzyme = c.from(ModelReactionHasModelProtein.class);	
		Root<ModelSubunit> modelSubunit = c.from(ModelSubunit.class);	
		Root<ModelGene> modelGene = c.from(ModelGene.class);	

		c.multiselect(reactionHasEnzyme.get("id").get("modelReactionIdreaction"), modelGene.get("name"),
				modelGene.get("locusTag"),modelSubunit.get("id").get("modelEnzymeEcnumber"));
		Predicate filter1 = cb.equal(this.getPath("id.modelEnzymeModelProteinIdprotein", reactionHasEnzyme),
				this.getPath("id.modelEnzymeModelProteinIdprotein", modelSubunit));
		Predicate filter2 = cb.equal(this.getPath("id.modelEnzymeEcnumber", reactionHasEnzyme),
				this.getPath("id.modelEnzymeEcnumber", modelSubunit));

		Predicate filter3 = cb.equal(this.getPath("id.geneIdgene", modelSubunit), this.getPath("idgene", modelGene));

		Order[] orderList = {cb.asc(reactionHasEnzyme.get("id").get("modelReactionIdreaction"))};
		c.orderBy(orderList);

		c.where(cb.and(filter1,filter2,filter3));

		Query<Object []> q = super.sessionFactory.getCurrentSession().createQuery(c);

		List<Object []> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] =  String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];
				parsedList.add(list);
			}
			return parsedList;
		}
		return null;
	}

	public List<String[]> getModelReactionHasModelProteinProteinIdAndEcNumberByreactionId(Integer reactionId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReactionHasModelProtein> react = c.from(ModelReactionHasModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.multiselect(react.get("modelProtein").get("idprotein"), react.get("modelProtein").get("ecnumber"));

		Predicate filter1 = cb.equal(react.get("modelReaction").get("inModel"), true);
		Predicate filter2 = cb.equal(react.get("id").get("modelReactionIdreaction"), reactionId);
		Predicate filter3 = cb.equal(react.get("id").get("modelProteinIdprotein"), prot.get("idprotein"));
		c.where(cb.and(filter1, filter2, filter3));

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
	public List<Pair<String, String>> getModelReactionHasModelProteinData(boolean isCompartimentalised){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReactionHasModelProtein> c = cb.createQuery(ModelReactionHasModelProtein.class);
		
		Root<ModelReactionHasModelProtein> reacEnz = c.from(ModelReactionHasModelProtein.class);

		c.select(reacEnz);
		
		Predicate filter = cb.isNull(reacEnz.get("modelReaction").get("modelCompartment").get("idcompartment"));
		
		if(isCompartimentalised)
			filter = cb.isNotNull(reacEnz.get("modelReaction").get("modelCompartment").get("idcompartment"));

		Predicate filter1 = cb.equal(reacEnz.get("modelReaction").get("inModel"), true);
		

		Order[] orderList = {cb.asc(reacEnz.get("id").get("modelReactionIdreaction"))}; 

		c.where(cb.and(filter, filter1)).orderBy(orderList);

		Query<ModelReactionHasModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReactionHasModelProtein> resultList = q.getResultList();
		
		List<Pair<String, String>> parsedList = new ArrayList<>();
		
		if(resultList != null) {

			for(ModelReactionHasModelProtein reactionHasProtein: resultList) {
				
				Pair<String, String> pair = new Pair<>(reactionHasProtein.getModelReaction().getModelReactionLabels().getName(),
						reactionHasProtein.getModelProtein().getEcnumber());
				
				parsedList.add(pair);	
			}
		}
		return parsedList;
	}
	
	@Override
	public List<Pair<Integer, String>> getModelReactionHasModelProteinData2(boolean isCompartimentalised){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReactionHasModelProtein> c = cb.createQuery(ModelReactionHasModelProtein.class);
		
		Root<ModelReactionHasModelProtein> reacEnz = c.from(ModelReactionHasModelProtein.class);

		c.select(reacEnz);
		
		Predicate filter = cb.isNull(reacEnz.get("modelReaction").get("modelCompartment").get("idcompartment"));
		
		if(isCompartimentalised)
			filter = cb.isNotNull(reacEnz.get("modelReaction").get("modelCompartment").get("idcompartment"));

		Predicate filter1 = cb.equal(reacEnz.get("modelReaction").get("inModel"), true);
		

		Order[] orderList = {cb.asc(reacEnz.get("id").get("modelReactionIdreaction"))}; 

		c.where(cb.and(filter, filter1)).orderBy(orderList);

		Query<ModelReactionHasModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReactionHasModelProtein> resultList = q.getResultList();
		
		List<Pair<Integer, String>> parsedList = new ArrayList<>();
		
		if(resultList != null) {

			for(ModelReactionHasModelProtein reactionHasProtein: resultList) {
				
				Pair<Integer, String> pair = new Pair<>(reactionHasProtein.getModelReaction().getIdreaction(),
						reactionHasProtein.getModelProtein().getEcnumber());
				
				parsedList.add(pair);	
			}
		}
		return parsedList;
	}


	public List<String[]> getModelReactionHasModelProteinData2(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReactionHasModelProtein> reacEnz = c.from(ModelReactionHasModelProtein.class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);
		Root<ModelGene> gene = c.from(ModelGene.class);

		c.multiselect(reacEnz.get("id").get("modelReactionIdreaction"), 
				gene.get("name"), gene.get("locusTag"), sub.get("id").get("modelEnzymeEcnumber")).distinct(true);

		Predicate filter1 = cb.equal(sub.get("id").get("modelEnzymeModelProteinIdprotein"),reacEnz.get("id").get("modelEnzymeModelProteinIdprotein") );
		Predicate filter2 = cb.equal(gene.get("idgene"), sub.get("id").get("modelGeneIdgene"));
		//Predicate filter3 = cb.notEqual(x, null); //onde está o note????!!!

		Order[] orderList = {cb.asc(reacEnz.get("id").get("modelReactionIdreaction"))}; 

		c.where(cb.and(filter1,filter2)).orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];
				parsedList.add(list);	
			}

			return parsedList;
		}
		return null;
	}

	public List<String[]> getModelReactionHasModelProteinAttributesByReactionId(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReactionHasModelProtein> react = c.from(ModelReactionHasModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.multiselect(react.get("id").get("modelEnzymeEcnumber"), prot.get("idprotein"), prot.get("name"));

		Predicate filter1 = cb.equal(react.get("id").get("modelEnzymeModelProteinIdprotein"), prot.get("idprotein"));
		Predicate filter2 = cb.equal(react.get("id").get("modelReactionIdreaction"), id);
		c.where(cb.and(filter1, filter2));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);

		List<Object[]> resultList = q.getResultList();

		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[3];
				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				list[2] = (String) item[2];
				parsedList.add(list);
			}
			return parsedList;
		}
		return null;
	}

	@Override
	public List<String> getModelReactionHasModelProteinEcNumberByReactionId(Integer reactId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.modelReactionIdreaction", reactId);

		List<ModelReactionHasModelProtein> res = this.findByAttributes(map);

		List<String> listECs = new ArrayList<>();

		if (res!=null && res.size()>0) {
			for(ModelReactionHasModelProtein reactHasEnz : res)
				listECs.add(reactHasEnz.getModelProtein().getEcnumber());
		}
		return listECs;
	}

	@Override
	public List<String[]> getModelReactionAttributes(Integer rowId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);	
		Join<ModelReaction, ModelPathway> path = reaction.join("modelPathways", JoinType.LEFT);
		Join<ModelReaction, ModelCompartment> compartment = reaction.join("modelCompartment", JoinType.LEFT);

		c.multiselect(reaction.get("name"), reaction.get("equation"), reaction.get("reversible"),
				path.get("name"), reaction.get("inModel"), compartment.get("name"), reaction.get("isSpontaneous"),
				reaction.get("isNonEnzymatic"), reaction.get("isGeneric"), reaction.get("lowerBound"),
				reaction.get("upperbound"), reaction.get("booleanRule"));

		Predicate filter1 = cb.equal(path.get("idpathway"), reaction.get("modelPathways").get("idpathway"));
		Predicate filter2 = cb.equal(compartment.get("idcompartment"), reaction.get("modelCompartment").get("idcompartment"));
		Predicate filter3 = cb.equal(reaction.get("idreaction"), rowId);

		c.where(cb.and(filter1,filter2, filter3));
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[12];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				list[3] = (String) item[3];
				list[4] = String.valueOf(item[4]);
				list[5] = (String) item[5];
				list[6] = String.valueOf(item[6]);
				list[7] = String.valueOf(item[7]);
				list[8] = String.valueOf(item[8]);
				list[9] = String.valueOf(item[9]);
				list[10] = String.valueOf(item[10]);
				list[11] = String.valueOf(item[11]);
				parsedList.add(list);
			}
			return parsedList;
		}
		return null;
	}

	@Override
	public boolean deleteModelReactionHasModelProteinByReactionId(Integer reactionId, Integer protId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.modelReactionIdreaction", reactionId);
		map.put("id.modelProteinIdprotein", protId);

		List<ModelReactionHasModelProtein> list =  this.findByAttributes(map);
		this.removeModelReactionHasModelProteinList(list);
		return true;
	}

	@Override
	public boolean deleteModelReactionHasModelProteinByReactionIdAndPathId(Integer reactionId, Integer protId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.modelReactionIdreaction", reactionId);
		map.put("id.modelProteinIdprotein", protId);

		List<ModelReactionHasModelProtein> list =  this.findByAttributes(map);
		this.removeModelReactionHasModelProteinList(list);;
		return true;
	}

	@Override
	public Map<String, List<Integer>> getEcNumbersByReactionId(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReactionHasModelProtein> c = cb.createQuery(ModelReactionHasModelProtein.class);
		Root<ModelReactionHasModelProtein> reactHasProt = c.from(ModelReactionHasModelProtein.class);
		c.multiselect(reactHasProt);

		Query<ModelReactionHasModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReactionHasModelProtein> list = q.getResultList();

		Map<String, List<Integer>> result = new HashMap<>();

		if(list.size() > 0) {
			for(ModelReactionHasModelProtein react: list) {

				String ecNumber = react.getModelProtein().getEcnumber();
				Integer reactionId = react.getId().getModelReactionIdreaction();

				if(ecNumber != null) {

					if(!result.containsKey(ecNumber))
						result.put(ecNumber, new ArrayList<Integer>());

					if(!result.get(ecNumber).contains(reactionId))
						result.get(ecNumber).add(reactionId);
				}

			}
		}
		return result;
	}

}
