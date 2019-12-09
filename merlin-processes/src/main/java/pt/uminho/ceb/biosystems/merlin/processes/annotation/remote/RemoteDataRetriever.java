
package pt.uminho.ceb.biosystems.merlin.processes.annotation.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.biojava.bio.search.SeqSimilaritySearchHit;
import org.biojava.bio.search.SeqSimilaritySearchResult;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.datatypes.EntryData;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot.UniProtAPI;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.NcbiAPI;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologuesData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.BlastSource;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HmmerRemoteDatabasesEnum;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologySearchServer;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.blast.ReadBlasttoList;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.hmmer.ReadHmmertoList;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * This class retrieves homology data for the results list provided by the Sequence 
 * Similarity Search Results from the Entrez Protein Database
 * 
 * @author oDias
 */
public class RemoteDataRetriever {

	/**
	 * homology data variables  
	 */
	private String organismID, blastSetupID; 
	private AtomicBoolean cancel;
	private boolean dataRetrieved;
	private int numberOfHits;
	private AnnotationEnzymesHomologuesData homologuesData;
	private long taxonomyID;

	/**
	 * @param query
	 * @param program
	 * @param cancel
	 * @param uniprotStatus
	 * @param homologySearchServer
	 * @param taxonomyID
	 * @throws Exception
	 */
	public RemoteDataRetriever(String query, String program, AtomicBoolean cancel, boolean uniprotStatus, HomologySearchServer homologySearchServer, long taxonomyID) throws Exception {

		this.homologuesData = new AnnotationEnzymesHomologuesData();

		this.setTaxonomyID(taxonomyID);
		this.setCancel(cancel);
		this.homologuesData.setQuery(query);
		this.homologuesData.setBlastProgram(program);
		if(homologySearchServer.equals(HomologySearchServer.EBI))
			this.homologuesData.setBlastProgram("ebi-blastp");
		this.homologuesData.setNoSimilarity(true);	

		try {

			String locusTag=null;

			this.processQueryInformation(query);

			if(locusTag==null && this.homologuesData.getUniprotLocusTag()!=null)				
				locusTag = this.homologuesData.getUniprotLocusTag();

			if(locusTag==null)
				locusTag=query;

			this.homologuesData.setLocusTag(locusTag);
		}
		catch(Exception e) {

			e.printStackTrace();
		}
	}


	/**
	 * @param blastList
	 * @param project
	 * @param organismTaxa
	 * @param taxonomyMap
	 * @param uniprotStar
	 * @param cancel
	 * @param blastServer 
	 * @param hitListSize 
	 * @param uniprotStatus 
	 * @param taxonomyID 
	 * @throws Exception
	 */
	public RemoteDataRetriever(ReadBlasttoList blastList, String[] organismTaxa, ConcurrentHashMap<String, String[]> taxonomyMap, 
			ConcurrentHashMap<String, Boolean> uniprotStar, AtomicBoolean cancel, HomologySearchServer homologySearchServer, int hitListSize, boolean uniprotStatus, long taxonomyID
			) throws Exception {

		this.homologuesData = new AnnotationEnzymesHomologuesData();

		this.setTaxonomyID(taxonomyID);
		this.setNumberOfHits(hitListSize);
		this.homologuesData.setNoSimilarity(false);	
		this.setCancel(cancel);

		this.homologuesData.setTaxonomyMap(taxonomyMap);
		this.homologuesData.setUniprotStatus(uniprotStar);
		this.homologuesData.setOrganismTaxa(organismTaxa);
		this.homologuesData.setSequenceCode(blastList.getQuery());
		this.homologuesData.setQuery(blastList.getQuery());

		this.processQueryInformation(blastList.getQuery());

		List<Pair<String, String>> resultsList = this.initialiseClass(blastList.getQuery(),blastList.getDatabaseId(), 
				blastList.getVersion(), blastList.getProgram(), 
				this.parseResults(blastList.getResults(), homologySearchServer), homologySearchServer);

		if(homologySearchServer.equals(HomologySearchServer.EBI))
			this.homologuesData = UniProtAPI.getUniprotData(homologuesData, resultsList, cancel, uniprotStatus, this.taxonomyID);
	}

