package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;

public interface IAnnotationCompartmentsService {

	public Long countNumberOfGenes() throws Exception;

	public String[][] getCompartmentsAnnotationDataContainerStats() throws Exception;

	public long getNumberOfCompartments() throws Exception;

	public Map<String, String> getAnnotationCompartments() throws Exception;

	public Map<String, String> getNameAndAbbreviation() throws Exception;

	public List<CompartmentContainer> getBestCompartmenForGene() throws Exception;

	public CompartmentContainer getAnnotationCompartmentByAbbreviation(String abb) throws Exception;

	public Integer insertAnnotationCompartmentNameAndAbbreviation(String name, String abb) throws Exception;

	public boolean checkCompartmentsReportsHasCompartmentEntry(Integer reportId, Integer compartmentId) throws Exception;

	public void insertCompartmentsReportsHasCompartmentEntry(Integer reportId, Integer compartmentId, Double score)
			throws Exception;

	public CompartmentContainer getAnnotationCompartmentByName(String name) throws Exception;

	public Map<Integer, List<CompartmentContainer>> getReportsByGene() throws Exception;

	public Integer countCompartmentsAnnotationReports() throws Exception;


}
