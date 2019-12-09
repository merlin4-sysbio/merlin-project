package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * @author amaromorais
 *
 */
@Root(name="DocumentSummary", strict=false)
public class DocumentSummary {
	
		@Attribute(name="uid")
		public String uid;
		
		@Element(name="Taxid", required=true)
		public String taxonomyID;
		
		@Element(name="AssemblyAccession", required=false)
		public String assemblyAccession;
		
		@Element(name="AssemblyName", required=false)
		public String assemblyName;
		
		@Element(name="AssemblyStatus",  required=false)
		public String assemblyLevel;
		
		@Element(name="SpeciesName", required=false)
		public String speciesName;
		
		@Element(name="LastUpdateDate", required=false)
		public String lastupdateDate;
		
		@ElementList(name="PropertyList", required=false)
		public List<String> propertyList = new ArrayList<> ();
		
		@Element(name="FtpPath_GenBank", required=false)
		public String ftpGenBank;
		
		@Element(name="FtpPath_RefSeq", required=false)
		public String ftpRefSeq;
		
		@Element(name="SubmitterOrganization", required=false)
		public String submitter;
		
		@Element(name="InfraspeciesList", required=false)
		public String infraspecificName;
		
		@Element(name="Assembly method", required=false)
		public String assemblyMethod;
		
		@Element(name="Sequencing technology", required=false)
		public String sequencingTechnology;
		
		@Element(name="Assembly record", required=false)
		public String assemblyRecord;
		
		@Element(name="GenBank status", required=false)
		public String genBankStatus;
		
		@Element(name="RefSeq status", required=false)
		public String refSeqStatus;
		
		@Element(name="Genbank", required=false)
		@Path("Synonym")
		public String accessionGenBank;
		
		@Element(name="RefSeq", required=false)
		@Path("Synonym")
		public String accessionRefSeq;

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "DocumentSummary [uid=" + uid + ", taxonomyID=" + taxonomyID + ", assemblyAccession="
					+ assemblyAccession + ", assemblyName=" + assemblyName + ", assemblyLevel=" + assemblyLevel + ", speciesName=" + speciesName
					+ ", lastupdateDate=" + lastupdateDate + ", propertyList=" + propertyList + ", ftpGenBank="
					+ ftpGenBank + ", ftpRefSeq=" + ftpRefSeq + ", submitter=" + submitter + ", infraspecificName="
					+ infraspecificName + ", assemblyMethod=" + assemblyMethod + ", sequencingTechnology="
					+ sequencingTechnology + ", accessionGenBank=" + accessionGenBank + ", accessionRefSeq="
					+ accessionRefSeq +", assemblyRecord=" +assemblyRecord+ ", genBankStatus=" +genBankStatus+ ", refSeqStatus=" +refSeqStatus +"]";
		}
		
		public Map<String, String> toHashMap() {
			
			Map<String, String> map = new HashMap<String, String>();
			
			map.put("uid", uid);
			map.put("taxonomyID", taxonomyID);
			map.put("assemblyAccession", assemblyAccession);
			map.put("assemblyName", assemblyName);
			map.put("assemblyLevel", assemblyLevel);
			map.put("speciesName", speciesName);
			map.put("lastupdateDate", lastupdateDate);
			map.put("ftpGenBank", ftpGenBank);
			map.put("ftpRefSeq", ftpRefSeq);
			map.put("submitter", submitter);
			map.put("infraspecificName", infraspecificName);
			map.put("assemblyMethod", assemblyMethod);
			map.put("sequencingTechnology", sequencingTechnology);
			map.put("accessionGenBank", accessionGenBank);
			map.put("accessionRefSeq", accessionRefSeq);
			map.put("assemblyRecord", assemblyRecord);
			map.put("genBankStatus", genBankStatus);
			map.put("refSeqStatus", refSeqStatus);
			
			return map;
		}
		
		
		
}


