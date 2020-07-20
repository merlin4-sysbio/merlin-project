/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.hmmer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fxe
 */

public class HmmerScan {

	private static Logger logger = LoggerFactory.getLogger(HmmerScan.class);

	//private String email;
	private String sequence;
	private String header = "seq";
	private static final String EBI_URL = "https://www.ebi.ac.uk/Tools/hmmer/search/phmmer";
	private String database = "uniprotkb"; //SET AS DEFAULT
	//nr, uniprotkb, swissprot, pdb, env_nr, unimes, rp

//	/**
//	 * @param seq
//	 */
//	public HmmerScan(String seq) {
//		this.seq = seq;
//	}

//	/**
//	 * @param seq
//	 * @param header
//	 */
//	public HmmerScan(String seq, String header) {
//
//		this.seq = seq;
//		this.header = header;
//	}

	/**
	 * @param sequence
	 * @param header
	 * @param database
	 * @param email
	 */
	public HmmerScan(String sequence, String header, String database){//, String email) {

		this.sequence = sequence;
		this.header = header;
		this.database = database;
	//	this.email = email;
	}

	/**
	 * @param db
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * @return
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @return
	 * @throws IOException 
	 */
//	public String HmmerSubmitJobOld() throws IOException {
//		URL respUrl;
//		HttpURLConnection httpcon = null;
//		try {
//			URL url = new URL(EBI_URL);
//			httpcon = (HttpURLConnection) url.openConnection();
//			httpcon.setDoInput(true);
//			httpcon.setDoOutput(true);
//			httpcon.setInstanceFollowRedirects(false);
//			httpcon.setRequestMethod("POST");
//			httpcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//			httpcon.setRequestProperty("Accept", "application/json");
//			String fasta = ">" + header + "\n" + seq;
//			String urlParameters = "seqdb=" + URLEncoder.encode(db, "UTF-8") + "&seq=" + URLEncoder.encode(fasta, "UTF-8");
//
//			System.out.println(urlParameters);
//
//			httpcon.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
//
//			DataOutputStream wr = new DataOutputStream (httpcon.getOutputStream ());
//			wr.writeBytes (urlParameters);
//			wr.flush ();
//			wr.close ();
//			respUrl = new URL( httpcon.getHeaderField( "Location" ));
//			return respUrl.toString();
//
//		} catch (MalformedURLException e) {
//			System.out.println(httpcon.getURL());
//			throw e;
//		}
//	}

	/**
	 * @param date
	 * @param cancel
	 * @return
	 * @throws IOException
	 */
	public String hmmerSubmitJob() throws IOException {

		String res = null;
		//
		String fasta = ">" + header + "\n" + sequence;
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(EBI_URL);
		List<NameValuePair> nameValuePairs = new ArrayList<> ();
		Map<String, String> params = new HashMap<> ();
//		params.put("email", this.email);
		params.put("seqdb", this.database);
		params.put("seq", fasta);
		for (String key : params.keySet())
			nameValuePairs.add(new BasicNameValuePair(key, params.get(key)));
		
		
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		logger.debug("line {}", httpPost.getRequestLine() );
		
		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		
		logger.debug("status {}", httpResponse.getStatusLine());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
		
		StringBuilder responseString = new StringBuilder();
		Pattern pattern = Pattern.compile("http*?.+score");
		
		String readline;
		
		while ((readline = br.readLine()) != null) {
			
			Matcher matcher = pattern.matcher(readline);

			while(matcher.find()) {

				String s = matcher.group();
				res = new String(s);

				s = s.substring(4);
				matcher = pattern.matcher(s);
			}
			responseString.append(readline+"\n");
			
		}
		br.close();

		
		//LOGGER.debug(res);
		
		if(res==null) {
			
			//System.out.println(responseString.toString());
		}
		//else 
			//System.out.println("res "+header+"\t"+res);

		return res;
	}

	/**
	 * @return the seq
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSequence(String seq) {
		this.sequence = seq;
	}

	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}
}
