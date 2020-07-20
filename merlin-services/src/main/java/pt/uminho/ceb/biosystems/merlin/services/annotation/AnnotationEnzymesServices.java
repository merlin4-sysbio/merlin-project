package pt.uminho.ceb.biosystems.merlin.services.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pt.uminho.ceb.biosystems.merlin.core.containers.alignment.AlignmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.AnnotationEnzymes;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesRowInfo;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologySearchType;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologyStatus;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.InterproStatus;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;
import pt.uminho.ceb.biosystems.merlin.services.interpro.InterproServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelModuleServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelSubunitServices;
import pt.uminho.ceb.biosystems.merlin.utilities.DatabaseProgressStatus;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * @author ODias
 *
 */
/**
 * @author joses
 *
 */
public class AnnotationEnzymesServices {


	/**
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static List<Integer> getStats(String databaseName) throws Exception {

		List<Integer> result = new ArrayList<>();

		HomologySearchType h = AnnotationEnzymesServices.getHomologySearchType(databaseName);

		String program = null;
		if(h!=null ) {

			switch (h)
			{
			case BLASTX:
				program = ("%blastx%");
			case HMMER:
				program = ("%hmmer%");
			default:
				program = ("%blastp%");
			}
		}

		int num=0, noLocusTag=0, noQuery=0, noGene=0, no_similarity=0;

		List<String[]> data =InitDataAccess.getInstance().getDatabaseService(databaseName).getSpecificStats(program);

		for(int i=0; i<data.size(); i++){
			String[] list = data.get(i);

			num++;
			if(list[1]==null) noLocusTag++;
			if(list[2]==null) noQuery++;
			if(list[3]==null || list[3].isEmpty()) noGene++;
		}

		List<Integer> results = InitDataAccess.getInstance().getDatabaseService(databaseName).getAllFromGeneHomology(program);


		for(int i=0; i<results.size(); i++){
			no_similarity++;
			num++;
		}

		result.add(num);
		result.add(no_similarity);
		result.add(noLocusTag);
		result.add(noQuery);
		result.add(noGene);

		Long rs1 = InitDataAccess.getInstance().getDatabaseService(databaseName).getNumberOfHomologueGenes(program);

		result.add(rs1.intValue());

		double homologueAv = (rs1/(Double.valueOf(num)));

		result.add(Double.valueOf(homologueAv).intValue());

		List<String> list = InitDataAccess.getInstance().getDatabaseService(databaseName).getTaxonomy(program);

		int orgNum=0, eukaryota=0, bacteria=0, archea=0, virus=0, other=0;

		for(int i=0; i<list.size(); i++) {

			orgNum++;
			if(list.get(i)!=null) {

				if(list.get(i).startsWith("Eukaryota")) eukaryota++;
				if(list.get(i).startsWith("Bacteria")) bacteria++;
				if(list.get(i).startsWith("Archaea")) archea++;
				if(list.get(i).startsWith("Viruses")) virus++;
				if(list.get(i).startsWith("other sequences")) other++;
			}
		}

		result.add(orgNum);
		result.add(eukaryota);
		result.add(bacteria);
		result.add(archea);
		result.add(virus);
		result.add(other);


		return result;
	}

	public static Integer insertGeneHomologyEntry(String databaseName,String locusTag, String uniprotEcnumber, Boolean uniprotStar, int setupId, String query,
			DatabaseProgressStatus status, int seqId, String chromosome, String organelle) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertGeneHomologyEntry(locusTag, uniprotEcnumber, uniprotStar, setupId, query, status, seqId, chromosome, organelle);
	}

	public static void updateGeneHomologyEntry(String databaseName,String locusTag, String uniprotEcnumber, Boolean uniprotStar, int setupId, String query,
			DatabaseProgressStatus status, int seqId, String chromosome, String organelle, int sKey) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateGeneHomologyEntry(locusTag, uniprotEcnumber, uniprotStar, setupId, query, status, seqId, chromosome, organelle, sKey);
	}

	public static void updateGeneHomologyStatus(String databaseName, String locusTag, DatabaseProgressStatus status) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateGeneHomologyStatus(databaseName, locusTag, status);
	}

	public static Integer insertGeneHomologues(String databaseName,int organismSKey, String locusID, String definition,Float calculatedMw, String product,String organelle,Boolean uniprotStar) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertGeneHomologues(organismSKey, locusID, definition, calculatedMw, product, organelle, uniprotStar);

	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, String> getGeneHomologyQueriesBySKey(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getQueriesBySKey();

	}

	/**
	 * Method to save in cache the information about all enzymes in the database.
	 * 
	 * @param stmt
	 * @throws Exception 
	 * @throws SQLException
	 */
	public static AnnotationEnzymes getMainTableData(String databaseName, AnnotationEnzymes enzymesAnnotation) throws Exception{


		enzymesAnnotation.setBlastGeneDatabase(InitDataAccess.getInstance().getDatabaseService(databaseName).getGenesPerDatabase());

		DecimalFormatSymbols separator = new DecimalFormatSymbols();
		separator.setDecimalSeparator('.');
		enzymesAnnotation.setFormat(new DecimalFormat("##.##",separator));

		enzymesAnnotation.setMaxTaxRank(InitDataAccess.getInstance().getDatabaseService(databaseName).getMaxTaxRank().toString());

		//GENE |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		enzymesAnnotation.setGeneData(AnnotationEnzymesServices.getGeneInformation(databaseName, enzymesAnnotation));

		enzymesAnnotation.setQueries(getGeneHomologyQueriesBySKey(databaseName));

		//PROD |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		enzymesAnnotation.setDataFromProduct(InitDataAccess.getInstance().getDatabaseService(databaseName).getProductRank());

		enzymesAnnotation.setTaxRank(InitDataAccess.getInstance().getDatabaseService(databaseName).getTaxRank());

		enzymesAnnotation.setHomologuesCountProduct(InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologuesCountByProductRank());

		//EC |||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		enzymesAnnotation.setDataFromEcNumber(InitDataAccess.getInstance().getDatabaseService(databaseName).getDataFromecNumberRank());

		enzymesAnnotation.setEcRank(InitDataAccess.getInstance().getDatabaseService(databaseName).getEcRank());

		enzymesAnnotation.setHomologuesCountEcNumber(InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologuesCountByEcNumber());

		enzymesAnnotation.setHomologuesCountEcNumber(InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologuesCountByEcNumber());

		return enzymesAnnotation;
	}	

