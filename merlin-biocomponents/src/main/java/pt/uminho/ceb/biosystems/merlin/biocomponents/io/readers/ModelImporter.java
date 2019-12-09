package pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.features.Qualifier;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.modelSeed.ModelSeedCompoundsDB;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.modelSeed.ModelSeedPathwaysDB;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.modelSeed.ModelSeedReactionsDB;
import pt.uminho.ceb.biosystems.merlin.biocomponents.io.Enumerators.ModelSources;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwaysHierarchyContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceData;
import pt.uminho.ceb.biosystems.merlin.utilities.RulesParser;
import pt.uminho.ceb.biosystems.merlin.utilities.SbmlUtilities;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.CompartmentCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.GeneCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.MetaboliteCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionTypeEnum;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.StoichiometryValueCI;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTreeNode;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.DataTypeEnum;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.IValue;


public class ModelImporter {

	private MerlinSBMLContainer cont;
	private Map<String, List<ProteinContainer>> enzymeReactionExternalIdentifier;
	private ModelSeedReactionsDB reactionsData;
	private ModelSeedPathwaysDB keggPathwaysData;
	private ModelSeedCompoundsDB metabolitesData;
	private Map<String, String> metaboliteCompartments;
	private List<String> transportReactions;
	private List<String> drains;
	private List<String> biomassPathway;
	private ModelSources modelSource;
	private Set<String> metabolitesSet, reactionsSet;
	private Map<String,String> compartments;
//	private Map<String,Set<String>> reactionEnzymes;
	private String biomassReaction;
	private WorkspaceData workspaceData;
	
	private Map<String, String> locusTagsToProteinIdsMap;
	
	final static Logger logger = LoggerFactory.getLogger(ModelImporter.class);


	public ModelImporter(MerlinSBMLContainer container, ModelSources source) throws SQLException{

		workspaceData = new WorkspaceData();
		this.cont = container;
		this.modelSource = source;
		
		this.enzymeReactionExternalIdentifier = new HashMap<>();
	//	this.reactionEnzymes = new HashMap<>();
		
		if(this.modelSource.equals(ModelSources.MODEL_SEED)){
			this.metabolitesData = new ModelSeedCompoundsDB();
			this.reactionsData = new ModelSeedReactionsDB();
		}	
		
		this.keggPathwaysData = new ModelSeedPathwaysDB();
		this.metaboliteCompartments = new HashMap<>();
		this.metabolitesSet = new HashSet<>();
		this.reactionsSet = new HashSet<>();
		this.compartments = new HashMap<>();
		
		this.processBiomass();
		
		this.transportReactions = new ArrayList<>(cont.getReactionsByType(ReactionTypeEnum.Transport));
		this.drains = new ArrayList<>(cont.getReactionsByType(ReactionTypeEnum.Drain));
		
		this.locusTagsToProteinIdsMap = new HashMap<>();
		
		logger.info("Reading compartments...");
		readCompartments();
		logger.info("Reading metabolites...");
		readMetabolites();
		logger.info("Reading pathways...");
		readPathways();
		logger.info("Reading genes...");
		readGenes();
		logger.info("Reading reactions...");
		readReactions();
		logger.info("Reading enzymes...");
		readEnzymes();
		
	}
	
	/**
	 * 
	 */
	private void processBiomass(){
		
		this.biomassReaction = cont.getBiomassId();
		
		if(biomassReaction!=null && !biomassReaction.isEmpty())
			if(!cont.getReaction(biomassReaction).getType().equals(ReactionTypeEnum.Biomass)
					&& biomassReaction.toLowerCase().contains("biomass"))
				cont.getReaction(biomassReaction).setType(ReactionTypeEnum.Biomass);
		
		this.biomassPathway = new ArrayList<>(cont.getReactionsByType(ReactionTypeEnum.Biomass));
	}

