/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.services.annotation.loaders;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologuesData;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;
import pt.uminho.ceb.biosystems.merlin.services.annotation.AnnotationEnzymesServices;
import pt.uminho.ceb.biosystems.merlin.utilities.DatabaseProgressStatus;
import pt.uminho.ceb.biosystems.merlin.utilities.Utilities;

/**
 * @author oDias
 *
 */
public class LoadSimilarityResultstoDatabase {

	final static Logger logger = LoggerFactory.getLogger(LoadSimilarityResultstoDatabase.class);

	private AnnotationEnzymesHomologuesData homologyDataClient;
	private int organism_s_key, homologues_s_key,
	geneHomology_s_key, undo_geneHomology_s_key, homologySetupID;
	private String databaseID, version, program, query; 
	private HashMap<String, List<Integer>> prodOrg;
	private boolean loaded;
	private int maxNumberOfAlignments;
	private double eVal;
	private AtomicBoolean cancel;

	private boolean hmmerSearch;

	private String matrix;

	private String gapCosts;

	private short wordSize;

	private String locusTag;

	private boolean isNoSimilarity;
	private Map<String,AbstractSequence<?>> sequences;

	private String workspaceName;


	/**
	 * @param homologyData
	 * @param expectedVal
	 * @param maxNumberOfAlignments
	 * @param cancel
	 * @param hmmerSearch
	 * @param statement
	 */
	public LoadSimilarityResultstoDatabase(String workspaceName, AnnotationEnzymesHomologuesData homologyData, double expectedVal, int maxNumberOfAlignments,
			AtomicBoolean cancel, boolean hmmerSearch, Map<String,AbstractSequence<?>> sequences) {

		this.workspaceName = workspaceName;
		this.maxNumberOfAlignments = maxNumberOfAlignments;
		this.hmmerSearch = hmmerSearch;
		this.homologyDataClient = homologyData;
		this.program = homologyData.getBlastProgram();
		this.version = homologyData.getBlastVersion();
		this.databaseID = homologyData.getBlastDatabaseIdentifier();
		this.isNoSimilarity = homologyData.isNoSimilarity();
		this.query = homologyData.getQuery();
		this.setLoaded(true);
		this.cancel = cancel;
		this.eVal = expectedVal;
		this.locusTag = homologyData.getQuery();
		if(homologyData.getLocusTag()!=null) 
			this.locusTag = homologyData.getLocusTag();
		this.sequences = sequences;
	}

	/**
	 * @param homologyData
	 * @param project
	 * @param cancel
	 * @param statement
	 */
	public LoadSimilarityResultstoDatabase(String workspaceName, AnnotationEnzymesHomologuesData homologyData, AtomicBoolean cancel) {

		this.workspaceName = workspaceName;
		this.homologyDataClient = homologyData;
		this.program = homologyData.getBlastProgram();
		this.version = homologyData.getBlastVersion();
		this.databaseID = homologyData.getBlastDatabaseIdentifier();
		this.isNoSimilarity =  homologyData.isNoSimilarity();
		this.query = homologyData.getQuery();
		this.setLoaded(true);
		this.cancel = cancel;
	}

