/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.services;

import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceInitialData;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelMetabolitesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelModuleServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelPathwaysServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;

/**
 * @author ODias
 *
 */
public class WorkspaceInitialDataServices {

	/**
	 * Retrieves all data from database and stores it in the instance Maps
	 * 
	 * @throws SQLException
	 */
	public static WorkspaceInitialData retrieveAllData(String databaseName) {

		WorkspaceInitialData workspaceInitialData = new WorkspaceInitialData();

		try {

			workspaceInitialData.setGenesIdentifier(new ConcurrentHashMap<>(ModelGenesServices.getGeneLocusTagAndIdgene(databaseName)));
			workspaceInitialData.setProteinsIdentifier(new ConcurrentHashMap<>(ModelProteinsServices.getEnzymeEcNumberAndProteinID(databaseName)));
			workspaceInitialData.setPathwaysIdentifier(new ConcurrentHashMap<>(ModelPathwaysServices.getPathwayCodeAndIdpathway(databaseName)));
			workspaceInitialData.setModulesIdentifier(new ConcurrentHashMap<>(ModelModuleServices.getModelModuleEntryIdAndId(databaseName)));
			workspaceInitialData.setMetabolitesIdentifier(new ConcurrentHashMap<>(ModelMetabolitesServices.getExternalIdentifierAndIdCompound(databaseName)));

			workspaceInitialData.setReactionsIdentifier(new ConcurrentHashMap<>(ModelReactionsServices.getModelReactionIdsRelatedToNames(databaseName)));
			workspaceInitialData.setOrthologuesIdentifier(new ConcurrentHashMap<>(ModelGenesServices.getEntryIdAndOrthologyId(databaseName)));

			workspaceInitialData.setReactionsPathway(new ConcurrentHashMap<Integer,Set<String>>());
			workspaceInitialData.setEnzymesPathway(new ConcurrentHashMap<String,Set<String>>());
			workspaceInitialData.setMetabolitesPathway(new ConcurrentHashMap<Integer,Set<String>>());
			workspaceInitialData.setModulesPathway(new ConcurrentHashMap<Integer,Set<String>>());
			workspaceInitialData.setEnzymesInModel(new ConcurrentLinkedQueue<String>());
			workspaceInitialData.setReactionsPathwayList(new ConcurrentLinkedQueue<Integer>());
			workspaceInitialData.setEnzymesPathwayList(new ConcurrentLinkedQueue<String>());
			workspaceInitialData.setMetabolitesPathwayList(new ConcurrentLinkedQueue<Integer>());
			workspaceInitialData.setModulesPathwayList(new ConcurrentLinkedQueue<Integer>());
			workspaceInitialData.setSimilarMetabolitesLoad(new ConcurrentHashMap<String, Integer>());

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return workspaceInitialData;
	}


	/**
	 * Retrieves annotation data from database and stores it in the instance Maps
	 * 
	 * @throws SQLException
	 */
	public static WorkspaceInitialData retrieveAnnotationData(String databaseName) {

		WorkspaceInitialData workspaceInitialData = new WorkspaceInitialData();

		try {

			workspaceInitialData.setGenesIdentifier(new ConcurrentHashMap<>(ModelGenesServices.getGeneLocusTagAndIdgene(databaseName)));
			workspaceInitialData.setProteinsIdentifier(new ConcurrentHashMap<>(ModelProteinsServices.getEnzymeEcNumberAndProteinID(databaseName)));
			workspaceInitialData.setOrthologuesIdentifier(new ConcurrentHashMap<>(ModelGenesServices.getEntryIdAndOrthologyId(databaseName)));
			workspaceInitialData.setEnzymesReactions(new ConcurrentHashMap<>(ModelReactionsServices.getEcNumbersByReactionId(databaseName)));
			workspaceInitialData.setEnzymesInModel(new ConcurrentLinkedQueue<String>());
			workspaceInitialData.setEnzymesPathway(new ConcurrentHashMap<>());
			workspaceInitialData.setEnzymesPathwayList(new ConcurrentLinkedQueue<>());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workspaceInitialData;
	}
}