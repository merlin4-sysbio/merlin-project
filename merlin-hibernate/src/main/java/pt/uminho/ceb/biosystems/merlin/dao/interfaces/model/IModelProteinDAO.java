package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSubunit;



public interface IModelProteinDAO  extends IGenericDao<ModelProtein>{

	public void addModelProtein(ModelProtein modelProtein); 
	
	public void addModelProteinList(List<ModelProtein> modelProteinList); 
	
	public List<ModelProtein> getAllModelProtein(); 
	
	public ModelProtein getModelProtein(Integer id); 
	
	public void removeModelProtein(ModelProtein modelProtein); 
	
	public void removeModelProteinList(List<ModelProtein> modelProteinList); 
	
	public void updateModelProteinList(List<ModelProtein> modelProteinList); 
	
	public void updateModelProtein(ModelProtein modelProtein);

	public List<ModelProtein> getDistinctModelProteinIdAndNameAndClass();

	public List<ModelProtein> getDistinctModelProteinIdAndNameAndInchi();

	public List<ModelProtein> getDistinctModelProteinAttributes();

	public List<ModelProtein> getAllModelProteinByNameAndClass(String name, String class_);

	public List<String[]> getDistinctModelProteinIdAndNameAndClass2();

	public Integer getProteinIdByName(String name);

	public Integer getProteinIdByNameAndClass(String string, String class_);

	public List<ModelProtein> getAllModelProteinById(Integer id);
				
	public List<String[]> getModelProteinAttributesOrderByEcNumber();

	public Integer insertModelProtein(String name, String classe, String inchi, Float molecularWeight,
			Float molecularWeightExp, Float molecularWeightKd, Float molecularWeightSeq, Float pi, String ecnumber,
			String source);

	public String[][] getProteins();

	public Map<Integer, Long> getProteinsData2();

//	public Map<String, Boolean> getModelProteinEcNumberAndInModelByProteinId(Integer protId);

	public List<ModelProtein> getAllModelProteinByAttributes(Integer protId, String source);

	public List<ProteinContainer> getModelProteinAttributes(Integer idPathway);

	public Long countModelProteinDistinctProteinIdNotLikeSource(String source);

	public Long getModelProteinDistinctProteinIdBySource(String source);

	public void removeAllEnzymeByProteinId(Integer protId);

	public Boolean checkEnzymeInModelExistence(Integer protId, String source);

	public void updateProteinSetEcNumberSourceAndInModel(Integer model_protein_idprotein, String ecnumber, String source) throws Exception;

	public List<ModelProtein> getDistincModelProteinAttributesByAtt(boolean distinct);

	public List<String> getECNumbersWithModules();

	public List<ModelSubunit> getSubunitByEcNumber(String ecNumber);

	public List<ProteinContainer> getEnzymesModel();

	public Map<String, Integer> getEnzymeEcNumberAndProteinID();

	public Map<Integer, List<Integer>> getEnzymesCompartments();

	public String getEnzymeEcNumberByProteinID(Integer proteinId);

	public List<ProteinContainer> getGeneQueryAndProteinName();

	public ModelProtein getProteinWithEcnumberNotNullByProteinID(Integer proteinId);
	
	public ProteinContainer getProteinIdByEcNumber(String ecNumber);

	public List<ProteinContainer> getProteinIdByIdgene(Integer idGene);

	public List<ProteinContainer> getEnzymeHasReaction();

	public List<CompartmentContainer> getProteinCompartmentsByProteinId(Integer proteinId);

	public List<String[]> getAllEnzymes(boolean isEncodedInGenome, boolean isCompartmentalizedModel);


}
