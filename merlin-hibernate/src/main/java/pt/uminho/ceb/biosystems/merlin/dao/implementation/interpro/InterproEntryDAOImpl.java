package pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproEntryDAO;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproEntry;


public class InterproEntryDAOImpl extends GenericDaoImpl<InterproEntry> implements I_InterproEntryDAO {

	public InterproEntryDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, InterproEntry.class);
		
	}

	@Override
	public void addInterproEntry(InterproEntry interproInterproEntry) {
		super.save(interproInterproEntry);
		
	}

	@Override
	public void addInterproEntryList(List<InterproEntry> interproInterproEntryList) {
		for (InterproEntry interproInterproEntry: interproInterproEntryList) {
			this.addInterproEntry(interproInterproEntry);
		}
	}

	@Override
	public List<InterproEntry> getAllInterproEntryList() {
		return super.findAll();
	}

	@Override
	public InterproEntry getInterproEntry(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeInterproEntry(InterproEntry interproInterproEntry) {
		super.delete(interproInterproEntry);
		
	}

	@Override
	public void removeInterproEntryList(List<InterproEntry> interproInterproEntryList) {
		for (InterproEntry interproInterproEntry: interproInterproEntryList) {
			this.removeInterproEntry(interproInterproEntry);
		}	
	}

	@Override
	public void updateInterproEntryList(List<InterproEntry> interproInterproEntryList) {
		for (InterproEntry interproInterproEntry: interproInterproEntryList) {
			this.update(interproInterproEntry);
		}
	}

	@Override
	public void updateInterproEntry(InterproEntry interproInterproEntry) {
		super.update(interproInterproEntry);
		
	}

	@Override	
	public List<InterproEntry> getAllInterproEntryByAccession(String accession) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("accession", accession);
		List<InterproEntry> list =  this.findByAttributes(dic);
		
		return list;
	}

	@Override
	public Integer insertInterproEntryData(String accession, String name, String description, String type){
		InterproEntry entry = new InterproEntry();
		entry.setAccession(accession);
		entry.setName(name);
		entry.setDescription(description);
		entry.setType(type);
		
		return (Integer) this.save(entry);	
	}
}
