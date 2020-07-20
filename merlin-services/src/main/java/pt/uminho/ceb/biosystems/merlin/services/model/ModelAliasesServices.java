package pt.uminho.ceb.biosystems.merlin.services.model;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;

public class ModelAliasesServices {

	/**
	 * @param encoded
	 * @return
	 * @throws Exception 
	 */
	public static boolean checkEntityFromAliases(String databaseName, String cl, int entity, String alias) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkEntityFromAliases(cl, entity, alias);
	}

	/**
	 * @param databaseName
	 * @param cl
	 * @param entity
	 * @param alias
	 * @throws Exception
	 */
	public static void insertNewModelAliasEntry(String databaseName, String cl, int entity, String alias) throws Exception {
		InitDataAccess.getInstance().getDatabaseService(databaseName).insertNewModelAliasEntry(cl, entity, alias);

	}

	/**
	 * Load BATCH_SIZE aliases
	 * 
	 * @param metabolites
	 * @param statement
	 * @param 
	 * @throws Exception 
	 */
	public static void  loadALiases(String databaseName, ConcurrentLinkedQueue<MetaboliteContainer> metabolites) throws Exception {

		for (MetaboliteContainer metaboliteContainer : metabolites) {

			if(metaboliteContainer.getNames()!=null)	{

				for(String synonym:metaboliteContainer.getNames()) {

					synonym = synonym.replace(";", "");

					boolean exists = ModelAliasesServices.checkEntityFromAliases(databaseName, "c", metaboliteContainer.getMetaboliteID(), synonym);

					if(!exists) {

						insertNewModelAliasEntry(databaseName, "c", metaboliteContainer.getMetaboliteID(), synonym);

					}

				}
			}
		}
	}
	
	/**
	 * @param databaseName
	 * @param class_
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static List<String> getSynonyms(String databaseName, String class_, Integer entity) throws Exception{
		
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getSynonyms(class_, entity);
	}
}
