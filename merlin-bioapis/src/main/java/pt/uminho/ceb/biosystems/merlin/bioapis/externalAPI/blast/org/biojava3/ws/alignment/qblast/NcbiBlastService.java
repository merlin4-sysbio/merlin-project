/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */
package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.qblast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentHashMap;

import org.biojava.nbio.core.sequence.template.Compound;
import org.biojava.nbio.core.sequence.template.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.RemotePairwiseAlignmentOutputProperties;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.RemotePairwiseAlignmentProperties;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.blast.org.biojava3.ws.alignment.RemotePairwiseAlignmentService;

/**
 * NCBIQBlastService - A simple way of submitting Blast request to the QBlast
 * service at NCBI.
 * 
 * <p>
 * NCBI provides a Blast server through a CGI-BIN interface. NCBIQBlastService
 * simply encapsulates an access to it by giving users access to get/set methods
 * to fix sequence, program and database as well as advanced options.
 * </p>
 * 
 * <p>
 * The philosophy behind NCBIQBlastService is to disconnect submission of Blast
 * requests from collection of Blast results. This is done so to allow a user to
 * submit multiple Blast requests while allowing recovery of the reports at a
 * later time.
 * </p>
 * 
 * <p>
 * Presently, only blastall programs are accessible. blastpgp and megablast are
 * high-priorities.
 * </p>
 * 
 * <p>
 * Many thanks to Matthew Busse for helping in debugging after the migration
 * from BJ1.7 to BJ3.0.
 * </p>
 * 
 * @author Sylvain Foisy, Diploide BioIT
 * @since Biojava 3
 * 
 */
/**
 * @author ODias
 *
 */
public class NcbiBlastService implements RemotePairwiseAlignmentService {

	private static String baseurl = "https://blast.ncbi.nlm.nih.gov/Blast.cgi";
	final static Logger logger = LoggerFactory.getLogger(NcbiBlastService.class);
	
	private URL aUrl;
	private URLConnection uConn;
	private OutputStreamWriter fromQBlast;
	private BufferedReader rd;

	private String email = "support@merlin-sysbio.org";
	private String tool = "www.merlin-sysbio.org";

	private String cmd = null;
	private String seq = null;
	private String tmp = null;

	private String rid;
	private long step;
	private long start;
	private ConcurrentHashMap<String, Long> holder;
	private int timeout = 180000; //default timeout 3 minutes
	//private Proxy proxy;

	/**
	 * The constructor for a QBlast service request.
	 * 
	 * @throws Exception
	 *             if the NCBI URL is unresponsive
	 * 
	 */
	public NcbiBlastService() throws Exception {
		
		try  {
			
			this.aUrl = new URL(baseurl);
			this.uConn = setQBlastServiceProperties(aUrl.openConnection());
			this.uConn.setConnectTimeout(this.timeout);
			this.holder = new ConcurrentHashMap<String, Long>();
		}
		/*
		 * Needed but should never be thrown since the URL is static and known
		 * to exist
		 */
		catch (MalformedURLException e) {
			throw new Exception(
					"It looks like the URL for NCBI QBlast service is wrong.\n");
		}
		/*
		 * Intercept if the program can't connect to QBlast service
		 */
		catch (IOException e) {
			throw new Exception(
					"Impossible to connect to QBlast service at this time. Check your network connection.\n");
		}
	}

	/**
	 * @param timeout
	 * @throws Exception
	 */
//	public NcbiBlastService(int timeout) throws Exception {
//		this.timeout = timeout;
//
//		try {
//
//			this.aUrl = new URL(baseurl);
//			this.uConn = setQBlastServiceProperties(aUrl.openConnection());//this.uConn = setQBlastServiceProperties(aUrl.openConnection(this.proxy));
//
//			this.uConn.setConnectTimeout(this.timeout);
//			this.holder = new ConcurrentHashMap<String, Long>();
//		}
//		/*
//		 * Needed but should never be thrown since the URL is static and known
//		 * to exist
//		 */
//		catch (MalformedURLException e) {
//			throw new Exception(
//					"It looks like the URL for NCBI QBlast service is wrong.\n");
//		}
//		/*
//		 * Intercept if the program can't connect to QBlast service
//		 */
//		catch (IOException e) {
//			throw new Exception(
//					"Impossible to connect to QBlast service at this time. Check your network connection.\n");
//		}
//	}

