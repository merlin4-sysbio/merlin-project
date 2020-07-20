package pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro;

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
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultsDAO;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproLocation;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResult;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResults;



public class InterproResultsDAOImpl extends GenericDaoImpl<InterproResults> implements I_InterproResultsDAO{

	public InterproResultsDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, InterproResults.class);

	}

	@Override
	public void addInterproResults(InterproResults interproInterproResults) {
		super.save(interproInterproResults);

	}

	@Override
	public void addInterproResults(List<InterproResults> interproInterproResultsList) {
		for (InterproResults interproInterproResults: interproInterproResultsList) {
			this.addInterproResults(interproInterproResults);
		}
	}

	@Override
	public List<InterproResults> getAllInterproResults() {
		return super.findAll();
	}

	@Override
	public InterproResults getInterproResults(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeInterproResults(InterproResults interproInterproResults) {
		super.delete(interproInterproResults);
	}

	@Override
	public void removeInterproResultsList(List<InterproResults> interproInterproResultsList) {
		for (InterproResults interproInterproResults: interproInterproResultsList) {
			this.removeInterproResults(interproInterproResults);
		}
	}

	@Override
	public void updateInterproResultsList(List<InterproResults> interproInterproResultsList) {
		for (InterproResults interproInterproResults: interproInterproResultsList) {
			this.update(interproInterproResults);
		}
	}

	@Override
	public void updateInterproResults(InterproResults interproInterproResults) {
		super.update(interproInterproResults);

	}

	@Override
	public List<InterproResults> getAllInterproResultsByQuery(String query) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("query", query);
		List<InterproResults> list =  this.findByAttributes(dic);
		return list;
	}

	//	"SELECT interpro_result.database, interpro_result.accession,  interpro_result.name, interpro_result.eValue, "
	//	+ " interpro_result.ec, interpro_result.name, interpro_result.goName, interpro_result.localization, interpro_entry.accession, "
	//	+ " interpro_entry.name, interpro_entry.description, "
	//	+ " interpro_location.start, interpro_location.end "
	//	+ " FROM interpro_results "
	//	+ " INNER JOIN interpro_result ON (interpro_results.id = interpro_result.results_id) "
	//	+ " INNER JOIN interpro_result_has_entry ON (interpro_result.id = interpro_result_has_entry.result_id) "
	//	+ " INNER JOIN interpro_entry ON (interpro_result_has_entry.entry_id = interpro_entry.id) "
	//	+ " INNER JOIN interpro_location ON (interpro_result.id = interpro_location.result_id) "
	//	+ " WHERE query = '" + query +"' " );
	//	
	@Override
	public List<String[]> getInterproResultsDataByQuery(String query) { 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<InterproResults> results = c.from(InterproResults.class);
		Root<InterproResult> res = c.from(InterproResult.class);
		Root<InterproEntry> entry = c.from(InterproEntry.class);
		//falta a resultHasEntry!!!
		Root<InterproLocation> location = c.from(InterproLocation.class);  //interpro_result.name repetido

		c.multiselect(res.get("database"), res.get("accession"), res.get("name"), res.get("evalue"), res.get("ec"),
				res.get("goName"), res.get("localization"), entry.get("accession"), entry.get("name"), entry.get("description"),
				location.get("start"), location.get("end")); 

		Predicate filter1 = cb.equal(results.get("id"), res.get("interproInterproResults").get("id"));
		//Predicate filter2 = cb.equal(res.get("id"), );//falta has entry
		// Predicate filter3 = cb.equal(); //falta has entry
		Predicate filter4 = cb.equal(res.get("id"), location.get("interproInterproResult").get("id"));


		c.where(cb.and(filter1, filter4));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		if(resultList.size() > 0) {
			ArrayList<String[]> parsedList = new ArrayList<String[]>();

			for(Object[] item: resultList) {
				String[] list = new String[12];
				list[0] = (String) item[0];
				list[1] = (String) item[1];
				list[2] = (String) item[2];
				list[3] = String.valueOf(item[3]);
				list[4] = (String) item[4];
				list[5] = (String) item[5];
				list[6] = (String) item[6];
				list[7] = (String) item[7];
				list[8] = (String) item[8];
				list[9] = (String) item[9];
				list[10] = String.valueOf(item[10]);
				list[11] = String.valueOf(item[11]);
				parsedList.add(list);	
			}

			return parsedList;
		}
		return null;
	}

	@Override
	public List<InterproResults> getAllInterproResultsByStatus(String status) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("status", status);
		List<InterproResults> list =  this.findByAttributes(dic);

		return list;
	}

	@Override
	public Integer insertInterproResults(String query, String querySequence, String mostLikelyEc, String mostLikelyLocalization, 
			String mostLikelyName, String status){
		InterproResults results = new InterproResults();
		results.setQuery(query);
		results.setQuerySequence(querySequence);
		results.setMostLikelyEc(mostLikelyEc);
		results.setMostLikelyLocalization(mostLikelyLocalization);
		results.setMostLikelyName(mostLikelyName);
		results.setStatus(status);

		return (Integer) this.save(results);	
	}

	@Override
	public void updateInterproResultsStatusById(String status, Integer id) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("id", id);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("status", status);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public void removeInterproResultsByStatus(String status) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("status", status);
		List<InterproResults> list = this.findByAttributes(dic);
		if (list.size() > 0) {
			this.removeInterproResultsList(list);
		}
	}

	@Override
	public List<String> getInterproResultsQueryByStatus(String status) {

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("status", status);
		List<InterproResults> res = this.findByAttributes(map);
		List<String> result = new ArrayList<String>();

		if (res!=null && res.size()>0) {
			for (InterproResults x:res) {
				result.add(x.getQuery());
			}

			return result;

		}
		
		return result;

	}


}
