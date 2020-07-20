package pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTable;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.IEntity;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologySearchType;

/**
 * @author ODias
 *
 */
public class AnnotationEnzymes extends WorkspaceEntity implements IEntity {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public static final double ALPHA = 0.5;
	public static final double BLAST_HMMER_WEIGHT = 0.5;
	public static final double THRESHOLD = 0.0;
	public static final double UPPER_THRESHOLD = 1.0;
	public static final double BETA = 0.15;
	public static final int MINIMUM_NUMBER_OF_HITS = 3;

	private Map<Integer, String>
	prodItem,
	ecItem,
	initialProdItem,
	initialEcItem, 
	namesList, 
	locusList, 
	initialLocus, 
	initialNames,
	notesMap,
	committedProdItem,
	committedEcItem, 
	committedNamesList, 
	committedLocusList,
	committedChromosome,
	committedNotesMap,
	geneDataEntries,
	integrationLocusList,
	integrationChromosome, 
	integrationNamesList, 
	integrationProdItem,
	integrationEcItem,
	initialOriginalLocus,
	initialOriginalNames;
	
	private Map<Integer,List<Object>> geneData;
	
	private Map<Integer, String[]> editedProductData, 
	editedEnzymeData,
	committedProductList, 
	committedEnzymeList;
	
	private Map<Integer, Boolean> committedSelected;
	
	private Map<Integer, Integer> keys, tableRowIndex //, reverseKeys
	;
	
	Map<Integer, Long> homologuesCountEcNumber;
	private Map<Integer, Long> homologuesCountProduct;
	
	private Map<Integer, String> enzymeFinalScore, enzymeFrequencyScore, enzymeTaxonomyScore,
	productFrequencyScore, productTaxonomyScore, productFinalScore, ecName, prodName;
	
	private Map<String, Integer> queryKeys;
	
	Map<Integer, List<Integer>> ecKeys, prodKeys;
	
	private Map<String,List<String>> uniProtECnumbers;
	
	private Map<String, Set<Integer>> blastGeneDatabase;
	
	
	private List<Map<Integer, String>> itemsList;
	
	private List<Integer> interProRows;
	
	private List<String[]> dataFromEcNumber, 
	ecRank, 
	dataFromProduct,
	taxRank;
	
	
	private String[][] ecnPercent, 
	prodPercent, 
	product,
	enzyme;
	
	private boolean hasCommittedData;
	
	
	private int selectedRow,
	minimumNumberofHits,
	committedMinHomologies;

	
	private Double alpha,
	beta,
	threshold, 
	upperThreshold;
	
	private double committedThreshold,
	committedBalanceBH,
	committedAlpha,
	committedBeta,
	committedUpperThreshold;
	
	
	private String //taxonomyRank, 
	maxTaxRank;
	
	
	private DecimalFormat format;
	
	
	private WorkspaceGenericDataTable dataTable;
	
	
	private HomologySearchType homologySearchType;
	private Map<Integer, String> geneQueries;
	
	/**
	 * @param dbt
	 * @param name
	 */
	public AnnotationEnzymes(WorkspaceTable dbt, String name) {

		super(dbt, name);

		this.identifiers = new HashMap<>();
		this.namesIndex = new HashMap<>();
				
		this.prodItem = new HashMap<Integer,String>();
		this.ecItem = new HashMap<Integer,String>();
		this.locusList =  new HashMap<Integer, String>();
		this.namesList = new HashMap<Integer, String>();
		this.setNotesMap(new HashMap<Integer, String>()); 
		this.editedEnzymeData = new HashMap<Integer, String[]>();
		this.editedProductData=new HashMap<>();
		this.setEnzymeFrequencyScore(new HashMap<>());
		this.setEnzymeTaxonomyScore(new HashMap<>());
		this.setEnzymeFinalScore(new HashMap<>());
		this.setProductFrequencyScore(new HashMap<>());
		this.setProductTaxonomyScore(new HashMap<>());
		this.setProductFinalScore(new HashMap<>());
		this.tableRowIndex = new HashMap<>();
		this.dataFromEcNumber = new ArrayList<>();
		this.ecRank = new ArrayList<>();
		this.dataFromProduct = new ArrayList<>();
		this.taxRank = new ArrayList<>();
		this.homologuesCountEcNumber = new HashMap<>();
		this.homologuesCountProduct = new HashMap<>();
		this.geneDataEntries = new HashMap<>();
	//	this.taxonomyRank = "";
		this.maxTaxRank = "";

		this.alpha= ALPHA;
		this.threshold = THRESHOLD;
		this.beta = BETA;
		this.minimumNumberofHits = MINIMUM_NUMBER_OF_HITS;

		this.setSelectedRow(-1);
	}