	/**
	 * @param row
	 * @return
	 * @throws Exception 
	 */
	public static AnnotationEnzymesRowInfo getRowInfo(String databaseName, int key) throws Exception {

		boolean interProAvailable = !InitDataAccess.getInstance().getDatabaseService(databaseName).getInterproAvailability(key).isEmpty();
		List<List<String>> resultsInterPro = null;
		if(interProAvailable)
			resultsInterPro = InitDataAccess.getInstance().getDatabaseService(databaseName).getInterProResult(key);

		List<List<String>> results = InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologyResults(key); 
		List<List<String>> resultsTaxonomy = InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologyTaxonomy(key);

		String sequence = InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologySequence(key);
		List<List<String>> setup = InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologySetup(key);

		AnnotationEnzymesRowInfo enzymesAnnotationRowInfo = new AnnotationEnzymesRowInfo(results, resultsTaxonomy, resultsInterPro, setup, interProAvailable, sequence); 

		return enzymesAnnotationRowInfo;

	}

	/**
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static HomologySearchType getHomologySearchType(String databaseName) throws Exception {

		HomologySearchType homologySearchType = null;
		List<String> data = new ArrayList<String>();
		List<String> res = InitDataAccess.getInstance().getDatabaseService(databaseName).getProgramFromHomologySetup("NO_SIMILARITY");
		if(res!=null)
			data.addAll(res);

		res = InitDataAccess.getInstance().getDatabaseService(databaseName).getProgramFromHomologySetup("PROCESSED");
		if(res!=null)
			data.addAll(res);


		if(data.size()>0) {
			String type = data.get(0); 

			if(type.equalsIgnoreCase("hmmer"))
				homologySearchType  = HomologySearchType.HMMER;

			if(type.equalsIgnoreCase("ncbi-blastp")
					|| type.equalsIgnoreCase("blastp")
					|| type.equalsIgnoreCase("ebi-blastp")) {

				homologySearchType = HomologySearchType.BLASTP;					
			}

			if(type.equalsIgnoreCase("ncbi-blastx")
					|| type.equalsIgnoreCase("blastx")
					|| type.equalsIgnoreCase("ebi-blastx")) {

				homologySearchType  = HomologySearchType.BLASTX;
			}
		}

		return homologySearchType;
	}


	/**
	 * @throws Exception 
	 * 
	 */
	public static void initializeScorerConfig(String databaseName, Float threshold, Float upperThreshold, Float alpha, Float beta, int minimumNumberofHits) throws Exception {

		String[] databases = InitDataAccess.getInstance().getDatabaseService(databaseName).getBlastDatabases(null);

		for(String database : databases)
			if(!database.equalsIgnoreCase("all databases"))
				AnnotationEnzymesServices.saveDatabaseStatus(databaseName, database, threshold, upperThreshold, alpha, beta, minimumNumberofHits);


	}


