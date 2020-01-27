package pt.uminho.ceb.biosystems.merlin.services.model;

import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;

public class ModelDBLinksServices {

	/**
	 * @param databaseName
	 * @param class_
	 * @param internalId
	 * @param database
	 * @param externalId
	 * @throws Exception
	 */
	public static void insertEntry(String databaseName, String class_, Integer internalId, String database, String externalId) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertDbLinksEntry(class_, internalId, database, externalId);
	}
	
	
}