	public void resetDataStuctures() {
		
		super.resetDataStuctures();
		this.uniProtECnumbers = null;
		this.interProRows = null;
		this.homologySearchType = null;
	}

	

	/**
	 * @return
	 */
	public WorkspaceGenericDataTable getAllGenes() {
		
		return this.dataTable;
	}
	
	/**
	 * @param alfa
	 * @return
	 */
	public void setHomologySearchType(HomologySearchType homologySearchType) {
		
		this.homologySearchType = homologySearchType;
	}
	
	/**
	 * @return
	 */
	public HomologySearchType getHomologySearchType() {
		
		return this.homologySearchType;
	}
	
	/**
	 * @param alfa
	 * @return
	 */
	public void setAllGenes(WorkspaceGenericDataTable dataTable) {
		
		this.dataTable = dataTable;
	}
	
	
	/**
	 * @param id
	 * @return
	 */
	public String getGeneLocus(Integer id) {

		return this.initialLocus.get(id);
	}

	/**
	 * @param row
	 * @param data
	 * 
	 * Sets a Map of user edited Product Lists
	 * 
	 */
	public void setEditedProductData(int row, String[] data) {

		this.editedProductData.put(row, data);
	}

	/**
	 * @return a Map of user edited Product Lists
	 */
	public Map<Integer, String[]> getEditedProductData() {

		return editedProductData;
	}

	/**
	 * @param data
	 * 
	 * Creates a new user edited product list
	 */
	public void setEditedProductData(Map<Integer, String[]> data) {
		this.editedProductData = data;
	}

	/**
	 * @param row
	 * @param data
	 * 
	 * Sets a Map of user edited Enzyme List
	 * 
	 */
	public void setEditedEnzymeData(int row, String[] data) {
		this.editedEnzymeData.put(row, data);
	}

	/**
	 * @return a Map of user edited Enzyme List
	 */
	public Map<Integer, String[]> getEditedEnzymeData() {
		return editedEnzymeData;
	}

	public void setEditedEnzymeData(Map<Integer, String[]> data) {
		this.editedEnzymeData = data;
	}


	/**
	 * @return the initial locus Tag list
	 */
	public Map<Integer, String> getInitialLocus() {
		return initialLocus;
	}

	/**
	 * @return the initial names List
	 */
	public Map<Integer, String> getInitialNames() {
		return initialNames;
	}

	/**
	 * @param score1 the score1 to set
	 */
	public void setEnzymeFrequencyScore(Map<Integer, String> enzymeFrequencyScore) {
		this.enzymeFrequencyScore = enzymeFrequencyScore;
	}

	/**
	 * @return the score1
	 */
	public Map<Integer, String> getEnzymeFrequencyScore() {
		return enzymeFrequencyScore;
	}

	/**
	 * @param score2 the score2 to set
	 */
	public void setEnzymeTaxonomyScore(Map<Integer, String> enzymeTaxonomyScore) {
		this.enzymeTaxonomyScore = enzymeTaxonomyScore;
	}

	/**
	 * @return the score2
	 */
	public Map<Integer, String> getEnzymeTaxonomyScore() {
		return enzymeTaxonomyScore;
	}

	/**
	 * @param score the score to set
	 */
	public void setEnzymeFinalScore(Map<Integer, String> enzymeFinalScore) {
		this.enzymeFinalScore = enzymeFinalScore;
	}

