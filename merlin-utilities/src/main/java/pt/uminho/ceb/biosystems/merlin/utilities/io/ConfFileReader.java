package pt.uminho.ceb.biosystems.merlin.utilities.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author adias
 *
 */
public class ConfFileReader {

	public static Map<String,String> loadConf(String path) throws IOException{
		
		File file = new File(path);
		
		return ConfFileReader.loadConf(file);
	}
	
	public static Map<String,String> loadConf(File file) throws IOException{
		
		HashMap<String, String> conf = new HashMap<String,String>();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		String text, key, value;
		while ((text = bufferedReader.readLine()) != null) {
			if(text.toLowerCase().matches("^[a-z].*") && (text.length()-text.replace(":", "").length())==1){
				text = text.replaceAll("\\s","");
				key = text.split(":")[0];
				value = text.split(":")[1];
				
				conf.put(key,value);
			}
		}
		bufferedReader.close();
		
		if(conf.isEmpty()){
			throw new IOException("required configuration file is empty!! check "+file.getPath());
		}
		return conf;
	}
	
	
	/**
	 * @return HashMap of file extensions to be download from NCBI
	 * @throws IOException
	 */
	public static HashMap<String,String> readExtensionsConf() throws IOException{

		File extensionFile = new File(FileUtils.getConfFolderPath() + "ftpfiles_extensions.conf");

		HashMap<String, String> extensions = new HashMap<String,String>();

		BufferedReader bufferedReader = new BufferedReader(new FileReader(extensionFile));

		String text, type, extension;
		while ((text = bufferedReader.readLine()) != null) {
			if(text.toUpperCase().matches("^[A-Z].*$")) {
				type = text.split("\t")[0];
				extension = text.split("\t")[1];

				extensions.put(type,extension);
			}
		}
		bufferedReader.close();

		return extensions;
		
	}
	
}
