package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelCompartmentDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelCompoundDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelStoichiometryDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStoichiometry;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IStoichiometryService;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class StoichiometryServiceImpl implements IStoichiometryService{


	private IModelStoichiometryDAO modelStoichiometryDAO;
	private IModelCompartmentDAO modelCompartmentDAO;
	private IModelReactionDAO modelReactionDAO;
	private IModelCompoundDAO modelCompoundDAO;

	public StoichiometryServiceImpl(IModelStoichiometryDAO modelstoichiometryDAO, IModelCompartmentDAO modelCompartmentDAO,
			IModelReactionDAO modelReactionDAO, IModelCompoundDAO modelCompoundDAO) { 
		this.modelStoichiometryDAO = modelstoichiometryDAO;
		this.modelCompartmentDAO = modelCompartmentDAO;
		this.modelReactionDAO = modelReactionDAO;
		this.modelCompoundDAO = modelCompoundDAO;
	}


	public List<Integer> getReversibilities(Boolean inModel, Boolean isCompartimentalized) throws Exception {
		return this.modelStoichiometryDAO.getDistinctReversibleCompoundIds(inModel,isCompartimentalized);

	}

	public List<String[]> getStoichiometryData(int reactionID) throws Exception {
		return this.modelStoichiometryDAO.getModelStoichiometryAttributesByReactionId(reactionID);
	}

	public Integer getStoichiometryID(int idReaction, int m, int idCompartment, double coefficient) throws Exception {
		return this.modelStoichiometryDAO.getIdStoichiometry(idReaction, m, idCompartment, coefficient);
	}

	public List<Integer> getMetabolitesInModel() throws Exception {
		List<ModelStoichiometry> list = this.modelStoichiometryDAO.getDistinctModelStoichiometryCompoundId();

		List<Integer> res = new ArrayList<Integer>();
		if (list != null && list.size() > 0) {
			for (ModelStoichiometry x : list) {
				res.add(x.getModelCompound().getIdcompound());
			}
		}
		return res;

	}

	public List<String[]> getCompoundData(int id) throws Exception {
		return this.modelStoichiometryDAO.getAllModelStoichiometryAttributesByReactionId(id);
	}

	public List<String[]> getCompoundIDsFromStoichiometry() throws Exception {
		return this.modelStoichiometryDAO.getModelStoichiometryCompoundIdAndStoichiometryCoef();
	}

	public Map<String, List<String>> getStoichiometryData2(int reactionId) throws Exception {
		List<String[]> list = this.modelStoichiometryDAO.getModelStoichiometryDifferentAttributesByReactionId(reactionId);

		Map<String, List<String>> result = new HashMap<String, List<String>>();


		if (list != null) {
			for (String[] x : list) {
				List<String> toInsert = new ArrayList<String>();
				for(int i = 0; i < x.length-1; i++){
					for (String y : x) {
						toInsert.add(y);
					}
					result.put(x[x.length-1],toInsert);
				}
			}
		}
		return result;
	}


	public List<Set<String>> getCompoundsReactions(boolean isCompartimentalizedModel) throws Exception {
		List<Set<String>> result = new ArrayList<Set<String>>();
		Set<String> reactants = new HashSet<String>();
		Set<String> products = new HashSet<String>();
		Set<String> reactionsReactants = new HashSet<String>();
		Set<String> productsReactants = new HashSet<String>();

		List<Object[]> res = this.modelStoichiometryDAO.getAllModelStoichiometryAttributes2(isCompartimentalizedModel);


		if(res != null) {

			for(Object[] item: res) {


				if(item[1].toString().startsWith("-")){

					reactants.add(item[0].toString());
					reactionsReactants.add(item[2].toString());
				}
				else {
					products.add(item[0].toString());
					productsReactants.add(item[2].toString());
				}
			}
		}

		result.add(reactants);
		result.add(products);
		result.add(reactionsReactants);
		result.add(productsReactants);

		return result;
	}


	public Map<String, Pair<String, Pair<Double, String>>> getExistingMetabolitesID(int reactionId) throws Exception {
		Map<String,Pair<String,Pair<Double,String>>> existingMetabolitesID = new HashMap<String,Pair<String,Pair<Double,String>>>();

		List<String[]> res = this.modelStoichiometryDAO.getAllModelStoichiometryByReactionId2(reactionId);

		if (res != null)
			for (String[] x : res) {

				Double coefficient = Double.parseDouble(x[2]);

				Pair<Double, String> data = new Pair<Double, String>(coefficient, x[3]);

				Pair<String, Pair<Double, String>> dataPair = new Pair<String, Pair<Double, String>>(x[1], data);

				String key = x[0];

				if(coefficient<0)
					key = "-"+key;


				existingMetabolitesID.put(key, dataPair);
			}

		return existingMetabolitesID;
	}


	public Pair<Double, Double> getReactantsAndProducts(boolean isCompartmentalized) throws Exception {

		double nreactants = 0.0;
		double nproducts = 0.0;

		List<String[]> list = this.modelStoichiometryDAO.getModelStoichiometryAttributes(isCompartmentalized);
		for (String[] x : list) {

			if(x[0].startsWith("-"))
				nreactants += Double.valueOf(x[1]); 
			else
				nproducts += Double.valueOf(x[1]);
		}

		Pair<Double, Double> res = new Pair<Double, Double>(nreactants,nproducts);

		return res;
	}

	@Override
	public boolean deleteModelStoichiometryByStoichiometryId(Integer stoichId) throws Exception {
		return this.modelStoichiometryDAO.deleteModelStoichiometryByStoichiometryId(stoichId);
	}


	@Override
	public void insertModelStoichiometry(Integer reactionId, Integer compoundId, Integer compartmentId,
			double stoichiometric_coef) throws Exception {

		ModelReaction reaction = this.modelReactionDAO.getModelReaction(reactionId);

		ModelCompound compound = this.modelCompoundDAO.getModelCompound(compoundId);

		ModelCompartment compartment = this.modelCompartmentDAO.getModelCompartment(compartmentId);

		ModelStoichiometry model = new ModelStoichiometry();
		model.setStoichiometricCoefficient((float)stoichiometric_coef);
		model.setModelCompartment(compartment);
		model.setModelCompound(compound);
		model.setModelReaction(reaction);

		this.modelStoichiometryDAO.addModelStoichiometry(model);

	}

	@Override
	public void updateModelStoichiometryByStoichiometryId(Integer id, double coeff, Integer compartmentId,
			Integer compoundId) throws Exception {
		this.modelStoichiometryDAO.updateModelStoichiometryByStoichiometryId(id, (float) coeff, compartmentId, compoundId);

	}
	
	@Override
	public void updateModelStoichiometryByReactionIdAndCompoundId(double coeff, Integer reactionId, Integer compoundId) throws Exception {
		
		Map<String, Serializable> eqRestrictions = new HashMap<>();
		eqRestrictions.put("modelReaction.idreaction", reactionId);
		eqRestrictions.put("modelCompound.idcompound", compoundId);
		
		List<ModelStoichiometry> list = this.modelStoichiometryDAO.findByAttributes(eqRestrictions);
		
		for(ModelStoichiometry stoichiometry : list) {
			stoichiometry.setStoichiometricCoefficient((float) coeff);
			
			this.modelStoichiometryDAO.update(stoichiometry);
		}
	}


	@Override
	public Map<Integer, MetaboliteContainer> getModelStoichiometryByReactionId(Integer reactionID) throws Exception {
		return modelStoichiometryDAO.getModelStoichiometryByReactionId(reactionID);
	}


	@Override
	public List<Integer[]> getStoichiometryDataFromTransportersSource() throws Exception {
		
		return modelStoichiometryDAO.getStoichiometryDataFromTransportersSource();
	}

	@Override
	public Map<Integer, ReactionContainer> getAllOriginalTransportersFromStoichiometry() { 
		
		return this.modelStoichiometryDAO.getAllOriginalTransportersFromStoichiometry();
		
	}


	@Override
	public List<String[]> getCompoundDataFromStoichiometry(Integer idreaction) throws Exception {
		
		return this.modelStoichiometryDAO.getCompoundDataFromStoichiometry(idreaction);
	}
	
	@Override
	public Set<String> checkUndefinedStoichiometry(boolean isCompartimentalized) throws Exception {

		return this.modelStoichiometryDAO.checkUndefinedStoichiometry(isCompartimentalized);
	}
	
	
	@Override
	public void replaceStoichiometryCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace) throws Exception{
		
		this.modelStoichiometryDAO.replaceCompartment(compartmentIdToKeep, compartmentIdToReplace);
	}
}