	/**
	 * @return the score
	 */
	public Map<Integer, String> getEnzymeFinalScore() {
		return enzymeFinalScore;
	}

	/**
	 * @param score1 the score1 to set
	 */
	public void setProductFrequencyScore(Map<Integer, String> productFrequencyScore) {
		this.productFrequencyScore = productFrequencyScore;
	}

	/**
	 * @return the score1
	 */
	public Map<Integer, String> getProductFrequencyScore() {
		return this.productFrequencyScore;
	}

	/**
	 * @param enzymeTaxonomyScore the score2 to set
	 */
	public void setProductTaxonomyScore(Map<Integer, String> productTaxonomyScore) {
		this.productTaxonomyScore = productTaxonomyScore;
	}

	/**
	 * @return the score2
	 */
	public Map<Integer, String> getProductTaxonomyScore() {
		return this.productTaxonomyScore;
	}

	/**
	 * @param enzymeFinalScore the score to set
	 */
	public void setProductFinalScore(Map<Integer, String> productFinalScore) {
		this.productFinalScore = productFinalScore;
	}

	/**
	 * @return the score
	 */
	public Map<Integer, String> getProductFinalScore() {
		return productFinalScore;
	}

	/**
	 * @return the alpha
	 */
	public Double getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha the alpha to set
	 */
	public void setAlpha(Double alpha) {
		this.alpha = alpha;
	}

	/**
	 * @return
	 */
	public int getSelectedRow() {
		return selectedRow;
	}

