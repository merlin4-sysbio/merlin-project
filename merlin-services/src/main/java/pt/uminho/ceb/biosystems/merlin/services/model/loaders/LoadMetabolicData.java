/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.services.model.loaders;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModuleContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwaysHierarchyContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceData;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceInitialData;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.utilities.datastructures.list.ListUtilities;

/**
 * @author ODias
 *
 */
public class LoadMetabolicData implements Runnable{

	private final static int  LIST_SIZE = 1000;
	private ConcurrentLinkedQueue<MetaboliteContainer> resultMetabolites;
	private ConcurrentLinkedQueue<ProteinContainer> resultEnzymes;
	private ConcurrentLinkedQueue<ReactionContainer> resultReactions;
	private ConcurrentLinkedQueue<GeneContainer> resultGenes;
	private ConcurrentLinkedQueue<ModuleContainer> resultModules;
	private ConcurrentLinkedQueue<String[]> resultPathways;
	private ConcurrentLinkedQueue<PathwaysHierarchyContainer> keggPathwaysHierarchy;
	private ConcurrentLinkedQueue<String> enzymesPathwayList,orthologueEntities;
	private ModelDataLoader metabolicDataLoader;
	private ConcurrentLinkedQueue<Integer> reactionsPathwayList, metabolitesPathwayList, modulesPathwayList;
	private ConcurrentHashMap<String,Integer> pathways_id;
	private AtomicBoolean cancel;
	//	private long startTime;
	private AtomicInteger dataSize;
	//	private AtomicInteger datum;
	private boolean error, loadAll;
	private AtomicBoolean addMetPathDataSize, addModPathDataSize, addReacPathDataSize, addEnzPathDataSize;
	private String databaseName;
	private ConcurrentLinkedQueue<String> compoundsWithBiologicalRoles;
	private boolean importFromSBML;
	private ConcurrentLinkedQueue<CompartmentContainer> resultCompartments;

	final static Logger logger = LoggerFactory.getLogger(LoadMetabolicData.class);


	/**
	 * Constructor for load KEGG data runnable
	 * 
	 * @param connection
	 * @param workspaceMetabolicData
	 * @param databaseInitialData
	 * @param cancel
	 * @param dataSize
	 * @throws Exception
	 */
	public LoadMetabolicData(String databaseName, WorkspaceInitialData databaseInitialData, WorkspaceData workspaceMetabolicData,
			AtomicBoolean cancel, AtomicInteger dataSize) throws Exception {

		this.cancel = cancel;
		//		this.startTime = startTime;
		//		this.datum = datum;
		this.dataSize = dataSize;
		this.setResultMetabolites(workspaceMetabolicData.getResultMetabolites());
		this.setResultEnzymes(workspaceMetabolicData.getResultEnzymes());
		this.setResultReactions(workspaceMetabolicData.getResultReactions());
		this.setResultGenes(workspaceMetabolicData.getResultGenes());
		this.setResultModules(workspaceMetabolicData.getResultModules());
		this.setKeggPathwaysHierarchy(workspaceMetabolicData.getKeggPathwaysHierarchy());
		this.setOrthologueEntities(workspaceMetabolicData.getOrthologueEntities());
		this.setPathways_id(databaseInitialData.getPathwaysIdentifier());
		this.setReactionsPathwayList(databaseInitialData.getReactionsPathwayList());
		this.setEnzymesPathwayList(databaseInitialData.getEnzymesPathwayList());
		this.setMetabolitesPathwayList(databaseInitialData.getMetabolitesPathwayList());
		this.setModulesPathwayList(databaseInitialData.getModulesPathwayList());
		this.metabolicDataLoader = new ModelDataLoader(databaseInitialData, workspaceMetabolicData.getCompoundsWithBiologicalRoles(), false, databaseName);

		this.setError(false);

		this.addMetPathDataSize = new AtomicBoolean(true);
		this.addEnzPathDataSize = new AtomicBoolean(true);
		this.addModPathDataSize = new AtomicBoolean(true);
		this.addReacPathDataSize = new AtomicBoolean(true);
		this.loadAll = true;
		this.databaseName = databaseName;

		if(this.resultGenes != null && this.resultGenes.size()>0)
			this.loadAll = false;
	}



