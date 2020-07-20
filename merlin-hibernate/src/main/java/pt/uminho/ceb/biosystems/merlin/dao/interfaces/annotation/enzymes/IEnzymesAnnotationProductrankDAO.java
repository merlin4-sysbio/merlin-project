package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductRank;


public interface IEnzymesAnnotationProductrankDAO extends IGenericDao<EnzymesAnnotationProductRank>{

	public void addEnzymesAnnotationProductRank(EnzymesAnnotationProductRank enzymesAnnotationProductRank); 
	
	public void addEnzymesAnnotationProductRankList(List<EnzymesAnnotationProductRank> enzymesAnnotationProductRankList); 
	
	public List<EnzymesAnnotationProductRank> getAllEnzymesAnnotationProductRank(); 
	
	public EnzymesAnnotationProductRank getEnzymesAnnotationProductRank(Integer id); 
	
	public void removeEnzymesAnnotationProductRank(EnzymesAnnotationProductRank enzymesAnnotationProductRank); 
	
	public void removeEnzymesAnnotationProductRankList(List<EnzymesAnnotationProductRank> enzymesAnnotationProductRankList); 
	
	public void updateEnzymesAnnotationProductRankList(List<EnzymesAnnotationProductRank> enzymesAnnotationProductRankList); 
	
	public void updateEnzymesAnnotationProductRank(EnzymesAnnotationProductRank enzymesAnnotationProductRank);

	public List<String[]> getEnzymesAnnotationProductrankAttributesByLocusTag(String locus);

	public List<String[]> getEnzymesAnnotationProductrankAttributes();
	
	public Integer insertEnzymesAnnotationProductrankSkeyAndProductNameAndRank(EnzymesAnnotationGeneHomology enzymesAnnotationGeneHomology, String name, Integer rank_);

	public List<EnzymesAnnotationProductRank> getAllEnzymesAnnotationProductRankByAttributes(Integer skey, String name, Integer rank);

	public Integer getEnzymesAnnotationProductRankIDByAttributes(Integer skey, String name, Integer rank);
}
