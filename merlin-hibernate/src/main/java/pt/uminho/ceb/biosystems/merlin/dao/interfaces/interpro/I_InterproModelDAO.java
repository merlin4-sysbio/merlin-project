package pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproModel;


public interface I_InterproModelDAO extends IGenericDao<InterproModel>{
	
	public void addInterproModel(InterproModel interproInterproModel); 
	
	public void addInterproModelList(List<InterproModel> interproInterproModelList); 
	
	public List<InterproModel> getAllInterproModel(); 
	
	public InterproModel getInterproModel(Integer id); 
	
	public void removeInterproModel(InterproModel interproInterproModel); 
	
	public void removeInterproModelList(List<InterproModel> interproInterproModelList); 
	
	public void updateInterproModel(List<InterproModel> interproInterproModelList); 
	
	public void updateInterproModel(InterproModel interproInterproModel);

	public List<String> getInterproModelAccessionByAccession(String accession);

	public Integer insertInterproModelData(String accession, String description, String name);
}
