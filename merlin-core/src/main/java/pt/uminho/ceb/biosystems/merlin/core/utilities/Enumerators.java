package pt.uminho.ceb.biosystems.merlin.core.utilities;

/**
 * @author Oscar Dias
 *
 */
public class Enumerators {

	/**
	 * @author Oscar Dias
	 *
	 */
	public enum CompartmentsTool {

		PSort,
		LocTree,
		WoLFPSORT;

		public String getTool(String tool) {

			if(tool.equals("PSort"))
				return "PSORTb v3.0";

			if(tool.equals("LocTree"))
				return "LocTree3";

			if(tool.equals("WoLFPSORT"))
				return "WoLFPSORT";

			return tool;
		}
	}

	/**
	 * @author Oscar Dias
	 *
	 */
	public enum HomologySearchServer {

		EBI,
		HMMER
	}

	/**
	 * @author Oscar Dias
	 *
	 */
	public enum HomologySearchType {

		BLASTP,
		BLASTX,
		HMMER
	}


	/**
	 * @author ODias
	 *
	 */
	public static enum IntegrationType {

		MODEL,
		MERGE,
		ANNOTATION

	}

	/**
	 * @author ODias
	 *
	 */
	public enum EbiRemoteDatabasesEnum{

		uniprotkb, 
		uniprotkb_swissprot, 
		uniprotkb_swissprotsv, 
		uniprotkb_trembl, 
		uniprotkb_archaea, 
		uniprotkb_arthropoda, 
		uniprotkb_bacteria
	}


	/**
	 * @author ODias
	 *
	 */
	public enum HmmerRemoteDatabasesEnum {

		uniprotkb,
		swissprot,
		uniprotrefprot,
		pdb,
		//		ensemblgenomes, 
		//		ensembl, 
		qfo,
		rp75,
		rp55,
		rp35,
		rp15

		//http://hmmer-web-docs.readthedocs.io/en/latest/searches.html?highlight=databases
	}

	/**
	 * @author ODias
	 *
	 */
	public enum BlastMatrix{
		AUTOSELECTION,
		BLOSUM62,
		BLOSUM45,
		BLOSUM80,
		PAM30,
		PAM70
	}

	/**
	 * @author ODias
	 *
	 */
	public enum BlastProgram{
		//ncbi-
		blastp,
		//ncbi-
		//		blastn,
		blastx,
		//		tblastn,
		//		tblastx
	}

	public enum NumberofAlignments {

		five (5),
		ten (10){
			@Override
			public String toString(){
				return "10";
			}
		},
		twenty (20){
			@Override
			public String toString(){
				return "20";
			}
		},
		fifty (50){
			@Override
			public String toString(){
				return "50";
			}
		},
		one_hundred (100){
			@Override
			public String toString(){
				return "100";
			}
		},
		one_hundred_and_fifty (150){
			@Override
			public String toString(){
				return "150";
			}
		},
		two_hundred (200){
			@Override
			public String toString(){
				return "200";
			}
		},
		two_hundred_and_fifty (250){
			@Override
			public String toString(){
				return "250";
			}
		},
		five_hundred (500){
			@Override
			public String toString(){
				return "500";
			}
		},
		seven_hundred_and_fifty (750){
			@Override
			public String toString(){
				return "750";
			}
		},
		one_thousand (1000){
			@Override
			public String toString(){
				return "1000";
			}
		};		

		private final int index;   

		NumberofAlignments(int index) {
			this.index = index;
		}

		public int index() { 
			return index; 
		}

		@Override
		public String toString(){
			return "5";
		}
	}

	/**
	 * @author ODias
	 *
	 */
	public enum NcbiRemoteDatabasesEnum{
		nr,
		swissprot,
		yeast,
		refseq_protein,
		ecoli,
		pdb
	}

	/**
	 * @author ODias
	 *
	 */
	public enum WordSize{

		auto (-1),
		wordSize_2 (2),
		wordSize_3 (3);

		private final int index;   

		WordSize(int index) {
			this.index = index;
		}

		public int index() { 
			return index; 
		}
	}

	public enum GeneticCode {

