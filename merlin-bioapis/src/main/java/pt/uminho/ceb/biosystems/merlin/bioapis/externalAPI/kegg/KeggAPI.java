/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.datatypes.EntryData;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.datastructures.KeggCompoundER;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.datastructures.KeggECNumberEntry;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.datastructures.KeggOrthologyEntry;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.datastructures.KeggReactionInformation;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.collection.CollectionUtils;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;

public class KeggAPI {

	private static final Logger logger = LoggerFactory.getLogger(KeggAPI.class);
	private static final String ALL_PATHWAYS = "allPathways";
	static public Client client = Client.create();
	private static JCS cache;
	private static boolean debug = false;

	static {
		try {
			cache = JCS.getInstance("KEGG_API");
		}
		catch (CacheException e ) {
			e.printStackTrace();
		}

	}

	public static String[] getOrthologyByEnzyme(String ecNumber) throws Exception {
		if(!ecNumber.startsWith("ec:"))
			ecNumber = "ec:" + ecNumber;

		String result = KeggRestful.fetch(KeggOperation.link, "ko", ecNumber);
		String[] lines = result.split("\n");
		for(int i = 0; i < lines.length; i++)
			lines[i] = lines[i].split("\t")[1].replaceAll("ko:", "");

		return lines;
	}

	public static KeggOrthologyEntry getOrthology(String ko) throws Exception {
		if(!ko.startsWith("ko:"))
			ko = "ko:" + ko;

		KeggOrthologyEntry entry = (KeggOrthologyEntry) cache.get(ko);

		if(entry == null) {
			String in = KeggRestful.fetch(KeggOperation.get, ko);
			if(in != null) {
				Map<String, List<String>> result = parseFullEntry(in);
				Map<String, List<String>> dbAssociations = new HashMap<String, List<String>>();
				if(result.get("DBLINKS") != null)
					for(String dbEntries : result.get("DBLINKS")) {
						String[] split = dbEntries.split("\\s+");
						String db = split[0];
						dbAssociations.put(db, new ArrayList<String>());
						for(int i = 1; i < split.length; i++)
							dbAssociations.get(db).add(split[i]);
					}

				List<String> genes = new ArrayList<String>();
				if(result.get("GENES") != null)
					for(String organismGenes : result.get("GENES")) {
						String[] split = organismGenes.split("\\s+");
						String org = split[0];
						for(int i = 1; i < split.length; i++)
							genes.add(org.toLowerCase() + ":" + split[i].replaceAll("\\(.+\\)", ""));
					}

				List<String> references = null;
				String definition = null;
				if(result.get("DEFINITION") == null)
					definition = result.get("NAME").get(0);
				else
					definition = result.get("DEFINITION").get(0);

				entry = new KeggOrthologyEntry(
						result.get("ENTRY").get(0),
						result.get("NAME"),
						definition,
						result.get("PATHWAY"),
						result.get("MODULE"),
						result.get("DISEASE"),
						result.get("BRITE"),
						dbAssociations,
						genes,
						references);
				try {
					cache.put(ko, entry);

				} catch (CacheException e) {
					e.printStackTrace();
				}
			}

		}

		return entry;
	}

	public static List<KeggCompoundER> findCompound(String query) throws Exception {

		String [] res = KeggRestful.findCompoundByQuery(query);
		List<KeggCompoundER> compounds = new ArrayList<KeggCompoundER>();
		for(String data : res) {

			Map<String, List<String>> resultsParsed = parseFullEntry(data);
			String formula = getFirstIfExists(resultsParsed.get("FORMULA"));
			String id = getFirstIfExists(resultsParsed.get("ENTRY_ID"));
			String mass  = getFirstIfExists(resultsParsed.get("MOL_WEIGHT"));
			List<String> names = resultsParsed.get("NAME"); 
			List<String> ecnumbers = splitWhiteSpaces(resultsParsed.get("ENZYME"));
			List<String> reactions = splitWhiteSpaces(resultsParsed.get("REACTION"));
			List<String> pathways = resultsParsed.get("PATHWAY");
			List<String> crossRefs = resultsParsed.get("DBLINKS");

			if(ecnumbers == null) ecnumbers = new ArrayList<String>();
			if(reactions == null) reactions = new ArrayList<String>();
			if(pathways == null) pathways = new ArrayList<String>();

			String comment = CollectionUtils.join(resultsParsed.get("COMMENT"),"\n");
			KeggCompoundER toReturn = new KeggCompoundER(id, formula, mass, names, ecnumbers, reactions, pathways, crossRefs,comment);
			compounds.add(toReturn);
		}
		return compounds;
	}

