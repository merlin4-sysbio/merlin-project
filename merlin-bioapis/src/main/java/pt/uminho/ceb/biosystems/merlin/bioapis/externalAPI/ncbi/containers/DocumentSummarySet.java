package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
		
/**
 * @author amaromorais
 *
 */
@Root(name="DocumentSummarySet", strict=false)
public class DocumentSummarySet {
	
	@ElementList(name="DocumentSummary", inline=true, required=false)
	public List<DocumentSummary> documentSummary = new ArrayList<> ();

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DocumentSummarySet [documentSummary=" + documentSummary + "]";
		
	}

}


		


