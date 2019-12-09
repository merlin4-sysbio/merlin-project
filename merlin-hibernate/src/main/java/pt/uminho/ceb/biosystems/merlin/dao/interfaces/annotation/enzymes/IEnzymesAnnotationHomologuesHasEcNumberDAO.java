package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologuesHasEcNumber;



public interface IEnzymesAnnotationHomologuesHasEcNumberDAO extends IGenericDao<EnzymesAnnotationHomologuesHasEcNumber>{

	public void addEnzymesAnnotationHomologuesHasEcNumber(EnzymesAnnotationHomologuesHasEcNumber enzymesAnnotationHomologuesHasEcNumber); 
	
	public void addEnzymesAnnotationHomologuesHasEcNumber(List<EnzymesAnnotationHomologuesHasEcNumber> enzymesAnnotationHomologuesHasEcNumberList); 
	
	public List<EnzymesAnnotationHomologuesHasEcNumber> getAllEnzymesAnnotationHomologuesHasEcNumber(); 
	
	public EnzymesAnnotationHomologuesHasEcNumber getEnzymesAnnotationHomologuesHasEcNumber(Integer id); 
	
	public void removeEnzymesAnnotationHomologuesHasEcNumber(EnzymesAnnotationHomologuesHasEcNumber enzymesAnnotationHomologuesHasEcNumber); 
	
	public void removeEnzymesAnnotationHomologuesHasEcNumberList(List<EnzymesAnnotationHomologuesHasEcNumber> enzymesAnnotationHomologuesHasEcNumberList); 
	
	public void updateEnzymesAnnotationHomologuesHasEcNumberList(List<EnzymesAnnotationHomologuesHasEcNumber> enzymesAnnotationHomologuesHasEcNumberList); 
	
	public void updateEnzymesAnnotationHomologuesHasEcNumber(EnzymesAnnotationHomologuesHasEcNumber enzymesAnnotationHomologuesHasEcNumber);

	public List<EnzymesAnnotationHomologuesHasEcNumber> getAllEnzymesAnnotationHomologuesHasEcNumberByAttributes(
			Integer hom_skey, Integer ec_skey);

	public void insertEnzymesAnnotationHomologuesHasEcNumber(int homologues_s_key, int ecnumber_s_key);
}

