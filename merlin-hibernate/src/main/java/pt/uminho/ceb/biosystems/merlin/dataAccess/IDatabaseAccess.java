package pt.uminho.ceb.biosystems.merlin.dataAccess;

import java.util.List;

public interface IDatabaseAccess {
	
	public String getDriverClassName();
	
	public String getDialectClassName();

	public String getDatabaseHost();

	public String getDatabasePort();

	public String getDatabaseName();

	public String getDatabasePassword();

	public String getDatabaseUser();
	
	public String getURLConnection();
	
	public DatabaseTypeEnum getDatabaseType();

	/**
	 * @param database_host the database_host to set
	 */
	public void setDatabaseHost(String database_host);

	/**
	 * @param database_port the database_port to set
	 */
	public void setDatabasePort(String database_port);

	/**
	 * @param database_user the database_user to set
	 */
	public void setDatabaseUser(String database_user);

	/**
	 * @param database_password the database_password to set
	 */
	public void setDatabasePassword(String database_password);

	public void setDatabaseName(String database_name);

	public List<String> getExistentDatabases() throws Exception;

	public void executeDropTable(List<String> tablesNames) throws Exception;

	public void dropDatabase() throws Exception;

}