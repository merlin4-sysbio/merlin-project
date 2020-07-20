package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.datatypes.EntryData;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.datatypes.NcbiData;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot.UniProtAPI;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.GBFeature;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.GBQualifier;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.GBSeq;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.GBSet;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.MySleep;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologuesData;
import pt.uminho.ceb.biosystems.merlin.utilities.datastructures.list.ListUtilities;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;


/**
 * @author ODias
 *
 */
public class EntrezFetch {

	final static Logger logger = LoggerFactory.getLogger(EntrezFetch.class);
	private EntrezService entrezService;

	/**
	 * @param numConnections
	 * @throws Exception
	 */
	public EntrezFetch() throws Exception {

		EntrezServiceFactory entrezServiceFactory = new EntrezServiceFactory("https://eutils.ncbi.nlm.nih.gov/entrez/eutils", false);
		this.entrezService = entrezServiceFactory.build();
	}


	/**
	 * @param GBSeq
	 * @param featureKey
	 * @return
	 */
	private static Map<String,String> getFeatures(GBSeq GBSeq, String featureKey) {

		Map<String, String> ret = new HashMap<>();

		List<GBFeature> gbFeatures =  GBSeq.features;

		for(int j=0;j<gbFeatures.size();j++) {

			if(featureKey.equalsIgnoreCase(gbFeatures.get(j).featureKey)) {

				List<GBQualifier> gbQualifiers = gbFeatures.get(j).qualifiers;

				for(int k=0;k<gbQualifiers.size();k++) {

					String name = gbQualifiers.get(k).name;
					String value = gbQualifiers.get(k).value;

					ret.put(name, value);
				}
			}
		}
		return ret;
	}

	/**
	 * @param genes
	 * @param queryResponseConcatenationSize
	 * @return
	 */
	public Map<String,String> getLocusFromID(Set<String> genes, int queryResponseConcatenationSize) {

		List<String> temp = new ArrayList<String>();
		Map<String,String> result = new HashMap<String,String>();
		List<String> ids_list = new ArrayList<String>(genes);

		try {	

			List<String> queryList = new ArrayList<String>();
			String links="";
			int index=0;

			double size = genes.size();

			for(int i=0; i<ids_list.size();i++) {

				if(index>0)
					links=links.concat(",");

				links=links.concat(ids_list.get(i).replace("\"", ""));
				index++;

				if(index>queryResponseConcatenationSize) {

					queryList.add(new String(links));
					links="";
					index=0;
				}
			}
			queryList.add(links);

			int counter = 0;
			boolean existsLocusTags = true;
			for(String query:queryList) {

				counter +=query.split(",").length;

				double progress = counter/size;
				logger.trace("NCBIAPI.getLocusFromID query {}%",progress*100);

				query = new String(query.getBytes(),"UTF-8");
				GBSet gbSet = this.entrezService.eFetch(NcbiDatabases.protein, query, "xml");

				List<GBSeq> gbSeqs = gbSet.gBSeq;

				for (int i = 0; i < gbSeqs.size(); i++) {

					GBSeq gbSeq = gbSeqs.get(i);
					String primary_accession = gbSeq.accessionVersion;

					logger.trace("gbSeq {}",gbSeq);

					Map<String, String> features = getFeatures(gbSeq, "CDS");

					result.put(primary_accession, primary_accession);

					if(features.containsKey("locus_tag")) {

						result.put(primary_accession, features.get("locus_tag"));
						temp.add(primary_accession);
					}

					if(result.get(primary_accession).equals(primary_accession)) {

						features = getFeatures(gbSeq, "Protein");

						if(features.containsKey("locus_tag")) {

							result.put(primary_accession, features.get("locus_tag"));
							temp.add(primary_accession);
						}
					}

					if(result.get(primary_accession).equals(primary_accession)) {

						features = getFeatures(gbSeq, "gene");

						if(features.containsKey("locus_tag")) {

							result.put(primary_accession, features.get("locus_tag"));
							temp.add(primary_accession);
						}
					}
					
					if(result.get(primary_accession).equals(primary_accession)) {

						features = getFeatures(gbSeq, "Region");

						if(features.containsKey("locus_tag")) {

							result.put(primary_accession, features.get("locus_tag"));
							temp.add(primary_accession);
						}
					}

					
					if(!temp.contains(primary_accession) && existsLocusTags) {

						UniProtEntry uni = UniProtAPI.getUniProtEntryFromXRef(primary_accession,0);

						List<String> g = UniProtAPI.getLocusTags(uni); 
						if(g!= null && g.size()>0) {

							String locus = g.get(0);
							result.put(primary_accession, locus);

							logger.trace("locus.equalsIgnoreCase(primary_accession) {}",locus.equalsIgnoreCase(primary_accession));

							if(locus.equalsIgnoreCase(primary_accession))
								existsLocusTags = false;
						}
						else {

							existsLocusTags = false;
						}
					}
				}
			}
			return result;
		}
		catch (Exception e) {

			if(queryResponseConcatenationSize>0) {

				MySleep.myWait(3000);

				int ind = (queryResponseConcatenationSize/2);

				if(ind<2)
					queryResponseConcatenationSize = 0;
				else
					queryResponseConcatenationSize=queryResponseConcatenationSize/2;

				logger.debug("getLocusFromID new initalNumberOfRequests = {}",queryResponseConcatenationSize);
				return getLocusFromID(genes, queryResponseConcatenationSize);
			}
			else {

				logger.error("Get locus error for query {} ", genes);
				logger.error("Stack trace {}",e);
				return null;
			}
		}
	}

