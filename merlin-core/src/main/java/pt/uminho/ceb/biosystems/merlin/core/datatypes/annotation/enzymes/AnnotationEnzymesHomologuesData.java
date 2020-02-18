package pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.biojava.nbio.core.sequence.ProteinSequence;

/**
 * @author Oscar
 *
 */
public class AnnotationEnzymesHomologuesData {

	private ConcurrentHashMap<String, String> locusTags;
	private ConcurrentHashMap<String, ProteinSequence> sequences;
	private Map<String, String> organism, taxonomy, product, calculated_mol_wt, definition, blastLocusTag, organelles, genes;
	private Map<String, Double> eValue, bits, identity, positives, target_coverage, query_coverage;
	private TreeMap<String, String[]> ecnumber;
	private List<String> locusIDs = new LinkedList<String>();
	private String organismID, fastaSequence;
	private String chromosome, organelle, locus_protein_note, locusTag, locus_gene_note, gene;
	private String[] organismTaxa;
	private String sequence_code;
	private ConcurrentHashMap<String, Boolean> uniprotStatus;
	private boolean dataRetrieved;
	private ConcurrentHashMap<String, String[]> taxonomyMap;
	private String refSeqGI;
	private String ncbiLocusTag;
	private String uniProtEntryID;
	private String uniprotLocusTag;
	private String query;
	private String uniprotReviewStatus;
	private String ecNumbers;
	private String blastProgram;
	private String blastVersion;
	private String blastDatabaseIdentifier;
	private boolean isNoSimilarity;

	/**
	 * 
	 */
	public AnnotationEnzymesHomologuesData() {

		this.locusTags = new ConcurrentHashMap<>();
		this.sequences = new ConcurrentHashMap<>();
		this.organism = new TreeMap<String, String>();
		this.taxonomy= new TreeMap<String, String>();
		this.product= new TreeMap<String, String>();
		this.calculated_mol_wt= new TreeMap<String, String>();
		this.definition= new TreeMap<String, String>();
		this.blastLocusTag = new TreeMap<String, String>();
		this.organelles = new TreeMap<String, String>();
		this.genes = new TreeMap<String, String>();
		this.uniprotStatus = new ConcurrentHashMap<>();
		this.ecnumber = new TreeMap<>();
		this.taxonomyMap = new ConcurrentHashMap<String, String[]>();
		this.eValue= new TreeMap<String, Double>();
		this.bits= new TreeMap<String, Double>();
		this.identity= new TreeMap<String, Double>();
		this.positives= new TreeMap<String, Double>();
		this.query_coverage= new TreeMap<String, Double>();
		this.target_coverage= new TreeMap<String, Double>();
		this.gene = "";
		this.chromosome = "";
		this.locusIDs = new ArrayList<String>();
	}

	/**
	 * @param name
	 * @param locus
	 */
	public void addLocusTag(String name, String locus){

		this.locusTags.put(name, locus);
	}

	/**
	 * @param name
	 * @param sequence
	 */
	public void addSequence(String name, ProteinSequence sequence){

		this.sequences.put(name, sequence);
	}

	/**
	 * @param name
	 * @param organism
	 */
	public void addOrganism(String name, String organism){

		this.organism.put(name, organism);
	}

	/**
	 * @param name
	 * @param eValue
	 */
	public void addEValue(String name, double eValue){

		this.eValue.put(name, eValue);
	}

	/**
	 * @param name
	 * @param bits
	 */
	public void addBits(String name, double bits){

		this.bits.put(name, bits);
	}

	/**
	 * @param name
	 * @param identity
	 */
	public void addIdentity(String name, double identity){

		this.identity.put(name, identity);
	}
	
	/**
	 * @param name
	 * @param positives
	 */
	public void addPositives(String name, double positives){

		this.positives.put(name, positives);
	}
	
	/**
	 * @param queryCoverage
	 * @param bits
	 */
	public void addQueryCoverage(String name, double queryCoverage){

		this.query_coverage.put(name, queryCoverage);
	}
	
	/**
	 * @param targetCoverage
	 * @param bits
	 */
	public void addTargetCoverage(String name, double targetCoverage){

		this.target_coverage.put(name, targetCoverage);
	}

	/**
	 * @param locus
	 * @param index 
	 */
	public void addLocusID(String locus, int index){

		this.locusIDs.set(index, locus);
	}

	/**
	 * @param primary_acession
	 * @param definition
	 */
	public void addDefinition(String name, String definition) {

		this.definition.put(name, definition);
	}

	/**
	 * @param name
	 * @param product
	 */
	public void addProduct(String name, String product) {

		this.product.put(name, product);
	}

