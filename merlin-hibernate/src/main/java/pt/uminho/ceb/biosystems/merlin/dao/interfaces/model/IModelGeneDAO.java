package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;



public interface IModelGeneDAO extends IGenericDao<ModelGene>{

	public void addModelGene(ModelGene modelGene); 
	
	public void addModelGeneList(List<ModelGene> modelGeneList); 
	
	public List<ModelGene> getAllModelGene(); 
	
	public ModelGene getModelGene(Integer id); 
	
	public void removeModelGene(ModelGene modelGene); 
	
	public void removeModelGeneList(List<ModelGene> modelGeneList); 
	
	public void updateModelGeneList(List<ModelGene> modelGeneList); 
	
	public void updateModelGene(ModelGene modelGene);

	public Integer getGeneIdBySequenceId(String sequenceId);

	public Integer insertModelGene(String name, String locusTag, String origin, String sequenceId,
			String transcriptionDirection, String leftEndPosition, String rightEndPosition, String booleanRule);

	public void updateModelGeneNameBySeqId(String name, String sequenceId);

	public Set<String> getAllDatabaseGenesSeqId();

	public Map<String, Set<String>> getAllSeqIdAndAliasByClassName(String className);

	public Map<String, List<String>> getSeqIdAndECNumber();

	public Map<String, String> getSeqIdAndProteinName();

	public Map<String, Set<String>> getSeqIdAndAlias(String className);

	public Map<String, String> getLocusTagAndECNumber();

	public Integer getGeneIdByLocusTag(String oldLocusTag);

	public void removeModelGeneByGeneId(Integer geneId);

	public void updateModelGeneLocusTagByGeneId(String newLocusTag, Integer idGeneOld);

	public Map<String, String> getModelGeneNameAndLocusTag();

	public Map<String, String> getModelGeneNameAndLocusTagAByProteinId(Integer protId);

	public Map<Integer, String> getModelGeneIdGeneAndLocusTag();

	public Map<Integer, String> getModelGeneIdGeneAndSequenceId();

	public String getLocusTagBySequenceId(String sequenceId);

	public Boolean checkIfIsFilled();

	public List<String[]> getModelGeneAttributesByGeneId(Integer id);

	public Integer getModelGeneCountIdGene();

	public Map<Integer, String> getModelGeneIdGeneAndOriginBySequenceId(String sequence_id);

	public void updateModelGeneOriginBySequenceId(String informationType, String sequence_id);

	public Integer getGeneIdByLocusTagAndSequenceId(String locusTag, String sequence_id);

	public void updateModelGeneLocusTagBySequenceId(String locusTag, String sequence_id);

	public Map<String, Set<String>> getEntryIdAndSequenceId();

	public List<String[]> getModelGeneAttributesGroupedByGeneIdAndLocusTag();

	public List<String[]> getModelGeneAttributesGroupedLocusTag();

	public List<Integer> getModelGeneProteinId(Integer protId);
	
	public void updateModelGeneNameAndLocusTagByGeneId(String name, String locusTag, Integer geneId);
	
	public void updateAllModelGeneNameByGeneId(String name, Integer geneId, String direction, String left, String right,
			String locusTag);

	public List<String> getModelGeneLocusTag();

	public List<String[]> getModelGeneAttributesByEcNumberAndProteinId(String ecNumber, Integer proteinId);

	public Boolean isModelGeneFilled();

	public Integer getGeneIdBySequenceIdAndLocusTag(String sequenceId, String locusTag);

	public Map<String, Integer> getModelSequenceIdAndGeneId();

	public Long countEntriesInGene() throws Exception;

	public Long countGenesWithName();

	public String getModelSequenceByQueryAndSequenceType(String sequenceId, SequenceType sequenceType);

	public Boolean checkModelSequenceType(String sequenceType);

	public Map<String, Integer> getGeneLocusTagAndIdgene();

	public ArrayList<String[]> getAllGenes();
	
	public List<String[]> getGeneData2(String ecnumber, Integer id);

	public Map<String, Set<String>> getQueryAndAliasFromProducts();

	public List<String[]> getEncodingGenes();

	public List<Integer> getModelGenesIDs(boolean encoded);
	
	public Integer countInitialMetabolicGenes();

	public Map<Integer, String> getGeneIdAndGeneQuery();
}