	/**
	 * @param locus
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static String getQueryFromLocus(String databaseName, String locus) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).geneHomologyHasHomologues(locus);
	}

	/**
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static int countEntriesInGeneHomology(String databaseName) throws Exception {

		return (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countEntriesInGeneHomology();
	}


	/**
	 * @param query
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static List<String[]> getECData(String databaseName, String query) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEnzymesAnnotationEcNumberrankAttributesByLocusTag(query);

	}


	/**
	 * @param query
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static List<String[]> getProductData(String databaseName, String query) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getProductRankData(query);

	}


	/**
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, List<String>> getUniprotECnumbers(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getUniprotEcNumbers();
	}

	/**
	 * @param stmt
	 * @return
	 * @throws Exception 
	 */
	public static Map<Integer,List<Object>> getGeneInformation(String databaseName, AnnotationEnzymes enzymesAnnotation) throws Exception{

		enzymesAnnotation.setInitialOriginalLocus(new HashMap<>());
		enzymesAnnotation.setInitialOriginalNames(new HashMap<>());
		enzymesAnnotation.setIdentifiers(new HashMap<>());
		enzymesAnnotation.setGeneDataEntries(new HashMap<>());

		TreeMap<Integer,List<Object>> geneData = new TreeMap<Integer,List<Object>>();

		List<String[]> result = InitDataAccess.getInstance().getDatabaseService(databaseName).getGenesInformation();

		int tableIndex = 0;

		if(result!=null) {
			for (int i = 0; i<result.size(); i++) {

				String[] list = result.get(i);

				List<Object> ql = new ArrayList<Object>();
				ql.add("");

				ql.add(list[1]);
				ql.add(list[5]);
				ql.add(list[2]);

				geneData.put(tableIndex, ql);

				enzymesAnnotation.getIdentifiers().put(tableIndex, Integer.parseInt(list[0]));//genes list
				enzymesAnnotation.getGeneDataEntries().put(tableIndex, list[7]);
				enzymesAnnotation.getInitialOriginalLocus().put(tableIndex, list[1]);
				enzymesAnnotation.getInitialOriginalNames().put(tableIndex, list[2]);

				tableIndex++;
			}
		}
		return geneData;
	}

	/**
	 * Get previously committed scorer parameter values
	 * 
	 * @param enzymeAnnotation
	 * @param blastDatabase
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static AnnotationEnzymes getCommitedScorerData(String databaseName, AnnotationEnzymes enzymeAnnotation, String blastDatabase) throws Exception {

		enzymeAnnotation.setCommittedThreshold(-1);
		enzymeAnnotation.setCommittedUpperThreshold(-1);
		enzymeAnnotation.setCommittedBalanceBH(-1);
		enzymeAnnotation.setCommittedAlpha(-1);
		enzymeAnnotation.setCommittedBeta(-1);
		enzymeAnnotation.setCommittedMinHomologies(-1);

		List<String> result = InitDataAccess.getInstance().getDatabaseService(databaseName).getCommitedScorerData(blastDatabase);


		for(int i=0; i<result.size(); i++){

			enzymeAnnotation.setCommittedThreshold(Double.parseDouble(result.get(0)));
			enzymeAnnotation.setCommittedUpperThreshold(Double.parseDouble(result.get(1)));
			enzymeAnnotation.setCommittedBalanceBH(Double.parseDouble(result.get(2)));
			enzymeAnnotation.setCommittedAlpha(Double.parseDouble(result.get(3)));
			enzymeAnnotation.setCommittedBeta(Double.parseDouble(result.get(4)));
			enzymeAnnotation.setCommittedMinHomologies(Integer.parseInt(result.get(5)));
		}

		enzymeAnnotation.setHasCommittedData(false);

		if(checkCommitedData(databaseName))
			enzymeAnnotation.setHasCommittedData(true);

		return enzymeAnnotation;
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static boolean checkCommitedData(String databaseName) throws Exception {
		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkCommitedData();
	}

	/**
	 * @param databaseName
	 * @param blastDatabase
	 * @return
	 * @throws Exception
	 */
	public static void setBestAlphaFound(String databaseName, String blastDatabase) throws Exception {
		InitDataAccess.getInstance().getDatabaseService(databaseName).setBestAlphaFound(blastDatabase);
	}

