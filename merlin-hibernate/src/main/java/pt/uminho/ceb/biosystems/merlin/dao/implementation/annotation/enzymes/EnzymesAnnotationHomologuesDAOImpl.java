package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologuesDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologues;

public class EnzymesAnnotationHomologuesDAOImpl extends GenericDaoImpl<EnzymesAnnotationHomologues> implements IEnzymesAnnotationHomologuesDAO {

	public EnzymesAnnotationHomologuesDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationHomologues.class);
		
	}

	@Override
	public void addEnzymesAnnotationHomologues(EnzymesAnnotationHomologues enzymesAnnotationHomologues) {
		super.save(enzymesAnnotationHomologues);
		
	}

	@Override
	public void addEnzymesAnnotationHomologues(List<EnzymesAnnotationHomologues> enzymesAnnotationHomologuesList) {
		for (EnzymesAnnotationHomologues enzymesAnnotationHomologues: enzymesAnnotationHomologuesList) {
			this.addEnzymesAnnotationHomologues(enzymesAnnotationHomologues);
		}
	}

	@Override
	public List<EnzymesAnnotationHomologues> getAllEnzymesAnnotationHomologues() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationHomologues getEnzymesAnnotationHomologues(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationHomologues(EnzymesAnnotationHomologues enzymesAnnotationHomologues) {
		super.delete(enzymesAnnotationHomologues);
		
	}

	@Override
	public void removeEnzymesAnnotationHomologuesList(List<EnzymesAnnotationHomologues> enzymesAnnotationHomologuesList) {
		for (EnzymesAnnotationHomologues enzymesAnnotationHomologues: enzymesAnnotationHomologuesList) {
			this.removeEnzymesAnnotationHomologues(enzymesAnnotationHomologues);
		}
	}

	@Override
	public void updateEnzymesAnnotationHomologuesList(List<EnzymesAnnotationHomologues> enzymesAnnotationHomologuesList) {
		for (EnzymesAnnotationHomologues enzymesAnnotationHomologues: enzymesAnnotationHomologuesList) {
			this.update(enzymesAnnotationHomologues);
		}
	}

	@Override
	public void updateEnzymesAnnotationHomologues(EnzymesAnnotationHomologues enzymesAnnotationHomologues) {
		super.update(enzymesAnnotationHomologues);
		
	}

	@Override
	public Integer getHomologuesSkey(String referenceID) {
		
		Integer res = -1;
		
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("definition", referenceID);
		List<EnzymesAnnotationHomologues> list =  this.findByAttributes(dic);
		
		if(list != null && list.size()>0)
			res = list.get(0).getSKey();
		
		return res;
	}
	

}


