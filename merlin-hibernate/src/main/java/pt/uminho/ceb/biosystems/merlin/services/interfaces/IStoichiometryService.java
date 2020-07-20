package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public interface IStoichiometryService {

	
	public List<Integer> getReversibilities(Boolean inModel, Boolean isCompartimentalized) throws Exception;
	
	public List<String[]> getStoichiometryData(int reactionID) throws Exception;
	
	public  Map<String, Pair<String, Pair<Double, String>>> getExistingMetabolitesID(int idReaction) throws Exception;
	
	public Integer getStoichiometryID(int idReaction, int m, int idCompartment, double coefficient) throws Exception;
	
	public List<Integer> getMetabolitesInModel() throws Exception;
	
	public List<String[]>  getCompoundData(int id) throws Exception;
	
	public List<String[]> getCompoundIDsFromStoichiometry() throws Exception;
	
	public Map<String, List<String>> getStoichiometryData2(int reactionId) throws Exception;
	
	public List<Set<String>> getCompoundsReactions(boolean isCompartimentalizedModel) throws Exception;
	
	public Pair<Double, Double> getReactantsAndProducts(boolean isCompartimentalied) throws Exception;
	
	public boolean deleteModelStoichiometryByStoichiometryId(Integer stoichId) throws Exception;
	
	public void insertModelStoichiometry(Integer reactionId, Integer compoundId, Integer compartmentId,
			double stoichiometric_coef) throws Exception;

	public void updateModelStoichiometryByStoichiometryId(Integer id, double coeff, Integer compartmentId, Integer compoundId)
			throws Exception;

	public Map<Integer, MetaboliteContainer> getModelStoichiometryByReactionId(Integer reactionID) throws Exception;

	public List<Integer[]> getStoichiometryDataFromTransportersSource() throws Exception;
	
	public Map<Integer, ReactionContainer> getAllOriginalTransportersFromStoichiometry()  throws Exception;
	
	public List<String[]> getCompoundDataFromStoichiometry(Integer idreaction)  throws Exception;

	public void updateModelStoichiometryByReactionIdAndCompoundId(double coeff, Integer reactionId,
			Integer compoundId) throws Exception;

	public Set<String> checkUndefinedStoichiometry(boolean isCompartimentalized) throws Exception;

	public void replaceStoichiometryCompartment(Integer compartmentIdToKeep, Integer compartmentIdToReplace) throws Exception;

}