	/**
	 * @param selectedRow
	 */
	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}

	/**
	 * @return a list of user selected products
	 */
	public Map<Integer, String> getProductList() {
		return prodItem;
	}

	/**
	 * @return a list of user selected ec numbers
	 */
	public Map<Integer, String> getEnzymesList() {

		return ecItem;
	}

	/**
	 * @return a list of user selected gene names
	 */
	public Map<Integer, String> getNamesList() {

		return namesList ;
	}

	/**
	 * @param prodItem
	 * 
	 * Sets a list of user selected products ec numbers
	 * 
	 */
	public void setProductList(Map<Integer, String> prodItem) {
		this.prodItem = prodItem;		
	}


	/**
	 * @param ecItem
	 * 
	 * Sets a list of user selected ec numbers
	 * 
	 */
	public void setEnzymesList(Map<Integer, String> ecItem) {
		this.ecItem = ecItem;

	}

	/**
	 * @param namesList
	 * 
	 * Sets a list of gene names
	 * 
	 */
	public void setNamesList(Map<Integer, String> namesList) {
		this.namesList = namesList ;
	}


	/**
	 * @param locusList
	 * 
	 * Sets a list of user selected gene Locus Tags
	 * 
	 */
	public void setLocusList(Map<Integer, String> locusList) {
		this.locusList=locusList;

	}

	/**
	 * @return a list of user selected locus Tags
	 */
	public Map<Integer, String> getLocusList() {
		return locusList;
	}

	/**
	 * @return the notesMap
	 */
	public Map<Integer, String> getNotesMap() {
		return notesMap;
	}

	/**
	 * @param notesMap the notesMap to set
	 */
	public void setNotesMap(Map<Integer, String> notesMap) {
		this.notesMap = notesMap;
	}

	/**
	 * @return the threshold
	 */
	public Double getThreshold() {
		return this.threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	/**
	 * @return the beta
	 */
	public Double getBeta() {
		return beta;
	}

	/**
	 * @param beta the beta to set
	 */
	public void setBeta(Double beta) {
		this.beta = beta;
	}

	/**
	 * @return the minimumNumberofHits
	 */
	public int getMinimumNumberofHits() {
		return minimumNumberofHits;
	}

	/**
	 * @param minimumNumberofHits the minimumNumberofHits to set
	 */
	public void setMinimumNumberofHits(int minimumNumberofHits) {
		this.minimumNumberofHits = minimumNumberofHits;
	}

	/**
	 * @return the blastPGeneDataEntries
	 */
	public Map<Integer, String> getGeneDataEntries() {
		return geneDataEntries;
	}

	/**
	 * @param blastPGeneDataEntries the blastPGeneDataEntries to set
	 */
	public void setGeneDataEntries(Map<Integer, String> geneDataEntries) {
		this.geneDataEntries = geneDataEntries;
	}

	/**
	 * @return the committedProdItem
	 */
	public Map<Integer, String> getCommittedProdItem() {
		return committedProdItem;
	}

	/**
	 * @param committedProdItem the committedProdItem to set
	 */
	public void setCommittedProdItem(Map<Integer, String> committedProdItem) {
		this.committedProdItem = committedProdItem;
	}

	/**
	 * @return the committedEcItem
	 */
	public Map<Integer, String> getCommittedEcItem() {
		return committedEcItem;
	}

	/**
	 * @param committedEcItem the committedEcItem to set
	 */
	public void setCommittedEcItem(Map<Integer, String> committedEcItem) {
		this.committedEcItem = committedEcItem;
	}

	/**
	 * @return the committedNamesList
	 */
	public Map<Integer, String> getCommittedNamesList() {
		return committedNamesList;
	}

	/**
	 * @param committedNamesList the committedNamesList to set
	 */
	public void setCommittedNamesList(Map<Integer, String> committedNamesList) {
		this.committedNamesList = committedNamesList;
	}

	/**
	 * @return the committedLocusList
	 */
	public Map<Integer, String> getCommittedLocusList() {
		return committedLocusList;
	}

	/**
	 * @param committedLocusList the committedLocusList to set
	 */
	public void setCommittedLocusList(Map<Integer, String> committedLocusList) {
		this.committedLocusList = committedLocusList;
	}

	/**
	 * @return the committedChromosome
	 */
	public Map<Integer, String> getCommittedChromosome() {
		return committedChromosome;
	}

	/**
	 * @param committedChromosome the committedChromosome to set
	 */
	public void setCommittedChromosome(Map<Integer, String> committedChromosome) {
		this.committedChromosome = committedChromosome;
	}

	/**
	 * @return the committedNotesMap
	 */
	public Map<Integer, String> getCommittedNotesMap() {
		return committedNotesMap;
	}

	/**
	 * @param committedNotesMap the committedNotesMap to set
	 */
	public void setCommittedNotesMap(Map<Integer, String> committedNotesMap) {
		this.committedNotesMap = committedNotesMap;
	}

	/**
	 * @return the committedSelected
	 */
	public Map<Integer, Boolean> getCommittedSelected() {
		return committedSelected;
	}

	/**
	 * @param committedSelected the committedSelected to set
	 */
	public void setCommittedSelected(Map<Integer, Boolean> committedSelected) {
		this.committedSelected = committedSelected;
	}

	/**
	 * @return
	 */
	public boolean hasCommittedData() {
		return hasCommittedData;
	}

	/**
	 * @return
	 */
	public boolean setHasCommittedData() {
		return hasCommittedData = true;
	}

	/**
	 * @return the committedProductList
	 */
	public Map<Integer, String[]> getCommittedProductList() {
		return committedProductList;
	}

	/**
	 * @param committedProductList the committedProductList to set
	 */
	public void setCommittedProductList(Map<Integer, String[]> committedProductList) {
		this.committedProductList = committedProductList;
	}

	/**
	 * @return the committedEnzymeList
	 */
	public Map<Integer, String[]> getCommittedEnzymeList() {
		return committedEnzymeList;
	}

	/**
	 * @param committedEnzymeList the committedEnzymeList to set
	 */
	public void setCommittedEnzymeList(Map<Integer, String[]> committedEnzymeList) {
		this.committedEnzymeList = committedEnzymeList;
	}

	/**
	 * @return the initialProdItem
	 */
	public Map<Integer, String> getInitialProdItem() {
		return initialProdItem;
	}

	/**
	 * @param initialProdItem the initialProdItem to set
	 */
	public void setInitialProdItem(Map<Integer, String> initialProdItem) {
		this.initialProdItem = initialProdItem;
	}

	/**
	 * @return the initialEcItem
	 */
	public Map<Integer, String> getInitialEcItem() {

		return initialEcItem;
	}

	/**
	 * @param initialEcItem the initialEcItem to set
	 */
	public void setInitialEcItem(Map<Integer, String> initialEcItem) {

		this.initialEcItem = initialEcItem;
	}

	/**
	 * @return the keys
	 */
	public Map<Integer, Integer> getKeys() {
		return keys;
	}

	/**
	 * @param keys the keys to set
	 */
	public void setKeys(Map<Integer, Integer> keys) {
		this.keys = keys;
	}

	/**
	 * @param mappedLocusList
	 */
	public void setIntegrationLocusList(Map<Integer, String> mappedLocusList) {

		this.integrationLocusList = mappedLocusList;
	}

	/**
	 * @param mappedNamesList
	 */
	public void setIntegrationNamesList(Map<Integer, String> mappedNamesList) {

		this.integrationNamesList = mappedNamesList;
	}

	/**
	 * @param mappedProdItem
	 */
	public void setIntegrationProdItem(Map<Integer, String> mappedProdItem) {

		this.integrationProdItem = mappedProdItem;
	}

	/**
	 * @param mappedEcItem
	 */
	public void setIntegrationEcItem(Map<Integer, String> mappedEcItem) {

		this.integrationEcItem = mappedEcItem;
	}

	/**
	 * @return the integrationProdItem
	 */
	public void setIntegrationChromosome(Map<Integer, String> integrationChromosome) {

		this.integrationChromosome = integrationChromosome;
	}

	/**
	 * @return the integrationLocusList
	 */
	public Map<Integer, String> getIntegrationLocusList() {
		return integrationLocusList;
	}

	/**
	 * @return the integrationNamesList
	 */
	public Map<Integer, String> getIntegrationNamesList() {
		return integrationNamesList;
	}

	/**
	 * @return the integrationProdItem
	 */
	public Map<Integer, String> getIntegrationProdItem() {
		return integrationProdItem;
	}

	/**
	 * @return the integrationNamesList
	 */
	public Map<Integer, String> getIntegrationChromosome() {
		return integrationChromosome;
	}

	/**
	 * @return the integrationEcItem
	 */
	public Map<Integer, String> getIntegrationEcItem() {
		return integrationEcItem;
	}

//	/**
//	 * @return the reverseKeys
//	 */
//	public Map<Integer, Integer> getReverseKeys() {
//		return reverseKeys;
//	}
//
//	/**
//	 * @param reverseKeys the reverseKeys to set
//	 */
//	public void setReverseKeys(Map<Integer, Integer> reverseKeys) {
//		this.reverseKeys = reverseKeys;
//	}

	/**
	 * @return the committedThreshold
	 */
	public double getCommittedThreshold() {
		return committedThreshold;
	}

	/**
	 * @param committedThreshold the committedThreshold to set
	 */
	public void setCommittedThreshold(double committedThreshold) {
		this.committedThreshold = committedThreshold;
	}

	/**
	 * @return the committedBalanceBH
	 */
	public double getCommittedBalanceBH() {
		return committedBalanceBH;
	}

	/**
	 * @param committedBalanceBH the committedBalanceBH to set
	 */
	public void setCommittedBalanceBH(double committedBalanceBH) {
		this.committedBalanceBH = committedBalanceBH;
	}

	/**
	 * @return the committedAlpha
	 */
	public double getCommittedAlpha() {
		return committedAlpha;
	}

	/**
	 * @param committedAlpha the committedAlpha to set
	 */
	public void setCommittedAlpha(double committedAlpha) {
		this.committedAlpha = committedAlpha;
	}

	/**
	 * @return the committedBeta
	 */
	public double getCommittedBeta() {
		return committedBeta;
	}

	/**
	 * @param committedBeta the committedBeta to set
	 */
	public void setCommittedBeta(double committedBeta) {
		this.committedBeta = committedBeta;
	}

	/**
	 * @return the committedMinHomologies
	 */
	public int getCommittedMinHomologies() {
		return committedMinHomologies;
	}

	/**
	 * @param committedMinHomologies the committedMinHomologies to set
	 */
	public void setCommittedMinHomologies(int committedMinHomologies) {
		this.committedMinHomologies = committedMinHomologies;
	}


	/**
	 * @return the committedUpperThreshold
	 */
	public double getCommittedUpperThreshold() {
		return committedUpperThreshold;
	}


	/**
	 * @param committedUpperThreshold the committedUpperThreshold to set
	 */
	public void setCommittedUpperThreshold(double committedUpperThreshold) {
		this.committedUpperThreshold = committedUpperThreshold;
	}


	/**
	 * @return the upperThreshold
	 */
	public Double getUpperThreshold() {
		return upperThreshold;
	}


	/**
	 * @param upperThreshold the upperThreshold to set
	 */
	public void setUpperThreshold(Double upperThreshold) {
		this.upperThreshold = upperThreshold;
	}


	/**
	 * @param itemsList
	 */
	public void setItemsList(List<Map<Integer, String>> itemsList) {

		this.itemsList = itemsList;
	}

	/**
	 * @return
	 */
	public List<Map<Integer, String>> getItemsList() {

		return this.itemsList;
	}


	public String[][] getEcnPercent() {
		return ecnPercent;
	}


	public void setEcnPercent(String[][] ecnPercent) {
		this.ecnPercent = ecnPercent;
	}


	public String[][] getProdPercent() {
		return prodPercent;
	}


	public void setProdPercent(String[][] prodPercent) {
		this.prodPercent = prodPercent;
	}


	public String[][] getProduct() {
		return product;
	}


	public void setProduct(String[][] product) {
		this.product = product;
	}


	public String[][] getEnzyme() {
		return enzyme;
	}


	public void setEnzyme(String[][] enzyme) {
		this.enzyme = enzyme;
	}


	public Map<Integer, String> getProdItem() {
		return prodItem;
	}


	public void setProdItem(Map<Integer, String> prodItem) {
		this.prodItem = prodItem;
	}


	public Map<Integer, String> getEcItem() {
		return ecItem;
	}


	public void setEcItem(Map<Integer, String> ecItem) {
		this.ecItem = ecItem;
	}


	public Map<Integer, Integer> getTableRowIndex() {
		return tableRowIndex;
	}


	public void setTableRowIndex(Map<Integer, Integer> tableRowIndex) {
		this.tableRowIndex = tableRowIndex;
	}

	public boolean isHasCommittedData() {
		return hasCommittedData;
	}


	public void setHasCommittedData(boolean hasCommittedData) {
		this.hasCommittedData = hasCommittedData;
	}


//	public String getTaxonomyRank() {
//		return taxonomyRank;
//	}
//
//
//	public void setTaxonomyRank(String taxonomyRank) {
//		this.taxonomyRank = taxonomyRank;
//	}


	public String getMaxTaxRank() {
		return maxTaxRank;
	}


	public void setMaxTaxRank(String maxTaxRank) {
		this.maxTaxRank = maxTaxRank;
	}


	public DecimalFormat getFormat() {
		return format;
	}


	public void setFormat(DecimalFormat format) {
		this.format = format;
	}


	public Map<Integer, List<Object>> getGeneData() {
		return geneData;
	}


	public void setGeneData(Map<Integer, List<Object>> geneData) {
		this.geneData = geneData;
	}


	public Map<Integer, String> getProdName() {
		return prodName;
	}


	public void setProdName(Map<Integer, String> prodName) {
		this.prodName = prodName;
	}


	public Map<Integer, List<Integer>> getProdKeys() {
		return prodKeys;
	}


	public void setProdKeys(Map<Integer, List<Integer>> prodKeys) {
		this.prodKeys = prodKeys;
	}


	public Map<Integer, String> getEcName() {
		return ecName;
	}


	public void setEcName(Map<Integer, String> ecName) {
		this.ecName = ecName;
	}


	public Map<Integer, List<Integer>> getEcKeys() {
		return ecKeys;
	}


	public void setEcKeys(Map<Integer, List<Integer>> ecKeys) {
		this.ecKeys = ecKeys;
	}


	public Map<String, Set<Integer>> getBlastGeneDatabase() {
		return blastGeneDatabase;
	}


	public void setBlastGeneDatabase(Map<String, Set<Integer>> blastGeneDatabase) {
		this.blastGeneDatabase = blastGeneDatabase;
	}


	public Map<Integer, String> getInitialOriginalLocus() {
		return initialOriginalLocus;
	}


	public void setInitialOriginalLocus(Map<Integer, String> initialOriginalLocus) {
		this.initialOriginalLocus = initialOriginalLocus;
	}


	public Map<Integer, String> getInitialOriginalNames() {
		return initialOriginalNames;
	}


	public void setInitialOriginalNames(Map<Integer, String> initialOriginalNames) {
		this.initialOriginalNames = initialOriginalNames;
	}


	public List<String[]> getDataFromEcNumber() {
		return dataFromEcNumber;
	}


	public void setDataFromEcNumber(List<String[]> dataFromEcNumber) {
		this.dataFromEcNumber = dataFromEcNumber;
	}


	public List<String[]> getEcRank() {
		return ecRank;
	}


	public void setEcRank(List<String[]> ecRank) {
		this.ecRank = ecRank;
	}


	public List<String[]> getDataFromProduct() {
		return dataFromProduct;
	}


	public void setDataFromProduct(List<String[]> dataFromProduct) {
		this.dataFromProduct = dataFromProduct;
	}


	public List<String[]> getTaxRank() {
		return taxRank;
	}


	public void setTaxRank(List<String[]> taxRank) {
		this.taxRank = taxRank;
	}


	public Map<Integer, Long> getHomologuesCountEcNumber() {
		return homologuesCountEcNumber;
	}


	public void setHomologuesCountEcNumber(Map<Integer, Long> homologuesCountEcNumber) {
		this.homologuesCountEcNumber = homologuesCountEcNumber;
	}


	public Map<Integer, Long> getHomologuesCountProduct() {
		return homologuesCountProduct;
	}


	public void setHomologuesCountProduct(Map<Integer, Long> homologuesCountProduct) {
		this.homologuesCountProduct = homologuesCountProduct;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public static double getBlastHmmerWeight() {
		return BLAST_HMMER_WEIGHT;
	}


	public static int getDefaultMinimumNumberOfHits() {
		return MINIMUM_NUMBER_OF_HITS;
	}


	public void setInitialLocus(Map<Integer, String> initialLocus) {
		this.initialLocus = initialLocus;
	}


	public void setInitialNames(Map<Integer, String> initialNames) {
		this.initialNames = initialNames;
	}


	public Map<String, List<String>> getUniProtECnumbers() {
		return uniProtECnumbers;
	}


	public void setUniProtECnumbers(Map<String, List<String>> uniProtECnumbers) {
		this.uniProtECnumbers = uniProtECnumbers;
	}

	/**
	 * @return the locusKeys
	 */
	public Map<String, Integer> getLocusKeys() {
		return queryKeys;
	}


	/**
	 * @param locusKeys the locusKeys to set
	 */
	public void setLocusKeys(Map<String, Integer> locusKeys) {
		this.queryKeys = locusKeys;
	}


	/**
	 * @return the interProRows
	 */
	public List<Integer> getInterProRows() {
		return interProRows;
	}


	/**
	 * @param interProRows the interProRows to set
	 */
	public void setInterProRows(List<Integer> interProRows) {
		this.interProRows = interProRows;
	}

	/**
	 * @param geneQueries
	 */
	public void setQueries( Map<Integer, String> geneQueries) {
		this.geneQueries = geneQueries;
	}

	
	/**
	 * @return
	 */
	public Map<Integer, String> getQueries() {

		return geneQueries;
	}

}
