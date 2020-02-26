package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationGeneHomologyHasHomologuesDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationGenehomologyDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologuesDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologydataDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologysetupDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationOrganismDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproEntryDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproModelDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproXRefDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSequenceDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologues;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologyData;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologySetup;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationOrganism;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproEntry;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResult;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproXRef;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequence;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IHomologyService;
import pt.uminho.ceb.biosystems.merlin.utilities.DatabaseProgressStatus;

public class HomologyServiceImpl implements IHomologyService{

	private IEnzymesAnnotationGenehomologyDAO enzGeneHomologyDAO;
	private IEnzymesAnnotationHomologysetupDAO homologysetupDAO;
	private IEnzymesAnnotationGeneHomologyHasHomologuesDAO hashomologuesDAO;
	private IEnzymesAnnotationHomologydataDAO dataDAO;
	private I_InterproResultsDAO interproResultsDAO;
	private I_InterproResultDAO interproResultDAO;
	private I_InterproEntryDAO interproentryDAO;
	private I_InterproXRefDAO interproxrefDAO;
	private I_InterproModelDAO interpromodelDAO;
	private IEnzymesAnnotationGenehomologyDAO geneHomologyDAO;
	private IModelSequenceDAO modelSequenceDAO;
	private IEnzymesAnnotationOrganismDAO organismDAO;
	private IEnzymesAnnotationHomologuesDAO homologuesDAO;



	public HomologyServiceImpl(IEnzymesAnnotationGenehomologyDAO enzGeneHomologyDAO, IEnzymesAnnotationHomologysetupDAO homologysetupDAO,
			IEnzymesAnnotationGeneHomologyHasHomologuesDAO hashomologuesDAO, IEnzymesAnnotationHomologydataDAO dataDAO,
			I_InterproResultsDAO interproResultsDAO, I_InterproResultDAO interproResultDAO,
			I_InterproEntryDAO interproentryDAO, I_InterproXRefDAO interproxrefDAO,
			I_InterproModelDAO interpromodelDAO, IEnzymesAnnotationGenehomologyDAO geneHomologyDAO,
			IModelSequenceDAO modelSequenceDAO, IEnzymesAnnotationOrganismDAO organismDAO, IEnzymesAnnotationHomologuesDAO homologuesDAO) {

		this.homologuesDAO = homologuesDAO;
		this.organismDAO = organismDAO;
		this.enzGeneHomologyDAO  = enzGeneHomologyDAO;
		this.homologysetupDAO = homologysetupDAO;
		this.hashomologuesDAO = hashomologuesDAO;
		this.dataDAO = dataDAO;
		this.interproResultsDAO = interproResultsDAO;
		this.interproResultDAO = interproResultDAO;
		this.interproentryDAO = interproentryDAO;
		this.interproxrefDAO = interproxrefDAO;
		this.interpromodelDAO = interpromodelDAO;
		this.geneHomologyDAO = geneHomologyDAO;
		this.modelSequenceDAO = modelSequenceDAO;

	}

	@Override
	public Integer insertGeneHomologues(int organismSKey, String locusID, String definition,Float calculatedMw,
			String product,String organelle,Boolean uniprotStar) {


		EnzymesAnnotationHomologues homologue = new EnzymesAnnotationHomologues();

		homologue.setLocusId(locusID);
		homologue.setDefinition(definition);
		homologue.setCalculatedMw(calculatedMw);
		homologue.setProduct(product);
		homologue.setOrganelle(organelle);

		int star = 0;

		if(uniprotStar == null)
			star = -1;
		else if(uniprotStar)
			star = 1;


		homologue.setUniprotStar(star);


		EnzymesAnnotationOrganism enzymesAnnotationOrganism = this.organismDAO.getEnzymesAnnotationOrganism(organismSKey);

		homologue.setEnzymesAnnotationOrganism(enzymesAnnotationOrganism);


		this.homologuesDAO.addEnzymesAnnotationHomologues(homologue);

		Integer id = (Integer) this.homologuesDAO.save(homologue);

		return id;

	} 

