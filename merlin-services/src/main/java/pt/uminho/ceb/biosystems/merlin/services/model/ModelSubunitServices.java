package pt.uminho.ceb.biosystems.merlin.services.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;

public class ModelSubunitServices {


	/**
	 * @param databaseName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static List<ProteinContainer> getModelSubunitAttributes(String databaseName, Integer id) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getDataFromSubunit(id);
	}

	/**
	 * @param databaseName
	 * @param proteinId
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> getGPRstatusAndReactionAndDefinition(String databaseName, Integer proteinId) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGPRstatusAndReactionAndDefinition(proteinId);
	}

	/**
	 * @param databaseName
	 * @param geneId
	 * @param protId
	 * @throws Exception
	 */
	public static void removeSubunitByGeneIdAndProteinId(String databaseName, Integer geneId, Integer protId) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).removeSubunitByGeneIdAndProteinId(geneId, protId);
	}

	/**
	 * @param databaseName
	 * @param geneId
	 * @param protId
	 * @return 
	 * @throws Exception
	 */
	public static Map<Integer, String> getModuleIdAndNoteByGeneIdAndProteinId(String databaseName, Integer geneId, Integer protId) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModuleIdAndNoteByGeneIdAndProteinId(geneId, protId);
	}

	/**
	 * @param databaseName
	 * @param geneId
	 * @param protId
	 * @param moduleId
	 * @param note
	 * @param gprStatus
	 * @throws Exception
	 */
	public static void insertModelSubunit(String databaseName, Integer geneId, Integer protId, String note, String gprStatus) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelSubunit(geneId, protId, note, gprStatus);
	}
	
	/**
	 * @param databaseName
	 * @param geneId
	 * @param protId
	 * @throws Exception
	 */
	public static void insertModelSubunit(String databaseName, Integer geneId, Integer protId) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelSubunit(geneId, protId, null, null);
	}

	/**
	 * @param databaseName
	 * @param geneId
	 * @param protId
	 * @return
	 * @throws Exception
	 */
	public static boolean checkModelSubunitEntry(String databaseName, Integer geneId, Integer protId) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkModelSubunitEntry(geneId, protId);
	}
	
	

	/**
	 * @param newDbStmt
	 * @param refDbStmt
	 * @param oldGeneID
	 * @param newGeneID
	 * @throws SQLException
	 */
	public static void transferEntryIfAbsentToSubunit(String newDbName, String refDbName, Integer oldGeneID, Integer newGeneID) throws Exception{

		List<ProteinContainer> oldProteins = ModelProteinsServices.getProteinIdByIdGene(refDbName, oldGeneID);

		for(ProteinContainer oldProtein : oldProteins){

			String ecNumber = oldProtein.getExternalIdentifier();

			ProteinContainer newProtein = ModelProteinsServices.getProteinByEcNumber(newDbName, ecNumber);

			if(newProtein != null) {

				boolean exists = checkModelSubunitEntry(newDbName, newGeneID, newProtein.getIdProtein());

				if(!exists){

					insertModelSubunit(newDbName, newGeneID, newProtein.getIdProtein(), null, null);
				}
			}
		}
	}
	
	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static int countSubunitEntries(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).countSubunitEntries().intValue();
	}
	
	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static boolean isProteinEncodedByGenes(String databaseName, Integer proteinId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).isProteinEncodedByGenes(proteinId);
	}
}
