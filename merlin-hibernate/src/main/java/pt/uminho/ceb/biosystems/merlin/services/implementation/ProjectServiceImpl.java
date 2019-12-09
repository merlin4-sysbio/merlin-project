package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.project.IProjectsDAO;
import pt.uminho.ceb.biosystems.merlin.entities.project.Projects;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IProjectService;


public class ProjectServiceImpl implements IProjectService{

	private IProjectsDAO projectDAO;

	public ProjectServiceImpl(IProjectsDAO projectDAO) {
		this.projectDAO =projectDAO;
	}

	@Override
	public Integer getProjectID(Long genomeID) throws Exception {

		Map<String, Serializable> eqRestrictions = new HashMap<String, Serializable>();
		eqRestrictions.put("organismId", genomeID);
		eqRestrictions.put("latestVersion", true);
		List<Projects> allProjects = this.projectDAO.findByAttributes(eqRestrictions);
		if(allProjects.size()>1)
			throw new Exception("There is more than one entry in DB with latest = true and organismID = "+String.valueOf(genomeID));

		if(allProjects != null && !allProjects.isEmpty())
			return allProjects.get(0).getId();

		Integer version = 1;

		eqRestrictions = new HashMap<String, Serializable>();
		eqRestrictions.put("organismId", genomeID);

		Integer maxVersion = this.projectDAO.findByAttributesWithMaxInColumn(eqRestrictions, "projectVersion");

		if(maxVersion != null) {
			for(Projects proj : allProjects) {
				version+=maxVersion;
				proj.setLatestVersion(false);
			}
		}

		return this.insertProjectEntry(genomeID, version);
	}

	@Override
	public void updateOrganismID(Long genomeID) throws Exception {

		Map<String, Serializable> eqRestrictions = new HashMap<String, Serializable>();
		eqRestrictions.put("organismId", genomeID);

		List<Projects> allProjects = this.projectDAO.findByAttributesAndOrderByColumn(eqRestrictions, "projectVersion", false);

		if(allProjects == null || allProjects.isEmpty()) {
			this.insertProjectEntry(genomeID, null);
			this.updateOrganismID(genomeID);

		}
		else {

			for(Projects proj : this.projectDAO.getAllProjects()) {
				proj.setLatestVersion(false);
			}

			allProjects.get(0).setLatestVersion(true);
		}

	}
	
	@Override
	public void updateProjectsByGenomeID(Long genomeID, Map<String, String> documentSummaryMap) throws Exception {

		Map<String, Serializable> eqRestrictions = new HashMap<String, Serializable>();
		eqRestrictions.put("organismId", genomeID);

		List<Projects> matchedProjects = this.projectDAO.findByAttributesAndOrderByColumn(eqRestrictions, "projectVersion", false);

		if(matchedProjects != null && !matchedProjects.isEmpty()) {
			for(Projects project: matchedProjects) {
				
				if(documentSummaryMap.get("accessionGenBank") != null)
					project.setAccessionGenbank(documentSummaryMap.get("accessionGenBank"));
			
				if(documentSummaryMap.get("accessionRefSeq") != null)
					project.setAccessionRefseq(documentSummaryMap.get("accessionRefSeq"));
				
				if(documentSummaryMap.get("assemblyAccession") != null)
					project.setAssemblyAccession(documentSummaryMap.get("assemblyAccession"));
				
				if(documentSummaryMap.get("assemblyLevel") != null)
					project.setAssemblyLevel(documentSummaryMap.get("assemblyLevel"));
				
				if(documentSummaryMap.get("lastupdateDate") != null)
					project.setLastUpdated(documentSummaryMap.get("lastupdateDate"));
				
				if(documentSummaryMap.get("submitter") != null)
					project.setSubmitter(documentSummaryMap.get("submitter"));
				
				if(documentSummaryMap.get("uid") != null)
					project.setUid(documentSummaryMap.get("uid"));
				
				if(documentSummaryMap.get("assemblyName") != null)
					project.setAssemblyRecord(documentSummaryMap.get("assemblyName"));
				
				if(documentSummaryMap.get("genBankStatus") != null)
					project.setGenbankStatus(documentSummaryMap.get("genBankStatus"));
				
				if(documentSummaryMap.get("refSeqStatus") != null)
					project.setRefseqStatus(documentSummaryMap.get("refSeqStatus"));
			}
				
		}		
	}

	@Override
	public Integer insertProjectEntry(Long genomeID, Integer version) throws Exception {

		if(version == null)
			version = 1;

		long time = System.currentTimeMillis();
		Timestamp timestamp = new Timestamp(time);
		Projects latestEntry = new Projects();
		
		latestEntry.setOrganismId(genomeID.intValue());
		latestEntry.setDate(timestamp);
		latestEntry.setProjectVersion(version);
		latestEntry.setLatestVersion(true);

		return (Integer) this.projectDAO.save(latestEntry);

	}

	@Override
	public String[] getOrganismData(Long genomeID) throws Exception {

		String[] res = new String[2];

		Map<String, Serializable> eqRestrictions = new HashMap<String, Serializable>();
		eqRestrictions.put("organismId", genomeID);

		List<Projects> projects = this.projectDAO.findByAttributesAndOrderByColumn(eqRestrictions, "projectVersion", false);

		if(projects == null || projects.isEmpty())
			return null;

		res[0] = projects.get(0).getOrganismName();
		res[1] = projects.get(0).getOrganismLineage();

		if(res[0] == null || res[0].equals(("-1")))
			return null;

		return res;

	}

	@Override
	public void updateOrganismData(long genomeID, String[] data) throws Exception {


		if(data != null) {
			Map<String, Serializable> eqRestrictions = new HashMap<String, Serializable>();
			eqRestrictions.put("organismId", genomeID);

			List<Projects> projects = this.projectDAO.findByAttributesAndOrderByColumn(eqRestrictions, "projectVersion", false);

			if(projects != null && !projects.isEmpty()) {

				Projects project = projects.get(0);

				project.setOrganismName(data[0]);
				project.setOrganismLineage(data[1]);
			}

		}

	}
	
	@Override
	public Integer getOrganismID() throws Exception {

		Map<String, Serializable> eqRestrictions = new HashMap<String, Serializable>();
		eqRestrictions.put("latestVersion", true);

		Projects project = this.projectDAO.findUniqueByAttributes(eqRestrictions);
		
		if(project != null)
			return project.getOrganismId();
		
		return null;
	}
	
	@Override
	public String getCompartmentsTool(Integer taxID) throws Exception {

		Map<String, Serializable> eqRestrictions = new HashMap<String, Serializable>();
		eqRestrictions.put("organismId", taxID);
		eqRestrictions.put("latestVersion", true);

		Projects project = this.projectDAO.findUniqueByAttributes(eqRestrictions);
		
		if(project != null)
			return project.getCompartmentsTool();
		
		return null;
	}
	
	@Override
	public void updateCompartmentsTool(Long organismId, String tool) throws Exception {

		Map<String, Serializable> eqRestrictions = new HashMap<String, Serializable>();
		eqRestrictions.put("organismId", organismId);
		eqRestrictions.put("latestVersion", true);

		List<Projects> projects = this.projectDAO.findByAttributes(eqRestrictions);
		
		if(projects != null) {
			for(Projects project : projects) {
				if(tool != null)
					project.setCompartmentsTool(tool);
				
				this.projectDAO.update(project);
			}
		}
	}
}
