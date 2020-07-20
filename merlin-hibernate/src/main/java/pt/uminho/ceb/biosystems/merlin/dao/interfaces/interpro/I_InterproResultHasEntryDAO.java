package pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasEntry;


public interface I_InterproResultHasEntryDAO extends IGenericDao<InterproResultHasEntry>{

	public List<InterproResultHasEntry> getAllInterproResultHasEntryByResultIdAndEntryID(int resultId, int entryId);

	public Integer insertInterproResultHasEntry(int resultId, int entryId);

}
