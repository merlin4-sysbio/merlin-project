package pt.uminho.ceb.biosystems.merlin.utilities.blast.ebi_blastparser;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EbiBlastParser {
	
	private EBIApplicationResult ebi;
	private boolean reprocessQuery;
	private boolean similarityFound;
	private TSSSR results;

	final static Logger logger = LoggerFactory.getLogger(EbiBlastParser.class);
	
	public EbiBlastParser (String filePath) {
		this(new File(filePath));
	}
	
	
	public EbiBlastParser (File xmlfile) {
		
		this.reprocessQuery = false;
		try {
			JAXBContext jc = JAXBContext.newInstance(EBIApplicationResult.class);

	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        EBIApplicationResult ebi = (EBIApplicationResult) unmarshaller.unmarshal(xmlfile);
	        
	        this.ebi = ebi;
	        
	        if(this.ebi.getSequenceSimilaritySearchResult().getHits().getTotal()>0)
	        	this.setSimilarityFound(true);
	        
	        buildResults();
	        
		}
		catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			this.setReprocessQuery(true);
			this.setSimilarityFound(false);

		}
		
	}
	
	private void setReprocessQuery(boolean reprocessQuery) {
		this.reprocessQuery = reprocessQuery;
	}


	public EbiBlastParser (InputStream inputStream) {
		
		this.reprocessQuery = false;
		try {
			
			JAXBContext jc = JAXBContext.newInstance(EBIApplicationResult.class);

	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        EBIApplicationResult ebi = (EBIApplicationResult) unmarshaller.unmarshal(inputStream);
	        
	        this.ebi = ebi;
	        
	        if(this.ebi.getSequenceSimilaritySearchResult().getHits().getTotal()>0)
	        	this.setSimilarityFound(true);
	        
	        buildResults();
		}
		catch(Exception ex) {
			
			ex.printStackTrace();
			logger.error(ex.getMessage());
			this.setReprocessQuery(true);
			this.setSimilarityFound(false);
			
		}
		
	}
	
	public void buildResults() {
        
        this.results = ebi.getSequenceSimilaritySearchResult();
	}
	
	/**
	 * @return
	 */
	public TSSSR getResults(){
		
		return this.results;
	}

	//Header
	public List<String> getHeaderProgram() {
		List<String> returnList = new ArrayList<String>();
		returnList.add(this.ebi.header.program.name);
		returnList.add(this.ebi.header.program.version);
		returnList.add(this.ebi.header.program.citation);
		
		return returnList;
	}
	
	public String getHeaderCommandLine() {
		return this.ebi.header.commandLine.command;
	}
	
	public List<String> getHeaderTimeInfo() {
		List<String> returnList = new ArrayList<String>();
		returnList.add(this.ebi.header.timeInfo.start.toString());
		returnList.add(this.ebi.header.timeInfo.end.toString());
		returnList.add(this.ebi.header.timeInfo.search.toString());
		
		return returnList;
	}

	//Parameters
	/**
	 * @return
	 * Retorna um objeto TParameters com todos os elementos preenchidos
	 */
	public TParameters getHeaderCompleteParameters() {

		return this.ebi.getHeader().getParameters();
		
	}
	
	public String getHeaderParametersAlignments() {
		return ebi.header.parameters.alignments.toString();
	}
	
	public String getHeaderParametersExpectationUpper() {
		return ebi.header.parameters.expectationUpper.toString();
	}
	
	public String getHeaderParametersFilter() {
		return ebi.header.parameters.filter.toString();
	}
	
	public String getHeaderParametersGapExtension() {
		return ebi.header.parameters.gapExtension.toString();
	}
	
	public String getHeaderParametersGapOpen() {
		return ebi.header.parameters.gapOpen.toString();
	}
	
	public String getHeaderParametersMatrix() {
		return ebi.header.parameters.matrix.toString();
	}
	
	/**
	 * @return
	 * mapa com key igual name da sequence e o value é uma lista com os outros atributos da sequence
	 */
	public HashMap<String,List<String>> getHeaderParametersSequences(){
		HashMap<String,List<String>> mapFinal = new HashMap<String,List<String>>();
		
		List<TSequence> listSeq = this.ebi.header.parameters.sequences.sequence;
		List<String> values = new ArrayList<String>();
		
		for (TSequence tSeq : listSeq) {
			values.add(Integer.toString(tSeq.length));
			values.add(tSeq.type);
			mapFinal.put(tSeq.name, values);
		}
		
		return mapFinal;
	}
	
	
	/**
	 * @return
	 */
	public List<String> getDatabases(){
		Set<String> dbNames = getHeaderParametersDatabases().keySet();
		List<String> databases = new ArrayList<String>(dbNames);
		
		return databases;
	}
	
	public String getOutputDB() {
		
		return getDatabases().get(0);
	}
	
	/**
	 * @return
	 * mapa com key igual name da database e o value é uma lista com os outros atributos da database
	 */
	public HashMap<String,List<String>> getHeaderParametersDatabases(){
		HashMap<String,List<String>> mapFinal = new HashMap<String,List<String>>();
		
		List<TDatabase> listDb = this.ebi.header.parameters.databases.database;
		List<String> values = new ArrayList<String>();
		
		for (TDatabase tDb : listDb) {
			values.add(tDb.created);
			values.add(tDb.type);
			mapFinal.put(tDb.name, values);
		}
		
		return mapFinal;
	}
	
	
	/**
	 * Blast sequence query ID
	 * 
	 * @return
	 */
	public String getQueryID() {
		
		return getHeaderCompleteParameters().getSequences().getSequence().get(0).name;
	}
	
	
	/**
	 * return blast program version
	 * 
	 * @return
	 */
	public String getVersion() {
		
		return getHeaderProgram().get(1);
	}
	
	
	/**
	 * @return
	 */
	public String getProgram() {
		
		return getHeaderProgram().get(0);
	}	
	
	//Sequence Result
	
	public List<THit> getHits() {
		return this.ebi.getSequenceSimilaritySearchResult().getHits().getHit();
	}

	
	public int getNumberOfHits() {
		return this.ebi.getSequenceSimilaritySearchResult().getHits().getTotal();
	}

	/**
	 * @return
	 * retorna um dicionario onde a key é o id de cada hit, e o value é uma lista com os atributos do hit
	 */
	public HashMap<String,List<String>> getHitsAttributes(){
		HashMap<String,List<String>> mapFinal = new HashMap<String,List<String>>();
		List<THit> listHits = new ArrayList<THit>();
		listHits= this.ebi.sequenceSimilaritySearchResult.hits.hit;
		List<String> listAtt = new ArrayList<String>();
		for (THit tHit : listHits) {
			listAtt.add(tHit.database);
			listAtt.add(tHit.ac);
			listAtt.add(Long.toString(tHit.length));
			listAtt.add(tHit.description);
			
			mapFinal.put(tHit.id, listAtt);
		}
		
		return mapFinal;
	}
	
	
	/**
	 * @return 
	 * retorna um dicionario onde a key é o id de cada hit, e o value é um objeto TAlignment com todos os elementos preenchidos
	 */
	public HashMap<String,List<TAlignment>> getHitsAlignments(){
		HashMap<String,List<TAlignment>> mapFinal = new HashMap<String,List<TAlignment>>();
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			mapFinal.put(tHit.id, listAlign);
		}
		
		return mapFinal;
	}
	
	public HashMap<String,HashMap<Integer,Integer>> getHitsAlignmentScore(){
		HashMap<String,HashMap<Integer,Integer>> finalMap = new HashMap<String,HashMap<Integer,Integer>>();
		HashMap<Integer,Integer> alignments = new HashMap<Integer,Integer>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			for (TAlignment tAlignment : listAlign) {
				alignments.put(tAlignment.number, tAlignment.score);
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	public HashMap<String,HashMap<Integer,Float>> getHitsAlignmentBits(){
		HashMap<String,HashMap<Integer,Float>> finalMap = new HashMap<String,HashMap<Integer,Float>>();
		HashMap<Integer,Float> alignments = new HashMap<Integer,Float>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			for (TAlignment tAlignment : listAlign) {
				alignments.put(tAlignment.number, tAlignment.bits);
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	public HashMap<String,HashMap<Integer,Double>> getHitsAlignmentExpectation(){
		HashMap<String,HashMap<Integer,Double>> finalMap = new HashMap<String,HashMap<Integer,Double>>();
		HashMap<Integer,Double> alignments = new HashMap<Integer,Double>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			for (TAlignment tAlignment : listAlign) {
				alignments.put(tAlignment.number, tAlignment.getExpectation());
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	public HashMap<String,HashMap<Integer,Float>> getHitsAlignmentIdentity(){
		HashMap<String,HashMap<Integer,Float>> finalMap = new HashMap<String,HashMap<Integer,Float>>();
		HashMap<Integer,Float> alignments = new HashMap<Integer,Float>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.getAlignments().getAlignment();
			for (TAlignment tAlignment : listAlign) {
				alignments.put(tAlignment.number, tAlignment.identity);
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	public HashMap<String,HashMap<Integer,Float>> getHitsAlignmentPositives(){
		HashMap<String,HashMap<Integer,Float>> finalMap = new HashMap<String,HashMap<Integer,Float>>();
		HashMap<Integer,Float> alignments = new HashMap<Integer,Float>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			for (TAlignment tAlignment : listAlign) {
				alignments.put(tAlignment.number, tAlignment.positives);
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	public HashMap<String,HashMap<Integer,Float>> getHitsAlignmentGaps(){
		HashMap<String,HashMap<Integer,Float>> finalMap = new HashMap<String,HashMap<Integer,Float>>();
		HashMap<Integer,Float> alignments = new HashMap<Integer,Float>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			for (TAlignment tAlignment : listAlign) {
				alignments.put(tAlignment.number, tAlignment.gaps);
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	public HashMap<String,HashMap<Integer,String>> getHitsAlignmentStrand(){
		HashMap<String,HashMap<Integer,String>> finalMap = new HashMap<String,HashMap<Integer,String>>();
		HashMap<Integer,String> alignments = new HashMap<Integer,String>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			for (TAlignment tAlignment : listAlign) {
				alignments.put(tAlignment.number, tAlignment.strand);
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	public HashMap<String,HashMap<Integer,String>> getHitsAlignmentPattern(){
		HashMap<String,HashMap<Integer,String>> finalMap = new HashMap<String,HashMap<Integer,String>>();
		HashMap<Integer,String> alignments = new HashMap<Integer,String>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			for (TAlignment tAlignment : listAlign) {
				alignments.put(tAlignment.number, tAlignment.pattern);
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	/**
	 * @return
	 * retorna um hashmap onde a key é o id do hit e o value é um hashmap com key igual ao number do alignment e value é uma 
	 * lista com os atributos e valor do queryseq
	 */
	public HashMap<String,HashMap<Integer,List<String>>> getHitsAlignmentCompleteQuerySeq(){
		HashMap<String,HashMap<Integer,List<String>>> finalMap = new HashMap<String,HashMap<Integer,List<String>>>();
		HashMap<Integer,List<String>> alignments = new HashMap<Integer,List<String>>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			for (TAlignment tAlignment : listAlign) {
				List<String> querySeq = new ArrayList<String>();
				querySeq.add(Integer.toString(tAlignment.querySeq.start));
				querySeq.add(Integer.toString(tAlignment.querySeq.end));
				querySeq.add(tAlignment.querySeq.value);
				alignments.put(tAlignment.number, querySeq);
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	public HashMap<String,HashMap<Integer,List<String>>> getHitsAlignmentCompleteMatchSeq(){
		HashMap<String,HashMap<Integer,List<String>>> finalMap = new HashMap<String,HashMap<Integer,List<String>>>();
		HashMap<Integer,List<String>> alignments = new HashMap<Integer,List<String>>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			for (TAlignment tAlignment : listAlign) {
				List<String> matchSeq = new ArrayList<String>();
				matchSeq.add(Integer.toString(tAlignment.matchSeq.start));
				matchSeq.add(Integer.toString(tAlignment.matchSeq.end));
				matchSeq.add(tAlignment.matchSeq.value);
				alignments.put(tAlignment.number, matchSeq);
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	public HashMap<String,HashMap<Integer,String>> getHitsAlignmentQuerySeq(){
		HashMap<String,HashMap<Integer,String>> finalMap = new HashMap<String,HashMap<Integer,String>>();
		HashMap<Integer,String> alignments = new HashMap<Integer,String>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			for (TAlignment tAlignment : listAlign) {
				alignments.put(tAlignment.number, tAlignment.querySeq.value);
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	public HashMap<String,HashMap<Integer,String>> getHitsAlignmentMatchSeq(){
		HashMap<String,HashMap<Integer,String>> finalMap = new HashMap<String,HashMap<Integer,String>>();
		HashMap<Integer,String> alignments = new HashMap<Integer,String>();
		
		List<THit> listHits = this.ebi.sequenceSimilaritySearchResult.hits.hit;
		for (THit tHit : listHits) {
			List<TAlignment> listAlign = tHit.alignments.alignment;
			for (TAlignment tAlignment : listAlign) {
				alignments.put(tAlignment.number, tAlignment.matchSeq.value);
			}
			
			
			finalMap.put(tHit.id, alignments);
		}
		
		return finalMap;
	}
	
	
 	/**
 	 * @return
 	 */
 	public boolean isReprocessQuery() {
		return this.reprocessQuery;
	}
	
	
	/**
	 * @param similarityFound
	 */
	private void setSimilarityFound(boolean similarityFound) {
				
		this.similarityFound = similarityFound;
	}
	
 	
	/**
	 * @return
	 */
	public boolean isSimilarityFound() {

		return this.similarityFound;
	}
}