	@Override
	public Integer insertGeneHomologyEntry(String locusTag, String uniprotEcnumber, Boolean uniprotStar, int setupId, String query,
			DatabaseProgressStatus status, int seqId, String chromosome, String organelle) {

		EnzymesAnnotationGeneHomology gene = new EnzymesAnnotationGeneHomology(locusTag, uniprotEcnumber);

		gene.setLocusTag(locusTag);

		int star = 0;

		if(uniprotStar == null)
			star = -1;
		else if(uniprotStar)
			star = 1;

		gene.setUniprotStar(star);
		gene.setQuery(query);
		gene.setStatus(status.toString());
		gene.setOrganelle(organelle);
		gene.setChromosome(chromosome);


		ModelSequence sequence = this.modelSequenceDAO.getModelSequence(seqId);
		gene.setModelSequence(sequence);

		EnzymesAnnotationHomologySetup setup = this.homologysetupDAO.getEnzymesAnnotationHomologysetup(setupId);

		gene.setEnzymesAnnotationHomologySetup(setup);

		this.enzGeneHomologyDAO.addEnzymesAnnotationGeneHomology(gene);

		Integer id = (Integer) this.enzGeneHomologyDAO.save(gene);

		return id;

	}

	@Override
	public void updateGeneHomologyEntry(String locusTag, String uniprotEcnumber, Boolean uniprotStar, int setupId, String query,
			DatabaseProgressStatus status, int seqId, String chromosome, String organelle, int sKey) {

		EnzymesAnnotationGeneHomology gene = this.geneHomologyDAO.findById(sKey);

		gene.setLocusTag(locusTag);

		int star = 0;

		if(uniprotStar == null)
			star = -1;
		else if(uniprotStar)
			star = 1;

		gene.setUniprotStar(star);

		gene.setUniprotEcnumber(uniprotEcnumber);

		gene.setQuery(query);

		gene.setStatus(status.toString());

		gene.setOrganelle(organelle);

		gene.setChromosome(chromosome);

		ModelSequence sequence = this.modelSequenceDAO.getModelSequence(seqId);

		if(sequence != null) {
			gene.setModelSequence(sequence);
		}

		EnzymesAnnotationHomologySetup setup = this.homologysetupDAO.getEnzymesAnnotationHomologysetup(setupId);

		if(setup != null) {
			gene.setEnzymesAnnotationHomologySetup(setup);
		}

		this.enzGeneHomologyDAO.updateEnzymesAnnotationGeneHomology(gene);
	}

	@Override
	public void updateGeneHomologyStatus(String databaseName, String locusTag, DatabaseProgressStatus status) {

		Map<String, Serializable> attributes = new HashMap<>();

		attributes.put("locusTag", locusTag);

		List<EnzymesAnnotationGeneHomology> geneList = this.geneHomologyDAO.findByAttributes(attributes);

		for(EnzymesAnnotationGeneHomology gene : geneList) {

			gene.setLocusTag(locusTag);

			gene.setStatus(status.toString());

			this.enzGeneHomologyDAO.updateEnzymesAnnotationGeneHomology(gene);

		}

	}


	public String getNcbiBlastDatabase() throws Exception {
		String ret = null;
		Map<String, String> res = this.homologysetupDAO.getModelHomologySetupProgramAndDatabaseId();

		if (res != null)
			for (String x : res.keySet()) {
				if(x.equalsIgnoreCase("ncbi-blastp"))
					ret = res.get(x);
			}
		return ret;
	}



	public String getHmmerDatabase() throws Exception {
		String ret = null; 
		Map<String, String> res = this.homologysetupDAO.getModelHomologySetupProgramAndDatabaseId();

		if (res != null)
			for (String x : res.keySet()) {
				if(x.equalsIgnoreCase("hmmer"))
					ret = res.get(x);
			}
		return ret;
	}



	public List<String> getProgramFromHomologySetup(String status) throws Exception {
		return this.homologysetupDAO.getEnzymesAnnotationHomologysetupProgramByStatus(status);
	}



	public List<String[]> getSpecificStats(String program) throws Exception {
		return this.enzGeneHomologyDAO.getHomologyAttributes(program, "NO_SIMILARITY");
	}



