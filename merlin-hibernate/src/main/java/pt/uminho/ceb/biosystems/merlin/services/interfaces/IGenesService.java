package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;

public interface IGenesService{
	
	public Integer loadGene(String sequenceId, String name, String locusTag, String origin, 
			String transcriptionDirection, String leftEndPosition, String rightEndPosition, String booleanRule) throws Exception;
	
	public Set<String> getAllDatabaseGenes() throws Exception;
	
	public Map<String, Set<String>> getGeneNamesAliases(String className) throws Exception;
	
	public Map<String, List<String>> getECNumbers() throws Exception;
	
	public Map<String, String> getSeqIdAndProteinName() throws Exception;
	
	public Map<String, Set<String>> getSeqIdAndAlias(String className) throws Exception;
	
	public boolean isGeneCompartmentLoaded(Integer idGene) throws Exception;
	
	public void updateLocusTag(String oldLocusTag, String newLocusTag) throws Exception;
	
	public List<String[]> getGeneData2(String ecnumber, Integer id) throws Exception;
	
	public ArrayList<String> getGenesModel() throws Exception;
	
	public List<String[]> getGeneInfo(Integer id) throws Exception;
	
	public Map<Integer, String> getGeneIdLocusTag () throws Exception;
	
	public Map<Integer, String> getGeneIds() throws Exception;
	
	public String getGeneId(String sequenceID) throws Exception;
	
	public List<ModelGene> getGenesID() throws Exception;
	
	public Map<String, Integer> getQueriesByGeneId() throws Exception;
	
	public boolean checkGenes() throws Exception;
	
	public List<String[]> getDataFromGene(Integer id) throws Exception;
	
	public int getGeneIDByLocusTag(String locusTag) throws Exception;
	
	public long countGenesInGeneHasCompartment() throws Exception;
	
	public int countGenes() throws Exception;
	
	public Map<String, List<String>> getECNumbers2() throws Exception;
	
	public void loadGenesCompartments(Integer idGene, Map<String, Integer> compartmentsDatabaseIDs,
			Integer primaryCompartment, String scorePrimaryCompartment, Map<String, String> secondaryCompartmens,
			boolean primLocation) throws Exception;
	
	public Map<String, Integer> getCompartmentsDatabaseIDs(String primaryCompartment, String primaryCompartmentAbb,
			Map<String, Double> secondaryCompartmens, Map<String, String> secondaryCompartmensAbb,
			Map<String, Integer> compartmentsDatabaseIDs, String name) throws Exception;
	
	public Integer loadGene(String locusTag, String sequence_id, String geneName, String direction, String left_end, String right_end, String informationType) throws Exception;
	
	public Map<String, Set<String>> getSequenceIds() throws Exception;
	
	public List<String[]> getAllGenes() throws Exception;
	
	public List<String[]> getRegulatoryGenes() throws Exception;
	
	public List<String[]> getEncodingGenes() throws Exception;
	
	public Map<Integer,Integer> getProteins(Integer protId) throws Exception;
	
	public void insertNewGene(String name, String transcription_direction, String left_end_position,
			String right_end_position, String[] subunits, String locusTag) throws Exception;
	
	public void updateGene(int geneIdentifier, String name, String transcription_direction, String left_end_position,
			String right_end_position, String[] subunits, String[] oldSubunits, String locusTag) throws Exception;
	
	public void updateGenesLocusByQuery(String query, String locus) throws Exception;
	
	public void removeGeneAssignemensts(Integer geneIdentifier, String proteindIdentifier) throws Exception;
	
	public int getGeneLastInsertedID() throws Exception;
	
	public void removeModelGeneByGeneId(Integer geneId) throws Exception;

	public long countEntriesInGene() throws Exception;

	public long countGenesWithName() throws Exception;

	public void insertModelGeneHasCompartment(boolean PrimaryLocation, Double score, Integer model_compartment_idcompartment,
			Integer model_gene_idgene) throws Exception;


	public Boolean checkModelSequenceType(String sequenceType) throws Exception;

	public GeneContainer getGeneDataById(Integer id) throws Exception;

	public Map<String, Integer> getGeneLocusTagAndIdgene() throws Exception;

	public ArrayList<String[]> getAllGenes2() throws Exception;

	public Integer insertNewGene(String locusTag, String name, String query, String origin) throws Exception;

	public Map<String, Set<String>> getQueryAndAliasFromProducts() throws Exception;

	public String getGeneNameById(Integer id) throws Exception;

	public List<GeneContainer> getAllGeneData() throws Exception;

	public List<CompartmentContainer> getCompartmentsRelatedToGene(Integer idGene) throws Exception;

	public void insertModelGeneHasOrthology(Integer geneId, Integer orthologyId, Double score) throws Exception;

	public void updateModelGeneEndPositions(Integer geneId, String leftEndPosition, String rightEndPosition) throws Exception;

	public GeneContainer getModelGeneByQuery(String query) throws Exception;

	public Map<String, Integer> getGeneIdByQuery() throws Exception;

	public Integer insertNewGene(GeneContainer container) throws Exception;

	public void updateGene(GeneContainer gene) throws Exception;

	public List<Integer> getModelGenesIDs(boolean encoded) throws Exception;

	public Integer countInitialMetabolicGenes() throws Exception;

	public Map<Integer, GeneContainer> getAllGeneDatabyIds() throws Exception;

	public Map<Integer, List<CompartmentContainer>> getCompartmentsRelatedToGenes() throws Exception;

	public void removeAllFromModelGeneHasCompartment() throws Exception;
	
	public Map<Integer, String> getGeneIdAndGeneQuery() throws Exception;

}
