package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.features.Qualifier;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.DocumentSummary;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.DocumentSummarySet;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.ESearchResult;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.ESummaryResult;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.Enumerators.FileExtensions;
import pt.uminho.ceb.biosystems.merlin.utilities.io.ConfFileReader;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

public class CreateGenomeFile {

	private static String today = CreateGenomeFile.setToday();


	//	/**
	//	 * @param genomeID
	//	 * @param numberOfDaysOld
	//	 * @param sourceDB
	//	 * @throws IOException
	//	 * @throws ParseException
	//	 */
	//	public static CreateGenomeFile(String databaseName, int numberOfDaysOld, String sourceDB, String extension) throws Exception {
	//		//databaseName
	//		this.createTempFile(numberOfDaysOld, sourceDB, extension);
	//	}
	//

	/**
	 * @param genomeID
	 * @param extension
	 * @return
	 * @throws Exception
	 */
	public static Map<String, AbstractSequence<?>> getGenomeFromID(String databaseName, Long taxonomyID, FileExtensions proteinFaa) throws Exception {

		try {
			
			if (!CreateGenomeFile.currentTemporaryDataIsNOTRecent(0, databaseName, taxonomyID, CreateGenomeFile.setToday(), proteinFaa)) {
				
				Map<String, AbstractSequence<?>> ret = new HashMap<>();
				Map<String,ProteinSequence> aas = FastaReaderHelper.readFastaProteinSequence(new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID) + proteinFaa.getName()));
				ret.putAll(aas);

				return ret;
			}

		} catch (Exception e) {

			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
	//	/**
	//	 * @param genomeID
	//	 * @param path
	//	 * @param extension
	//	 * @return
	//	 * @throws Exception
	//	 */
	//	public static Map<String, ProteinSequence> getGenomeFromID(String genomeID, String path, String extension) throws Exception {
	//
	//		if(!CreateGenomeFile.currentTemporaryDataIsNOTRecent(0,path, genomeID, CreateGenomeFile.setToday(), extension)) {
	//
	//			return FastaReaderHelper.readFastaProteinSequence(new File(path+genomeID+extension));
	//		}
	//		return null;
	//	}


	/** Retrieves assembly information for a given taxonomyID
	 * @param taxonomyID
	 * @return eSummaryReport (GenBank and RefSeq accessions, GenBank and RefSeq ftps url,...)
	 */
	public static DocumentSummarySet getESummaryFromNCBI(String taxonomyID) {

		EntrezServiceFactory entrezServiceFactory = new EntrezServiceFactory("https://eutils.ncbi.nlm.nih.gov/entrez/eutils", false);
		EntrezService entrezService = entrezServiceFactory.build();

		ESearchResult eSearchResult = entrezService.eSearch(NcbiDatabases.assembly, taxonomyID +"[Taxonomy ID]", "xml", "100");

		List<String> idList = eSearchResult.idList;

		String uids = idList.get(0);
		idList.remove(0);
		if(idList.size()>0) {
			for(String i:eSearchResult.idList)
				uids += ","+i;
		}

		ESummaryResult eSummaryResult = entrezService.eSummary(NcbiDatabases.assembly, uids);
		DocumentSummarySet docSummarySet = eSummaryResult.documentSummarySet.get(0);

		return docSummarySet;
	}

	/**
	 * @param docSummaryset
	 * @return ArrayList<String> assemblyNames
	 */
	public static List<String> getAssemblyNames(DocumentSummarySet docSummaryset){

		List<String> assemblyNames = new ArrayList<>();

		for (int i=0; i<docSummaryset.documentSummary.size(); i++) {
			DocumentSummary doc = docSummaryset.documentSummary.get(i);

			for(String property : doc.propertyList) {

				if(property.contains("genbank")){

					if(doc.propertyList.get(doc.propertyList.indexOf(property)-1).equals("latest"))
						assemblyNames.add(doc.assemblyName);

				}
			}
		}

		return assemblyNames;
	}


	/**
	 * @param docSum
	 * @param databaseName
	 */
	public static void saveAssemblyRecordInfo(DocumentSummary docSum, String databaseName) {

		PrintWriter writer;
		String uid = docSum.uid;
		String speciesName = docSum.speciesName;
		String assemblyAccession = docSum.assemblyAccession;
		String lastupdateDate = docSum.lastupdateDate.substring(0, 10);
		String accessionGenBank = docSum.accessionGenBank;
		String accessionRefSeq = docSum.accessionRefSeq;
		Long taxonomyID = Long.parseLong(docSum.taxonomyID);
		String submitter = docSum.submitter;
		String genBankStatus = null;
		String refSeqStatus = null;

		for(String property : docSum.propertyList) {

			if(property.contains("genbank"))
				genBankStatus = property;

			if(property.contains("refseq"))
				refSeqStatus = property;
		}

		try {
			
			writer = new PrintWriter(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID)  +"assemblyRecordInfo.txt", "UTF-8");
			writer.println("UID: " + uid + System.getProperty("line.separator") + "Assembly Accession: " + assemblyAccession + System.getProperty("line.separator")
			+ "Accession GeneBank: " + accessionGenBank + System.getProperty("line.separator") + "Accession RefSeq: " + accessionRefSeq);
			writer.println("Species Name: " + speciesName + System.getProperty("line.separator") +  "Last Update Date: " + lastupdateDate + System.getProperty("line.separator") 
			+ "GenBank Status: " + genBankStatus + System.getProperty("line.separator") + "RefSeq Status: " + refSeqStatus + System.getProperty("line.separator") 
			+ "Submitter: " + submitter);
			writer.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}


	/**
	 * @param databaseName
	 * @param taxonomyID
	 * @return
	 */
	public static List<String> getAssemblyRecordInfo(String databaseName, String taxonomyID) {

		Long longTaxID = Long.parseLong(taxonomyID);

		String filePath = FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, longTaxID) +"assemblyRecordInfo.txt";
		List<String> assemblyInfo = null;

		try {
			assemblyInfo = FileUtils.readLines(filePath);
		} 

		catch (IOException e) {
			e.printStackTrace();
		} 

		return assemblyInfo;
	}

	/** Given an assembly UID and a DocumentSummarySet, retrives the desired ftp url info
	 * @param uid
	 * @param docSumSet
	 * @param isGenBankFtp
	 * @return (GenBank/RefSeq) ftp URL info
	 */
	public static ArrayList<String> getFtpURLFromAssemblyUID(DocumentSummary documentSummary, boolean isGenBankFtp){

		//		DocumentSummary document = null;

		//		for (int i=0; i<docSumSet.documentSummary.size(); i++) {
		//			DocumentSummary doc = docSumSet.documentSummary.get(i);
		//			if (doc.uid == uid)
		//				document = doc;
		//		}
		ArrayList<String> ftpURLinfo = new ArrayList<>();

		if(isGenBankFtp) {

			ftpURLinfo.add(documentSummary.ftpGenBank);
			ftpURLinfo.add(documentSummary.accessionGenBank);
			ftpURLinfo.add(documentSummary.assemblyName.replace(" ", "_"));
			ftpURLinfo.add(documentSummary.taxonomyID);
		}
		else {
			ftpURLinfo.add(documentSummary.ftpRefSeq);
			ftpURLinfo.add(documentSummary.accessionRefSeq);
			ftpURLinfo.add(documentSummary.assemblyName.replace(" ", "_"));
			ftpURLinfo.add(documentSummary.taxonomyID);
		}

		return ftpURLinfo;		
	}


	/** Retrieves files from NCBI ftp
	 * @param ftpURLinfo
	 * @param fileType
	 * @param databaseName
	 * @throws IOException
	 */
	public static void getFilesFromFtpURL(ArrayList<String> ftpURLinfo , String databaseName) throws IOException {

		int BUFFER_SIZE = 4096;  

		String ftpUrl = ftpURLinfo.get(0);
		String filePath = null;
		String savePath = null;

		HashMap <String, String> fileExtensions = ConfFileReader.readExtensionsConf();

		for(String type : fileExtensions.keySet()){
			if(type.equals("ASSEMBLY_REPORT") || type.equals("ASSEMBLY_STATS")) {
				filePath = ftpURLinfo.get(1) + "_" + ftpURLinfo.get(2) + "_" + fileExtensions.get(type);	
				savePath = FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, Long.parseLong(ftpURLinfo.get(3))) + fileExtensions.get(type);
			}
			else {
				filePath = ftpURLinfo.get(1) + "_" + ftpURLinfo.get(2) + "_" + fileExtensions.get(type) + ".gz";
				savePath = FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, Long.parseLong(ftpURLinfo.get(3))) + filePath;	
			}

			String ftpUrlFile = ftpUrl + "/" + filePath;
			String httpUrl = ftpUrlFile.replace("ftp:", "https:");

			try {
				URL u = new URL (httpUrl);
				HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection (); 
				huc.setRequestMethod ("HEAD"); 
				HttpURLConnection.setFollowRedirects(false);
				huc.connect () ; 
				int code = huc.getResponseCode();

				if(code == HttpURLConnection.HTTP_OK){

					URL url = new URL(ftpUrlFile);
					URLConnection conn = url.openConnection();
					InputStream inputStream = conn.getInputStream();

					FileOutputStream outputStream = new FileOutputStream(savePath);

					byte[] buffer = new byte[BUFFER_SIZE];
					int bytesRead = -1;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}
					
					outputStream.close();
					inputStream.close();
					
					if(!type.equals("ASSEMBLY_REPORT") && !type.equals("ASSEMBLY_STATS")) 
						FileUtils.unGunzipFile(savePath, FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, Long.parseLong(ftpURLinfo.get(3))) + fileExtensions.get(type));

					if(type.equals("RNA_FROM_GENOMIC")){
						File rnaFile = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, Long.parseLong(ftpURLinfo.get(3))) + fileExtensions.get(type));
						divideAndBuildRNAGenomicFastaFile(databaseName, Long.parseLong(ftpURLinfo.get(3)), rnaFile);
					}
				} 
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	
	/**
	 * verify if a database directory exists, if not creats it
	 * @param databaseName
	 * @return
	 */
	public static boolean createFolder(String databaseName, Long taxonomyID) {

		File newPath = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID));
		//		File newPath = new File(FileUtils.getWorkspacesFolderPath().concat(databaseName).concat("/").concat(taxonomyID.toString()));

		if (newPath.exists())
			return false;

		else 
			newPath.mkdirs();

		return true;
	}


	/**
	 * this method receives a rnaFastaFile and split it in two fasta filas (rRNA.fna and tRNA.fna)
	 * 
	 * @param dbName
	 * @param taxID
	 * @param rnaFastaFile
	 */
	public static void divideAndBuildRNAGenomicFastaFile (String dbName, Long taxID, File rnaFastaFile){

		Map<String, AbstractSequence<?>> codingSequences = new HashMap<String, AbstractSequence<?>>();
		PrintWriter tRNA, rRNA;

		try {
			codingSequences.putAll(FastaReaderHelper.readFastaDNASequence(rnaFastaFile));

			tRNA = new PrintWriter(FileUtils.getWorkspaceTaxonomyFolderPath(dbName, taxID) +"trna_from_genomic.fna", "UTF-8");
			rRNA = new PrintWriter(FileUtils.getWorkspaceTaxonomyFolderPath(dbName, taxID) +"rrna_from_genomic.fna", "UTF-8");

			for(String key:codingSequences.keySet()){

//				String newHeader = key;
//				newHeader = newHeader.split("\\s")[0];
				
				if(key.contains("gbkey=tRNA")){

					tRNA.write(">"+codingSequences.get(key).getOriginalHeader()+"\n");
					tRNA.write(((DNASequence)codingSequences.get(key)).getRNASequence().getSequenceAsString()+"\n");
				}
				else if(key.contains("gbkey=rRNA")){
					
					rRNA.write(">"+codingSequences.get(key).getOriginalHeader()+"\n");
					rRNA.write(((DNASequence)codingSequences.get(key)).getRNASequence().getSequenceAsString()+"\n");
				}
			}
			tRNA.close();
			rRNA.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @param fastaFiles
	 * @throws Exception 
	 */
	public static void createGenomeFileFromFasta(String databaseName, Long taxonomyID, File fastaFile, FileExtensions extension, Map<String, String[]> genesData) throws Exception {

		CreateGenomeFile.createFolder(databaseName, taxonomyID);

		if(CreateGenomeFile.currentTemporaryDataIsNOTRecent(-1,databaseName, taxonomyID, today, extension)) {

			Map<String, AbstractSequence<?>> sequences= new HashMap<String, AbstractSequence<?>>();

			//for(File fastFile : fastaFiles)	
			
			if(extension.equals(FileExtensions.RNA_FROM_GENOMIC))
				sequences.putAll(FastaReaderHelper.readFastaRNASequence(fastaFile));
			else if(extension.equals(FileExtensions.PROTEIN_FAA))
				sequences.putAll(FastaReaderHelper.readFastaProteinSequence(fastaFile));
			else
				sequences.putAll(FastaReaderHelper.readFastaDNASequence(fastaFile));	//FileExtensions.CDS_FROM_GENOMIC AND FileExtensions.GENOMIC_FNA

			
//			Map<String, String> locus_tags = null;
			
//			if(extension.equals(FileExtensions.PROTEIN_FAA) && extension.equals(FileExtensions.CDS_FROM_GENOMIC)){
//				
//				File genBankFile = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID).concat(GenBankFiles.GENOMIC_GBFF.extension()));
//				if(genBankFile.exists()){
//					locus_tags = getLocusTagFromGenBankFastFile(genBankFile);
//				}
//				else if((genBankFile = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID).concat(GenBankFiles.CUSTOM_FILE.extension()).concat(".gbff"))).exists()){
//					locus_tags = getLocusTagFromGenBankFastFile(genBankFile);
//				}
//				else if((genBankFile = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID).concat(GenBankFiles.CUSTOM_FILE.extension()).concat(".gpff"))).exists()){
//					locus_tags = getLocusTagFromGenBankFastFile(genBankFile);
//				}
//					
//			}
			
			CreateGenomeFile.buildFastFile(databaseName, taxonomyID, sequences, genesData, fastaFile, extension);//,locus_tags);
			CreateGenomeFile.createLogFile(databaseName, taxonomyID, extension);
			
//			PreparedStatement pStatement = connection.prepareStatement("INSERT INTO sequence(gene_idgene, sequence_type, sequence, sequence_length, entry_type, kegg_id) VALUES(?,?,?,?,?,?);");


		}
	}

	/**
	 * @return
	 */
	private static String setToday() {

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd");
		return formatter.format(currentDate.getTime());
		//formatter = new SimpleDateFormat("HH:mm:ss");
		//String now = formatter.format(currentDate.getTime());
	}

	//	/**
	//	 * @param numberOfDaysOld
	//	 * @return
	//	 * @throws Exception
	//	 */
	//	public Map<String, ProteinSequence> getGenome(String taxonomyID, String databaseName, String extension) throws Exception {
	//
	//		return FastaReaderHelper.readFastaProteinSequence(new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID) + FileExtensions.valueOf(extension.toUpperCase()).getExtension()));
	//	}


	//	/**
	//	 * @param numberOfDaysOld
	//	 * @param sourceDB
	//	 * @throws IOException
	//	 * @throws ParseException
	//	 */
	//	private static void createTempFile(String databaseName, int numberOfDaysOld,String sourceDB, String extension) throws Exception{
	//
	//		new File(tempPath).mkdir();
	//		if(CreateGenomeFile.currentTemporaryDataIsNOTRecent(numberOfDaysOld,tempPath, databaseName, today, extension))
	//		{
	//			long startTime = System.currentTimeMillis();
	//			Pair<Map<String,String>, Map<String,AbstractSequence<?>>> pair = CreateGenomeFile.getFastaAAGenomeFromEntrezProtein(sourceDB);
	//			Map<String, AbstractSequence<?>> sequences = pair.getPairValue();
	//			Map<String, String> locusTag = pair.getValue();
	//			long endTime = System.currentTimeMillis();
	//
	//			CreateGenomeFile.buildFastFile(databaseName, locusTag, sequences, extension);
	//			CreateGenomeFile.createLogFile(databaseName, extension);
	//			//return pair;
	//		}
	//	}

	/**
	 * @param locusTag
	 * @param sequences
	 * @throws IOException
	 */
	private static void buildFastFile(String databaseName, Long taxonomyID, Map<String, AbstractSequence<?>> sequences,
			Map<String,String[]> processedSequences, File fastaFile, FileExtensions extension) throws IOException{//, Map<String, String> locusTag) throws IOException{

//		File myFile = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxonomyID) + extension.getName());

		FileWriter fstream = new FileWriter(fastaFile);  
		BufferedWriter out = new BufferedWriter(fstream); 
		
		for(String key:sequences.keySet()) {

			if(!sequences.get(key).getOriginalHeader().contains("pseudo=true")){
				
				String newFileHeader = "";// key;
				String origianlHeader = sequences.get(key).getOriginalHeader();
				Map<String,String> seqData = getInformationFromFastasHeader(origianlHeader);

				if(extension.equals(FileExtensions.GENOMIC_FNA)){
					newFileHeader = origianlHeader;				
				}
				else{
					
					//Building sequence's header
					if(seqData.containsKey("protein_id")){
						newFileHeader = newFileHeader + seqData.get("protein_id");

						if(seqData.containsKey("locus_tag"))
							newFileHeader = newFileHeader + " | " + seqData.get("locus_tag");

						//IF WE CHOOSE TO PASS A MAP WITH THE SEQUENCES LOCUS_TAGS
						//					else if(locusTag!=null && locusTag.containsKey(seqData.get("protein_id")) && locusTag.get(seqData.get("protein_id"))!=null) 
						//						newFileHeader = newFileHeader + " | " + locusTag.get(seqData.get("protein_id"));

					}
					else if(seqData.containsKey("locus_tag")){
						newFileHeader =  newFileHeader + seqData.get("locus_tag");
					}
					else{
						newFileHeader = origianlHeader.split("\\s")[0];	//protein.faa header format : "<protein_id> <protein name> [<organism>]

						//IF WE CHOOSE TO PASS A MAP WITH THE SEQUENCES LOCUS_TAGS
						//					if(locusTag!=null && locusTag.containsKey(newFileHeader) && locusTag.get(newFileHeader)!=null)
						//						newFileHeader = newFileHeader + " | " + locusTag.get(origianlHeader.split("\\s")[0]);
					}
					////////////////////

					
					//Gene data to load into database//
					if(processedSequences!=null){
						String[] seqInfo = new String[4];

						//locusTag
						if(seqData.get("locus_tag")!=null && !seqData.get("locus_tag").isEmpty()){
							seqInfo[0] = seqData.get("locus_tag");
						}
						else
							seqInfo[0] = newFileHeader.split("\\|")[0].trim();		//to avoid locusTag as null
						
						//IF WE CHOOSE TO PASS A MAP WITH THE SEQUENCES LOCUS_TAGS
						//					else if(locusTag!=null && !locusTag.isEmpty()){														
						//						if(seqData.keySet().contains("protein_id") && locusTag.get(seqData.get("protein_id"))!=null 
						//								&& !locusTag.get(seqData.get("protein_id")).isEmpty()){
						//							seqInfo[0] = locusTag.get(seqData.get("protein_id")); 
						//						}
						//						else if(locusTag.containsKey(newFileHeader.split("\\|")[0].trim()) && locusTag.get(newFileHeader.split("\\|")[0].trim())!=null 
						//								&& !locusTag.get(newFileHeader.split("\\|")[0].trim()).isEmpty()){
						//							seqInfo[0] = locusTag.get(newFileHeader.split("\\|")[0].trim());
						//						}
						//					}
						/////

						seqInfo[1] = sequences.get(key).getSequenceAsString();					//Protein/DNA sequence
						seqInfo[2] = Integer.toString(sequences.get(key).getLength());			//Sequence Length

						if(seqData.get("gene")!=null && !seqData.get("gene").isEmpty())			//Gene Name
							seqInfo[3] = seqData.get("gene");
						else
							seqInfo[3]="";

						processedSequences.put(newFileHeader.split("\\|")[0].trim(), seqInfo);
					}
				}
				///////////////////

				//Write the sequence's header
				out.write(">"+newFileHeader+"\n");
				
				//Write the sequence
				out.write(sequences.get(key).getSequenceAsString()+"\n");

			}
		}
		out.close();
		fstream.close();
	}
	
	
	/**
	 * @param header
	 * @return
	 */
	public static Map<String,String> getInformationFromFastasHeader(String header){
		
		Map<String,String> sequenceData = new HashMap<>();
		
		for(String headerComp : Arrays.asList(header.split("\\s"))){
			
			if(headerComp.matches("\\[locus_tag=.+\\]")){
				
				sequenceData.put("locus_tag", headerComp.replaceAll("(\\[locus_tag=)*\\]*", ""));
			}
			
			else if(headerComp.matches("\\[gene=.+\\]")){

				sequenceData.put("gene", headerComp.replaceAll("(\\[gene=)*\\]*", ""));
			}
			
			else if(headerComp.matches("\\[product=.+\\]")){

				sequenceData.put("product", headerComp.replaceAll("(\\[product=)*\\]*", ""));
			}
			
			else if(headerComp.matches("\\[protein_id=.+\\]")){

				sequenceData.put("protein_id", headerComp.replaceAll("(\\[protein_id=)*\\]*", ""));
			}
		}
		
		return sequenceData;
	}
	
	/**
	 * @param path
	 * @param sequences
	 */
	public static void buildFastaFile(String path, Map<String, AbstractSequence<?>> sequences){
		
		try {
			File fastaFile = new File(path);
			
			FileWriter fstream = new FileWriter(fastaFile);  
			BufferedWriter out = new BufferedWriter(fstream); 

			for(String seqID : sequences.keySet()) {
				
				AbstractSequence<?> sequence = sequences.get(seqID);
				
				if(sequence.getOriginalHeader()==null || sequence.getOriginalHeader().isEmpty())
					out.write(">"+seqID+"\n");
				else
					out.write(">"+sequence.getOriginalHeader()+"\n");
				out.write(sequence.getSequenceAsString()+"\n");
				
			}
			out.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param path
	 * @param sequences
	 */
	public static void buildSubFastaFiles(String filesPath, Map<String, AbstractSequence<?>> allSequences, 
			List<Map<String,AbstractSequence<?>>> queriesSubSetList, List<String> queryFilesPaths, int numberOfFiles){
		
		Map<String, AbstractSequence<?>> queriesSubSet = new HashMap<>();
		
		int batch_size= allSequences.size()/numberOfFiles;
		
		String fastaFileName;
		
		int c=0;
		for (String query : allSequences.keySet()) {
			
			queriesSubSet.put(query, allSequences.get(query));

			if ((c+1)%batch_size==0 && ((c+1)/batch_size < numberOfFiles)) {
				
				fastaFileName = filesPath.concat("/SubFastaFile_").concat(Integer.toString((c+1)/batch_size)).concat("_of_").
						concat(Integer.toString(numberOfFiles)).concat(FileExtensions.PROTEIN_FAA.getExtension());
				
				CreateGenomeFile.buildFastaFile(fastaFileName, queriesSubSet);
				queryFilesPaths.add(fastaFileName);
				queriesSubSetList.add(queriesSubSet);
				
				queriesSubSet = new HashMap<>();
			}
			c++;
		}
		
		fastaFileName = filesPath.concat("/SubFastaFile_").concat(Integer.toString(numberOfFiles)).concat("_of_").
				concat(Integer.toString(numberOfFiles)).concat(FileExtensions.PROTEIN_FAA.getExtension());
		
		CreateGenomeFile.buildFastaFile(fastaFileName, queriesSubSet);
		queriesSubSetList.add(queriesSubSet);
		queryFilesPaths.add(fastaFileName);

	}
	
	

	/**
	 * @throws IOException
	 */
	private static void createLogFile(String databaseName, Long taxID, FileExtensions extension) throws IOException{

		String tempPath = FileUtils.getWorkspacesFolderPath();
		StringBuffer buffer = new StringBuffer();
		if(new File(tempPath+"genomes.log").exists()) {

			FileInputStream finstream = new FileInputStream(tempPath+"genomes.log");
			DataInputStream in = new DataInputStream(finstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			while ((strLine = br.readLine()) != null) {

				buffer.append(strLine+"\n");
			}
			br.close();
			in.close();
			finstream.close();
		}
		else
		{
			buffer.append("organismID\tdate\n");
		}

		new File(tempPath+"genomes.log");
		FileWriter fstream = new FileWriter(tempPath+"genomes.log");  
		BufferedWriter out = new BufferedWriter(fstream);
		out.append(buffer);
		out.write("genome_"+databaseName+"_"+taxID+"_"+extension.getName()+"\t"+today);
		out.close();
	}



	/**
	 * @param numberOfDaysOld
	 * @param databaseName
	 * @param taxID
	 * @param today
	 * @param proteinFaa
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private static boolean currentTemporaryDataIsNOTRecent(int numberOfDaysOld, String databaseName, Long taxID, String today, FileExtensions proteinFaa) throws ParseException, IOException{


		String tempPath = FileUtils.getWorkspacesFolderPath();

		if(numberOfDaysOld<0) {

			return true;
		}

		if(new File(tempPath+"genomes.log").exists()) {

			FileInputStream fstream = new FileInputStream(tempPath+"genomes.log");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String logFileDate = null;
			StringBuffer buffer = new StringBuffer();

			String strLine;
			while ((strLine = br.readLine()) != null) {

				String[] data = strLine.split("\t"); 

				if(data[0].equalsIgnoreCase("genome_"+databaseName+"_"+ taxID +"_"+proteinFaa.getName())) {

					logFileDate = data[1];
				}
				else {

					buffer.append(strLine+"\n");
				}
			}
			br.close();
			in.close();
			fstream.close();

			if(logFileDate!=null) {

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/dd");
				Date date1 = sdf.parse(today);
				Date date2 = sdf.parse(logFileDate.replace("\"", ""));

				if(numberOfDaysOld==0) {

					date2 = sdf.parse(today);
				}

				if(date1.after(date2) || date1.equals(date2)) {

					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.setTime(date1);
					cal2.setTime(date2);

					if(cal1.get(Calendar.YEAR)==cal2.get(Calendar.YEAR)) {

						int old = cal1.get(Calendar.DAY_OF_YEAR)-cal2.get(Calendar.DAY_OF_YEAR);

						if(old<=numberOfDaysOld || numberOfDaysOld<=0) {

							return false;
						}
					}
				}
				new File(tempPath+"genomes.log");
				FileWriter fWriterStream = new FileWriter(tempPath+"genomes.log");  
				BufferedWriter out = new BufferedWriter(fWriterStream);
				out.append(buffer);
				out.write("genome_"+databaseName+proteinFaa.getName()+"\t"+logFileDate);
				out.close();
			}
		}
		else {

			FileWriter fstream = new FileWriter(tempPath+"genomes.log");  
			BufferedWriter out = new BufferedWriter(fstream);  
			new File(tempPath+"genomes.log");
			out.write("organismID\tdate\n");
			out.close();
		}
		return true;
	}

	
	
	/**
	 * this method retrieve genes locus_tag by protein ids from genBank fasta file (genomic.gbff)
	 * 
	 * @param databaseName
	 * @param taxonomyID
	 * @param gbFile
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getLocusTagFromGenBankFastFile(File genBankFile) throws Exception {
//
		Map<String, String> locusTags = new HashMap<>();
		
		try {
			LinkedHashMap<String, DNASequence> genBankReader = GenbankReaderHelper.readGenbankDNASequence(genBankFile);

			for(DNASequence cds : genBankReader.values()) {
				for (FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound> cdsFeature : cds.getFeatures()) {
					if (cdsFeature.getType().equals("CDS")){   
						Map<String, List<Qualifier>> qualifiers = cdsFeature.getQualifiers();
						List<Qualifier> protein_id = qualifiers.get("protein_id");
						List<Qualifier> locus_tag = qualifiers.get("locus_tag");

						if(protein_id != null && locus_tag != null)
							locusTags.put(protein_id.get(0).getValue(), locus_tag.get(0).getValue());

					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
			
		return locusTags;
	}

}
