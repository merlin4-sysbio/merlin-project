package pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproLocationDAO;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproLocation;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResult;


public class InterproLocationDAOImpl extends GenericDaoImpl<InterproLocation> implements I_InterproLocationDAO{

	public InterproLocationDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, InterproLocation.class);
		
	}

	@Override
	public void addInterproLocation(InterproLocation interproInterproLocation) {
		super.save(interproInterproLocation);
		
	}

	@Override
	public void addInterproLocationList(List<InterproLocation> interproInterproLocationList) {
		for (InterproLocation interproInterproLocation: interproInterproLocationList) {
			this.addInterproLocation(interproInterproLocation);
		}
	}

	@Override
	public List<InterproLocation> getAllInterproLocation() {
		return super.findAll();
	}

	@Override
	public InterproLocation getInterproLocation(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeInterproLocation(InterproLocation interproInterproLocation) {
		super.delete(interproInterproLocation);
	}

	@Override
	public void removeInterproLocationList(List<InterproLocation> interproInterproLocationList) {
		for (InterproLocation interproInterproLocation: interproInterproLocationList) {
			this.removeInterproLocation(interproInterproLocation);
		}
	}

	@Override
	public void updateInterproLocationList(List<InterproLocation> interproInterproLocationList) {
		for (InterproLocation interproInterproLocation: interproInterproLocationList) {
			this.update(interproInterproLocation);
		}
	}

	@Override
	public void updateInterproLocation(InterproLocation interproInterproLocation) {
		super.update(interproInterproLocation);
		
	}

	@Override
	public List<InterproLocation> getAllInterproLocationByAttributes(int id, int start, int end, float score, float evalue) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id", id);
		dic.put("start", start);
		dic.put("end", end);
		dic.put("score", score);
		dic.put("evalue", evalue);
		List<InterproLocation> list =  this.findByAttributes(dic);
		
		return list;
		}

	@Override
	public Integer insertInterproLocationData(int start, int end, float score, int hmmStart, int hmmEnd, float evalue, int envStart, int envEnd, int hmmLength, int result_id){
		InterproLocation loc = new InterproLocation(); 

		InterproResultDAOImpl resultDAO = new InterproResultDAOImpl(this.sessionFactory);
		InterproResult result = resultDAO.getInterproResult(result_id);
		
		loc.setStart(start);
		loc.setEnd(end);
		loc.setScore(score);
		loc.setHmmStart(hmmStart);
		loc.setHmmEnd(hmmEnd);
		loc.setEvalue(evalue);
		loc.setEnvStart(envStart);
		loc.setEnvEnd(envEnd);
		loc.setHmmLength(hmmLength);
		loc.setInterproResult(result);
	
		return (Integer) this.save(loc);	
	}
}
