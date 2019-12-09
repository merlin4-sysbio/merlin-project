/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.processes.model.kegg;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.KeggAPI;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.KeggOperation;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.KeggRestful;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModuleContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwaysHierarchyContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


/**
 * @author ODias
 *
 */
public class KeggDataRetrieverRunnable implements Runnable, PropertyChangeListener{

	final static Logger logger = LoggerFactory.getLogger(KeggDataRetrieverRunnable.class);

	private ConcurrentHashMap<String,MetaboliteContainer> resultMetabolites;
	private ConcurrentHashMap<String,ProteinContainer> resultEnzymes;
	private ConcurrentHashMap<String,ReactionContainer> resultReactions;
	private ConcurrentHashMap<String,GeneContainer> resultGenes;
	private ConcurrentHashMap<String,ModuleContainer> resultModules;
	private String organismID;
	private EntityType entityTypeString;
	private ConcurrentLinkedQueue<String> entity;

	private int errorCount;
	private AtomicBoolean cancel;
	private int dataSize;
	private AtomicInteger datum;

	private PropertyChangeSupport changes;

	/**
	 * Run Kegg metabolic data retriever concurrently.
	 * 
	 * @param entity
	 * @param organismID
	 * @param entityTypeString
	 * @param resultMetabolites
	 * @param resultEnzymes
	 * @param resultReactions
	 * @param resultModules
	 * @param cancel
	 * @param dataSize
	 * @param datum
	 */
	public KeggDataRetrieverRunnable(
			ConcurrentLinkedQueue<String> entity,
			String organismID,
			EntityType entityTypeString,
			ConcurrentHashMap<String, MetaboliteContainer> resultMetabolites,
			ConcurrentHashMap<String, ProteinContainer> resultEnzymes, 
			ConcurrentHashMap<String, ReactionContainer> resultReactions, 
			ConcurrentHashMap<String, ModuleContainer> resultModules,
			AtomicBoolean cancel, int dataSize, AtomicInteger datum) {

		this.changes = new PropertyChangeSupport(this);
		this.setEntity(entity);
		this.setEntityTypeString(entityTypeString);
		this.setOrganismID(organismID);
		this.setResultMetabolites(resultMetabolites);
		this.setResultEnzymes(resultEnzymes);
		this.setResultReactions(resultReactions);
		this.setResultModules(resultModules);
		this.cancel = cancel;
		this.dataSize = dataSize;
		this.datum = datum;
	}

	/**
	 * Constructor for retrieving genes annotation
	 * 
	 * @param organismID
	 * @param entityTypeString
	 * @param resultGenes
	 * @param resultEnzymes
	 * @param resultReactions
	 * @param cancel
	 * @param dataSize
	 * @param datum
	 */
	public KeggDataRetrieverRunnable(
			ConcurrentLinkedQueue<String> entity,
			String organismID, 
			EntityType entityTypeString,
			ConcurrentHashMap<String, GeneContainer> resultGenes, 
			ConcurrentHashMap<String, ProteinContainer> resultEnzymes, 
			ConcurrentHashMap<String, ReactionContainer> resultReactions,
			AtomicBoolean cancel, int dataSize, AtomicInteger datum) {

		this.changes = new PropertyChangeSupport(this);
		this.setEntity(entity);
		this.setEntityTypeString(entityTypeString);
		this.setOrganismID(organismID);
		this.setResultGenes(resultGenes);
		this.setResultEnzymes(resultEnzymes);
		this.setResultReactions(resultReactions);
		this.cancel = cancel;
		this.dataSize = dataSize;
		this.datum = datum;
	}


