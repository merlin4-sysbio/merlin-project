package pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryTranscriptionUnit;



public interface IRegulatoryTranscriptionUnitDAO  extends IGenericDao<RegulatoryTranscriptionUnit>{

	public void addRegulatoryTranscriptionUnit(RegulatoryTranscriptionUnit RegulatoryTranscriptionUnit); 
	
	public void addRegulatoryTranscriptionUnitList(List<RegulatoryTranscriptionUnit> RegulatoryTranscriptionUnitList); 
	
	public List<RegulatoryTranscriptionUnit> getAllRegulatoryTranscriptionUnit(); 
	
	public RegulatoryTranscriptionUnit getRegulatoryTranscriptionUnit(Integer id); 
	
	public void removeRegulatoryTranscriptionUnit(RegulatoryTranscriptionUnit RegulatoryTranscriptionUnit); 
	
	public void removeRegulatoryTranscriptionUnitList(List<RegulatoryTranscriptionUnit> RegulatoryTranscriptionUnitList); 
	
	public void updateRegulatoryTranscriptionUnitList(List<RegulatoryTranscriptionUnit> RegulatoryTranscriptionUnitList); 
	
	public void updateRegulatoryTranscriptionUnit(RegulatoryTranscriptionUnit RegulatoryTranscriptionUnit);

	public List<String> getRegulatoryTranscriptionUnitPromoterNameById(Integer id);

	public Integer insertTranscriptionUnitName(String gene);

	public boolean checkTranscriptionUnitNameExists(String name);

}
