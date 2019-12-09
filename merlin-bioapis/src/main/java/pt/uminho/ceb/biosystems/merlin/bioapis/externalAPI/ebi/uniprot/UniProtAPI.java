package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.AccessionID;
import org.biojava.nbio.core.sequence.DataSource;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.datatypes.EntryData;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.NcbiAPI;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.MySleep;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologuesData;
import pt.uminho.ceb.biosystems.merlin.utilities.datastructures.list.ListUtilities;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;
import uk.ac.ebi.kraken.interfaces.common.Sequence;
import uk.ac.ebi.kraken.interfaces.uniprot.DatabaseCrossReference;
import uk.ac.ebi.kraken.interfaces.uniprot.DatabaseType;
import uk.ac.ebi.kraken.interfaces.uniprot.Gene;
import uk.ac.ebi.kraken.interfaces.uniprot.NcbiTaxon;
import uk.ac.ebi.kraken.interfaces.uniprot.NcbiTaxonomyId;
import uk.ac.ebi.kraken.interfaces.uniprot.Organelle;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntryType;
import uk.ac.ebi.kraken.interfaces.uniprot.description.FieldType;
import uk.ac.ebi.kraken.interfaces.uniprot.evidences.EvidenceId;
import uk.ac.ebi.kraken.interfaces.uniprot.genename.GeneNameSynonym;
import uk.ac.ebi.kraken.interfaces.uniprot.genename.ORFName;
import uk.ac.ebi.kraken.interfaces.uniprot.genename.OrderedLocusName;
import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.QueryResult;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtQueryBuilder;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtService;
import uk.ac.ebi.uniprot.dataservice.query.Query;

/**
 * @author oscar
 *
 */
///*
// * Client Class has a couple of static methods to create a ServiceFactory instance.
// * From ServiceFactory, you can fetch the JAPI Services.
// */
//ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();
//
//// UniProtService
//UniProtService uniprotService = serviceFactoryInstance.getUniProtQueryService();
//
//// UniParcService
//UniParcService uniparcService = serviceFactoryInstance.getUniParcQueryService();
//
//// UniRefService
//UniRefService unirefService = serviceFactoryInstance.getUniRefQueryService();
//
//// Blast Service
//BlastService blastService = serviceFactoryInstance.getBlastService();
//
//// UniProt Blast Service
//UniProtBlastService uniProtBlastService = serviceFactoryInstance.getUniProtBlastService();
//
//// Clustal Omega Service
//ClustalOService clustalOService = serviceFactoryInstance.getClustalOService();
//
//// UniProt Clustal Omega Service
//UniProtClustalOService uniProtClustalOService = serviceFactoryInstance.getUniProtClustalOService();
public class UniProtAPI {

	final static Logger logger = LoggerFactory.getLogger(UniProtAPI.class);
	public static ServiceFactory serviceFactoryInstance = null;
	public static UniProtService uniProtService = null;
	public static UniProtAPI uniProtApi = null;

	/**
	 * 
	 */
	public UniProtAPI() {

		serviceFactoryInstance = Client.getServiceFactoryInstance();
		uniProtService = serviceFactoryInstance.getUniProtQueryService();
	}



	/**
	 * @param uniprot_id
	 * @param errorCount
	 * @return
	 */
	public static UniProtEntry getEntryFromUniProtID(String uniprot_id, int errorCount){

		UniProtAPI.getInstance();

		try {

			UniProtEntry entry = uniProtService.getEntry(uniprot_id);

			return entry;
		} 
		catch(Exception e) {

			if(errorCount<20) {

				errorCount+=1;
				MySleep.myWait(1000);
				logger.debug("Entry From UniProt ID trial {}",errorCount);
				return (UniProtEntry) getEntryFromUniProtID(uniprot_id, errorCount);
			}
			else {

				logger.error("Could not retrieve single entry. Returning null {}",uniprot_id);
				logger.trace("StackTrace {}",e);
				return null;
			}
		}
	}

	/**
	 * @param uniprotIDs
	 * @param errorCount
	 * @return
	 */
	public static List<UniProtEntry> getEntriesFromUniProtIDs(List<String> uniprotIDs, int errorCount){

		UniProtAPI.getInstance();

		try {

			List<UniProtEntry> uniprotEntries = new ArrayList<>(uniprotIDs.size());
			for (int i = 0; i < uniprotIDs.size(); i++)
				uniprotEntries.add(null);

			List<List<String>> uniprotIDs_subsets = ListUtilities.split(uniprotIDs, 50);

			for(List<String> uniprotIDs_subset : uniprotIDs_subsets) {

				Query query = UniProtQueryBuilder.accessions(new HashSet<String> (uniprotIDs_subset));

				QueryResult<UniProtEntry> entries = uniProtService.getEntries(query);
				while (entries.hasNext()) {

					UniProtEntry entry = entries.next();
					uniprotEntries.set(uniprotIDs.indexOf(entry.getPrimaryUniProtAccession().getValue()),entry);
				}
			}
			return uniprotEntries;
		} 
		catch(Exception e) {


			if(errorCount<20) {

				MySleep.myWait(1000);
				errorCount+=1;
				logger.debug("Entries From UniProt IDs trial {}",errorCount);
				return UniProtAPI.getEntriesFromUniProtIDs(uniprotIDs, errorCount);
			}
			else {

				logger.error("Could not retrieve entries list. Returning null. {}",uniprotIDs);
				logger.trace("StackTrace {}",e);
				return null;
			}
		}
	}

