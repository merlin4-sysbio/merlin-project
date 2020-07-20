package pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryEvent;

public interface IRegulatoryEventDAO extends IGenericDao<RegulatoryEvent>{

	public void addRegulatoryEvent(RegulatoryEvent RegulatoryEvent); 
	
	public void addRegulatoryEventList(List<RegulatoryEvent> RegulatoryEventList); 
	
	public List<RegulatoryEvent> getAllModelGene(); 
	
	public RegulatoryEvent getRegulatoryEvent(Integer id); 
	
	public void removeRegulatoryEvent(RegulatoryEvent RegulatoryEvent); 
	
	public void removeRegulatoryEventList(List<RegulatoryEvent> RegulatoryEventList); 
	
	public void updateRegulatoryEventList(List<RegulatoryEvent> RegulatoryEventList); 
	
	public void updateRegulatoryEvent(RegulatoryEvent RegulatoryEvent);

	public List<String[]> getDistinctRegulatoryEventGeneIdAndGeneNameByProteinId(Integer id);

	public Map<String, String> getDataFromRegulatoryEvent();

	public List<String[]> getRegulatoryAttributes();

	public List<String> getRegulatoryEventProteinName(Integer id);

	public List<Object[]> getRegulatoryEventProteinIdAndGeneIdAndGeneName();
}
