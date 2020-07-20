package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologyData;



public interface IEnzymesAnnotationHomologydataDAO extends IGenericDao<EnzymesAnnotationHomologyData>{

	public void addEnzymesAnnotationHomologyData(EnzymesAnnotationHomologyData enzymesAnnotationHomologyData); 
	
	public void addEnzymesAnnotationHomologyDataList(List<EnzymesAnnotationHomologyData> enzymesAnnotationHomologyDataList); 
	
	public List<EnzymesAnnotationHomologyData> getAllEnzymesAnnotationHomologyData(); 
	
	public EnzymesAnnotationHomologyData getEnzymesAnnotationHomologyData(Integer id); 
	
	public void removeEnzymesAnnotationHomologyData(EnzymesAnnotationHomologyData enzymesAnnotationHomologyData); 
	
	public void removeEnzymesAnnotationHomologyDataList(List<EnzymesAnnotationHomologyData> enzymesAnnotationHomologyDataList); 
	
	public void updateEnzymesAnnotationHomologyDataList(List<EnzymesAnnotationHomologyData> enzymesAnnotationHomologyDataList); 
	
	public void updateEnzymesAnnotationHomologyData(EnzymesAnnotationHomologyData enzymesAnnotationHomologyData);

	public List<EnzymesAnnotationHomologyData> getAllEnzymesAnnotationHomologyDataBySKey(Integer skey);

	public Boolean checkHomologyDataByGeneHomologySKey(Integer skey);
	
	public List<String[]> getEnzymesAnnotationHomologydataSKeyAndOtherNames();

	public List<String[]> getEnzymesAnnotationHomologydataSKeyAndOtherEcNumbers();

	public void updateEnzymesAnnotationHomologydataLocusTagByHomologySkey(String locus, Integer skey);
	
	public void updateEnzymesAnnotationHomologydataNameByHomologySkey(String name, Integer skey);
	
	public void updateEnzymesAnnotationHomologydataNotesAndProductByHomologySkey(String notes, String product, Integer skey);
	
	public void updateEnzymesAnnotationHomologydataAttributesByHomologySkey(Integer homologyskey, String product, Integer skey,
			String locus, String ec);

	List<EnzymesAnnotationHomologyData> getAllEnzymesAnnotationHomologyDataAttributes();
	
	public Integer getHomologyDataIDByGeneHomologySKey(Integer skey);

	public Integer insertHomologyData(EnzymesAnnotationGeneHomology geneHomology, String locusTag, String geneName, String product,
			String ecnumber, Boolean selected, String chromossome, String notes);

	public List<String[]> getHomologyProductsByGeneHomologySkey(Integer skey);
}
