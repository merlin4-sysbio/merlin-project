package pt.uminho.ceb.biosystems.merlin.gui.operations.annotation.enzymes;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.axis2.AxisFault;
import org.apache.commons.validator.routines.EmailValidator;
import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.MySleep;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologuesData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.BlastMatrix;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.BlastProgram;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.EbiRemoteDatabasesEnum;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologySearchServer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologyStatus;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Matrix;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.NumberofAlignments;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationEnzymesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.AIBenchUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TimeLeftProgress;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.HomologueSequencesSearch;
import pt.uminho.ceb.biosystems.merlin.services.annotation.AnnotationEnzymesServices;
import pt.uminho.ceb.biosystems.merlin.services.annotation.loaders.LoadSimilarityResultstoDatabase;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelSequenceServices;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

/**
 * @author Oscar
 *
 */
@Operation(name="EBI annotation", description="perform a semi-automatic (re)annotation of the organism's genome. this process may take several hours, depending on the web-server availability.")
public class HomologyBlastEBI  implements PropertyChangeListener {

	final static Logger logger = LoggerFactory.getLogger(HomologyBlastEBI.class);
	private String program;
	private BlastMatrix blastMatrix=BlastMatrix.AUTOSELECTION;
	private EbiRemoteDatabasesEnum database;
	private NumberofAlignments numberOfAlignments;
	private String eVal;
	private HomologueSequencesSearch ebiBlastSearch;
	private boolean autoEval;
	private int latencyWaitingPeriod;
	private SequenceType sequenceType;
	private String email;
	private long startTime;
	private ConcurrentLinkedQueue<AnnotationEnzymesHomologuesData> resultsList;
	private TimeLeftProgress progress = new TimeLeftProgress();
	private boolean emailValid;
	private WorkspaceAIB project;
	private Map<String, AbstractSequence<?>> sequences;


	@Port(direction=Direction.INPUT, name="type", validateMethod="checkProgram",defaultValue="blastp",  advanced = true, description="BLAST program. Default: 'blastp'",order=1)
	public void setDatabaseName(BlastProgram program) {

		this.program = program.toString();
	}

	@Port(direction=Direction.INPUT, name="sequence type",defaultValue="protein",  advanced = true, order=2)
	public void setSequence(SequenceType sequenceType) {

		this.sequenceType=sequenceType;
	}

	@Port(direction=Direction.INPUT, name="e-value",defaultValue="1E-30",description="expected value",order=3)
	public void setUser(String eVal) {

		if(eVal.isEmpty())
			this.eVal="1E-30";
		else 
			this.eVal = eVal;
	}

	@Port(direction=Direction.INPUT, name="adjust E-Value",defaultValue="true",  advanced = true, description="automatically adjust e-value for smaller sequences search",order=4)
	public void setEValueAutoSelection(boolean autoEval){

		this.autoEval=autoEval;
	}

	@Port(direction=Direction.INPUT, name="remote database",defaultValue="uniprotkb_swissprot", validateMethod="checkDatabase", description="select the sequence database to run searches against",order=5)
	public void setRemoteDatabase(EbiRemoteDatabasesEnum ls){

		this.database=ls;
	}

	@Port(direction=Direction.INPUT, name="number of results",defaultValue="100",  advanced = true, description="select the maximum number of aligned sequences to display. Default: '100'",order=6)
	public void setNumberOfAlignments(NumberofAlignments numberOfAlignments) {

		this.numberOfAlignments = numberOfAlignments;
	}

	@Port(direction=Direction.INPUT, name="substitution matrix",defaultValue="AUTO",  advanced = true, description="assigns a score for aligning pairs of residues. default: 'Adapts to Sequence length'.",order=7)
	public void setMatrix(BlastMatrix blastMatrix){

		this.blastMatrix = blastMatrix;
	}

	@Port(direction=Direction.INPUT, name="latency period",  advanced = true, description="request latency waiting period (minutes)", validateMethod="checkLatencyWaitingPeriod", defaultValue = "30", order=8)
	public void setLatencyWaitingPeriod(int latencyWaitingPeriod) {

		this.latencyWaitingPeriod = latencyWaitingPeriod;
	}

