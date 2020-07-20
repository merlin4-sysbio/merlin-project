package pt.uminho.ceb.biosystems.merlin.dao.implementation.regulatory;

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

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory.IRegulatoryEventDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryEvent;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryPromoter;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryRiFunction;

public class RegulatoryEventDAOImpl extends GenericDaoImpl<RegulatoryEvent> implements IRegulatoryEventDAO{

	public RegulatoryEventDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, RegulatoryEvent.class);

	}

	@Override 
	public void addRegulatoryEvent(RegulatoryEvent RegulatoryEvent) {
		super.save(RegulatoryEvent);

	}

	@Override 
	public void addRegulatoryEventList(List<RegulatoryEvent> RegulatoryEventList) {
		for (RegulatoryEvent RegulatoryEvent: RegulatoryEventList) {
			this.addRegulatoryEvent(RegulatoryEvent);
		}

	}

	@Override 
	public List<RegulatoryEvent> getAllModelGene() {
		return super.findAll();
	}

	@Override 
	public RegulatoryEvent getRegulatoryEvent(Integer id) {
		return super.findById(id);
	}

	@Override 
	public void removeRegulatoryEvent(RegulatoryEvent RegulatoryEvent) {
		super.delete(RegulatoryEvent);

	}

	@Override 
	public void removeRegulatoryEventList(List<RegulatoryEvent> RegulatoryEventList) {
		for (RegulatoryEvent RegulatoryEvent: RegulatoryEventList) {
			this.removeRegulatoryEvent(RegulatoryEvent);
		}

	}

	@Override 
	public void updateRegulatoryEventList(List<RegulatoryEvent> RegulatoryEventList) {
		for (RegulatoryEvent RegulatoryEvent: RegulatoryEventList) {
			this.update(RegulatoryEvent);
		}

	}

	@Override 
	public void updateRegulatoryEvent(RegulatoryEvent RegulatoryEvent) {
		super.update(RegulatoryEvent);

	}


	@Override 
	public List<Object[]> getRegulatoryEventProteinIdAndGeneIdAndGeneName(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<RegulatoryEvent> reg = c.from(RegulatoryEvent.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);
		Root<ModelGene> gene = c.from(ModelGene.class);

		c.multiselect(reg.get("id").get("proteinIdprotein"), gene.get("idgene"), gene.get("name"));

		Predicate filter1 = cb.equal(reg.get("id").get("proteinIdprotein"), prot.get("idprotein"));
		Predicate filter2 = cb.equal(prot.get("idprotein"), sub.get("id").get("modelEnzymeModelProteinIdprotein"));
		Predicate filter3 = cb.equal(gene.get("idgene"), sub.get("id").get("modelGeneIdgene"));

		Order[] orderList = {cb.asc(prot.get("idprotein"))}; 

		c.where(cb.and(filter1,filter2, filter3)).orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		return list;
	}


	@Override 
	public List<String[]> getDistinctRegulatoryEventGeneIdAndGeneNameByProteinId(Integer id){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<RegulatoryEvent> reg = c.from(RegulatoryEvent.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);
		Root<ModelSubunit> sub = c.from(ModelSubunit.class);
		Root<ModelGene> gene = c.from(ModelGene.class);

		c.multiselect(gene.get("idgene"), gene.get("name")).distinct(true);

		Predicate filter1 = cb.equal(reg.get("id").get("proteinIdprotein"), prot.get("idprotein"));
		Predicate filter2 = cb.equal(prot.get("idprotein"), sub.get("id").get("modelEnzymeModelProteinIdprotein"));
		Predicate filter3 = cb.equal(gene.get("idgene"), sub.get("id").get("modelGeneIdgene"));
		Predicate filter4 = cb.equal(prot.get("idprotein"), id);

		Order[] orderList = {cb.asc(prot.get("idprotein"))}; 

		c.where(cb.and(filter1,filter2, filter3, filter4)).orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[2];
				list[0] = String.valueOf(item[0]);
				list[1] = (String) item[1];
				parsedList.add(list);
			}
			return parsedList;
		}
		return null;
	}


	@Override 
	public Map<String,String> getDataFromRegulatoryEvent(){

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<RegulatoryEvent> event = c.from(RegulatoryEvent.class);

		c.multiselect(event.get("id").get("promoterIdpromoter"), cb.count(event.get("id").get("proteinIdprotein"))); 

		Order[] orderList = {cb.asc(event.get("id").get("promoterIdpromoter"))};
		c.orderBy(orderList);

		c.groupBy(event.get("id").get("promoterIdpromoter"));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();
		Map<String, String> res = null;
		if(list.size() > 0) {
			res = new HashMap<String,String>();
			for(Object[] result: list) {

				res.put((String) result[0], (String) result[1]);
			}	
		}
		return res;
	}

	@Override 
	public List<String[]> getRegulatoryAttributes(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<RegulatoryEvent> reg = c.from(RegulatoryEvent.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);
		Root<RegulatoryPromoter> promoter = c.from(RegulatoryPromoter.class);
		Root<RegulatoryRiFunction> rifunction = c.from(RegulatoryRiFunction.class);

		c.multiselect(prot.get("idprotein"), promoter.get("idpromoter"), rifunction.get("iidriFunctiond"),
				reg.get("bindingSitePosition"), prot.get("name"), promoter.get("name"), rifunction.get("symbol"));

		Predicate filter1 = cb.equal(promoter.get("idpromoter"), reg.get("id").get("promoterIdpromoter"));
		Predicate filter2 = cb.equal(prot.get("idprotein"), reg.get("id").get("proteinIdprotein"));
		Predicate filter3 = cb.equal(reg.get("id").get("riFunctionIdriFunction"), rifunction.get("idriFunction"));

		Order[] orderList = {cb.asc(prot.get("idprotein")), cb.asc(promoter.get("idpromoter"))}; 

		c.where(cb.and(filter1,filter2, filter3)).orderBy(orderList);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();

		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[7];
				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				list[2] = String.valueOf(item[2]);
				list[3] = String.valueOf(item[3]);
				list[4] = (String) item[4];
				list[5] = (String) item[5];
				list[6] = (String) item[6];
				parsedList.add(list);	
			}
			return parsedList;
		}
		return null;
	}

	@Override 
	public List<String> getRegulatoryEventProteinName(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelProtein> c = cb.createQuery(ModelProtein.class);
		Root<RegulatoryEvent> event = c.from(RegulatoryEvent.class);
		Root<ModelProtein> prot = c.from(ModelProtein.class);

		c.multiselect(prot.get("name")); 
		Predicate filter1 = cb.equal(event.get("id").get("proteinIdprotein"), prot.get("idprotein"));
		Predicate filter2 = cb.equal(event.get("id").get("promoterIdpromoter"), id);

		c.where(cb.and(filter1, filter2));

		Query<ModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelProtein> resultList = q.getResultList();

		List<String> result = new ArrayList<String>();
		if(resultList.size() > 0) {

			for(ModelProtein item: resultList) {
				result.add(item.getName());
			}
			return result;
		}
		return null;
	}

	//	@Override 
