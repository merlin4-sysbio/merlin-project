package pt.uminho.ceb.biosystems.merlin.gui.operations.modelTools;

import java.util.Map;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers.ContainerBuilder;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelReactionsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.validation.chemestry.BalanceValidator;

@Operation(description="Find unbalanced reactions in the metabolic network.")
public class FindUnbalancedReactions {

	private WorkspaceAIB project;
	private ModelReactionsAIB reaction;
	private int protonId;
	private Container container;
	private String proton = null;

	/**
	 * 
	 * @param project
	 */
	@Port(name="Workspace",description="select Workspace",direction=Direction.INPUT,order=1, validateMethod="checkProject")
	public void setProject(WorkspaceAIB project) {

		try {

			boolean reactionsExtraInfoRequired = true;
			
			this.container = new Container(new ContainerBuilder(project.getName(), 
					project.getName(),ProjectServices.isCompartmentalisedModel(project.getName()), reactionsExtraInfoRequired, project.getOrganismName(), null));

		}
		catch (Exception e) {

			e.printStackTrace();
			throw new IllegalArgumentException(e);
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

	@Port(name="Proton name",description="set the proton (H+) name",direction=Direction.INPUT,order=2, defaultValue="H+", validateMethod="checkProtonId")
	public void setProtonId(String protonName) {

		try {
			

//			for(String mid : container.getMetabolites().keySet()) {
//
//				if(this.protonId == Integer.parseInt(container.getMetabolites().get(mid).getId()))
//					proton = mid;
//			}

			BalanceValidator bv = new BalanceValidator(container);
			bv.setFormulasFromContainer();
			bv.validateAllReactions();
			bv.balanceH(proton);
			Container cont = bv.getBalancedContainer();

			for(String id : cont.getReactions().keySet())
				this.reaction.getExternalModelIdentifiers().put(container.getReactionsExtraInfo().get(id).get("MERLIN_ID"),id);


			this.reaction.setBalanceValidator(bv);
			reaction.setNewBlockedReaction(true);
			MerlinUtils.updateReactionsView(project.getName());
			Workbench.getInstance().info("balance validaton performed! \n \nunbalanced reaction names are bold and italicized");
		} 
		catch (Exception e) {

			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * @param proton
	 * @throws Exception 
	 */
	public void checkProtonId(String protonName) {

		this.protonId = -1;

		if(protonName == null || protonName.isEmpty()) {

			throw new IllegalArgumentException("Define proton identifier!");
		}
		else {

			try {
				Map<Integer, MetaboliteContainer> matrix = this.reaction.getAllMetabolites();

				for(MetaboliteContainer metabolite : matrix.values())
					if(protonName.equalsIgnoreCase(metabolite.getName())) {
						this.protonId = metabolite.getMetaboliteID();
						this.proton = metabolite.getExternalIdentifier();
					}
			} 
			catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}

			if(this.protonId < 0)
				throw new IllegalArgumentException("proton name not available in model");

		}
	}

}