	@Override
	public void run() {

		List<String> poooledEntities=new ArrayList<String>();
		String poooledEntity;

		String message = "Getting " + entityTypeString;

		try  {

			if(entityTypeString.equals(EntityType.Compound)){// || entityTypeString.equals(EntityType.Drugs) || entityTypeString.equals(EntityType.Glycan)) {

				while(entity.size()>0 && !this.cancel.get()) {

					poooledEntity = entity.poll();
					poooledEntities.add(poooledEntity);
					String query = poooledEntity;
					int i=0;

					while(i<9 && entity.size()>0) {

						poooledEntity= entity.poll();
						poooledEntities.add(poooledEntity);
						query=query.concat("+"+poooledEntity);
						i++;
					}

					Map<String, MetaboliteContainer> ret = getMetabolitesArray(KeggRestful.fetch(KeggOperation.get,query));
					this.resultMetabolites.putAll(ret);
					this.changes.firePropertyChange("size", -1, dataSize);
					this.changes.firePropertyChange("sequencesCounter", datum.get(), datum.addAndGet(ret.size()));
					this.changes.firePropertyChange("message", "", message);

					poooledEntities=new ArrayList<String>();
				}
			}

			if(entityTypeString.equals(EntityType.Enzyme)) {

				while(entity.size()>0 && !this.cancel.get()) {

					poooledEntity= entity.poll();
					poooledEntities.add(poooledEntity);
					String query = poooledEntity;
					int i=0;

					while(i<9 && entity.size()>0) {

						poooledEntity= entity.poll();
						poooledEntities.add(poooledEntity);
						query=query.concat("+"+poooledEntity);
						i++;
					}

					Map<String, ProteinContainer> ret = this.getEnzymesArray(KeggRestful.fetch(KeggOperation.get,query),organismID);
					this.resultEnzymes.putAll(ret);
					this.changes.firePropertyChange("size", -1, dataSize);
					this.changes.firePropertyChange("sequencesCounter", datum.get(), datum.addAndGet(ret.size()));
					this.changes.firePropertyChange("message", "", message);

					poooledEntities=new ArrayList<String>();
				}
			}

			if(entityTypeString.equals(EntityType.Reaction)) {

				while(entity.size()>0 && !this.cancel.get()) {

					poooledEntity= entity.poll(); 
					poooledEntities.add(poooledEntity);
					String query = poooledEntity;
					int i=0;

					while(i<9 && entity.size()>0) {

						poooledEntity= entity.poll();
						poooledEntities.add(poooledEntity);
						query=query.concat("+"+poooledEntity);
						i++;
					}

					Map<String, ReactionContainer> ret = this.getReactionsArray(KeggRestful.fetch(KeggOperation.get,query));
					this.resultReactions.putAll(ret);
					this.changes.firePropertyChange("size", -1, dataSize);
					this.changes.firePropertyChange("sequencesCounter", datum.get(), datum.addAndGet(ret.size()));
					this.changes.firePropertyChange("message", "", message);

					poooledEntities=new ArrayList<String>();

				}
				
			}

			if(entityTypeString.equals(EntityType.Gene)) {

				while(entity.size()>0 && !this.cancel.get()) {

					poooledEntity= entity.poll();
					poooledEntities.add(poooledEntity);
					String query = poooledEntity;
					int i=0;

					while(i<9 && entity.size()>0 && !this.cancel.get()) {

						poooledEntity= entity.poll();
						poooledEntities.add(poooledEntity);
						query=query.concat("+"+poooledEntity);
						i++;

					}

					Map<String, GeneContainer> ret = this.getGeneArray(KeggRestful.fetch(KeggOperation.get,query));
					this.resultGenes.putAll(ret);
					this.changes.firePropertyChange("size", -1, dataSize);
					this.changes.firePropertyChange("sequencesCounter", datum.get(), datum.addAndGet(ret.size()));
					this.changes.firePropertyChange("message", "", message);

					poooledEntities=new ArrayList<String>();
				}
			}

			if(entityTypeString.equals(EntityType.Module)) {

				while(entity.size()>0 && !this.cancel.get()) {

					poooledEntity= entity.poll();
					poooledEntities.add(poooledEntity);
					String query = poooledEntity;
					int i=0;

					while(i<9 && entity.size()>0 && !this.cancel.get()) {

						poooledEntity= entity.poll();
						poooledEntities.add(poooledEntity);
						query=query.concat("+"+poooledEntity);
						i++;
					}

					Map<String, ModuleContainer> ret = this.getStructuralComplexArray(KeggRestful.fetch(KeggOperation.get,query));
					this.resultModules.putAll(ret);
					this.changes.firePropertyChange("size", -1, dataSize);
					this.changes.firePropertyChange("sequencesCounter", datum.get(), datum.addAndGet(ret.size()));
					this.changes.firePropertyChange("message", "", message);

					poooledEntities=new ArrayList<String>();
				}
			}
		}
		catch (Exception e)  {

			e.printStackTrace();

			if(this.errorCount<10) {

				this.errorCount += 1;
				this.entity.addAll(poooledEntities);
				//System.err.println("\n\n\n\n\n\n\n\nTEM DE SE FAZER A REQUERY senao perdemos dados!!!!\n\n\n\n\n\n\n\n");
				logger.error("queries read {}", poooledEntities);

				try {

					Thread.sleep(60000);

				} 
				catch (InterruptedException e1){

					Thread.currentThread().interrupt();
				}

				this.run();
			}
			else {

				e.printStackTrace();
			}
		}

	}

