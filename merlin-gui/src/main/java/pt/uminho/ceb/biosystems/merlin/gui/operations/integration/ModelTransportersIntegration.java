package pt.uminho.ceb.biosystems.merlin.gui.operations.integration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.compartments.AnnotationCompartmentsGenes;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.operations.transporters.transyt.IntegrateTransportersDataTransyt;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TimeLeftProgress;
import pt.uminho.ceb.biosystems.merlin.processes.model.compartments.CompartmentsAnnotationIntegrationProcesses;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.JSBMLLevel3Reader;

@Operation(name="IntegrateTransporterstoDatabase", description="Integrates the generated transporters data into the model")
public class ModelTransportersIntegration implements PropertyChangeListener {

	private Map<Integer, AnnotationCompartmentsGenes> geneCompartments;
	private TimeLeftProgress progress = new TimeLeftProgress();
	private IntegrateTransportersDataTransyt transportersData;
	private long startTime;
	private AtomicBoolean cancel;
	private AtomicInteger querySize;
	private AtomicInteger processingCounter;
	private String transytResultPath;
	private CompartmentContainer defaultInternalCompartment;
	private CompartmentContainer defaultExternalCompartment;
	private CompartmentContainer defaultMembraneCompartment;

	@Port(direction=Direction.INPUT, name="compartments",description="select compartments", order=1)
	public void setIntegration(Map<Integer, AnnotationCompartmentsGenes> geneCompartments) throws Exception {

		this.geneCompartments = geneCompartments;
	}

	@Port(direction=Direction.INPUT, name="transytResultPath",description="transyt's results file path", order=2)
	public void setTransytFile(String transytResultPath) throws Exception {

		this.transytResultPath = transytResultPath;

	}
	
	@Port(direction=Direction.INPUT, name="defaultInternalCompartment", order=3)
	public void setDefaultInternalCompartment(CompartmentContainer defaultInternalCompartment) throws Exception {

		this.defaultInternalCompartment = defaultInternalCompartment;

	}
	
	@Port(direction=Direction.INPUT, name="defaultExternalCompartment", order=4)
	public void setDefaultExternalCompartment(CompartmentContainer defaultExternalCompartment) throws Exception {

		this.defaultExternalCompartment = defaultExternalCompartment;

	}
	
	@Port(direction=Direction.INPUT, name="defaultMembraneCompartment", order=5)
	public void setDefaultMembraneCompartment(CompartmentContainer defaultMembraneCompartment) throws Exception {

		this.defaultMembraneCompartment = defaultMembraneCompartment;

	}

	@Port(direction=Direction.INPUT, name="workspace",description="select workspace", order=6)
	public void setProject(WorkspaceAIB project) throws Exception {

		this.cancel = new AtomicBoolean(false);
		this.querySize = new AtomicInteger();
		this.processingCounter = new AtomicInteger();

		boolean isCompartmentalisedModel = ProjectServices.isCompartmentalisedModel(project.getName());


//		if(!isCompartmentalisedModel || this.geneCompartments!= null) {

			if(ProjectServices.isMetabolicDataAvailable(project.getName())) {

				if(!this.cancel.get()) {

					this.startTime = GregorianCalendar.getInstance().getTimeInMillis();

					JSBMLLevel3Reader reader;
					Container container=null;
					try {

						reader = new JSBMLLevel3Reader(this.transytResultPath, project.getOrganismName());
						container = new Container(reader);

						Map<String, ReactionCI> reactions = container.getReactions();  //assuming that all retrieved reactions are already only transporters! If not, perform container.identifyTransportReactions() first

						this.transportersData = new IntegrateTransportersDataTransyt(project, reactions);
						this.transportersData.setTimeLeftProgress(this.progress);

						boolean result = this.transportersData.performIntegration();
						
						if(result && isCompartmentalisedModel) {

							if(!transportersData.isCancel().get()) {
								
								boolean go = ProjectServices.isCompartmentalisedModel(project.getName()); 

								if(go) {
									
									CompartmentsAnnotationIntegrationProcesses integration = 
											new CompartmentsAnnotationIntegrationProcesses(project.getName(), geneCompartments);
									integration.setQuerySize(querySize);
									integration.setProcessingCounter(processingCounter);
									integration.addPropertyChangeListener(this);
									if(geneCompartments == null) {
										integration.setExternalCompartment(defaultExternalCompartment);
										integration.setInternalCompartment(defaultInternalCompartment);
									}
									
									integration.setEukaryote(project.isEukaryoticOrganism());
									integration.setDefaultMembraneCompartment(defaultMembraneCompartment);
									integration.initProcessCompartments();
//									integration.performIntegration(); //this step should be performed first during the metabolic reactions compartmentalization
									result = integration.assignCompartmentsToTransportReactions(new ArrayList<String>(), go);
								}


								Workbench.getInstance().info("transporters integration complete. check the reactions at Transporters Pathway in reactions view");
							}
							else {

								Workbench.getInstance().warn("transporters integration canceled");
							}
						}
						else if(result && !isCompartmentalisedModel) {
							Workbench.getInstance().info("transporters integration complete but not compartmentalised (no compartments predictions found in 'annotation compartments board'). check the reactions at 'Transporters Pathway' in reactions view"); 
						}
						else{

							Workbench.getInstance().warn("all transport reactions generated were integrated, however an error occurred while compartmentalizing these reactions");
						}
					}
					catch (NullPointerException e) {

						e.printStackTrace();
						Workbench.getInstance().info("no transport reactions were generated");
					}
					catch (Exception e) {

						e.printStackTrace();
						Workbench.getInstance().error(e);
					}
				} 
			}
			else {

				Workbench.getInstance().warn("metabolic data for integration unavailable");
			}
//		}
//		else {
//
//			Workbench.getInstance().warn("please perform the compartments prediction to assign compartments to transport reactions");
//		}	

		MerlinUtils.updateReactionsView(project.getName());
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
	public void cancel(){
		this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-GregorianCalendar.getInstance().getTimeInMillis()),1,1);
		this.transportersData.setCancel();
	}

	/**
	 * @param project
	 */
	public void checkProject(WorkspaceAIB project){

		if(project == null)
			throw new IllegalArgumentException("no workspace selected!");
	}
}