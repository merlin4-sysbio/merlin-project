package pt.uminho.ceb.biosystems.merlin.gui.operations.modelTools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers.ContainerBuilder;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Pathways;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelReactionsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.processes.verifiers.CompartmentsVerifier;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelCompartmentServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelPathwaysServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.CompartmentCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;

@Operation(description="create drains for external metabolites",name="create drains")
public class CreateDrains {

	private static final Logger logger = LoggerFactory.getLogger(CreateDrains.class);

	private ModelReactionsAIB reaction;
	private WorkspaceAIB project;
	private String outsideName;

	@Port(name="Workspace",description="Select Workspace",direction=Direction.INPUT,order=1, validateMethod="checkProject")
	public void setProject(WorkspaceAIB project) {

	}

	@Port(name="External compartment", description="Set External compartment", direction=Direction.INPUT, defaultValue="auto", order=2, validateMethod="checkCompartment")
	public void setExternalCompartment(String compartment) throws Exception{

		Set<String> drains = new HashSet<>(), drainsTemp = new HashSet<>();


		boolean reactionsExtraInfoRequired = true;

		Container cont = new Container(new ContainerBuilder(this.project.getName(), 
				this.project.getName(), ProjectServices.isCompartmentalisedModel(this.project.getName()), reactionsExtraInfoRequired, this.project.getOrganismName(),
				null));

		Integer compartmentID = null;

		if(ProjectServices.isCompartmentalisedModel(this.project.getName())) {

			String abbreviation = "dra";

			CompartmentContainer container = ModelCompartmentServices.getCompartmentByName(this.project.getName(), "drains");

			if(container == null) 
				compartmentID = ModelCompartmentServices.insertNameAndAbbreviation(this.project.getName(), 
						"drains", abbreviation);
			else 
				compartmentID = container.getCompartmentID();
		}

		String sbmlCompartment = null;

		for(CompartmentCI compCI : cont.getCompartments().values()){

			if(compCI.getName().equalsIgnoreCase(outsideName))
				sbmlCompartment = compCI.getId();
		}

		if(sbmlCompartment != null){

			Set<String> existingDrains = new HashSet<>(cont.getDrains());
			cont.clearInfoElements();
			cont.constructDrains(cont.getCompartment(sbmlCompartment).getMetabolitesInCompartmentID(), sbmlCompartment);
			drains = cont.getDrains();
			drainsTemp.addAll(drains);

			for(String drainTemp : drainsTemp)
				for(String exDrain : existingDrains)				
					if(cont.getReaction(exDrain).hasSameStoichiometry(cont.getReaction(drainTemp), true))
						drains.remove(drainTemp);

			Map<String, Set<String>> pathway  = new HashMap<>();

			if(drains.size()>0) {
				String name = Pathways.DRAINS.getName();
				
				Integer id = ModelPathwaysServices.getPathwayIDbyName(this.project.getName(), name);
				
				if(id == null) {
					ModelPathwaysServices.insertModelPathwayCodeAndName(this.project.getName(), Pathways.DRAINS.getCode(), name);
				}
				pathway.put(name, new HashSet<String>());
			}

			int drains_counter = 0;

			for(String drain : drains) {

				ReactionCI reactionCI = cont.getReaction(drain);

				String drainID = null;
				String reactant = null, reactantID = null;
				String product = null, productID = null;
				long lowerBound = 0, upperBound = 0;

				for(String metaid : reactionCI.getReactants().keySet()) {

					reactant = cont.getMetabolitesExtraInfo().get(metaid).get("MERLIN_ID");
					reactantID = metaid;
					drainID = cont.getMetabolite(metaid).getName();
				}

				for(String metaid : reactionCI.getProducts().keySet()) {

					product = cont.getMetabolitesExtraInfo().get(metaid).get("MERLIN_ID");
					productID = metaid;
					if(drainID==null)
						drainID = cont.getMetabolite(metaid).getName();
				}

				String prefix = "R_EX_";

				drainID = prefix.concat(drainID);

				String name = drainID;

				boolean isCompartmentalisedModel = ProjectServices.isCompartmentalisedModel(this.project.getName());

				List<Integer> aux = ModelReactionsServices.getModelReactionLabelIdByName(this.project.getName(), name, isCompartmentalisedModel);

				Integer reactionID = -1;
				if(aux != null && aux.size() > 0)
					reactionID = aux.get(0);

				if(reactionID<0) {

					drains_counter ++;

					Map<String, String> compartments = new HashMap<>();
					Map<String, Double>	metabolites = new HashMap<>();
					Map<String, String> chains= new HashMap<>();

					if(reactant!=null) {

						compartments.put(reactant, cont.getCompartment(reactionCI.getReactants().get(reactantID).getCompartmentId()).getName());
						metabolites.put(reactant, reactionCI.getReactants().get(reactantID).getStoichiometryValue()*(-1));
						chains.put(reactant, "0");
						lowerBound = 0;
						upperBound = 999999;
					}

					if(product!=null) {

						compartments.put(product, cont.getCompartment(reactionCI.getReactants().get(productID).getCompartmentId()).getName());
						metabolites.put(product, reactionCI.getReactants().get(productID).getStoichiometryValue());
						chains.put(product, "0");

						lowerBound = -999999;
						upperBound = 0;

						logger.debug("drain product not null, verify bounds");
					}

					ModelReactionsServices.insertNewReaction(drainID, reactionCI.getName(), 
							compartments, metabolites, true, pathway, compartmentID, false, false, false,
							lowerBound, upperBound, SourceType.DRAINS, null, this.project.getName());
				}
			}

			reaction.setNewBlockedReaction(false);
			MerlinUtils.updateReactionsView(project.getName());
			Workbench.getInstance().info(drains_counter+" drains added.");
		}

	}

	/**
	 * @param compartment
	 * @throws Exception 
	 */
	public void checkCompartment(String compartment) throws Exception {

		CompartmentContainer container = CompartmentsVerifier.checkExternalCompartment(compartment, this.project.getName());

		if(container != null)
			this.outsideName = container.getName();
		else
			Workbench.getInstance().warn("No external compartmentID defined!");

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
					reaction = (ModelReactionsAIB) ent;
				}
			}
		}
	}

}