	/**
	 * @param resultArray
	 * @return
	 */
	private Map<String ,MetaboliteContainer> getMetabolitesArray(String resultArray) {

		this.errorCount=0;
		Map<String ,MetaboliteContainer> result = new HashMap<String, MetaboliteContainer>();

		for(String results : resultArray.split("///")) {

			results=results.trim();
			if(!results.isEmpty()) {

				results=results.concat("\n///");
				Map<String, List<String>> resultsParsed = KeggAPI.parseFullEntry(results);
				String entry = KeggAPI.getFirstIfExists(KeggAPI.splitWhiteSpaces(resultsParsed.get("ENTRY")));

				if(entry!=null) {

					MetaboliteContainer metaboliteContainer = new MetaboliteContainer(entry);

					metaboliteContainer.setFormula(KeggAPI.getFirstIfExists(resultsParsed.get("FORMULA")));
					metaboliteContainer.setMolecular_weight(KeggAPI.getFirstIfExists(resultsParsed.get("MOL_WEIGHT")));
					String rawName = KeggAPI.getFirstIfExists(resultsParsed.get("NAME"));

					if(rawName != null) {

						metaboliteContainer.setName(rawName.replace(";", "").trim());
					}
					String same_as = KeggAPI.getFirstIfExists(resultsParsed.get("REMARK"));

					if(same_as != null) {

						if(same_as.contains("Same as:")) metaboliteContainer.setSame_as(same_as.split(":")[1].trim());
					}

					List<String> names = resultsParsed.get("NAME");
					if(names == null) names = new ArrayList<String>();
					names.remove(rawName);
					metaboliteContainer.setNames(names);

					List<String> ecnumbers = KeggAPI.splitWhiteSpaces(resultsParsed.get("ENZYME"));
					if(ecnumbers == null) ecnumbers = new ArrayList<String>();
					metaboliteContainer.setEnzymes(ecnumbers);

					List<String> reactions = KeggAPI.splitWhiteSpaces(resultsParsed.get("REACTION"));
					if(reactions == null) reactions = new ArrayList<String>();
					metaboliteContainer.setReactions(reactions);

					List<String> pathwaysData = resultsParsed.get("PATHWAY");

					if(pathwaysData != null) {

						Map<String, String> pathways = new HashMap<String, String>();

						for(String path: pathwaysData) {

							pathways.put(path.split("\\s")[0].replace("ko", "").replace("map", ""), path.replace(path.split("\\s")[0], "").trim());
						}
						metaboliteContainer.setPathways(pathways);

						//System.out.println(entry+"\t"+pathways);
					}

					List<String> crossRefs = resultsParsed.get("DBLINKS");
					if(crossRefs == null) crossRefs = new ArrayList<String>();
					metaboliteContainer.setDblinks(crossRefs);

					result.put(entry,metaboliteContainer);
					////System.out.println(result.get(entry).getEntry());
					////System.out.println(result.get(entry));
				}
			}
		}
		return result;
	}

	/**
	 * @param resultArray
	 * @return
	 */
	private Map<String ,GeneContainer> getGeneArray(String resultArray) {

		this.errorCount=0;
		Map<String ,GeneContainer> result = new HashMap<String, GeneContainer>();

		for(String results : resultArray.split("///")) {

			results=results.trim();

			if(!results.isEmpty()) {

				results=results.concat("\n///");
				Map<String, List<String>> resultsParsed = KeggAPI.parseFullEntry(results);
				String entry = KeggAPI.getFirstIfExists(KeggAPI.splitWhiteSpaces(resultsParsed.get("ENTRY")));

				if(entry!=null)  {

					GeneContainer geneContainer = new GeneContainer(entry);

					String rawName = KeggAPI.getFirstIfExists(resultsParsed.get("NAME"));

					if(rawName != null) {

						geneContainer.setName(rawName.replace(";", "").trim());
					}

					List<String> names = resultsParsed.get("NAME");

					if(names == null)
						names = new ArrayList<String>();

					names.remove(rawName);
					geneContainer.setNames(names);


					List<String> orthologyData = resultsParsed.get("ORTHOLOGY");

					if(orthologyData != null) {

						List<String> orthologues = new ArrayList<String>();

						for(String orthologue: orthologyData) {

							orthologues.add(orthologue.split("\\s")[0]);
						}
						geneContainer.setOrthologues(orthologues);
					}


					List<String> modulesData = resultsParsed.get("MODULE");

					if(modulesData != null) {

						List<String> modules = new ArrayList<String>();

						for(String module: modulesData) {

							modules.add(module.split("\\s")[0]);
						}
						geneContainer.setModules(modules);
					}

					String position = KeggAPI.getFirstIfExists(resultsParsed.get("POSITION"));

					if(position != null && position.contains(":")) {

						String[] position_array = position.split(":");

						if(position_array.length>1) {

							//							String chromosome_name = position_array[0];
							//							geneContainer.setChromosome_name(chromosome_name);
							//position = position.replace(chromosome_name+":", "").trim();
							position = position_array[1];
						}

						position = position.replaceFirst("\\.\\.", ":");
						geneContainer.setLeft_end_position(position.split(":")[0]);
						geneContainer.setRight_end_position(position.split(":")[1]);
					}

					List<String> sequenceAAData = resultsParsed.get("AASEQ");

					if(sequenceAAData != null) {
						geneContainer.setAalength(Integer.valueOf(sequenceAAData.get(0)));
						sequenceAAData.remove(0);
						String aaSequence = "";
						for (String aa :sequenceAAData) 
						{
							aaSequence=aaSequence.concat(aa);
						}
						geneContainer.setAasequence(aaSequence);
					}

					List<String> sequenceNTData = resultsParsed.get("NTSEQ");

					if(sequenceNTData != null) {

						geneContainer.setNtlength(Integer.valueOf(sequenceNTData.get(0)));
						sequenceNTData.remove(0);
						String ntSequence = "";

						for (String nt :sequenceNTData)  {

							ntSequence=ntSequence.concat(nt);
						}
						geneContainer.setNtsequence(ntSequence);
					}

					List<String> crossRefs = resultsParsed.get("DBLINKS");
					if(crossRefs == null) crossRefs = new ArrayList<String>();
					geneContainer.setDblinks(crossRefs);

					result.put(entry,geneContainer);
				}
			}
		}
		return result;
	}


