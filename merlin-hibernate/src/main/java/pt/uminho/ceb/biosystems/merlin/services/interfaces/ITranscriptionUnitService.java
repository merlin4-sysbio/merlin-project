package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;

public interface ITranscriptionUnitService {
	
	public Map<String, String[]> getDataFromTU2(Map<String, String[]> qls) throws Exception;
	
	public List<String> getPromoterNameFromTU(Integer id) throws Exception;
	
	public Map<String,String[]> getDataFromTranscriptUnitPromoter(Map<String,String[]> qls) throws Exception;
	
	public Long countPromoterWithRegulationsByTFs() throws Exception;
	
	public List<String> getNameFromTranscriptionUnitPromoterTable(Integer id) throws Exception;
	
	public double getAvarageNumberOfPromotersByTU(int num) throws Exception;
	
	public Integer insertTranscriptionUnitName(String gene) throws Exception;
	
	public boolean checkTranscriptionUnitNameExists(String name) throws Exception;

}
