package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.compartments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.compartments.ICompartmentsAnnotationReportsHasCompartmentsDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.CompartmentsAnnotationCompartments;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.CompartmentsAnnotationReportsHasCompartments;


public class CompartmentsAnnotationReportsHasCompartmentsDAOImpl extends GenericDaoImpl<CompartmentsAnnotationReportsHasCompartments> implements ICompartmentsAnnotationReportsHasCompartmentsDAO{

	public CompartmentsAnnotationReportsHasCompartmentsDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, CompartmentsAnnotationReportsHasCompartments.class);
		
	}

	@Override
	public void addCompartmentsAnnotationReportsHasCompartment(CompartmentsAnnotationReportsHasCompartments compartmentsAnnotationReportsHasCompartments) {
		super.save(compartmentsAnnotationReportsHasCompartments);
		
	}

	@Override
	public void addCompartmentsAnnotationReportsHasCompartments(List<CompartmentsAnnotationReportsHasCompartments> compartmentsAnnotationReportsHasCompartments) {
		for (CompartmentsAnnotationReportsHasCompartments compartmentsAnnotationReportsHasCompartment: compartmentsAnnotationReportsHasCompartments) {
			this.addCompartmentsAnnotationReportsHasCompartment(compartmentsAnnotationReportsHasCompartment);
		}
	}

	@Override
	public List<CompartmentsAnnotationReportsHasCompartments> getAllCompartmentsAnnotationReportsHasCompartments() {
		return super.findAll();
	}

	@Override
	public CompartmentsAnnotationReportsHasCompartments getCompartmentsAnnotationReportsHasCompartment(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeCompartmentsAnnotationReportsHasCompartment(CompartmentsAnnotationReportsHasCompartments compartmentsAnnotationReportsHasCompartment) {
		super.delete(compartmentsAnnotationReportsHasCompartment);
		
		
	}

	@Override
	public void removeCompartmentsAnnotationReportsHasCompartments(List<CompartmentsAnnotationReportsHasCompartments> compartmentsAnnotationReportsHasCompartments) {
		for (CompartmentsAnnotationReportsHasCompartments compartmentsAnnotationReportsHasCompartment: compartmentsAnnotationReportsHasCompartments) {
			this.removeCompartmentsAnnotationReportsHasCompartment(compartmentsAnnotationReportsHasCompartment);
		}
		
	}

	@Override
	public void updateCompartmentsAnnotationReportsHasCompartments(List<CompartmentsAnnotationReportsHasCompartments> compartmentsAnnotationReportsHasCompartments) {
		for (CompartmentsAnnotationReportsHasCompartments compartmentsAnnotationReportsHasCompartment: compartmentsAnnotationReportsHasCompartments) {
			this.update(compartmentsAnnotationReportsHasCompartment);
		}
		
	}

	@Override
	public void updateCompartmentsAnnotationReportsHasCompartment(CompartmentsAnnotationReportsHasCompartments compartmentsAnnotationReportsHasCompartment) {
		super.update(compartmentsAnnotationReportsHasCompartment);
	}

	@Override
	public Long getDistinctCompartmentsAnnotationReportsHasCompartmentsIds(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		Root<CompartmentsAnnotationReportsHasCompartments> comp = c.from(CompartmentsAnnotationReportsHasCompartments.class);
		
		c.multiselect(cb.countDistinct(comp.get("id").get("compartmentsAnnotationCompartmentsId")));
		
	    Query<Long> q = super.sessionFactory.getCurrentSession().createQuery(c);
		return q.getSingleResult();
		
	}

	@Override
	public Map<String, String> getDistinctCompartmentsAnnotationReportsHasCompartmentsNameAndAbbreviation(){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<CompartmentsAnnotationReportsHasCompartments> rep = c.from(CompartmentsAnnotationReportsHasCompartments.class);
		Root<CompartmentsAnnotationCompartments> comp = c.from(CompartmentsAnnotationCompartments.class);

	    c.multiselect(comp.get("name"), comp.get("abbreviation")).distinct(true);
	    Predicate filter1 = cb.equal(comp.get("idcompartment"), rep.get("id").get("compartmentsAnnotationCompartmentsId"));
	    c.where(cb.and(filter1));
	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		
		Map<String, String> dics = null;
		if(list.size() > 0) {
			dics = new HashMap<String, String>();
			for (Object[] x: list){
			
			dics.put((String) x[0], (String) x[1]);
			}
		}
		return dics;
	}
	
	@Override
	public List<String[]> getCompartmentsAnnotationReportsHasCompartmentsNameAndScore(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<CompartmentsAnnotationReportsHasCompartments> has = c.from(CompartmentsAnnotationReportsHasCompartments.class);
		Root<CompartmentsAnnotationCompartments> comp = c.from(CompartmentsAnnotationCompartments.class);
//		Root<CompartmentsAnnotationReports> reports = c.from(CompartmentsAnnotationReports.class);

	    c.multiselect(comp.get("name"), has.get("score"));
	    
	    Predicate filter1 = cb.equal(has.get("id").get("compartmentsAnnotationCompartmentsId"), comp.get("id"));
//	    Predicate filter2 = cb.equal(has.get("id").get("modelGeneIdgene"), reports.get("id"));
	    Predicate filter2 = cb.equal(has.get("id").get("modelGeneIdgene"), id);
	    c.where(cb.and(filter1, filter2)).orderBy(cb.desc(has.get("score")));
	    
	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		
		List<String[]> parsedList = new ArrayList<>();
		
		if(list.size() > 0) {
			
			for(Object[] item: list) {
				String[] result = new String[2];
				result[0] = (String) item[0];
				result[1] = String.valueOf(item[1]);
				
				parsedList.add(result);	
			}
		}
		return parsedList;
	}

	@Override
	public Integer countCompartmentsAnnotationReports() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		Root<CompartmentsAnnotationReportsHasCompartments> has = c.from(CompartmentsAnnotationReportsHasCompartments.class);
		
		c.select(cb.countDistinct(has.get("id").get("modelGeneIdgene")));
		
		Query<Long> q = super.sessionFactory.getCurrentSession().createQuery(c);
		return q.getSingleResult().intValue();
	}

	@Override
	public List<CompartmentContainer> getBestCompartmenForGene() {
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<CompartmentsAnnotationReportsHasCompartments> repHasComp = c.from(CompartmentsAnnotationReportsHasCompartments.class);

		c.multiselect(repHasComp.get("id").get("compartmentsAnnotationCompartmentsId"), repHasComp.get("id").get("modelGeneIdgene"), repHasComp.get("modelGene").get("locusTag"),
				repHasComp.get("score"), repHasComp.get("compartmentsAnnotationCompartments").get("abbreviation"), repHasComp.get("compartmentsAnnotationCompartments").get("name"),
				repHasComp.get("modelGene").get("query"));
		
		Order[] orderList = {cb.asc(repHasComp.get("id").get("modelGeneIdgene")), cb.desc(repHasComp.get("score"))}; 
		
		c.orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		List<CompartmentContainer> compartments = new ArrayList<>(); 

		if(resultList.size() > 0) {
			for(Object[] item: resultList) {
				
				CompartmentContainer container = new CompartmentContainer(Integer.valueOf(item[0].toString()));
				
				container.setReportID(Integer.valueOf(item[1].toString()));
				if(item[2] == null)
					container.setLocusTag(item[6].toString());
				else
					container.setLocusTag(item[2].toString());
				container.setScore(Double.valueOf(item[3].toString()));
				container.setAbbreviation(item[4].toString());
				container.setName(item[5].toString());
				
				compartments.add(container);
			}
		}
		return compartments;

	}
	
	
}
