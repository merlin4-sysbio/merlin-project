package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Pathways;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasModelCompoundDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasModuleDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasReactionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSuperpathwayDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathway;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSuperpathway;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IPathwayService;


public class PathwayServiceImpl implements IPathwayService{


	private IModelPathwayDAO modelpathwayDAO;
	private IModelPathwayHasReactionDAO modelpathwayhasreactionDAO;
	private IModelPathwayHasModelProteinDAO modelpathwayhasenzymeDAO;
	private IModelPathwayHasModelCompoundDAO modelPathwayHasModelCompoundDAO;
	private IModelPathwayHasModuleDAO modelPathwayHasModuleDAO;
	private IModelSuperpathwayDAO modelSuperpathwayDAO; 
	
	public PathwayServiceImpl(IModelPathwayDAO modelpathwayDAO, IModelPathwayHasReactionDAO modelpathwayhasreactionDAO, IModelPathwayHasModelProteinDAO modelpathwayhasenzymeDAO,
			IModelPathwayHasModelCompoundDAO modelPathwayHasModelCompoundDAO, IModelPathwayHasModuleDAO modelPathwayHasModuleDAO,
			IModelSuperpathwayDAO modelSuperpathwayDAO) {
		this.modelpathwayDAO  = modelpathwayDAO;
		this.modelpathwayhasreactionDAO = modelpathwayhasreactionDAO;
		this.modelpathwayhasenzymeDAO = modelpathwayhasenzymeDAO;
		this.modelPathwayHasModelCompoundDAO = modelPathwayHasModelCompoundDAO;
		this.modelSuperpathwayDAO = modelSuperpathwayDAO;
	}


	@Override
	public Map<String, Set<String>> getAllPathways() throws Exception {
		return this.modelpathwayDAO.getPathwayIdAndNameAndEcNumber();
	}

	@Override
	public Map<String, Set<String>> getEnzymesPathways() throws Exception {
		return this.modelpathwayDAO.getEnzymesPathwaysEcNumber();
	}

	@Override
	public boolean addPathway_has_Reaction(Integer idPathway, Integer idReaction) throws Exception {
		List<ModelPathwayHasReaction> res = this.modelpathwayhasreactionDAO.getAllModelPathwayHasReactionByAttributes(idReaction, idPathway);
		
		if (res != null) {
			return false;
		}
		else{
			this.modelpathwayhasreactionDAO.insertModelPathwayHasReaction(idReaction, idPathway);
			return true;
		}
	}

	@Override
	public Integer addBiomassPathway(String name, String code) throws Exception {
		boolean res = this.modelpathwayDAO.checkIfModelPathwayNameExists(name);
		
		if (res == false) {
			return this.modelpathwayDAO.insertModelPathwayCodeAndName(code, name);
		}
		return null;
	}

	@Override
	public List<String> getReactionPathway() throws Exception {
		return this.modelpathwayhasreactionDAO.getModelPathwayHasReactionAttributes();
	}

	@Override
	public String getPathwaysByRowID(int id) throws Exception {
	
		return this.modelpathwayDAO.getModelPathwayNameByReactionId(id);
		
	}

	@Override
	public List<String> getPathwaysList() throws Exception {
		List<String> lls = new ArrayList<String>();
		
		List<String[]> list = this.modelpathwayDAO.getModelPathwayIdAndName();
		
		if (list != null) {
			for (String[] x : list) {
				lls.add(x[1]);
			}
		}
		
		return lls;
	}
	

	@Override
	public List<Integer> getReactionsID(int pathwayId, int proteinId) throws Exception {
		return this.modelpathwayhasenzymeDAO.getModelPathwayHasModelProteinReactionIdByPathwayIdAndProteinId(pathwayId, proteinId);
	}

	@Override
	public List<Integer> getPathwayID(int idReaction, int idProtein) throws Exception {
		return this.modelpathwayhasenzymeDAO.getModelPathwayHasModelProteinReactionIdByReactionIdAndProteinId(idReaction, idProtein);
	}

	@Override
	public List<String[]> getPathwayHasReactionData() throws Exception {
		return this.modelpathwayhasreactionDAO.getModelPathwayHasReactionData();
	}

	@Override
	public Boolean checkIfModelPathwayNameExists(String name) throws Exception {
		return this.modelpathwayDAO.checkIfModelPathwayNameExists(name);
	}

