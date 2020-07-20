package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="LinkSet", strict=false)
public class LinkSetDb {

	@Element(name="DbTo", required=true)
	String dbTo;
	
	@Element(name="LinkName", required=true)
	String linkName;
	
	@ElementList(name="Link", inline=true)
	public List<Link> link = new ArrayList<> ();
}