	/**
	 * @param locusTag
	 * @param query
	 * @param gene
	 * @param chromosome
	 * @param organelle
	 * @throws Exception 
	 */
	private void loadGene(String locusTag, String query, String gene, String chromosome, String organelle, String uniprot_star) throws Exception {

		this.undo_geneHomology_s_key = -1;
		Boolean uniprot_star_boolean = null;

		if(uniprot_star!= null)
			uniprot_star_boolean = Utilities.get_boolean_string_to_boolean(uniprot_star);

		String uniProtEC = "";
		if(this.homologyDataClient.getEntryUniprotECnumbers()!=null)
			uniProtEC =	this.homologyDataClient.getEntryUniprotECnumbers();

		if(this.isNoSimilarity) {

			int sKey = AnnotationEnzymesServices.getGeneHomologySkey(this.workspaceName, query, this.homologySetupID);

			if(sKey<0) {

				int seqId = Integer.valueOf(this.sequences.get(this.homologyDataClient.getQuery()).getSource());

				sKey = AnnotationEnzymesServices.insertGeneHomologyEntry(this.workspaceName, locusTag, uniProtEC, uniprot_star_boolean, this.homologySetupID, query, DatabaseProgressStatus.NO_SIMILARITY, seqId, null, null);


				//				String query2 = "INSERT INTO enzymes_annotation_geneHomology (locusTag, query, homologySetup_s_key, status, uniprot_star, uniprot_ecnumber) VALUES "
				//						+ " ('"+ locusTag +"','"+ query + "','" +this.homologySetupID+ "','"+DatabaseProgressStatus.NO_SIMILARITY+"',"
				//						+ "'"+uniprot_star_int+"','"+uniProtEC+"');";
				//
				//				sKey = ProjectAPI.executeAndGetLastInsertID(query2, statement);

			}
			this.geneHomology_s_key = sKey;
		}
		else {


			int sKey = AnnotationEnzymesServices.getGeneHomologySkey(this.workspaceName, query, this.homologySetupID);

			if(sKey>0) {

				this.geneHomology_s_key = sKey;
				this.undo_geneHomology_s_key = this.geneHomology_s_key;

				int seqId = Integer.valueOf(this.sequences.get(this.homologyDataClient.getFastaSequence()).getSource());

				AnnotationEnzymesServices.updateGeneHomologyEntry(this.workspaceName, locusTag, uniProtEC, uniprot_star_boolean, this.homologySetupID, query, DatabaseProgressStatus.PROCESSING, seqId, chromosome, organelle, sKey);

				// Uma pequena alteração foi feita ao transformar em hibernate: 
				// -> a sKey passou a ser a chave em vez da "query"
				//				String query2 = "UPDATE enzymes_annotation_geneHomology " +
				//						"SET locusTag='"+ locusTag +"', " +
				//						"homologySetup_s_key = '" +this.homologySetupID+ "'," +
				//						"gene = '" +gene+ "'," +
				//						"chromosome = '" +chromosome+ "'," +
				//						"organelle = '" +organelle+ "'," +
				//						"uniprot_star = '" +uniprot_star_boolean+ "'," +
				//						"status = '"+DatabaseProgressStatus.PROCESSING+"'" +
				//						"WHERE query = '"+query+"';";  <- AQUI
				//
				//				ProjectAPI.executeQuery(query2, statement);

			}
			else {

				int seqId = Integer.valueOf(this.sequences.get(this.homologyDataClient.getQuery()).getSource());

				sKey = AnnotationEnzymesServices.insertGeneHomologyEntry(this.workspaceName, locusTag, uniProtEC, uniprot_star_boolean, 
						this.homologySetupID, query, DatabaseProgressStatus.NO_SIMILARITY, 
						seqId, chromosome, organelle);

				this.geneHomology_s_key = sKey;
				this.undo_geneHomology_s_key = this.geneHomology_s_key;

			}
		}
	}

	/**
	 * @param locusTag
	 * @throws Exception 
	 */
	private void updataGeneStatus(String locusTag) throws Exception {

		AnnotationEnzymesServices.updateGeneHomologyStatus(this.workspaceName, locusTag, DatabaseProgressStatus.PROCESSED);

		//			String query = "UPDATE enzymes_annotation_geneHomology SET status ='"+DatabaseProgressStatus.PROCESSED+
		//					"' WHERE locusTag ='" + locusTag +"';";
		//
		//			ProjectAPI.executeQuery(query, statement);

	}


