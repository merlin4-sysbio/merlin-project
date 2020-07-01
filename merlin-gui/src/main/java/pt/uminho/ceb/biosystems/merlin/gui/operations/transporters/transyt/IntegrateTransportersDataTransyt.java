package pt.uminho.ceb.biosystems.merlin.gui.operations.transporters.transyt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.biocomponents.container.TransportReactionCI;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.IIntegrateData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Pathways;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TimeLeftProgress;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelCompartmentServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelMetabolitesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelPathwaysServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelStoichiometryServices;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.ContainerUtils;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.StoichiometryValueCI;

public class IntegrateTransportersDataTransyt implements IIntegrateData {

	private static final Logger logger = LoggerFactory.getLogger(IntegrateTransportersDataTransyt.class);
	private Map<String,String> metabolites_ids;
	private Map<String,String> metabolites_names;
//	private Map<String,TransportProteinFamiliesSet> genes_protein_ids;
	Map<String, ReactionCI> reactions;
	private AtomicBoolean cancel;
	private TimeLeftProgress progress;
	private long startTime;
	private List<String> metabolitesInModel;
//	private Integer compartmentID;
	private String workspaceName;

	/**
	 * @param project
	 * @throws Exception 
	 */
	public IntegrateTransportersDataTransyt (WorkspaceAIB project, Map<String, ReactionCI> reactions) throws Exception {

		this.reactions = reactions;
//		this.compartmentID = null;
		this.metabolites_ids = new HashMap<String, String>();
		this.metabolites_names = new HashMap<String, String>();
		this.workspaceName = project.getName();
		this.getMetabolitesInModel();
		this.cancel = new AtomicBoolean(false);
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	private void getMetabolitesInModel() throws Exception {

		this.metabolitesInModel = new ArrayList<String>();

		try {

			List<String> data = ModelMetabolitesServices.getAllCompoundsInModel(this.workspaceName);

			for(int i=0; i<data.size(); i++)
				this.metabolitesInModel.add(data.get(i));	
			boolean isCompartmentalisedModel = ProjectServices.isCompartmentalisedModel(this.workspaceName);
//			if(isCompartmentalisedModel) {
//				CompartmentContainer compartment = ModelCompartmentServices.getCompartmentByName(this.workspaceName, "cytoplasmic_membrane");
//				if(compartment == null)
//					compartment = ModelCompartmentServices.getCompartmentByName(this.workspaceName, "plasma_membrane");
//				if(compartment == null)
//					this.compartmentID =  ModelCompartmentServices.insertNameAndAbbreviation(this.workspaceName, "plasma_membrane", CompartmentsUtilities.DEFAULT_MEMBRANE);
//				else
//					this.compartmentID = compartment.getCompartmentID();
//			}
//			if (this.compartmentID<0) {
//
//				int compID = CompartmentsAPI.getCompartmentID("inside", statement);
//
//				if(compID<0) {
//
//					String query = "INSERT INTO model_compartment (name, abbreviation) VALUES('inside','in')";
//					compID = ProjectAPI.executeAndGetLastInsertID(query, statement);
//				}
//				this.compartmentID = compID;
//			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * @return
	 */
	public boolean performIntegration() {

		int counter = 0;

		try {

			Map<String, String> compartments_ids = new HashMap<>();
			
			Map<Integer, String> allGenes = ModelGenesServices.getGeneIdandGeneQuery(this.workspaceName);

			
			for(String reactionName : reactions.keySet()) {

				if(this.cancel.get()) {

					counter = reactions.keySet().size();
					break;
				}
				else {
					
					ReactionCI reactionCi = reactions.get(reactionName);
					
					String geneRule = this.getGeneRuleWithGeneIDs(reactionCi, allGenes);

					TransportReactionCI reaction = new TransportReactionCI(reactionName, reactionName, reactionCi.getReversible(), reactionCi.getReactants(), reactionCi.getProducts());
					boolean isReversible = reaction.isReversible();

					try  {

						Integer idPathway = ModelPathwaysServices.getPathwayIDbyName(this.workspaceName, Pathways.TRANSPORTERS.getName());
						
						if(idPathway == null)
							idPathway = ModelPathwaysServices.insertModelPathwayCodeAndName(this.workspaceName, Pathways.TRANSPORTERS.getCode(), Pathways.TRANSPORTERS.getName());

						Map<String, StoichiometryValueCI> reactants = reaction.getReactants();
						Map<String, StoichiometryValueCI> products = reaction.getProducts();
						boolean reactionInModel = true;

						boolean go = true;

						//###seach and save in memory the id compound to avoid multiple searches to the database of the same metabolite!!

						Set<String> allMetabolitesInReaction = new HashSet<>(reactants.keySet());
						allMetabolitesInReaction.addAll(products.keySet());

						for(String metabolite : allMetabolitesInReaction) {

							if(!this.metabolites_ids.containsKey(metabolite)) {

								String auxMetab = metabolite.replace("M_", "").replaceAll("_\\w\\d$", "");
								
								MetaboliteContainer compound = ModelMetabolitesServices.getCompoundByExternalIdentifier(this.workspaceName, auxMetab);

								if(compound == null)
									go = false;
								else {
									this.metabolites_ids.put(metabolite, compound.getMetaboliteID()+"");
									this.metabolites_names.put(metabolite, compound.getName());
								}
							}

							if(this.metabolites_ids.containsKey(metabolite) && !this.metabolitesInModel.contains(this.metabolites_ids.get(metabolite)))
								reactionInModel = false;
						}

						//###

						if(go) {

							String equation="";
							for(String key :reactants.keySet()) {

								String comp = "in";

								if(reactants.get(key).getCompartmentId().equals("e"))
									comp = "out";

								equation=equation.concat(reactants.get(key).getStoichiometryValue()+" "+ metabolites_names.get(key) +" ("+ comp)+") + ";
							}

							equation=equation.substring(0, equation.lastIndexOf("+")-1);
							double lowerBound = 0.0;
							double upperBound = 9999.0;
							if(isReversible) {
								equation=equation.concat(" <=> ");
								lowerBound = -9999.0;
							}
							else
								equation=equation.concat(" => ");

							for(String key :products.keySet()) {

								String comp = "in";

								if(products.get(key).getCompartmentId().equals("e"))
									comp = "out";

								equation=equation.concat(products.get(key).getStoichiometryValue()+" "+ metabolites_names.get(key) +" ("+ comp)+") + ";
							}
							equation=equation.substring(0, equation.lastIndexOf("+")-1);

							boolean		ontology = false;
							
							Integer idReaction = null;
							
							List<Integer> reactionsRes = ModelReactionsServices.getReactionID(reactionName, this.workspaceName);
							
							if(reactionsRes != null && !reactionsRes.isEmpty())
								idReaction = reactionsRes.get(0);
							else {
								
								String notes = "";

								if(ontology) {

									notes="ontology";
									reactionInModel=false; // verificar esta flag
								}
								
								ReactionContainer reactionContainer = new ReactionContainer(reactionName, equation, SourceType.TRANSPORTERS.toString(), reactionInModel, false, false, false);
								
								reactionContainer.setNotes(notes);
								reactionContainer.setGeneRule(geneRule.replace("\\(", "").replace("\\)", ""));
//								if(compartmentID != null)
//									reactionContainer.setLocalisation(compartmentID);
								reactionContainer.setLowerBound(lowerBound);
								reactionContainer.setUpperBound(upperBound);
								reactionContainer.setInModel(true);
								reactionContainer.setSource(SourceType.TRANSPORTERS);

								
								idReaction = ModelReactionsServices.insertNewReaction(this.workspaceName, reactionContainer);
								
							}

							boolean exists = ModelPathwaysServices.checkPathwayHasReactionData(this.workspaceName, Integer.valueOf(idPathway), idReaction);
							
							if(!exists)
								ModelPathwaysServices.insertModelPathwayHasModelReaction(this.workspaceName, Integer.valueOf(idPathway), idReaction);

							for(String metabolite : reactants.keySet()) {

								String compartment;
								if (reactants.get(metabolite).getCompartmentId().equalsIgnoreCase("in") || reactants.get(metabolite).getCompartmentId().equalsIgnoreCase("c"))
									compartment = "inside";
								else
									compartment = "outside";

								Integer idCompartment = this.getCompartmentsID(compartment, compartments_ids);
								
								double reactants_stoichiometry = -1*reactants.get(metabolite).getStoichiometryValue();
								ModelStoichiometryServices.insertStoichiometry(this.workspaceName, idReaction, Integer.valueOf(this.metabolites_ids.get(metabolite)), idCompartment, reactants_stoichiometry);
								
							}

							for(String metabolite : products.keySet()) {

								String compartment;
								if (products.get(metabolite).getCompartmentId().equalsIgnoreCase("in") || products.get(metabolite).getCompartmentId().equalsIgnoreCase("c"))
									compartment = "inside";
								else
									compartment = "outside";

								Integer idCompartment = this.getCompartmentsID(compartment, compartments_ids);

								ModelStoichiometryServices.insertStoichiometry(this.workspaceName, idReaction, Integer.valueOf(this.metabolites_ids.get(metabolite)), idCompartment, products.get(metabolite).getStoichiometryValue());
							}

//							for(String sequence_id : reaction.getGenesIDs()) {
//
//								geneDatabaseIDs = TransportersAPI.getGenesDatabaseIDs(sequence_id, geneDatabaseIDs, statement);
//								String idDatabaseGene = geneDatabaseIDs.get(sequence_id);
//
////								if(idDatabaseGene != null) {
////
////									Set<String> tcNumbers = new HashSet<String>();
////
////									this.genes_protein_ids.get(sequence_id).calculateTCfamily_score();
////
////									if(this.genes_protein_ids.get(sequence_id).getTc_families_above_half() == null || this.genes_protein_ids.get(sequence_id).getTc_families_above_half().isEmpty())
////										tcNumbers.add(this.genes_protein_ids.get(sequence_id).getMax_score_family());
////									else
////										tcNumbers = this.genes_protein_ids.get(sequence_id).getTc_families_above_half().keySet();
////
////									for(String tcNumber : tcNumbers)  {
////
////										if(reaction.getProteinIds().contains(tcNumber)) {
////
////											String idProtein = TransportersAPI.addProteinIDs(tcNumber, reactionID, statement);
////
////											TransportersAPI.addSubunit(idProtein, tcNumber, idDatabaseGene, statement);
////
////											TransportersAPI.addReaction_has_Enzyme(idProtein, tcNumber, idReaction, statement);
////
////											TransportersAPI.addPathway_has_Enzyme(idProtein, tcNumber, idPathway, statement);
////										}
////									}
////								}
//							}
						}
						else {

							logger.debug("Could not integrate reaction {} {}",reactionName, ContainerUtils.getReactionToString(reaction));
						}
					} 
					catch (Exception e) { 

						e.printStackTrace();
					}
				}
				this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), counter, reactions.keySet().size(), "integrating transporters...");
				counter++;
			}
			return true;

		}
		catch (Exception e1) {

			e1.printStackTrace();
			return false;
		}
	}
	

	/**
	 * Get compartment identifier.
	 * 
	 * @param compartment
	 * @param compartments_ids
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	private Integer getCompartmentsID(String compartment, Map<String, String> compartments_ids) throws Exception {

		Integer idcompartment = null;

		if(compartments_ids.containsKey(compartment)) {

			idcompartment = Integer.valueOf(compartments_ids.get(compartment));
		}
		else {

			CompartmentContainer container = ModelCompartmentServices.getCompartmentByName(this.workspaceName, compartment);
			
			if(container == null) {

				String abb = compartment;

				if(compartment.length()>3) {

					abb = compartment.substring(0, 3);
				}

				idcompartment = ModelCompartmentServices.insertNameAndAbbreviation(this.workspaceName, compartment, abb);
			}
			else
				idcompartment = container.getCompartmentID();

			compartments_ids.put(compartment, idcompartment.toString());

		}
		return idcompartment;
	}


	/**
	 * Build the boolean rule based on merlin's gene IDs
	 * 
	 * @param reactionCi
	 * @param allGenes
	 * @return
	 */
	private String getGeneRuleWithGeneIDs(ReactionCI reactionCi, Map<Integer, String> allLocustoGeneID) {

		String transytGeneRule = reactionCi.getGeneRuleString();
		
		if(transytGeneRule != null) {
			
			Set<String> genes = reactionCi.getGenesIDs();

			for(String gene : genes) {
				
				Integer geneAux = Integer.valueOf(gene.replace("G_" , ""));

				if(allLocustoGeneID.containsKey(geneAux))
					transytGeneRule = transytGeneRule.replaceAll(gene, geneAux.toString());
				else {
					transytGeneRule = "";
					break;
				}
			}
		}	
		
//		System.out.println(transytGeneRule);

		return transytGeneRule;
	}


	
	
	/**
	 * 
	 */
	public void setCancel() {

		this.cancel = new AtomicBoolean(true);
	}

	/**
	 * @param progress
	 */
	public void setTimeLeftProgress(TimeLeftProgress progress) {

		this.progress = progress;
	}


	/**
	 * @return
	 */
	public AtomicBoolean isCancel() {

		return this.cancel;
	}

}
