package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberList;


public interface IEnzymesAnnotationEcNumberListDAO extends IGenericDao<EnzymesAnnotationEcNumberList>{
	
	public void addEnzymesAnnotationEcNumberList(EnzymesAnnotationEcNumberList EnzymesAnnotationEcNumberList); 
	
	public void addEnzymesAnnotationEcNumberListList(List<EnzymesAnnotationEcNumberList> EnzymesAnnotationEcNumberListList); 
	
	public List<EnzymesAnnotationEcNumberList> getAllEnzymesAnnotationEcNumberList(); 
	
	public EnzymesAnnotationEcNumberList getEnzymesAnnotationEcNumberList(Integer id); 
	
	public void removeEnzymesAnnotationEcNumberList(EnzymesAnnotationEcNumberList EnzymesAnnotationEcNumberList); 
	
	public void removeEnzymesAnnotationEcNumberListList(List<EnzymesAnnotationEcNumberList> EnzymesAnnotationEcNumberListList); 
	
	public void updateEnzymesAnnotationEcNumberListList(List<EnzymesAnnotationEcNumberList> EnzymesAnnotationEcNumberListList); 
	
	public void updateEnzymesAnnotationEcNumberList(EnzymesAnnotationEcNumberList EnzymesAnnotationEcNumberList);

	public List<EnzymesAnnotationEcNumberList> getAllEnzymesAnnotationEcNumberListByHomologyDataSKey(Integer skey);
	
	public void removeEnzymesAnnotationEcNumberListByHomologyDataSkey(Integer skey);
	
	public Integer insertEnzymesAnnotationEcNumberListSkeyAndOtherEcNumbers(Integer skey, String otherEcnumbers);

	public List<EnzymesAnnotationEcNumberList> getAllEnzymesAnnotationEcNumberListBySKey(Integer skey);

	public Boolean checkEcNumberHasenzymesAnnotationHomologydata(Integer homologyDataSkey);

}
