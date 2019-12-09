package pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneHasOrthologyDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasOrthologyId;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelOrthology;

public class ModelGeneHasOrthologyDAOImpl extends GenericDaoImpl<ModelGeneHasOrthology> implements IModelGeneHasOrthologyDAO {

	public ModelGeneHasOrthologyDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelGeneHasOrthology.class);
		
	}

	public void addModelGeneHasOrthology(ModelGeneHasOrthology modelGeneHasOrthology) {
		super.save(modelGeneHasOrthology);
		
	}

	public void addModelGeneHasOrthologyList(List<ModelGeneHasOrthology> modelGeneHasOrthologyList) {
		for (ModelGeneHasOrthology modelGeneHasOrthology: modelGeneHasOrthologyList) {
			this.addModelGeneHasOrthology(modelGeneHasOrthology);
		}
		
	}

	public List<ModelGeneHasOrthology> getAllModelGeneHasOrthology() {
		return super.findAll();
	}

	public ModelGeneHasOrthology getModelGeneHasOrthology(Integer id) {
		return super.findById(id);
	}

	public void removeModelGeneHasOrthology(ModelGeneHasOrthology modelGeneHasOrthology) {
		super.delete(modelGeneHasOrthology);
		
	}

	public void removeModelGeneHasOrthologyList(List<ModelGeneHasOrthology> modelGeneHasOrthologyList) {
		for (ModelGeneHasOrthology modelGeneHasOrthology: modelGeneHasOrthologyList) {
			this.removeModelGeneHasOrthology(modelGeneHasOrthology);
		}
		
	}

	public void updateModelGeneHasOrthologyList(List<ModelGeneHasOrthology> modelGeneHasOrthologyList) {
		for (ModelGeneHasOrthology modelGeneHasOrthology: modelGeneHasOrthologyList) {
			this.update(modelGeneHasOrthology);
		}
		
	}

	public void updateModelGeneHasOrthology(ModelGeneHasOrthology modelGeneHasOrthology) {
		super.update(modelGeneHasOrthology);
		
	}

	public List<ModelGeneHasOrthology> getAllModelGeneHasOrthologyByGeneIdAndOrthologyId(Integer geneId, Integer orthId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
	
		dic.put("id.modelGeneIdgene", geneId);
		dic.put("id.modelOrthologyId", orthId);
		
		List<ModelGeneHasOrthology> list =  this.findByAttributes(dic);

		return list;
	}
	
	@Override
	public ModelGeneHasOrthologyId insertModelGeneHasOrthologyGeneIdAndOrthologyIdAndSimilarity(int gene_id, int orth_id, Float similarity){
		ModelGeneHasOrthology gene_has_orthology = new ModelGeneHasOrthology();
		ModelGeneHasOrthologyId id = new ModelGeneHasOrthologyId();
		id.setModelGeneIdgene(gene_id);
		id.setModelOrthologyId(orth_id);
		gene_has_orthology.setSimilarity(similarity);
		gene_has_orthology.setId(id);
	
		return (ModelGeneHasOrthologyId) this.save(gene_has_orthology);			
	}
	
	@Override
	public List<String[]> getModelGeneHasOrthologyAttributesByGeneId(Integer geneId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelGeneHasOrthology> has = c.from(ModelGeneHasOrthology.class);
		Root<ModelOrthology> orth = c.from(ModelOrthology.class);

	    c.multiselect(orth.get("entryId"), orth.get("locusId"), has.get("similarity")); 
	    Predicate filter1 = cb.equal(has.get("id").get("modelOrthologyId"), orth.get("id"));
	    Predicate filter2 = cb.equal(has.get("id").get("modelGeneIdgene"), geneId);
	    
	    c.where(cb.and(filter1, filter2));
	   
		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		
		List<String[]> parsedList = new ArrayList<>();
		
		
		if(resultList.size() > 0) {
			
			
			for(Object[] item: resultList) {
				String[] list = new String[3];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = String.valueOf(item[2]);
				parsedList.add(list);
			}
		}
		return parsedList;
	}

//	public List<ModelGeneHasOrthology> getAllModelGeneHasOrthologyByGeneIdAndOrthologyId(int geneID, int orthId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
}