	public List<Integer> getAllFromGeneHomology(String program) throws Exception {
		List<Integer> result = new ArrayList<Integer>();
		List<EnzymesAnnotationGeneHomology> res = this.enzGeneHomologyDAO.getAllEnzymesAnnotationGeneHomologyByStatusAndProgram("NO_SIMILARITY", program);
		if (res != null)
			for (EnzymesAnnotationGeneHomology x : res)
				result.add(x.getSKey());
		return result;
	}



	public List<String> getTaxonomy(String program) throws Exception {
		Map<String,String> dic = this.homologysetupDAO.getDistinctTaxonomyAndOrganismByProgram(program);
		List<String> res = new ArrayList<String>();
		if(dic != null) {
			for (String x : dic.keySet()) {
				res.add(x);
			}
		}
		return res;

	}



	public String geneHomologyHasHomologues(String locus) throws Exception {
		return this.hashomologuesDAO.getEnzymesAnnotationGeneHomologyHasHomologuesQueryByLocusTag(locus);
	}



	public boolean homologyDataHasKey(int key) throws Exception {
		boolean exists = false;

		List<EnzymesAnnotationHomologyData> rs = this.dataDAO.getAllEnzymesAnnotationHomologyDataBySKey(key);

		if(rs != null)
			exists = true;

		return exists;
	}

	@Override
	public long countEntriesInGeneHomology() throws Exception{

		return this.enzGeneHomologyDAO.countAll();

	}

	public Integer getHomologyDataKey(int key) throws Exception{

		Integer result = null;

		List<EnzymesAnnotationHomologyData> rs = this.dataDAO.getAllEnzymesAnnotationHomologyDataBySKey(key);

		while(rs != null)
			result = rs.get(0).getSKey();

		return result;
	}

	@Override
	public boolean checkCommitedData() throws Exception {

		List<EnzymesAnnotationHomologyData> list = this.dataDAO.getAllEnzymesAnnotationHomologyData();

		if(list != null && list.size() > 0)
			return true;

		return false;
	}

	public String getSetupProgram() throws Exception {
		return this.homologysetupDAO.getEnzymesAnnotationHomologysetupDistinctProgram();
	}


	public List<List<String>> getHomologyResults(int skey) throws Exception {

		List<String> res = this.enzGeneHomologyDAO.getsequenceIdBySKey(skey);
		String query = "";

		if (res != null)
			for (String x : res)
				query = x; 
		
		List<List<String>> result = new ArrayList<List<String>>();

		ArrayList<String> ql = null;

		String previous_homology_s_key="";
		String ecnumber="";
		ql = new ArrayList<String>();

		List<String[]> res2 = this.geneHomologyDAO.getHomologySetupAttributes(query);

		boolean go = true;

		if (res2 != null) {

			int counter = 0;
			for (String[] x : res2) {

				go = false;

				String s_key = "";
				if(x[10] != null) {

					s_key = x[10];

					if(previous_homology_s_key.equals(s_key)) {

						ecnumber+=", "+x[11];
					}
					else {

						previous_homology_s_key=s_key;
						if(!ql.isEmpty()) {

							ql.add(ecnumber);
							result.add(ql);
						}

						ecnumber="";
						ql = new ArrayList<String>();
						ql.add(x[0]);  // referenceID
						ql.add(x[1]); // locusID
						ql.add(x[12]); // uniprotStar
						ql.add(x[2]); // organism
						ql.add(x[3]); // Evalue
						ql.add(x[4]); // Bits
						ql.add(x[5]); // Identity
						ql.add(x[6]); // Positives
						ql.add(x[7]); // queryCoverage
						ql.add(x[8]); // targetCoverage
						ql.add(x[9]); // product
						
						if(x[11]!=null)
							ecnumber=x[11];

					}
					counter++;
					if(counter == res2.size()) {

						ql.add(ecnumber);
						result.add(ql);
					}		
				}
				else {

					ql = new ArrayList<String>();
					ql.add("");
					ql.add("");
					ql.add("-1");
					ql.add("");
					ql.add("");
					ql.add("");
					ql.add("");
					ql.add("");
					ql.add("");
					ql.add("");
					ql.add("");
					ql.add("");
					ql.add("");
					result.add(ql);
				}
			}

			if(go) {

				ql = new ArrayList<String>();
				ql.add("");
				ql.add("");
				ql.add("-1");
				ql.add("");
				ql.add("");
				ql.add("");
				ql.add("");
				ql.add("");
				ql.add("");
				ql.add("");
				ql.add("");
				ql.add("");
				ql.add("");
				result.add(ql);
			}
		}

		return result;

	}



