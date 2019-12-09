package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneHasOrthologyDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelOrthologyDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelOrthology;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IOrthologyService;

public class OrthologyServiceImpl implements IOrthologyService{


	private IModelOrthologyDAO orthologyDAO;
	private IModelGeneHasOrthologyDAO hasOrthology;


	public OrthologyServiceImpl(IModelOrthologyDAO orthologyDAO, IModelGeneHasOrthologyDAO hasOrthology) {
		this.orthologyDAO  = orthologyDAO;
		this.hasOrthology = hasOrthology;
	}


	@Override
	public Map<String, Set<String>> getOrthologs(String entryId) throws Exception {
		Map<String, Set<String>> ret = new HashMap<String, Set<String>>();
		Set<String> ret_set = new HashSet<String>();

		ret_set.add(entryId);

		List<String> res = this.orthologyDAO.getModelOrthologyLocusIdbyEntryId(entryId);

		if (res != null) {
			for (String x : res)
				ret.put(":" + x, ret_set);	
		}

		return ret;
	}

	@Override
	public List<String[]> getDataFromGeneHasOrthology(Integer id) throws Exception {
		return this.hasOrthology.getModelGeneHasOrthologyAttributesByGeneId(id);
	}

	@Override
	public Map<String, Integer> getEntryIdAndOrthologyId(){
		return this.orthologyDAO.getEntryIdAndOrthologyId();
	}
	
	@Override
	public Integer insertNewOrthologue(String entryId, String locus) throws Exception{

		ModelOrthology orth = new ModelOrthology();
		
		orth.setEntryId(entryId);
		
		if(locus != null)
			orth.setLocusId(locus);
		
		return (Integer) this.orthologyDAO.save(orth);
	}	
	
	@Override
	public boolean checkGeneHasOrthologyEntries(int geneID, int orthId) throws Exception {
		boolean exists = false;

		List<ModelGeneHasOrthology> list = this.hasOrthology.getAllModelGeneHasOrthologyByGeneIdAndOrthologyId(geneID, orthId);

		if(list != null && !list.isEmpty()) {
			exists = true;
		}
	
		return exists;
	}
	
	@Override
	public Integer getOrthologyIdByEntryIdAndLocus(String entryId, String locusId){
		
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("entryId", entryId);
		dic.put("locusId", locusId);
		
		List<ModelOrthology> res = this.orthologyDAO.findByAttributes(dic);

		if(res == null || res.isEmpty())
			return null;
		
		return res.get(0).getId();
	}
	
	@Override
	public Integer getOrthologyIdByEntryIdWherwLocusIdIsNullOrEmpty(String entryId){
		
		List<ModelOrthology> res = this.orthologyDAO.findBySingleAttribute("entryId", entryId);

		if(res != null) {
		
			for(ModelOrthology orthology : res) {
				
				if(orthology.getLocusId() == null || orthology.getLocusId().isEmpty())
					return orthology.getId();
			}
		}
		
		return null;
	}
	
	@Override
	public void updateOrthologyLocusIdByEntryId(String entryId, String locusId){
		
		List<ModelOrthology> res = this.orthologyDAO.findBySingleAttribute("entryId", entryId);

		if(res != null) {
		
			for(ModelOrthology orthology : res) {
				
				orthology.setLocusId(locusId);
				
				this.orthologyDAO.update(orthology);
			}
		}
		
	}
}