	/**
	 * @param name
	 * @param value
	 */
	public void addCalculated_mol_wt (String name, String value) {

		calculated_mol_wt.put(name , value);
	}

	/**
	 * @param name
	 * @param value
	 */
	public void addOrganelles(String name, String value) {

		this.organelles.put(name, value);
	}

	/**
	 * @param name
	 * @param value
	 */
	public void addBlastLocusTags(String name, String value) {

		this.blastLocusTag.put(name,value);
	}

	/**
	 * @param name
	 * @param value
	 */
	public void addGenes(String name, String value) {

		this.genes.put(name, value);
	}

	/**
	 * @param name
	 * @param value
	 */
	public void addUniprotStatus(String name, boolean value) {

		this.uniprotStatus.put(name, value);
	}

	/**
	 * @param name
	 * @param value
	 */
	public void addECnumbers(String name, String[] value) {

		this.ecnumber.put(name, value);
	}

	/**
	 * @param gene
	 * @param taxonomy
	 */
	public void addTaxonomy(String gene, String taxonomy) {

		this.taxonomy.put(gene, taxonomy);
	}

	/**
	 * @return
	 */
	public Map<String, String> getLocusTags() {
		return locusTags;
	}

	/**
	 * @param locus_Tag
	 */
	public void setLocusTags(ConcurrentHashMap<String, String> locusTags) {
		this.locusTags = locusTags;
	}

	/**
	 * @return
	 */
	public Map<String, ProteinSequence> getSequences() {
		return sequences;
	}

	/**
	 * @param sequences
	 */
	public void setSequences(ConcurrentHashMap<String, ProteinSequence> sequences) {
		this.sequences = sequences;
	}

	public Map<String, String> getOrganism() {
		return organism;
	}

	public void setOrganism(Map<String, String> organism) {
		this.organism = organism;
	}

	public Map<String, String> getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(Map<String, String> taxonomy) {
		this.taxonomy = taxonomy;
	}

	public Map<String, String> getProduct() {
		return product;
	}

	public void setProduct(Map<String, String> product) {
		this.product = product;
	}

	public Map<String, String> getCalculated_mol_wt() {
		return calculated_mol_wt;
	}

	public void setCalculated_mol_wt(Map<String, String> calculated_mol_wt) {
		this.calculated_mol_wt = calculated_mol_wt;
	}

	public Map<String, String> getDefinition() {
		return definition;
	}

	public void setDefinition(Map<String, String> definition) {
		this.definition = definition;
	}

	public Map<String, String> getBlastLocusTag() {
		return blastLocusTag;
	}

	public void setBlastLocusTag(Map<String, String> blastLocusTag) {
		this.blastLocusTag = blastLocusTag;
	}

	public Map<String, String> getOrganelles() {
		return organelles;
	}

	public void setOrganelles(Map<String, String> organelles) {
		this.organelles = organelles;
	}

	public Map<String, String> getGenes() {
		return genes;
	}

	public void setGenes(Map<String, String> genes) {
		this.genes = genes;
	}

	public Map<String, Double> getEValue() {
		return eValue;
	}

	public void setEValue(Map<String, Double> eValue) {
		this.eValue = eValue;
	}

	public Map<String, Double> getBits() {
		return bits;
	}

	public void setBits(Map<String, Double> bits) {
		this.bits = bits;
	}

	public TreeMap<String, String[]> getEcnumber() {
		return ecnumber;
	}

	public void setEcnumber(TreeMap<String, String[]> ecnumber) {
		this.ecnumber = ecnumber;
	}

	/**
	 * @return the locusID
	 */
	public List<String> getLocusIDs() {
		return locusIDs;
	}

	/**
	 * @param locusID the locusID to set
	 */
	public void setLocusIDs(List<String> locusIDs) {
		this.locusIDs = locusIDs;
	}

	/**
	 * @return the organismID
	 */
	public String getOrganismID() {
		return organismID;
	}

	/**
	 * @param organismID the organismID to set
	 */
	public void setOrganismID(String organismID) {
		this.organismID = organismID;
	}

	/**
	 * @return the fastaSequence
	 */
	public String getFastaSequence() {
		return fastaSequence;
	}

	/**
	 * @param fastaSequence the fastaSequence to set
	 */
	public void setFastaSequence(String fastaSequence) {
		this.fastaSequence = fastaSequence;
	}

	/**
	 * @param chromosome
	 */
	public void setChromosome(String chromosome) {

		this.chromosome = chromosome;
	}

	/**
	 * @return
	 */
	public String getChromosome() {

		return this.chromosome ;
	}

