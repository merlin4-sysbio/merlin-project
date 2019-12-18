package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberListDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationGenehomologyDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologydataDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationProductListDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationScorerconfigDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelAliasesDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelEnzymaticCofactorDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelProteinCompositionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionLabelsDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationScorerConfig;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEnzymaticCofactor;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelEnzymaticCofactorId;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProteinComposition;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IProteinService;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


public class ProteinServiceImpl implements IProteinService{


	private IModelProteinDAO modelproteinDAO;
	private IModelProteinCompositionDAO modelprotcompositionDAO;
	private IModelReactionHasModelProteinDAO reactionHasEnzymeDAO;
	private IModelReactionDAO reactionDAO;
	private IModelAliasesDAO aliasDAO;
	private IModelReactionLabelsDAO modelReactionLabelsDAO;
	private IEnzymesAnnotationScorerconfigDAO scorerconfigDAO;
	private IEnzymesAnnotationHomologydataDAO homologyDataDAO;
	private IEnzymesAnnotationProductListDAO productListDAO;
	private IEnzymesAnnotationEcNumberListDAO ecnumberListDAO;
	private IEnzymesAnnotationGenehomologyDAO enzGeneHomologyDAO;
	private IModelEnzymaticCofactorDAO cofactorDAO;


	public ProteinServiceImpl(IModelProteinDAO modelproteinDAO, IModelProteinCompositionDAO modelprotcompositionDAO,
			IModelReactionHasModelProteinDAO reactionHasEnzymeDAO,
			IModelReactionDAO reactionDAO, IModelEnzymaticCofactorDAO cofactorDAO, IModelAliasesDAO aliasDAO, IModelReactionLabelsDAO modelReactionLabelsDAO, IEnzymesAnnotationScorerconfigDAO scorerconfigDAO, 
			IEnzymesAnnotationHomologydataDAO homologyDataDAO, IEnzymesAnnotationProductListDAO productListDAO, IEnzymesAnnotationEcNumberListDAO ecnumberListDAO, IEnzymesAnnotationGenehomologyDAO enzGeneHomologyDAO) {
		this.modelproteinDAO  = modelproteinDAO;
		this.modelprotcompositionDAO = modelprotcompositionDAO;
		this.reactionHasEnzymeDAO = reactionHasEnzymeDAO;
		this.reactionDAO = reactionDAO;
		this.aliasDAO = aliasDAO;
		this.modelReactionLabelsDAO = modelReactionLabelsDAO;
		this.scorerconfigDAO = scorerconfigDAO;
		this.homologyDataDAO = homologyDataDAO;
		this.productListDAO = productListDAO;
		this.ecnumberListDAO = ecnumberListDAO;
		this.enzGeneHomologyDAO = enzGeneHomologyDAO;
		this.cofactorDAO = cofactorDAO;
	}


	public List<String[]> getProteinsInModel() throws Exception {
		List<ModelProtein> list = this.modelproteinDAO.getDistinctModelProteinIdAndNameAndClass();
		List<String[]> res = new ArrayList<String[]>();

		if (list != null && list.size()>0) {
			String[] result = new String[3];
			for (ModelProtein x : list) {
				result[0] =  String.valueOf(x.getIdprotein());
				result[1] = (String) x.getName();
				result[2] = (String) x.getClass_();
			}
			res.add(result);
			return res;
		}
		return null;	
	}


	public List<String[]> getEnzymes() throws Exception {
		List<ModelProtein> list = this.modelproteinDAO.getDistinctModelProteinIdAndNameAndInchi();

		List<String[]> res = new ArrayList<String[]>();

		if(list != null && list.size()>0) {
			String[] result = new String[3];
			for(ModelProtein x : list) {
				result[0] = String.valueOf(x.getIdprotein());
				result[1] = (String) x.getName();
				result[2] = (String) x.getInchi();
			}
			res.add(result);
			return res;
		}
		return null;
	}


	public List<String[]> getDistinctModelProteinAttributes() throws Exception {
		List<ModelProtein> list = this.modelproteinDAO.getDistinctModelProteinAttributes();

		List<String[]> res = new ArrayList<String[]>();

		if(list != null && list.size()>0) {
			String[] result = new String[3];
			for(ModelProtein x : list) {
				result[0] = String.valueOf(x.getIdprotein());
				result[1] = (String) x.getName();
				result[2] = (String) x.getInchi();
			}
			res.add(result);
			return res;
		}
		return null;
	}


