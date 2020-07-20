package pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.blast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.StringTokenizer;

import org.xml.sax.InputSource;

/**
 * @author ODias
 *
 */
public class CheckBlastResult {

	private InputSource inputSource;
	private StringReader strInputStream;
	private boolean similarityFound=true,
			blastResultOk=true;
	private String query;
	private String database;

	//TODO replace.("&gt;",">") in the old files and create an inputStream for the old files

	/**
	 * @param input
	 * @throws IOException
	 */
	public CheckBlastResult(String input) throws IOException {
		super();
		this.inputSource = new InputSource();
		this.inputSource = this.readFile(input);
	}

	/**
	 * @param input
	 * @throws IOException
	 */
	public CheckBlastResult(InputStream input) throws IOException {
		super();
		this.inputSource = new InputSource();
		this.inputSource = this.readInputStream(input);
	}

	/**
	 * @return
	 */
	public InputSource getInputSource() {
		return inputSource;
	}

	/**
	 * 
	 */
	public void closeInputStream(){
		this.strInputStream.close();	
	}

	/**
	 * @param inputFile
	 * @return
	 * @throws IOException
	 */
	public InputSource readFile(String inputFile) throws IOException {
		
		//Create input stream
		FileInputStream inputFileStream = new FileInputStream(inputFile);

		// Get the object of DataInputStream
		DataInputStream dataStream = new DataInputStream(inputFileStream);

		InputSource is = this.readInputStream(dataStream);
		if(is==null)
			is = new InputSource(new FileInputStream(inputFile));

		inputFileStream.close();
		return is;
	}

