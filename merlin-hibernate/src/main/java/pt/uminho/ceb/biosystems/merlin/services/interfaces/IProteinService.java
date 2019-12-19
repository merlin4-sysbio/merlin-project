package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public interface IProteinService {
	
	public List<String[]> getProteinsInModel() throws Exception;
	
	public List<String[]> getEnzymes() throws Exception;
	
	public List<String[]> getDistinctModelProteinAttributes() throws Exception;
	
	public List<ModelProtein> getAllFromProtein() throws Exception;
	
	public Integer getProteinID(String class_, String name) throws Exception;
	
	public long countProteinsComplexes() throws Exception;
	
	public List<String[]> getAllProteins() throws Exception;
	
	public List<String[]> getAllFromProteinComposition() throws Exception;
	
	public Integer getProteinIDFromName(String name) throws Exception;
	
	public int getProteinIDFromNameAndClass(String name, String class_) throws Exception;
	
	public ProteinContainer getProteinData(int id) throws Exception;
	
	public void insertEnzymes(int idProtein, String ecnumber, boolean editedReaction) throws Exception;

	
	public boolean insertProtein(ProteinContainer protein,
			String[] synonyms, String[] enzymes) throws Exception;
	
	public boolean updateProtein(ProteinContainer protein, String[] synonyms,
			String[] oldSynonyms, String[] enzymes, String[] oldEnzymes) throws Exception;

	public int[] countProteins() throws Exception;

	public Integer insertNewProteinEntry(String name, String classString) throws Exception;

	public boolean checkEnzymeInModelExistence(Integer protId, String source) throws Exception;

	public void saveDatabaseStatus(String blastDatabase, Float threshold, Float upperThreshold, Float alpha, Float beta,
			Integer minHits) throws Exception;

	public void commitToDatabase(String blastDatabase, Float threshold, Map<Integer, String[]> annotationEditedEnzymeData,
			Map<Integer, String> annotationEnzymesList, Map<Integer, String[]> annotationEditedProductData,
			Map<Integer, String> annotationProductList, Map<Integer, String> annotationNamesList,
			Map<Integer, String> annotationNotesMap, Map<Integer, String> annotationLocusList, Float upperThreshold,
			Float alpha, Float beta, Integer minHits) throws Exception;

	public void removeEnzymesAssignments(String ecnumber, Boolean[] inModel, List<String> enzymes_ids, Integer proteinID,
			boolean removeReaction) throws Exception;

//	public String[] getECnumber2(int protId) throws Exception;

//	public Pair<List<String>, Boolean[]> getECnumber(int protId) throws Exception;

	public List<Integer> getEnzymesStats(String originalReaction) throws Exception;

	public boolean checkModelEnzymaticCofactorEntry(int compoundID, int protID) throws Exception;

	public long countProteinsTransporters() throws Exception;

	public long countProteinsEnzymesNotLikeSource(String source) throws Exception;

	public List<ProteinContainer> getDataFromEnzyme(Integer pathId) throws Exception;

	public Map<Integer, Long> getProteinsData2() throws Exception;

	public String[][] getProteins() throws Exception;

	public List<String> getECNumbersWithModules() throws Exception;

	public List<ProteinContainer> getEnzymesModel() throws Exception;

	public Map<String, Integer> getEnzymeEcNumberAndProteinID() throws Exception;

	public Map<Integer, List<Integer>> getEnzymesCompartments() throws Exception;

	public String getEnzymeEcNumberByProteinID(Integer proteinId) throws Exception;
	
	public void updateProteinSetEcNumberSourceAndInModel(Integer model_protein_idprotein, String ecnumber, boolean inModel,
			String source) throws Exception;

	public List<ProteinContainer> getProducts() throws Exception;
	
	public ProteinContainer getProteinIdByEcNumber(String ecNumber) throws Exception;
	
	public List<ProteinContainer> getProteinIdByIdgene(Integer idGene) throws Exception;

	public ProteinContainer getProteinEcNumberAndInModelByProteinID(Integer proteinId) throws Exception;

	public void updateProteinSetEcNumber(Integer idProtein, String ecNumber) throws Exception;

	public void removeProtein(Integer proteinId) throws Exception;

	public void insertModelEnzymaticCofactor(int compoundID, int protID) throws Exception;

	public List<ProteinContainer> getEnzymeHasReaction() throws Exception;

	public List<String[]> getProteinComposition() throws Exception;

	public List<CompartmentContainer> getProteinCompartmentsByProteinId(Integer proteinId) throws Exception;

	public Integer insertNewProteinEntry(ProteinContainer protein) throws Exception;

	public List<String[]> getAllEnzymes(boolean isEncodedInGenome, boolean isCompartmentalizedModel) throws Exception;

}
