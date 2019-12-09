package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologyStatus;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberListDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberRankDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberrankHasOrganismDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationGeneHomologyHasHomologuesDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationGenehomologyDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologuesDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologuesHasEcNumberDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologydataDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologysetupDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationOrganismDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationProductListDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationProductrankDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationProductrankHasOrganismDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationScorerconfigDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumber;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberRank;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomologyHasHomologues;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologuesHasEcNumber;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologyData;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologySetup;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationOrganism;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductList;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductRank;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductRankHasOrganism;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationScorerConfig;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResults;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IAnnotationEnzymesService;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class AnnotationEnzymesServiceImpl implements IAnnotationEnzymesService {

	private IEnzymesAnnotationEcNumberDAO ecNumberDAO;
	private IEnzymesAnnotationEcNumberRankDAO ecNumberRankDAO;
	private IEnzymesAnnotationProductrankDAO productRankDAO;
	private IEnzymesAnnotationScorerconfigDAO scorerconfigDAO;
	private IEnzymesAnnotationHomologydataDAO homologydataDAO;
	private IEnzymesAnnotationProductListDAO productlistDAO;
	private IEnzymesAnnotationEcNumberListDAO ecNumberlistDAO;
	private IEnzymesAnnotationOrganismDAO organismDAO;
	private IEnzymesAnnotationProductrankHasOrganismDAO productRankHasOrganism;
	private IEnzymesAnnotationHomologuesDAO homologuesDAO;
	private IEnzymesAnnotationHomologysetupDAO setupDAO;
	private IEnzymesAnnotationGenehomologyDAO homologyDAO;
	private IEnzymesAnnotationHomologuesHasEcNumberDAO homologuesHasEcNumberDAO;
	private IEnzymesAnnotationEcNumberrankHasOrganismDAO ecNumberRankHasOrganismDAO;
	private IEnzymesAnnotationGeneHomologyHasHomologuesDAO hasHomologuesDAO;
	private IModelGeneDAO modelGeneDAO;
	private I_InterproResultsDAO interproDAO;

	
	public AnnotationEnzymesServiceImpl(IEnzymesAnnotationEcNumberDAO EcNumberDAO,
			IEnzymesAnnotationEcNumberRankDAO EcNumberRankDAO, IEnzymesAnnotationProductrankDAO productRankDAO,
			IEnzymesAnnotationScorerconfigDAO scorerconfigDAO, IEnzymesAnnotationHomologydataDAO homologydataDAO,
			IEnzymesAnnotationProductListDAO productlistDAO, IEnzymesAnnotationEcNumberListDAO EcNumberlistDAO,
			IEnzymesAnnotationOrganismDAO organismDAO, IEnzymesAnnotationProductrankHasOrganismDAO productRankHasOrganism,
			IEnzymesAnnotationHomologuesDAO homologuesDAO,
			IEnzymesAnnotationHomologysetupDAO setupDAO, IEnzymesAnnotationGenehomologyDAO homologyDAO,
			IEnzymesAnnotationHomologuesHasEcNumberDAO homologuesHasEcNumberDAO,
			IEnzymesAnnotationEcNumberrankHasOrganismDAO ecNumberRankHasOrganismDAO, IModelGeneDAO modelGeneDAO, 
			I_InterproResultsDAO interproDAO, IEnzymesAnnotationGeneHomologyHasHomologuesDAO hasHomologuesDAO){
		
		this.ecNumberDAO = EcNumberDAO;
		this.ecNumberRankDAO = EcNumberRankDAO;
		this.productRankDAO = productRankDAO;
		this.scorerconfigDAO = scorerconfigDAO;
		this.homologydataDAO = homologydataDAO;
		this.productlistDAO = productlistDAO;
		this.ecNumberlistDAO = EcNumberlistDAO;
		this.productRankHasOrganism  = productRankHasOrganism;
		this.homologuesDAO = homologuesDAO;
		this.setupDAO = setupDAO;
		this.organismDAO = organismDAO;
		this.homologyDAO = homologyDAO;
		this.homologuesHasEcNumberDAO = homologuesHasEcNumberDAO;
		this.ecNumberRankHasOrganismDAO = ecNumberRankHasOrganismDAO;
		this.modelGeneDAO = modelGeneDAO;
		this.interproDAO = interproDAO;
		this.hasHomologuesDAO = hasHomologuesDAO;
	
	}

	public List<String[]> getProgram(String query) throws Exception {
		return this.ecNumberRankDAO.getEnzymesAnnotationEcNumberrankAttributesByQuery(query);
				
	}

	public List<String[]> getProductRankData(String locus) throws Exception {
		return this.productRankDAO.getEnzymesAnnotationProductrankAttributesByLocusTag(locus);
	}

	public String getLastestUsedBlastDatabase() throws Exception {
		return this.scorerconfigDAO.getScorerConfigBlastDB();
	}

	public List<String[]> getCommittedHomologyData() throws Exception {
		List<EnzymesAnnotationHomologyData> list = this.homologydataDAO.getAllEnzymesAnnotationHomologyData();
		List<String[]> result = new ArrayList<String[]>();

		if(list != null) {
			for (EnzymesAnnotationHomologyData x  : list) {
				String[] res = new String[9];
				
				res[0] = String.valueOf(x.getSKey());
				res[1] = String.valueOf(x.getEnzymesAnnotationGeneHomology().getSKey());
				res[2] = (String) x.getLocusTag();
				res[3] = (String) x.getGeneName();
				res[4] = (String) x.getProduct();
				res[5] = (String) x.getEcNumber();
				res[6] = String.valueOf(x.getSelected());
				res[7] = (String) x.getChromosome();
				res[8] = (String) x.getNotes();
				
				result.add(res);
		}
		}
		return result;	
	}

	public boolean productListHasKey(Integer skey) throws Exception {
		List<EnzymesAnnotationProductList> res = this.productlistDAO.getAllEnzymesAnnotationProductListByHomologyDataSKey(skey);
		boolean exists = false;
		if (res != null) {
			exists = true;
		}
		
		return exists;	
	}
	
	@Override
	public boolean ecNumberListHasKey(Integer skey) throws Exception {
		Map<String, Serializable> eqRestrictions = new HashMap<String, Serializable>();
		eqRestrictions.put("enzymesAnnotationHomologydata.sKey", skey);
		return this.ecNumberlistDAO.checkByAttributes(eqRestrictions);
	}

	@Override
	public List<String[]> getDataFromecNumberRank() throws Exception {
		return this.ecNumberRankDAO.getEnzymesAnnotationEcNumberrankAttributes();
	}

	public List<String[]> getEcRank() throws Exception {
		return this.ecNumberRankDAO.getEnzymesAnnotationEcNumberrankSKeyAndTaxRank();
	}

	public Integer getMaxTaxRank() throws Exception {
		return this.organismDAO.getEnzymesAnnotationOrganismMaxTaxRank();
	}

	public List<String[]> getProductRank() throws Exception {
		return this.productRankDAO.getEnzymesAnnotationProductrankAttributes();
	}

	public List<String[]> getTaxRank() throws Exception {
		return this.productRankHasOrganism.getEnzymesAnnotationProductrankHasOrganismSKeyAndTaxRank();
	}

//	public List<String[]> getProductRank2() throws Exception {
//		return this.productRankDAO.getEnzymesAnnotationProductrankAttributes();
//	}

	public boolean checkHomologuesHasEcNumber(int homologues_s_key, int EcNumber_s_key) throws Exception {
		boolean exists = false;
		
		List<EnzymesAnnotationHomologuesHasEcNumber> list = this.homologuesHasEcNumberDAO.getAllEnzymesAnnotationHomologuesHasEcNumberByAttributes(homologues_s_key, EcNumber_s_key);
		if (list != null && list.size() > 0) {
			exists = true; 
		}
		return exists;
	}

	public int getSKeyFromOrganism(String organism) throws Exception {
		int res = -1;
		
		List<EnzymesAnnotationOrganism> list = this.organismDAO.getAllEnzymesAnnotationOrganismByOrganism(organism);
		if (list != null && !list.isEmpty())
			res = list.get(0).getSKey();
		
		return res;
	}

	@Override
	public int getecNumberSkey(String EcNumber) throws Exception {
		int res = -1;
		
		List<EnzymesAnnotationEcNumber> list = this.ecNumberDAO.getAllEnzymesAnnotationEcNumberByEcNumber(EcNumber);
		if( list != null && !list.isEmpty())
			res = list.get(0).getSKey();
		return res;
	}

	public int getProductRankSkey(int skey, String name, Integer rank) throws Exception {
		int res = -1;
		List<EnzymesAnnotationProductRank> list = this.productRankDAO.getAllEnzymesAnnotationProductRankByAttributes(skey, name, rank);
		
		if(list != null && !list.isEmpty())
			res = list.get(0).getSKey();
		
		return res;
	}

	public int getProductRankHasOrganismSkey(int prodKey, int orgKey) throws Exception {
		int res = -1;
		List<EnzymesAnnotationProductRankHasOrganism> list = this.productRankHasOrganism.getAllEnzymesAnnotationProductRankHasOrganismByAttributes(prodKey, orgKey);
		
		if(list != null && !list.isEmpty())
			res = list.get(0).getId().getEnzymesAnnotationProductRankSKey();
		
		return res;
	}

	public String[] getAllGenus() throws Exception {
		 List<String> allGenus = new ArrayList<String>();
		 
		 List<String> list = this.organismDAO.getEnzymesAnnotationOrganismTaxonomy();
		 
		 if (list != null) {  
			for (String x: list){
			 String[] entry = x.split(";"); 
			 
			 if(entry[entry.length-1].trim().contains("\\s")){
	            	allGenus.add(entry[entry.length-1].trim().split("\\s")[0]);
	            }
	        else {
	            	allGenus.add(entry[entry.length-1].trim());
	            }
	        }
		 }
			 
		 String[] result = new String[allGenus.size()];
		 
		 int i = 0;
         
	        for(String text : allGenus) {
	             
	            result[i] = text.trim();
	            i++;
	        }
		 
		return result;
	}

	public Double getBlastEValue(String database) throws Exception {
		Double eValue = null;
		ArrayList<Double> eValueList = new ArrayList<Double>();
		Double toRemove = 1000.0;
		List<String> res = null;
		
		 if(database == null) {
			 res = this.setupDAO.getModelHomologySetupEvalue();
		 }
		 else {
			 res = this.setupDAO.getModelHomologySetupEvalueByDatabaseId(database);
		 }
		 
		 if (res != null) {
			 for (String x : res) {
				 eValueList.add(Double.valueOf(x)); 
				 
			 }
		 }
		 
		 eValue = Collections.max(eValueList);
		 
	     while (eValueList.remove(toRemove)){
	         
	        eValue = Collections.max(eValueList);
	    }
	     
		return eValue; 
	}

	public void resetDatabaseScorer(String blastDatabase) throws Exception {
		this.scorerconfigDAO.removeEnzymesAnnotationScorerconfigByDatabase(blastDatabase);
		
	}
	
	@Override
	public void resetAllScorers() throws Exception {
//		List<EnzymesAnnotationScorerConfig> data = this.scorerconfigDAO.getAllEnzymesAnnotationScorerConfig();
		
//		this.scorerconfigDAO.removeEnzymesAnnotationScorerConfigList(data);
		this.scorerconfigDAO.deleteAllScorerConfig();
	}
	
	@Override
	public void updateScorerConfigSetLatest(boolean latest) throws Exception {
		List<EnzymesAnnotationScorerConfig> data = this.scorerconfigDAO.getAllEnzymesAnnotationScorerConfig();
		
		for(EnzymesAnnotationScorerConfig config : data) {
			config.getId().setLatest(latest);
		
			this.scorerconfigDAO.update(config);
		}
	}
	
	@Override
	public void updateScorerConfigSetLatestByBlastDatabase(boolean latest, String blastDatabase) throws Exception {
		List<EnzymesAnnotationScorerConfig> data = this.scorerconfigDAO.findBySingleAttribute("id.databaseName", blastDatabase);
		
		for(EnzymesAnnotationScorerConfig config : data) {
			config.getId().setLatest(latest);
		
			this.scorerconfigDAO.update(config);
		}
	}

	public List<String> getCommitedScorerData(String blastDatabase) throws Exception {
		ArrayList<String> result = new ArrayList<String>();

		List<EnzymesAnnotationScorerConfig> res = this.scorerconfigDAO.getAllScorerConfigByBlastDB(blastDatabase);

		if (res != null)
			for (EnzymesAnnotationScorerConfig x : res) {
			
			result.add(String.valueOf(x.getId().getThreshold()));
			result.add(String.valueOf(x.getId().getUpperThreshold()));
			result.add(String.valueOf(x.getId().getBalanceBh()));
			result.add(String.valueOf(x.getId().getAlpha()));
			result.add(String.valueOf(x.getId().getBeta()));
			result.add(String.valueOf(x.getId().getMinHomologies()));
			result.add(String.valueOf(x.getId().getDatabaseName()));
			}

		return result;
	}

	public void setBestAlphaFound(String db) throws Exception {
		this.scorerconfigDAO.updateEnzymesAnnotationScorerconfigByDB(db);
		
	}

	public List<String> bestAlphasFound(boolean bestAlpha) throws Exception {
		return this.scorerconfigDAO.getDatabaseByBestAlpha(bestAlpha);
	}

	public void setLastestUsedBlastDatabase(String db) throws Exception {
		//falta uma
		this.scorerconfigDAO.updateEnzymesAnnotationLatestByDB(db);
		
	}

	public int getEcNumberRankSkey(int geneHomology_s_key, String concatEC, int EcNumber) throws Exception {
		int res = -1; 
		
		List<EnzymesAnnotationEcNumberRank> list = this.ecNumberRankDAO.getAllEnzymesAnnotationEcNumberRankAttributes(geneHomology_s_key, concatEC, EcNumber);
		
		if(list != null && list.size()>0)
			res = list.get(0).getSKey();
		
		return res;
	}
	
	@Override
	public void loadProductRank(Map<String, Integer> pd, Integer geneHomology_s_key, Map<String, List<Integer>> prodOrg) throws Exception {

		for(String product : pd.keySet()) {
			
			Integer productSKey =  this.productRankDAO.getEnzymesAnnotationProductRankIDByAttributes(geneHomology_s_key, product, pd.get(product));
			if(productSKey == null) {
				EnzymesAnnotationGeneHomology geneHomology = this.homologyDAO.findById(geneHomology_s_key);
				if(geneHomology != null) {
					productSKey = this.productRankDAO.insertEnzymesAnnotationProductrankSkeyAndProductNameAndRank(geneHomology, product, pd.get(product));
				}
			}
			for(int orgKey : prodOrg.get(product)) {

				int sKey = this.getProductRankHasOrganismSkey(productSKey, orgKey);

				if(sKey<0)
					this.productRankHasOrganism.insertEnzymesAnnotationProductrankHasOrganismAttributes(productSKey, orgKey);
			}
		}
	}

	@Override
	public List<String[]> getEnzymesAnnotationEcNumberrankAttributesByLocusTag(String query) throws Exception {
		return this.ecNumberRankDAO.getEnzymesAnnotationEcNumberrankAttributesByLocusTag(query);
	}
	
	@Override
	public Integer insertEcNumberEntry(String ecNumber) throws Exception {
		
		EnzymesAnnotationEcNumber ec = new EnzymesAnnotationEcNumber();
		
		ec.setEcNumber(ecNumber);
		
		return (Integer) this.ecNumberDAO.save(ec);
		
	}
	
	@Override
	public Integer insertEcNumberRank(Integer geneHomology_s_key, String concatEC, Integer ecnumber) throws Exception {
		
		EnzymesAnnotationGeneHomology geneHomology = this.homologyDAO.getEnzymesAnnotationGeneHomology(geneHomology_s_key);
		return this.ecNumberRankDAO.insertEcNumberRank(concatEC, ecnumber, geneHomology);
		
	}
	
	@Override
	public Boolean FindEnzymesAnnotationEcNumberRankHasOrganismByIds(Integer sKey, Integer orgKey) throws Exception {
		
		return this.ecNumberRankHasOrganismDAO.FindEnzymesAnnotationEcNumberRankHasOrganismByIds(sKey, orgKey);
	}
	
	public void InsertEnzymesAnnotationEcNumberRankHasOrganism(Integer sKey, Integer orgKey) {

		this.ecNumberRankHasOrganismDAO.InsertEnzymesAnnotationEcNumberRankHasOrganism(sKey, orgKey);
	}

	
	@Override
	public Integer getGeneHomologySkey(String query, Integer homologySetupID) throws Exception {
		
		return this.homologyDAO.getGeneHomologySkey(query, homologySetupID);	
	}
	
	
	@Override
	public Integer insertEnzymesAnnotationOrganism(String organism, String taxonomy, Integer taxrank) throws Exception {
		
		return this.organismDAO.insertEnzymesAnnotationOrganism(organism, taxonomy, taxrank);	
	}
	
	
	@Override
	public Integer getHomologuesSkey(String referenceID) throws Exception {
		
		return this.homologuesDAO.getHomologuesSkey(referenceID);
	}
	
	
	@Override
	public void insertEnzymesAnnotationHomologuesHasEcNumber(int homologues_s_key, int ecnumber_s_key) throws Exception {
		
		this.homologuesHasEcNumberDAO.insertEnzymesAnnotationHomologuesHasEcNumber(homologues_s_key, ecnumber_s_key);
	}
	
	
	@Override
	public int getHomologySetupSkeyByAttributes(String databaseID, String program, double eVal, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version) throws Exception {
		
		return this.setupDAO.getHomologySetupSkeyByAttributes(databaseID, program, eVal, matrix, wordSize, gapCosts, maxNumberOfAlignments, version);
	}
	
	@Override
	public Integer insertHomologySetup(String databaseID, String program, double eVal, String matrix, short wordSize,
			String gapCosts, int maxNumberOfAlignments, String version) throws Exception {
		
		return this.setupDAO.insertHomologySetup(databaseID, program, eVal, matrix, wordSize, gapCosts, maxNumberOfAlignments, version);

	}

	@Override
	public int getHomologySetupSkeyByAttributes2(String databaseID, String program, double eVal, int maxNumberOfAlignments, String version) throws Exception {
		
		return this.setupDAO.getHomologySetupSkeyByAttributes2(databaseID, program, eVal, maxNumberOfAlignments, version);
	}
	
	@Override
	public Integer insertHomologySetup2(String databaseID, String program, double eVal, int maxNumberOfAlignments, String version) throws Exception {
		
		return this.setupDAO.insertHomologySetup2(databaseID, program, eVal, maxNumberOfAlignments, version);

	}
	
	@Override
	public void removeGeneHomologyBySKey(Integer skey) {
		this.homologyDAO.removeGeneHomologyBySKey(skey);
	}
	
	@Override
	public void removeGeneHomologyByQuery(String query) {
		
		List<EnzymesAnnotationGeneHomology> enzymes = this.homologyDAO.findBySingleAttribute("query", query);
		
		if(enzymes != null)
			for(EnzymesAnnotationGeneHomology enzyme : enzymes)
				this.homologyDAO.removeEnzymesAnnotationGeneHomology(enzyme);
	}

	@Override
	public String[] getAllOrganisms() {
		
		List<EnzymesAnnotationOrganism> organisms = this.organismDAO.getAllEnzymesAnnotationOrganism();
		
		if(organisms != null) {
			String[] res = new String[organisms.size()];
			
			for(int i = 0; i < organisms.size(); i++)
				res[i] = organisms.get(i).getOrganism();
			
			return res;
		}
		
		return null;
	}
	
	@Override
	public Map<Integer, String> getQueriesBySKey(){
		return this.homologyDAO.getSKeyAndQuery();					
	}

	@Override
	public Map<String, Integer> getQueries(){
		return this.homologyDAO.getQueryAndSkey();					
	}

	@Override
	public Map<String, String> loadGeneLocusFromHomologyData(String query) throws Exception {
		return this.homologyDAO.getLocusTagAndGeneByQuery(query);
	}

	@Override
	public String getLocusTagFromHomologyData(String sequence_id) throws Exception {
		return this.homologyDAO.getLocusTagbyQuery(sequence_id);

	}

	@Override
	public Integer getGeneHomologySkey(Integer skey, String query) throws Exception {
		List<EnzymesAnnotationGeneHomology> list = this.homologyDAO.getAllEnzymesAnnotationGeneHomologyByQueryAndHomologySetupId(skey, query);

		int res = -1;
		if(list != null) {
			for (EnzymesAnnotationGeneHomology x : list) {
				res = x.getSKey();
			}
		}
		return res;
	}

	@Override
	public boolean loadGeneHomologyData(String query, String program) throws Exception {
		boolean exists = false;
		List<EnzymesAnnotationGeneHomology> list = this.homologyDAO.getAllEnzymesAnnotationGeneHomologyByQueryAndProgram(query, program);

		if (list != null && list.size()>0) {
			exists = true;
		}
		return exists;
	}
	
	@Override
	public void deleteSetOfGenes(Set<Integer> deleteGeneslist) throws Exception {
		for (Integer skey : deleteGeneslist) {
			this.homologyDAO.removeGeneHomologyBySKey(skey);
		}
	}

	@Override
	public Map<Integer, String> getDatabaseLocus() throws Exception {
		return this.homologyDAO.getEnzymesAnnotationGenehomologySKeyAndLocusTag();
	}

	@Override
	public void deleteDuplicatedQuerys(String query) throws Exception { 
		this.homologyDAO.removeModelGeneHomologyByQuery(query);

	}

	@Override
	public Map<Integer, String> getLocusKeys() throws Exception {
		return this.homologyDAO.getSequenceIds();
	}

	@Override
	public Map<String, Boolean> getHomologyAvailabilities(Integer skey) throws Exception {  
		List<String> res = this.homologyDAO.getsequenceIdBySKey(skey);

		String query = "";

		if (res != null)
			query = res.get(0);

		boolean gene_hmmerAvailable = false;
		boolean gene_blastPAvailable = false;
		boolean gene_blastXAvailable = false;

		List<String> res2 = this.setupDAO.getEnzymesAnnotationHomologysetupProgramByQuery(query);

		if (res2 != null)
			for (String x : res2){

				if(x.equalsIgnoreCase("hmmer"))
					gene_hmmerAvailable = true;

				if(x.equalsIgnoreCase("ncbi-blastp")
						|| x.equalsIgnoreCase("blastp")
						|| x.equalsIgnoreCase("ebi-blastp"))
					gene_blastPAvailable = true;

				if(x.equalsIgnoreCase("ncbi-blastx")
						|| x.equalsIgnoreCase("blastx")
						|| x.equalsIgnoreCase("ebi-blastx"))
					gene_blastXAvailable = true;
			}

		Map<String, Boolean> ret = new HashMap<String, Boolean>();
		ret.put("gene_blastXAvailable", gene_blastXAvailable);
		ret.put("gene_blastPAvailable", gene_blastPAvailable);
		ret.put("gene_hmmerAvailable", gene_hmmerAvailable);

		return ret;
	}

	@Override
	public List<InterproResults> getInterproAvailability(Integer skey) throws Exception { //conf return
		List<String> res1 = this.homologyDAO.getsequenceIdBySKey(skey);

		String query = "";
		if (res1 != null && !res1.isEmpty()) {
			query = res1.get(0);
		}

		List<InterproResults> result = this.interproDAO.getAllInterproResultsByQuery(query);	

		return result;
	}

	@Override
	public String getHomologySequence(Integer skey) throws Exception {

		List<String> q = this.homologyDAO.getsequenceIdBySKey(skey);

		String query = "";
		if(q != null && !q.isEmpty()) {
			query = q.get(0);
		}

		String seq = this.modelGeneDAO.getModelSequenceByQueryAndSequenceType(query, SequenceType.PROTEIN);

		return seq;
	}

	@Override
	public List<List<String>> getHomologySetup(Integer skey) throws Exception { 
		EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology = this.homologyDAO.findById(skey);
		EnzymesAnnotationHomologySetup setup = enzymesAnnotationGeneHomology.getEnzymesAnnotationHomologySetup();
		List<List<String>> res = new ArrayList<>();

		List<String> re = new ArrayList<>();
		re.add(setup.getProgram());
		re.add(setup.getProgramVersion());
		re.add(setup.getDatabaseId());
		re.add(setup.getEvalue());
		re.add(setup.getMatrix());
		re.add(setup.getWordSize());
		re.add(setup.getGapCosts());
		re.add(setup.getMaxNumberOfAlignments().toString());

		res.add(re);

		return res;
	}
	
	@Override
	public void load_geneHomology_has_homologues(String referenceID, String gene, Float eValue, Float bits, Integer geneHomology_s_key, Integer homologues_s_key) {

		if(gene!=null) {

			List<EnzymesAnnotationGeneHomologyHasHomologues> res = this.hasHomologuesDAO.getAllEnzymesAnnotationGeneHomologyHasHomologuesByAttributes(geneHomology_s_key, homologues_s_key, referenceID, eValue, bits, gene);

			boolean exists = false;
			if (res != null)
				exists = true;

			if(!exists)
				this.hasHomologuesDAO.insertEnzymesAnnotationGeneHomologyHasHomologues(geneHomology_s_key, referenceID, gene, eValue, bits, homologues_s_key);

		}
		else {

			List<EnzymesAnnotationGeneHomologyHasHomologues> res2 = this.hasHomologuesDAO.getAllEnzymesAnnotationGeneHomologyHasHomologuesByAttributes(geneHomology_s_key, homologues_s_key, null, null, null, null);

			boolean exists = false;
			if (res2 != null && !res2.isEmpty())
				exists = true;

			if(!exists) {

				this.hasHomologuesDAO.insertEnzymesAnnotationGeneHomologyHasHomologues(geneHomology_s_key, referenceID, null, eValue, bits, homologues_s_key);
			}
		}
	}
	
	@Override
	public GeneContainer getGeneHomologyEntryByQuery(String query) {

		EnzymesAnnotationGeneHomology entry = this.homologyDAO.findUniqueByAttribute("query", query);
		
		if(entry == null)
			return null;
		
		GeneContainer gene = new GeneContainer(entry.getQuery());
		
		gene.setLocusTag(entry.getLocusTag());
		gene.setGenes(entry.getGene());
		
		return gene;
	}
	
	@Override
	public List<String> getDuplicatedQuerys() {

		List<EnzymesAnnotationGeneHomology> list = this.homologyDAO.getAllEnzymesAnnotationGeneHomology();
		
		List<String> allQueries = new ArrayList<>();
		List<String> duplicated = new ArrayList<>();
		
		for(EnzymesAnnotationGeneHomology homology : list) {
			
			String query = homology.getQuery();
			
			if(allQueries.contains(query))
				duplicated.add(query);
			else
				allQueries.add(query);
		}
		
		return duplicated;
	}
	
	@Override
	public Set<Integer> getHomologyGenesSKeyByStatus(HomologyStatus status, String program) {

		return this.homologyDAO.getHomologyGenesSKeyByStatus(status, program);
	}
	
	@Override
	public Set<String> getHomologyGenesQueryByStatus(HomologyStatus status, String program) {

		return this.homologyDAO.getHomologyGenesQueryByStatus(status, program);
	}
	
	@Override
	public Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByAttributes(HomologyStatus status,
			String program, double evalue, String matrix, String wordSize, Integer maxNumberOfAlignments) {
		return this.homologyDAO.getHomologyGenesSKeyAndQueryByAttributes(status, program, evalue, matrix, wordSize, maxNumberOfAlignments);
	}
	
	@Override
	public Pair<Set<Integer>, Set<String>> getHomologyGenesSKeyAndQueryByStatusAndProgramAndDatabaseId(HomologyStatus status,
			String program, String databaseId) {

		return this.homologyDAO.getHomologyGenesSKeyAndQueryByStatusAndProgramAndDatabaseId(status, program, databaseId);
	}
	
	@Override
	public void deleteFromHomologyDataByDatabaseID(String database) {

		Set<EnzymesAnnotationHomologyData> data = new HashSet<>();
		
		if(database != null && !database.isEmpty()) {
		
			EnzymesAnnotationHomologySetup setup = this.setupDAO.findUniqueByAttribute("databaseId", database);
			
			for(EnzymesAnnotationGeneHomology homology : setup.getEnzymesAnnotationGeneHomologies())
				data.addAll(homology.getEnzymesAnnotationHomologyDatas());
		
		}
		else {
			data = new HashSet<>(this.homologydataDAO.getAllEnzymesAnnotationHomologyData());
		}
		
		for(EnzymesAnnotationHomologyData homologyData : data)
			this.homologydataDAO.removeEnzymesAnnotationHomologyData(homologyData);
		
	}
	
	@Override
	public Integer insertHomologyData(Integer geneHomologySKey, String locusTag, String geneName,
			String product, String ecnumber, Boolean selected, String chromossome, String notes) {
		
		EnzymesAnnotationGeneHomology geneHomology = this.homologyDAO.getEnzymesAnnotationGeneHomology(geneHomologySKey);

		return this.homologydataDAO.insertHomologyData(geneHomology, locusTag, geneName, product, ecnumber, selected, chromossome, notes);
		
	}
	
	@Override
	public Set<Integer> getSKeyForAutomaticAnnotation(String blastDatabase) {

		return this.homologyDAO.getSKeyForAutomaticAnnotation(blastDatabase);
	}
	
	@Override
	public Set<String> getAllBlastDatabases() {

		Set<String> databases = new HashSet<>();
		
		List<EnzymesAnnotationHomologySetup> list = this.setupDAO.getAllEnzymesAnnotationHomologysetup();
		
		for(EnzymesAnnotationHomologySetup setup : list)
			databases.add(setup.getDatabaseId());
	
		return databases;
	}
	
	@Override
	public boolean deleteGenesWithoutECRankAndProdRank() throws Exception{		//this could be faster if impletemented with nested selects but I was getting cast String to Integer errors - Davide, TODO check later
		
		Set<Integer> toDelete = new HashSet<>();
		
		List<EnzymesAnnotationEcNumberRank> ecRanks = this.ecNumberRankDAO.getAllEnzymesAnnotationEcNumberRank();
		List<Integer> existingEcRankHasOrganism = this.ecNumberRankHasOrganismDAO.getAllEcRankSKeyInEcRankHasOrganism();
		
		for(EnzymesAnnotationEcNumberRank ecRank : ecRanks) {
			
			if(!existingEcRankHasOrganism.contains(ecRank.getSKey())) {
				toDelete.add(ecRank.getEnzymesAnnotationGeneHomology().getSKey());
				this.ecNumberRankDAO.removeEnzymesAnnotationEcNumberRank(ecRank);
			}
		}
		
		List<EnzymesAnnotationProductRank> prodRanks = this.productRankDAO.getAllEnzymesAnnotationProductRank();
		List<Integer> existingProdRankHasOrganism = this.productRankHasOrganism.getAllProdRankSKeyInProdRankHasOrganism();
		
		for(EnzymesAnnotationProductRank prodRank : prodRanks) {
			
			if(!existingProdRankHasOrganism.contains(prodRank.getSKey())) {
				toDelete.add(prodRank.getEnzymesAnnotationGeneHomology().getSKey());
				this.productRankDAO.removeEnzymesAnnotationProductRank(prodRank);
			}
		}
		
		if(toDelete.isEmpty())
			return false;
		
		deleteSetOfGenes(toDelete);
		
		return true;
	}
}
