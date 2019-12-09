package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelProteinCompositionDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProteinComposition;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;


public class ModelProteinCompositionDAOImpl extends GenericDaoImpl<ModelProteinComposition> implements IModelProteinCompositionDAO{

	public ModelProteinCompositionDAOImpl(SessionFactory sessionFactory) {super(sessionFactory, ModelProteinComposition.class);
		
	}

	@Override
	public void addModelProteinComposition(ModelProteinComposition modelProteinComposition) {
		super.save(modelProteinComposition);
		
	}

	@Override
	public void addModelProteinCompositionList(List<ModelProteinComposition> modelProteinCompositionList) {
		for (ModelProteinComposition modelProteinComposition: modelProteinCompositionList) {
			this.addModelProteinComposition(modelProteinComposition);
		}
		
	}

	@Override
	public List<ModelProteinComposition> getAllModelProteinComposition() {
		return super.findAll();
	}

	@Override
	public ModelProteinComposition getModelProteinComposition(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelProteinComposition(ModelProteinComposition modelProteinComposition) {
		super.delete(modelProteinComposition);
		
	}

	@Override
	public void removeModelProteinCompositionList(List<ModelProteinComposition> modelProteinCompositionList) {
		for (ModelProteinComposition modelProteinComposition: modelProteinCompositionList) {
			this.removeModelProteinComposition(modelProteinComposition);
		}
		
	}

	@Override
	public void updateModelProteinCompositionList(List<ModelProteinComposition> modelProteinCompositionList) {
		for (ModelProteinComposition modelProteinComposition: modelProteinCompositionList) {
			this.update(modelProteinComposition);
		}
		
	}

	@Override
	public void updateModelProteinComposition(ModelProteinComposition modelProteinComposition) {
		super.update(modelProteinComposition);
		
	}
	
	@Override
	public Long getModelCompositionDistinctProteinId(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		Root<ModelProteinComposition> composit = c.from(ModelProteinComposition.class);

	    c.multiselect(cb.countDistinct(composit.get("id").get("modelProteinIdprotein"))); 
	    
	    Query<Long> q = super.sessionFactory.getCurrentSession().createQuery(c);
		
		return q.uniqueResult();
		
	}
	
	@Override 
	public List<String[]> getProteinComposition() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelSubunit> c = cb.createQuery(ModelSubunit.class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);
		Root<ModelProteinComposition> composition = c.from(ModelProteinComposition.class);

		c.select(sub);

		Predicate filter1 = cb.equal(sub.get("id").get("modelProteinIdprotein"), composition.get("id").get("subunit"));
		c.where(filter1);

		Query<ModelSubunit> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelSubunit> resultList = q.getResultList();
		
		ArrayList<String[]> parsedList = new ArrayList<>();
		
		if(resultList != null) {

			for(ModelSubunit subunit: resultList) {
				String[] list = new String[3];
				list[0] = subunit.getModelGene().getIdgene()+"";
				list[1] = subunit.getModelGene().getName();
				list[2] = subunit.getId().getModelProteinIdprotein()+"";

				parsedList.add(list);
			}
		}
		return parsedList;
	}
	
}
