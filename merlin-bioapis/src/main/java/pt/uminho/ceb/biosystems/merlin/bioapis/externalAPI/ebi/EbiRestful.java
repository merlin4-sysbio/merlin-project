package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro.InterProClientRest;

public class EbiRestful {

	private List<String> result;

	/**
	 * Make EBI request.
	 * 
	/**
	 * @param tool
	 * @param nameValuePairs
	 * @return
	 * @throws IOException 
	 * @throws  
	 */
	public static String makeRequest (String tool, List<NameValuePair> nameValuePairs) throws IOException {

		StringBuilder responseString = new StringBuilder();
		HttpPost httpPost = new HttpPost("http://www.ebi.ac.uk/Tools/services/rest/"+tool+"/run/");
		HttpClient httpClient = HttpClientBuilder.create().build();

			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));

			String readline;
			while ((readline = br.readLine()) != null)
				responseString.append(readline);

		return responseString.toString();
	}

	/**
	 * Get xml result.
	 * Output format: out.
	 * 
	 * @param identifier
	 * @param tool
	 * @param waitingPeriod
	 * @param cancel
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static List<String> getXml(String identifier, String tool, long waitingPeriod, AtomicBoolean cancel) throws ClientProtocolException, IOException, InterruptedException {

		return EbiRestful.getXml(identifier, tool, "out", waitingPeriod, cancel);
	}

	/**
	 * Get xml result.
	 * 
	 * @param identifier
	 * @param tool
	 * @param outputformat
	 * @param waitingPeriod 
	 * @param cancel 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static List<String> getXml(String identifier, String tool, String outputformat, long waitingPeriod, AtomicBoolean cancel) throws ClientProtocolException, IOException, InterruptedException {

		String status = InterProClientRest.getHttpUrl("http://www.ebi.ac.uk/Tools/services/rest/"+tool.toString().toLowerCase()+"/status/"+identifier);

		long wait = 0;
		boolean go = status.equals("RUNNING");

		if (status.equalsIgnoreCase("ERROR") || status.equalsIgnoreCase("NOT_FOUND") || status.equalsIgnoreCase("FAILURE"))
			throw new IOException("Job ID not found.");

		while(go && wait<waitingPeriod && !cancel.get()) {

			status = InterProClientRest.getHttpUrl("http://www.ebi.ac.uk/Tools/services/rest/"+tool.toString().toLowerCase()+"/status/"+identifier);
			go = status.equals("RUNNING");

			if (status.equalsIgnoreCase("ERROR") || status.equalsIgnoreCase("NOT_FOUND") || status.equalsIgnoreCase("FAILURE"))
				throw new IOException("Job ID not found.");	

			int lag = 5000;
			Thread.sleep(lag);
			wait += lag;
		}

		if(!go) {

			String result = InterProClientRest.getHttpUrl("http://www.ebi.ac.uk/Tools/services/rest/"+tool.toString().toLowerCase()+"/result/"+identifier+"/"+outputformat);
			List<String> resultList = Arrays.asList(result.split("\n"));
			return resultList;
		}
		else {

			throw new InterruptedException("No answer for request "+identifier);
		}
	}

	/**
	 * Get web service result.
	 * 
	 * @return result.
	 */
	public List<String> getResult() {

		return this.result;
	}
}