	/**
	 * Get entry from Locus Tag.
	 * 
	 * @param locusTag
	 * @param errorCount
	 * @return
	 * @throws ServiceException 
	 */
	public static UniProtEntry getEntry(String locusTag, int errorCount) throws ServiceException {

		UniProtAPI.getInstance();

		try {

			String originalLocusTag = locusTag;
			Query query = UniProtQueryBuilder.gene(locusTag);

			QueryResult<UniProtEntry> entries = uniProtService.getEntries(query);

			while(entries.hasNext()) {

				UniProtEntry uniProtEntry = entries.next();

				if(locusTag.contains(".*"))
					locusTag = locusTag.replace(".*","");

				List<Gene> genes = uniProtEntry.getGenes();

				for(int i=0;i<genes.size();i++) {

					List<OrderedLocusName> locusTags = genes.get(i).getOrderedLocusNames();

					for(int j=0;j<locusTags.size();j++) {

						if(locusTags.get(j).getValue().equalsIgnoreCase(locusTag)) {

							i=genes.size();
							j=locusTags.size();
							return uniProtEntry;
						}
					}

					if(genes.get(i).getGeneName().getValue().equalsIgnoreCase(locusTag))
						return uniProtEntry;

					List<ORFName> orfNames = genes.get(i).getORFNames();

					for(int j=0;j<orfNames.size();j++) {

						if(orfNames.get(j).getValue().equalsIgnoreCase(locusTag)) {

							i=genes.size();
							j=orfNames.size();
							return uniProtEntry;
						}
					}
					List<GeneNameSynonym> geneNameSynonyms = genes.get(i).getGeneNameSynonyms();

					for(int j=0;j<geneNameSynonyms.size();j++) {

						if(geneNameSynonyms.get(j).getValue().equalsIgnoreCase(locusTag)) {

							i=genes.size();
							j=geneNameSynonyms.size();

							return uniProtEntry;
						}
					}
				}

				for(DatabaseCrossReference dbcr : uniProtEntry.getDatabaseCrossReferences()) {

					String[] xRefs = dbcr.toString().split(":");

					for(String xRef:xRefs)
						if(xRef.contains(locusTag))
							return uniProtEntry;
				}
			}
			if(!originalLocusTag.contains(".")) {

				originalLocusTag=originalLocusTag.concat(".*");
				return getEntry(originalLocusTag, 0);
			}

			return null;
		} 
		catch(ServiceException e) {

			if(errorCount<10) {

				MySleep.myWait(1000);
				logger.debug("GetEntry trial {}",errorCount);
				return getEntry(locusTag, errorCount+1);
			}
			else {
				logger.error("GetEntry error");
				logger.trace("StackTrace {}",e);
				throw e;
			}
		}
		catch(OutOfMemoryError err) {

			if(errorCount<10) {

				MySleep.myWait(1000);
				logger.debug("GetEntry trial {}",errorCount);
				return getEntry(locusTag, errorCount+1);
			}
			else {
				logger.error("GetEntry OutOfMemoryError {}", locusTag);
				logger.trace("StackTrace",err);
				throw err;
			}
		}
	}

	/**
	 * @param crossReference
	 * @param errorCount
	 * @return
	 */
	public static UniProtEntry getUniProtEntryFromXRef(String crossReference, int errorCount) {

		return UniProtAPI.getUniProtEntryFromXRef(crossReference, -1, errorCount);
	}


	/**
	 * @param crossReference
	 * @param taxonomyID
	 * @param errorCount
	 * @return
	 */
	public static UniProtEntry getUniProtEntryFromXRef(String crossReference, long taxonomyID, int errorCount) {

		UniProtAPI.getInstance();

		try {

			Query query = UniProtQueryBuilder.xref(crossReference);

			Query finalQuery = query; 
			if(taxonomyID>0)
				finalQuery = UniProtQueryBuilder.and(query, UniProtQueryBuilder.taxonID(Integer.parseInt(taxonomyID+""))); 

			QueryResult<UniProtEntry> entries = uniProtService.getEntries(finalQuery);

			if(entries.getNumberOfHits()<1) {

				finalQuery = UniProtQueryBuilder.xref(crossReference.split("\\.")[0]);
				entries = uniProtService.getEntries(finalQuery);
			}

			while(entries.hasNext()) {

				UniProtEntry uniProtEntry = entries.next();
				boolean go = false;
				for(NcbiTaxonomyId ncbiTaxonomyID : uniProtEntry.getNcbiTaxonomyIds())					
					if(taxonomyID <0 || ncbiTaxonomyID.getValue().equalsIgnoreCase(String.valueOf(taxonomyID)))
						go=true;


				if(!go) {

					List<NcbiTaxon> uniprotTaxon = uniProtEntry.getTaxonomy();
					List<NcbiTaxon> taxonNcbi = NcbiAPI.getTaxonomyFromNCBI(taxonomyID, 0).getTaxonomy();

					for(NcbiTaxon taxon: taxonNcbi)
						for(int i= 0; i < uniprotTaxon.size(); i++)
							if(uniprotTaxon.get(i).getValue().equalsIgnoreCase(taxon.getValue()))
								uniprotTaxon.remove(i);

					if(uniprotTaxon.isEmpty())
						go = true;

				}

				if(go) {

					Iterator<DatabaseCrossReference> it = uniProtEntry.getDatabaseCrossReferences().iterator();

					while(it.hasNext()) {

						DatabaseCrossReference dbcr = it.next();

						//DatabaseType database = dbcr.getDatabase();
						//if(database.equals(DatabaseType.REFSEQ)) {	

						String x_ref = dbcr.getPrimaryId().getValue();

						for(String id:x_ref.split(":")) {

							if(id.trim().equalsIgnoreCase(crossReference.trim()))
								return uniProtEntry;

							id = id.trim().split("\\.")[0];
							String cross = crossReference.split("\\.")[0];

							if(id.equalsIgnoreCase(cross))
								return uniProtEntry;
						}

						for(EvidenceId e : dbcr.getEvidenceIds())
							if(e.getValue().trim().equalsIgnoreCase(crossReference.trim()))
								return uniProtEntry;

						if(dbcr.getThird()!= null && dbcr.getThird().getValue().trim().equalsIgnoreCase(crossReference.trim()))
							return uniProtEntry;

						if(dbcr.getFourth()!= null && dbcr.getFourth().getValue().trim().equalsIgnoreCase(crossReference.trim()))
							return uniProtEntry;

						if(dbcr.getDescription()!= null && dbcr.getDescription().getValue().trim().equalsIgnoreCase(crossReference.trim()))
							return uniProtEntry;
					}
				}
			}
		}
		catch(Exception e) {

			if(errorCount<5) {

				MySleep.myWait(1000);
				errorCount=errorCount+1;
				logger.trace("xRef trial {}",errorCount);
				return getUniProtEntryFromXRef(crossReference, errorCount);
			}
			else {

				logger.error("Could not retrieve single entry from xRef. Returning null. Crossreference {}.",crossReference);
				logger.error("StackTrace {}",e.getCause().getMessage());
			}
		}
		return null;
	}