	/**
	 * @param organism
	 * @param taxonomy
	 * @param myOrganismTaxonomy
	 * @throws SQLException
	 */
	private void loadOrganism(String organism, String taxonomy, String myOrganismTaxonomy, String locus) throws Exception {

		try {

			if(organism==null)
				organism = "unknown";

			if(taxonomy==null) 
				taxonomy = "unknown";

			int sKey = AnnotationEnzymesServices.getSKeyFromOrganism(this.workspaceName, organism);

			if(sKey > 0) 
				this.organism_s_key = sKey;

			else {

				sKey = AnnotationEnzymesServices.insertEnzymesAnnotationOrganism(this.workspaceName,
						organism,
						taxonomy,
						this.rankTaxonomy(taxonomy.concat("; "+organism), myOrganismTaxonomy));


				this.organism_s_key = sKey;
			}
		}
		catch (NullPointerException e) {
			logger.error(e.getMessage());
			logger.error("loadOrganism error null pointer locus:"+locus+"\t\torganism:\t"+organism+"\t\ttax:\t"+taxonomy+"\t\tmyOrgTax:\t"+myOrganismTaxonomy);
			logger.error("Organism map: {}", this.homologyDataClient.getOrganism().toString());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @param taxonomy
	 * @param myOrganismTaxonomy
	 * @return
	 */
	private Integer rankTaxonomy(String taxonomy, String myOrganismTaxonomy) {

		if(taxonomy== null || myOrganismTaxonomy == null) {

			return 0;
		}
		else {

			String[] taxonomyArray = taxonomy.split(";");
			String[] myOrganismTaxonomyArray = myOrganismTaxonomy.split(";");

			for(int i = 0; i< myOrganismTaxonomyArray.length; i++) {

				if(taxonomyArray.length>i) {

					if(!myOrganismTaxonomyArray[i].equals(taxonomyArray[i]))
						return i;
				}
				else {

					return taxonomyArray.length;
				}
			}
			return myOrganismTaxonomyArray.length;
		}
	}

	/**
	 * @param locusID
	 * @param calculated_mw
	 * @param productRank
	 * @param referenceID
	 * @return
	 * @throws Exception
	 */
	private Map<String, Integer> loadHomologues(String locusID, Float calculated_mw, Map<String, Integer> productRank,  String referenceID) throws Exception {

		String product = this.homologyDataClient.getProduct().get(referenceID);
		String organelle = this.homologyDataClient.getOrganelles().get(referenceID);

		if(product==null)
			product= this.homologyDataClient.getDefinition().get(referenceID).replace("'", "\\'");

		if(organelle!= null && organelle.length()>99)
			organelle = organelle.subSequence(0, 99).toString();

		int sKey;

		// the definition field is being used like the reference id. DO NOT CHANGE THIS
		sKey = AnnotationEnzymesServices.getHomologuesSkey(this.workspaceName, referenceID);

		if(sKey > 0) {

			this.homologues_s_key = sKey;
		}
		else {

			Boolean uniprot_star_boolean = null;

			if(this.homologyDataClient.getUniprotStatus().containsKey(referenceID)) {

				String star = this.homologyDataClient.getUniprotStatus().get(referenceID).toString();
				uniprot_star_boolean = Utilities.get_boolean_string_to_boolean(star);
			}

			sKey = AnnotationEnzymesServices.insertGeneHomologues(this.workspaceName,this.organism_s_key, locusID, referenceID, calculated_mw, product, organelle, uniprot_star_boolean);

			this.homologues_s_key = sKey;
		}

		if(productRank.containsKey(product)) {

			productRank.put(product, productRank.get(product)+1);
			List<Integer> orgKey = this.prodOrg.get(product);
			orgKey.add(this.organism_s_key);
			this.prodOrg.put(product, orgKey);
		}
		else {

			productRank.put(product, 1);
			List<Integer> orgKey = new ArrayList<>();
			orgKey.add(this.organism_s_key);
			this.prodOrg.put(product, orgKey);
		}

		return productRank;
	}


	/**
	 * @param ecNumberRank
	 * @param ecOrg
	 * @param locus
	 * @return
	 * @throws SQLException
	 */
	private Map<Set<String>, Integer> loadECNumbers(Map<Set<String>, Integer> ecNumberRank, Map<Set<String>, List<Integer>> ecOrg, String locus) throws Exception {

		if(this.homologyDataClient.getEcnumber().keySet().contains(locus)) {

			Set<String> ecnumbers = new HashSet<String>();

			for(int e =0; e<this.homologyDataClient.getEcnumber().get(locus).length; e++) {

				this.loadECNumbers(this.homologyDataClient.getEcnumber().get(locus)[e]);

				if(!ecnumbers.contains(this.homologyDataClient.getEcnumber().get(locus)[e]))
					ecnumbers.add(this.homologyDataClient.getEcnumber().get(locus)[e]); 
			}

			if(ecNumberRank.containsKey(ecnumbers)) {

				List<Integer> orgKey = ecOrg.get(ecnumbers); 
				ecNumberRank.put(ecnumbers, ecNumberRank.get(ecnumbers)+1);
				orgKey.add(this.organism_s_key);
				ecOrg.put(ecnumbers, orgKey);
			}
			else {

				List<Integer> orgKey = new ArrayList<>();
				ecNumberRank.put(ecnumbers, 1);
				orgKey.add(this.organism_s_key);
				ecOrg.put(ecnumbers, orgKey);
			}
		}
		return ecNumberRank;		
	}

	/**
	 * @param referenceID
	 * @param gene
	 * @param eValue
	 * @param bits
	 * @param targetCoverage 
	 * @param queryCoverage 
	 * @param positives 
	 * @param identity 
	 * @throws SQLException
	 */
	private void load_geneHomology_has_homologues(String databaseName, String referenceID, String gene, Float eValue, Float bits, float identity, float positives, float queryCoverage, float targetCoverage) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).load_geneHomology_has_homologues(referenceID, gene, eValue, bits, geneHomology_s_key, homologues_s_key, identity, positives, queryCoverage, targetCoverage);
	}


