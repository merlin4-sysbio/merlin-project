package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Link", strict=false)
public class Link {

	@Element(name="Id", required=true)
	public String id;
}
