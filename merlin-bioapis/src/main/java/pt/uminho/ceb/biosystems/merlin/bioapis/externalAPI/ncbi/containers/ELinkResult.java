package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ELinkResult", strict=false)
public class ELinkResult {

	@ElementList(name="LinkSet", inline=true)
	public List<LinkSet> linkSet = new ArrayList<> ();


}
