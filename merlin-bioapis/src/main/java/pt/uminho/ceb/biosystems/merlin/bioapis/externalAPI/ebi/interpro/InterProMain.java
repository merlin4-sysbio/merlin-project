package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.biojava.nbio.core.sequence.ProteinSequence;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.EbiWebServices;

/**
 * @author Oscar Dias
 *
 */
public class InterProMain {

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<String> readFile(String file) throws IOException{

		Scanner s = new Scanner(new File(file));
		List<String> list = new ArrayList<String>();
		while (s.hasNextLine()){

			list.add(s.nextLine());
		}
		s.close();

		return list;
	}

	public static HashMap<String,String> readFasta(List<String> file) throws FileNotFoundException {

		HashMap<String,String> list = new HashMap<String,String>();
		for(int i = 0; i  < file.size(); i++){
			if(file.get(i).startsWith(">")){
				String id = file.get(i);
				list.put(id, "");
				i++;
				while(i < file.size() && !(file.get(i).startsWith(">"))){
					list.put(id,list.get(id)+file.get(i));
					i++;
				}
				i--;
			}
		}
		return list;
	}



	/**
	 * @param email
	 * @param title
	 * @param goterms
	 * @param pathways
	 * @param appls
	 * @param requests
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Map<String,InterProResultsList> processGenome(String email, String title, String goterms, String pathways, List<String> appls, Map<String,ProteinSequence> requests) throws FileNotFoundException, IOException, InterruptedException {

		List<Map<String,ProteinSequence>> mapsList = new ArrayList<Map<String,ProteinSequence>>();
		List<String> keys = new ArrayList<>(requests.keySet());
		Map<String,InterProResultsList> result = new HashMap<String,InterProResultsList>();

		int i = 0;
		Map<String,ProteinSequence> map = new HashMap<String,ProteinSequence>();

		while (i < requests.size()) {

			int mapSize = 50;

			if ((requests.size() - i) < 50)				
				mapSize = (requests.size() - i);

			for (int j = 0; j < mapSize; j++)				
				map.put(keys.get(j), requests.get(keys.get(j)));

			mapsList.add(map);
			map = new HashMap<String,ProteinSequence>();
			i+=mapSize;
		}

		for (Map<String,ProteinSequence> queryMap : mapsList) {

			Map<String,String> requestResult = InterProMain.makeRequest(email, title, goterms, pathways, appls, queryMap);
			Map<String,InterProResultsList> partResult = getResults(requestResult);
			result.putAll(partResult);
		}

		return result;
	}


	/**
	 * @param email
	 * @param title
	 * @param goterms
	 * @param pathways
	 * @param appls
	 * @param requests
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Map<String,String> makeRequest (String email, String title, String goterms, String pathways, List<String> appls, Map<String,ProteinSequence> requests) throws FileNotFoundException, IOException {

		Map<String,String> result = new HashMap<String,String>();

		for (String query: requests.keySet()) {

//			System.out.println("submiting  "+requests.get(query).getOriginalHeader());
			String requestId = getJobID(email, title, query, requests.get(query), goterms, pathways, appls);

			result.put(query, requestId);
		}

		return result;
	}

	/**
	 * @param requests
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static Map<String,InterProResultsList> getResults (Map<String,String> requests) throws ClientProtocolException, IOException, InterruptedException{

		List<String> keys = new ArrayList<>(requests.keySet());
		Map<String,InterProResultsList>result = new HashMap<String,InterProResultsList>();

		while (keys.size()>0){

			boolean foundOne = false;

			for(String request : requests.keySet()){

				if (keys.contains(request)) {

//					System.out.println(getStatus(requests.get(request)) + " " + request);
//					System.out.println(getStatus(requests.get(request)).equalsIgnoreCase("FINISHED") + " " + request);
					
					if (getStatus(requests.get(request)).equalsIgnoreCase("FINISHED")){

						foundOne = true;

						List<String>xml = getResult(requests.get(request));
						InterProResultsList parsed = getParsed(xml);

						result.put(requests.get(request),parsed);

						keys.remove(request);
//						System.out.println(keys);

					}
					else if (getStatus(requests.get(request)).equalsIgnoreCase("ERROR") || 
							getStatus(requests.get(request)).equalsIgnoreCase("NOT_FOUND") ||
							getStatus(requests.get(request)).equalsIgnoreCase("FAILURE")) {

						keys.remove(request);
						throw new IOException("Job ID not found");	
					}
				}


			}		
			if (!foundOne) 					
				Thread.sleep(30000);
		}

		return result;
	}

	/**
	 * @param tool
	 * @param email
	 * @param title
	 * @param sequence
	 * @param goterms
	 * @param pathways
	 * @param appls
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static String getJobID (String tool, String email, String title, ProteinSequence sequence, String goterms, String pathways, List<String> appls) throws ClientProtocolException, IOException {

		return EbiWebServices.runInterProScan(email, title, sequence, goterms, pathways, appls);
	}

	/**
	 * @param ID
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static String getStatus(String ID) throws ClientProtocolException, IOException{

		return InterProClientRest.getHttpUrl("http://www.ebi.ac.uk/Tools/services/rest/iprscan5/status/"+ID);
	}

	/**
	 * @param ID
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static List<String> getResult(String ID) throws ClientProtocolException, IOException {

		return Arrays.asList(InterProClientRest.getHttpUrl("http://www.ebi.ac.uk/Tools/services/rest/iprscan5/result/"+ID+"/xml").split("\n"));
	}

	/**
	 * @param xml
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static InterProResultsList getParsed(List<String> xml) throws IOException, InterruptedException {

		return InterProParser.getXmlInformation(xml);
	}

}