	/**
	 * @param hmmerList
	 * @param organismTaxa
	 * @param taxonomyMap
	 * @param uniprotStar
	 * @param cancel
	 * @param uniprotStatus
	 * @param homologySearchServer
	 * @param taxonomyID 
	 * @throws Exception
	 */
	public RemoteDataRetriever(ReadHmmertoList hmmerList, String[] organismTaxa, ConcurrentHashMap<String, String[]> taxonomyMap,
			ConcurrentHashMap<String, Boolean> uniprotStar, AtomicBoolean cancel, boolean uniprotStatus
			, HomologySearchServer homologySearchServer, long taxonomyID) throws Exception {

		this.homologuesData = new AnnotationEnzymesHomologuesData();

		this.setTaxonomyID(taxonomyID);
		this.setCancel(cancel);
		this.homologuesData.setNoSimilarity(false);	
		this.homologuesData.setTaxonomyMap(taxonomyMap);
		this.homologuesData.setUniprotStatus(uniprotStar);
		this.homologuesData.setBits(hmmerList.getScores());
		this.homologuesData.setEValue(hmmerList.getEValues());
		this.homologuesData.setOrganismTaxa(organismTaxa);
		this.homologuesData.setSequenceCode(hmmerList.getQuery());
		this.homologuesData.setQuery(hmmerList.getQuery());

		this.processQueryInformation(hmmerList.getQuery());

		List<Pair<String, String>> resultsList = this.initialiseClass(hmmerList.getQuery(), hmmerList.getDatabaseId().toString(), hmmerList.getVersion(), hmmerList.getProgram(), hmmerList.getResults(), homologySearchServer);		

		if(hmmerList.getDatabase().equals(HmmerRemoteDatabasesEnum.pdb) )
			this.homologuesData = NcbiAPI.getNcbiData(homologuesData, resultsList, 1, this.cancel, uniprotStatus, this.taxonomyID);
		else
			this.homologuesData = UniProtAPI.getUniprotData(this.homologuesData, resultsList, cancel, true, this.taxonomyID);
	}

	/**
	 * 
	 * @param blastList
	 * @param organismTaxa
	 * @param taxonomyMap
	 * @param uniprotStar
	 * @param cancel
	 * @param uniprotStatus
	 * @param homologySearchServer
	 * @param blastSource
	 * @param taxonomyID
	 * @throws Exception
	 */
	public RemoteDataRetriever(ReadBlasttoList blastList, String[] organismTaxa, ConcurrentHashMap<String, String[]> taxonomyMap,
			ConcurrentHashMap<String, Boolean> uniprotStar, AtomicBoolean cancel, boolean uniprotStatus
			, HomologySearchServer homologySearchServer, BlastSource blastSource, long taxonomyID) throws Exception {

		this.homologuesData = new AnnotationEnzymesHomologuesData();

		this.setTaxonomyID(taxonomyID);
		
		this.setCancel(cancel);
		this.homologuesData.setNoSimilarity(false);	
		this.homologuesData.setTaxonomyMap(taxonomyMap);
		this.homologuesData.setUniprotStatus(uniprotStar);
		this.homologuesData.setOrganismTaxa(organismTaxa);
		this.homologuesData.setSequenceCode(blastList.getQuery());
		this.homologuesData.setQuery(blastList.getQuery());

		this.processQueryInformation(blastList.getQuery());

		List<Pair<String, String>> resultsList = this.initialiseClass(blastList.getQuery(),blastList.getDatabaseId(), blastList.getVersion(), blastList.getProgram(), 
				this.parseResults(blastList.getResults(), homologySearchServer), homologySearchServer);

		if(blastSource.equals(BlastSource.NCBI))
			this.homologuesData = NcbiAPI.getNcbiData(this.homologuesData, resultsList, 1, this.cancel, uniprotStatus, this.taxonomyID);
		else
			this.homologuesData = UniProtAPI.getUniprotData(this.homologuesData, resultsList, cancel, true, this.taxonomyID);
	}

	/**
	 * @param query
	 * @throws Exception
	 */
	private void processQueryInformation(String query) throws Exception {

		EntryData entryData = UniProtAPI.getEntryData(query, this.taxonomyID);

		String ecNumbers = null;
		Set<String> ecnumbers = entryData.getEcNumbers();
		if(ecnumbers!= null) {

			ecNumbers = "";

			for(String ecnumber : ecnumbers)
				ecNumbers += ecnumber+", ";

			if(ecNumbers.contains(", "))
				ecNumbers = ecNumbers.substring(0, ecNumbers.lastIndexOf(", "));
		}
		this.homologuesData.setEntryUniprotECnumbers(ecNumbers);
		this.homologuesData.setEntryUniProtStarred(entryData.getUniprotReviewStatus());
		this.homologuesData.setUniProtEntryID(entryData.getEntryID());
		this.homologuesData.setUniprotLocusTag(entryData.getLocusTag());
		this.homologuesData.setRefSeqGI(NcbiAPI.getNcbiGI(query)); 
	}


	/**
	 * @param query
	 * @param databaseID
	 * @param version
	 * @param program
	 * @param rawResultsList
	 * @param homologySearchServer
	 * @return
	 * @throws Exception
	 */
	private List<Pair<String, String>> initialiseClass(String query, String databaseID, String version, String program, List<String> rawResultsList, HomologySearchServer homologySearchServer) throws Exception {

		this.homologuesData.setQuery(query);
		this.setDataRetrieved(true);
		this.homologuesData.setBlastDatabaseIdentifier(databaseID);
		this.homologuesData.setBlastVersion(version);
		this.homologuesData.setBlastProgram(program);
		if(homologySearchServer.equals(HomologySearchServer.EBI))
			this.homologuesData.setBlastProgram("ebi-blastp");

		List<Pair<String, String>>  resultsList = new ArrayList<>();
		for(int i=0;i<rawResultsList.size();i++)
			resultsList.add(i, new Pair<String, String> (rawResultsList.get(i), rawResultsList.get(i)));

		return resultsList;
	}

