package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResult;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResults;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproXRef;

public interface I_InterproService {
	
	public List<String> getLoadedIntroProAnnotations(String status) throws Exception;
	
	public Integer loadInterProAnnotation(String query, String querySequence, String mostLikelyEC,
			String mostLikelyLocalization, String name, String mostLikelyName, String status) throws Exception;
	
	public void loadInterProLocation(int start, int end, float score, int hmmstart, int hmmend, float eValue,
			int envstart, int envend, int hmmlength, int resultID) throws Exception;
	
	public void loadInterProResultHasModel(int resultID, String modelAccession) throws Exception;
	
	public void setInterProStatus(Integer id, String status) throws Exception;
	
	public void deleteInterProEntries(String status) throws Exception;
	
	public List<String> getInterProGenes(String status) throws Exception;
	
	public List<InterproResults> getAllInterproResultsByQuery(String query) throws Exception;
	
	public Integer insertInterproResults(String query, String querySequence, 
			String mostLikelyEc, String mostLikelyLocalization, 
			String mostLikelyName, String status) throws Exception;

	public List<InterproResult> getAllInterproResultByDatabaseAndAccessionAndResultsId(String database, String accession,
			Integer results) throws Exception;

	public Integer insertInterproResultData(String tool, Float eValue, Float score, String familyName, String accession,
			String name, String ec, String goName, String localization, String database, Integer resultId)
			throws Exception;

	public List<InterproEntry> getAllInterproEntryByAccession(String accession) throws Exception;

	public Integer insertInterproEntryData(String accession, String name, String description, String type) throws Exception;

	public List<InterproXRef> getAllInterproXRefByExternalIdAndEtryId(String external, Integer entryId) throws Exception;

	public Integer insertInterproXRefData(String category, String database, String name, String external_id, Integer entry_id)
			throws Exception;

	public List<String> getInterproModelAccessionByAccession(String accession) throws Exception;

	public Integer insertInterproModelData(String accession, String description, String name) throws Exception;

	public List<InterproResultHasEntry> getAllInterproResultHasEntryByResultIdAndEntryID(int resultId, int entryId)
			throws Exception;

	public Integer insertInterproResultHasEntry(int resultId, int entryId) throws Exception;

}
