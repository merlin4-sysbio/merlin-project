package pt.uminho.ceb.biosystems.merlin.gui.operations.modelTools;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.ModelReactions;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;

@Operation(description="remove drains for external metabolites",name="remove drains")
public class RemoveDrains {
	private static final Logger logger = LoggerFactory.getLogger(RemoveDrains.class);
	private ModelReactions reaction;
	private WorkspaceAIB project;
	/**
	 * @param project
	 */
	@Port(name="workspace",description="select workspace",direction=Direction.INPUT,order=1, validateMethod="checkProject")
	public void setProject(WorkspaceAIB project) {
		
		try {  
			
			Set<Integer> drains = ModelReactionsServices.getModelDrains(project.getName());
			
			int drains_counter = drains.size();
			this.removeDrains(drains);
			reaction.setNewBlockedReaction(false);
			MerlinUtils.updateReactionsView(project.getName());
			
			Workbench.getInstance().info(drains_counter + " drains removed.");
		}
		catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * @param drain
	 * @param stmt
	 * @return
	 */
	private int removeDrains(Set<Integer> drains) {

		try {

			if(drains.size()>0) {

				logger.info("{} drains will be removed!", drains.size());
		
			ModelReactionsServices.removeReactionsBySource(project.getName(), SourceType.DRAINS );
					
			}
			return drains.size();
		}
		catch (Exception e) {
			e.printStackTrace();
			Workbench.getInstance().error(e.getMessage());
		}
		return 0;
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
			for(WorkspaceEntity ent : project.getDatabase().getEntities().getEntities()){
				if(ent.getName().equalsIgnoreCase("Reactions")){
					reaction = (ModelReactions) ent;
				}
			}
		}
	}
}