	/**
	 * @param crossReferences
	 * @return
	 */
	public static Map<String, String> getUniProtLocus(Collection<String> crossReferences) {

		Map<String, String> out = new TreeMap<>();

		for(String crossReference : crossReferences) {

			String locus = UniProtAPI.getLocusTag(UniProtAPI.getUniProtEntryFromXRef(crossReference, 0));
			out.put(crossReference, locus);
		}

		return out;
	}

	/**
	 * @param taxID
	 * @param errorCount
	 * @return
	 */
	public static TaxonomyContainer getTaxonomyFromNCBITaxnomyID(long taxID, int errorCount) {

		UniProtAPI.getInstance();

		TaxonomyContainer result = new TaxonomyContainer();

		try {

			Query query = UniProtQueryBuilder.taxonID(Integer.parseInt(Long.toString(taxID)));

			QueryResult<UniProtEntry> entries = uniProtService.getEntries(query);

			while(entries.hasNext()) {

				UniProtEntry uniProtEntry = entries.next();
				List<NcbiTaxonomyId> taxa = uniProtEntry.getNcbiTaxonomyIds();

				for(NcbiTaxonomyId taxon : taxa) {

					if(taxon.getValue().equalsIgnoreCase(""+taxID)) {

						result.setSpeciesName(uniProtEntry.getOrganism().getScientificName().getValue());
						result.setTaxonomy(uniProtEntry.getTaxonomy());


						return result;
					}
				}
			}
			return null;
		}
		catch(ServiceException e) {

			if(errorCount<10) {

				MySleep.myWait(1000);
				errorCount = errorCount+1;
				logger.debug("getTaxonomyFromNCBITaxnomyID trial {}",errorCount);
				return getTaxonomyFromNCBITaxnomyID(taxID, errorCount+1);
			}
			else {

				logger.error("getTaxonomyFromNCBITaxnomyID eror, returning null. {}",taxID);
				logger.trace("StackTrace {}",e);
				return null;
			}
		}
	}

	/**
	 * @param entry
	 * @return
	 */
	public static String getProteinExistence(UniProtEntry entry){

		if(entry!=null)
			return entry.getProteinExistence().getValue();

		return null;
	}

	/**
	 * @param entry
	 * @return
	 */
	public static List<String> getLocusTags(UniProtEntry entry) {

		List<String> out = null;

		if(entry!=null) {

			out = new ArrayList<String>();

			if(entry.getGenes().size()>0) {

				if(entry.getGenes().get(0).getOrderedLocusNames()!=null) {

					for(OrderedLocusName oln : entry.getGenes().get(0).getOrderedLocusNames())
						out.add(oln.getValue());
				}
				else if(entry.getGenes().get(0).getORFNames()!=null) {

					for(ORFName oln : entry.getGenes().get(0).getORFNames())
						out.add(oln.getValue());
				}
			}
		}
		return out;
	}

	/**
	 * @param query
	 * @return
	 */
	public static String getFirstLocusTag(String query) {

		UniProtEntry uniProtEntry = UniProtAPI.getUniProtEntryFromXRef(query,0);

		return UniProtAPI.getLocusTags(uniProtEntry).get(0);//.getValue();
	}

	/**
	 * @param entry
	 * @return
	 */
	public static Set<String> get_ecnumbers(UniProtEntry entry) {

		Set<String> out = new HashSet<>();

		if(entry!=null) {

			for(String ec : entry.getProteinDescription().getEcNumbers())
				out.add(ec.trim());

			return out;
		}
		return null;
	}

	/**
	 * @param entry
	 * @return
	 */
	public static List<String> getECnumbers(UniProtEntry entry){

		if(entry!=null) {

			if(entry.getProteinDescription()!=null) {

				if(entry.getProteinDescription().getEcNumbers()!=null) {

					List<String> ecs = entry.getProteinDescription().getEcNumbers();
					if(!ecs.isEmpty())
						return ecs;
				}
			}
		}
		return null;
	}

	/**
	 * @param entry
	 * @return
	 */
	public static String getOrganism(UniProtEntry entry){

		if(entry!=null) {

			String organism = entry.getOrganism().getScientificName().getValue();
			if(organism!=null)
				return organism;

		}
		return null;
	}

	/**
	 * Get entry from UniProt accession.
	 * 
	 * @param entryRetrievalService
	 * @param uniprotID
	 * @param errorCount
	 * @return
	 */
	public static UniProtEntry getUniprotEntry(String uniprotID, int errorCount){

		UniProtAPI.getInstance();

		UniProtEntry entry = null;
		try
		{
			entry = (UniProtEntry) uniProtService.getEntry(uniprotID);
			return entry;
		}
		catch(ServiceException e){

			if(errorCount<10){

				MySleep.myWait(1000);
				logger.debug("GetUniprotEntry trial {}",errorCount);				
				entry=getUniprotEntry(uniprotID, (errorCount+1)); 
			}
			else {

				logger.error("GetUniprotEntry error {}",uniprotID);	
				logger.trace("StackTrace {}",e);
			}
		}

		return entry;
	}