	/**
	 * 
	 */
	private void readGenes(){

		//		GENE CONTAINER
		//		private String entry;	X
		//		private String name;	X
		//		private	List<String> dblinks;
		//		private List<String> orthologues;
		//		private	List<String> genes;
		//		private	List<String> modules;
		//		private String chromosome_name;
		//		private String  left_end_position, right_end_position, aasequence, aalength, ntsequence, ntlength;

		Map<String, GeneCI> genes = cont.getGenes();
		
		if(!genes.isEmpty()){

			for(GeneCI gene : genes.values()){

				String geneID = gene.getGeneId();
				
//				System.out.println("GeneID: "+geneID);
				
				if(geneID!=null && !geneID.isEmpty()){
				 
					GeneContainer geneContainer = new GeneContainer(geneID);
					if(gene.getGeneName()!=null)
						geneContainer.setName(gene.getGeneName());
					
					this.workspaceData.getResultGenes().add(geneContainer);
				}
			}
		}
	}


	/**
	 * 
	 */
	private void readMetabolites(){

		//		METABOLITE CONTAINER
		//		private int metaboliteID;
		//		private String name;	X
		//		private String formula;	X
		//		private String stoichiometric_coefficient;
		//		private String numberofchains;
		//		private String compartment_name;	X
		//		private String entryID;
		//		private String molecular_weight;	X
		//		private List<String> names;
		//		private List<String> enzymes;
		//		private List<String> reactions;	X
		//		private Map<String, String> pathways;
		//		private List<String> dblinks;
		//		private List<String> same_as;

		Map<String, MetaboliteCI> metabolites = cont.getMetabolites();
		
		boolean saveDrains = true;
		if(this.drains!=null && !this.drains.isEmpty())
			saveDrains = false;
		
		
//		Set<String> coreMetabolites = new HashSet<>();
//		if(this.metabolitesData!=null)
//			coreMetabolites = this.metabolitesData.getCoreCompounds();
		
		for(String metID : metabolites.keySet()){
			
			if(!metID.endsWith("_b")){

				MetaboliteCI metabolite = metabolites.get(metID);
				
				String metaboliteID = SbmlUtilities.processSBMLMetaboliteIdentifier(metID);//.split("_")[1];
				
				String metaboliteCompartmentID = cont.getMetaboliteCompartments(metID).toArray()[0].toString();
				String compartmentName = this.compartments.get(metaboliteCompartmentID);
//				if(metaboliteCompartmentID!=null && !metaboliteCompartmentID.isEmpty())
//					compartmentName = this.compartments.get(metaboliteCompartmentID);
				
				this.metaboliteCompartments.put(metID, compartmentName);


				if(!this.metabolitesSet.contains(metaboliteID)){
					
					MetaboliteContainer metContainer = new MetaboliteContainer(metaboliteID);
					
					//METABOLITE COMPARTMENT
					metContainer.setCompartment_name(compartmentName);

					//METABOLITE NAME, FORMULA AND MOLECULAR WEIGHT
					if(this.metabolitesData!=null && this.metabolitesData.existsCompoundID(metaboliteID)){
						
						metContainer.setName(this.metabolitesData.getCompoundName(metaboliteID));
						metContainer.setFormula(this.metabolitesData.getCompoundFormula(metaboliteID));
						metContainer.setMolecular_weight(this.metabolitesData.getCompoundMolecularWeight(metaboliteID));
						
//						coreMetabolites.remove(metaboliteID);
					}
					else{
						metContainer.setName(metabolite.getName());
						metContainer.setFormula(metabolite.getFormula());
						Double mass = metabolite.getMass();
						if(mass != null && mass!=0)
							metContainer.setMolecular_weight(mass.toString());

					}

					//METABOLITE REACTIONS
					Set<String> metaboliteReactions = metabolite.getReactionsId();
					List<String> reactionsList = new ArrayList<>(metaboliteReactions);
					metContainer.setReactions(reactionsList);

					//METABOLITE SYMNONYMS
					if(metabolite.getSymnonyms() != null)
						metContainer.setSame_as(metabolite.getSymnonyms());
					
					this.metabolitesSet.add(metaboliteID);
					this.workspaceData.getResultMetabolites().add(metContainer);
				}
			}
			else if(saveDrains){
				Set<String> reactionsList = cont.getMetabolite(metID).getReactionsId();
				
				for(String drainID : reactionsList)
					if(!this.drains.contains(drainID))
						this.drains.add(drainID);
			}
		}
	}

