package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.chebi;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.utilities.external.ExternalRefSource;
import pt.uminho.ceb.biosystems.merlin.utilities.external.IMetaboliteExternalRef;
import pt.uminho.ceb.biosystems.merlin.utilities.external.MetaboliteExternalRef;

public class ChebiER extends MetaboliteExternalRef implements IMetaboliteExternalRef{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String inchikey;
	protected List<String> secondaryIds;
	protected List<String> secondaryNames;
	protected String kegg_id;
	protected Set<String> functional_children;
	protected Map<ExternalRefSource, String> otherBDLinks;

	/**
	 * @param mainId
	 * @param mainName
	 * @param charge
	 * @param formula
	 * @param mass
	 * @param smiles
	 * @param inchikey
	 * @param secondaryIds
	 * @param secondaryNames
	 */
	public ChebiER(String mainId, String mainName,int charge, String formula, String mass, String smiles, String inchikey, List<String> secondaryIds,List<String> secondaryNames, Map<ExternalRefSource, String> otherBDLinks) {
		super(mainId, mainName, ExternalRefSource.CHEBI, charge, formula, mass, smiles, inchikey);
		this.inchikey = inchikey;
		this.secondaryIds = secondaryIds;
		this.secondaryNames = secondaryNames;
		this.kegg_id=null;
		this.functional_children=null;
		this.otherBDLinks = otherBDLinks;
	}

	@Override
	public Boolean hasMiriamCode() {
		return true;
	}
	
	public String toString(){
		String ret = "Chebi ID: " + getId()+"\t"+
		"Name: "+getName()+"\t"+
	    "Charge: "+charge+"\t"+
	    "Mass: "+ mass+"\t"+
	    "Smiles: "+smiles+"\t"+
	    //"\tStatus: "+ status + "\n"+
		"Inchikey: "+ inchikey+"\n"+
		"SecondaryChEBIIds: "+secondaryIds+"\t"+
		"SecondaryNames: "+secondaryNames+"\t"+
		"URL LINK: "+getURLLink()+"\t"+
		"MIRIAM CODE: "+getMiriamCode()+"\t";
		return ret;
	}

	@Override
	public String getSmiles() {
		return smiles;
	}

	public List<String> getSecundaryNames() {
		return secondaryNames;
	}

	/**
	 * @return the kegg_id
	 */
	public String getKegg_id() {
		return kegg_id;
	}

	/**
	 * @param kegg_id the kegg_id to set
	 */
	public void setKegg_id(String kegg_id) {
		this.kegg_id = kegg_id;
	}

	/**
	 * @return the functional_children
	 */
	public Set<String> getFunctional_children() {
		return functional_children;
	}

	/**
	 * @param functional_children the functional_children to set
	 */
	public void setFunctional_children(Set<String> functional_children) {
		this.functional_children = functional_children;
	}

	public String getOtherDBLink(ExternalRefSource db) {
		return otherBDLinks.get(db);
	}
}
