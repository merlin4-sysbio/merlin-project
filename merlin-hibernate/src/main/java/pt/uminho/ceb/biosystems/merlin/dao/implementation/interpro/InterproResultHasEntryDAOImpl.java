package pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultHasEntryDAO;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasEntryId;


public class InterproResultHasEntryDAOImpl extends GenericDaoImpl<InterproResultHasEntry> implements I_InterproResultHasEntryDAO{

	public InterproResultHasEntryDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, InterproResultHasEntry.class);
	}

	
	
	@Override
	public List<InterproResultHasEntry> getAllInterproResultHasEntryByResultIdAndEntryID(int resultId, int entryId) {
		
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.interproResultId", resultId);
		map.put("id.interproEntryId", entryId);
		List<InterproResultHasEntry> res = this.findByAttributes(map);
		return res;
	}
	
	@Override
	public Integer insertInterproResultHasEntry(int resultId, int entryId){
		
		InterproResultHasEntry interpro = new InterproResultHasEntry();
		InterproResultHasEntryId id = new InterproResultHasEntryId();
		id.setInterproResultId(resultId);
		id.setInterproEntryId(entryId);
		interpro.setId(id);
		
		return (Integer) this.save(interpro);	
	}
	
}
