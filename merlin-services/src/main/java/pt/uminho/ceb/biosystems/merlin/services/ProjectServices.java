package pt.uminho.ceb.biosystems.merlin.services;

import java.io.IOException;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelSequenceServices;

/**
 * @author odias
 *
 */
public class ProjectServices {


	/**
	 * @return
	 * @throws Exception 
	 */
	public static boolean isTransporterLoaded(String databaseName) throws Exception {
		boolean out = false;
		out = InitDataAccess.getInstance().getDatabaseService(databaseName).isTransporterLoaded();
		return out;
	}

	/**
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static boolean isGeneDataAvailable(String databaseName) throws IOException, Exception {

		boolean geneDataAvailable = false;
		geneDataAvailable = ModelGenesServices.existGenes(databaseName);
		return geneDataAvailable;
	}

	/**
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static boolean isMetabolicDataAvailable(String databaseName) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).isMetabolicDataLoaded();

	}


	/**
	 * @return
	 * @throws Exception 
	 */
	public static boolean isCompartmentalisedModel(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).isCompartimentalizedModel();

	}

	/**
	 * @param databaseName
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static boolean genomeFilesLoaded(String databaseName) throws Exception {

		boolean genomeFilesLoaded = false;

		if(!ModelGenesServices.getGeneIds(databaseName).isEmpty() || ModelSequenceServices.checkGenomeSequences(databaseName, SequenceType.CDS_DNA)
				|| ModelSequenceServices.checkGenomeSequences(databaseName, SequenceType.PROTEIN))
			genomeFilesLoaded = true;

		return genomeFilesLoaded;
	}


	/**
	 * @return the project_id
	 * @throws Exception 
	 */
	public int getProjectID(String databaseName,Long taxonomyID) throws Exception {
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProjectID(taxonomyID);
	}


	/**
	 * Update organismID.
	 * @param orgID
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static void updateOrganismID(String databaseName, Long taxonomyID) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateOrganismID(taxonomyID);

	}

	/**
	 * Method to retrieve information about the organism from the table projects
	 * 
	 * @param taxonomyID
	 * @return
	 * @throws Exception 
	 * @ 
	 */
	public static String[] getOrganismData(String databaseName, long taxonomyID) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getOrganismData(taxonomyID);

	}

	/**
	 * Update organism name and lineage in the table projects
	 * 
	 * @param taxonomyID
	 * @param data
	 * @param statement
	 * @throws Exception 
	 * @throws  
	 * @
	 */
	public static void updateOrganismData(String databaseName, long taxonomyID, String[] data) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateOrganismData(taxonomyID, data);

	}

	/**
	 * Get organismID.
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static Integer getOrganismID(String databaseName) throws IOException, Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getOrganismID();

	}

	
	/**
	 * Get organismID.
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static boolean checkInternalIdFromDblinks(String databaseName, String cl, int internal, String database) throws IOException, Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkInternalIdFromDblinks(cl, internal, database);

	}
	
	/**
	 * @param databaseName
	 * @param taxID
	 * @return
	 * @throws Exception
	 */
	public static String getCompartmentsTool(String databaseName, Long taxID) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartmentsTool(Integer.valueOf(taxID.toString()));

	}
	
	public static void updateCompartmentsTool(String databaseName, Long organismId, String tool) throws Exception {
		InitDataAccess.getInstance().getDatabaseService(databaseName).updateCompartmentsTool(organismId, tool);
	}
	
	public static void updateProjectsByGenomeID(String databaseName, Long genomeID, Map<String, String> documentSummaryMap) throws Exception {
		
		InitDataAccess.getInstance().getDatabaseService(databaseName).updateProjectsByGenomeID(genomeID, documentSummaryMap);
	}
}
