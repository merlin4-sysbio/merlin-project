package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.blast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.biojava.nbio.core.sequence.template.Compound;
import org.biojava.nbio.core.sequence.template.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.RemotePairwiseAlignmentOutputProperties;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.RemotePairwiseAlignmentProperties;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.RemotePairwiseAlignmentService;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.qblast.NCBIQBlastAlignmentProperties;


/**
 * @author Oscar
 *
 */
public class EbiBlastClientRest implements RemotePairwiseAlignmentService {

	//private static final String BLAST_PARAMETER_DETAILS_URL = "http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/parameterdetails/";
	private static final String BLAST_RUN_URL = "http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/run/";
	private static final String JOB_STATUS_URL = "http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/status/%s";
	private static final String RESULT_TYPE_URL = "http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/resulttypes/%s";
	private static final String GET_RESULT_URL = "http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/result/%s/%s";
	private static final Logger logger = LoggerFactory.getLogger(EbiBlastClientRest.class);
	private String email = "";
	private static final String tool = "www.merlin_sysbio.org";
	private static final int _DEFAULT_TIMEOUT = 10000;

	private ConcurrentHashMap<String, Long> holder;
	private long step;
	private long start;
	private int timeout;

	/**
	 * @param timeout
	 * @param email
	 */
	public EbiBlastClientRest(int timeout, String email) {

		this.email = email;
		this.timeout = timeout;
		this.holder = new ConcurrentHashMap<String, Long>();
	}
	/**
	 * @param operation
	 * @param maxTries
	 * @param args
	 * @return
	 * 
	 *		URL	http://www.ebi.ac.uk/Tools/services/rest/ncbiblast/run/
	 *    	POSTemail	 User e-mail address. See Why do you need my e-mail address?
	 *		title	 an optional title for the job.
	 *		program	Blast program to use to perform the search.
	 *		matrix	 Scoring matrix to be used in the search.
	 *		alignments	 Maximum number of alignments displayed in the output.
	 *		scores	 Maximum number of scores displayed in the output.
	 *		exp	 E-value threshold.
	 *		dropoff	 Amount score must drop before extension of hits is halted.
	 *		match_scores	 Match/miss-match scores to generate a scoring matrix for for nucleotide searches.
	 *		gapopen	 Penalty for the initiation of a gap.
	 *		gapext	 Penalty for each base/residue in a gap.
	 *		filter	 Low complexity sequence filter to process the query sequence before performing the search.
	 *		seqrange	 Region of the query sequence to use for the search. Default: whole sequence.
	 *		gapalign	 Perform gapped alignments.
	 *		compstats	 Compositional adjustment or compositional statistics mode to use.
	 *		align	 Alignment format to use in output.
	 *		stype	 Query sequence type. One of: dna, rna or protein.
	 *		sequence	 Query sequence. The use of fasta formatted sequence is recommended.
	 *		database	 List of database names for search.
	 * @throws Exception 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String sendAlignmentRequest(String query, RemotePairwiseAlignmentProperties rpap) throws Exception {

		try {
			
			NCBIQBlastAlignmentProperties rqb = (NCBIQBlastAlignmentProperties) rpap;
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(this.timeout)
		            .setSocketTimeout(this.timeout)
		            .setConnectionRequestTimeout(this.timeout).build();
			
			HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
			HttpPost httpPost = new HttpPost(BLAST_RUN_URL);
//			if(httpPost.started();)
			{
				List<NameValuePair> nameValuePairs = new ArrayList<> ();

			Map<String, String> params = new HashMap<> ();
			params.put("email", email);
			params.put("title", tool);
			//params.put("gapalign", rqb.getBlastGapCreation());
			params.put("database", rqb.getBlastDatabase());
			params.put("alignments", rqb.getHitlistSize()+"");
			
			params.put("stype", rqb.getType());
			params.put("program", rqb.getBlastProgram());
			String exp = (rqb.getBlastExpect()+"").replace(".0","").toLowerCase();
			params.put("exp", exp);
			params.put("gapopen", rqb.getBlastGapCreation()+"");
			params.put("gapext", rqb.getBlastGapExtension()+"");
			params.put("sequence", query);

			for (String key : params.keySet()) {
				nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			logger.debug("line {}, {}", httpPost.getRequestLine(), nameValuePairs);
			
			//System.out.println(httpPost.getRequestLine());
			
			CloseableHttpResponse httpResponse = (CloseableHttpResponse) httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
			StringBuilder responseString = new StringBuilder();
			
			String readline;
			while ((readline = br.readLine()) != null) {
				responseString.append(readline);
			}

			br.close();
			httpPost.releaseConnection();

			start = System.currentTimeMillis() + step;
			holder.put(responseString.toString(), start);
			EntityUtils.consume(httpEntity);
			httpResponse.close();
			
			return responseString.toString();
			}
		}
		catch (Exception e) {
			
			e.printStackTrace();
			throw e;
		}

	}
	
//	/**
//	 * @return
//	 * @throws ClientProtocolException
//	 * @throws IOException
//	 */
//	public static String getDatabases() throws ClientProtocolException, IOException {
//		
		//TODO
//		HttpClient httpClient = HttpClientBuilder.create().build();
//		HttpPost httpPost = new HttpPost(BLAST_PARAMETER_DETAILS_URL+"database");
//
//	
//		liu
//		
//		LOGGER.debug(httpPost.getRequestLine());
//		HttpResponse httpResponse = httpClient.execute(httpPost);
//		HttpEntity httpEntity = httpResponse.getEntity();
//		BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
//		StringBuilder responseString = new StringBuilder("line");
//		
//		String readline;
//		while ((readline = br.readLine()) != null) {
//			responseString.append(readline);
//		}
//
//		br.close();
//
//		return responseString.toString();
	//	
//	}