	public List<List<String>> getHomologyTaxonomy(int skey)// String tool1, String tool2, String tool3)
			throws Exception {

		List<String> res = this.enzGeneHomologyDAO.getsequenceIdBySKey(skey);

		String query = "";

		if (res != null)
			for (String x : res)
				query = x; 

		List<List<String>> result = new ArrayList<List<String>>();

		ArrayList<String> ql = null;

		ArrayList<String[]> res2 = this.homologysetupDAO.getOrganismAndTaxonomyAndEvalue(query);// tool1, tool2, tool3);
		boolean go = true;

		if (res2 != null)
			for (String[] x : res2){

				go = false;

				ql = new ArrayList<String>();
				ql.add(x[0]);
				ql.add(x[1]);
				result.add(ql);
			}

		if(go){
			ql = new ArrayList<String>();
			ql.add("");
			ql.add("");
			result.add(ql);
		}

		return result;
	}


	public List<List<String>> getInterProResult(int skey) throws Exception {

		List<String> res = this.enzGeneHomologyDAO.getsequenceIdBySKey(skey);

		String query = "";

		if (res != null)
			for (String x : res)
				query = x; 

		List<List<String>> result = new ArrayList<List<String>>();

		ArrayList<String> ql = null;

		List<String[]> res2 = this.interproResultsDAO.getInterproResultsDataByQuery(query);

		if (res2 != null) {
			for (String[] y: res2) {

				String out = "";
				int counter=0;
				ql = new ArrayList<String>();

				while (counter<13) {

					out = "";
					counter++;
					if(y[counter]!= null && !y[counter].equals("null"))
						out = y[counter];	
					ql.add(out);
				}
				counter = 1;
				result.add(ql);
			}
		}

		return result;
	}



	public Integer loadInterProResult(String tool, float eValue, float score, String family, String accession,
			String name, String ec, String goName, String localization, String database, int resultsID)
					throws Exception {

		Integer ret = null;

		if(name!= null) {

			int size = name.length();

			if (size>250)
				size=249;
			name = name.substring(0, size);
		}

		if(goName!= null) {

			int size = goName.length();

			size = goName.length();
			if (size>250)
				size=249;

			goName = goName.substring(0, size);
		}

		String aux = "interpro_result.";

		List<InterproResult> res = this.interproResultDAO.getAllInterproResultByDatabaseAndAccessionAndResultsId(database, accession, resultsID);

		if(res == null) {

			Integer res2 = this.interproResultDAO.insertInterproResultData(tool, eValue, score, family, accession, name, ec, goName, localization, database, resultsID);
			ret = res2;
		}

		return ret;

	}



	public Integer loadInterProEntry(String accession, String description, String name, String type) throws Exception {

		Integer ret = null;

		if(name!= null) {

			int size = name.length();

			if (size>250)
				size=249;

			name = name.substring(0, size);
		}

		if(description!= null) {

			int size = description.length();

			if (size>250)
				size=249;

			description = description.substring(0, size);
		}

		List<InterproEntry> res = this.interproentryDAO.getAllInterproEntryByAccession(accession);

		if(res == null) {

			Integer res2 = this.interproentryDAO.insertInterproEntryData(accession, name, description, type);
			ret = res2;
		}


		return ret;
	}



	public void loadXrefs(String category, String database, String name, String id, int entryId) throws Exception {

		if(name!= null) {

			int size = name.length();

			if (size>250)
				size=249;

			name = name.substring(0, size);
		}

		String aux = "interpro_xRef.";

		List<InterproXRef> res = this.interproxrefDAO.getAllInterproXRefByExternalIdAndEtryId(id, entryId);

		if(res == null) {

			this.interproxrefDAO.insertInterproXRefData(category, database, name, id, entryId);
		}
	}



