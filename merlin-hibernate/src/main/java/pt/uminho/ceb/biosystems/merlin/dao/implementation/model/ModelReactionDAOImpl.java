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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModule;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelModuleHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathway;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStoichiometry;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;

public class ModelReactionDAOImpl extends GenericDaoImpl<ModelReaction> implements IModelReactionDAO {

	public ModelReactionDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelReaction.class);

	}

	@Override
	public void addModelReaction(ModelReaction modelReaction) {
		super.save(modelReaction);

	}

	@Override
	public void addModelReactionList(List<ModelReaction> modelReactionList) {
		for (ModelReaction modelReaction: modelReactionList) {
			this.addModelReaction(modelReaction);
		}

	}

	@Override
	public List<ModelReaction> getAllModelReaction() {
		return super.findAll();
	}

	@Override
	public ModelReaction getModelReaction(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelReaction(ModelReaction modelReaction) {
		super.delete(modelReaction);

	}

	@Override
	public void removeModelReactionList(List<ModelReaction> modelReactionList) {
		for (ModelReaction modelReaction: modelReactionList) {
			this.removeModelReaction(modelReaction);
		}

	}

	@Override
	public void updateModelReactionList(List<ModelReaction> modelReactionList) {
		for (ModelReaction modelReaction: modelReactionList) {
			this.update(modelReaction);
		}

	}

	@Override
	public void updateModelReaction(ModelReaction modelReaction) {
		super.update(modelReaction);

	}

	@Override
	public Boolean checkReactionNotLikeSourceAndNotReversible(String source){ 

		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReaction> c = cb.createQuery(ModelReaction.class);

		Root<ModelReaction> table = c.from(ModelReaction.class); 
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class); 

		c.select(table); 

		Predicate filter1 = cb.equal(table.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter2 = cb.notEqual(reactionLabels.get("source"), source);

		c.where(filter1, filter2);
		Query<ModelReaction> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReaction> resultList = q.getResultList();


		for(ModelReaction reaction : resultList) {

			ReactionContainer container = new ReactionContainer(reaction.getIdreaction(), reaction.getInModel(), reaction.getLowerBound(), reaction.getUpperBound());
			if(container.isReversible())
				return true;
		}

		return false;



	}

	@Override
	public Boolean isCompartimentalizedModel(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.multiselect(cb.count(react.get("idreaction")));  

		Predicate filter1 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));
		Predicate filter2 = cb.equal(react.get("inModel"), true);

		c.where(filter1, filter2);

		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Long result = (Long) q.uniqueResult();

		return (result != null && result > 0);

	}

	@Override
	public List<Integer> getDistinctReactionsByEnzymeAndCompartmentalized(Integer proteinId, boolean hasPathway, boolean isCompartimentalized){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		Root<ModelReactionHasModelProtein> reactionHasProtein = c.from(ModelReactionHasModelProtein.class);
		Root<ModelPathwayHasModelProtein> pathProtein = c.from(ModelPathwayHasModelProtein.class);
		Root<ModelPathwayHasReaction> pathReaction = c.from(ModelPathwayHasReaction.class);
		c.multiselect(reaction.get("idreaction")).distinct(true);
		Predicate filter1 = cb.equal(reactionHasProtein.get("id").get("reactionIdreaction"),  reaction.get("idreaction"));
		Predicate filter2 = cb.equal(pathProtein.get("id").get("modelProteinIdprotein"), reactionHasProtein.get("id").get("modelProteinIdprotein"));
		Predicate filter3 = cb.equal(pathReaction.get("id").get("pathwayIdpathway"), pathProtein.get("id").get("modelPathwayIdpathway")); 
		Predicate filter4 = cb.equal(reactionHasProtein.get("id").get("modelProteinIdprotein"), proteinId);

		Predicate constraints = cb.and(filter1, filter2, filter3, filter4);

		if(isCompartimentalized) {
			Predicate filter6 = cb.isNotNull(reaction.get("modelCompartment"));

			constraints = cb.and(constraints, filter6);
		}


		if(hasPathway) {
			Predicate filter7 = cb.equal(pathReaction.get("id").get("reactionIdreaction"), reaction.get("idreaction"));

			constraints = cb.and(constraints, filter7);
		}

		c.where(constraints);

		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer> list = q.getResultList();
		return list;

	}

	@Override
	public List<Object[]> getReactionsList(Integer pathwayID, boolean noEnzymes, boolean isCompartimentalized){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);

		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelCompound> comp = c.from(ModelCompound.class);
		Root<ModelPathwayHasReaction> pathHasReact = c.from(ModelPathwayHasReaction.class);

		c.multiselect(reactionLabels.get("name"), comp.get("externalIdentifier"));

		Predicate filter1 = cb.equal(reaction.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter2 = cb.equal(reaction.get("idreaction"), st.get("modelReaction").get("idreaction"));
		Predicate filter3 = cb.equal(st.get("modelCompound").get("idcompound"), comp.get("idcompound"));
		Predicate filter4 = cb.equal(reaction.get("inModel"), true);

		Predicate filter5 = cb.isNull(reaction.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter5 = cb.isNotNull(reaction.get("modelCompartment").get("idcompartment"));

		Predicate filter10 = cb.equal(reaction.get("idreaction"), pathHasReact.get("id").get("reactionIdreaction"));
		Predicate filter11 = cb.equal(pathHasReact.get("id").get("pathwayIdpathway"), pathwayID);

		Predicate constraints = cb.and(filter1, filter2, filter3, filter4, filter5, filter10, filter11);

		if(noEnzymes) {
			Join<ModelReaction, ModelReactionHasModelProtein> enz = reaction.join("modelReactionHasModelProteins",JoinType.LEFT);

			Predicate filter6 = cb.equal(reactionLabels.get("isNonEnzymatic"), true);
			Predicate filter7 = cb.equal(reactionLabels.get("isSpontaneous"), true);
			Predicate filter8 = cb.equal(reactionLabels.get("source"), SourceType.MANUAL.toString());
			Predicate filter9 = cb.isNull(enz.get("modelProtein").get("ecnumber"));

			Predicate constraints2 = cb.or(filter6, filter7, filter8, filter9);

			c.where(constraints, constraints2);
		}
		else
			c.where(constraints);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		return list;

	}

	@Override
	public List<ModelReaction> getAllModelReactionsByAttributes(int idCompartment, boolean inModel){ 

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("inModel", inModel);
		map.put("modelCompartment", idCompartment);

		List<ModelReaction> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return res;
		}
		return null;
	}

	@Override
	public List<Integer> getAllModelReactionsIDsByAttributes(String equation, boolean inModel, boolean isGeneric, boolean isSpontaneous, boolean isNonEnzymatic, String source, boolean isCompartimentalized){ 

		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);

		Root<ModelReaction> reaction = c.from(ModelReaction.class); 

		c.select(reaction.get("idreaction")); 

		List<Predicate> filters = new ArrayList<Predicate>();

		Predicate filter2 = cb.equal(reaction.get("modelReactionLabels").get("equation"), equation);
		Predicate filter3 = cb.equal(reaction.get("inModel"), inModel);
		Predicate filter4 = cb.equal(reaction.get("modelReactionLabels").get("isGeneric"), isGeneric);
		Predicate filter5 = cb.equal(reaction.get("modelReactionLabels").get("isSpontaneous"), isSpontaneous);
		Predicate filter6 = cb.equal(reaction.get("modelReactionLabels").get("isNonEnzymatic"), isNonEnzymatic);
		Predicate filter7 = cb.equal(reaction.get("modelReactionLabels").get("source"), source);
		filters.add(filter7);

		Predicate filter8 = cb.isNull(reaction.get("modelCompartment").get("idcompartment"));


		if(equation != null) {
			filters.add(filter2);
		}

		if(inModel) {
			filters.add(filter3);
		}

		if(isGeneric) {
			filters.add(filter4);
		}

		if(isSpontaneous) {
			filters.add(filter5);
		}

		if(isNonEnzymatic) {
			filters.add(filter6);
		}

		if(isCompartimentalized) 
			filters.add(filter8);

		Predicate p = cb.and(filters.toArray(new Predicate[] {}));  
		c.where(p);

		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer> resultList = q.getResultList();

		if (resultList!=null && resultList.size()>0) {

			return resultList;
		}

		return resultList;
	}


	@Override
	public List<Object[]> getAllModelReactionsByTransportersAndIsCompartimentalized(boolean isTransporters){ 


		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);

		Root<ModelReaction> reaction = c.from(ModelReaction.class); 
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class); 

		c.multiselect(reactionLabels.get("name"), reactionLabels.get("equation"), reaction.get("inModel"), 
				reactionLabels.get("isGeneric"), reactionLabels.get("isSpontaneous"), reactionLabels.get("isNonEnzymatic"), reactionLabels.get("source"),
				reaction.get("idreaction"), reaction.get("lowerBound"), reaction.get("upperBound"), reaction.get("notes")); 

		Predicate filter1 = cb.equal(reaction.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));

		Predicate filter2 = cb.isNull(reaction.get("modelCompartment").get("idcompartment"));


		Predicate filter3;
		if (isTransporters) {
			filter3= cb.equal(this.getPath("source", reactionLabels), "TRANSPORTERS"); 
		} 
		else {
			filter3 = cb.notEqual(this.getPath("source", reactionLabels), "TRANSPORTERS"); 
		}

		c.where(filter1, filter2, filter3);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		if (resultList!=null && resultList.size()>0) {
			return resultList;
		}
		return null;
	}



	@Override
	public List<ReactionContainer> getAllModelReactionsByAttributesAndSource(int idCompartment, boolean inModel, String source){ 

		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReaction> c = cb.createQuery(ModelReaction.class);

		Root<ModelReaction> table = c.from(ModelReaction.class); 
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class); 

		c.select(table); 

		Predicate filter1 = cb.equal(table.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter2 = cb.equal(table.get("modelCompartment"), idCompartment);
		Predicate filter3 = cb.equal(table.get("inModel"), inModel);
		Predicate filter4 = cb.equal(reactionLabels.get("source"), source);

		c.where(filter1, filter2, filter3, filter4);
		Query<ModelReaction> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReaction> resultList = q.getResultList();

		List<ReactionContainer> list = new ArrayList<>();

		for(ModelReaction reaction : resultList) {

			ReactionContainer container = new ReactionContainer(reaction.getIdreaction(), reaction.getInModel(), reaction.getLowerBound(), reaction.getUpperBound());
			list.add(container);
		}

		return list;
	}




	@Override
	public void updateInModelByReactionId(Integer reactionId, boolean inModel) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("idreaction", reactionId);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("inModel", inModel);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public void updateModelReactionNotesByReactionId(Integer reactionId, String notes) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("idreaction", reactionId);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("notes", notes);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public void updateReactionReversibility(Long lower,Integer reactionId) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("idreaction", reactionId);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("lowerBound", lower);


		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	//	@Override
	//	public Integer insertModelReaction(boolean inModel, Integer compartmentId, String notes, Float lowerBound, Float upperBound, String boolean_rule) {
	//		ModelReaction model = new ModelReaction();
	//		model.setInModel(inModel);
	//		model.setNotes(notes);
	//		model.setBooleanRule(boolean_rule);
	//		model.setLowerBound(lowerBound);
	//		model.setUpperBound(upperBound);
	//
	////		ModelCompartmentDAOImpl dao = new ModelCompartmentDAOImpl(sessionFactory);
	////		ModelCompartment compartment = dao.getModelCompartmentByCompartmentId(compartmentId);
	//		
	//		model.setModelCompartment(compartment);
	//
	//		return (Integer) this.save(model);
	//	}


	@Override
	public ModelReaction getModelReactionByReactionId(Integer reactionId){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("idreaction", reactionId);

		return this.findUniqueByAttributes(dic);

	}

	@Override
	public Integer getModelReactionLabelByReactionId(Integer reactionId){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("idreaction", reactionId);

		List<ModelReaction> list =  this.findByAttributes(dic);

		if(list != null)
			return list.get(0).getModelReactionLabels().getIdreactionLabel();

		return null;
	}


	@Override
	public boolean getModelReactionInModelByReactionId(Integer reactionId){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("idreaction", reactionId);

		List<ModelReaction> list =  this.findByAttributes(dic);

		if (list != null) {
			return list.get(0).getInModel();
		}
		return false;

	}

	@Override
	public List<ModelReaction> getAllModelReactionsByTransportersType(boolean isTransporters){ 

		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReaction> c = cb.createQuery(ModelReaction.class);

		Root<ModelReaction> table = c.from(ModelReaction.class); 
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class); 

		Predicate filter1 = cb.equal(table.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));

		c.select(table); 

		List<Predicate> filters = new ArrayList<Predicate>();

		filters.add(filter1);

		if (isTransporters) {
			filters.add(cb.equal(this.getPath("source", reactionLabels), "TRANSPORTERS")); 
		} 
		else {
			filters.add(cb.notEqual(this.getPath("source", reactionLabels), "TRANSPORTERS")); 
		}

		cb.isNull(this.getPath("modelCompartment", table));

		Predicate p = cb.and(filters.toArray(new Predicate[] {}));  
		c.where(p);
		Query<ModelReaction> q = sessionFactory.getCurrentSession().createQuery(c);

		return q.getResultList();
	}

	@Override
	public List<Integer> getModelReactionIdByName(String name) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReaction> c = cb.createQuery(ModelReaction.class);

		Root<ModelReaction> table = c.from(ModelReaction.class); 
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class); 

		Predicate filter1 = cb.equal(table.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));

		c.select(table); 

		List<Predicate> filters = new ArrayList<Predicate>();

		filters.add(filter1);

		filters.add(cb.equal(this.getPath("name", reactionLabels), name)); 

		Predicate p = cb.and(filters.toArray(new Predicate[] {}));  
		c.where(p);
		Query<ModelReaction> q = sessionFactory.getCurrentSession().createQuery(c);

		List<ModelReaction> result = q.getResultList();

		List<Integer> res = new ArrayList<>();

		for(ModelReaction reaction : result)
			res.add(reaction.getIdreaction());

		return res;
	}

	@Override
	public Map<String, List<Integer>> getModelReactionIdsRelatedToNames() {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);

		Root<ModelReaction> table = c.from(ModelReaction.class); 
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class); 

		Predicate filter1 = cb.equal(table.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));

		c.multiselect(reactionLabels.get("name"), table.get("idreaction")); 

		c.where(filter1);

		Query<Object[]> q = sessionFactory.getCurrentSession().createQuery(c);

		List<Object[]> result = q.getResultList();

		Map<String, List<Integer>> res = new HashMap<>();

		if(result != null) {

			for(Object[] item : result) {

				String name = item[0].toString();
				Integer id = Integer.valueOf(item[1].toString());

				if(res.containsKey(name))
					res.get(name).add(id);
				else {
					List<Integer> l = new ArrayList<>();
					l.add(id);
					res.put(name, l);

				}
			}
		}

		return res;
	}

	@Override
	public List<ReactionContainer> getModelReactionIdsRelatedToName(String name) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReaction> c = cb.createQuery(ModelReaction.class);

		Root<ModelReaction> modelReaction = c.from(ModelReaction.class); 
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class); 

		Predicate filter1 = cb.equal(modelReaction.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter2 = cb.equal(modelReaction.get("modelReactionLabels").get("name"), name);


		c.select(modelReaction); 

		c.where(cb.and(filter1, filter2));

		Query<ModelReaction> q = sessionFactory.getCurrentSession().createQuery(c);

		List<ModelReaction> result = q.getResultList();

		List<ReactionContainer> res = new ArrayList<>();

		if(result != null) {

			for(ModelReaction reaction : result) {
				
				ModelReactionLabels label = reaction.getModelReactionLabels();
				
				ModelCompartment compartment = reaction.getModelCompartment();
				
				ReactionContainer container = new ReactionContainer(reaction.getIdreaction(), label.getName(), label.getEquation());
				container.setInModel(reaction.getInModel());
				
				if(compartment != null)
					container.setLocalisation(new CompartmentContainer(compartment.getIdcompartment(),
							compartment.getName(), compartment.getAbbreviation()));
				
				container.setSpontaneous(label.getIsSpontaneous());
				container.setNon_enzymatic(label.getIsNonEnzymatic());
				container.setGeneric(label.getIsGeneric());
				container.setLowerBound(reaction.getLowerBound().doubleValue());
				container.setUpperBound(reaction.getUpperBound().doubleValue());
				container.setGeneRule(reaction.getBooleanRule());
				container.setNotes(reaction.getNotes());
				container.setSource(SourceType.valueOf(label.getSource()));
				res.add(container);
			}
		}

		return res;
	}

	@Override
	public boolean deleteModelReactionByReactionId(Integer reactionId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("idreaction", reactionId);

		List<ModelReaction> list =  this.findByAttributes(map);
		this.removeModelReactionList(list);
		return true;
	}

	//	@Override
	//	public List<ModelReaction> getAllModelReactionWithEnzyme(boolean isCompartimentalized){
	//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
	//		CriteriaQuery<ModelReaction> c = cb.createQuery(ModelReaction.class);
	//		Root<ModelReaction> react = c.from(ModelReaction.class);
	//		Root<ModelReactionHasEnzyme> reacEnz = c.from(ModelReactionHasEnzyme.class);
	//		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);
	//		
	//
	//		c.select(react); 
	//		Predicate filter1 = cb.equal(react.get("idreaction"), reacEnz.get("id").get("reactionIdreaction"));
	//		Predicate filter2 = cb.equal(react.get("inModel"), true);
	//
	//		Predicate constraints = cb.and(filter1, filter2);
	//
	//		Predicate filter3 = cb.isNull(react.get("modelCompartment").get("idcompartment"));
	//
	//		if(isCompartimentalized) 
	//			filter3 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));
	//
	//		constraints = cb.and(constraints, filter3);
	//
	//
	//		c.where(constraints);
	//		Query<ModelReaction> q = super.sessionFactory.getCurrentSession().createQuery(c);
	//		List<ModelReaction> list = q.getResultList();
	//
	//		return list;
	//	}

	@Override
	public List<String[]> getModelReactionNameAndECNumber(boolean isCompartimentalized){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String[]> c = cb.createQuery(String[].class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);
		Root<ModelReactionHasModelProtein> reacEnz = c.from(ModelReactionHasModelProtein.class);

		c.multiselect(react.get("name"), reacEnz.get("modelProtein").get("ecnumber"));
		Predicate filter1 = cb.equal(react.get("idreaction"), reacEnz.get("id").get("modelReactionIdreaction"));
		Predicate filter2 = cb.equal(react.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter3 = cb.equal(react.get("inModel"), true);

		Predicate constraints = cb.and(filter1, filter2, filter3);

		Predicate filter4 = cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter4 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		constraints = cb.and(constraints, filter4);


		c.where(constraints);
		Query<String[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<String[]> list = q.getResultList();

		return list;
	}

	//	@Override
	//	public List<String> getModelReactionNameByInModelAndStoichCoefOrderByReactionId(boolean inmodel, float stCoef, boolean isCompartimentalized) {
	//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
	//		CriteriaQuery<String> c = cb.createQuery(String.class);
	//		Root<ModelReaction> react = c.from(ModelReaction.class);
	//		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
	//
	//		c.multiselect(react.get("name"));
	//		Predicate filter1 = cb.equal(react.get("idreaction"), st.get("modelReaction").get("idreaction"));
	//		Predicate filter2 = cb.equal(react.get("inModel"), true);
	//		Predicate filter3 = cb.equal(st.get("stoichiometricCoefficient"), stCoef);
	//		
	//		Predicate constraints = cb.and(filter1, filter2, filter3);
	//		
	//		if(isCompartimentalized) {
	//			Predicate filter4 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));
	//			
	//			constraints = cb.and(constraints, filter4);
	//		}
	//		
	//		Order[] orderList = {cb.asc(react.get("idreaction"))};
	//		c.where(constraints).orderBy(orderList);
	//
	//		Query<String> q = super.sessionFactory.getCurrentSession().createQuery(c);
	//		List<String> list = q.getResultList();
	//		return list;
	//	}


	//	"SELECT reaction.name, reaction.equation, source, inModel, reversible FROM reaction " + //AINDA NAO FIZ
	//	"INNER JOIN reaction_has_enzyme ON reaction_has_enzyme.reaction_idreaction = reaction.idreaction " +
	//	"WHERE reaction_has_enzyme.enzyme_ecnumber = '" + ecnumber+"' " +
	//	"AND reaction_has_enzyme.enzyme_protein_idprotein = " + id + aux+ "" +
	//	" ORDER BY inModel DESC, reversible DESC, name");
	@Override
	public Map<Integer, ReactionContainer> getModelReactionsData(String ecnumber, Integer proteinID, boolean isCompartimentalized){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelReactionHasModelProtein> reacProtein = c.from(ModelReactionHasModelProtein.class);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);

		c.multiselect(react.get("idreaction"), reactionLabels.get("source"), react.get("inModel"), react.get("lowerBound"),
				react.get("upperBound"), reactionLabels.get("name"), reactionLabels.get("equation"));

		Predicate filter1 = cb.equal(react.get("idreaction"), reacProtein.get("id").get("modelReactionIdreaction"));
		//		This line was commented in the SQL query - "ModelAPi.getReactionsData"
		//		Predicate filter2 = cb.equal(reacEnz.get("id").get("modelEnzymeEcnumber"), ecnumber);
		Predicate filter3 = cb.equal(reacProtein.get("id").get("modelProteinIdprotein"), proteinID);
		Predicate filter4 = cb.equal(react.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter5 = cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter5 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		Order[] orderList = {cb.desc(react.get("inModel")), cb.asc(reactionLabels.get("name"))}; 
		c.where(cb.and(filter1, filter3, filter4,filter5)).orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		Map<Integer, ReactionContainer> res = new HashMap<>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				ReactionContainer container =  new ReactionContainer((Integer) item[0], (Boolean) item[2], (Float) item[3], (Float) item[4]);
				container.setSource(SourceType.valueOf((String) item[1]));
				container.setExternalIdentifier((String) item[5]);
				container.setEquation((String) item[6]);

				res.put((Integer) item[0], container);

			}
		}
		return res;
	}


	@Override
	public List<String[]> getModelReactionByInModelAndConditions(boolean isCompartimentalized){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelStoichiometry> stoi = c.from(ModelStoichiometry.class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);

		c.multiselect(stoi.get("id").get("idstoichiometry"), react.get("idreaction"), compound.get("idcompound"), stoi.get("id").get("modelCompartmentIdcompartment"),
				stoi.get("stoichiometricCoefficient"), stoi.get("numberofchains"), compound.get("name"), compound.get("formula"),
				compound.get("externalIdentifier")); 

		Predicate filter1 = cb.equal(stoi.get("id").get("reactionIdreaction"), react.get("idreaction"));
		Predicate filter2 = cb.equal(stoi.get("id").get("compoundIdcompound"), compound.get("idcompound"));
		Predicate filter3 = cb.equal(react.get("inModel"), true);

		Predicate filter4 = cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter4 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		c.where(cb.and(filter1,filter2, filter3, filter4));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[9];
				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				list[2] = String.valueOf(item[2]);
				list[3] = String.valueOf(item[3]);
				list[4] = String.valueOf(item[4]);
				list[5] = (String) item[5];
				list[6] = (String) item[6];
				list[7] = (String) item[7];
				list[8] = (String) item[8];

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;	
	}

	@Override
	public List<Integer> getDistinctModelReactionIdByEcNumber(String ecNumber){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		Root<ModelReactionHasModelProtein> reactionHasEnzyme = c.from(ModelReactionHasModelProtein.class);
		Root<ModelPathwayHasReaction> pathReaction = c.from(ModelPathwayHasReaction.class);
		c.multiselect(reaction.get("idreaction")).distinct(true);
		Predicate filter1 = cb.equal(reactionHasEnzyme.get("id").get("modelReactionIdreaction"),  reaction.get("idreaction"));
		Predicate filter2 = cb.equal(pathReaction.get("id").get("reactionIdreaction"), reaction.get("idreaction")); 
		Predicate filter3 = cb.equal(reactionHasEnzyme.get("modelProtein").get("ecnumber"), ecNumber);

		c.where(filter1, filter2, filter3);

		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer> list = q.getResultList();
		return list;

	}

	@Override
	public List<String[]> getModelReactionStoichiometryData(boolean isCompartimentalizedModel){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		//		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		//		Root<ModelCompound> compound = c.from(ModelCompound.class);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);
		//		Root<ModelCompartment> compartment = c.from(ModelCompartment.class);

		c.multiselect(st.get("idstoichiometry"), 
				st.get("modelReaction").get("idreaction"),
				st.get("modelCompound").get("idcompound"),
				st.get("stoichiometricCoefficient"),
				st.get("modelCompound").get("name"),
				st.get("modelCompound").get("formula"),
				st.get("modelCompound").get("externalIdentifier"),
				reactionLabels.get("name"),
				st.get("modelCompartment").get("name"),
				st.get("modelCompartment").get("idcompartment"),
				st.get("modelCompartment").get("abbreviation"));

		//		c.multiselect(st.get("idstoichiometry"), react.get("idreaction"), compound.get("idcompound"),
		//				st.get("stoichiometricCoefficient"), compound.get("name"), compound.get("formula"),
		//				compound.get("externalIdentifier"), reactionLabels.get("name"), compartment.get("name"),
		//				compartment.get("idcompartment"), compartment.get("abbreviation"));

		//		Predicate filter1 = cb.equal(react.get("idreaction"), st.get("modelReaction").get("idreaction"));
		//		Predicate filter2 = cb.equal(compound.get("idcompound"), st.get("modelCompound").get("idcompound"));
		Predicate filter3 = cb.equal(st.get("modelReaction").get("inModel"), true);
		Predicate filter4 = cb.isNull(st.get("modelReaction").get("modelCompartment").get("idcompartment"));
		if(isCompartimentalizedModel) 
			filter4 = cb.isNotNull(st.get("modelReaction").get("modelCompartment").get("idcompartment"));
		Predicate filter5 = cb.equal(st.get("modelReaction").get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		//		Predicate filter6 = cb.equal(react.get("modelCompartment").get("idcompartment"), compartment.get("idcompartment"));

		Order[] orderList = {cb.desc(st.get("modelReaction").get("inModel")), cb.asc(reactionLabels.get("name"))}; 

		c.where(cb.and(//filter1, filter2,
				filter3, filter4, filter5
				//, filter6
				)).orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[11];
				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				list[2] = String.valueOf(item[2]);
				list[3] = String.valueOf(item[3]);
				list[4] = String.valueOf(item[4]);
				list[5] = String.valueOf(item[5]); 
				list[6] = (String) item[6];
				list[7] = (String) item[7];
				list[8] = (String) item[8];
				list[9] = String.valueOf(item[9]);
				list[10] = (String) item[10];
				parsedList.add(list);

			}

			return parsedList;
		}
		return null;
	}

	@Override
	public List<String[]> getModelReactionsData2(){ //nao coloquei o aux!!!
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelCompound> comp = c.from(ModelCompound.class);

		c.multiselect(react.get("name"), comp.get("externalIdentifier"), react.get("idreaction"));

		Predicate filter1 = cb.equal(react.get("idreaction"), st.get("modelReaction").get("idreaction"));
		Predicate filter2 = cb.equal(comp.get("idcompound"), st.get("modelCompound").get("idcompound"));
		Predicate filter3 = cb.equal(react.get("inModel"), true);

		c.where(cb.and(filter1,filter2, filter3));

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
	public List<String[]> getModelReactions(Integer compart, Integer id, boolean isCompartimentalized){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelReactionLabels> reactLabel = c.from(ModelReactionLabels.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);
		Root<ModelCompartment> compartment = c.from(ModelCompartment.class);

		c.multiselect(react.get("idreaction"), reactLabel.get("name"), reactLabel.get("equation"), 
				reactLabel.get("source"), react.get("inModel"), react.get("lowerBound"), 
				react.get("upperBound")).distinct(true);

		Predicate filter1 = cb.equal(react.get("idreaction"), st.get("modelReaction").get("idreaction"));
		Predicate filter2 = cb.equal(compartment.get("idcompartment"), st.get("modelCompartment").get("idcompartment"));
		Predicate filter3 = cb.equal(compartment.get("idcompartment"), compart);
		Predicate filter4 = cb.equal(st.get("modelCompound").get("idcompound"), id);
		Predicate filter5= cb.equal(react.get("modelReactionLabels").get("idreactionLabel"), reactLabel.get("idreactionLabel"));
		Predicate filter6 = cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter6 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		Order[] orderList = {cb.desc(react.get("inModel")), cb.asc(reactLabel.get("source")), cb.asc(reactLabel.get("name"))}; 
		c.where(cb.and(filter1,filter2, filter3, filter4, filter5, filter6)).orderBy(orderList);


		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();



		ArrayList<String[]> parsedList = new ArrayList<String[]>();

		if( resultList != null && resultList.size() > 0) {

			for(Object[] item: resultList) {
				String[] list = new String[7];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];
				list[4] = String.valueOf(item[4]);
				list[5] = String.valueOf(item[5]);
				list[6] = String.valueOf(item[6]);

				parsedList.add(list);
			}	
		}
		return parsedList;
	}

	@Override
	public Long countModelReactionDistinctReactionsIdsByInModel(boolean isCompartimentalized){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.multiselect(cb.count(react.get("idreaction")));  
		Predicate filter1 = cb.equal(react.get("inModel"), true);

		Predicate filter2= cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter2 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		c.where(cb.and(filter1, filter2));

		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Object result = q.uniqueResult();

		return (Long) result;
	}

	@Override
	public Long countModelReactionDistinctReactionsIdsBySourceAndInModel(boolean isCompartimentalized, boolean inModel, String source){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelReactionLabels> reactLabel = c.from(ModelReactionLabels.class);

		c.multiselect(cb.countDistinct(react.get("idreaction")));

		Predicate filter1= cb.equal(react.get("modelReactionLabels").get("idreactionLabel"), reactLabel.get("idreactionLabel"));

		Predicate filter3= cb.equal(reactLabel.get("source"), source);

		Predicate filter4= cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter4 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		if(inModel) {
			Predicate filter2 = cb.equal(react.get("inModel"), true);
			c.where(cb.and(filter1, filter2, filter3, filter4));
		}
		else
			c.where(cb.and(filter1, filter3, filter4));

		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Object result = q.uniqueResult();

		return (Long) result;
	}

	@Override
	public Long countModelReactionDistinctReactionsIdsByReactionId(Integer id){ //nao tem o aux!!
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelPathwayHasReaction> pathHasReaction = c.from(ModelPathwayHasReaction.class);

		c.multiselect(cb.count(react.get("idreaction")));  
		Predicate filter1 = cb.equal(react.get("idreaction"), pathHasReaction.get("id").get("reactionIdreaction"));

		c.where(filter1);

		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Object result = q.uniqueResult();

		return (Long) result;
	}

	@Override
	public Long countModelReactionDistinctReactionsId(){ //nao tem o aux!!
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelPathwayHasReaction> path = c.from(ModelPathwayHasReaction.class);

		c.multiselect(cb.count(react.get("idreaction")));  
		Predicate filter1 = cb.equal(react.get("idreaction"), path.get("id").get("reactionIdreaction"));

		c.where(filter1);

		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Object result = q.uniqueResult();

		return (Long) result;
	}

	@Override
	public String getModelReactionBooleanRuleByReactionId(Integer id) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("idreation", id);
		ModelReaction reaction = this.findUniqueByAttributes(map);

		if(reaction != null) {
			return reaction.getBooleanRule();
		}
		return null;
	}

	@Override
	public List<Object[]> getModelReactionDataBySource(String source) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelReactionHasModelProtein> hasProt = c.from(ModelReactionHasModelProtein.class);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);

		c.multiselect(react.get("idreaction"), hasProt.get("modelProtein").get("ecnumber"), hasProt.get("id").get("modelProteinIdprotein")).distinct(true);

		Predicate filter1 = cb.notEqual(reactionLabels.get("source"), source);
		Predicate filter2 = cb.isNotNull(hasProt.get("modelProtein").get("ecnumber"));
		Predicate filter3 = cb.isNull(react.get("modelCompartment").get("idcompartment"));
		Predicate filter4 = cb.equal(react.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter5 = cb.equal(hasProt.get("modelReaction").get("idreaction"), reactionLabels.get("idreactionLabel"));


		c.where(cb.and(filter1, filter2, filter3, filter4, filter5));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);

		return q.getResultList();

	}

	@Override
	public List<ModelReaction> getModelReactionsBySourceAndCompartmentalized(String source, boolean transporter){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReaction> c = cb.createQuery(ModelReaction.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);

		c.select(react);

		Predicate filter1 = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		Predicate filter2 = cb.notEqual(reactionLabels.get("source"), source);
		if(transporter)
			filter2 = cb.equal(reactionLabels.get("source"), source);
		Predicate filter3 = cb.equal(react.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));

		c.where(cb.and(filter1, filter2, filter3));

		Query<ModelReaction> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReaction> list = q.getResultList();

		return list;
	}


	@Override
	public List<ReactionContainer> getAllModelReactionByCompartmentalized(boolean isCompartimentalized) {

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReaction> c = cb.createQuery(ModelReaction.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);

		c.select(react);

		Predicate filter = cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		c.where(cb.and(filter));


		Query<ModelReaction> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReaction> resultList = q.getResultList();

		List<ReactionContainer> list = new ArrayList<>();

		for(ModelReaction reaction : resultList) {

			ReactionContainer container = new ReactionContainer(reaction.getIdreaction(), reaction.getInModel(), reaction.getLowerBound(), reaction.getUpperBound());
			list.add(container);
		}

		return list;
	}



	//	@Override
	//	public void deleteModelReactionByOriginalReaction(Integer original) {
	//		Map<String, Serializable> map = new HashMap<String, Serializable>();
	//		map.put("originalReaction", original);
	//
	//		List<ModelReaction> list =  this.findByAttributes(map);
	//		this.removeModelReactionList(list);
	//	}

	@Override
	public List<String[]> getDistinctModelReactionAttributesByPathwayIdAndReactionId(Integer path, Integer reaction) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelReactionHasModelProtein> hasEnz = c.from(ModelReactionHasModelProtein.class);
		Root<ModelPathwayHasReaction> pathReac = c.from(ModelPathwayHasReaction.class);
		Root<ModelPathwayHasModelProtein> pathEnz = c.from(ModelPathwayHasModelProtein.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.multiselect(prot.get("ecnumber"), prot.get("idprotein"), prot.get("name")).distinct(true);

		Predicate filter1 = cb.equal(hasEnz.get("id").get("modelReactionIdreaction"), react.get("idreaction"));
		Predicate filter3 = cb.equal(hasEnz.get("id").get("modelProteinIdprotein"), prot.get("idprotein"));

		Predicate filter4 = cb.equal(react.get("idreaction"), pathReac.get("id").get("reactionIdreaction"));

		Predicate filter7 = cb.equal(prot.get("idprotein"), pathEnz.get("id").get("modelProteinIdprotein"));

		Predicate filter9 = cb.equal(pathReac.get("id").get("pathwayIdpathway"), pathEnz.get("id").get("modelPathwayIdpathway"));
		Predicate filter10 = cb.equal(pathEnz.get("id").get("modelPathwayIdpathway"), path);
		Predicate filter11 = cb.equal(react.get("idreaction"), reaction);

		c.where(cb.and(filter1, filter3, filter4, filter7, filter9, filter10, filter11));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);

		List<Object[]> resultList = q.getResultList();

		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[3];
				list[0] = (String) item[0];
				list[1] = String.valueOf(item[1]);
				list[2] = (String) item[2];

				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}

	@Override
	public List<ReactionContainer> getAllModelReactionByInModel(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);

		c.multiselect(react.get("idreaction"), reactionLabels.get("name"), reactionLabels.get("equation"), react.get("modelCompartment").get("idcompartment"),
				react.get("notes"), react.get("lowerBound"), react.get("upperBound")).distinct(true);

		Predicate filter1 = cb.equal(react.get("inModel"), true);
		Predicate filter2 = cb.equal(react.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));

		c.where(cb.and(filter1, filter2));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<ReactionContainer> list = new ArrayList<>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				ReactionContainer container = new ReactionContainer((Integer) item[0], (String) item[1], (String) item[2]);
				container.setLocalisation((Integer) item[3]);
				container.setNotes((String) item[4]);
				container.setLowerBound((Double) item[5]);
				container.setUpperBound((Double) item[6]);

				list.add(container);	
			}
		}
		return list;
	}


	@Override
	public ReactionContainer getModelReactionAttributesByReactionId(Integer idreaction, boolean isCompartimentalized) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);	
		Join<ModelReaction, ModelPathway> path = reaction.join("modelPathways",JoinType.LEFT);
		Join<ModelReaction, ModelCompartment> comp = reaction.join("modelCompartment", JoinType.LEFT);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);


		c.multiselect(reaction.get("idreaction"), reactionLabels.get("name"), reactionLabels.get("equation"), reaction.get("modelCompartment").get("idcompartment"),
				path.get("name"), reaction.get("inModel"),
				comp.get("name"), reactionLabels.get("isSpontaneous"), reactionLabels.get("isNonEnzymatic"), reactionLabels.get("isGeneric"),
				reaction.get("lowerBound"), reaction.get("upperBound"), reaction.get("booleanRule"), reaction.get("notes"),
				reactionLabels.get("source"));

		Predicate filter1 = cb.equal(comp.get("idcompartment"), reaction.get("modelCompartment").get("idcompartment"));
		Predicate filter2 = cb.equal(path.get("idpathway"), reaction.get("modelPathways").get("idpathway"));
		Predicate filter3 = cb.equal(reaction.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter4 = cb.equal(reaction.get("idreaction"), idreaction);

		Predicate filter5 = cb.isNull(reaction.get("modelCompartment"));

		if(isCompartimentalized) 
			filter5 = cb.isNotNull(reaction.get("modelCompartment"));

		c.where(cb.and(filter1,filter2, filter3, filter4, filter5));


		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		ReactionContainer container = null;

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				container = new ReactionContainer((Integer) item[0], (String) item[1], (String) item[2]);
				container.addPathway((String) item[3]);
				container.setInModel((Boolean) item[4]);
				container.setLocalisation((String) item[5]);
				container.setSpontaneous((Boolean) item[6]);
				container.setNon_enzymatic((Boolean) item[7]);
				container.setGeneric((Boolean) item[8]);
				container.setLowerBound((Double) item[9]);
				container.setUpperBound((Double) item[10]);
				container.setGeneRule((String) item[11]);
				container.setNotes((String) item[12]);
				container.setSource(SourceType.valueOf((String) item[13]));
			}
		}

		return container;
	}

	@Override
	public List<ReactionContainer> getAllModelReactionAttributes(boolean isCompartimentalized) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);	
		Join<ModelReaction, ModelPathway> path = reaction.join("modelPathways",JoinType.LEFT);
		Join<ModelReaction, ModelCompartment> comp = reaction.join("modelCompartment", JoinType.LEFT);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);


		c.multiselect(reaction.get("idreaction"), reactionLabels.get("name"), reactionLabels.get("equation"), path.get("name"), reaction.get("inModel"),
				comp.get("name"), reactionLabels.get("isSpontaneous"), reactionLabels.get("isNonEnzymatic"), reactionLabels.get("isGeneric"),
				reaction.get("lowerBound"), reaction.get("upperBound"), reaction.get("booleanRule"), reaction.get("notes"),
				reactionLabels.get("source"));

		Predicate filter1 = cb.equal(comp.get("idcompartment"), reaction.get("modelCompartment").get("idcompartment"));
		Predicate filter2 = cb.equal(path.get("idpathway"), reaction.get("modelPathways").get("idpathway"));
		Predicate filter3 = cb.equal(reaction.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));

		Predicate filter5 = cb.isNull(reaction.get("modelCompartment"));

		if(isCompartimentalized) 
			filter5 = cb.isNotNull(reaction.get("modelCompartment"));

		c.where(cb.and(filter1,filter2, filter3, filter5));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<ReactionContainer> list = new ArrayList<>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				ReactionContainer container = new ReactionContainer((Integer) item[0], (String) item[1], (String) item[2]);
				container.addPathway((String) item[3]);
				container.setInModel((Boolean) item[4]);
				container.setLocalisation((String) item[5]);
				container.setSpontaneous((Boolean) item[6]);
				container.setNon_enzymatic((Boolean) item[7]);
				container.setGeneric((Boolean) item[8]);
				container.setLowerBound((Double) item[9]);
				container.setUpperBound((Double) item[10]);
				container.setGeneRule((String) item[11]);
				container.setNotes((String) item[12]);
				container.setSource(SourceType.valueOf((String) item[13]));


				list.add(container);
			}
		}

		return list;
	}


	@Override
	public Map<Integer, ReactionContainer> getAllModelReactionAttributesbySource(boolean isCompartimentalized, String source) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);	
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);


		c.multiselect(reaction.get("idreaction"), reactionLabels.get("name"), reactionLabels.get("equation"), reactionLabels.get("source"), reaction.get("inModel"),
				reaction.get("notes"), reactionLabels.get("isSpontaneous"), reactionLabels.get("isNonEnzymatic"), reactionLabels.get("isGeneric"),
				reaction.get("lowerBound"), reaction.get("upperBound"), reaction.get("booleanRule"));

		Predicate filter3 = cb.equal(reaction.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter4 = cb.equal(reactionLabels.get("source"), source);
		Predicate filter5 = cb.isNull(reaction.get("modelCompartment"));

		if(isCompartimentalized) 
			filter5 = cb.isNotNull(reaction.get("modelCompartment"));

		c.where(cb.and(filter3, filter4, filter5));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		Map<Integer, ReactionContainer> map = new HashMap<>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {
				ReactionContainer container = new ReactionContainer((Integer) item[0], (String) item[1], (String) item[2]);
				container.setSource(SourceType.valueOf((String) item[3]));
				container.setInModel((Boolean) item[4]);
				container.setNotes((String) item[5]);
				container.setSpontaneous((Boolean) item[6]);
				container.setNon_enzymatic((Boolean) item[7]);
				container.setGeneric((Boolean) item[8]);
				container.setLowerBound((Double.valueOf(item[9].toString())));
				container.setUpperBound((Double.valueOf(item[10].toString())));
				container.setGeneRule((String) item[11]);


				map.put(container.getReactionID(), container);
			}
		}

		return map;
	}


	@Override
	public Map<Integer, ReactionContainer> getAllModelReactionAttributesByReactionIdWithPathways(boolean isCompartimentalized) {

		Map<Integer, ReactionContainer> res = new HashMap<>();

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		Root<ModelCompartment> compartment = c.from(ModelCompartment.class);
		Join<ModelReaction, ModelPathwayHasReaction> path = reaction.join("modelPathwayHasReactions",JoinType.LEFT);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);

		c.multiselect(reaction.get("idreaction"), 
				reactionLabels.get("name"), 
				reactionLabels.get("equation"),// pathHasReac.get("name"),
				path.get("modelPathway").get("name"),
				reaction.get("inModel"),
				compartment.get("name"),
				reactionLabels.get("isSpontaneous"), 
				reactionLabels.get("isNonEnzymatic"), 
				reactionLabels.get("isGeneric"),
				reaction.get("lowerBound"), 
				reaction.get("upperBound"), 
				reaction.get("booleanRule"), 
				reaction.get("notes"),
				reactionLabels.get("source"), 
				reaction.get("modelCompartment").get("idcompartment"), 
				compartment.get("abbreviation"));

		Predicate filter1 = cb.equal(reaction.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter2 = cb.equal(reaction.get("idreaction"), path.get("id").get("reactionIdreaction"));
		Predicate filter3= cb.isNull(reaction.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter3 = cb.and(cb.isNotNull(reaction.get("modelCompartment").get("idcompartment")), cb.equal(reaction.get("modelCompartment").get("idcompartment"), compartment.get("idcompartment")));

		c.where(cb.and(filter1, filter2, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();


		if(resultList.size() > 0) {

			for(Object[] item: resultList) {

				ReactionContainer container = new ReactionContainer((Integer) item[0], (String) item[1], (String) item[2]);
				container.addPathway(String.valueOf(item[3]));
				container.setInModel((Boolean) item[4]);
				if(item[14] != null) {

					CompartmentContainer compartmentcontain = new CompartmentContainer(Integer.valueOf(item[14].toString()));
					compartmentcontain.setName(String.valueOf (item[5]));
					compartmentcontain.setAbbreviation(String.valueOf (item[15]));
					container.setLocalisation(compartmentcontain);
				}
				container.setSpontaneous((Boolean) item[6]);
				container.setNon_enzymatic((Boolean) item[7]);
				container.setGeneric((Boolean) item[8]);
				container.setLowerBound(Double.valueOf(item[9].toString()));
				container.setUpperBound(Double.valueOf(item[10].toString()));
				if(item[11] != null)
					container.setGeneRule((String.valueOf (item[11])));
				container.setNotes((String.valueOf (item[12])));
				container.setSource(SourceType.valueOf((String.valueOf (item[13]))));

				//				System.out.println(container.getExternalIdentifier()+"\t"+container.getReactionID()+"\t"+container.getPathwayNames());

				res.put((Integer) item[0], container);
			}
		}

		return res;
	}

	@Override
	public Map<Integer, ReactionContainer> getAllModelReactionAttributesByReactionId(boolean isCompartimentalized) {

		Map<Integer, ReactionContainer> res = new HashMap<>();

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		Root<ModelCompartment> compartment = c.from(ModelCompartment.class);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);

		c.multiselect(reaction.get("idreaction"), 
				reactionLabels.get("name"), 
				reactionLabels.get("equation"),
				reaction.get("inModel"),
				compartment.get("name"),
				reactionLabels.get("isSpontaneous"), 
				reactionLabels.get("isNonEnzymatic"), 
				reactionLabels.get("isGeneric"),
				reaction.get("lowerBound"), 
				reaction.get("upperBound"), 
				reaction.get("booleanRule"), 
				reaction.get("notes"),
				reactionLabels.get("source"), 
				reaction.get("modelCompartment").get("idcompartment"), 
				compartment.get("abbreviation"));

		Predicate filter1 = cb.equal(reaction.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter3= cb.isNull(reaction.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) {
			filter3 = cb.and(cb.isNotNull(reaction.get("modelCompartment").get("idcompartment")), cb.equal(reaction.get("modelCompartment").get("idcompartment"), compartment.get("idcompartment")), 
					cb.equal(reaction.get("inModel"), true));
		}
		c.where(cb.and(filter1, filter3));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();


		if(resultList.size() > 0) {

			for(Object[] item: resultList) {

				ReactionContainer container = new ReactionContainer((Integer) item[0], (String) item[1], (String) item[2]);
				container.setInModel((Boolean) item[3]);
				if(item[13] != null) {

					CompartmentContainer compartmentcontain = new CompartmentContainer(Integer.valueOf(item[13].toString()));
					compartmentcontain.setName(String.valueOf (item[4]));
					compartmentcontain.setAbbreviation(String.valueOf (item[14]));
					container.setLocalisation(compartmentcontain);
				}
				container.setSpontaneous((Boolean) item[5]);
				container.setNon_enzymatic((Boolean) item[6]);
				container.setGeneric((Boolean) item[7]);
				container.setLowerBound(Double.valueOf(item[8].toString()));
				container.setUpperBound(Double.valueOf(item[9].toString()));
				if(item[10] != null)
					container.setGeneRule((String.valueOf (item[10])));
				container.setNotes((String.valueOf (item[11])));
				container.setSource(SourceType.valueOf((String.valueOf (item[12]))));

				//				System.out.println(container.getExternalIdentifier()+"\t"+container.getReactionID()+"\t"+container.getPathwayNames());

				res.put((Integer) item[0], container);
			}
		}

		return res;
	}

	//	@Override
	//	public CriteriaQuery<Object[]> reactions_view_noPath_or_noEC() {
	//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
	//		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
	//		Root<ModelReaction> reaction = c.from(ModelReaction.class);	
	//
	//		Join<ModelReaction, ModelPathway> path = reaction.join("modelPathways",JoinType.LEFT);
	//
	//		c.multiselect(reaction.get("idreaction"), reaction.get("name"), reaction.get("equation"), reaction.get("reversible"),
	//				path.get("idpathway"), path.get("name"), reaction.get("inModel"), reaction.get("isGeneric"), reaction.get("source"), 
	//				reaction.get("originalReaction"), reaction.get("modelCompartment.idcompartment"),
	//				reaction.get("notes")).distinct(true);
	//
	//		Predicate filter1 = cb.equal(reaction.get("idreaction"), path.get("modelReactions.idreaction")); //conf
	//		Predicate filter2 = cb.isNull(path.get("idpathway"));
	//		Predicate filter3 = cb.equal(reaction.get("modelPathways.idpathway"), path.get("idpathway"));
	//
	//		Order[] orderList = {cb.asc(path.get("name")), cb.asc(reaction.get("name"))}; 
	//
	//		c.where(cb.and(filter1,filter2, filter3)).orderBy(orderList);
	//
	//		return c;
	//	}

	//	@Override
	//	public List<String> getDataFromReactionsViewNoPathOrNoEc(Set<String> reactionsIDs, Integer enzyme_protein_idprotein,
	//			Integer reaction_has_enzymeidprotein, String source, String ecnumber) throws Exception {
	//
	//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
	//		CriteriaQuery<Object[]> c = this.reactions_view_noPath_or_noEC();
	//		/*
	//		 * 		ResultSet rs= stmt.executeQuery("SELECT DISTINCT idreaction FROM reactions_view_noPath_or_noEC " +
	//				"INNER JOIN reaction_has_enzyme ON reaction_has_enzyme.reaction_idreaction=idreaction " +
	//				"WHERE enzyme_ecnumber = '"+ecnumber+"'"+aux);
	//		 * */
	//
	//		Root<ModelReaction> reaction = c.from(ModelReaction.class);
	//		Join<ModelReaction, ModelEnzyme> enz = reaction.join("modelEnzymes", JoinType.INNER);
	//
	//		c.multiselect(reaction.get("idreaction")).distinct(true);
	//		Predicate filter1 = cb.equal(enz.get("modelEnzymes.ec"), ecnumber);
	//		c.where(filter1);
	//
	//		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
	//		List<Object[]> resultList = q.getResultList();
	//		List<String> res = new ArrayList<String>();
	//
	//		if(resultList.size() > 0) {
	//			for (Object[] x : resultList)
	//				res.add(x.toString());	
	//		}
	//		return res;
	//	}

	//	@Override
	//	public List<String[]> reactions_view(){
	//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
	//		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
	//		Root<ModelReaction> reaction = c.from(ModelReaction.class);	
	//		Root<ModelPathway> path = c.from(ModelPathway.class);	
	//		Root<ModelPathwayHasReaction> pathHasReaction = c.from(ModelPathwayHasReaction.class);
	//
	//		c.multiselect(reaction.get("idreaction"), reaction.get("name"), reaction.get("equation"), reaction.get("reversible"), path.get("idpathway"), path.get("name"), reaction.get("inModel"),
	//				reaction.get("isGeneric"), reaction.get("source"), reaction.get("originalReaction"), reaction.get("modelCompartment").get("idcompartment"),
	//				reaction.get("notes"));
	//
	//		Predicate filter1 = cb.equal(reaction.get("idreaction"), pathHasReaction.get("id").get("reactionIdreaction"));
	//		Predicate filter2 = cb.equal(path.get("idpathway"), pathHasReaction.get("id").get("pathwayIdpathway"));
	//		Order[] orderList = {cb.asc(path.get("name")), cb.asc(reaction.get("name"))};
	//
	//		c.where(cb.and(filter1,filter2)).orderBy(orderList);
	//		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
	//		List<Object[]> resultList = q.getResultList();
	//		if(resultList.size() > 0) {
	//			ArrayList<String[]> parsedList = new ArrayList<String[]>();
	//
	//			for(Object[] item: resultList) {
	//				String[] list = new String[12];
	//				list[0] = String.valueOf(item[0]);
	//				list[1] = (String) item[1];
	//				list[2] = (String) item[2];
	//				list[3] =  String.valueOf(item[3]);
	//				list[4] = String.valueOf(item[4]);
	//				list[5] = (String) item[5];
	//				list[6] = String.valueOf(item[6]);
	//				list[7] = String.valueOf(item[7]);
	//				list[8] = (String) item[8];
	//				list[9] = String.valueOf(item[9]);
	//				list[10] = String.valueOf(item[10]);
	//				list[11] = (String) item[11];
	//				parsedList.add(list);
	//			}
	//			return parsedList;
	//		}
	//		return null;
	//	}

	@Override
	public List<ReactionContainer> getAllModelReactionWithLabelsByCompartmentalized(boolean isCompartimentalized) {

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReaction> c = cb.createQuery(ModelReaction.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		c.select(react);

		Predicate filter = cb.isNull(react.get("modelCompartment").get("idcompartment"));

		if(isCompartimentalized) 
			filter = cb.isNotNull(react.get("modelCompartment").get("idcompartment"));

		c.where(filter);

		Query<ModelReaction> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReaction> resultList = q.getResultList();

		List<ReactionContainer> list = new ArrayList<>();

		if(resultList != null) {

			for(ModelReaction reaction : resultList) {

				ReactionContainer container = new ReactionContainer(reaction.getIdreaction(), reaction.getModelReactionLabels().getName(), reaction.getModelReactionLabels().getEquation());
				container.setLowerBound(reaction.getLowerBound().doubleValue());

				container.setUpperBound(reaction.getUpperBound().doubleValue());
				list.add(container);
			}
		}

		return list;
	}



	@Override
	public List<String> getModelReactionNamesByCompound(String name){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReactionLabels> c = cb.createQuery(ModelReactionLabels.class);
		Root<ModelReaction> react = c.from(ModelReaction.class);
		Root<ModelReactionLabels> reactLabel = c.from(ModelReactionLabels.class);
		Root<ModelCompound> comp = c.from(ModelCompound.class);
		Root<ModelStoichiometry> stoi = c.from(ModelStoichiometry.class);

		c.select(reactLabel);  
		Predicate filter1 = cb.equal(react.get("idreaction"), stoi.get("modelReaction").get("idreaction"));
		Predicate filter2= cb.equal(react.get("modelReactionLabels").get("idreactionLabel"), reactLabel.get("idreactionLabel"));
		Predicate filter3 = cb.equal(comp.get("idcompound"), stoi.get("modelCompound").get("idcompound"));
		Predicate filter4 = cb.equal(comp.get("name"), name);

		c.where(filter1, filter2, filter3, filter4);

		Query<ModelReactionLabels> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReactionLabels> resultList = q.getResultList();
		List<String> res = new ArrayList<>();
		if(resultList!=null && resultList.size() > 0) {
			for (ModelReactionLabels x: resultList) {
				res.add(x.getName());
			}
		}
		return res;	
	}



	@Override
	public List<String[]> getDataFromReactionForBlockedReactionsTool(boolean isCompartimentalized ){ 


		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);
		Root<ModelCompound> comp = c.from(ModelCompound.class);
		Root<ModelStoichiometry> st = c.from(ModelStoichiometry.class);

		c.multiselect(reaction.get("idreaction"), reactionLabels.get("name"), comp.get("externalIdentifier"));

		Predicate filter1 = cb.equal(reaction.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
		Predicate filter2 = cb.equal(st.get("modelReaction").get("idreaction"), reaction.get("idreaction"));
		Predicate filter3 = cb.equal(st.get("modelCompound").get("idcompound"), comp.get("idcompound"));

		Predicate filter4 = cb.isNull(reaction.get("modelCompartment"));

		if(isCompartimentalized) 
			filter4 = cb.isNotNull(reaction.get("modelCompartment"));

		c.where(cb.and(filter1,filter2, filter3, filter4));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> list = new ArrayList<>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {

				String[] list2 = new String[3] ;

				list2[0] = item[0].toString();
				list2[1] = item[1].toString();
				list2[2] = item[2].toString();

				list.add(list2);
			}
		}
		return list;
	}

	@Override
	public List<ProteinContainer> getEnzymesForReaction(int rowID){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		//		Root<ModelProtein> protein = c.from(ModelProtein.class);
		Root<ModelReactionHasModelProtein> reactionhasprot = c.from(ModelReactionHasModelProtein.class);

		//		Join<ModelProtein, ModelReactionHasModelProtein> enz = reactionhasprot.join("modelReactionHasModelProteins",JoinType.INNER);

		c.multiselect(reactionhasprot.get("modelProtein").get("ecnumber"), 
				reactionhasprot.get("modelProtein").get("idprotein"), 
				reactionhasprot.get("modelProtein").get("name"));

		Predicate filter1 = cb.equal(reactionhasprot.get("id").get("modelReactionIdreaction"), rowID);

		c.where(cb.and(filter1));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<ProteinContainer> list = new ArrayList<>();

		if(resultList.size() > 0) {

			for(Object[] item: resultList) {

				ProteinContainer container = new ProteinContainer(item[0].toString());

				container.setIdProtein(Integer.valueOf(item[1].toString()));
				container.setName(item[2].toString());

				list.add(container);
			}

		}
		return list;

	}

	@Override
	public List<ReactionContainer> getDistinctReactionByProteinIdAndCompartimentalized(Integer proteinID, boolean isCompartimentalized){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathwayHasReaction> c = cb.createQuery(ModelPathwayHasReaction.class);

		Root<ModelPathwayHasReaction> pathwayHasReac = c.from(ModelPathwayHasReaction.class);
		Root<ModelReactionHasModelProtein> reactionHasProt = c.from(ModelReactionHasModelProtein.class);	
		Root<ModelPathwayHasModelProtein> pathwayHasProte = c.from(ModelPathwayHasModelProtein.class);

		c.select(pathwayHasReac).distinct(true);

		List<Predicate> filters = new ArrayList<Predicate>();


		if(isCompartimentalized) {

			Predicate filter1 = cb.isNotNull(pathwayHasReac.get("modelReaction").get("modelCompartment").get("idcompartment"));
			filters.add(filter1);
		}

		Predicate filter2 = cb.equal(pathwayHasReac.get("id").get("reactionIdreaction"), reactionHasProt.get("id").get("modelReactionIdreaction"));
		filters.add(filter2);

		Predicate filter3 = cb.equal(reactionHasProt.get("id").get("modelProteinIdprotein"), proteinID);
		filters.add(filter3);

		Predicate filter4 = cb.equal(pathwayHasProte.get("id").get("modelProteinIdprotein"), reactionHasProt.get("id").get("modelProteinIdprotein"));
		filters.add(filter4);


		c.where(cb.and(filters.toArray(new Predicate[] {})));

		Query<ModelPathwayHasReaction> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelPathwayHasReaction> list = q.getResultList();

		List<ReactionContainer> containers = new ArrayList<ReactionContainer>();

		for(ModelPathwayHasReaction item : list) {
			ReactionContainer container = new ReactionContainer(item.getId().getReactionIdreaction());
			containers.add(container);
		}

		return containers;

	}

	@Override
	public List<ReactionContainer> getReactionIdFromProteinIdWithPathwayIdNull(Integer proteinId) { 

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);

		Root<ModelReaction> reaction = c.from(ModelReaction.class);

		Join<ModelReaction, ModelPathwayHasReaction> pathHasReaction = reaction.join("modelPathwayHasReactions", JoinType.LEFT);
		Join<ModelPathwayHasReaction, ModelPathway> pathway = pathHasReaction.join("modelPathway", JoinType.LEFT);
		Join<ModelReaction, ModelReactionHasModelProtein> reactionhasprotein = reaction.join("modelReactionHasModelProteins", JoinType.INNER);

		Predicate filter1 = cb.isNull(pathway.get("idpathway"));
		Predicate filter = cb.equal(reactionhasprotein.get("modelProtein").get("idprotein"), proteinId);
		c.where(cb.and(filter1, filter));

		c.select(reaction.get("idreaction"));


		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer> list = q.getResultList();

		List<ReactionContainer> containers = new ArrayList<ReactionContainer>();

		for(Integer item : list) {

			ReactionContainer container = new ReactionContainer(item);
			containers.add(container);
		}

		return containers;

	}

	@Override
	public List<String[]> getReactionIdAndEcNumberAndProteinId(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReactionHasModelProtein> reactHasProtei = c.from(ModelReactionHasModelProtein.class);

		c.multiselect(reactHasProtei.get("modelReaction").get("idreaction"),
				reactHasProtei.get("modelProtein").get("idprotein"), 
				reactHasProtei.get("modelProtein").get("ecnumber"));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> res = new ArrayList<>();

		if(resultList!=null && resultList.size() > 0) {

			for (Object[] x: resultList) {

				String[] list2 = new String[3] ;

				list2[0] = x[0].toString(); // reactionId
				list2[1] = x[1].toString(); // proteinId
				
				if(x[2] != null)
					list2[2] = x[2].toString(); // ecNumber

				res.add(list2);
			}
		}
		return res;	
	}

	@Override
	public List<Integer[]> getReactionIdAndPathwayId(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelPathwayHasReaction> pathwayHasReact = c.from(ModelPathwayHasReaction.class);

		c.multiselect(pathwayHasReact.get("modelReaction").get("idreaction"), 
				pathwayHasReact.get("modelPathway").get("idpathway"));


		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<Integer[]> res = new ArrayList<>();

		if(resultList!=null && resultList.size() > 0) {

			for (Object[] x: resultList) {

				Integer[] list2 = new Integer[2] ;

				list2[0] = (Integer) x[0]; // reactionId
				list2[1] = (Integer) x[1]; // pathwayId

				res.add(list2);
			}
		}
		return res;	
	}


	@Override
	public List<Object[]> GetCompoundIDAndStoichiometricCoefficientAndCompartmentIDByReactionID(Integer reactionID) { 

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);

		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		Join<ModelReaction, ModelStoichiometry> stoich = reaction.join("modelStoichiometries",JoinType.INNER);
		Join<ModelStoichiometry, ModelCompound> compound = stoich.join("modelCompound",JoinType.INNER);

		c.multiselect(stoich.get("stoichiometricCoefficient"), compound.get("idcompound"), stoich.get("modelCompartment").get("name"));

		Predicate filter = cb.equal(reaction.get("idreaction"), reactionID);

		c.where(cb.and(filter));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		List<Object[]> result = new ArrayList<Object[]>();

		for(Object[] intSet : list) {

			Object[] tempList = new Object[3];
			tempList[0] = (Integer) Math.round((Float) intSet[0]);
			tempList[1] = (Integer) intSet[1];
			tempList[2] = (String) intSet[2];
			result.add(tempList);
		}

		return result;

	}

	@Override
	public List<String[]> getReacIdEcNumProtIdWhereSourceEqualTransporters(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelReactionHasModelProtein> reactHasProtei = c.from(ModelReactionHasModelProtein.class);

		c.multiselect(reactHasProtei.get("modelReaction").get("idreaction"), 
				reactHasProtei.get("modelProtein").get("ecnumber"),
				reactHasProtei.get("modelProtein").get("idprotein"));

		Predicate filter = cb.equal(reactHasProtei.get("modelReaction").get("modelReactionLabels").get("source"), "TRANSPORTERS");
		Predicate filter1 = cb.isNotNull(reactHasProtei.get("modelProtein").get("ecnumber"));
		Predicate filter2= cb.isNull(reactHasProtei.get("modelReaction").get("modelCompartment").get("idcompartment"));

		c.where(filter,filter1,filter2);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> res = new ArrayList<String[]>();

		if(resultList!=null && resultList.size() > 0) {

			for (Object[] x: resultList) {

				String[] list2 = new String[3] ;

				list2[0] = x[0].toString(); // reactionId
				list2[1] = x[1].toString(); // ecNumber
				list2[2] = x[2].toString(); // proteinId

				res.add(list2);
			}
		}
		return res;	
	}

	@Override
	public List<Integer> getDistinctReactionIdWhereSourceTransporters(Boolean isTransporter) { 
		
		boolean compartmentalized = true;

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReaction> c = cb.createQuery(ModelReaction.class);

		Root<ModelReaction> reaction = c.from(ModelReaction.class);


		c.select(reaction).distinct(true);

		Predicate filter = cb.notEqual(reaction.get("modelReactionLabels").get("source"), SourceType.TRANSPORTERS.toString());

		if(isTransporter) 
			filter = cb.equal(reaction.get("modelReactionLabels").get("source"), SourceType.TRANSPORTERS.toString());

		Predicate filter1;
		
		if(compartmentalized)
			filter1 = cb.isNotNull(reaction.get("modelCompartment").get("idcompartment"));
		else
			filter1 = cb.isNull(reaction.get("modelCompartment").get("idcompartment"));
		
		c.where(filter,filter1);

		Query<ModelReaction> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReaction> resultList = q.getResultList();

		List<Integer> listIds = new ArrayList<Integer>();

		if(resultList!=null && resultList.size() > 0) {

			for(ModelReaction item : resultList) {

				listIds.add(item.getIdreaction());

				return listIds;

			}
		}
		return listIds;
	}

	@Override
	public void removeNotOriginalReactions(Boolean isTransporter) { 

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelReaction> c = cb.createQuery(ModelReaction.class);

		Root<ModelReaction> reaction = c.from(ModelReaction.class);

		c.select(reaction);

		Predicate filter1 = cb.isNotNull(reaction.get("modelCompartment").get("idcompartment"));

		if(isTransporter != null) {

			Predicate filter2;

			if(isTransporter)
				filter2 = cb.equal(reaction.get("modelReactionLabels").get("source"), SourceType.TRANSPORTERS.toString());
			else
				filter2 = cb.notEqual(reaction.get("modelReactionLabels").get("source"), SourceType.TRANSPORTERS.toString());

			c.where(cb.and(filter1, filter2));
		}
		else {
			c.where(filter1);
		}

		Query<ModelReaction> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelReaction> resultList = q.getResultList();

		if(resultList!=null) {

			for(ModelReaction item : resultList) {

				this.removeModelReaction(item);
			}
		}
	}

	@Override
	public List<String[]> getReactionGenes(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelSubunit> subunit = c.from(ModelSubunit.class);

		Join<ModelSubunit, ModelProtein> protein = subunit.join("modelProtein",JoinType.INNER);
		Join<ModelProtein, ModelReactionHasModelProtein> reactionHasProtein = protein.join("modelReactionHasModelProteins",JoinType.INNER);

		c.multiselect(reactionHasProtein.get("modelReaction").get("idreaction"),
				subunit.get("modelGene").get("name"), subunit.get("modelGene").get("locusTag"),
				subunit.get("modelProtein").get("ecnumber"), subunit.get("modelGene").get("query"));

		c.orderBy(cb.asc(reactionHasProtein.get("modelReaction").get("idreaction")));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> res = new ArrayList<>();

		if(resultList!=null && resultList.size() > 0) {

			for (Object[] x: resultList) {

				String[] list2 = new String[4] ;

				list2[0] =  x[0].toString(); // reactionId
				if (x[1] == null)
					list2[1] = "";
				else
					list2[1] =  x[1].toString(); // geneName
				if (x[2]==null)
					list2[2] =  x[4].toString(); // sequenceId
				else
					list2[2] = x[2].toString();// locusTag
				list2[3] =  x[3].toString(); // ecNumber

				res.add(list2);
			}
		}
		return res;	
	}

	@Override
	public Set<Integer> getModelDrains() { 

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);

		Root<ModelStoichiometry> stoichiometry = c.from(ModelStoichiometry.class);

		c.multiselect(stoichiometry.get("modelReaction").get("idreaction"), 
				cb.count(stoichiometry.get("modelCompound").get("idcompound")),
				cb.count(stoichiometry.get("modelCompartment").get("idcompartment")));

		c.groupBy(stoichiometry.get("modelReaction").get("idreaction"));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		Set<Integer> ret = new HashSet<>();

		for(Object[] item : resultList) {

			if(Integer.valueOf(item[1].toString()) == 1 && Integer.valueOf(item[2].toString())==1) {

				ret.add(Integer.valueOf(item[0].toString()));
			}

		}
		return ret;
	}

	@Override
	public List<String[]> getDataForGPRAssignment(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		
		Root<ModelModule> module = c.from(ModelModule.class);
		
		Join<ModelModule, ModelModuleHasModelProtein> moduleHasProt = module.join("modelModuleHasModelProteins",JoinType.INNER);
		Join<ModelProtein, ModelModuleHasModelProtein> protein = moduleHasProt.join("modelProtein",JoinType.INNER);
		Join<ModelModule, ModelModuleHasOrthology> modHasOrth = module.join("modelModuleHasOrthologies",JoinType.INNER);
		Join<ModelModuleHasOrthology, ModelOrthology> orth = modHasOrth.join("modelOrthology",JoinType.INNER);
		Join<ModelGeneHasOrthology, ModelOrthology> geneHasOrth = orth.join("modelGeneHasOrthologies",JoinType.INNER);
		Join<ModelGeneHasOrthology, ModelGene> gene = geneHasOrth.join("modelGene",JoinType.INNER);

		c.multiselect(module.get("reaction"), protein.get("ecnumber"),
						module.get("definition"), geneHasOrth.get("id").get("modelGeneIdgene"), orth.get("entryId"),
							geneHasOrth.get("similarity"), geneHasOrth.get("id").get("modelOrthologyId")).distinct(true);		

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<String[]> res = new ArrayList<>();

		if(resultList!=null && resultList.size() > 0) {

			for (Object[] x: resultList) {

				String[] list = new String[7] ;
				
				list[0] =  x[0].toString();
				list[1] =  x[1].toString();
				list[2] =  x[2].toString();
				list[3] =  x[3].toString();
				list[4] =  x[4].toString();
				list[5] =  x[5].toString();
				list[6] =  x[6].toString();

				res.add(list);
			}
		}
		return res;	
	}

	@Override
	public Integer countTotalOfReactions(boolean isCompartmentalized) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);

		c.select(cb.count(reaction.get("idreaction")));
		
		Predicate filter = cb.isNull(reaction.get("modelCompartment").get("idcompartment"));

		if(isCompartmentalized) 
			filter = cb.isNotNull(reaction.get("modelCompartment").get("idcompartment"));

		c.where(filter);
		
		Query<Long> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Long count = q.getSingleResult();

		if(count != null)
			return count.intValue();
		
		return null;	
	}
	
	@Override
	public void replaceCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaUpdate<ModelReaction> update = cb.createCriteriaUpdate(ModelReaction.class);
		Root<ModelReaction> reaction = update.from(ModelReaction.class);

		update.set(reaction.get("modelCompartment").get("idcompartment"), compartmentIdToKeep);
		update.where(cb.equal(reaction.get("modelCompartment").get("idcompartment"), compartmentIdToReplace));
		
		super.sessionFactory.getCurrentSession().createQuery(update).executeUpdate();
	}
}
