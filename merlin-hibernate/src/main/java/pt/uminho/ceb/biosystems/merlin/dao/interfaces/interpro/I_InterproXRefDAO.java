package pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproXRef;


public interface I_InterproXRefDAO extends IGenericDao<InterproXRef>{
	
	
	public void addInterproXRef(InterproXRef interproInterproXRef); 
	
	public void addInterproXRefList(List<InterproXRef> interproInterproXRefList); 
	
	public List<InterproXRef> getAllInterproXRef(); 
	
	public InterproXRef getInterproXRef(Integer id); 
	
	public void removeInterproXRef(InterproXRef interproInterproXRef); 
	
	public void removeInterproXRefList(List<InterproXRef> interproInterproXRefList); 
	
	public void updateInterproXRefList(List<InterproXRef> interproInterproXRefList); 
	
	public void updateInterproXRef(InterproXRef interproInterproXRef);

	public List<InterproXRef> getAllInterproXRefByExternalIdAndEtryId(String external, Integer entryId);

	public Integer insertInterproXRefData(String category, String database, String name, String external_id, Integer entry_id);
}
