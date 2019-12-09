package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproEntryDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproLocationDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproModelDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultHasEntryDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultHasModelDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproXRefDAO;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproLocation;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResult;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasModel;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResults;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproXRef;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.I_InterproService;

public class InterproServiceImpl implements I_InterproService {
	
	// ********* VARIAVEIS PRIVADAS ****//
	private I_InterproResultsDAO resultsDAO;
	private I_InterproResultDAO resultDAO;
	private I_InterproLocationDAO locationDAO; 
	private I_InterproResultHasModelDAO resultHasModelDAO;
	private I_InterproEntryDAO entryDAO;
	private I_InterproXRefDAO xrefDAO;
	private I_InterproModelDAO modelDAO;
	private I_InterproResultHasEntryDAO resultHasEntryDAO;
	
	
	//******* INSTANCIAR **********//
	public InterproServiceImpl(I_InterproResultsDAO resultsDAO, I_InterproResultDAO resultDAO,
			I_InterproLocationDAO locationDAO, I_InterproResultHasModelDAO resultHasModelDAO, 
			I_InterproEntryDAO entryDAO, I_InterproXRefDAO xrefDAO, I_InterproModelDAO modelDAO,
			I_InterproResultHasEntryDAO resultHasEntryDAO) { 
		
		this.resultsDAO = resultsDAO;
		this.resultDAO = resultDAO;
		this.locationDAO = locationDAO;
		this.resultHasModelDAO = resultHasModelDAO;
		this.entryDAO = entryDAO;
		this.xrefDAO = xrefDAO;
		this.modelDAO = modelDAO;
		this.resultHasEntryDAO = resultHasEntryDAO;
	}
	
	@Override
	public List<String> getLoadedIntroProAnnotations(String status) throws Exception {
		List<InterproResults> list = this.resultsDAO.getAllInterproResultsByStatus("PROCESSED");
		
		List<String> res = new ArrayList<String>();
		if (list != null) {
			for (InterproResults x: list) {
				res.add(x.getQuery()); 
			}				
		}
		return res;
	}
	
	@Override
	public Integer loadInterProAnnotation(String query, String querySequence, String mostLikelyEc,
			String mostLikelyLocalization, String name, String mostLikelyName, String status) throws Exception {
		
		Integer ret = null;
		if(name!= null) {
			Integer size = name.length();
			if (size>250) {
				size=249;}
	
			name = name.substring(0, size);
		}
		
		List<InterproResults> res = this.resultsDAO.getAllInterproResultsByQuery(querySequence);
		
		if (res != null) {
			Integer res2 = this.resultsDAO.insertInterproResults(query, querySequence, mostLikelyEc, mostLikelyLocalization, mostLikelyName, status);
			ret = res2;
		}
		
		return ret;
	}
	
	@Override
	public void loadInterProLocation(int start, int end, float score, int hmmstart, int hmmend, float evalue,
			int envstart, int envend, int hmmlength, int id) throws Exception {  
	 
		List<InterproLocation> list = this.locationDAO.getAllInterproLocationByAttributes(id, envstart, envend, score, evalue);
	
		if(list.size() == 0) {
			this.locationDAO.insertInterproLocationData(start, end, score, hmmstart, hmmend, evalue, envstart, envend, hmmlength, id);
		}
	}
	
	@Override
	public void loadInterProResultHasModel(int resultId, String modelAccession) throws Exception {
		List<InterproResultHasModel> list = this.resultHasModelDAO.getAllInterproResultHasModelByResultIdAndModelAccession(resultId, modelAccession);
		
		if (list.size() == 0) {
			this.resultHasModelDAO.insertInterproResultHasModel(modelAccession, resultId);
		}
	}

	@Override
	public void setInterProStatus(Integer id, String status) throws Exception {
		this.resultsDAO.updateInterproResultsStatusById(status, id);
		
	}

	@Override
	public void deleteInterProEntries(String status) throws Exception {
		this.resultsDAO.removeInterproResultsByStatus(status);
		
	}

	@Override
	public List<String> getInterProGenes(String status) throws Exception {
		return this.resultsDAO.getInterproResultsQueryByStatus(status);
	}
	
	@Override
	public List<InterproResults> getAllInterproResultsByQuery(String query) throws Exception {
		return this.resultsDAO.getAllInterproResultsByQuery(query);
	}
	
	@Override
	public Integer insertInterproResults(String query, String querySequence, 
			String mostLikelyEc, String mostLikelyLocalization, 
			String mostLikelyName, String status) throws Exception {
		
		return this.resultsDAO.insertInterproResults(query, querySequence, mostLikelyEc,
				mostLikelyLocalization, mostLikelyName, status);
	}

	@Override
	public List<InterproResult> getAllInterproResultByDatabaseAndAccessionAndResultsId(String database, String accession, Integer results) throws Exception {
		return this.resultDAO.getAllInterproResultByDatabaseAndAccessionAndResultsId(database, accession, results);
	}
	
	@Override
	public Integer insertInterproResultData(String tool, Float eValue, Float score,
			String familyName, String accession, String name, String ec,
			String goName, String localization, String database, Integer resultId) throws Exception {
		
		return this.resultDAO.insertInterproResultData(tool, eValue, score, familyName,
				accession, name, ec, goName, localization, database, resultId);
	}
	
	@Override
	public List<InterproEntry> getAllInterproEntryByAccession(String accession) throws Exception {
		return this.entryDAO.getAllInterproEntryByAccession(accession);
	
	}
	
	@Override
	public Integer insertInterproEntryData(String accession, String name, 
			String description, String type) throws Exception {
		
		return this.entryDAO.insertInterproEntryData(accession, name, description, type);
	}
	
	@Override
	public List<InterproXRef> getAllInterproXRefByExternalIdAndEtryId(String external, Integer entryId) throws Exception {
		return this.xrefDAO.getAllInterproXRefByExternalIdAndEtryId(external, entryId);
	
	}
	
	@Override
	public Integer insertInterproXRefData(String category, String database, String name,
			String external_id, Integer entry_id) throws Exception {
		
		return this.xrefDAO.insertInterproXRefData(category, database, name, external_id, entry_id);
	}
	
	@Override
	public List<String> getInterproModelAccessionByAccession(String accession) throws Exception {
		return this.modelDAO.getInterproModelAccessionByAccession(accession);
	
	}
	
	@Override
	public Integer insertInterproModelData(String accession, String description, String name) throws Exception {
		
		return this.modelDAO.insertInterproModelData(accession, description, name);
	}
	
	@Override
	public List<InterproResultHasEntry> getAllInterproResultHasEntryByResultIdAndEntryID(int resultId, int entryId) throws Exception {
		
		return this.resultHasEntryDAO.getAllInterproResultHasEntryByResultIdAndEntryID(resultId, entryId);
	}
	
	@Override
	public Integer insertInterproResultHasEntry(int resultId, int entryId) throws Exception {
		
		return this.resultHasEntryDAO.insertInterproResultHasEntry(resultId, entryId);
	}
	
	
}
