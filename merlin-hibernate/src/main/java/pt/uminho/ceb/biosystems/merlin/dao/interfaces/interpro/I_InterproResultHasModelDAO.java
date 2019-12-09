package pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasModel;

public interface I_InterproResultHasModelDAO extends IGenericDao<InterproResultHasModel>{
	
	public void addInterproResultHasModel(InterproResultHasModel InterproResultHasModel); 
	
	public void addInterproResultHasModelList(List<InterproResultHasModel> InterproResultHasModelList); 
	
	public List<InterproResultHasModel> getAllInterproResultHasModel(); 
	
	public InterproResultHasModel getInterproResultHasModel(Integer id); 
	
	public void removeInterproResultHasModel(InterproResultHasModel InterproResultHasModel); 
	
	public void removeInterproResultHasModelList(List<InterproResultHasModel> InterproResultHasModelList); 
	
	public void updateInterproResultHasModelList(List<InterproResultHasModel> InterproResultHasModelList); 
	
	public void updateInterproResultHasModel(InterproResultHasModel InterproResultHasModel);

	public List<InterproResultHasModel> getAllInterproResultHasModelByResultIdAndModelAccession(
			int resultId, String modelAccession);

	public Integer insertInterproResultHasModel(String modelAccession, int resultId);

}