	/**
	 * @param project
	 * @throws SQLException
	 * @throws AxisFault
	 */
	@Port(direction=Direction.INPUT, name="workspace",description="select Workspace",validateMethod="checkProject", order=10)
	public void selectProject(WorkspaceAIB project) throws SQLException{
		
		getEmail();
		
		if(!this.emailValid) {
			Workbench.getInstance().warn("your email is invalid");
			return;
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Date date = new Date();
		System.out.println("init -> " + dateFormat.format(date));


		if(!this.email.equals("")){
			try {

				this.project = project;

				resultsList = new ConcurrentLinkedQueue<>();

				SequenceType seqType = SequenceType.PROTEIN;

				if(this.program == "blastx")
					seqType = SequenceType.CDS_DNA;

				this.sequences = ModelSequenceServices.getGenomeFromDatabase(project.getName(), seqType);

				this.ebiBlastSearch = new HomologueSequencesSearch(resultsList, this.sequences, HomologySearchServer.EBI, this.email, project.getTaxonomyID());
				this.ebiBlastSearch.setTaxonomyNames(project.getOrganismName(), project.getOrganismLineage());
				this.ebiBlastSearch.addPropertyChangeListener(this);
				this.startTime = GregorianCalendar.getInstance().getTimeInMillis();
				//it has to be milliseconds
				this.ebiBlastSearch.setLatencyWaitingPeriod(this.latencyWaitingPeriod*60000);
				this.ebiBlastSearch.setRetrieveUniprotStatus(true);


				if(blastMatrix!=BlastMatrix.AUTOSELECTION) {

					if(blastMatrix==BlastMatrix.BLOSUM62)
						this.ebiBlastSearch.setBlastMatrix(Matrix.BLOSUM62);
					if(blastMatrix==BlastMatrix.BLOSUM45)
						this.ebiBlastSearch.setBlastMatrix(Matrix.BLOSUM45);
					if(blastMatrix==BlastMatrix.BLOSUM80)
						this.ebiBlastSearch.setBlastMatrix(Matrix.BLOSUM80);
					if(blastMatrix==BlastMatrix.PAM30)
						this.ebiBlastSearch.setBlastMatrix(Matrix.PAM30);
					if(blastMatrix==BlastMatrix.PAM70)
						this.ebiBlastSearch.setBlastMatrix(Matrix.PAM70);
				}

				int errorOutput = 0;

				while(this.ebiBlastSearch.isReBlast()) {

					this.ebiBlastSearch.setReBlast(false);
					this.ebiBlastSearch.setSimilaritySearchProcessAvailable(true);
					
					errorOutput += this.ebiBlastSearch.blastSequencesEBI(this.program.toString(), this.database.toString(), this.numberOfAlignments.index(), Double.parseDouble(this.eVal) , this.autoEval, this.sequenceType.toString());

					if(this.ebiBlastSearch.isReBlast()) {

						MySleep.myWait(300000);
						this.ebiBlastSearch.setSequences_size(0);
					}
					else{
						Set<Integer> processing = AnnotationEnzymesServices.getHomologyGenesSKeyByStatus(this.project.getName(), HomologyStatus.PROCESSING, program);
						AnnotationEnzymesServices.deleteSetOfGenes(project.getName(), processing);
						
						if(AnnotationEnzymesServices.deleteGenesWithoutECRankAndProdRankInfoComplete(this.project.getName())) {
							logger.info("BLAST was terminating but EcNumber_rank_has_organism and prod_rank_has_organism were missing data. Reblasting...");
							this.ebiBlastSearch.setReBlast(true);
						}
					}
				}

				if(errorOutput == 0) {

					if(AnnotationEnzymesServices.removeDuplicates(this.project.getName()) && !this.ebiBlastSearch.isCancel().get())
						errorOutput = this.ebiBlastSearch.blastSequencesEBI(this.program.toString(), this.database.toString(), this.numberOfAlignments.index(), Double.parseDouble(this.eVal), this.autoEval, this.sequenceType.toString());

					if(errorOutput == 0 && !this.ebiBlastSearch.isCancel().get()) {

//						MerlinUtils.updateEnzymesAnnotationView(project.getName());
						Workbench.getInstance().info("BLAST process complete!");
					}
				}

				AIBenchUtils.updateView(project.getName(), AnnotationEnzymesAIB.class);

				if(errorOutput > 0)
					Workbench.getInstance().error("errors have ocurred while processsing "+errorOutput+" query(ies).");

				if(this.ebiBlastSearch.isCancel().get())
					Workbench.getInstance().warn("BLAST search canceled!");

			}
			catch (Error e) {e.printStackTrace();}//e.printStackTrace();
			catch (IOException e) {e.printStackTrace();}//e.printStackTrace();
			catch (ParseException e) {e.printStackTrace();}//e.printStackTrace();
			catch (IllegalArgumentException e) {
				Workbench.getInstance().error("Wrong email");
				e.printStackTrace();
			}
			catch (Exception e) {e.printStackTrace();}
		}
			
	}


	/**
	 * @return
	 */
	@Progress(progressDialogTitle = "EBI BLAST annotation", modal = false, workingLabel = "performing EBI BLAST annotation", preferredWidth = 400, preferredHeight=300)
	public TimeLeftProgress getProgress(){

		return progress;
	}

	/**
	 * 
	 */
	@Cancel
	public void cancel() {

		String[] options = new String[2];
		options[0]="yes";
		options[1]="no";

		int result=CustomGUI.stopQuestion("cancel confirmation", "are you sure you want to cancel the operation?", options);

		if(result==0) {
			
			progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-GregorianCalendar.getInstance().getTimeInMillis()),1,1);
			ebiBlastSearch.setCancel();
			this.propertyChange(new PropertyChangeEvent(this, "saveToDatabase", null, this.resultsList.size()));
			logger.warn("BLAST search canceled!");
			Workbench.getInstance().warn("Please hold on. Your operation is being cancelled.");
		}
	}


	/**
	 * @param database
	 */
	public void checkDatabase(EbiRemoteDatabasesEnum database) {

		this.database=database;
	}

	/**
	 * @param project
	 */
	public void checkProject(WorkspaceAIB project) {
		
		

		if(project == null) {

			throw new IllegalArgumentException("please select a workspace");
		}
		else {

			try {
				
			if(project.getTaxonomyID()<0)
				throw new IllegalArgumentException("please enter the taxonomic identification from NCBI taxonomy");

			if(this.program.toString().equalsIgnoreCase("blastp") && !ModelSequenceServices.checkGenomeSequences(project.getName(), SequenceType.PROTEIN))//!Project.isFaaFiles(dbName, taxID))
				throw new IllegalArgumentException("please add 'faa' files to perform blastp homology searches");

			if(this.program.toString().equalsIgnoreCase("blastx") && !ModelSequenceServices.checkGenomeSequences(project.getName(), SequenceType.GENOMIC_DNA))//!Project.isFnaFiles(dbName, taxID))
				throw new IllegalArgumentException("please add 'fna' files to perform blastx homology searches");

			String hmmerDatabase = AnnotationEnzymesServices.getHmmerDatabase(project.getName());
			if(hmmerDatabase!=null) {

				logger.error("the database selected for performing EBI Blast is not compatible with HMMER");
				throw new IllegalArgumentException("the database selected for performing EBI Blast is not compatible with HMMER");
			}
			
			} catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}	
		}
	}

	/**
	 * @param project
	 */
	public void checkProgram(BlastProgram program) {

		this.program = program.toString();
	}

	/**
	 * @param contents
	 */
	public void checkEmail(String email) {

		EmailValidator validator = EmailValidator.getInstance();

		if(validator.isValid(email)) {

			this.email = email;
		}
		else {

			logger.error("please set a valid email address!");
			throw new IllegalArgumentException("please set a valid email address");
		}
	}

	/**
	 * @param project
	 */
	public void checkLatencyWaitingPeriod(int latencyWaitingPeriod) {

		if(latencyWaitingPeriod <0){

			logger.error("the request latency waiting period must be greater than 0 (zero)");
			throw new IllegalArgumentException("the request latency waiting period must be greater than 0 (zero)");
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		try {
			if(evt.getPropertyName().equalsIgnoreCase("sequencesCounter")) {

				int sequencesCounter =	(int) evt.getNewValue();			
				this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), sequencesCounter, this.ebiBlastSearch.getSequences_size());
			}
			else if(evt.getPropertyName().equalsIgnoreCase("saveToDatabase")) {
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
				Date date = new Date();
				System.out.println("saving -> " + dateFormat.format(date));

				while(this.resultsList.size()>0) {

					LoadSimilarityResultstoDatabase lbr = new LoadSimilarityResultstoDatabase(this.project.getName(),this.resultsList.poll(), 
							Double.parseDouble(this.eVal), this.numberOfAlignments.index(), this.ebiBlastSearch.isCancel(), false, this.sequences);
					lbr.setGapCosts(this.ebiBlastSearch.getGapOpenPenalty()+"+"+this.ebiBlastSearch.getGapExtensionPenalty());
					lbr.setMatrix(this.ebiBlastSearch.getBlastMatrix().toString());
					lbr.setWordSize(this.ebiBlastSearch.getWordSize());
					lbr.loadData(project.getName());
				}
				
				date = new Date();
				System.out.println("saving complete -> " + dateFormat.format(date));
			}
			else if(evt.getPropertyName().equalsIgnoreCase("updateLoadedGenes")) {

				Set<String> genes = AnnotationEnzymesServices.getGenesFromDatabase(this.project.getName(), Double.parseDouble(this.eVal), this.ebiBlastSearch.getBlastMatrix().toString(), 
						this.numberOfAlignments.index(), this.ebiBlastSearch.getWordSize(), program,  database.toString(), true);
				this.ebiBlastSearch.setLoadedGenes(genes);
			}
			else
				logger.warn("Property {} not being processed!", evt.getPropertyName());
		} 
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
	}

	public void getEmail() {

		String confEmail = "";
		ArrayList<String> listLines = new ArrayList<>();
		String confPath = FileUtils.getConfFolderPath().concat("email.conf");
		File configFile = new File(confPath);
		try {
			Scanner file = new Scanner(configFile);
			while(file.hasNextLine()==true) {
				listLines.add(file.nextLine());
			}
			file.close();	
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			e.printStackTrace();

		}

		for (String item : listLines) {
			if(item.startsWith("email")) {

				String[]parts=item.split(":");
				confEmail = parts[1].trim();
			}
		}
		logger.debug("e-mail obtained from method getEmail(): " + confEmail);
		this.email = confEmail;

		String validation = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		if(this.email.matches(validation)) 
			this.emailValid = true;
		else
			this.emailValid = false;
	}

}
