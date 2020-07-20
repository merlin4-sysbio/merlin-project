package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductList;


public interface IEnzymesAnnotationProductListDAO extends IGenericDao<EnzymesAnnotationProductList>{

	public void addEnzymesAnnotationProductList(EnzymesAnnotationProductList EnzymesAnnotationProductList); 
	
	public void addEnzymesAnnotationProductList(List<EnzymesAnnotationProductList> EnzymesAnnotationProductListList); 
	
	public List<EnzymesAnnotationProductList> getAllEnzymesAnnotationProductList(); 
	
	public EnzymesAnnotationProductList getEnzymesAnnotationProductListInteger(Integer id); 
	
	public void removeEnzymesAnnotationProductList(EnzymesAnnotationProductList EnzymesAnnotationProductList); 
	
	public void removeEnzymesAnnotationProductListList(List<EnzymesAnnotationProductList> EnzymesAnnotationProductListList); 
	
	public void updateEnzymesAnnotationProductListList(List<EnzymesAnnotationProductList> EnzymesAnnotationProductListList); 
	
	public void updateEnzymesAnnotationProductList(EnzymesAnnotationProductList EnzymesAnnotationProductList);

	public List<EnzymesAnnotationProductList> getAllEnzymesAnnotationProductListByHomologyDataSKey(Integer skey);
	
	public void removeEnzymesAnnotationProductListByHomologyDataSkey(Integer skey);
	
	public Integer insertEnzymesAnnotationProductListHomologyDataSkeyAndOtherNames(Integer skey, String otherNames);

	List<EnzymesAnnotationProductList> getAllEnzymesAnnotationProductListBySKey(Integer skey);
	
	public Boolean checkEnzymesAnnotationProductListByHomologyDataSKey(Integer skey);
}
