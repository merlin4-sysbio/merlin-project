package pt.uminho.ceb.biosystems.merlin.gui.datatypes.model;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModelContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary.ModelBlockedReactions;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary.ModelReactionsmetabolitesEnzymesSets;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.ModelReactions;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.datatables.ModelPathwayReactions;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Compartments;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTableAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.interfaces.IEntityAIB;
import pt.uminho.ceb.biosystems.merlin.processes.model.ModelReactionsProcesses;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelPathwaysServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelStoichiometryServices;
import pt.uminho.ceb.biosystems.mew.biocomponents.validation.chemestry.BalanceValidator;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * @author ODias
 *
 */
@Datatype(structure= Structure.SIMPLE, namingMethod="getName",removable=true,removeMethod ="remove")
public class ModelReactionsAIB extends ModelReactions implements IEntityAIB {

	private Map<Integer,Color> pathwayColors;
	private ModelBlockedReactions blockedReactions;
	private BalanceValidator balanceValidator;
	private Map<String, String> externalModelIdentifiers;
	private String workspaceName; 


	/**
	 * @param dbt
	 * @param name
	 * @throws Exception 
	 */
	public ModelReactionsAIB(WorkspaceTableAIB dbt, String name) throws Exception {

		super(dbt, name);
		workspaceName = dbt.getWorkspaceName();
		this.colorPaths();
		this.externalModelIdentifiers = new HashMap<String, String>();

	}


	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public String[][] getStats() {

		if(super.getStats()==null) {

			String[][] stats;
			try {
				List<Integer> data = ModelReactionsServices.getStats(workspaceName);
				stats = ModelReactionsProcesses.getStats(data);
				this.setStats(stats);
			} 
			catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}
			
		}

