package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.datastructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.KeggAPI;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.KeggUtils;
import pt.uminho.ceb.biosystems.merlin.utilities.external.ExternalRef;
import pt.uminho.ceb.biosystems.merlin.utilities.external.ExternalRefSource;
import pt.uminho.ceb.biosystems.merlin.utilities.external.IExternalRef;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.collection.CollectionUtils;



public class KeggReactionInformation extends ExternalRef implements IExternalRef, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static private Pattern isMultiStepPattern = Pattern.compile("R\\d{5}\\+R\\d{5}");
	
	protected List<String> productKeggIds;
	protected List<String> reactantKeggIds;
	protected List<String> productStoichiometry;
	protected List<String> reactantStoichiometry;
	protected List<String> sameas;
	
	protected List<String> names;
	protected List<String> ecnumbers;
	@Deprecated
	protected List<String> pathway;
	protected Map<String,String> pathwaysMap;
	protected String comment;

	
	
	public KeggReactionInformation( String rcID, String name, 
			List<String> productKeggIds, 
			List<String> reactantKeggIds,
			List<String> sameas,
			List<String> ecnumbers ) {
		
		super(ExternalRefSource.KEGG_REACTION);
		this.names = new ArrayList<String>();
		names.add(name);
		this.productKeggIds = productKeggIds;
		this.reactantKeggIds = reactantKeggIds;
		this.ecnumbers = ecnumbers;
		this.sameas = sameas;
		this.name = name;
		this.id = rcID;
	}
	
	public KeggReactionInformation(String rcId, String name,
			List<String> names,
			List<String> productKeggIds, 
			List<String> reactantKeggIds,
			List<String> sameas,
			List<String> ecnumbers) {
		this(rcId, name, productKeggIds, reactantKeggIds, sameas, ecnumbers);
		this.names = names;
	}
	
	public KeggReactionInformation( String rcID, String name, 
			Map<String, Integer> productKeggIds, 
			Map<String, Integer> reactantKeggIds,
			List<String> sameas,
			List<String> ecnumbers ) {
		
		super(ExternalRefSource.KEGG_REACTION);
		
		List<String> product_id = new ArrayList<String> ();
		List<String> product_stoich = new ArrayList<String> ();
		for ( String product_key: productKeggIds.keySet()) {
			product_id.add(product_key);
			product_stoich.add(productKeggIds.get(product_key).toString());
		}
		

		
		List<String> reactant_id = new ArrayList<String> ();
		List<String> reactant_stoich = new ArrayList<String> ();
		for ( String reactant_key: reactantKeggIds.keySet()) {
			reactant_id.add( reactant_key);
			reactant_stoich.add( reactantKeggIds.get(reactant_key).toString());
		}
		
		this.productKeggIds = product_id;
		this.productStoichiometry = product_stoich;
		this.reactantKeggIds = reactant_id;
		this.reactantStoichiometry = reactant_stoich;
		this.ecnumbers = ecnumbers;
		this.sameas = sameas;
		this.name = name;
		this.id = rcID;
		
	}
	
	public KeggReactionInformation(Map<String, List<String>> data){
		super(ExternalRefSource.KEGG_REACTION);
		
		id = KeggUtils.parserKeggCode(data.get("ENTRY").get(0));
		if(data.get("NAME")!=null && data.get("NAME").size()>0) {
			this.name = data.get("NAME").get(0);
			this.names = data.get("NAME");
		}
		else {
			name = data.get("DEFINITION").get(0);
			names = new ArrayList<String>();
		}
		
		ecnumbers = KeggUtils.parserMultiLineInfo(data.get("ENZYME"));
		
		pathwaysMap = KeggAPI.convertIntoMap(data.get("PATHWAY"), " +", 0);
		this.pathway = new ArrayList<String>(pathwaysMap.keySet());
		
		parserEquation(data.get("EQUATION").get(0));
		
		comment = CollectionUtils.join(data.get("COMMENT"), "\n");
	}

	private void parserEquation(String equation){
		
//		if(equation!=null)
		String rlHandEquation[] = equation.split("<=>");
		
		String reactants[] = rlHandEquation[0].split("\\+");
		String products[] = rlHandEquation[1].split("\\+");
		
		Pattern pattern = Pattern.compile("(.*)\\s*(C\\d{5})");
		
		reactantStoichiometry = new ArrayList<String>();
		reactantKeggIds = new ArrayList<String>();
		for(int i =0; i < reactants.length; i++){
			Matcher matcher = pattern.matcher(reactants[i]);
			
			if(matcher.find()){
				reactantKeggIds.add(matcher.group(2));
				reactantStoichiometry.add(matcher.group(1));
			}
		}
		
		productStoichiometry = new ArrayList<String>();
		productKeggIds = new ArrayList<String>();
		for(int i =0; i < products.length; i++){
			Matcher matcher = pattern.matcher(products[i]);
			
			if(matcher.find()){
				productKeggIds.add(matcher.group(2));
				productStoichiometry.add(matcher.group(1));
			}
		}
	}

	public boolean isMultiStep(){
		Matcher m = isMultiStepPattern.matcher(comment);
		return m.find();
	}
	
	public String getId() {
		return id;
	}

	public List<String> getProductKeggIds() {
		return productKeggIds;
	}

	public List<String> getReactantKeggIds() {
		return reactantKeggIds;
	}

	public List<String> getProductStoichiometry() {
		return productStoichiometry;
	}

	public List<String> getReactantStoichiometry() {
		return reactantStoichiometry;
	}
	
	public List<String> getNames() {
		return names;
	}

	public List<String> getEcnumbers() {
		return ecnumbers;
	}

//	Use getPathwaysInfo
	/**
	 * Deprecated method use getPathwaysMap the ids are the keys of the map
	 * @return
	 */
	@Deprecated 
	public List<String> getPathway() {
		return pathway;
	}
	
	public Map<String, String> getPathwayMap() {
		return pathwaysMap;
	}
	
	public List<String> getSameAs() {
		return sameas;
	}

	public String getName() {
		return name;
	}

	public String toString(){
		return id + "\t"+name+"\t" +reactantKeggIds + " <=> " + productKeggIds + "\t" + reactantStoichiometry +"<=>"+ productStoichiometry + ecnumbers +"\t"+pathway;
	}
	
	public String getComment(){
		return comment;
	}
}