	/**
	 * @param locusTag
	 * @param errorCount
	 * @return
	 */
	public static TaxonomyContainer setOriginOrganism(String locusTag, int errorCount) {

		TaxonomyContainer result= new TaxonomyContainer();

		UniProtAPI.getInstance();

		try {

			Query query = UniProtQueryBuilder.gene(locusTag);

			QueryResult<UniProtEntry> entries = uniProtService.getEntries(query);

			while(entries.hasNext()) {

				UniProtEntry uniProtEntry = entries.next();

				if (uniProtEntry != null) {

					result.setSpeciesName(uniProtEntry.getOrganism().getScientificName().getValue());
					result.setTaxonomy(uniProtEntry.getTaxonomy());

					return result;
				}
			}
		}
		catch(ServiceException e) { 

			if(errorCount<10) {

				errorCount = errorCount+1;
				MySleep.myWait(1000);
				logger.debug("setOriginOrganism trial {}",errorCount);	
				result=UniProtAPI.setOriginOrganism(locusTag, errorCount);
			}
			else {

				logger.error("setOriginOrganism error {}",locusTag);	
				logger.trace("StackTrace {}",e);
			}
		}

		return result;
	}

	/**
	 * @param entry
	 * @return
	 */
	public static TaxonomyContainer get_uniprot_entry_organism(String entry){

		TaxonomyContainer result = new TaxonomyContainer();

		UniProtEntry uniProtEntry = UniProtAPI.getEntryFromUniProtID(entry,0);
		if(uniProtEntry==null) {

			result.setSpeciesName("deleted");
			logger.error("Deleted {}",entry);
		}
		else {

			result.setSpeciesName(uniProtEntry.getOrganism().getScientificName().getValue());
			result.setTaxonomy(uniProtEntry.getTaxonomy());
		}
		return result;

	}


	/**
	 * @param entry
	 * @return
	 */
	public static String[] getOrganismTaxa(UniProtEntry entry) {
		String [] taxon = new String[2];

		if(entry!=null) {

			String organism = entry.getOrganism().getScientificName().getValue();
			if(organism!=null)
				taxon[0] = organism;

			String tax = entry.getTaxonomy().toString();
			if(tax!=null)
				taxon[1] = tax;

			return taxon;
		}
		return null;
	}


	/**
	 * @param locusTag
	 * @return
	 * @throws ServiceException 
	 */
	public static boolean isStarred(String locusTag) throws ServiceException {

		UniProtEntry entry = UniProtAPI.getEntry(locusTag,0);

		return UniProtAPI.isStarred(entry);
	}

	/**
	 * @param entry
	 * @return
	 */
	public static boolean isStarred(UniProtEntry entry) {

		if(entry==null)
			throw new NullPointerException("UniProt Entry Does not exist!");

		if(entry.getType().getValue().equals( UniProtEntryType.SWISSPROT.getValue()))
			return true;

		return false;
	}

	//	/**
	//	 * @param ecNumber
	//	 * @param reviewed
	//	 * @return
	//	 */
	//	public static EntryIterator<UniProtEntry> getEntriesByECNumber(String ecNumber, boolean reviewed) {
	//		Query query = UniProtQueryBuilder.buildECNumberQuery(ecNumber);
	//		if(reviewed)
	//			query = UniProtQueryBuilder.setReviewedEntries(query);
	//
	//		return uniProtQueryService.getEntryIterator(query);
	//
	//	};

	/**
	 * @param entry
	 * @return
	 * @throws CompoundNotFoundException 
	 */
	public static ProteinSequence getProteinSequenceFromEntry(UniProtEntry entry) throws CompoundNotFoundException {
		Sequence sequence = entry.getSequence();
		ProteinSequence proteinSequence = new ProteinSequence(sequence.getValue());
		AccessionID accession = 
				new AccessionID(entry.getUniProtId().toString(), DataSource.UNIPROT);
		proteinSequence.setAccession(accession);
		return proteinSequence;
	}

	/**
	 * @param query
	 * @return
	 */
	public static String getUniprotStatus(String query) {

		String star = null;
		try {

			UniProtEntry uniProtEntry = UniProtAPI.getUniProtEntryFromXRef(query,0);

			star = UniProtAPI.isStarred(uniProtEntry)+"";
		}
		catch(Exception e) {

			star = null;
			//e.printStackTrace();
		}
		return star;
	}

	/**
	 * @param query
	 * @return
	 */
	public static String getUniprotECnumbers(String query) {

		String uniprot_ecnumber = null;

		try {

			UniProtEntry uniProtEntry = UniProtAPI.getUniProtEntryFromXRef(query,0);

			Set<String> ecnumbers = UniProtAPI.get_ecnumbers(uniProtEntry);
			if(ecnumbers!= null) {

				uniprot_ecnumber = "";

				for(String ecnumber : ecnumbers)
					uniprot_ecnumber += ecnumber+", ";

				if(uniprot_ecnumber.contains(", "))
					uniprot_ecnumber = uniprot_ecnumber.substring(0, uniprot_ecnumber.lastIndexOf(", "));

			}
		}
		catch(Exception e) {

			logger.error("getUniprotECnumbers error {}",query);	
			logger.trace("StackTrace {}",e);
		}
		return uniprot_ecnumber;
	}

