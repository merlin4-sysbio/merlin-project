package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * @author amaromorais
 *
 */
@Root(name="ESummaryResult", strict=false)
public class ESummaryResult {
	
	@ElementList(name="DocumentSummarySet", inline=true, required=false)
	public List<DocumentSummarySet> documentSummarySet = new ArrayList<> ();

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ESummaryResult [d=" + documentSummarySet + "]";
		
	}

}