	public List<ModelProtein> getAllFromProtein() throws Exception {
		return this.modelproteinDAO.getAllModelProtein();
	}


	public Integer getProteinID(String class_, String name) throws Exception {
		List<ModelProtein> res = this.modelproteinDAO.getAllModelProteinByNameAndClass(name, class_);
		Integer result = null;
		if(res != null) {
			result =  res.get(0).getIdprotein();	
		}
		return result;
	}


	public long countProteinsComplexes() throws Exception {

		return this.modelprotcompositionDAO.getModelCompositionDistinctProteinId();

	}


	public List<String[]> getAllProteins() throws Exception {
		return this.modelproteinDAO.getDistinctModelProteinIdAndNameAndClass2();
	}


	public List<String[]> getAllFromProteinComposition() throws Exception {

		List<ModelProteinComposition> resultList = this.modelprotcompositionDAO.getAllModelProteinComposition();

		ArrayList<String[]> parsedList = new ArrayList<>();

		if(resultList!= null && resultList.size() > 0) {

			for(ModelProteinComposition item: resultList) {
				String[] list = new String[2];
				list[0] = String.valueOf(item.getId().getModelProteinIdprotein());
				list[1] = String.valueOf(item.getId().getSubunit());
				parsedList.add(list);
			}
		}
		return parsedList;
	}

	public Integer getProteinIDFromName(String name) throws Exception {

		return this.modelproteinDAO.getProteinIdByName(name);
	}

	public int getProteinIDFromNameAndClass(String name, String class_) throws Exception {
		int res = -1;

		Integer result = this.modelproteinDAO.getProteinIdByNameAndClass(name, class_);
		if(result != null)
			res =  result;
		return res;
	}

	@Override
	public ProteinContainer getProteinData(int id) throws Exception {

		ModelProtein protein = this.modelproteinDAO.getModelProtein(id);

		if(protein != null) {
			ProteinContainer container = new ProteinContainer(protein.getEcnumber());
			container.setIdProtein(protein.getIdprotein());
			container.setName(protein.getName());
			container.setClass_(protein.getClass_());
			container.setInchi(protein.getInchi());
			container.setMolecularWeight(protein.getMolecularWeight());
			container.setMolecularWeightExp(protein.getMolecularWeightExp());
			container.setMolecularWeightKd(protein.getMolecularWeightKd());
			container.setMolecularWeightSeq(protein.getMolecularWeightSeq());
			container.setPi(protein.getPi());
			container.setSource(protein.getSource());
//			container.setInModel(protein.getInModel());

			return container;
		}

		return null;
	}

	public void insertEnzymes(int idProtein, String ecnumber, boolean editedReaction) throws Exception {

		ModelProtein modelProtein = this.modelproteinDAO.getModelProtein(idProtein);


		//		if(enzyme == null) {
		//			enzyme = this.modelproteinDAO.insertModelEnzyme(idProtein, ecnumber, true, source);
		//		}
		//		else {
		//			enzyme.setInModel(true);
		//			if(editedReaction)
		//				enzyme.setSource("MANUAL");
		//			this.modelproteinDAO.update(enzyme);
		//		}

		if (modelProtein != null) {

			Set<ModelReactionHasModelProtein> reactions = modelProtein.getModelReactionHasModelProteins();

			for(ModelReactionHasModelProtein reactionHasEnzyme :reactions) {

				ModelReaction reaction = reactionHasEnzyme.getModelReaction();

				int reactionLabelId = this.reactionDAO.getModelReactionLabelByReactionId(reactionHasEnzyme.getId().getModelReactionIdreaction());

				if(editedReaction)
					this.modelReactionLabelsDAO.updateSourceByReactionLabelId(reactionLabelId, "MANUAL");
				reaction.setInModel(true);
				this.reactionDAO.update(reaction);
			}
		}

	}

	@Override
	public Integer insertNewProteinEntry(String name, String classString) {

		ModelProtein protein = new ModelProtein();

		protein.setClass_(classString);
		protein.setName(name);

		return (Integer) this.modelproteinDAO.save(protein);

	}

