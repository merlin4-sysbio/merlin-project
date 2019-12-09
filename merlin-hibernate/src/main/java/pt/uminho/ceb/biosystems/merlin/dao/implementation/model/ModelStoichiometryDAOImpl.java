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
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelStoichiometryDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStoichiometry;

public class ModelStoichiometryDAOImpl extends GenericDaoImpl<ModelStoichiometry> implements IModelStoichiometryDAO {

	public ModelStoichiometryDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelStoichiometry.class);

	}

	@Override 
	public void addModelStoichiometry(ModelStoichiometry modelStoichiometry) {
		super.save(modelStoichiometry);

	}

	@Override 
	public void addModelStoichiometryList(List<ModelStoichiometry> modelStoichiometryList) {
		for (ModelStoichiometry modelStoichiometry: modelStoichiometryList) {
			this.addModelStoichiometry(modelStoichiometry);
		}

	}

	@Override 
	public List<ModelStoichiometry> getAllModelStoichiometry() {
		return super.findAll();
	}

	@Override 
	public ModelStoichiometry getModelStoichiometry(Integer id) {
		return super.findById(id);
	}

	@Override 
	public void removeModelStoichiometry(ModelStoichiometry modelStoichiometry) {
		super.delete(modelStoichiometry);

	}

	@Override 
	public void removeModelStoichiometryList(List<ModelStoichiometry> modelStoichiometryList) {
		for (ModelStoichiometry modelStoichiometry: modelStoichiometryList) {
			this.removeModelStoichiometry(modelStoichiometry);
		}

	}

	@Override 
	public void updateModelStoichiometryList(List<ModelStoichiometry> modelStoichiometryList) {
		for (ModelStoichiometry modelStoichiometry: modelStoichiometryList) {
			this.update(modelStoichiometry);
		}

	}

	@Override 
	public void updateModelStoichiometry(ModelStoichiometry modelStoichiometry) {
		super.update(modelStoichiometry);

	}


	@Override 
	public void updateModelStoichiometryByStoichiometryId(Integer id, float coeff, Integer compartmentId, Integer compoundId) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("idstoichiometry", id);


		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("stoichiometricCoefficient", coeff);
		updateAttributes.put("modelCompartment.idcompartment", compartmentId);
		updateAttributes.put("modelCompound.idcompound", compoundId);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override 
	public List<ModelStoichiometry> getAllModelStoichiometryByAttributes(Integer reactionId, Integer compartmentId, Integer compoundId, Float stoichiometricCoef) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("modelReaction.idreaction", reactionId);
		dic.put("modelCompartment.idcompartment", compartmentId);
		dic.put("modelCompound.idcompound", compoundId);
		dic.put("stoichiometricCoefficient", stoichiometricCoef);

		List<ModelStoichiometry> list =  this.findByAttributes(dic);

		return list;		
	}

	@Override 
	public Integer getIdStoichiometry(int idReaction, int m, int idCompartment, double coefficient){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("modelReaction.idreaction", idReaction);
		dic.put("modelCompound.idcompound", m);
		dic.put("modelCompartment.idcompartment", idCompartment);
		dic.put("stoichiometricCoefficient", coefficient);

		ModelStoichiometry st = this.findUniqueByAttributes(dic);

		Integer idstoich = null;

		if (st!= null) {
			idstoich =  st.getIdstoichiometry();
		}
		return idstoich;
	}

	@Override 
	public List<ModelStoichiometry> getAllModelStoichiometryByReactionId(Integer reactionId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelStoichiometry> c = cb.createQuery(ModelStoichiometry.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.select(st); 
		Predicate filter1 = cb.equal(st.get("modelReaction").get("idreaction"), react.get("idreaction"));
		Predicate filter2 = cb.equal(st.get("modelReaction").get("idreaction"), reactionId);
		c.where(cb.and(filter1,filter2));
		Query<ModelStoichiometry> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelStoichiometry> list = q.getResultList();

		return list;
	}

	@Override 
	public List<String[]> getAllModelStoichiometryByReactionId2(Integer reactionId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);

		c.multiselect(st.get("modelCompound").get("idcompound"), st.get("idstoichiometry"), st.get("stoichiometricCoefficient"), st.get("modelCompartment").get("idcompartment")); 

		Predicate filter1 = cb.equal(st.get("modelReaction").get("idreaction"), reactionId);
		c.where(cb.and(filter1));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				list[2] = String.valueOf(item[2]);
				list[3] = String.valueOf(item[3]);

				parsedList.add(list);
			}

			return parsedList;
		}
		return null;
	}



	@Override 
	public List<ModelStoichiometry> getAllModelStoichiometryBySourceAndOriginalReaction(String source, boolean originalReaction ){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelStoichiometry> c = cb.createQuery(ModelStoichiometry.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.select(st); 
		Predicate filter1 = cb.equal(st.get("modelReaction").get("idreaction"), react.get("idreaction"));
		Predicate filter2 = cb.notEqual(react.get("source"), source);
		Predicate filter3 = cb.notEqual(react.get("originalReaction"), originalReaction);
		c.where(cb.and(filter1, filter2, filter3));


		Query<ModelStoichiometry> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelStoichiometry> list = q.getResultList();

		return list;
	}

	@Override 
	public List<ModelStoichiometry> getAllModelStoichiometryBySourceAndOriginalReaction2(String source, boolean originalReaction ){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelStoichiometry> c = cb.createQuery(ModelStoichiometry.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.select(st); 
		Predicate filter1 = cb.equal(st.get("modelReaction").get("idreaction"), react.get("idreaction"));
		Predicate filter2 = cb.notEqual(react.get("source"), source);
		Predicate filter3 = cb.equal(react.get("originalReaction"), originalReaction);
		c.where(cb.and(filter1, filter2, filter3));


		Query<ModelStoichiometry> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelStoichiometry> list = q.getResultList();

		return list;
	}


	@Override 
	public List<ModelStoichiometry> getDistinctModelStoichiometryCompoundId(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelStoichiometry> c = cb.createQuery(ModelStoichiometry.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.multiselect(st.get("modelCompound").get("idcompound")).distinct(true); 
		Predicate filter1 = cb.equal(st.get("modelReaction").get("idreaction"), react.get("idreaction"));
		Predicate filter2 = cb.equal(react.get("inModel"), true);

		c.where(cb.and(filter1, filter2));

		Query<ModelStoichiometry> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelStoichiometry> list = q.getResultList();

		return list;
	}


	@Override 
	public List<ModelStoichiometry> getAllModelStoichiometryAttributes(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelStoichiometry> c = cb.createQuery(ModelStoichiometry.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.select(st); 
		Predicate filter1 = cb.equal(st.get("modelReaction").get("idreaction"), react.get("idreaction"));
		c.where(cb.and(filter1));

		Query<ModelStoichiometry> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelStoichiometry> list = q.getResultList();

		return list;
	}

	@Override 
	public List<Object[]> getAllModelStoichiometryAttributes2(boolean isCompartimentalizedModel){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.multiselect(st.get("modelCompound").get("idcompound"), st.get("stoichiometricCoefficient"), react.get("idreaction")); 
		Predicate filter1 = cb.equal(st.get("modelReaction").get("idreaction"), react.get("idreaction"));
		Predicate filter2 = cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalizedModel) 
			filter2 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		c.where(cb.and(filter1, filter2));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		return q.getResultList();

	}
	@Override 
	public List<String[]> getAllModelStoichiometryAttributesByReactionId(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);
		Root<ModelCompartment> compart = c.from(ModelCompartment.class);

		c.multiselect(compound.get("name"), compound.get("formula"), compart.get("name"), st.get("stoichiometricCoefficient"), compound.get("externalIdentifier")); 
		Predicate filter1 = cb.equal(compound.get("idcompound"), st.get("modelCompound").get("idcompound"));
		Predicate filter2 = cb.equal(compart.get("idcompartment"), st.get("modelCompartment").get("idcompartment"));
		Predicate filter3 = cb.equal(st.get("reactionIdreaction"), id);
		c.where(cb.and(filter1, filter2, filter3));


		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[6];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = String.valueOf(item[3]);
				list[4] =  null; //numberOfChains was in this position
				list[5] = (String) item[5];
				parsedList.add(list);
			}

			return parsedList;
		}
		return null;
	}


	@Override 
	public List<String[]> getModelStoichiometryAttributesByReactionId(int reactionId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelCompartment> comp = c.from(ModelCompartment.class);

		c.multiselect(comp.get("name"), st.get("stoichiometricCoefficient"), st.get("modelCompound").get("idcompound")); 
		Predicate filter1 = cb.equal(st.get("modelCompartment").get("idcompartment"), comp.get("idcompartment"));
		Predicate filter2 = cb.equal(st.get("modelReaction").get("idreaction"), reactionId);

		c.where(cb.and(filter1, filter2));
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = (String) item[0];
				list[1] = String.valueOf(item[1]);
				list[2] = null; //numberOfChains was in this position
				list[3] = String.valueOf(item[3]);
				parsedList.add(list);
			}
			return parsedList;
		}
		return null;
	}


	@Override 
	public List<Integer> getDistinctReversibleCompoundIds(Boolean inModel, Boolean isCompartimentalized){  
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);

		c.multiselect(st.get("modelCompound").get("idcompound"), st.get("modelReaction").get("lowerBound"), st.get("modelReaction").get("upperBound")).distinct(true);

		List<Predicate> filters = new ArrayList<Predicate>();

		//		filters.add(cb.equal(st.get("modelReaction").get("reversible"), true));

		if(inModel)
			filters.add(cb.equal(st.get("modelReaction").get("inModel"), inModel));

		if(isCompartimentalized) 
			filters.add(cb.isNotNull(st.get("modelReaction").get("modelCompartment").get("idcompartment")));
		else
			filters.add(cb.isNull(st.get("modelReaction").get("modelCompartment").get("idcompartment")));

		Order[] orderList = {cb.asc(st.get("modelCompound").get("idcompound"))};

		c.where(cb.and(filters.toArray(new Predicate[] {}))).orderBy(orderList);
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			List<Integer> list = new ArrayList<Integer>();
			for (Object[] x :  resultList) {

				if((Double.valueOf(x[1]+"") < 0 && Double.valueOf(x[2]+"") > 0))
					list.add((Integer) x[0]);
			}
			return list;
		}
		return null;	    
	}


	@Override 
	public List<String[]> getModelStoichiometryCompartmentNameAndDistinctCompoundId(){  //nao tem o aux
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelCompartment> compt = c.from(ModelCompartment.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);


		c.multiselect(compt.get("name"), cb.countDistinct(st.get("modelCompound").get("idcompound"))); 
		Predicate filter1 = cb.equal(compt.get("idcompartment"), st.get("modelCompartment").get("idcompartment"));
		Predicate filter2 = cb.equal(react.get("idreaction"), st.get("modelReaction").get("idreaction"));

		c.where(cb.and(filter1, filter2)).groupBy((compt.get("name")));
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[2];
				list[0] = (String) item[0];
				list[1] = String.valueOf(item[1]);

				parsedList.add(list);
			}

			return parsedList;
		}
		return null;
	}


	@Override 
	public List<String[]> getModelStoichiometryCompoundIdAndStoichiometryCoef() {
		List<String[]> result = new ArrayList<String[]>();

		List<ModelStoichiometry> list = this.findAll();
		if (list != null) {
			for(ModelStoichiometry item: list) {
				String[] res = new String[2];
				res[0] = String.valueOf(item.getModelCompound().getIdcompound());
				res[1] = String.valueOf(item.getStoichiometricCoefficient());

				result.add(res);	
			}
		}
		return result;
	}

	@Override 
	public List<String[]> getAllModelStoichiometryAttributesByCompoundId(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);

		c.multiselect(compound.get("name"), compound.get("formula"), reaction.get("name"), reaction.get("equation")); 
		Predicate filter1 = cb.equal(compound.get("idcompound"), st.get("modelCompound").get("idcompound"));
		Predicate filter2 = cb.equal(reaction.get("idreaction"), st.get("reactionIdreaction"));
		Predicate filter3 = cb.equal(compound.get("idcompound"), id);
		c.where(cb.and(filter1, filter2, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[4];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[4] = (String) item[3];

				parsedList.add(list);
			}
			return parsedList;
		}
		return null;
	}

	@Override 
	public List<String[]> getModelStoichiometryAttributes(boolean isCompartmentalized){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);


		c.multiselect(st.get("stoichiometricCoefficient"), cb.countDistinct(st.get("modelCompound").get("idcompound")),
				react.get("idreaction")); 
		Predicate filter1 = cb.equal(react.get("idreaction"), st.get("modelReaction").get("idreaction"));

		Predicate filter2 = cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartmentalized) 
			filter2 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		c.where(cb.and(filter1, filter2));
		c.groupBy(react.get("idreaction"), st.get("stoichiometricCoefficient"));
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> parsedList = new ArrayList<>();

		if(resultList.size() > 0) {
			for(Object[] item: resultList) {
				String[] list = new String[3];
				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				list[2] = String.valueOf(item[2]);

				parsedList.add(list);
			}
		}
		return parsedList;
	}

	@Override 
	public List<String[]> getModelStoichiometryDifferentAttributesByReactionId(Integer reactionId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);
		Root<ModelCompartment> compartment = c.from(ModelCompartment.class);

		c.multiselect(compound.get("idcompound"), compound.get("name"), compound.get("formula"), st.get("stoichiometricCoefficient"),
				compartment.get("name"), st.get("idstoichiometry"));  
		Predicate filter1 = cb.equal(st.get("modelCompound").get("idcompound"), compound.get("idcompound"));
		Predicate filter2 = cb.equal(st.get("modelCompartment").get("idcompartment"), compartment.get("idcompartment"));
		Predicate filter3 = cb.equal(st.get("modelReaction").get("idreaction"), reactionId);

		c.where(cb.and(filter1, filter2, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[7];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = String.valueOf(item[3]);
				list[4] = null; //numberOfChains was in this position
				list[5] = (String) item[5];
				list[6] = String.valueOf(item[6]);

				parsedList.add(list);
			}
			return parsedList;
		}
		return null;
	}

	@Override 
	public boolean deleteModelStoichiometryByStoichiometryId(Integer stoichId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("idstoichiometry", stoichId);

		List<ModelStoichiometry> list =  this.findByAttributes(map);
		this.removeModelStoichiometryList(list);
		return true;
	}

	@Override
	public Map<Integer, MetaboliteContainer> getModelStoichiometryByReactionId(Integer reactionID){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelStoichiometry> c = cb.createQuery(ModelStoichiometry.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.select(st);

		Predicate filter1 = cb.equal(react.get("idreaction"), st.get("modelReaction").get("idreaction"));
		Predicate filter2 = cb.equal(react.get("idreaction"), reactionID);

		c.where(cb.and(filter1, filter2));

		Query<ModelStoichiometry> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelStoichiometry> list = q.getResultList();

		Map<Integer, MetaboliteContainer> result = new HashMap<>();

		if(list != null) {
			for(ModelStoichiometry res : list) {

				MetaboliteContainer container = new MetaboliteContainer(res.getModelCompound().getIdcompound(), 
						res.getStoichiometricCoefficient(), res.getModelCompartment().getIdcompartment());

				container.setName(res.getModelCompound().getName());
				container.setFormula(res.getModelCompound().getFormula());
				container.setCompartment_name(res.getModelCompartment().getName());

				result.put(res.getIdstoichiometry(), container);

			}
		}

		return result;
	}

	@Override
	public List<Integer[]> getStoichiometryDataFromTransportersSource(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelStoichiometry> stoichi = c.from(ModelStoichiometry.class);

		c.multiselect(stoichi.get("idstoichiometry"), 
				stoichi.get("modelReaction").get("idreaction"), 
				stoichi.get("modelCompound").get("idcompound"), 
				stoichi.get("modelCompartment").get("idcompartment"),
				stoichi.get("stoichiometricCoefficient"));

		Predicate filter = cb.isNull(stoichi.get("modelCompartment").get("idcompartment"));
		Predicate filter2 = cb.notEqual(stoichi.get("modelReaction").get("modelReactionLabels").get("source"), SourceType.TRANSPORTERS.toString());

		c.where(filter,filter2);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<Integer[]> res = new ArrayList<>();

		if(resultList!=null && resultList.size() > 0) {

			for (Object[] x: resultList) {

				Integer[] list2 = new Integer[5] ;

				list2[0] = (Integer) x[0]; // stoichiometryId
				list2[1] = (Integer) x[1]; // reactionId
				list2[2] = (Integer) x[2]; // compoundId
				list2[3] = (Integer) x[3]; // compartmentId
				list2[4] = (Integer) x[4]; // stoichiometricCoefficient

				res.add(list2);
			}
		}
		return res;	
	}

	@Override
	public Map<Integer, ReactionContainer> getAllOriginalTransportersFromStoichiometry() { 
		boolean compartmentalized = false;

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);

		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);

		c.multiselect(reaction.get("idreaction"), st.get("modelCompound").get("idcompound"),
				st.get("stoichiometricCoefficient"), st.get("modelCompartment").get("idcompartment"),
				st.get("modelCompartment").get("abbreviation"));

		Predicate filter1 = cb.equal(reaction.get("idreaction"), st.get("modelReaction").get("idreaction"));

		Predicate filter2 = cb.equal(reaction.get("modelReactionLabels").get("source"), SourceType.TRANSPORTERS.toString());

		Predicate filter3 = null;

		if(compartmentalized)
			filter3 = cb.isNotNull(reaction.get("modelCompartment").get("idcompartment"));
		else
			filter3 = cb.isNull(reaction.get("modelCompartment").get("idcompartment"));

		c.where(cb.and(filter1, filter2, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		Map<Integer, ReactionContainer> containers = new HashMap<>();

		if(list != null) {
			for(Object[] item : list) {

				Integer reactionId = Integer.valueOf(item[0].toString());

				MetaboliteContainer metaboliteContainer = new MetaboliteContainer(Integer.valueOf(item[1].toString()),
						Double.valueOf(item[2].toString()), Integer.valueOf(item[3].toString()));
				
				metaboliteContainer.setAbbreviation(item[4].toString());

				if(!containers.containsKey(reactionId)) {
					containers.put(reactionId, new ReactionContainer(reactionId));
				}

				if(metaboliteContainer.getStoichiometric_coefficient() < 0)
					containers.get(reactionId).addReactant(metaboliteContainer);
				else
					containers.get(reactionId).addProduct(metaboliteContainer);

			}
		}
		return containers;
	}

	@Override
	public List<String[]> getCompoundDataFromStoichiometry(Integer idreaction) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("modelReaction", idreaction);

		List<ModelStoichiometry> result =  this.findByAttributes(map); 
		List<String[]> list = new ArrayList<String[]>();

		if(result.size() > 0) {

			for (ModelStoichiometry x: result){

				String[] newList = new String[5];

				newList[0] = x.getModelCompound().getName();
				newList[1] = x.getModelCompound().getFormula();
				newList[2] = x.getModelCompartment().getName();
				newList[3] = String.valueOf(x.getStoichiometricCoefficient());
				newList[4] = x.getModelCompound().getExternalIdentifier();

				list.add(newList);

			}
		}

		return list;
	}

	@Override
	public Set<String> checkUndefinedStoichiometry(boolean isCompartimentalized) { 

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String> c = cb.createQuery(String.class);

		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);

		c.select(st.get("modelReaction").get("modelReactionLabels").get("name"));

		Predicate filter1 = cb.equal(st.get("modelReaction").get("inModel"), true);
		Predicate filter2 = cb.equal(st.get("stoichiometricCoefficient"), 0);

		Predicate filter3 = cb.isNotNull(st.get("modelReaction").get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized)
			filter3 = cb.isNull(st.get("modelReaction").get("modelCompartment").get("idcompartment"));

		c.where(cb.and(filter1, filter2, filter3)).orderBy(cb.asc(st.get("modelReaction").get("idreaction")));

		Query<String> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<String> list = q.getResultList();

		if(list == null)
			return null;

		return new HashSet<>(list);
	}

	@Override
	public void replaceCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaUpdate<ModelStoichiometry> update = cb.createCriteriaUpdate(ModelStoichiometry.class);
		Root<ModelStoichiometry> stoichiometry = update.from(ModelStoichiometry.class);

		update.set(stoichiometry.get("modelCompartment").get("idcompartment"), compartmentIdToKeep);
		update.where(cb.equal(stoichiometry.get("modelCompartment").get("idcompartment"), compartmentIdToReplace));
		
		super.sessionFactory.getCurrentSession().createQuery(update).executeUpdate();
	}
}


