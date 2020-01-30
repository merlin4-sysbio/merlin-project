package pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.blast;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesHomologuesData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.BlastProgram;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.BlastSource;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.HomologySearchServer;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.remote.RemoteDataRetriever;
import pt.uminho.ceb.biosystems.merlin.utilities.blast.ebi_blastparser.EbiBlastParser;

public class BlastReportsLoader implements Runnable {

	final static Logger logger = LoggerFactory.getLogger(BlastReportsLoader.class);
	
	private PropertyChangeSupport changes;
	
	private ConcurrentLinkedQueue<String> existingGenes;
	private ConcurrentLinkedQueue<AnnotationEnzymesHomologuesData> resultsList;
	
	private String[] organismTaxa;
	private Map<String, AbstractSequence<?>> sequences;
	
	private BlastProgram blastProgram;
	private BlastSource blastSource;

	private ConcurrentLinkedQueue<File> outFiles;
	private ConcurrentHashMap<String, String[]> taxonomyMap;
	private ConcurrentHashMap<String, Boolean> uniprotStar;

	private AtomicBoolean cancel;
	private AtomicInteger errorCounter, sequencesCounter;

	private Long taxonomyIdentifier;

	private String extension;

	/**
	 * Constructor for concurrent Blast reporst loader. 
	 * 
	 * @param extension
	 * @param existingGenes
	 * @param organismTaxa
	 * @param taxonomyIdentifier
	 * @param blastProgram
	 * @param blastSource
	 * @param outFiles
	 * @param sequences
	 * @param taxonomyMap
	 * @param uniprotStar
	 * @param startTime
	 * @param cancel
	 * @param errorCounter
	 * @param counter
	 * @param progress
	 * @param resultsList
	 * @param connection
	 */
	public BlastReportsLoader(String extension, ConcurrentLinkedQueue<String> existingGenes, String[] organismTaxa, Long taxonomyIdentifier,
			BlastProgram blastProgram, BlastSource blastSource, ConcurrentLinkedQueue<File> outFiles,
			Map<String, AbstractSequence<?>> sequences, ConcurrentHashMap<String, String[]> taxonomyMap,
			ConcurrentHashMap<String, Boolean> uniprotStar, AtomicBoolean cancel, 
			AtomicInteger errorCounter, AtomicInteger counter, ConcurrentLinkedQueue<AnnotationEnzymesHomologuesData> resultsList) {
		super();
		this.extension = extension;
		this.existingGenes = existingGenes;
		this.organismTaxa = organismTaxa;
		this.blastProgram = blastProgram;
		this.blastSource = blastSource;
		this.outFiles = outFiles;
		this.sequences = sequences;
		this.taxonomyMap = taxonomyMap;
		this.uniprotStar = uniprotStar;
		this.cancel = cancel;
		this.errorCounter = errorCounter;
		this.sequencesCounter = counter;
		this.taxonomyIdentifier = taxonomyIdentifier;
		this.resultsList = resultsList;
		this.changes = new PropertyChangeSupport(this);
	}

	@Override
	public void run() {

		while(outFiles.size()>0) {

			File outFile = outFiles.poll();
			try {

				if(outFile.getName().endsWith(extension))
					this.runner(outFile);
			} 
			catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	private void runner(File outFile) throws Exception {

		CheckBlastResult cbr = new CheckBlastResult(new FileInputStream(outFile.getAbsolutePath()));
		EbiBlastParser ebiBlastParser = new EbiBlastParser(new FileInputStream(outFile.getAbsolutePath()));

		boolean go = cbr.isBlastResultOK();
		String query = cbr.getQuery().split(" ")[0];
		RemoteDataRetriever homologyDataClient;
		
		
		logger.debug("Processing "+query);

		if(go) {

			if(cbr.isSimilarityFound()) {
				
				if(!ebiBlastParser.isReprocessQuery()) {

					if(!existingGenes.contains(ebiBlastParser.getResults().get(0).getQueryID())) {

						if(!this.cancel.get()) {

							HomologySearchServer hss = HomologySearchServer.EBI;

							homologyDataClient = new RemoteDataRetriever(ebiBlastParser, this.organismTaxa, this.taxonomyMap, this.uniprotStar, 
									this.cancel, false, hss, this.blastSource, taxonomyIdentifier);

							if(sequences.containsKey(query))
								homologyDataClient.setFastaSequence(sequences.get(query).getSequenceAsString());

							if(homologyDataClient.isDataRetrieved() && !existingGenes.contains(ebiBlastParser.getResults().get(0).getQueryID())) {

								homologyDataClient.setDatabaseIdentifier(ebiBlastParser.getResults().get(0).getSeqDb());
								resultsList.add(homologyDataClient.getHomologuesData());
								changes.firePropertyChange("resultsList", sequencesCounter.get(), sequencesCounter.incrementAndGet());
								if(this.resultsList.size()>25)
									changes.firePropertyChange("saveToDatabase", null, this.resultsList.size());
								
								logger.debug("Gene\t{}\tprocessed..", homologyDataClient.getLocusTag());
							}
							else {

								if(!this.cancel.get())		
									logger.debug("Reprocessing");
							}
						}
					}

					else {

						logger.debug("Gene {} already processed.", ebiBlastParser.getResults().get(0).getQueryID());
					}
				}
				else {

					errorCounter.incrementAndGet();
					logger.error("Error processing "+cbr.getQuery());
				}
			}
			else {

				if(!this.cancel.get()) {

					HomologySearchServer hss = HomologySearchServer.EBI;

					homologyDataClient = new RemoteDataRetriever(query, this.blastProgram.toString(), this.cancel, blastSource.equals(BlastSource.EBI), hss, taxonomyIdentifier);
					homologyDataClient.setFastaSequence(sequences.get(query).getSequenceAsString());
					homologyDataClient.setDatabaseIdentifier(cbr.getDatabase());
					resultsList.add(homologyDataClient.getHomologuesData());
					changes.firePropertyChange("resultsList", sequencesCounter.get(), sequencesCounter.incrementAndGet());
					if(this.resultsList.size()>25)
						changes.firePropertyChange("saveToDatabase", null, this.resultsList.size());
				}
			}
			cbr.getInputSource().getCharacterStream().close();
			existingGenes.add(query);
		}
		else {

			errorCounter.incrementAndGet();
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}
	
}