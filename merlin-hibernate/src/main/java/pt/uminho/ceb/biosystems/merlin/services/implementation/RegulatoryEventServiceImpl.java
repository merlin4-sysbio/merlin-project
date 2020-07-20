package pt.uminho.ceb.biosystems.merlin.services.implementation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.regulatory.IRegulatoryEventDAO;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IRegulatoryEventService;

public class RegulatoryEventServiceImpl implements IRegulatoryEventService{
	

	private IRegulatoryEventDAO regulatoryeventDAO;

	
	public RegulatoryEventServiceImpl(IRegulatoryEventDAO regulatoryeventDAO) {
		this.regulatoryeventDAO  = regulatoryeventDAO;
		
	}
	
	public List<String[]> getRowInfoTFs(Integer id) throws Exception {
		return this.regulatoryeventDAO.getDistinctRegulatoryEventGeneIdAndGeneNameByProteinId(id);
	}



	public Map<String, String[]> getDataFromRegulatoryEvent(Map<String,String[]> qls) throws Exception {
		Map<String,String> res = this.regulatoryeventDAO.getDataFromRegulatoryEvent();
		
		if (res != null)
			for (String x : res.keySet())
				qls.get(x)[2] = res.get(x);
		
		return qls;
				
	}



	public Integer[] countPromoters() throws Exception {
		Integer[] result = new Integer[3];
		int num=0;
		int noname=0;
		int noap=0;
		
		List<String[]> list = this.regulatoryeventDAO.getRegulatoryAttributes();
		
		if (list != null) {
			for (String[] x : list) {
				num++;
				if(x[1] == null) noname++;
				if(x[2] == null) noap++;
			}
		}

		result[0] = num;
		result[1] = noname;
		result[2] = noap;

		return result;
	}
			



	public List<String[]> loadData() throws Exception {
		return this.regulatoryeventDAO.getRegulatoryAttributes();
	}



	public List<String> getProteinName(Integer id) throws Exception {
		return this.regulatoryeventDAO.getRegulatoryEventProteinName(id);
	}



	public Map<String, List<String[]>> getDataFromRegulatoryEvent2() throws Exception {
		Map<String, List<String[]>> index = new HashMap<String, List<String[]>>();

		List<String> check = new ArrayList<String>();		

		List<Object[]> res = this.regulatoryeventDAO.getRegulatoryEventProteinIdAndGeneIdAndGeneName();

		if ( res != null)
			for (Object[] x : res)
		{
			if(!check.contains(x[0]+"."+x[1]))
			{
				check.add(x[0]+"."+x[1]);
				if(index.containsKey(x[0]))
				{
					index.get(x[0]).add(new 
							String[]{(String) x[0], (String)x[1], (String) x[2]}
							);
				}
				else
				{
					ArrayList<String[]> lis = new ArrayList<String[]>();
					lis.add(new String[]{(String) x[0], (String)x[1], (String) x[2]});
					index.put((String) x[0],lis);
				}
			}
		}

		return index;
	}

}
