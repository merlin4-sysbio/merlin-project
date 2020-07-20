package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationScorerconfigDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationScorerConfig;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationScorerConfigId;


public class EnzymesAnnotationScorerconfigDAOImpl extends GenericDaoImpl<EnzymesAnnotationScorerConfig> implements IEnzymesAnnotationScorerconfigDAO  {

	public EnzymesAnnotationScorerconfigDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationScorerConfig.class);

	}

	@Override
	public void addEnzymesAnnotationScorerConfig(EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig) {
		super.save(enzymesAnnotationScorerConfig);

	}

	@Override
	public void addEnzymesAnnotationScorerConfig(List<EnzymesAnnotationScorerConfig> enzymesAnnotationScorerConfigList) {
		for (EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig: enzymesAnnotationScorerConfigList) {
			this.addEnzymesAnnotationScorerConfig(enzymesAnnotationScorerConfig);
		}
	}

	@Override
	public List<EnzymesAnnotationScorerConfig> getAllEnzymesAnnotationScorerConfig() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationScorerConfig getEnzymesAnnotationScorerConfig(Integer id) {
		return super.findById(id);
	}

	@Override
	public void EnzymesAnnotationScorerConfig(EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig) {
		super.delete(enzymesAnnotationScorerConfig);

	}

	@Override
	public void EnzymesAnnotationScorerConfigList(List<EnzymesAnnotationScorerConfig> enzymesAnnotationScorerConfigList) {
		for (EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig: enzymesAnnotationScorerConfigList) {
			this.EnzymesAnnotationScorerConfig(enzymesAnnotationScorerConfig);
		}
	}

	@Override
	public void updateEnzymesAnnotationScorerConfigList(List<EnzymesAnnotationScorerConfig> enzymesAnnotationScorerConfigList) {
		for (EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig: enzymesAnnotationScorerConfigList) {
			this.update(enzymesAnnotationScorerConfig);
		}

	}

	@Override
	public void updateEnzymesAnnotationScorerConfig(EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig) {
		super.update(enzymesAnnotationScorerConfig);

	}

	@Override
	public List<EnzymesAnnotationScorerConfig> getAllEnzymesAnnotationScorerConfigAttributes() {

		List<EnzymesAnnotationScorerConfig> list =  this.findAll();
		return list;
	}

	@Override
	public String getScorerConfigBlastDB() {

		String allDatabases = "";	//this represents all databases

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.latest", true);
		List<EnzymesAnnotationScorerConfig> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return res.get(0).getId().getDatabaseName();
		}

		return allDatabases;
	}

	@Override
	public List<EnzymesAnnotationScorerConfig> getAllScorerConfigByBlastDB(String db) {

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.databaseName", db);
		List<EnzymesAnnotationScorerConfig> res = this.findByAttributes(map);

		return res;
	}

	@Override
	public void removeEnzymesAnnotationScorerConfig(EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig) {

		this.delete(enzymesAnnotationScorerConfig);

	}

	@Override
	public void removeEnzymesAnnotationScorerConfigList(List<EnzymesAnnotationScorerConfig> enzymesAnnotationScorerConfigList) {

		for (EnzymesAnnotationScorerConfig enzymesAnnotationScorerConfig: enzymesAnnotationScorerConfigList) {
			this.delete(enzymesAnnotationScorerConfig);
		}
	}
	
	@Override
	public void deleteAllScorerConfig() {
	        CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
	        CriteriaDelete<EnzymesAnnotationScorerConfig> delete = cb.createCriteriaDelete(EnzymesAnnotationScorerConfig.class);

	        delete.from(EnzymesAnnotationScorerConfig.class);

	        // perform update
	        super.sessionFactory.getCurrentSession().createQuery(delete).executeUpdate();
	    }

	@Override
	public void removeEnzymesAnnotationScorerconfigByDatabase(String db){
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.databaseName", db);
		List<EnzymesAnnotationScorerConfig> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			this.removeEnzymesAnnotationScorerConfigList(res);
		}
	}

	@Override
	public void updateEnzymesAnnotationScorerconfigByDB(String db) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("id.databaseName", db);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("id.bestAlpha", true);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public List<String> getDatabaseByBestAlpha(boolean bestAlpha) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.bestAlpha", bestAlpha);
		List<EnzymesAnnotationScorerConfig> res = this.findByAttributes(map);
		List<String> result = new ArrayList<>();
		if (res!=null && res.size()>0) {
			for (EnzymesAnnotationScorerConfig x : res)
				result.add(x.getId().getDatabaseName());
		}
		return result;
	}

	@Override
	public void updateEnzymesAnnotationLatestByDB(String db) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("id.databaseName", db);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("id.latest", true);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);

	}

	@Override
	public void updateEnzymesAnnotationAttributesByDB(String db, Float threshold, Float upperThreshold, Float balanceBh, Float beta, Float alpha,
			Integer minHomologies) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("id.databaseName", db);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("id.threshold", threshold);
		updateAttributes.put("id.upperThreshold", upperThreshold);
		updateAttributes.put("id.balanceBh", balanceBh);
		updateAttributes.put("id.beta", beta);
		updateAttributes.put("id.alpha", alpha);
		updateAttributes.put("id.minHomologies", minHomologies);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);

	}

	@Override
	public void insertEnzymesAnnotationScorerConfigAttributes(Float threshold, Float upperThreshold, Float balanceBH, Float alpha, Float beta, Integer minHomologies, String blastDB, Boolean latest, boolean bestAlpha){
		EnzymesAnnotationScorerConfig scorer = new EnzymesAnnotationScorerConfig();
		EnzymesAnnotationScorerConfigId id = new EnzymesAnnotationScorerConfigId();
		id.setAlpha(alpha);
		id.setUpperThreshold(upperThreshold);
		id.setBalanceBh(balanceBH);
		id.setBestAlpha(bestAlpha);
		id.setBeta(beta);
		id.setDatabaseName(blastDB);
		id.setLatest(latest);
		id.setMinHomologies(minHomologies);
		id.setThreshold(threshold);
		scorer.setId(id);
		this.save(scorer);	
	}


}
