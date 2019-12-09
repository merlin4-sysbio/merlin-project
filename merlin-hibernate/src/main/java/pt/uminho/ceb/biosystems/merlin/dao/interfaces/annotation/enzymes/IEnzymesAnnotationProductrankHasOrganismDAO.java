package pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductRankHasOrganism;




public interface IEnzymesAnnotationProductrankHasOrganismDAO extends IGenericDao<EnzymesAnnotationProductRankHasOrganism>{

	public void addEnzymesAnnotationProductRankHasOrganism(EnzymesAnnotationProductRankHasOrganism enzymesAnnotationProductRankHasOrganism); 
	
	public void addEnzymesAnnotationProductRankHasOrganismList(List<EnzymesAnnotationProductRankHasOrganism> enzymesAnnotationProductRankHasOrganismList); 
	
	public List<EnzymesAnnotationProductRankHasOrganism> getAllEnzymesAnnotationProductRankHasOrganism(); 
	
	public EnzymesAnnotationProductRankHasOrganism getEnzymesAnnotationProductRankHasOrganism(Integer id); 
	
	public void removeEnzymesAnnotationProductRankHasOrganism(EnzymesAnnotationProductRankHasOrganism enzymesAnnotationProductRankHasOrganism); 
	
	public void removeEnzymesAnnotationProductRankHasOrganismList(List<EnzymesAnnotationProductRankHasOrganism> enzymesAnnotationProductRankHasOrganismList); 
	
	public void updateEnzymesAnnotationProductRankHasOrganismList(List<EnzymesAnnotationProductRankHasOrganism> enzymesAnnotationProductRankHasOrganismList); 
	
	public void updateEnzymesAnnotationProductRankHasOrganism(EnzymesAnnotationProductRankHasOrganism enzymesAnnotationProductRankHasOrganism);

	public List<String[]> getEnzymesAnnotationProductrankHasOrganismSKeyAndTaxRank();

	public List<EnzymesAnnotationProductRankHasOrganism> getAllEnzymesAnnotationProductRankHasOrganismByAttributes(
			Integer skey, Integer orgSkey);
	
	public void insertEnzymesAnnotationProductrankHasOrganismAttributes(Integer productRankSKey, Integer organismSKey);

	public List<Integer> getAllProdRankSKeyInProdRankHasOrganism();
}