package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.hmmer;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;

/**
 *
 * @author fxe
 */
public class HmmerFetch {

	/**
	 * @param url
	 * @param range
	 * @return
	 * @throws Exception
	 */
	public static String fetch(String jobID, int range) throws Exception {

		try{
			
			StringBuilder sb = new StringBuilder();
			
			URL urlres = new URL(jobID);
			if(range>0)
				urlres = new URL(jobID+"?range=1,"+range);
			
			HttpURLConnection httpcon = (HttpURLConnection) urlres.openConnection();
			httpcon.setRequestMethod("GET");
			httpcon.setRequestProperty("Accept", "application/json");

			BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
			}
			return sb.toString();
		}
		catch(MalformedURLException e){
			
			System.err.println("URL\t"+jobID);
			throw e;
		}
		catch(IOException e){

			if(e.getMessage().contains("429"))
				throw new JSONException("");
			throw e;
		}
		catch (OutOfMemoryError e) {
			
			throw new Exception("Out of memory error");
		}
	}

	/**
	 * @param url
	 * @throws Exception
	 */
	public static void delete(String url) throws Exception {

		try{

			URL urlres = new URL(url);
			HttpURLConnection httpcon = (HttpURLConnection) urlres.openConnection();
			httpcon.setRequestMethod("DELETE");
			httpcon.disconnect();

		}
		catch(MalformedURLException me){

			System.err.println("URL\t"+url);
			throw me;
		}
		catch (OutOfMemoryError e) {
			throw new Exception("Out of memory error");
		}
	}
}