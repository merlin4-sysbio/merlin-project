package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.sbml_semantics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


public class SemanticSbmlAPI {

	static public Client client = Client.create();


	static public SemanticSbmlSearchQueryResult searchEntity(String searchString, double inicialPrecision) throws Exception{
		client.setReadTimeout(30000);
		client.setConnectTimeout(30000);
		WebResource webResource = client.resource("http://www.semanticsbml.org/semanticSBML/annotate/search.json");
		
		int status = webResource.head().getStatus();
		if(status!=200 && status!=302 && status!=400)
		{
			System.out.println("Web service status: "+status+" using Marvin's PC");
			webResource = client.resource("http://lynx.biologie.hu-berlin.de//semanticSBML/annotate/search.json");
			System.out.println(" Marvin's PC web service status: "+webResource.head().getStatus());
		}


		double precision = inicialPrecision;
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("q", searchString);
		queryParams.add("precision",String.valueOf(precision));
		queryParams.add("full_info", "1");


		boolean searching = true;
		List<SematicSbmlItemRef> result = null;

		while(searching){

			String s = webResource.queryParams(queryParams).get(String.class);
			try {
				result = parserJSonString(s);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new Exception(e);
			}
			if(result.size() == 0 && precision > 0){
				precision-=0.1;
				queryParams.remove("precision");
				queryParams.add("precision",String.valueOf(precision));
			}else{
				searching = false;
			}

		}
		return new SemanticSbmlSearchQueryResult(result, searchString, precision);
	}	

	/**
	 * @param searchString
	 * @param inicialPrecision
	 * @return
	 * @throws ParseException
	 * @throws UniformInterfaceException
	 */
	static public SemanticSbmlSearchQueryResult searchFirstEntity(String searchString, double inicialPrecision) throws Exception{
		client.setReadTimeout(30000);
		client.setConnectTimeout(30000);

		WebResource webResource = client.resource("http://www.semanticsbml.org/semanticSBML/annotate/search.json");

		int status = webResource.head().getStatus();
		if(status!=200 && status!=302 && status!=400)
		{
			System.out.println("Web service status: "+status+" using Marvin's PC");
			webResource = client.resource("http://lynx.biologie.hu-berlin.de/semanticSBML/annotate/search.json");
			status = webResource.head().getStatus();
			System.out.println(" Marvin's PC web service status: "+status);
			if(status!=200 && status!=302 && status!=400)
			{
				return null;
			}
		}


		double precision = inicialPrecision;
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("q", searchString);
		queryParams.add("precision",String.valueOf(precision));
		queryParams.add("full_info", "1");

		List<SematicSbmlItemRef> result = null;


		String s = webResource.queryParams(queryParams).get(String.class);
		result = parserJSonString(s);

		return new SemanticSbmlSearchQueryResult(result, searchString, precision);
	}

	/**
	 * @param miriamCode
	 * @return
	 * @throws ParseException
	 */
	static public String getChebiRefConjugateAcid(String miriamCode) throws ParseException{
		client.setReadTimeout(30000);
		client.setConnectTimeout(30000);
		WebResource webResource = client.resource("http://www.semanticsbml.org/semanticSBML/default/get_relation_with_type.json");
		
		int status = webResource.head().getStatus();
		if(status!=200 && status!=302 && status!=400)
		{
			System.out.println("Web service status: "+status+" using Marvin's PC");
			webResource = client.resource("http://lynx.biologie.hu-berlin.de/semanticSBML/default/get_relation_with_type.json");
			System.out.println(" Marvin's PC web service status: "+webResource.head().getStatus());
		}

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("urn", miriamCode);
		queryParams.add("source","chebi");
		//		queryParams.add("full_info", "1");

		String s = webResource.queryParams(queryParams).get(String.class);
		parserRelation_with_typeResult(s);

		return null;
	}