		return super.getStats();	
	}

	/**
	 * @param encodedOnly
	 * @param completeOnly
	 * @return
	 * @throws Exception 
	 */
	public ModelPathwayReactions getMainTableData(boolean encodedOnly, boolean update) throws Exception {

		if(update || this.getReactionsData() == null) {

			Map<Integer, String> pathways = ModelPathwaysServices.getPathwaysNames(workspaceName);
			ModelContainer modelContainer  = ModelReactionsServices.getMainTableData(encodedOnly, workspaceName);

			this.setIdentifiers(modelContainer.getIdentifiers());
			this.setNamesIndex(modelContainer.getNamesIndex());
			this.setActiveReactions(modelContainer.getActiveReactions());
			this.setFormulasIndex(modelContainer.getFormulasIndex());

			ModelPathwayReactions reactionsData = ModelReactionsProcesses.getMainTableData(this.workspaceName, encodedOnly, modelContainer, pathways);

			super.setReactionsData(reactionsData);
		}

		return this.getReactionsData();
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getRowInfo(java.lang.String)
	 */
	public WorkspaceDataTable[] getRowInfo(int reactionIdentifier, boolean refresh) throws Exception {

		if(super.getRowInfo()==null || refresh) {

			String name = this.getNamesIndex().get(reactionIdentifier);
			Pair<Map<String, String>, List<List<List<String>>>> data = ModelReactionsServices.getRowInfo(reactionIdentifier, name, workspaceName);
			
			WorkspaceDataTable[] results  = ModelReactionsProcesses.getRowInfo(reactionIdentifier, name, data, 
					this.getBalanceValidator(), this.getBlockedReactions(), this.externalModelIdentifiers);
			
			super.setRowInfo(results);
		}
		return super.getRowInfo();
	}


	/**
	 * @return a list with all pathways, except the SuperPathways
	 * @throws Exception 
	 */
	public String[] getUpdatedPathways(boolean encoded) throws Exception {

		List<String[]>	availablePathways = ModelPathwaysServices.getUpdatedPathways(encoded, workspaceName);

		this.setPathwaysList(ModelReactionsProcesses.getUpdatedPathways(availablePathways)); 
		this.setSelectedPathIndexID(ModelReactionsProcesses.getSelectedPathIndexID(availablePathways));

		return this.getPathwaysList();
	}

	/**
	 * @param idReaction
	 * @return
	 */
	public String[] getEnzymes(int idReaction, int pathway, Boolean refresh) {

		try {
			if(this.getEnzymes()==null || refresh) {
				this.setEnzymes(ModelReactionsServices.getEnzymes(idReaction, pathway, workspaceName));			
			}
		}
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
		return this.getEnzymes(); 
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public String[] getGenesModel() throws Exception {

		if(this.getModelGenes()==null) {
			
			List<String> genes = new ArrayList<>();
			
			List<GeneContainer> containers = ModelGenesServices.getAllGeneData(this.workspaceName);

			for(GeneContainer container : containers) {

				String gene = container.getLocusTag();

				if(gene != null && !gene.trim().isEmpty()) {
					gene = gene.concat(" (").concat(gene).concat(")");
					genes.add(gene);
				}
			}
			
			Collections.sort(genes);

			String[] res = ModelReactionsProcesses.getGenesModel(genes);
			this.setModelGenes(res);
		}
		return this.getModelGenes();
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Integer> getGenesModelID() {

		if(this.getModelGenesIdentifier()==null) {
			
			try {
				List<GeneContainer> data = ModelGenesServices.getAllGeneData(this.workspaceName);
				
				Map<String, Integer> ret = new HashMap<>();
				
				for(GeneContainer gene : data) {

					String locusTag = gene.getLocusTag();
					String name = gene.getName();

					if(name != null && !name.trim().isEmpty())
						locusTag = locusTag.concat(" (").concat(name).concat(")");

					ret.put(locusTag, gene.getIdGene());
				}

				this.setModelGenesIdentifier(ret);
			} 
			catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}
		}
		return this.getModelGenesIdentifier(); 
	}

	/**
	 * @return
	 */
	public Map<String, Integer> getGenesModelMap() {

		try {
			if(super.getGenesModelMap()==null) {

				Map<String, Integer> ret  = ModelReactionsProcesses.getGenesModelMap(ModelGenesServices.getAllGeneData(this.workspaceName));

				this.setGenesModelMap(ret);
			}
		} 
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}

		return super.getGenesModelMap();
	}

	/**
	 * @return
	 */
	public String[] getEnzymesModel() {

		if(super.getEnzymesModel()==null) {

			List<ProteinContainer> data = new ArrayList<ProteinContainer>();
			try {
				data = ModelProteinsServices.getEnzymesModel(this.workspaceName);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			String[] ret  = ModelReactionsProcesses.getEnzymesModel(data);
			super.setEnzymesModel(ret);
		}

		return super.getEnzymesModel();
	}

	/**
	 * @param rowID
	 * @return
	 * @throws Exception 
	 */
	public Set<String> getEnzymesForReaction(int rowID) throws Exception {

		List<ProteinContainer> enzymesForReaction = ModelReactionsServices.getEnzymesForReaction(this.workspaceName,rowID);
		
		Set<String> res = new HashSet<String>();
		
		for(ProteinContainer container1 : enzymesForReaction) {
			
			res.add(container1.getExternalIdentifier() + "___" + 
			container1.getIdProtein() + "___" + container1.getName());
		}
		
		return res;
		
	}

	/**
	 * @param rowID
	 * @return
	 * @throws Exception 
	 */
	public String[] getPathways(int rowID) throws Exception {

		String[] ret = ModelPathwaysServices.getExistingPathwaysNamesByReactionId(this.workspaceName, rowID);

		return ret;
	}

	/**
	 * @return list of all pathways, including superpathways
	 */
	public String[] getAllPathways(boolean inModel) {

		try {
			List<String> data = ModelPathwaysServices.getPathways(workspaceName, inModel);
			String[] ret  = ModelReactionsProcesses.getPathways(data);

			return ret;
		} catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public int getPathwayID(String name) throws Exception {

		int ret = ModelPathwaysServices.getPathwayIDbyName(workspaceName, name);
		return ret;
	}

	/**
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public String getPathwayCode(String name) throws Exception {

		String ret = ModelPathwaysServices.getPathwayCode(workspaceName, name);
		return ret;
	}

	/**
	 * @return
	 */
	public BalanceValidator getBalanceValidator() {

		return this.balanceValidator;
	}

	/**
	 * @param balanceValidator
	 */
	public void setBalanceValidator(BalanceValidator balanceValidator) {

		this.balanceValidator = balanceValidator;
	}

	/**
	 * @param rowID
	 * @return
	 */
	public ReactionContainer getReaction(int rowID) throws Exception {

		return ModelReactionsServices.getReaction(this.workspaceName, rowID);
	}

	/**
	 * @param rowID
	 * @return
	 * @throws Exception 
	 */
	public Map<Integer, MetaboliteContainer> getMetabolites(int reactionId) throws Exception {

		return ModelStoichiometryServices.getStoichiometryDataForReaction(this.workspaceName, reactionId);
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public Map<Integer, MetaboliteContainer> getAllMetabolites() throws Exception {

		return ModelReactionsServices.getAllMetabolites(workspaceName);
	}

	/**
	 * colorize pathways
	 * @param encoded 
	 * @return 
	 * @throws Exception 
	 */
	public Map<Integer, Color> colorPaths() throws Exception {

		this.pathwayColors= new TreeMap<Integer,Color>();
		List<String[]>	availablePathways = ModelPathwaysServices.getUpdatedPathways(false, workspaceName);

		this.pathwayColors = ModelReactionsProcesses.colorPaths(availablePathways);
		return this.pathwayColors;
	}

	public Map<Integer, Color> getPathwayColors() {

		return pathwayColors;
	}

	/**
	 * @param pathwayID
	 * @return
	 * @throws Exception 
	 */
	public ModelReactionsmetabolitesEnzymesSets getEnzymesIdentifiersList(int pathwayID) throws Exception {

		return ModelReactionsServices.getEnzymesIdentifiersList(pathwayID, blockedReactions, workspaceName);
	}

	/**
	 * @param noEnzymes 
	 * @param pathwayID
	 * @return
	 * @throws Exception 
	 */
	public ModelReactionsmetabolitesEnzymesSets getReactionsList(boolean noEnzymes, int pathwayID) throws Exception {

		return ModelReactionsServices.getReactionsList(noEnzymes, pathwayID, blockedReactions, workspaceName);
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public String[] getCompartments(boolean isMetabolites, boolean isCompartmentalisedModel) throws Exception {
		
		List<String> compartments = ModelReactionsServices.getCompartments(this.workspaceName, isMetabolites, isCompartmentalisedModel);
		
		return compartments.toArray(new String[compartments.size()]);
	}
	
	/**
	 * @param isMetabolites
	 * @param isCompartmentalisedModel
	 * @return
	 * @throws Exception
	 */
	public String[] getCompartmentsForReactions(boolean isMetabolites, boolean isCompartmentalisedModel) throws Exception {
		
		List<String> compartments = ModelReactionsServices.getCompartments(this.workspaceName, isMetabolites, isCompartmentalisedModel);
		
		compartments.remove(Compartments.inside.getName());
		compartments.remove(Compartments.outside.getName());
		compartments.add("");
		
		return compartments.toArray(new String[compartments.size()]);
	}

	/**
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	public boolean existGenes() throws IOException, Exception{

		return ModelGenesServices.existGenes(this.workspaceName);
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getName()
	 */
	public String getName() {

		return "reactions";

	}


	/**
	 * @return the gapReactions
	 */
	public ModelBlockedReactions getBlockedReactions() {
		return blockedReactions;
	}


	/**
	 * @param gapReactions the gapReactions to set
	 */
	public void setBlockedReactions(ModelBlockedReactions blockedReactions) {
		this.blockedReactions = blockedReactions;

		this.setNewBlockedReaction(true);
	}


	/**
	 * @return the externalModelIds
	 */
	public Map<String, String> getExternalModelIdentifiers() {
		return externalModelIdentifiers;
	}


	/**
	 * @param externalModelIds the externalModelIds to set
	 */
	public void setExternalModelIdentifiers(Map<String, String> externalModelIdentifiers) {
		this.externalModelIdentifiers = externalModelIdentifiers;
	}

	/**
	 * @param row
	 * @return
	 */
	public int getRowID(int row) {
		
		if(!this.getIdentifiers().containsKey(row))
			row --;
		return this.getIdentifiers().get(row);
	}

	/**
	 * @param id
	 * @return
	 */
	public int getRowFromID(int id) {

		for(int i : this.getIdentifiers().keySet())
			if(this.getIdentifiers().get(i) == id)
				return i;

		return -1;
	}
}