	//Import SBML Model Constructor
	///////////////////////////////////////////////////////////////
	/**
	 * test constructor to load from imported SBML models
	 * 
	 * @param connection
	 * @param progress
	 * @param datum
	 * @param cancel
	 * @param dataSize
	 * @param startTime
	 * @throws Exception
	 */
	public LoadMetabolicData(String databaseName, WorkspaceData modelData, WorkspaceInitialData databaseInitialData, 
			AtomicBoolean cancel, AtomicInteger dataSize) throws Exception {

		this.cancel = cancel;
		this.dataSize = dataSize;

		this.importFromSBML = true;
		this.compoundsWithBiologicalRoles = new ConcurrentLinkedQueue<String>();


		this.setPathways_id(databaseInitialData.getPathwaysIdentifier());
		this.setReactionsPathwayList(databaseInitialData.getReactionsPathwayList());
		this.setEnzymesPathwayList(databaseInitialData.getEnzymesPathwayList());
		this.setMetabolitesPathwayList(databaseInitialData.getMetabolitesPathwayList());
		this.setModulesPathwayList(databaseInitialData.getModulesPathwayList());


		this.metabolicDataLoader = new ModelDataLoader(databaseInitialData, compoundsWithBiologicalRoles, importFromSBML, databaseName);

		this.addMetPathDataSize = new AtomicBoolean(false);
		this.addEnzPathDataSize = new AtomicBoolean(false);
		this.addModPathDataSize = new AtomicBoolean(false);
		this.addReacPathDataSize = new AtomicBoolean(false);
		this.loadAll = true;

		this.setKeggPathwaysHierarchy(modelData.getKeggPathwaysHierarchy());
		this.setResultMetabolites(modelData.getResultMetabolites());
		this.setResultCompartments(modelData.getResultCompartments());
		this.setResultEnzymes(modelData.getResultEnzymes());
		this.setResultReactions(modelData.getResultReactions());
		this.setResultGenes(modelData.getResultGenes());
		this.databaseName = databaseName;

		metabolicDataLoader.setLocusTagsToProteinIdsMap(modelData.getLocusTagsToProteinIdsMap());

	}
	/////////////////////////////////////////////////////////////////////////////////




	/**
	 * 
	 * Constructor to be removed after changing all loaders to prepared statements
	 * 
	 * @param connection2
	 * @param workspaceMetabolicData
	 * @param databaseInitialData
	 * @param progress2
	 * @param datum2
	 * @param cancel2
	 * @param dataSize2
	 * @param startTime2
	 * @param flag
	 * @throws Exception 
	 */
	public LoadMetabolicData(String databaseName, WorkspaceData workspaceMetabolicData, WorkspaceInitialData databaseInitialData, 
			AtomicBoolean cancel, AtomicInteger dataSize, boolean b) throws Exception {

		this.cancel = cancel;
		this.dataSize = dataSize;
		this.setResultMetabolites(workspaceMetabolicData.getResultMetabolites());
		this.metabolicDataLoader = new ModelDataLoader(databaseInitialData, workspaceMetabolicData.getCompoundsWithBiologicalRoles(), false, databaseName);

		this.setError(false);

		this.addMetPathDataSize = new AtomicBoolean(false);
		this.addEnzPathDataSize = new AtomicBoolean(false);
		this.addModPathDataSize = new AtomicBoolean(false);
		this.addReacPathDataSize = new AtomicBoolean(false);
		this.loadAll = false;
		this.databaseName = databaseName;
	}

