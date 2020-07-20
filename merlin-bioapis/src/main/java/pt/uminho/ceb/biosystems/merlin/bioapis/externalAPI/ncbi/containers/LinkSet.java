package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="LinkSet", strict=false)
public class LinkSet {

	@Element(name="DbFrom", required=true)
	String dbFrom;
	
	@ElementList(name="IdList")
	public List<String> idList = new ArrayList<> ();
	
	@Element(name="LinkSetDb", required=false)
	public List<LinkSetDb> linkSetDb = new ArrayList<> ();
	
	@Override
	public String toString() {
		return String.format("GBSeq[IdList=%s, dbFrom=%s]", idList, dbFrom);
	}
	
}