	/**
	 * Get previously committed Homology Data
	 * 
	 * @param enzymeAnnotation
	 * @param connection
	 * @return
	 * @throws Exception 
	 */
	public static AnnotationEnzymes getCommittedHomologyData(String databaseName, AnnotationEnzymes enzymeAnnotation) throws Exception {

		enzymeAnnotation.setCommittedLocusList(new TreeMap<Integer, String>());
		enzymeAnnotation.setCommittedNamesList(new TreeMap<Integer, String>());
		enzymeAnnotation.setCommittedProdItem(new TreeMap<Integer, String>());
		enzymeAnnotation.setCommittedEcItem(new TreeMap<Integer, String>());
		enzymeAnnotation.setCommittedChromosome(new TreeMap<Integer, String>());
		enzymeAnnotation.setCommittedNotesMap(new TreeMap<Integer, String>());
		enzymeAnnotation.setCommittedSelected(new TreeMap<Integer, Boolean>());
		enzymeAnnotation.setCommittedProductList(new TreeMap<Integer, String[]>());
		enzymeAnnotation.setCommittedEnzymeList(new TreeMap<Integer, String[]>());

		List<String[]> result = InitDataAccess.getInstance().getDatabaseService(databaseName).getCommittedHomologyData();

		for(int i=0; i<result.size(); i++){

			String[] list = result.get(i);


			if(enzymeAnnotation.getTableRowIndex().containsKey(Integer.parseInt(list[1]))) {

				int row = enzymeAnnotation.getTableRowIndex().get(Integer.parseInt(list[1]));

				if(list[2] != null && !list[2].equalsIgnoreCase("null") && !list[2].isEmpty())
					enzymeAnnotation.getCommittedLocusList().put(row, list[2]);

				if(list[3] != null && !list[3].equalsIgnoreCase("null") && !list[3].isEmpty())
					enzymeAnnotation.getCommittedNamesList().put(row, list[3]);

				if(list[4] != null && !list[4].equalsIgnoreCase("null"))
					enzymeAnnotation.getCommittedProdItem().put(row, list[4]);

				if(list[5] != null && !list[5].equalsIgnoreCase("null"))
					enzymeAnnotation.getCommittedEcItem().put(row, list[5]);

				if(list[6] != null && !list[6].equalsIgnoreCase("null") && !list[6].isEmpty())
					enzymeAnnotation.getCommittedSelected().put(row, Boolean.valueOf(list[6]));

				if(list[7] != null && !list[7].equalsIgnoreCase("null") && !list[7].isEmpty())
					enzymeAnnotation.getCommittedChromosome().put(row, list[7]);

				if(list[8] != null && !list[8].equalsIgnoreCase("null") && !list[8].isEmpty())
					enzymeAnnotation.getCommittedNotesMap().put(row, list[8]);
			}
		}

		Set<String> dataSet = new HashSet<String>();
		int dataKey=-1;

		result = InitDataAccess.getInstance().getDatabaseService(databaseName).getCommittedHomologyData2();

		for(int i=0; i<result.size(); i++){

			String[] list = result.get(i);
			if(dataSet.isEmpty()) { 

				dataKey = enzymeAnnotation.getTableRowIndex().get(Integer.parseInt(list[0]));
				dataSet.add(list[1]);
			}
			else {
				if (Integer.parseInt(list[0])==dataKey) {
					dataSet.add(list[1]);
				}
				else {
					enzymeAnnotation.getCommittedProductList().put(dataKey,dataSet.toArray(new String[dataSet.size()]));
					dataKey = enzymeAnnotation.getTableRowIndex().get(Integer.parseInt(list[0]));
					dataSet = new HashSet<String>();
					dataSet.add(list[1]);
				}

				if(i == (result.size()-1)) 
					enzymeAnnotation.getCommittedProductList().put(dataKey,dataSet.toArray(new String[dataSet.size()]));
			}
		}

		dataSet = new HashSet<>();
		dataKey = -1;

		result = InitDataAccess.getInstance().getDatabaseService(databaseName).getCommittedHomologyData3();

		for(int i=0; i<result.size(); i++){

			String[] list = result.get(i);

			if(dataSet.isEmpty()) { 

				dataKey = enzymeAnnotation.getTableRowIndex().get(Integer.parseInt(list[0]));
				dataSet.add(list[1]);
			}
			else {

				if(list[0] != null ) {
					if(Integer.parseInt(list[0]) == dataKey) {

						dataSet.add(list[1]);
					}
					else {

						enzymeAnnotation.getCommittedEnzymeList().put(dataKey, dataSet.toArray(new String[dataSet.size()]));

						dataKey = enzymeAnnotation.getTableRowIndex().get(Integer.parseInt(list[0]));
						dataSet = new HashSet<String>();
						dataSet.add(list[1]);
					}

					if(i == (result.size()-1)) 
						enzymeAnnotation.getCommittedEnzymeList().put(dataKey,dataSet.toArray(new String[dataSet.size()]));

				}
			}
		}
		return enzymeAnnotation;
	}