	/**
	 * @param accession
	 * @param errorCount
	 * @return
	 */
	public EntryData getEntryDataFromAccession(String accession, int errorCount) {

		EntryData entryData = new EntryData(accession);
		entryData.setLocusTag(accession);

		GBSet gbSet = this.entrezService.eFetch(NcbiDatabases.protein, accession, "xml");

		List<GBSeq> gbSeqs = gbSet.gBSeq;

		for (int i = 0; i < gbSeqs.size(); i++) {

			GBSeq gbSeq = gbSeqs.get(i);
			String primary_accession = gbSeq.accessionVersion;

			entryData.setEntryID(primary_accession);

			Map<String, String> features = getFeatures(gbSeq, "CDS");

			if(features.containsKey("locus_tag"))
				entryData.setLocusTag(features.get("locus_tag"));

			if(features.containsKey("coded_by"))
				entryData.setCodedBy(features.get("coded_by"));

			features = getFeatures(gbSeq, "source");

			if(features.containsKey("db_xref") && features.get("db_xref").contains("taxon"))
				entryData.setTaxonomyID(Long.parseLong(features.get("db_xref").replaceAll("taxon\\:", "")));

			if(features.containsKey("organism"))
				entryData.setOrganism(features.get("organism"));

			if(features.containsKey("EC_number"))
				entryData.addEcNumber(features.get("EC_number"));

			features = getFeatures(gbSeq, "Protein");

			if(features.containsKey("product"))
				entryData.setFunction(features.get("product"));

			if(features.containsKey("EC_number"))
				entryData.addEcNumber(features.get("EC_number"));

			features = getFeatures(gbSeq, "source");

			if(features.containsKey("db_xref")) {

				if(features.get("db_xref").startsWith("UniProtKB/Swiss-Prot"))
					entryData.setUniprotReviewStatus("true");

				if(features.get("db_xref").startsWith("UniProtKB/TrEMBL"))
					entryData.setUniprotReviewStatus("false");
			}
		}

		return entryData;
	}

	/**
	 * @param geneID
	 * @return
	 * @throws RemoteException 
	 */
	public String getTaxonomy(String geneID) throws Exception {

		String taxID = null;

		GBSet gbSet = this.entrezService.eFetch(NcbiDatabases.protein, geneID, "xml");

		List<GBSeq> gbSeqs = gbSet.gBSeq;

		for (int i = 0; i < gbSeqs.size(); i++) {

			GBSeq gbSeq = gbSeqs.get(i);

			Map<String, String> features = getFeatures(gbSeq, "source");

			if(features.containsKey("db_xref")) {

				if(features.get("db_xref").startsWith("taxon")) {

					taxID = features.get("db_xref").replace("taxon:", "");
				}
			}
		}
		return taxID;	
	}