	/**
	 * @param timeout
	 * @param email
	 * @throws Exception
	 */
	public NcbiBlastService(int timeout, String email) throws Exception {

		this.timeout = timeout;
		try {

			this.aUrl = new URL(baseurl);
			this.uConn = setQBlastServiceProperties(aUrl.openConnection());//this.uConn = setQBlastServiceProperties(aUrl.openConnection(proxy));		
			this.uConn.setConnectTimeout(this.timeout);
			this.holder = new ConcurrentHashMap<String, Long>();
			this.email = email;
		}
		/*
		 * Needed but should never be thrown since the URL is static and known
		 * to exist
		 */
		catch (MalformedURLException e) {

			throw new Exception(
					"It looks like the URL for NCBI QBlast service is wrong.\n");
		}
		/*
		 * Intercept if the program can't connect to QBlast service
		 */
		catch (IOException e) {

			throw new Exception(
					"Impossible to connect to QBlast service at this time. Check your network connection.\n");
		}
	}

	/**
	 * 
	 * This class is the actual worker doing all the dirty stuff related to
	 * sending the Blast request to the CGI_BIN interface. It should never be
	 * used as is but any method wanting to send a Blast request should manage
	 * to use it by feeding it the right parameters.
	 * 
	 * @param str
	 *            : a <code>String</code> representation of a sequence from
	 *            either of the three wrapper methods: Sequence, sequence
	 *            string, gid
	 * @param rpa
	 *            :a <code>RemotePairwiseAlignmentProperties</code> object
	 * 
	 * @return rid : the ID of this request on the NCBI QBlast server
	 * 
	 * @throws Exception
	 *             if unable to connect to the NCBI QBlast service
	 * 
	 */
	private String sendActualAlignementRequest(String str,
			NCBIQBlastAlignmentProperties rpa) throws Exception {

		rpa.setBlastCommandsToQBlast();

		if (str.length() == 0 || str == null) {
			
			throw new Exception("Impossible to execute QBlast request. Your sequence info has not been set correctly.\n");
		} 
		else {
			seq = "&QUERY=" + str;
		}
		
		cmd = rpa.getBlastCommandsToQBlast() + "&TOOL=" + this.getTool() + "&EMAIL=" + this.getEmail();

		// Let's end with the sequence's string
		cmd = seq + "&" + cmd;
		/*
		 * DEBUG LINE
		 */
		logger.debug(baseurl+this.cmd);
		String line = "";

		try {

			uConn.setConnectTimeout(this.timeout);
			uConn = setQBlastServiceProperties(aUrl.openConnection());//uConn = setQBlastServiceProperties(aUrl.openConnection(this));
			fromQBlast = new OutputStreamWriter(uConn.getOutputStream());
			fromQBlast.write(cmd);
			fromQBlast.flush();
			// Get the response
			rd = new BufferedReader(new InputStreamReader(
					uConn.getInputStream()));

			line = "";

			while ((line = rd.readLine()) != null && !line.trim().toLowerCase().contains("</html>") && rd.ready()) {
				
				logger.trace(line);
				/*
				 * If there are no error present, capture RID
				 */
				if (!line.contains("class=\"error\"") && !line.contains("Message ID#") ) {

					if (line.contains("RID = ") && !line.contains("myncbi")) {

						String[] arr = line.split("=");
						rid = arr[1].trim();
					}
					else if (line.contains("RTOE = ") && !line.contains("myncbi")) {
						
						logger.debug(line);
						String[] arr = line.split("=");
						step = Long.parseLong(arr[1].trim()) * 1000;
						start = System.currentTimeMillis() + step;
						holder.put(rid, start);
					}
					else if (line.contains("Request ID") && !line.contains("myncbi")) {
						
						String[] arr = line.split("value=\"");
						rid = arr[1].split("\"")[0];
						step = 30;
						start = System.currentTimeMillis() + step;
						holder.put(rid, start);
					}
				}
				/*
				 * On the contrary, if QBlast send a message error...
				 */
				else {

					// Capture everything to the left of this HTML statement...
					String[] tmp = line.split("</p></li></ul>");

					// Only the error message is on the right side of this...
					//String[] moreTmp = tmp[0].split("<p class=\"error\">");
					//throw new Exception("NCBI QBlast refused this request because: "+ moreTmp[1].trim() + "\n");
					throw new Exception("NCBI QBlast refused this request because: "+ tmp[0].trim() + "\n");
				}
			}
		}
		catch (IOException e) {
			
			logger.error(e.getMessage());
			throw new IOException("Can't submit sequence to BLAST server at this time.\n");
		} 
		catch (StackOverflowError e) {

			logger.error("BLAST Service stack overflow");
			logger.error(e.getMessage());
			System.gc();
			throw new IOException(
					"STACK OVERFLOW!!!!!!!!!!.\n");
		}
		catch (Exception e) {

			logger.error(e.getMessage());
			throw new Exception("BLAST unsigned exception.\n");
		}

		if(rid!=null) {

			return rid;
		}
		else {

			throw new Exception("Unable to retrieve RID because of a server error fpr sequence "+seq);
		}
	}

