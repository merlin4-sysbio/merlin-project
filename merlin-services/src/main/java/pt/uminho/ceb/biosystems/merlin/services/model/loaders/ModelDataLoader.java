package pt.uminho.ceb.biosystems.merlin.services.model.loaders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModuleContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwaysHierarchyContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceInitialData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Pathways;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelAliasesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelCompartmentServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelDBLinksServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelEnzymesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelMetabolitesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelModuleServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelPathwaysServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelSequenceServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelStoichiometryServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelSubunitServices;
import pt.uminho.ceb.biosystems.merlin.utilities.RulesParser;
import pt.uminho.ceb.biosystems.merlin.utilities.SbmlUtilities;

/**
 * @author ODias
 *
 */
public class ModelDataLoader {

	private static double _LOWERBOUND = -999999, _UPPERBOUND = 999999;
	private ConcurrentHashMap<String,List<String>> genesModulesIdentifier;
	private Map<String, String> locusTagsToProteinIdsMap;
	private boolean importSBML;
	private WorkspaceInitialData databaseInitialData;
	private ConcurrentLinkedQueue<String> compoundsWithBiologicalRoles;
	private String databaseName; 

	/**
	 * @param databaseInitialData
	 * @param compoundsWithBiologicalRoles
	 * @param importFromSBML
	 * @param databaseName
	 * @throws Exception
	 */
	public ModelDataLoader(WorkspaceInitialData databaseInitialData, ConcurrentLinkedQueue<String> compoundsWithBiologicalRoles, boolean importFromSBML, String databaseName) throws Exception {


		this.databaseName = databaseName;
		this.databaseInitialData = databaseInitialData;
		this.setCompoundsWithBiologicalRoles(compoundsWithBiologicalRoles);
		this.genesModulesIdentifier = new ConcurrentHashMap<String, List<String>>();
		this.importSBML = importFromSBML;

	}

	/**
	 * @param geneContainer
	 * @throws Exception 
	 * @throws IOException 
	 */
	public void loadGene(GeneContainer geneContainer) throws IOException, Exception {

		if(databaseInitialData.getGenesIdentifier().containsKey(geneContainer.getExternalIdentifier())) {

			if(geneContainer.getModules()!=null)
				this.genesModulesIdentifier.put(geneContainer.getExternalIdentifier(),geneContainer.getModules());
		}
		else {

			int geneID;

			if(!databaseInitialData.getGenesIdentifier().containsKey(geneContainer.getExternalIdentifier())) {

				SourceType source = SourceType.KEGG;
				String geneEntry = geneContainer.getExternalIdentifier();

				if(this.importSBML){
					source = SourceType.SBML;
					geneEntry = SbmlUtilities.processSBMLGeneIdentifier(geneEntry);
				}

				Integer res = ModelGenesServices.insertNewGene(this.databaseName, geneEntry, geneContainer.getName(), geneEntry, source);

				databaseInitialData.getGenesIdentifier().put(geneContainer.getExternalIdentifier(), res);

				if(geneContainer.getModules()!=null)
					this.genesModulesIdentifier.put(geneContainer.getExternalIdentifier(),geneContainer.getModules());
			}

			geneID= databaseInitialData.getGenesIdentifier().get(geneContainer.getExternalIdentifier());

			if(geneContainer.getLeft_end_position()!=null && geneContainer.getRight_end_position()!=null){

				ModelGenesServices.updateModelGeneEndPositions(this.databaseName, geneID, geneContainer.getLeft_end_position(), geneContainer.getRight_end_position());
			}

			if(geneContainer.getDblinks()!=null)
			{
				for(String dbLink:geneContainer.getDblinks())
				{
					String database = dbLink.split(":")[0], link = dbLink.split(":")[1];

					boolean exists = ProjectServices.checkInternalIdFromDblinks(this.databaseName, "g", geneID, database);

					if(!exists)
					{
						ModelDBLinksServices.insertEntry(this.databaseName, "g", geneID, database, link);
					}
				}
			}

			if(geneContainer.getNames()!=null)
			{	
				for(String synonym:geneContainer.getNames())
				{
					synonym = synonym.replace(";", "");

					boolean exists = ModelAliasesServices.checkEntityFromAliases(this.databaseName, "g", geneID, synonym);
					if(!exists)
					{
						ModelAliasesServices.insertNewModelAliasEntry(this.databaseName, "g", geneID, synonym);

					}
				}
			}

			if(geneContainer.getAasequence()!=null)
			{
				boolean exists = ModelSequenceServices.checkGeneIDFromSequence(this.databaseName, geneID, SequenceType.PROTEIN);
				if(!exists)
				{
					ModelSequenceServices.insertNewSequence(this.databaseName, geneID, SequenceType.PROTEIN, geneContainer.getAasequence(), geneContainer.getAalength());
				}
			}
			if(geneContainer.getNtsequence()!=null)
			{
				boolean exists = ModelSequenceServices.checkGeneIDFromSequence(this.databaseName, geneID, SequenceType.GENOMIC_DNA);
				if(!exists)
				{
					ModelSequenceServices.insertNewSequence(this.databaseName, geneID, SequenceType.GENOMIC_DNA, geneContainer.getNtsequence(), geneContainer.getNtlength());
				}
			}

			if(geneContainer.getOrthologues()!=null) {

				for(String orthologue:geneContainer.getOrthologues()) {

					if(!databaseInitialData.getOrthologuesIdentifier().containsKey(orthologue)) {

						int res = ModelGenesServices.insertNewOrthologue(this.databaseName, orthologue, null);

						databaseInitialData.getOrthologuesIdentifier().put(orthologue, res);
					}

					boolean exists = ModelGenesServices.checkGeneHasOrthologyEntries(this.databaseName, geneID, databaseInitialData.getOrthologuesIdentifier().get(orthologue));

					if(!exists) {
						ModelGenesServices.insertModelGeneHasOrthology(this.databaseName, geneID, databaseInitialData.getOrthologuesIdentifier().get(orthologue));
					}
				}
			}
		}
	}


