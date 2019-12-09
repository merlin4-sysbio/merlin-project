package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.datastructures;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.utilities.external.ExternalRefSource;
import pt.uminho.ceb.biosystems.merlin.utilities.external.MetaboliteExternalRef;

public class KeggCompoundER extends MetaboliteExternalRef {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected List<String> names;
	protected List<String> ecnumbers;
	protected List<String> reactions;
	protected List<String> pathways;
	protected List<String> crossRefs;
	protected String chebiId;
	protected String comment;
	
	public KeggCompoundER(String id, String formula,
			String mass, List<String> names,
			List<String> ecnumbers, List<String> reactions,
			List<String> pathways, List<String> crossRefs, String comment) {
		
		super(id, names.get(0), ExternalRefSource.KEGG_CPD, null, formula, mass, null,null);
		this.names = names;
		this.ecnumbers = ecnumbers;
		this.reactions = reactions;
		this.pathways = pathways;
		this.crossRefs = crossRefs;
		this.comment = comment;
	}
	
	public List<String> getNames() {
		return names;
	}

	public List<String> getEcnumbers() {
		return ecnumbers;
	}

	public List<String> getReactions() {
		return reactions;
	}

	public List<String> getPathways() {
		return pathways;
	}

	public List<String> getCrossRefs() {
		return crossRefs;
	}
	
	public String getCrossRef(String db){
		
		String toReturn = null;
		
		if(crossRefs!=null)
		for(String xref : crossRefs){
			if(xref.startsWith(db+":")){
				toReturn = xref.replaceAll(db+":\\s*", "");
			}
		}
		
		if(toReturn != null && toReturn.contains(" "))
			toReturn = toReturn.split("\\s+")[0];
		
		return toReturn;
	}
	
	public String getChebiXref(){
		
		if(chebiId==null)
		{
			String getChebiIdByKegg = getCrossRef("ChEBI");
			if(getChebiIdByKegg!=null)
			{
				chebiId = "CHEBI:"+getChebiIdByKegg;
			}
		}
		return chebiId;
	}
	
	public String toString(){
		return names+" "+ecnumbers.size() + ecnumbers+" "+reactions.size() +reactions+" "+pathways+" "+crossRefs;
	}
}
