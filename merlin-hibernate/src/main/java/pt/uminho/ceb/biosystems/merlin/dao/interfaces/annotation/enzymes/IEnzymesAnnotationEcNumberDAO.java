package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumber;


public interface IEnzymesAnnotationEcNumberDAO extends IGenericDao<EnzymesAnnotationEcNumber>{

	
	public void addEnzymesAnnotationEcNumber(EnzymesAnnotationEcNumber EnzymesAnnotationEcNumber); 
	
	public void addEnzymesAnnotationEcNumberList(List<EnzymesAnnotationEcNumber> EnzymesAnnotationEcNumberList); 
	
	public List<EnzymesAnnotationEcNumber> getAllEnzymesAnnotationEcNumber(); 
	
	public EnzymesAnnotationEcNumber getEnzymesAnnotationEcNumber(Integer id); 
	
	public void removeEnzymesAnnotationEcNumber(EnzymesAnnotationEcNumber EnzymesAnnotationEcNumber); 
	
	public void removeEnzymesAnnotationEcNumberList(List<EnzymesAnnotationEcNumber> EnzymesAnnotationEcNumberList); 
	
	public void updateEnzymesAnnotationEcNumberList(List<EnzymesAnnotationEcNumber> EnzymesAnnotationEcNumberList); 
	
	public void updateEnzymesAnnotationEcNumber(EnzymesAnnotationEcNumber EnzymesAnnotationEcNumber);

	public List<EnzymesAnnotationEcNumber> getAllEnzymesAnnotationEcNumberByEcNumber(String ecNumber);

}