	/**
	 * @param resultArray
	 * @param organismID
	 * @return
	 */
	private Map<String,ProteinContainer> getEnzymesArray(String resultArray, String organismID) {

		this.errorCount=0;
		Map<String ,ProteinContainer> result = new TreeMap<String, ProteinContainer>();

		for(String results : resultArray.split("///")) {

			results=results.trim();
			if(!results.isEmpty()) {

				results=results.concat("\n///");
				Map<String, List<String>> resultsParsed = KeggAPI.parseFullEntry(results);
				String entry = KeggAPI.getSecondIfExists(KeggAPI.splitWhiteSpaces(resultsParsed.get("ENTRY")));

				if(entry!=null) {

					ProteinContainer enzymeContainer = new ProteinContainer(entry.replace("EC ", "").trim());

					List<String> names = resultsParsed.get("NAME");
					if(names == null) names = new ArrayList<String>();
					enzymeContainer.setNames(names);


					enzymeContainer.setName(KeggAPI.getFirstIfExists(resultsParsed.get("SYSNAME")));

					if(enzymeContainer.getName()==null) {

						if(!enzymeContainer.getNames().isEmpty()) {

							enzymeContainer.setName(enzymeContainer.getNames().get(0));
							names = enzymeContainer.getNames();
							names.remove(enzymeContainer.getName());
							enzymeContainer.setNames(names);
						}
					}

					enzymeContainer.setClass_(KeggAPI.getFirstIfExists(resultsParsed.get("CLASS")));

					List<String> genesList = resultsParsed.get("GENES");
					List<String> genes = new ArrayList<String>();

					if(genesList != null) {

						for(int i=0; i<genesList.size(); i++) {

							String gene = genesList.get(i);		

							if(gene.startsWith(organismID.toUpperCase()+":")) {

								gene=gene.split(":")[1].trim();

								for(String geneID: gene.split("\\s")) {

									if(geneID.contains("(")) {

										geneID=geneID.split("\\(")[0].trim();
									}
									genes.add(geneID.trim());
									i= genesList.size();
								}
								enzymeContainer.setGenes(genes);
							}
						}
					}

					List<String> cofactorsList = resultsParsed.get("COFACTOR");
					List<String> cofactors = new ArrayList<String>();

					if(cofactorsList!=null) {

						for(String cofactor:cofactorsList) {

							cofactors.add(cofactor.split(":")[1].replace("]", "").replace(";", "").trim());
						}
						enzymeContainer.setCofactors(cofactors);
					}

					List<String> reactions = resultsParsed.get("ALL_REAC");
					if(reactions == null) reactions = new ArrayList<String>();
					enzymeContainer.setReactionsExternalIdentifiers(reactions);

					List<String> pathwaysData = resultsParsed.get("PATHWAY");

					if(pathwaysData != null) {

						Map<String, String> pathways = new HashMap<String, String>();

						for(String path: pathwaysData) {

							pathways.put(path.split("\\s")[0].replace("ec", ""), path.replace(path.split("\\s")[0], "").trim());
						}
						enzymeContainer.setPathways(pathways);
					}

					List<String> orthologyData = resultsParsed.get("ORTHOLOGY");

					if(orthologyData != null) {

						List<String> orthologues = new ArrayList<String>();

						for(String orthologue: orthologyData) {

							orthologues.add(orthologue.split("\\s")[0]);
						}
						enzymeContainer.setOrthologues(orthologues);
					}

					List<String> crossRefs = resultsParsed.get("DBLINKS");
					if(crossRefs == null) crossRefs = new ArrayList<String>();
					enzymeContainer.setDblinks(crossRefs);

					result.put(entry,enzymeContainer);
				}
			}
		}
		return result;
	}


