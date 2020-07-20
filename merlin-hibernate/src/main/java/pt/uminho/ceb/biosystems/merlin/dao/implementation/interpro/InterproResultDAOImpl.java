package pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultDAO;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResult;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResults;

public class InterproResultDAOImpl extends GenericDaoImpl<InterproResult> implements I_InterproResultDAO {

	public InterproResultDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, InterproResult.class);
		
	}

	@Override
	public void addInterproResult(InterproResult interproInterproResult) {
		super.save(interproInterproResult);
	}

	@Override
	public void addInterproResultList(List<InterproResult> interproInterproResultList) {
		for (InterproResult interproInterproResult: interproInterproResultList) {
			this.addInterproResult(interproInterproResult);
		}
	}

	@Override
	public List<InterproResult> getAllInterproResult() {
		return super.findAll();
	}

	@Override
	public InterproResult getInterproResult(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeInterproResult(InterproResult interproInterproResult) {
		super.delete(interproInterproResult);
		
	}

	@Override
	public void removeInterproResultList(List<InterproResult> interproInterproResultList) {
		for (InterproResult interproInterproResult: interproInterproResultList) {
			this.removeInterproResult(interproInterproResult);
		}
	}

	@Override
	public void updateInterproResultList(List<InterproResult> interproInterproResultList) {
		for (InterproResult interproInterproResult: interproInterproResultList) {
			this.update(interproInterproResult);
		}
	}

	@Override
	public void updateInterproResult(InterproResult interproInterproResult) {
		super.update(interproInterproResult);
		
	}

	@Override
	public List<InterproResult> getAllInterproResultByDatabaseAndAccessionAndResultsId(String database, String accession, Integer results) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("database", database);
		dic.put("accession", accession);
		dic.put("interproInterproResults.id", results);
		List<InterproResult> list =  this.findByAttributes(dic);
		
		return list;
		}
	
	@Override
	public Integer insertInterproResultData(String tool, Float eValue, Float score, String familyName, 
			String accession, String name, String ec, String goName, String localization, String database, Integer resultId){
		InterproResult result = new InterproResult();
		InterproResultsDAOImpl resDAO = new InterproResultsDAOImpl(this.sessionFactory);
		InterproResults res = resDAO.getInterproResults(resultId);
		
		result.setTool(tool);
		result.setEvalue(eValue);
		result.setScore(score);
		result.setFamilyName(familyName);
		result.setAccession(accession);
		result.setName(name);
		result.setEc(ec);
		result.setGoName(goName);
		result.setLocalization(localization);
		result.setDatabaseName(database); //tinha aux: + " "+aux+"database,
		result.setInterproResults(res);

		return (Integer) this.save(result);	
	}


	
}