	/**
	 * @param ecNumber
	 * @throws SQLException
	 */
	private void loadECNumbers(String ecNumber) throws Exception {

		//this.statement.execute("LOCK tables ecNumber read,homology_has_ecNumber read;");

		int sKey = AnnotationEnzymesServices.getecNumberSkey(this.workspaceName, ecNumber);

		if(sKey<0) {

			sKey = AnnotationEnzymesServices.insertEcNumberEntry(this.workspaceName, ecNumber);

		}
		int ecnumber_s_key = sKey;

		boolean exists = AnnotationEnzymesServices.checkHomologuesHasEcNumber(this.workspaceName, this.homologues_s_key, ecnumber_s_key);

		if(!exists) {

			AnnotationEnzymesServices.insertEnzymesAnnotationHomologuesHasEcNumber(this.workspaceName, this.homologues_s_key, ecnumber_s_key);
		}
	}

	/**
	 * @param databaseID
	 * @param program
	 * @param version
	 * @throws Exception 
	 */
	private void loadhomologySetup(String databaseID, String program, String version) throws Exception {

		int sKey = AnnotationEnzymesServices.getHomologySetupSkeyByAttributes(this.workspaceName, databaseID, program,
				this.eVal, this.getMatrix(), this.getWordSize(), this.getGapCosts(), this.maxNumberOfAlignments, version);

		if(sKey<0) {

			sKey = AnnotationEnzymesServices.insertHomologySetup(this.workspaceName, databaseID, program, this.eVal, 
					this.getMatrix(), this.getWordSize(), this.getGapCosts(), this.maxNumberOfAlignments, version);
		}
		this.homologySetupID = sKey;
		//this.statement.execute("UNLOCK tables;");
	}



	public String getMatrix() {

		return this.matrix;
	}

	public String getGapCosts() {

		return this.gapCosts;
	}

	public short getWordSize() {

		return this.wordSize;
	}

	public void setMatrix(String matrix) {

		this.matrix = matrix;
	}

	public void setGapCosts(String gapCosts) {

		this.gapCosts = gapCosts;
	}

	public void setWordSize(short wordSize) {

		this.wordSize = wordSize;
	}

	/**
	 * @param databaseID
	 * @param program
	 * @param version
	 * @throws Exception 
	 */
	private void loadHmmerSetup(String databaseID, String program, String version) throws Exception {

		//this.statement.execute("LOCK tables homologySetup read;");

		int sKey = AnnotationEnzymesServices.getHomologySetupSkeyByAttributes2(this.workspaceName, databaseID, program, this.eVal, this.maxNumberOfAlignments, version);

		if(sKey<0) {

			sKey = AnnotationEnzymesServices.insertHomologySetup2(this.workspaceName, databaseID, program, this.eVal, this.maxNumberOfAlignments, version);
		}
		this.homologySetupID = sKey;
		//this.statement.execute("UNLOCK tables;");
	}

	/**
	 * @param pd
	 * @throws SQLException
	 */
	private void loadProductRank(String databaseName, Map<String, Integer> pd) throws Exception {

		//		for(String product : pd.keySet()) {
		//
		//			String aux = product;
		//
		//			int sKey = ProjectAPI.getProductRankSkey(this.geneHomology_s_key, aux, pd.get(product), statement);
		//
		//			if(sKey<0) {
		//
		//				String query = "INSERT INTO productRank (geneHomology_s_key, productName, rank) " +
		//						"VALUES("+ this.geneHomology_s_key +",'"
		//						+ product + "','" +pd.get(product)+ "');";
		//
		//				int prodkey = ProjectAPI.executeAndGetLastInsertID(query, statement);
		//
		//				for(int orgKey : this.prodOrg.get(product)) {
		//
		//					sKey = ProjectAPI.getProductRankHasOrganismSkey(prodkey, orgKey, statement);
		//
		//					if(sKey<0)
		//						query = "INSERT INTO productRank_has_organism (productRank_s_key, organism_s_key) "
		//								+ "VALUES("+ prodkey+ "," + orgKey+ ");";
		//					ProjectAPI.executeQuery(query, statement);
		//				}
		//			}
		//		}
		//		//this.statement.execute("UNLOCK tables;");
		InitDataAccess.getInstance().getDatabaseService(databaseName).loadProductRank(pd, geneHomology_s_key, prodOrg);
	}