	/**
	 * Metabolites container
	 * 
	 * @param metabolites
	 * @throws Exception 
	 */
	public void loadMetabolites(ConcurrentLinkedQueue<MetaboliteContainer> metabolites) throws Exception {

		ModelMetabolitesServices.loadMetabolites(this.databaseName, metabolites, databaseInitialData.getMetabolitesIdentifier(), this.getCompoundsWithBiologicalRoles(), this.importSBML);

		for(MetaboliteContainer m : metabolites)
			databaseInitialData.getMetabolitesIdentifier().put(m.getExternalIdentifier(), m.getMetaboliteID());

		for (MetaboliteContainer metaboliteContainer : metabolites) {

			if(metaboliteContainer.getDblinks()!=null) {
				for(String dbLink : metaboliteContainer.getDblinks()) {

					String database = dbLink.split(":")[0], link = dbLink.split(":")[1];	
					
					boolean exists = ProjectServices.checkInternalIdFromDblinks(this.databaseName, "c", metaboliteContainer.getMetaboliteID(), database);
					
					if(!exists)
						ModelDBLinksServices.insertEntry(this.databaseName, "c", metaboliteContainer.getMetaboliteID(), database, link);

				}
			}
		}

		ModelAliasesServices.loadALiases(this.databaseName, metabolites);

		//		pStatement = this.connection.prepareStatement("INSERT INTO same_as (metabolite_id, similar_metabolite_id) VALUES(?,?);");
		//
		//		ModelAPI.loadSameAs(metabolites, pStatement);

		for(MetaboliteContainer metaboliteContainer : metabolites) {

			if(metaboliteContainer.getPathways()!=null) {

				databaseInitialData.getMetabolitesPathway().put(metaboliteContainer.getMetaboliteID(), metaboliteContainer.getPathways().keySet());
				databaseInitialData.getMetabolitesPathwayList().add(metaboliteContainer.getMetaboliteID());
			}
		}
	}


