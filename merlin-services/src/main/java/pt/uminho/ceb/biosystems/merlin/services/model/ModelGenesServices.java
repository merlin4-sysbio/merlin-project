package pt.uminho.ceb.biosystems.merlin.services.model;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * @author odias
 *
 */
public class ModelGenesServices {


	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public static double[] getStats(String databaseName) throws Exception {

		double[] ret = new double[9];

		try {

			int position = 0;
			int num = countEntriesInGene(databaseName);

			//			stmt = connection.createStatement();

			ret[position++] = num;
			ret[position++] = num - countGenesWithName(databaseName);

			int snumgenes = (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countGenesSynonyms("g");
			ret[position++] = snumgenes;

			double synmed = 0;
			if(num>0)
				//				synmed = snumgenes/Double.parseDouble(String.valueOf(num));
				synmed = snumgenes/num;

			NumberFormat formatter = NumberFormat.getNumberInstance();
			formatter.setMaximumFractionDigits(5);

			String synmedFormatted = formatter.format(synmed);
			ret[position++] = Double.parseDouble(synmedFormatted);

			int prot = countGenesEncodingProteins(databaseName);
			ret[position++] = prot;

			Pair<Integer, Integer> pair = InitDataAccess.getInstance().getDatabaseService(databaseName).countGenesEncodingEnzymesAndTransporters();
			int enz = pair.getA();
			int trp = pair.getB();
			int both = enz + trp - prot;

			ret[position++] = (enz-both);
			ret[position++] = (trp-both);
			ret[position++] = both;

			int inModel= countGenesInModel(databaseName);
			ret[position++] = inModel;


		}
		catch (Exception ex) {

			ex.printStackTrace();
		}
		return ret;
	}

	/**
	 * @param id
	 * @param connection
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static Map<String,List<List<String>>> getRowInfo(String databaseName, int id) throws Exception {

		Map<String,List<List<String>>> resultsLists = new HashMap<>();

		List<String> result =  InitDataAccess.getInstance().getDatabaseService(databaseName).getAliasClassG(id);

		List<List<String>> resultLists = new ArrayList<List<String>>();

		for(int i=0; i<result.size(); i++){

			ArrayList<String> resultsList = new ArrayList<String>();
			resultsList.add(result.get(i));
			resultLists.add(resultsList);
		}
		resultsLists.put("synonyms",resultLists);

		//			resultLists = new ArrayList<List<String>>();
		//
		//			result = ProjectAPI.getProteinIDs(id, stmt);
		//
		//			for(int i=0; i<result.size(); i++){
		//
		//				ArrayList<String> resultsList = new ArrayList<String>();
		//				resultsList.add(result.get(i));
		//				resultLists.add(resultsList);
		//			}
		//			resultsLists.put("regulations",resultLists);

		resultLists = new ArrayList<List<String>>();

		List<ProteinContainer> subunitData = InitDataAccess.getInstance().getDatabaseService(databaseName).getDataFromSubunit(id);

		for(ProteinContainer item : subunitData){

			ArrayList<String> resultsList = new ArrayList<String>();

			resultsList.add(item.getName());
			resultsList.add(item.getClass_());
			resultsList.add(item.getExternalIdentifier());
			resultLists.add(resultsList);
		}
		resultsLists.put("encoded proteins",resultLists);

		resultLists = new ArrayList<List<String>>();

		List<String[]> data = InitDataAccess.getInstance().getDatabaseService(databaseName).getDataFromGeneHasOrthology(id);

		for(int i=0; i<data.size(); i++){
			String[] list = data.get(i);

			ArrayList<String> resultsList = new ArrayList<String>();
			resultsList.add(list[0]);
			resultsList.add(list[1]);
			resultsList.add(list[2]);
			resultLists.add(resultsList);
		}
		resultsLists.put("orthologs",resultLists);

		resultLists = new ArrayList<List<String>>();
		data = InitDataAccess.getInstance().getDatabaseService(databaseName).getDataFromGene(id);
		for(int i=0; i<data.size(); i++){

			String[] list = data.get(i);

			ArrayList<String> ql = new ArrayList<String>();
			ql.add(list[1]);
			ql.add(list[3]);

			if(Boolean.valueOf(list[2]))
				ql.add(list[2]);
			else
				ql.add("");

			resultLists.add(ql);
		}
		resultsLists.put("compartments",resultLists);

		resultLists = new ArrayList<List<String>>();

		List<GeneContainer> containers = InitDataAccess.getInstance().getDatabaseService(databaseName).getSequenceByGeneId(id);

		for(GeneContainer gene : containers){

			ArrayList<String> ql = new ArrayList<String>();
			if(gene.getSequenceType() != null)
				ql.add(gene.getSequenceType());
			else
				ql.add("");

			if(gene.getAasequence() != null && !gene.getAasequence().isEmpty())
				ql.add(gene.getAasequence());
			else if(gene.getNtsequence() != null && !gene.getNtsequence().isEmpty())
				ql.add(gene.getNtsequence());
			else
				ql.add("");

			resultLists.add(ql);
		}
		resultsLists.put("sequence",resultLists);

		resultLists = new ArrayList<List<String>>();

		return resultsLists;
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static int countGenesInModel(String databaseName) throws Exception {

		return (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countGenesInModel();

	}

	/**
	 * @param databaseName
	 * @param encoded
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> getModelGenesIDs(String databaseName, boolean encoded) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModelGenesIDs(encoded);

	}

	/**
	 * 
	 * @param row
	 * @param geneID
	 * @param encodedOnly
	 * @param connection
	 * @throws Exception 
	 */
	public static void removeGene(String databaseName, int geneID, boolean encodedOnly) throws Exception {


		Map<Integer, String> proteins = ModelGenesServices.getProteinIdAndEcNumber(databaseName, geneID);

		for(Integer id : proteins.keySet())
			ModelGenesServices.removeGeneAssignemensts(databaseName, geneID, id + "__" + proteins.get(id));

		removeModelGeneByGeneId(databaseName, geneID);
	}

	/**
	 * 
	 * @param row
	 * @param geneID
	 * @param encodedOnly
	 * @param connection
	 * @throws Exception 
	 */
	public static void removeGenes(String databaseName, boolean encodedOnly) throws Exception {


		Set<Integer> genes = new HashSet<>(getModelGenesIDs(databaseName, encodedOnly));

		for(int gene : genes) {

			Map<Integer, String> proteins = getProteinIdAndEcNumber(databaseName, gene);

			for(Integer id : proteins.keySet())
				ModelGenesServices.removeGeneAssignemensts(databaseName, gene, id + "__" + proteins.get(id));
			removeModelGeneByGeneId(databaseName, gene);
		}
	} 

	/**
	 * @param selectedRow
	 * @return
	 * @throws Exception 
	 */
	public static GeneContainer getGeneDataById(String databaseName, int geneIdentifier) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneDataById(geneIdentifier);
	}

