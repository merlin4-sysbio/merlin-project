package pt.uminho.ceb.biosystems.merlin.gui.operations.loaders.annotation.enzymes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicBoolean;

import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceData;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TimeLeftProgress;
import pt.uminho.ceb.biosystems.merlin.processes.model.kegg.KeggDataRetriever;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.loaders.kegg.KeggDataLoader;

@Operation(name="load KEGG annotation",description="load model with KEGG annotation")
public class LoadAnnotationKegg  implements PropertyChangeListener {


	private WorkspaceAIB project;
	private TimeLeftProgress progress = new TimeLeftProgress();
	private AtomicBoolean cancel = new AtomicBoolean(false);
	private String message;
	private int dataSize;
	private long startTime;

	@Port(direction=Direction.INPUT, name="workspace",description="select workspace", order=1)
	public void setProject(WorkspaceAIB project){
		this.project = project;
	}


	@Port(direction=Direction.INPUT, name="organism", order=2)
	public void setOrganism(String organism) throws Exception {
		
		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		
		if(ModelReactionsServices.checkIfReactionsHasData(this.project.getName())) {

			WorkspaceData workspaceMetabolicData = this.getOrganismData(organism);

			if(!this.cancel.get())			
				KeggDataLoader.loadData(this.project.getName(), workspaceMetabolicData, cancel);
			
			this.getOrganismData(organism);


			if(!this.cancel.get()){

				MerlinUtils.updateAllViews(project.getName());
				Workbench.getInstance().info("annotation successfully loaded.");
			}
			else {

				Workbench.getInstance().warn("annotation loading cancelled!");
			}
		}

		else{
			Workbench.getInstance().warn("Please load metabolic data!");
		}
	}


	/**
	 * Get organisms annotation data from kegg web servers. 
	 * 
	 * @param project
	 * @param organismID
	 * @return
	 */
	public WorkspaceData getOrganismData(String organismID) {

		try  {

			KeggDataRetriever retrieveKeggData = new KeggDataRetriever(organismID, this.cancel);
			retrieveKeggData.addPropertyChangeListener(this);
			WorkspaceData workspaceData = retrieveKeggData.retrieveOrganismData();
			
			return workspaceData;
		}
		catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if(evt.getPropertyName().equalsIgnoreCase("message"))
			this.message = (String) evt.getNewValue();
		
		if(evt.getPropertyName().equalsIgnoreCase("size"))
			this.dataSize = (int) evt.getNewValue();
		
		if(evt.getPropertyName().equalsIgnoreCase("sequencesCounter")) {
			int sequencesCounter = (int) evt.getNewValue();
			this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis() - startTime), sequencesCounter, dataSize, this.message);
		}
	}

	/**
	 * @return
	 */
	@Progress
	public TimeLeftProgress getProgress() {

		return progress;
	}

	/**
	 * 
	 */
	@Cancel
	public void cancel(){
		
		String[] options = new String[2];
		options[0] = "yes";
		options[1] = "no";
		
		int result = CustomGUI.stopQuestion("Cancel confirmation", "Are you sure you want to cancel the operation?", options);
		
		if(result == 0) {
			
			this.progress.setTime(0,1,1);
			this.cancel.set(true);
			Workbench.getInstance().warn("Please hold on. Your operation is being cancelled.");
			
		}
	}
}