	/**
	 * @param uniprotID
	 * @return
	 */
	public static String getRefSeq(String uniprotID) {

		try {

			UniProtEntry uniProtEntry = UniProtAPI.getEntryFromUniProtID(uniprotID, 0);

			if(uniProtEntry!=null && uniProtEntry.getDatabaseCrossReferences(DatabaseType.REFSEQ)!=null) {

				for(DatabaseCrossReference dbcr : uniProtEntry.getDatabaseCrossReferences(DatabaseType.REFSEQ)) {

					for(String ref : dbcr.toString().split(":")) {

						//XM_, XR_, and XP_
						//NM_, NR_, and NP_
						//Nucleotide
						//ref.contains("XM")
						//ref.contains("NM")
						//ref.contains("XR")
						if(ref.contains("NR"))
							logger.warn("get ref "+uniprotID+" "+ref);

						if(ref.contains("XM") || ref.contains("XR") ||ref.contains("XP") || ref.contains("NM") ||ref.contains("NR") || ref.contains("NP"))
							return  ref.replace(";", "");
					}
				}
			}
			else {

				logger.warn("getRefSeq error for {} xrefs {} and not null {}.", uniprotID, uniProtEntry.getDatabaseCrossReferences(),uniProtEntry.getDatabaseCrossReferences()!= null);
			}
			return null;
		}
		catch (Exception e) {

			logger.error("GetRefSeq error {}.", uniprotID);
			logger.trace("StackTrace {}",e);
			return null;
		}
	}

	/**
	 * @param uniprotID
	 * @return
	 */
	public static Map<String, String> getRefSeq(List<String> uniprotIDs) {

		try {

			Map<String, String> ret = new HashMap<>();
			List<UniProtEntry> uniProtEntries = UniProtAPI.getEntriesFromUniProtIDs(uniprotIDs,0);

			for(UniProtEntry uniProtEntry: uniProtEntries) {

				if(uniProtEntry!=null && uniProtEntry.getDatabaseCrossReferences(DatabaseType.REFSEQ)!=null) {

					for(DatabaseCrossReference dbcr : uniProtEntry.getDatabaseCrossReferences(DatabaseType.REFSEQ)) {

						for(String ref : dbcr.toString().split(":")) {

							//XM_, XR_, and XP_
							//NM_, NR_, and NP_
							//Nucleotide
							//ref.contains("XM")
							//ref.contains("NM")
							//ref.contains("XR")
							if(ref.contains("NR"))
								logger.warn("get ref "+uniprotIDs+" "+ref);

							if( ref.contains("XP") || ref.contains("NR") || ref.contains("NP"))
								ret.put(uniProtEntry.getPrimaryUniProtAccession().getValue(), ref.replace(";", ""));
						}
					}
				}
				else {

					logger.debug("GetRefSeq error {}",uniProtEntry.getDatabaseCrossReferences());
				}
			}
			return ret;
		}
		catch (Exception e) {

			logger.error("getRefSeq error {}.", uniprotIDs);
			logger.trace("StackTrace {}",e);
			return null;
		}
	}

	/**
	 * @param refSeqIDs
	 * @param cancel
	 * @param errorCount
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, List<UniProtEntry>> getUniprotEntriesFromRefSeq(List<String> refSeqIDs, AtomicBoolean cancel, int errorCount) throws Exception {

		try {

			Map<String, List<UniProtEntry>> ret = new HashMap<>();

			List<String> ids = new ArrayList<>(refSeqIDs);

			int counter = 0;
			while(ids.size()>0 && counter<20) {

				ids = UniProtAPI.processXRefsData(ids, ret, cancel);
				counter++;
			}
			return ret;
		}
		catch (Exception e) {

			errorCount+=1;

			if(errorCount<20) {

				logger.debug("getUniprotEntriesFromRefSeq trial {}.",errorCount);
				MySleep.myWait(1000);

				return UniProtAPI.getUniprotEntriesFromRefSeq(refSeqIDs, cancel, errorCount);
			}
			else{

				logger.error("getUniprotEntriesFromRefSeq error {}.", refSeqIDs);
				logger.trace("StackTrace {}",e);
				throw new Exception();
			}
		}
	}

	/**
	 * @param refSeqIDs
	 * @param uniprotEntries
	 * @param cancel
	 * @return
	 * @throws ServiceException
	 */
	private static List<String> processXRefsData(List<String> refSeqIDs, Map<String, List<UniProtEntry>> uniprotEntries, AtomicBoolean cancel) throws ServiceException {

		List<String> ret = new ArrayList<>();

		UniProtAPI.getInstance();

		for(String refSeqID: refSeqIDs) {

			List<UniProtEntry> temp = new ArrayList<>();
			Query query = UniProtQueryBuilder.or(UniProtQueryBuilder.xref(refSeqID), 
					UniProtQueryBuilder.gene(refSeqID),
					UniProtQueryBuilder.id(refSeqID),
					UniProtQueryBuilder.keyword(refSeqID),
					UniProtQueryBuilder.proteinName(refSeqID),
					UniProtQueryBuilder.accession(refSeqID)
					);
			QueryResult<UniProtEntry> entries = uniProtService.getEntries(query);

			try {

				if(entries.getNumberOfHits()<1) {

					String parsedRefSeqID = refSeqID.split("\\.")[0];
					query =  UniProtQueryBuilder.or(UniProtQueryBuilder.xref(parsedRefSeqID), 
							UniProtQueryBuilder.gene(parsedRefSeqID),
							UniProtQueryBuilder.id(parsedRefSeqID),
							UniProtQueryBuilder.keyword(parsedRefSeqID),
							UniProtQueryBuilder.proteinName(parsedRefSeqID),
							UniProtQueryBuilder.accession(parsedRefSeqID));
					entries = uniProtService.getEntries(query);
				}

				while(entries.hasNext() && !cancel.get()) {

					UniProtEntry uniProtEntry = entries.next();
					temp.add(uniProtEntry);
				}
			} 
			catch (Exception e) {

				ret.add(refSeqID);
				logger.error("processXRefsData error {}.", refSeqID);
				logger.trace("StackTrace {}",e);
			}
			uniprotEntries.put(refSeqID, temp);
		}		

		return ret;
	}