	/**
	 * @param name
	 * @param transcription_direction
	 * @param left_end_position
	 * @param right_end_position
	 * @param boolean_rule
	 * @param subunits
	 * @param locusTag
	 * @param connection 
	 */
	public static void insertNewGene(String databaseName, String name, String transcription_direction, String left_end_position,
			String right_end_position, String[] subunits, String locusTag) throws Exception{


		InitDataAccess.getInstance().getDatabaseService(databaseName).insertNewGene(name, transcription_direction, left_end_position, right_end_position, subunits, locusTag);
	}

	/**
	 * @param databaseName
	 * @param locusTag
	 * @param name
	 * @param query
	 * @param origin
	 * @throws Exception
	 */
	public static Integer insertNewGene(String databaseName,String locusTag, String name, String query, SourceType source) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertNewGene(locusTag, name, query, source.toString());
	}

	/**
	 * @param idChromosome
	 * @param name
	 * @param transcription_direction
	 * @param left_end_position
	 * @param right_end_position
	 * @param subunits
	 * @param selectedRow
	 * @param oldSubunits
	 * @param locusTag
	 * @param connection 
	 * @throws Exception 
	 */
	public static void updateGene(String databaseName, int geneIdentifier, String name, String transcription_direction, String left_end_position,
			String right_end_position, String[] subunits, String[] oldSubunits, String locusTag) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateGene(geneIdentifier, name, transcription_direction, left_end_position, right_end_position, subunits, oldSubunits, locusTag);
	}

	/**
	 * @param geneIdentifier
	 * @param proteindIdentifier
	 * @param stmt
	 * @throws SQLException
	 */
	private static void removeGeneAssignemensts(String databaseName, int geneIdentifier, String proteindIdentifier) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).removeGeneAssignemensts(geneIdentifier, proteindIdentifier);
	}


	public static int getGeneLastInsertedID(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneLastInsertedID();

	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, String> getGeneIds(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneIds();

	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Integer> getQueriesByGeneID(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getQueriesByGeneId();

	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Integer> getGeneIDsByQuery(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneIdByQuery();

	}


	/**
	 * @param geneNames
	 * @param query
	 * @param sequenceIDs
	 * @param informationType
	 * @param databaseName
	 * @return
	 * @throws Exception 
	 */
	public static Integer loadGene(Pair<String,String> geneNames, String query, Map<String, Integer> sequenceIDs, String informationType, String databaseName) throws Exception {

		String locusTag = geneNames.getA();
		String geneName = geneNames.getB();

		return InitDataAccess.getInstance().getDatabaseService(databaseName).loadGene(locusTag, query, geneName, null, null, null, informationType);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static int countEntriesInGene(String databaseName) throws Exception {

		return (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countEntriesInGene();

	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static int countGenesInGeneHasCompartment(String databaseName) throws Exception {

		return (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countGenesInGeneHasCompartment();

	}


	public static void removeModelGeneByGeneId(String databaseName, Integer geneId) throws Exception{
		InitDataAccess.getInstance().getDatabaseService(databaseName).removeModelGeneByGeneId(geneId);
	}


	/**
	 * Count the number of genes that encode proteins.
	 * @return int
	 * @throws Exception 
	 * @throws  
	 * @
	 */
	public static int countGenesEncodingProteins(String databaseName) throws Exception {

		return (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countGenesEncodingProteins();

	}

	/**
	 * Count the number of genes with name associated.
	 * @return int
	 * @throws Exception 
	 * @throws  
	 * @
	 */
	public static int countGenesWithName(String databaseName) throws Exception {

		return (int) InitDataAccess.getInstance().getDatabaseService(databaseName).countGenesWithName();

	}

	/**
	 * @param databaseName
	 * @param geneIdentifier
	 * @return
	 * @throws Exception
	 */
	public static String[][] getSubunitsByGeneId(String databaseName, int geneIdentifier) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getSubunitsByGeneId(geneIdentifier);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Integer> getQueriesByGeneId(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getQueriesByGeneId();

	}

	/**
	 * @param databaseName
	 * @param geneIdentifier
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, String> getProteinIdAndEcNumber(String databaseName, int geneIdentifier) throws Exception{

		Map<Integer, String> res = InitDataAccess.getInstance().getDatabaseService(databaseName).getProteinIdAndEcNumber(geneIdentifier);

		if(res != null)
			return res;

		return new HashMap<>();
	}

	/**
	 * @param connection
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static boolean existGenes(String databaseName) throws IOException, Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkGenes();

	}

	/**
	 * Get locus tag ec numbers from database.
	 * 
	 * @param dba
	 * @param originalReactions 
	 * @return
	 * @throws Exception 
	 */
	public static Map<String, List<String>> getGPRsECNumbers(String databaseName, boolean isCompartmentalisedModel) throws Exception {


		Map<String, List<String>> ec_numbers = new HashMap<>();


		List<String[]> subs = InitDataAccess.getInstance().getDatabaseService(databaseName).getGPRsECNumbers(isCompartmentalisedModel);

		if(subs != null && !subs.isEmpty()) {
			for(Object[] sub : subs) {

				String[] strings = Arrays.stream(sub).toArray(String[]::new);
				List<String> genes = new ArrayList<>();

				String gene = strings[0];
				String enzyme = strings[1];

				String seqID = strings[2];

				if(ec_numbers.containsKey(enzyme))
					genes = ec_numbers.get(enzyme);

				if(seqID!=null && !seqID.isEmpty())
					genes.add(seqID);

				else
					genes.add(gene);

				ec_numbers.put(enzyme, genes);
			}
		}

		return ec_numbers;
	}

	public static boolean checkSubunitData(String databaseName, Integer id) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkSubunitData(id);

	}

	public static void insertModelGeneHasCompartment(String databaseName, boolean PrimaryLocation, Double score, Integer model_compartment_idcompartment, Integer model_gene_idgene) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelGeneHasCompartment(PrimaryLocation, score, model_compartment_idcompartment, model_gene_idgene);

	}

	/**
	 * Method for loading compartments into database.
	 * It requires a Map previously obtained with the database IDs of the compartments.
	 * 
	 * @param idGene
	 * @param compartmentsDatabaseIDs
	 * @param statement
	 * @param primaryCompartment
	 * @param scorePrimaryCompartment
	 * @param secondaryCompartmens
	 * @throws Exception 
	 */
	public static void loadGenesCompartments(String databaseName, Integer idGene, Map<String,Integer> compartmentsDatabaseIDs,
			String primaryCompartment, Double scorePrimaryCompartment, Map<String, Double> secondaryCompartmens) throws Exception {

		List<CompartmentContainer> containers = getCompartmentsRelatedToGene(databaseName, idGene);

		if(containers.isEmpty()) {
			insertModelGeneHasCompartment(databaseName, true, scorePrimaryCompartment, compartmentsDatabaseIDs.get(primaryCompartment), idGene);
			containers = getCompartmentsRelatedToGene(databaseName, idGene);
		}

		List<String> compartments = new ArrayList<>();

		for(String loc : secondaryCompartmens.keySet())
			compartments.add(loc);

		List<Integer> compIds = new ArrayList<>();

		for(CompartmentContainer container : containers)
			compIds.add(container.getCompartmentID());

		for(String compartment:compartments) {

			if(!compIds.contains(compartmentsDatabaseIDs.get(compartment)))
				insertModelGeneHasCompartment(databaseName, false, secondaryCompartmens.get(compartment), compartmentsDatabaseIDs.get(compartment), idGene);
		}
	}

	/**
	 * @param databaseName
	 * @param geneId
	 * @return
	 * @throws Exception
	 */
	public static List<CompartmentContainer> getCompartmentsRelatedToGene(String databaseName, Integer geneId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartmentsRelatedToGene(geneId);
	}

	/**
	 * @param databaseName
	 * @param locusTag
	 * @return
	 * @throws Exception
	 */
	public static Integer getGeneIdByLocusTag(String databaseName, String locusTag) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneIDByLocusTag(locusTag);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Set<String>> getSequenceIds(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getSequenceIds();		
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Integer> getGeneLocusTagAndIdgene(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneLocusTagAndIdgene();		
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Integer> getEntryIdAndOrthologyId(String databaseName) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEntryIdAndOrthologyId();
	}

	/**
	 * @param databaseName
	 * @param ecnumber
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> getGeneData2(String databaseName, String ecnumber, Integer id) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneData2(ecnumber, id);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String[]> getAllGenes2(String databaseName) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllGenes2();
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Set<String>> getQueryAndAliasFromProducts(String databaseName) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getQueryAndAliasFromProducts();
	}

	/**
	 * @param databaseName
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static String getQueryAndAliasFromProducts(String databaseName, Integer id) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneNameById(id);
	}	

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<GeneContainer> getAllGeneData(String databaseName) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllGeneData();
	}	

	/**
	 * @param databaseName
	 * @param entryId
	 * @param locus
	 * @return
	 * @throws Exception
	 */
	public static Integer insertNewOrthologue(String databaseName, String entryId, String locus) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertNewOrthologue(entryId, locus);
	}	

	/**
	 * @param databaseName
	 * @param geneId
	 * @param orthologyId
	 * @throws Exception
	 */
	public static void insertModelGeneHasOrthology(String databaseName, Integer geneId, Integer orthologyId, Double score) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelGeneHasOrthology(geneId, orthologyId, score);
	}	

	/**
	 * @param databaseName
	 * @param geneId
	 * @param orthologyId
	 * @throws Exception
	 */
	public static void insertModelGeneHasOrthology(String databaseName, Integer geneId, Integer orthologyId) throws Exception{

		insertModelGeneHasOrthology(databaseName, geneId, orthologyId, null);
	}	

	/**
	 * @param databaseName
	 * @param geneId
	 * @param leftEndPosition
	 * @param rightEndPosition
	 * @throws Exception
	 */
	public static void updateModelGeneEndPositions(String databaseName, Integer geneId, String leftEndPosition, String rightEndPosition) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateModelGeneEndPositions(geneId, leftEndPosition, rightEndPosition);
	}	

	/**
	 * @param databaseName
	 * @param geneId
	 * @param orthologyId
	 * @return
	 * @throws Exception
	 */
	public static boolean checkGeneHasOrthologyEntries(String databaseName, Integer geneId, Integer orthologyId) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkGeneHasOrthologyEntries(geneId, orthologyId);
	}	

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> getEncodingGenes(String databaseName) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getEncodingGenes();

	}

	/**
	 * @param databaseName
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static GeneContainer getGeneByQuery(String databaseName, String query) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModelGeneByQuery(query);

	}

	/**
	 * @param databaseName
	 * @param gene
	 * @return
	 * @throws Exception
	 */
	public static Integer insertNewGene(String databaseName, GeneContainer gene) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertNewGene(gene);

	}

	/**
	 * @param databaseName
	 * @param gene
	 * @throws Exception
	 */
	public static void updateGene(String databaseName, GeneContainer gene) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateGene(gene);
	}

	/**
	 * Load Gene Information
	 * Returns gene id in database.
	 * 
	 * @param locusTag
	 * @param query
	 * @param geneName
	 * @param chromosome
	 * @param statement
	 * @param informationType
	 * @return String
	 * @throws Exception 
	 */
	public static int loadGene(String databaseName, String locusTag, String query, String geneName, 
			String direction, String left_end, String right_end, SourceType informationType) throws Exception {

		GeneContainer gene = getGeneByQuery(databaseName, query);

		Integer geneID;

		if(gene != null) {

			SourceType informationType_db = gene.getOrigin();

			geneID = gene.getIdGene();

			if(!informationType.equals(informationType_db))
				gene.setOrigin(informationType);

			if(geneName!=null)
				gene.setName(geneName);

			updateGene(databaseName, gene);

		}
		else {

			gene = new GeneContainer(query);

			if(direction!=null) 
				gene.setTranscriptionDirection(direction);


			if(left_end!=null)
				gene.setLeft_end_position(left_end);

			if(right_end!=null) 
				gene.setRight_end_position(right_end);

			gene.setLocusTag(locusTag);
			gene.setOrigin(informationType);
			gene.setName(geneName);

			geneID = insertNewGene(databaseName, gene);
		}

		return geneID;
	}

	/**
	 * @param databaseName
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static GeneContainer getGeneHomologyEntryByQuery(String databaseName, String query) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneHomologyEntryByQuery(query);
	}

	/**
	 * Method for loading gene information retrieved from homology data for a given query.
	 * 
	 * @param query
	 * @param statement
	 * @param informationType
	 * @return String
	 * @throws SQLException
	 */
	public static int loadGeneLocusFromGeneHomology (String databaseName, String query, SourceType informationType) throws Exception {

		String locusTag = query, name = null;

		GeneContainer entry = getGeneHomologyEntryByQuery(databaseName, query);

		if(entry != null) {

			locusTag = entry.getLocusTag();
			name = entry.getName();
		}

		return loadGene(databaseName, locusTag, query, name, null, null, null, informationType);
	}

	/**
	 * @param databaseName
	 * @param class_
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Set<String>> getGeneNamesAliases(String databaseName, String class_) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getGeneNamesAliases(class_);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> getGenesLocusTagBySequenceId(String databaseName) throws Exception {

		Map<String,String> data = new HashMap<>();

		List<GeneContainer> genes = getAllGeneData(databaseName);

		for(GeneContainer gene : genes)
			data.put(gene.getExternalIdentifier(), gene.getLocusTag());

		return data;
	}

	/**
	 * @param databaseName
	 * @param entryId
	 * @param locusId
	 * @return
	 * @throws Exception
	 */
	public static Integer getOrthologyIdByEntryIdAndLocus(String databaseName, String entryId, String locusId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getOrthologyIdByEntryIdAndLocus(entryId, locusId);
	}

	/**
	 * @param databaseName
	 * @param entryId
	 * @return
	 * @throws Exception
	 */
	public static Integer getOrthologyIdByEntryIdWherwLocusIdIsNullOrEmpty(String databaseName, String entryId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getOrthologyIdByEntryIdWherwLocusIdIsNullOrEmpty(entryId);
	}

	/**
	 * @param databaseName
	 * @param entryId
	 * @param locusId
	 * @throws Exception
	 */
	public static void updateOrthologyLocusIdByEntryId(String databaseName, String entryId, String locusId) throws Exception {

		InitDataAccess.getInstance().getDatabaseService(databaseName).updateOrthologyLocusIdByEntryId(entryId, locusId);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Integer countInitialMetabolicGenes(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).countInitialMetabolicGenes();

	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, GeneContainer> getAllGeneDatabyIds(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getAllGeneDatabyIds();

	}
	
	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, List<CompartmentContainer>> getCompartmentsRelatedToGenes(String databaseName) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).getCompartmentsRelatedToGenes();

	}

	/**
	 * @param databaseName
	 * @throws Exception
	 */
	public static void removeAllGeneHasCompartments(String databaseName) throws Exception {
		InitDataAccess.getInstance().getDatabaseService(databaseName).removeAllFromModelGeneHasCompartment();
		
	}
	
	/**
	 * @param databaseName
	 * @param key
	 * @param value
	 * @throws Exception
	 */
	public static void updateGenesLocusByQuery(String databaseName, String key, String value) throws Exception {
		InitDataAccess.getInstance().getDatabaseService(databaseName).updateGenesLocusByQuery(key, value);
	}
}