	/**
	 * @param enzymeContainer
	 * @throws Exception 
	 * @throws IOException 
	 */
	public void loadProtein(ProteinContainer enzymeContainer) throws IOException, Exception {

		int enzymeClass = Integer.valueOf(enzymeContainer.getExternalIdentifier().substring(0,1));

		SourceType source = SourceType.KEGG;
		boolean inModel = false;

		if(this.importSBML){
			source = SourceType.SBML;
			inModel = true;
		}

		if(enzymeContainer.getName()!=null) {

			String protein_name = enzymeContainer.getName();
			//			if(enzyme_entry.getName().startsWith("Transferred to"))
			//			{
			//				System.out.println("TRANSFERRED "+enzyme_entry.getEntry());
			//			}
			//			else if(enzyme_entry.getName().startsWith("Deleted entry"))
			//			{
			//				System.out.println("Deleted "+enzyme_entry.getEntry());
			//			}
			//			else
			{

				if(!databaseInitialData.getProteinsIdentifier().containsKey(enzymeContainer.getExternalIdentifier())) {
					
					ProteinContainer container = ModelProteinsServices.getProteinByEcNumber(this.databaseName, enzymeContainer.getExternalIdentifier());
					
					Integer protein_id = null;
					
					if(container == null)
						protein_id = ModelProteinsServices.insertProtein(this.databaseName, protein_name, getEnzymeClass(enzymeClass));
					else
						protein_id = container.getIdProtein();

					databaseInitialData.getProteinsIdentifier().put(enzymeContainer.getExternalIdentifier(), protein_id);

					ModelEnzymesServices.updateEnzyme(this.databaseName, protein_id, enzymeContainer.getExternalIdentifier(), inModel, source);

					if(enzymeContainer.getCofactors()!=null) {

						for(String cofactor_string:enzymeContainer.getCofactors()) {

							if(!databaseInitialData.getMetabolitesIdentifier().containsKey(cofactor_string)) {

								int res = ModelMetabolitesServices.getCompoundIDbyExternalIdentifier(this.databaseName, cofactor_string);
								databaseInitialData.getMetabolitesIdentifier().put(cofactor_string,res);
							}

							boolean exists = ModelProteinsServices.checkModelEnzymaticCofactorEntry(this.databaseName, 
									databaseInitialData.getMetabolitesIdentifier().get(cofactor_string), protein_id);
							
							if(!exists) {
								ModelProteinsServices.insertModelEnzymaticCofactor(this.databaseName, databaseInitialData.getMetabolitesIdentifier().get(cofactor_string), protein_id);
							}
						}
					}

					//TODO add when using prepared statements  
					if(enzymeContainer.getDblinks()!=null) {

						for(String dbLink:enzymeContainer.getDblinks()) {

							String database = dbLink.split(":")[0], link = dbLink.split(":")[1];
							boolean exists = ProjectServices.checkInternalIdFromDblinks(this.databaseName, "p", protein_id, database);
							if(!exists) {

								ModelDBLinksServices.insertEntry(this.databaseName, "p", protein_id, database, link);

							}
						}
					}

					if(enzymeContainer.getNames()!=null) {

						for(String synonym:enzymeContainer.getNames()) {

							synonym = synonym.replace(";", "");
							boolean exists = ModelAliasesServices.checkEntityFromAliases(this.databaseName, "p", protein_id, synonym);

							if(!exists) {
								ModelAliasesServices.insertNewModelAliasEntry(this.databaseName, "p", protein_id, synonym);
							}
						}
					}
				}

				if(enzymeContainer.getPathways()!=null) {

					databaseInitialData.getEnzymesPathway().put(enzymeContainer.getExternalIdentifier(), enzymeContainer.getPathways().keySet());
					databaseInitialData.getEnzymesPathwayList().add(enzymeContainer.getExternalIdentifier());
				}

				int protein_id = databaseInitialData.getProteinsIdentifier().get(enzymeContainer.getExternalIdentifier());

				if(enzymeContainer.getGenes()!=null) {

					databaseInitialData.getEnzymesInModel().add(enzymeContainer.getExternalIdentifier());

					ModelProteinsServices.updateProteinSetEcNumber(this.databaseName, protein_id, enzymeContainer.getExternalIdentifier());

					for(String gene:enzymeContainer.getGenes()) {

						if(!databaseInitialData.getGenesIdentifier().containsKey(gene)) {

							int res = ModelGenesServices.getGeneIdByLocusTag(this.databaseName, gene);
							if(res > -1)
								databaseInitialData.getGenesIdentifier().put(gene,res);

						}

						if(this.genesModulesIdentifier.contains(gene)) {

							for (String module : this.genesModulesIdentifier.get(gene)) {
								
								boolean exists = ModelModuleServices.checkModulesByModuleId(this.databaseName, databaseInitialData.getGenesIdentifier().get(gene),
										protein_id, databaseInitialData.getModulesIdentifier().get(module));

								if(!exists) {
									ModelSubunitServices.insertModelSubunit(this.databaseName, databaseInitialData.getGenesIdentifier().get(gene), protein_id, null, null);

								}
							}
						}
						else {

							boolean exists = ModelModuleServices.checkModules(this.databaseName, databaseInitialData.getGenesIdentifier().get(gene), protein_id);
							if(!exists){

								ModelSubunitServices.insertModelSubunit(this.databaseName, databaseInitialData.getGenesIdentifier().get(gene), protein_id);
							}
						}

						if(databaseInitialData.getEnzymesReactions()!=null && databaseInitialData.getEnzymesReactions().containsKey(enzymeContainer.getExternalIdentifier())) {

							for(int idReaction : databaseInitialData.getEnzymesReactions().get(enzymeContainer.getExternalIdentifier())) {

								ModelReactionsServices.updateModelReactionInModelByReactionId(this.databaseName, idReaction, true);
							}
						}
					}
				}
			}
		}
		else {
			//System.out.println("NULL NAME "+enzyme_entry.getEntry());

			int proteinId = ModelProteinsServices.getProteinIDByNameAndClass(this.databaseName, enzymeContainer.getExternalIdentifier(), getEnzymeClass(enzymeClass));
			if(proteinId<0) {

				proteinId = ModelProteinsServices.insertProtein(this.databaseName, enzymeContainer.getExternalIdentifier(), getEnzymeClass(enzymeClass));

			}

			databaseInitialData.getProteinsIdentifier().put(enzymeContainer.getExternalIdentifier(), proteinId);

			ProteinContainer container = ModelProteinsServices.getProteinEcNumberAndInModelByProteinID(this.databaseName, proteinId);

			if(container == null || container.getExternalIdentifier() == null || !container.getExternalIdentifier().equals(enzymeContainer.getExternalIdentifier()))

				ModelEnzymesServices.updateEnzyme(this.databaseName, proteinId, enzymeContainer.getExternalIdentifier(), inModel, source);

		}
	}

