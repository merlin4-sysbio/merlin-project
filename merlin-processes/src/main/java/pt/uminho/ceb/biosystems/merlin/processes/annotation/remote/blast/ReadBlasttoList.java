package pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.blast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.Annotation;
import org.biojava.bio.program.sax.BlastLikeSAXParser;
import org.biojava.bio.program.ssbind.BlastLikeSearchBuilder;
import org.biojava.bio.program.ssbind.SeqSimilarityAdapter;
import org.biojava.bio.search.SearchContentHandler;
import org.biojava.bio.search.SeqSimilaritySearchResult;
import org.biojava.bio.seq.db.DummySequenceDB;
import org.biojava.bio.seq.db.DummySequenceDBInstallation;
import org.xml.sax.InputSource;

public class ReadBlasttoList {
	

	private static final boolean verbose = false;

	/**
	 *  The list to hold the SeqSimilaritySearchResults
	 *  List of Database ID's  
	 */
	private List<SeqSimilaritySearchResult> results = new ArrayList<SeqSimilaritySearchResult>();

	/**
	 * The query that was blasted
	 */
	private String query = "", dbid = "", program = "", version = "";

	private boolean reprocessQuery;

	private boolean similarityFound;

	/**
	 * This is the constructor for implementations of the Blast to List class.
	 * This class parses an input file and retrieves the Sequence Similarity Search Results in the form of a List
	 * 
	 * @param input
	 */
	public ReadBlasttoList(String input){
		this.setReprocessQuery(false);

		try 
		{
			CheckBlastResult cbf = new CheckBlastResult(input);
			this.parseStream(cbf);
		}
		catch (IOException e){	this.setReprocessQuery(true);}
	}

	
	/**
	 * @param input
	 */
	public ReadBlasttoList(CheckBlastResult cbr) {

		this.parseStream(cbr);
	}

	/**
	 * @param input
	 */
	public ReadBlasttoList(InputStream input) {

		this.setReprocessQuery(false);
		
		try {

			CheckBlastResult cbf = new CheckBlastResult(input);
			this.parseStream(cbf);
		}
		catch (IOException e) {

			e.printStackTrace();
			this.setReprocessQuery(true);
		}
	}


	/**
	 * @param cbf
	 */
	private void parseStream(CheckBlastResult cbf){

		this.results=new ArrayList<SeqSimilaritySearchResult>();

		try {

			if(cbf.isBlastResultOK()) {

				InputSource inS = cbf.getInputSource();

				// make a BlastLikeSAXParser
				BlastLikeSAXParser parser = new BlastLikeSAXParser();

				//make the SAX event adapter that will pass events to a Handler
				//this adapter will retrieve the SAX file results, and pass them to the handler
				SeqSimilarityAdapter adapter = new SeqSimilarityAdapter();

				//create the SearchContentHandler that will build SeqSimilaritySearchResults in the results List
				
				SearchContentHandler scHandler = new BlastLikeSearchBuilder(this.results,
						new DummySequenceDB("dummy"), new DummySequenceDBInstallation());

				//register the adapter to the created SearchContentHandler,
				//that will build SeqSimilaritySearchResults in the results List
				adapter.setSearchContentHandler(scHandler);

				// set the parsers SAX event adapter
				parser.setContentHandler(adapter);

				// try to parse, even if the blast version is not recognized.
				parser.setModeLazy();

				// parse the file, after this the result idList will be populated with
				// SeqSimilaritySearchResults
				if(inS != null)
					parser.parse(inS);

				//close InputStream
				cbf.closeInputStream();

				this.setSimilarityFound(cbf.isSimilarityFound());

				// output some blast details

				if(cbf.isSimilarityFound()) {

					for(SeqSimilaritySearchResult result:this.results) {

						Annotation anno = result.getAnnotation();
						this.setQuery((String) anno.getProperty("queryId"));
						this.dbid = (String) anno.getProperty("databaseId");
						this.program = (String) anno.getProperty("program");
						this.version = (String) anno.getProperty("version");
					}

					if(this.query == null || this.query.isEmpty()) 
						this.setReprocessQuery(true);
					
				}
			}
			else {

				this.setReprocessQuery(true);
			}
		}
		catch (Exception ex) {

			if(verbose)
				ex.printStackTrace();
			
			if(cbf.isSimilarityFound()) {

				this.setReprocessQuery(true);
			}
		}
		this.setSimilarityFound(cbf.isSimilarityFound());
	}


