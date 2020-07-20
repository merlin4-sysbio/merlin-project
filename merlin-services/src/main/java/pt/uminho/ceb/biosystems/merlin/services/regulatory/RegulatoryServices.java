package pt.uminho.ceb.biosystems.merlin.services.regulatory;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;

public class RegulatoryServices {


	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static double[] getStats(String databaseName) throws Exception {

		double[] ret = new double[9];

		try {


		}
		catch (Exception ex) {

			ex.printStackTrace();
		}
		return ret;
	}


	/**
	 * @param databaseName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static List<String> getRowInfo(String databaseName, int id) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAliasClassG(id);
		
	}
	
	public Integer insertTranscriptionUnitName(String databaseName, String gene) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertTranscriptionUnitName(gene);
		
	}
	
	public boolean checkTranscriptionUnitNameExists(String databaseName, String name) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkTranscriptionUnitNameExists(name);
		
	}
	
	


}
