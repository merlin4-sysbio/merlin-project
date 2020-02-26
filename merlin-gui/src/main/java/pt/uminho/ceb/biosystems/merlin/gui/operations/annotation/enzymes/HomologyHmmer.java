/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.gui.operations.annotation.enzymes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.axis.AxisFault;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologuesData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.EbiRemoteDatabasesEnum;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HmmerRemoteDatabasesEnum;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologySearchServer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TimeLeftProgress;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.HomologueSequencesSearch;
import pt.uminho.ceb.biosystems.merlin.services.annotation.AnnotationEnzymesServices;
import pt.uminho.ceb.biosystems.merlin.services.annotation.loaders.LoadSimilarityResultstoDatabase;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelSequenceServices;

/**
 * @author ODias
 *
 */
@Operation(description="Perform a semi-automatic (re)annotation of the organism's genome. This process may take several hours, depending on the web-server availability.")
public class HomologyHmmer implements PropertyChangeListener {

	final static Logger logger = LoggerFactory.getLogger(HomologyBlastEBI.class);
	private HmmerRemoteDatabasesEnum database;
	private String numberOfAlignments;
	private String eVal;
	private TimeLeftProgress progress = new TimeLeftProgress();
	private HomologueSequencesSearch hmmer_loader;
	private boolean uniprotStatus;
	private int latencyWaitingPeriod;
	private ConcurrentLinkedQueue<AnnotationEnzymesHomologuesData> resultsList;
	private long startTime;
	private WorkspaceAIB project;
	private Map<String, AbstractSequence<?>> sequences;

	@Port(direction=Direction.INPUT, name="expected Value",defaultValue="1e-30",description="Default: '1e-30'",order=1)
	public void setUser(String eVal){

		if(eVal.isEmpty()) {

			this.eVal="1e-30";
		}
		else {

			this.eVal = eVal;
		}
	}

	@Port(direction=Direction.INPUT, name="remote database", validateMethod="checkDatabase", description="Select the sequence database to run searches against",order=2)
	public void setRemoteDatabase(HmmerRemoteDatabasesEnum database){
		this.database=database;
	}

	@Port(direction=Direction.INPUT, name="number of results",defaultValue="100",  advanced = true, description="Select the maximum number of aligned sequences to display. Default: '100'",order=3)
	public void setNumberOfAlignments(String numberOfAlignments) {

		if(numberOfAlignments.isEmpty()) {

			this.numberOfAlignments = "100";
		}
		else {

			this.numberOfAlignments = numberOfAlignments;
		}
	}


	@Port(direction=Direction.INPUT, name="UniProt Status",description="retrieve status from uniprot",  advanced = true, defaultValue = "false", order=4)
	public void retrieveUniprotStatus(boolean uniprotStatus) {

		this.uniprotStatus = uniprotStatus;
	}

	@Port(direction=Direction.INPUT, name="latency period",description="request latency waiting period (minutes)",  advanced = true, validateMethod="checkLatencyWaitingPeriod", defaultValue = "30", order=5)
	public void setLatencyWaitingPeriod(int latencyWaitingPeriod) {

		this.latencyWaitingPeriod = latencyWaitingPeriod;
	}

	/**
	 * @param project
	 * @throws SQLException
	 * @throws AxisFault
	 */
	@Port(direction=Direction.INPUT, name="workspace",description="Select Workspace",validateMethod="checkProject", order=6)
	public void selectProject(WorkspaceAIB project) throws SQLException{

		try  {
			
			sequences = ModelSequenceServices.getGenomeFromDatabase(project.getName(), SequenceType.PROTEIN);

			this.hmmer_loader = new HomologueSequencesSearch(resultsList, sequences, HomologySearchServer.HMMER, null, project.getTaxonomyID());
			this.hmmer_loader.setTaxonomyNames(project.getOrganismName(), project.getOrganismLineage(), database);
			this.hmmer_loader.addPropertyChangeListener(this);
			this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
			this.hmmer_loader.setLatencyWaitingPeriod(this.latencyWaitingPeriod*60000);

			int errorOutput =  this.hmmer_loader.hmmerSearchSequences(this.database, Integer.parseInt(this.numberOfAlignments), Double.parseDouble(this.eVal), this.uniprotStatus);

			if(!this.hmmer_loader.isCancel().get()) {
				errorOutput =  this.hmmer_loader.hmmerSearchSequences(this.database, Integer.parseInt(this.numberOfAlignments), Double.parseDouble(this.eVal), this.uniprotStatus);
			}

			if(!this.hmmer_loader.isCancel().get()) {
				if(errorOutput == 0) {

					if(AnnotationEnzymesServices.removeDuplicates(project.getName()) && !this.hmmer_loader.isCancel().get())
						errorOutput = this.hmmer_loader.hmmerSearchSequences(this.database, Integer.parseInt(this.numberOfAlignments), Double.parseDouble(this.eVal), this.uniprotStatus);

					if(errorOutput == 0 && !hmmer_loader.isCancel().get()) {

						MerlinUtils.updateEnzymesAnnotationView(project.getName());
						Workbench.getInstance().info("Hmmer search complete!");
					}
				}
			}

			if(!this.hmmer_loader.isCancel().get()) {
				if(errorOutput > 0)
					Workbench.getInstance().error("Errors have ocurred while processsing "+errorOutput+" query(ies).");
			}else {
				Workbench.getInstance().warn("HMMER search cancelled!");
			}

		}
		catch (Error e) {Workbench.getInstance().error(e); e.printStackTrace();}
		catch (IOException e) {Workbench.getInstance().error(e);e.printStackTrace();}
		catch (ParseException e) {Workbench.getInstance().error(e);e.printStackTrace();}
		catch (Exception e) {Workbench.getInstance().error(e);e.printStackTrace();}
	}



