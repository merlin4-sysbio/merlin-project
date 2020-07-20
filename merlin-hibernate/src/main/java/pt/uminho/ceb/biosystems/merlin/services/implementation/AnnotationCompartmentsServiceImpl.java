package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.compartments.ICompartmentsAnnotationCompartmentsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.compartments.ICompartmentsAnnotationReportsHasCompartmentsDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.CompartmentsAnnotationCompartments;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.CompartmentsAnnotationReportsHasCompartments;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.compartments.CompartmentsAnnotationReportsHasCompartmentsId;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IAnnotationCompartmentsService;

public class AnnotationCompartmentsServiceImpl implements IAnnotationCompartmentsService {

	private ICompartmentsAnnotationCompartmentsDAO compartmentDAO;
	private ICompartmentsAnnotationReportsHasCompartmentsDAO reportsHasCompartments;

	public AnnotationCompartmentsServiceImpl(ICompartmentsAnnotationCompartmentsDAO compartmentDAO,
			ICompartmentsAnnotationReportsHasCompartmentsDAO reportsHasCompartments) {
		this.compartmentDAO = compartmentDAO;
		this.reportsHasCompartments = reportsHasCompartments;
	}

	@Override
	public String[][] getCompartmentsAnnotationDataContainerStats() throws Exception {

		String[][] res = new String[2][];

		Long num = countNumberOfGenes();
		Integer num_comp = this.reportsHasCompartments.countCompartmentsAnnotationReports();

		res[0] = new String[] { "Number of genes", "" + num };
		res[1] = new String[] { "Number of distinct compartments ", "" + num_comp };
		return res;
	}
	
	@Override
	public Integer countCompartmentsAnnotationReports() throws Exception {
		
		return this.reportsHasCompartments.countCompartmentsAnnotationReports();
	}
	

	@Override
	public long getNumberOfCompartments() throws Exception {

		return this.reportsHasCompartments.getDistinctCompartmentsAnnotationReportsHasCompartmentsIds();

	}

	@Override
	public Map<String, String> getAnnotationCompartments() throws Exception {
		return this.reportsHasCompartments.getDistinctCompartmentsAnnotationReportsHasCompartmentsNameAndAbbreviation();
	}

	@Override
	public Long countNumberOfGenes() throws Exception {

		return this.compartmentDAO.countAll();
	}

	@Override
	public Map<String, String> getNameAndAbbreviation() throws Exception {

		List<CompartmentsAnnotationCompartments> compartments = this.compartmentDAO
				.getAllCompartmentsAnnotationCompartments();

		Map<String, String> res = new HashMap<>();

		if (compartments != null)
			for (CompartmentsAnnotationCompartments compartment : compartments)
				res.put(compartment.getName(), compartment.getAbbreviation());

		return res;
	}

	@Override
	public List<CompartmentContainer> getBestCompartmenForGene() throws Exception {
		return this.reportsHasCompartments.getBestCompartmenForGene();
	}

	@Override
	public CompartmentContainer getAnnotationCompartmentByAbbreviation(String abb) throws Exception {

		CompartmentsAnnotationCompartments annotCompartment = this.compartmentDAO.findUniqueByAttribute("abbreviation",
				abb);

		if (annotCompartment == null)
			return null;

		return new CompartmentContainer(annotCompartment.getId(), annotCompartment.getName(),
				annotCompartment.getAbbreviation());
	}

	@Override
	public CompartmentContainer getAnnotationCompartmentByName(String name) throws Exception {

		CompartmentsAnnotationCompartments annotCompartment = this.compartmentDAO.findUniqueByAttribute("name", name);

		if (annotCompartment == null)
			return null;

		return new CompartmentContainer(annotCompartment.getId(), annotCompartment.getName(),
				annotCompartment.getAbbreviation());
	}

	@Override
	public Integer insertAnnotationCompartmentNameAndAbbreviation(String name, String abb) throws Exception {

		CompartmentsAnnotationCompartments comp = new CompartmentsAnnotationCompartments();

		comp.setName(name);
		comp.setAbbreviation(abb);

		return (Integer) this.compartmentDAO.save(comp);
	}

	@Override
	public boolean checkCompartmentsReportsHasCompartmentEntry(Integer reportId, Integer compartmentId)
			throws Exception {

		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelGeneIdgene", reportId);
		dic.put("id.compartmentsAnnotationCompartmentsId", compartmentId);

		CompartmentsAnnotationReportsHasCompartments entry = this.reportsHasCompartments.findUniqueByAttributes(dic);

		return (entry != null);
	}

	@Override
	public void insertCompartmentsReportsHasCompartmentEntry(Integer reportId, Integer compartmentId, Double score)
			throws Exception {

		CompartmentsAnnotationReportsHasCompartments repHasComp = new CompartmentsAnnotationReportsHasCompartments();

		CompartmentsAnnotationReportsHasCompartmentsId id = new CompartmentsAnnotationReportsHasCompartmentsId(reportId,
				compartmentId);

		repHasComp.setId(id);
		repHasComp.setScore(Float.valueOf(score.toString()));

		this.reportsHasCompartments.save(repHasComp);

	}
	
	@Override
	public Map<Integer, List<CompartmentContainer>> getReportsByGene() throws Exception {

		Map<Integer, List<CompartmentContainer>> result = new HashMap<>();
		
		List<CompartmentsAnnotationCompartments> compartments = this.compartmentDAO.getAllCompartmentsAnnotationCompartments();
		
		for(CompartmentsAnnotationCompartments compartment : compartments) {
			
			Set<CompartmentsAnnotationReportsHasCompartments> has = compartment.getCompartmentsAnnotationReportsHasCompartmentses();
			
			for(CompartmentsAnnotationReportsHasCompartments repHasComp : has) {
				
				Integer geneId = repHasComp.getId().getModelGeneIdgene();
				
				if(!result.containsKey(geneId))
					result.put(geneId, new ArrayList<>());
				
				result.get(geneId).add(new CompartmentContainer(compartment.getName(), compartment.getAbbreviation()));
			}
		}

		return result;
	}
}
