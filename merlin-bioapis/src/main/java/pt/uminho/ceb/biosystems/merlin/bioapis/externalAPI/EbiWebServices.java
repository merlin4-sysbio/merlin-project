package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.EbiRestful;

/**
 * @author Oscar Dias
 *
 */
public class EbiWebServices {

	/**
	 * Run InterProScan web service.
	 * 
	 * @param email
	 * @param title
	 * @param sequence
	 * @param goterms
	 * @param pathways
	 * @param appls
	 * @return
	 * @throws IOException 
	 */
	public static String runInterProScan(String email, String title, AbstractSequence<?> sequence, String goterms, String pathways, List<String> appls) throws IOException {
       
        List<NameValuePair> nameValuePairs = new ArrayList<> ();
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("title", title));
        nameValuePairs.add(new BasicNameValuePair("goterms", goterms));
        nameValuePairs.add(new BasicNameValuePair("pathways", pathways));
        nameValuePairs.add(new BasicNameValuePair("sequence", sequence.getSequenceAsString()));
        for (String appl : appls)
        	nameValuePairs.add(new BasicNameValuePair("appl", appl));
        
		return EbiRestful.makeRequest("iprscan5",nameValuePairs);
	}
	
	
	/**
	 * Run phobius web service.
	 * Parameter:
	 * 	stype - protein.
	 * 	format - short.
	 * 
	 * 
	 * @param email
	 * @param title
	 * @param sequence
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String runPhobius(String email, String title, AbstractSequence<?> sequence) throws ClientProtocolException, IOException {
	    
        return  EbiWebServices.runPhobius(email, title, sequence, "protein", "short");
	}
	
	/**
	 * Run phobius web service.
	 * 
	 * http://www.ebi.ac.uk/Tools/webservices/services/pfa/phobius_rest
	 * 
	 * @param email
	 * @param title
	 * @param sequence
	 * @param stype
	 * @param format
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String runPhobius(String email, String title, AbstractSequence<?> sequence, String stype, String format) throws ClientProtocolException, IOException {
	    
		List<NameValuePair> nameValuePairs = new ArrayList<> ();
	    nameValuePairs.add(new BasicNameValuePair("email", email));
	    nameValuePairs.add(new BasicNameValuePair("title", title));
	    nameValuePairs.add(new BasicNameValuePair("stype", stype));
	    nameValuePairs.add(new BasicNameValuePair("format", format));
	    nameValuePairs.add(new BasicNameValuePair("sequence", ">"+sequence.getOriginalHeader()+"\n"+sequence.getSequenceAsString()));
	    
        return  EbiRestful.makeRequest("phobius", nameValuePairs);
	}

}