		Standard (1),
		Vertebrate_Mitochondrial (2),
		Mitochondrial (3),
		MoldProtoCoelMitoMycoSpiro (4),
		Invertebrate_Mitochondrial (5),
		Ciliate_Macronuclear (6),
		Echinodermate_Mitochondrial (9),
		Alt_Ciliate_Macronuclear (10),
		Eubacterial (11),
		Alternative_Yeast (12),
		Ascidian_Mitochondrial (13),
		Flatworm_Mitochondrial (14),
		Blepharisma_Macronuclear (15);

		private final int index;   

		GeneticCode(int index) {
			this.index = index;
		}

		public int index() { 
			return index; 
		}
	}

	/**
	 * @author ODias
	 *
	 */
	public enum SchemaType {

		IGNORE("ignore"),			
		ALL_INFORMATION("all information") {

			@Override
			public String toString(){
				return "all information";
			}
		},
		MODEL("model"){

			@Override
			public String toString(){
				return "model";
			}
		},
		ENZYMES_ANNOTATION("enzymes annotation"){

			@Override
			public String toString(){
				return "enzymes annotation";
			}
		},
		COMPARTMENTS_ANNOTATION("compartment annotation"){

			@Override
			public String toString(){
				return "compartment annotation";
			}
		},
		INTERPRO_ANNOTATION("interpro annotation"){

			@Override
			public String toString(){
				return "interpro annotation";
			}
		}
		;

		private String schemaType;

		private SchemaType(String schemaType){
			this.schemaType = schemaType;
		}

		public String sourceName(){
			return this.schemaType;
		}

		@Override
		public String toString(){
			return "ignore";
		}
	}

	/**
	 * Types of database information
	 * 
	 * @author Oscar Dias
	 *
	 */
	public enum SourceType {

		HOMOLOGY,
		KEGG,
		KO,
		TRANSPORTERS,
		MANUAL,
		EBIOMASS,
		DRAINS,
		GENBANK,
		GFF,
		SBML,
		TRANSYT,
		BiGG

		//ENUM('HOMOLOGY','MANUAL','KEGG','TRANSPORTERS','KO')
	}

	/**
	 * GFF3 Source
	 * 
	 * @author Antonio Dias
	 *
	 */
	public enum GFFSource {

		UniProt,
		//		NCBI,
		Other

	}

	public enum BlastSource {

		NCBI,
		EBI
	}

	public enum FileExtension {

		OUT,
		TXT
	}

	public enum reversibilitySource {

		ModelSEED,
		Zeng
	}

	public enum reversibilityTemplate {

		GramNegative,
		GramPositive,
		Microbial,
		Mycobacteria,
		Plant,
		Fungi,
		Human
	}

	public enum EBIOMASSTEMPLATE {

		Archea,
		Cyano,
		GramPositive,
		GramNegative,
		Mold,
		Yeast,
		Custom
	}

	/**
	 * @author davidelagoa
	 *
	 */
	public enum ExportType{

		REPORTS,
		INTEGRATION

	}
	
	/**
	 * @author diogolima
	 *
	 */
	public enum InterproStatus{

		PROCESSED,
		PROCESSING
	}
	
	/**
	 * @author diogolima
	 *
	 */
	public enum HomologyStatus{

		PROCESSED,
		PROCESSING,
		NO_SIMILARITY
	}
	
	public enum Compartments {

		inside{
			@Override
			public String getAbbreviation(){
				return "in";
			}
			
			@Override
			public String getName(){
				return "inside";
			}
		},
		outside{
			@Override
			public String getAbbreviation(){
				return "out";
			}
			
			@Override
			public String getName(){
				return "outside";
			}
		};

		public String getAbbreviation() {
			return null;
		}

		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public enum ModuleType {

		Complex,
		Pathway
	}

	/**
	 * @author Oscar
	 *
	 */
	public static enum AlignmentScoreType {
	
		ALIGNMENT,
		IDENTITY,
		SIMILARITY
	}

	/**
	 * @author ODias
	 * available methods for alignment
	 */
	public static enum AlignmentPurpose {
	
		TRANSPORT,
		ORTHOLOGS,
		OTHER
	}

	/**
	 * @author ODias
	 * available methods for alignment
	 */
	public static enum Method {
		
