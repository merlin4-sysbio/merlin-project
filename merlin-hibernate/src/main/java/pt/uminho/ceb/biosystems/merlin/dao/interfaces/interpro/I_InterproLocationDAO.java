package pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproLocation;



public interface I_InterproLocationDAO extends IGenericDao<InterproLocation>{
	
	public void addInterproLocation(InterproLocation interproInterproLocation); 
	
	public void addInterproLocationList(List<InterproLocation> interproInterproLocation); 
	
	public List<InterproLocation> getAllInterproLocation(); 
	
	public InterproLocation getInterproLocation(Integer id); 
	
	public void removeInterproLocation(InterproLocation interproInterproLocation); 
	
	public void removeInterproLocationList(List<InterproLocation> interproInterproLocationList); 
	
	public void updateInterproLocationList(List<InterproLocation> interproInterproLocationList); 
	
	public void updateInterproLocation(InterproLocation interproInterproLocation);

	public List<InterproLocation> getAllInterproLocationByAttributes(int id, int envstart, int envend,
			float score, float evalue);

	public Integer insertInterproLocationData(int start, int end, float score, int hmmstart, int hmmend,
			float evalue, int envstart, int envend, int hmmlength, int id);
}
