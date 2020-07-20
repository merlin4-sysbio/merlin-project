package pt.uminho.ceb.biosystems.merlin.gui.operations.loaders.annotation.enzymes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava.nbio.genome.parsers.gff.FeatureList;
import org.biojava.nbio.genome.parsers.gff.GFF3Reader;

import es.uvigo.ei.aibench.core.operation.annotation.Cancel;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.core.operation.annotation.Progress;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.datatypes.EntryData;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot.UniProtAPI;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.GFFSource;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.TimeLeftProgress;
import pt.uminho.ceb.biosystems.merlin.services.model.loaders.ModelDatabaseLoadingServices;

/**
 * @author Antonio Dias
 *
 */
@Operation(name="Load GFF3",description="Load GFF3 information")
public class LoadAnnotationGFF {
	
	private WorkspaceAIB project;
//	private String filename;
	private GFFSource sourceOption;
	private Boolean ecOption;
	private ArrayList<String[]> genes;
	private StringBuffer gffBuffer;
	private TimeLeftProgress progress = new TimeLeftProgress();
	private long startTime;
	private AtomicInteger datum;
	private AtomicBoolean cancel = new AtomicBoolean(false);

	/**
	 * @param file
	 */
	@Port(direction=Direction.INPUT, name="File", description = "Select File", validateMethod="validateGFF",order=1)
	public void setFileName(File file){
//		this.filename = file.getAbsolutePath();
	}
	
	/**
	 * @param sourceOption
	 */
	@Port(direction=Direction.INPUT, name="Source", description = "Select File Origin",order=2)
	public void setSource(GFFSource sourceOption){
		this.sourceOption = sourceOption;
	}
	
	/**
	 * @param ecOption
	 */
	@Port(direction=Direction.INPUT, name="EC option", description = "Only load genes with available EC number", defaultValue="false",order=3)
	public void setECoption(boolean ecOption) {
		this.ecOption = ecOption;
	}
	
	/**
	 * @param project
	 * @throws Exception
	 */
	@Port(direction=Direction.INPUT, name="Workspace", description = "Select Workspace",order=4)
	public void setProject(WorkspaceAIB project) throws Exception{
		this.project = project;
		
		this.loadGene();
		List<EntryData> entries = this.readFiles();
		
		this.startTime = System.currentTimeMillis();
		this.datum = new AtomicInteger(0);
		this.saveGeneToDB(entries);
	}
	
