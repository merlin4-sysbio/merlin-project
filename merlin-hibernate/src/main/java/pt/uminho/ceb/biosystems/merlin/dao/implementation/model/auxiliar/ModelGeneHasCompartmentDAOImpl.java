package pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneHasCompartmentDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartmentId;

public class ModelGeneHasCompartmentDAOImpl extends GenericDaoImpl<ModelGeneHasCompartment> implements IModelGeneHasCompartmentDAO {

	public ModelGeneHasCompartmentDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelGeneHasCompartment.class);
		
	}

	@Override
	public void addModelGeneHasCompartment(ModelGeneHasCompartment modelGeneHasCompartment) {
		super.save(modelGeneHasCompartment);
		
	}

	@Override
	public void addModelGeneHasCompartmentList(List<ModelGeneHasCompartment> modelGeneHasCompartmentList) {
		for (ModelGeneHasCompartment modelGeneHasCompartment: modelGeneHasCompartmentList) {
			this.addModelGeneHasCompartment(modelGeneHasCompartment);
		}
		
	}

	@Override
	public List<ModelGeneHasCompartment> getAllModelGeneHasCompartment() {
		return super.findAll();
	}

	@Override
	public ModelGeneHasCompartment getModelGeneHasCompartment(Integer id) {
		return super.findById(id);
	}
	
	@Override
	public ModelGeneHasCompartment getModelGeneHasCompartment(ModelGeneHasCompartmentId id) {
		return super.findById(id);
	}

	@Override
	public void removeModelGeneHasCompartment(ModelGeneHasCompartment modelGeneHasCompartment) {
		super.delete(modelGeneHasCompartment);
		
	}
	
	@Override
	public void removeModelGeneHasCompartmentList(List<ModelGeneHasCompartment> modelGeneHasCompartmentList) {
		for (ModelGeneHasCompartment modelGeneHasCompartment: modelGeneHasCompartmentList) {
			this.removeModelGeneHasCompartment(modelGeneHasCompartment);
		}
		
	}

	@Override
	public void updateModelGeneHasCompartmentList(List<ModelGeneHasCompartment> modelGeneHasCompartmentList) {
		for (ModelGeneHasCompartment modelGeneHasCompartment: modelGeneHasCompartmentList) {
			this.update(modelGeneHasCompartment);
		}
		
	}

	@Override
	public void updateModelGeneHasCompartment(ModelGeneHasCompartment modelGeneHasCompartment) {
		super.update(modelGeneHasCompartment);
	}

	@Override
	public Integer getIdByGeneIdAndCompartmentId(Integer geneId, Integer compartmentId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.geneIdgene", geneId);
		dic.put("id.compartmentIdcompartment", compartmentId);
		List<ModelGeneHasCompartment> list =  this.findByAttributes(dic);
		Integer res = null;
		if (list.size()>0) {
			res = list.get(0).getId().getModelGeneIdgene();
		}
		return res;		
	}
	
	
	public void insertModelGeneHasCompartment(Integer geneId, Integer compartmentId, boolean primaryLocation, String score) {
		ModelGeneHasCompartment modelGeneHasCompartment = new ModelGeneHasCompartment();
		ModelGeneHasCompartmentId id = new ModelGeneHasCompartmentId();
		id.setModelGeneIdgene(geneId);
		id.setModelCompartmentIdcompartment(compartmentId);
		modelGeneHasCompartment.setId(id);
		modelGeneHasCompartment.setPrimaryLocation(primaryLocation);
		modelGeneHasCompartment.setScore(score);
		this.save(modelGeneHasCompartment);
	}
	
	
	public List<Integer> getIdByGeneIdAndPrimaryLocation(Integer geneId, boolean primlocation) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.geneIdgene", geneId);
		dic.put("primaryLocation", primlocation);
		List<ModelGeneHasCompartment> list =  this.findByAttributes(dic);
		List<Integer> res = null;
		if (list.size()>0) {
			res = new ArrayList<Integer>();
			for (ModelGeneHasCompartment model : list) {
			res.add(model.getId().getModelGeneIdgene());
			}
		}
		return res;
	}
	
	
	public List<ModelGeneHasCompartment> getAllModelGeneHasCompartmentByGeneId(Integer geneId){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelGeneIdgene", geneId);
		List<ModelGeneHasCompartment> list =  this.findByAttributes(dic);
		
		return list;
	}
	
	public Long getModelGeneHasCompartmentDistinctGeneId(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object> c = cb.createQuery(Object.class);
		Root<ModelGeneHasCompartment> gene = c.from(ModelGeneHasCompartment.class);
		
		c.multiselect(cb.count(gene.get("id").get("modelGeneIdgene"))).distinct(true);
	
		Query<Object> q = super.sessionFactory.getCurrentSession().createQuery(c);
		Object result = q.uniqueResult();
		
		return (Long) result;
	}

	@Override
	public void replaceCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaUpdate<ModelGeneHasCompartment> update = cb.createCriteriaUpdate(ModelGeneHasCompartment.class);
		Root<ModelGeneHasCompartment> geneHasComp = update.from(ModelGeneHasCompartment.class);

		update.set(geneHasComp.get("id").get("modelCompartmentIdcompartment"), compartmentIdToKeep);
		update.where(cb.equal(geneHasComp.get("id").get("modelCompartmentIdcompartment"), compartmentIdToReplace));
		
		super.sessionFactory.getCurrentSession().createQuery(update).executeUpdate();
	}
	
	@Override
	public List<ModelGeneHasCompartmentId> getEqualGeneCompartments(Integer compartmentIdToKeep, Integer compartmentIdToReplace) {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelGeneHasCompartmentId> qr = cb.createQuery(ModelGeneHasCompartmentId.class);
		
		Root<ModelGeneHasCompartment> geneHasComp1 = qr.from(ModelGeneHasCompartment.class);
		Root<ModelGeneHasCompartment> geneHasComp2 = qr.from(ModelGeneHasCompartment.class);
		Predicate idsReplace = cb.equal(geneHasComp1.get("id").get("modelCompartmentIdcompartment"), compartmentIdToReplace);
		Predicate idsKeep = cb.equal(geneHasComp2.get("id").get("modelCompartmentIdcompartment"),compartmentIdToKeep);
		Predicate idGene = cb.equal(geneHasComp1.get("id").get("modelGeneIdgene"), geneHasComp2.get("id").get("modelGeneIdgene"));

		qr.select(geneHasComp1.get("id"));
		qr.where(cb.and(cb.and(idsReplace,idsKeep), idGene));
		
		
		Query<ModelGeneHasCompartmentId> q = super.sessionFactory.getCurrentSession().createQuery(qr);
		
		return q.getResultList(); 
		}
		
	public void removeAllFromModelGeneHasCompartment() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaDelete<ModelGeneHasCompartment> delete = cb.createCriteriaDelete(ModelGeneHasCompartment.class);
		delete.from(ModelGeneHasCompartment.class);
		
		super.sessionFactory.getCurrentSession().createQuery(delete).executeUpdate();
	}

}
