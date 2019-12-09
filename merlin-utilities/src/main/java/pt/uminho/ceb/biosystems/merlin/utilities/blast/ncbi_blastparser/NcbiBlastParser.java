package pt.uminho.ceb.biosystems.merlin.utilities.blast.ncbi_blastparser;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author claudia sampaio
 *
 */
public class NcbiBlastParser {
	
	private BlastOutput blout;
	private List<BlastIterationData> results;
	private boolean reprocessQuery;
	private boolean similarityFound;
	
	final static Logger logger = LoggerFactory.getLogger(NcbiBlastParser.class);

	
	public NcbiBlastParser (String filePath, JAXBContext jc) {
		this(new File(filePath),jc);
	}

	public NcbiBlastParser (File xmlfile, JAXBContext jc) {
		
		this.reprocessQuery = false;
		this.results = new ArrayList<>();
		try {
			
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
//	        unmarshaller.setProperty(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, Boolean.TRUE);
	        
	        
	        
	        BlastOutput blout = (BlastOutput) unmarshaller.unmarshal(xmlfile);
	        
	        this.blout = blout;
	        
	        if(this.blout.getBlastOutputIterations().getIteration().get(0).getIterationHits().getHit().size()>0)
	        	this.setSimilarityFound(true);
	        
			buildResults();

		}
		catch(Exception ex) {
			ex.printStackTrace();
			logger.error("An error occurred while reading the file: "+ ex.getMessage());
			this.setReprocessQuery(true);
			this.setSimilarityFound(false);

		}
		
	}
	
	public NcbiBlastParser (InputStream inputStream) {
		
		this.reprocessQuery = false;
		
		this.results = new ArrayList<>();
		try {
			
			JAXBContext jc = JAXBContext.newInstance(BlastOutput.class);

	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        BlastOutput blout = (BlastOutput) unmarshaller.unmarshal(inputStream);
	        
	        this.blout = blout;
	        
			buildResults();

		}
		catch(Exception ex) {
			ex.printStackTrace();
			logger.error("An error occurred while reading the file: "+ ex.getMessage());
			this.setReprocessQuery(true);
		}
	}
	
	
	public void buildResults() {
		
		List<BlastIterationData> res = new ArrayList<>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			
//        	Integer iterNum = Integer.parseInt(iteration.iterationIterNum);
			
        	String seqDB = getBlastOutputDb();
        	String queryID = iteration.getIterationQueryID();
        	String queryDef = iteration.getIterationQueryDef();
        	String querylen = iteration.getIterationQueryLen();
			List<Hit> listHits = iteration.iterationHits.hit;
			
			BlastIterationData iterationData = new BlastIterationData(iteration, queryID, queryDef, querylen, listHits, seqDB);
			res.add(iterationData);
		}
        