	/**
	 * @return the locus_protein_note
	 */
	public String getOrganelle() {
		return organelle;
	}

	/**
	 * @param organelle
	 */
	public void setOrganelle(String organelle) {

		this.organelle = organelle;
	}

	/**
	 * @return the locus_protein_note
	 */
	public String getLocus_protein_note() {
		return locus_protein_note;
	}

	/**
	 * @param locus_protein_note the locus_protein_note to set
	 */
	public void setLocus_protein_note(String locus_protein_note) {
		this.locus_protein_note = locus_protein_note;
	}

	/**
	 * @return the locus_tag
	 */
	public String getLocusTag() {
		return locusTag;
	}

	/**
	 * @param locus_tag the locus_tag to set
	 */
	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}

	/**
	 * @return the organism taxa
	 */
	public String[] getOrganismTaxa() {
		return organismTaxa;
	}

	/**
	 * @param organismTaxa the organism taxa to set
	 */
	public void setOrganismTaxa(String[] organismTaxa) {
		this.organismTaxa = organismTaxa;
	}

	/**
	 * @return the sequence_code
	 */
	public String getSequence_code() {
		return sequence_code;
	}

	/**
	 * @param sequence_code the sequence_code to set
	 */
	public void setSequenceCode(String sequence_code) {
		this.sequence_code = sequence_code;
	}

	/**
	 * @return the orgID
	 */
	public String getRefSeqGI() {
		return refSeqGI;
	}

	/**
	 * @param orgID the orgID to set
	 */
	public void setRefSeqGI(String refSeqGI) {
		this.refSeqGI = refSeqGI;
	}

	/**
	 * @return the locus_gene_note
	 */
	public String getLocus_gene_note() {
		return locus_gene_note;
	}

	/**
	 * @param locus_gene_note the locus_gene_note to set
	 */
	public void setLocus_gene_note(String locus_gene_note) {
		this.locus_gene_note = locus_gene_note;
	}

	/**
	 * @return the gene
	 */
	public String getGene() {
		return gene;
	}

	/**
	 * @param gene the gene to set
	 */
	public void setGene(String gene) {
		this.gene = gene;
	}

	/**
	 * @return the uniprotStar
	 */
	public ConcurrentHashMap<String, Boolean> getUniprotStatus() {
		return uniprotStatus;
	}

	/**
	 * @param uniprotStar the uniprotStar to set
	 */
	public void setUniprotStatus(ConcurrentHashMap<String, Boolean> uniprotStatus) {
		this.uniprotStatus = uniprotStatus;
	}

	/**
	 * @param dataRetrieved
	 */
	public void setDataRetrieved(boolean dataRetrieved) {

		this.dataRetrieved = dataRetrieved; 
	}

	/**
	 * @param dataRetrieved
	 */
	public boolean isDataRetrieved() {

		return this.dataRetrieved; 
	}

	/**
	 * @return the taxonomyMap
	 */
	public ConcurrentHashMap<String, String[]> getTaxonomyMap() {
		return taxonomyMap;
	}

	/**
	 * @param taxonomyMap the taxonomyMap to set
	 */
	public void setTaxonomyMap(ConcurrentHashMap<String, String[]> taxonomyMap) {
		this.taxonomyMap = taxonomyMap;
	}
	
	
	/**
	 * @param accessionNumber
	 * @return
	 */
	public double getBits(String accessionNumber){
		
		for (String id : this.getBits().keySet())			
			if(id.contains(accessionNumber))
				return this.getBits().get(id);

		return -1;
	}

	/**
	 * @param results
	 * @param locus
	 * @return
	 */
	public double getEvalue(String locus){
		
		double eValue = 0;
		
		for (String id : this.getEValue().keySet())			
			if(id.contains(locus))				
				return this.getEValue().get(id);

		return eValue;
	}

	/**
	 * @return the ncbiLocusTag
	 */
	public String getNcbiLocusTag() {
		return ncbiLocusTag;
	}

	/**
	 * @param ncbiLocusTag the ncbiLocusTag to set
	 */
	public void setNcbiLocusTag(String ncbiLocusTag) {
		this.ncbiLocusTag = ncbiLocusTag;
	}

	/**
	 * @return the uniProtEntryID
	 */
	public String getUniProtEntryID() {
		return uniProtEntryID;
	}

	/**
	 * @param uniProtEntryID the uniProtEntryID to set
	 */
	public void setUniProtEntryID(String uniProtEntryID) {
		this.uniProtEntryID = uniProtEntryID;
	}

	/**
	 * @return the uniprotLocusTag
	 */
	public String getUniprotLocusTag() {
		return uniprotLocusTag;
	}

	/**
	 * @param uniprotLocusTag the uniprotLocusTag to set
	 */
	public void setUniprotLocusTag(String uniprotLocusTag) {
		this.uniprotLocusTag = uniprotLocusTag;
	}

	@Override
	public String toString() {
		return "HomologuesData [locus_Tag size =" + locusTags.size() + ", sequences size ="
				+ sequences.size() + ", organism=" + organism + ", taxonomy="
				+ taxonomy + ", product=" + product + ", calculated_mol_wt="
				+ calculated_mol_wt + ", definition=" + definition
				+ ", blastLocusTag=" + blastLocusTag + ", organelles="
				+ organelles + ", genes=" + genes.size() + ", eValue=" + eValue.size()
				+ ", bits=" + bits + ", ecnumber=" + ecnumber + ", locusID="
				+ locusIDs + ", organismID=" + organismID + ", fastaSequence="
				+ fastaSequence + ", chromosome=" + chromosome + ", organelle="
				+ organelle + ", locus_protein_note=" + locus_protein_note
				+ ", locus_tag=" + locusTag + ", locus_gene_note="
				+ locus_gene_note + ", gene=" + gene + ", organismTaxa="
				+ Arrays.toString(organismTaxa) + ", sequence_code="
				+ sequence_code + ", uniprotStatus=" + uniprotStatus
				+ ", dataRetrieved=" + dataRetrieved + ", taxonomyMap="
				+ taxonomyMap + ", refSeqGI=" + refSeqGI + ", ncbiLocusTag="
				+ ncbiLocusTag + ", uniProtEntryID=" + uniProtEntryID
				+ ", uniprotLocusTag=" + uniprotLocusTag + "]";
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @param ecNumbers
	 */
	public void setEntryUniprotECnumbers(String ecNumbers) {
		
		this.ecNumbers = ecNumbers;
	}

	/**
	 * @param uniprotReviewStatus
	 */
	public void setEntryUniProtStarred(String uniprotReviewStatus) {
		
		this.uniprotReviewStatus = uniprotReviewStatus;
	}

	/**
	 * @return the uniprotReviewStatus
	 */
	public String getEntryUniProtStarred() {
		return uniprotReviewStatus;
	}

	/**
	 * @return the ecNumbers
	 */
	public String getEntryUniprotECnumbers() {
		return ecNumbers;
	}

	public Map<String, Double> geteValue() {
		return eValue;
	}

	public void seteValue(Map<String, Double> eValue) {
		this.eValue = eValue;
	}

	public String getUniprotReviewStatus() {
		return uniprotReviewStatus;
	}

	public void setUniprotReviewStatus(String uniprotReviewStatus) {
		this.uniprotReviewStatus = uniprotReviewStatus;
	}

	public String getEcNumbers() {
		return ecNumbers;
	}

	public void setEcNumbers(String ecNumbers) {
		this.ecNumbers = ecNumbers;
	}

	public String getBlastProgram() {
		return blastProgram;
	}

	public void setBlastProgram(String blastProgram) {
		this.blastProgram = blastProgram;
	}

	public String getBlastVersion() {
		return blastVersion;
	}

	public void setBlastVersion(String blastVersion) {
		this.blastVersion = blastVersion;
	}

	public String getBlastDatabaseIdentifier() {
		return blastDatabaseIdentifier;
	}

	public void setBlastDatabaseIdentifier(String blastDatabaseIdentifier) {
		this.blastDatabaseIdentifier = blastDatabaseIdentifier;
	}

	public boolean isNoSimilarity() {
		return isNoSimilarity;
	}

	public void setNoSimilarity(boolean isNoSimilarity) {
		this.isNoSimilarity = isNoSimilarity;
	}

	public void setSequence_code(String sequence_code) {
		this.sequence_code = sequence_code;
	}

	public Map<String, Double> getIdentity() {
		return identity;
	}

	public void setIdentity(Map<String, Double> identity) {
		this.identity = identity;
	}

	public Map<String, Double> getPositives() {
		return positives;
	}

	public void setPositives(Map<String, Double> positives) {
		this.positives = positives;
	}

	public Map<String, Double> getTarget_coverage() {
		return target_coverage;
	}

	public void setTarget_coverage(Map<String, Double> target_coverage) {
		this.target_coverage = target_coverage;
	}

	public Map<String, Double> getQuery_coverage() {
		return query_coverage;
	}

	public void setQuery_coverage(Map<String, Double> query_coverage) {
		this.query_coverage = query_coverage;
	}

}