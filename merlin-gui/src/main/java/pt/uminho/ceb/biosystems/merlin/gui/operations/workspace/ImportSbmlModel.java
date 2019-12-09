package pt.uminho.ceb.biosystems.merlin.gui.operations.workspace;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.biocomponents.io.Enumerators.ModelSources;
import pt.uminho.ceb.biosystems.merlin.biocomponents.io.Enumerators.SBMLModelLevel;
import pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers.MerlinSBML3Reader;
import pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers.MerlinSBMLContainer;
import pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers.ModelImporter;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceData;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceInitialData;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TimeLeftProgress;
import pt.uminho.ceb.biosystems.merlin.processes.WorkspaceProcesses;
import pt.uminho.ceb.biosystems.merlin.services.WorkspaceInitialDataServices;
import pt.uminho.ceb.biosystems.merlin.services.model.loaders.LoadMetabolicData;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.JSBMLReader;


/**
 * @author amaromorais
 *
 */
@Operation(description="Import model from SBML.")
public class ImportSbmlModel {

	private String databaseName;
	private WorkspaceAIB project=null;
	private File model;
	private ModelSources modelSource;
	private SBMLModelLevel level;
	private TimeLeftProgress progress = new TimeLeftProgress();
	
	@Port(direction=Direction.INPUT, name="select workspace", validateMethod = "checkProject", order=1)

	public void setProject(WorkspaceAIB project) {

	}
	
	/**
	 * @param project
	 */
	public void checkProject(WorkspaceAIB project) {

		this.project = project;
		
		if(this.project==null) {

			throw new IllegalArgumentException("please select a workspace.");
		}
		
		else {
			this.databaseName = project.getName();
		}
	}



	@Port(direction=Direction.INPUT,validateMethod="",name="sbml source", description="",order=2)
	public void setExtensions(ModelSources source) {

		this.modelSource = source;
	}
	
	@Port(direction=Direction.INPUT,validateMethod="",name="sbml level", description="",order=3)
	public void setSBMLLevel(SBMLModelLevel level) {

		this.level = level;
	}
	
	/**
	 * @param directory
	 * @throws IOException 
	 */
	@Port(direction=Direction.INPUT, name="sbml model:",description="",validateMethod="checkModelPath",order=4)
	public void selectDirectory(File model){
		
		if(!model.getAbsolutePath().endsWith(".xml") && !model.getAbsolutePath().endsWith(".sbml")){

			throw new IllegalArgumentException("Please select a valid '.xml'/'.sbml' file!");
		}

		try {
			
			MerlinSBMLContainer cont;
			
			if(this.level.equals(SBMLModelLevel.L2)){
				JSBMLReader reader = new JSBMLReader(this.model.getAbsolutePath(), this.databaseName);
				cont = new MerlinSBMLContainer(reader);
			}
			else{
				
				MerlinSBML3Reader reader = new MerlinSBML3Reader(this.model.getAbsolutePath(), this.databaseName);
				cont = new MerlinSBMLContainer(reader);
			}
				
			ModelImporter modelImporter = new ModelImporter(cont, this.modelSource);
			
			File genBankFile;
			if((genBankFile = WorkspaceProcesses.checkGenBankFile(this.databaseName, this.project.getTaxonomyID()))!=null)
				modelImporter.createProteinIdLocusTagMap(genBankFile);

			WorkspaceData data = modelImporter.getWorkspaceData();	
			
			
			WorkspaceInitialData databaseInitialData = new WorkspaceInitialData();
			databaseInitialData = WorkspaceInitialDataServices.retrieveAllData(this.databaseName);

			
			AtomicBoolean cancel = new AtomicBoolean(false);
			AtomicInteger dataSize = new AtomicInteger(10); 

			Runnable importModel;

			importModel = new LoadMetabolicData(this.project.getName(), data, databaseInitialData, cancel, dataSize);
			importModel.run();
			
		} catch (Exception e) {

			e.printStackTrace();
			throw new IllegalArgumentException("error while importing.");
		}

		Workbench.getInstance().info("model successfully imported with name: " + this.databaseName);

	}
	

	/**
	 * @return
	 */
	@Progress(progressDialogTitle = "importing model", modal = false, workingLabel = "loading SBML model", preferredWidth = 400, preferredHeight=300)
	public TimeLeftProgress getProgress(){

		return progress;
	}

	/**
	 * @param directory
	 */
	public void checkModelPath(File model) {

		if(model == null || model.toString().isEmpty()) {

			throw new IllegalArgumentException("Please select a '.xml'/'.sbml' file!");
		}
		else {
			
			this.model = model;
		}
	}
}
