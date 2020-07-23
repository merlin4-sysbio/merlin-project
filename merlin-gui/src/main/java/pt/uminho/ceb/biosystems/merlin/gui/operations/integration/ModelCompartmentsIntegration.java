package pt.uminho.ceb.biosystems.merlin.gui.operations.integration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.compartments.AnnotationCompartmentsGenes;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TimeLeftProgress;
import pt.uminho.ceb.biosystems.merlin.processes.model.compartments.CompartmentsAnnotationIntegrationProcesses;
import pt.uminho.ceb.biosystems.merlin.processes.verifiers.CompartmentsVerifier;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.annotation.AnnotationCompartmentsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;

@Operation(name="integrate compartments", description="integrate compartments to the model reactions")
public class ModelCompartmentsIntegration implements PropertyChangeListener {

	private boolean loaded;
	private TimeLeftProgress progress = new TimeLeftProgress();
	private CompartmentsAnnotationIntegrationProcesses integration;
	private List<String> ignoreList;
	private AtomicBoolean cancel;
	private AtomicInteger processingCounter;
	private AtomicInteger querySize; 
	private long startTime;
	private boolean biochemical;
	private boolean transport;
	private WorkspaceAIB workspace;
	private CompartmentContainer membraneCompartment;
	private boolean go = true;
	private boolean cloneWorkspace;

	final static Logger logger = LoggerFactory.getLogger(ModelCompartmentsIntegration.class);

	@Port(direction=Direction.INPUT, name="biochemical", order=1)
	public void setBiochemical(boolean biochemical){

		this.biochemical = biochemical;
	};

	@Port(direction=Direction.INPUT, name="transporters", order=2)
	public void setTransporters(boolean transport){

		this.transport = transport;
	};

	@Port(direction=Direction.INPUT, name="ignore", order=3)
	public void setIgnore(List<String> ignore){

		this.ignoreList = ignore;
	};

	@Port(direction=Direction.INPUT, name="project", order=4)
	public void setProject(WorkspaceAIB project){
		try {
			checkProject(project);
		} catch (Exception e) {
			Workbench.getInstance().error(e);
			this.go=false;
			e.printStackTrace();
		}
	};

	@Port(direction=Direction.INPUT, name="compartment",description="name of the default membrane compartment", advanced=true, defaultValue = "auto", order = 5)
	public void setDefaultMembrane(String compartment) {
		try {
			checkMembraneCompartment(compartment);
		} catch (Exception e) {
			Workbench.getInstance().error(e);
			this.go=false;
			e.printStackTrace();
		}
	};

	@Port(direction=Direction.INPUT, name="clone workspace before integrating",defaultValue="false", description="clone the currently selected workspace and integrate compartments in the cloned version",order=6)
	public void setCloneWorkspace(boolean cloneWorkspace){

		this.cloneWorkspace = cloneWorkspace;
	}


	@Port(direction=Direction.INPUT, name="geneCompartments", order=7)
	public void setGeneCompartments(Map<Integer, AnnotationCompartmentsGenes> geneCompartments) throws Exception{


		if(this.cloneWorkspace == true)
			System.out.println("test");




		this.cancel = new AtomicBoolean(false);
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();

		if(this.go && ProjectServices.isGeneDataAvailable(workspace.getName())) {

			this.progress = new TimeLeftProgress();
			this.querySize = new AtomicInteger(geneCompartments.size());
			this.processingCounter = new AtomicInteger();
			this.cancel = new AtomicBoolean();

			this.integration = new CompartmentsAnnotationIntegrationProcesses(this.workspace.getName(), geneCompartments);
			this.integration.addPropertyChangeListener(this);
			this.integration.setQuerySize(this.querySize);
			this.integration.setProcessingCounter(this.processingCounter);
			this.integration.setCancel(this.cancel);
			this.integration.setDefaultMembraneCompartment(this.membraneCompartment);

			boolean result = false;

			if(!this.cancel.get()){
				if(this.loaded)				
					result = integration.initProcessCompartments();
				else
					result = integration.performIntegration();
			}

			this.progress.setTime(0, 0, 0, "processing biochemical reactions");

			if(this.biochemical && !this.cancel.get())
				result = integration.assignCompartmentsToMetabolicReactions(ignoreList);

			System.out.println(result + " outcome of biochemical");

			if(this.transport && ProjectServices.isTransporterLoaded(this.workspace.getName()) && !this.cancel.get()) {

				try {

					this.progress.setTime(0, 0, 0, "processing transport reactions");

					result = integration.assignCompartmentsToTransportReactions(ignoreList, false);

					System.out.println(result + " outcome of transporters");
				}
				catch (Exception e) {

					result = false;

					System.out.println(result + "error during transporters");

					e.printStackTrace();

					Workbench.getInstance().error(e);
				}
			}

			MerlinUtils.updateCompartmentsAnnotationView(workspace.getName());
			MerlinUtils.updateReactionsView(workspace.getName());

			if(result && !this.cancel.get()) {

				Workbench.getInstance().info("Compartments integration complete!");
			}
			else{

				Workbench.getInstance().error("an error occurred while performing the operation.");
			}
		}
		else if(this.go) {
			Workbench.getInstance().error("gene data for integration unavailable!");
		}

	};

	/**
	 * @param workspace
	 */
	public void checkProject(WorkspaceAIB workspace) throws Exception{


		if(workspace == null) {

			throw new IllegalArgumentException("no project selected!");
		}
		else {
			try {

				this.workspace = workspace;

				if(!AnnotationCompartmentsServices.areCompartmentsPredicted(workspace.getName()))
					throw new IllegalArgumentException("please perform the compartments prediction operation before integrating compartments data.");

				int comp_genes = ModelGenesServices.countGenesInGeneHasCompartment(workspace.getName());
				int genes = ModelGenesServices.countEntriesInGene(workspace.getName());

				this.loaded = genes == comp_genes;

			} 
			catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}	
		}
	}


	/**
	 * @param compartment
	 * @throws Exception
	 */
	public void checkMembraneCompartment(String compartment) throws Exception {

		this.membraneCompartment = CompartmentsVerifier.checkMembraneCompartment(compartment, this.workspace.getName(), this.workspace.isEukaryoticOrganism());

		if(this.membraneCompartment == null) {
			//            Workbench.getInstance().warn("No membrane compartmentID defined!");
			logger.warn("No membrane compartmentID defined!");
		}

	}


	/**
	 * @return
	 */
	@Progress
	public TimeLeftProgress getProgress() {

		return this.progress;
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		progress.setTime(GregorianCalendar.getInstance().getTimeInMillis() - this.startTime, this.processingCounter.get(), this.querySize.get());
	}

	/**
	 * 
	 */
	@Cancel
	public void cancel() {

		Workbench.getInstance().warn("operation canceled!");

		this.progress.setTime(0,1,1);
		this.integration.cancel();
	}
}
