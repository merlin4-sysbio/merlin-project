package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;

public interface IPathwayService {
	
	
	public Map<String, Set<String>> getAllPathways() throws Exception;
	
	public List<String> getAllPathwaysNamesOrdered() throws Exception;

	public Map<String, Set<String>> getEnzymesPathways() throws Exception;
	
	public boolean addPathway_has_Reaction(Integer idPathway, Integer idReaction) throws Exception;
	
	public Integer addBiomassPathway(String name, String code) throws Exception;
	
	public List<String> getReactionPathway () throws Exception;
	
	public String getPathwaysByRowID(int id) throws Exception;
	
	public List<Integer> getReactionsID(int pathway, int idProtein) throws Exception;
	
	public List<String[]> getPathwayHasReactionData() throws Exception;
	
	public Boolean checkIfModelPathwayNameExists(String name) throws Exception;
	
	public Map<Integer, PathwayContainer> getPathways(Integer id) throws Exception;
	
	public boolean hasDrains() throws Exception;
	
	public boolean isTransporterLoaded() throws Exception;
	
	public List<PathwayContainer> getAllFromPathwaySortedByName() throws Exception;
	
	public Map<Integer, String> getPathwaysNames() throws Exception;
	
	public Map<Integer,String> getExistingPathwaysID(int idReaction) throws Exception;
	
	public Map<Integer,String> getExistingPathwaysID2(int idReaction) throws Exception;
	
	public Integer getPathwayID(String name) throws Exception;
	
	public List<PathwayContainer> getPathwaysIDsByReactionID(Integer idReaction) throws Exception;
	
	public Map<Integer, ReactionContainer> getReactionIdAndPathwayID(String source, boolean original) throws Exception;
	
	public List<Integer> getReactionIdByPathwayID(int id) throws Exception;
	
	public boolean checkPathwayHasEnzymeEntryByReactionID(Integer id) throws Exception;
	
	public int getPathwayIdByNameAndCode(String name, String code) throws Exception;
	
	public boolean checkPathwayHasReactionData(Integer idreaction, Integer pathwayID) throws Exception;
	
	public boolean checkPathwayHasEnzymeData(Integer proteinID, Integer pathwayID) throws Exception;
	
	public Map<Integer,String[]> countReactionsByPathwayID(Map<Integer,String[]> qls, boolean isCompartimentalized) throws Exception;

	public String getPathwayCodeByName(String name) throws Exception;
	
	public Map<Integer, String> getPathwayID3(String name) throws Exception ;
	
	public Integer insertModelPathwayCodeAndName(String code, String name) throws Exception;
	
	public void insertModelPathwayIdAndSuperPathway(Integer id, Integer superID) throws Exception;
	
	public boolean deleteModelPathwayHasModelProteinByIdProtein(Integer proteinId) throws Exception;
	
	public boolean deleteModelPathwayHasModelProteinByPathwayId(Integer pathId) throws Exception;
	
	public boolean deleteModelPathwayHasReactionByPathwayId(Integer id) throws Exception;
	
	public boolean deleteModelPathwayHasReactionByReactionId(Integer id) throws Exception;
	
	public boolean deleteModelPathwayHasReactionByReactionIdAndPathwayId(Integer reactionId, Integer pathId)  throws Exception;
	
	public void insertModelPathwayHasReaction(Integer idReaction, Integer idPathway)  throws Exception;
	
	public void insertModelPathwayHasModelProtein(Integer pathId, Integer proteinId)  throws Exception;
	
	public List<Integer> getModelPathwayHasEnzymeReactionIdByEcNumberAndPathwayIdAndProteinId(
			Integer pathwayId, Integer proteinId) throws Exception;
	
	public List<Integer> getModelPathwayHasEnzymeReactionIdByEcNumberAndReactionIdAndProteinId(Integer reactId, Integer proteinId) throws Exception;

	public List<String> getAllPathwaysOrderedByNameInModelWithReaction(Boolean inModel) throws Exception;

	public List<String[]> getUpdatedPathways(boolean isCompartimentalized, boolean encodedOnly) throws Exception;

	public Map<Integer, String[]> countProteinIdByPathwayID(Map<Integer,String[]> qls) throws Exception;

	public List<String[]> getPathwayHasEnzymeData(int pathId, boolean isCompartimentalized) throws Exception;

	public List<String> getPathwaysList() throws Exception;

	public List<Integer> getPathwayID(int idReaction, int idProtein) throws Exception;


	public boolean checkPathwayHasEnzymeEntryByProteinId(Integer idprotein) throws Exception;

	public List<Integer> getProteinIdByPathwayID(int id) throws Exception;

	public Map<String, Integer> getPathwayCodeAndIdpathway() throws Exception;

	public void insertModelPathwayHasCompound(Integer compoundId, Integer idPathway) throws Exception;

	public void insertModelPathwayHasModuleEntry(Integer pathId, Integer moduleId) throws Exception;

	public boolean checkModelPathwayHasModuleEntry(Integer pathId, Integer moduleId) throws Exception;

	public boolean checkSuperPathwayData(Integer id, Integer superID) throws Exception;

	public boolean checkPathwayHasCompoundData(Integer pathwayId, Integer compoundID) throws Exception;

	public Long countPathwayHasReaction(boolean isCompartimentalized) throws Exception;

	public Map<Integer, List<PathwayContainer>> getPathwaysByReaction() throws Exception;
}
