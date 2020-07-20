package pt.uminho.ceb.biosystems.merlin.alignments.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper;
import org.biojava.nbio.core.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import pt.uminho.ceb.biosystems.merlin.core.containers.alignment.AlignmentContainer;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * @author amaromorais
 *
 */
public class AlignmentsUtils {
	
	
	/**
	 * @param sequence
	 * @return
	 */
	public static double getSequenceAlignmentMaxScore(AbstractSequence<AminoAcidCompound> sequence, String matrixName){
		
		double maxScore = 0;
		
		SubstitutionMatrix<AminoAcidCompound> matrix = SubstitutionMatrixHelper.getAminoAcidSubstitutionMatrix(matrixName.toLowerCase());
		
		List<AminoAcidCompound> sequenceCompounds = sequence.getAsList();
		
		for(AminoAcidCompound compound : sequenceCompounds){
			
			maxScore += (double) matrix.getValue(compound, compound);
		}
		
		return maxScore;
	}
	
	
	/**
	 * @param sequences
	 * @return
	 */
	public static Map<String,Double> getSequencesAlignmentMaxScoreMap(Map<String,AbstractSequence<?>> sequences, String matrixName){
		
		Map<String,Double> maxScores = new HashMap<>();
		
		for(String sequenceID : sequences.keySet()){
			
			@SuppressWarnings("unchecked")
			AbstractSequence<AminoAcidCompound> sequence = (AbstractSequence<AminoAcidCompound>) sequences.get(sequenceID);
			
			double sequenceMaxScore = getSequenceAlignmentMaxScore(sequence,matrixName);
			
			maxScores.put(sequenceID, sequenceMaxScore);
		}
		
		return maxScores;
		
	}
	
	
	/**
	 * Method to verify if Blast+ is installed
	 * 
	 * @return
	 */
	public static boolean checkBlastInstalation() {
		
		try {
			
			Process p = Runtime.getRuntime().exec("blastp -version");
			p.waitFor();
			
			return true;
			
		} 
		catch (IOException e) {

			if(!e.getMessage().contains("The system cannot find the file specified"))				
				e.printStackTrace();
			return false;
		}
		catch (InterruptedException e) {
			
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * @param alignmentContainerSet
	 * @return
	 */
	public static Map<String,List<AlignmentContainer>> getAlignmentsByQuery(ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet){

		Map<String,List<AlignmentContainer>> alignmentMap = new HashMap<>();

		for(AlignmentContainer alignContainer : alignmentContainerSet){

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
	 * return a Map<String,String> where the keys are queryGenes sequence Ids and the values are targetGenes sequence ids
	 * 
	 * @param alignmentContainerSet
	 * @return
	 */
	public static Map<String,Set<String>> getOrthologsGenesMap(ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet){
		
		Map<String,List<AlignmentContainer>> alignmentsMap = getAlignmentsByQuery(alignmentContainerSet);
		
		Map<String,Set<String>> orthologsGenesMap = new HashMap<>();
		
		for(String queryID : alignmentsMap.keySet()){
			
			List<AlignmentContainer> containers = alignmentsMap.get(queryID);
			
			Set<String> orthologs = new HashSet<>();
			
			for(AlignmentContainer container : containers)				
				orthologs.add(container.getTarget());
			
			orthologsGenesMap.put(queryID, orthologs);
		}
				
		return orthologsGenesMap;
		
	}
	
	
	/**
	 * return a Map<String,String> where the keys are queryGenes sequence Ids and the values are targetGenes sequence ids
	 * 
	 * @param alignmentContainerSet
	 * @return
	 */
	public static Map<String,String> getBestOrthologsGenesMap(ConcurrentLinkedQueue<AlignmentContainer> alignmentContainerSet){
		
		Map<String,List<AlignmentContainer>> alignmentsMap = getAlignmentsByQuery(alignmentContainerSet);
		
		Map<String,String> orthologsGenesMap = new HashMap<>();
		
		for(String queryID : alignmentsMap.keySet()){
			
			List<AlignmentContainer> containers = alignmentsMap.get(queryID);
			
			double bestBitScore = -1;
			
			for(AlignmentContainer container : containers){
				
				if(!orthologsGenesMap.containsKey(queryID)){
					
					orthologsGenesMap.put(queryID, container.getTarget());
					bestBitScore = container.getBitScore();
				}
				else if(container.getBitScore()>bestBitScore){
						
					orthologsGenesMap.replace(queryID, container.getTarget());
					bestBitScore = container.getBitScore();
				}
			}
		}
				
		return orthologsGenesMap;
		
	}
	
	
	
	/**
	 * This method allows to run the BLAST+ from merlin, if it is installed.
	 * outputFormat parameter comprises a integer in the range of 0 to 11.
	 * See NCBI's BLAST+ documentation for detailed information.
	 * 
	 * @param queryFilePath
	 * @param subjectFilePath
	 * @param outputFilePath
	 * @param outputFormat
	 */
	public static boolean runBlast(String queryFilePath, String subjectFilePath, String outputFilePath, Integer outputFormat){
		
		String outfmt = " -outfmt ";
		if(outputFormat==null)
			outfmt = "";
		else
			outfmt = outfmt.concat(Integer.toString(outputFormat));
		
		boolean hasBlastCompleted = false;
		
		try {

			Process	blastProcess = Runtime.getRuntime().exec("blastp -query " + queryFilePath + " -subject " 
					+ subjectFilePath + " -out " + outputFilePath + outfmt);

			int exitValue = blastProcess.waitFor();

			if (exitValue != 0) {
				System.err.println("Abnormal BLAST process termination");
			}
			else{
				System.out.println("BLAST search completed with success!");
				hasBlastCompleted = true;
			}

			blastProcess.destroy();
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hasBlastCompleted;
	}
	
	
	/**
	 * @param bbHits
	 * @return
	 */
	public static Map<String, Set<String>> processBidirectionalBestHits(Pair<ConcurrentLinkedQueue<AlignmentContainer>,ConcurrentLinkedQueue<AlignmentContainer>> bbHits){
		
		Map<String, Set<String>> queryGenomeOrthologsMap = AlignmentsUtils.getOrthologsGenesMap(bbHits.getA());
		Map<String, Set<String>> subjectGenomeOrthologsMap = AlignmentsUtils.getOrthologsGenesMap(bbHits.getB());
		
		return intersectBidirectionalBestHitsMaps(queryGenomeOrthologsMap,subjectGenomeOrthologsMap);
	}

	
	/**
	 * @param genesHomologousMap1
	 * @param genesHomologousMap2
	 * @return
	 */
	public static Map<String, Set<String>> intersectBidirectionalBestHitsMaps(Map<String,Set<String>> genesHomologousMap1, Map<String,Set<String>> genesHomologousMap2){
		
		Map<String, Set<String>> genesHomologousMapFiltered = new TreeMap<>();

		for(String queryGene : genesHomologousMap1.keySet()){

			Set<String> homologousGenes = genesHomologousMap1.get(queryGene);

			if(homologousGenes!=null && !homologousGenes.isEmpty()){

				for(String homolog : homologousGenes){

					if(genesHomologousMap2.containsKey(homolog)){

						if(genesHomologousMap2.get(homolog).contains(queryGene)){

							if(genesHomologousMapFiltered.containsKey(queryGene)){
								genesHomologousMapFiltered.get(queryGene).add(homolog);
							}
							else{
								Set<String> homologousSet = new HashSet<>();
								homologousSet.add(homolog);
								genesHomologousMapFiltered.put(queryGene, homologousSet);
							}
						}
					}

				}
			}
		}
		
		return genesHomologousMapFiltered;
	}
	
	
	/**
	 * @param target
	 * @param listOfMatches
	 * @return
	 */
	public static AlignmentContainer getAlignmentContainer(String target, List<AlignmentContainer> listOfMatches){
		
		for(AlignmentContainer alignment : listOfMatches)
			if(alignment.getTarget().equals(target))
				return alignment;
		
		return null;
	}
	
}