	/**
	 * 
	 */
	private void readReactions(){

		//		REACTION CONTAINER
		//		private String entryID;		X
		//		private String reactionID;	X
		//		private boolean reversible, inModel;	X	X
		//		private Double lowerBound, upperBound;	X	X
		//		private String name, localisation, notes;	X	X	X
		//		private List<String> names;
		//		private List<String> dblinks;
		//		private String equation;	X
		//		private	Map<String, String[]> reactantsStoichiometry;	X
		//		private	Map<String, String[]> productsStoichiometry;	X
		//		private Set<String> enzymes, comments, genes, pathways;   X  -  X    X
		//		private	Map<String, String> pathwaysMap; 	-
		//		private String geneRule;  X

		Map<String, ReactionCI> reactions = cont.getReactions();
		
		for(String idReaction : reactions.keySet()){
			
			String reactionID = SbmlUtilities.processSBMLReactionIdentifier(idReaction);
			
			ReactionCI reaction = reactions.get(idReaction);
			
			Map <String, StoichiometryValueCI> reactants = reaction.getReactants();
			Map <String, StoichiometryValueCI> products = reaction.getProducts();
			
			if((reactants==null || reactants.isEmpty()) && !this.drains.contains(idReaction))
				this.drains.add(idReaction);
			else if((products==null || products.isEmpty()) && !this.drains.contains(idReaction))
				this.drains.add(idReaction);
			
			String compSuffix = "";
			
			if(reactants!=null && !reactants.isEmpty() && reactants.get(reactants.keySet().iterator().next()).getCompartmentId()!=null 
					&& !reactants.get(reactants.keySet().iterator().next()).getCompartmentId().isEmpty()){
				
				compSuffix = compSuffix.concat("_").concat(reactants.get(reactants.keySet().iterator().next()).getCompartmentId());
			}
			
			else if(products!=null && !products.isEmpty() && products.get(products.keySet().iterator().next()).getCompartmentId()!=null 
					&& !products.get(products.keySet().iterator().next()).getCompartmentId().isEmpty()){
				
				compSuffix = compSuffix.concat("_").concat(products.get(products.keySet().iterator().next()).getCompartmentId());
			}
			
			else if(!reaction.identifyCompartments().isEmpty() && reaction.identifyCompartments()!=null){
				
				compSuffix = compSuffix.concat("_").concat(reaction.identifyCompartments().toArray()[0].toString());
			}

			String reactionIDcompartment = reactionID.concat(compSuffix);
			
			int i=1;
			while(this.reactionsSet.contains(reactionIDcompartment)){
				reactionIDcompartment = reactionIDcompartment.concat("_copy")+i;
				i++;
			}
			
			ReactionContainer reactionContainer = new ReactionContainer(reactionIDcompartment);
			
			//REACTION COMPARTMENT
			
			if(cont.getCompartment(compSuffix.substring(1)).getName().split("_")[0].isEmpty()) {
				reactionContainer.setLocalisation(compSuffix.substring(1));
			}
			
			else if(!compSuffix.isEmpty() && !cont.getCompartment(compSuffix.substring(1)).getName().split("_")[0].isEmpty()) {
				reactionContainer.setLocalisation(cont.getCompartment(compSuffix.substring(1)).getName().split("_")[0]);
			}

			String externalID = "";
			
			if(this.reactionsData!=null && this.reactionsData.existsReactionID(reactionID)){
				
				if(this.reactionsData.getReactionAbbreviation(reactionID).contains("_"))
					externalID = this.reactionsData.getReactionAbbreviation(reactionID).split("_")[1];
				else
					externalID = this.reactionsData.getReactionAbbreviation(reactionID);
				
				if(externalID.matches("R\\d{5}")){
					
//					try {
//						Integer.parseInt(externalID.substring(1));	//verify we have an ID number with KeggID format ("R"+ numbers)
					reactionIDcompartment = externalID.concat(compSuffix);
					
					i=1;
					while(this.reactionsSet.contains(reactionIDcompartment)){
						reactionIDcompartment = reactionIDcompartment.concat("_copy")+i;
						i++;
					}
					
					reactionContainer.setExternalIdentifier(reactionIDcompartment);
//					} 
//					catch (NumberFormatException e) {
//					}
				}
					
				
				if(reaction.isReversible() == reactionsData.isReactionReversible(reactionID)){
					
//					String splitChar = "";
//					if(!reactionsData.isReactionReversible(reactionID))
//						splitChar = reactionsData.getReactionDirection(reactionID);
//					else
//						splitChar = "<=>";
					
					String equation = this.reactionsData.getReactionEquation(reactionID);//.replaceAll("[\\[\\(]\\s*\\d*\\s*[\\]\\)]", "").trim();
					
//					if(equation.trim().split(splitChar).length == 2)
//						reactionContainer.setEquation(equation);
					if(!equation.isEmpty())
						reactionContainer.setEquation(equation);
				}
			}
			
			this.reactionsSet.add(reactionIDcompartment);
			
			//EQUATION
			if(reactionContainer.getEquation() == null) {
				
				String direction = null;

				if(reaction.isReversible())
					direction = " <=> ";
				else
					direction = " => ";

				String equation = "";
				Map<String, MetaboliteCI> metabolites = cont.getMetabolites();

				if(!reactants.isEmpty() && !products.isEmpty()){
					for(String reactantID : reactants.keySet()){

						String reactant = metabolites.get(reactantID).getName();
						equation = equation.concat(reactant).concat(" + ");
					}

					equation = equation.substring(0, equation.lastIndexOf(" + "));
					equation = equation.concat(direction);

					for(String productID : products.keySet()){

						String product = metabolites.get(productID).getName();
						equation = equation.concat(product).concat(" + ");
					}

					equation = equation.substring(0, equation.lastIndexOf(" + "));
					equation.replaceAll("[\\[\\(]\\s*\\d*\\s*[\\]\\)]", "").trim();
					reactionContainer.setEquation(equation);
				}
				else{
					reactionContainer.setEquation(reaction.getName());
				}
			}
			
			//reactionContainer.setReactionID(Integer.parseInt(reactionID));
			reactionContainer.setExternalIdentifier(reactionID);
			for(String geneID : reaction.getGenesIDs())
				reactionContainer.addGene(geneID, cont.getGene(geneID).getGeneName());
//			for(String protein : reaction.getProteinIds())
//				reactionInformationContainer.addProteins(protein);
			reactionContainer.setInModel(true);
			
//			GENES RULES
			if(reaction.getGeneRule()!=null && reaction.getGeneRule().size()!=0){
				
				AbstractSyntaxTreeNode<DataTypeEnum, IValue> treeRoot = reaction.getGeneRule().getRootNode();
				List<String> geneCombinations = RulesParser.getGeneRuleCombinations(treeRoot);
				
//				String geneRule = RulesParser.getGeneRuleString(geneCombinations, this.genesIds);
				String geneRule = RulesParser.getOR_geneRulesList2String(geneCombinations);
				
				reactionContainer.setGeneRule(geneRule);
			}
			
			//ENZYMES
//			String name = reaction.getName();
			if(reaction.getEcNumbers() != null) {
				for(String protein : reaction.getEcNumbers())
					reactionContainer.addProteins(protein);
			}
	
			this.enzymeReactionExternalIdentifier.put(reactionContainer.getExternalIdentifier(), reactionContainer.getEnzymes());
//			this.reactionEnzymes.put(reactionID,enzymes);

			//BOUNDS
			if(cont.getDefaultEC().containsKey(idReaction)){
				reactionContainer.setLowerBound(cont.getDefaultEC().get(idReaction).getLowerLimit());
				reactionContainer.setUpperBound(cont.getDefaultEC().get(idReaction).getUpperLimit());
			}
			
			for(String reactantID : reactants.keySet()){
				
				if(!reactantID.endsWith("_b")){
				
	//				String kBaseReactantID = reactantID.split("_")[1];
					String[] stoichiometryValue = new String[3];
					Double stoichCoef = reactants.get(reactantID).getStoichiometryValue();
					if(stoichCoef>0)
						stoichCoef = -stoichCoef;
					stoichiometryValue[0] = Double.toString(stoichCoef);
					stoichiometryValue[2] = this.metaboliteCompartments.get(reactantID);
					
					//verify if compartments of metabolites in reaction matches
					String compID = null;
					for(String component : reactantID.split("_"))
						if(this.compartments.keySet().contains(component))
							compID=component;
					
					if(compID!=null && !compID.isEmpty()){
						
						String compartmentName = this.compartments.get(compID);
//						if(compartmentName.contains("_"))
//							compartmentName = compartmentName.split("_")[0];
						
						if(!compartmentName.equals(this.metaboliteCompartments.get(reactantID))){
							if(reactionContainer.getNotes()==null || reactionContainer.getNotes().isEmpty())
								reactionContainer.setNotes("verify the reactants compartments for this reaction");
						}
					}
					
					String id = SbmlUtilities.processSBMLMetaboliteIdentifier(reactantID);
					
						
					reactionContainer.addReactant(id, Double.parseDouble(stoichiometryValue[0]), stoichiometryValue[2]);
				}
			}

			//PRODUCTS STOICHIOMETRY
			for(String productID : products.keySet()){
				
				if(!productID.endsWith("_b")){
				
	//				String kBaseProductID = productID.split("_")[1];
					String[] stoichiometryValue = new String[3];
					Double stoichCoef = products.get(productID).getStoichiometryValue();
					if(stoichCoef<0)
						stoichCoef = -stoichCoef;
					stoichiometryValue[0] = Double.toString(stoichCoef);
					stoichiometryValue[2] = this.metaboliteCompartments.get(productID);
					
					//verify if compartments of metabolites in reaction matches
//					String compID = productID.split("_")[2];
					String compID = null;
					for(String component : productID.split("_"))
						if(this.compartments.keySet().contains(component))
							compID=component;
					
					if(compID!=null && !compID.isEmpty()){
						
						String compartmentName = this.compartments.get(compID);
						
//						if(compartmentName.contains("_"))
//							compartmentName = compartmentName.split("_")[0];
						
						if(!compartmentName.equals(this.metaboliteCompartments.get(productID))){
							if(reactionContainer.getNotes()==null || reactionContainer.getNotes().isEmpty())
								reactionContainer.setNotes("verify products compartments for this reaction");
						}
					}
					
					String id = SbmlUtilities.processSBMLMetaboliteIdentifier(productID);
					
					reactionContainer.addProduct(id, Double.parseDouble(stoichiometryValue[0]), stoichiometryValue[2]);
				}
			}
			
			
			//PATHWAYS
			Set<String> pathways = new HashSet<>();
			
			if(idReaction.equals(this.biomassReaction) || this.biomassPathway.contains(idReaction)){
				
				pathways.add("B0001");
				reactionContainer.setPathways(pathways);
				
				reactionContainer.addPathway("B0001", "Biomass pathway");
			}
			
			else if(this.drains.contains(idReaction)){
				
				pathways.add("D0001");
				reactionContainer.setPathways(pathways);
				
				reactionContainer.addPathway("D0001", "Drains pathway");
			}
			
			else if(this.transportReactions.contains(idReaction)){
				
				pathways.add("T0001");
				reactionContainer.setPathways(pathways);
				
				reactionContainer.addPathway("T0001", "Transporters pathway");
			}
				
			else if(keggPathwaysData.existsReactionIDinKeggPathway(externalID)){
				
				pathways.addAll(keggPathwaysData.getReactionPathways(externalID));
				reactionContainer.setPathways(pathways);
				
				for(String pathway : pathways)
					reactionContainer.addPathway(pathway, keggPathwaysData.getPathwayName(pathway));

			}

			this.workspaceData.getResultReactions().add(reactionContainer);
		}
	}
	