	@Override
	public Map<Integer, PathwayContainer> getPathways(Integer proteinId) throws Exception {
		return this.modelpathwayhasenzymeDAO.getDistinctModelPathwayHasModelProteinCodeAndNameByAttributes( proteinId);
	}

	
	@Override
	public boolean hasDrains() throws Exception {
		
		boolean ret = false;

		List<ModelPathway> list = this.modelpathwayDAO.getAllModelPathwayByCode("D0001");
		if (list != null)
			ret = true;

		return ret;
	}

	@Override
	public boolean isTransporterLoaded() throws Exception {
		boolean ret = false;
		
		List<ModelPathway> list = this.modelpathwayDAO.getAllModelPathwayByCode(Pathways.TRANSPORTERS.getCode());
		
		if (list != null && !list.isEmpty())
			ret = true;
		return ret;
		
	}

	@Override
	public List<PathwayContainer> getAllFromPathwaySortedByName() throws Exception {
		return this.modelpathwayDAO.getAllFromPathwaySortedByName();
	}

	@Override
	public Map<Integer, String> getPathwaysNames() throws Exception {
		return this.modelpathwayDAO.getAllModelPathwayIdAndName();
		
	}

	@Override
	public Map<Integer, String> getExistingPathwaysID(int idReaction) throws Exception {
		List<PathwayContainer> list = this.modelpathwayhasreactionDAO.getModelPathwayHasReactionIdByReactionId(idReaction);
		Map<Integer, String> existingPathwaysID = new HashMap<Integer, String>();
		
		for (PathwayContainer x: list)
			existingPathwaysID.put(x.getIdPathway(), x.getName());
		
		return existingPathwaysID;
	}

	@Override
	public Map<Integer, String> getExistingPathwaysID2(int idReaction)
			throws Exception {
		Map <Integer, String> existingPathwaysID  = new HashMap<Integer, String>();
		List<Integer> list = this.modelpathwayDAO.getPathwayIdByReactionId(idReaction);
		
		if(list != null) {
			for (Integer x : list) {
			existingPathwaysID.put(x, "");
	}
		}
		return existingPathwaysID;
	}

	@Override
	public Integer getPathwayID(String name) throws Exception {
		
		return this.modelpathwayDAO.getPathwayIdByName(name);
		
	}

	@Override
	public boolean checkPathwayHasEnzymeData(Integer idProtein, Integer pathId) throws Exception {
		boolean exists = false;
		
		List<ModelPathwayHasModelProtein> list = this.modelpathwayhasenzymeDAO.getAllModelPathwayHasModelProteinByPathwayIdAndProteinId(pathId, idProtein);
		if(list != null && list.size() > 0)
			exists = true;
		return exists;
	}
	
	@Override
	public List<String[]> getPathwayHasEnzymeData(int pathId, boolean isCompartimentalized) throws Exception {
		return this.modelpathwayhasenzymeDAO.getModelPathwayHasModelProteinAttributesByPathwayId(pathId, isCompartimentalized);
	}

	@Override
	public List<PathwayContainer> getPathwaysIDsByReactionID(Integer idReaction) throws Exception {
		return this.modelpathwayhasreactionDAO.getModelPathwayHasReactionIdByReactionId(idReaction);
	}

	@Override
	public Map<Integer, ReactionContainer> getReactionIdAndPathwayID(String source, boolean isCompartimentalized) throws Exception {
		return this.modelpathwayhasreactionDAO.getModelPathwayHasReactionIdAndPathIdByAttributes(source, isCompartimentalized);
	}

	@Override
	public List<Integer> getProteinIdByPathwayID(int id) throws Exception {
		return this.modelpathwayhasenzymeDAO.getAllModelPathwayHasModelProteinIdByPathId(id);
	}

	@Override
	public List<Integer> getReactionIdByPathwayID(int id) throws Exception {
		return this.modelpathwayhasreactionDAO.getAllModelPathwayHasReactionIdReactionByPathId(id);
	}

	@Override
	public boolean checkPathwayHasEnzymeEntryByProteinId(Integer idprotein) throws Exception {
		boolean exists = false;
		
		List<ModelPathwayHasModelProtein> list = this.modelpathwayhasenzymeDAO.getAllModelPathwayHasModelProteinByEcNumber(idprotein);
		
		if(list != null && !list.isEmpty())
			exists = true;
		
		return exists;
	}

