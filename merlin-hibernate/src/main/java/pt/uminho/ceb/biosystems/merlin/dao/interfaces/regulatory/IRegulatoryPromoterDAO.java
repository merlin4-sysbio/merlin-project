package pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.regulatory.RegulatoryPromoter;


public interface IRegulatoryPromoterDAO extends IGenericDao<RegulatoryPromoter>{

	public void addRegulatoryPromoter(RegulatoryPromoter RegulatoryPromoter); 
	
	public void addRegulatoryPromoterList(List<RegulatoryPromoter> RegulatoryPromoterList); 
	
	public List<RegulatoryPromoter> getAllRegulatoryPromoter(); 
	
	public RegulatoryPromoter getRegulatoryPromoter(Integer id); 
	
	public void removeRegulatoryPromoter(RegulatoryPromoter RegulatoryPromoter); 
	
	public void removeRegulatoryPromoterList(List<RegulatoryPromoter> RegulatoryPromoterList); 
	
	public void updateRegulatoryPromoterList(List<RegulatoryPromoter> RegulatoryPromoterList); 
	
	public void updateRegulatoryPromoter(RegulatoryPromoter RegulatoryPromoter);
}