	/**
	 * 
	 */
	public void readEnzymes(){

		//		ENZYME CONTAINER
		//		private String entry;	X
		//		private String name;	X
		//		private List<String> names;	X
		//		private	List<String> dblinks;
		//		private String enzyme_class;	X
		//		private List<String> orthologues;
		//		private	List<String> cofactors;
		//		private List<String> reactions;	X
		//		private	Map<String, String> pathways;
		//		private	List<String> genes;

		Map<String, Set<String>> ecNumbers = cont.getECNumbers();
		
		for(String ecNumber : ecNumbers.keySet()){
			
			ProteinContainer enzymeContainer = new ProteinContainer(ecNumber);
			
			Set<String> reactionsSet = ecNumbers.get(ecNumber);
			
			for(String reactionExternalIdentifier : reactionsSet) {
				
				if(reactionExternalIdentifier.contains("_")){
					
//					String[] reacNameSplit = reaction.toLowerCase().split("_");
					String[] reacNameSplit = reactionExternalIdentifier.split("_");
					
					if(reactionExternalIdentifier.startsWith("R"))
						reactionExternalIdentifier = reacNameSplit[1];
					else
						reactionExternalIdentifier = reacNameSplit[0].concat("_").concat(reacNameSplit[1]);
				}
				
				
//				String name = "";
//				
//				if(this.reactionsData!=null && this.reactionsData.existsReactionID(reactionID))
//					name = this.reactionsData.getEnzymeName(reactionID);
//				else if(this.enzymeReactionExternalIdentifier.containsKey(reactionID))
//					name = this.enzymeReactionExternalIdentifier.get(reactionID);
//				
//				if(name.length()>10)
//					names.add(name);
				
				enzymeContainer.addReactionExternalIdentifier(reactionExternalIdentifier);
				
				
			}
			
//			for(String reactionExternalIdentifier : reactionsExternalIdentifiers)
//				enzymeContainer.setReactions(reactionsExternalIdentifiers);
//
//			if(names.size()==1){
//				enzymeContainer.setName(names.get(0));
//				enzymeContainer.setNames(names);
//			}
//			else
//				enzymeContainer.setNames(names);
			
			enzymeContainer.setClass_(ecNumber);

			if(enzymeContainer.getExternalIdentifier() != null)
				this.workspaceData.getResultEnzymes().add(enzymeContainer);
		}
	}
	
	
	/**
	 * 
	 */
	public void readPathways() {
		
//		PATHWAYS HIERARCHY CONTAINER
//		private Map<String, List<String[]>> pathways_hierarchy;	X
//		private String super_pathway;	X
		
		Map<String , List<String>> superPathways = keggPathwaysData.getSuperPathways();
		
		for(String superPathway : superPathways.keySet()){
			
			PathwaysHierarchyContainer pathwayHierarchyContainer = new PathwaysHierarchyContainer(superPathway);
			
			List<String> intermediaryPathways = superPathways.get(superPathway);
			Map<String, List<String[]>> allPathwaysHierarchy = keggPathwaysData.getPathwaysHierarchy();
			
			Map<String, List<String[]>> pathwaysHierarchy = new HashMap<>();
			
			for(String intermediaryPathway : intermediaryPathways){
				
				List<String[]> pathways = allPathwaysHierarchy.get(intermediaryPathway);
				pathwaysHierarchy.put(intermediaryPathway, pathways);
			}
			
			pathwayHierarchyContainer.setPathwaysHierarchy(pathwaysHierarchy);
			
			this.workspaceData.getResultPathwaysHierarchy().add(pathwayHierarchyContainer);
		}
	}


