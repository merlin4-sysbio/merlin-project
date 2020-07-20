package pt.uminho.ceb.biosystems.merlin.services.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;

public class AnnotationCompartmentsServices {

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static boolean areCompartmentsPredicted(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).areCompartmentsPredicted();

	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static long getNumberOfGenesInCompartmentReports(String databaseName) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).countCompartmentsAnnotationReports();
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public static List<Integer> getStats(String databaseName) throws Exception {

		ArrayList<Integer> result = new ArrayList<>();

		int num = (int) getNumberOfGenesInCompartmentReports(databaseName);

		int num_comp = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).getNumberOfCompartments();

		result.add(num);
		result.add(num_comp);

		return result;
	}

	/**
	 * @param databaseName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> getCompartmentsAnnotationReportsHasCompartmentsNameAndScore(String databaseName, int id) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartmentsAnnotationReportsHasCompartmentsNameAndScore(id);

	}
	
	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getNameAndAbbreviation(String databaseName) throws Exception {
		
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getNameAndAbbreviation();

	}
	
	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<CompartmentContainer> getBestCompartmenForGene(String databaseName) throws Exception {
		
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getBestCompartmenForGene();

	}
	
	/**
	 * @param databaseName
	 * @param abb
	 * @return
	 * @throws Exception
	 */
	public static CompartmentContainer getAnnotationCompartmentByAbbreviation(String databaseName, String abb) throws Exception {
		
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAnnotationCompartmentByAbbreviation(abb);

	}
	
	/**
	 * @param databaseName
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static CompartmentContainer getAnnotationCompartmentByName(String databaseName, String name) throws Exception {
		
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAnnotationCompartmentByAbbreviation(name);

	}
	
	/**
	 * @param databaseName
	 * @param geneID
	 * @param name
	 * @param abbreviation
	 * @param compartmentScore
	 * @throws Exception
	 */
	public static void insertIntoCompartments(String databaseName, Integer geneID, String name, String abbreviation, Double compartmentScore) throws Exception{
		
		CompartmentContainer res = getAnnotationCompartmentByAbbreviation(databaseName, abbreviation);
		
		Integer compartmentId = null;
		
		if(res == null)
			compartmentId = InitDataAccess.getInstance().getDatabaseService(databaseName).insertAnnotationCompartmentNameAndAbbreviation(name, abbreviation);
		else
			compartmentId = res.getCompartmentID();
		
		boolean exists = InitDataAccess.getInstance().getDatabaseService(databaseName).checkCompartmentsReportsHasCompartmentEntry(geneID, compartmentId);
		
		if(!exists)
			InitDataAccess.getInstance().getDatabaseService(databaseName).insertCompartmentsReportsHasCompartmentEntry(geneID, compartmentId, compartmentScore);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, List<CompartmentContainer>> getReportsByGene(String databaseName) throws Exception {
		
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getReportsByGene();

	}
}
