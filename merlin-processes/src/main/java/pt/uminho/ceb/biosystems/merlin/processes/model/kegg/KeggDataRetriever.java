/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.processes.model.kegg;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.KeggAPI;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModuleContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwaysHierarchyContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceData;
import pt.uminho.ceb.biosystems.merlin.processes.model.kegg.KeggDataRetrieverRunnable.EntityType;

/**
 * @author ODias
 *
 */
public class KeggDataRetriever implements PropertyChangeListener {

	private static final int NUMBER_OF_THREADS = 10;
	private AtomicBoolean cancel;
	private String organismID;
	private List<String> reactionsToExclude;
	private List<String> metabolitesToExclude;
	private List<String> pathwaysToExclude;
	private PropertyChangeSupport changes;

	/**
	 * Retrieve Kegg data
	 * 
	 * @param organismID
	 * @param cancel
	 * @throws Exception
	 */
	public KeggDataRetriever(String organismID, AtomicBoolean cancel) throws Exception {

		this.organismID = organismID;
		this.cancel = cancel;
		this.reactionsToExclude = null;
		this.metabolitesToExclude = null;
		this.changes = new PropertyChangeSupport(this);
	}

	/**
	 * Retrieve Kegg data excluding unwanted reactions and/or metabolites. If null, all are retrieved.
	 * 
	 * @param organismID
	 * @param reactionsToExclude
	 * @param metabolitesToExclude
	 * @param progress
	 * @param cancel
	 * @throws Exception
	 */
	public KeggDataRetriever(String organismID, List<String> reactionsToExclude, List<String> metabolitesToExclude, List<String> pathwaysToExclude, AtomicBoolean cancel) throws Exception {

		this.organismID = organismID;
		this.reactionsToExclude = reactionsToExclude;
		this.metabolitesToExclude = metabolitesToExclude;
		this.pathwaysToExclude = pathwaysToExclude;
		this.cancel = cancel;
		this.changes = new PropertyChangeSupport(this);
	}