	/**
	 * Load reaction data from KEGG
	 * 
	 * @param reactionContainer
	 * @param stmt
	 * @param databaseType
	 * @throws Exception 
	 */
	public void loadReaction(ReactionContainer reactionContainer) throws Exception {

		if(this.databaseInitialData.getReactionsIdentifier().containsKey(reactionContainer.getExternalIdentifier())) {

			boolean inModel = false;

			if(reactionContainer.getEnzymes() != null){

				for(ProteinContainer enzyme : reactionContainer.getEnzymes()) {

					if(databaseInitialData.getEnzymesInModel().contains(enzyme.getExternalIdentifier())) {

						if(reactionContainer.getPathways() == null) {

							inModel=true;
						}
						else {

							if(databaseInitialData.getEnzymesPathway().containsKey(enzyme.getExternalIdentifier())) {

								for(PathwayContainer path : reactionContainer.getPathways()) {

									if(databaseInitialData.getEnzymesPathway().get(enzyme.getExternalIdentifier()).contains(path.getExternalIdentifier()))
										inModel=true;
								}
							}
						}
					}
				}
			}

			if(inModel) {

				for(Integer id : this.databaseInitialData.getReactionsIdentifier().get(reactionContainer.getExternalIdentifier())){

					ModelReactionsServices.updateModelReactionInModelByReactionId(this.databaseName, id, inModel);

				}
			}
		}

		else {

			if(importSBML && reactionContainer.getLocalisation() != null)
				databaseInitialData.setCompartmentIdentifier(ModelCompartmentServices.getCompartmentID(this.databaseName , reactionContainer.getLocalisation().getName()));

			boolean go = true;

			for(MetaboliteContainer reactant : reactionContainer.getReactantsStoichiometry()) {

				String metaboliteID = null;

				if(importSBML)
					metaboliteID = SbmlUtilities.processSBMLMetaboliteIdentifier(reactant.getExternalIdentifier());
				else
					metaboliteID = reactant.getExternalIdentifier();

				if(!databaseInitialData.getMetabolitesIdentifier().containsKey(metaboliteID))
					go = false;
			}

			for(MetaboliteContainer product : reactionContainer.getProductsStoichiometry()) {

				String metaboliteID = null;

				if(importSBML)
					metaboliteID = SbmlUtilities.processSBMLMetaboliteIdentifier(product.getExternalIdentifier());
				else
					metaboliteID = product.getExternalIdentifier();

				if(!databaseInitialData.getMetabolitesIdentifier().containsKey(metaboliteID))
					go = false;
			}

			if(go) {

				boolean inModel = false;

				if(reactionContainer.getEnzymes() != null){

					for(ProteinContainer enzyme : reactionContainer.getEnzymes()) {

						if(databaseInitialData.getEnzymesInModel().contains(enzyme.getExternalIdentifier())) {

							if(reactionContainer.getPathways() == null) {

								inModel=true;
							}
							else {

								if(databaseInitialData.getEnzymesPathway().containsKey(enzyme.getExternalIdentifier())) {

									for(PathwayContainer path : reactionContainer.getPathways()) {

										if(databaseInitialData.getEnzymesPathway().get(enzyme.getExternalIdentifier()).contains(path.getExternalIdentifier()));
										inModel=true;
									}
								}
							}
						}
					}
				}
				if (reactionContainer.isSpontaneous()) {

					if(!databaseInitialData.getPathwaysIdentifier().contains(Pathways.SPONTANEOUS.getCode())) {

						int res = ModelPathwaysServices.getPathwayIdByNameAndCode(this.databaseName, Pathways.SPONTANEOUS.getName(), Pathways.SPONTANEOUS.getCode());
						if(res<0) {

							res = ModelPathwaysServices.insertModelPathwayCodeAndName(this.databaseName, Pathways.SPONTANEOUS.getCode(), Pathways.SPONTANEOUS.getName());

						}
						databaseInitialData.getPathwaysIdentifier().put(Pathways.SPONTANEOUS.getCode(), res);
					}

					reactionContainer.addPathway(Pathways.SPONTANEOUS.getCode(),Pathways.SPONTANEOUS.getName());

					inModel = true;
				} 

				if(reactionContainer.isNon_enzymatic()) {

					if(!databaseInitialData.getPathwaysIdentifier().contains(Pathways.NON_ENZYMATIC.getCode())) {

						int res = ModelPathwaysServices.getPathwayIdByNameAndCode(this.databaseName, Pathways.NON_ENZYMATIC.getName(), Pathways.NON_ENZYMATIC.getCode());

						if(res<0) {

							res = ModelPathwaysServices.insertModelPathwayCodeAndName(this.databaseName, Pathways.NON_ENZYMATIC.getCode(), Pathways.NON_ENZYMATIC.getName());
						}
						databaseInitialData.getPathwaysIdentifier().put(Pathways.NON_ENZYMATIC.getCode(), res);
					}

					reactionContainer.addPathway(Pathways.NON_ENZYMATIC.getCode(),Pathways.NON_ENZYMATIC.getName());
				}

				if(!this.databaseInitialData.getReactionsIdentifier().containsKey(reactionContainer.getExternalIdentifier())) {

					if(importSBML){
						reactionContainer.setSource(SourceType.SBML);
						reactionContainer.setInModel(true);

						String rule = reactionContainer.getGeneRule();

						if(rule!=null && !rule.isEmpty()){
							String boolean_rule = RulesParser.reverseParseGeneRules(rule, databaseInitialData.getGenesIdentifier());
							boolean_rule = "'".concat(boolean_rule).concat("'");
							reactionContainer.setGeneRule(boolean_rule);
						}

						if(reactionContainer.getNotes()!=null && !reactionContainer.getNotes().isEmpty()) {
							String notes = "'".concat(reactionContainer.getNotes()).concat("'");
							reactionContainer.setNotes(notes);
						}
					}
					else{
						reactionContainer.setInModel(inModel);
						reactionContainer.setSource(SourceType.KEGG);
						reactionContainer.setLowerBound(_LOWERBOUND);
						reactionContainer.setUpperBound(_UPPERBOUND);
					}

					int res = ModelReactionsServices.insertNewReaction(this.databaseName, reactionContainer);

					if(this.databaseInitialData.getReactionsIdentifier().containsKey(reactionContainer.getExternalIdentifier())) {
						this.databaseInitialData.getReactionsIdentifier().get(reactionContainer.getExternalIdentifier()).add(res);
					}
					else {
						List<Integer> set = new ArrayList<>();
						set.add(res);
						this.databaseInitialData.getReactionsIdentifier().put(reactionContainer.getExternalIdentifier(), set);

					}
				}

				for(Integer reaction_id : this.databaseInitialData.getReactionsIdentifier().get(reactionContainer.getExternalIdentifier())) {

					boolean isReactant = true;

					for(MetaboliteContainer reactant : reactionContainer.getReactantsStoichiometry())
						this.loadMetabolites(reactant, isReactant, reaction_id);

					isReactant = false;

					for(MetaboliteContainer product : reactionContainer.getProductsStoichiometry())
						this.loadMetabolites(product, isReactant, reaction_id);

					if(reactionContainer.getEnzymes()!=null) {

						for(ProteinContainer enzyme : reactionContainer.getEnzymes()) {

							if(enzyme != null && !enzyme.getExternalIdentifier().isEmpty() && !enzyme.getExternalIdentifier().contains("-")) {
								
								ProteinContainer container = ModelProteinsServices.getProteinByEcNumber(this.databaseName, enzyme.getExternalIdentifier());
								
								Integer protein_id = null;
								
								if(container == null) {
//									if(enzyme.getName() == null)
//										System.out.println(enzyme.getExternalIdentifier());
//									
									protein_id = ModelProteinsServices.insertProtein(this.databaseName, enzyme);
								}
								else
									protein_id = container.getIdProtein();

//								int protein_id=databaseInitialData.getProteinsIdentifier().get(enzyme.getExternalIdentifier());

								boolean exists = ModelReactionsServices.checkReactionHasEnzymeData(this.databaseName, protein_id, reaction_id);

								if(!exists){

									ModelReactionsServices.insertModelReactionHasModelProtein(this.databaseName, reaction_id, protein_id);

									enzyme.setIdProtein(protein_id);

								}
							}
						}
					}

					if(reactionContainer.getNames()!=null) {

						for(String synonym:reactionContainer.getNames()) {

							synonym = synonym.replace(";", "");

							boolean exists = ModelAliasesServices.checkEntityFromAliases(this.databaseName, "r", reaction_id, synonym);

							if(!exists) {

								ModelAliasesServices.insertNewModelAliasEntry(this.databaseName, "r", reaction_id, synonym);
							}
						}
					}

					if(reactionContainer.getPathways()!=null) {

						databaseInitialData.getReactionsPathway().put(reaction_id, reactionContainer.getAllPathwaysCodes());
						databaseInitialData.getReactionsPathwayList().add(reaction_id);
					}
				}
			}
			else {

				System.out.println("\t reaction "+reactionContainer.getExternalIdentifier()+" has unexisting metabolites.");
			}
		}

	}


