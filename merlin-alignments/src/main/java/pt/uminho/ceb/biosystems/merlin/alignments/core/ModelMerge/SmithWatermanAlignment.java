package pt.uminho.ceb.biosystems.merlin.alignments.core.ModelMerge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import pt.uminho.ceb.biosystems.merlin.alignments.core.PairwiseSequenceAlignement;
import pt.uminho.ceb.biosystems.merlin.core.containers.alignment.AlignmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.AlignmentPurpose;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.AlignmentScoreType;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Method;

public class SmithWatermanAlignment implements ModelAlignments {
	
	
	private static final long serialVersionUID = 1L;
	private ConcurrentLinkedQueue<AlignmentContainer> alignments;
	private String alignmentMatrix;
	private File queryFasta, subjectFasta;
	private AtomicBoolean cancel;
	
	
	public SmithWatermanAlignment(String queryFasta, String subjectFasta, AtomicBoolean cancel){
		
		this.queryFasta = new File(queryFasta);
		this.subjectFasta = new File(subjectFasta);
		this.cancel = cancel;
		
		run();
	}
	
	@Override
	public void run() {
		
		try {
			AtomicBoolean cancel = new AtomicBoolean(false);
			
			ConcurrentHashMap<String, AbstractSequence<?>> querySequences= new ConcurrentHashMap<String, AbstractSequence<?>>();
			querySequences.putAll(FastaReaderHelper.readFastaProteinSequence(queryFasta));
			
			Map<String, AbstractSequence<?>> subjectSequences= new HashMap<String, AbstractSequence<?>>();
			subjectSequences.putAll(FastaReaderHelper.readFastaProteinSequence(subjectFasta));
			
			double threshold = 1.0;
			
			ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet = new ConcurrentLinkedQueue<>();					
			ConcurrentLinkedQueue<String> queryArray = new ConcurrentLinkedQueue<>(querySequences.keySet());
			
			ConcurrentLinkedQueue<String> sequencesWithoutSimilarities = new ConcurrentLinkedQueue<>();
			
			Runnable lc	= new PairwiseSequenceAlignement(Method.SmithWaterman, querySequences, subjectSequences, queryArray,
					threshold, new AtomicInteger(0), cancel, AlignmentPurpose.OTHER, AlignmentScoreType.ALIGNMENT, 
					alignmentContainerSet);
			
			((PairwiseSequenceAlignement) lc).setSequencesWithoutSimilarities(sequencesWithoutSimilarities);
			
//		((PairwiseSequenceAlignement) lc).setEc_number(this.ec_number);
//		((PairwiseSequenceAlignement) lc).setClosestOrthologs(this.closestOrthologs);
//		((PairwiseSequenceAlignement) lc).setReferenceTaxonomyScore(this.referenceTaxonomyScore);
//		((PairwiseSequenceAlignement) lc).setKegg_taxonomy_scores(this.kegg_taxonomy_scores);
//		((PairwiseSequenceAlignement) lc).setReferenceTaxonomyThreshold(this.referenceTaxonomyThreshold);
//		((PairwiseSequenceAlignement) lc).setSequenceIdsSet(sequenceIdsSet);
			
			
			lc.run();
			
			this.alignments = alignmentContainerSet;
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		RunSimilaritySearch search = new RunSimilaritySearch(this.genome, this.similarity_threshold, 
//				Method.SmithWaterman, orthologs, this.cancel, 
//				new AtomicInteger(0), new AtomicInteger(0), AlignmentScoreType.ALIGNMENT);
//
//		search.setEc_number(ec_number);
//
//		if(!gapsIdentification)								
//			search.setModules(genes_ko_modules);	
//
//		search.setClosestOrthologs(MapUtils.revertMapFromSet(this.closestOrtholog));
//		search.setReferenceTaxonomyScore(referenceTaxonomy.size());
//		search.setKegg_taxonomy_scores(kegg_taxonomy_scores);
//		search.setAnnotatedGenes(this.ecNumbers.get(ec_number));
//		search.setReferenceTaxonomyThreshold(this.referenceTaxonomyThreshold);
//		search.setCompareToFullGenome(this.compareToFullGenome);
//
//		this.alignments = search.run_OrthologsSearch(sequenceIdsSet, alignmentContainerSet);
				
	}
	
	@Override
	public void buildAlignmentCapsules() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see pt.uminho.ceb.biosystems.merlin.local.alignments.core.ModelMerge.ModelAlignments#getAlignmentsCapsules()
	 */
	public ConcurrentLinkedQueue<AlignmentContainer> getAlignmentsCapsules() {
		return this.alignments;
	}
	
	/**
	 * @return
	 */
	public Map<String,List<AlignmentContainer>> getAlignmentsByQuery(){
		
		Map<String,List<AlignmentContainer>> alignmentMap = new HashMap<>();
		
		for(AlignmentContainer alignContainer : this.alignments){
			
			String query = alignContainer.getQuery();
			
			if(alignmentMap.containsKey(query)){
				alignmentMap.get(query).add(alignContainer);
			}
			else{
				List<AlignmentContainer> containersList = new ArrayList<>();
				containersList.add(alignContainer);
				alignmentMap.put(query, containersList);
			}
		}
		
		return alignmentMap;
	}
	
	/**
	 * @param cancel
	 */
	public void setCancel(AtomicBoolean cancel){
		this.cancel = cancel;
	}

//	@Override
//	public void addObserver(Observable runSimilaritySearch) {
//		// TODO Auto-generated method stub
//		
//	}
	
}
