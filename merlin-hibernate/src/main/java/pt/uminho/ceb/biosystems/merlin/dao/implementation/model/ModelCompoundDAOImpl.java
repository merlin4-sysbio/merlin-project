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
import javax.persistence.criteria.Subquery;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.auxiliary.ModelCompoundType;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelCompoundDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStoichiometry;


public class ModelCompoundDAOImpl extends GenericDaoImpl<ModelCompound> implements IModelCompoundDAO {

	public ModelCompoundDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelCompound.class);
		
	}

	@Override
	public void addModelCompound(ModelCompound modelCompound) {
		super.save(modelCompound);
		
	}

	@Override
	public void addModelCompoundList(List<ModelCompound> modelCompoundList) {
		for (ModelCompound modelCompound: modelCompoundList) {
			this.addModelCompound(modelCompound);
		}	
	}

	@Override
	public List<ModelCompound> getAllModelCompound() {
		return super.findAll();
	}

	@Override
	public ModelCompound getModelCompound(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelCompound(ModelCompound modelCompound) {
		super.delete(modelCompound);
		
	}

	@Override
	public void removeModelCompoundList(List<ModelCompound> modelCompoundList) {
		for (ModelCompound modelCompound: modelCompoundList) {
			this.removeModelCompound(modelCompound);
		}	
	}

	@Override
	public void updateModelCompoundList(List<ModelCompound> modelCompoundList) {
		for (ModelCompound modelCompound: modelCompoundList) {
			this.update(modelCompound);
		}	
	}

	@Override
	public void updateModelCompound(ModelCompound modelCompound) {
		super.update(modelCompound);
		
	}
	
	@Override
	public Map<Integer, String> getModelCompoundIdAndMolecularWeightByKeggId(String keggId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("externalIdentifier", keggId);
	
		List<ModelCompound> list =  this.findByAttributes(map);
		Map<Integer, String> dic = null;
		
		if(list.size() > 0) {
			dic = new HashMap<Integer, String>();
			for (ModelCompound x: list){
				dic.put(x.getIdcompound(), x.getMolecularWeight());
			}
		}
		return dic;
	}
	
	@Override
	public List<ModelCompound> getAllModelCompoundByName(String name) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("name", name);
		List<ModelCompound> list =  this.findByAttributes(dic);

		return list;
	}
	
	@Override
	public List<String> getAllModelCompoundEntryTypeByCompoundId(Integer id) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("idcompound", id);
		List<ModelCompound> list =  this.findByAttributes(dic);
		
		List<String> result = new ArrayList<String>();
		if (list != null) {
			for (ModelCompound x : list) {
				result.add(x.getEntryType());
				}
		}
		return result;
	}
	
	@Override
	public Integer getModelCompoundIdByName(String name) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("name", name);
		List<ModelCompound> list =  this.findByAttributes(dic);
		if (list!=null && list.size()>0) {
			return list.get(0).getIdcompound();
		}
		return null;
	}
	
	@Override
	public Integer getModelCompoundIdByExternalIdentifier(String keggid) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("externalIdentifier", keggid);
		List<ModelCompound> list =  this.findByAttributes(dic);
		if (list!=null && list.size()>0) {
			return list.get(0).getIdcompound();
		}
		return null;
	}
	
	@Override
	public Integer insertModelCompound(String name, String keggId, String entryType, String molecularWeight, 
			boolean hasBiologicalRoles){
		ModelCompound modelCompound = new ModelCompound();
		modelCompound.setName(name);
		modelCompound.setExternalIdentifier(keggId);
		modelCompound.setEntryType(entryType);
		modelCompound.setMolecularWeight(molecularWeight);
		modelCompound.setHasBiologicalRoles(hasBiologicalRoles);
		return (Integer) this.save(modelCompound);	
	}
	
	@Override
	public List<String[]> getMetabolitesWithBothProperties(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> type){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);

		Join<ModelCompound, ModelStoichiometry> stoich = compound.join("modelStoichiometries",JoinType.LEFT);
		Join<ModelStoichiometry, ModelReaction> reaction = stoich.join("modelReaction");
		Join<ModelStoichiometry, ModelCompartment> compartment = stoich.join("modelCompartment");
		
		c.multiselect(compound.get("name"), compound.get("formula"), 
				cb.function("COUNT", Integer.class, cb.function("DISTINCT SIGN", Integer.class, stoich.get("stoichiometricCoefficient"))), //COMO APLICO O SIGN??! 
				compound.get("idcompound"), compound.get("externalIdentifier"), 
				cb.countDistinct(reaction.get("idreaction")),
				compartment.get("name"), stoich.get("modelCompartment").get("idcompartment")); 
		
		List<Predicate> filters = new ArrayList<>(); 
		List<Predicate> filterss =new ArrayList<>();
		for(ModelCompoundType t : type)
			filterss.add(cb.equal(compound.get("entryType"),  t.name()));
		
		if(!filterss.isEmpty())
			filters.add(cb.or(filterss.toArray(new Predicate[] {})));
		
		if(inModel)
			filters.add(cb.equal(reaction.get("inModel"), inModel));
		
		filters.add(cb.equal(compartment.get("idcompartment"), stoich.get("modelCompartment").get("idcompartment")));
		filters.add(cb.equal(reaction.get("idreaction"), stoich.get("modelReaction").get("idreaction")));
		
		
		if(isCompartimentalized) 
			filters.add(cb.isNotNull(reaction.get("modelCompartment").get("idcompartment")));
		else
			filters.add(cb.isNull(reaction.get("modelCompartment").get("idcompartment")));
		
		Order[] orderList = {cb.asc(compound.get("name")), cb.asc(compound.get("externalIdentifier"))};
		c.where(cb.and(filters.toArray(new Predicate[] {}))).orderBy(orderList);
	    c.groupBy(compound.get("name"), compound.get("externalIdentifier"), stoich.get("modelCompartment").get("idcompartment"));
	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				String[] list = new String[8];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				list[3] = String.valueOf(item[3]);
				list[4] = (String) item[4];
				list[5] = String.valueOf(item[5]);
				list[6] = (String) item[6];
				list[7] = String.valueOf(item[7]);
			
				parsedList.add(list);
				
			}
			
			return parsedList;
		}
		return new ArrayList<String[]>();
	}

	@Override
	public List<String[]> getMetabolitesProperties(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> types){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);

		Join<ModelCompound, ModelStoichiometry> stoich = compound.join("modelStoichiometries",JoinType.LEFT);
		Join<ModelStoichiometry, ModelReaction> reaction = stoich.join("modelReaction");
		Join<ModelStoichiometry, ModelCompartment> compart = stoich.join("modelCompartment");
		
		c.multiselect(compound.get("name"), compound.get("formula"), 
				cb.function("SIGN", Integer.class, stoich.get("stoichiometricCoefficient")), 
				compound.get("idcompound"), compound.get("externalIdentifier"), 
				cb.countDistinct(reaction.get("idreaction")),
				compart.get("name"), stoich.get("modelCompartment").get("idcompartment")); 
		
		List<Predicate> filters = new ArrayList<>(); 
		List<Predicate> filterss =new ArrayList<>();
		for(ModelCompoundType t : types)
			filterss.add(cb.equal(compound.get("entryType"),  t.name()));
		if(!filterss.isEmpty())
			filters.add(cb.or(filterss.toArray(new Predicate[] {})));
		
		filters.add(cb.equal(compart.get("idcompartment"), stoich.get("modelCompartment").get("idcompartment")));
		filters.add(cb.equal(reaction.get("idreaction"), stoich.get("modelReaction").get("idreaction")));

		if(isCompartimentalized) 
			filters.add(cb.isNotNull(reaction.get("modelCompartment").get("idcompartment")));
		else
			filters.add(cb.isNull(reaction.get("modelCompartment").get("idcompartment")));
		
		
		if(inModel)
			filters.add(cb.equal(reaction.get("inModel"), inModel));
		
		Order[] orderList = {cb.asc(compound.get("name")), cb.asc(compound.get("externalIdentifier"))};
		c.where(cb.and(filters.toArray(new Predicate[] {}))).orderBy(orderList);
	    c.groupBy(compound.get("name"), compound.get("externalIdentifier"), stoich.get("modelCompartment").get("idcompartment"),cb.function("SIGN", Integer.class, stoich.get("stoichiometricCoefficient")));
	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				String[] list = new String[8];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				list[3] = String.valueOf(item[3]);
				list[4] = (String) item[4];
				list[5] = String.valueOf(item[5]);
				list[6] = (String) item[6];
				list[7] = String.valueOf(item[7]);
			
				parsedList.add(list);
			}
			return parsedList;
		}
		return new ArrayList<String[]>();
	}
	

	@Override
	public List<ModelCompound> getAllModelCompoundData2(ModelCompoundType type){ //nao tem aux!!
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelCompound> c = cb.createQuery(ModelCompound.class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);
		Root<ModelStoichiometry> stoi = c.from(ModelStoichiometry.class);
		
		c.select(compound);
		Predicate filter1 = cb.not(compound.get("idcompound").in(stoi.get("modelCompound").get("idcompound")));
		if(type != null) {
			if(type.equals(ModelCompoundType.ALL)) {
				cb.and(
						cb.or(cb.equal(compound.get("entryType"), ModelCompoundType.GLYCAN.toString()), 
								cb.equal(compound.get("entryType"), ModelCompoundType.COMPOUND.toString()),
								cb.equal(compound.get("entryType"), ModelCompoundType.DRUGS.toString())));
			}else {
				cb.and(cb.equal(compound.get("entryType"), type.toString()));
			}
		}
			
		c.where(filter1);
		
		Query<ModelCompound> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelCompound> resultList = q.getResultList();
		
		return resultList;
	}
	
	@Override
	public List<ModelCompound> getAllModelCompoundData(List<ModelCompoundType> typeList){ //nao tem aux!!
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelCompound> c = cb.createQuery(ModelCompound.class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);
		
		Subquery<Integer> sub = c.subquery(Integer.class);
		Root<ModelStoichiometry> subrootc = sub.from(ModelStoichiometry.class);
		sub.select(subrootc.get("modelCompound").get("idcompound"));
		
		c.select(compound);
		
		List<Predicate> filters = new ArrayList<>(); 
		List<Predicate> filterss =new ArrayList<>();
		for(ModelCompoundType t : typeList)
			filterss.add(cb.equal(compound.get("entryType"),  t.name()));
		if(!filterss.isEmpty())
			filters.add(cb.or(filterss.toArray(new Predicate[] {})));
		
		filters.add(cb.not(compound.get("idcompound").in(sub)));
			
		c.where(cb.and(filters.toArray(new Predicate[] {})));
		
		Query<ModelCompound> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelCompound> resultList = q.getResultList();
		
		return resultList;
	}
	
	@Override
	public Boolean checkIfIsFilled() {
		return super.checkIfIsFilled();
	}
	
	@Override
	public List<ModelCompound> getAllModelCompoundDataOrderByKeggId(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelCompound> c = cb.createQuery(ModelCompound.class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);
		Root<ModelStoichiometry> stoi = c.from(ModelStoichiometry.class);
	
		c.select(compound);
		Predicate filter1 = cb.not(compound.get("idcompound").in(cb.in(stoi.get("modelCompound").get("idcompound")))); //falta o distinct
		Predicate filter2 = cb.equal(compound.get("hasBiologicalRoles"), true);
		
		c.where(filter1, filter2);
		c.orderBy(cb.asc(compound.get("externalIdentifier")));
		
		Query<ModelCompound> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelCompound> resultList = q.getResultList();
		
		return resultList;
	}

	@Override
	public Long countCompoundsByName(String name){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);

		c.multiselect(cb.count(compound.get("name"))); 
		
		Predicate filter1 = cb.equal(compound.get("name"), name);
		c.where(cb.and(filter1));
	    
		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Object result = q.uniqueResult();
		
		return (Long) result;
	}
	
	@Override
	public List<String[]> getAllModelCompoundAttributesByName(String name){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);

		c.multiselect(compound.get("name"), compound.get("entryType"), compound.get("formula"), compound.get("molecularWeight"),
				compound.get("charge")); 
		
		Predicate filter1 = cb.equal(compound.get("name"), name);
		
		c.where(cb.and(filter1));
	   	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				String[] list = new String[5];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = (String) item[3];
				list[4] = String.valueOf(item[4]);
				
				parsedList.add(list);
			}
			return parsedList;
		}
		return new ArrayList<String[]>();
	}
	
	@Override
	public List<String[]> getAllModelCompoundAttributesByName2(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);

		c.multiselect(compound.get("name"), compound.get("formula"), compound.get("idcompound"),
				compound.get("externalIdentifier")); 
				
		
		Order[] orderList = {cb.asc(cb.selectCase().when(compound.get("name").isNull(), 1).otherwise(0)), cb.asc(compound.get("name"))};
		
		c.orderBy(orderList);
	   	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();
			
			for(Object[] item: resultList) {
				
				String[] list = new String[4];
				list[0] = (String) item[0];
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
	public List<Integer[]> getAllModelCompoundIdWithCompartmentIdCountedReactions(Boolean isCompartimentalized, Boolean inModel, List<ModelCompoundType> type, Boolean withTransporters) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer[]> c = cb.createQuery(Integer[].class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);
		Root<ModelStoichiometry> stoichiometry = c.from(ModelStoichiometry.class);
		Join<ModelStoichiometry, ModelCompartment> compartment = stoichiometry.join("modelCompartment");
		Join<ModelStoichiometry, ModelReaction> reaction = stoichiometry.join("modelReaction");
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);
		
		c.multiselect(compound.get("idcompound"),compartment.get("idcompartment"), cb.countDistinct(reaction.get("idreaction")));
		
		List<Predicate> filters = new ArrayList<>();
		
		
		
		List<Predicate> filterss = new ArrayList<>(); 
		for(ModelCompoundType t : type)
			filterss.add(cb.equal(compound.get("entryType"), t.name()));
		
		if(!filterss.isEmpty())
			filters.add(cb.or(filterss.toArray(new Predicate[] {})));
		
		if(withTransporters)
			filters.add(cb.like(reactionLabels.get("name"), "T%"));
		else
			filters.add(cb.notLike(reactionLabels.get("name"), "T%"));
		
		if(isCompartimentalized) 
			filters.add(cb.isNotNull(reaction.get("modelCompartment").get("idcompartment")));
		else
			filters.add(cb.isNull(reaction.get("modelCompartment").get("idcompartment")));
		
		filters.add(cb.equal(compound.get("idcompound"), stoichiometry.get("modelCompound").get("idcompound")));
		filters.add(cb.equal(reaction.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel")));
		
		if(inModel)
			filters.add(cb.equal(reaction.get("inModel"), inModel));
		
		c.where(cb.and(filters.toArray(new Predicate[] {})));
		
		c.groupBy(compound.get("name"), compound.get("idcompound"), compound.get("externalIdentifier"), compartment.get("idcompartment"));
		c.orderBy(cb.asc(compound.get("name")), cb.asc(compound.get("externalIdentifier")) );
		
		Query<Integer[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer[]> queryres = q.getResultList();
//		List<List<Integer>> result = new ArrayList<>();
//		for(Integer[] res : queryres)
//			result.add(Arrays.asList(res));
		return  queryres;
	}
	
	@Override
	public void updateModelCompoundAttributes(String name, String entryType, String formula, String molecularW, Short charge, String externalIdentifier) {
		
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("externalIdentifier", externalIdentifier);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("name", name);
		updateAttributes.put("entryType", entryType);
		updateAttributes.put("formula", formula);
		updateAttributes.put("molecularWeight", molecularW);
		updateAttributes.put("charge", charge);
		updateAttributes.put("name", name);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
		
	}
	
	@Override
	public List<String> getAllCompoundsInModel() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<String> c = cb.createQuery(String.class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);
		Root<ModelStoichiometry> stoichiometry = c.from(ModelStoichiometry.class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		
		c.multiselect(compound.get("externalIdentifier")).distinct(true);
		
		List<Predicate> filters = new ArrayList<>();
		
		filters.add(cb.equal(compound.get("idcompound"), stoichiometry.get("modelCompound").get("idcompound")));
		filters.add(cb.equal(stoichiometry.get("modelReaction").get("idreaction"), reaction.get("idreaction")));
		
		filters.add(cb.equal(reaction.get("inModel"), true));
		
		c.where(cb.and(filters.toArray(new Predicate[] {})));
		
		Query<String> q = super.sessionFactory.getCurrentSession().createQuery(c);
		return q.getResultList();
	}
	
	@Override
	public Map<String, Integer> getExternalIdentifierAndIdCompound(){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelCompound> c = cb.createQuery(ModelCompound.class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);
		c.select(compound);
		
		Predicate filter1 = cb.isNotNull(compound.get("externalIdentifier"));
		c.where(cb.and(filter1));
		Query<ModelCompound> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelCompound> list = q.getResultList();
		
		Map<String, Integer> result = new HashMap<>();
		
		if(list.size() > 0) {
			for(ModelCompound compoundItem: list) {
				result.put(compoundItem.getExternalIdentifier(), compoundItem.getIdcompound());
			}
		}
		return result;
	}
	
	

}
