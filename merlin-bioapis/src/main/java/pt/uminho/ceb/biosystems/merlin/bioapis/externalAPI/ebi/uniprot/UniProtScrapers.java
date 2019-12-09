package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UniProtScrapers {

	private static final String UNIPROT_TAXONOMY_URL = "https://www.uniprot.org/taxonomy/";

	final static Logger logger = LoggerFactory.getLogger(UniProtScrapers.class);


	/**
	 * @param taxonomyID
	 * @param i
	 * @return
	 */
	public static String[] getUniProtTaxonomy(Long taxonomyID, int i){

		try {

			URL url = new URL(UNIPROT_TAXONOMY_URL + taxonomyID);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
			
			int responseCode = conn.getResponseCode();
			
			if(responseCode==200){

				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"utf-8"));

				boolean go = false;
				String html;
				Pattern pattern;
				Matcher matcher;

				String[] taxonomy = new String[2];

				while ((html = in.readLine()) != null){

					if(html.contains("<span property=\"schema:name up:scientificName\">")){

						pattern = Pattern.compile("<span property=\"schema:name up:scientificName\">(.+)</span></td></tr>");
						matcher = pattern.matcher(html);

						if(matcher.find())
							taxonomy[0] = matcher.group(1).trim();

					}

					if(go){
						if(html.contains("</tr>"))
							go=false;

						if(!html.contains("style=\"color: gray\"") && html.contains("property=\"rdfs:subClassOf\"")){

							pattern = Pattern.compile("property=\"rdfs:subClassOf\">(.+)</a><br/>");
							matcher = pattern.matcher(html);


							if(matcher.find())
								taxonomy[1] = taxonomy[1].concat(matcher.group(1).trim()).concat("; ");

						}
					}

					if(html.contains("Lineage")){
						go = true;
						taxonomy[1] = "";
					}
				}

				taxonomy[1] = taxonomy[1].replaceAll(";\\s$", "");

				return taxonomy;

			}

			else{
				logger.error("Could not access the requested URL (" + url.toString() +").\nResponse code: "+ responseCode);
				return null;
			}

		} catch(IOException e) {

			e.printStackTrace();

			logger.debug("getUniProtTaxonomy IOException trial {}.", i);
			i++;
			if(i<3)
				return getUniProtTaxonomy(taxonomyID, i);

			logger.error("getUniProtTaxonomy IOException error {}.", taxonomyID);
			logger.trace("StackTrace {}",e);

		} catch (Exception e1){

			e1.printStackTrace();
		}

		return null;
	}


}