	/**
	 * @param ecn
	 * @param ecOrg
	 * @throws Exception 
	 * @throws IOException 
	 */
	private void loadECNumberRank(Map<Set<String>, Integer> ecn, Map<Set<String>, List<Integer>> ecOrg) throws IOException, Exception {

		//this.statement.execute("LOCK tables ecNumberRank read, ecNumberRank_has_organism read;");

		for(Set<String> ecnumber: ecn.keySet()) {

			String concatEC="";
			for(String s:ecnumber) {

				if(concatEC.isEmpty())
					concatEC = s;
				else
					concatEC += ", " + s;
			}


			int sKey = AnnotationEnzymesServices.getEcNumberRankSkey(this.workspaceName, this.geneHomology_s_key,  concatEC, ecn.get(ecnumber));

			if(sKey<0) {

				sKey = AnnotationEnzymesServices.insertEcNumberRank(this.workspaceName, this.geneHomology_s_key, concatEC, ecn.get(ecnumber));

				if(sKey > 0) {
					int eckey = sKey;

					for(int orgKey : ecOrg.get(ecnumber)) {

						Boolean exists = AnnotationEnzymesServices.FindEnzymesAnnotationEcNumberRankHasOrganismByIds(this.workspaceName, eckey, orgKey);

						if(!exists) { 

							AnnotationEnzymesServices.InsertEnzymesAnnotationEcNumberRankHasOrganism(this.workspaceName, sKey, orgKey);
						}
					}
				}
			}
		}
		//this.statement.execute("UNLOCK tables;");
	}