	@Override
	public void run() {

//		while(true) {
			try {


				if(!this.cancel.get()) {

					if(importFromSBML) {
						logger.info("Loading Compartments...");
						this.loadCompartments();
					}
					logger.info("Loading Pathways...");
					this.loadPathways();
					logger.info("Loading Enzymes...");
					this.loadEnzymes();
					logger.info("Loading Compounds...");
					this.loadCompounds();
					logger.info("Loading Genes...");
					this.loadGenes();
					logger.info("Loading Modules...");
					this.loadModules();
					logger.info("Loading Reactions...");
					this.loadReactions();

					if(loadAll) {

						this.metabolicDataLoader.getDatabaseInitialData().setPathwaysIdentifier(this.getPathways_id());

						if(this.addModPathDataSize.get()) {

							this.dataSize = new AtomicInteger(this.dataSize.get() + this.modulesPathwayList.size());
							this.addModPathDataSize.set(false);
						}
						this.loadModulesPathwayList();

						if(this.addReacPathDataSize.get()) {

							this.dataSize = new AtomicInteger(this.dataSize.get() + this.reactionsPathwayList.size());
							this.addReacPathDataSize.set(false);
						}

						this.loadReactionsPathwayList();

						if(this.addEnzPathDataSize.get()) {

							this.dataSize = new AtomicInteger(this.dataSize.get() + this.enzymesPathwayList.size());
							this.addEnzPathDataSize.set(false);
						}
						this.loadEnzymesPathwayList();

						if(this.addMetPathDataSize.get()) {

							this.dataSize = new AtomicInteger(this.dataSize.get() + this.metabolitesPathwayList.size());
							this.addMetPathDataSize.set(false);
						}			
						this.loadMetabolitePathwayList();

					}
				}
			}
			catch (RemoteException e) {e.printStackTrace(); this.setError(true);}
			catch (Exception e) {e.printStackTrace(); this.setError(true);}
//		}
	}


	/**
	 * @throws Exception 
	 * @throws ServiceException
	 */
	public void loadGenes() throws Exception{

		if(this.resultGenes!=null) {

			if(this.importFromSBML){
				ConcurrentHashMap<String, Integer> genesSeqIDs = new ConcurrentHashMap<>(ModelGenesServices.getGeneIDsByQuery(this.databaseName));
				this.metabolicDataLoader.getDatabaseInitialData().getGenesIdentifier().putAll(genesSeqIDs);
			}

			while(!this.resultGenes.isEmpty()) {

				GeneContainer geneList;
				if((geneList = this.resultGenes.poll()) != null) {

					this.metabolicDataLoader.loadGene(geneList);
					//this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Genes");
				}
			}
		}
	}



	//////////////////
	/**
	 * @throws SQLException
	 */
	public void loadCompartments() throws Exception{

		if(this.resultCompartments != null) {

			while(!this.resultCompartments.isEmpty() && !this.cancel.get()) {

				CompartmentContainer compartment;
				if((compartment = this.resultCompartments.poll()) != null) {

					this.metabolicDataLoader.loadCompartment(compartment);
					//					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Genes");
				}
			}
		}
	}
	/////////////////////////////	



	/**
	 * @throws Exception 
	 * @throws ServiceException
	 */
	public void loadCompounds() throws Exception{

		if(this.resultMetabolites!=null) {

			while(!this.resultMetabolites.isEmpty() && !this.cancel.get()) {

				//				this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Compounds");
				@SuppressWarnings("unchecked")
				ConcurrentLinkedQueue<MetaboliteContainer> containers = (ConcurrentLinkedQueue<MetaboliteContainer>) ListUtilities.getConcurrentList(this.resultMetabolites, LIST_SIZE);

				this.metabolicDataLoader.loadMetabolites(containers);
				//				this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Compounds");
			}
		}
	}

	/**
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ServiceException
	 */
	//	public void loadCompounds(Statement stmt) throws RemoteException, SQLException{
	//
	//		if(this.resultMetabolites!=null) {
	//
	//			while(!this.resultMetabolites.isEmpty() && !this.cancel.get()) {
	//
	//				MetaboliteContainer metaboliteList;
	//				if((metaboliteList = this.resultMetabolites.poll()) != null) {
	//
	//					this.keggLoader.loadMetabolite(metaboliteList, stmt);
	//					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize, "Loading Compounds");
	//				}
	//			}
	//		}
	//	}

