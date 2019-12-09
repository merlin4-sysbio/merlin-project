package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
	
@Root(name="GBSet", strict=false)
public class GBSet {
	
	@ElementList(name="GBSeq", inline=true, required=false)
	public List<GBSeq> gBSeq = new ArrayList<> ();

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GBSet [gBSeq=" + gBSeq + "]";
		
	}
	
	
}