	/**
	 * @param ids_list
	 * @param queryResponseConcatenationSize
	 * @return
	 */
	public Map<String, ProteinSequence> createSequencesMap(List<String> ids_list, int queryResponseConcatenationSize){
		Map<String,ProteinSequence> sequences = new HashMap<String,ProteinSequence>();
		Set<String> added= new TreeSet<String>();

		try {

			List<String> queryList = new ArrayList<String>();
			String links="";
			int index=0;
			for(int i=0; i<ids_list.size();i++) {

				if(index>0) {

					links=links.concat(",");
				}
				links=links.concat(ids_list.get(i));
				index++;
				if(index>queryResponseConcatenationSize) {

					queryList.add(links);
					links="";
					index=0;
				}
			}
			queryList.add(links);

			for(String query:queryList) {

				GBSet gbSet = this.entrezService.eFetch(NcbiDatabases.protein, query, "xml");

				List<GBSeq> gbSeqs = gbSet.gBSeq;

				for (int i = 0; i < gbSeqs.size(); i++) {

					GBSeq gbSeq = gbSeqs.get(i);
					String primary_accession = gbSeq.accessionVersion;

					Map<String, String> features = getFeatures(gbSeq, "CDS");

					if(features.containsKey("locus_tag")) {

						sequences.put(features.get("locus_tag"), new ProteinSequence(gbSeq.sequence.toUpperCase()));
						added.add(primary_accession);
					}

					String acc = primary_accession;
					if(acc!=null) {

						if(!added.contains(primary_accession)) {

							UniProtEntry uni = UniProtAPI.getUniProtEntryFromXRef(acc,0); 
							if(uni!=null) {

								String locus = UniProtAPI.getLocusTags(uni).get(0);//.getValue();
								sequences.put(locus, 
										new ProteinSequence(gbSeq.sequence.toUpperCase()));
								added.add(primary_accession);
							}
						}
					}
				}
			}
			return sequences;
		}
		catch (Exception e)  {

			if(queryResponseConcatenationSize>5) {

				MySleep.myWait(3000);

				logger.debug("createSequencesMap new initalNumberOfRequests = {}",queryResponseConcatenationSize);
				queryResponseConcatenationSize=queryResponseConcatenationSize/2;
				return this.createSequencesMap(ids_list, queryResponseConcatenationSize);
			}

			logger.trace("Stack trace {}",e);
		}
		return null;
	}

	/**
	 * @param ids_list
	 * @param queryResponseConcatenationSize
	 * @return
	 */
	public Pair<Map<String, String>,Map<String, AbstractSequence<?>>> getLocusAndSequencePairFromID(List<List<String>> ids_list, int queryResponseConcatenationSize, String sourceDB){

		try {

			List<List<String>> queryList = new ArrayList<List<String>>();
			List<String> links= new ArrayList<String>();
			int index=0;
			for(int i=0; i<ids_list.size();i++)
			{
				for(int j=0; j<ids_list.get(i).size();j++)
				{
					links.add(ids_list.get(i).get(j).trim());
				}
				index++;
				if(index>=queryResponseConcatenationSize)
				{
					if(!links.isEmpty())
					{
						queryList.add(links);
					}
					links=new ArrayList<String>();
					index=0;
				}
			}
			if(!links.isEmpty())
			{
				queryList.add(links);
			}

			List<Thread> threads = new ArrayList<Thread>();
			ConcurrentLinkedQueue<List<String>> queryArray = new ConcurrentLinkedQueue<List<String>>(queryList);

			int numberOfCores = Runtime.getRuntime().availableProcessors()*2;
			if(queryArray.size()<numberOfCores){numberOfCores=queryArray.size();}

			ConcurrentHashMap<String, AbstractSequence<?>> sequences = new ConcurrentHashMap<String, AbstractSequence<?>>();
			ConcurrentHashMap<String,String> locus_Tag = new ConcurrentHashMap<String,String>();

			for(int i=0; i<numberOfCores; i++) {

				Runnable lc	= new RunnableSequencesRetriever(queryArray,locus_Tag,sequences,sourceDB);
				Thread thread = new Thread(lc);
				threads.add(thread);
				thread.start();
			}

			for(Thread thread :threads)
				thread.join();


			Map<String, AbstractSequence<?>> sequences_Pair = new HashMap<String, AbstractSequence<?>>(sequences);
			Map<String,String> locus_Tag_Pair = new HashMap<String,String>(locus_Tag);

			return new Pair<Map<String, String>,Map<String, AbstractSequence<?>>>(locus_Tag_Pair,sequences_Pair);
		}
		catch (Exception e) {

			if(queryResponseConcatenationSize>5) {

				MySleep.myWait(3000);

				logger.debug("GetLocusAndSequencePairFromID Reducing query size to {} ids.",queryResponseConcatenationSize);
				queryResponseConcatenationSize=queryResponseConcatenationSize/2;
				return this.getLocusAndSequencePairFromID(ids_list, queryResponseConcatenationSize,sourceDB);
			}

			logger.trace("Stack trace {}",e);
		}
		return null;
	}