	public Integer loadInterProModel(String accession, String name, String description) throws Exception {

		if(name!= null) {

			int size = 0;
			size = name.length();

			if (size>250)
				size=249;

			name = name.substring(0, size);
		}

		if(description!= null) {

			int size = description.length();

			if (size>250)
				size=249;

			description = description.substring(0, size);
		}

		Integer ret = null;
		List<String> res = this.interpromodelDAO.getInterproModelAccessionByAccession(accession);

		boolean go = false;
		if (res != null)
			go = true;

		if(!go) {

			try {

				Integer res2 = this.interpromodelDAO.insertInterproModelData(accession, description, name);
				ret = res2;
			} 
			catch (Exception e) {

				List<String> res3 = this.interpromodelDAO.getInterproModelAccessionByAccession(accession);
				if(res3 != null)
					throw new Exception("Entry exists {}"+ accession);
			}
		}
		return ret;
	}



	public Set<String> getGenesFromDatabase(String eVal, String matrix, Integer numberOfAlignments, String wordSize,
			String program, String databaseID, boolean deleteProcessing) throws Exception {

		//			Set<String> loadedGenes = new HashSet<String>();
		//
		//			try  {
		//				// get processing genes
		//				Set<Integer> deleteGenes = new HashSet<Integer>();
		//				Map<Integer, String> genes = this.geneHomologyDAO.getGeneHomologySKeyAndProgramByStatus("PROCESSING");
		//				if(genes!=null) {
		//					deleteGenes.addAll(genes.keySet());
		//				}
		//				// get processed genes
		//				Map<String,String> res1 = this.enzGeneHomologyDAO.getGeneHomologyQueryAndProgramByStatus("PROCESSED");
		//
		//				if (res1 != null) {
		//					for (String x : res1.keySet()) {
		//						if(res1.get(x).contains(program))
		//							loadedGenes.add(x);
		//					}
		//				}
		//
		//				// get NO_SIMILARITY genes
		//				Map<String,String> res2 = this.enzGeneHomologyDAO.getGeneHomologyQueryAndProgramByStatus("NO_SIMILARITY");
		//
		//				if (res2 != null)
		//					for (String x : res1.keySet()) {
		//						if(res1.get(x).contains(program))
		//							loadedGenes.add(x);
		//					}
		//
		//				// get NO_SIMILARITY genes and delete if new eVal > setup eVal
		//				List<String[]>  res3 = this.enzGeneHomologyDAO.getGeneHomologySKeyANdQueryAndProgramByAttributes("NO_SIMILARITY", eVal, matrix, wordSize, numberOfAlignments);
		//
		//				if (res3 != null)
		//					for (String[] y : res3){
		//						if(y[2].contains(program)) {
		//							System.out.println("4.0 delete " + y[1] + "     " + y[0]);
		//
		//						loadedGenes.remove(y[1]);
		//						deleteGenes.add(Integer.valueOf(y[0]));
		//					}
		//				}
		//
		//				// get NO_SIMILARITY genes and delete if new database <> setup database
		//				List<String[]> res4 = this.enzGeneHomologyDAO.getGeneHomologySKeyANdQueryAndProgramByStatusAndDatabaseId("NO_SIMILARITY", databaseID);
		//
		//				System.out.println(databaseID);
		//
		//				if (res4 != null) 
		//					for (String[] x : res4){
		//						if(x[2].contains(program)) {
		//							System.out.println("3.0 delete " + x[1] + "     " + x[0]);
		//
		//						loadedGenes.remove(x[1]);
		//						deleteGenes.add(Integer.valueOf(x[0]));
		//					}
		//				}
		//
		//				/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//
		//				//get genes with less than numberOfAlignments hits if new eVal > setup eVal and hit eVal< eVal 
		//				List<String[]> res5 = this.homologysetupDAO.getGeneHomologySetupAttributesByAttributes(eVal, "PROCESSED", matrix, wordSize);
		//
		//				if (res5 != null)
		//					for (String[] x : res5){
		//						System.out.println(">> " + x[2] + "     +     " + x[4]);
		//
		//							if( Integer.valueOf(x[4]) < numberOfAlignments && Integer.valueOf(x[1]) < numberOfAlignments && x[3].contains(program) ) {
		//
		//						//					loadedGenes.remove(rs.getString(3));
		//						//					deleteGenes.add(rs.getString(1));
		//
		//								System.out.println("to delete " + x[2]);
		//					}
		//				}
		//
		//				
		//				rs =statement.executeQuery("SELECT geneHomology.s_key, COUNT(referenceID), query, databaseID FROM homologySetup " +
		//						" LEFT JOIN geneHomology ON (homologySetup.s_key = homologySetup_s_key) " +
		//						" LEFT JOIN geneHomology_has_homologues ON (geneHomology.s_key = geneHomology_s_key) " +
		////fazer esta DAO		" WHERE status='PROCESSED' AND geneHomology.s_key NOT IN (SELECT distinct (geneHomology.s_key) FROM homologySetup " +
		//						" LEFT JOIN geneHomology ON (homologySetup.s_key = homologySetup_s_key) " +
		//						" LEFT JOIN geneHomology_has_homologues ON (geneHomology.s_key = geneHomology_s_key) " +
		//						" WHERE status='PROCESSED' AND geneHomology_has_homologues.eValue <" + eVal + ") " +
		//						" GROUP BY geneHomology.s_key;");
		//
		//				while(rs.next()) {
		//
		//					if(!rs.getString(4).equals(databaseID)){
		//
		//						//					loadedGenes.remove(rs.getString(3));
		//						//					deleteGenes.add(rs.getString(1));
		//
		//						System.out.println("2.0 to delete " + rs.getString(3));
		//					}
		//				}
		//				for (Integer x : deleteGenes)
		//					this.enzGeneHomologyDAO.removeModelGeneHomologyBySKey(x);
		//
		//			}
		//			catch (Exception e) {
		//
		//				return null;
		//			}
		//			return loadedGenes;
		return null;
	}



