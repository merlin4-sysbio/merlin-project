package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="GBSeq", strict=false)
public class GBSeq {
	
	@Element(name="GBSeq_locus", required=true)
	public String locus;
	
	@Element(name="GBSeq_length", required=false)
	public int length;
	
	@Element(name="GBSeq_moltype", required=false)
	public String molType;
	
	@Element(name="GBSeq_primary-accession", required=false)
	public String primaryAccession;
	
	@Element(name="GBSeq_accession-version", required=false)
	public String accessionVersion;
	
	@ElementList(name="GBSeq_other-seqids", required=false)
	public List<String> xReferences = new ArrayList<> ();
	
	@Element(name="GBSeq_organism", required=false)
	public String organism;
	
	@Element(name="GBSeq_taxonomy", required=false)
	public String taxonomy;
	
	@ElementList(name="GBSeq_feature-table")
	public List<GBFeature> features = new ArrayList<> ();
	
	@Element(name="GBSeq_sequence", required=false)
	public String sequence;
	
	@Element(name="GBSeq_definition", required=false)
	public String definition;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GBSeq [locus=" + locus + ", length=" + length + ", molType=" + molType + ", primaryAccession="
				+ primaryAccession + ", accessionVersion=" + accessionVersion + ", xReferences=" + xReferences
				+ ", organism=" + organism + ", taxonomy=" + taxonomy + ", features=" + features + ", sequence="
				+ sequence + ", definition=" + definition + "]";
	}
	

}
