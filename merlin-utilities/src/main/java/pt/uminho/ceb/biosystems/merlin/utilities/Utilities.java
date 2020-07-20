package pt.uminho.ceb.biosystems.merlin.utilities;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class Utilities {

	/**
	 * Round a number to decimal places.
	 * This breaks down badly in corner cases with either a very high number of decimal places (e.g. round(1000.0d, 17)) or large integer part (e.g. round(90080070060.1d, 9)
	 * 
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	//	/**
	//	 * @param value
	//	 * @param places
	//	 * @return
	//	 */
	//	public static double round(double value, int places) {
	//		
	//	    if (places < 0) throw new IllegalArgumentException();
	//
	//	    BigDecimal bd = new BigDecimal(value);
	//	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	//	    return bd.doubleValue();
	//	}
	//	
	/**
	 * @param value
	 * @param places
	 * @return
	 */
	public static String round(String value, int places) {

		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return String.valueOf(bd.doubleValue());
	}

	/**
	 * Parse boolean rules to string.
	 * 
	 * @param new_rules
	 * @return
	 */
	public static String parseRuleToString(Set<Set<String>> new_rules) {

		String ret = "";
		boolean not_first_or = false;

		for(Set<String> rules : new_rules) {

			if(not_first_or)
				ret = ret.concat(" OR ");

			boolean not_first_and = false;

			for(String new_rule : rules) {	

				if(not_first_and)
					ret = ret.concat(" AND ");

				ret = ret.concat(new_rule);

				not_first_and = true;
			}

			not_first_or = true;
		}
		return ret;
	}

	/**
	 * Parse boolean rules to string.
	 * 
	 * @param new_rules
	 * @return
	 */
	public static String parseRuleListToString(List<List<Pair<String,String>>> new_rules) {

		String ret = "";
		boolean not_first_or = false;

		for(List<Pair<String,String>> rules : new_rules) {

			if(not_first_or)
				ret = ret.concat(" OR ");

			boolean not_first_and = false;

			for(Pair<String,String> new_rule : rules) {	

				if(not_first_and)
					ret = ret.concat(" AND ");

				if(new_rule.getA() == null)
					System.out.println();
				ret = ret.concat(new_rule.getA());

				//				if(new_rule.getB() != null && !new_rule.getB().isEmpty())
				//					ret = ret.concat(" (").concat(new_rule.getB()).concat(")");

				not_first_and = true;
			}

			not_first_or = true;
		}
		return ret.replaceAll(" OR\\s+OR ", " OR ").replaceAll(" AND\\s+AND ", " AND ");
	}

	/**
	 * Parse boolean rules to string.
	 * 
	 * @param new_rules
	 * @return
	 */
	public static Set<Integer> parseRuleList(String rule) {

		Set<Integer> genes = new HashSet<>();

		if(rule != null) {

			for(String halfRule : rule.split(" OR ")) {
				for(String geneID : halfRule.split(" AND ")) {

					genes.add(Integer.valueOf(geneID.trim()));
				}
			}
		}

		return genes;
	}

	/**
	 * Parse boolean rules to list.
	 * 
	 * @param new_rules
	 * @return
	 */
	public static Set<Set<Integer>> parseStringRuleToSet(String rule) {

		Set<Set<Integer>> rules = new HashSet<>();

		if(rule != null) {

			for(String halfRule : rule.split(" OR ")) {

				Set<Integer> genes = new HashSet<>();

				for(String geneID : halfRule.split(" AND ")) {

					genes.add(Integer.valueOf(geneID.replaceAll("[\\(\\)]", "").trim()));
				}

				rules.add(genes);
			}
		}

		return rules;
	}
	
	
	/**
	 * Parse boolean rules to list.
	 * 
	 * @param new_rules
	 * @return
	 */
	public static List<List<Integer>> parseStringRuleToList(String rule) {

		List<List<Integer>> rules = new ArrayList<>();

		if(rule != null && !rule.isEmpty()) {

			for(String halfRule : rule.split(" OR ")) {

				List<Integer> genes = new ArrayList<>();

				for(String geneID : halfRule.split(" AND ")) {

					genes.add(Integer.parseInt(geneID.replaceAll("[\\(\\)]", "").trim()));
				}

				rules.add(genes);
			}
		}

		return rules;
	}
	
	


	/**
	 * retrieve the download file in List format
	 * 
	 * @param httpUrl
	 * @return
	 * @throws IOException
	 */
	public static List<String> getFileFromHttpUrl(String httpUrl) throws IOException{

		URL u = new URL (httpUrl);
		HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection (); 
		huc.setRequestMethod ("HEAD"); 
		HttpURLConnection.setFollowRedirects(false);
		huc.connect () ; 
		int code = huc.getResponseCode();

		String res = null;
		List<String> result = null;

		if(code == HttpURLConnection.HTTP_OK){

			URL url = new URL(httpUrl);
			URLConnection conn = url.openConnection();
			InputStream inputStream = conn.getInputStream();

			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				writer.write(buffer, 0, length);
			}
			res = writer.toString(StandardCharsets.UTF_8.name());

			//ALTERNATIVES
			//1
			//String writer = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			//2
			//Scanner s = new Scanner(inputStream).useDelimiter("\\A");
			//String writer = s.hasNext() ? s.next() : "";

			String[] splited = res.split("\n");
			result = new ArrayList<>(Arrays.asList(splited));
		}
		return result;
	}


	/**
	 * @param url
	 * @param savePath
	 * @throws IOException 
	 */
	public void downloadFileFromHttpUrl(String httpUrl, String savePath) throws IOException{

		int BUFFER_SIZE = 4096;  

		URL u = new URL (httpUrl);
		HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection (); 
		huc.setRequestMethod ("HEAD"); 
		HttpURLConnection.setFollowRedirects(false);
		huc.connect () ; 
		int code = huc.getResponseCode();

		if(code == HttpURLConnection.HTTP_OK){

			URL url = new URL(httpUrl);
			URLConnection conn = url.openConnection();
			InputStream inputStream = conn.getInputStream();


			FileOutputStream outputStream = new FileOutputStream(savePath);

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();
		} 
	}


	/**
	 * @return
	 */
	public static String getMerlinVersion(){

		List<String> readerConfFile = null;
		String merlinVersion = null;
		try {
			String path = FileUtils.getConfFolderPath().concat("workbench.conf");
			readerConfFile = FileUtils.readLines(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int i = 0;
		while(i<readerConfFile.size() && merlinVersion==null){
			//		for(String line : readerConfFile)
			String line = readerConfFile.get(i);
			if(line.trim().startsWith("#merlin-version")){
				merlinVersion = line.split(":")[1].trim();
				//				break;
			}
			i++;
		}
		return merlinVersion;
	}

	/**
	 * @param boolean_string
	 * @return
	 */
	public static Boolean get_boolean_string_to_boolean(String boolean_string){
		if(boolean_string.equalsIgnoreCase("true"))
		{
			return true;
		}
		else if(boolean_string.equalsIgnoreCase("false"))
		{
			return false;
		}
		return null;
	}

	/**
	 * @param boolean_int
	 * @return
	 */
	public static boolean get_boolean_int_to_boolean(String boolean_int){

		if(boolean_int.equalsIgnoreCase("1"))
		{
			return true;
		}
		return false;
	}

	/**
	 * @param map
	 * @return
	 */
	public static Map<String, Integer> sortMapByDescendingOrder(Map<String, Integer> map){
		try {
			
			LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
			//Use Comparator.reverseOrder() for reverse ordering
			map.entrySet()
			.stream()
			.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			.forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