	@Override
	public Integer insertNewProteinEntry(ProteinContainer protein) {

		ModelProtein prot = new ModelProtein();

		String name = protein.getName();
		String class_ = protein.getClass_();
		String inchi = protein.getInchi();
		Float molWeight = protein.getMolecularWeight();
		Float molWeightExp = protein.getMolecularWeightExp();
		Float molWeightKd = protein.getMolecularWeightKd();
		Float molWeightSeq = protein.getMolecularWeightSeq();
		Float pi = protein.getPi();
		String ecnumber = protein.getExternalIdentifier();
		String source = protein.getSource();
//		Boolean inModel = protein.getInModel();

		if(name != null)
			prot.setName(name);

		if(class_ != null)
			prot.setClass_(class_);

		if(inchi != null)
			prot.setInchi(inchi);

		if(molWeight != null)
			prot.setMolecularWeight(molWeight);

		if(molWeightExp != null)
			prot.setMolecularWeightExp(molWeightExp);

		if(molWeightKd != null)
			prot.setMolecularWeightKd(molWeightKd);

		if(molWeightSeq != null)
			prot.setMolecularWeightSeq(molWeightSeq);

		if(pi != null)
			prot.setPi(pi);

		if(ecnumber != null)
			prot.setEcnumber(ecnumber);

		if(source != null)
			prot.setSource(source);

//		if(inModel != null)
//			prot.setInModel(inModel);

		return (Integer) this.modelproteinDAO.save(prot);

	}

	public boolean insertProtein(ProteinContainer protein,
			String[] synonyms, String[] enzymes) throws Exception{

//		protein.setInModel(inModel[0]);

		Integer proteinID = this.insertNewProteinEntry(protein);

		for(int s=0; s<synonyms.length; s++) {

			if(!synonyms[s].equals("")) {

				List<Integer> res = this.aliasDAO.getModelAliasesEntityByAttributes("p", proteinID, protein.getName());

				if(res.isEmpty()) {
					this.aliasDAO.insertModelAlias("p", proteinID, synonyms[s] , null); //confirmar este insert

				}
			}
		}

		List<String> enzymes_ids = new ArrayList<String>();
		int i = 0;
		for(String id : enzymes) {

			enzymes_ids.add(i,id);
			i++;

		}

		for(String id : enzymes_ids) {

			if(!id.isEmpty()) {
				boolean exists = this.modelproteinDAO.checkEnzymeInModelExistence(proteinID, "MANUAL");

				if(!exists) {

					this.modelproteinDAO.updateProteinSetEcNumberSourceAndInModel(proteinID, id, "MANUAL");

				}

			this.insertEnzymes(proteinID, id, false);
			}
		}

		return true;
	}

