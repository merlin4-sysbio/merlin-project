package pt.uminho.ceb.biosystems.merlin.services.interpro;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.InterproStatus;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResult;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResults;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproXRef;

public class InterproServices {

	final static Logger logger = LoggerFactory.getLogger(InterproServices.class);

	
	
	
	public static List<String> getInterproResultsQueryByStatus(String databaseName, String status) throws Exception{
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getInterProGenes(status);
	}
	
	
	public static int loadInterProAnnotation(String databaseName, String query, String querySequence, String mostLikelyEC,
			String mostLikelyLocalization, String name) throws Exception{
		
		if (name != null) {

			int size = name.length();

			if (size > 250)
				size = 249;

			name = name.substring(0, size);
		}
		
		List<InterproResults> resByQuery = InitDataAccess.getInstance().getDatabaseService(databaseName).getAllInterproResultsByQuery(query);
		
		if (resByQuery.size() == 0) {
			int res = InitDataAccess.getInstance().getDatabaseService(databaseName).insertInterproResults(query, querySequence, mostLikelyEC,
					mostLikelyLocalization, name, InterproStatus.PROCESSING.toString());
			return res;
		}
		
		return resByQuery.get(0).getId();
	
	}
	
	
	public static int loadInterProResult(String databaseName, String tool, double eValue, double score, String family, String accession,
			String name, String ec, String goName, String localization, String database, int resultsID) throws Exception{
		
		if (name != null) {

			int size = name.length();

			if (size > 250)
				size = 249;

			name = name.substring(0, size);
		}
		
		if (goName != null) {

			int size = goName.length();

			size = goName.length();
			if (size > 250)
				size = 249;

			goName = goName.substring(0, size);
		}
		
		
		List<InterproResult> intermediateRes = InitDataAccess.getInstance().getDatabaseService(databaseName).getAllInterproResultByDatabaseAndAccessionAndResultsId(database, accession, resultsID);
		
		if (intermediateRes.size() == 0) {
			int res = InitDataAccess.getInstance().getDatabaseService(databaseName).insertInterproResultData(tool,
					(float) eValue, (float) score, family, accession, name, ec, goName, localization, database, resultsID);
			return res;
		}
		
		return intermediateRes.get(0).getId();
	
	}
	
	
	public static int loadInterProEntry(String databaseName, String accession, String description, 
			String name, String type) throws Exception{
		
		if (name != null) {

			int size = name.length();

			if (size > 250)
				size = 249;

			name = name.substring(0, size);
		}
		
		if (description != null) {

			int size = description.length();

			if (size > 250)
				size = 249;

			description = description.substring(0, size);
		}
		
		
		List<InterproEntry> intermediateRes = InitDataAccess.getInstance().getDatabaseService(databaseName).getAllInterproEntryByAccession(accession);
		
		if (intermediateRes.size() == 0) {
			int res = InitDataAccess.getInstance().getDatabaseService(databaseName).insertInterproEntryData(accession, name, description, type);
			return res;
		}
		
		return intermediateRes.get(0).getId();
	
	}
	
	public static void loadXrefs(String databaseName, String category, 
			String database, String name, String id, int entryID) throws Exception{
		
		if (name != null) {

			int size = name.length();

			if (size > 250)
				size = 249;

			name = name.substring(0, size);
		}
		
		List<InterproXRef> intermediateRes = InitDataAccess.getInstance().getDatabaseService(databaseName).getAllInterproXRefByExternalIdAndEtryId(id, entryID);
		
		if (intermediateRes.size() == 0) {
			InitDataAccess.getInstance().getDatabaseService(databaseName).insertInterproXRefData(category, database, name, id, entryID);
		}
		
	}
	
	
	public static void loadInterProLocation(String databaseName, int start, int end, float score, int hmmstart, int hmmend, float eValue,
			int envstart, int envend, int hmmlength, int resultID) throws Exception{
		
			InitDataAccess.getInstance().getDatabaseService(databaseName).loadInterProLocation(start, end, score, hmmstart, hmmend, eValue,
					envstart, envend, hmmlength, resultID);
		
	}
	
	
	public static String loadInterProModel(String databaseName, String accession, String name, String description) throws Exception{
		
		if (name != null) {

			int size = 0;
			size = name.length();

			if (size > 250)
				size = 249;

			name = name.substring(0, size);
		}
		
		if (description != null) {

			int size = description.length();

			if (size > 250)
				size = 249;

			description = description.substring(0, size);
		}
		
		List<String> intermediateRes = InitDataAccess.getInstance().getDatabaseService(databaseName).getInterproModelAccessionByAccession(accession);
		
		if (intermediateRes == null) {
			
			InitDataAccess.getInstance().getDatabaseService(databaseName).insertInterproModelData(accession, description, name);
		}
		else {
			logger.warn("Entry exists {}", accession);
		}
		
		return accession;
	}
	
	
	public static void loadInterProResultHasModel(String databaseName, int resultID, String modelAccession) throws Exception{
		
		InitDataAccess.getInstance().getDatabaseService(databaseName).loadInterProResultHasModel(resultID, modelAccession);
	}
	
	
	public static void loadInterProResultHasEntry(String databaseName, int resultID, int entryID) throws Exception{
		
		List<InterproResultHasEntry> intermediateRes = InitDataAccess.getInstance().getDatabaseService(databaseName).getAllInterproResultHasEntryByResultIdAndEntryID(resultID, entryID);
		
		if (intermediateRes.size() == 0) {
			InitDataAccess.getInstance().getDatabaseService(databaseName).insertInterproResultHasEntry(resultID, entryID);
		}
		
	}
	
	
	public static void setInterProStatus(String databaseName, Integer id, String status) throws Exception{
		
		InitDataAccess.getInstance().getDatabaseService(databaseName).setInterProStatus(id, status);
	}
	
	public static void deleteInterProEntries(String databaseName, String status) throws Exception{
		
		InitDataAccess.getInstance().getDatabaseService(databaseName).deleteInterProEntries(status);
	}
	
	public static List<String> getInterProGenes(String databaseName, String status) throws Exception{
		
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getInterProGenes(status);
	}
	
	
	
	
	
}