        this.results = res;
	}
	
	/**
	 * @return
	 */
	public List<BlastIterationData> getResults(){
		
		return this.results;
	}
	
	//--------------------------------------------------------------------------
	//BlastOutput 
	public String getBlastOutputQueryID() {
		String queryId="";
		queryId= this.blout.blastOutputQueryID;
		return queryId;
	}
	public String getBlastOutputQueryDef() {
		String queryDef="";
		queryDef= this.blout.blastOutputQueryDef;
		return queryDef;
	}	
	public String getBlastOutputQueryLen() {
		String queryLen="";
		queryLen= this.blout.blastOutputQueryLen;
		return queryLen;
	}	
	public  String getBlastOutputReference() {
		String ref="";
		ref= blout.blastOutputReference;
		return ref;
	}	
	public  String getBlastOutputDb() {
		String db="";
		
		db= blout.blastOutputDb;
		return db;
	}	
	public  String getBlastOutputProgram() {
		String program="";
		
		program= blout.blastOutputProgram;
		return program;
	}
	public  String getBlastOutputVersion() {
		String version="";
		
		version= blout.blastOutputVersion;
		return version;
	}
	//--------------------------------------------------------------------------
	//Parameters
	public  String getParametersExpect() {
		String expect="";
		
		expect = blout.blastOutputParam.parameters.parametersExpect;
		return expect;
	}
	public  String getParametersSCMatch() {
		String scmatch="";
		
		scmatch = blout.blastOutputParam.parameters.parametersScMatch;
		return scmatch;
	}
	public  String getParametersSCMisMatch() {
		String scmismatch="";
		
		scmismatch = blout.blastOutputParam.parameters.parametersScMismatch;
		return scmismatch;
	}
	public  String getParametersGapOpen() {
		String gapopen="";
		
		gapopen = blout.blastOutputParam.parameters.parametersGapOpen;
		return gapopen;
	}
	public  String getParametersGapExtend() {
		String gapextend="";
		
		gapextend = blout.blastOutputParam.parameters.parametersGapExtend;
		return gapextend;
	}
	public  String getParametersFilter() {
		String filter="";
		
		filter = blout.blastOutputParam.parameters.parametersFilter;
		return filter;
	}
	
	/**
	 * @return
	 */
	public String getMatrix(){
		return blout.getBlastOutputParam().getParameters().getParametersMatrix();
	}
	
	//--------------------------------------------------------------------------
	//Iteration
	public  HashMap<String, String> getQueryId(){
		
		
		//Initialize variables
		String iterNum ="";
		String iterQueryId="";
		HashMap<String,String> mapFinal = new HashMap<String,String>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
			iterQueryId=iteration.iterationQueryID;
			
			mapFinal.put(iterNum, iterQueryId);
        }
        
        return mapFinal;
	}
	
	
	/**
	 * @return
	 */
	public  HashMap<String, String> getQueryDef(){
		
		//Initialize variables
		String iterNum ="";
		String iterQueryDef="";
		HashMap<String,String> mapFinal = new HashMap<String,String>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
			iterQueryDef=iteration.iterationQueryDef;
			
			mapFinal.put(iterNum, iterQueryDef);
        }
        
        return mapFinal;
	}
	public  HashMap<String, String> getQueryLen(){
		
		
		//Initialize variables
		String iterNum ="";
		String iterQueryLen="";
		HashMap<String,String> mapFinal = new HashMap<String,String>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
			iterQueryLen=iteration.iterationQueryLen;
			
			mapFinal.put(iterNum, iterQueryLen);
        }
        
        return mapFinal;
	}
	//--------------------------------------------------------------------------
	//Hits
	
	/**
	 * Retorna um dic - Key: Id da iteraçao; Value: Array com os Ids dos HIts
	 * 
	 * @return
	 */
	public  HashMap<String,String[]> getAllHitsIdByIteration(){
		
		
		//Initialize variables
		ArrayList<String> hits = new ArrayList<String>();
		String iterNum ="";
		HashMap<String,String[]> mapFinalGetAllHits = new HashMap<String,String[]>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				hits.add(hit.hitId);
			}	
				
		    //Add new item to the map
			String[] hitsArray = new String[hits.size()];
			hitsArray = hits.toArray(hitsArray);
			
			mapFinalGetAllHits.put(iterNum, hitsArray);
				
			//Clear variables
			hits.clear();
			iterNum="";
        
		}
        return mapFinalGetAllHits;
	}
	
	//Retorna um dic - Key: Id da iteracao, Value: Dic com key:id hit e value: Len
	public  HashMap<String,HashMap<String,String>> getHitLenByHitId(){
		
		
		HashMap<String, String> mapLen = new HashMap<String, String>();
		HashMap<String,HashMap<String,String>> mapFinalLen = new HashMap<String,HashMap<String,String>>();
		String iterNum=" ";
		
		List<Iteration> listIterations = blout.blastOutputIterations.iteration;
		
		for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
			
			List<Hit> listHits = iteration.iterationHits.hit;
			
			for (Hit hit : listHits) {
				mapLen.put(hit.hitId, hit.hitLen);
			}
			
			mapFinalLen.put(iterNum, mapLen);
			
//			mapLen.clear();
		}
		
		return mapFinalLen;
	}
	
	//Retorna um dic - Key: Id da iteracao, Value: Dic com key:id hit e value: accession
	public  HashMap<String,HashMap<String,String>> getHitAccessionByHitId(){
			
			
			HashMap<String, String> mapAccession = new HashMap<String, String>();
			HashMap<String,HashMap<String,String>> mapFinalAccession = new HashMap<String,HashMap<String,String>>();
			String iterNum=" ";
			
			List<Iteration> listIterations = blout.blastOutputIterations.iteration;
			
			for (Iteration iteration : listIterations) {
				iterNum = iteration.iterationIterNum;
				
				List<Hit> listHits = iteration.iterationHits.hit;
				
				for (Hit hit : listHits) {
					mapAccession.put(hit.hitId, hit.hitAccession);
				}
				
				mapFinalAccession.put(iterNum, mapAccession);
				
//				mapAccession.clear();
			}
			
			return mapFinalAccession;
		}
	
	//Retorna um dic - Key: Id da iteracao, Value: Dic com key:id hit e value: def
	public  HashMap<String,HashMap<String,String>> getHitDefByHitId(){
				
				
				HashMap<String, String> mapDef = new HashMap<String, String>();
				HashMap<String,HashMap<String,String>> mapFinalDef = new HashMap<String,HashMap<String,String>>();
				String iterNum=" ";
				
				List<Iteration> listIterations = blout.blastOutputIterations.iteration;
				
				for (Iteration iteration : listIterations) {
					iterNum = iteration.iterationIterNum;
					
					List<Hit> listHits = iteration.iterationHits.hit;
					
					for (Hit hit : listHits) {
						mapDef.put(hit.hitId, hit.hitDef);
					}
					
					mapFinalDef.put(iterNum, mapDef);
					
//					mapLen.clear();
				}
				
				return mapFinalDef;
			}
	//--------------------------------------------------------------------------
	//Hsps
	
	//
	
	/**
	 * Retorna um mapa, em que a Key é o ID da Iteraçao, 
	 * 	e o Value é um mapa que tem como Key o HitId e o Value o respetivo valor do Hsp.
	 * 
	 * @return
	 */
	public  HashMap<String,HashMap<String,String[] >> getAlignLenByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapAlignLen = new HashMap<String, String[]>();
		ArrayList<String> alignLen = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalAlignLen = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					alignLen.add(hsp.hspAlignLen);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] alignLenArray = new String[alignLen.size()];
				alignLenArray = alignLen.toArray(alignLenArray);
				
				mapAlignLen.put(hitId, alignLenArray);
				
				//Limpar variaveis, para, da proxima vez que percorrer o for, a lista estar vazia,
				//para nao acrescentar aos valores anteriores, mas sim substitui-los
				alignLen.clear();
				hitId="";
			}
			mapFinalAlignLen.put(iterNum, mapAlignLen);
		}
        return mapFinalAlignLen;
	}
	
	
	/**
	 * @return
	 */
	public  HashMap<String,HashMap<String,String[]>> getBitScoreByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapBitScore = new HashMap<String, String[]>();
		ArrayList<String> bitScore = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalBitScore = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					bitScore.add(hsp.hspBitScore);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] bitScoreArray = new String[bitScore.size()];
				bitScoreArray = bitScore.toArray(bitScoreArray);
				
				mapBitScore.put(hitId, bitScoreArray);
				
				//Clear variables
				bitScore.clear();
				hitId="";
			}
			mapFinalBitScore.put(iterNum, mapBitScore);
		}
        return mapFinalBitScore;
	}
	
	
	/**
	 * @return
	 */
	public  HashMap<String,HashMap<String,String[]>> getEValueByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapEValue = new HashMap<String, String[]>();
		ArrayList<String> eValue = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalEValue = new HashMap<String,HashMap<String, String[]>>();
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	    String iterNum = iteration.iterationIterNum;
        		//Get all Hits
    			List<Hit> listHits = iteration.iterationHits.hit;
    			
    			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
    			for (Hit hit : listHits) {
    				
    				hitId=hit.hitId;
    				
    				//Get all Hsps of that Hit
    				List<Hsp> listHsps = hit.hitHsps.hsp;
    				//For each Hsp, add the AlignLen value to the list
    				for (Hsp hsp : listHsps) {
    					eValue.add(hsp.hspEvalue);
    				}
    				
    				//Add new item to the map
    				
    				//Convert the List to Array
    				String[] eValueArray = new String[eValue.size()];
    				eValueArray = eValue.toArray(eValueArray);
    				
    				mapEValue.put(hitId, eValueArray);
    				
    				//Clear variables
    				eValue.clear();
    				hitId="";
    			}
    			
    			mapFinalEValue.put(iterNum, mapEValue);
        	
        	
		}
        return mapFinalEValue;
	}
    public  HashMap<String,HashMap<String,String[]>> getScoreByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapScore = new HashMap<String, String[]>();
		ArrayList<String> score = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalScore = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					score.add(hsp.hspScore);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] scoreArray = new String[score.size()];
				scoreArray = score.toArray(scoreArray);
				
				mapScore.put(hitId, scoreArray);
				
				//Clear variables
				score.clear();
				hitId="";
			}
			mapFinalScore.put(iterNum, mapScore);
		}
        return mapFinalScore;
	}
    public  HashMap<String,HashMap<String,String[]>> getQueryFromByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapQueryFrom = new HashMap<String, String[]>();
		ArrayList<String> queryfrom = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalQueryFrom = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					queryfrom.add(hsp.hspQueryFrom);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] QueryFromArray = new String[queryfrom.size()];
				QueryFromArray = queryfrom.toArray(QueryFromArray);
				
				mapQueryFrom.put(hitId, QueryFromArray);
				
				//Clear variables
				queryfrom.clear();
				hitId="";
			}
			mapFinalQueryFrom.put(iterNum, mapQueryFrom);
		}
        return mapFinalQueryFrom;
	}
    public  HashMap<String,HashMap<String,String[]>> getQuerytoByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapQueryTo = new HashMap<String, String[]>();
		ArrayList<String> queryto = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalQueryTo = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					queryto.add(hsp.hspQueryTo);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] QuerytoArray = new String[queryto.size()];
				QuerytoArray = queryto.toArray(QuerytoArray);
				
				mapQueryTo.put(hitId, QuerytoArray);
				
				//Clear variables
				queryto.clear();
				hitId="";
			}
			mapFinalQueryTo.put(iterNum, mapQueryTo);
		}
        return mapFinalQueryTo;
	}    
    public  HashMap<String,HashMap<String,String[]>> getHitFromByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapHitFrom = new HashMap<String, String[]>();
		ArrayList<String> hitfrom = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalHitFrom = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					hitfrom.add(hsp.hspHitFrom);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] HitFromArray = new String[hitfrom.size()];
				HitFromArray = hitfrom.toArray(HitFromArray);
				
				mapHitFrom.put(hitId, HitFromArray);
				
				//Clear variables
				hitfrom.clear();
				hitId="";
			}
			mapFinalHitFrom.put(iterNum, mapHitFrom);
		}
        return mapFinalHitFrom;
	}    
    public  HashMap<String,HashMap<String,String[]>> getHitToByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapHitTo = new HashMap<String, String[]>();
		ArrayList<String> hitto = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalHitTo = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					hitto.add(hsp.hspHitTo);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] HitToArray = new String[hitto.size()];
				HitToArray = hitto.toArray(HitToArray);
				
				mapHitTo.put(hitId, HitToArray);
				
				//Clear variables
				hitto.clear();
				hitId="";
			}
			mapFinalHitTo.put(iterNum, mapHitTo);
		}
        return mapFinalHitTo;
	}    
    public  HashMap<String,HashMap<String,String[]>> getIdentityByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapIdentity = new HashMap<String, String[]>();
		ArrayList<String> identity = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalIdentity = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					identity.add(hsp.hspIdentity);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] idArray = new String[identity.size()];
				idArray = identity.toArray(idArray);
				
				mapIdentity.put(hitId, idArray);
				
				//Clear variables
				identity.clear();
				hitId="";
			}
			mapFinalIdentity.put(iterNum, mapIdentity);
		}
        return mapFinalIdentity;
	}    
    public  HashMap<String,HashMap<String,String[]>> getHitFrameByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapHitFrame = new HashMap<String, String[]>();
		ArrayList<String> hitframe = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalHitFrame = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					hitframe.add(hsp.hspHitFrame);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] HitFrameArray = new String[hitframe.size()];
				HitFrameArray = hitframe.toArray(HitFrameArray);
				
				mapHitFrame.put(hitId, HitFrameArray);
				
				//Clear variables
				hitframe.clear();
				hitId="";
			}
			mapFinalHitFrame.put(iterNum, mapHitFrame);
		}
        return mapFinalHitFrame;
	}    
    public  HashMap<String,HashMap<String,String[]>> getPositiveByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapPositive = new HashMap<String, String[]>();
		ArrayList<String> positive = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalPositive = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					positive.add(hsp.hspPositive);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] positiveArray = new String[positive.size()];
				positiveArray = positive.toArray(positiveArray);
				
				mapPositive.put(hitId, positiveArray);
				
				//Clear variables
				positive.clear();
				hitId="";
			}
			mapFinalPositive.put(iterNum, mapPositive);
		}
        return mapFinalPositive;
	}
    public  HashMap<String,HashMap<String,String[]>> getGapsByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapGaps = new HashMap<String, String[]>();
		ArrayList<String> gaps = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalGaps = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					gaps.add(hsp.hspGaps);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] gapsArray = new String[gaps.size()];
				gapsArray = gaps.toArray(gapsArray);
				
				mapGaps.put(hitId, gapsArray);
				
				//Clear variables
				gaps.clear();
				hitId="";
			}
			mapFinalGaps.put(iterNum, mapGaps);
		}
        return mapFinalGaps;
	}
    public  HashMap<String,HashMap<String,String[]>> getQueryFrameByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapQueryFrame = new HashMap<String, String[]>();
		ArrayList<String> queryframe = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalQueryFrame = new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					queryframe.add(hsp.hspQueryFrame);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] queryframeArray = new String[queryframe.size()];
				queryframeArray = queryframe.toArray(queryframeArray);
				
				mapQueryFrame.put(hitId, queryframeArray);
				
				//Clear variables
				queryframe.clear();
				hitId="";
			}
			mapFinalQueryFrame.put(iterNum, mapQueryFrame);
		}
        return mapFinalQueryFrame;
	}
    
    
    /**
     * @return
     */
    public  HashMap<String,HashMap<String,String[]>> getqSeqByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapqSeq = new HashMap<String, String[]>();
		ArrayList<String> qseq = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalqSeq= new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					qseq.add(hsp.hspQseq);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] qseqArray = new String[qseq.size()];
				qseqArray = qseq.toArray(qseqArray);
				
				mapqSeq.put(hitId, qseqArray);
				
				//Clear variables
				qseq.clear();
				hitId="";
			}
			mapFinalqSeq.put(iterNum, mapqSeq);
		}
        return mapFinalqSeq;
	}
    public  HashMap<String,HashMap<String,String[]>> gethSeqByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> maphSeq = new HashMap<String, String[]>();
		ArrayList<String> hseq = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalhSeq= new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					hseq.add(hsp.hspHseq);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] hseqArray = new String[hseq.size()];
				hseqArray = hseq.toArray(hseqArray);
				
				maphSeq.put(hitId, hseqArray);
				
				//Clear variables
				hseq.clear();
				hitId="";
			}
			mapFinalhSeq.put(iterNum, maphSeq);
		}
        return mapFinalhSeq;
	}
    public  HashMap<String,HashMap<String,String[]>> getMidlineByHit() {
		
		
		//Initialize variables
		HashMap<String, String[]> mapMidline = new HashMap<String, String[]>();
		ArrayList<String> midline = new ArrayList<String>();
		String hitId ="";
		HashMap<String,HashMap<String, String[]>> mapFinalMidline= new HashMap<String,HashMap<String, String[]>>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
        	String iterNum = iteration.iterationIterNum;
			
        	//Get all Hits
			List<Hit> listHits = iteration.iterationHits.hit;
			
			//For each hit, set the ID variable with the Hit Id and get all the Hsps related with the Hit
			for (Hit hit : listHits) {
				
				hitId=hit.hitId;
				
				//Get all Hsps of that Hit
				List<Hsp> listHsps = hit.hitHsps.hsp;
				//For each Hsp, add the AlignLen value to the list
				for (Hsp hsp : listHsps) {
					midline.add(hsp.hspMidline);
				}
				
				//Add new item to the map
				
				//Convert the List to Array
				String[] midArray = new String[midline.size()];
				midArray = midline.toArray(midArray);
				
				mapMidline.put(hitId, midArray);
				
				//Clear variables
				midline.clear();
				hitId="";
			}
			mapFinalMidline.put(iterNum, mapMidline);
		}
        return mapFinalMidline;
	}
    //--------------------------------------------------------------------------
    
    //Statistics
    public  HashMap<String, String> getStatDbNum(){

		
		
		//Initialize variables
		String iterNum ="";
		String iterDbNum="";
		HashMap<String,String> mapFinal = new HashMap<String,String>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
			iterDbNum=iteration.iterationStat.statistics.statisticsDbNum;
			
			mapFinal.put(iterNum, iterDbNum);
        }
        
        return mapFinal;
	}
    public  HashMap<String, String> getStatDbLen(){
		
		
		//Initialize variables
		String iterNum ="";
		String iterDbLen="";
		HashMap<String,String> mapFinal = new HashMap<String,String>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
			iterDbLen=iteration.iterationStat.statistics.statisticsDbLen;
			
			mapFinal.put(iterNum, iterDbLen);
        }
        
        return mapFinal;
	}
    public  HashMap<String, String> getStatHspLen(){
		
		
		//Initialize variables
		String iterNum ="";
		String iterHspLen="";
		HashMap<String,String> mapFinal = new HashMap<String,String>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
			iterHspLen=iteration.iterationStat.statistics.statisticsHspLen;
			
			mapFinal.put(iterNum, iterHspLen);
        }
        
        return mapFinal;
	}
    public  HashMap<String, String> getStatEffSpace(){

		
		
		//Initialize variables
		String iterNum ="";
		String iterEffSpace="";
		HashMap<String,String> mapFinal = new HashMap<String,String>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
			iterEffSpace=iteration.iterationStat.statistics.statisticsEffSpace;
			
			mapFinal.put(iterNum, iterEffSpace);
        }
        
        return mapFinal;
	}
    public  HashMap<String, String> getStatKappa(){
		
		
		//Initialize variables
		String iterNum ="";
		String iterKappa="";
		HashMap<String,String> mapFinal = new HashMap<String,String>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
			iterKappa=iteration.iterationStat.statistics.statisticsKappa;
			
			mapFinal.put(iterNum, iterKappa);
        }
        
        return mapFinal;
	}
    public  HashMap<String, String> getStatLambda(){
		
		
		//Initialize variables
		String iterNum ="";
		String iterLambda="";
		HashMap<String,String> mapFinal = new HashMap<String,String>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
			iterLambda=iteration.iterationStat.statistics.statisticsLambda;
			
			mapFinal.put(iterNum, iterLambda);
        }
        
        return mapFinal;
	}
    
    /**
     * @return
     */
    public  HashMap<String, String> getStatEntropy(){
		
		
		//Initialize variables
		String iterNum ="";
		String iterEntropy="";
		HashMap<String,String> mapFinal = new HashMap<String,String>();
		
		List<Iteration> listIterations = blout.getBlastOutputIterations().getIteration();
        
        for (Iteration iteration : listIterations) {
			iterNum = iteration.iterationIterNum;
			iterEntropy=iteration.iterationStat.statistics.statisticsEntropy;
			
			mapFinal.put(iterNum, iterEntropy);
        }
        
        return mapFinal;
	}
    //--------------------------------------------------------------------------
    //Gerais
    
    //Return numero de Hits por iteraçao
	public  HashMap<String,Integer> getNumberOfHits() {
		
		int numHits=0;
		String iterNum="";
		HashMap<String,Integer> numHitsFinal = new HashMap<String,Integer>();
		List<Iteration> listIteration = blout.blastOutputIterations.iteration;
		
		for (Iteration iteration : listIteration) {
			iterNum=iteration.iterationIterNum;
			
			List<Hit> listHits = iteration.iterationHits.hit;
			numHits = listHits.size();
			
			numHitsFinal.put(iterNum, numHits);
		}
		
		
		return numHitsFinal;
	}
	//Return um dic, com key=num iteraçao e chave é um dic com key= id do hit e value = nr de hsps do hit
	public HashMap<String,HashMap<String, Integer>> getNumberOfHspsByHit(){
		
		int numHsps=0;
		String HitId="";
		String IterNum="";
		HashMap<String, Integer> mapHsps = new HashMap<String, Integer>();
		HashMap<String,HashMap<String, Integer>> mapFinalHsps = new HashMap<String,HashMap<String, Integer>>();
				
		List<Iteration> listIteration = blout.blastOutputIterations.iteration;
		
		for (Iteration iteration : listIteration) {
			IterNum= iteration.iterationIterNum;
			List<Hit> listHits = iteration.iterationHits.hit;
			
			for (Hit hit : listHits) {
				HitId=hit.hitId;
				
				List<Hsp> listHsps = hit.hitHsps.hsp;
				numHsps=listHsps.size();
				
				mapHsps.put(HitId, numHsps);
			}
			
			mapFinalHsps.put(IterNum, mapHsps);
			
		}
		
		return mapFinalHsps;
	}
	//--------------------------------------------------------------------------

	/**
	 * @param reprocessQuery
	 */
	private void setReprocessQuery(boolean reprocessQuery) {
		this.reprocessQuery = reprocessQuery;
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