	public boolean updateProtein(ProteinContainer protein, String[] synonyms,
			String[] oldSynonyms, String[] enzymes, String[] oldEnzymes) throws Exception {

		Integer proteinID = protein.getIdProtein();

		ModelProtein prot = this.modelproteinDAO.getModelProtein(proteinID);
		String name = protein.getName();
		String class_ = protein.getClass_();
		String inchi = protein.getInchi();
		Float molWeight = protein.getMolecularWeight();
		Float molWeightExp = protein.getMolecularWeightExp();
		Float molWeightKd = protein.getMolecularWeightKd();
		Float molWeightSeq = protein.getMolecularWeightSeq();
		Float pi = protein.getPi();
		String ecnumber = protein.getExternalIdentifier();

		if(name != null)
			prot.setName(name);

		if(class_ != null)
			prot.setClass_(class_);

		if(inchi != null)
			prot.setInchi(inchi);

		if(molWeight != null)
			prot.setMolecularWeight(molWeight);

		if(molWeightExp != null)
			prot.setMolecularWeightExp(molWeightExp);

		if(molWeightKd != null)
			prot.setMolecularWeightKd(molWeightKd);

		if(molWeightSeq != null)
			prot.setMolecularWeightSeq(molWeightSeq);

		if(pi != null)
			prot.setPi(pi);

		if(ecnumber != null)
			prot.setEcnumber(ecnumber);

//		if(inModel != null)
//			prot.setInModel(inModel[0]);

		for(int s=0; s<synonyms.length; s++) {

			if(oldSynonyms.length>s) {

				if(!synonyms[s].equals("")) {

					if(!synonyms[s].equals(oldSynonyms[s])) {
						this.aliasDAO.updateModelAliasesAliasByClassAndEntityAndAlias("p", proteinID, synonyms[s], oldSynonyms[s]);
					}
				}
				else {
					this.aliasDAO.removeAllModelAliasesByEntityAndAliases(proteinID, oldSynonyms[s]);
				}
			}			
			else {

				if(!synonyms[s].equals("")) {
					this.aliasDAO.insertModelAliasClassAndAliasAndEntity("p", synonyms[s], proteinID);
				}
			}

			this.modelproteinDAO.save(prot);
		}

		//		List<Integer> old_enzymes_ids = new ArrayList<>();
		//		List<Integer> enzymes_ids = new ArrayList<>();

		//		int i = 0;
		//				for(String id : oldEnzymes) {
		//
		//					old_enzymes_ids.add(i,id);
		//					i++;
		//				}

		//		i = 0;
		//		for(String id : enzymes) {
		//
		//			//	enzymes_ids.add(i,id);
		//			i++;
		//		}

		//		List<String> enzymes_ids_add = new ArrayList<String>();

		//				for(String id : enzymes_ids) {
		//
		//					if(!id.equals("dummy") && !id.isEmpty()) {
		//
		//						if(old_enzymes_ids.contains(id)) {
		//
		//							old_enzymes_ids.remove(id);
		//						}
		//						else {
		//
		//							enzymes_ids_add.add(id);
		//						}
		//					}
		//				}

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//				for(String id : old_enzymes_ids) {
		//					this.enzymeservice.removeEnzymesAssignments(id, enzymes_ids_add, inModel, proteinID, false);
		//				}

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//				for(String id : enzymes_ids) {
		//
		//					if(enzymes_ids_add.contains(id)) {
		//
		//						this.insertEnzymes(proteinID, id, false);
		//					}
		//					else {
		//
		//						if(inModel[enzymes_ids.indexOf(id)]) {
		//							this.insertEnzymes(proteinID, id, true);
		//
		//						}
		//						else {
		//							//this.enzymeservice.removeEnzymesAssignments(id, enzymes_ids_add, inModel, proteinID, false);
		//						}
		//					}
		//				}
		return true;
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	/**
	 * Calculate number of proteins.
	 * @param stmt
	 * @return int[]
	 * @
	 */
	@Override
	public int[] countProteins() {

		int[] res = new int[2];

		int num = 0;
		int noname = 0;

		List<ModelProtein> proteins = this.modelproteinDAO.findAll();

		for(ModelProtein protein : proteins) {

			num++;

			if(protein.getName() == null || protein.getName().isEmpty())
				noname++;
		}

		res[0] = num;
		res[1] = noname;

		return res;
	}

	@Override
	public List<String[]> getAllEnzymes() throws Exception {
		return this.modelproteinDAO.getAllEnzymes();
	}
	
	@Override
	public List<String[]> getAllEncodedEnzymes() throws Exception {
		return this.modelproteinDAO.getAllEncodedEnzymes();
	}

	@Override
	public Map<Integer, Long> getProteinsData2() throws Exception {
		return this.modelproteinDAO.getProteinsData2();
	}

	@Override
	public String[][] getProteins() throws Exception {

		return this.modelproteinDAO.getProteins();

	}

	@Override
	public List<ProteinContainer> getDataFromEnzyme(Integer pathId) throws Exception {
		return this.modelproteinDAO.getModelProteinAttributes(pathId);
	}

	@Override
	public long countProteinsEnzymesNotLikeSource(String source) throws Exception {
		return this.modelproteinDAO.countModelProteinDistinctProteinIdNotLikeSource(source);

	}

	@Override
	public long countProteinsTransporters() throws Exception {
		return this.modelproteinDAO.getModelProteinDistinctProteinIdBySource(SourceType.TRANSPORTERS.toString());
	}

	@Override
	public boolean checkModelEnzymaticCofactorEntry(int compoundID, int protID) throws Exception {

		return this.cofactorDAO.getModelEnzymaticCofactorProteinIdByattributes(protID, compoundID);
	}

	@Override
	public void insertModelEnzymaticCofactor(int compoundID, int protID) throws Exception {

		ModelEnzymaticCofactor cofactor = new ModelEnzymaticCofactor();
		ModelEnzymaticCofactorId id = new ModelEnzymaticCofactorId(protID, compoundID);

		cofactor.setId(id);

		this.cofactorDAO.save(cofactor);
	}

	@Override
	public List<Integer> getEnzymesStats(String originalReaction) throws Exception {
		List<Integer> result = new ArrayList<Integer>();

		int enz_num=0,hom_num=0,kegg_num=0,man_num=0,trans_num=0;
		int encoded_enz=0, encoded_hom=0, encoded_kegg=0, encoded_man=0, encoded_trans=0;

		List<ModelProtein>  res = this.modelproteinDAO.getDistincModelProteinAttributesByAtt(true);

		if (res != null)
			for (ModelProtein x : res){

				if(x.getSource().equalsIgnoreCase(SourceType.TRANSPORTERS.toString())) {

					trans_num++;

//					if(x.getInModel())
//						encoded_trans++;
				}
				else {

					if(x.getSource().equalsIgnoreCase(SourceType.KEGG.toString())) {

						kegg_num++;

//						if(x.getInModel()){
//
//							encoded_kegg++;
//							encoded_enz++;
//
//						}
					}

					if(x.getSource().equalsIgnoreCase(SourceType.HOMOLOGY.toString())) {

						hom_num++;

//						if(x.getInModel()){
//
//							encoded_hom++;
//							encoded_enz++;
//						}
					}

					if(x.getSource().equalsIgnoreCase(SourceType.MANUAL.toString())) {

						man_num++;

//						if(x.getInModel()){
//
//							encoded_man++;
//							encoded_enz++;
//						}
					}

					enz_num++;
				}
			}
		result.add(enz_num);
		result.add(hom_num);
		result.add(kegg_num);
		result.add(man_num);
		result.add(trans_num);
		result.add(encoded_enz);
		result.add(encoded_hom);
		result.add(encoded_kegg);
		result.add(encoded_man);
		result.add(encoded_trans);

		//array structure: [enz_num, hom_num, kegg_num, man_num, trans_num, encoded_enz, encoded_hom, encoded_kegg, encoded_man, encoded_trans]
		return result;
	}


//	@Override
//	public Pair<List<String>, Boolean[]> getECnumber(int protId) throws Exception {
//		List<String> enzymesIDs = new ArrayList<String>();
//		Pair<List<String>, Boolean[]> result = new Pair<List<String>, Boolean[]>(enzymesIDs, null);
//
//		Map<String, Boolean> res = this.modelproteinDAO.getModelProteinEcNumberAndInModelByProteinId(protId);
//
//		int i = 0;
//		Integer size = res.size();
//
//		Boolean[] inModel = new Boolean[size];
//
//		if (res != null)
//			for (String x : res.keySet()) {
//
//				enzymesIDs.add(i, x);
//				inModel[i] = res.get(x);
//			}
//
//		result.setA(enzymesIDs);
//		result.setB(inModel);
//
//		return result;
//	}


//	@Override
//	public String[] getECnumber2(int protId) throws Exception {
//		String[] list = new String[2];
//
//		String temp = "", tempBoolean="";
//
//		Map<String, Boolean> res = this.modelproteinDAO.getModelProteinEcNumberAndInModelByProteinId(protId);
//
//		if (res != null)
//			for (String x : res.keySet()){
//
//				temp+=(x+";");
//
//				if(res.get(x))
//					tempBoolean+=("true;");
//				else
//					tempBoolean+=("false;");
//			}
//
//		list[0] = temp;
//		list[1] = tempBoolean;
//
//		return list;
//	}

	@Override
	public void removeEnzymesAssignments(String ecnumber, Boolean[] inModel, List<String> enzymes_ids, Integer proteinID, boolean removeReaction) throws Exception {

		String ec = ecnumber;

		if(removeReaction) {

			this.modelproteinDAO.removeAllEnzymeByProteinId(proteinID);
		}
		else {
			this.modelproteinDAO.updateProteinSetEcNumberSourceAndInModel(proteinID, ec, null);

		}

		List<Integer> reactionsIDs = this.reactionDAO.getDistinctReactionsByEnzymeAndCompartmentalized(proteinID, false, false);
		//reactionsIDs = ModelAPI.getReactionsIDs2(reactionsIDs, proteinID, ec); //nao tenho a tabela

		for(int idreaction: reactionsIDs) {

			List<String[]> proteins_array = new ArrayList<String[]>();
			List<String[]> result = this.reactionHasEnzymeDAO.getAllModelReactionHasModelProteinByreactionId(idreaction);

			for(int i=0; i<result.size(); i++){
				String[] list = result.get(i);

				if(Integer.parseInt(list[0])==proteinID && ecnumber.equalsIgnoreCase(list[1])) {}
				else {

					if(Integer.parseInt(list[0])==proteinID && enzymes_ids.contains(list[1])) {

						if(inModel[enzymes_ids.indexOf(list[1])]) {

							proteins_array.add(new String[] {list[0],list[1]});
						}
					}
					else {

						proteins_array.add(new String[] {list[0],list[1]});
					}
				}
			}

			int reactionLabelID = this.reactionDAO.getModelReactionLabelByReactionId(idreaction);

			if(proteins_array.isEmpty()) {
				this.reactionDAO.updateInModelByReactionId(idreaction, false);
				this.modelReactionLabelsDAO.updateSourceByReactionLabelId(reactionLabelID, "MANUAL");
			}
		}
	}

	@Override
	public void commitToDatabase(String blastDatabase, Float threshold, Map<Integer, String[]>  annotationEditedEnzymeData, Map<Integer, String> annotationEnzymesList, Map<Integer, String[]> annotationEditedProductData, Map<Integer, String> annotationProductList,
			Map<Integer, String> annotationNamesList, Map<Integer, String> annotationNotesMap, Map<Integer, String> annotationLocusList, Float upperThreshold, Float alpha, Float beta, Integer minHits ) throws Exception {

		//boolean result = true;

		this.saveDatabaseStatus(blastDatabase, threshold, upperThreshold, alpha, beta, minHits);

		this.scorerconfigDAO.updateEnzymesAnnotationLatestByDB(blastDatabase);

		Map<Integer, String> database_locus = this.enzGeneHomologyDAO.getEnzymesAnnotationGenehomologySKeyAndLocusTag();

		for(Integer key : annotationLocusList.keySet()) {

			boolean exists = this.homologyDataDAO.checkHomologyDataByGeneHomologySKey(key);

			//String query = null;

			if(exists)	
				this.homologyDataDAO.updateEnzymesAnnotationHomologydataLocusTagByHomologySkey(annotationLocusList.get(key), key);

			else {
				EnzymesAnnotationGeneHomology gene = this.enzGeneHomologyDAO.getEnzymesAnnotationGeneHomology(key);
				this.homologyDataDAO.insertHomologyData(gene, annotationLocusList.get(key), null, null, null, null, null, null);

			}
		}

		for(Integer key : annotationNamesList.keySet()) {

			boolean exists = this.homologyDataDAO.checkHomologyDataByGeneHomologySKey(key);

			if(annotationNamesList.get(key)!=null && !annotationNamesList.get(key).equalsIgnoreCase("null") && database_locus.containsKey(key)) {

				if(exists){
					this.homologyDataDAO.updateEnzymesAnnotationHomologydataNameByHomologySkey(annotationNamesList.get(key), key);
				}
				else{
					EnzymesAnnotationGeneHomology gene = this.enzGeneHomologyDAO.getEnzymesAnnotationGeneHomology(key);
					this.homologyDataDAO.insertHomologyData(gene, database_locus.get(key), annotationNamesList.get(key), null, null, null, null, null);
				}
			}
		}

		for(Integer key : annotationNotesMap.keySet()) {

			if(annotationNotesMap.get(key)!=null && !annotationNotesMap.get(key).equalsIgnoreCase("null") && //!enzymeAnnotation.getNotesMap().get(key).isEmpty()
					database_locus.containsKey(key)) {

				boolean exists = this.homologyDataDAO.checkHomologyDataByGeneHomologySKey(key);

				if(exists){
					this.homologyDataDAO.updateEnzymesAnnotationHomologydataNotesAndProductByHomologySkey(annotationNotesMap.get(key), null, key);
				}
				else{
					EnzymesAnnotationGeneHomology gene = this.enzGeneHomologyDAO.getEnzymesAnnotationGeneHomology(key);
					this.homologyDataDAO.insertHomologyData(gene, database_locus.get(key), null, null, null, null, null, annotationNotesMap.get(key));
				}
			}
		}

		for(Integer key : annotationProductList.keySet()) {

			if(annotationProductList.get(key)!=null && !annotationProductList.get(key).equalsIgnoreCase("null") && database_locus.containsKey(key)) {

				boolean exists = this.homologyDataDAO.checkHomologyDataByGeneHomologySKey(key);

				if(exists){
					this.homologyDataDAO.updateEnzymesAnnotationHomologydataNotesAndProductByHomologySkey(null, annotationProductList.get(key), key);

				}
				else {
					EnzymesAnnotationGeneHomology gene = this.enzGeneHomologyDAO.getEnzymesAnnotationGeneHomology(key);
					this.homologyDataDAO.insertHomologyData(gene, database_locus.get(key), null, annotationProductList.get(key), null, null, null, null);
				}
			}
		}

		for(Integer key : annotationEditedProductData.keySet()) {

			if(annotationEditedProductData.get(key)!=null && database_locus.containsKey(key)) {

				boolean exists = this.homologyDataDAO.checkHomologyDataByGeneHomologySKey(key);
				Integer s_key;

				if(exists){

					s_key = this.homologyDataDAO.getHomologyDataIDByGeneHomologySKey(key);
					this.homologyDataDAO.updateEnzymesAnnotationHomologydataNotesAndProductByHomologySkey(null, annotationProductList.get(key), key);

				}
				else {
					EnzymesAnnotationGeneHomology gene = this.enzGeneHomologyDAO.getEnzymesAnnotationGeneHomology(key);
					s_key = this.homologyDataDAO.insertHomologyData(gene, database_locus.get(key), null, annotationProductList.get(key), null, null, null, null);


				}

				boolean exists2 = this.productListDAO.checkEnzymesAnnotationProductListByHomologyDataSKey(s_key);

				if(exists2) {
					this.productListDAO.removeEnzymesAnnotationProductListByHomologyDataSkey(s_key);

				}

				String [] products = annotationEditedProductData.get(key);

				for(String product : products) {
					this.productListDAO.insertEnzymesAnnotationProductListHomologyDataSkeyAndOtherNames(s_key, product);
				}
			}
		}

		for(Integer key : annotationEnzymesList.keySet()) {

			if(annotationEnzymesList.get(key)!=null && !annotationEnzymesList.get(key).equalsIgnoreCase("null") && database_locus.containsKey(key)) {

				boolean exists = this.homologyDataDAO.checkHomologyDataByGeneHomologySKey(key);

				if(exists) {
					this.homologyDataDAO.updateEnzymesAnnotationHomologydataAttributesByHomologySkey(key, null, null, null, annotationEnzymesList.get(key));

				}
				else {
					EnzymesAnnotationGeneHomology gene = this.enzGeneHomologyDAO.getEnzymesAnnotationGeneHomology(key);
					this.homologyDataDAO.insertHomologyData(gene, database_locus.get(key), null, null, annotationEnzymesList.get(key), null, null, null);
				}
			}
		}

		for(Integer key : annotationEditedEnzymeData.keySet()) {

			if(annotationEditedEnzymeData.get(key)!=null && database_locus.containsKey(key)) {

				boolean exists = this.homologyDataDAO.checkHomologyDataByGeneHomologySKey(key);

				Integer s_key;

				if(exists) {

					s_key =  this.homologyDataDAO.getHomologyDataIDByGeneHomologySKey(key);
					this.homologyDataDAO.updateEnzymesAnnotationHomologydataAttributesByHomologySkey(key, null, null, null, annotationEnzymesList.get(key));

				}
				else {
					EnzymesAnnotationGeneHomology gene = this.enzGeneHomologyDAO.getEnzymesAnnotationGeneHomology(key);
					s_key = this.homologyDataDAO.insertHomologyData(gene, database_locus.get(key), null, null, annotationEnzymesList.get(key), null, null, null);
				}

				boolean exists2 = this.ecnumberListDAO.checkEcNumberHasenzymesAnnotationHomologydata(s_key);
				if(exists2){
					this.ecnumberListDAO.removeEnzymesAnnotationEcNumberListByHomologyDataSkey(s_key);
				}

				//////////////////////////////////////////////////////////////////////////////////////////

				String [] ecs = annotationEditedEnzymeData.get(key);
				for(String ec : ecs) {

					this.ecnumberListDAO.insertEnzymesAnnotationEcNumberListSkeyAndOtherEcNumbers(s_key, ec);			
				}
			}
		}

		//		enzymeAnnotation.setEditedProductData(new TreeMap<Integer, String[]>());
		//		enzymeAnnotation.setEditedEnzymeData(new TreeMap<Integer, String[]>());
		//		enzymeAnnotation.setLocusList(new TreeMap<Integer, String>());
		//		enzymeAnnotation.setNamesList(new TreeMap<Integer, String>());
		//		enzymeAnnotation.setNotesMap(new TreeMap<Integer, String>());
		//		enzymeAnnotation.setProductList(new TreeMap<Integer, String>());
		//		enzymeAnnotation.setEnzymesList(new TreeMap<Integer, String>());
	}

	@Override
	public void saveDatabaseStatus(String blastDatabase, Float threshold, Float upperThreshold, Float alpha, Float beta, Integer minHits) throws Exception {


		List<String> data = new ArrayList<String>();

		List<EnzymesAnnotationScorerConfig> res = this.scorerconfigDAO.getAllScorerConfigByBlastDB(blastDatabase);

		if (res != null)
			for (EnzymesAnnotationScorerConfig x : res) {

				data.add(String.valueOf(x.getId().getThreshold()));
				data.add(String.valueOf(x.getId().getUpperThreshold()));
				data.add(String.valueOf(x.getId().getBalanceBh()));
				data.add(String.valueOf(x.getId().getAlpha()));
				data.add(String.valueOf(x.getId().getBeta()));
				data.add(String.valueOf(x.getId().getMinHomologies()));
				data.add(String.valueOf(x.getId().getDatabaseName()));
			}

		if(data.size()>0) { //falta o upperthreshold
			this.scorerconfigDAO.updateEnzymesAnnotationAttributesByDB(blastDatabase, threshold, upperThreshold, 0.0f, beta, alpha, minHits);

		}
		else{
			this.scorerconfigDAO.insertEnzymesAnnotationScorerConfigAttributes(threshold, upperThreshold, 0.0f, alpha, beta, minHits, blastDatabase, false, false);
		}
	}



	@Override
	public boolean checkEnzymeInModelExistence(Integer protId, String source) throws Exception {

		return modelproteinDAO.checkEnzymeInModelExistence(protId, source);
	}

	@Override
	public List<String> getECNumbersWithModules() throws Exception {

		return modelproteinDAO.getECNumbersWithModules();
	}


	@Override
	public List<ProteinContainer> getEnzymesModel() throws Exception {

		return modelproteinDAO.getEnzymesModel();
	}

	@Override
	public Map<String,Integer> getEnzymeEcNumberAndProteinID() throws Exception {

		return modelproteinDAO.getEnzymeEcNumberAndProteinID();
	}

	@Override
	public 	Map<Integer, List<Integer>> getEnzymesCompartments() throws Exception {

		return modelproteinDAO.getEnzymesCompartments();
	}

	@Override
	public 	String getEnzymeEcNumberByProteinID(Integer proteinId) throws Exception {

		return modelproteinDAO.getEnzymeEcNumberByProteinID(proteinId);
	}

	public void updateProteinSetEcNumberSourceAndInModel(Integer model_protein_idprotein, String ecnumber, boolean inModel, String source) throws Exception {

		this.modelproteinDAO.updateProteinSetEcNumberSourceAndInModel(model_protein_idprotein, ecnumber, source);

	}

	public List<ProteinContainer> getProducts() throws Exception {

		return this.modelproteinDAO.getGeneQueryAndProteinName();

	}

	@Override
	public ProteinContainer getProteinEcNumberAndInModelByProteinID(Integer proteinId) throws Exception {

		ModelProtein retrievedProt = modelproteinDAO.getProteinWithEcnumberNotNullByProteinID(proteinId);

		if(retrievedProt != null) {

			ProteinContainer container = new ProteinContainer(proteinId, retrievedProt.getEcnumber());
//			container.setInModel(retrievedProt.getInModel());

			return container;
		}

		return null;
	}

	public ProteinContainer getProteinIdByEcNumber(String ecNumber) throws Exception {

		return this.modelproteinDAO.getProteinIdByEcNumber(ecNumber);

	}

	@Override
	public List<ProteinContainer> getProteinIdByIdgene(Integer idGene) throws Exception{

		return this.modelproteinDAO.getProteinIdByIdgene(idGene);
	}

	@Override
	public void updateProteinSetEcNumber(Integer idProtein, String ecNumber) throws Exception {

		ModelProtein protein = this.modelproteinDAO.getModelProtein(idProtein);

		if(ecNumber != null)
			protein.setEcnumber(ecNumber);

		this.modelproteinDAO.save(protein);

	}

	@Override
	public void removeProtein(Integer proteinId) throws Exception {

		ModelProtein protein = this.modelproteinDAO.getModelProtein(proteinId);

		if(protein != null)
			this.modelproteinDAO.delete(protein);
	}

	@Override
	public List<ProteinContainer> getEnzymeHasReaction() throws Exception{

		return this.modelproteinDAO.getEnzymeHasReaction();
	}

	@Override
	public List<String[]> getProteinComposition() throws Exception{

		return this.modelprotcompositionDAO.getProteinComposition();
	}

	@Override
	public List<CompartmentContainer> getProteinCompartmentsByProteinId(Integer proteinId) throws Exception{

		return this.modelproteinDAO.getProteinCompartmentsByProteinId(proteinId);
	}

}
