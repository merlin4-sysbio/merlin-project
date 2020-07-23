package pt.uminho.ceb.biosystems.merlin.services.implementation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelAliasesDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelModuleDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelModuleHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionLabelsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSubunitDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunitId;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.ISubunitService;


public class SubunitServiceImpl implements ISubunitService{


	private IModelSubunitDAO modelsubunitDAO;
	private IModelProteinDAO proteinDAO;
	private IModelReactionDAO reactionDAO;
	private IModelAliasesDAO aliasDAO;
	private IModelGeneDAO geneDAO;
	private IModelReactionLabelsDAO reactionsLabelsDAO;

	public SubunitServiceImpl(IModelSubunitDAO modelsubunitDAO, IModelProteinDAO proteinDAO,
			IModelReactionDAO reactionDAO, IModelAliasesDAO aliasDAO, IModelGeneDAO geneDAO,
			IModelReactionLabelsDAO reactionLabelsDAO, IModelModuleDAO moduleDAO, IModelModuleHasModelProteinDAO modelModuleHasModelProteinDAO) {
		this.modelsubunitDAO  = modelsubunitDAO;
		this.proteinDAO = proteinDAO;
		this.reactionDAO = reactionDAO;
		this.aliasDAO = aliasDAO;
		this.geneDAO = geneDAO;
		this.reactionsLabelsDAO = reactionLabelsDAO;
	}

	public Map<String, List<Integer>> loadEnzymeGetReactions(Integer idgene, Set<String> ecNumber, String proteinName,
			boolean integratePartial, boolean integrateFull, boolean insertProductNames, String classe, 
			String inchi, Float molecularWeight, Float molecularWeightExp, Float molecularWeightKd, 
			Float molecularWeightSeq, Float pi, boolean inModel, String source, String aliasName, Integer aliasId, Boolean originalreaction) throws Exception {

		//		String aux = " AND originalReaction = " + originalreaction; 

		Integer idProtein = null;
		Map<String, List<Integer>> enzymesReactions = new HashMap<String, List<Integer>>();

		//		List<String> res = this.modelsubunitDAO.getEcNumberByGeneId(idgene);
		//
		//		Set<String> ecs = new HashSet<String>();
		//		if (res != null)
		//			for (String x : res)
		//				ecs.add(x);
		//
		//		for(String ec: ecs)
		//			if(!ecNumber.contains(ec))
		this.modelsubunitDAO.removeSubunitByGeneIdAndProteinId(idgene, idProtein);

		//		List<ModelSubunit> res1 = this.modelsubunitDAO.getAllSubunitByGeneId(idgene);

		for(String enzyme : ecNumber) {

			List<Integer> reactions_ids = new ArrayList<Integer>();

			if(((enzyme.contains(".-") && integratePartial) || (!enzyme.contains(".-") && integrateFull)) && !enzyme.isEmpty()) {

				ProteinContainer container = this.proteinDAO.getProteinIdByEcNumber(enzyme);

				boolean go = true;

				if(container != null) {

					go = false;

					idProtein = container.getIdProtein();

					boolean res4 = this.modelsubunitDAO.isProteinEncodedByGenes(idProtein);
					go = !res4;

				}
				else {

					if(proteinName==null)
						proteinName = enzyme;

					Integer res5 = this.proteinDAO.getProteinIdByName(proteinName);
					if(res5 == null) {

						this.proteinDAO.insertModelProtein(proteinName, classe, inchi, molecularWeight, molecularWeightExp, molecularWeightKd, molecularWeightSeq, pi, enzyme, source);
						insertProductNames = false;
					}
					idProtein = res5;

				}	

				if(go) {

					this.proteinDAO.updateProteinSetEcNumberSourceAndInModel(idProtein, enzyme, source);

					if(!enzyme.contains(".-")) {

						List<Integer> res8 = this.reactionDAO.getDistinctReactionsByEnzymeAndCompartmentalized(idProtein, false, false);

						if (res8 != null)
							for (Integer x : res8)
								reactions_ids.add(x);

						//						resultSet= statement.executeQuery("SELECT idreaction FROM reactions_view_noPath_or_noEC " +
						//								"INNER JOIN reaction_has_enzyme ON reaction_has_enzyme.reaction_idreaction=idreaction " +
						//								"WHERE enzyme_protein_idprotein = "+idProtein+" AND enzyme_ecnumber = '"+enzyme+"'");
						//	
						//						while(resultSet.next())
						//							reactions_ids.add(resultSet.getString(1));

						for(Integer idreaction: reactions_ids){
							this.reactionDAO.updateInModelByReactionId(idreaction, inModel);

							int reactionLabelID = this.reactionDAO.getModelReactionLabelByReactionId(idreaction);
							this.reactionsLabelsDAO.updateSourceByReactionLabelId(reactionLabelID, source);
						}

					}
				}

				Integer res9 = this.modelsubunitDAO.checkProteinIdByGeneIdAndProteinId(idgene, idProtein);
				if(res9 == null)
					this.modelsubunitDAO.insertModelSubunit(idgene, idProtein);


				if(insertProductNames)
					this.aliasDAO.insertModelAlias(classe, idProtein, aliasName, aliasId);
			}
			enzymesReactions.put(enzyme, reactions_ids);
		}

		return enzymesReactions;
	}