	/**
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHttpUrl(String url) throws ClientProtocolException, IOException{
		
		HttpGet httpGet = new HttpGet(url);
		RequestConfig config = RequestConfig.custom()
				.setConnectTimeout(_DEFAULT_TIMEOUT)
	            .setSocketTimeout(_DEFAULT_TIMEOUT)
	            .setConnectionRequestTimeout(_DEFAULT_TIMEOUT).build();
		
		HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		CloseableHttpResponse httpResponse = (CloseableHttpResponse) httpClient.execute(httpGet);

		HttpEntity httpEntity = httpResponse.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
		StringBuilder responseString = new StringBuilder();
		String readline;
		
		while ((readline = br.readLine()) != null)
			responseString.append(readline).append('\n');

		br.close();
		EntityUtils.consume(httpEntity);
		httpResponse.close();

		return responseString.toString().trim();
	}

	/**
	 * @param jobId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getJobStatus(String jobId) throws ClientProtocolException, IOException {

		return getHttpUrl(String.format(JOB_STATUS_URL, jobId));
	}

	public static String getJobResultType(String jobId) throws ClientProtocolException, IOException {
		return getHttpUrl(String.format(RESULT_TYPE_URL, jobId));
	}

	public static String getJobResultType(String jobId, String type) throws ClientProtocolException, IOException {
		return getHttpUrl(String.format(GET_RESULT_URL, jobId, type));
	}

	/**
	 * @author Oscar
	 *
	 */
	public enum Operation {

		run,
		parameters,
		parameterdetails,
		status,
		resulttypes,
		result
	}

	/**
	 * @author Oscar
	 *
	 */
	public enum Status {

		RUNNING,
		FINISHED,
		ERROR,
		FAILURE,
		FOUND
	}

	/* (non-Javadoc)
	 * @see alignment.blast.org.biojava3.ws.alignment.RemotePairwiseAlignmentService#sendAlignmentRequest(org.biojava3.core.sequence.template.Sequence, alignment.blast.org.biojava3.ws.alignment.RemotePairwiseAlignmentProperties)
	 */
	@Override
	public String sendAlignmentRequest(Sequence<Compound> seq, RemotePairwiseAlignmentProperties rpa) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see alignment.blast.org.biojava3.ws.alignment.RemotePairwiseAlignmentService#isReady(java.lang.String, long)
	 */
	@Override
	public boolean isReady(String jobID, long present) throws Exception {

		boolean isReady = false;
		if (holder.containsKey(jobID)) {

			/*
			 * If present time is less than the start of the search added to
			 * step obtained from NCBI, just do nothing ;-)
			 * 
			 * This is done so that we do not send zillions of requests to the
			 * server. We do the waiting internally first.
			 */
			if (present < start) {

				isReady = false;
			}
			/*
			 * If we are at least step seconds in the future from the actual
			 * call sendAlignementRequest()
			 */
			else {

				String status = getJobStatus(jobID);
				if (status.equalsIgnoreCase(Status.FINISHED.toString())) {

					//System.out.println(line);
					isReady = true;
					//return true;
				} 
				else if (status.equalsIgnoreCase(Status.RUNNING.toString())) {
					/*
					 * Else, move start forward in time... for the next
					 * iteration
					 */
					start = present + step;
					holder.put(jobID, start);
					return false;
				}
				else {

					throw new Exception("No Status for request ID "+jobID);
				}
			}

		}
		else {

			throw new Exception("Impossible to check for request ID named "
					+ jobID + " because it does not exists!\n");
		}
		return isReady;
	}


	@Override
	public InputStream getAlignmentResults(String jobID, RemotePairwiseAlignmentOutputProperties out) throws Exception {

		String output = getJobResultType(jobID, "out");
		
		byte[] data = output.getBytes();
		return new ByteArrayInputStream(data);
	}

	/**
	 * @return the holder
	 */
	public ConcurrentHashMap<String, Long> getHolder() {
		return holder;
	}

	/**
	 * @param holder the holder to set
	 */
	public void setHolder(ConcurrentHashMap<String, Long> holder) {
		this.holder = holder;
	}
}