package pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.blast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WriteGBFile {

	//private BlastFilesToMap btm;
	private File genbankFile;
	private Map<String,String> ecn;
	private Set<String> tcn;
	private Map<String,String> prod;
	private Map<String, String> gene;

	//	/**
	//	 * Constructor for a GenBank file reader
	//	 * 
	//	 * @param btm Map of Blast files
	//	 * @param genbankFile file
	//	 */
	//	public WriteGBFile(BlastFilesToDB btm, File genbankFile){
	//		//this.btm=btm;
	//		this.ecn = btm.getECNumber();
	//		this.prod = btm.getProduct();
	//		this.genbankFile=genbankFile;
	//
	//	}

	/**
	 * Constructor for a GenBank file reader
	 * 
	 * @param genbankFile
	 * @param ecn
	 * @param prod
	 */
	public WriteGBFile(File genbankFile, Map<String,String> ecn, Map<String,String> prod, Map<String,String> gene) {

		this.ecn = ecn;
		this.tcn= new TreeSet<String>();
		this.gene = gene;
		this.prod = prod;
		this.genbankFile=genbankFile;

		for(String key:this.ecn.keySet()) {

			if(this.ecn.get(key).matches("\\d+\\.\\D\\..")) {

				this.tcn.add(key);
			}
		}
	}

	/**
	 * This method reads the GenBank file and inserts the product and the EC Number, if available for each gene
	 */
	public boolean writeFile() {

		// an input GenBank file  
		String inputFile = genbankFile.getAbsolutePath();

		//Create input stream
		FileInputStream inputFileStream;
		try 
		{
			inputFileStream = new FileInputStream(inputFile);
			// Get the object of DataInputStream
			DataInputStream dataStream = new DataInputStream(inputFileStream);

			//Buffer the InputStream
			BufferedReader bfr = new BufferedReader(new InputStreamReader(dataStream));
			//int line=0;
			String strLine="";
			StringBuffer strInput=new StringBuffer();
			while((strLine=bfr.readLine())!=null)
			{
				strInput.append("\n"+strLine);
				if(strLine.trim().startsWith("CDS"))
				{
					String gene=new String();
					String locus_tag = "", oldGeneName="", oldProduct = "", oldNote = "";
					Set<String> old_EC_number = new HashSet<String>(), old_TC_number= new HashSet<String>();

					while(!(strLine=bfr.readLine()).trim().startsWith("/translation"))
					{
						gene=gene.concat(strLine+"\n");
					}

					StringTokenizer stg = new StringTokenizer(gene,"\n");

					String tkg="";
					boolean next=true;
					while (stg.hasMoreTokens())
					{
						if(next)
						{
							tkg= stg.nextToken();
						}
						if(tkg.contains("/locus_tag="))
						{
							locus_tag=tkg.trim().substring(11).replace("\"", "");
							//System.out.println(locus_tag);
							next=true;
						}
						else if(tkg.contains("/product=\""))
						{
							oldProduct=tkg.replace("/product=\"", "product=");
							while(!(tkg=stg.nextToken()).contains("/"))
							{
								oldProduct=oldProduct.concat(" \n"+tkg);
							}
							next=false;
						}
						else if(tkg.contains("/gene="))
						{
							oldGeneName=tkg.trim().substring(6).replace("\"", "");
							next=true;
						}
						else if(tkg.contains("/note="))
						{
							oldNote=tkg;
							while(!(tkg=stg.nextToken()).contains("/"))
							{
								oldNote=oldNote.concat(" \n"+tkg);
							}
							next=false;
						}
						else if(tkg.contains("/EC_number="))
						{
							old_EC_number.add(tkg.trim().replace("/EC_number=\"","").replace("\"", ""));
							next=true;
						}
						else if(tkg.contains("/TC_number="))
						{
							old_TC_number.add(tkg.trim().replace("/TC_number=\"","").replace("\"", ""));
							next=true;
						}
						else
						{
							next=true;
						}
					}

					stg = new StringTokenizer(gene,"\n");
					tkg= stg.nextToken();
					while (stg.hasMoreTokens())
					{

						if(tkg.trim().startsWith("/locus_tag="))
						{
							strInput.append("\n"+tkg);
							if(this.gene.containsKey(locus_tag)) {

								strInput.append("\n                     /gene=\""+this.gene.get(locus_tag)+"\"");	
							}
							if(this.ecn.containsKey(locus_tag))
							{
								StringBuffer stEC = new StringBuffer(), stTC = new StringBuffer();

								StringTokenizer st = new StringTokenizer(this.ecn.get(locus_tag),", ");
								while (st.hasMoreTokens())
								{
									String aux = st.nextToken();
									old_EC_number.remove(aux);
									old_TC_number.remove(aux);

									if(aux.matches("\\d+\\.[A-Z]\\..+"))
									{
										stTC.append("\n                     /TC_number=\""+aux+"\"");
									}
									else
									{
										stEC.append("\n                     /EC_number=\""+aux+"\"");
									}
								}
								for(String aux:old_EC_number)
								{
									stEC.append("\n                     /EC_number=\""+aux+"\"");
								}
								for(String aux:old_TC_number)
								{
									stTC.append("\n                     /TC_number=\""+aux+"\"");
								}
								strInput.append(stEC);
								strInput.append(stTC);
							}
							//							if(this.ecn.containsKey(locus_tag))
							//							{
							//								StringTokenizer st = new StringTokenizer(this.ecn.get(locus_tag),", ");
							//								while (st.hasMoreTokens())
							//								{
							//									String aux = st.nextToken();
							//									old_EC_number.remove(aux);
							//									strInput.append("\n                     /EC_number=\""+aux+"\"");
							//								}
							//								for(String aux:old_TC_number)
							//								{
							//									strInput.append("\n                     /EC_number=\""+aux+"\"");
							//								}
							//							}
							if(this.prod.containsKey(locus_tag)) {

								String productString="/product=\""+this.prod.get(locus_tag)+"\"";
								String productsShort=divideString(productString);
								strInput.append("\n"+productsShort);
							}

							if(oldNote.isEmpty()) {

								oldNote="                     /note=\"\"";
							}

							if(this.gene.containsKey(locus_tag) && !oldGeneName.equals(this.gene.get(locus_tag))) {

								oldNote=oldNote.replace("\n                     /note=\"", "\n                     /note=\""+oldGeneName.trim()+"\n                     ");
							}

							String productString="/product=\""+this.prod.get(locus_tag)+"\"";

							if(this.prod.containsKey(locus_tag) && !oldProduct.equals(divideString(productString.replace("/product=\"", "product=")))) {

								oldNote=oldNote.replace("\"", "").replace("/note=", "/note=\"")+"\n"+oldProduct;
							}

							if(oldNote.trim()!="/note=\"\""+"\n") {

								strInput.append("\n"+oldNote);
							}
							tkg= stg.nextToken();
						}
						else
						{
							if(tkg.contains("/product=") && this.prod.containsKey(locus_tag))
							{
								while(!(tkg=stg.nextToken()).contains("/"));
							}
							else if(tkg.contains("/gene=") && this.gene.containsKey(locus_tag))
							{
								while(!(tkg=stg.nextToken()).contains("/"));
							}
							else if(tkg.contains("/EC_number=") && this.ecn.containsKey(locus_tag))
							{
								while(!(tkg=stg.nextToken()).contains("/"));
							}
							else if(tkg.contains("/TC_number=") && this.tcn.contains(locus_tag))
							{
								while(!(tkg=stg.nextToken()).contains("/"));
							}
							else if(tkg.contains("/note=") && oldNote.trim()!="/note=\"\""+"\n")
							{
								while(!(tkg=stg.nextToken()).contains("/"));
							}
							else 
							{
								strInput.append("\n"+tkg);
								tkg= stg.nextToken();
							}
						}
					}	
					strInput.append("\n"+strLine);
				}
			}

			String path = genbankFile.getParent()+"/newData/";
			File f = new File(path);
			boolean exit = f.mkdirs();

			BufferedWriter out = new BufferedWriter(new FileWriter(path+genbankFile.getName().replace(".gbk",".1.gbk")));
			out.write(strInput.toString());
			out.close();
			bfr.close();
			return exit;
		} 
		catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		catch (IOException e) {

			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param patternStr
	 * @param input
	 * @return
	 */
	public static String find(String patternStr, CharSequence input) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) 
		{
			return matcher.group();
		} 
		return null; 
	}

	/**
	 * @param longString
	 * @return
	 */
	public String divideString(String longString) {

		String shortStrings="";

		if(longString!=null) {

			while (longString.length()>56) {

				String tempString = find(".{1,57}.\\s", longString);
				shortStrings=shortStrings.concat("                     "+tempString.trim()+" \n");
				longString=longString.replace(tempString, "");
			}
			shortStrings=shortStrings.concat("                     "+longString);
		}

		return shortStrings;
	}

}
