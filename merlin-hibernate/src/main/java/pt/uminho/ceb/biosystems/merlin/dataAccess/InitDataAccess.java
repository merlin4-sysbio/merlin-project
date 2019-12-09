package pt.uminho.ceb.biosystems.merlin.dataAccess;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import pt.uminho.ceb.biosystems.merlin.services.implementation.DatabaseServiceEntityExporterBatch;
import pt.uminho.ceb.biosystems.merlin.services.implementation.DatabaseServiceImpl;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IDatabaseService;
import pt.uminho.ceb.biosystems.merlin.utilities.io.ConfFileReader;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

public class InitDataAccess {
	
	private static InitDataAccess _instance = null;
	private String hibernateXMLPath = "hibernate.cfg.xml";
	private Map<String, IDatabaseService> databases;
	
	// TODO: create a method that sets xml. If folder confg not found, uses resources folder as now.
	// otherwise, get hibernate xml file from conf folder and uses variables of database
	
	public static synchronized InitDataAccess getInstance(){
		if (_instance == null) 
			InitDataAccess.createInstance();
		
		return _instance;
	}
	
	private static void createInstance() {
		if(_instance==null)
			_instance = new InitDataAccess();
	}
	
	private InitDataAccess() {
		if(databases == null)
			databases = new HashMap<>();
	}
	
	public IDatabaseService getDatabaseService(String databaseName) throws Exception {
		if(databaseName != null && !databases.containsKey(databaseName)) {
			IDatabaseAccess databaseacess = readDatabaseConfigurations();
			databaseacess.setDatabaseName(databaseName);
			IDatabaseService service = startDatabaseService(databaseacess, false, false, false);
			databases.put(databaseName, service);
		}
			
		return databases.get(databaseName);
	}
	
	public IDatabaseService getDatabaseService(String databaseName, boolean validateOnly) throws Exception {
		if(databaseName != null && !databases.containsKey(databaseName)) {
			IDatabaseAccess databaseacess = readDatabaseConfigurations();
			databaseacess.setDatabaseName(databaseName);
			IDatabaseService service = startDatabaseService(databaseacess, false, false, validateOnly);
			databases.put(databaseName, service);
		}
			
		return databases.get(databaseName);
	}
	
	public void dropConnection(String databaseName) {
		if(databases.containsKey(databaseName)) {
			IDatabaseService serviceToRemove = databases.remove(databaseName);
			serviceToRemove.closeJDBCConnection();
		}
			
	}
	
	public void dropExporterConnection(String databaseName) {
		
		if(databases.containsKey("e".concat(databaseName))) {
			IDatabaseService serviceToRemove = databases.remove("e".concat(databaseName));
			serviceToRemove.closeJDBCConnection();
		}
			
	}
	
    private File readHibernateConfig() {
    	String confPath = FileUtils.getConfFolderPath();
    	File xmlFile = null;
    	if(confPath != null && !confPath.isEmpty()) {
    		xmlFile = new File(confPath+hibernateXMLPath);
    	}else {
    		xmlFile = new File(this.getClass().getClassLoader().getResource(hibernateXMLPath).getFile());
    	}
    	
    	return xmlFile;
    }
    
    public void generateDatabase(String databaseName) throws Exception{
    	IDatabaseAccess databaseacess = readDatabaseConfigurations();
		databaseacess.setDatabaseName(databaseName);
		IDatabaseService service = startDatabaseService(databaseacess, true, false, false);
		databases.put(databaseName, service);
		dropConnection(databaseName);
    }
    
