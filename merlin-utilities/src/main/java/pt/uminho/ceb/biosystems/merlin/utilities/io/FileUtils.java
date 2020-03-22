package pt.uminho.ceb.biosystems.merlin.utilities.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;


public class FileUtils extends pt.uminho.ceb.biosystems.mew.utilities.io.FileUtils{

	/**method to return utilities folder path in merlin folder
	 * @return
	 */
	public static String getUtilitiesFolderPath(){

		return FileUtils.getHomeFolderPath().concat("utilities/");
	}

	/**method to return utilities folder path in merlin folder
	 * @return
	 */
	public static String getDatabaseManagementFolderPath(){

		return FileUtils.getUtilitiesFolderPath().concat("database_management/");
	}

	/** 
	 * Method to return merlin home folder path
	 * @return
	 */
	public static String getHomeFolderPath(){

		return new File(System.getProperty("user.dir")).getPath().concat("/");
	}

	/**
	 * @return
	 */
	public static String getCurrentLibDirectory(){

		String libfolder = getHomeFolderPath().concat("lib");

		File f = new File(libfolder);
		if(!f.isDirectory())
			f.mkdir();

		return libfolder;
	}

	/**
	 * @return
	 */
	public static String getCurrentTempDirectory(){

		String tempDirectory = getHomeFolderPath().concat("temp/");
		File file = new File(tempDirectory);

		if(!file.exists())
			file.mkdirs();

		return tempDirectory;
	}

	/**method to return workspace folder path
	 * @return
	 */
	public static String getWorkspaceFolderPath(String databaseName){

		return FileUtils.getWorkspacesFolderPath().concat(databaseName).concat("/");
	}


	/**method to return taxonomy workspace folder path
	 * @return
	 */
	public static String getWorkspaceTaxonomyFolderPath(String databaseName, Long taxonomyID){

		return FileUtils.getWorkspaceFolderPath(databaseName).concat(taxonomyID.toString()).concat("/");
	}


	/**method to return taxonomy workspace folder path
	 * @return
	 */
	public static String getWorkspaceTaxonomyTriageFolderPath(String databaseName, Long taxonomyID){

		String workspaceTriageFolder = FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID).concat("triage/");

		File file = new File(workspaceTriageFolder);
		file.mkdirs();