	/**
	 * @param dataStream
	 * @return
	 * @throws IOException 
	 */
	public InputSource readInputStream(InputStream dataStream) throws IOException {

		InputSource is = new InputSource();
		boolean notOldBlast=true,findLength=false;
		String strLine = "";
		StringBuffer strBuff = new StringBuffer();

		boolean streamBeggining = true;
		//Buffer the InputStream
		BufferedReader bfr = new BufferedReader(new InputStreamReader(dataStream));

		//Read File Line By Line
		while(!findLength) {

//			strLine=bfr.readLine();
//			if(strLine!= null && strLine.startsWith("<p>")) {
//
//				//while ((strLine=bfr.readLine())!= null && !strLine.equalsIgnoreCase("--><p>"));
//				while ((strLine=bfr.readLine())!= null && !strLine.equalsIgnoreCase("<PRE>")) {
//
//					System.out.println(strLine);	
//				}
//				strLine=bfr.readLine();
//			}

			if(streamBeggining) {

				streamBeggining=false;
				while (strLine!= null && 
						(!strLine.toLowerCase().startsWith("BLASTP".toLowerCase()) &&
								!strLine.toLowerCase().startsWith("BLASTN".toLowerCase()) && 
								!strLine.toLowerCase().startsWith("BLASTX".toLowerCase()) && 
								!strLine.toLowerCase().startsWith("TBLASTN".toLowerCase()) && 
								!strLine.toLowerCase().startsWith("TBLASTX".toLowerCase()))) {
					strLine=bfr.readLine();}
			}
			else {

				strLine=bfr.readLine();
			}
			
			if(strLine==null) {

				this.setBlastResultOk(false);
				return  null;
			}

			if(!strLine.startsWith("Length")) {

				if(strLine.startsWith("Reference:")) {

					strLine="\n\n"+strLine;
				}

				if(strLine.startsWith("Reference for")) {

					//strLine=bfr.readLine();
					while (strLine!= null && !strLine.startsWith("RID") && !strLine.startsWith("Database")){
						
						strLine=bfr.readLine();
					}
				}

				//while(!strLine.startsWith("Length") && notOldBlast){
				if(strLine.startsWith("Blast")) {

					String str = strLine;
					str=str.substring(7,10).replace(".","");
					if((Integer.parseInt(str, 10))<22) {

						notOldBlast=false;
						findLength=true;
					}
				}

				if(strLine.startsWith("Database:")) {
					
					if(strLine.contains("Database"))
						this.database = strLine.replaceAll("Database:", "").trim();
					
					String db = strLine;
					strLine=bfr.readLine();
					while (!strLine.startsWith("Query=")) {
						
						db+=(strLine+" \n");
						strLine=bfr.readLine();
					}
					
					if(strLine.contains("Query"))
						this.query = strLine.replace("Query=","").trim();
					
//					System.out.println(strLine);
					//String query = strLine+" \n";
					String query = "";
					while (!strLine.startsWith("Length=")) {

						query+=(strLine+" \n");
						findLength=true;
						strLine=bfr.readLine();
					}
					strLine = query+strLine+"\n"+db;
				}
				//strInput+=strLine+"\n";
				strBuff.append(strLine+"\n");
			}
			else {

				findLength=true;
			}
		}

		//whether  is an old or new Blast version
		if(notOldBlast) {

			strBuff.append("\nSearching \n");
			//strInput+="\nSearching \n";
			String s;
			//StringBuffer buff = new StringBuffer();
			//buff.append(strInput);
			//BufferedWriter br = new BufferedReader();
			boolean go = true;
			boolean databaseFound = false, lambdaFound = false, write = true;
			String dbBuff="";
			while ((s=bfr.readLine())!= null && go) {
				
				if(s.contains("No significant similarity found.") || s.contains(" No hits found "))
					this.setSimilarityFound(false);
				
				if(s.startsWith("Query")) {
					
					s=s.replace("Query","Query:");
					StringTokenizer oSt = new StringTokenizer(s,":");
					String tempS = oSt.nextToken().concat(":");
					String oSeq = oSt.nextToken().trim();
					oSt = new StringTokenizer(oSeq," ABCDEFGHIJKLMNOPQRSTUVWXYZ-*");
					if (oSt.countTokens() == 0) {
						
						tempS=tempS.concat("\t0\t").concat(oSeq).concat("\t0");
						s=tempS;
						//System.out.println(s);
					}
				}
				
				if(s.startsWith("Sbjct")) {
					
					s=s.replace("Sbjct","Sbjct:");
					StringTokenizer oSt = new StringTokenizer(s,":");
					String tempS = oSt.nextToken().concat(":");
					String oSeq = oSt.nextToken().trim();
					oSt = new StringTokenizer(oSeq," ABCDEFGHIJKLMNOPQRSTUVWXYZ-*");
					if (oSt.countTokens() == 0) {
						
						tempS=tempS.concat("\t0\t").concat(oSeq).concat("\t0");
						s=tempS;
						//System.out.println(s);
					}
				}
				if(s.startsWith("Length=")) {
					
					s=s.replace("Length=", "Length = ");
				}
				
				if(s.startsWith("  Database")) {
					
					databaseFound = true;
				}
				
				if(s.startsWith("Lambda")) {
					
					lambdaFound = true;	
					
					if(!databaseFound) {
						
						while (!s.startsWith("Effective search space")) {
							
							dbBuff+=(strLine+" \n");
							s=bfr.readLine();
						}
						dbBuff+=(strLine+" \n");
					}
				}
				
				if(s.startsWith("Matrix") && lambdaFound && databaseFound && write) {
					
					write = false;					
					strBuff.append(dbBuff);
				}
				
				//System.out.println(s);
				//strInput+=s+"\n";
				//buff.append(s+"\n");
				strBuff.append(s+"\n");
				
				if(s.startsWith("S2:")) {
					
					go = false;
				}
			}
			strBuff.append("\n\n\n\n");

			//StringReader strInputStream = new StringReader(strInput);
			//StringReader strInputStream = new StringReader(buff.toString());
			this.strInputStream = new StringReader(strBuff.toString());
			is = new InputSource(this.strInputStream);
			//dataStream.close();
			//strBuff=null;
			//new InputSource();
		}
		else
		{
			is = null;
		}
		return is;
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
	 * @return the errorGettingResult
	 */
	public boolean isBlastResultOK() {
		return blastResultOk;
	}

	/**
	 * @param errorGettingResult the errorGettingResult to set
	 */
	public void setBlastResultOk(boolean blastResultOk) {
		this.blastResultOk = blastResultOk;
	}

	/**
	 * @return
	 */
	public String getQuery() {

		return this.query;
	}
	
	/**
	 * @return
	 */
	public String getDatabase(){
		
		return this.database;
	}
}