	public List<Integer> getEnzymeCompartments(String ecNumber) throws Exception {
		return this.modelsubunitDAO.getDistinctCompartmentIdByEcNumber(ecNumber);

	}

	public Map<Integer, String> getProteinIdAndEcNumber(Integer geneID) throws Exception {
		return this.modelsubunitDAO.getModelSubunitProteinIdAndEcNumberByGeneId(geneID);
	}


	public long countGenesEncodingProteins() throws Exception {
		Long prot = (long) 0;

		Long res = this.modelsubunitDAO.countModelSubunitDistinctGeneId();

		if (res!= null)
			prot = res;

		return prot;

	}


	public long countGenesInModel() throws Exception {
		return this.modelsubunitDAO.countGenesInModel();
	}

	public List<String[]> getGPRsECNumbers(boolean isCompartimentalized) throws Exception {
		return this.modelsubunitDAO.getGPRsECNumbers(isCompartimentalized);
	}


	public List<ProteinContainer> getDataFromSubunit(Integer id) throws Exception {
		return this.modelsubunitDAO.getModelSubunitAttributes(id);
	}

	@Override
	public boolean checkModules(int geneId, int proteinId) throws Exception {
		boolean exists = false;

		Integer res = this.modelsubunitDAO.checkProteinIdByGeneIdAndProteinId(geneId, proteinId);
		if (res != null)
			exists = true;

		return exists;
	}

	@Override
	public boolean checkModulesByModuleId(int geneId, int proteinId, int moduleId) throws Exception {
		boolean exists = false;

		Integer res = this.modelsubunitDAO.checkProteinByGeneIdAndProteinIdAndModuleId(geneId, proteinId, moduleId);
		if (res != null)
			exists = true;

		return exists;
	}

	public Map<String, List<String>> getECNumbers_() throws Exception {

		Map<String, List<String>> ec_numbers = new HashMap<String, List<String>>();

		Map<String,String> res = this.geneDAO.getLocusTagAndECNumber();

		if (res != null)
			for (String x : res.keySet()){

				List<String> genes = new ArrayList<String>();

				String gene = x;
				String enzyme = res.get(x);

				if(ec_numbers.containsKey(enzyme))
					genes = ec_numbers.get(enzyme);

				genes.add(gene);

				ec_numbers.put(enzyme, genes);

			}
		return ec_numbers;
	}




