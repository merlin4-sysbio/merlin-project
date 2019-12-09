package pt.uminho.ceb.biosystems.merlin.gui.datatypes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Properties;

import es.uvigo.ei.aibench.core.datatypes.annotation.Clipboard;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;

@Datatype(structure = Structure.SIMPLE)
public class WorkspaceConfigurationFileAIB extends Observable {

	private String confFile;
	private String errorLog;
	private String gadgetLog;
	private String databaseUser; 
	private String databasePWD;
	private String databaseHost;
	private String databasePort;
	private String databaseName;
	
	public WorkspaceConfigurationFileAIB(String file)
	{
		this.confFile = file;
		
		Properties p = new Properties();
		try{
			p.loadFromXML(new FileInputStream(this.confFile));
			
			
			this.errorLog = p.getProperty("ErrorLog");
			this.gadgetLog = p.getProperty("GadgetLog");
			this.databaseUser = p.getProperty("DatabaseUser");
			this.databasePWD = p.getProperty("DatabasePWD");
			this.databaseHost = p.getProperty("DatabaseHost");
			this.databasePort = p.getProperty("DatabasePort");
			this.databaseName = p.getProperty("DatabaseName");
			
			
		} catch (FileNotFoundException e) {
			System.out.println("While loading configuration file: "+e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("While loading configuration file: "+e);
			e.printStackTrace();
		} 
		
	}
	
	public String getConfFile() {
		return confFile;
	}
	
	public void setConfFile(String confFile) {
		this.confFile = confFile;
	}
	
	@Clipboard(name="Host")
	public String getDatabaseHost() {
		return databaseHost;
	}
	
	public void setDatabaseHost(String databaseHost) {
		this.databaseHost = databaseHost;
	}
	
	@Clipboard(name="Port")
	public String getDatabasePort() {
		return databasePort;
	}
	
	
	public void setDatabasePort(String databasePort) {
		this.databasePort = databasePort;
	}
	
	@Clipboard(name="Password")
	public String getDatabasePWD() {
		return databasePWD;
	}
	
	public void setDatabasePWD(String databasePWD) {
		this.databasePWD = databasePWD;
	}
	
	@Clipboard(name="Database user")
	public String getDatabaseUser() {
		return databaseUser;
	}
	
	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}
	
	@Clipboard(name="Error log path")
	public String getErrorLog() {
		return errorLog;
	}
	
	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}
	
	@Clipboard(name="Log path")
	public String getGadgetLog() {
		return gadgetLog;
	}
	
	public void setGadgetLog(String gadgetLog) {
		this.gadgetLog = gadgetLog;
	}

	@Clipboard(name="Database Name")
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
}
