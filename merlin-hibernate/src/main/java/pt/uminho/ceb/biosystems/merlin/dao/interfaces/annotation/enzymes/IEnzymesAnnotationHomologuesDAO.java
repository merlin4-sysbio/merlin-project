package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologues;



public interface IEnzymesAnnotationHomologuesDAO extends IGenericDao<EnzymesAnnotationHomologues>{
	
	public void addEnzymesAnnotationHomologues(EnzymesAnnotationHomologues enzymesAnnotationHomologues); 
	
	public void addEnzymesAnnotationHomologues(List<EnzymesAnnotationHomologues> enzymesAnnotationHomologuesList); 
	
	public List<EnzymesAnnotationHomologues> getAllEnzymesAnnotationHomologues(); 
	
	public EnzymesAnnotationHomologues getEnzymesAnnotationHomologues(Integer id); 
	
	public void removeEnzymesAnnotationHomologues(EnzymesAnnotationHomologues enzymesAnnotationHomologues); 
	
	public void removeEnzymesAnnotationHomologuesList(List<EnzymesAnnotationHomologues> enzymesAnnotationHomologuesList); 
	
	public void updateEnzymesAnnotationHomologuesList(List<EnzymesAnnotationHomologues> enzymesAnnotationHomologuesList); 
	
	public void updateEnzymesAnnotationHomologues(EnzymesAnnotationHomologues enzymesAnnotationHomologues);

	public Integer getHomologuesSkey(String referenceID);
}
