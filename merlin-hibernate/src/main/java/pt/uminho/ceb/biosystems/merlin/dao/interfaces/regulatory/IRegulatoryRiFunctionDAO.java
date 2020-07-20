package pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryRiFunction;



public interface IRegulatoryRiFunctionDAO extends IGenericDao<RegulatoryRiFunction>{

	public void addRegulatoryRiFunction(RegulatoryRiFunction RegulatoryRiFunction); 
	
	public void addRegulatoryRiFunctionList(List<RegulatoryRiFunction> RegulatoryRiFunctionList); 
	
	public List<RegulatoryRiFunction> getAllRegulatoryRiFunction(); 
	
	public RegulatoryRiFunction getRegulatoryRiFunction(Integer id); 
	
	public void removeRegulatoryRiFunction(RegulatoryRiFunction RegulatoryRiFunction); 
	
	public void removeRegulatoryRiFunctionList(List<RegulatoryRiFunction> RegulatoryRiFunctionList); 
	
	public void updateRegulatoryRiFunctionList(List<RegulatoryRiFunction> RegulatoryRiFunctionList); 
	
	public void updateRegulatoryRiFunction(RegulatoryRiFunction RegulatoryRiFunction);
}
