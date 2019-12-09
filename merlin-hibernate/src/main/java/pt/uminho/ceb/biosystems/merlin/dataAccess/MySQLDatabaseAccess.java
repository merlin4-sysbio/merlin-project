package pt.uminho.ceb.biosystems.merlin.dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * @author Oscar Dias
 *
 */
public class MySQLDatabaseAccess implements IDatabaseAccess{


	private String databaseHost, databasePort, databaseName, databaseUser, databasePassword;


	public MySQLDatabaseAccess() {
	}

	public MySQLDatabaseAccess(String user, String password, String server, String port) {

		this.databaseHost=server;
		this.databasePort=port+"";
		this.databaseUser=user;
		this.databasePassword=password;

	}

	/**
	 * @param user
	 * @param password
	 * @param server
	 * @param port
	 * @param database
	 */
	public MySQLDatabaseAccess(String user, String password, String server, String port, String database) {

		this.databaseHost=server;
		this.databasePort=port+"";
		this.databaseName=database;
		this.databaseUser=user;
		this.databasePassword=password;

	}

	/**
	 * @param user
	 * @param password
	 * @param server
	 * @param port
	 * @param database
	 */
	public MySQLDatabaseAccess(String user, String password, String server, int port, String database) {

		this(user, password, server, port+"", database);
	}

	@Override
	public String getDatabaseHost() {
		return databaseHost;
	}

	@Override
	public String getDatabasePort() {
		return databasePort;
	}

	@Override
	public String getDatabaseName() {
		return databaseName;
	}

	@Override
	public String getDatabasePassword() {
		return databasePassword;
	}

	@Override
	public String getDatabaseUser() {
		return databaseUser;
	}

	@Override
	public DatabaseTypeEnum getDatabaseType() {
		return DatabaseTypeEnum.MYSQL;
	}

	/**
	 * @param database_host the database_host to set
	 */
	@Override
	public void setDatabaseHost(String database_host) {
		this.databaseHost = database_host;
	}

	/**
	 * @param database_port the database_port to set
	 */
	@Override
	public void setDatabasePort(String database_port) {
		this.databasePort = database_port;
	}

	/**
	 * @param database_name the database_name to set
	 */
	@Override
	public void setDatabaseName(String database_name) {
		this.databaseName = database_name;
	}

	/**
	 * @param database_user the database_user to set
	 */
	@Override
	public void setDatabaseUser(String database_user) {
		this.databaseUser = database_user;
	}

	/**
	 * @param database_password the database_password to set
	 */
	@Override
	public void setDatabasePassword(String database_password) {
		this.databasePassword = database_password;
	}

	@Override
	public String getDriverClassName() {
		return "com.mysql.jdbc.Driver";
	}

	@Override
	public String getDialectClassName() {
		//			return "org.hibernate.dialect.MySQLDialect";
		//			return "org.hibernate.dialect.MySQLMyISAMDialect";
		//			return "org.hibernate.dialect.MySQL5InnoDBDialect";
		//			return "org.hibernate.dialect.MySQLInnoDBDialect";
		//					return "org.hibernate.dialect.MySQL57InnoDBDialect";
		//		return "org.hibernate.dialect.MySQL5Dialect";
		return "org.hibernate.dialect.MySQL57Dialect";
	}

	@Override
	public String getURLConnection() {
		return "jdbc:mysql://" + this.getDatabaseHost() + ":" + this.getDatabasePort() + "/" + this.getDatabaseName() + "?createDatabaseIfNotExist=true&useSSL=false";
	}

	@Override
	public List<String> getExistentDatabases() throws Exception {
		List<String> schemasList = new ArrayList<>();
		
		try {
			
			Properties props = new Properties();
			props.setProperty("user", this.databaseUser);
			props.setProperty("password", this.databasePassword);
			props.setProperty("useSSL","false");
			
			Connection connection = DriverManager.getConnection("jdbc:mysql://" + this.getDatabaseHost() + ":" + this.getDatabasePort(), props);
			

			List<String> tablesCheck = new ArrayList<String>();
			tablesCheck.add("enzymes_annotation_genehomology");

			Statement statement = connection.createStatement();
			statement.execute("SELECT TABLE_SCHEMA, TABLE_NAME FROM INFORMATION_SCHEMA.TABLES;");
			ResultSet rs = statement.getResultSet();

			while(rs.next()) {
				if(tablesCheck.contains(rs.getString(2).toLowerCase()))
					schemasList.add(rs.getString(1));
			}

			statement.close();
			connection.close();
		} 
		catch (SQLException e) {
			throw new Exception(e);
		}
		return schemasList;
	}

	@Override
	public void executeDropTable(List<String> tablesNames) throws Exception {
		try {

			Connection connection = DriverManager.getConnection("jdbc:mysql://" + this.getDatabaseHost() + ":" + this.getDatabasePort()+ "/" + this.getDatabaseName(), this.databaseUser, this.databasePassword);

			Statement statement = connection.createStatement();

			for(String table : tablesNames) {
				System.out.println("table : " + table);
				try {
					statement.execute("DROP TABLE " + table + ";");
				} 
				catch (MySQLSyntaxErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			statement.close();
			connection.close();
		} 
		catch (SQLException e) {
			throw new Exception(e);
		}
	}

	@Override
	public void dropDatabase() throws Exception {
		try {

			Connection connection = DriverManager.getConnection("jdbc:mysql://" + this.getDatabaseHost() + ":" + this.getDatabasePort()+ "/" + this.getDatabaseName(), this.databaseUser, this.databasePassword);

			Statement statement = connection.createStatement();

			statement.execute("DROP SCHEMA " +this.databaseName + ";"); 

			statement.close();
			connection.close();
		} 
		catch (SQLException e) {
			throw new Exception(e);
		}

	}


}