	/**
	 * @return
	 */
	@Progress
	public TimeLeftProgress getProgress(){
		return this.progress;
	}

	/**
	 * 
	 */
	@Cancel
	public void cancel() {

		String[] options = new String[2];
		options[0]="yes";
		options[1]="no";

		int result=CustomGUI.stopQuestion("Cancel confirmation", "Are you sure you want to cancel the operation?", options);

		if(result==0) {
			this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-GregorianCalendar.getInstance().getTimeInMillis()),1,1);
			this.hmmer_loader.setCancel();
			logger.warn("HMMER search canceled!");
			Workbench.getInstance().warn("Please hold on. Your operation is being cancelled.");

		}

	}

	/**
	 * @param database
	 */
	public void checkDatabase(HmmerRemoteDatabasesEnum database) {

		this.database=database;
	}

	/**
	 * @param project
	 */
	public void checkProject(WorkspaceAIB project) {
		
		if(project == null) {

			throw new IllegalArgumentException("No ProjectGUISelected!");
		}
		else {
			this.project = project;
			try {
				if(project.getTaxonomyID()<0)
					throw new IllegalArgumentException("The loaded genome is not in the NCBI fasta format.\nPlease enter the taxonomic identification from NCBI taxonomy.");

				if(!ModelSequenceServices.checkGenomeSequences(project.getName(), SequenceType.PROTEIN))//!Project.isFaaFiles(dbName, taxID))
					throw new IllegalArgumentException("Please add 'faa' files to perform hmmer homology searches.");

				String ebiBlastDatabase = AnnotationEnzymesServices.getEbiBlastDatabase(project.getName());

				if(ebiBlastDatabase!=null) {

					boolean go = true;
					for(EbiRemoteDatabasesEnum r : EbiRemoteDatabasesEnum.values())
						if(r.toString().replace("uniprotkb_", "").equalsIgnoreCase(this.database.toString()))
							go=false;

					if(go)
						throw new IllegalArgumentException("The database selected for performing HMMER is not compatible with the EBI Blast.");
				}
			} 
			catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param project
	 */
	public void checkLatencyWaitingPeriod(int latencyWaitingPeriod) {

		if(latencyWaitingPeriod <0) {

			throw new IllegalArgumentException("The latency waiting period must be greater than 0 (zero)");
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		try {
			if(evt.getPropertyName().equalsIgnoreCase("sequencesCounter")) {

				int sequencesCounter =	(int) evt.getNewValue();			
				this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), sequencesCounter, this.hmmer_loader.getSequences_size());
			}
			else if(evt.getPropertyName().equalsIgnoreCase("resultsList")) {

				while(this.resultsList.size()>0) {

					LoadSimilarityResultstoDatabase lbr = new LoadSimilarityResultstoDatabase(this.project.getName(),this.resultsList.poll(), 
							Double.parseDouble(this.eVal), (float) 0, (float) 1, (float) 0, (float) 0, (float) 0,  Integer.parseInt(this.numberOfAlignments), 
							this.hmmer_loader.isCancel(), true, this.sequences);
	
					lbr.loadData(project.getName());
				}
			}
			else if(evt.getPropertyName().equalsIgnoreCase("updateLoadedGenes")) {

				Set<String> genes = AnnotationEnzymesServices.getGenesFromDatabase(this.project.getName(), Double.parseDouble(this.eVal), null,
						Integer.parseInt(this.numberOfAlignments), (short) -1, "hmmer",  database.toString(), true);
				this.hmmer_loader.setLoadedGenes(genes);
			}
			else
				logger.warn("Property {} not being processed!", evt.getPropertyName());
		} 
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
	}

}
