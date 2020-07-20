package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;

public interface IRegulatoryEventService {
	
	public List<String[]> getRowInfoTFs(Integer id) throws Exception;
	
	public Map<String,String[]> getDataFromRegulatoryEvent(Map<String,String[]> qls) throws Exception;
	
	public Integer[] countPromoters() throws Exception;
	
	public List<String[]> loadData() throws Exception;
	
	public List<String> getProteinName(Integer id) throws Exception;
	
	public Map<String,List<String[]>> getDataFromRegulatoryEvent2() throws Exception;
	
	


}
