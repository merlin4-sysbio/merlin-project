package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ESearchResult", strict=false)
public class ESearchResult {

	@Element(name="Count")
	public int count;
	
	@ElementList(name="IdList")
	public List<String> idList = new ArrayList<> ();

}
