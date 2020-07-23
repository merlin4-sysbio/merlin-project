package pt.uminho.ceb.biosystems.merlin.utilities.blast.ebi_blastparser;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EbiBlastParser {


	private EBIApplicationResult blout;
	private List<BlastIterationData> results;
	private boolean reprocessQuery;
	private boolean similarityFound;

	final static Logger logger = LoggerFactory.getLogger(EbiBlastParser.class);


	public EbiBlastParser (String filePath, JAXBContext jc) {
		this(new File(filePath),jc);
	}

	public EbiBlastParser (File xmlfile, JAXBContext jc) {

		this.setReprocessQuery(false);
		this.setResults(new ArrayList<>());
		try {

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			EBIApplicationResult blout = (EBIApplicationResult) unmarshaller.unmarshal(xmlfile);
			this.blout = blout;
			if(this.blout.getSequenceSimilaritySearchResult().getHits().getHit().size()>0)
				this.setSimilarityFound(true);
			this.buildResults();

		}
		catch(Exception ex) {
			ex.printStackTrace();
			logger.error("An error occurred while reading the file: "+ ex.getMessage());
			this.setReprocessQuery(true);
			this.setSimilarityFound(false);

		}

	}

	public EbiBlastParser (InputStream inputStream) {

		this.setReprocessQuery(false);

		this.setResults(new ArrayList<>());
		try {

			JAXBContext jc = JAXBContext.newInstance(EBIApplicationResult.class);

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			EBIApplicationResult blout = (EBIApplicationResult) unmarshaller.unmarshal(inputStream);

			this.blout = blout;			
			if(this.blout.getSequenceSimilaritySearchResult().getHits().getHit().size()>0)
				this.setSimilarityFound(true);
			this.buildResults();

		}
		catch(Exception ex) {
			ex.printStackTrace();
			logger.error("An error occurred while reading the file: "+ ex.getMessage());
			this.setReprocessQuery(true);
			this.setSimilarityFound(false);
		}
	}

	
	public boolean isSimilarityFound() {
		return similarityFound;
	}

	public void setSimilarityFound(boolean similarityFound) {
		this.similarityFound = similarityFound;
	}

	public boolean isReprocessQuery() {
		return reprocessQuery;
	}

	public void setReprocessQuery(boolean reprocessQuery) {
		this.reprocessQuery = reprocessQuery;
	}

	public List<BlastIterationData> getResults() {
		return results;
	}

	public void setResults(List<BlastIterationData> results) {
		this.results = results;
	}
	
	public void buildResults() {
		
		List<BlastIterationData> res = new ArrayList<>();
		
		
    	String seqDB = blout.getHeader().getParameters().getDatabases().getDatabase().get(0).getName();
    	String queryID = blout.getHeader().getParameters().getSequences().getSequence().get(0).getName();
    	//String queryDef = blout.getHeader().getParameters().getSequences().getSequence().get(0).getType();
    	String querylen = blout.getHeader().getParameters().getSequences().getSequence().get(0).getLength()+"";
    	String program = blout.getHeader().getProgram().getName();
    	String version = blout.getHeader().getProgram().getVersion();
        
    	List<THit> listHits = blout.getSequenceSimilaritySearchResult().getHits().getHit();			
		BlastIterationData iterationData = new BlastIterationData(queryID, querylen, listHits, seqDB, program, version);
		res.add(iterationData);
        this.results = res;
	}
}
