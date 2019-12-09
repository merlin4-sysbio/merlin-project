/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.core.containers.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;

/**
 * @author ODias
 *
 */
public class GeneContainer implements EntityContainer {
	
	private String externalIdentifier;
	private String name;
	private	List<String> dblinks;
	private List<String> orthologues;
	private	String genes;
	private	List<String> modules;
	private String  left_end_position, right_end_position, aasequence, ntsequence;
	private Integer idGene, aalength, ntlength, idSequence;
	private String sequenceType;
	private String transcriptionDirection;
	private String booleanRule;
	private String locusTag;
	private SourceType origin;
	private List<CompartmentContainer> localization;
	
	/**
	 * 
	 */
	public GeneContainer(Integer idGene) {
		this.setIdGene(idGene);
	}
	
	/**
	 * 
	 */
	public GeneContainer(Integer idGene, String locusTag) {
		this.setIdGene(idGene);
		this.setLocusTag(locusTag);
	}
	
	/**
	 * 
	 */
	public GeneContainer(String externalIdentifier) {
		this.setExternalIdentifier(externalIdentifier);
	}

	/**
	 * @param entry the entry to set
	 */
	public void setExternalIdentifier(String externalIdentifier) {
		this.externalIdentifier = externalIdentifier;
	}

	/**
	 * @return the entry
	 */
	public String getExternalIdentifier() {
		return externalIdentifier;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param dblinks the dblinks to set
	 */
	public void setDblinks(List<String> dblinks) {
		this.dblinks = dblinks;
	}

	/**
	 * @return the dblinks
	 */
	public List<String> getDblinks() {
		return dblinks;
	}

	/**
	 * @param genes the genes to set
	 */
	public void setGenes(String genes) {
		this.genes = genes;
	}

	/**
	 * @return the genes
	 */
	public String getGenes() {
		return genes;
	}

	@Override
	public List<String> getNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNames(List<String> names) {
		// TODO Auto-generated method stub
		
	}

//	/**
//	 * @param chromosome_name the chromosome_name to set
//	 */
//	public void setChromosome_name(String chromosome_name) {
//		this.chromosome_name = chromosome_name;
//	}
//
//	/**
//	 * @return the chromosome_name
//	 */
//	public String getChromosome_name() {
//		return chromosome_name;
//	}

	/**
	 * @param left_end_position the left_end_position to set
	 */
	public void setLeft_end_position(String left_end_position) {
		this.left_end_position = left_end_position;
	}

	/**
	 * @return the left_end_position
	 */
	public String getLeft_end_position() {
		return left_end_position;
	}

	/**
	 * @param right_end_position the right_end_position to set
	 */
	public void setRight_end_position(String right_end_position) {
		this.right_end_position = right_end_position;
	}

	/**
	 * @return the right_end_position
	 */
	public String getRight_end_position() {
		return right_end_position;
	}

	/**
	 * @param aasequence the aasequence to set
	 */
	public void setAasequence(String aasequence) {
		this.aasequence = aasequence;
	}

	/**
	 * @return the aasequence
	 */
	public String getAasequence() {
		return aasequence;
	}

	/**
	 * @param aalength the aalength to set
	 */
	public void setAalength(Integer aalength) {
		this.aalength = aalength;
	}

	/**
	 * @return the aalength
	 */
	public Integer getAalength() {
		return aalength;
	}

	/**
	 * @param ntsequence the ntsequence to set
	 */
	public void setNtsequence(String ntsequence) {
		this.ntsequence = ntsequence;
	}

	/**
	 * @return the ntsequence
	 */
	public String getNtsequence() {
		return ntsequence;
	}

	/**
	 * @param ntlength the ntlength to set
	 */
	public void setNtlength(Integer ntlength) {
		this.ntlength = ntlength;
	}

	/**
	 * @return the ntlength
	 */
	public Integer getNtlength() {
		return ntlength;
	}

	/**
	 * @param orthologues the orthologues to set
	 */
	public void setOrthologues(List<String> orthologues) {
		this.orthologues = orthologues;
	}

	/**
	 * @return the orthologues
	 */
	public List<String> getOrthologues() {
		return orthologues;
	}

	/**
	 * @return the modules
	 */
	public List<String> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GeneContainer ["
				+ (externalIdentifier != null ? "entry=" + externalIdentifier + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (dblinks != null ? "dblinks=" + dblinks + ", " : "")
				+ (orthologues != null ? "orthologues=" + orthologues + ", "
						: "")
				+ (genes != null ? "genes=" + genes + ", " : "")
				+ (modules != null ? "modules=" + modules + ", " : "")
//				+ (chromosome_name != null ? "chromosome_name="
//						+ chromosome_name + ", " : "")
				+ (left_end_position != null ? "left_end_position="
						+ left_end_position + ", " : "")
				+ (right_end_position != null ? "right_end_position="
						+ right_end_position + ", " : "")
				+ (aasequence != null ? "aasequence=" + aasequence + ", " : "")
				+ (aalength != null ? "aalength=" + aalength + ", " : "")
				+ (ntsequence != null ? "ntsequence=" + ntsequence + ", " : "")
				+ (ntlength != null ? "ntlength=" + ntlength : "") + "]";
	}

	public Integer getIdGene() {
		return idGene;
	}

	public void setIdGene(Integer idGene) {
		this.idGene = idGene;
	}

	public String getSequenceType() {
		return sequenceType;
	}

	public void setSequenceType(String sequenceType) {
		this.sequenceType = sequenceType;
	}

	/**
	 * @return the transcriptionDirection
	 */
	public String getTranscriptionDirection() {
		return transcriptionDirection;
	}

	/**
	 * @param transcriptionDirection the transcriptionDirection to set
	 */
	public void setTranscriptionDirection(String transcriptionDirection) {
		this.transcriptionDirection = transcriptionDirection;
	}

	/**
	 * @return the boolenaRule
	 */
	public String getBoolenaRule() {
		return booleanRule;
	}

	/**
	 * @param boolenaRule the boolenaRule to set
	 */
	public void setBoolenaRule(String boolenaRule) {
		this.booleanRule = boolenaRule;
	}

	/**
	 * @return the locusTag
	 */
	public String getLocusTag() {
		return locusTag;
	}

	/**
	 * @param locusTag the locusTag to set
	 */
	public void setLocusTag(String locusTag) {
		this.locusTag = locusTag;
	}

	/**
	 * @return the localization
	 */
	public List<CompartmentContainer> getLocalization() {
		return localization;
	}

	/**
	 * @param localization the localization to set
	 */
	public void setLocalization(List<CompartmentContainer> localization) {
		this.localization = localization;
	}

	/**
	 * @return the origin
	 */
	public SourceType getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(SourceType origin) {
		this.origin = origin;
	}

	/**
	 * @return the idSequence
	 */
	public Integer getIdSequence() {
		return idSequence;
	}

	/**
	 * @param idSequence the idSequence to set
	 */
	public void setIdSequence(Integer idSequence) {
		this.idSequence = idSequence;
	}

	
}
