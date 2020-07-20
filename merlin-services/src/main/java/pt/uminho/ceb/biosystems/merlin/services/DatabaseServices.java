package pt.uminho.ceb.biosystems.merlin.services;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.hibernate.tool.schema.spi.CommandAcceptanceException;

import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;

public class DatabaseServices {

	/**
	 * @param databaseName
	 * @throws Exception
	 */
	public static void generateDatabase(String databaseName) {
		try {
			InitDataAccess.getInstance().generateDatabase(databaseName);
		} catch (CommandAcceptanceException e) {
			// TODO Auto-generated catch block		//usually drop before creating
//			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @return 
	 * @throws Exception
	 */
	public static List<String> getDatabasesAvailable() throws Exception {
		return InitDataAccess.getInstance().getDatabasesAvailable();
	}
	
	/**
	 * @param databaseName
	 * @param outputDirectory
	 * @param l
	 * @throws Exception
	 */
	public static void databaseToXML(String databaseName, String outputDirectory, PropertyChangeListener l) throws Exception {
		
		if(l != null)
			InitDataAccess.getInstance().getDatabaseExporterBatchService(databaseName).addPropertyChangeListener(l);
		InitDataAccess.getInstance().getDatabaseExporterBatchService(databaseName).dbtoXML(outputDirectory);
		
		dropExporterConnection(databaseName);
	}
	
	/**
	 * @param databaseName
	 * @param xmlDirectory
	 * @param cancel
	 * @param l
	 * @throws Exception
	 */
	public static void readxmldb(String databaseName, String xmlDirectory, AtomicBoolean cancel, PropertyChangeListener l) throws Exception {
		
		if(l != null)
			InitDataAccess.getInstance().getDatabaseExporterBatchService(databaseName).addPropertyChangeListener(l);
		InitDataAccess.getInstance().getDatabaseExporterBatchService(databaseName).readxmldb(xmlDirectory, cancel);
		
		dropExporterConnection(databaseName);
	}
	
	/**
	 * @param databaseName
	 * @throws Exception
	 */
	public static void dropExporterConnection(String databaseName) throws Exception {
		InitDataAccess.getInstance().dropExporterConnection(databaseName);
	}
	
	/**
	 * @param databaseName
	 * @throws Exception
	 */
	public static void dropConnection(String databaseName) throws Exception {
		InitDataAccess.getInstance().dropConnection(databaseName);
	}
	
	/**
	 * @param databaseName
	 * @param tablesNames
	 * @throws Exception
	 */
	public static void executeDropTable(String databaseName, List<String> tablesNames) throws Exception {
		InitDataAccess.getInstance().executeDropTable(databaseName, tablesNames);
	}
	
	/**
	 * @param databaseName
	 * @throws Exception
	 */
	public static void getDatabaseService(String databaseName) throws Exception {
		InitDataAccess.getInstance().getDatabaseService(databaseName, false);
	}
	
	/**
	 * @param databaseName
	 * @throws Exception
	 */
	public static void dropDatabase(String databaseName) throws Exception {
		InitDataAccess.getInstance().dropDatabase(databaseName);
	}
	
	/**
	 * @param databaseName
	 * @param cancel
	 */
	public static void setCancelExporterBatch(String databaseName, boolean cancel){
		InitDataAccess.getInstance().getDatabaseExporterBatchService(databaseName).setCancel(cancel);
	}

	/**
	 * @param databaseName
	 * @throws Exception
	 */
	public static void getValidationDatabaseService(String databaseName) throws Exception {
		InitDataAccess.getInstance().getDatabaseService(databaseName, true);
	}
}