    private IDatabaseService startDatabaseService(IDatabaseAccess credentials, Boolean createDrop, boolean exporter, boolean validateOnly) throws JAXBException {

    	Configuration configuration = new Configuration().configure(readHibernateConfig());
    	configuration.setProperty("hibernate.connection.driver_class", credentials.getDriverClassName());
		configuration.setProperty("hibernate.dialect", credentials.getDialectClassName());
		configuration.setProperty("hibernate.connection.url", credentials.getURLConnection());
		configuration.setProperty("hibernate.connection.username", credentials.getDatabaseUser());
		configuration.setProperty("hibernate.connection.password", credentials.getDatabasePassword());
//		configuration.setProperty("hibernate.default_catalog", credentials.getDatabaseName());
		configuration.setProperty("hibernate.default_schema", credentials.getDatabaseName());
		
		if(createDrop)
			configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");				//TRANSFORM THIS TO USE ENUMERATORS AND SWITCH CASE. NO TIME TO DO IT NOW!
		else if(validateOnly)
			configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
		else
			configuration.setProperty("hibernate.hbm2ddl.auto", "update");
		
		configuration.setProperty("hibernate.dialect.storage_engine", "innodb");
		configuration.setProperty("hibernate.jdbc.batch_size", "500");
		
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure(readHibernateConfig()).applySettings(configuration.getProperties()).build();
		
		if(exporter)
			return new DatabaseServiceEntityExporterBatch(registry);
		
		return new DatabaseServiceImpl(registry);
    }
    
    
    private IDatabaseAccess readDatabaseConfigurations() throws IOException{
    	Map<String, String> settings = ConfFileReader.loadConf(FileUtils.getConfFolderPath()+"/database_settings.conf");
		String dbType = settings.get("dbtype");
		if(dbType.equals("mysql")) 
			return new MySQLDatabaseAccess(settings.get("username"), settings.get("password"), settings.get("host"), settings.get("port"));
		else if(dbType.equals("h2"))
			return new H2DatabaseAccess(settings.get("h2_username"), settings.get("h2_password"));
		return null;
    }
    
    public List<String> getDatabasesAvailable() throws Exception{
    	Map<String, String> settings = ConfFileReader.loadConf(FileUtils.getConfFolderPath()+"/database_settings.conf");
		String dbType = settings.get("dbtype");
		if(dbType.equals("mysql")) 
			return new MySQLDatabaseAccess(settings.get("username"), settings.get("password"), settings.get("host"), settings.get("port")).getExistentDatabases();
		else if(dbType.equals("h2"))
			return new H2DatabaseAccess(settings.get("h2_username"), settings.get("h2_password")).getExistentDatabases();
		return null;
    }
    
    public void executeDropTable(String databaseName, List<String> tablesNames) throws Exception{
    	Map<String, String> settings = ConfFileReader.loadConf(FileUtils.getConfFolderPath()+"/database_settings.conf");
		String dbType = settings.get("dbtype");
		if(dbType.equals("mysql")) 
			new MySQLDatabaseAccess(settings.get("username"), settings.get("password"), settings.get("host"), settings.get("port"), databaseName).executeDropTable(tablesNames);
		else if(dbType.equals("h2"))
			new H2DatabaseAccess(settings.get("h2_username"), settings.get("h2_password"), databaseName).executeDropTable(tablesNames);
    }
    
    public void dropDatabase(String databaseName) throws Exception{
    	Map<String, String> settings = ConfFileReader.loadConf(FileUtils.getConfFolderPath()+"/database_settings.conf");
		String dbType = settings.get("dbtype");
		if(dbType.equals("mysql")) 
			new MySQLDatabaseAccess(settings.get("username"), settings.get("password"), settings.get("host"), settings.get("port"), databaseName).dropDatabase();
		else if(dbType.equals("h2"))
			new H2DatabaseAccess(settings.get("h2_username"), settings.get("h2_password"), databaseName).dropDatabase();
    }

	public DatabaseServiceEntityExporterBatch getDatabaseExporterBatchService(String databaseName) {
		
		DatabaseServiceEntityExporterBatch service = null;
		try {
			if(databaseName != null && !databases.containsKey("e".concat(databaseName))) {
				IDatabaseAccess databaseacess = readDatabaseConfigurations();
				databaseacess.setDatabaseName(databaseName);
				service = (DatabaseServiceEntityExporterBatch) startDatabaseService(databaseacess, false, true, false);
				databases.put("e".concat(databaseName), service);
			}
			else {
				service = (DatabaseServiceEntityExporterBatch)  databases.get("e".concat(databaseName));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return service;
	}
	
}