	/**
	 * @param query
	 * @param sourceDB
	 * @param ncbiData
	 * @return
	 * @throws Exception
	 */
	public NcbiData getSequences(List<String> query, String sourceDB, NcbiData ncbiData) throws Exception {

		List<String> added = new ArrayList<String>();

		String queryString = new String(query.toString().replace("[", "").replace("]", "").replace(" ", "").getBytes(),"UTF-8");

		GBSet gbSet = this.entrezService.eFetch(NcbiDatabases.protein, queryString, "xml");

		List<GBSeq> gbSeqs = gbSet.gBSeq;

		for (int i = 0; i < gbSeqs.size(); i++) {

			GBSeq gbSeq = gbSeqs.get(i);

			String primary_accession = gbSeq.accessionVersion;

			Map<String, String> features = getFeatures(gbSeq, "CDS");

			if(features.containsKey("locus_tag")) {

				ncbiData.addLocusTag(primary_accession, features.get("locus_tag"));
				ncbiData.addSequence(primary_accession, new ProteinSequence(gbSeq.sequence.toUpperCase()));
				added.add(primary_accession);
			}

			String acc = gbSeq.accessionVersion;

			if(acc!=null &&!added.contains(acc)) {

				UniProtEntry uniProtEntry = UniProtAPI.getUniProtEntryFromXRef(acc,0);
				if(uniProtEntry!=null) {

					if(UniProtAPI.getLocusTags(uniProtEntry)!=null && UniProtAPI.getLocusTags(uniProtEntry).size()>0) {

						String locus = UniProtAPI.getLocusTags(uniProtEntry).get(0);//.getValue();
						ncbiData.addLocusTag(acc, locus);
						ncbiData.addSequence(acc, new ProteinSequence(gbSeq.sequence.toUpperCase()));
						added.add(acc);
					}
					else {

						//System.err.println(UniprotAPI.getUniProtEntryID(acc).getUniProtId());
					}
				}
			}
		}

		return ncbiData;
	}