	/**
	 * @param resultArray
	 * @return
	 */
	private Map<String,ReactionContainer> getReactionsArray(String resultArray) {

		this.errorCount=0;
		//System.out.println(resultArray);
		Map<String,ReactionContainer> result = new TreeMap<String, ReactionContainer>();

		for(String results : resultArray.split("///")) {

			results=results.trim();
			if(!results.isEmpty()) {

				results=results.concat("\n///");
				Map<String, List<String>> resultsParsed = KeggAPI.parseFullEntry(results);
				String entry = KeggAPI.getFirstIfExists(KeggAPI.splitWhiteSpaces(resultsParsed.get("ENTRY")));

				if(entry!=null) {

					ReactionContainer reactionContainer = new ReactionContainer(entry);

//					String reactionName = KeggAPI.getFirstIfExists(resultsParsed.get("NAME"));
//					if(reactionName!=null)
//						reactionContainer.addName(reactionName);

					List<String> names = resultsParsed.get("NAME");
					if(names == null) {

						names = new ArrayList<String>();
					}
					reactionContainer.setNames(names);

					reactionContainer.setEquation(KeggAPI.getFirstIfExists(resultsParsed.get("DEFINITION")));

					String stoichiometry= KeggAPI.getFirstIfExists(resultsParsed.get("EQUATION"));
					String[] data=stoichiometry.split("<=>");
					String[] reactants = data[0].split("\\s\\+\\s");
					String[] products = data[1].split("\\s\\+\\s");

					Pair<List<MetaboliteContainer>, Boolean> aux = this.parseReactions(reactants,true);
					
					reactionContainer.setReactantsStoichiometry(aux.getA());
					
					if(aux.getB())
						reactionContainer.setNotes("unbalanced");
					
					aux = this.parseReactions(products,false);
					
					reactionContainer.setProductsStoichiometry(aux.getA());
					
					if(aux.getB())
						reactionContainer.setNotes("unbalanced");

					List<String> enzymesTemp = KeggAPI.splitWhiteSpaces(resultsParsed.get("ENZYME"));

					Set<String> enzymes = new HashSet<String>();
					if(enzymesTemp != null)
						enzymes = new HashSet<String>(enzymesTemp);

					for(String enzyme:enzymes)
						reactionContainer.addProteins(enzyme);

					List<String> genericsTemp = resultsParsed.get("COMMENT");

					if(genericsTemp!=null) {

						for(String generic:genericsTemp) {

							if(generic.trim().toLowerCase().contains("general reaction"))
								reactionContainer.setGeneric(true);

							if(generic.trim().toLowerCase().contains("non-enzymatic") || generic.trim().toLowerCase().contains("non enzymatic"))
								reactionContainer.setNon_enzymatic(true);

							if(generic.trim().toLowerCase().contains("spontaneous"))
								reactionContainer.setSpontaneous(true);
						}
						List<String> generics = new ArrayList<>(genericsTemp);
						reactionContainer.setComments(generics);
					}

					List<String> pathwaysData = resultsParsed.get("PATHWAY");

					if(pathwaysData != null) {

						for(String path: pathwaysData)
						{
							reactionContainer.addPathway(path.split("\\s")[0].replace("rn", ""), path.replace(path.split("\\s")[0], "").trim());
						}
					}

					List<String> crossRefs = resultsParsed.get("DBLINKS");

					if(crossRefs == null) {

						crossRefs = new ArrayList<String>();
					}
					reactionContainer.setDblinks(crossRefs);

					result.put(entry,reactionContainer);
				}
			}
		}
		return result;
	}