	/**
	 * This method is a wrapper that executes the Blast request via the Put
	 * command of the CGI-BIN interface with the specified parameters and a
	 * string representing the sequence. It gets the estimated time of
	 * completion by capturing the value of the RTOE variable and sets a loop
	 * that will check for completion of analysis at intervals specified by
	 * RTOE.
	 * 
	 * <p>
	 * It also capture the value for the RID variable, necessary for fetching
	 * the actual results after completion.
	 * </p>
	 * 
	 * @param str
	 *            : a <code>String</code> with an identifier like RefSeq, etc.
	 * @param rpa
	 *            : a <code>NCBIQBlastAlignmentProperties</code> object
	 * @return rid : a <code>String</code> with the request ID for this sequence
	 * @throws Exception
	 *             if it is not possible to sent the Blast command
	 */
	public String sendAlignmentRequest(String str,
			RemotePairwiseAlignmentProperties rpa) throws Exception {

		/*
		 * sending the command to execute the Blast analysis
		 */
		rid = sendActualAlignementRequest(str,
				(NCBIQBlastAlignmentProperties) rpa);
		int counter = 0;
		
		while(rid.length()>12 || rid.length()<10 || !isAllUpper(rid) || rid.contains(" ") || rid.contains("_") || rid == null) {

			if(counter>50) {

				logger.debug("New rid error. New rid = \t"+rid);
				throw new Exception("Cannot get RID for alignment");
			}
			logger.debug("rid id error asking for new rid\t"+rid);
			rid = sendActualAlignementRequest(str, (NCBIQBlastAlignmentProperties) rpa);
			logger.debug("new rid\t"+rid);
			counter++;
		}

		return rid;
	}

	/**
	 * @param s
	 * @return
	 */
	private static boolean isAllUpper(String s) {

		for(char c : s.toCharArray()) {

			if(Character.isLetter(c) && Character.isLowerCase(c)) {

				return false;
			}
		}
		return true;
	}


	/**
	 * This method is a wrapper that executes the Blast request via the Put
	 * command of the CGI-BIN interface with the specified parameters and a
	 * Sequence. It gets the estimated time of completion by capturing the value
	 * of the RTOE variable and sets a loop that will check for completion of
	 * analysis at intervals specified by RTOE.
	 * 
	 * <p>
	 * It also capture the value for the RID variable, necessary for fetching
	 * the actual results after completion.
	 * </p>
	 * 
	 * @param rs
	 *            :a <code>Sequence</code> object
	 * @param rpa
	 *            :a <code>NCBIQBlastAlignmentProperties</code> object
	 * @return rid : a <code>String</code> with the request ID for this sequence
	 * 
	 * @throws Exception
	 *             if it is not possible to sent the Blast command
	 */
	public String sendAlignmentRequest(Sequence<Compound> rs,
			RemotePairwiseAlignmentProperties rpa) throws Exception {

		tmp = rs.getSequenceAsString();

		return rid = sendActualAlignementRequest(tmp,
				(NCBIQBlastAlignmentProperties) rpa);
	}