	/**
	 * @param ncbiData
	 * @param cancel
	 * @param resultsPairs
	 * @param taxonomyIDs
	 * @param uniprotStatus
	 * @param taxonomyID
	 * @return
	 * @throws Exception
	 */
	public AnnotationEnzymesHomologuesData getNcbiData(AnnotationEnzymesHomologuesData ncbiData, AtomicBoolean cancel, List<Pair<String, String>> resultsPairs, 
			Map<String, String> taxonomyIDs, boolean uniprotStatus, long taxonomyID) throws Exception {

		ncbiData.setOrganismID(ncbiData.getOrganismTaxa()[0]);
		List<String> resultList = new ArrayList<>(), accessionNumbers = new ArrayList<>();

		for(int i = 0; i< resultsPairs.size(); i++)
			resultList.add(resultsPairs.get(i).getA());

		List<List<String>> resultsListsList = ListUtilities.split(resultList, 99);

		List<GBSeq> gbSeqs = new ArrayList<>();

		for(List<String> result : resultsListsList) {

			if(!result.isEmpty()) {

				GBSet gbSet = this.getEntries(result, 0);
				gbSeqs.addAll(gbSeqs.size(), gbSet.gBSeq);
			}
		}

		List<String> dummyList = new ArrayList<>();
		for(int dummy = 0; dummy<gbSeqs.size(); dummy++) 
			dummyList.add("");

		ncbiData.setLocusIDs(dummyList);

		for (int i = 0; i < gbSeqs.size(); i++) {

			GBSeq gbSeq = gbSeqs.get(i);

			boolean locusSet = false;

			if(ncbiData.getLocusTag()!=null && !ncbiData.getLocusTag().equalsIgnoreCase(ncbiData.getQuery()))
				locusSet = true;

			if(cancel.get()) {

				i=gbSeqs.size();
			}
			else {

				String primary_accession;

				if (gbSeq.accessionVersion!=null) {		

					primary_accession = gbSeq.accessionVersion; 
					ncbiData.addDefinition(primary_accession, gbSeq.definition);

					Map<String, String> features = EntrezFetch.getFeatures(gbSeq, "source");

					if(primary_accession!=null && !accessionNumbers.contains(primary_accession))
						accessionNumbers.add(primary_accession);

					if(i==0 || ncbiData.getBits(primary_accession)>=0) {

						ncbiData.addEValue(primary_accession,ncbiData.getEvalue(primary_accession));
						ncbiData.addBits(primary_accession,ncbiData.getBits(primary_accession));
						ncbiData.addLocusID(primary_accession, i);

						boolean goTaxonomyID = false;
						boolean firstHit = false;

						if(features.containsKey("db_xref") && features.get("db_xref").startsWith("taxon"))	{

							String taxID = features.get("db_xref").replace("taxon:", "");
							taxonomyIDs.put(primary_accession, features.get("db_xref").replace("taxon:", ""));

							if(taxonomyID==Long.parseLong(taxID))
								goTaxonomyID = true;
						}

						if(!goTaxonomyID && i==0 && ncbiData.getBits(primary_accession)<0) {

							goTaxonomyID = true;
							firstHit = true;
						}

						////////////////////////////////////////////////////////////////////////////////////
						////////////////////////////////////////////////////////////////////////////////////

						LinkedList<String> ecn = new LinkedList<String>();

						if(features.containsKey("EC_number"))
							ecn.add(features.get("EC_number"));

						features = getFeatures(gbSeq, "Protein");

						if(features.containsKey("product"))
							ncbiData.addProduct(primary_accession, features.get("product"));

						if(features.containsKey("calculated_mol_wt"))
							ncbiData.addCalculated_mol_wt(primary_accession,features.get("calculated_mol_wt"));

						if(features.containsKey("EC_number"))
							ecn.add(features.get("EC_number"));

						if(features.containsKey("note"))
							ncbiData.setLocus_protein_note(features.get("note"));

						if(!ecn.isEmpty()) {

							String[] ecnb = new String[ecn.size()];
							for(int e=0;e<ecn.size();e++){ecnb[e]=ecn.get(e);}
							ncbiData.addECnumbers(primary_accession, ecnb);
						}

						////////////////////////////////////////////////////////////////////////////////////
						////////////////////////////////////////////////////////////////////////////////////

						features = getFeatures(gbSeq, "CDS");

						if(features.containsKey("locus_tag")) {

							if(goTaxonomyID && (i==0 || !locusSet)) {

								firstHit = true;
								locusSet = true;
								ncbiData.setLocusTag(features.get("locus_tag"));
							}	
							ncbiData.addBlastLocusTags(primary_accession, features.get("locus_tag"));
						}

						////////////////////////////////////////////////////////////////////////////////////
						////////////////////////////////////////////////////////////////////////////////////

						if(features.containsKey("coded_by")) {

							if(goTaxonomyID) {

								if(!locusSet && ncbiData.getSequence_code().equalsIgnoreCase(features.get("coded_by").split(":")[1])) {

									firstHit = true;
									locusSet = true;
									ncbiData.setLocusTag(ncbiData.getBlastLocusTag().get(primary_accession));
								}
							}

							////////////////////////////////////////////////////////////////////////////////////
							////////////////////////////////////////////////////////////////////////////////////

							if(features.containsKey("note") && goTaxonomyID && !features.get("note").contains(""))
								ncbiData.setLocus_gene_note(features.get("note") );

						}

						////////////////////////////////////////////////////////////////////////////////////
						////////////////////////////////////////////////////////////////////////////////////

						if(i==0 && ncbiData.getLocusTag() == null && ncbiData.getUniprotLocusTag()!=null) {

							ncbiData.setLocusTag(ncbiData.getUniprotLocusTag());
							locusSet=true;
							firstHit = true;
						}

						////////////////////////////////////////////////////////////////////////////////////
						////////////////////////////////////////////////////////////////////////////////////

						if(features.containsKey("organelle")) {

							if(goTaxonomyID && firstHit && locusSet)
								ncbiData.setOrganelle(features.get("organelle"));
							else
								ncbiData.addOrganelles(primary_accession, features.get("organelle"));
						}

						////////////////////////////////////////////////////////////////////////////////////
						////////////////////////////////////////////////////////////////////////////////////

						if(features.containsKey("chromosome")) {

							if(goTaxonomyID && firstHit && locusSet) 
								ncbiData.setChromosome(features.get("chromosome"));
						}
						////////////////////////////////////////////////////////////////////////////////////
						////////////////////////////////////////////////////////////////////////////////////

						if(features.containsKey("gene")) {

							if(goTaxonomyID && firstHit && locusSet) {

								String tempGene= features.get("gene") ;

								if(tempGene!=null)
									ncbiData.setGene(tempGene);
								else
									ncbiData.setGene("");
							}
							else {

								ncbiData.addGenes(primary_accession, features.get("gene") );
							}
						}

						if(goTaxonomyID && firstHit && locusSet && (ncbiData.getGene()==null || ncbiData.getGene().isEmpty())) {

							String tempGene=ncbiData.getGenes().get(primary_accession);

							if(tempGene!=null)
								ncbiData.setGene(tempGene);
							else
								ncbiData.setGene("");
						}

						////////////////////////////////////////////////////////////////////////////////////
						////////////////////////////////////////////////////////////////////////////////////

						if(features.containsKey("db_xref")) {

							if(features.get("db_xref").startsWith("UniProtKB/Swiss-Prot")) {

								//uniProtReference = value.replace("UniProtKB/Swiss-Prot:", "");
								ncbiData.addUniprotStatus(primary_accession,true);
								accessionNumbers.remove(primary_accession);
							}

							if(features.get("db_xref").startsWith("UniProtKB/TrEMBL")) {

								//uniProtReference = value.replace("UniProtKB/TrEMBL:", "");
								ncbiData.addUniprotStatus(primary_accession,false);
								accessionNumbers.remove(primary_accession);
							}	
						}
					}
				}
			}
		}

		if(uniprotStatus && accessionNumbers.size()>0 && !cancel.get()) {

			Map<String, List<UniProtEntry>> uniProtEntries = UniProtAPI.getUniprotEntriesFromRefSeq(accessionNumbers, cancel,0);
			
			for(String accessionsNumber:uniProtEntries.keySet()) {
				
				if(!cancel.get()) {
					
					for(UniProtEntry uniProtEntry : uniProtEntries.get(accessionsNumber)) {

						String primary_accession = accessionsNumber;
						
						if(!ncbiData.getUniprotStatus().containsKey(primary_accession) && ncbiData.getUniprotStatus().get(primary_accession)) {

							try {

								ncbiData.addUniprotStatus(primary_accession, UniProtAPI.isStarred(uniProtEntry));
							} 
							catch (Exception e) {
								logger.error(e.getMessage());
								e.printStackTrace();
							}
						} 
					}
				}
			}
		}
		return ncbiData;
	}

	/**
	 * @param result
	 * @param errorCount
	 * @return
	 * @throws Exception 
	 */
	public GBSet getEntries(List<String> result, int errorCount) throws Exception{

		try {

			String query = result.toString().replaceAll("\\[", "").replaceAll("\\]", "");
			GBSet gbSet = this.entrezService.eFetch(NcbiDatabases.protein, query, "xml");
			return gbSet;
		}
		catch (Exception e)  {

			logger.debug("getEntries error for: {}.", result);

			if(errorCount<20) {

				MySleep.myWait(3000);
				errorCount+=1;
				return this.getEntries(result, errorCount);
			}
			else {

				logger.trace("Stack trace {}",e);
				throw new Exception();
			}
		}
	}
}