	@Override
	public boolean checkPathwayHasEnzymeEntryByReactionID(Integer id) throws Exception {
		boolean exists = false;
		
		List<ModelPathwayHasReaction> list = this.modelpathwayhasreactionDAO.getAllModelPathwayHasReactionByReactionId(id);
		
		if(list != null && !list.isEmpty())
			exists = true;
		
		return exists;
	}

	@Override
	public int getPathwayIdByNameAndCode(String name, String code) throws Exception {
		int res = -1;
		
		List<ModelPathway> result = this.modelpathwayDAO.getAllModelPathwayByNameAndCode(name, code);
		
		if (result != null && !result.isEmpty())
			res = result.get(0).getIdpathway();
		return res;
	}

	@Override
	public boolean checkPathwayHasReactionData(Integer idReaction, Integer pathId) throws Exception {
		boolean exists = false;
		
		Integer result = this.modelpathwayhasreactionDAO.getModelPathwayHasReactionIdByReactionIdAndPathId(idReaction, pathId);
		if (result != null)
			exists = true;
		
		return exists;
	}

	@Override
	public Map<Integer, String[]> countReactionsByPathwayID(Map<Integer, String[]> qls, boolean isCompartimentalized) throws Exception {
		List<Object[]>  dic = this.modelpathwayDAO.getPathwayIdAndReactionId(isCompartimentalized);
		
		if (dic != null && !dic.isEmpty()) {
			for (Object[] x : dic) {
				Integer key = Integer.valueOf(x[0].toString());
				if(qls.containsKey(key)) {
					qls.get(key)[2] = x[1].toString();
				}
			}
		}
		return qls;
				
	}

	@Override
	public String getPathwayCodeByName(String name) throws Exception {
		return this.modelpathwayDAO.getPathwayCodeByName(name);
	}


	@Override
	public Map<Integer, String> getPathwayID3(String name) throws Exception  {
		return this.modelpathwayDAO.getPathwayIdAndNameByName(name);
	}

	@Override
	public Integer insertModelPathwayCodeAndName(String code, String name) throws Exception{
		return this.modelpathwayDAO.insertModelPathwayCodeAndName(code, name);
	}
	
	@Override
	public void insertModelPathwayIdAndSuperPathway(Integer id, Integer superID) throws Exception{
		this.modelSuperpathwayDAO.insertModelPathwayIdAndSuperPathway(id, superID);
	}

	@Override
	public boolean deleteModelPathwayHasModelProteinByPathwayId(Integer pathId) throws Exception {
		return this.modelpathwayhasenzymeDAO.deleteModelPathwayHasModelProteinByPathwayId(pathId);
	}
	
	@Override      
	public boolean deleteModelPathwayHasModelProteinByIdProtein(Integer proteinId) throws Exception {
		return this.modelpathwayhasenzymeDAO.deleteModelPathwayHasModelProteinByIdProtein(proteinId);
	}
	
	@Override
	public boolean deleteModelPathwayHasReactionByPathwayId(Integer id) throws Exception {
		return this.modelpathwayhasreactionDAO.deleteModelPathwayHasReactionByPathwayId(id);
	}
	
	@Override
	public boolean deleteModelPathwayHasReactionByReactionId(Integer id) throws Exception{
		return this.modelpathwayhasreactionDAO.deleteModelPathwayHasReactionByReactionId(id);
	}

	@Override
	public boolean deleteModelPathwayHasReactionByReactionIdAndPathwayId(Integer reactionId, Integer pathId)
			throws Exception {
		return this.modelpathwayhasreactionDAO.deleteModelPathwayHasReactionByReactionIdAndPathwayId(reactionId, pathId);
	}

	@Override
	public void insertModelPathwayHasReaction(Integer idReaction, Integer idPathway) throws Exception {
		 this.modelpathwayhasreactionDAO.insertModelPathwayHasReaction(idReaction, idPathway);
	}
	
	@Override
	public void insertModelPathwayHasCompound(Integer compoundId, Integer idPathway) throws Exception {
		 this.modelPathwayHasModelCompoundDAO.insertModelPathwayHasModelCompound(idPathway, compoundId);
	}

	@Override
	public void insertModelPathwayHasModelProtein(Integer pathId, Integer proteinId) throws Exception {
		this.modelpathwayhasenzymeDAO.insertModelPathwayHasModelProtein(pathId, proteinId);
	}

