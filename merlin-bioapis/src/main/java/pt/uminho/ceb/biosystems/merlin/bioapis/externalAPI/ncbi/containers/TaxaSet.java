package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="GBSet", strict=false)
public class TaxaSet {
	
	@ElementList(name="Taxon", inline=true)
	public List<Taxon> taxon = new ArrayList<> ();

}