	/**
	 * @param activeCompounds 
	 * @throws Exception
	 */
	public WorkspaceData retrieveMetabolicData(boolean activeCompounds) throws Exception {

		WorkspaceData workspaceMetabolicData = new WorkspaceData();
		
		workspaceMetabolicData.setResultMetabolites(new ConcurrentLinkedQueue<MetaboliteContainer> ());
		workspaceMetabolicData.setResultEnzymes(new ConcurrentLinkedQueue<ProteinContainer> ());
		workspaceMetabolicData.setResultReactions(new ConcurrentLinkedQueue<ReactionContainer> ());
		workspaceMetabolicData.setResultModules(new ConcurrentLinkedQueue<ModuleContainer> ());

		List<EntityType> data = new ArrayList<EntityType>();
		data.add(EntityType.Reaction);
//		data.add(EntityType.Drugs);
		data.add(EntityType.Compound);
//		data.add(EntityType.Glycan);
		data.add(EntityType.Pathways);
		data.add(EntityType.Enzyme);
		data.add(EntityType.Module);

		long startTime = System.currentTimeMillis();
		AtomicInteger datum = new AtomicInteger(0);

		int dataSize = 1 ;

		{	
			long startTime_cbr = System.currentTimeMillis();
			workspaceMetabolicData.setCompoundsWithBiologicalRoles(KeggDataRetrieverRunnable.getCompoundsWithBiologicalRoles());
			long endTime_process_cbr = System.currentTimeMillis();

			this.changes.firePropertyChange("size", -1, dataSize);
			this.changes.firePropertyChange("sequencesCounter", datum.get(), datum.incrementAndGet());
			this.changes.firePropertyChange("message", "", "get biological compounds");
			

			System.out.println("Total elapsed time in execution of method setCompoundsWithBiologicalRoles is :"+ String.format("%d min, %d sec", 
					TimeUnit.MILLISECONDS.toMinutes(endTime_process_cbr-startTime_cbr),TimeUnit.MILLISECONDS.toSeconds(endTime_process_cbr-startTime_cbr) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime_process_cbr-startTime_cbr))));
		}

		//Concurrent Data structures 
		List<Thread> threads = new ArrayList<Thread>();
		int numberOfProcesses =  Runtime.getRuntime().availableProcessors()*2;
		ConcurrentHashMap<String,MetaboliteContainer> resultMetabolites=new ConcurrentHashMap<String,MetaboliteContainer>();
		ConcurrentHashMap<String,ProteinContainer> resultEnzymes=new ConcurrentHashMap<String, ProteinContainer>();
		ConcurrentHashMap<String,ReactionContainer> resultReactions=new ConcurrentHashMap<String, ReactionContainer>();
		ConcurrentHashMap<String,ModuleContainer> resultModules=new ConcurrentHashMap<String, ModuleContainer>();
		workspaceMetabolicData.setKeggPathwaysHierarchy(new ConcurrentLinkedQueue<PathwaysHierarchyContainer>());
		workspaceMetabolicData.setOrthologueEntities(new ConcurrentLinkedQueue<String>());
		Set<String> compoundsInReactions = null;
		

		for(EntityType entityTypeString:data) {

			long startTime_process = System.currentTimeMillis();
			ConcurrentLinkedQueue<String> entity = new ConcurrentLinkedQueue<String>();

			if(!this.cancel.get()) {

				if(entityTypeString.equals(EntityType.Compound)||entityTypeString.equals(EntityType.Drugs)||entityTypeString.equals(EntityType.Glycan)) {

					if(compoundsInReactions == null && resultReactions.size()>0)
						compoundsInReactions = KeggDataRetriever.getCompoundsInReactions(resultReactions);

					entity.addAll(KeggDataRetrieverRunnable.getEntitiesAndExcludeUnwantedData(
							KeggAPI.getInfo(entityTypeString.getEntity_Type()[0]/*+"/"+entity_Type_String.getEntity_Type()[1]+suffix+i*/), entityTypeString.getEntity_Type()[1], metabolitesToExclude));

					if(activeCompounds)
						entity.retainAll(compoundsInReactions);

				}

				if(entityTypeString.equals(EntityType.Enzyme))
					entity.addAll(KeggDataRetrieverRunnable.getEntities(KeggAPI.getInfo(EntityType.Enzyme.getEntity_Type()[0]/*+"/"+EntityType.Enzyme.getEntity_Type()[1]+":"+i*/),EntityType.Enzyme.getEntity_Type()[1]));

				if(entityTypeString.equals(EntityType.Reaction))
					entity = KeggDataRetrieverRunnable.getEntitiesAndExcludeUnwantedData(
							KeggAPI.getInfo(EntityType.Reaction.getEntity_Type()[0]/*+"/"+EntityType.Reaction.getEntity_Type()[1]+":R"+i*/),EntityType.Reaction.getEntity_Type()[1], reactionsToExclude);

				if(entityTypeString.equals(EntityType.Module))
					entity = KeggDataRetrieverRunnable.getStructuralComplexModules();//entity = KEGGDataRetriever.getEntities(KeggAPI.getinfo(EntityType.Module.getEntity_Type()[0]+" "+EntityType.Module.getEntity_Type()[1]),EntityType.Module.getEntity_Type()[1]);

				if(entityTypeString.equals(EntityType.Pathways))
					workspaceMetabolicData.setKeggPathwaysHierarchy(KeggDataRetrieverRunnable.get_Kegg_Pathways_Hierarchy_And_Exclude_Unwanted_Data(false, pathwaysToExclude));//resultPathways = KEGGDataRetriever.get_All_Kegg_Pathways();
					//this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis() - startTime), datum.incrementAndGet(), dataSize, "Get pathways");

				MetaboliteContainer  metaboliteContainer = new MetaboliteContainer("Biomass");
				metaboliteContainer.setName("Biomass");
				resultMetabolites.put("Biomass",metaboliteContainer);

//				numberOfProcesses=Runtime.getRuntime().availableProcessors()*10;
				
				numberOfProcesses = NUMBER_OF_THREADS; //Testar

				dataSize = entity.size();
				datum = new AtomicInteger(0);

				if(entity.size()>0) {
					
					for(int i=0; i<numberOfProcesses; i++) {
						
						Runnable keggDataRetriever = new KeggDataRetrieverRunnable(entity, organismID, entityTypeString, resultMetabolites, resultEnzymes, resultReactions, 
								resultModules, this.cancel, dataSize, datum);
						((KeggDataRetrieverRunnable) keggDataRetriever).addPropertyChangeListener(this);
						Thread thread = new Thread(keggDataRetriever);
						threads.add(thread);
						//System.out.println("Start "+i);
						thread.start();

						try {
							Thread.sleep(5000);
						} 
						catch (InterruptedException e1){

							Thread.currentThread().interrupt();
						}
					}

					for(Thread thread :threads) {

						thread.join();
					}
				}

			}

			long endTime_process = System.currentTimeMillis();

			System.out.println("Total elapsed time in execution of method "+entityTypeString+" is :"+ String.format("%d min, %d sec", 
					TimeUnit.MILLISECONDS.toMinutes(endTime_process-startTime_process),TimeUnit.MILLISECONDS.toSeconds(endTime_process-startTime_process) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime_process-startTime_process))));
		}
		long endTime = System.currentTimeMillis();

		System.out.println("Total elapsed time in execution of method GLOBAL is :"+ String.format("%d min, %d sec", 
				TimeUnit.MILLISECONDS.toMinutes(endTime-startTime),TimeUnit.MILLISECONDS.toSeconds(endTime-startTime) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime-startTime))));

		//from maps to lists
		for(String entry : resultMetabolites.keySet())
			workspaceMetabolicData.getResultMetabolites().add(resultMetabolites.get(entry));

		for(String entry : resultEnzymes.keySet())
			workspaceMetabolicData.getResultEnzymes().add(resultEnzymes.get(entry));

		for(String entry : resultReactions.keySet())	
			workspaceMetabolicData.getResultReactions().add(resultReactions.get(entry));

		for(String entry : resultModules.keySet())			
			workspaceMetabolicData.getResultModules().add(resultModules.get(entry));
		
		return workspaceMetabolicData;
	}

	/**
	 * Retrieve Kegg data
	 * 
	 * @param organismID
	 * @param progress 
	 * @param cancel 
	 * @throws Exception
	 */
	public WorkspaceData retrieveOrganismData() throws Exception {

		WorkspaceData workspaceGenesData = new WorkspaceData();
		
		workspaceGenesData.setResultGenes(new ConcurrentLinkedQueue<GeneContainer> ());
		workspaceGenesData.setResultEnzymes(new ConcurrentLinkedQueue<ProteinContainer> ());
		workspaceGenesData.setResultReactions(new ConcurrentLinkedQueue<ReactionContainer> ());

		List<EntityType> data = new ArrayList<EntityType>();
		data.add(EntityType.Gene);
		data.add(EntityType.Enzyme);
//		data.add(EntityType.Reaction);

		long startTime = System.currentTimeMillis();
		AtomicInteger datum = new AtomicInteger(0);
		int dataSize = 1 ;

		for(EntityType entityTypeString:data) {

			List<Thread> threads = new ArrayList<Thread>();
			int numberOfProcesses =  Runtime.getRuntime().availableProcessors()*2;
			ConcurrentHashMap<String,GeneContainer> resultGenes = new ConcurrentHashMap<String, GeneContainer>();
			ConcurrentHashMap<String,ProteinContainer> resultEnzymes = new ConcurrentHashMap<String, ProteinContainer>();
			ConcurrentHashMap<String,ReactionContainer> resultReactions=new ConcurrentHashMap<String, ReactionContainer>();
			ConcurrentLinkedQueue<String> entity = new ConcurrentLinkedQueue<String>();

			if(entityTypeString.equals(EntityType.Gene))
				entity = KeggDataRetrieverRunnable.getEntities(KeggAPI.getInfo(/*EntityType.Gene.getEntity_Type()[0]+"/"+*/organismID/*+":"*/),organismID.toLowerCase());

			if(entityTypeString.equals(EntityType.Enzyme))
				entity.addAll(KeggDataRetrieverRunnable.getEntities(KeggAPI.getInfo(EntityType.Enzyme.getEntity_Type()[0]/*+"/"+EntityType.Enzyme.getEntity_Type()[1]+":"+i*/),EntityType.Enzyme.getEntity_Type()[1]));
			
			if(entityTypeString.equals(EntityType.Reaction))
				entity = KeggDataRetrieverRunnable.getEntities(KeggAPI.getInfo(EntityType.Reaction.getEntity_Type()[0]/*+"/"+EntityType.Reaction.getEntity_Type()[1]+":R"+i*/),EntityType.Reaction.getEntity_Type()[1]);

//			numberOfProcesses=Runtime.getRuntime().availableProcessors();
			numberOfProcesses = NUMBER_OF_THREADS;

			dataSize = entity.size();
			datum = new AtomicInteger(0);

			for(int i=0; i<numberOfProcesses; i++) {
				
				Runnable keggDataRetriever = new KeggDataRetrieverRunnable(entity, organismID, entityTypeString, resultGenes, resultEnzymes, resultReactions, this.cancel, dataSize, datum);
				((KeggDataRetriever) keggDataRetriever).addPropertyChangeListener(this);
				Thread thread = new Thread(keggDataRetriever);
				threads.add(thread);
				//System.out.println("Start "+i);
				thread.start();

				try {
					Thread.sleep(5000);
				} 
				catch (InterruptedException e1){

					Thread.currentThread().interrupt();
				}

			}

			for(Thread thread :threads) {

				thread.join();
			}

			long endTime_process = System.currentTimeMillis();

			System.out.println("Total elapsed time in execution of method RETRIEVE is :"+ String.format("%d min, %d sec", 
					TimeUnit.MILLISECONDS.toMinutes(endTime_process-startTime),TimeUnit.MILLISECONDS.toSeconds(endTime_process-startTime) -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime_process-startTime))));
			//from maps to lists

			if(!organismID.isEmpty())			
				for(String entry : resultGenes.keySet())
					workspaceGenesData.getResultGenes().add(resultGenes.get(entry));

			for(String entry : resultEnzymes.keySet())
				workspaceGenesData.getResultEnzymes().add(resultEnzymes.get(entry));
		}
		return workspaceGenesData;

	}


	/**
	 * Get compounds available in reactions.
	 * 
	 * @param resultReactions
	 * @return
	 */
	public static Set<String> getCompoundsInReactions(ConcurrentHashMap<String,ReactionContainer> resultReactions) {

		Set<String> compoundsInReactions = new HashSet<>();

		for(String key : resultReactions.keySet()) {

			ReactionContainer rc = resultReactions.get(key);

			for(MetaboliteContainer metabolite : rc.getReactantsStoichiometry()) {

				String prefix = "cpd:";
				if(metabolite.getExternalIdentifier().startsWith("D"))
					prefix = "dr:";
				else if(metabolite.getExternalIdentifier().startsWith("G"))
					prefix = "gl:";

				String metaboliteExtId = prefix.concat(metabolite.getExternalIdentifier());
				if(!compoundsInReactions.contains(metaboliteExtId))
					compoundsInReactions.add(metaboliteExtId);
			}

			for(MetaboliteContainer metabolite : rc.getProductsStoichiometry()) {

				String prefix = "cpd:";
				if(metabolite.getExternalIdentifier().startsWith("D"))
					prefix = "dr:";
				else if(metabolite.getExternalIdentifier().startsWith("G"))
					prefix = "gl:";

				String metaboliteExtId = prefix.concat(metabolite.getExternalIdentifier());
				if(!compoundsInReactions.contains(metaboliteExtId))
					compoundsInReactions.add(metaboliteExtId);
			}
		}

		return compoundsInReactions;
	}

	/**
	 * @param cancel
	 */
	public void setCancel(AtomicBoolean cancel) {

		this.cancel = cancel;
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