	/**
	 * @param metabolite
	 * @param isReactant
	 * @param statement 
	 * @param reactionIdentifier 
	 * @throws Exception 
	 * @throws IOException 
	 */
	private void loadMetabolites(MetaboliteContainer metabolite, boolean isReactant, int reactionIdentifier) throws IOException, Exception {

		String metaboliteID = metabolite.getExternalIdentifier();

		double stoichiometry = metabolite.getStoichiometric_coefficient();

		if(databaseInitialData.getMetabolitesIdentifier().containsKey(metaboliteID)) {

			//				if(stoichiometry.contains("n")) {
			//
			//					String pattern = ".*\\d+n";
			//					Pattern r = Pattern.compile(pattern);
			//
			//					Matcher matcher = r.matcher(stoichiometry);
			//					if (matcher.find( ))
			//						stoichiometry = stoichiometry.replaceAll("n", "*1");
			//					else
			//						stoichiometry = stoichiometry.replaceAll("n", "1");
			//
			//					ScriptEngineManager factory = new ScriptEngineManager();
			//					ScriptEngine engine = factory.getEngineByName("JavaScript");
			//
			//					try {
			//
			//						stoichiometry = ""+engine.eval(stoichiometry);
			//					} 
			//					catch (ScriptException e) {
			//						e.printStackTrace();
			//					}
			//				}

			ScriptEngineManager factory = new ScriptEngineManager();
			ScriptEngine engine = factory.getEngineByName("JavaScript");

			try {

				stoichiometry = Double.valueOf(engine.eval(stoichiometry+"")+"");
			} 
			catch (ScriptException e) {
				e.printStackTrace();
			}
		}

		Integer metabolite_id = databaseInitialData.getMetabolitesIdentifier().get(metaboliteID.replace("-", ""));
		
		Integer res = ModelStoichiometryServices.getStoichiometryID(this.databaseName, reactionIdentifier, metabolite_id.toString(), databaseInitialData.getCompartmentIdentifier(), stoichiometry);

		if(res == null){

			ModelStoichiometryServices.insertStoichiometry(this.databaseName, reactionIdentifier, metabolite_id, databaseInitialData.getCompartmentIdentifier(), stoichiometry);
		}
	}

