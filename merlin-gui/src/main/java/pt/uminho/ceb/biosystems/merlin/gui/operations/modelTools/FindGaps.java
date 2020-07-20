package pt.uminho.ceb.biosystems.merlin.gui.operations.modelTools;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers.ContainerBuilder;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary.ModelBlockedReactions;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelReactionsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.merlin.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.ContainerUtils;

/**
 * @author ODias
 *
 */
@Operation(name="Find unconnected reactions", description="Find reactions with unconnected metabolites.")
public class FindGaps {

	private WorkspaceAIB project;
	private ModelReactionsAIB reaction;

	/**
	 * 
	 * @param project
	 * @throws Exception 
	 */
	@Port(name="Workspace",description="Select Workspace",direction=Direction.INPUT,order=1, validateMethod="checkProject")
	public void setProject(WorkspaceAIB project) throws Exception {

		Container container;
		boolean reactionsExtraInfoRequired = true;
		try {
			
			container = new Container(new ContainerBuilder(project.getName(), 
					project.getName(), ProjectServices.isCompartmentalisedModel(this.project.getName()), reactionsExtraInfoRequired, project.getOrganismName(), null));
			
			Set<String> compounds = new HashSet<>(), 
					neighbourReactions = new HashSet<>(), neighbourReactionsOriginal = new HashSet<>(), 
					gapIds = ContainerUtils.identyfyReactionWithDeadEnds(container);
			
			Map<String, Set<String>> reactions = new HashMap<>(),
					gapReactionsOriginal= new HashMap<String, Set<String>>(),
					gapReactions = new HashMap<String, Set<String>>();
		
			Map<String,String> idsMap = new HashMap<>();
			
			Set<String> mets = container.identifyDeadEnds(true);
		
			for(String id : mets)
				compounds.add(container.getMetabolitesExtraInfo().get(id).get("KEGG_CPD"));
			
			for(String id : gapIds)
				reactions.put(container.getReactionsExtraInfo().get(id).get("MERLIN_ID"), new HashSet<>());
			
//			Statement stmt = connection.createStatement();
//
//			String aux = "";
//
//			if(ProjectServices.isCompartmentalisedModel(this.project.getName()))
//				aux = aux.concat(" WHERE NOT originalReaction ");
//			else
//				aux = aux.concat(" WHERE originalReaction ");

			List<String[]> result = ModelReactionsServices.getDataFromReactionForBlockedReactionsTool(this.project.getName(), ProjectServices.isCompartmentalisedModel(this.project.getName()));

			for(int i=0; i<result.size(); i++){
				String[] list = result.get(i);
				
				String reaction = list[1];
				String compound = list[2];
				String id = list[0];
				
				if(reactions.containsKey(reaction))
					reactions.get(reaction).add(compound);
				
				idsMap.put(reaction, id);
			}
			
			for(String reaction : reactions.keySet())
				for(String metabolite: reactions.get(reaction))
					if(compounds.contains(metabolite)) {
						
						gapReactions.put(reaction, reactions.get(reaction));
						gapReactionsOriginal.put(reaction.split("_")[0], reactions.get(reaction));
						
					}
					else {
						
						neighbourReactions.add(reaction);
						neighbourReactionsOriginal.add(reaction.split("_")[0]);
					}
			
			ModelBlockedReactions rga = new ModelBlockedReactions(compounds, gapReactions, neighbourReactions, idsMap);
			rga.setReactionsOriginal(gapReactionsOriginal);
			rga.setNeighbourReactionsOriginal(neighbourReactionsOriginal);
			this.reaction.setBlockedReactions(rga);
			
			MerlinUtils.updateReactionsView(project.getName());
			Workbench.getInstance().info(gapIds.size()+" unconnected reaction(s) found \n for "+mets.size()+" dead end metabolite(s)!");
		} 
		catch (IOException e) {

			e.printStackTrace();
		}
	
	}

	/**
	 * @param project
	 */
	public void checkProject(WorkspaceAIB project) {

		if(project == null) {

			throw new IllegalArgumentException("No ProjectGUISelected!");
		}
		else {

			this.project = project;

			for(WorkspaceEntity ent : this.project.getDatabase().getEntities().getEntities())
				if(ent.getName().equalsIgnoreCase("Reactions"))
					reaction = (ModelReactionsAIB) ent;

			if(this.reaction.getActiveReactions() == null)
				throw new IllegalArgumentException("Please open the reactions viewer!");
		}
	}
}
