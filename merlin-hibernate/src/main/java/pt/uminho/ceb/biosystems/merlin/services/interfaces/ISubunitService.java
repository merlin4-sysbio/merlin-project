package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;



public interface ISubunitService {
	
	public Map<String, List<Integer>> loadEnzymeGetReactions(Integer idgene, Set<String> ecNumber, 
			String proteinName, boolean integratePartial, boolean integrateFull, 
			boolean insertProductNames, String classe, String inchi, Float molecularWeight, 
			Float molecularWeightExp, Float molecularWeightKd, Float molecularWeightSeq, Float pi,
			boolean inModel, String source, String aliasName, Integer aliasId) throws Exception;
	
	public List<Integer> getEnzymeCompartments(String ecNumber) throws Exception;
	
	public Map<Integer, String> getProteinIdAndEcNumber(Integer geneID) throws Exception;
	
	public long countGenesEncodingProteins() throws Exception;
	
	public long countGenesInModel() throws Exception;
	
	public List<ProteinContainer> getDataFromSubunit(Integer id) throws Exception;
	
	public boolean checkModules(int gene, int protein_id) throws Exception;
	
	public Map<String, List<String>> getECNumbers_() throws Exception;
	
	public List<Integer> countGenesEncodingEnzymesAndTransporters() throws Exception;
	
	public int countProteinsAssociatedToGenes() throws Exception;
	
	public boolean checkSubunitData(Integer id) throws Exception;

	public Map<String, Integer> countGenesReactionsBySubunit() throws Exception;

	public String[][] getSubunitsByGeneId(int geneIdentifier) throws Exception;

	public Map<String, Integer> getProteinsCountFromSubunit() throws Exception;

	public List<String[]> getGPRsECNumbers(boolean isCompartimentalized) throws Exception;

	public List<String[]> getGeneData(Integer id) throws Exception;

	public boolean checkModulesByModuleId(int geneId, int proteinId, int moduleId) throws Exception;

	public List<String[]> getGPRstatusAndReactionAndDefinition(Integer proteinId) throws Exception;

	public void removeSubunitByGeneIdAndProteinId(Integer geneId, Integer protId) throws Exception;

	public boolean checkModelSubunitEntry(Integer geneId, Integer protId) throws Exception;

	public void insertModelSubunit(Integer geneId, Integer protId, String note, String gprStatus) throws Exception;

	public Long countSubunitEntries() throws Exception;

	public boolean isProteinEncodedByGenes(Integer proteinId) throws Exception;

	public Map<Integer, Integer> getModelSubunitGeneIdAndEnzymeProteinIdByEcNumber(String ecNumber) throws Exception;

}