	public Set<String> getGenesFromDatabase(String program, boolean deleteProcessing) throws Exception {
		Set<String> loadedGenes = new HashSet<String>();

		try  {

			Set<Integer> deleteGenes = new HashSet<Integer>();
			Map<Integer, String> genes = this.geneHomologyDAO.getGeneHomologySKeyAndProgramByStatus("PROCESSING");
			if(genes!=null) {
				deleteGenes.addAll(genes.keySet());
			}

			// get processed genes
			Map<String,String> res1 = this.enzGeneHomologyDAO.getGeneHomologyQueryAndProgramByStatus("PROCESSED");

			if (res1 != null)
				for (String x : res1.keySet()){
					if(res1.get(x).contains(program))
						loadedGenes.add(x);
				}

			// get NO_SIMILARITY genes
			Map<String,String> res2 = this.enzGeneHomologyDAO.getGeneHomologyQueryAndProgramByStatus("NO_SIMILARITY");

			if (res2 != null)
				for (String y : res2.keySet())
					if(res2.get(y).contains(program))
						loadedGenes.add(y);


			for (Integer x : deleteGenes)
				this.enzGeneHomologyDAO.removeGeneHomologyBySKey(x);
		}
		catch (Exception e) {

			e.printStackTrace();
			return null;
		}
		return loadedGenes;
	}



	public Map<String, List<String>> getUniprotEcNumbers() throws Exception {

		Map<String, List<String>> result = new HashMap<String, List<String>>();

		Map<String,String> res1 = this.enzGeneHomologyDAO.getDistinctGeneHomologyLocusTagAndUniprotEcNumber();

		if (res1 != null)
			for (String x : res1.keySet()){

				if(res1.get(x)!= null && !res1.get(x).isEmpty()) {

					List<String> ecnumbers = new ArrayList<String>();
					String[] ecs = res1.get(x).split(", ");

					for(String ec : ecs) {

						ecnumbers.add(ec.trim());
					}
					result.put(x, ecnumbers);
				}
			}
		return result;
	}



	public List<String[]> getCommittedHomologyData2() throws Exception {
		return this.dataDAO.getEnzymesAnnotationHomologydataSKeyAndOtherNames();
	}


	public List<String[]> getCommittedHomologyData3() throws Exception {
		return this.dataDAO.getEnzymesAnnotationHomologydataSKeyAndOtherEcNumbers();
	}