	/**
	 * Save user changes to the local database
	 * @throws Exception 
	 * 
	 */
	public static void commitToDatabase(String databaseName, String blastDatabase, AnnotationEnzymes annotationEnzymes) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).commitToDatabase(blastDatabase, (float) annotationEnzymes.getThreshold().doubleValue(), annotationEnzymes.getEditedEnzymeData(),
				annotationEnzymes.getEnzymesList(), annotationEnzymes.getEditedProductData(), annotationEnzymes.getProductList(), annotationEnzymes.getNamesList(),
				annotationEnzymes.getNotesMap(), annotationEnzymes.getLocusList(), (float) annotationEnzymes.getUpperThreshold().doubleValue(),
				(float) annotationEnzymes.getAlpha().doubleValue(),(float) annotationEnzymes.getBeta().doubleValue(), annotationEnzymes.getMinimumNumberofHits());
	}


	/**
	 * @param stament
	 * @param blastDatabase
	 * @param threshold
	 * @param upperThreshold
	 * @param alpha
	 * @param beta
	 * @param minHits
	 * @throws SQLException
	 */
	public static void saveDatabaseStatus(String databaseName, String blastDatabase, Float threshold, Float upperThreshold, Float alpha, Float beta, Integer minHits) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).saveDatabaseStatus(blastDatabase, threshold, upperThreshold, alpha, beta, minHits);
	}

	/**
	 * Get genes with InterPro entries.
	 * 
	 * @return
	 */
	public static List<String> getInterProGenes(String databaseName) {

		List<String> interProGenes = null;

		try {

			interProGenes = InterproServices.getInterProGenes(databaseName, InterproStatus.PROCESSED.toString());
		}
		catch (Exception e) {

			e.printStackTrace();
		}

		return interProGenes;
	}

	/**
	 * Retrieve mapping of queries to database ids.
	 * 
	 * @param statement
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, Integer> getQueryKeys(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getQueries();
	}

	/**
	 * Get last database used in the last "session"
	 * 
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	public static String getLastestUsedBlastDatabase(String databaseName) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getLastestUsedBlastDatabase();

	}

	/**
	 * Get last database used in the last "session"
	 * 
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getBestAlphasFound(String databaseName) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).bestAlphasFound(true);

	}

	/**
	 * @param databaseName
	 * @param organism
	 * @return
	 * @throws Exception
	 */
	public static int getSKeyFromOrganism(String databaseName, String organism) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getSKeyFromOrganism(organism);

	}

	/**
	 * @param databaseName
	 * @param organism
	 * @return
	 * @throws Exception
	 */
	public static int getecNumberSkey(String databaseName, String ecNumber) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getecNumberSkey(ecNumber);

	}

	/**
	 * @param databaseName
	 * @param organism
	 * @return
	 * @throws Exception
	 */
	public static int insertEcNumberEntry(String databaseName, String ecNumber) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertEcNumberEntry(ecNumber);

	}


	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static String getEbiBlastDatabase(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEbiBlastDatabase();

	}


	/**
	 * @param databasename
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String getHmmerDatabase(String databasename) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databasename).getHmmerDatabase();
	}

	public static Integer insertEcNumberRank(String databaseName, Integer geneHomology_s_key, String concatEC, Integer ecnumber) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertEcNumberRank(geneHomology_s_key, concatEC, ecnumber);
	}

	public static Boolean FindEnzymesAnnotationEcNumberRankHasOrganismByIds(String databaseName, Integer sKey, Integer orgKey) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).FindEnzymesAnnotationEcNumberRankHasOrganismByIds(sKey, orgKey);
	}

	public static void InsertEnzymesAnnotationEcNumberRankHasOrganism(String databaseName, Integer sKey, Integer orgKey) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).InsertEnzymesAnnotationEcNumberRankHasOrganism(sKey, orgKey);

	}

	public static int getEcNumberRankSkey(String databaseName, int geneHomology_s_key, String concatEC, int ecnumber) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEcNumberRankSkey(geneHomology_s_key, concatEC, ecnumber);

	}

	public static Integer getGeneHomologySkey(String databaseName, String query, Integer homologySetupID) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneHomologySkey(query, homologySetupID);

	}

	public static Integer insertEnzymesAnnotationOrganism(String databaseName, String organism, String taxonomy, Integer taxrank) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertEnzymesAnnotationOrganism(organism, taxonomy, taxrank);

	}

	public static Integer getHomologuesSkey(String databaseName, String referenceID) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologuesSkey(referenceID);

	}

	public static boolean checkHomologuesHasEcNumber(String databaseName, int homologues_s_key, int ecnumber_s_key) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkHomologuesHasEcNumber(homologues_s_key, ecnumber_s_key);
	}

	public static void insertEnzymesAnnotationHomologuesHasEcNumber(String databaseName, int homologues_s_key, int ecnumber_s_key) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertEnzymesAnnotationHomologuesHasEcNumber(homologues_s_key, ecnumber_s_key);

	}


	public static int getHomologySetupSkeyByAttributes(String databaseName, String databaseID, String program, double eVal, Float lowerIdentity, Float positives, Float queryCoverage, Float targetCoverage, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologySetupSkeyByAttributes(databaseID, program, eVal, lowerIdentity, positives, queryCoverage, targetCoverage,
				matrix, wordSize, gapCosts, maxNumberOfAlignments, version);

	}

	public static Integer insertHomologySetup(String databaseName, String databaseID, String program, double eVal, Float lowerIdentity, Float positives, Float queryCoverage, Float targetCoverage, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertHomologySetup(databaseID, program, eVal, lowerIdentity, positives, queryCoverage, targetCoverage,
				matrix, wordSize, gapCosts, maxNumberOfAlignments, version);
	}

	public static int getHomologySetupSkeyByAttributes2(String databaseName, String databaseID, String program, double eVal,  int maxNumberOfAlignments, String version) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologySetupSkeyByAttributes2(databaseID, program, eVal, maxNumberOfAlignments, version);

	}

	public static Integer insertHomologySetup2(String databaseName, String databaseID, String program, double eVal, int maxNumberOfAlignments, String version) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertHomologySetup2(databaseID, program, eVal, maxNumberOfAlignments, version);
	}

	public static boolean loadGeneHomologyData(String databaseName, String homologyDataClient, String program) throws Exception{
		return InitDataAccess.getInstance().getDatabaseService(databaseName).loadGeneHomologyData(homologyDataClient, program);
	}

	public static void removeModelGeneHomologyBySKey(String databaseName, Integer skey) throws Exception{
		InitDataAccess.getInstance().getDatabaseService(databaseName).removeGeneHomologyBySKey(skey);
	}

	public static String[] getAllOrganisms(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllOrganisms();
	}

	public static String[] getAllGenus(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllGenus();
	}

	public static Double getBlastEValue(String databaseName, String blastDatabase) throws Exception {

		if(blastDatabase != null && blastDatabase.isEmpty())
			blastDatabase = null;

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getBlastEValue(blastDatabase);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<String> getDuplicatedQuerys(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getDuplicatedQuerys();
	}

	/**
	 * @param databaseName
	 * @return
	 */
	public static boolean removeDuplicates(String databaseName) {

		boolean result = false;
		Set<String> duplicateQueries = new HashSet<String>();

		try {

			List<String> querys = getDuplicatedQuerys(databaseName);

			for(int i=0; i < querys.size(); i++){

				duplicateQueries.add(querys.get(i));
				result = true;
			}

			for(String query : duplicateQueries)				
				removeModelGeneHomologyByQuery(databaseName, query);
		}
		catch (Exception e) {

			result = false;
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param databaseName
	 * @param query
	 * @throws Exception
	 */
	public static void removeModelGeneHomologyByQuery(String databaseName, String query) throws Exception{
		InitDataAccess.getInstance().getDatabaseService(databaseName).removeGeneHomologyByQuery(query);
	}

	/**
	 * Deletes the configurations of a sprecific database. merlin will automatically
	 * provide new standard configurations.
	 * 
	 * @param databaseName
	 * @param query
	 * @throws Exception
	 */
	public static void resetDatabaseScorer(String databaseName, String blastDatabase) throws Exception{
		InitDataAccess.getInstance().getDatabaseService(databaseName).resetDatabaseScorer(blastDatabase);
	}

	/**
	 * Resets the configurations of all databases by deleting them. merlin will
	 * automatically provide new standard configurations.
	 * 
	 * @param databaseName
	 * @throws Exception
	 */
	public static void resetAllScorers(String databaseName) throws Exception{
		InitDataAccess.getInstance().getDatabaseService(databaseName).resetAllScorers();
	}

	/**
	 * @param databaseName
	 * @param latest
	 * @throws Exception
	 */
	public static void updateScorerConfigSetLatest(String databaseName, boolean latest) throws Exception{
		InitDataAccess.getInstance().getDatabaseService(databaseName).updateScorerConfigSetLatest(latest);
	}

	/**
	 * @param databaseName
	 * @param latest
	 * @param blastDatabase
	 * @throws Exception
	 */
	public static void updateScorerConfigSetLatestByBlastDatabase(String databaseName, boolean latest, String blastDatabase) throws Exception{
		InitDataAccess.getInstance().getDatabaseService(databaseName).updateScorerConfigSetLatestByBlastDatabase(latest, blastDatabase);
	}

	/**
	 * @param databaseName
	 * @param deleteGenes
	 * @throws Exception
	 */
	public static void deleteSetOfGenes(String databaseName, Set<Integer> deleteGenes) throws Exception {

		for (Integer skey : deleteGenes)
			removeModelGeneHomologyBySKey(databaseName, skey);
	}

	/**
	 * @param databaseName
	 * @param status
	 * @param program
	 * @return
	 * @throws Exception
	 */
	public static Set<Integer> getHomologyGenesSKeyByStatus(String databaseName, HomologyStatus status, String program) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologyGenesSKeyByStatus(status, program);
	}

	/**
	 * @param databaseName
	 * @param status
	 * @param program
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getHomologyGenesQueryByStatus(String databaseName, HomologyStatus status, String program) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologyGenesQueryByStatus(status, program);
	}

	/**
	 * @param databaseName
	 * @param status
	 * @param program
	 * @param evalue
	 * @param matrix
	 * @param wordSize
	 * @param maxNumberOfAlignments
	 * @return
	 * @throws Exception
	 */
	public static Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByAttributes(String databaseName, HomologyStatus status,
			String program, double evalue, String matrix, String wordSize, Integer maxNumberOfAlignments) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologyGenesSKeyAndQueryByAttributes(status,
				program, evalue, matrix, wordSize, maxNumberOfAlignments);
	}

	/**
	 * @param databaseName
	 * @param status
	 * @param program
	 * @param databaseId
	 * @return
	 * @throws Exception
	 */
	public static Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByStatusAndProgramAndDatabaseId(String databaseName, HomologyStatus status,
			String program, String databaseId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getHomologyGenesSKeyAndQueryByStatusAndProgramAndDatabaseId(status, program, databaseId);
	}

	/**
	 * Retrieve genes available in homology database.
	 * 
	 * @param program
	 * @param databaseID
	 * @param deleteProcessing
	 * @param statement
	 * @return
	 * @throws Exception 
	 */
	public static Set<String> getGenesFromDatabase(String databaseName, String program, boolean deleteProcessing) throws Exception {

		Set<String> loadedGenes = new HashSet<String>();

		Set<Integer> deleteGenes = new HashSet<>();

		// get processing genes
		deleteGenes.addAll(getHomologyGenesSKeyByStatus(databaseName, HomologyStatus.PROCESSING, program));

		// get processed genes
		loadedGenes.addAll(getHomologyGenesQueryByStatus(databaseName, HomologyStatus.PROCESSED, program));

		// get NO_SIMILARITY genes
		loadedGenes.addAll(getHomologyGenesQueryByStatus(databaseName, HomologyStatus.NO_SIMILARITY, program));

		deleteSetOfGenes(databaseName, deleteGenes);

		return loadedGenes;
	}

	/**
	 * Retrieve genes available in homology database.
	 * 
	 * @param eVal
	 * @param matrix
	 * @param numberOfAlignments
	 * @param word
	 * @param program
	 * @param databaseID
	 * @param deleteProcessing
	 * @param statement
	 * @return
	 * @throws Exception 
	 */
	public static Set<String> getGenesFromDatabase(String databaseName, double eVal, String matrix, int numberOfAlignments, short word,
			String program, String databaseID, boolean deleteProcessing) throws Exception {

		Set<String> loadedGenes = new HashSet<String>();
		Set<Integer> deleteGenes = new HashSet<>();

		// get processing genes
		deleteGenes.addAll(getHomologyGenesSKeyByStatus(databaseName, HomologyStatus.PROCESSING, program));

		// get processed genes
		loadedGenes.addAll(getHomologyGenesQueryByStatus(databaseName, HomologyStatus.PROCESSED, program));

		// get NO_SIMILARITY genes
		loadedGenes.addAll(getHomologyGenesQueryByStatus(databaseName, HomologyStatus.NO_SIMILARITY, program));

		Pair<Set<Integer>, Set<String>> pair = getHomologyGenesSKeyAndQueryByAttributes(databaseName, HomologyStatus.NO_SIMILARITY, program, eVal, matrix, Short.toString(word), numberOfAlignments);

		// get NO_SIMILARITY genes and delete if new eVal > setup eVal
		loadedGenes.removeAll(pair.getB());
		deleteGenes.addAll(pair.getA());

		// get NO_SIMILARITY genes and delete if new database <> setup database
		pair = getHomologyGenesSKeyAndQueryByStatusAndProgramAndDatabaseId(databaseName, HomologyStatus.NO_SIMILARITY, program, databaseID);

		loadedGenes.removeAll(pair.getB());
		deleteGenes.addAll(pair.getA());

		deleteSetOfGenes(databaseName, deleteGenes);

		return loadedGenes;
	}

	/**
	 * Loads Orthologs data
	 * 
	 * @param capsule
	 * @param geneIds
	 * @param statement
	 * @throws Exception 
	 */
	public static void loadOrthologsInfo(String databaseName, AlignmentContainer capsule, Map<String, Integer> geneIds)
			throws Exception {

		String orthologLocus = capsule.getQuery().split(":")[1];

		double score = capsule.getAlignmentScore();

		String ecnumber = capsule.getEcNumber();

		Map<String, Set<Integer>> modules = capsule.getModules();

		Map<String, Set<String>> closestOrthologs = capsule.getClosestOrthologues();

		String sequenceID = capsule.getTarget();

		int idGene = geneIds.get(sequenceID);

		for (String ortholog : closestOrthologs.get(capsule.getQuery())) {

			Integer orthology_id = ModelGenesServices.getOrthologyIdByEntryIdWherwLocusIdIsNullOrEmpty(databaseName, ortholog);

			if (orthology_id != null) {
				ModelGenesServices.updateOrthologyLocusIdByEntryId(databaseName, ortholog, orthologLocus);
			}

			else {

				orthology_id = ModelGenesServices.getOrthologyIdByEntryIdAndLocus(databaseName, ortholog, orthologLocus);

				if (orthology_id == null) {
					orthology_id = ModelGenesServices.insertNewOrthologue(databaseName, ortholog, orthologLocus);
				}
			}

			boolean exists = ModelGenesServices.checkGeneHasOrthologyEntries(databaseName, idGene, orthology_id);

			if (!exists)
				ModelGenesServices.insertModelGeneHasOrthology(databaseName, idGene, orthology_id, score);

			ProteinContainer proteinContainer = ModelProteinsServices.getProteinByEcNumber(databaseName, ecnumber);

			int protein_idprotein = proteinContainer.getIdProtein();

			Map<Integer, String> subunitModulesNotes = ModelSubunitServices.getModuleIdAndNoteByGeneIdAndProteinId(databaseName, idGene, protein_idprotein);

			List<Integer> modules_ids = new ArrayList<>();
			exists = false;
			boolean	noModules = true;

			String note = "unannotated";

			for(Integer moduleId : subunitModulesNotes.keySet()) {

				exists = true;

				String moduleNote = subunitModulesNotes.get(moduleId);

				if (moduleId > 0) {

					noModules = false;
					modules_ids.add(moduleId);
				}

				if (moduleNote != null && !moduleNote.equalsIgnoreCase("null"))
					note = moduleNote;
				else
					note = "";
			}



			if (modules != null) {

				for (int module_id : modules.get(ortholog)) { 	


					boolean moduleIdProteinId = ModelModuleServices.checkModelModuletHasProteinData(databaseName, protein_idprotein, module_id);
					boolean geneIdProteinId = ModelSubunitServices.checkModelSubunitEntry(databaseName, idGene, protein_idprotein);
					
					if(geneIdProteinId && moduleIdProteinId == false) {

						if (modules_ids.contains(module_id)) {

							if (exists) {

								if (noModules) {

									ModelModuleServices.insertModelModuleHasModelProtein(databaseName, protein_idprotein, module_id);
									noModules = false;
									modules_ids.add(module_id);

								}
							} 
							else {

								ModelSubunitServices.insertModelSubunit(databaseName, idGene, protein_idprotein, note, null);
							}

						}

						else {

							ModelModuleServices.insertModelModuleHasModelProtein(databaseName, protein_idprotein, module_id);

						}
					}
				}
			}
		}
	}

	/**
	 * @param databaseName
	 * @param databaseId
	 * @throws Exception
	 */
	public static void deleteFromHomologyDataByDatabaseID(String databaseName, String databaseId) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).deleteFromHomologyDataByDatabaseID(databaseId);
	}

	/**
	 * @param databaseName
	 * @param geneHomologySKey
	 * @param locusTag
	 * @param geneName
	 * @param product
	 * @param ecnumber
	 * @param selected
	 * @param chromossome
	 * @param notes
	 * @return
	 * @throws Exception
	 */
	public static Integer insertHomologyData(String databaseName, Integer geneHomologySKey, String locusTag, String geneName,
			String product, String ecnumber, Boolean selected, String chromossome, String notes) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertHomologyData(geneHomologySKey, locusTag, geneName, product, ecnumber, selected, chromossome, notes);
	}

	/**
	 * @param databaseName
	 * @param homologySKey
	 * @return
	 * @throws Exception
	 */
	public static Set<Integer> getSKeyForAutomaticAnnotation(String databaseName, String blastDatabase) throws Exception {

		if(blastDatabase != null && blastDatabase.isEmpty())
			blastDatabase = null;

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getSKeyForAutomaticAnnotation(blastDatabase);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getAllBlastDatabases(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllBlastDatabases();
	}

	/**
	 * Method to return the databases used to perform the blast.
	 * 
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static String[] getBlastDatabasesAsArrayIncludingEmpty(String databaseName) throws Exception {


		List<String> res = new ArrayList<String>(getAllBlastDatabases(databaseName));

		String[] databases = new String[res.size()+1];

		for(int i = 1; i < res.size()+1; i++){
			databases[i] = res.get(i-1);
		}

		databases[0] = "all databases";

		return databases;
	}
	
	/**
	 * @param databaseName
	 * @return
	 * @throws Exception 
	 */
	public static boolean deleteGenesWithoutECRankAndProdRankInfoComplete(String databaseName) throws Exception {
		
		return InitDataAccess.getInstance().getDatabaseService(databaseName).deleteGenesWithoutECRankAndProdRank();
	}
}
