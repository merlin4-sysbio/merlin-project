package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="OtherNames", strict=false)
public class OtherNames {

	@Element(name="GenbankCommonName", required=false)
	public String genbankCommonName;
	
	@Element(name="synonym", required=false)
	public String synonym;
	
	@Element(name="commonName", required=false)
	public String commonName;
	
	@Element(name="Includes", required=false)
	public String includes;
	
	@ElementList(name="Name", required=false)
	public List<String> name = new ArrayList<> ();
}