	public Map<Integer, Long> getHomologuesCountByEcNumber() throws Exception {
		Map<Integer,Long> homologuesCount = new TreeMap<Integer, Long>();

		Map<Integer, Long> res = this.hashomologuesDAO.getEnzymesAnnotationGeneHomologyHasHomologuesAttributes();

		if (res != null)
			for (Integer x : res.keySet())
				homologuesCount.put(x, res.get(x));

		return homologuesCount;
	}



	public Map<Integer, Long> getHomologuesCountByProductRank() throws Exception {
		Map<Integer,Long> homologuesCount = new TreeMap<Integer, Long>();

		Map<Integer, Long> res = this.hashomologuesDAO.getEnzymesAnnotationGeneHomologyHasHomologuesAttributes2();

		if (res != null)
			for (Integer x : res.keySet())
				homologuesCount.put(x, res.get(x));

		return homologuesCount;
	}



	public List<String[]> getGenesInformation() throws Exception {
		return this.enzGeneHomologyDAO.getGeneHomologyAttributesByStatus2();
	}



	public Map<String, Set<Integer>> getGenesPerDatabase() throws Exception {

		return this.enzGeneHomologyDAO.getGeneHomologySkeyAndDatabaseId();

	}



	public String[] getBlastDatabases(String program) throws Exception {
		String[] databases = new String[1];

		Long res = this.homologysetupDAO.getEnzymesAnnotationHomologysetupCountDistinctDatabaseId(program);

		if(res != null)
			databases = new String[(int) (res+1)];

		List<String> res2 = this.homologysetupDAO.getEnzymesAnnotationHomologysetupDistinctDatabaseId();

		int i = 1;

		if (res2 != null)
			for (String y : res2) {
				databases[i] = y;
				i++;
			}

		databases[0] = "all databases";

		return databases;
	}



	public void deleteHomologyData(String database) throws Exception {
		String aux = "";

		//			if(database!= null)
		//				aux = " WHERE geneHomology_s_key IN (SELECT geneHomology.s_key FROM geneHomology INNER JOIN homologySetup ON (homologySetup_s_key = homologySetup.s_key) WHERE databaseID ='" + database + "')";
		//			
		//			 statement.execute("DELETE FROM homologyData "+aux+";");
	}



	public void insertAutomaticEnzymeAnnotation(Integer homologySkey, String locusTag, String geneName, String product, String ecnumber, boolean selected,
			String chromossome, String notes, Map<Integer, String> locusTag2, Map<Integer, String> geneName2,
			Map<Integer, String> ecMap, Map<Integer, String> confLevelMap) throws Exception {

		EnzymesAnnotationGeneHomology gene = this.enzGeneHomologyDAO.getEnzymesAnnotationGeneHomology(homologySkey);

		EnzymesAnnotationHomologyData homologyData = new EnzymesAnnotationHomologyData();
		homologyData.setChromosome(chromossome);
		homologyData.setEcNumber(ecnumber);

		homologyData.setEnzymesAnnotationGeneHomology(gene);

		homologyData.setLocusTag(locusTag);
		homologyData.setGeneName(geneName);
		homologyData.setProduct(product);
		homologyData.setSelected(selected);
		homologyData.setNotes(notes);
		//acabar!!!
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}



	public Long getNumberOfHomologueGenes(String program) throws Exception {
		Long result = (long) 0;

		Long res = this.homologysetupDAO.getAllEnzymesAnnotationHomologysetupByProgram(program);

		if (res != null)
			result = res;

		return result;
	}



	public String getEbiBlastDatabase() throws Exception {
		String ret = null;

		Map<String, String> res1 = this.homologysetupDAO.getModelHomologySetupProgramAndDatabaseId();

		if (res1 != null)
			for (String x : res1.keySet()){

				if(x.equalsIgnoreCase("ebi-blastp"))
					ret = res1.get(x);
			}
		return ret;
	}


	public List<String[]> getHomologyProductsByGeneHomologySkey(Integer skey) throws Exception {
		
		return this.dataDAO.getHomologyProductsByGeneHomologySkey(skey);
	
	}

	public Integer getAnnotationHomologySkeyBySequenceId(int sequenceId) throws Exception {
		
		return this.enzGeneHomologyDAO.getAnnotationHomologySkeyBySequenceId(sequenceId);
	
	}



}
