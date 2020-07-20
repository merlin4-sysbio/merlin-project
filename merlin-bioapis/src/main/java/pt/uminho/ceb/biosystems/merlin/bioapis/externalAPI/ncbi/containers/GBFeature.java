package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="GBFeature", strict=false)
public class GBFeature {
	
	@Element(name="GBFeature_key", required=false)
	public String featureKey;
	
	@ElementList(name="GBFeature_quals", required=false)
	public List<GBQualifier> qualifiers = new ArrayList<> ();

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GBFeature [featureKey=" + featureKey + ", qualifiers=" + qualifiers + "]";
	}
	
	
	
}
