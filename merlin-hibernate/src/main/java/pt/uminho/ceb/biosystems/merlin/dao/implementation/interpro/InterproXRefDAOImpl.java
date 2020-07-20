package pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproXRefDAO;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproXRef;

public class InterproXRefDAOImpl extends GenericDaoImpl<InterproXRef> implements I_InterproXRefDAO{

	public InterproXRefDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, InterproXRef.class);
	
	}

	@Override
	public void addInterproXRef(InterproXRef interproInterproXRef) {
		super.save(interproInterproXRef);
		
	}

	@Override
	public void addInterproXRefList(List<InterproXRef> interproInterproXRefList) {
		for (InterproXRef interproInterproXRef: interproInterproXRefList) {
			this.addInterproXRef(interproInterproXRef);
		}
	}

	@Override
	public List<InterproXRef> getAllInterproXRef() {
		return super.findAll();
	}

	@Override
	public InterproXRef getInterproXRef(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeInterproXRef(InterproXRef interproInterproXRef) {
		super.delete(interproInterproXRef);
		
	}

	@Override
	public void removeInterproXRefList(List<InterproXRef> interproInterproXRefList) {
		for (InterproXRef interproInterproXRef: interproInterproXRefList) {
			this.removeInterproXRef(interproInterproXRef);
		}
	}

	@Override
	public void updateInterproXRefList(List<InterproXRef> interproInterproXRefList) {
		for (InterproXRef interproInterproXRef: interproInterproXRefList) {
			this.update(interproInterproXRef);
		}
	}

	@Override
	public void updateInterproXRef(InterproXRef interproInterproXRef) {
		super.update(interproInterproXRef);
	}
	
	@Override
	public List<InterproXRef> getAllInterproXRefByExternalIdAndEtryId(String external, Integer entryId) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("externalId", external);
		dic.put("interproInterproEntry.id", entryId);
		List<InterproXRef> list =  this.findByAttributes(dic);
		
		return list;
		}
	
	@Override
	public Integer insertInterproXRefData(String category, String database, String name, String external_id, Integer entry_id){
		InterproXRef xref = new InterproXRef();
		InterproEntryDAOImpl entryDAO = new InterproEntryDAOImpl(this.sessionFactory);
		InterproEntry entry = entryDAO.getInterproEntry(entry_id);
		
		xref.setCategory(category);
		xref.setDatabaseName(database);
		xref.setName(name);
		xref.setExternalId(external_id);
		xref.setInterproEntry(entry); 

		return (Integer) this.save(xref);	
	}

//	@Override
//	public void insertInterproXRefData(String category, String database, String name, String id, int entryId) {
//		// TODO Auto-generated method stub
//		
//	}
}