	/**
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static String[] findGenesFromKO(String query) throws Exception {

		try {

			String [] res = KeggAPI.findGenesFromKO(query,0);
			return res;
		} 
		catch (Exception e) {

			System.out.println("Query "+query);
			throw e;
		}
	}

	/**
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static String[] findGenesFromKO(String query, int trial) throws Exception {

		try {

			String [] res = KeggRestful.findGenesFromKO(query);
			return res;
		} 
		catch (Exception e) {

			if(trial<=5) {

				try {

					int xMilliseconds = trial*60000;
					System.out.println("Waiting "+xMilliseconds+" milliseconds on KEGG API.");
					Thread.sleep(xMilliseconds);
				}	
				catch(InterruptedException ie) {

					ie.printStackTrace();
				}

				trial = trial + 1;
				return KeggAPI.findGenesFromKO(query,trial);
			}
			else {

				throw e;
			}
		}
	}

	/**
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static String[] findGenome(String query) throws Exception {

		String [] res = KeggRestful.findGenomeByQuery(query);
		return res;
	}
	
	
	/**
	 * Retrieve Tnumber from NCBI taxonomyID.
	 * 
	 * @param taxonomyID
	 * @return KeggIDs
	 * @throws Exception
	 */
	public static List<String> findKeggTaxonomyID(String taxonomyID) throws Exception{
		
		String[] keggTaxonomyEntry = KeggRestful.findKeggEntryByTaxonomyID(taxonomyID);
		List<String> keggTaxonomyID = new ArrayList<String>();
		
		for(int i=0 ; i<keggTaxonomyEntry.length; i++){
			String id = keggTaxonomyEntry[i].split(":")[1];
			keggTaxonomyID.add(id);
		}
		
		return keggTaxonomyID;
	}
	
	
	/**
	 * @param organism
	 * @return
	 * @throws Exception
	 */
	static public String[] getGenesByOrganism(String organism) throws Exception {

		return KeggRestful.listGenesOrganism(organism);
	}


	/**
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static String getInfo(String query) throws Exception {
		return KeggRestful.fetch(KeggOperation.list, query); //serv.bfind(query);
	}

	/**
	 * @param ecNumber
	 * @return
	 * @throws Exception
	 */
	public static Map<String, List<String>> getEnzymeInfo(String ecNumber) throws Exception {
		String results = null;
		if(cache != null) results = (String) cache.get(ecNumber);

		if(results == null) {
			results = KeggRestful.getDBEntry(KeggDB.ENZYME, ecNumber);
			if(cache != null)
				try {
					cache.put(ecNumber, results);

				}
			catch (CacheException e) {
				if(debug)
					e.printStackTrace();
			}
		}

		Map<String, List<String>> result = new HashMap<String, List<String>>();

		for(String ec : results.split("///"))
		{
			ec=ec.concat("\n///");
			Map<String, List<String>> resultsParsed = KeggAPI.parseFullEntry(ec);

			String entry = KeggAPI.getSecondIfExists(KeggAPI.splitWhiteSpaces(resultsParsed.get("ENTRY")));
			if(entry!=null)
			{
				String key = entry.replace("EC ", "").trim();
				List<String> names = resultsParsed.get("NAME");
				result.put(key, names);
			}
		}
		return result;
	}

	/**
	 * @param compoundId
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getEnzymesByCompound(String compoundId) throws Exception {

		KeggCompoundER cInfo = getCompoundByKeggId(compoundId);
		return new TreeSet<String>(cInfo.getEcnumbers());

	}

	/**
	 * @param ecNumber
	 * @return
	 * @throws Exception
	 */

	public static Set<String> getReactionsByEnzymes(String ecNumber) throws Exception { 
		try {
			KeggECNumberEntry entry = (KeggECNumberEntry) cache.get(ecNumber);

			if(entry == null) {

				String raw = KeggRestful.getDBEntry("ec", ecNumber);
				Map<String, List<String>> data = parseFullEntry(raw);
				List<String> names = data.get("NAME");
				List<String> klass = data.get("CLASS");
				String sysname = null;

				if(data.containsKey("SYSNAME")) {

					List<String> sysnames = data.get("SYSNAME");
					sysname = sysnames.size() > 0 ? sysnames.get(0) : "";
				}
				List<String> reactions = data.get("ALL_REAC");
				String rawReactions = CollectionUtils.join(reactions, " ");
				Set<String> reactionSet = new HashSet<String>();
				Matcher m = Pattern.compile("R\\d{5}").matcher(rawReactions);
				while (m.find())
					reactionSet.add(m.group());

				entry = new KeggECNumberEntry(ecNumber, names, klass, sysname, reactionSet);

				try {
					cache.put(ecNumber, entry);


				}
				catch (CacheException e) {
					if(debug )
						e.printStackTrace();
				}

			}

			return entry.getReactionIds();

		} catch (Exception e) {

			logger.error("error with entry {}", ecNumber);
			logger.error("error {}",e);
			throw e;
		}
	}