	/**
	 * @throws IOException
	 */
	public void loadGene() throws IOException
	{
		genes = new ArrayList<String[]>();
		
		File file = new File("gff");
		BufferedWriter bwr = new BufferedWriter(new FileWriter(file));
        bwr.write(this.gffBuffer.toString());
        bwr.flush();
        bwr.close();

		FeatureList features = GFF3Reader.read(file.getAbsolutePath());

		file.delete();
		
		for (int i = 0; i < features.size(); i++)
		{	
			String[] gene = new String[7];
			gene[0] = features.get(i).seqname();
			gene[1] = String.valueOf(features.get(i).location().getBegin());
			gene[2] = String.valueOf(features.get(i).location().getEnd());
			gene[3] = String.valueOf(features.get(i).location().bioStrand()); //direction

			gene[4] = features.get(i).getAttributes().toString();
			gene[5] = features.get(i).getAttribute("locus_tag");
			gene[6] = features.get(i).getAttribute("protein_id");

			genes.add(gene);
		}	
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public List<EntryData> readFiles() throws Exception // ReadFiles will read the list from Loadgenes and obtain UniProt's or NCBI's information 
																		   // about the genes. It will put that information into a list
	{
		EntryData entrydata = null;
		List<EntryData> entries = new ArrayList<EntryData>();
		String accession;

		for (int i = 0; i < genes.size(); i++)
		{	
			accession = genes.get(i)[0];
			if (sourceOption.equals(GFFSource.UniProt))
			{
				entrydata = UniProtAPI.getEntryDataFromAccession(accession);
			}
//			else if (sourceOption.equals(GFFSource.NCBI))
//			{
//				entrydata = NcbiAPI.getEntryDataFromAccession(accession);
//			}
			else if (sourceOption.equals(GFFSource.Other))
			{
			//look for ec numbers and fill entrydata
				entrydata = fillEntryData(i);
			}

			if (entrydata!=null)
			{
				entries.add(entrydata);
			}
		}
		return entries;
	}
	
	/**
	 * @param entries
	 * @throws Exception
	 */
	public void saveGeneToDB(List<EntryData> entries) throws Exception {
		
		this.cancel = new AtomicBoolean(false);
		
		String temp = "";
		EntryData entrydata;

		if (ecOption && sourceOption.equals(GFFSource.Other)){
			for (int i = 0; i < entries.size(); i++)
			{
				if(!this.cancel.get()) {

					entrydata = entries.get(i);
					if (! entrydata.getEcNumbers().isEmpty())
					{
						ModelDatabaseLoadingServices.loadGeneAnnotation(project.getName(), entrydata.getLocusTag(), genes.get(i)[6], temp, genes.get(i)[3]
								, genes.get(i)[1], genes.get(i)[2], entrydata.getEcNumbers(), temp, project, SourceType.GFF);
					}

					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), entries.size(), "Loading Genes");
				}
			}
		}
		else {
			for (int i = 0; i < entries.size(); i++)
			{
				if(!this.cancel.get()) {

					entrydata = entries.get(i);
					ModelDatabaseLoadingServices.loadGeneAnnotation(project.getName(), entrydata.getLocusTag(), genes.get(i)[6], temp,  genes.get(i)[3], genes.get(i)[1], genes.get(i)[2], 
							entrydata.getEcNumbers(), temp, project, SourceType.MANUAL);

					this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-this.startTime), this.datum.incrementAndGet(), entries.size(), "Loading Genes");
				}
			}

		}
		
		if (!cancel.get())
			Workbench.getInstance().info("GFF successfully loaded.");
		else
			Workbench.getInstance().warn("GFF load cancelled!");
		
	}
	
	/**
	 * @param i
	 * @return
	 */
	public EntryData fillEntryData(int i)
	
	{
		EntryData entrydata = new EntryData(genes.get(i)[0]);
		
		
		String attributes = genes.get(i)[4];
		String pattern = "(\\d)(\\.)(\\d|-)(\\.)(\\d|-)(.\\d|-)*";
		
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(attributes);
		
		entrydata.setLocusTag(genes.get(i)[5]);
		
		if (m.find())
		{
			m.reset();
			while (m.find()) {
				entrydata.addEcNumber(m.group());
			}
		}
		return entrydata;
	}
	
	/**
	 * @param file
	 * @throws IOException
	 */
	public void validateGFF(File file) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));
		
		PrintWriter writer = new PrintWriter("gff_error_report.txt", "UTF-8");
		writer.println("The following lines do not follow GFF version 3 standards:");
		
		String s;
		int counter = 0;
		boolean errorfound = false;
		int nrErrors = 0;
		
		this.gffBuffer = new StringBuffer();
		
		for (s = br.readLine(); null != s; s = br.readLine()) {
			counter++;
			List<String> columns = new ArrayList<String>(Arrays.asList(s.split("\t")));
			if (columns.size() == 9) {
				this.gffBuffer.append(s);
				this.gffBuffer.append("\n");
			}
			else {
				errorfound = true;
				writer.println(counter);
				nrErrors++;
			}
		}

		writer.close();
		br.close();
		
		if(errorfound) {
			
			if(nrErrors/(double) counter < 0.2) {
				Workbench.getInstance().warn("Some lines contain errors and where ignored. For more information check error report file available in merlin's directory.");
			}
			else {
				Workbench.getInstance().error("File could not be loaded. For more information check error report file available in merlin's directory.");
			}
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
	public void cancel() {
		
		String[] options = new String[2];
		options[0] = "yes";
		options[1] = "no";
		
		int result = CustomGUI.stopQuestion("Cancel confirmation", "Are you sure you want to cancel the operation?", options);
		
		if(result == 0) {

			this.progress.setTime((GregorianCalendar.getInstance().getTimeInMillis()-GregorianCalendar.getInstance().getTimeInMillis()),1,1);
			
			this.cancel.set(true);
			Workbench.getInstance().warn("Please hold on. Your operation is being cancelled.");
	
			
			
		}
		
	}

}