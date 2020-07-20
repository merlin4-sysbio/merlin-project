package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.interpro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;


/*
 @author Joao Sequeira
 */

public class InterProClientRest  {

	private static final String BASE_URL = "http://www.ebi.ac.uk/Tools/services/rest/iprscan5/";
//	private static final String JOB_STATUS_URL = "http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/status/%s";
//	private static final String RESULT_TYPE_URL = "http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/resulttypes/%s";
//	private static final String GET_RESULT_URL = "http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/result/%s/%s";
	//private static final Logger LOGGER = Logger.getLogger(InterProClientRest.class);
//	private static final String email = "odias@deb.uminho.pt";
//	private static final String tool = "www.merlin_sysbio.org";




	/**
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHttpUrl(String url) throws ClientProtocolException, IOException {
		
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httpResponse = httpClient.execute(httpGet);

		HttpEntity httpEntity = httpResponse.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
		StringBuilder responseString = new StringBuilder();
		
		String readline;
		while ((readline = br.readLine()) != null)
			responseString.append(readline).append('\n');

		br.close();

		return responseString.toString().trim();
	}

	/**
	 * @param jobId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getJobStatus(String jobId) throws ClientProtocolException, IOException {

		return getHttpUrl(String.format(BASE_URL, "status/", jobId));
	}

	public static String getJobResultType(String jobId) throws ClientProtocolException, IOException {
		return getHttpUrl(String.format(BASE_URL, "resulttypes/", jobId));
	}

	public static String getJobResultType(String jobId, String type) throws ClientProtocolException, IOException {
		return getHttpUrl(String.format(BASE_URL, jobId,"/", type));
	}
}