	/**
	 * @param moduleContainer
	 * @throws Exception 
	 */
	public void loadModule(ModuleContainer moduleContainer) throws Exception{

		if(!databaseInitialData.getModulesIdentifier().containsKey(moduleContainer.getExternalIdentifier())) {

			Integer id = ModelModuleServices.insertNewModelModule(this.databaseName, moduleContainer);

			databaseInitialData.getModulesIdentifier().put((moduleContainer.getExternalIdentifier()), id);

		}
		int moduleID = databaseInitialData.getModulesIdentifier().get((moduleContainer.getExternalIdentifier()));

		for(String orthologue:moduleContainer.getOrthologues()) {

			if(!databaseInitialData.getOrthologuesIdentifier().containsKey(orthologue)) {

				Integer res = ModelGenesServices.insertNewOrthologue(this.databaseName, orthologue, null);

				databaseInitialData.getOrthologuesIdentifier().put(orthologue,res);
			}

			boolean exists = ModelModuleServices.checkModelModuleHasModelOrthology(this.databaseName, moduleID, databaseInitialData.getOrthologuesIdentifier().get(orthologue));

			if(!exists) {
				ModelModuleServices.insertNewModelModuleHasModelOrthology(this.databaseName, moduleID, databaseInitialData.getOrthologuesIdentifier().get(orthologue));
			}
		}

		if(moduleContainer.getPathways()!=null) {

			databaseInitialData.getModulesPathway().put(moduleID, moduleContainer.getPathways().keySet());
			databaseInitialData.getModulesPathwayList().add(moduleID);
		}
	}

