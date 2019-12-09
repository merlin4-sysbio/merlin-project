package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IOrthologyService {
	
	public Map<String, Set<String>> getOrthologs(String entry) throws Exception;
	
	public List<String[]> getDataFromGeneHasOrthology(Integer id) throws Exception;
	
	public boolean checkGeneHasOrthologyEntries(int geneID, int orthid) throws Exception;

	public Map<String, Integer> getEntryIdAndOrthologyId() throws Exception;

	public Integer insertNewOrthologue(String entryId, String locus) throws Exception;

	public Integer getOrthologyIdByEntryIdAndLocus(String entryId, String locusId) throws Exception;

	public Integer getOrthologyIdByEntryIdWherwLocusIdIsNullOrEmpty(String entryId) throws Exception;

	public void updateOrthologyLocusIdByEntryId(String entryId, String locusId) throws Exception;
	
}
