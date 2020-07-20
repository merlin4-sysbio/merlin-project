package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi;

import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.RestAdapter.LogLevel;
import retrofit.converter.SimpleXMLConverter;

/**
 * @author Oscar
 *
 */
public class EntrezServiceFactory {
	
	private String baseURL;
	private boolean verbose;
	
	/**
	 * 
	 */
	public EntrezServiceFactory(){
		
		this.baseURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils";
		this.verbose = false;
	}

	/**
	 * @param baseURL
	 * @param verbose
	 */
	public EntrezServiceFactory(String baseURL, boolean verbose){
		
		this.baseURL = baseURL;
		this.verbose = verbose;
	}
	
	/**
	 * @return
	 */
	public EntrezService build() {
		
		
		Builder builder = new RestAdapter.Builder()
		   .setEndpoint(this.baseURL)
		   .setConverter(new SimpleXMLConverter());
		if(verbose)
			 builder.setLogLevel(LogLevel.FULL);
		
		RestAdapter adapter = builder.build();
		
		EntrezService service = adapter.create(EntrezService.class);
		return service;
	}
}