	/**
	 * @param pathwaysHierarchyContainer
	 * @param stmt
	 * @param databaseType
	 * @throws SQLException
	 */
	public void loadPathways(PathwaysHierarchyContainer pathwaysHierarchyContainer) throws Exception{

		if(this.importSBML)
			loadTransportersDrainsAndBiomassPathways(true);

		Integer res = ModelPathwaysServices.getPathwayIDbyName(this.databaseName, pathwaysHierarchyContainer.getSuper_pathway());

		if(res == null) {

			res = ModelPathwaysServices.insertModelPathwayCodeAndName(this.databaseName, "", pathwaysHierarchyContainer.getSuper_pathway());

		}

		int super_pathway_id = res;

		for(String intermediary_pathway: pathwaysHierarchyContainer.getPathways_hierarchy().keySet()) {

			res = ModelPathwaysServices.getPathwayIDbyName(this.databaseName, intermediary_pathway.trim());

			if(res == null) {

				res = ModelPathwaysServices.insertModelPathwayCodeAndName(this.databaseName, "", intermediary_pathway.trim());
			}

			int intermediary_pathway_id = res;

			boolean exists = ModelPathwaysServices.checkSuperPathwayData(this.databaseName, intermediary_pathway_id, super_pathway_id);
			
			if(!exists) {

				ModelPathwaysServices.insertModelPathwayIdAndSuperPathway(this.databaseName, intermediary_pathway_id, super_pathway_id);
			}

			for(String[] pathway: pathwaysHierarchyContainer.getPathways_hierarchy().get(intermediary_pathway)) {

				if(!databaseInitialData.getPathwaysIdentifier().contains(pathway[0])) {
					String aux = pathway[1].trim()+"' AND code='"+pathway[0];

					res = ModelPathwaysServices.getPathwayIDbyName(this.databaseName, aux);

					if(res == null) {

						res = ModelPathwaysServices.insertModelPathwayCodeAndName(this.databaseName,  pathway[0], pathway[1].trim());

					}
					databaseInitialData.getPathwaysIdentifier().put(pathway[0], res);
				}
				int pathway_id = databaseInitialData.getPathwaysIdentifier().get(pathway[0]);

				exists = ModelPathwaysServices.checkSuperPathwayData(this.databaseName, pathway_id, intermediary_pathway_id);
				if(!exists) {

					ModelPathwaysServices.insertModelPathwayIdAndSuperPathway(this.databaseName, pathway_id, intermediary_pathway_id);
				}
			}
		}
		//}
	}

	/**
	 * @param idreaction
	 * @param stmt
	 * @param databaseType
	 * @throws SQLException
	 */
	public void load_ReactionsPathway(int idreaction) throws Exception{

		for(String pathway:databaseInitialData.getReactionsPathway().get(idreaction)) {

			if(databaseInitialData.getPathwaysIdentifier().containsKey(pathway)) {

				boolean exists = ModelPathwaysServices.checkPathwayHasReactionData(this.databaseName, databaseInitialData.getPathwaysIdentifier().get(pathway), idreaction);

				if(!exists) {

					ModelPathwaysServices.insertModelPathwayHasModelReaction(this.databaseName, databaseInitialData.getPathwaysIdentifier().get(pathway), idreaction);

				}
			}
		}
	}

	/**
	 * @param ecnumber
	 * @throws SQLException
	 */
	public void load_EnzymesPathway(String ecnumber){

		for(String pathway : databaseInitialData.getEnzymesPathway().get(ecnumber)) {

			try {
				if(databaseInitialData.getProteinsIdentifier().get(ecnumber) != null && databaseInitialData.getPathwaysIdentifier().get(pathway) != null) {

					boolean exists = ModelPathwaysServices.checkPathwayHasModelProtein(this.databaseName, databaseInitialData.getPathwaysIdentifier().get(pathway),
							databaseInitialData.getProteinsIdentifier().get(ecnumber));

					if(!exists) {
						ModelPathwaysServices.insertModelPathwayHasModelProtein(this.databaseName, databaseInitialData.getPathwaysIdentifier().get(pathway), 
								databaseInitialData.getProteinsIdentifier().get(ecnumber));
					}
				}
			} catch (Exception e) {
				System.out.println("An error occurred while loading the keys pathwayID = " + databaseInitialData.getPathwaysIdentifier().get(pathway)
						+ " and proteinID = " + databaseInitialData.getProteinsIdentifier().get(ecnumber) + " to table model_pathway_has_model_protein!");
				//				e.printStackTrace();
			}
		}
	}

	/**
	 * @param moduleID
	 * @throws SQLException
	 */
	public void load_ModulePathway(int moduleID) throws Exception{

		for(String pathway:databaseInitialData.getModulesPathway().get(moduleID))
		{
			boolean exists = ModelPathwaysServices.checkModelPathwayHasModuleEntry(this.databaseName, databaseInitialData.getPathwaysIdentifier().get(pathway), moduleID);
			if(!exists)	{
				ModelPathwaysServices.insertModelPathwayHasModuleEntry(this.databaseName, databaseInitialData.getPathwaysIdentifier().get(pathway), moduleID);
			}
		}
	}

