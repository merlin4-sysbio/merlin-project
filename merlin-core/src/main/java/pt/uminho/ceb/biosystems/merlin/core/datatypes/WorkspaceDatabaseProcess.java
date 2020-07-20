package pt.uminho.ceb.biosystems.merlin.core.datatypes;

/**
 * @author ODias
 *
 */
public class WorkspaceDatabaseProcess {

//	/**
//	 * Start a new MySQL process, if it does not exists
//	 * 
//	 * @param user
//	 * @param password
//	 * @param host
//	 * @param port
//	 * @return
//	 * @throws SQLException 
//	 */
//	public static String startDBProcess(String user, String password, String host, String port) throws SQLException{
//		Map<String, String> pidSessionPost;
//		String pid = "";
//		
//		if(existsdbProcess()) {
//			
//			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(Project.class);
//			if(cl.size()==0) {
//				
//			//	if(useAvailableConnection()) {
//					
//					DatabaseSchemas schemas = new DatabaseSchemas(user, password, host, port, DatabaseType.MYSQL);
//					if(schemas.isConnected()) {
//						
////						if(schemas.getSchemas().isEmpty())
////						{
////							//StringTokenizer st = new StringTokenizer(System.getProperty("java.class.path"),";");
////							//String filePath=st.nextToken()+"/../../utilities/sysbio.sql";
////							String[] filePath=new String[6];
////							String path = FileUtils.getCurrentLibDirectory()+"/../"; //st.nextToken();
////							filePath[0]=path +"utilities/sysbio_KEGG.sql";
////							filePath[1]=path +"utilities/sysbio_blast.sql";
////							filePath[2]=path +"utilities/sysbio_metabolites_transporters.sql";
////							filePath[3]=path +"utilities/sysbio_sw_tcdb.sql";
////							filePath[4]=path +"utilities/sysbio_compartments.sql";
////							filePath[5]=path +"utilities/sysbio_metabolites_backup.sql";
////
////
////							if(schemas.newSchemaAndScript("merlinDB", filePath))
////							{
////								Workbench.getInstance().info("Database merlinDB successfuly created.");
////							}
////							else
////							{
////								Workbench.getInstance().error("There was an error when trying to create merlinDB database!!");
////							}
////						}
////					}
////					else {
////						
////						if(!insertCorrectCredentials()) {
////							
////							return returnNewPID();
////						}
////					}
//				}
//				else {
//					
//					return returnNewPID();
//				}
//			}
//		}
//		else {
//			
//			if(newdbProcess())
//				while(listDBProcess().size()==0);
//			else
//				return null;
//		}
//		pidSessionPost = listDBProcess();
//		for(String key: pidSessionPost.keySet()){pid=key;}
//		while(DatabaseProcess.listDBProcess().size()==0);
//		return pid;
//	}

//	/**
//	 * Start a new MySQL process
//	 * @return
//	 */
//	private static Boolean newdbProcess(){
//
//		if(netStartMysql()) {
//			
//			try {
//				
//				Runtime.getRuntime().exec("NET STOP MySQL").waitFor();
//			} 
//			catch (InterruptedException e){e.printStackTrace();}
//			catch (IOException e){e.printStackTrace();}
//			
//			if(startDBProcess()) {
//				
//				if(useMerlinConnection()) {
//					
//					return true;
//				}
//				else
//				{			
//					for(String key: listDBProcess().keySet()){terminatedbProcess(key,"");}
//					netStartMysql();
//					while(listDBProcess().size()==0);
//					return true;
//				}
//			}
//			else {
//				
//				if(netStartMysql()) {
//					
//					while(listDBProcess().size()==0);
//					return true;
//				}
//			}
//		}
//		else
//		{
//			if(startDBProcess()) {
//				
//				return true;
//			}
//			else {
//				
//				Workbench.getInstance().warn("MySQL server not found.\nPlease use H2 database.");
//				return false;
//			}
//		}
//		return false;
//	}


//	/**
//	 * Start merlin MySQL instance
//	 * 
//	 * @return
//	 */
//	private static boolean startDBProcess(){
//		
//		try {
//			
//			String[] command;
//			String path = new File(FileUtils.getCurrentLibDirectory()).getParent();
//			System.out.println(System.getProperty("java.class.path"));
//			//String path = FileUtils.getCurrentDirectory()+"/../";
//			command = new String[] {path+"/mysql/bin/mysqld","--defaults-file="+path+"utilities/my-medium-odias.ini"};
//			Runtime.getRuntime().exec(command);
//			//readStream(pro.getInputStream()).trim();
//			while(listDBProcess().size()==0);
//			return true;
//		}
//		catch (Exception e) {
//			
//			//e.printStackTrace();
//			return false;
//		}
//	}

//	/**
//	 * Start MySQL service
//	 * 
//	 * @return
//	 */
//	private static boolean netStartMysql(){
//		Process process;
//		try 
//		{
//			process = Runtime.getRuntime().exec("NET START MYSQL");
//			//String error = 
//			readStream(process.getErrorStream(), "\nPROCESS ERROR STREAM:\t");
//			//String input = 
//			readStream(process.getInputStream(), "\nPROCESS INPUT STREAM:\t");
//			int exitVal = process.waitFor();
//
//			if(exitVal!=0)
//				return false;
//			else
//				return true;
//		}
//		catch (IOException e) {e.printStackTrace();}
//		catch (InterruptedException e) {e.printStackTrace();}
//
//		return listDBProcess().size()>0;
//	}

//	/**
//	 * @param is
//	 * @param type
//	 */
//	private static String readStream(InputStream is, String type){
//		InputStreamReader isr = new InputStreamReader(is);
//		BufferedReader br = new BufferedReader(isr);
//		String line=null;
//		String stream="";
//		try
//		{
//			while ( (line = br.readLine()) != null)
//			{
//				System.out.println(type + ">" + line);
//				stream +=line+" ";
//			}
//			return stream;
//		}
//		catch (IOException e) {e.printStackTrace();}
//		return null; 
//
//	}

//	/**
//	 * Start a new MySQL process returning the process ID
//	 * @return
//	 */
//	private static String returnNewPID(){
//		String pid = null;
//		Map<String, String> pidSessionPre = listDBProcess();
//		for(String key: listDBProcess().keySet()){pid=key;}
//		terminatedbProcess(pid, pidSessionPre.get(pid));
//		if(newdbProcess())
//		{
//			while(listDBProcess().size()==0);
//			Map<String, String> pidSessionPost = listDBProcess();
//			for(String key:pidSessionPre.keySet())
//			{
//				pidSessionPost.remove(key);
//			}
//			for(String key: pidSessionPost.keySet()){pid=key;}
//			for(String key:pidSessionPre.keySet())
//			{
//				pidSessionPost.put(key,pidSessionPre.get(key));
//			}
//			return pid;
//		}
//		else{return null;}
//	}

//	/**
//	 * Read a MySQL start process stream
//	 * @param is
//	 * @return
//	 */
//	private static Map<String, String> readStream(InputStream is){
//		Map<String,String> pids = new TreeMap<String,String>();
//		InputStreamReader isr = new InputStreamReader(is);
//		BufferedReader br = new BufferedReader(isr);
//		String line=null;
//		try
//		{
//			while ( (line = br.readLine()) != null)
//			{
//				if(line.trim().startsWith("mysqld.exe"))
//				{
//					line=line.replace("mysqld.exe","");
//					StringTokenizer st = new StringTokenizer(line.trim()," "); 
//					pids.put(st.nextToken(), st.nextToken());
//				}
//			}
//		}
//		catch (IOException e) {e.printStackTrace();}
//		return pids; 
//	}

//	/**
//	 * A list of all running MySQL processes
//	 * @return
//	 */
//	public static Map<String, String> listDBProcess(){
//		Process pro = null;
//		try
//		{
//			pro=Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq mysqld.exe\"");
//			pro.waitFor();
//		}
//		catch (InterruptedException e) {e.printStackTrace();}
//		catch (IOException e) {e.printStackTrace();}
//		return readStream(pro.getInputStream());
//	}

//	/**
//	 * If exists at least one running MySQL process
//	 * @return
//	 */
//	public static boolean existsdbProcess(){
//		return !listDBProcess().isEmpty();
//	}

//	/**
//	 * Terminate MySQL processes by its process id
//	 * 
//	 * @param pid
//	 * @param type
//	 * @return
//	 */
//	public static int terminatedbProcess(String pid, String type){
//
//		//Process pro = null;
//		//String[] command;
//		try
//		{
//			if(type.trim().equals("Services"))
//			{
//				//return Runtime.getRuntime().exec("NET STOP MySQL").waitFor();
//				Runtime.getRuntime().exec("NET STOP MySQL").waitFor();
//				return 0;
//			}
//			else
//			{
//				String com = "TASKKILL /PID "+pid+" /F";
//				return Runtime.getRuntime().exec(com).waitFor();
//			}
//
//		} 
//		catch (IOException e) {e.printStackTrace();}
//		catch (InterruptedException e) {e.printStackTrace();}
//		return 1;
//	}

//	/**
//	 * Select whether to use one available MySQL connection
//	 * @return
//	 */
//	private static boolean useAvailableConnection(){
//		int i =CustomGUI.stopQuestion("Available MySQL connections detected",
//				"At least one MySQL database connection was detected by merlin. Do you wish to try to use it?",
//				new String[]{"Yes", "No", "Info"});
//		if(i<2)
//		{
//			switch (i)
//			{
//			case 0:return true;
//			default:return false;
//			}
//		}
//		else
//		{
//			Workbench.getInstance().info(
//					"There is an available database connection for merlin. If such database does not contain any data \n" +
//							"that can be used by merlin it will try to create a new schema.\n" +
//							"If you select 'no' the available process will be terminated and a new connection to merlin's database\n" +
//					"will be established.");
//			return useAvailableConnection();
//		}
//	}
//
//	/**
//	 * @return
//	 */
//	private static boolean insertCorrectCredentials(){
//		int i =CustomGUI.stopQuestion("Insert MySQL database credentials",
//				"Do you wan't to insert the correct MySQL user credentials, for the available MySQL connection,\n" +
//						"and try to use the current database or just abort and go to merlin's own database?",
//						new String[]{"Insert", "Abort", "Info"});
//		if(i<2)
//		{
//			switch (i)
//			{
//			case 0:return true;
//			default:return false;
//			}
//		}
//		else
//		{
//			Workbench.getInstance().info(
//					"The default credentials used by merlin USER=\"root\", PASSWORD =\"password\", HOST=\"localhost\", PORT=\"3306\"\n" +
//							"aren't correct for the available connection. If you known the correct cerdentials you can insert them and merlin will \n" +
//					"use the available database, else you can just use merlin's default database.\n");
//			return insertCorrectCredentials();
//		}
//	}

//	/**
//	 * Select whether to use one available MySQL connection
//	 * @return
//	 */
//	private static boolean useMerlinConnection(){
//		int i =CustomGUI.stopQuestion("Several MySQL databases detected",
//				"Several MySQL databases were detected, which do you want to use?",
//				new String[]{"Merlin", "MySQL Service", "Info"});
//		if(i<2)
//		{
//			switch (i)
//			{
//			case 0:return true;
//			default:return false;
//			}
//		}
//		else
//		{
//			Workbench.getInstance().info(
//					"Merlin detected an MySQL service installation. Do you wich to use  \n" +
//					" such installation or do you wish to use merlin's own database.\n");
//			return useMerlinConnection();
//		}
//	}




	//	private static void readStream(InputStream is, String type){
	//		InputStreamReader isr = new InputStreamReader(is);
	//		BufferedReader br = new BufferedReader(isr);
	//		String line=null;
	//		try
	//		{
	//			while ( (line = br.readLine()) != null)
	//			{
	//				System.out.println(type + ">" + line);
	//			}
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		} 
	//	}

}