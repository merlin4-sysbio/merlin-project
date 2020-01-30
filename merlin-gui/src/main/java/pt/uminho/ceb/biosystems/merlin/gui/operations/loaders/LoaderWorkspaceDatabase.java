package pt.uminho.ceb.biosystems.merlin.gui.operations.loaders;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import pt.uminho.ceb.biosystems.merlin.gui.utilities.NewWorkspaceRequirements;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TimeLeftProgress;
import pt.uminho.ceb.biosystems.merlin.processes.model.kegg.KeggDataRetriever;
import pt.uminho.ceb.biosystems.merlin.processes.model.kegg.KeggDataRetrieverRunnable;
import pt.uminho.ceb.biosystems.merlin.services.DatabaseServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.merlin.utilities.DatabaseFilesPaths;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;


@Operation(name="Load Database",description="Load KEGG metabolic information")
public class LoaderWorkspaceDatabase implements PropertyChangeListener {

	public static final String DEFAULT_ORG = "no_org";
	public static final String KEGG_XML_FILENAME = "keggXMLbackup.zip";
	private final boolean ACTIVE_COMPOUNDS = true;
	private KeggDataRetrieverRunnable keggRetriever;
	private long startTime;
	private String message;
	private int dataSize;
	public TimeLeftProgress progress = new TimeLeftProgress();
	private AtomicBoolean cancel = new AtomicBoolean(false);
	private static final String FTP_MERLIN_KEGGDATA = "https://merlin-sysbio.org/data/kegg2/old_versions/keggXMLbackup20200116_0958.zip";
	final static Logger logger = LoggerFactory.getLogger(LoaderWorkspaceDatabase.class);


	@Port(direction=Direction.INPUT, name="workspace",description="select workspace",//validateMethod="checkProject",
			order=1)
	public void setProject(WorkspaceAIB project) {

		this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
		this.cancel = new AtomicBoolean(false);

		
		try {
			
			if(!ModelReactionsServices.checkIfReactionsHasData(project.getName())) {
				
				String path = DatabaseFilesPaths.getModelPath(false);
				DatabaseServices.executeDropTable(project.getName(), FileUtils.readLines(path));
				String keggXmlPath = FileUtils.getUtilitiesFolderPath().concat(KEGG_XML_FILENAME) ;
				
				try {
					URL website = new URL(FTP_MERLIN_KEGGDATA);
					org.apache.commons.io.FileUtils.copyURLToFile(website, new File (keggXmlPath));
				}
				catch (Exception e) {
					logger.error("Connection to get file was not possible. Using local backup!");
					e.printStackTrace();
				}
				
				String workspaceKeggTaxonomyPath = FileUtils.getWorkspaceTaxonomyFolderPath(project.getName(), project.getTaxonomyID()).concat("keggXml/");
				
				File keggDataXml = new File(workspaceKeggTaxonomyPath);
				
				if(keggDataXml.exists())
					org.apache.commons.io.FileUtils.deleteDirectory(keggDataXml);

				FileUtils.extractZipFile(keggXmlPath, workspaceKeggTaxonomyPath);
							
				this.message = "loading metabolic data";
				
				DatabaseServices.readxmldb(project.getName(), workspaceKeggTaxonomyPath, this.cancel, this);
				
				org.apache.commons.io.FileUtils.deleteDirectory(new File (workspaceKeggTaxonomyPath));
				
				NewWorkspaceRequirements.injectRequiredDataToNewWorkspace(project.getName());
				
				
				
				if(!this.cancel.get()) {
					
					MerlinUtils.updateAllViews(project.getName());
					Workbench.getInstance().info("metabolic data successfully loaded");
				}
				else {

					Workbench.getInstance().warn("metabolic data loading canceled");
				}
				
			}
			else{
				Workbench.getInstance().warn("metabolic data already loaded");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			Workbench.getInstance().info("an error occurred while performing the operation.");
		}
	}


	/**
	 * @return
	 * @throws Exception
	 */
	public WorkspaceData retrieveKeggData() throws Exception {

		KeggDataRetriever retrieveKeggData = new KeggDataRetriever(DEFAULT_ORG, this.cancel);
		retrieveKeggData.addPropertyChangeListener(this);
		WorkspaceData workspaceMetabolicData = retrieveKeggData.retrieveMetabolicData(ACTIVE_COMPOUNDS);
		return workspaceMetabolicData;

	}
	
	

	/**
	 * @return
	 */
	@Progress
	public TimeLeftProgress getProgress() {

		return progress;
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		if(evt.getPropertyName().equalsIgnoreCase("message"))
			this.message = (String) evt.getNewValue();
		
		if(evt.getPropertyName().equalsIgnoreCase("size")) {
			this.dataSize = (int) evt.getNewValue();
		}
		
		if(evt.getPropertyName().equalsIgnoreCase("tablesCounter")) {
						
			int tablesCounter = (int) evt.getNewValue();
			this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis() - startTime), tablesCounter, dataSize, this.message);
		}
	}

	@Cancel
	public void cancel() {

		String[] options = new String[2];
		options[0] = "yes";
		options[1] = "no";

		int result = CustomGUI.stopQuestion("Cancel confirmation", "Are you sure you want to cancel the operation?", options);

		if (result == 0) {

			this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-GregorianCalendar.getInstance().getTimeInMillis()),1,1);
			this.cancel.set(true);
			this.keggRetriever.setCancel();
			
			Workbench.getInstance().warn("Please hold on. Your operation is being cancelled.");
		}


	}
}
