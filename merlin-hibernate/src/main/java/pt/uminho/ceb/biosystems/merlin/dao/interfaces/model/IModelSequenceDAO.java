package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequence;


public interface IModelSequenceDAO extends IGenericDao<ModelSequence>{
	
	public void addModelSequence(ModelSequence modelSequence); 
	
	public void addModelSequenceList(List<ModelSequence> modelSequenceList); 
	
	public List<ModelSequence> getAllModelSequence(); 
	
	public ModelSequence getModelSequence(Integer id); 
	
	public void removeModelSequence(ModelSequence modelSequence); 
	
	public void removeModelSequenceList(List<ModelSequence> modelSequenceList); 
	
	public void updateModelSequenceList(List<ModelSequence> modelSequenceList); 
	
	public void updateModelSequence(ModelSequence modelSequence);

	public List<Integer> getModelSequenceGeneIdByGeneIdAndSequenceType(int geneID, SequenceType seqtype);

	public List<ModelSequence> getModelSequencesBySequenceType(SequenceType seqtype);

	public List<GeneContainer> getSequenceByGeneId(int idGene);

	public boolean checkGenomeSequencesByType(SequenceType type);

	public Integer countSequencesByType(SequenceType type);

}