	/**
	 * @param homologuesData
	 * @param homologuesList
	 * @param cancel
	 * @param uniprotStatus
	 * @param taxonomyID 
	 * @return
	 * @throws Exception
	 */
	public static AnnotationEnzymesHomologuesData getUniprotData(AnnotationEnzymesHomologuesData homologuesData, List<Pair<String, String>> homologuesList, 
			AtomicBoolean cancel, boolean uniprotStatus, long taxonomyID) throws Exception {

		List<String> genesList = new ArrayList<>();

		UniProtEntry geneEntry = UniProtAPI.getEntryFromUniProtID(homologuesData.getUniProtEntryID(),0); 

		if(geneEntry!=null)
			genesList.add(0,geneEntry.getPrimaryUniProtAccession().getValue());

		for(int i = 0; i<homologuesList.size();i++)
			if(!genesList.contains(homologuesList.get(i).getA()))
				genesList.add(genesList.size(),homologuesList.get(i).getA());

		List<String> dummyList = new ArrayList<>();
		for(int dummy = 0; dummy<genesList.size(); dummy++) 
			dummyList.add("");

		homologuesData.setLocusIDs(dummyList);

		List<UniProtEntry> entriesList = UniProtAPI.getEntriesFromUniProtIDs(genesList,0);

		for(int i=0 ; i<entriesList.size() ; i++) {

			UniProtEntry entry = entriesList.get(i);

			if(entry != null) {

				String primary_accession = entry.getPrimaryUniProtAccession().getValue();
				String locus = UniProtAPI.getLocusTag(entry);

				if(primary_accession.equalsIgnoreCase(homologuesData.getUniProtEntryID())) {

					if(!homologuesData.geteValue().containsKey(primary_accession))
						homologuesData.addEValue(primary_accession,0.0);
					if(!homologuesData.getBits().containsKey(primary_accession))
						homologuesData.addBits(primary_accession,999999);

					UniProtAPI.setHomologuesData(homologuesData, primary_accession, locus, entry);
				}

				homologuesData.addLocusID(primary_accession, genesList.indexOf(primary_accession));
				homologuesData.addLocusTag(primary_accession, locus);
				homologuesData.addBlastLocusTags(primary_accession, locus);

//				boolean goTaxonomyID = false;
//				for(NcbiTaxonomyId ncbiTaxonomyID : entry.getNcbiTaxonomyIds())			
//					if(Long.parseLong(ncbiTaxonomyID.getValue()) == taxonomyID)
//						goTaxonomyID=true;

//				String taxLocus = locus;
//				if(taxLocus.equalsIgnoreCase(primary_accession))
//					taxLocus = homologuesData.getQuery();

//				if(goTaxonomyID && (homologuesData.getLocusTag()==null || homologuesData.getLocusTag().equalsIgnoreCase(homologuesData.getQuery())))
//					UniProtAPI.setHomologuesData(homologuesData, primary_accession, taxLocus, entry);

				String name = null;

				try {

					for(int j = 0; j < entry.getProteinDescription().getRecommendedName().getFieldsByType(FieldType.FULL).size(); j++)
						if(name == null && entry.getProteinDescription().getRecommendedName().getFieldsByType(FieldType.FULL).get(j) != null)
							name = entry.getProteinDescription().getRecommendedName().getFieldsByType(FieldType.FULL).get(j).getValue();
				}
				catch (Exception e) {name = null;}

				if(name == null) {

					try {

						for(int j = 0; j < entry.getProteinDescription().getSubNames().size(); j++)
							for(int k = 0; k < entry.getProteinDescription().getSubNames().get(j).getFieldsByType(FieldType.FULL).size(); k++)
								if(name == null && entry.getProteinDescription().getSubNames().get(j).getFieldsByType(FieldType.FULL).get(k) != null)
									name = entry.getProteinDescription().getSubNames().get(j).getFieldsByType(FieldType.FULL).get(k).getValue();
					} 
					catch (Exception e) {name=null;}
				}

				homologuesData.addDefinition(primary_accession, name);
				homologuesData.addProduct(primary_accession, name);		
				homologuesData.addOrganism(primary_accession, entry.getOrganism().getScientificName().getValue());
				homologuesData.addTaxonomy(primary_accession, entry.getTaxonomy().toString().replace(",", ";").replace("[", "").replace("]", ""));
				homologuesData.addUniprotStatus(primary_accession, entry.getType().getValue().equals(UniProtEntryType.SWISSPROT.getValue()));

				try {

					List<String> le = entry.getProteinDescription().getEcNumbers();
					if(le.size()>0) {

						String[] ec = new String[le.size()]; 
						homologuesData.addECnumbers(primary_accession, le.toArray(ec));
					}
				} 
				catch (Exception e) {

					logger.debug("getUniprotData getProteinDescription error {}.", primary_accession);
				}

				try {
					List<Organelle> organelles = entry.getOrganelles();
					if(organelles.size()>0)
						homologuesData.addOrganelles(primary_accession, organelles.toString());

				} catch (Exception e) {

					logger.debug("getUniprotData getOrganelles error {}.", primary_accession);
				}
			}
		}

		return homologuesData;
	}


