package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory.IRegulatoryTranscriptionUnitDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory.IRegulatoryTranscriptionUnitPromoterDAO;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.ITranscriptionUnitService;

public class TranscriptionUnitServiceImpl implements ITranscriptionUnitService{

	private IRegulatoryTranscriptionUnitDAO transcriptionunitDAO;
	private IRegulatoryTranscriptionUnitPromoterDAO transcriptionpromoterDAO;

	

	public TranscriptionUnitServiceImpl(IRegulatoryTranscriptionUnitDAO transcriptionunitDAO, IRegulatoryTranscriptionUnitPromoterDAO transcriptionpromoterDAO) {
		this.transcriptionunitDAO  = transcriptionunitDAO;
		this.transcriptionpromoterDAO = transcriptionpromoterDAO;

	}


	public Map<String, String[]> getDataFromTU2(Map<String, String[]> qls) throws Exception {
		Map<Integer, Integer>  res = this.transcriptionpromoterDAO.getRegulatoryTranscriptionUnitIdAndPromoterId();
		
		if (res != null)
			for (Integer x : res.keySet())
					qls.get(x)[2] = String.valueOf(res.get(x));
		return qls;
				
	}


	public List<String> getPromoterNameFromTU(Integer id) throws Exception {
		return this.transcriptionunitDAO.getRegulatoryTranscriptionUnitPromoterNameById(id);
	}


	public Map<String,String[]> getDataFromTranscriptUnitPromoter(Map<String,String[]> qls) throws Exception {
		Map<Integer, Integer> res = this.transcriptionpromoterDAO.getRegulatoryTranscriptionUnitPromoterIdAndTranscriptionId();
		
		if (res != null)
			for (Integer x : res.keySet())
				qls.get(x)[4] = String.valueOf(res.get(x));
		
		return qls;		
	}
	


	public Long countPromoterWithRegulationsByTFs() throws Exception {
		return this.transcriptionpromoterDAO.getRegulatoryTranscriptionUnitPromoterDistinctIdAndTranscriptionId();
	}


	public List<String> getNameFromTranscriptionUnitPromoterTable(Integer id) throws Exception {
		return this.transcriptionpromoterDAO.getRegulatoryTranscriptionUnitNameByPromoterId(id);
	}


	public double getAvarageNumberOfPromotersByTU(int num) throws Exception {
		double promoters_by_tus = 0.0;

		Long res = this.transcriptionpromoterDAO.getRegulatoryTranscriptionUnitPromoterDistinctId();

		if(res != null)
			promoters_by_tus = (Double.valueOf(res)) / (Double.valueOf(num));

		return promoters_by_tus;
	}
	
	public Integer insertTranscriptionUnitName(String gene) throws Exception {
		
		return this.transcriptionunitDAO.insertTranscriptionUnitName(gene);
		
	}
	
	public boolean checkTranscriptionUnitNameExists(String name) throws Exception {
		
		return this.transcriptionunitDAO.checkTranscriptionUnitNameExists(name);
		
	}


}
