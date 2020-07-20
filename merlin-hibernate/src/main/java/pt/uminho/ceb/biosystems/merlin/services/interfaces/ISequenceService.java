package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.SequenceContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequence;

public interface ISequenceService {
	
	public boolean checkGeneIDFromSequence(int geneID, SequenceType type) throws Exception;
	
	public List<ModelSequence> getAllModelSequence() throws Exception;
	
	public Map<String, SequenceContainer> getSequencesWithQueriesBySequenceType(SequenceType seqtype) throws Exception;

	public void loadFastaSequences(Map<Integer, String[]> sequences, SequenceType seqType) throws Exception;

	public List<GeneContainer> getSequenceByGeneId(int idGene) throws Exception;

	public boolean checkGenomeSequencesByType(SequenceType type) throws Exception;

	public List<ModelSequence> getAllEntityModelSequences() throws Exception;
	
	public void insertNewSequence(Integer idGene, SequenceType type, String sequence, Integer sequenceLength) throws Exception;

	public Integer countSequencesByType(SequenceType type) throws Exception;

}