	/**
	 * @param homologuesData
	 * @param primary_accession
	 * @param locus
	 * @param entry
	 * @return
	 */
	private static AnnotationEnzymesHomologuesData setHomologuesData(AnnotationEnzymesHomologuesData homologuesData, String primary_accession, String locus, UniProtEntry entry) {

		homologuesData.setLocusTag(locus);
		homologuesData.setOrganismID(homologuesData.getOrganismTaxa()[0]);
		homologuesData.setFastaSequence(entry.getSequence().getValue());

		try {

			homologuesData.setGene(entry.getGenes().get(0).getGeneName().getValue());
		}
		catch (Exception e) {

			logger.trace("StackTrace {}",e);
		}

		homologuesData.addUniprotStatus(homologuesData.getQuery(), entry.getType().getValue().equals(UniProtEntryType.SWISSPROT.getValue()));

		return homologuesData;
	}

	/**
	 * Get parsed UniProt entry data from locus tag and taxonomy identifier.
	 * 
	 * @param query
	 * @param taxonomyID
	 * @return
	 */
	public static EntryData getEntryData(String query, long taxonomyID) {

		return UniProtAPI.getEntryData(query, taxonomyID, 0);
	}

	/**
	 * Get parsed UniProt entry data from UniProt accession.
	 * 
	 * @param query
	 * @return
	 */
	public static EntryData getEntryDataFromAccession(String accession) {

		return UniProtAPI.getEntryDataFromAccession(accession,0);
	}


	/**
	 * @param entry
	 * @return
	 */
	public static String getLocusTag(UniProtEntry entry) {


		String out = null;

		try {

			for(int i = 0; i<entry.getGenes().size(); i++)
				if(out == null)
					for(int j = 0; j<entry.getGenes().get(i).getOrderedLocusNames().size(); j++) {

						if(out == null && entry.getGenes().get(i).getOrderedLocusNames().get(j).getValue()!= null )
							out = entry.getGenes().get(i).getOrderedLocusNames().get(j).getValue();

					}
		}
		catch(Exception e) {

			out=null;
		}

		if(out==null) {

			try {

				for(int i = 0; i<entry.getGenes().size(); i++)
					if(out == null)
						for(int j = 0; j<entry.getGenes().get(i).getORFNames().size(); j++) {

							if(out == null && entry.getGenes().get(i).getORFNames().get(j).getValue()!= null )
								out = entry.getGenes().get(i).getORFNames().get(j).getValue();
						}
				if(out==null) 
					out = entry.getPrimaryUniProtAccession().getValue();
			}
			catch(Exception e) {

				out = entry.getPrimaryUniProtAccession().getValue();
				logger.debug("getLocusData error {}.", out);
				logger.trace("StackTrace {}",e);
			}
		}
		return out;
	}

	/**
	 * Get entry data from query.
	 * 
	 * @param query
	 * @param taxonomyID 
	 * @return
	 */
	public static String getProduct(String query, long taxonomyID) {

		EntryData entryData = UniProtAPI.getEntryData(query, taxonomyID);
		String function = entryData.getFunction();

		if(function != null && entryData.getEcNumbers()!= null && entryData.getEcNumbers().size()>0)
			function = function.concat(" (EC:").concat(entryData.getEcNumbers().toString().replaceAll("\\[", "").replaceAll("\\]", "")).concat(")");

		return function;
	}

	/**
	 * @param query
	 * @param taxonomyID 
	 * @param errorCount
	 * @return
	 */
	private static EntryData getEntryData(String query, long taxonomyID, int errorCount) {

		EntryData entry;

		UniProtEntry uniProtEntry;

		uniProtEntry = UniProtAPI.getUniProtEntryFromXRef(query, taxonomyID, 0);

		try {

			boolean go = true;

			if(uniProtEntry==null) {

				uniProtEntry = UniProtAPI.getUniprotEntry(query, errorCount);
				go = false;

				if(taxonomyID <0)
					go=true;
				else if(uniProtEntry!=null && uniProtEntry.getNcbiTaxonomyIds() != null)
					for(NcbiTaxonomyId ncbiTaxonomyID : uniProtEntry.getNcbiTaxonomyIds())	
						if(ncbiTaxonomyID.getValue().equalsIgnoreCase(String.valueOf(taxonomyID)))
							go=true;
			}

			if(go) {

				entry = new EntryData(uniProtEntry.getPrimaryUniProtAccession().getValue());
				entry.setEcNumbers(UniProtAPI.get_ecnumbers(uniProtEntry));
				entry.setUniprotReviewStatus(UniProtAPI.isStarred(uniProtEntry)+"");

				if(uniProtEntry.getProteinDescription().getRecommendedName().getFieldsByType(FieldType.FULL).size()>0)
					entry.setFunction(uniProtEntry.getProteinDescription().getRecommendedName().getFieldsByType(FieldType.FULL).get(0).getValue());

				if(entry.getFunction()==null && uniProtEntry.getProteinDescription().getSubNames().size()>0 &&
						uniProtEntry.getProteinDescription().getSubNames().get(0).getFieldsByType(FieldType.FULL).size()>0)
					entry.setFunction(uniProtEntry.getProteinDescription().getSubNames().get(0).getFieldsByType(FieldType.FULL).get(0).getValue());

				String locus = UniProtAPI.getLocusTag(uniProtEntry);

				if(locus.equals(entry.getEntryID()))
					locus = query;

				entry.setLocusTag(locus);
			}
			else {

				entry = new EntryData(query.replace(".*", ""));
				entry.setUniprotReviewStatus(null);
				entry.setEcNumbers(null);
				entry.setLocusTag(query.replace(".*", ""));
			}

		}
		catch (Exception e) {

			logger.trace("StackTrace {}",e);

			errorCount+=1;

			if(errorCount<3) {

				MySleep.myWait(1000);
				logger.trace("getEntryData trial {}.", errorCount);
				entry = UniProtAPI.getEntryData(query, taxonomyID, errorCount);
			}
			else {

				if(errorCount<5) {

					MySleep.myWait(1000);
					String newQuery = query;
					if(query.contains("."))
						newQuery = query.split("\\.")[0];

					logger.trace("Get EntryData trial {}.", errorCount);
					entry = UniProtAPI.getEntryData(newQuery+".*", taxonomyID, errorCount);
					entry.setEntryID(query);
					entry.setLocusTag(query);
				}
				else {

					entry = new EntryData(query);
					entry.setUniprotReviewStatus(null);
					entry.setEcNumbers(null);
					entry.setLocusTag(query);
					logger.trace("Get EntryData error {}.", query);
				}
			}
		}

		return entry;
	}