	public static Set<String> getReactionIdsByOrganism(String organismTag) throws Exception  {
		List<String[]> reactions = KeggRestful.link("reaction", organismTag);
		TreeSet<String> reactionIds = new TreeSet<String>();

		for(String[] reaction : reactions)
			reactionIds.add(reaction[1].replaceAll("rn:", ""));

		return reactionIds;
	}

	public static TreeSet<String> getReactionsByCompounds(String compoundId) throws Exception {
		KeggCompoundER cInfo = getCompoundByKeggId(compoundId);
		return new TreeSet<String>( cInfo.getReactions());
	}

	//	static public TreeSet<String> get_reactions_by_enzimes_and_compounds(String compoundId , String ecNumber) {
	//		
	//		TreeSet<String> reactionsByCompound = get_reactions_by_compounds(compoundId);
	//		TreeSet<String> reactionsByEcNumber = get_reactions_by_enzimes(ecNumber);
	//		
	//		TreeSet<String> toReturn = new TreeSet<String>();
	//		
	//		for(String reaction: reactionsByCompound)
	//			if(reactionsByEcNumber.contains(reaction))
	//				toReturn.add(reaction);
	//		
	//		return toReturn;
	//	}

	/**
	 * @param pathwayId
	 * @return
	 * @throws Exception
	 */
	static public String getPathwayName(String pathwayId) throws Exception {
		String name = parseFullEntry(KeggRestful.fetch(KeggOperation.get, pathwayId)).get("NAME").get(0);

		return name;
	}

	/**
	 * @param compoundId
	 * @param linkName
	 * @return
	 * @throws Exception
	 */
	public static String getCompoundExternalLinkIdByKeggId(String compoundId, String linkName) throws Exception {

		String query = "cpd:"+compoundId;
		String results = null;//(String) cache.get(query);



		if(results == null) 
			results = KeggRestful.fetch(KeggOperation.get, query); //serv.bget(query);


		Map<String, List<String>> resultsParsed = parseFullEntry(results);

		List<String> allDds = resultsParsed.get("DBLINKS");

		Pattern p = Pattern.compile("(?i)"+linkName+":(.+)");
		Matcher m;
		String id = null;
		for(String link: allDds){
			m = p.matcher(link);
			if(m.matches())
				id = m.group(1).trim();
		}

		return "CHEBI:"+id;
	}

	/**
	 * @return
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public static  List<String[]> getOrganisms() throws Exception {
		return KeggRestful.getAllOrganismIDs();
	}	

	/**
	 * @return
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public static  List<String[]> getGenomes() throws Exception {
		return KeggRestful.getAllGenomeIDs();
	}

	/**
	 * @return
	 * @throws Exception 
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public static  List<String[]> getPathways() throws Exception {
		return KeggRestful.getAllPathwayIDs();
	}
	
	/**
	 * Returns empty list if argument list is empty, otherwise it would retrieve all entries from KEGG.
	 * 
	 * @return
	 * @throws Exception 
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public static  List<String[]> getSpecificPathways(List<String> listOfpathways) throws Exception {
		
		String query = "";
		
		for(String pathway : listOfpathways)
			query = query.concat(pathway).concat("+");
		
		query = query.replaceAll("\\+$", "");
		
		if(query.isEmpty())
			return new ArrayList<>();
		
		return KeggRestful.getSpecificPathwayIDs(query);
	}
	
	/**
	 * Search only pathways with reactions.
	 * 
	 * @return
	 * @throws Exception 
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	public static  Set<String> getPathwaysContainingReactions() throws Exception {
		
		return KeggRestful.getPathwaysContainingReactions();
	}

	/**
	 * @param briteID
	 * @return
	 * @throws Exception 
	 * @throws RemoteException 
	 * @throws ServiceException 
	 */

	public static String getXMLDataFromBriteID(String briteID) throws Exception {
		client.setReadTimeout(30000);
		client.setConnectTimeout(30000);
		WebResource webResource = client.resource("http://rest.kegg.jp/get/br:"+briteID);
		int status = webResource.head().getStatus();
		if(status==200 || status==302 || status==400) {

			String result = webResource.accept("text/plain").get(String.class);
			result = result.replaceAll("\\<.*?\\>", "");
			return result;
		}
		return null;
	}