	@Override
	public List<Integer> getModelPathwayHasEnzymeReactionIdByEcNumberAndPathwayIdAndProteinId(Integer pathwayId, Integer proteinId) throws Exception {
		return this.modelpathwayhasenzymeDAO.getModelPathwayHasModelProteinReactionIdByPathwayIdAndProteinId(pathwayId, proteinId);
	}

	@Override
	public List<Integer> getModelPathwayHasEnzymeReactionIdByEcNumberAndReactionIdAndProteinId(Integer reactId, Integer proteinId) throws Exception {

		return this.modelpathwayhasenzymeDAO.getModelPathwayHasModelProteinReactionIdByReactionIdAndProteinId(reactId, proteinId);
	}

	@Override
	public List<String> getAllPathwaysNamesOrdered() throws Exception {
		
		return this.modelpathwayDAO.getAllPathwaysNamesOrdered();
	}


	@Override
	public List<String> getAllPathwaysOrderedByNameInModelWithReaction(Boolean inModel) throws Exception {
		return this.modelpathwayDAO.getAllPathwaysOrderedByNameInModelWithReaction(inModel);
	}
	
	@Override
	public List<String[]> getUpdatedPathways(boolean isCompartimentalized, boolean encodedOnly) throws Exception {
		return this.modelpathwayDAO.getUpdatedPathways(isCompartimentalized, encodedOnly);
	}
	
	@Override
	public Map<Integer, String[]> countProteinIdByPathwayID(Map<Integer,String[]> qls) throws Exception {
		
		List<Object[]> result = this.modelpathwayDAO.countProteinIdByPathwayID();
		
		if(result != null && !result.isEmpty()) {

			for(Object[] item : result) {

				Integer key = Integer.valueOf(item[0].toString());
				if(qls.containsKey(key)) {
					qls.get(key)[3] = item[1]+"";
				}
				
			}
		}
		
		return qls;
		
	}

	@Override
	public Map<String, Integer> getPathwayCodeAndIdpathway() throws Exception {
		return this.modelpathwayDAO.getPathwayCodeAndIdpathway();
	}
	
	@Override
	public void insertModelPathwayHasModuleEntry(Integer pathId, Integer moduleId) throws Exception {
		this.modelPathwayHasModuleDAO.insertModelPathwayHasModule(pathId, moduleId);
	}
	
	@Override
	public boolean checkModelPathwayHasModuleEntry(Integer pathId, Integer moduleId) throws Exception {
		return this.modelPathwayHasModuleDAO.checkModelPathwayHasModuleEntry(pathId, moduleId);
	}
	
	@Override
	public boolean checkSuperPathwayData(Integer id, Integer superID) throws Exception{
		
		Map<String, Serializable> restrictions = new HashMap<>();
		restrictions.put("id.pathwayIdpathway", id);
		restrictions.put("id.superpathway", superID);
		
		ModelSuperpathway superpathway = this.modelSuperpathwayDAO.findUniqueByAttributes(restrictions);
		
		return (superpathway != null);
	}
	
	@Override
	public boolean checkPathwayHasCompoundData(Integer pathwayId, Integer compoundID) throws Exception{
		
		Map<String, Serializable> restrictions = new HashMap<>();
		restrictions.put("id.modelPathwayIdpathway", pathwayId);
		restrictions.put("id.modelCompoundIdcompound", compoundID);
		
		ModelPathwayHasCompound pathHasComp = this.modelPathwayHasModelCompoundDAO.findUniqueByAttributes(restrictions);
		
		return (pathHasComp != null);
		
	}
	
	@Override
	public Long countPathwayHasReaction(boolean isCompartimentalized) throws Exception{
		
		return this.modelpathwayDAO.countPathwayHasReaction(isCompartimentalized);
	}
	
	@Override
	public Map<Integer, List<PathwayContainer>> getPathwaysByReaction(){
		
		List<ModelPathwayHasReaction> pathHasReact = this.modelpathwayhasreactionDAO.getAllModelPathwayHasReaction();
		
		Map<Integer, List<PathwayContainer>> map = new HashMap<>();
			
		for(ModelPathwayHasReaction entry : pathHasReact) {
			
			Integer reactionId = entry.getId().getReactionIdreaction();
			
			if(!map.containsKey(reactionId))
				map.put(reactionId, new ArrayList<>());
			
			map.get(reactionId).add(new PathwayContainer(entry.getModelPathway().getIdpathway(),
					entry.getModelPathway().getCode(), entry.getModelPathway().getName()));
		}
		
		return map;
	}
	
}