	/**
	 * Retrieves the idList
	 * @return
	 */
	public List<SeqSimilaritySearchResult> getResults() {
		return this.results;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Retrieves the query
	 * @return
	 */
	public String getQuery() {
		return this.query;
	}

	/**
	 * Retrieves the databaseID
	 * @return
	 */
	public String getDatabaseId() {
		return this.dbid;
	}

	/**
	 * Retrieves the Program used to blast the sequences
	 * @return
	 */
	public String getProgram() {
		return this.program;
	}

	/**
	 * Retrieves the Version
	 * @return
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * @param noSimilarityFound the noSimilarityFound to set
	 */
	public void setSimilarityFound(boolean similarityFound) {
		this.similarityFound = similarityFound;
	}

	/**
	 * @return the noSimilarityFound
	 */
	public boolean isSimilarityFound() {
		return similarityFound;
	}

	/**
	 * @return the elementException
	 */
	public boolean isReprocessQuery() {
		return reprocessQuery;
	}

	/**
	 * @param reprocessQuery the elementException to set
	 */
	public void setReprocessQuery(boolean reprocessQuery) {
		this.reprocessQuery = reprocessQuery;
	}

	/**
	 * method deprecated
	 * 
	 * @return
	 */
	@Deprecated
	public boolean isGo(){
		return false;
	}


	/**
	 * method deprecated
	 * 
	 * @return
	 */
	@Deprecated
	public boolean isNoSimilarityFound() {
		return false;
	}

	//	public static void main (String[] args){
	//		//ReadBlasttoList rb = new ReadBlasttoList("D:/My Dropbox/SharedFolders/AGO/gi44979986gbAAS50182.1.out");	
	//		//rb.getResults();
	//		try 
	//		{
	//		FileInputStream finstream = new FileInputStream("/home/oscar/Desktop/issue.txt");
	//		DataInputStream in = new DataInputStream(finstream);
	//		BufferedReader br = new BufferedReader(new InputStreamReader(in));
	//		String poLine;
	//		
	//			while((poLine=br.readLine())!=null)
	//			{
	//				poLine = poLine.toUpperCase();
	//				//System.out.println(poLine);
	//				if ( (poLine.startsWith("QUERY:")) ||
	//						(poLine.startsWith("SBJCT:")) )
	//				{
	//
	//					StringTokenizer oSt = new StringTokenizer(poLine,":");
	//					//there should be two tokens at this point
	//					if (oSt.countTokens() != 2)
	//					{
	//						System.out.println("more than two");
	//					}
	//					//get here if Query line OK
	//					//skip first token (i.e. "Query")
	//					oSt.nextToken();
	//					//next token is the alignment - make it uppercase.
	//					String oSeq = oSt.nextToken().trim();
	//					//System.out.println(oSeq);
	//					//To get numbers robustly, tokenize on letters, gaps, and unknowns
	//					oSt = new StringTokenizer(oSeq," ABCDEFGHIJKLMNOPQRSTUVWXYZ-*");
	//					if (oSt.countTokens() != 2)
	//					{
	//						System.out.println(oSeq);
	//						System.out.println(oSt.countTokens());
	//						while (oSt.hasMoreElements())
	//						{
	//							System.out.println("1\t"+oSt.nextToken());
	//						}
	//						
	//						System.out.println("Failed to parse a line of an alignment due to it having" +
	//								" an unexpected character."+oSeq);
	//					}
	//
	//					
	//				}}
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}
}