	/**
	public static KeggReactionInformation get_reaction_by_keggId(String reactionId) {

		KEGGPortType serv    = locator.getKEGGPort();

		String query = reactionId;

		if(!reactionId.startsWith("rn:"))
			query = "rn:"+reactionId;

		String results = serv.bget(query);

		//		System.out.println(results);
		Map<String, List<String>> resultsParsed = parseFullEntry(results);

		return new KeggReactionInformation(resultsParsed);
	}

	public static String[] get_compounds_id_by_name(String name) {
		KEGGPortType serv    = locator.getKEGGPort();
		return  serv.search_compounds_by_name(name);
	}


	public static KeggCompoundER get_compound_by_keggId(String compoundId) , NullPointerException{
		KEGGPortType serv    = locator.getKEGGPort();

		String query = compoundId;

		if(!compoundId.startsWith("cpd:"))
			query = "cpd:"+compoundId;

		String results = serv.bget(query);

		//		System.out.println(results);
		Map<String, List<String>> resultsParsed = parseFullEntry(results);

		//		System.out.println(resultsParsed);

		String id = compoundId;
		String formula = getFirstIfExists(resultsParsed.get("FORMULA"));

		String mass  = getFirstIfExists(resultsParsed.get("MOL_WEIGHT"));
		List<String> names = resultsParsed.get("NAME"); 
		List<String> ecnumbers = splitWhiteSpaces(resultsParsed.get("ENZYME"));
		List<String> reactions = splitWhiteSpaces(resultsParsed.get("REACTION"));
		List<String> pathways = resultsParsed.get("PATHWAY");
		List<String> crossRefs = resultsParsed.get("DBLINKS");

		if(ecnumbers == null) ecnumbers = new ArrayList<String>();
		if(reactions == null) reactions = new ArrayList<String>();
		if(pathways == null) pathways = new ArrayList<String>();

		KeggCompoundER toReturn = new KeggCompoundER(id, formula, mass, names, ecnumbers, reactions, pathways, crossRefs);

		return toReturn;
	}*/

	public static List<String> splitWhiteSpaces(List<String> data){
		if(data == null)
			return null;

		List<String> toReturn = new ArrayList<String>(); 
		for(String value : data){
			String[] splitedData = value.split("\\s+");
			for(int i = 0; i < splitedData.length; i++){
				toReturn.add(splitedData[i]);
			}
		}

		return toReturn;
	}

	public static List<String> splitLinesGetOrthologues(List<String> data){
		if(data == null)
			return null;

		List<String> toReturn = new ArrayList<String>(); 
		for(String value : data)
		{
			String[] splitedData = value.split("\\s+");
			String first = splitedData[0];

			if(first.startsWith("K"))
				toReturn.add(first);
		}
		return toReturn;
	}

	public static String getFirstIfExists(List<String> data){
		if(data == null || data.size() < 1)
			return null;
		else
			return data.get(0);
	}

	public static String getSecondIfExists(List<String> data){

		if(data == null || data.size() < 2)
			return null;
		else
			return data.get(1);
	}


	/**
	 * @param response
	 * @return
	 */
	public static  Map<String, List<String>> parseFullEntry(String response) {
		// Split
		String[] lines = response.split("\n");
		Pattern p = Pattern.compile("^[A-Z].+");
		Matcher m;
		final Map<String, List<String>> data = new HashMap<String, List<String>>();

		String key = null;
		String val = null;
		for (int i = 0; i < lines.length; i++) {
			m = p.matcher(lines[i]);
			if (m.matches()) {
				final List<String> entry = new ArrayList<String>();

				String[] tags = lines[i].split(" ");
				if (tags.length < 2)
					continue;

				key = tags[0];
				val = lines[i].split(key)[1].trim();
				entry.add(val);

				i++;
				m = p.matcher(lines[i]);
				while (!m.matches()) 
				{
					val = lines[i].trim();
					if (val.startsWith("///"))
					{
						data.put(key, entry);
						return data;
					}
					entry.add(val);
					i++;
					m = p.matcher(lines[i]);
				}
				i--;
				data.put(key, entry);
			}
		}
		return data;
	}



