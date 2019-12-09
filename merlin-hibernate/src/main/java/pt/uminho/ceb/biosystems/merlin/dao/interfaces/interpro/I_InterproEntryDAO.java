package pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproEntry;



public interface I_InterproEntryDAO extends IGenericDao<InterproEntry>{
	
	public void addInterproEntry(InterproEntry interproInterproEntry); 
	
	public void addInterproEntryList(List<InterproEntry> interproInterproEntryList); 
	
	public List<InterproEntry> getAllInterproEntryList(); 
	
	public InterproEntry getInterproEntry(Integer id); 
	
	public void removeInterproEntry(InterproEntry interproInterproEntry); 
	
	public void removeInterproEntryList(List<InterproEntry> interproInterproEntryList); 
	
	public void updateInterproEntryList(List<InterproEntry> interproInterproEntryList); 
	
	public void updateInterproEntry(InterproEntry interproInterproEntry);

	public List<InterproEntry> getAllInterproEntryByAccession(String accession);

	public Integer insertInterproEntryData(String accession, String name, String description, String type);

}