	/**
	 * @throws Exception 
	 * @throws IOException 
	 * @throws ServiceException
	 */
	public void loadEnzymes() throws IOException, Exception{

		if(this.resultEnzymes!=null) {

			while(!this.resultEnzymes.isEmpty() && !this.cancel.get()) {
				
				@SuppressWarnings("unchecked")
				ConcurrentLinkedQueue<ProteinContainer> containers = (ConcurrentLinkedQueue<ProteinContainer>) ListUtilities.getConcurrentList(this.resultEnzymes, LIST_SIZE);

//				ProteinContainer enzymesList;
//				if((enzymesList = this.resultEnzymes.poll()) != null) {
				
				for(ProteinContainer container : containers) {
					if(container.getExternalIdentifier().equals("2.3.1.163"))
						System.out.println(container.getExternalIdentifier());
					this.metabolicDataLoader.loadProtein(container);
				}
					//					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Enzymes");
//				}
			}
		}
	}

	/**
	 * @throws Exception 
	 * @throws ServiceException
	 */
	public void loadReactions() throws Exception{

		if(this.resultReactions!=null) {

			while(!this.resultReactions.isEmpty() && !this.cancel.get()) {

				ReactionContainer reactionContainer;// = this.resultReactions.poll();

				if((reactionContainer = this.resultReactions.poll()) != null) {

					Set<String> products = new HashSet<>();
					Set<String> reactants = new HashSet<>();

					for(MetaboliteContainer metabolite : reactionContainer.getProductsStoichiometry())
						products.add(metabolite.getExternalIdentifier());

					for(MetaboliteContainer metabolite : reactionContainer.getReactantsStoichiometry())
						reactants.add(metabolite.getExternalIdentifier());

					if(products.containsAll(reactants) && reactants.containsAll(products)) {

						String reactionIdentifier = reactionContainer.getExternalIdentifier();
						String reactionEquation = reactionContainer.getEquation();

						//						System.out.println(reactionIdentifier);
						//						System.out.println(reactionEquation);

						String message = "Reaction ".concat(reactionIdentifier).concat(" has the same products and metabolites: ")
								.concat(reactionEquation).concat("\n reaction will be ignored!");
						logger.warn(message);
					}
					else {
						this.metabolicDataLoader.loadReaction(reactionContainer);
					}

					//					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Reactions");
				}
			}
		}
	}

	/**
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ServiceException
	 */
	public void loadModules() throws Exception{

		if(this.resultModules!=null) {

			while(!this.resultModules.isEmpty() && !this.cancel.get()) {

				ModuleContainer moduleList;
				if((moduleList =this.resultModules.poll()) != null) {

					this.metabolicDataLoader.loadModule(moduleList);
					//					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Modules");
				}
			}
		}
	}

	/**
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ServiceException
	 */
	public void loadPathways() throws Exception{

		if(this.getKeggPathwaysHierarchy()!=null) {

			while(!this.getKeggPathwaysHierarchy().isEmpty() && !this.cancel.get()) {

				PathwaysHierarchyContainer pathwayContainer;
				if((pathwayContainer = this.getKeggPathwaysHierarchy().poll()) != null) {

					this.metabolicDataLoader.loadPathways(pathwayContainer);
					//					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Pathways");
				}
			}
		}
	}

	/**
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ServiceException
	 */
	public void loadReactionsPathwayList() throws Exception{

		if(this.getReactionsPathwayList() != null) {

			while(!this.getReactionsPathwayList().isEmpty() && !this.cancel.get()) {

				Integer reactionPathList;

				if((reactionPathList = this.getReactionsPathwayList().poll()) != null) {

					this.metabolicDataLoader.load_ReactionsPathway(reactionPathList);
					//					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Reactions Pathways");
				}
			}
		}
	}