	/**
	 * 
	 */
	public void readCompartments(){

		//		COMPARTMENT CONTAINER
		//		private String compartmentID;	X
		//		private String name;	X
		//		private String abbreviation;	X

		Map<String, CompartmentCI> compartments = cont.getCompartments();

		for(String compartmentID : compartments.keySet()){
			
			CompartmentCI compartment = compartments.get(compartmentID);
			
			String compartmentName = "";
			
			if(compartment.getName().split("_")[0].isEmpty()) {
				compartmentName = compartmentID;
			}
			
			else {
				compartmentName = compartment.getName().split("_")[0];
			}
			
			String abbreviation = "";
			
			if(compartmentName.length() > 3) {
				abbreviation = compartmentName.substring(3);
			}	
			else {
				abbreviation = compartmentName;

			}
			
			CompartmentContainer compartmentContainer = new CompartmentContainer(compartmentName, abbreviation);

			this.compartments.put(compartmentID, compartmentName);
			this.workspaceData.getResultCompartments().add(compartmentContainer);
		}
	}

	
	
	/**
	 * @return
	 * @throws Exception 
	 */
	public Map<String,String> createProteinIdLocusTagMap(File genBankFile) throws Exception{

		List<String> idsToSearch = new ArrayList<>(this.cont.getGenes().keySet()); 

		Map<String, String> proteinIds = getProteiIdFromGenBankFastFile(genBankFile);

		for(String locusTag : idsToSearch){

			if(proteinIds.containsKey(locusTag))
				this.locusTagsToProteinIdsMap.put(locusTag, proteinIds.get(locusTag));

		}

		return this.locusTagsToProteinIdsMap;
	}


