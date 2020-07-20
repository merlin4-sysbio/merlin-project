package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="Taxon", strict=false)
public class Taxon {
	
	@Element(name="Lineage", required=false)
	public String lineage;
	
	@Element(name="TaxId", required=true)
	public String taxId;
	
	@Element(name="ScientificName", required=false)
	public String scientificName;
	
	@Element(name="ParentTaxId", required=false)
	public String parentTaxId;
	
	@Element(name="Rank", required=false)
	public String rank;
	
	@Element(name="Division", required=false)
	public String division;
	
	@ElementList(name="OtherNames", required=false)
	public List<OtherNames> otherNames = new ArrayList<> ();
	
	@ElementList(name="GeneticCode", required=false)
	public List<String> geneticCode = new ArrayList<> ();
	
	@ElementList(name="mitoGeneticCode", required=false)
	public List<String> mitoGeneticCode = new ArrayList<> ();
	
	@ElementList(name="LineageEx", required=false)
	public List<Taxon> lineageEx = new ArrayList<> ();
	
}