		return workspaceTriageFolder;
	}


	/**
	 * @param databaseName
	 * @param taxonomyID
	 * @return
	 */
	public static String getWorkspaceTaxonomyTempFolderPath(String databaseName, Long taxonomyID){

		String workspaceTempFolder = FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID).concat("temp/");

		File file = new File(workspaceTempFolder);
		file.mkdirs();

		return workspaceTempFolder;
	}


	/**
	 * @param databaseName
	 * @param taxonomyID
	 * @return
	 */
	public static String getWorkspaceTaxonomyGprsFolderPath(String databaseName, Long taxonomyID){

		String workspaceGprsFolder = FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID).concat("gprs/");

		File file = new File(workspaceGprsFolder);
		file.mkdirs();

		return workspaceGprsFolder;
	}


	/**
	 * @param databaseName
	 * @param taxonomyID
	 * @return
	 */
	public static String getWorkspaceTaxonomyFillGapsFolderPath(String databaseName, Long taxonomyID){

		String workspaceFillGapsFolder = FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID).concat("fill_gaps/");

		File file = new File(workspaceFillGapsFolder);
		file.mkdirs();

		return workspaceFillGapsFolder;
	}


	/** 
	 * Method to return merlin home folder file
	 * @return
	 */
	public static File getHomeFolder(){

		return new File(getCurrentLibDirectory()).getParentFile();
	}


	/**
	 * method to return merlin configuration(conf) folder
	 * @return
	 */
	public static String getConfFolderPath(){

		return FileUtils.getHomeFolderPath().concat("conf/");
	}


	/**
	 * method to return workspace configuration(conf) folder path
	 * @return
	 */
	public static String getWorkspaceConfFolderPath(String databaseName){

		String path = FileUtils.getWorkspaceFolderPath(databaseName).concat("conf/");

		File file = new File(path);

		if(!file.exists())
			file.mkdirs();

		return path;
	}


	/**
	 * method to return workspaces folder (folder which contains all created workspaces)
	 * the folder "workspaces" is created if it doesn't exist
	 * @return
	 */
	public static String getWorkspacesFolderPath(){

		File path = new File(FileUtils.getHomeFolderPath().concat("ws").concat("/"));

		if(!path.exists())
			path.mkdirs();

		String workspacesPath = path.getAbsolutePath().concat("/");

		return workspacesPath;
	}


	/**
	 * @param compressedFile
	 * @param decompressedFile
	 */
	public static void unGunzipFile(String compressedFile, String decompressedFile) {

		byte[] buffer = new byte[1024];

		try {

			FileInputStream fileIn = new FileInputStream(compressedFile);

			GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);

			FileOutputStream fileOutputStream = new FileOutputStream(decompressedFile);

			int bytes_read;

			while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {

				fileOutputStream.write(buffer, 0, bytes_read);
			}

			gZIPInputStream.close();
			fileOutputStream.close();

			//			System.out.println("The file was decompressed successfully!");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param path
	 * @return
	 */
	public static Map<String, String> readMapFromFile(String path){

		Map<String, String> dic = new HashMap<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));

			String line = br.readLine();

			while (line != null) {

				if(!line.trim().isEmpty() && !line.startsWith("#")){

					//					System.out.println(line);

					try {
						String[] content = line.split("=");

						if(content.length > 1)
							dic.put(content[0].trim(), content[1].trim());
						else
							dic.put(content[0].trim(), "");
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}

				line = br.readLine();
			}

			br.close();

		} 
		catch(Exception e) {
			return new HashMap<>();
		}
		return dic;
	}

	/**
	 * Method to get TranSyT's configurations from the file located at /conf/transyt.conf
	 * 
	 * @return
	 */
	public static Map<String, String> readTransytConfFile(){

		String path = getConfFolderPath().concat("transyt.cfg");

		return readMapFromFile(path);
	}

	/**
	 * Method to get BioISO's configurations from the file located at /conf/bioiso.conf
	 * 
	 * @return
	 */
	public static Map<String, String> readBioisoConfFile(){

		String path = getConfFolderPath().concat("bioiso.cfg");

		return readMapFromFile(path);
	}


	public static String getIdsConverterConfFile(String databaseName){

		String path = getWorkspaceConfFolderPath(databaseName).concat("ids_database_format.cfg");

		return path;
	}

	public static void writeIdsConverterConfFile(String databaseName) throws IOException{

		String path = getWorkspaceConfFolderPath(databaseName).concat("ids_database_format.cfg");

		if (!new File(path).exists()){

			FileWriter writer = new FileWriter(path);
			BufferedWriter buffer = new BufferedWriter(writer);

			buffer.write("############## Ids converter configurations ##############\n\nIDs_format = KEGG");
			buffer.close();
		}

	}

	/**
	 * Method to get Ids converter's configurations from the file located at /conf/ids_database_format.cfg
	 * 
	 * @return 
	 */
	public static Map<String, String> readIdsConverterConfFile(String databaseName){

		String path = getWorkspaceConfFolderPath(databaseName).concat("ids_database_format.cfg");

		return readMapFromFile(path);
	}

	public static void changeParameterInIdsConverterConfFile(String confParameter, String value, String databaseName) throws FileNotFoundException {

		String path = getWorkspaceConfFolderPath(databaseName).concat("ids_database_format.cfg");

		changeParameterInConfFile(confParameter, value, path);

	}



	public static void changeParameterInConfFile(String confParameter, String value, String path) throws FileNotFoundException {

		ArrayList<String> listLines= new ArrayList<>(); 
		File configFile= new File(path); 
		Scanner file= new Scanner(configFile); 
		while(file.hasNextLine()==true) {
			listLines.add(file.nextLine());
		}
		file.close();

		int i = 0;

		boolean stop = false;
		while (i < listLines.size() && !stop) { 

			String line = listLines.get(i);

			Matcher m = Pattern.compile(confParameter).matcher(line);

			if (m.find()) {

				String[] lineList = line.trim().split("=");
				lineList[1] = value;
				line = lineList[0] + " = "+ lineList[1];
				stop = true;
			}

			listLines.remove(i);
			listLines.add(i, line);
			i++;

		}	
		PrintWriter confFile = new PrintWriter(path); 
		for (String line : listLines) {
			confFile.println(line);
		}
		confFile.close();
	}

	public static String getIdsConverterFolder(String databaseName) {
		
		return FileUtils.getWorkspaceFolderPath(databaseName) + "idsConverter/";
		
	}


}
