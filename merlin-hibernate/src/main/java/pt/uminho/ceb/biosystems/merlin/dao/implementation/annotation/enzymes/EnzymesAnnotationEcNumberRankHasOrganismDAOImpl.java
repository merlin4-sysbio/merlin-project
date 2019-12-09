package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberrankHasOrganismDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberRankHasOrganism;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberRankHasOrganismId;


public class EnzymesAnnotationEcNumberRankHasOrganismDAOImpl extends GenericDaoImpl<EnzymesAnnotationEcNumberRankHasOrganism> implements IEnzymesAnnotationEcNumberrankHasOrganismDAO {

	public EnzymesAnnotationEcNumberRankHasOrganismDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationEcNumberRankHasOrganism.class);
		
	}

	@Override
	public void addEnzymesAnnotationEcNumberRankHasOrganism(EnzymesAnnotationEcNumberRankHasOrganism EnzymesAnnotationEcNumberRankHasOrganism) {
		super.save(EnzymesAnnotationEcNumberRankHasOrganism);
		
	}

	@Override
	public void addEnzymesAnnotationEcNumberrankList(List<EnzymesAnnotationEcNumberRankHasOrganism> EnzymesAnnotationEcNumberRankHasOrganismList) {
		for (EnzymesAnnotationEcNumberRankHasOrganism EnzymesAnnotationEcNumberRankHasOrganism: EnzymesAnnotationEcNumberRankHasOrganismList) {
			this.addEnzymesAnnotationEcNumberRankHasOrganism(EnzymesAnnotationEcNumberRankHasOrganism);
		}
		
	}
	
	@Override
	public List<EnzymesAnnotationEcNumberRankHasOrganism> getAllEnzymesAnnotationEcNumberRankHasOrganism() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationEcNumberRankHasOrganism getEnzymesAnnotationEcNumberRankHasOrganism(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationEcNumberRankHasOrganism(EnzymesAnnotationEcNumberRankHasOrganism EnzymesAnnotationEcNumberRankHasOrganism) {
		super.delete(EnzymesAnnotationEcNumberRankHasOrganism);
		
	}

	@Override
	public void removeEnzymesAnnotationEcNumberRankHasOrganismList(List<EnzymesAnnotationEcNumberRankHasOrganism> EnzymesAnnotationEcNumberRankHasOrganismList) {
		for (EnzymesAnnotationEcNumberRankHasOrganism EnzymesAnnotationEcNumberRankHasOrganism: EnzymesAnnotationEcNumberRankHasOrganismList) {
			this.removeEnzymesAnnotationEcNumberRankHasOrganism(EnzymesAnnotationEcNumberRankHasOrganism);
		}
	}

	@Override
	public void updateEnzymesAnnotationEcNumberRankHasOrganismList(List<EnzymesAnnotationEcNumberRankHasOrganism> EnzymesAnnotationEcNumberRankHasOrganismList) {
		for (EnzymesAnnotationEcNumberRankHasOrganism EnzymesAnnotationEcNumberRankHasOrganism: EnzymesAnnotationEcNumberRankHasOrganismList) {
			this.update(EnzymesAnnotationEcNumberRankHasOrganism);
		}
	}

	@Override
	public void updateEnzymesAnnotationEcNumberRankHasOrganism(EnzymesAnnotationEcNumberRankHasOrganism EnzymesAnnotationEcNumberRankHasOrganism) {
		super.update(EnzymesAnnotationEcNumberRankHasOrganism);
		
	}
	
	
	@Override
	public Boolean FindEnzymesAnnotationEcNumberRankHasOrganismByIds(Integer sKey, Integer orgKey) {

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("enzymesAnnotationEcNumberRank.SKey", sKey);
		map.put("enzymesAnnotationOrganism.SKey", orgKey);
		List<EnzymesAnnotationEcNumberRankHasOrganism> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			return true;
		}
		else return false;

	}
	
	@Override
	public void InsertEnzymesAnnotationEcNumberRankHasOrganism(Integer sKey, Integer orgKey) {

		
		EnzymesAnnotationEcNumberRankHasOrganism ecNumberRankHasOrganism = new EnzymesAnnotationEcNumberRankHasOrganism();
		EnzymesAnnotationEcNumberRankHasOrganismId ecNumberRankHasOrganismId = new EnzymesAnnotationEcNumberRankHasOrganismId();
		
		ecNumberRankHasOrganismId.setEcNumberRankSKey(sKey);
		ecNumberRankHasOrganismId.setOrganismSKey(orgKey);
		ecNumberRankHasOrganism.setId(ecNumberRankHasOrganismId);

		this.save(ecNumberRankHasOrganism);
		
	}
	
	@Override
	public List<Integer> getAllEcRankSKeyInEcRankHasOrganism(){
		
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);
		Root<EnzymesAnnotationEcNumberRankHasOrganism> rankHasOrg = c.from(EnzymesAnnotationEcNumberRankHasOrganism.class);
		
	    c.select(rankHasOrg.get("id").get("ecNumberRankSKey")).distinct(true); 
	    
		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer> resultList = q.getResultList();
		
		return resultList;
	}
}
