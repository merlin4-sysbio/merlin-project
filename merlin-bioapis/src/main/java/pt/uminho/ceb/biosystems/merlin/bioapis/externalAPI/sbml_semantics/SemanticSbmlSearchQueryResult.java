package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.sbml_semantics;

import java.io.Serializable;
import java.util.List;

public class SemanticSbmlSearchQueryResult implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List<SematicSbmlItemRef> results;
	protected String searchString;
	protected double precision;
	
	
	
	public SemanticSbmlSearchQueryResult(List<SematicSbmlItemRef> results,
			String searchString, double precision) {
		super();
		this.results = results;
		this.searchString = searchString;
		this.precision = precision;
	}
	
	
	public List<SematicSbmlItemRef> getResults() {
		return results;
	}
	public void setResults(List<SematicSbmlItemRef> results) {
		this.results = results;
	}
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	
	
	public Integer size(){
		return results.size();
	}
	
	public double getPrecision() {
		return precision;
	}


	public void setPrecision(double precision) {
		this.precision = precision;
	}


	public String toString(){
		String ret = searchString + "\t" + results.size() + "\t" + precision + "\n";
		
		for(int i =0; i < results.size(); i++){
			ret+=i + ".Miriam: "+results.get(i).getMiriamCodes()+"\n";
			ret+=i + ".Names: "+results.get(i).getNames()+"\n";
			ret+=i + ".Urls: "+results.get(i).getUrls()+"\n";
		}
		return  ret;
	}

}
