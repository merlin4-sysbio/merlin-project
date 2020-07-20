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

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelCompartmentDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStoichiometry;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;


public class ModelCompartmentDAOImpl extends GenericDaoImpl<ModelCompartment> implements IModelCompartmentDAO {

	public ModelCompartmentDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelCompartment.class);

	}

	@Override
	public void addModelCompartment(ModelCompartment modelCompartment) {
		super.save(modelCompartment);

	}

	@Override
	public void addModelCompartmentList(List<ModelCompartment> modelCompartmentList) {
		for (ModelCompartment modelCompartment: modelCompartmentList) {
			this.addModelCompartment(modelCompartment);
		}
	}

	@Override
	public ModelCompartment getModelCompartment(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelCompartment(ModelCompartment modelCompartment) {
		super.delete(modelCompartment);

	}

	@Override
	public void removeModelCompartmentList(List<ModelCompartment> modelCompartmentList) {
		for (ModelCompartment modelCompartment: modelCompartmentList) {
			this.removeModelCompartment(modelCompartment);
		}
	}

	@Override
	public void updateModelCompartmentList(List<ModelCompartment> modelCompartmentList) {
		for (ModelCompartment modelCompartment: modelCompartmentList) {
			this.update(modelCompartment);
		}
	}

	@Override
	public void updateModelCompartment(ModelCompartment modelCompartment) {
		super.update(modelCompartment);
	}


	@Override
	public ModelCompartment getCompartmentByCompartmentName(String name) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("name", name);
		List<ModelCompartment> list =  this.findByAttributes(dic);

		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public ModelCompartment getCompartmentByCompartmentAbbreviation(String abbreviation) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("abbreviation", abbreviation);
		List<ModelCompartment> list =  this.findByAttributes(dic);

		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public ModelCompartment getModelCompartmentByCompartmentId(Integer compartmentId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("idcompartment", compartmentId);
		List<ModelCompartment> list =  this.findByAttributes(dic);
		ModelCompartment res = null;
		if(list.size() > 0) {
			res = list.get(0);
		}
		return res;
	}

	@Override
	public Set<String> getModelCompartmentNames(){
		Set<String> comp = new TreeSet<String>();
		List<ModelCompartment> list = this.findAll();
		for (ModelCompartment x: list){
			comp.add(x.getName());
		}
		return comp;
	}

	@Override
	public Integer insertNameAndAbbreviation(String name, String abb) {
		ModelCompartment modelCompartment = new ModelCompartment();
		modelCompartment.setName(name);
		modelCompartment.setAbbreviation(abb);
		return (Integer) this.save(modelCompartment);
	}



	@Override
	public Map<Integer, String> getModelCompartmentIdAndAbbreviation() {
		List<ModelCompartment> list =  this.findAll();
		Map<Integer, String> dic = null;

		if(list.size() > 0) {
			dic = new HashMap<Integer, String>();
			for (ModelCompartment x: list){

				dic.put(x.getIdcompartment(), x.getAbbreviation());
			}
		}

		return dic;
	}

	@Override
	public Map<Integer, String> getModelCompartmentIdAndName() {
		List<ModelCompartment> list =  this.findAll();
		Map<Integer, String> dic = null;

		if(list.size() > 0) {
			dic = new HashMap<Integer, String>();
			for (ModelCompartment x: list){

				dic.put(x.getIdcompartment(), x.getName());
			}
		}

		return dic;
	}


	@Override
	public List<CompartmentContainer> getModelCompartmentIdAndNameAndAbbreviation() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelCompartment> comp = c.from(ModelCompartment.class);

		c.multiselect(comp.get("idcompartment"), comp.get("name"), comp.get("abbreviation"));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<CompartmentContainer> containers = new ArrayList<>();

		if(resultList.size() > 0) {
			
			for(Object[] item: resultList) {
				
				if(item[2] != null && !item[1].toString().isEmpty()) {
					
					CompartmentContainer container = new CompartmentContainer(Integer.valueOf(item[0].toString()), (String) item[1], 
							(String) item[2].toString().toLowerCase());

					containers.add(container);
				}

			}
		}
		return containers;
	}

	@Override
	public Integer getCompartmentIdByCompartmentNameAndAbbreviation(String name, String abb) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("name", name);
		dic.put("abbreviation", abb);
		List<ModelCompartment> list =  this.findByAttributes(dic);
		Integer res = null;
		if(list.size() > 0) {
			ModelCompartment model = list.get(0);
			res =  model.getIdcompartment();
		}
		return res;
	}

	@Override
	public List<ModelCompartment> getAllModelCompartmentByAbbreviationName(String compartments){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("name", compartments);
		List<ModelCompartment> list =  this.findByAttributes(dic);

		return list;
	}

	@Override
	public List<ModelCompartment> getAllModelCompartment() {
		return super.findAll();
	}


	@Override
	public List<Object[]> getReactantsOrProductsInCompartment(Boolean isCompartimentalized, Boolean Reactants, Boolean Products){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);

		Root<ModelStoichiometry> stoichiometry = c.from(ModelStoichiometry.class);
		Join<ModelStoichiometry, ModelCompartment> compartment = stoichiometry.join("modelCompartment");
		Join<ModelStoichiometry, ModelReaction> reaction = stoichiometry.join("modelReaction");

		c.multiselect(compartment.get("name"), cb.countDistinct(stoichiometry.get("modelReaction").get("idreaction")));
		List<Predicate> filters = new ArrayList<>();			
		filters.add(cb.equal(stoichiometry.get("modelCompartment").get("idcompartment"), compartment.get("idcompartment")));
		filters.add(cb.equal(stoichiometry.get("modelReaction").get("idreaction"), reaction.get("idreaction")));
		if(isCompartimentalized) 
			filters.add(cb.isNotNull(reaction.get("modelCompartment").get("idcompartment")));
		else
			filters.add(cb.isNull(reaction.get("modelCompartment").get("idcompartment")));
		if(Reactants)
			filters.add(cb.lessThan(stoichiometry.get("stoichiometricCoefficient"), 0));
		if(Products)
			filters.add(cb.greaterThan(stoichiometry.get("stoichiometricCoefficient"), 0));
		c.where(cb.and(filters.toArray(new Predicate[] {})));
		c.groupBy(compartment.get("name"));		
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> queryres = q.getResultList();
		return  queryres;
	}


	@Override
	public ArrayList<String[]> getCompartmentDataByName(String name) {
		
		ArrayList<String[]> result = new ArrayList<>();
		
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("name", name);
		List<ModelCompartment> list =  this.findByAttributes(dic);
		
		if(list.size() > 0) {
			
			for(ModelCompartment compartment : list) {
				
				String[] subList = new String[3];
				
				subList[0] = compartment.getIdcompartment()+"";
				subList[1] = compartment.getName();
				subList[2] = compartment.getAbbreviation();
				
				result.add(subList);
			}
			return result;
		}
		return null;
	}

	@Override
	public Map<String, Set<Integer>> getCompartIdAndEcNumbAndProtId(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);

		Root<ModelGene> gene = c.from(ModelGene.class);
		
		Join<ModelGene, ModelGeneHasCompartment> geneHasCompart = gene.join("modelGeneHasCompartments",JoinType.INNER);
		Join<ModelGene, ModelSubunit> subunit = gene.join("modelSubunits",JoinType.INNER);

		c.multiselect(geneHasCompart.get("modelCompartment").get("idcompartment"),
						subunit.get("modelProtein").get("ecnumber"),
							subunit.get("modelProtein").get("idprotein"));

		c.orderBy(cb.asc((subunit.get("modelProtein").get("ecnumber"))));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		Map<String, Set<Integer>> transportProteinsCompartments = new HashMap<>();
		Set<Integer> compartments = new HashSet<Integer>();

		if(resultList!=null && resultList.size() > 0) {

			for (Object[] item : resultList) {

				String key = item[1].toString().concat("_").concat(item[2].toString());

				if(transportProteinsCompartments.containsKey(key))
					compartments = transportProteinsCompartments.get(key);	
				
				transportProteinsCompartments.put(key, compartments);

			}
		}

		return transportProteinsCompartments;
	}
	
}