		Blast,
		SmithWaterman,
		NeedlemanWunsch
	}

	/**
	 * @author ODias
	 *
	 */
	public static enum Matrix {
	
		BLOSUM62 ("blosum62.txt"),
		PAM30 ("pam30.txt"),
		PAM70 ("pam70.txt"),
		BLOSUM80 ("blosum80.txt"),
		BLOSUM45 ("blosum45.txt");
		private String path;
	
		private Matrix(String path) {
			this.path = path;
		}
	
		public String getPath(){
			return this.path;
		}
	}
	
	public enum SequenceType {
		
		PROTEIN{
			@Override
			public String toString(){
				return "Protein";
			}
		},
		GENOMIC_DNA{
			@Override
			public String toString(){
				return "DNA";
			}
		},
		CDS_DNA{
			@Override
			public String toString(){
				return "DNA_cds";
			}
		},
		RNA{
			@Override
			public String toString(){
				return "RNA";
			}
		},
//		MRNA{
//			@Override
//			public String toString(){
//				return "mRNA";
//			}
//		},
		RRNA{
			@Override
			public String toString(){
				return "rRNA";
			}
		},
		TRNA{
			@Override
			public String toString(){
				return "tRNA";
			}
		}
	}
	
	public enum Pathways {
		
		
		TRANSPORTERS{
			@Override
			public String getName(){
				return "Transporters pathway";
			}
			
			@Override
			public String getCode(){
				return "T0001";
			}
		},
		DRAINS{
			@Override
			public String getName(){
				return "Drains pathway";
			}
			
			@Override
			public String getCode(){
				return "D0001";
			}
		},
		BIOMASS{
			@Override
			public String getName(){
				return "Biomass Pathway";
			}
			
			@Override
			public String getCode(){
				return "B0001";
			}
		},
		SPONTANEOUS{
			@Override
			public String getName(){
				return "Spontaneous";
			}
			
			@Override
			public String getCode(){
				return "SPONT";
			}
		},
		NON_ENZYMATIC{
			@Override
			public String getName(){
				return "Non enzymatic";
			}
			
			@Override
			public String getCode(){
				return "NOENZ";
			}
		};

		public String getName() {
			return null;
		}

		public String getCode() {
			return null;
		}
	}
	
public enum Plugins {
		
		GPRS{
			@Override
			public String toString(){
				return "merlin-gpr";
			}
		},
		BIOCOISO{
			@Override
			public String toString(){
				return "merlin-biocoiso";
			}
		},
		BIOMASS{
			@Override
			public String toString(){
				return "merlin-biomass";
			}
		},
		COMPARTMENTS{
			@Override
			public String toString(){
				return "merlin-compartments";
			}
		},
		DRAFT_RECONSTRUCTION{
			@Override
			public String toString(){
				return "merlin-draft-reconstruction";
			}
		},
		EXPORTER{
			@Override
			public String toString(){
				return "merlin-exporter";
			}
		},
		INTERPRO{
			@Override
			public String toString(){
				return "merlin-interpro";
			}
		},
		REGULATORY{
			@Override
			public String toString(){
				return "merlin-regulatory";
			}
		},
		SAMPLER{
			@Override
			public String toString(){
				return "merlin-sampler";
			}
		},
		SETTINGS{
			@Override
			public String toString(){
				return "merlin-settings";
			}
		},
		TRANSYT{
			@Override
			public String toString(){
				return "merlin-transyt";
			}
		},
		WORKSPACE_COMPATIBILITY{
			@Override
			public String toString(){
				return "merlin-workspace-compatibility";
			}
		},
		AUTOMATIC_ANNOTATION{
			@Override
			public String toString(){
				return "merlin-automatic-annotation";
			}
		},
		ADDONS{
			@Override
			public String toString(){
				return "merlin-addons";
			}
		},
		AIBENCH_CORE{
			@Override
			public String toString(){
				return "merlin-aibench-core";
			}
		},
		PLUGIN_MANAGER{
			@Override
			public String toString(){
				return "merlin-aibench-pluginmanager";
			}
		},
		AIBENCH_WORKBENCH{
			@Override
			public String toString(){
				return "merlin-aibench-workbench";
			}
		}	
	}
	

}



