package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.Map;

public interface IProjectService {

	public Integer getProjectID(Long genomeID) throws Exception;

	public void updateOrganismID(Long genomeID) throws Exception;

	public Integer insertProjectEntry(Long genomeID, Integer version) throws Exception;

	public String[] getOrganismData(Long genomeID) throws Exception;

	public void updateOrganismData(long genomeID, String[] data) throws Exception;

	public Integer getOrganismID() throws Exception;

	public String getCompartmentsTool(Integer taxID) throws Exception;

	public void updateCompartmentsTool(Long organismId, String tool) throws Exception;

	void updateProjectsByGenomeID(Long genomeID, Map<String, String> documentSummaryMap) throws Exception;
	
}
