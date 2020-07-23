package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.MySleep;

/**
 * @author Oscar Dias and fxe
 *
 */
public class KeggRestful {

	private static final String NEW_LINE = "\n";
	private static final String TAB = "\\t";

	private static final String BASE_URL = "http://rest.kegg.jp/";

	final static Logger logger = LoggerFactory.getLogger(KeggRestful.class);

	private static final int READ_TIMEOUT = 20000; //miliseconds

	private static final HttpClientParams params = new HttpClientParams();
	private static final int MAX_TRIES = 3;

	static {
		params.setSoTimeout(READ_TIMEOUT);
	}


	/**
	 * @param operation
	 * @param args
	 * @return
	 */
	public static String getURL(String url) {

		logger.debug("KeggRestful::get - " + url.toString());
		
		String body = null;

		HttpClient client = new HttpClient();
		client.setParams(params);
		GetMethod method = new GetMethod(url);
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(3, false));
		try {

			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {

				System.err.println("Method failed: " + method.getStatusLine());
				System.err.println("get URL: " + url);
			}

			// Read the response body.
			byte[] responseBody = method.getResponseBody();

			// Deal with the response.
			// Use caution: ensure correct character encoding and is not binary data
			body = new String(responseBody);

		} 
		catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			// Release the connection.
			method.releaseConnection();
		}  

		/*
		URL restURL = new URL( url.toString());
		HttpURLConnection httpcon = (HttpURLConnection) restURL.openConnection();
		httpcon.setRequestMethod("GET");
		httpcon.setRequestProperty("Accept", "text/xml");
		httpcon.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
		String inputLine;
		while ( (inputLine = in.readLine()) != null) {
			ret.append(inputLine);
			ret.append("\n");
        }

		httpcon.disconnect();
		in.close(); */

		return body;
	}	

	public static String fetch(KeggOperation operation, String... args) throws Exception {
		return fetch(operation,0, args);
	}

	/**
	 * @param operation
	 * @param args
	 * @return
	 * @throws Exception 
	 */
	protected static String fetch(KeggOperation operation,int num, String... args) throws Exception {

		StringBuilder url = new StringBuilder( BASE_URL);
		
		url.append(operation.toString());//.append('/');
		
		for ( int i = 0; i < args.length; i++) {

			url.append('/').append( args[i]);
		}
		
			logger.debug( "KeggRestful::fetch - {}", url.toString());

		String body = null;
		
		HttpClient client = new HttpClient();
		client.setParams(params);
		GetMethod method = new GetMethod(URIUtil.encodeQuery(url.toString()));
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(3, false));
		
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, READ_TIMEOUT);
		
		try {
			
			int statusCode = client.executeMethod(method);
			
			if (statusCode != HttpStatus.SC_OK) {
				
				MySleep.myWait(1000);
				
				if(num<MAX_TRIES){
					num++;
					logger.debug("try number " + num+"\tstatus code: "+statusCode);
					return fetch(operation, num, args);
				}
				else{
	
					logger.error(" class {} Method failed: {}", KeggRestful.class, method.getStatusLine());
					logger.error(" class {} fetch: {}", KeggRestful.class, args[0]);
					method.releaseConnection();
					return null;
				}
			}

			// Read the response body.
			// byte[] responseBody = method.getResponseBody();
			InputStream responseBody = method.getResponseBodyAsStream();

			// Deal with the response.
			// Use caution: ensure correct character encoding and is not binary data
			//body = new String(responseBody);

			BufferedReader br = new BufferedReader(new InputStreamReader(responseBody));

			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = br.readLine()) != null) {

				sb.append(line+NEW_LINE);
			} 

			body = sb.toString();

			br.close();
		} 
		catch(SocketTimeoutException e) {
			
			logger.warn("Error: {}\ttry: {}\turl: {}", e.getMessage(), num,  url);
			
			MySleep.myWait(3000);
			
			if(num<MAX_TRIES){
				num++;
				logger.debug("Time out number " + num);
				body = fetch(operation, num, args);
			}
			else {
				
				e.printStackTrace();
			}

		} catch (HttpException e) {

			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}  

		/*
		URL restURL = new URL( url.toString());
		HttpURLConnection httpcon = (HttpURLConnection) restURL.openConnection();
		httpcon.setRequestMethod("GET");
		httpcon.setRequestProperty("Accept", "text/xml");
		httpcon.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
		String inputLine;
		while ( (inputLine = in.readLine()) != null) {
			ret.append(inputLine);
			ret.append("\n");
        }

		httpcon.disconnect();
		in.close(); */

		return body;
	}
	
	/**
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static List<String[]> getSpecificPathwayIDs(String query) throws Exception {
		//XXX:returns path:ID should remove path: ?

		String keggListResult = fetch(KeggOperation.list, query);

		List<String[]> ret = new ArrayList<String[]> ();
		String[] lines = keggListResult.split(NEW_LINE);
		for ( int i = 0; i < lines.length; i++) {
			String[] data = new String[2];
			data[0] = lines[i].split(TAB)[1];
			data[0].trim(); //prolly not needed !
			data[1] = lines[i].split(TAB)[0];
			data[1].trim(); //prolly not needed !
			data[1] = data[1].split(":")[1];
			ret.add(data);
		}
		return ret;
	}

	/**
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static List<String[]> getAllPathwayIDs() throws Exception {
		//XXX:returns path:ID should remove path: ?

		String keggListResult = fetch(KeggOperation.list, "pathway");

		List<String[]> ret = new ArrayList<String[]> ();
		String[] lines = keggListResult.split(NEW_LINE);
		for ( int i = 0; i < lines.length; i++) {
			String[] data = new String[2];
			data[0] = lines[i].split(TAB)[1];
			data[0].trim(); //prolly not needed !
			data[1] = lines[i].split(TAB)[0];
			data[1].trim(); //prolly not needed !
			data[1] = data[1].split(":")[1];
			ret.add(data);
		}
		return ret;
	}
	
	/**
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static Set<String> getPathwaysContainingReactions() throws Exception {
		//XXX:returns path:ID should remove path: ?

		String keggListResult = fetch(KeggOperation.link, "pathway", "reaction");
		
		Set<String> res = new HashSet<>();
		String[] lines = keggListResult.split(NEW_LINE);
		for ( int i = 0; i < lines.length; i++) {
			
			String path = lines[i].split(TAB)[1].split(":")[1];
			
			if(path.contains("map"))
				res.add(path.replace("map", ""));
		}
		return res;
	}

	/**
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static List<String[]> getAllECNumbersIDs() throws Exception {
		String keggListResult = fetch(KeggOperation.list, "enzyme");

		List<String[]> ret = new ArrayList<String[]> ();
		String[] lines = keggListResult.split(NEW_LINE);
		for ( int i = 0; i < lines.length; i++) {
			String[] data = new String[2];
			data[0] = lines[i].split(TAB)[0];
			data[0].trim();
			data[1] = lines[i].split(TAB)[1];
			data[1].trim();
			data[0] = data[0].split(":")[1];
			ret.add(data);
		}
		return ret;
	}
	
	/**
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static List<String[]> getAllMetaboliteIDs() throws Exception {
		String keggListResult = fetch(KeggOperation.list, "compound");

		List<String[]> ret = new ArrayList<String[]> ();
		String[] lines = keggListResult.split(NEW_LINE);
		for ( int i = 0; i < lines.length; i++) {
			String[] data = new String[2];
			data[0] = lines[i].split(TAB)[0];
			data[0].trim();
			data[1] = lines[i].split(TAB)[1];
			data[1].trim();
			data[0] = data[0].split(":")[1];
			ret.add(data);
		}
		return ret;
	}
	
	/**
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static List<String[]> getAllGlycanIDs() throws Exception {
		String keggListResult = fetch(KeggOperation.list, "glycan");

		List<String[]> ret = new ArrayList<String[]> ();
		String[] lines = keggListResult.split(NEW_LINE);
		for ( int i = 0; i < lines.length; i++) {
			String[] data = new String[2];
			data[0] = lines[i].split(TAB)[0];
			data[0].trim();
			data[1] = lines[i].split(TAB)[1];
			data[1].trim();
			data[0] = data[0].split(":")[1];
			ret.add(data);
		}
		return ret;
	}
	
	/**
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static List<String[]> getAllReactionIDs() throws Exception {
		String keggListResult = fetch(KeggOperation.list, "reaction");

		List<String[]> ret = new ArrayList<String[]> ();
		String[] lines = keggListResult.split(NEW_LINE);
		for ( int i = 0; i < lines.length; i++) {
			String[] data = new String[2];
			data[0] = lines[i].split(TAB)[0];
			data[0].trim();
			data[1] = lines[i].split(TAB)[1];
			data[1].trim();
			data[0] = data[0].split(":")[1];
			ret.add(data);
		}
		return ret;
	}
	
	
	/**
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static Map<String, String> mapPathways() throws Exception {
		//XXX:returns path:ID should remove path: ?
		String keggListResult = fetch(KeggOperation.list, "pathway");
		Map<String, String> ret = new HashMap<String, String>();
		String[] lines = keggListResult.split(NEW_LINE);
		for(int i = 0; i < lines.length; i++) {
			String[] data = lines[i].split(TAB);
			ret.put(data[0], data[1]);
		}
		return ret;
	}

	/**
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static List<String[]> getAllOrganismIDs() throws Exception {

		String keggListResult = fetch(KeggOperation.list, "organism");

		List<String[]> ret = new ArrayList<String[]> ();
		String[] lines = keggListResult.split(NEW_LINE);

		for ( int i = 0; i < lines.length; i++) {
			
			String[] data = new String[3];
			
			data[0] = lines[i].split(TAB)[2];
			data[0].trim();
			data[1] = lines[i].split(TAB)[1];
			data[1].trim();
			data[2] = lines[i].split(TAB)[0];
			data[2].trim();
			ret.add(data);
			
		}
		return ret;
	}

	/**
	 * 
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static List<String[]> getAllGenomeIDs() throws Exception {

		String keggListResult = fetch(KeggOperation.list, "genome");

		List<String[]> ret = new ArrayList<String[]> ();
		String[] lines = keggListResult.split(NEW_LINE);

		for (int i = 0; i < lines.length; i++) {

			String id = lines[i].split(TAB)[1];

			String[] data = new String[2];
			data[0] = id.split(",")[0];
			data[0].trim();

			Pattern pattern = Pattern.compile("(\\d{3,7})");
			Matcher matcher = pattern.matcher(id);

			if (lines[i].contains(";") && matcher.find()) {

				data[1] = matcher.group();
				data[1].trim();
				ret.add(data);
			}
		}
		return ret;
	}

	/**
	 * @param organismID
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String[] listGenesOrganism(String organismID) throws Exception {

		String keggListResult = fetch(KeggOperation.list, organismID);
		String[] ret = retrieveColumn(keggListResult, 0);
		logger.debug(ret.toString());
		return ret;
	}

	/**
	 * @param query
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String[] findCompoundKeggIDByQuery(String query) throws Exception  {

		String keggFindResult = fetch(KeggOperation.find, "compound", query);

		String[] ret = retrieveColumn(keggFindResult, 0);

		logger.debug(Arrays.toString(ret));
		
		return ret;
	}

	/**
	 * @param query
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String[] findCompoundByQuery(String query) throws Exception  {

		String[] entryKeggIDs = findCompoundKeggIDByQuery(query);
		String[] ret = new String[ entryKeggIDs.length];

		for ( int i = 0; i < ret.length; i++) {

			ret[i] = fetch(KeggOperation.get, entryKeggIDs[i]);

		}

		logger.debug(ret.toString());
		return ret;
	}

	/**
	 * @param query
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String[] findGenesFromKO(String query) throws Exception  {

		String keggFindResult = fetch(KeggOperation.link, "genes", query);

		String[] ret = retrieveColumn(keggFindResult, 1);

		logger.debug(Arrays.toString(ret));
		return ret;
	}
	
	/**
	 * @param query
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String findFirstGeneQuery(String query) throws Exception  {

		String keggFindResult = fetch(KeggOperation.find, "genes", query);
		
		List<String> ret = new ArrayList<String>(Arrays.asList(retrieveColumn(keggFindResult, 0)));

		logger.debug("ret {}",ret);
		
		if(ret.size()>0)
			return ret.get(0);
		else
			return null;
	}


	/**
	 * @param query
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String[] findGenomeByQuery(String query) throws Exception  {

		String keggFindResult = fetch(KeggOperation.find, "genome", query);

		String[] ret = retrieveColumn(keggFindResult, 1);

		logger.debug(ret.toString());
		return ret;
	}
	
	
	/**
	 * @param taxonomyID
	 * @return Kegg Entries
	 * @throws Exception
	 */
	public static String[] findKeggEntryByTaxonomyID(String taxID) throws Exception  {

		String keggFindResult = fetch(KeggOperation.find, "genome", taxID);

		String[] tNumbers = retrieveColumn(keggFindResult, 0);
		String[] taxIDs = retrieveColumn(keggFindResult, 1);
		List<Integer> indexes = new ArrayList<Integer>();
		
		for(int i = 0; i<taxIDs.length; i++){
			
			String[] contents = taxIDs[i].split(";")[0].split(",");
			
			for(String content : contents){
				if(content.trim().equals(taxID))
					indexes.add(i);
			}
		}
		
		String[] keggTaxonomyIDs = new String[indexes.size()];
		
		if(!indexes.isEmpty()){
			for(int index = 0; index<indexes.size(); index++)
				keggTaxonomyIDs[index] = tNumbers[indexes.get(index)];
		}
			
		logger.debug(Arrays.toString(keggTaxonomyIDs));
		
		return keggTaxonomyIDs;
	}
	
	
	/**
	 * @param db
	 * @param entry
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String getDBEntry(String db, String entry) throws Exception {

		return KeggRestful.fetch(KeggOperation.get, db + ":" + entry);
	}

	/**
	 * @param flatFile
	 * @param index
	 * @return
	 */
	private static String[] retrieveColumn(String flatFile, int index) {

		String[] ret = null;
		String[] lines = flatFile.split(NEW_LINE);
		ret = new String[ lines.length];
		for ( int i = 0; i < lines.length; i++) {

			ret[i] = lines[i].split(TAB)[index];
			ret[i].trim(); //prolly not needed !
		}

		return ret;
	}
	
	/**
	 * @param flatFile
	 * @param ec
	 * @return
	 */
	private static List<String> retrieveKoSpecificEC(String flatFile, String ec) {

		List<String> ret = new ArrayList<>();
		String[] lines = flatFile.split(NEW_LINE);
		
		for ( int i = 0; i < lines.length; i++) {

			Pattern pattern = Pattern.compile("\\[EC:(.+)\\]");
			Matcher matcher = pattern.matcher(lines[i]);

			boolean addKO = false;
			
			if (matcher.find()) {
				
				String ecNumbers = matcher.group(1).replaceAll("EC:", "");
				
				for(String ecNumber : ecNumbers.split(" "))
					if(ecNumber.equalsIgnoreCase(ec))
						addKO = true;
			}
			
			if(addKO)
				ret.add(lines[i].split(TAB)[0].trim()); //prolly not needed !
		}
		
		return ret;
	}

	public static List<String[]> link(String database, String query) throws Exception {
		String response = fetch(KeggOperation.link, database, query);
		List<String[]> result = new ArrayList<String[]>();
		String[] lines = response.split(NEW_LINE);
		for(String line : lines)
			result.add(line.split(TAB));

		return result;

	}

	public static List<String> findOrthologsByECnumber(String query) throws Exception {

		String keggFindResult = fetch(KeggOperation.find, "ko", query);
		
		List<String> ret = retrieveKoSpecificEC(keggFindResult, query);

		logger.debug(ret.toString());

		return ret;
	}

	public static List<String> findOrthologsByReaction(String query) throws Exception {

		String keggFindResult = fetch(KeggOperation.link, "ko", query);
		List<String> ret = new ArrayList<String>(Arrays.asList(retrieveColumn(keggFindResult, 1)));

		logger.debug(ret.toString());

		return ret;
	}

	public static String findModulesStringByQuery(String query) throws Exception {

		return fetch(KeggOperation.link, "module", query);
	}

	public static List<String> findModulesByQuery(String query) throws Exception {

		String keggFindResult = findModulesStringByQuery(query);
		List<String> ret = new ArrayList<String>(Arrays.asList(retrieveColumn(keggFindResult, 1)));

		logger.debug(ret.toString());

		return ret;
	}

	/**
	 * @param ortholog
	 * @return
	 * @throws Exception
	 */
	public static List<String> findECNumbersByOrtholog(String ortholog) throws Exception {

		String keggFindResult = fetch(KeggOperation.link, "ec", ortholog);
		List<String> ret = new ArrayList<String>(Arrays.asList(retrieveColumn(keggFindResult, 1)));

		logger.debug(ret.toString());

		return ret;
	}

	/**
	 * @param module
	 * @return
	 * @throws Exception
	 */
	public static List<String> findPathwaysByModule(String module) throws Exception {

		String keggFindResult = fetch(KeggOperation.link, "pathway", module);
		List<String> ret = new ArrayList<String>(Arrays.asList(retrieveColumn(keggFindResult, 1)));
		return ret;
	}

}