	/**
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ServiceException
	 */
	public void loadEnzymesPathwayList() throws Exception{

		if(this.getEnzymesPathwayList()!=null) {

			while(!this.getEnzymesPathwayList().isEmpty() && !this.cancel.get()) {

				String enzymesPathwayList;
				if((enzymesPathwayList = this.getEnzymesPathwayList().poll()) != null) {

					this.metabolicDataLoader.load_EnzymesPathway(enzymesPathwayList);
					//					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Enzymes Pathways");
				}
			}
		}
	}

	/**
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ServiceException
	 */
	public void loadModulesPathwayList() throws Exception{

		if(this.getModulesPathwayList()!=null) {

			while(!this.getModulesPathwayList().isEmpty() && !this.cancel.get()) {

				Integer modulePathList;
				if((modulePathList = this.getModulesPathwayList().poll()) != null) {

					this.metabolicDataLoader.load_ModulePathway(modulePathList);
					//					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Modules Pathways");
				}
			}
		}
	}

	/**
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ServiceException
	 */
	public void loadMetabolitePathwayList() throws Exception{

		if(this.getMetabolitesPathwayList()!=null) {

			while(!this.getMetabolitesPathwayList().isEmpty() && !this.cancel.get()) {

				Integer metaPathList;
				if((metaPathList = this.getMetabolitesPathwayList().poll()) != null) {

					this.metabolicDataLoader.load_MetabolitePathway(metaPathList);
					//					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), this.dataSize.get(), "Loading Metabolites Pathways");
				}
			}
		}
	}


	/**
	 * @param resultReactions the resultReactions to set
	 */
	public void setResultReactions(ConcurrentLinkedQueue<ReactionContainer> resultReactions) {
		this.resultReactions = resultReactions;
	}



	/**
	 * @return the resultReactions
	 */
	public ConcurrentLinkedQueue<ReactionContainer> getResultReactions() {
		return resultReactions;
	}



	/**
	 * @param resultPathways the resultPathways to set
	 */
	public void setResultPathways(ConcurrentLinkedQueue<String[]> resultPathways) {
		this.resultPathways = resultPathways;
	}



	/**
	 * @return the resultPathways
	 */
	public ConcurrentLinkedQueue<String[]> getResultPathways() {
		return resultPathways;
	}



	/**
	 * @param keggPathwaysHierarchy the kegg_Pathways_Hierarchy to set
	 */
	public void setKeggPathwaysHierarchy(ConcurrentLinkedQueue<PathwaysHierarchyContainer> keggPathwaysHierarchy) {
		this.keggPathwaysHierarchy = keggPathwaysHierarchy;
	}



	/**
	 * @return the kegg_Pathways_Hierarchy
	 */
	public ConcurrentLinkedQueue<PathwaysHierarchyContainer> getKeggPathwaysHierarchy() {
		return keggPathwaysHierarchy;
	}



	/**
	 * @param orthologueEntities the orthologueEntities to set
	 */
	public void setOrthologueEntities(ConcurrentLinkedQueue<String> orthologueEntities) {
		this.orthologueEntities = orthologueEntities;
	}



	/**
	 * @return the orthologueEntities
	 */
	public ConcurrentLinkedQueue<String> getOrthologueEntities() {
		return orthologueEntities;
	}

	/**
	 * @return the resultMetabolites
	 */
	public ConcurrentLinkedQueue<MetaboliteContainer> getResultMetabolites() {
		return resultMetabolites;
	}

	/**
	 * @param resultMetabolites the resultMetabolites to set
	 */
	public void setResultMetabolites(
			ConcurrentLinkedQueue<MetaboliteContainer> resultMetabolites) {
		this.resultMetabolites = resultMetabolites;
	}

	/**
	 * @return the resultEnzymes
	 */
	public ConcurrentLinkedQueue<ProteinContainer> getResultEnzymes() {
		return resultEnzymes;
	}

	/**
	 * @param resultEnzymes the resultEnzymes to set
	 */
	public void setResultEnzymes(ConcurrentLinkedQueue<ProteinContainer> resultEnzymes) {
		this.resultEnzymes = resultEnzymes;
	}

