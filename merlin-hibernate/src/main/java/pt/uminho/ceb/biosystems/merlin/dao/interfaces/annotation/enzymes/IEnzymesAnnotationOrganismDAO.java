package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationOrganism;


public interface IEnzymesAnnotationOrganismDAO extends IGenericDao<EnzymesAnnotationOrganism>{

	public void addEnzymesAnnotationOrganism(EnzymesAnnotationOrganism enzymesAnnotationOrganism); 
	
	public void addEnzymesAnnotationOrganismList(List<EnzymesAnnotationOrganism> enzymesAnnotationOrganismList); 
	
	public List<EnzymesAnnotationOrganism> getAllEnzymesAnnotationOrganism(); 
	
	public EnzymesAnnotationOrganism getEnzymesAnnotationOrganism(Integer id); 
	
	public void removeEnzymesAnnotationOrganism(EnzymesAnnotationOrganism enzymesAnnotationOrganism); 
	
	public void removeEnzymesAnnotationOrganismList(List<EnzymesAnnotationOrganism> enzymesAnnotationOrganismList); 
	
	public void updateEnzymesAnnotationOrganismList(List<EnzymesAnnotationOrganism> enzymesAnnotationOrganismList); 
	
	public void updateEnzymesAnnotationOrganism(EnzymesAnnotationOrganism enzymesAnnotationOrganism);

	public Integer getEnzymesAnnotationOrganismMaxTaxRank();

	public List<EnzymesAnnotationOrganism> getAllEnzymesAnnotationOrganismByOrganism(String organism);

	public List<String> getEnzymesAnnotationOrganismDistinctOrganism();

	public List<String> getEnzymesAnnotationOrganismTaxonomy();

	Long getEnzymesAnnotationOrganismCountDistinctOrganism();

	public Integer insertEnzymesAnnotationOrganism(String organism, String taxonomy, Integer taxrank);
}
