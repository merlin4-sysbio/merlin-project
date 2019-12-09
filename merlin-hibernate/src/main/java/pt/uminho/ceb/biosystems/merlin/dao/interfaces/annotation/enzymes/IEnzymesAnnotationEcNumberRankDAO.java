package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberRank;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;


public interface IEnzymesAnnotationEcNumberRankDAO extends IGenericDao<EnzymesAnnotationEcNumberRank>{
	public void addEnzymesAnnotationEcNumberRank(EnzymesAnnotationEcNumberRank EnzymesAnnotationEcNumberRank); 
	
	public void addEnzymesAnnotationEcNumberRankList(List<EnzymesAnnotationEcNumberRank> EnzymesAnnotationEcNumberRankList); 
	
	public List<EnzymesAnnotationEcNumberRank> getAllEnzymesAnnotationEcNumberRank(); 
	
	public EnzymesAnnotationEcNumberRank getEnzymesAnnotationEcNumberRank(Integer id); 
	
	public void removeEnzymesAnnotationEcNumberRank(EnzymesAnnotationEcNumberRank EnzymesAnnotationEcNumberRank); 
	
	public void removeEnzymesAnnotationEcNumberRankList(List<EnzymesAnnotationEcNumberRank> EnzymesAnnotationEcNumberRankList); 
	
	public void updateEnzymesAnnotationEcNumberRankList(List<EnzymesAnnotationEcNumberRank> EnzymesAnnotationEcNumberRankList); 
	
	public void updateEnzymesAnnotationEcNumberRank(EnzymesAnnotationEcNumberRank EnzymesAnnotationEcNumberRank);

	public List<String[]> getEnzymesAnnotationEcNumberrankAttributesByQuery(String query);

	public List<String[]> getEnzymesAnnotationEcNumberrankAttributes();

	public List<String[]> getEnzymesAnnotationEcNumberrankSKeyAndTaxRank();

	public List<EnzymesAnnotationEcNumberRank> getAllEnzymesAnnotationEcNumberRankAttributes(Integer geneHomology_s_key,
			String concatEC, int ecnumber);

	public List<String[]> getEnzymesAnnotationEcNumberrankAttributesByLocusTag(String query);

	public Integer insertEcNumberRank(String concatEC, Integer ecnumber, EnzymesAnnotationGeneHomology geneHomology);

}