	/**
	 * @throws Exception 
	 */
	public void loadData(String database) throws Exception {

		try {

			String program = "hmmer";
			if(!this.hmmerSearch)
				program = this.program;

			
			boolean exists = AnnotationEnzymesServices.loadGeneHomologyData(this.workspaceName, this.homologyDataClient.getQuery(), program);
			



			if(exists) {

				logger.warn("gene {} already processed!", this.locusTag);
			}
			else {

				String star;
				if(this.homologyDataClient.getQuery() != null && this.homologyDataClient.getUniprotStatus()!=null && 
						this.homologyDataClient.getUniprotStatus().containsKey(this.homologyDataClient.getQuery())) {

					star = this.homologyDataClient.getUniprotStatus().get(this.homologyDataClient.getQuery()).toString();
				}
				else {
					try {

						star =  this.homologyDataClient.getEntryUniProtStarred()+"";
					}
					catch(NullPointerException e) {

						//e.printStackTrace();
						star = null;
					}
				}


				if(this.hmmerSearch)
					this.loadHmmerSetup(this.databaseID, this.program, this.version);
				else
					this.loadhomologySetup(this.databaseID, this.program, this.version);

				
				if(this.isNoSimilarity) {

					String locusTag = this.query;

					if(this.locusTag != null) {

						locusTag = this.locusTag;
					}

					
					this.loadGene(locusTag, this.query, null, null, null, star);
					//					this.loadFastaSequence(this.homologyDataClient.getFastaSequence());
					
				}
				else {

					String locusTag;

					if(this.locusTag==null) {

						if(this.homologyDataClient.getLocus_gene_note()==null) {

							locusTag=this.homologyDataClient.getLocus_protein_note();	
						}
						else {

							if(this.homologyDataClient.getLocusIDs().get(0).matches("[A-Za-z]*\\d*\\s+") && !this.locusTag.contains(":") ) {//if the locus tag contains spaces and not: 

								locusTag=this.homologyDataClient.getLocusIDs().get(0);
							}
							else {

								String[] locus = this.homologyDataClient.getLocus_gene_note().split(":");
								locusTag = locus[locus.length-1];
								locus = locusTag.split(";");
								locusTag = locus[0];
							}
						}
					}
					else {

						locusTag=this.locusTag;
					}

					
					this.loadGene(locusTag, this.query, this.homologyDataClient.getGene(), this.homologyDataClient.getChromosome(), this.homologyDataClient.getOrganelle(), star);
					//					this.loadFastaSequence(this.homologyDataClient.getFastaSequence());
					
					
					Map<String, Integer> productRank = new HashMap<String,Integer>();
					this.prodOrg = new HashMap<>();
					Map<Set<String>, Integer> ecNumberRank = new HashMap<Set<String>,Integer>();
					Map<Set<String>, List<Integer>> ecOrg = new HashMap<>();
					String myOrganismTaxonomy="";

					myOrganismTaxonomy = this.homologyDataClient.getOrganismTaxa()[1].concat("; "+this.homologyDataClient.getOrganismTaxa()[0]);
					
					
					this.loadOrganism(this.homologyDataClient.getOrganismTaxa()[0], this.homologyDataClient.getOrganismTaxa()[1],myOrganismTaxonomy,"origin organism");
					logger.info("homologues total" + this.homologyDataClient.getLocusIDs().size());

					for(int l = 0 ; l< this.homologyDataClient.getLocusIDs().size(); l++) {

						String locus = this.homologyDataClient.getLocusIDs().get(l);

						if(locus!=null && !locus.trim().isEmpty()) {

							if(!this.cancel.get()) {

								Float calculateMW = (float) 0.0;
								if(this.homologyDataClient.getCalculated_mol_wt().get(locus)!= null)
									calculateMW = Float.valueOf(this.homologyDataClient.getCalculated_mol_wt().get(locus));

								String blastLocusTagID = this.homologyDataClient.getBlastLocusTag().get(locus);			
								//
								if (blastLocusTagID==null)
									blastLocusTagID=locus;				

								String organism = this.homologyDataClient.getOrganism().get(locus),
										taxonomy =	this.homologyDataClient.getTaxonomy().get(locus);

								
								this.loadOrganism(organism,taxonomy,myOrganismTaxonomy,locus);
								

								float eValue = (float) this.homologyDataClient.getEValue().get(locus).doubleValue();

								if(this.homologyDataClient.getEValue().containsKey(locus) && this.homologyDataClient.getBits().containsKey(locus)) {

									float bits = (float) this.homologyDataClient.getBits().get(locus).doubleValue();
									float identity = (float) this.homologyDataClient.getIdentity().get(locus).doubleValue();
									float positives = (float) this.homologyDataClient.getPositives().get(locus).doubleValue();
									float queryCoverage = (float) this.homologyDataClient.getQuery_coverage().get(locus).doubleValue();
									float targetCoverage = (float) this.homologyDataClient.getTarget_coverage().get(locus).doubleValue();

									String gene= this.homologyDataClient.getGenes().get(locus);

									if(this.homologyDataClient.getBits().get(locus)<0) {

										bits = 999999f;
										eValue=0f;
									}

									if(this.eVal >= eValue) {

										
										productRank = loadHomologues(blastLocusTagID, calculateMW, productRank, locus);
										
										
										ecNumberRank = loadECNumbers(ecNumberRank, ecOrg, locus);
										

										this.load_geneHomology_has_homologues(database, locus, gene, eValue, bits, identity, positives, queryCoverage, targetCoverage);
										
									}
								}
							}
						}
					}


					if(!this.cancel.get()) {

						
						this.loadProductRank(database, productRank);


						
						if (!ecNumberRank.isEmpty()) {

							
							this.loadECNumberRank(ecNumberRank,ecOrg);		
							
						}
						
						
						this.updataGeneStatus(locusTag);
						
					}
				}

				if(this.cancel.get()) {

					this.setLoaded(false);
				}
			}

		}
		catch (Exception e) {

			logger.error(e.getMessage());
			e.printStackTrace();
			this.deleteEntry();
			this.setLoaded(false);
		}
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void deleteEntry() throws Exception {

		if(this.undo_geneHomology_s_key>0) {

			AnnotationEnzymesServices.removeModelGeneHomologyBySKey(this.workspaceName, this.undo_geneHomology_s_key);
		}
	}


	/**
	 * @param loaded the loaded to set
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * @return the loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}

}
