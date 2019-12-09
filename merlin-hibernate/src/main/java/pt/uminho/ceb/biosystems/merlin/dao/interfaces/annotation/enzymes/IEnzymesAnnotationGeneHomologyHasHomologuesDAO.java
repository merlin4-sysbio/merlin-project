package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomologyHasHomologues;



public interface IEnzymesAnnotationGeneHomologyHasHomologuesDAO extends IGenericDao<EnzymesAnnotationGeneHomologyHasHomologues>{

	public void addEnzymesAnnotationGeneHomologyHasHomologues(EnzymesAnnotationGeneHomologyHasHomologues EnzymesAnnotationGeneHomologyHasHomologues); 
	
	public void addEnzymesAnnotationGeneHomologyHasHomologuesList(List<EnzymesAnnotationGeneHomologyHasHomologues> EnzymesAnnotationGeneHomologyHasHomologuesList); 
	
	public List<EnzymesAnnotationGeneHomologyHasHomologues> getAllEnzymesAnnotationGeneHomologyHasHomologues(); 
	
	public EnzymesAnnotationGeneHomologyHasHomologues getEnzymesAnnotationGeneHomologyHasHomologues(Integer id); 
	
	public void removeEnzymesAnnotationGeneHomologyHasHomologues(EnzymesAnnotationGeneHomologyHasHomologues EnzymesAnnotationGeneHomologyHasHomologues); 
	
	public void removeEnzymesAnnotationGeneHomologyHasHomologuesList(List<EnzymesAnnotationGeneHomologyHasHomologues> EnzymesAnnotationGeneHomologyHasHomologuesList); 
	
	public void updateEnzymesAnnotationGeneHomologyHasHomologuesList(List<EnzymesAnnotationGeneHomologyHasHomologues> EnzymesAnnotationGeneHomologyHasHomologuesList); 
	
	public void updateEnzymesAnnotationGeneHomologyHasHomologues(EnzymesAnnotationGeneHomologyHasHomologues EnzymesAnnotationGeneHomologyHasHomologues);

	public String getEnzymesAnnotationGeneHomologyHasHomologuesQueryByLocusTag(String locus);

	public Map<Integer, Long> getEnzymesAnnotationGeneHomologyHasHomologuesAttributes();

	public Map<Integer, Long> getEnzymesAnnotationGeneHomologyHasHomologuesAttributes2();
	
	public void insertEnzymesAnnotationGeneHomologyHasHomologues(Integer geneHomology_s_key, String referenceID, String gene, Float eValue, Float bits, Integer homologues_s_key);
	
	public List<EnzymesAnnotationGeneHomologyHasHomologues> getAllEnzymesAnnotationGeneHomologyHasHomologuesByAttributes(Integer homologyskey, Integer homologuesSkey, String referenceId, Float evalue, Float bits,String gene);
}