	/**
	 * @param list
	 * @return 
	 */
	private List<String> parseResults(List<SeqSimilaritySearchResult> list, HomologySearchServer homologySearchServer) {

		this.homologuesData.setBits(new HashMap<String, Double>());
		this.homologuesData.setEValue(new HashMap<String, Double>());
		List<String> resultsList = new ArrayList<String>();

		for (SeqSimilaritySearchResult result : list) {
			
			@SuppressWarnings("unchecked")
			List<SeqSimilaritySearchHit> hits = (List<SeqSimilaritySearchHit>) result.getHits();

			for (int i = 0; i<hits.size();i++ ){

				SeqSimilaritySearchHit hit = hits.get(i);
				String id = hit.getSubjectID();

				if(homologySearchServer.equals(HomologySearchServer.EBI))
					id = this.parseUniProtIds(hit);

				if(id!=null) {

					resultsList.add(i, id);
					this.homologuesData.addBits(id, hit.getScore());
					this.homologuesData.addEValue(id, hit.getEValue());
				}
			}
		}
		return resultsList;
	}

	/**
	 * @param id
	 * @return
	 */
	private String parseUniProtIds(SeqSimilaritySearchHit hit) {

//		String id = hit.getAnnotation().getProperty("subjectDescription").toString();
//		String[] xrefs = id.split("\\s");
//		String uni = xrefs[0];
		
		String id = hit.getAnnotation().getProperty("subjectId").toString();
		String[] xrefs = id.split(":");
		String uni = xrefs[1];


		return uni;
	}


	/**
	 * @return the blast'ed organism name
	 */
	public String getOrganismID(){
		return this.organismID;		
	}

	/**
	 * @return the locus_tag
	 */
	public String getLocusTag() {

		if(homologuesData.getLocusTag()==null)
			return this.homologuesData.getQuery();

		return homologuesData.getLocusTag();
	}

	/**
	 * @return the readBlast instance
	 */
	public String getFastaSequence() {
		return homologuesData.getFastaSequence();
	}

	/**
	 * @param fastaSequence the fastaSequence to set
	 */
	public void setFastaSequence(String fastaSequence) {
		homologuesData.setFastaSequence(fastaSequence);
	}

	/**
	 * @param cancel
	 */
	public void setCancel(AtomicBoolean cancel) {

		this.cancel = cancel;
	}

	/**
	 * @param dataRetrieved the dataRetrieved to set
	 */
	public void setDataRetrieved(boolean dataRetrieved) {
		this.dataRetrieved = dataRetrieved;
	}

	/**
	 * @return the dataRetrieved
	 */
	public boolean isDataRetrieved() {
		return dataRetrieved;
	}

	/**
	 * @param databaseID the databaseID to set
	 */
	public void setDatabaseIdentifier(String databaseID) {
		this.homologuesData.setBlastDatabaseIdentifier(databaseID);
	}

	/**
	 * @return the blastSetupID
	 */
	public String getBlastSetupID() {
		return blastSetupID;
	}

	/**
	 * @param blastSetupID the blastSetupID to set
	 */
	public void setBlastSetupID(String blastSetupID) {
		this.blastSetupID = blastSetupID;
	}

	/**
	 * @param version the version to set
	 */
	public void setBlastVersion(String version) {
		this.homologuesData.setBlastVersion(version);
	}

		/**
		 * @param program the program to set
		 */
		public void setBlastProgram(String program) {
			this.homologuesData.setBlastProgram(program);
		}

	/**
	 * @return the numberOfHits
	 */
	public int getNumberOfHits() {
		return numberOfHits;
	}

	/**
	 * @param numberOfHits the numberOfHits to set
	 */
	public void setNumberOfHits(int numberOfHits) {
		this.numberOfHits = numberOfHits;
	}

	/**
	 * @return the taxonomyID
	 */
	public long getTaxonomyID() {
		return taxonomyID;
	}

	/**
	 * @param taxonomyID the taxonomyID to set
	 */
	public void setTaxonomyID(long taxonomyID) {
		this.taxonomyID = taxonomyID;
	}


	public void setNoSimilarity(boolean isNoSimilarity) {

		this.homologuesData.setNoSimilarity(isNoSimilarity);
	}


	/**
	 * @return the homologuesData
	 */
	public AnnotationEnzymesHomologuesData getHomologuesData() {
		return homologuesData;
	}


	/**
	 * @param homologuesData the homologuesData to set
	 */
	public void setHomologuesData(AnnotationEnzymesHomologuesData homologuesData) {
		this.homologuesData = homologuesData;
	}

}

