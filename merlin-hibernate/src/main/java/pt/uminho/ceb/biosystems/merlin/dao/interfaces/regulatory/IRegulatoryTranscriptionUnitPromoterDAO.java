package pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryTranscriptionUnitPromoter;

public interface IRegulatoryTranscriptionUnitPromoterDAO extends IGenericDao<RegulatoryTranscriptionUnitPromoter>{
	public void addRegulatoryTranscriptionUnitPromoter(RegulatoryTranscriptionUnitPromoter model); 
	
	public void addRegulatoryTranscriptionUnitPromoterList(List<RegulatoryTranscriptionUnitPromoter> model); 
	
	public List<RegulatoryTranscriptionUnitPromoter> getAllRegulatoryTranscriptionUnitPromoter(); 
	
	public RegulatoryTranscriptionUnitPromoter getRegulatoryTranscriptionUnitPromoter(Integer id); 
	
	public void removeRegulatoryTranscriptionUnitPromoter(RegulatoryTranscriptionUnitPromoter model); 
	
	public void removeRegulatoryTranscriptionUnitPromoterList(List<RegulatoryTranscriptionUnitPromoter> model); 
	
	public void updateRegulatoryTranscriptionUnitPromoterList(List<RegulatoryTranscriptionUnitPromoter> model); 
	
	public void updateRegulatoryTranscriptionUnitPromoter(RegulatoryTranscriptionUnitPromoter model);

	public Map<Integer, Integer> getRegulatoryTranscriptionUnitIdAndPromoterId();

	public Map<Integer, Integer> getRegulatoryTranscriptionUnitPromoterIdAndTranscriptionId();

	public Long getRegulatoryTranscriptionUnitPromoterDistinctIdAndTranscriptionId();

	public List<String> getRegulatoryTranscriptionUnitNameByPromoterId(Integer id);

	public Long getRegulatoryTranscriptionUnitPromoterDistinctId();
}
