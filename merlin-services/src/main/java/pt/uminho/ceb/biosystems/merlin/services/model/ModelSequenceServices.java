package pt.uminho.ceb.biosystems.merlin.services.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.RNASequence;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.SequenceContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.dataAccess.InitDataAccess;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequence;

public class ModelSequenceServices {

	public static Map<String, AbstractSequence<?>> getGenomeFromDatabase(String databaseName, SequenceType seqType) throws Exception{

		Map<String, AbstractSequence<?>> genomeSequences = new HashMap<>();

		Map<String, SequenceContainer> modelSequence = InitDataAccess.getInstance().getDatabaseService(databaseName).getSequencesWithQueriesBySequenceType(seqType);
		
		AbstractSequence<?> sequence;

		//protein.faa
		if(seqType.equals(SequenceType.PROTEIN)){

			for(Entry<String, SequenceContainer> entry : modelSequence.entrySet()) {

				
				sequence = new ProteinSequence(entry.getValue().getSequence());
				sequence.setSource(entry.getValue().getId()+"");
				sequence.setOriginalHeader(entry.getValue().getQuery());
				
//				sequence = new ProteinSequence(entry.getKey());
//				sequence.setSource(entry.getValue().getId()+"");
				
//				sequence.setOriginalHeader(entry.getValue().getQuery());

				genomeSequences.put(entry.getValue().getQuery(), sequence);
			}
		}

		//cds_from_genomic.faa
		else if(seqType.equals(SequenceType.CDS_DNA)){

			for(Entry<String, SequenceContainer> entry : modelSequence.entrySet()) {

				sequence = new DNASequence(entry.getValue().getSequence());
				sequence.setOriginalHeader(entry.getValue().getQuery());
				sequence.setSource(entry.getValue().getId()+"");

				genomeSequences.put(entry.getValue().getQuery(),sequence);
			}
		}

		//rna_from_genomic.fna and genomic.fna
		else if(seqType.equals(SequenceType.GENOMIC_DNA)){

			for(Entry<String, SequenceContainer> entry : modelSequence.entrySet()) {

				String key = seqType.toString().concat("_").concat(entry.getValue().getQuery());

				sequence = new DNASequence(entry.getValue().getSequence());
				sequence.setSource(entry.getValue().getId()+"");

				genomeSequences.put(key, sequence);

			}
		} 

		else {

			for(Entry<String, SequenceContainer> entry : modelSequence.entrySet()) {
				
				String key = seqType.toString().concat("_").concat(entry.getValue().getId()+"");

				sequence = new RNASequence(entry.getValue().getSequence());
				sequence.setSource(entry.getValue().getId()+"");

				genomeSequences.put(key, sequence);
			}
		}
		return genomeSequences;
	}
	
	
	
	// this is an identical method to the one above except that it uses gene database IDs instead of gene queries
	public static Map<String, AbstractSequence<?>> getGenomeFromDatabaseDbIdsOnly(String databaseName, SequenceType seqType) throws Exception{

		Map<String, AbstractSequence<?>> genomeSequences = new HashMap<>();

		List<SequenceContainer> sequences = InitDataAccess.getInstance().getDatabaseService(databaseName).getSequencesbySequenceType(seqType);
		
		AbstractSequence<?> sequence;

		//protein.faa
		if(seqType.equals(SequenceType.PROTEIN)){

			for(SequenceContainer entry :sequences) {

				sequence = new ProteinSequence(entry.getSequence());
				sequence.setSource(entry.getId()+"");
				sequence.setOriginalHeader(entry.getGeneID()+"");
				genomeSequences.put(entry.getGeneID()+"", sequence);
			}
		}

		//cds_from_genomic.faa
		else if(seqType.equals(SequenceType.CDS_DNA)){

			for(SequenceContainer entry :sequences) {

				sequence = new DNASequence(entry.getSequence());
				sequence.setOriginalHeader(entry.getGeneID()+"");
				sequence.setSource(entry.getId()+"");

				genomeSequences.put(entry.getGeneID()+"",sequence);
			}
		}

		//rna_from_genomic.fna and genomic.fna
		else if(seqType.equals(SequenceType.GENOMIC_DNA)){

			for(SequenceContainer entry :sequences) {

				String key = seqType.toString().concat("_").concat(entry.getGeneID()+"");

				sequence = new DNASequence(entry.getSequence());
				sequence.setSource(entry.getId()+"");

				genomeSequences.put(key, sequence);

			}
		} 

		else {

			for(SequenceContainer entry :sequences) {
				
				String key = seqType.toString().concat("_").concat(entry.getId()+"");

				sequence = new RNASequence(entry.getSequence());
				sequence.setSource(entry.getId()+"");

				genomeSequences.put(key, sequence);
			}
		}
		return genomeSequences;
	}
	
	
	/**
	 * @param sequences
	 * @param pStmt
	 * @throws Exception 
	 * @throws  
	 */
	public static void loadFastaSequences(String databaseName, Map<Integer, String[]> sequences,
			SequenceType seqType) throws Exception{

		InitDataAccess.getInstance().getDatabaseService(databaseName).loadFastaSequences(sequences, seqType);
		
	}
	
	/**
	 * @param databaseName
	 * @param idGene
	 * @param type
	 * @param sequence
	 * @param sequenceLength
	 * @throws IOException
	 * @throws Exception
	 */
	public static void insertNewSequence(String databaseName, Integer idGene, SequenceType type, String sequence, Integer sequenceLength) throws IOException, Exception {
		
		InitDataAccess.getInstance().getDatabaseService(databaseName).insertNewSequence(idGene, type, sequence, sequenceLength);
	}
	
	/**
	 * @param databaseName
	 * @param idGene
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static boolean checkGeneIDFromSequence(String databaseName, Integer idGene, SequenceType type) throws IOException, Exception {
		
		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkGeneIDFromSequence(idGene, type);
	}	
	
	/**
	 * @param databaseName
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static boolean checkGenomeSequences(String databaseName, SequenceType type) throws Exception {
		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkGenomeSequencesByType(type);
	}
	
	/**
	 * @param databaseName
	 * @param sequenceType
	 * @return
	 * @throws Exception
	 */
	public static Boolean checkModelSequenceType(String databaseName, String sequenceType) throws Exception {
		return InitDataAccess.getInstance().getDatabaseService(databaseName).checkModelSequenceType(sequenceType);		
	}
	
	/**
	 * @param databaseName
	 * @param sequenceType
	 * @return
	 * @throws Exception
	 */
	public static Integer countSequencesByType(String databaseName, SequenceType sequenceType) throws Exception {
		return InitDataAccess.getInstance().getDatabaseService(databaseName).countSequencesByType(sequenceType);		
	}
}