	static public String getXRef(String miriamCode, Database database) throws Exception{
		client.setReadTimeout(30000);
		client.setConnectTimeout(30000);
		WebResource webResource = client.resource("http://www.semanticsbml.org/semanticSBML/default/get_xrefs.json?");

		int status = webResource.head().getStatus();
		if(status!=200 && status!=302 && status!=400)
		{
			System.out.println("Web service status: "+status+" using Marvin's PC");
			webResource = client.resource("http://lynx.biologie.hu-berlin.de/semanticSBML/default/get_xrefs.json?");
			System.out.println(" Marvin's PC web service status: "+webResource.head().getStatus());
		}

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("urn", miriamCode);
		queryParams.add("source",database.toString());

		String s = webResource.queryParams(queryParams).get(String.class);
		return getUrn(parserXRefs(s,database),database);
	}

	private static String getUrn(String id, Database database){
		if(id!=null)
		{
			if(database.equals(Database.ChEBI))
			{
				return "urn:miriam:obo.chebi:"+id+"";
			}
			return "urn:miriam:kegg.compound:"+id;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static void parserRelation_with_typeResult(String result) throws ParseException{

		Map<String,List<Map<String,String>>> toReturn = new HashMap<String, List<Map<String,String>>>();

		JSONParser parser = new JSONParser();
		JSONObject value = (JSONObject) parser.parse(result);

		JSONArray array =  (JSONArray) value.get("result");


		for(int i =0; i < array.size(); i++){

			JSONArray array2 = (JSONArray) array.get(i);

			JSONObject ids =  (JSONObject) array2.get(0);
			Map<String,String> xref = new HashMap<String, String>(ids);

			String bqbiol = (String) array2.get(1);

			if(toReturn.containsKey(bqbiol)){
				toReturn.get(bqbiol).add(xref);
			}else{
				ArrayList<Map<String, String>> values = new ArrayList<Map<String,String>>();
				values.add(xref);
				toReturn.put(bqbiol, values);
			}
		}

		System.out.println(toReturn);

	}

	private static List<SematicSbmlItemRef> parserJSonString(String jsonString) throws ParseException {

		List<SematicSbmlItemRef> itemRef = new ArrayList<SematicSbmlItemRef>();
		JSONParser parser = new JSONParser();

		JSONObject value = (JSONObject) parser.parse(jsonString);

		JSONArray array =  (JSONArray) value.get("result");


		for(int i =0; i < array.size(); i++){

			boolean isSiminal = false;
			SematicSbmlItemRef testRef = getSemeanticSbmlItemRef((JSONArray) array.get(i));
			for(int j =0; j < itemRef.size() && !isSiminal; j++){
				isSiminal = itemRef.get(j).hasTheSameName(testRef);
				if(isSiminal)
					itemRef.get(j).concatInformation(testRef);
			}

			if(!isSiminal)
				itemRef.add(testRef);
		}

		return itemRef;
	}

	private static String parserXRefs(String jsonString, Database database) throws ParseException {

		JSONParser parser = new JSONParser();

		JSONObject value =(JSONObject) ((JSONObject) parser.parse(jsonString)).get("result");

		if(database.equals(Database.KEGG_Compound))
		{
			return (String) value.get("KEGG Compound");
		}
		else if(database.equals(Database.ChEBI))
		{
			return (String) value.get("ChEBI");
		}

		return null;
	}


	private static SematicSbmlItemRef getSemeanticSbmlItemRef(
			JSONArray array) {

		List<String> names = new ArrayList<String>();
		List<String> miriamCodes = new ArrayList<String>();
		List<String> urls = new ArrayList<String>();

		for(int i =0; i < array.size(); i++){

			JSONObject map = (JSONObject) array.get(i);

			String name = (String) map.get("name");
			String url = (String) map.get("hyperlink");
			String miriamCode = (String) map.get("uri");

			names.add(name);
			urls.add(url);
			miriamCodes.add(miriamCode);
		}

		return new SematicSbmlItemRef(miriamCodes, names, null, urls);
	}

	public enum Database{
		KEGG_Compound,
		ChEBI
	}

}
