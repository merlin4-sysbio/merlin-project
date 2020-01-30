/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.biocomponents.container.ContainerUtils;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelCompartmentServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelPathwaysServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.merlin.utilities.RulesParser;
import pt.uminho.ceb.biosystems.merlin.utilities.Utilities;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.CompartmentCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.GeneCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.InvalidBooleanRuleException;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.MetaboliteCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionConstraintCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionTypeEnum;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.StoichiometryValueCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.interfaces.IContainerBuilder;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.map.indexedhashmap.IndexedHashMap;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * @author Oscar
 *
 */
public class ContainerBuilder implements IContainerBuilder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ContainerBuilder.class);

	private static final String GENE_RULE_PREFIX = "GENE_ASSOCIATION: ";
	private static final String PROTEIN_RULE_PREFIX = "PROTEIN_ASSOCIATION: ";
	private static final String PROTEIN_CLASS_PREFIX = "PROTEIN_CLASS: ";
	private static final String SUBSYSTEM_PREFIX = "SUBSYSTEM: ";
	//private static final String FOMULA_PREFIX = "FORMULA: ";
	//private static final String CHARGE_PREFIX = "CHARGE: ";

	private static final boolean concatenate = false;


	private String modelID;
	private String modelName;
	private String notes;
	private int version;
	private IndexedHashMap<String, ReactionConstraintCI> defaultEC;

	private IndexedHashMap<String, MetaboliteCI> compoundsMap;

	private IndexedHashMap<String, ReactionCI> reactionsMap;

	private IndexedHashMap<String, CompartmentCI> compartmentsMap;

	private Map<String, GeneCI> genes;

	// Extra info
	private Map<String, Map<String, String>> metabolitesExtraInfo;
	private Map<String, Map<String, String>> reactionsExtraInfo;

	private Map<Integer, List<MetaboliteContainer>> reactionMetabolites;
	private Map<Integer, ReactionContainer> reactions;

	private Map<String, CompartmentContainer> compartments;
	private Map<String, String> compartmentID;
	private int compartmentCounter;

	private String externalCompartmentID;
	private String organismName;
	private String biomassID;

	private String biomassName;
	private String databaseName;

	/**
	 * @param databaseConnector
	 * @param modelName
	 * @param isCompartmentalizedModel
	 * @param reactionsExtraInfoRequired
	 * @param organismName
	 * @param biomassName
	 * @throws Exception 
	 */
	public ContainerBuilder(String databasename,
			String modelName, boolean isCompartmentalizedModel, boolean reactionsExtraInfoRequired, String organismName, String biomassName) throws Exception  {
		this.databaseName = databasename;
		this.biomassName = biomassName;
		this.setModelID(modelID);
		this.modelName = modelName;
		this.organismName = organismName;
		this.notes = "";
		this.version = 0;
		this.compoundsMap = new IndexedHashMap<>();
		this.reactionsMap = new IndexedHashMap<>();
		this.compartmentsMap = new IndexedHashMap<>();
		this.metabolitesExtraInfo = new IndexedHashMap<>();
		this.reactionsExtraInfo	= new IndexedHashMap<>();
		this.defaultEC = new IndexedHashMap<>();
		this.reactions=new HashMap<>();
		this.reactionMetabolites=new HashMap<>();
		this.compartments=new HashMap<>();
		this.compartmentID=new HashMap<>();
		this.compartmentCounter = 1;
		populateInformation(isCompartmentalizedModel, reactionsExtraInfoRequired);
	}


	/**
	/**
	 * @param isCompartmentalizedModel
	 * @param reactionsExtraInfoRequired
	 * @throws Exception 
	 */
	private void populateInformation(boolean isCompartmentalizedModel, boolean reactionsExtraInfoRequired) throws Exception  {

		this.getReactions(isCompartmentalizedModel);
		this.getStoichiometry(isCompartmentalizedModel);
		this.getCompartments(isCompartmentalizedModel);
		this.buildModel(reactionsExtraInfoRequired);
		
	}


	/**
	 * @param link
	 * @param conditions
	 * @throws Exception 
	 * @throws IOException 
	 */
	private void getReactions(boolean isCompartmentalizedModel) throws Exception {


		Map<Integer, ReactionContainer> result = ModelReactionsServices.getReactionsByReactionId(databaseName, isCompartmentalizedModel);
		this.reactions.putAll(result);

		List<ProteinContainer> result2 = ModelProteinsServices.getEnzymeHasReaction(databaseName);
		ProteinContainer enzymeContainer;

		for(int i=0; i<result2.size(); i++) {

			enzymeContainer = result2.get(i);	

			for(int reactionIdentifier : enzymeContainer.getReactionsIdentifier()) {
				
				if(this.reactions.containsKey(reactionIdentifier) && enzymeContainer.getExternalIdentifier() != null && !enzymeContainer.getExternalIdentifier().contains(".-")) {

					ReactionContainer reactionContainer = this.reactions.get(reactionIdentifier);

					List<ProteinContainer> enzymeSet = new ArrayList<>();
					if(reactionContainer.getEnzymes()!=null) {

						enzymeSet = reactionContainer.getEnzymes();
					}

					if(enzymeContainer.getExternalIdentifier()!=null) {

						enzymeSet.add(enzymeContainer);
						reactionContainer.setEnzymes(enzymeSet);
					}
					reactionContainer = this.reactions.put(reactionIdentifier, reactionContainer);
				}
			}
		}

		Map<Integer, List<PathwayContainer>> map = ModelPathwaysServices.getPathwaysByReaction(this.databaseName);

		for(Integer reactionId : map.keySet()) {

			if(this.reactions.containsKey(reactionId)) {

				ReactionContainer reactionContainer = this.reactions.get(reactionId);

				for(PathwayContainer pathway : map.get(reactionId))
					reactionContainer.addPathway(pathway);

				reactionContainer = this.reactions.put(reactionId, reactionContainer);
			}
		}

		List<String[]> result3 = ModelReactionsServices.getReactionGenes(databaseName);

		for(int i=0; i<result3.size(); i++) {

			String[] list2 = result3.get(i);			

			if(this.reactions.containsKey(Integer.parseInt(list2[0])) && !list2[3].contains(".-")) {

				ReactionContainer reactionContainer = this.reactions.get(Integer.parseInt(list2[0]));

				String locus= list2[2], geneName = null;

				if(list2[1]!=null)
					geneName = list2[1].replace(" ","").replace(",","_").replace("/","_").replace("\\","_").trim();

				this.addGeneCI(locus, geneName);
				reactionContainer.addGene(locus, geneName);
				reactionContainer = this.reactions.put(Integer.parseInt(list2[0]), reactionContainer);
			}
		}

	}

	/**
	 * @param locus
	 * @param geneName
	 */
	private void addGeneCI(String locus, String geneName) {

		if(this.genes==null)
			this.genes = new IndexedHashMap<>();

		if(!this.genes.containsKey(locus))
			this.genes.put(locus, new GeneCI(locus, geneName));
	}


	/**
	 * @param link
	 * @param conditions
	 * @throws Exception 
	 */
	private void getStoichiometry(boolean isCompartmentalized) throws Exception {

		List<String[]> result2 = ModelReactionsServices.getStoichiometryInfo(this.databaseName, isCompartmentalized);
		if(result2 != null) {
		
			for(int i=0; i<result2.size(); i++) {
	
				int idreaction = Integer.parseInt(result2.get(i)[1]);
				int idMetabolite = Integer.parseInt(result2.get(i)[2]);
				double stoichiometry = Double.parseDouble(result2.get(i)[3]);
				String metaboliteName = result2.get(i)[4];
				String formula = result2.get(i)[5];
				String metaboliteExternalIdentifier = result2.get(i)[6];
				String metaboliteCompartmentName = result2.get(i)[8];
				int metaboliteIdCompartment = Integer.valueOf(result2.get(i)[9]);
				String metaboliteCompartmentAbbreviation = result2.get(i)[10];
	
				//				System.out.println(idreaction+" "+reactionName+" "+this.reactions.containsKey(idreaction));
	
				if(this.reactions.containsKey(idreaction)) {
	
					//					if(!list2[3].contains("m") && !list2[3].contains("n")) {
	
					List<MetaboliteContainer> metabolitesContainer = new ArrayList<MetaboliteContainer>();
	
					if(this.reactionMetabolites.containsKey(idreaction))
						metabolitesContainer = this.reactionMetabolites.get(idreaction);
	
					MetaboliteContainer metabolite = new MetaboliteContainer(idMetabolite, metaboliteName, formula, 
							stoichiometry,metaboliteCompartmentName, metaboliteIdCompartment, metaboliteCompartmentAbbreviation, metaboliteExternalIdentifier);
					metabolitesContainer.add(metabolite);
	
					this.reactionMetabolites.put(idreaction, metabolitesContainer);
					//					}
					//					else {
					//
					//						this.reactionMetabolites.remove(Integer.parseInt(list2[1]));
					//						this.reactions.remove(Integer.parseInt(list2[1]));
					//					}
				}
			}
		}
	}


	/**
	 * @param stmt
	 * @param isCompartmentalized
	 * @throws Exception 
	 */
	private void getCompartments(boolean isCompartmentalized) throws Exception {


		List<CompartmentContainer> result = ModelCompartmentServices.getCompartmentsInfo(this.databaseName);

		for(CompartmentContainer container : result) {

			this.compartments.put(container.getCompartmentID() + "", container);

			if((container.getName().equalsIgnoreCase("extracellular") && isCompartmentalized) || (container.getName().equalsIgnoreCase("outside") && !isCompartmentalized) 
					|| (container.getAbbreviation().equalsIgnoreCase("e") && isCompartmentalized))
				this.externalCompartmentID=this.getCompartmentID(container.getCompartmentID()+"");
		}
	}

	/**
	 * @param compartmentID
	 * @return
	 */
	private String getCompartmentID(String compartmentID) {

		if(!this.compartmentID.containsKey(compartmentID.toString())) {

			String id = ContainerBuilder.buildID("C_", this.compartmentCounter);

			CompartmentCI c = new CompartmentCI(id, this.compartments.get(compartmentID).getName(), this.externalCompartmentID);
			this.compartmentsMap.put(id, c);
			this.compartmentID.put(compartmentID, id);
			this.compartmentCounter++ ;

		}
		return this.compartmentID.get(compartmentID);
	}

	/**
	 * @param type
	 * @param counter
	 * @return
	 */
	private static String buildID(String type, int counter) {

		if(counter<10 ){return type.concat("0000"+counter);}
		if(counter<100) {return type.concat("000"+counter);}
		if(counter<1000) {return type.concat("00"+counter);}
		if(counter<10000) {return type.concat("0"+counter);}

		return type.concat("_"+counter);
	}

	/**
	 * @param prefix
	 * @param identifier
	 * @param compartment
	 * @return
	 */
	private static String buildID(String prefix, String identifier, String compartment) {

		if(compartment == null)
			System.out.println("null compartment");
		
		String output = identifier.concat("__").concat(compartment.toLowerCase());

		if(!output.startsWith(prefix))
			output = prefix.concat(output);

		output = output.replace("-", "_").replace(":", "_").replace(" ", "_").replace("\t", "_").replace(".", "_").replace("+", "_");
		return output;
	}


	/**
	 * @throws Exception 
	 * @throws InvalidBooleanRuleException 
	 * @throws NumberFormatException 
	 * 
	 */
	private void buildModel(boolean reactionsExtraInfoRequired) throws Exception {

		Map<String,String> compoundCompartmentID = new TreeMap<String, String>();

		for(int reaction_id : this.reactions.keySet()) {

			ReactionContainer reaction = this.reactions.get(reaction_id);
			Map<String, StoichiometryValueCI> reactants = new HashMap<String, StoichiometryValueCI>();
			Map<String, StoichiometryValueCI> products = new HashMap<String, StoichiometryValueCI>();

			//			System.out.println(reaction_id+" "+reaction.getExternalIdentifier()+" ");

			if(this.reactionMetabolites.containsKey(reaction_id)) {

				//				System.out.println("<<<<IN>>>>>");

				MetaboliteCI mci = null;
				for(MetaboliteContainer metabolite : this.reactionMetabolites.get(reaction_id)) {


					String metabolite_surrogate = metabolite.getMetaboliteID()+"".concat("_").concat(metabolite.getCompartment_name());
					String mid;
					if(compoundCompartmentID.containsKey(metabolite_surrogate)) {

						mid = compoundCompartmentID.get(metabolite_surrogate);
						mci = this.compoundsMap.get(mid);
					}
					else {

						String compartmentAbb = this.compartments.get(metabolite.getCompartmentID()+"").getAbbreviation();
//						System.out.println(metabolite.getName());
						mid = ContainerBuilder.buildID("M_", metabolite.getExternalIdentifier(), compartmentAbb);
						compoundCompartmentID.put(metabolite_surrogate,mid);

						if(this.compoundsMap.containsKey(mid)) {

							mci = this.compoundsMap.get(mid);
						}
						else  {

							String containerMetaboliteName="";

							if(metabolite.getExternalIdentifier() != null) {

								containerMetaboliteName=containerMetaboliteName.concat(metabolite.getExternalIdentifier());
							}

							if(metabolite.getName() != null) {

								if(containerMetaboliteName!=null && !containerMetaboliteName.isEmpty()) {

									containerMetaboliteName=containerMetaboliteName.concat("_");
								}
								containerMetaboliteName=containerMetaboliteName.concat(metabolite.getName());
							}

							if(metabolite.getFormula() != null) {

								if(containerMetaboliteName!=null && !containerMetaboliteName.isEmpty()) {

									containerMetaboliteName=containerMetaboliteName.concat("_");
								}
								containerMetaboliteName=containerMetaboliteName.concat(metabolite.getFormula());
							}

							mci = new MetaboliteCI(mid, containerMetaboliteName);

							Map<String, String> value = new HashMap<>();
							value.put("KEGG_CPD", metabolite.getExternalIdentifier());
							value.put("MERLIN_ID", metabolite.getMetaboliteID()+"");

							if(reactionsExtraInfoRequired)
								this.metabolitesExtraInfo.put(mid, value); //Commented because of merlindb-Optflux, check if this is needed for other applications

							if(metabolite.getFormula() != null)
								mci.setFormula(metabolite.getFormula());
							this.compoundsMap.put(mid, mci);
						}
					}

					mci.addReaction(reaction_id+"");

					String compartmentId = this.getCompartmentID(metabolite.getCompartmentID()+"");

					if(compartmentId==null)
						logger.error("null compartment {} for metabolite {} ", metabolite,this.compartmentID);

					double value = Double.valueOf(metabolite.getStoichiometric_coefficient());

					StoichiometryValueCI s = new StoichiometryValueCI(mid, Math.abs( value), compartmentId);
					this.compartmentsMap.get(compartmentId).addMetaboliteInCompartment(mid);
					if(value>0)
						products.put(mid, s);
					else
						reactants.put(mid, s);
				}


				//if (this.reactionMetabolites.containsKey(compartmentID))
				//{
				double upper_bound = 999999;
				double lower_bound = 0;

				if(reaction.getLowerBound()!= null)
					lower_bound = reaction.getLowerBound();
				else 
					if(reaction.isReversible())
						lower_bound = -999999;

				if(reaction.getUpperBound()!= null)
					upper_bound = reaction.getUpperBound();


				String equation = reaction.getEquation().replace(" ", "");
				String compartmentAbb = "cytop";
				if(reaction.getLocalisation() != null)
					compartmentAbb = reaction.getLocalisation().getAbbreviation();
				//TODO RESOLVER ISTO
				//String compartmentAbb = this.compartments.get(reaction.getLocalisation().getCompartmentID()+"").getAbbreviation();
				String name = reaction.getExternalIdentifier();
				if((reactants.isEmpty() || products.isEmpty()) && StringUtils.countMatches(name,"_")>2) {

					if(name.contains("R_EX_"))					
						name=name.substring(0, name.indexOf("_", 5));
				}

				String rid = ContainerBuilder.buildID("R_", name, compartmentAbb);

				if(this.biomassName!=null && this.biomassName.equalsIgnoreCase(reaction.getExternalIdentifier()))
					this.biomassID = rid;

				this.defaultEC.put(rid, new ReactionConstraintCI(lower_bound, upper_bound));

				ReactionCI r = new ReactionCI(rid, equation, reaction.isReversible(), reactants, products);
				if(this.reactions.get(reaction_id).getEnzymes()!=null) {

					List<String> enzymes = new ArrayList<>();
					if(this.reactions.get(reaction_id).getEnzymes() != null) {
						for(ProteinContainer container : this.reactions.get(reaction_id).getEnzymes()) {
							enzymes.add(container.getExternalIdentifier());
						}	
						r.setEc_number(enzymes.toString());
					}
				}

				if(ContainerUtils.isReactionDrain(r))
					r.setType(ReactionTypeEnum.Drain);

				//ADD PATHWAYS
				if(reaction.getPathways()!=null && !reaction.getPathways().isEmpty())
					r.setSubsystems(reaction.getPathwayNames());

				//ADD GENES
				if(reaction.getGenes()!= null)
					for(Pair<String,String> gene : reaction.getGenes())
						r.addGene(gene.getA());


				String geneRule = null;

				if(reaction.getGeneRule()==null || reaction.getGeneRule().isEmpty())
					geneRule = RulesParser.processReactionGenes(reaction.getGenes(), concatenate);

				else {

					List<List<Pair<String,String>>> rule = ModelReactionsServices.parseBooleanRule(this.databaseName, reaction.getGeneRule());
					geneRule = Utilities.parseRuleListToString(rule);
				}
				
				
				if(geneRule!= null && !geneRule.isEmpty())
					r.setGeneRule(geneRule);

				this.reactionsMap.put(r.getId(), r);

				Map<String, String> value = new HashMap<>();
				value.put("MERLIN_ID", reaction.getExternalIdentifier());

				if(reactionsExtraInfoRequired)
					this.reactionsExtraInfo.put(rid, value);	
				//}
			}
			else {

				logger.error("null reaction metabolites for {} {}",reaction_id ,reaction.getExternalIdentifier());
			}
		}
	}


	/**
	 * @param proteins
	 * @return
	 */
	public static String processReactionProteins(Set<String> proteins) {

		String proteinData = "";

		if(proteins!=null) {

			for(String prot:proteins) {

				if(prot!=null) {

					proteinData=proteinData.concat(prot).concat(" or ");
				}
			}
			proteinData=proteinData.substring(0, proteinData.lastIndexOf(" or "));
		}
		return proteinData;
	}

	/**
	 * @param pathways
	 * @return
	 */
	public static String processReactionPathways(List<PathwayContainer> pathways) {

		String pathwaysData = "";

		if(pathways!=null && !pathways.isEmpty()) {

			for(PathwayContainer path:pathways) {

				if(path!=null) {

					pathwaysData=pathwaysData.concat(path.getName()).concat(" and ");
				}
			}
			pathwaysData=pathwaysData.substring(0, pathwaysData.lastIndexOf(" and "));
		}
		return pathwaysData;
	}

	/**
	 * @param proteinClass
	 * @return
	 */
	public static String processReactionProteinClass(List<ProteinContainer> proteinClass) {

		String enzymesData = "";

		if(proteinClass!=null && !proteinClass.isEmpty()) {

			for(ProteinContainer enzyme:proteinClass) {

				if(enzyme!=null)
					enzymesData=enzymesData.concat(enzyme.getExternalIdentifier()).concat(" and ");
			}

			if(enzymesData != null && !enzymesData.isEmpty()) {
				enzymesData=enzymesData.substring(0, enzymesData.lastIndexOf(" and "));
			}

		}
		return enzymesData;	
	}


	/**
	 * @param notes
	 * @return
	 */
	public static Set<String> processReactionNotes(String notes) {

		Set<String> reactionNotes = new HashSet<>();
		//<html:p>GENE_ASSOCIATION: </html:p>
		//<html:p>PROTEIN_ASSOCIATION: "</html:p>
		//<html:p>SUBSYSTEM: S_Tyrosine__Tryptophan__and_Phenylalanine_Metabolism</html:p>
		//<html:p>PROTEIN_CLASS: 1.13.11.27</html:p>
		//<html:p>GENE_ASSOCIATION: ( YOL096C  and  YDR204W  and  YML110C  and  YGR255C  and  YOR125C  and  YGL119W  and  YLR201C )</html:p>
		//<html:p>PROTEIN_ASSOCIATION: ( Coq3-m and Coq4-m and Coq5-m and Coq6-m and Coq7-m and Coq8-m and Coq9-m )"</html:p>
		//<html:p>SUBSYSTEM: S_Quinone_Biosynthesis</html:p>
		//<html:p>PROTEIN_CLASS: </html:p>


		if(notes!=null && !notes.trim().isEmpty()) {

			for(String note : notes.split(" \\| "))
				reactionNotes.add(note.replace(",","_").replace(" ","_").replace(":_",": ").replace("_AND_"," and ").replace("_OR_"," or ").trim());	 //.replace(")","").replace("(","_")
		}
		return reactionNotes; 
	}

	/**
	 * @param genesNotes
	 * @param notesList
	 * @param addAllNotes
	 * @param gene_rule
	 * @return
	 */
	public static Set<XMLNode> getGeneRules(String merlinGenes, Set<String> notesList, boolean addAllNotes, String gene_rule) {

		Set<XMLNode> xmlNodeSet = new HashSet<>();
		XMLNode node = new XMLNode(new XMLTriple("p", "", "html"));
		boolean noGene = true, go = true;
		String genesNotes = null;

		if(gene_rule!=null && !gene_rule.isEmpty()) {

			noGene = false;
			genesNotes = gene_rule;
		}

		for (String noteReaction : notesList) {

			if(noteReaction.contains(GENE_RULE_PREFIX) && genesNotes==null) {

				go = false;
				noGene = false;

				if(noteReaction.contains("no_gene")) {

					if(addAllNotes) {

						node = new XMLNode(new XMLTriple("p", "", "html"));
						node.addChild(new XMLNode("reaction annotation: no gene"));
						xmlNodeSet.add(node);
					}
				}
				else {

					node = new XMLNode(new XMLTriple("p", "", "html"));

					//					String out = GENE_RULE_PREFIX;
					//					
					//					String note = noteReaction.split(":")[1];
					//					
					//					String [] rules = note.split(" or ");
					//					
					//					String auxGeneOR = " or ";
					//					
					//					for(int j = 0 ; j<rules.length; j++) {
					//						
					//						String rule = rules[j];
					//						
					//						String [] genes = rule.split(" and ");
					//						
					//						String auxGeneAND = " and ";
					//						
					//						for(int i = 0 ; i<genes.length; i++) {
					//							
					//							String gene = genes[i];
					//							
					//							String[] gene_locus = gene.split("_");
					//							
					//							if(genes.length-1 == i)
					//								auxGeneAND = "";
					//							
					//							out = out.concat(gene_locus[gene_locus.length-1]).concat(auxGeneAND);
					//						}
					//						
					//						if(j<rules.length-1)
					//							out = out.concat(auxGeneOR);
					//					}
					//					
					//					node.addChild(new XMLNode(out.replace(" OR ", " or ").replace(" AND ", " and ")));

					node.addChild(new XMLNode(noteReaction.replace(" OR ", " or ").replace(" AND ", " and ")));
					xmlNodeSet.add(node);
				}
			}

			if(addAllNotes && go) {

				node = new XMLNode(new XMLTriple("p", "", "html"));
				node.addChild(new XMLNode(noteReaction));
				xmlNodeSet.add(node);
			}
		}


		if(noGene) {

			genesNotes = merlinGenes;
		}
		else {

			if(merlinGenes!=null && !merlinGenes.trim().isEmpty() && addAllNotes) {

				node = new XMLNode(new XMLTriple("p", "", "html"));
				node.addChild(new XMLNode("merlin_gene_data: "+merlinGenes));
				xmlNodeSet.add(node);
			}
		}

		if(go) {

			String ret="";
			if(genesNotes!=null && !genesNotes.trim().isEmpty())
				ret += genesNotes;
			node = new XMLNode(new XMLTriple("p", "", "html"));
			node.addChild(new XMLNode(GENE_RULE_PREFIX+ret));
			xmlNodeSet.add(node);
		}

		return xmlNodeSet;		
	}

	/**
	 * This method returns the gene rule as a String.
	 * 
	 * @param genesNotes
	 * @param notesList
	 * @param addAllNotes 
	 * @return
	 */
	public static String getSimpleGeneRule_SBML2(String genesNotes, Set<String> notesList) {

		String ret= null;
		boolean noGene = true;

		for (String noteReaction : notesList) {

			if(noteReaction.contains(GENE_RULE_PREFIX)) {

				if(!noteReaction.contains("no_gene")) 

					noGene = false;

				ret = noteReaction.replace(" OR ", " or ").replace(" AND ", " and ").replace(GENE_RULE_PREFIX, "").trim();
			}
		}

		if(noGene) {

			if(genesNotes!=null && !genesNotes.trim().isEmpty())
				ret = genesNotes;
		}

		return ret;		
	}

	/**
	 * This method returns the gene rule as a String.
	 * 
	 * @param genesNotes
	 * @param boolean_rule 
	 * @return
	 */
	public static String getSimpleGeneRule(String genesNotes, String boolean_rule) {

		String ret= null;

		if (boolean_rule != null && !boolean_rule.isEmpty())
			ret = boolean_rule;
		else if(genesNotes!=null && !genesNotes.trim().isEmpty())
			ret = genesNotes;

		return ret;		
	}

	/**
	 * @param proteinNotes
	 * @param notesList
	 * @param addAllNotes 
	 * @return
	 */
	public static Set<XMLNode> getProteinRules(String proteinNotes, Set<String> notesList, boolean addAllNotes) {

		Set<XMLNode> xmlNodeSet = new HashSet<>(); 
		XMLNode node = new XMLNode(new XMLTriple("p", "", "html"));
		boolean noProtein = true;

		for (String noteReaction : notesList) {

			if(noteReaction.contains(PROTEIN_RULE_PREFIX)) {

				noProtein = false;

				node = new XMLNode(new XMLTriple("p", "", "html"));
				node.addChild(new XMLNode(noteReaction.replace(" OR ", " or ").replace(" AND ", " and ")));
				xmlNodeSet.add(node);

				if(proteinNotes!=null && !proteinNotes.trim().isEmpty() && addAllNotes) {

					node = new XMLNode(new XMLTriple("p", "", "html"));
					node.addChild(new XMLNode("merlin_protein_data: "+proteinNotes));
					xmlNodeSet.add(node);
				}
			}
			//			else {
			//
			//				node.addChild(new XMLNode(noteReaction));
			//			}
		}

		if(noProtein) {

			String ret = "";
			if(proteinNotes!=null && !proteinNotes.trim().isEmpty())
				ret += proteinNotes;

			node = new XMLNode(new XMLTriple("p", "", "html"));
			node.addChild(new XMLNode(PROTEIN_RULE_PREFIX+ret));
			xmlNodeSet.add(node);
		}

		return xmlNodeSet;	
	}

	/**
	 * @param pathwayNotes
	 * @param notesList
	 * @param addAllNotes 
	 * @return
	 */
	public static Set<XMLNode> getPathwaysRules(String pathwayNotes, Set<String> notesList, boolean addAllNotes) {

		Set<XMLNode> xmlNodeSet = new HashSet<>();
		XMLNode node = new XMLNode(new XMLTriple("p", "", "html"));
		boolean noPathway = true;

		for (String note : notesList) {

			if(note.contains(SUBSYSTEM_PREFIX)) {

				noPathway = false;

				node = new XMLNode(new XMLTriple("p", "", "html"));
				node.addChild(new XMLNode(note.replace(" OR ", " or ").replace(" AND ", " and ")));
				xmlNodeSet.add(node);

				if(pathwayNotes!=null && !pathwayNotes.trim().isEmpty() && addAllNotes) {

					node = new XMLNode(new XMLTriple("p", "", "html"));
					node.addChild(new XMLNode("merlin_subsystem_data: "+pathwayNotes));
					xmlNodeSet.add(node);
				}
			}
		}

		if(noPathway) {

			String ret = "";
			if(pathwayNotes!=null && !pathwayNotes.trim().isEmpty())
				ret += pathwayNotes;

			node = new XMLNode(new XMLTriple("p", "", "html"));
			node.addChild(new XMLNode(SUBSYSTEM_PREFIX+ret));
			xmlNodeSet.add(node);
		}

		return xmlNodeSet;	
	}

	/**
	 * @param enzymesNotes
	 * @param notesList
	 * @param addAllNotes 
	 * @return
	 */
	public static Set<XMLNode> getProteinClassRules(String enzymesNotes, Set<String> notesList, boolean addAllNotes) {

		Set<XMLNode> xmlNodeSet = new HashSet<>();
		XMLNode node = new XMLNode(new XMLTriple("p", "", "html"));
		boolean noEnzyme = true;

		for (String note : notesList) {

			if(note.contains(PROTEIN_CLASS_PREFIX)) {

				noEnzyme = false;
				node = new XMLNode(new XMLTriple("p", "", "html"));
				node.addChild(new XMLNode(note.replace(" OR ", " or ").replace(" AND ", " and ")));
				xmlNodeSet.add(node);

				if(enzymesNotes!=null && !enzymesNotes.trim().isEmpty() && addAllNotes) {

					node = new XMLNode(new XMLTriple("p", "", "html"));
					node.addChild(new XMLNode("merlin_protein_class_data: "+enzymesNotes));
					xmlNodeSet.add(node);
				}
			}
		}
		if(noEnzyme) {

			String ret = "";
			if(enzymesNotes!=null && !enzymesNotes.trim().isEmpty())
				ret += enzymesNotes;

			node = new XMLNode(new XMLTriple("p", "", "html"));
			node.addChild(new XMLNode(PROTEIN_CLASS_PREFIX+ret));
			xmlNodeSet.add(node);
		}

		return xmlNodeSet;	
	}

	/**
	 * @return The model name
	 */
	@Override
	public String getModelName() {
		return modelName;
	}

	/**
	 * @return The organism name
	 */
	@Override
	public String getOrganismName() {
		return organismName;
	}

	/**
	 * @return The notes
	 */
	@Override
	public String getNotes() {
		return notes;
	}

	/**
	 * @return The version
	 */
	@Override
	public Integer getVersion() {
		return version;
	}

	/**
	 * @return A map with the reactions
	 */
	@Override
	public Map<String, ReactionCI> getReactions() {
		return reactionsMap;
	}

	/**
	 * @return A map with the metabolites
	 */
	@Override
	public Map<String, MetaboliteCI> getMetabolites() {
		return compoundsMap;
	}

	/**
	 * @return A map with the genes
	 */
	@Override
	public Map<String, GeneCI> getGenes() {
		return genes;
	}

	/**
	 * @return A map with the compartments
	 */
	@Override
	public HashMap<String, CompartmentCI> getCompartments() {
		return compartmentsMap;
	}

	/**
	 * @return The biomassID
	 */
	@Override
	public String getBiomassId() {
		return biomassID;
	}

	/**
	 * @return A map with the Environmental Conditions
	 */
	@Override
	public Map<String, ReactionConstraintCI> getDefaultEC() {
		return defaultEC;
	}

	/**
	 * @return The external compartment ID
	 */
	@Override
	public String getExternalCompartmentId() {
		return this.externalCompartmentID;
	}

	/**
	 * @return A map with the metabolites extra info
	 */
	@Override
	public Map<String, Map<String, String>> getMetabolitesExtraInfo() {
		return metabolitesExtraInfo;
	}

	/**
	 * @return A map with the reactions extra info
	 */
	@Override
	public Map<String, Map<String, String>> getReactionsExtraInfo() {
		return reactionsExtraInfo;
	}

	public String getModelID() {
		return modelID;
	}

	public void setModelID(String modelID) {
		this.modelID = modelID;
	}
}