	static public KeggReactionInformation getReactionByKeggId(String reactionId) throws EntityNotFoundException,Exception {

		String query = reactionId;

		if(!reactionId.startsWith("rn:"))
			query = "rn:"+reactionId;

		KeggReactionInformation result = (KeggReactionInformation) cache.get(query);

		if(result == null) {

			String results = KeggRestful.fetch(KeggOperation.get, query);

			if(results==null) throw new EntityNotFoundException();
			//System.out.println(results);

			Map<String, List<String>> resultsParsed = parseFullEntry(results);
			result = new KeggReactionInformation(resultsParsed); 

			try {
				cache.put(query, result);
			} catch (CacheException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static String[] getCompoundIdsByName(String name) throws Exception {

		String[] ret = null;

		name = name.split("\\+")[0];

		if(name!=null) {

			//name=name.split("+")[0];

			ret = KeggRestful.findCompoundKeggIDByQuery(name
					//					.replace(" -", "-") //"%2D")
					//					.replace(" ", "%20")
					//					.replace("(", "%28").replace(")", "%29")
					//					.replace("[", "%5B").replace("]", "%5D")
					//					.replace("`", "%60")
					//.replace("+", "") //"%2B")
					//.replace("'", "%27")
					);
		}
		return ret;
	}

	/**
	 * @param ecnumbers
	 * @return
	 * @throws Exception
	 */
	static public String[] getPathwaysByECNumbers(String[] ecnumbers) throws Exception {

		String[] ret = null;

		//USING SET TO AVOID REPEATS, replace to list for repeats
		Set<String> pathways = new HashSet<String> ();

		for ( String ec : ecnumbers) {
			Map<String, List<String>> map = parseFullEntry(KeggRestful.getDBEntry(KeggDB.ENZYME, ec));
			for ( String pathway : map.get("PATHWAY")) {
				//System.out.println(pathway);
				//System.out.println( Arrays.toString(pathway.split(" ")));
				pathways.add( pathway.split(" ")[0]);
			}
		}

		ret = pathways.toArray( new String[0]);

		return ret;
	}

	static public String[] getPathwaysByECNumber(String ecnumber) throws Exception{

		/*
		KEGGPortType serv = null;
		try{
			serv = locator.getKEGGPort();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		 */

		String[] query = new String[1];
		query[0] = ecnumber;

		if (!query[0].startsWith("ec:"))
			query[0] = "ec:" + query[0];
		//TODO: implementation needed
		return null; //serv.get_pathways_by_enzymes(query);
	}

	static public Set<String> getECNumbersByPathways(String pathway) throws Exception{
		/*
		KEGGPortType serv = null;
		try{
			serv = locator.getKEGGPort();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}*/

		Map<String, List<String>> map = parseFullEntry( KeggRestful.getDBEntry(KeggDB.PATHWAY, pathway));

		//System.out.println( map.get("ENZYME"));


		String[] ecs = map.get("ENZYME").toArray( new String[0]); //serv.get_enzymes_by_pathway(pathway);

		Set<String> ecsSet = new TreeSet<String>();

		for (int i = 0; i < ecs.length; i++) {
			String ec = ecs[i];
			if (ec.startsWith("ec:"))
				ec = ec.substring(3);
			ecsSet.add(ec);
		}

		return ecsSet;
	}

	static public Set<String> getCompoundsByPatways(String pathway) throws Exception {

		//String[] cpd = null; //serv.get_compounds_by_pathway(pathway);
		//String[] glycans = null; //serv.get_glycans_by_pathway(pathway);
		Map<String, List<String>> map = parseFullEntry( KeggRestful.getDBEntry(KeggDB.PATHWAY, pathway));

		String[] compound_list = map.get("COMPOUND").toArray(new String[0]);



		Set<String> compounds = new TreeSet<String>();

		for (String cpd : compound_list) {
			compounds.add( cpd.split(" ")[0]);
		}

		/*
		for (int i = 0; i < cpd.length; i++) {
			String ec = cpd[i];
			if (ec.startsWith("cpd:"))
				ec = ec.substring(4);
			compounds.add(ec);
		}

		for (int i = 0; i < glycans.length; i++) {
			String gly = glycans[i];
			if (gly.startsWith("gl:"))
				gly = gly.substring(3);
			System.out.println(gly);
			compounds.add(gly);
		}*/

		return compounds;
	}

	static public String color_pathway_by_objects(String pathway_id,
			String[] object_id_list, String[] fg_color_list,
			String[] bg_color_list) throws Exception {
		//TODO: GET PATHWAY COLOR MAP from REST


		// return serv.get_html_of_colored_pathway_by_objects(pathway_id,
		// object_id_list, fg_color_list, bg_color_list);
		return null; 
		//serv.color_pathway_by_objects(pathway_id, object_id_list, fg_color_list, bg_color_list);
	}

	/**
	 * @param pathway_id
	 * @param enzymes_id_list
	 * @param compounds_id_list
	 * @return
	 * @throws Exception
	 */
	static public String colorPathwayByElements (String pathway_id, List<String> enzymes_id_list, List<String> compounds_id_list) {

		client.setReadTimeout(30000);
		client.setConnectTimeout(30000);

		String buildQuery = pathway_id;

		if(enzymes_id_list!= null) {

			for(String enzymes_id : enzymes_id_list) {

				buildQuery  = buildQuery.concat("+"+enzymes_id);
			}
		}

		if(compounds_id_list!= null) {

			for(String compounds_id : compounds_id_list) {

				buildQuery  = buildQuery.concat("+"+compounds_id);
			}
		}

		WebResource webResource = client.resource("http://www.kegg.jp/pathway/"+buildQuery);

		int status = webResource.head().getStatus();
		if(status==200 || status==302 || status==400) {

			String result = webResource.accept("text/plain").get(String.class);
			return result;
		}
		return null;
	}


	public static String[] getEnzymesByGenes(String gene) throws Exception {
		String[] ret = null;
		//TODO: unimplemented
		return ret;
	}

	/**
	 * 
	 * @param ortholog
	 * @return
	 * @throws Exception
	 */
	static public Set<String> getECnumbersByOrthology(String ortholog) throws Exception {

		Set<String> brite_set = new TreeSet<String>();

		Map<String, List<String>> map = parseFullEntry( KeggRestful.getDBEntry(KeggDB.ORTHOLOGY, ortholog));

		if(map.containsKey("BRITE")) {

			String[] brite_list = map.get("BRITE").toArray(new String[0]);

			Pattern pattern = Pattern.compile("(\\d{1}(\\.[\\d+,-]){3})");

			for (String brite : brite_list) {

				Matcher matcher = pattern.matcher(brite);

				if (matcher.find())
					brite_set.add(matcher.group());
			}
		}
		return brite_set;
	}

	/**
	 * @param ortholog
	 * @return
	 * @throws Exception
	 */
	static public Set<String> getReactionsByOrthology(String orthologue) throws Exception {

		Map<String, List<String>> map = parseFullEntry( KeggRestful.getDBEntry(KeggDB.ORTHOLOGY, orthologue));

		Set<String> brite_set = new TreeSet<String>();

		if(map.containsKey("DBLINKS")) {

			String[] brite_list = map.get("DBLINKS").toArray(new String[0]);

			Pattern pattern = Pattern.compile("(R{1}\\d{5})");

			for (String brite : brite_list) {

				Matcher matcher = pattern.matcher(brite);

				if (matcher.find()) {

					brite_set.add(matcher.group());
				}
			}
		}

		return brite_set;
	}

	/**
	 * @param reactionId
	 * @return
	 * @throws Exception
	 */
	public static String[] getEnzymesByReaction(String reactionId) throws Exception {
		String[] reactionECs = new String[0];
		String query;
		if(reactionId.startsWith("rn:"))
			query = reactionId;
		else 
			query = "rn:" + reactionId;

		reactionECs = (String[]) cache.get(query);

		if(reactionECs == null) {
			String res = KeggRestful.fetch(KeggOperation.link, "enzyme", query);
			if(res != null) {
				String[] lines = res.split("\n");
				reactionECs = new String[lines.length];
				for(int i = 0; i< lines.length; i++) {
					String[] data = lines[i].split("\\t");
					reactionECs[i] = data[1].replaceAll("ec:", "");
				}

				if(cache != null)
					try {
						cache.put(query, reactionECs);

					} catch (CacheException e) {
						e.printStackTrace();
					}
			}

		}		

		return reactionECs;

	}

	/**
	 * @param compoundId
	 * @return
	 * @throws Exception
	 */
	static public KeggCompoundER getCompoundByKeggId(String compoundId) throws Exception {

		String query = compoundId.trim();

		char first = query.toUpperCase().charAt(0);

		if (first == 'C' && !query.startsWith("cpd:"))
			query = "cpd:" + query;

		else if (first == 'G' && !query.startsWith("gl:"))
			query = "gl:" + query;

		else if (first == 'D' && !query.startsWith("dr:"))
			query = "dr:" + query;

		// System.out.println(query);


		KeggCompoundER result = null;
		if(cache != null) 
			result = (KeggCompoundER) cache.get(query);

		if(result == null) {
			String results = KeggRestful.fetch( KeggOperation.get, query); //serv.bget(query);

			if(results!=null) {

				Map<String, List<String>> resultsParsed = parseFullEntry(results);

				// System.out.println(resultsParsed);

				String id = compoundId;
				String formula = getFirstIfExists(resultsParsed.get("FORMULA"));

				String mass = getFirstIfExists(resultsParsed.get("MOL_WEIGHT"));
				List<String> names = resultsParsed.get("NAME");
				List<String> ecnumbers = splitWhiteSpaces(resultsParsed.get("ENZYME"));
				List<String> reactions = splitWhiteSpaces(resultsParsed.get("REACTION"));
				List<String> pathways = resultsParsed.get("PATHWAY");
				List<String> crossRefs = resultsParsed.get("DBLINKS");

				// System.out.println(id +"\t"+resultsParsed.get("REMARK"));

				if (ecnumbers == null)
					ecnumbers = new ArrayList<String>();
				if (reactions == null)
					reactions = new ArrayList<String>();
				if (pathways == null)
					pathways = new ArrayList<String>();

				if (names == null) {
					names = resultsParsed.get("COMPOSITION");
					// System.out.println(names);
				}

				if (names == null) {
					names = new ArrayList<String>();
					names.add(id);
				}

				String comment = CollectionUtils.join(resultsParsed.get("COMMENT"),"\n");
				result = new KeggCompoundER(id, formula, mass, names,
						ecnumbers, reactions, pathways, crossRefs,comment);


				if(cache != null)
					try {
						cache.put(query, result);

					}
				catch(CacheException e) {
					if(debug)
						e.printStackTrace();

				}

			}

		}

		return result;
	}


	public static List<String> getOrthologsByECnumber(String query) throws Exception {

		return KeggRestful.findOrthologsByECnumber(query);
	}

	public static List<String> getOrthologsByReaction(String query) throws Exception {

		return KeggRestful.findOrthologsByReaction(query);
	}

	public static List<String> getModulesByQuery(String query) throws Exception {

		return KeggRestful.findModulesByQuery(query);
	}

	public static String getModulesStringByQuery(String query) throws Exception {

		return KeggRestful.findModulesStringByQuery(query);
	}

	public static String getModuleEntry(String module) throws Exception {

		return KeggRestful.fetch( KeggOperation.get, module);
	}

	public static List<String> getLinkECnumbersFromOrthology(String ortholog) throws Exception {

		return KeggRestful.findECNumbersByOrtholog(ortholog);
	}

	public static List<String> getPathwaysByModule(String module) throws Exception {

		List<String> ret = new ArrayList<String>();

		for(String pathway : KeggRestful.findPathwaysByModule(module)) {

			if(pathway.startsWith("path:rn"))
				ret.add(pathway.replace("path:rn", ""));
		}


		return ret;
	}

	public static List<String> getPathwaysIDByReaction(String reaction) throws Exception {

		List<String> ret = new ArrayList<String>();

		for(String pathway : KeggAPI.getReactionByKeggId(reaction).getPathway()) {

			ret.add(pathway.replace("rn", "").substring(0,5));
		}

		return ret;
	}

	//	public static void main(String[] args) throws Exception {
	//
	//		//cache.clear();
	//		//System.out.println( KEGGAPI.getXMLDataFromBriteID("EC 1.1.1.1"));
	//		System.out.println( KEGGAPI.getXMLDataFromBriteID("br08901"));
	//
	//	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> getPathwaysByReaction(String id) throws Exception {

		Map<String, String> reactionPathways;
		String query;
		if(id.startsWith("rn:"))
			query = id;
		else 
			query = "rn:" + id;

		reactionPathways = (Map<String, String>) cache.get(query);
		if(reactionPathways == null){
			reactionPathways = new HashMap<String, String>();
			List<String[]> res = KeggRestful.link("pathway", query);
			if(res.size() > 0) {
				Map<String, String> allPathways = pathwaysMap();

				for(String[] entry : res) {
					String key = entry[1];
					boolean global = key.endsWith("map01120") 
							|| key.endsWith("map01110") 
							|| key.endsWith("map01100"); 
					if(allPathways.containsKey(key) && !global)
						reactionPathways.put(key, allPathways.get(key));
				}
			}
			try {
				cache.put(query, reactionPathways);
			} catch (CacheException e) {
				e.printStackTrace();
			}
		}
		return reactionPathways;
	}

	public static Map<String, String> pathwaysMap() throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> pathways = (Map<String, String>) cache.get(ALL_PATHWAYS);
		if(pathways == null) {
			pathways = KeggRestful.mapPathways();
			try {
				cache.put(ALL_PATHWAYS, pathways);
			} catch (CacheException e) {
				e.printStackTrace();
			}
		}
		return pathways;

	}

	/** @return Map<Compound Kegg Id, NCBI-GeneID> */
	static public Map<String, String> getNcbiGeneIdByKeggIds(String organismId, Collection<String> compoundIds) throws Exception{
		return getGeneIdByKeggIds("ncbi-geneid", organismId, compoundIds);
	}

	/** @return Map<Compound Kegg Id, NCBI-GI> */
	static public Map<String, String> getNcbiGiByKeggIds(String organismId, Collection<String> compoundIds) throws Exception{
		return getGeneIdByKeggIds("ncbi-gi", organismId, compoundIds);
	}

	/** @return Map<Compound Kegg Id, ID> */
	static public Map<String, String> getGeneIdByKeggIds(String idType, String organismId, Collection<String> compoundIds) throws Exception{
		String query = "/" + idType +  "/";
		String orgId = organismId.trim() + ":";

		for(String cId : compoundIds)
			query += "+" + orgId + cId.trim();

		String result = KeggRestful.fetch(KeggOperation.conv, query);

		return parseGeneIdByKeggIdsResult(idType, organismId, result);
	}

	/** @return Map<Compound Kegg Id, ID> */
	static private Map<String, String> parseGeneIdByKeggIdsResult(String idType, String organismId, String result){
		Pattern p = Pattern.compile(organismId + ":(.+)" + idType + ":(.+)");

		if(result==null || result.equals(""))
			return null;

		String[] lines = result.split("\n");
		Map<String, String> assoc = new HashMap<String, String>();

		for(String line : lines)
		{
			Matcher m = p.matcher(line);
			if(m.find())
				assoc.put(m.group(1).trim(), m.group(2));
		}
		return assoc;		
	}



	public static void persistCache(){
		cache.dispose();
	}

	/**
	 * @param gene_id
	 * @return
	 * @throws Exception
	 */
	public static Map<String, List<String>> getGenesByID(String gene_id) throws Exception {

		Map<String, List<String>> ret = null;
		String response = KeggRestful.fetch(KeggOperation.get,gene_id);

		if(response!=null)
			ret = parseFullEntry(response);

		return  ret ;
	}

	public static Map<String, String> convertIntoMap(List<String> list, String sep, int idxKey){

		Map<String, String> ret = new HashMap<String, String>();
		if(list!=null)
			for (String string: list) {
				String[] data = string.split(sep);

				String key = data[idxKey];
				data[idxKey] = "";

				String info = CollectionUtils.join(data, " ").trim();

				ret.put(key, info);
			}

		return ret;
	}


	/**
	 * Get the gene function from query.
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static String getProduct(String query) {

		try {

			EntryData entryData = KeggAPI.getEntryData(query);

			return getProduct(entryData);
		} 
		catch (Exception e) {

			return null;
		}
	}


	/**
	 * Get the gene function from entryData.
	 * 
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static String getProduct(EntryData entryData) {

		try {

			String function = entryData.getFunction();

			if(entryData.getEcNumbers()!= null && entryData.getEcNumbers().size()>0)
				function = function .concat(" (EC:").concat(entryData.getEcNumbers().toString().replaceAll("\\[", "").replaceAll("\\]", "")).concat(")");

			return function;
		} 
		catch (Exception e) {

			return null;
		}
	}


	/**
	 * @param gene
	 * @return
	 * @throws Exception 
	 */
	public static EntryData getEntryData(String query) throws Exception {

		EntryData entryData = new EntryData(query);

		String gene = KeggRestful.findFirstGeneQuery(query);

		if(gene!=null) {
			
			Map<String, List<String>> map = KeggAPI.getGenesByID(gene);

			entryData.setFunction(map.get("DEFINITION").get(0));

			String orthology = "";

			if(map.get("ORTHOLOGY")!=null)
				orthology = map.get("ORTHOLOGY").get(0);

			if(orthology.contains("EC:")) {

				Set<String> ecs = new HashSet<>(new ArrayList<>(Arrays.asList(map.get("ORTHOLOGY").get(0).split("EC:")[1].split(","))));
				entryData.setEcNumbers(ecs);
			}

			String locus = map.get("ENTRY").get(0).split("\\s")[0].trim();
			if(StringUtils.isNumeric(locus)) {

				if(map.containsKey("NAME") && map.get("NAME") != null && !map.get("NAME").isEmpty() && map.get("NAME").get(0)!=null)
					locus = map.get("NAME").get(0).split(",")[0].trim();
			}

			entryData.setLocusTag(locus);		

			entryData.setOrganism(map.get("ORGANISM").get(0).split("\\s\\s")[1].trim());
		}
		return entryData;
	}


}
