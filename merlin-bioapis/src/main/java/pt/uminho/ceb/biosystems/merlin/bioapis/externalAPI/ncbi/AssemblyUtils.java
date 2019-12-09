package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.DocumentSummary;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.DocumentSummarySet;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.ESearchResult;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.ESummaryResult;
import pt.uminho.ceb.biosystems.merlin.utilities.io.ConfFileReader;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;


/**
 * @author amaromorais
 *
 */
public class AssemblyUtils {
	
	
	/** Retrieves assembly information for a given taxonomyID
	 * @param taxonomyID
	 * @return eSummaryReport (GenBank and RefSeq accessions, GenBank and RefSeq ftps url,...)
	 */
	public static DocumentSummarySet getESummaryFromNCBI(String taxonomyID) {

		EntrezServiceFactory entrezServiceFactory = new EntrezServiceFactory("https://eutils.ncbi.nlm.nih.gov/entrez/eutils", false);
		EntrezService entrezService = entrezServiceFactory.build();

		ESearchResult eSearchResult = entrezService.eSearch(NcbiDatabases.assembly, taxonomyID +"[Taxonomy ID]", "xml", "1000");

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
						CreateGenomeFile.divideAndBuildRNAGenomicFastaFile(databaseName, Long.parseLong(ftpURLinfo.get(3)), rnaFile);
					}
				} 
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