	public Map<String, List<Integer>> loadEnzymeGetReactions(Integer idgene, Set<String> ecNumber, 
			String proteinName, boolean integratePartial, boolean integrateFull, 
			boolean insertProductNames, String classe, String inchi, Float molecularWeight, 
			Float molecularWeightExp, Float molecularWeightKd, Float molecularWeightSeq, Float pi,
			boolean inModel, String source, String aliasName, Integer aliasId) throws Exception {
		Integer idProtein = null;
		Map<String, List<Integer>> enzymesReactions = new HashMap<String, List<Integer>>();


		//		List<String> res1 = this.modelsubunitDAO.getEcNumberByGeneId(idgene);
		//		Set<String> ecs = new HashSet<String>();
		//
		//		if (res1 != null) { 
		//			for(String x : res1)
		//				ecs.add(x);
		//
		//			if (ecs != null) { 
		//				for(String ec : ecs)
		//					if(!ecNumber.contains(ec))
		this.modelsubunitDAO.removeSubunitByGeneIdAndProteinId(idgene, idProtein);
		//			}

		//			List<ModelSubunit> res2 = this.modelsubunitDAO.getAllSubunitByGeneId(idgene);

		for(String enzyme : ecNumber) {
			List<Integer> reactions_ids = new ArrayList<Integer>();

			if(((enzyme.contains(".-") && integratePartial) || (!enzyme.contains(".-") && integrateFull)) && !enzyme.isEmpty()) {

				ProteinContainer container = this.proteinDAO.getProteinIdByEcNumber(enzyme);
				boolean go = true;

				if(container != null) {

					idProtein = container.getIdProtein();
					boolean res4 = this.modelsubunitDAO.isProteinEncodedByGenes(idProtein);
					go = !res4;
				}
				else {

					if(proteinName==null)
						proteinName = enzyme;

					Integer res5 = this.proteinDAO.getProteinIdByName(proteinName);
					if (res5 == null) {
						this.proteinDAO.insertModelProtein(proteinName, null, null, null, null, null, null, null, enzyme, "HOMOLOGY");
						insertProductNames = false;
					}
					idProtein = res5;

				}
				if (go == true) {
					this.proteinDAO.updateProteinSetEcNumberSourceAndInModel(idProtein, enzyme, "HOMOLOGY");
					if(!enzyme.contains(".-")) {
						List<Integer> res6 = this.reactionDAO.getDistinctReactionsByEnzymeAndCompartmentalized(idProtein, false, false);

						if(res6 != null)
							for (Integer x : res6)
								reactions_ids.add(x);

						//							resultSet= statement.executeQuery("SELECT idreaction FROM reactions_view_noPath_or_noEC " +
						//									"INNER JOIN reaction_has_enzyme ON reaction_has_enzyme.reaction_idreaction=idreaction " +
						//									"WHERE enzyme_protein_idprotein = "+idProtein+" AND enzyme_ecnumber = '"+enzyme+"'"); //tem view! fazer?! nao FIZ
						//							
						//							while(resultSet.next())
						//								reactions_ids.add(resultSet.getString(1));
						//
						//							for(String idreaction: reactions_ids){
						//								statement.execute("UPDATE reaction SET inModel = true, source = 'HOMOLOGY' WHERE idreaction = '"+idreaction+"'"); //updateInModelAndSourceByReactionId
					}
				}		
			}
			Integer res7 = this.modelsubunitDAO.checkProteinIdByGeneIdAndProteinId(idgene, idProtein);
			if (res7 == null) {
				this.modelsubunitDAO.insertModelSubunit(idgene, idProtein);

				if(insertProductNames)
					this.aliasDAO.insertModelAlias("p", idProtein, proteinName, null);
			}
			enzymesReactions.put(enzyme, reactions_ids);
		}
		//		}
		return enzymesReactions;
	}

	public List<Integer> countGenesEncodingEnzymesAndTransporters() throws Exception {
		int enz=0, trp=0;

		List<Integer> counts = new ArrayList<Integer>();
		Map<Integer, ArrayList <String>> dic = this.modelsubunitDAO.getModelSubunitDistinctGeneIdAndSource();
		List<Integer> enzymeIds = new ArrayList<Integer>();
		List<Integer> transporterIds = new ArrayList<Integer>();

		if (dic != null)
			for (Integer x : dic.keySet()){
				for(String source : dic.get(x)) {
					if(source.equalsIgnoreCase(SourceType.TRANSPORTERS.toString()) && dic.get(x) != null) {
						trp ++;
						transporterIds.add(x);
					}
					else if(dic.get(x) != null) {
						enz ++;
						enzymeIds.add(x);
					}
				}
			}
		
		HashSet<Integer> tempEnz = new HashSet<Integer>(enzymeIds);
		HashSet<Integer> tempTrans = new HashSet<Integer>(transporterIds);
		tempEnz.removeAll(tempTrans);
		tempTrans.removeAll(enzymeIds);
		
		Integer CountGenesEncodingOnlyEnzymes = tempEnz.size();
		Integer CountGenesEncodingOnlyTransporters = tempTrans.size();
		
		if(enzymeIds.contains(5000001))
			System.out.println("here");
		counts.add(enz);
		counts.add(trp);
		counts.add(CountGenesEncodingOnlyEnzymes);
		counts.add(CountGenesEncodingOnlyTransporters);
		
		return counts;
	}


