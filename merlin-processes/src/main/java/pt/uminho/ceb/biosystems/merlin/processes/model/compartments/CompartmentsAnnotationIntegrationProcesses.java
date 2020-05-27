package pt.uminho.ceb.biosystems.merlin.processes.model.compartments;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.compartments.AnnotationCompartmentsGenes;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.IIntegrateData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Compartments;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.core.utilities.compartments.CompartmentsEnumerators.STAIN;
import pt.uminho.ceb.biosystems.merlin.core.utilities.compartments.CompartmentsUtilities;
import pt.uminho.ceb.biosystems.merlin.processes.model.compartments.services.CompartmentsIntegrationServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelCompartmentServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelStoichiometryServices;
import pt.uminho.ceb.biosystems.merlin.services.model.loaders.ModelDatabaseLoadingServices;
import pt.uminho.ceb.biosystems.merlin.utilities.Utilities;

/**
 * @author ODias
 *
 */
public class CompartmentsAnnotationIntegrationProcesses implements IIntegrateData, PropertyChangeListener {

	private static final Logger logger = LoggerFactory.getLogger(CompartmentsAnnotationIntegrationProcesses.class);

	private Map<Integer,AnnotationCompartmentsGenes> geneCompartments;
	private AtomicBoolean cancel;
	private CompartmentsProcesses processCompartments;
	private AtomicInteger processingTotal;
	private AtomicInteger processingCounter;
	private String workspaceName;
	private PropertyChangeSupport changes;
	private CompartmentContainer defaultInternalCompartment;
	private CompartmentContainer defaultExternalCompartment;
	private CompartmentContainer defaultMembraneCompartment;
	private boolean isEukaryote;



	/**
	 * @param project
	 * @param threshold
	 * @throws Exception 
	 */
	public CompartmentsAnnotationIntegrationProcesses(String workspaceName, Map<Integer,AnnotationCompartmentsGenes> geneCompartments) throws Exception {

		this.workspaceName = workspaceName;
		this.changes = new PropertyChangeSupport(this);
		this.geneCompartments = geneCompartments;
		this.cancel = new AtomicBoolean(false);
	}

	/**
	 * @param bool
	 * @throws Exception 
	 */
	public boolean initProcessCompartments() throws Exception {

		Set<String> compartments = new HashSet<>();

		List<CompartmentContainer> containers = ModelCompartmentServices.getCompartmentsInfo(this.workspaceName);

		for(CompartmentContainer container : containers) {
			if(container.getName() != null && !container.getName().isEmpty())
				compartments.add(container.getName());
		}

		String interiorCompartment = CompartmentsIntegrationServices.autoSetInteriorCompartment(this.workspaceName);
		Map<Integer,String> compartmentsAbb_ids = ModelCompartmentServices.getModelCompartmentIdAndAbbreviation(this.workspaceName);
		Map<String,Integer> idCompartmentAbbIdMap = ModelCompartmentServices.getAbbreviationAndCompartmentId(this.workspaceName);
		this.processCompartments = new CompartmentsProcesses(interiorCompartment, compartmentsAbb_ids, idCompartmentAbbIdMap);
		this.processCompartments.initProcessCompartments(compartments);

		return true;
	}