	/**
	 * @param metaboliteID
	 * @throws SQLException
	 */
	public void load_MetabolitePathway(int metaboliteID) throws Exception{

		for(String pathway:databaseInitialData.getMetabolitesPathway().get(metaboliteID)) {

			if(databaseInitialData.getPathwaysIdentifier().contains(pathway)) {
				boolean exists = ModelPathwaysServices.checkPathwayHasCompoundData(this.databaseName, databaseInitialData.getPathwaysIdentifier().get(pathway), metaboliteID);

				if(!exists) {

					ModelPathwaysServices.insertModelPathwayHasModelCompound(this.databaseName, databaseInitialData.getPathwaysIdentifier().get(pathway), metaboliteID);
				}
			}

		}
	}


	/////////////
	/**
	 * Insert into db transporters and drains pathways
	 * @param stmt
	 * @throws SQLException
	 */
	public void loadTransportersDrainsAndBiomassPathways(boolean loadBiomassPathway) throws Exception{

		int res = ModelPathwaysServices.getPathwayIDbyName(this.databaseName, Pathways.TRANSPORTERS.getName());
		if(res<0) {

			res = ModelPathwaysServices.insertModelPathwayCodeAndName(this.databaseName, Pathways.TRANSPORTERS.getCode(),  Pathways.TRANSPORTERS.getName());

		}

		databaseInitialData.getPathwaysIdentifier().put(Pathways.TRANSPORTERS.getCode(), res);

		res = ModelPathwaysServices.getPathwayIDbyName(this.databaseName, Pathways.DRAINS.getName());
		if(res<0) {

			res = ModelPathwaysServices.insertModelPathwayCodeAndName(this.databaseName, Pathways.DRAINS.getCode(),  Pathways.DRAINS.getName());
		}

		databaseInitialData.getPathwaysIdentifier().put(Pathways.DRAINS.getCode(), res);

		if(loadBiomassPathway){

			res = ModelPathwaysServices.getPathwayIDbyName(this.databaseName, Pathways.BIOMASS.getName());
			if(res<0) {

				res = ModelPathwaysServices.insertModelPathwayCodeAndName(this.databaseName, Pathways.BIOMASS.getCode(),  Pathways.BIOMASS.getName());
			}

			databaseInitialData.getPathwaysIdentifier().put(Pathways.BIOMASS.getCode(), res);

		}
	}


	/**
	 * @param compartments
	 * @param stmt
	 * @throws SQLException 
	 */
	public void loadCompartment(CompartmentContainer compartment) throws Exception{

		String compartmentName = compartment.getName();
		String abbreviation = compartment.getAbbreviation();

		CompartmentContainer container = ModelCompartmentServices.getCompartmentByName(this.databaseName , compartmentName);

		if(container == null) {
			ModelCompartmentServices.insertNameAndAbbreviation(this.databaseName, compartmentName, abbreviation);
		}
	}
	///////////////

	/**
	 * @param enzyme
	 * @return
	 */
	private static String getEnzymeClass(int enzymeClass){
		String classes = null;

		switch (enzymeClass)  {

		case 1:  classes = "Oxidoreductases";break;
		case 2:  classes = "Transferases";break;
		case 3:  classes = "Hydrolases";break;
		case 4:  classes = "Lyases";break;
		case 5:  classes = "Isomerases";break;
		case 6:  classes = "Ligases";break;
		}
		return classes;
	}

	/**
	 * @param compoundsWithBiologicalRoles the compoundsWithBiologicalRoles to set
	 */
	public void setCompoundsWithBiologicalRoles(
			ConcurrentLinkedQueue<String> compoundsWithBiologicalRoles) {
		this.compoundsWithBiologicalRoles = compoundsWithBiologicalRoles;
	}

	/**
	 * @return the compoundsWithBiologicalRoles
	 */
	public ConcurrentLinkedQueue<String> getCompoundsWithBiologicalRoles() {
		return compoundsWithBiologicalRoles;
	}

	/**
	 * @return the locusTagsToProteinIdsMap
	 */
	public Map<String, String> getLocusTagsToProteinIdsMap() {
		return locusTagsToProteinIdsMap;
	}

	/**
	 * @param locusTagsToProteinIdsMap the locusTagsToProteinIdsMap to set
	 */
	public void setLocusTagsToProteinIdsMap(Map<String, String> locusTagsToProteinIdsMap) {
		this.locusTagsToProteinIdsMap = locusTagsToProteinIdsMap;
	}

	/**
	 * @return the databaseInitialData
	 */
	public WorkspaceInitialData getDatabaseInitialData() {
		return databaseInitialData;
	}

	/**
	 * @param databaseInitialData the databaseInitialData to set
	 */
	public void setDatabaseInitialData(WorkspaceInitialData databaseInitialData) {
		this.databaseInitialData = databaseInitialData;
	}

}