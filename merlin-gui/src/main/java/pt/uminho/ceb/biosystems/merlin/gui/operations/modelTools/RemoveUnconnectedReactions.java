/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.gui.operations.modelTools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelReactionsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;

/**
 * @author ODias
 *
 */
@Operation(name="Remove unconnected reactions", description="Remove reactions with unconnected metabolites.")
public class RemoveUnconnectedReactions {


	private boolean removeReactions;
	private boolean removeTransportReactions;
	private ModelReactionsAIB reaction;
	private WorkspaceAIB project;
	private boolean removeNeighbourReactions;

	/**
	 * @param removeReactions
	 */
	@Port(name="Remove unconnected chemical reactions",//description="Remove chemical reactions with unconnected metabolites from the model.", 
			defaultValue="true", direction = Direction.INPUT, order=1)
	public void removeReactions(boolean removeReactions) {

		this.removeReactions = removeReactions;
	}

	/**
	 * @param project
	 */
	@Port(name="Remove unconnected transport reactions",//description="Remove transport reactions with unconnected metabolites from the model.", 
			defaultValue="true", direction = Direction.INPUT, order=2)
	public void removeTransportReactions(boolean removeTransportReactions){

		this.removeTransportReactions = removeTransportReactions;
	}

	/**
	 * @param removeReactions
	 */
	@Port(name="Remove unconnected neighbour reactions",//description="Remove chemical reactions with unconnected metabolites from the model.", 
			defaultValue="true", direction = Direction.INPUT, order=3)
	public void removeNeighboursReactions(boolean removeNeighbourReactions) {

		this.removeNeighbourReactions = removeNeighbourReactions;
	}

	/**
	 * @param project
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@Port(name="Select Workspace",description="Select Workspace", validateMethod="checkProject", direction = Direction.INPUT, order=4)
	public void setProject(WorkspaceAIB project) throws NumberFormatException, Exception {

		if(ProjectServices.isMetabolicDataAvailable(project.getName()) && (this.removeReactions || this.removeTransportReactions)) {

			List<String> removed_reactions = new ArrayList<String> ();
			Set<String> transportCounter = new HashSet<String>();
			Set<String> reactionsCounter = new HashSet<String>();
			Set<String> globalTransportCounter = new HashSet<String>();
			Set<String> globalReactionsCounter = new HashSet<String>();
			Set<String> globalcounter = new HashSet<String>();

			boolean go = false;

			for(String reaction : this.reaction.getBlockedReactions().getReactions().keySet()) {

				if(removeTransportReactions && reaction.startsWith("T"))
					go = true;

				if(removeReactions && reaction.startsWith("R") && !reaction.toLowerCase().contains("biomass"))
					go = true;

				if(go) {

					globalcounter.add(reaction);

					if(reaction.startsWith("T")) {

						transportCounter.add(reaction);
						globalTransportCounter.add(reaction);
					}
					else {

						reactionsCounter.add(reaction);
						globalReactionsCounter.add(reaction);
					}
					
					ModelReactionsServices.updateModelReactionInModelByReactionId(project.getName(), Integer.valueOf(this.reaction.getBlockedReactions().getAllReactions().get(reaction)), false);

//					String query = "UPDATE reaction SET inModel = false WHERE idreaction ="
//							+this.reaction.getBlockedReactions().getAllReactions().get(reaction);

					//ProjectAPI.executeQuery(query, stmt);
					go = false;
					removed_reactions.add(reaction);
				}
			}

			if(removeNeighbourReactions)
				for(String reaction : this.reaction.getBlockedReactions().getNeighbourReactions()) {

					if(removeTransportReactions && reaction.startsWith("T"))
						go = true;

					if(removeReactions && reaction.startsWith("R") && !reaction.toLowerCase().contains("biomass"))
						go = true;

					if(go) {

						globalcounter.add(reaction);

						if(reaction.startsWith("T")) {

							transportCounter.add(reaction);
							globalTransportCounter.add(reaction);
						}
						else {

							reactionsCounter.add(reaction);
							globalReactionsCounter.add(reaction);
						}
						ModelReactionsServices.updateModelReactionInModelByReactionId(project.getName(), Integer.valueOf(this.reaction.getBlockedReactions().getAllReactions().get(reaction)), false);
//						String query = "UPDATE reaction SET inModel = false WHERE idreaction = "
//								+this.reaction.getBlockedReactions().getAllReactions().get(reaction);
//
//						ProjectAPI.executeQuery(query, stmt);
						go = false;
						removed_reactions.add(reaction);
					}
				}

			reaction.setNewBlockedReaction(false);
			MerlinUtils.updateReactionsView(project.getName());

			Workbench.getInstance().info(globalReactionsCounter.size()+" chemical reactions removed!\n"+
					globalTransportCounter.size()+" transport reactions removed!\n"
					+globalcounter.size()+" reactions removed!");
		}
		else {

			if(!this.removeReactions && !this.removeTransportReactions && !this.removeNeighbourReactions)
				Workbench.getInstance().error(new Exception("Please select reactions to be removed!"));
			else
				Workbench.getInstance().error("Gene data for integration unavailable!");
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
				throw new IllegalArgumentException("Reactions view unavailable!");

			if(this.reaction.getBlockedReactions()==null)
				throw new IllegalArgumentException("Please find unconnect reactions in the model before performing this operation!");

			if(this.reaction.getBlockedReactions().getReactions().isEmpty())
				throw new IllegalArgumentException("There are no gaps in this model!");
		}
	}

}