	/**
	 * This method is a wrapper that executes the Blast request via the Put
	 * command of the CGI-BIN interface with the specified parameters and a
	 * GenBank GID. It gets the estimated time of completion by capturing the
	 * value of the RTOE variable and sets a loop that will check for completion
	 * of analysis at intervals specified by RTOE.
	 * 
	 * <p>
	 * It also capture the value for the RID variable, necessary for fetching
	 * the actual results after completion.
	 * </p>
	 * 
	 * @param gid
	 *            :an integer with a Genbank GID
	 * @param rpa
	 *            :a <code>NCBIQBlastAlignmentProperties</code> object
	 * @return rid : a String with the request ID for this sequence
	 * @throws Exception
	 *             if it is not possible to sent the Blast command
	 */
	public String sendAlignmentRequest(int gid,
			RemotePairwiseAlignmentProperties rpa) throws Exception {

		tmp = Integer.toString(gid);
		return rid = sendActualAlignementRequest(tmp,
				(NCBIQBlastAlignmentProperties) rpa);
	}

	/**
	 * <p>
	 * This method is used only for the executeBlastSearch method to check for
	 * completion of request using the NCBI specified RTOE variable
	 * </p>
	 * 
	 * @param id
	 *            : a valid request ID
	 * @param present
	 *            : a representation of "now" using System.currentTimeMillis().
	 * @return a boolean value telling if the request has been completed or not.
	 */
	public boolean isReady(String id, long present) throws Exception {

		boolean isReady = false;
		String check = "CMD=Get&RID=" + id;

		if (holder.containsKey(id)) {

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

				try {

					uConn = setQBlastServiceProperties(aUrl.openConnection());//uConn = setQBlastServiceProperties(aUrl.openConnection(this.proxy));
					uConn.setConnectTimeout(this.timeout);
					fromQBlast = new OutputStreamWriter(uConn.getOutputStream());
					fromQBlast.write(check);
					fromQBlast.flush();

					rd = new BufferedReader(new InputStreamReader(
							uConn.getInputStream()));

					String line = "";

					while ((line = rd.readLine()) != null) {

						if(line.contains("Status=")) {

							if (line.contains("Status=READY")) {

								isReady = true;
								return true;
							} 
							else if (line.contains("Status=WAITING")) {
								/*
								 * Else, move start forward in time... for the next
								 * iteration
								 */
								start = present + step;
								holder.put(id, start);
								return false;
							}
							else {

								throw new Exception("No Status for request ID "+id);
							}
						}
					}
				}
				catch (StackOverflowError e) {

					logger.error("BLAST Service stack overflow");
					logger.error(e.getMessage());
					System.gc();
					throw new IOException(
							"STACK OVERFLOW!!!!!!!!!!.\n");
				}
				catch (Exception e)  {

					e.printStackTrace();
					throw new Exception("Blast Failed");
				}
				finally {
					
					fromQBlast.close();
				}
			}
		} 
		else {

			throw new Exception("Impossible to check for request ID named "+ id + " because it does not exists!\n");
		}
		return isReady;
	}

	/**
	 * <p>
	 * This method extracts the actual Blast report for this request ID. It uses
	 * an object implementing the RemotePairwiseAlignmentOutputProperties
	 * interface which will specify output formatting options.
	 * </p>
	 * 
	 * @param id
	 *            :a valid request ID
	 * @param rb
	 *            : a <code>RemotePairwiseAlignmentOutputProperties</code> that
	 *            will specify specific output formatting commands
	 * 
	 * @return an <code>InputStream</code> that can be use any way one might
	 *         desire
	 * 
	 * @throws Exception
	 *             if it is not possible to recover the results.
	 * 
	 */
	public InputStream getAlignmentResults(String id,
			RemotePairwiseAlignmentOutputProperties rb) throws Exception {

		if (holder.containsKey(id)) {
			
			String org = "";
			if(rb.getOutputOptions().contains("FORMAT_ENTREZ_QUERY")) {
			
				org=rb.getOutputOption("FORMAT_ENTREZ_QUERY") + "&";
			}

			String srid = "CMD=Get&RID=" + id + "&"
					+ rb.getOutputOption("FORMAT_TYPE") + "&"
					+ rb.getOutputOption("ALIGNMENT_VIEW") + "&"
					+ rb.getOutputOption("DESCRIPTIONS") + "&"
					+ org
					+ rb.getOutputOption("ALIGNMENTS") 
					//+ "&" 
					//+"&TOOL=" + getTool() + "&EMAIL=" + getEmail()
					;
			
			logger.debug("stream output" + srid);
			
			try {

				uConn = setQBlastServiceProperties(aUrl.openConnection());//uConn = setQBlastServiceProperties(aUrl.openConnection(this.proxy));

				fromQBlast = new OutputStreamWriter(uConn.getOutputStream());
				fromQBlast.write(srid);
				fromQBlast.flush();

				return uConn.getInputStream();

			}
			catch (IOException ioe) {

				throw new Exception(
						"It is not possible to fetch Blast report from NCBI at this time.\n");
			}
		} 
		else {

			throw new Exception(
					"Impossible to get output for request ID named " + id
					+ " because it does not exists!\n");
		}
	}

	/**
	 * A simple method to check the availability of the QBlast service
	 * 
	 * @throws Exception
	 *             if unable to connect to the NCBI QBlast service
	 */
	public void printRemoteBlastInfo() throws Exception {

		try {

			OutputStreamWriter out = new OutputStreamWriter(
					uConn.getOutputStream());

			out.write("CMD=Info");
			out.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					uConn.getInputStream()));

			out.close();
			rd.close();
		} 
		catch (IOException e) {
			throw new Exception(
					"Impossible to get info from QBlast service at this time. Check your network connection.\n");
		}
	}

	/**
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	private URLConnection setQBlastServiceProperties(URLConnection conn) throws IOException {

		URLConnection tmp = conn;
		//conn.connect();
		conn.setDoOutput(true);
		conn.setUseCaches(false);

		tmp.setRequestProperty("User-Agent", "merlin/Biojava/NCBIQBlastService");
		tmp.setRequestProperty("Connection", "Keep-Alive");
		tmp.setRequestProperty("Content-type",
				"application/x-www-form-urlencoded");
//		tmp.setRequestProperty("Content-length", "200");

		return tmp;
	}

	/**
	 * Set the tool identifier for QBlast. Defaults to 'biojava3'.
	 * 
	 * @param tool
	 *            the new identifier.
	 */
	public void setTool(String tool) {

		this.tool = tool;
	}

	/**
	 * Get the tool identifier for QBlast. Defaults to 'biojava3'.
	 * 
	 * @return the identifier.
	 */
	public String getTool() {

		return this.tool;
	}

	/**
	 * Set the email for QBlast. Defaults to 'anonymous@biojava.org'.
	 * 
	 * @param email
	 *            the new email.
	 */
	public void setEmail(String email) {

		this.email = email;
	}

	/**
	 * Get the email for QBlast. Defaults to 'anonymous@biojava.org'.
	 * 
	 * @return the email.
	 */
	public String getEmail() {

		return this.email;
	}

	/**
	 * @param timeoutMillis
	 */
	public void setConnectTimeout(int timeoutMillis) {

		this.uConn.setConnectTimeout(timeoutMillis);
	}

	public void setReadTimeout(int timeoutMillis) {

		this.uConn.setReadTimeout(timeoutMillis);
	}
}