	/**
	 * @return
	 */
	public boolean performIntegration() {

		try {
			

			this.processingTotal.set(this.geneCompartments.size());


			Map<String, Integer> sequenceID_geneID = ModelGenesServices.getQueriesByGeneID(this.workspaceName);

			Map<String,Integer> compartmentsDatabaseIDs = new HashMap<>();

			List<CompartmentContainer> compartments = new ArrayList<>();
			Set<String> compartmentNames = new HashSet<>();

			for(AnnotationCompartmentsGenes genesCompartment : this.geneCompartments.values()) {

				if(compartments.isEmpty()) {

					compartments.add(new CompartmentContainer(genesCompartment.getPrimary_location(), genesCompartment.getPrimary_location_abb() ));
					compartmentNames.add(genesCompartment.getPrimary_location());
				}

				boolean add = false;
				for(String compartmentName : compartmentNames) {

					if(!compartmentName.equals(genesCompartment.getPrimary_location())) {

						compartments.add(new CompartmentContainer(genesCompartment.getPrimary_location(), genesCompartment.getPrimary_location_abb() ));
						add = true;
					}
				}
				if(add)
					compartmentNames.add(genesCompartment.getPrimary_location());


				for( String secondaryCompartment : genesCompartment.getSecondary_location().keySet()) {

					add = false;
					for(String compartmentName : compartmentNames) {

						if(!compartmentName.equals(secondaryCompartment)) {

							compartments.add(new CompartmentContainer(secondaryCompartment, genesCompartment.getSecondary_location_abb().get(secondaryCompartment) ));
							add = true;
						}
					}
					if(add)
						compartmentNames.add(secondaryCompartment);
				}
			}
			compartmentsDatabaseIDs.putAll(ModelCompartmentServices.getCompartmentsDatabaseIDs(this.workspaceName, compartments));
			this.initProcessCompartments();

			for(Map.Entry<Integer, AnnotationCompartmentsGenes> entry : this.geneCompartments.entrySet())
			{

				if(this.cancel.get()) {

					this.processingCounter = new AtomicInteger(this.geneCompartments.keySet().size());
					break;
				}
				else {

					AnnotationCompartmentsGenes geneCompartments = this.geneCompartments.get(entry.getKey());
					String primaryCompartment = geneCompartments.getPrimary_location();
					double scorePrimaryCompartment = geneCompartments.getPrimary_score();
					Map<String, Double> secondaryCompartments = geneCompartments.getSecondary_location();

					Integer idGene = null;
					if(sequenceID_geneID.containsKey(geneCompartments.getGene()))
						idGene = sequenceID_geneID.get(geneCompartments.getGene());

					if(idGene==null)
						logger.trace("Gene {} not found!", entry.getKey());
					else						
						ModelGenesServices.loadGenesCompartments(this.workspaceName, idGene, compartmentsDatabaseIDs,  primaryCompartment, scorePrimaryCompartment, secondaryCompartments);

				}

				this.changes.firePropertyChange("sequencesCounter", this.processingCounter.get(), this.processingCounter.incrementAndGet());
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}


	/**
	 * @param ignoreList
	 * @return
	 */
	/**
	 * @param ignoreList
	 * @return
	 */
	public boolean assignCompartmentsToMetabolicReactions(List<String> ignoreList) {

		try {

			Map<Integer, List<Integer>> enzymesReactions = ModelReactionsServices.getEnzymesReactions2(this.workspaceName);

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			Map<Integer, List<Integer>> enzymesCompartments = ModelProteinsServices.getEnzymesCompartments(this.workspaceName);
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			Map<Integer, ReactionContainer> reactionsMap = ModelDatabaseLoadingServices.getEnzymesReactionsMap(this.workspaceName, false);

			this.processingTotal.set(this.processingTotal.get()+enzymesReactions.size());

			for(Integer proteinId : enzymesReactions.keySet()) {

				for(Integer idReaction: enzymesReactions.get(proteinId)) {

					ReactionContainer reaction = new ReactionContainer(reactionsMap.get(idReaction), true);                                                                                                                                                                                                                                                                     

					if(enzymesCompartments.containsKey(proteinId)) {

						Set<Integer> parsedCompartments = this.processCompartments.parseCompartments(enzymesCompartments.get(proteinId), ignoreList);

						//all compartments are assigned to the enzyme
						for(int idCompartment: parsedCompartments) {

							if(idCompartment>0) {

								if(this.processCompartments.getIgnoreCompartmentsID().contains(idCompartment))
									reaction.setLocalisation(this.processCompartments.getInteriorCompartmentID());

								reaction.setLocalisation(idCompartment);
								assignCompartmentsToMetabolites(reaction, null, null);
								ModelDatabaseLoadingServices.loadReaction(this.workspaceName, reaction, proteinId, false);
							}
						}
					}
					else {
						int idCompartment = this.processCompartments.getInteriorCompartmentID();
						reaction.setLocalisation(idCompartment);
						assignCompartmentsToMetabolites(reaction, null, null);
						ModelDatabaseLoadingServices.loadReaction(this.workspaceName, reaction, proteinId, false);
					}
				}

				this.changes.firePropertyChange("sequencesCounter", this.processingCounter.get(), this.processingCounter.incrementAndGet());
			}

			this.processingTotal.set(this.processingTotal.get()+reactionsMap.size());

			for(int idReaction: reactionsMap.keySet()) {

				logger.trace("reaction {} is in model {}", reactionsMap.get(idReaction).getExternalIdentifier(), reactionsMap.get(idReaction).isInModel());

				int idCompartment = this.processCompartments.getInteriorCompartmentID();
				reactionsMap.get(idReaction).setLocalisation(idCompartment);
				ModelDatabaseLoadingServices.loadReaction(this.workspaceName, reactionsMap.get(idReaction), null, false);

				this.changes.firePropertyChange("sequencesCounter", this.processingCounter.get(), this.processingCounter.incrementAndGet());

			}
			return true;
		}
		catch (Exception e) { 

			e.printStackTrace();
		}

		return false;
	}

	private void assignCompartmentsToMetabolites(ReactionContainer reaction, String inside, String outside) {

		Integer reactionCompartmentId = null;

		if(reaction.getLocalisation() != null) {
			reactionCompartmentId = reaction.getLocalisation().getCompartmentID();
		}

		List<MetaboliteContainer> reactants = reaction.getReactantsStoichiometry();

		for(MetaboliteContainer reac : reactants) {
			reac.setCompartmentID(reactionCompartmentId);
		}

		List<MetaboliteContainer> products = reaction.getProductsStoichiometry();
		for(MetaboliteContainer prod : products) {
			prod.setCompartmentID(reactionCompartmentId);
		}
	}

	/**
	 * @param reaction
	 * @param geneHasCompartments
	 * @return
	 * @throws Exception 
	 */
	private List<ReactionContainer> assignCompartmentsToTransportReactionContainers(ReactionContainer reaction, Map<Integer, List<CompartmentContainer>> geneHasCompartments) throws Exception {

		Map<String, ReactionContainer> reactionsByCompartment = new HashMap<>();
		
		String geneRule = reaction.getGeneRule();

		if(geneRule != null)
			geneRule = geneRule.toUpperCase();
		
		Set<Set<Integer>> rules = Utilities.parseStringRuleToSet(geneRule);			//deve faltar tirar os parentesis

		for(Set<Integer> rule : rules) {

			if(rule.size() == 1) {

				Integer geneId = new ArrayList<>(rule).get(0);

					if(geneHasCompartments.containsKey(geneId)) {
						for(CompartmentContainer compartment : geneHasCompartments.get(geneId)) {
							
							boolean valid = this.processCompartments.checkIfValidMembrane(compartment.getAbbreviation());
							
							if(!valid)
								compartment = this.defaultMembraneCompartment;
	
							reactionsByCompartment = replicateReaction(geneId.toString(), reaction, compartment, reactionsByCompartment);
						}
					}
					else {
						reactionsByCompartment = replicateReaction(geneId.toString(), reaction, this.defaultMembraneCompartment, reactionsByCompartment);
					}
			}
			else {
				Map<String, Integer> counts = new HashMap<>();
				Map<String, CompartmentContainer> containersMapping = new HashMap<>();
				String newRule = "";

				for(Integer geneId : rule) {
					if(geneHasCompartments.containsKey(geneId)) {
						for(CompartmentContainer compartment : geneHasCompartments.get(geneId)) {
	
							String abb = compartment.getAbbreviation();
	
							if(counts.containsKey(abb))
								counts.put(abb, counts.get(abb)+1);
							else
								counts.put(abb, 1);
	
							if(!containersMapping.containsKey(abb))
								containersMapping.put(abb, compartment);
						}
					}
					else {
						String abb = this.defaultMembraneCompartment.getAbbreviation();
						
						if(counts.containsKey(abb))
							counts.put(abb, counts.get(abb)+1);
						else
							counts.put(abb, 1);

						if(!containersMapping.containsKey(abb))
							containersMapping.put(abb, this.defaultMembraneCompartment);
					}
					newRule = newRule.concat(geneId.toString()).concat(" AND ");
				}
				newRule = newRule.replaceAll(" AND $", "");

				List<String> abbreviations = processCounts(counts);

				for(String abb : abbreviations) {

					reactionsByCompartment = replicateReaction(newRule, reaction, containersMapping.get(abb), reactionsByCompartment);

				}

			}
		}

		return new ArrayList<>(reactionsByCompartment.values());
	}

	/**
	 * @param rule
	 * @param reaction
	 * @param compartment
	 * @param reactionsByCompartment
	 * @return
	 */
	private static Map<String, ReactionContainer> replicateReaction(String rule, ReactionContainer reaction, CompartmentContainer compartment, Map<String, ReactionContainer> reactionsByCompartment){

		String abb = compartment.getAbbreviation();

		ReactionContainer compReaction = null;

		if(reactionsByCompartment.containsKey(abb)) {
			compReaction = reactionsByCompartment.get(abb);
		}
		else {
			compReaction = new ReactionContainer(reaction, false);
			compReaction.setGeneRule("");
			compReaction.setLocalisation(compartment);
		}

		String geneRule = compReaction.getGeneRule();

		geneRule = geneRule.concat(" OR (").concat(rule.toString()).concat(")").replaceAll("^ OR ", "").replaceAll(" OR\\s+OR ", " OR ").replaceAll(" AND\\s+AND ", " AND ");

		compReaction.setGeneRule(geneRule);
		reactionsByCompartment.put(abb, compReaction);

		return reactionsByCompartment;
	}

	/**
	 * @param counts
	 * @return
	 */
	private List<String> processCounts(Map<String, Integer> counts){

		List<String> results = new ArrayList<>();
		Integer max = null;
		
		Map<String, Integer> countsAux = Utilities.sortMapByDescendingOrder(counts);
		
		for(String abbreviation : countsAux.keySet()) {
			
			if(max == null)
				max = countsAux.get(abbreviation);
			
			boolean valid = this.processCompartments.checkIfValidMembrane(abbreviation);
			
			if(countsAux.get(abbreviation) == max && valid)
				results.add(abbreviation);
			else if(results.isEmpty() && valid);
				results.add(abbreviation);
		}
		
		if(results.isEmpty())
			results.add(this.defaultMembraneCompartment.getAbbreviation());
		
		return results;
	}


	/**
	 * @param ignoreList
	 * @return
	 * @throws Exception 
	 */
	public boolean assignCompartmentsToTransportReactions(List<String> ignoreList, boolean compartmentalized) throws Exception {

		Map<Integer, List<CompartmentContainer>> geneHasCompartments = ModelGenesServices.getCompartmentsRelatedToGenes(this.workspaceName);

		Map<Integer, ReactionContainer> reactionsMap = ModelReactionsServices.getAllModelReactionAttributesbySource(this.workspaceName, false, SourceType.TRANSPORTERS);

		Map<Integer, ReactionContainer> reactionsPathways = ModelReactionsServices.getReactionIdAndPathwayID(this.workspaceName, SourceType.TRANSPORTERS, false);

		for(Integer reactionId : reactionsPathways.keySet()){
			reactionsMap.get(reactionId).setPathways(reactionsPathways.get(reactionId).getPathways());
		}


		Map<Integer, ReactionContainer> reactionsMetabolites = ModelStoichiometryServices.getAllOriginalTransportersFromStoichiometry(this.workspaceName);

		for(Integer reactionId : reactionsMetabolites.keySet()){

			for(MetaboliteContainer container : reactionsMetabolites.get(reactionId).getProductsStoichiometry()) 
				reactionsMap.get(reactionId).addProduct(container);

			for(MetaboliteContainer container : reactionsMetabolites.get(reactionId).getReactantsStoichiometry()) 
				reactionsMap.get(reactionId).addReactant(container);
		}

		this.defaultMembraneCompartment = getDefaultMembraneCompartment();

		for(Integer idReaction: reactionsMap.keySet()) {

			ReactionContainer transportReaction = reactionsMap.get(idReaction);

			transportReaction.setInModel(true);

			List<ReactionContainer> reactionsToSave = assignCompartmentsToTransportReactionContainers(transportReaction, geneHasCompartments);

			for(ReactionContainer reactionToSave : reactionsToSave) {
				
				String newAbb = reactionToSave.getLocalisation().getAbbreviation();

				STAIN stain = this.processCompartments.getStain();

				List<MetaboliteContainer> reactants = reactionToSave.getReactantsStoichiometry();
				reactionToSave.setReactantsStoichiometry(new ArrayList<MetaboliteContainer>());

				for ( MetaboliteContainer metabolite : reactants) {

					String oldMetaboliteAbb = metabolite.getAbbreviation();
					String newMetaboliteCompartment = null;

					if(oldMetaboliteAbb.equals(Compartments.inside.getAbbreviation()))
						newMetaboliteCompartment = CompartmentsUtilities.getInsideMembrane(newAbb, stain);

					else
						newMetaboliteCompartment = CompartmentsUtilities.getOutsideMembrane(newAbb, stain);

					Integer newCompartmentID = null;

					if(!this.processCompartments.getIdCompartmentAbbIdMap().containsKey(newMetaboliteCompartment.toLowerCase())) {

						String name = CompartmentsUtilities.parseAbbreviation(newMetaboliteCompartment.toLowerCase()).toLowerCase();

						newCompartmentID = ModelCompartmentServices.insertNameAndAbbreviation(this.workspaceName, name, newMetaboliteCompartment.toLowerCase());

						this.processCompartments.getIdCompartmentAbbIdMap().put(newMetaboliteCompartment.toLowerCase(), newCompartmentID);

					}
					else
						newCompartmentID = this.processCompartments.getIdCompartmentAbbIdMap().get(newMetaboliteCompartment.toLowerCase());

					metabolite.setCompartmentID(newCompartmentID);
					reactionToSave.addReactant(metabolite);
				}

				List<MetaboliteContainer> products = reactionToSave.getProductsStoichiometry();
				reactionToSave.setProductsStoichiometry(new ArrayList<MetaboliteContainer>());

				for ( MetaboliteContainer metabolite : products) {

					String oldMetaboliteAbb = metabolite.getAbbreviation();
					String newMetaboliteCompartment = null;

					if(oldMetaboliteAbb.equals(Compartments.inside.getAbbreviation())) {

						if(this.defaultInternalCompartment == null)
							newMetaboliteCompartment = CompartmentsUtilities.getInsideMembrane(newAbb, stain);
						else
							newMetaboliteCompartment = this.defaultInternalCompartment.getAbbreviation();
					}
					else {
						if(this.defaultExternalCompartment == null)
							newMetaboliteCompartment = CompartmentsUtilities.getOutsideMembrane(newAbb, stain);
						else
							newMetaboliteCompartment = this.defaultExternalCompartment.getAbbreviation();
					}
					Integer newCompartmentID = null;

					if(!this.processCompartments.getIdCompartmentAbbIdMap().containsKey(newMetaboliteCompartment.toLowerCase())) {

						String name = CompartmentsUtilities.parseAbbreviation(newMetaboliteCompartment.toLowerCase()).toLowerCase();

						newCompartmentID = ModelCompartmentServices.insertNameAndAbbreviation(this.workspaceName, name, newMetaboliteCompartment.toLowerCase());

						this.processCompartments.getIdCompartmentAbbIdMap().put(newMetaboliteCompartment.toLowerCase(), newCompartmentID);
					}
					else
						newCompartmentID = this.processCompartments.getIdCompartmentAbbIdMap().get(newMetaboliteCompartment.toLowerCase());

					metabolite.setCompartmentID(newCompartmentID);
					reactionToSave.addProduct(metabolite);

				}

				//////////////////////////////////////////////////////////////////
				ModelDatabaseLoadingServices.loadReaction(this.workspaceName, reactionToSave, null, true);

				//				logger.debug("Transporter compartment {}",abb);
			}
		}
		return true;
	}

	/**
	 * @return
	 */
	public CompartmentContainer getInternalCompartment() {
		return defaultInternalCompartment;
	}

	/**
	 * @param internalCompartment
	 */
	public void setInternalCompartment(CompartmentContainer internalCompartment) {
		this.defaultInternalCompartment = internalCompartment;
	}

	/**
	 * @return
	 */
	public CompartmentContainer getExternalCompartment() {
		return defaultExternalCompartment;
	}
	
	/**
	 * @param internalCompartment
	 */
	public void setExternalCompartment(CompartmentContainer externalCompartment) {
		this.defaultExternalCompartment = externalCompartment;
	}

	/**
	 * @param defaultMembraneCompartment
	 */
	public void setDefaultMembraneCompartment(CompartmentContainer defaultMembraneCompartment) {
		this.defaultMembraneCompartment = defaultMembraneCompartment;
	}
	
	/**
	 * @return
	 * @throws Exception 
	 */
	public CompartmentContainer getDefaultMembraneCompartment() throws Exception {
		
		if(this.defaultMembraneCompartment == null) {
			
			String defaultAbb = CompartmentsUtilities.assignCorrectMembraneDefaultCompartment(isEukaryote);
			Integer idCompartment = null;
	
			if(!this.processCompartments.getIdCompartmentAbbIdMap().containsKey(defaultAbb)) {
				String name = CompartmentsUtilities.parseAbbreviation(defaultAbb);
				
				CompartmentContainer container = ModelCompartmentServices.getCompartmentByAbbreviation(this.workspaceName, defaultAbb);
				
				if(container == null)
					idCompartment = ModelCompartmentServices.insertNameAndAbbreviation(this.workspaceName, name, defaultAbb);
				else
					idCompartment = container.getCompartmentID();
				
				this.processCompartments.getIdCompartmentAbbIdMap().put(defaultAbb, idCompartment);
			}
			idCompartment = this.processCompartments.getIdCompartmentAbbIdMap().get(defaultAbb);
			
			return ModelCompartmentServices.getCompartmentById(this.workspaceName, idCompartment);
		}
		
		return this.defaultMembraneCompartment;
	}

	/**
	 * @return the isEukaryote
	 */
	public boolean isEukaryote() {
		return isEukaryote;
	}

	/**
	 * @param isEukaryote the isEukaryote to set
	 */
	public void setEukaryote(boolean isEukaryote) {
		this.isEukaryote = isEukaryote;
	}

	/**
	 * @param processingTotal
	 */
	public void setQuerySize(AtomicInteger querySize) {

		this.processingTotal = querySize; 		
	}

	public void setCancel(AtomicBoolean cancel) {

		this.cancel = cancel;
	}

	public void cancel() {

		this.cancel.set(true);
	}

	/**
	 * @return the processingCounter
	 */
	public AtomicInteger getProcessingCounter() {
		return processingCounter;
	}

	/**
	 * @param processingCounter the processingCounter to set
	 */
	public void setProcessingCounter(AtomicInteger processingCounter) {
		this.processingCounter = processingCounter;
	}

	/**
	 * @param l
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	/**
	 * @param l
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		this.changes.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());				
	}

}