	/**
	 * @param genBankFile
	 * @return
	 * @throws Exception
	 */
	private static Map<String, String> getProteiIdFromGenBankFastFile(File genBankFile) throws Exception {

		Map<String, String> proteinIDs = new HashMap<>();

		try {
			LinkedHashMap<String, DNASequence> genBankReader = GenbankReaderHelper.readGenbankDNASequence(genBankFile);

			for(DNASequence cds : genBankReader.values()) {
				for (FeatureInterface<AbstractSequence<NucleotideCompound>, NucleotideCompound> cdsFeature : cds.getFeatures()) {
					if (cdsFeature.getType().equals("CDS")){   
						Map<String, List<Qualifier>> qualifiers = cdsFeature.getQualifiers();
						List<Qualifier> old_locus_tag = qualifiers.get("old_locus_tag");
						List<Qualifier> locus_tag = qualifiers.get("locus_tag");
						List<Qualifier> protein_id = qualifiers.get("protein_id");

						if(protein_id!=null){

							if(locus_tag != null)
								proteinIDs.put(locus_tag.get(0).getValue(), protein_id.get(0).getValue());

							if(old_locus_tag != null)
								proteinIDs.put(old_locus_tag.get(0).getValue(), protein_id.get(0).getValue());

						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return proteinIDs;
	}

	/**
	 * @return the workspaceData
	 */
	public WorkspaceData getWorkspaceData() {
		return workspaceData;
	}

	/**
	 * @param workspaceData the workspaceData to set
	 */
	public void setWorkspaceData(WorkspaceData workspaceData) {
		this.workspaceData = workspaceData;
	}

}