	/**
	 * @param resultArray
	 * @return
	 */
	private Map<String,ModuleContainer> getStructuralComplexArray(String resultArray) {

		this.errorCount=0;
		Map<String,ModuleContainer> result = new TreeMap<String, ModuleContainer>();
		for(String results : resultArray.split("///"))
		{
			results=results.trim();
			if(!results.isEmpty())
			{
				results=results.concat("\n///");
				Map<String,List<String>> resultsParsed = KeggAPI.parseFullEntry(results);
				String entry = KeggAPI.getFirstIfExists(KeggAPI.splitWhiteSpaces(resultsParsed.get("ENTRY")));
				String moduleType = KeggAPI.getSecondIfExists(KeggAPI.splitWhiteSpaces(resultsParsed.get("ENTRY")));
				if(entry!=null && moduleType.toLowerCase().equals("complex"))
				{
					ModuleContainer moduleContainer = new ModuleContainer(entry);
					moduleContainer.setName(KeggAPI.getFirstIfExists(resultsParsed.get("NAME")));
					moduleContainer.setDefinition(KeggAPI.getFirstIfExists(resultsParsed.get("DEFINITION")));
					moduleContainer.setModuleType(moduleType);

					List<String> orthologues = KeggAPI.splitLinesGetOrthologues(resultsParsed.get("ORTHOLOGY"));
					moduleContainer.setOrthologues(orthologues);


					List<String> comments = resultsParsed.get("COMMENT");
					if(comments!=null)
					{
						for(String comment:comments)
						{
							if(comment.trim().startsWith("Stoichiometry"))
							{
								moduleContainer.setStoichiometry(comment.replace("Stoichiometry: ", "").trim());
							}

							if(comment.trim().startsWith("Substrate"))
							{
								List<String> data = new ArrayList<String>();
								String[] substrates = comment.replace("Substrate: ", "").split(",");
								Pattern pat = Pattern.compile("[C|G|D|K]\\d{5}");
								Matcher m;
								for(String substrate:substrates)
								{
									m = pat.matcher(substrate);
									if (m.find()) 
									{
										data.add(m.group());				
									}
								}
								if(!data.isEmpty())
								{
									moduleContainer.setSubstrates(data);
								}
							}
						}
					}

					List<String> pathwaysData = resultsParsed.get("PATHWAY");
					if(pathwaysData != null)
					{
						Map<String, String> pathways = new HashMap<String, String>();
						for(String path: pathwaysData)
						{
							pathways.put(path.split("\\s")[0].replace("map", ""), path.replace(path.split("\\s")[0], "").trim());
						}
						moduleContainer.setPathways(pathways);
					}
					String moduleHieralchicalClass = KeggAPI.getFirstIfExists(resultsParsed.get("CLASS"));
					moduleContainer.setModuleHieralchicalClass(moduleHieralchicalClass);

					result.put(entry,moduleContainer);
					//					//System.out.println(result.get(entry).getEntry());
					//					//System.out.println(result.get(entry));
					//					//System.out.println(module_entry);

				}
			}

		}
		return result;
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public static ConcurrentLinkedQueue<String[]> get_All_Kegg_Pathways() throws Exception {

		//long startTime = System.currentTimeMillis();

		List<String[]> pathways = KeggAPI.getPathways();
		ConcurrentLinkedQueue<String[]> result = new ConcurrentLinkedQueue<String[]>();

		for(int index = 0; index<pathways.size(); index++) {

			String[] data = new String[2];
			data[0]=pathways.get(index)[1].replace("map", "");
			data[1]=pathways.get(index)[0].replace(" - Reference pathway", "").trim();
			result.add(data);
		}

		return result;
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public static ConcurrentLinkedQueue<PathwaysHierarchyContainer> get_Kegg_Pathways_Hierarchy_And_Exclude_Unwanted_Data(boolean includeEmptyPathways, 
			List<String> pathwaysToExclude) throws Exception {

		Set<String> pathwaysWithReactions = null;

		if(!includeEmptyPathways)
			pathwaysWithReactions = KeggAPI.getPathwaysContainingReactions();
		
		//long startTime = System.currentTimeMillis();
		ConcurrentLinkedQueue<PathwaysHierarchyContainer> result = new ConcurrentLinkedQueue<PathwaysHierarchyContainer>();
		String resultString = KeggAPI.getXMLDataFromBriteID("br08901");	
		//System.out.println(resultString);
		
//		for(String path : pathwaysWithReactions)
//			System.out.println(path);

		String[] auxLines = resultString.split("\n");
		
		List<String> lines = new ArrayList<String>();
		
		for(int i = 0; i < auxLines.length; i++) {
			
			String data = auxLines[i];
			
			if(data.startsWith("C")) {
				try {
					String pathCode = data.split("\\s+")[2];
					
					if(!pathwaysToExclude.contains(pathCode) && !pathwaysToExclude.contains("map" + pathCode));
						lines.add(data);
				} 
				catch (Exception e) {
					e.printStackTrace();
					lines.add(data);	//no data is lost
				}
			}
			else {
				lines.add(data);
			}
		}
		
		String key=null;
		PathwaysHierarchyContainer pathwaysHierarchyContainer=null;
		Map<String,List<String[]>> pathways_hierarchy_map = null;
		List<String[]> pathways=null;

		for (int i = 0; i < lines.size(); i++)  {

			String data = lines.get(i);

			if(data.startsWith("A")) {

				if(pathwaysHierarchyContainer!=null) {

					pathways_hierarchy_map.put(key, pathways);
					pathwaysHierarchyContainer.setPathwaysHierarchy(pathways_hierarchy_map);
					result.add(pathwaysHierarchyContainer);
				}
				pathwaysHierarchyContainer = new PathwaysHierarchyContainer(data.substring(1));
				pathways_hierarchy_map = new HashMap<String, List<String[]>>();
				key=null;
			}
			else {

				if(data.startsWith("B")) {

					if(key!=null) {

						pathways_hierarchy_map.put(key, pathways);
						pathwaysHierarchyContainer.setPathwaysHierarchy(pathways_hierarchy_map);
					}
					key=data.substring(1).trim();
					pathways = new ArrayList<String[]>();
				}
				else {

					if(data.startsWith("C")) {

						String[] path = new String[2];
						path[0]=data.substring(1).trim().split("\\s")[0].trim();
						path[1]=data.substring(1).trim().replace(path[0],"").trim();

						if(includeEmptyPathways || pathwaysWithReactions.contains(path[0]))
							pathways.add(path);												
					}
					else {

						if(data.equals("///")) {

							pathways_hierarchy_map.put(key, pathways);
							pathwaysHierarchyContainer.setPathwaysHierarchy(pathways_hierarchy_map);
							result.add(pathwaysHierarchyContainer);
						}
					}
				}
			}
		}
		//long endTime = System.currentTimeMillis();
		//System.out.println("Total elapsed time in execution of method callMethod() is :"+ String.format("%d min, %d sec", 
		//	TimeUnit.MILLISECONDS.toMinutes(endTime-startTime),TimeUnit.MILLISECONDS.toSeconds(endTime-startTime) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime-startTime))));
		return result;

	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public static ConcurrentLinkedQueue<String> getStructuralComplexModules() throws Exception {

		String resultString = KeggAPI.getXMLDataFromBriteID("ko00002");
		String[] lines = resultString.split("\n");

		ConcurrentLinkedQueue<String> result = new ConcurrentLinkedQueue<String>();
		for (int i = 0; i < lines.length; i++) 
		{
			String data = lines[i];
			if(data.startsWith("AStructural Complex"))
			{
				i++;
				data = lines[i];
				while(!data.startsWith("#"))
				{
					if(data.startsWith("B"))
					{
						i++;
						data = lines[i];
					}
					else
					{
						if(data.startsWith("C"))
						{
							i++;
							data = lines[i];
						}
						else
						{
							if(data.startsWith("D"))
							{
								String module_id=data.substring(1).trim().split("\\s")[0].trim();
								result.add("md:"+module_id);		
								i++;
								data = lines[i];
							}
						}
					}
				}
				i=lines.length;
			}
		}
		return result;
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public static ConcurrentLinkedQueue<String> getCompoundsWithBiologicalRoles() throws Exception {

		//Compounds with biological roles 	br08001

		String resultString = KeggAPI.getXMLDataFromBriteID("br08001");
		String[] lines = resultString.split("\n");

		ConcurrentLinkedQueue<String> result = new ConcurrentLinkedQueue<String>();
		for (int i = 0; i < lines.length; i++) 
		{
			String data=lines[i];
			if(data.startsWith("D"))
			{
				String metabolite_id=data.substring(1).trim().split("\\s")[0].trim();
				result.add(metabolite_id);	
			}
		}
		return result;
	}


	/**
	 * @param response
	 * @param pattern
	 * @return
	 */
	public static ConcurrentLinkedQueue<String> getEntities(String response, String pattern) {

		String[] lines = response.split("\n");

		Pattern p = Pattern.compile("^"+pattern+":"+".+");
		Matcher m;
		ConcurrentLinkedQueue<String> data = new ConcurrentLinkedQueue<String>();

		for (int i = 0; i < lines.length; i++)  {

			m = p.matcher(lines[i]);

			if (m.matches())  {

				data.add(m.group().split("\\s")[0]);				
			}
			
		}

		return data;
	}
	
	/**
	 * If list of unwanted data is empty or null, all data will be retrieved.
	 * 
	 * @param response
	 * @param pattern
	 * @param unwantedData
	 * @return
	 */
	public static ConcurrentLinkedQueue<String> getEntitiesAndExcludeUnwantedData(String response, String pattern, List<String> unwantedData) {

		if(unwantedData!= null && unwantedData.isEmpty())
			unwantedData = null;
		
		String[] lines = response.split("\n");
		
		Pattern p = Pattern.compile("^"+pattern+":"+".+");
		Matcher m;
		ConcurrentLinkedQueue<String> data = new ConcurrentLinkedQueue<String>();

		for (int i = 0; i < lines.length; i++)  {

			m = p.matcher(lines[i]);
			
			if (m.matches())  {
				
				String s = m.group().split("\\s")[0].trim();
				
				if(unwantedData == null || !unwantedData.contains(s.replace(pattern.concat(":"), "")))
					data.add(s);				
			}
			
		}

		return data;
	}

	/**
	 * @param results
	 * @param metabolites
	 * @param reactant
	 */
	private Pair<List<MetaboliteContainer>, Boolean> parseReactions(String[] metabolites, boolean reactant) {

		List<MetaboliteContainer> result = new ArrayList<>();

		String metabolite_ID = null;
		double stoichiometry;

		Pattern pat = Pattern.compile("[C|G|D]\\d{5}");
		
		Boolean addNoteUnbalanced = false;

		for(String metabolite:metabolites) {

			metabolite=metabolite.trim();
			Matcher mat = pat.matcher(metabolite);

			if(mat.find()) { 

				metabolite_ID=mat.group();
			}
			
			double signal = 1;
			
			if(reactant)
				signal = -1;

			if(metabolite.startsWith(metabolite_ID))
				stoichiometry=signal;
			else {
				try {
					stoichiometry=Double.parseDouble(metabolite.split(metabolite_ID)[0].trim())*signal;
				}
				catch (NumberFormatException e) {
					stoichiometry = signal;
					addNoteUnbalanced = true;
				}
			}
				
			MetaboliteContainer data = new MetaboliteContainer(metabolite_ID, stoichiometry);

			//data[0]=stoichiometry;
			//data[1]=chains;
			result.add(data);
		}

		return new Pair<List<MetaboliteContainer>, Boolean>(result, addNoteUnbalanced);
	}

	/**
	 * @author ODias
	 *
	 */
	public static enum EntityType {

		Drugs(new String[]{"drug","dr"}),
		Compound(new String[]{"compound","cpd"}),
		Glycan(new String[]{"glycan","gl"}),
		Reaction(new String[]{"reaction","rn"}),
		Pathways(new String[]{"pathways","path"}),
		Enzyme(new String[]{"enzyme","ec"}),
		Gene(new String[]{"genes",""}),
		Module(new String[]{"module","md"});

		private String[] entity_Type;

		/**
		 * @param entity_Type
		 */
		private EntityType(String[] entity_Type){
			this.entity_Type = entity_Type;
		}

		/**
		 * @return
		 */
		public String[] getEntity_Type(){
			return this.entity_Type;
		}
	}

	/**
	 * @param resultMetabolites the resultMetabolites to set
	 */
	public void setResultMetabolites(ConcurrentHashMap<String,MetaboliteContainer> resultMetabolites) {
		this.resultMetabolites = resultMetabolites;
	}

	/**
	 * @return the resultMetabolites
	 */
	public ConcurrentHashMap<String,MetaboliteContainer> getResultMetabolites() {
		return resultMetabolites;
	}

	/**
	 * @param resultEnzymes the resultEnzymes to set
	 */
	public void setResultEnzymes(ConcurrentHashMap<String,ProteinContainer> resultEnzymes) {
		this.resultEnzymes = resultEnzymes;
	}

	/**
	 * @return the resultEnzymes
	 */
	public ConcurrentHashMap<String,ProteinContainer> getResultEnzymes() {
		return resultEnzymes;
	}

	/**
	 * @param resultReactions the resultReactions to set
	 */
	public void setResultReactions(ConcurrentHashMap<String,ReactionContainer> resultReactions) {
		this.resultReactions = resultReactions;
	}

	/**
	 * @return the resultReactions
	 */
	public ConcurrentHashMap<String,ReactionContainer> getResultReactions() {
		return resultReactions;
	}

	/**
	 * @param resultGenes the resultGenes to set
	 */
	public void setResultGenes(ConcurrentHashMap<String,GeneContainer> resultGenes) {
		this.resultGenes = resultGenes;
	}

	/**
	 * @return the resultGenes
	 */
	public ConcurrentHashMap<String,GeneContainer> getResultGenes() {
		return resultGenes;
	}

	/**
	 * @param resultModules the resultModules to set
	 */
	public void setResultModules(ConcurrentHashMap<String,ModuleContainer> resultModules) {
		this.resultModules = resultModules;
	}

	/**
	 * @return the resultModules
	 */
	public ConcurrentHashMap<String,ModuleContainer> getResultModules() {
		return resultModules;
	}

	/**
	 * @param organismID the organismID to set
	 */
	public void setOrganismID(String organismID) {
		this.organismID = organismID;
	}


	/**
	 * @return the organismID
	 */
	public String getOrganismID() {
		return organismID;
	}

	/**
	 * @return the entity
	 */
	public ConcurrentLinkedQueue<String> getEntity() {
		return entity;
	}


	/**
	 * @param entity the entity to set
	 */
	public void setEntity(ConcurrentLinkedQueue<String> entity) {
		this.entity = entity;
	}


	/**
	 * @return the entityTypeString
	 */
	public EntityType getEntity_Type_String() {
		return entityTypeString;
	}


	/**
	 * @param entityTypeString the entityTypeString to set
	 */
	public void setEntityTypeString(EntityType entityTypeString) {
		this.entityTypeString = entityTypeString;
	}

	public void setCancel() {

		this.cancel.set(true);
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
