package pt.uminho.ceb.biosystems.merlin.dataAccess;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

/**
 *
 */
public class H2DatabaseAccess implements IDatabaseAccess{

	private String databaseName, databaseUser, databasePassword, databaseHost;

	/**
	 * 
	 */
	public H2DatabaseAccess() {
		super();
	}

	public H2DatabaseAccess(String databaseUser, String password){
		this.databaseUser=databaseUser;
		this.databasePassword=password;
		this.databaseHost=FileUtils.getHomeFolderPath();
	}

	/**
	 * @param user
	 * @param password
	 * @param database_name
	 */
	public H2DatabaseAccess(String databaseUser, String password, String databaseName){

		this.databaseName=databaseName;
		this.databaseUser=databaseUser;
		this.databasePassword=password;
		this.databaseHost=FileUtils.getHomeFolderPath();
	}

	/**
	 * @param user
	 * @param password
	 * @param database_name
	 */
	public H2DatabaseAccess(String user, String password, String database_name, String database_path){

		this.databaseName=database_name;
		this.databaseUser=user;
		this.databasePassword=password;

		if(database_path==null)
			this.databaseHost=FileUtils.getHomeFolderPath();
		else
			this.databaseHost=database_path;
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
	public String getDatabaseHost() {
		return this.databaseHost;
	}

	@Override
	public String getDatabasePort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatabaseTypeEnum getDatabaseType() {
		return DatabaseTypeEnum.H2;
	}

	@Override
	public String getDriverClassName() {
		return "org.h2.Driver";
	}

	@Override
	public String getDialectClassName() {
		return "org.hibernate.dialect.H2Dialect";
		//			return "org.hibernate.dialect.MySQLDialect";
	}

	@Override
	public String getURLConnection() {
		return "jdbc:h2:file:" + getDatabaseHost() + "/h2Database/"+ getDatabaseName() + ";"
				+ "INIT=create schema IF NOT EXISTS "+this.getDatabaseName()+";"
				//					+ "MODE=MySQL;"
				//					+ "DATABASE_TO_UPPER=FALSE;"
				+ "AUTO_SERVER=TRUE;";
	}

	@Override
	public List<String> getExistentDatabases() throws Exception {		//implement force to connect that deletes the .lock file??

		Set<String> files = new HashSet<>();

		File dir = new File(this.getDatabaseHost() + "/h2Database/");

		if(!dir.exists())
			dir.mkdirs();

		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				files.add(child.getName().split("\\.")[0]);
			}
		} 
		else {
			throw new Exception("No databases available!");
		}

		List<String> schemasList = new ArrayList<>();

		for(String db : files) {
			try {
				Connection connection = DriverManager.getConnection("jdbc:h2:file:" + getDatabaseHost() + "/h2Database/"+ db, this.databaseUser, this.databasePassword);
				List<String> tablesCheck = new ArrayList<String>();
				tablesCheck.add("enzymes_annotation_genehomology");

				Statement statement = connection.createStatement();
				statement.execute("SELECT TABLE_SCHEMA, TABLE_NAME FROM INFORMATION_SCHEMA.TABLES;");
				ResultSet rs = statement.getResultSet();

				while(rs.next()) {
					if(tablesCheck.contains(rs.getString(2).toLowerCase())) {
						if(db.equalsIgnoreCase(rs.getString(1))) {
							schemasList.add(db);
							break;
						}
					}
				}

				statement.close();
				connection.close();

			} 

			
			catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		return schemasList;
	}

	@Override
	public void executeDropTable(List<String> tablesNames) throws Exception {
			
			Connection connection = DriverManager.getConnection("jdbc:h2:file:" + getDatabaseHost() + "/h2Database/"+ this.databaseName+";"
					+ "AUTO_SERVER=TRUE;", this.databaseUser, this.databasePassword);
			
			Statement statement = connection.createStatement();
			
			for(String table : tablesNames) {
				try {
				statement.execute("DROP TABLE " +this.databaseName.toLowerCase() +"."+ table.toLowerCase() + ";");  //CHANGE ME WHEN ALL QUERIES ARE IN HIBERNATE!!!!
				} 
				catch (MySQLSyntaxErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			statement.close();
			connection.close();
	}

	@Override
	public void dropDatabase() throws Exception {
			
			Connection connection = DriverManager.getConnection("jdbc:h2:file:" + getDatabaseHost() + "/h2Database/"+ this.databaseName+";"
					+ "AUTO_SERVER=TRUE;", this.databaseUser, this.databasePassword);
			
			Statement statement = connection.createStatement();
			
			statement.execute("DROP SCHEMA " +this.databaseName.toLowerCase() + " CASCADE;");  //CHANGE ME WHEN ALL QUERIES ARE IN HIBERNATE!!!!
				
			statement.close();
			connection.close();
	}
}