	/**
	 * @param accession
	 * @param errorCount
	 * @return
	 */
	private static EntryData getEntryDataFromAccession(String accession, int errorCount) {

		EntryData entry;

		UniProtEntry uniProtEntry;

		uniProtEntry = UniProtAPI.getUniprotEntry(accession,0);

		try {

			entry = new EntryData(uniProtEntry.getPrimaryUniProtAccession().getValue());
			entry.setEcNumbers(UniProtAPI.get_ecnumbers(uniProtEntry));
			entry.setUniprotReviewStatus(UniProtAPI.isStarred(uniProtEntry)+"");

			if(uniProtEntry.getProteinDescription().getRecommendedName().getFieldsByType(FieldType.FULL).size()>0)
				entry.setFunction(uniProtEntry.getProteinDescription().getRecommendedName().getFieldsByType(FieldType.FULL).get(0).getValue());

			String locus = UniProtAPI.getLocusTag(uniProtEntry);

			if(locus.equals(entry.getEntryID()))
				locus = accession;

			entry.setLocusTag(locus);

		}
		catch (Exception e) {

			logger.trace("StackTrace {}",e);

			errorCount+=1;

			if(errorCount<3) {

				MySleep.myWait(1000);
				logger.debug("getEntryDataFromAccession trial {}.", errorCount);
				return UniProtAPI.getEntryDataFromAccession(accession, errorCount);
			}
			else {

				logger.warn("getEntryDataFromAccession error {}.", accession);

				entry = new EntryData(accession.replace(".*", ""));
				entry.setUniprotReviewStatus(null);
				entry.setEcNumbers(null);
				entry.setLocusTag(accession.replace(".*", ""));
			}
		}

		return entry;
	}

	/**
	 * @param orgID
	 * @param i
	 * @return
	 */
	//	public static String[] newTaxID(String organismmId, int i) {
	//
	//		UniProtAPI.getInstance();
	//
	//		try {
	//			String[] result = new String[2];
	//
	//			Query query = UniProtQueryBuilder.organismName(organismmId);
	//			QueryResult<UniProtEntry> entries = uniProtService.getEntries(query);
	//
	//			while (entries.hasNext()) {
	//
	//				UniProtEntry entry = entries.next();
	//				result[0] = entry.getOrganism().getScientificName().getValue();
	//				result[1] = entry.getTaxonomy().toString().replace(",", ";").replace("[", "").replace("]", "");
	//
	//				return result;
	//			}
	//
	//		} catch (ServiceException e) {
	//
	//			logger.debug("newTaxID ServiceException trial {}.", i);
	//			i++;
	//			if(i<10)
	//				return newTaxID(organismmId, i);
	//			
	//			logger.error("newTaxID ServiceException error {}.", organismmId);
	//			logger.trace("StackTrace {}",e);
	//		}
	//
	//		return null;
	//	} 

	public static String[] newTaxID(int organismmId, int i) {

		UniProtAPI.getInstance();

		try {
			String[] result = new String[2];

			Query query = UniProtQueryBuilder.taxonID(organismmId);
			QueryResult<UniProtEntry> entries = uniProtService.getEntries(query);

			while (entries.hasNext()) {

				UniProtEntry entry = entries.next();
				result[0] = entry.getOrganism().getScientificName().getValue();
				result[1] = entry.getTaxonomy().toString().replace(",", ";").replace("[", "").replace("]", "");

				return result;
			}

		} catch (ServiceException e) {

			e.printStackTrace();

			logger.debug("newTaxID ServiceException trial {}.", i);
			i++;
			if(i<10)
				return newTaxID(organismmId, i);

			logger.error("newTaxID ServiceException error {}.", organismmId);
			logger.trace("StackTrace {}",e);
		}

		return null;
	} 


	/**
	 * @param accessionNumber
	 * @param organism
	 * @return
	 */
	public static String retrieveLocusTagIfOrganism(String accessionNumber, String organism) {

		UniProtAPI.getInstance();

		//Retrieve UniProt entry by its accession number
		UniProtEntry entry = UniProtAPI.getUniprotEntry(accessionNumber, 0);

		//If entry with a given accession number is not found, entry will be equal null
		if (entry != null) {

			if(entry.getOrganism().getScientificName().getValue().contains(organism)) {

				logger.trace("entry = {}",entry.getUniProtId().getValue());
				if(entry.getGenes().size()>0 && entry.getGenes().get(0).getOrderedLocusNames().size()>0) {

					String locusTag = entry.getGenes().get(0).getOrderedLocusNames().get(0).getValue();
					if(locusTag!=null)						
						return locusTag;
				}
			}
		}

		return "";
	}

	/**
	 * @return
	 */
	public static UniProtAPI getInstance() {

		if(uniProtApi == null)
			UniProtAPI.uniProtApi = new UniProtAPI();

		if(!uniProtService.isStarted())
			uniProtService.start();

		return uniProtApi;
	}

	/**
	 * 
	 */
	public static void stopUniProtService(){

		UniProtAPI.getInstance();
		uniProtService.stop();
	}



	/**
	 * Get parsed UniProt entry data from locus tag.
	 * 
	 * @param query
	 * @param taxonomyID
	 * @return
	 */
	public static EntryData getEntryData(String query) {

		return UniProtAPI.getEntryData(query, -1, 0);
	}

}