//	public List<String[]> getRegulatoryEventAttributesOrderByGeneName(){
		//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		//		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		//		Root<RegulatoryEvent> regulatory = c.from(RegulatoryEvent.class);
		//		Root<RegulatoryTranscriptionUnit> prot = c.from(RegulatoryTranscriptionUnit.class);
		//		//transcription_unit_gene - nao existe
		//		Root<RegulatoryTranscriptionUnitPromoter> promoter = c.from(RegulatoryTranscriptionUnitPromoter.class);
		//		Root<ModelRiFunction> rifunction = c.from(ModelRiFunction.class);
		//		
		//	    c.multiselect(prot.get("idprotein"), promoter.get("idpromoter"), rifunction.get("iidriFunctiond"),
		//	    		reg.get("bindingSitePosition"), prot.get("name"), promoter.get("name"), rifunction.get("symbol"));
		//	    
		//		Predicate filter1 = cb.equal(promoter.get("idpromoter"), reg.get("id").get("promoterIdpromoter"));
		//	    Predicate filter2 = cb.equal(prot.get("idprotein"), reg.get("id").get("proteinIdprotein"));
		//	    Predicate filter3 = cb.equal(reg.get("id").get("riFunctionIdriFunction"), rifunction.get("idriFunction"));
		//	   
		//	    Order[] orderList = {cb.asc(prot.get("idprotein")), cb.asc(promoter.get("idpromoter"))}; 
		//
		//	    c.where(cb.and(filter1,filter2, filter3)).orderBy(orderList);
		//	    
		//	    Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		//		List<Object[]> resultList = q.getResultList();
		//
		//		if(resultList.size() > 0) {
		//			ArrayList<String[]> parsedList = new ArrayList<String[]>();
		//			
		//			for(Object[] item: resultList) {
		//				String[] list = new String[7];
		//				list[0] = String.valueOf(item[0]);
		//				list[1] = String.valueOf(item[1]);
		//				list[2] = String.valueOf(item[2]);
		//				list[3] = String.valueOf(item[3]);
		//				list[4] = (String) item[4];
		//				list[5] = (String) item[5];
		//				list[6] = (String) item[6];
		//				parsedList.add(list);	
		//			}
		//			return parsedList;
		//		}
		//		return null;
		//	} 


//	}
}