	/**
	 * @return the resultGenes
	 */
	public ConcurrentLinkedQueue<GeneContainer> getResultGenes() {
		return resultGenes;
	}

	/**
	 * @param resultGenes the resultGenes to set
	 */
	public void setResultGenes(ConcurrentLinkedQueue<GeneContainer> resultGenes) {
		this.resultGenes = resultGenes;
	}

	/**
	 * @return the resultModules
	 */
	public ConcurrentLinkedQueue<ModuleContainer> getResultModules() {
		return resultModules;
	}

	/**
	 * @param resultModules the resultModules to set
	 */
	public void setResultModules(ConcurrentLinkedQueue<ModuleContainer> resultModules) {
		this.resultModules = resultModules;
	}

	/**
	 * @return the resultCompartments
	 */
	public ConcurrentLinkedQueue<CompartmentContainer> getresultCompartments() {
		return resultCompartments;
	}

	/**
	 * @param the resultCompartments to set
	 */
	public void setResultCompartments(ConcurrentLinkedQueue<CompartmentContainer> resultCompartments) {
		this.resultCompartments = resultCompartments;
	}

	/**
	 * @return the keggLoader
	 */
	public ModelDataLoader getKegg_loader() {
		return this.metabolicDataLoader;
	}

	/**
	 * @param keggLoader the keggLoader to set
	 */
	public void setKegg_loader(ModelDataLoader keggLoader) {
		this.metabolicDataLoader = keggLoader;
	}

	/**
	 * @param reactionsPathwayList the reactionsPathwayList to set
	 */
	public void setReactionsPathwayList(ConcurrentLinkedQueue<Integer> reactionsPathwayList) {
		this.reactionsPathwayList = reactionsPathwayList;
	}

	/**
	 * @return the reactionsPathwayList
	 */
	public ConcurrentLinkedQueue<Integer> getReactionsPathwayList() {
		return this.reactionsPathwayList;
	}

	/**
	 * @param enzymesPathwayList the enzymesPathwayList to set
	 */
	public void setEnzymesPathwayList(ConcurrentLinkedQueue<String> enzymesPathwayList) {
		this.enzymesPathwayList = enzymesPathwayList;
	}

	/**
	 * @return the enzymesPathwayList
	 */
	public ConcurrentLinkedQueue<String> getEnzymesPathwayList() {
		return this.enzymesPathwayList;
	}

	/**
	 * @param metabolitesPathwayList the metabolitesPathwayList to set
	 */
	public void setMetabolitesPathwayList(ConcurrentLinkedQueue<Integer> metabolitesPathwayList) {
		this.metabolitesPathwayList = metabolitesPathwayList;
	}

	/**
	 * @return the metabolitesPathwayList
	 */
	public ConcurrentLinkedQueue<Integer> getMetabolitesPathwayList() {
		return this.metabolitesPathwayList;
	}

	/**
	 * @param modulesPathwayList the modulesPathwayList to set
	 */
	public void setModulesPathwayList(ConcurrentLinkedQueue<Integer> modulesPathwayList) {
		this.modulesPathwayList = modulesPathwayList;
	}

	/**
	 * @return the modulesPathwayList
	 */
	public ConcurrentLinkedQueue<Integer> getModulesPathwayList() {
		return modulesPathwayList;
	}

	/**
	 * @param pathways_id the pathways_id to set
	 */
	public void setPathways_id(ConcurrentHashMap<String,Integer> pathways_id) {
		this.pathways_id = pathways_id;
	}

	/**
	 * @return the pathways_id
	 */
	public ConcurrentHashMap<String,Integer> getPathways_id() {
		return this.pathways_id;
	}

	/**
	 * @param cancel
	 */
	public void setCancel(AtomicBoolean cancel) {

		this.cancel = cancel;
	}

	/**
	 * @return the error
	 */
	public boolean isError() {
		return this.error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

}
