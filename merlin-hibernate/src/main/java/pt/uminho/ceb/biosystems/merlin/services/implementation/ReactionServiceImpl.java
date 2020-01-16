package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelCompartmentDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasReactionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionLabelsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelStoichiometryDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStoichiometry;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IReactionService;
import pt.uminho.ceb.biosystems.merlin.utilities.Utilities;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class ReactionServiceImpl implements IReactionService {


	private IModelReactionDAO modelreactionDAO;
	private IModelReactionHasModelProteinDAO modelreactionhasproteinDao;
	private IModelPathwayHasReactionDAO pathHasReactionDAO;
	private IModelStoichiometryDAO stoichDAO;
	private IModelCompartmentDAO compartmentDAO;
	private IModelReactionLabelsDAO modelreactionLabelsDAO;
	private IModelGeneDAO modelGeneDAO;

	public ReactionServiceImpl(IModelReactionDAO modelreaction, IModelReactionHasModelProteinDAO modelreactionhasproteinDao,
			IModelPathwayHasReactionDAO	pathHasReaction, IModelStoichiometryDAO stoichDAO, IModelCompartmentDAO compartmentDAO,
			IModelReactionLabelsDAO modelreactionLabelsDAO, IModelGeneDAO modelGeneDAO) { 
		this.modelreactionDAO = modelreaction;
		this.modelreactionhasproteinDao = modelreactionhasproteinDao;
		this.pathHasReactionDAO = pathHasReaction;
		this.stoichDAO = stoichDAO;
		this.compartmentDAO = compartmentDAO;
		this.modelreactionLabelsDAO = modelreactionLabelsDAO;
		this.modelGeneDAO = modelGeneDAO;

	}
	
	private ReactionContainer buildContainer(ModelReaction reaction) {
		
		Integer reactionLabelId = modelreactionDAO.getModelReactionLabelByReactionId(reaction.getIdreaction());
		ModelReactionLabels reactionLabel = modelreactionLabelsDAO.getModelReactionLabels(reactionLabelId);
		
		ReactionContainer container = new ReactionContainer(reaction.getIdreaction(), reactionLabel.getName(), reactionLabel.getEquation());

		container.setInModel(reaction.getInModel());
		container.setGeneric(reactionLabel.getIsGeneric());
		container.setSpontaneous(reactionLabel.getIsSpontaneous());
		container.setNon_enzymatic(reactionLabel.getIsNonEnzymatic());
		container.setSource(SourceType.valueOf(reactionLabel.getSource()));
		container.setLowerBound((double) reaction.getLowerBound());
		container.setUpperBound((double) reaction.getUpperBound());
		container.setNotes(reaction.getNotes());
		container.setGeneRule(reaction.getBooleanRule());

		
		return container;
	}

	public ModelReactionLabels insertNewModelReactionLabel(String equation, boolean isGeneric, boolean isNonEnzymatic, boolean isSpontaneous, String name, String source) {

		ModelReactionLabels reactionLabel = new ModelReactionLabels();

		reactionLabel.setEquation(equation);
		reactionLabel.setIsGeneric(isGeneric);
		reactionLabel.setIsNonEnzymatic(isNonEnzymatic);
		reactionLabel.setIsSpontaneous(isSpontaneous);
		reactionLabel.setName(name);
		reactionLabel.setSource(source);

		//		this.modelreactionLabelsDAO.addModelReactionLabels(reactionLabel);
		Integer id = (Integer) this.modelreactionhasproteinDao.save(reactionLabel);

		reactionLabel.setIdreactionLabel(id);

		return reactionLabel;
	}

	@Override
	public boolean addReaction_has_Enzyme(Integer idprotein, Integer idReaction) throws Exception {
		List<ModelReactionHasModelProtein> res = this.modelreactionhasproteinDao.getAllModelReactionHasModelProteinByAttributes(idprotein, idReaction);

		if (res != null && !res.isEmpty()) {
			return false;
		}
		else {
			this.modelreactionhasproteinDao.insertModelReactionHasModelProtein(idReaction, idprotein);
			return true;
		}
	}

	@Override
	public Map<Integer, ReactionContainer> getReactionsData(String ecnumber, Integer id, boolean isCompartimentalized) throws Exception {
		return this.modelreactionDAO.getModelReactionsData(ecnumber, id, isCompartimentalized);
	}

	@Override
	public boolean removeSelectedReaction(Integer reaction_id) throws Exception {
		return this.modelreactionDAO.deleteModelReactionByReactionId(reaction_id);
	}

	@Override
	public boolean removeSelectedReactionLabel(Integer reactionLabelId) throws Exception {
		return this.modelreactionLabelsDAO.deleteModelReactionLabelsById(reactionLabelId);
	}
	
	
	@Override
	public List<ModelReactionHasModelProtein> getProteinHasReaction() throws Exception {
		return this.modelreactionhasproteinDao.getAllModelReactionHasModelProteinOrderByReactionId();
	}

	@Override
	public List<String[]> getModelReactionNameAndECNumber(boolean originalreactions) throws Exception {
		return this.modelreactionDAO.getModelReactionNameAndECNumber(originalreactions);
	}

	@Override
	public Boolean checkReactionNotLikeSourceAndNotReversible(String source) throws Exception {
		return this.modelreactionDAO.checkReactionNotLikeSourceAndNotReversible(source);
	}

	@Override
	public List<ArrayList<String>> getReactions(Integer id, Integer compartment, boolean isCompartimentalized) throws Exception {
		List<String[]> res = this.modelreactionDAO.getModelReactions(compartment, id, isCompartimentalized);
		List<ArrayList<String>> ret = new ArrayList<>();
		if (res != null)
			for (String[] x : res){

				ArrayList<String> ql = new ArrayList<>(); 
				ql.add(x[1]);
				ql.add(x[2]);
				ql.add(x[3]);

				if(Boolean.valueOf(x[4]))
					ql.add(Boolean.valueOf(x[4])+"");
				else
					ql.add("-");

				if((Double.valueOf(x[5]) < 0 && Double.valueOf(x[6]+"") > 0))
					ql.add(Boolean.valueOf(x[5])+"");
				else
					ql.add("-");

				ret.add(ql);
			}

		return ret;
	}


	@Override
	public long countReactionsInModel(boolean isCompartimentalized) throws Exception { 
		return this.modelreactionDAO.countModelReactionDistinctReactionsIdsByInModel(isCompartimentalized);
	}

	@Override
	public long countPathwayHasReactionByReactionId(Integer id) throws Exception {
		return this.modelreactionDAO.countModelReactionDistinctReactionsIdsByReactionId(id);
	}

	@Override
	public List<ProteinContainer> getEnzymesForReaction(Integer idReaction) throws Exception {

		return this.modelreactionDAO.getEnzymesForReaction(idReaction);
	}


	/**
	 *@deprecated
	 * This method is already available here as getDatabaseReactionContainer()
	 */
	public List<ModelReaction> getReactionData(Integer id) throws Exception {  
		//		return this.modelreactionDAO.getAllModelReactionByReactionId(id);
		return null;
	}


	/**
	 *@deprecated
	 * This method is already available here as getDatabaseReactionContainer()
	 */
	public String[] getReactionData2(Integer reactionId) throws Exception { 
		//		List<ModelReaction> list = this.modelreactionDAO.getAllModelReactionByReactionId(reactionId);
		//
		//		int reactionModelID = this.modelreactionDAO.getModelReactionLabelByReactionId(reactionId);
		//		ModelReactionLabels listLabels = this.modelreactionLabelsDAO.getModelReactionLabels(reactionModelID);
		//		String[] res = new String[12];
		//
		//		if (list != null) {
		//			for (ModelReaction x : list) {
		//				res[0] = (String) listLabels.getName();
		//				res[1] = (String) listLabels.getEquation();
		//				res[2] = // position for reversible
		//						res[3] = String.valueOf(x.getInModel());
		//				res[4] = (String) x.getModelCompartment().getName();
		//				res[5] = String.valueOf(listLabels.getIsSpontaneous());
		//				res[6] = String.valueOf(listLabels.getIsNonEnzymatic());
		//				res[7] = String.valueOf(listLabels.getIsGeneric());
		//				res[8] = String.valueOf(x.getLowerBound());
		//				res[9] = String.valueOf(x.getUpperBound());
		//				res[10] = (String) listLabels.getSource();
		//				res[11] = (String) x.getBooleanRule();
		//			}
		//		}
		return null;
	}

	@Override
	public Pair<Boolean, Boolean> checkReactionIsReversibleAndInModel(Integer reactionId) throws Exception {
		ModelReaction reaction = this.modelreactionDAO.getModelReactionByReactionId(reactionId);

		Pair<Boolean, Boolean> res = null;
		if (reaction != null) {

			res = new Pair<Boolean, Boolean>((reaction.getLowerBound() < 0 && reaction.getUpperBound() > 0), reaction.getInModel());

		}
		return res;	
	}

	@Override
	public boolean isCompartimentalizedModel() throws Exception {
		
		return this.modelreactionDAO.isCompartimentalizedModel();
	}

	@Override
	public Pair<String, String> getEquationAndSourceFromReaction(Integer reactionID) throws Exception {
		//		List<ModelReaction> list = this.modelreactionDAO.getAllModelReactionByReactionId(reactionID);

		int reactionLabelId = this.modelreactionDAO.getModelReactionLabelByReactionId(reactionID);

		ModelReactionLabels reactionLabel = this.modelreactionLabelsDAO.getModelReactionLabels(reactionLabelId);

		Pair<String, String> res = null;
		if (reactionLabel != null) {
			res = new Pair<String, String>(reactionLabel.getEquation(), reactionLabel.getSource());

		}
		return res;	
	}

	@Override
	public List<Integer> getReactionsIDs(Integer proteinId, String ecNumber) throws Exception {
		return this.modelreactionDAO.getDistinctReactionsByEnzymeAndCompartmentalized(proteinId, false, false);

	}

	@Override
	public List<String[]> getReactionHasEnzymeData2(Integer reactionId) throws Exception {
		return this.modelreactionhasproteinDao.getModelReactionHasModelProteinProteinIdAndEcNumberByreactionId(reactionId);

	}

	@Override
	public List<Integer> getDataFromReaction(String ecnumber) throws Exception {
		return this.modelreactionDAO.getDistinctModelReactionIdByEcNumber(ecnumber);
	}

	@Override
	public List<Pair<String, String>> getReactionHasEnzyme(boolean isCompartimentalised) throws Exception {
		return this.modelreactionhasproteinDao.getModelReactionHasModelProteinData(isCompartimentalised);
	}
	
	@Override
	public List<Pair<Integer, String>> getReactionHasEnzyme2(boolean isCompartimentalised) throws Exception {
		return this.modelreactionhasproteinDao.getModelReactionHasModelProteinData2(isCompartimentalised);
	}

	@Override
	public List<String[]> getStoichiometry(boolean isCompartmentalizedModel) throws Exception {
		return this.modelreactionDAO.getModelReactionStoichiometryData(isCompartmentalizedModel);
	}

	@Override
	public List<String[]> getDataFromReaction2() throws Exception {
		return this.modelreactionDAO.getModelReactionsData2();
	}

	@Override
	public List<ReactionContainer> getAllModelReactionByInModel() throws Exception {
		return this.modelreactionDAO.getAllModelReactionByInModel();
	}

	@Override
	public Map<Integer, String> getLabelsByExternalIdentifiers(String name) throws Exception {
		return this.modelreactionLabelsDAO.getLabelsByExternalIdentifiers(name);
	}

	@Override
	public long countReactionsByInModelAndSource(boolean idCompartimentalized, boolean inModel, String source) throws Exception {
		return this.modelreactionDAO.countModelReactionDistinctReactionsIdsBySourceAndInModel(idCompartimentalized, inModel, source);
	}

	@Override
	public String[] getBooleanRuleFromReaction(Integer id) throws Exception { 
		String[] res = new String[2]; 

		res[0] = "0";
		res[1] = null;

		String result = this.modelreactionDAO.getModelReactionBooleanRuleByReactionId(id);

		if (result != null) {
			res[0] = "1";
			res[1] = result;
		}
		return res;
	}

	@Override
	public List<String[]> getEnzymeProteinID(Integer reactionId) throws Exception {
		return this.modelreactionhasproteinDao.getModelReactionHasModelProteinProteinIdAndEcNumberByreactionId(reactionId);
	}

	@Override
	public List<String> getEcNumbersList(Integer reactId) throws Exception {
		return this.modelreactionhasproteinDao.getModelReactionHasModelProteinEcNumberByReactionId(reactId);
	}


	public List<String[]> getEnzymesByReaction(Integer idReaction) throws Exception {
		return this.modelreactionhasproteinDao.getAllModelReactionHasModelProteinByreactionId(idReaction);
	}


	public List<String[]> getEnzymesByReactionAndPathway(Integer reaction, Integer path) throws Exception {
		return this.modelreactionDAO.getDistinctModelReactionAttributesByPathwayIdAndReactionId(path, reaction);
	}


	//	public boolean containsReactionByOriginalReaction(boolean originalReaction) throws Exception {
	//		boolean ret = false;
	//
	//		List<ModelReaction> list = this.modelreactionDAO.getAllModelReactionByOriginalReaction(originalReaction);
	//
	//		if(list != null)
	//			ret = true;
	//
	//		return ret;
	//	}


	public Map<Integer, String> getReactionsNames(List<Integer> identifiers) throws Exception {  //this should return a map!!!!!!!!!!

		Map<Integer, String> res = new HashMap<>();

		for (Integer id : identifiers) {
			String res_name = this.modelreactionLabelsDAO.getModelReactionLabelsNameByReactionId(modelreactionDAO.getModelReactionLabelByReactionId(id));
			res.put(id, res_name);
		}

		return res;
	}



	public boolean isReactionInModel(Integer reactionId) throws Exception {
		return this.modelreactionDAO.getModelReactionInModelByReactionId(reactionId);
	}


	public List<String> getExistingEnzymesID(Integer idReaction) throws Exception {
		List<String[]> list = this.modelreactionhasproteinDao.getAllModelReactionHasModelProteinByreactionId(idReaction);

		List<String> existingEnzymesID = new ArrayList<String>();

		if (list != null) {
			for (String[] x : list) {
				existingEnzymesID.add(x[0] + "___" + x[2] + "___" + x[1]);
			}
		}

		return existingEnzymesID;
	}

	@Override
	public boolean checkReactionHasEnzymeData(Integer idProtein, Integer idReaction) throws Exception {
		boolean exists = false;

		List<ModelReactionHasModelProtein> list = this.modelreactionhasproteinDao.getAllModelReactionHasModelProteinByAttributes(idProtein, idReaction);

		if (list != null && list.size() > 0)
			exists = true;
		return exists;
	}

	public List<String> getExistingEnzymesID2(Integer idReaction) throws Exception {
		List<String[]> list = this.modelreactionhasproteinDao.getAllModelReactionHasModelProteinByreactionId(idReaction);

		List<String> existingEnzymesID = new ArrayList<String>();

		if (list != null) {
			for (String[] x : list) {
				existingEnzymesID.add(x[0] + "___" + x[2] + "___" + x[1]);
			}
		}
		return existingEnzymesID;
	}

	@Override
	public boolean checkIfReactioLabelNameAlreadyExists(String name) throws Exception {
		boolean exists = false;

		ModelReactionLabels res = this.modelreactionLabelsDAO.getModelReactionLabelByName(name);

		if (res != null)
			exists = true;

		return exists;
	}

	@Override
	public Integer getReactionLabelIdByName(String name) throws Exception {

		ModelReactionLabels res = this.modelreactionLabelsDAO.getModelReactionLabelByName(name);

		if (res != null)
			return res.getIdreactionLabel();

		return null;

	}

	@Override
	public ModelReactionLabels getReactionLabelByName(String name) throws Exception {

		return this.modelreactionLabelsDAO.getModelReactionLabelByName(name);

	}

	public List<Integer> getReactionID(Integer proteinId, String ecNumber) throws Exception {
		return this.modelreactionDAO.getDistinctReactionsByEnzymeAndCompartmentalized(proteinId, true, false);
	}

	@Override
	public Map<Integer, List<Integer>> getEnzymesReactions2() throws Exception {
		Map<Integer, List<Integer>> enzymesReactions = new HashMap<>();
		List<Integer> reactionsIDs = null;

		List<Object[]> list = this.modelreactionDAO.getModelReactionDataBySource("TRANSPORTERS");

		if (list != null) {

			for (Object[] x : list) {
				reactionsIDs = new ArrayList<>();
				
				Integer proteinId = Integer.valueOf(x[2].toString());

				if(enzymesReactions.containsKey(proteinId))
					reactionsIDs = enzymesReactions.get(proteinId);

				reactionsIDs.add(Integer.valueOf(x[0].toString()));
				enzymesReactions.put(proteinId, reactionsIDs);
			}
		}
		return enzymesReactions;
	}

	@Override
	public boolean checkBiochemicalOrTransportReactions(boolean transporter) throws Exception {
		List<ModelReaction> list = this.modelreactionDAO.getModelReactionsBySourceAndCompartmentalized(SourceType.TRANSPORTERS.toString(), transporter);

		if (list != null && !list.isEmpty())
			return true;

		return false;
	}


	public String[] getEnzymesByReaction2(Integer idReaction) throws Exception {
		String[] res = new String[0];

		List<String[]> list = this.modelreactionhasproteinDao.getAllModelReactionHasModelProteinByreactionId(idReaction);
		if(list != null) {
			res = new String[list.size()];
			int col = 0;
			for (String[] x : list)

			{
				res[col] = x[0]+"___"+x[2] +"___"+x[1]; //
				col++;
			}
		}
		return res;
	} 


	public String[] getEnzymesByReactionAndPathway2(Integer reaction, Integer path) throws Exception {
		String[] res = new String[0];

		List<String[]> list = this.modelreactionDAO.getDistinctModelReactionAttributesByPathwayIdAndReactionId(path, reaction);

		if (list != null) {
			res = new String[list.size()];
			int col=0;

			for (String[] x : list) {

				res[col] = x[0]+"___"+x[2] +"___"+x[1]; //
				col++;
			}
		}

		return res;
	}

	@Override
	public List<Object[]> getReactionsList(Integer pathwayID, boolean noEnzymes, boolean isCompartimentalized) {

		return this.modelreactionDAO.getReactionsList(pathwayID, noEnzymes, isCompartimentalized);

	}

	@Override
	public Map<Integer, ReactionContainer> getEnzymesReactionsMap(boolean isTransporters) throws Exception {


		Map<Integer, ReactionContainer> reactionsMap = new HashMap<Integer, ReactionContainer>();

		List<ModelReaction> res1 = this.modelreactionDAO.getAllModelReactionsByTransportersType(isTransporters);

		if (res1 != null) {
			for (ModelReaction reaction : res1) {

				reactionsMap.put(reaction.getIdreaction(), buildContainer(reaction));
			}
		}

		List<ModelReactionHasModelProtein> res2 = this.modelreactionhasproteinDao.getAllModelReactionHasModelProtein();


		if (res2 != null) {
			for (ModelReactionHasModelProtein x : res2){

				if(reactionsMap.containsKey(x.getId().getModelReactionIdreaction())){

					Pair<Integer, String> pair = new  Pair<Integer, String>(x.getId().getModelProteinIdprotein(), x.getModelProtein().getEcnumber());

					reactionsMap.get(x.getId().getModelReactionIdreaction()).addProteinPair(pair);

				}
			}
		}

		List<ModelPathwayHasReaction> res3 = this.pathHasReactionDAO.getAllModelPathwayHasReaction();

		if (res3 != null) {
			for (ModelPathwayHasReaction x : res3){

				if(reactionsMap.containsKey(x.getId().getReactionIdreaction())) {

					reactionsMap.get(x.getId().getReactionIdreaction()).addPathway(x.getId().getPathwayIdpathway(), x.getModelPathway().getName());

				}
			}
		}

		List<ModelStoichiometry> res4 = this.stoichDAO.getAllModelStoichiometryBySourceAndOriginalReaction("TRANSPORTERS", true);

		if (res4 != null) {
			for (ModelStoichiometry x : res4){

				int reactionID = x.getModelReaction().getIdreaction();

				//				List<String[]> entry = new ArrayList<String[]>();
				if(reactionsMap.containsKey(reactionID)) {

					if(reactionsMap.containsKey(reactionID)) {

						if(x.getStoichiometricCoefficient() > 0)
							reactionsMap.get(reactionID).addProduct(x.getModelCompound().getIdcompound(), x.getStoichiometricCoefficient(), x.getModelCompartment().getIdcompartment());
						else
							reactionsMap.get(reactionID).addReactant(x.getModelCompound().getIdcompound(), x.getStoichiometricCoefficient(), x.getModelCompartment().getIdcompartment());
					}
				}
			}
		}
		return reactionsMap;
	}



	public ReactionContainer getDatabaseReactionContainer(Integer reactionId) throws Exception {

		ModelReaction reaction = this.modelreactionDAO.getModelReactionByReactionId(reactionId);

		if (reaction != null) {

			ReactionContainer container = buildContainer(reaction);

			if(reaction.getModelCompartment() != null) {

				CompartmentContainer comp = new CompartmentContainer(reaction.getModelCompartment().getIdcompartment(),
						reaction.getModelCompartment().getName(), reaction.getModelCompartment().getAbbreviation());

				container.setLocalisation(comp);
			}

			List<ModelReactionHasModelProtein> res2 = this.modelreactionhasproteinDao.getAllModelReactionHasModelProteinByReactionId(reactionId);

			if (res2 != null) {
				for (ModelReactionHasModelProtein x : res2){

					Pair<Integer, String> pair = new  Pair<Integer, String>(x.getId().getModelProteinIdprotein(), x.getModelProtein().getEcnumber());

					container.addProteinPair(pair);

				}
			}

			List<ModelPathwayHasReaction> res3 = this.pathHasReactionDAO.getAllModelPathwayHasReactionByReactionId(reactionId);

			if (res3 != null) {
				for (ModelPathwayHasReaction x : res3){
					container.addPathway(x.getId().getPathwayIdpathway(), x.getModelPathway().getName());
				}
			}

			List<ModelStoichiometry> res4 = this.stoichDAO.getAllModelStoichiometryByReactionId(reactionId);

			if (res4 != null) {
				for (ModelStoichiometry x : res4){

					if(x.getStoichiometricCoefficient() > 0)
						container.addProduct(x.getModelCompound().getIdcompound(), x.getStoichiometricCoefficient(), x.getModelCompartment().getIdcompartment());
					else
						container.addReactant(x.getModelCompound().getIdcompound(), x.getStoichiometricCoefficient(), x.getModelCompartment().getIdcompartment());
				}
			}
			return container;
		}
		return null;
	}

	@Override
	public Integer insertNewReaction(ReactionContainer container) throws Exception {

		Integer localisation = null;

		CompartmentContainer comp = container.getLocalisation();
			
		if(comp != null)
			localisation = comp.getCompartmentID();

		return this.insertNewReaction(container.isInModel(), container.getLowerBound(), container.getUpperBound(), container.getGeneRule(),
				container.getEquation(), container.isGeneric(), container.isNon_enzymatic(), container.isSpontaneous(), 
				container.getExternalIdentifier(), container.getSource().toString(), localisation, container.getNotes());
	}

	@Override
	public Integer insertNewReaction(boolean inModel, double lowerBound, double upperBound, String boolean_rule,
			String equation, boolean isGeneric, boolean isNonEnzymatic, boolean isSpontaneous, String name,
			String source, Integer compartmentId, String notes) throws Exception {

		ModelReaction reaction = new ModelReaction();

		ModelReactionLabels reactionLabel = this.getReactionLabelByName(name);

		ModelCompartment compartment = null;

		if(compartmentId != null)
			compartment = compartmentDAO.getModelCompartment(compartmentId);

		if(compartmentId != null && compartment == null) {
			throw new Exception("compartment key " + compartmentId + " does not exists in table model_compartment, please insert a valid id!");
		}
		else {
			reaction.setModelCompartment(compartment);
		}

		if(reactionLabel == null)
			reactionLabel = this.insertNewModelReactionLabel(equation, isGeneric, isNonEnzymatic, isSpontaneous, name, source);
		
		if(reactionLabel.getEquation().isEmpty()) {
			reactionLabel.setEquation(equation);
			modelreactionLabelsDAO.save(reactionLabel);
		}

		reaction.setNotes(notes);
		reaction.setBooleanRule(boolean_rule);
		reaction.setInModel(inModel);
		reaction.setLowerBound((float) lowerBound);
		reaction.setUpperBound((float) upperBound);
		reaction.setModelReactionLabels(reactionLabel);

		Integer res = (Integer) modelreactionDAO.save(reaction);

		return res;

	}

	public List<String[]> getStoichiometryInfo(boolean isCompartmentalizedModel) throws Exception {
		return this.modelreactionDAO.getModelReactionByInModelAndConditions(isCompartmentalizedModel);
	}

//	public List<String[]> getReactionHasEnzymeData(Integer id) throws Exception {
//		ArrayList<String[]> result = new ArrayList<String[]>();
//
//		List<String[]> res = this.modelreactionhasproteinDao.getAllModelReactionHasModelProteinByreactionId2(id);
//
//		if (res != null)
//			for (String[] x : res) {
//				String[] list = new String[3];
//
//				list[0]= x[0];
//				list[1]= x[1];
//				list[2]= x[2] +"";
//
//				result.add(list);
//			}
//		return result;
//	}
	
	@Override
	public Integer countTotalOfReactions(boolean isCompartmentalized) throws Exception {
		return this.modelreactionDAO.countTotalOfReactions(isCompartmentalized);
	}


	public List<String[]> countReactions(Integer id, boolean isCompartmentalized) throws Exception {
		return this.pathHasReactionDAO.getPathwayHasReactionAttributes(id, isCompartmentalized);
	}


	public ReactionContainer getDataForReactionContainerByReactionId(Integer rowID, boolean isCompartmentalized) throws Exception {
		return this.modelreactionDAO.getModelReactionAttributesByReactionId(rowID, isCompartmentalized); 
	}

	public List<ReactionContainer> getDataForReactionContainer(boolean isCompartmentalized) throws Exception {
		return this.modelreactionDAO.getAllModelReactionAttributes(isCompartmentalized); 
	}

	public Map<Integer, ReactionContainer> getAllDataForReactionContainerByReactionId(boolean isCompartmentalized) throws Exception {
		return this.modelreactionDAO.getAllModelReactionAttributesByReactionId(isCompartmentalized); 
	}

	public void updateReactionProperties(int reactionID, int columnNumber, Object object, Integer notesColumn,
			Integer isReversibleColumn, Integer inModelColun) throws Exception {


		if(columnNumber == notesColumn) {

			this.modelreactionDAO.updateModelReactionNotesByReactionId(reactionID, (String) object);

		}
		else {

			boolean value = (Boolean) object;

			Pair<Boolean, Boolean> pair = this.checkReactionIsReversibleAndInModel(reactionID);

			if((columnNumber == isReversibleColumn && value!=pair.getA()) || //(columnNumber == IS_GENERIC_COLUMN && value!=rs.getBoolean(2)) ||
					(columnNumber == inModelColun && value!=pair.getB())) {

				String equation="", source="", lowerBound = "0";

				Pair<String, String> res = this.getEquationAndSourceFromReaction(reactionID);

				equation = res.getA();
				source = res.getB();

				if(columnNumber==isReversibleColumn) {

					if(value) {

						equation=equation.replace(" => ", " <=> ").replace(" <= ", " <=> ");
						lowerBound = "-999999";
					}
					else {

						equation=equation.replace("<=>", "=>");
					}

					this.modelreactionDAO.updateReactionReversibility(Long.valueOf(lowerBound), reactionID);

					int reactionLabelId = this.modelreactionDAO.getModelReactionLabelByReactionId(reactionID);

					this.modelreactionLabelsDAO.updateAllModelReactionLabels(null, equation, null, null, null, reactionLabelId);

				}
				else {

					if(source.equalsIgnoreCase("KEGG"))
						source = "MANUAL";

					this.modelreactionDAO.updateInModelByReactionId(reactionID, value);

					int reactionLabelID = this.modelreactionDAO.getModelReactionLabelByReactionId(reactionID);
					this.modelreactionLabelsDAO.updateSourceByReactionLabelId(reactionLabelID, source);


				}
			}
		}
	}


	public List<Integer> getReactionsRelatedToLabelName(String name) throws Exception {
		return this.modelreactionDAO.getModelReactionIdByName(name);
	}

	public Set<String> getDataFromReactionsViewNoPathOrNoEc(Set<String> reactionsIDs, String aux, String ecnumber, Statement stmt) throws SQLException{

		ResultSet rs= stmt.executeQuery("SELECT DISTINCT idreaction FROM reactions_view_noPath_or_noEC " +
				"INNER JOIN reaction_has_enzyme ON reaction_has_enzyme.reaction_idreaction=idreaction " +
				"WHERE enzyme_ecnumber = '"+ecnumber+"'"+aux);

		while(rs.next())
			reactionsIDs.add(rs.getString(1));

		rs.close();
		return reactionsIDs;
	}

	public void updateModelReactionReversibleAndLowerBoundAndEquationByReactionId(Boolean reversible, Long lower, Integer reactionId, String equation) throws Exception {

		this.modelreactionDAO.updateReactionReversibility(lower, reactionId);
		int reactionLabelId = this.modelreactionDAO.getModelReactionLabelByReactionId(reactionId);
		this.modelreactionLabelsDAO.updateEquationByReactionLabelId(reactionLabelId, equation);

	}

	public void updateModelReactionInModelByReactionId(Integer reactionId, Boolean inModel) throws Exception{
		ModelReaction modelReaction = this.modelreactionDAO.findById(reactionId);
	
		if(modelReaction != null) {
			modelReaction.setInModel(inModel);
			this.modelreactionDAO.update(modelReaction);
		}
		
	}


	@Override
	public void updateReaction(String name, String equation, Boolean reversible, Integer compartmentId,
			Boolean isSpontaneous, Boolean isNonEnzymatic, Boolean isGeneric, String booleanRule, Long lower,
			Long upper, Integer reactionId, Boolean inModel) throws Exception {

		int reactionLabelId = this.modelreactionDAO.getModelReactionLabelByReactionId(reactionId);

		ModelCompartment modelCompartment = null;
		
		if(compartmentId != null)
			modelCompartment = this.compartmentDAO.findById(compartmentId);
		ModelReaction modelReaction = this.modelreactionDAO.findById(reactionId);
		ModelReactionLabels reactionLabel = this.modelreactionLabelsDAO.findById(reactionLabelId);

		if(reactionLabel !=null) {
			reactionLabel.setName(name);
			reactionLabel.setEquation(equation);
			reactionLabel.setIsSpontaneous(isSpontaneous);
			reactionLabel.setIsNonEnzymatic(isNonEnzymatic);
			reactionLabel.setIsGeneric(isGeneric);

			this.modelreactionLabelsDAO.update(reactionLabel);
		}

		if(modelReaction !=null) {
			modelReaction.setModelCompartment(modelCompartment);
			modelReaction.setBooleanRule(booleanRule);
			modelReaction.setLowerBound((float) lower);
			modelReaction.setUpperBound((float) upper);
			modelReaction.setInModel(inModel);

			this.modelreactionDAO.update(modelReaction);
		}
	}


	@Override
	public boolean deleteModelReactionHasModelProteinByReactionId(Integer reactionId, Integer protId)
			throws Exception {
		return this.modelreactionhasproteinDao.deleteModelReactionHasModelProteinByReactionId(reactionId, protId);
	}

	@Override
	public Map<Integer, ReactionContainer> getAllModelReactionAttributesByReactionId(boolean isCompartimentalised)
			throws Exception {
		return this.modelreactionDAO.getAllModelReactionAttributesByReactionId(isCompartimentalised);
	}


	@Override
	public void insertModelReactionHasModelProtein(Integer idReaction, Integer idprotein) {
		this.modelreactionhasproteinDao.insertModelReactionHasModelProtein(idReaction, idprotein);

	}


	@Override
	public void updateModelReactionNotesByReactionId(Integer reactionId, String notes) throws Exception {
		this.modelreactionDAO.updateModelReactionNotesByReactionId(reactionId, notes);

	}


	@Override
	public void updateInModelAndSourceByReactionId(Integer idreaction, boolean inModel, String source)
			throws Exception {

		this.modelreactionDAO.updateInModelByReactionId(idreaction, inModel);

		int reactionLabelID = this.modelreactionDAO.getModelReactionLabelByReactionId(idreaction);
		this.modelreactionLabelsDAO.updateSourceByReactionLabelId(reactionLabelID, source);

	}

	@Override
	public void removeReactionsFromModelByBooleanRule(List<String> names, boolean keepReactionsWithNotes, boolean keepManualReactions)
			throws Exception {

		for (String name : names) {

			ModelReactionLabels label = this.getReactionLabelByName(name);

			Set<ModelReaction> reactions = label.getModelReactions();

			for(ModelReaction reaction : reactions) {
				
				boolean toKeep = false;

				String old_note = reaction.getNotes();

				if(old_note!=null && !old_note.isEmpty()) {

					if(keepReactionsWithNotes) {

						toKeep = true;
					}
					else {

						old_note = old_note.replaceAll(" \\| Removed by GPR rule","");
						old_note = old_note.replaceAll("Removed by GPR rule","");

						if(old_note.contains("new Annotation. automatic GPR")) {

							String[] data = old_note.split(" \\| ");

							if(data.length>2)
								old_note+=" | "+data[2];
						}

						if(old_note.contains("automatic GPR")) {

							String[] data = old_note.split(" \\| ");

							if(data.length>2)
								old_note+=" | "+data[2];
						}
					}
				}
				if(label.getIsSpontaneous() || label.getIsNonEnzymatic())
					toKeep = true;

				if(keepManualReactions && label.getSource().equalsIgnoreCase(SourceType.MANUAL.toString()))
					toKeep = true;
				
				
				if(!toKeep) {
					old_note += "Removed by GPR";
					reaction.setInModel(false);
					reaction.setNotes(old_note);
					
					this.modelreactionDAO.update(reaction);
				}
			}
		}
	}

	@Override
	public void updateBooleanRuleAndNotes(List<String> names, Map<String, String> rules, String message)
			throws Exception {

		for(String name : names) {

			ModelReactionLabels label = this.getReactionLabelByName(name);

			Set<ModelReaction> reactions = label.getModelReactions();

			for(ModelReaction reaction : reactions) {

				String note = reaction.getNotes();

				if(note != null && !note.contains(message)) {

					if(!note.isEmpty())
						note = note.concat(" | ");

					note = note.concat(message);

					reaction.setNotes(note);
				}
				
				else {
					
					note = message;
					reaction.setNotes(note);
				}
					

				reaction.setBooleanRule(rules.get(name));

				this.modelreactionDAO.update(reaction);
			}
		}
	}


	@Override
	public List<Pair<Integer, String>> getAllModelReactionHasModelProteinByReactionId(Integer idreaction)throws Exception {

		List<Pair<Integer, String>> res = new ArrayList<>();

		List<ModelReactionHasModelProtein> result = modelreactionhasproteinDao.getAllModelReactionHasModelProteinByReactionId(idreaction);

		if(result != null) {

			for(ModelReactionHasModelProtein reactHasEnz : result)
				res.add(new Pair<Integer, String>(reactHasEnz.getId().getModelProteinIdprotein(), reactHasEnz.getModelProtein().getEcnumber()));
		}

		return res;
	}

	@Override
	public List<ReactionContainer> getReactionDataForStats(boolean isCompartimentalized)throws Exception {

		return this.modelreactionDAO.getAllModelReactionWithLabelsByCompartmentalized(isCompartimentalized);

	}

	@Override
	public Map<String, List<Integer>> getModelReactionIdsRelatedToNames()throws Exception {

		return this.modelreactionDAO.getModelReactionIdsRelatedToNames();

	}
	
	@Override
	public List<ReactionContainer> getModelReactionIdsRelatedToName(String name)throws Exception {

		return this.modelreactionDAO.getModelReactionIdsRelatedToName(name);

	}

	@Override
	public List<Integer> getAllModelReactionsIDsByAttributes(String equation, boolean inModel, boolean isGeneric, boolean isSpontaneous,
			boolean isNonEnzymatic, String source, boolean isCompartimentalized) throws Exception {

		return this.modelreactionDAO.getAllModelReactionsIDsByAttributes(equation, inModel, isGeneric, isSpontaneous, isNonEnzymatic, source, true);

	}

	@Override
	public List<String> getRelatedReactions(String name) throws Exception {

		return this.modelreactionDAO.getModelReactionNamesByCompound(name);
	}

	public List<Integer> getModelReactionLabelIdByName(String name, boolean isCompartimentalized) throws Exception {

		return this.modelreactionLabelsDAO.getModelReactionsIdByName(name, isCompartimentalized);

	}
	
	public List<String[]> getDataFromReactionForBlockedReactionsTool(boolean isCompartimentalized) throws Exception {

		return this.modelreactionDAO.getDataFromReactionForBlockedReactionsTool(isCompartimentalized);

	}
	
	public List<Object[]> getAllModelReactionsByTransportersAndIsCompartimentalized(boolean isTransporters) throws Exception {

		return this.modelreactionDAO.getAllModelReactionsByTransportersAndIsCompartimentalized(isTransporters);

	}
	
	@Override
	public Map<Integer, ReactionContainer> getAllModelReactionAttributesbySource(boolean isCompartimentalized, String source) {
		return this.modelreactionDAO.getAllModelReactionAttributesbySource(isCompartimentalized, source);

	}

	@Override
	public Map<String, List<Integer>> getEcNumbersByReactionId() {
		return this.modelreactionhasproteinDao.getEcNumbersByReactionId();

	}
	
	@Override
	public List<ReactionContainer> getDistinctReactionByProteinIdAndCompartimentalized(Integer proteinID, boolean isCompartimentalized) {
		return this.modelreactionDAO.getDistinctReactionByProteinIdAndCompartimentalized(proteinID, isCompartimentalized);
				

	}
	
	@Override
	public List<ReactionContainer> getReactionIdFromProteinIdWithPathwayIdNull(Integer proteinId) {
		return this.modelreactionDAO.getReactionIdFromProteinIdWithPathwayIdNull(proteinId);

		
	}
	
	@Override
	public Integer getIdReactionLabelFromReactionId(Integer reactionId) {
		return this.modelreactionLabelsDAO.getIdReactionLabelFromReactionId(reactionId);
	}

	@Override
	public void removeReactionsBySource(String source) throws Exception {
		
		List<ModelReactionLabels> labels = this.modelreactionLabelsDAO.findBySingleAttribute("source", source);
		
		for(ModelReactionLabels reactionLabel : labels) {
			
			Set<ModelReaction> reactions = reactionLabel.getModelReactions();
			
			for(ModelReaction reaction : reactions)
				this.modelreactionDAO.removeModelReaction(reaction);
			
			this.modelreactionLabelsDAO.removeModelReactionLabels(reactionLabel);
		}
		
		
	}

	@Override
	public void updateSourceByReactionLabelId(Integer reactionLabelId, String source) {
		this.modelreactionLabelsDAO.updateSourceByReactionLabelId(reactionLabelId, source);
		
	}

	@Override
	public List<String[]> getReactionIdAndEcNumberAndProteinId() throws Exception {

		return this.modelreactionDAO.getReactionIdAndEcNumberAndProteinId();
	}
	
	@Override
	public List<Integer[]> getReactionIdAndPathwayId() throws Exception {

		return this.modelreactionDAO.getReactionIdAndPathwayId();
	}
	
	@Override
	public List<String[]> getReacIdEcNumProtIdWhereSourceEqualTransporters() throws Exception {

		return this.modelreactionDAO.getReacIdEcNumProtIdWhereSourceEqualTransporters();
	}
	
	@Override
	public List<Integer> getDistinctReactionIdWhereSourceTransporters(Boolean isTransporter) throws Exception {

		return this.modelreactionDAO.getDistinctReactionIdWhereSourceTransporters(isTransporter);
	}
	
	
	
	@Override
	public boolean checkReactionsBySource(String source) throws Exception {
		
		List<ModelReactionLabels> labels = this.modelreactionLabelsDAO.findBySingleAttribute("source", source);
		
		if(labels != null)
			return labels.size() > 0;
		
		return false;
	}
	
	@Override
	public List<Object[]> getCompoundIDAndStoichiometricCoefficientAndCompartmentIDByReactionID(Integer reactionID){
		return this.modelreactionDAO.GetCompoundIDAndStoichiometricCoefficientAndCompartmentIDByReactionID(reactionID);
	}
	
	@Override
	public Set<String> getAllReactionsNames(){
		
		Set<String> names = new HashSet<>();
		
		List<ModelReactionLabels> labels = this.modelreactionLabelsDAO.getAllModelReactionLabels();
		
		if(labels != null)
			for(ModelReactionLabels label : labels)
				names.add(label.getName());
		
		return names;
	}
	
	@Override
	public void removeNotOriginalReactions(Boolean isTransporter){
		this.modelreactionDAO.removeNotOriginalReactions(isTransporter);
	}

	@Override
	public List<String[]> getReactionGenes() throws Exception {

		return this.modelreactionDAO.getReactionGenes();
	}
	
	@Override
	public Set<Integer> getModelDrains() throws Exception {

		return this.modelreactionDAO.getModelDrains();
	}
	
	@Override
	public boolean checkIfReactionsHasData() throws Exception {

		return this.modelreactionDAO.countAll() > 0;
	}
	
	@Override
	public void setAllReactionsInModel(boolean setInModel, boolean keepSpontaneousReactions) throws Exception{
		
		if(setInModel){
			
			List<ModelReaction> reactions = this.modelreactionDAO.getAllModelReaction();
			
			for(ModelReaction reaction : reactions) {
				reaction.setInModel(true);
				this.modelreactionDAO.update(reaction);
			}
		}
		else if(keepSpontaneousReactions){
			
			List<ModelReaction> reactions = this.modelreactionDAO.findBySingleAttribute("modelReactionLabels.isSpontaneous", false);
			
			for(ModelReaction reaction : reactions) {
				reaction.setInModel(false);
				reaction.setBooleanRule(null);
				this.modelreactionDAO.update(reaction);
			}
		}
		else{
			List<ModelReaction> reactions = this.modelreactionDAO.getAllModelReaction();
			
			for(ModelReaction reaction : reactions) {
				reaction.setInModel(false);
				reaction.setBooleanRule(null);
				this.modelreactionDAO.update(reaction);
			}
		}
	}
	
	@Override
	public void updateModelReactionSetInModelAndNotesAndBooleanRuleByReactionName(boolean inModel, String notes,
			String booleanRule, String name) throws Exception{
		
		List<ModelReactionLabels> reactionLabels = this.modelreactionLabelsDAO.findBySingleAttribute("name", name);	
		
		for(ModelReactionLabels label : reactionLabels) {
			
			for(ModelReaction reaction : label.getModelReactions()) {
				
				reaction.setInModel(inModel);
				reaction.setNotes(notes);
				reaction.setBooleanRule(booleanRule);
				
				this.modelreactionDAO.update(reaction);
			}
		}
	}
	
	@Override
	public List<String[]> getDataForGPRAssignment() throws Exception{
		
		return this.modelreactionDAO.getDataForGPRAssignment();
				
	}
	
	@Override
	public List<Integer> getProteinsByReactionId(Integer reactionId) throws Exception{
		
		List<Integer> proteins = new ArrayList<>();
		
		ModelReaction reaction = this.modelreactionDAO.getModelReaction(reactionId);
		
		if(reaction != null) {
			
			for(ModelReactionHasModelProtein reactHasProt : reaction.getModelReactionHasModelProteins())
				proteins.add(reactHasProt.getId().getModelProteinIdprotein());
			
		}
		return proteins;
	}
	
	@Override
	public CompartmentContainer getReactionCompartment(Integer reactionId) throws Exception{
		
		ModelReaction reaction = this.modelreactionDAO.getModelReaction(reactionId);
		
		if(reaction == null || reaction.getModelCompartment() == null) 
			return null;
		
		ModelCompartment modelCompartment = reaction.getModelCompartment();
	
		return new CompartmentContainer(modelCompartment.getIdcompartment(), modelCompartment.getName(), modelCompartment.getAbbreviation());
		
	}
	
	@Override
	public List<ReactionContainer> getGenesReactionsBySubunit() throws Exception{
		
		List<ModelReaction> modelReactions = this.modelreactionDAO.getAllModelReaction();
		
		List<ReactionContainer> containers = new ArrayList<>();
		
		for(ModelReaction modelReaction : modelReactions) {
			
			Set<Pair<String, String>> genes = new HashSet<>();
			
			ReactionContainer container = buildContainer(modelReaction);
		
			Set<Integer> genesIds = Utilities.parseRuleList(modelReaction.getBooleanRule());
			
			for(Integer geneId : genesIds) {
				ModelGene modelGene = this.modelGeneDAO.getModelGene(geneId);
			
				genes.add(new Pair<String, String>(modelGene.getQuery(), modelGene.getLocusTag()));
				
			}
			
			container.setGenes(genes);
			
			containers.add(container);
		}
		
		return containers;
	}
	
	@Override
	public void replaceReactionCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace) throws Exception{
		
		this.modelreactionDAO.replaceCompartment(compartmentIdToKeep, compartmentIdToReplace);
	}
}
