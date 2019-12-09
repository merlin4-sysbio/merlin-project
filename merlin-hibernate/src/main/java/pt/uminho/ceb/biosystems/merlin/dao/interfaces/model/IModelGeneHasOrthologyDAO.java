package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasOrthologyId;

public interface IModelGeneHasOrthologyDAO extends IGenericDao<ModelGeneHasOrthology>{

	public void addModelGeneHasOrthology(ModelGeneHasOrthology modelGeneHasOrthology); 
	
	public void addModelGeneHasOrthologyList(List<ModelGeneHasOrthology> modelGeneHasOrthologyList); 
	
	public List<ModelGeneHasOrthology> getAllModelGeneHasOrthology(); 
	
	public ModelGeneHasOrthology getModelGeneHasOrthology(Integer id); 
	
	public void removeModelGeneHasOrthology(ModelGeneHasOrthology modelGeneHasOrthology); 
	
	public void removeModelGeneHasOrthologyList(List<ModelGeneHasOrthology> modelGeneHasOrthologyList); 
	
	public void updateModelGeneHasOrthologyList(List<ModelGeneHasOrthology> modelGeneHasOrthologyList); 
	
	public void updateModelGeneHasOrthology(ModelGeneHasOrthology modelGeneHasOrthology);

	public List<String[]> getModelGeneHasOrthologyAttributesByGeneId(Integer id);

	public List<ModelGeneHasOrthology> getAllModelGeneHasOrthologyByGeneIdAndOrthologyId(Integer geneId, Integer orthId);

	ModelGeneHasOrthologyId insertModelGeneHasOrthologyGeneIdAndOrthologyIdAndSimilarity(int gene_id, int orth_id,
			Float similarity);
}