	public int countProteinsAssociatedToGenes() throws Exception {
		int p_g = 0;

		Map<Integer, String> res = this.modelsubunitDAO.getModelSubunitDistinctEnzymeProteinIdAndEnzymeEcNumber();

		if (res != null)
			p_g = res.size();

		return p_g;
	}


	@Override
	public boolean checkSubunitData(Integer id) throws Exception {
		return this.modelsubunitDAO.checkSubunitData(id);
	}

	@Override
	public Map<String, Integer> countGenesReactionsBySubunit() {

		return this.modelsubunitDAO.countGenesReactionsBySubunit();

	}


	@Override
	public String[][] getSubunitsByGeneId(int geneIdentifier) {

		return this.modelsubunitDAO.getSubunitsByGeneId(geneIdentifier);
	}

	@Override
	public Map<String,Integer> getProteinsCountFromSubunit() {

		return this.modelsubunitDAO.getProteinsCount();
	}

	@Override
	public List<String[]> getGeneData(Integer id) throws Exception {
		return this.modelsubunitDAO.getModelEnzymeByEcNumberAndProteinId(id);
	}

	@Override
	public List<String[]> getGPRstatusAndReactionAndDefinition(Integer proteinId) throws Exception {
		return this.modelsubunitDAO.getGPRstatusAndReactionAndDefinition(proteinId);
	}

	@Override
	public void removeSubunitByGeneIdAndProteinId(Integer geneId, Integer protId) throws Exception {
		this.modelsubunitDAO.removeSubunitByGeneIdAndProteinId(geneId, protId);
	}


//	@Override
//	public void updateModuleIdByGeneIdAndProteinId(Integer geneId, Integer protId, Integer moduleId) throws Exception{
//
//		Map<String, Serializable> dic = new HashMap<String, Serializable>();
//		dic.put("id.modelGeneIdgene", geneId);
//		dic.put("id.modelProteinIdprotein", protId);
//
//		ModelSubunit subunit = this.modelsubunitDAO.findUniqueByAttributes(dic);
//
//		ModelModule module = this.moduleDAO.getModelModule(moduleId);
//		
//		this.modelsubunitDAO.update(subunit);
//	}
	
	@Override
	public void insertModelSubunit(Integer geneId, Integer protId, String note, String gprStatus) throws Exception{
		
		ModelSubunit subunit = new ModelSubunit();
		ModelSubunitId id = new ModelSubunitId(geneId, protId);
		
		subunit.setId(id);
		
		this.modelsubunitDAO.save(subunit);
	}
	
	@Override
	public boolean checkModelSubunitEntry(Integer geneId, Integer protId) throws Exception{

		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelGeneIdgene", geneId);
		dic.put("id.modelProteinIdprotein", protId);

		ModelSubunit subunit = this.modelsubunitDAO.findUniqueByAttributes(dic);

		return subunit != null;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	@Override
	public Long countSubunitEntries() throws Exception{

		return this.modelsubunitDAO.countAll();
	}
	

	@Override
	public boolean isProteinEncodedByGenes(Integer proteinId) throws Exception{

		return this.modelsubunitDAO.isProteinEncodedByGenes(proteinId);
	}

	@Override
	public Map<Integer, Integer> getModelSubunitGeneIdAndEnzymeProteinIdByEcNumber(String ecNumber) throws Exception {
		// TODO Auto-generated method stub
		return this.modelsubunitDAO.getModelSubunitGeneIdAndEnzymeProteinIdByEcNumber(ecNumber);
	}

}		
