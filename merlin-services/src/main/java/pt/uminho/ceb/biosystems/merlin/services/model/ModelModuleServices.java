package pt.uminho.ceb.biosystems.merlin.services.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.gpr.GeneAssociation;
import pt.uminho.ceb.biosystems.merlin.core.containers.gpr.ModuleCI;
import pt.uminho.ceb.biosystems.merlin.core.containers.gpr.ReactionProteinGeneAssociation;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModuleContainer;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;

/**
 * @author BioSystems
 *
 */
public class ModelModuleServices {

	/**
	 * @param databaseName
	 * @param gene
	 * @param protein_id
	 * @param ecnumber
	 * @return
	 * @throws Exception
	 */
	public static boolean checkModules(String databaseName, int gene, int protein_id) throws Exception{
		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkModules(gene, protein_id);
	}

	/**
	 * @param databaseName
	 * @param geneId
	 * @param proteinId
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public static boolean checkModulesByModuleId(String databaseName, int geneId, int proteinId, int moduleId) throws Exception {

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkModulesByModuleId(geneId, proteinId, moduleId);
	}

	/**
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Integer> getModelModuleEntryIdAndId(String databaseName) throws Exception{
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModelModuleEntryIdAndId();
	}

	/**
	 * @param databaseName
	 * @param container
	 * @return
	 * @throws Exception
	 */
	public static Integer insertNewModelModule(String databaseName, ModuleContainer container) throws Exception{
		return InitDataAccess.getInstance().getDatabaseService(databaseName).insertNewModelModule(container);
	}

	/**
	 * @param databaseName
	 * @param moduleId
	 * @param orthologyId
	 * @return
	 * @throws Exception
	 */
	public static void insertNewModelModuleHasModelOrthology(String databaseName, Integer moduleId, Integer orthologyId) throws Exception{
		InitDataAccess.getInstance().getDatabaseService(databaseName).insertNewModelModuleHasModelOrthology(moduleId, orthologyId);
	}

	/**
	 * @param databaseName
	 * @param moduleId
	 * @param orthologyId
	 * @return
	 * @throws Exception
	 */
	public static boolean checkModelModuleHasModelOrthology(String databaseName, Integer moduleId, Integer orthologyId) throws Exception{
		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkModelModuleHasModelOrthology(moduleId, orthologyId);
	}

	/**
	 * @param databaseName
	 * @param entryId
	 * @param reaction
	 * @param definition
	 * @return
	 * @throws Exception
	 */
	public static Integer getModuleIdByReactionAndDefinitionAndEntryId(String databaseName, String entryId, String reaction, String definition) throws Exception{
		return InitDataAccess.getInstance().getDatabaseService(databaseName).getModuleIdByReactionAndDefinitionAndEntryId(entryId, reaction, definition);
	}

	/**
	 * Load module to model.
	 * 
	 * @param databaseName
	 * @param result
	 * @throws Exception 
	 */
	public static Map<String, Set<Integer>> loadModule(String databaseName, Map<String, List<ReactionProteinGeneAssociation>> result) throws Exception {

		Map<String, Set<Integer>> genes_ko_modules = new HashMap<>();

		for(String reaction: result.keySet()) {

			for(int i=0; i<result.get(reaction).size(); i++) {

				for(String ecnumber : result.get(reaction).get(i).getProteinGeneAssociation().keySet()) {

					List<GeneAssociation> genes_list = result.get(reaction).get(i).getProteinGeneAssociation().get(ecnumber).getGenes();

					String definition = "";

					for(int index_list = 0; index_list< genes_list.size(); index_list++) {

						GeneAssociation g = genes_list.get(index_list);

						if(index_list!=0)
							definition += " OR ";

						for(int index = 0; index< g.getGenes().size(); index++) {

							String gene  = g.getGenes().get(index);

							if(index!=0)
								definition += " AND ";  

							definition += gene;
						}
					}

					for(GeneAssociation geneAssociation : genes_list) {

						for(ModuleCI mic : geneAssociation.getModules().values()) {

							Integer idModule = getModuleIdByReactionAndDefinitionAndEntryId(databaseName, mic.getModule(), reaction, definition);

							if(idModule == null) {

								ModuleContainer container = new ModuleContainer(mic.getModule());

								container.setReaction(reaction);
								container.setName(mic.getName());
								container.setDefinition(definition);
								container.setModuleType(mic.getModuleType().toString());

								idModule = insertNewModelModule(databaseName, container);

							}

							for(String entryId : geneAssociation.getGenes()) {

								Map<String, Integer> orthologues = ModelGenesServices.getEntryIdAndOrthologyId(databaseName);

								Set<Integer> ids = new HashSet<>();

								ids.addAll(orthologues.values());

								if(!orthologues.keySet().contains(entryId)) { 

									Integer id = ModelGenesServices.insertNewOrthologue(databaseName, entryId, null);

									ids.add(id);
								}

								for (int idOrtholog : ids) {

									boolean exists = checkModelModuleHasModelOrthology(databaseName, idModule, idOrtholog);
									if(!exists)
										insertNewModelModuleHasModelOrthology(databaseName, idModule, idOrtholog);


									Set<Integer> modules = new HashSet<>();

									if(genes_ko_modules.containsKey(entryId))
										modules = genes_ko_modules.get(entryId);

									modules.add(idModule);
									genes_ko_modules.put(entryId, modules);
								}
							}
						}
					}
				}
			}
		}
		return genes_ko_modules;
	}

	/**
	 * @param databaseName
	 * @param proteinId
	 * @param moduleId
	 * @throws Exception
	 */
	public static void insertModelModuleHasModelProtein(String databaseName, Integer proteinId, Integer moduleId) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).insertModelModuleHasModelProtein(proteinId, moduleId);

	}

	public static boolean checkModelModuletHasProteinData(String databaseName,Integer proteinId, Integer moduleId) throws Exception{

		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkModelModuletHasProteinData(proteinId, moduleId);

	}

}
