package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelAliasesDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelCompartmentDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneHasCompartmentDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelGeneHasOrthologyDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelReactionLabelsDAO;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSubunitDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGene;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartment;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasCompartmentId;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasOrthology;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelGeneHasOrthologyId;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IGenesService;


public class GenesServiceImpl implements IGenesService{


	private IModelGeneDAO modelgeneDAO;
	private IModelGeneHasCompartmentDAO genehascompartDAO;
	private IModelSubunitDAO subunitDAO;
	private IModelAliasesDAO aliasDAO;
	private IModelCompartmentDAO compartmentDAO;
	private IModelReactionDAO reactionDAO;
	private IModelReactionLabelsDAO modelReactionLabelsDAO;
	private IModelReactionHasModelProteinDAO modelReactionHasModelProteinDAO;
	private IModelProteinDAO modelproteinDAO;
	private  IModelGeneHasOrthologyDAO modelGeneHasOrthologyDAO;


	public GenesServiceImpl(IModelGeneDAO modelgeneDAO, IModelGeneHasCompartmentDAO genehascompartDAO,
			IModelSubunitDAO subunitDAO, IModelAliasesDAO aliasDAO,
			IModelCompartmentDAO compartmentDAO, 
			IModelReactionDAO reactionDAO, IModelReactionLabelsDAO modelReactionLabelsDAO,
			IModelReactionHasModelProteinDAO modelreactionhasproteinDAO, IModelProteinDAO modelproteinDAO, IModelGeneHasOrthologyDAO modelGeneHasOrthologyDAO) {
		this.modelgeneDAO  = modelgeneDAO;
		this.genehascompartDAO = genehascompartDAO;
		this.subunitDAO = subunitDAO;
		this.aliasDAO = aliasDAO;
		this.compartmentDAO = compartmentDAO;
		this.reactionDAO = reactionDAO;
		this.modelReactionLabelsDAO = modelReactionLabelsDAO;
		this.modelReactionHasModelProteinDAO = modelreactionhasproteinDAO;
		this.modelproteinDAO = modelproteinDAO;
		this.modelGeneHasOrthologyDAO = modelGeneHasOrthologyDAO;
	}

	private static GeneContainer buildGeneContainer(ModelGene gene) {

		GeneContainer container =  new GeneContainer(gene.getIdgene());
		container.setName(gene.getName());
		container.setTranscriptionDirection(gene.getTranscriptionDirection());
		container.setLeft_end_position(gene.getLeftEndPosition());
		container.setRight_end_position(gene.getRightEndPosition());
		container.setBoolenaRule(gene.getBooleanRule());
		container.setLocusTag(gene.getLocusTag());
		container.setExternalIdentifier(gene.getQuery());

		return container;
	}

	public Integer loadGene(String sequenceId, String name, String locusTag, String origin, 
			String transcriptionDirection, String leftEndPosition, String rightEndPosition, String booleanRule) {

		Integer geneId = this.modelgeneDAO.getGeneIdBySequenceId(sequenceId);

		if (geneId == null) { 
			geneId = this.modelgeneDAO.insertModelGene(name, locusTag, origin, sequenceId, transcriptionDirection, 
					leftEndPosition, rightEndPosition, booleanRule);
		}

		if (name != null) {
			this.modelgeneDAO.updateModelGeneNameBySeqId(name, sequenceId);
		}

		return (Integer) geneId;
	}

	public Set<String> getAllDatabaseGenes(){
		return this.modelgeneDAO.getAllDatabaseGenesSeqId();
	}


	public Map<String, Set<String>> getGeneNamesAliases(String className){
		return this.modelgeneDAO.getAllSeqIdAndAliasByClassName(className);
	}


	public Map<String, List<String>> getECNumbers(){
		return this.modelgeneDAO.getSeqIdAndECNumber();
	}


	public Map<String, String> getSeqIdAndProteinName() throws Exception {		
		return this.modelgeneDAO.getSeqIdAndProteinName();
	}


	public Map<String, Set<String>> getSeqIdAndAlias(String className) throws Exception {
		return this.modelgeneDAO.getSeqIdAndAlias(className);
	}

	@Override
	public List<CompartmentContainer> getCompartmentsRelatedToGene(Integer idGene) throws Exception {
		List<ModelGeneHasCompartment> res =  this.genehascompartDAO.getAllModelGeneHasCompartmentByGeneId(idGene);

		List<CompartmentContainer> containers = new ArrayList<>();

		if(res != null) {
			for(ModelGeneHasCompartment geneHasComp : res) {
				CompartmentContainer container = new CompartmentContainer(geneHasComp.getModelCompartment().getIdcompartment());

				if(geneHasComp.getPrimaryLocation() == null)
					container.setPrimaryLocation(false);
				else
					container.setPrimaryLocation(geneHasComp.getPrimaryLocation());
				
				container.setScore(Double.valueOf(geneHasComp.getScore()));
				container.setPrimaryLocation(geneHasComp.getPrimaryLocation());

				containers.add(container);
			}
		}

		return containers;
	}
	
	@Override
	public Map<Integer, List<CompartmentContainer>> getCompartmentsRelatedToGenes() throws Exception {
		
		Map<Integer, List<CompartmentContainer>> results = new HashMap<>();
		
		List<ModelGeneHasCompartment> res =  this.genehascompartDAO.getAllModelGeneHasCompartment();

		if(res != null) {
			for(ModelGeneHasCompartment geneHasComp : res) {
				
				Integer geneId = geneHasComp.getId().getModelGeneIdgene();
				ModelCompartment compartment = geneHasComp.getModelCompartment();
				
				CompartmentContainer container = new CompartmentContainer(compartment.getIdcompartment(),
						compartment.getName(), compartment.getAbbreviation());

				if(geneHasComp.getPrimaryLocation() == null)
					container.setPrimaryLocation(false);
				else
					container.setPrimaryLocation(geneHasComp.getPrimaryLocation());
				
				container.setScore(Double.valueOf(geneHasComp.getScore()));
				container.setPrimaryLocation(geneHasComp.getPrimaryLocation());
				
				if(!results.containsKey(geneId))
					results.put(geneId, new ArrayList<>());
				
				results.get(geneId).add(container);
			}
		}

		return results;
	}

	public boolean isGeneCompartmentLoaded(Integer idGene) throws Exception {
		List<ModelGeneHasCompartment> res = this.genehascompartDAO.getAllModelGeneHasCompartmentByGeneId(idGene);

		if (res != null) {
			return true;
		}

		else {
			return false;
		}	
	}

	public Map<String, List<String>> getECNumbers2() throws Exception {
		Map<String, List<String>> ec_numbers = new HashMap<String, List<String>>();

		Map<String,String> res = this.modelgeneDAO.getLocusTagAndECNumber();

		if (res != null){

			List<String> genes = new ArrayList<String>();
			for (String key : res.keySet()) {
				String gene = key;
				String enzyme = res.get(key);

				if(ec_numbers.containsKey(enzyme))
					genes = ec_numbers.get(enzyme);

				genes.add(gene);

				ec_numbers.put(enzyme, genes);
			}
		}
		return ec_numbers;
	}



	public void updateLocusTag(String oldLocusTag, String newLocusTag) throws Exception {   
		Integer idGeneOld = this.modelgeneDAO.getGeneIdByLocusTag(oldLocusTag);
		Integer idGeneNew = this.modelgeneDAO.getGeneIdByLocusTag(newLocusTag);

		if (idGeneNew != null) {
			this.subunitDAO.updateSubunitGeneIdWithLocusTagId(idGeneNew, idGeneOld);
			this.modelgeneDAO.removeModelGeneByGeneId(idGeneOld);
		}
		else {
			this.modelgeneDAO.updateModelGeneLocusTagByGeneId(newLocusTag, idGeneOld);
		}

		this.aliasDAO.insertModelAlias("g", idGeneOld, oldLocusTag, null);

	}


	public List<String[]> getGeneData2(String ecnumber, Integer id) throws Exception {
		return this.modelgeneDAO.getGeneData2(ecnumber, id);
	}


	public ArrayList<String> getGenesModel() throws Exception { //VERFICIAR
		ArrayList<String> lls = new ArrayList<String>();
		Map<String,String> res = this.modelgeneDAO.getModelGeneNameAndLocusTag();

		if (res != null)
			for (String x : res.keySet()) {
				if(x == null || x.trim().isEmpty())
					lls.add(res.get(x));
				else
					lls.add(x+"_"+res.get(x));
			}
		return lls;
	}


	public List<String[]> getGeneInfo(Integer protId) throws Exception {

		Map<String,String> list = this.modelgeneDAO.getModelGeneNameAndLocusTagAByProteinId(protId);
		List<String[]> res = new ArrayList<String[]>();

		if (list != null && list.size()>0) {
			String[] result = new String[2];

			result[0] = list.get("name");
			result[1] = list.get("locusTag");

			res.add(result);
		}
		return res;	
	}


	public Map<Integer, String> getGeneIdLocusTag() throws Exception {
		return this.modelgeneDAO.getModelGeneIdGeneAndLocusTag();
	}

	public Map<Integer, String> getGeneIds() throws Exception {
		return this.modelgeneDAO.getModelGeneIdGeneAndSequenceId();
	}

	@Override
	public Map<String, Integer> getGeneIdByQuery() throws Exception {
		return this.modelgeneDAO.getModelSequenceIdAndGeneId();
	}

	public String getGeneId(String sequenceId) throws Exception {
		return this.modelgeneDAO.getLocusTagBySequenceId(sequenceId);
	}
	
	public List<ModelGene> getGenesID() throws Exception {
		return this.modelgeneDAO.getAllModelGene();
	}



	public Map<String, Integer> getQueriesByGeneId() throws Exception {
		return this.modelgeneDAO.getModelSequenceIdAndGeneId();
	}

	public boolean checkGenes() throws Exception {
		return this.modelgeneDAO.checkIfIsFilled();
	}


	public List<String[]> getDataFromGene(Integer id) throws Exception {
		return this.modelgeneDAO.getModelGeneAttributesByGeneId(id);
	}


	public int getGeneIDByLocusTag(String locusTag) throws Exception {
		return this.modelgeneDAO.getGeneIdByLocusTag(locusTag);
	}


	public long countGenesInGeneHasCompartment() throws Exception {
		Long res = (long) 0;

		Long result = this.genehascompartDAO.getModelGeneHasCompartmentDistinctGeneId();
		if (result != null)
			res = result;
		return res;
	}


	public int countGenes() throws Exception {
		int res = 0;

		Integer result = this.modelgeneDAO.getModelGeneCountIdGene();
		if (result != null)
			res = result;

		return res;
	}


	public void loadGenesCompartments(Integer idGene, Map<String, Integer> compartmentsDatabaseIDs,
			Integer primaryCompartment, String scorePrimaryCompartment, Map<String, String> secondaryCompartmens,
			boolean primLocation)
					throws Exception {

		List<Integer> res1 = this.genehascompartDAO.getIdByGeneIdAndPrimaryLocation(idGene, primLocation);

		if (res1 != null) {
			this.genehascompartDAO.insertModelGeneHasCompartment(idGene, primaryCompartment, primLocation, scorePrimaryCompartment);
		}

		List<String> compartments = new ArrayList<String>();

		for(String loc : secondaryCompartmens.keySet())
			compartments.add(loc);

		for(String compartment:compartments) {

			List<Integer> res3 = this.genehascompartDAO.getIdByGeneIdAndPrimaryLocation(idGene, primLocation);

			if(res3 == null)
				this.genehascompartDAO.insertModelGeneHasCompartment(idGene, compartmentsDatabaseIDs.get(compartment), false, secondaryCompartmens.get(compartment));
		}

	}



	public Map<String, Integer> getCompartmentsDatabaseIDs(String primaryCompartment, String primaryCompartmentAbb,
			Map<String, Double> secondaryCompartmens, Map<String, String> secondaryCompartmensAbb,
			Map<String, Integer> compartmentsDatabaseIDs, String name) throws Exception {

		List<String> compartments = new ArrayList<String>();
		compartments.add(primaryCompartment);

		for(String loc : secondaryCompartmens.keySet())
			compartments.add(loc);

		for(String compartment:compartments) {

			String abb = primaryCompartmentAbb;

			if(secondaryCompartmensAbb.containsKey(compartment))
				abb = secondaryCompartmensAbb.get(compartment);

			abb = abb.toUpperCase();

			if(!compartmentsDatabaseIDs.containsKey(compartment)) {

				ModelCompartment modelCompartment = this.compartmentDAO.getCompartmentByCompartmentName(name);

				Integer id = null;

				if(modelCompartment == null){

					id = this.compartmentDAO.insertNameAndAbbreviation(name, abb);
				}
				else {
					id = modelCompartment.getIdcompartment();
				}

				compartmentsDatabaseIDs.put(compartment, id);

			}
		}
		return compartmentsDatabaseIDs;
	}


	public Integer loadGene(String locusTag, String query, String geneName, String direction,
			String left_end, String right_end, String informationType) throws Exception {

		Map<Integer, String> idgeneMap = this.modelgeneDAO.getModelGeneIdGeneAndOriginBySequenceId(query);

		Integer geneID = null;

		if(idgeneMap != null && idgeneMap.size()>0) {

			geneID = (Integer) idgeneMap.keySet().toArray()[0];
			String informationType_db = idgeneMap.get(geneID);


			if(!informationType.equalsIgnoreCase(informationType_db))
				this.modelgeneDAO.updateModelGeneOriginBySequenceId(informationType, query);

			Integer query2 = this.modelgeneDAO.getGeneIdByLocusTagAndSequenceId(locusTag, query);

			if(query2 == null && !locusTag.equalsIgnoreCase(query))
				this.modelgeneDAO.updateModelGeneLocusTagBySequenceId(locusTag, query);	
		}
		else {

			geneID = this.modelgeneDAO.insertModelGene(null, locusTag, informationType, query, direction, left_end, right_end, null);
		}

		if(geneName!=null)
			this.modelgeneDAO.updateModelGeneNameBySeqId(geneName, query);

		return geneID;
	}


	public Map<String, Set<String>> getSequenceIds() throws Exception {
		return this.modelgeneDAO.getEntryIdAndSequenceId();
	}


	public List<String[]> getAllGenes() throws Exception {
		return this.modelgeneDAO.getModelGeneAttributesGroupedByGeneIdAndLocusTag();
	}


	public List<String[]> getRegulatoryGenes() throws Exception {
		return this.subunitDAO.getModelSubunitAttributesOrderByName();
	}


	public List<String[]> getEncodingGenes() throws Exception {
		return this.modelgeneDAO.getEncodingGenes();
	}


	public Map<Integer, Integer> getProteins(Integer protId) throws Exception {

		Map<Integer,Integer> index = new HashMap<Integer,Integer>();

		List<Integer> res = this.modelgeneDAO.getModelGeneProteinId(protId);

		if (res != null)
			for (Integer x : res){

				if(!index.containsKey(x)) {

					index.put(x, Integer.valueOf(1));
				}
				else {

					Integer ne = Integer.valueOf(index.get(x).intValue() + 1);
					index.put(x, ne);
				}
			}
		return index;
	}

	@Override
	public Integer insertNewGene(String locusTag, String name, String query, String origin) {

		ModelGene gene = new ModelGene();
		gene.setLocusTag(locusTag);
		gene.setName(name);
		gene.setQuery(query);
		gene.setOrigin(origin);

		return (Integer) this.modelgeneDAO.save(gene);

	}
	
	@Override
	public Integer insertNewGene(GeneContainer container) {

		ModelGene gene = new ModelGene();
		gene.setLocusTag(container.getLocusTag());
		gene.setName(container.getName());
		gene.setQuery(container.getExternalIdentifier());
		gene.setOrigin(container.getOrigin().toString());
		gene.setLeftEndPosition(container.getLeft_end_position());
		gene.setRightEndPosition(container.getRight_end_position());
		gene.setTranscriptionDirection(container.getTranscriptionDirection());
		
		return (Integer) this.modelgeneDAO.save(gene);

	}

	@Override
	public void insertNewGene(String name, String transcription_direction, String left_end_position,
			String right_end_position, String[] subunits, String locusTag) {

		Integer idNewGene = null; 

		if(left_end_position.equals("")) {

			//				sequence id does not have default value when inserting new gene.
			//				has id or search it in the homology table or in the fasta Files.class
			//				when annotation is loaded all genes are loaded
			//				so inserting a new gene will only be present in the enzyme annotation data
			//				if gene is not in the other table set  its compartment to cytosol compartmentalization

			idNewGene = this.modelgeneDAO.insertModelGene(name, locusTag, "MANUAL", locusTag, null, null, null, null);

		}			
		else {
			idNewGene = this.modelgeneDAO.insertModelGene(name, locusTag, "MANUAL", locusTag, transcription_direction, left_end_position, right_end_position, null);

		}


		for(int s=0; s<subunits.length;s++) {

			if(!subunits[s].equals("dummy") && !subunits[s].isEmpty()) {


				String proteinID=subunits[s].split("__")[0];

				this.subunitDAO.insertModelSubunit(idNewGene, Integer.valueOf(proteinID));

				//	Set<Integer> reactionsIDs = ProjectAPI.getReactionID2(proteinID, e, stmt); -// nao tinha a view, nao fiz DAO

				//					for(Integer idreaction: reactionsIDs) {
				//						
				//						this.modelReaction.updateInModelAndSourceByReactionId(idreaction, true, "MANUAL");
				//	
				//					}
			}
		}
	}
	
	@Override
	public void updateGene(GeneContainer gene) {
		
		ModelGene modelGene = this.modelgeneDAO.getModelGene(gene.getIdGene());
		
		if(modelGene != null) {
			
			if(gene.getLocusTag() != null)
				modelGene.setLocusTag(gene.getLocusTag());
			
			if(gene.getName() != null)
				modelGene.setName(gene.getName());
			
			if(gene.getOrigin() != null)
				modelGene.setOrigin(gene.getOrigin().toString());
			
			this.modelgeneDAO.update(modelGene);
		}
	}

	@Override
	public void updateGene(int geneIdentifier, String name, String transcription_direction, String left_end_position,
			String right_end_position, String[] subunits, String[] oldSubunits, String locusTag) throws Exception {

		if(left_end_position.equals("")) {
			this.modelgeneDAO.updateModelGeneNameAndLocusTagByGeneId(name, locusTag, geneIdentifier);
		}			
		else {
			this.modelgeneDAO.updateAllModelGeneNameByGeneId(name, geneIdentifier, transcription_direction, left_end_position, right_end_position, locusTag);
		}

		List<String> old_protein_ids = new ArrayList<String>();
		List<String> protein_ids = new ArrayList<String>();

		int i = 0;
		for(String id : oldSubunits) {

			old_protein_ids.add(i,id);
			i++;
		}

		i = 0;
		for(String id : subunits) {

			protein_ids.add(i,id);
			i++;
		}

		List<String> subunit_protein_id_add = new ArrayList<String>();

		for(String id : protein_ids) {

			if(!id.contains("dummy") && !id.isEmpty()) {

				if(old_protein_ids.contains(id)) {

					old_protein_ids.remove(id);
				}
				else {

					subunit_protein_id_add.add(id);
				}
			}
		}

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		for(String id : old_protein_ids) {

			this.removeGeneAssignemensts(geneIdentifier, id);
		}

		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		for(String protein_id_ec : protein_ids) {

			if(subunit_protein_id_add.contains(protein_id_ec)) {

				int protein_id = Integer.parseInt(protein_id_ec.split("__")[0]);
				String ecnumber=protein_id_ec.split("__")[1];

				this.subunitDAO.insertModelSubunit(geneIdentifier, protein_id);

				this.insertEnzymes(protein_id, ecnumber, true); 

			}
		}
	}


	public void insertEnzymes(int idProtein, String ecnumber, boolean editedReaction) throws Exception {

		String source = (editedReaction) ? "MANUAL" : null; 

		ModelProtein enzyme = this.modelproteinDAO.getModelProtein(idProtein);
		if(enzyme == null) {
			this.modelproteinDAO.updateProteinSetEcNumberSourceAndInModel(idProtein, ecnumber, true, source);
		}else {
			enzyme.setInModel(true);
			if(editedReaction)
				enzyme.setSource("MANUAL");
			this.modelproteinDAO.update(enzyme);
		}

		List<ModelReactionHasModelProtein> reactions = this.modelReactionHasModelProteinDAO.getAllModelReactionHasModelProtein();

		for(ModelReactionHasModelProtein enzymeReactions :reactions) {

			ModelReaction reaction = reactionDAO.findById(enzymeReactions.getId());
			ModelReactionLabels reactionLabel = reaction.getModelReactionLabels();

			if(editedReaction) 
				reactionLabel.setSource("MANUAL");

			reaction.setInModel(true);

			this.reactionDAO.update(enzymeReactions);
		}

	}

	public void removeGeneAssignemensts(Integer geneIdentifier, String proteindIdentifier) throws Exception {

		this.subunitDAO.removeSubunitByGeneIdAndProteinId(geneIdentifier, Integer.valueOf(proteindIdentifier.split("__")[0]));


		boolean exists = this.subunitDAO.checkSubunitData(Integer.valueOf(proteindIdentifier));

		if(!exists) {

			List<String> enzymes_ids = new ArrayList<String>();
			enzymes_ids.add(proteindIdentifier.split("__")[1]);

			Boolean[] inModel = new Boolean[enzymes_ids.size()];
			for(int i= 0; i< inModel.length; i++) {

				inModel[i]=false;
			}


			for(String e:enzymes_ids) {
				this.removeEnzymesAssignments(String.valueOf(e), inModel, enzymes_ids, Integer.parseInt(proteindIdentifier.split("__")[0]), false);

			}
		}
	}

	@Override
	public int getGeneLastInsertedID() throws Exception {

		List<ModelGene> genes = this.modelgeneDAO.getAllModelGene();

		int max = -1;

		for(ModelGene gene: genes) {

			if(gene.getIdgene()>max)
				max = gene.getIdgene();
		}

		return max;
	}

	public void removeModelGeneByGeneId(Integer geneId) throws Exception{
		this.modelgeneDAO.removeModelGeneByGeneId(geneId);
	}

	private void removeEnzymesAssignments(String ecnumber, Boolean[] inModel, List<String> enzymes_ids, Integer proteinID, boolean removeReaction) throws Exception {

		if(removeReaction) {

			this.modelproteinDAO.removeAllEnzymeByProteinId(proteinID);
		}
		else {
			this.modelproteinDAO.updateProteinSetEcNumberSourceAndInModel(proteinID, ecnumber, false, null);

		}

		List<Integer> reactionsIDs = this.reactionDAO.getDistinctReactionsByEnzymeAndCompartmentalized(proteinID, false, false);
		//reactionsIDs = ModelAPI.getReactionsIDs2(reactionsIDs, proteinID, ec); //nao tenho a tabela

		for(int idreaction: reactionsIDs) {

			List<String[]> proteins_array = new ArrayList<String[]>();

			List<String[]> result = this.modelReactionHasModelProteinDAO.getModelReactionHasModelProteinProteinIdAndEcNumberByreactionId(idreaction);

			for(int i=0; i<result.size(); i++){
				String[] list = result.get(i);

				if(Integer.parseInt(list[0])==proteinID && ecnumber.equalsIgnoreCase(list[1])) {}
				else {

					if(Integer.parseInt(list[0])==proteinID && enzymes_ids.contains(list[1])) {

						if(inModel[enzymes_ids.indexOf(list[1])]) {

							proteins_array.add(new String[] {list[0],list[1]});
						}
					}
					else {

						proteins_array.add(new String[] {list[0],list[1]});
					}
				}
			}

			if(proteins_array.isEmpty()) {
				this.reactionDAO.updateInModelByReactionId(idreaction, false);

				int reactionLabelId = reactionDAO.getModelReactionLabelByReactionId(idreaction);
				this.modelReactionLabelsDAO.updateSourceByReactionLabelId(reactionLabelId, "MANUAL");

			}
		}
	}

	@Override
	public void updateGenesLocusByQuery(String query, String locus) throws Exception {
		Integer geneId = this.modelgeneDAO.getGeneIdBySequenceId(query);
		if(geneId != null) {
			ModelGene geneModel = this.modelgeneDAO.findById(geneId);
			geneModel.setLocusTag(locus);
			this.modelgeneDAO.update(geneModel);
		}
	}

	@Override
	public long countEntriesInGene() throws Exception {

		return this.modelgeneDAO.countEntriesInGene();
	}

	@Override
	public long countGenesWithName() throws Exception {
		return this.modelgeneDAO.countGenesWithName();
	}


	@Override
	public void insertModelGeneHasCompartment(boolean PrimaryLocation, Double score, Integer model_compartment_idcompartment, Integer model_gene_idgene) throws Exception {

		ModelGeneHasCompartment genehascompartment = new ModelGeneHasCompartment();
		ModelGeneHasCompartmentId id = new ModelGeneHasCompartmentId(model_compartment_idcompartment, model_gene_idgene);

		genehascompartment.setId(id);
		genehascompartment.setPrimaryLocation(PrimaryLocation);
		if(score != null)
			genehascompartment.setScore(score.toString());

		this.genehascompartDAO.addModelGeneHasCompartment(genehascompartment);

	}

	@Override
	public Boolean checkModelSequenceType(String sequenceType) {
		return this.modelgeneDAO.checkModelSequenceType(sequenceType);
	}

	@Override
	public GeneContainer getGeneDataById(Integer id) throws Exception {

		ModelGene gene = this.modelgeneDAO.getModelGene(id);

		if(gene == null) 
			return null;

		return buildGeneContainer(gene);
	}

	@Override
	public List<GeneContainer> getAllGeneData() throws Exception {

		List<ModelGene> genes = this.modelgeneDAO.getAllModelGene();

		List<GeneContainer> containers = new ArrayList<>();

		for(ModelGene gene : genes) {

			if(gene != null) {
				containers.add(buildGeneContainer(gene));
			}
		}

		return containers;
	}

	@Override
	public Map<Integer, GeneContainer> getAllGeneDatabyIds() throws Exception {

		List<ModelGene> genes = this.modelgeneDAO.getAllModelGene();

		Map<Integer, GeneContainer> containers = new HashMap<>();

		for(ModelGene gene : genes) {

			if(gene != null) {
				containers.put(gene.getIdgene(), buildGeneContainer(gene));
			}
		}

		return containers;
	}

	@Override
	public Map<String, Integer> getGeneLocusTagAndIdgene() {
		return this.modelgeneDAO.getGeneLocusTagAndIdgene();
	}

	@Override
	public 	ArrayList<String[]> getAllGenes2() {
		return this.modelgeneDAO.getAllGenes();
	}

	@Override
	public 	Map<String, Set<String>> getQueryAndAliasFromProducts() {
		return this.modelgeneDAO.getQueryAndAliasFromProducts();
	}

	@Override
	public String getGeneNameById(Integer id) {

		ModelGene gene = this.modelgeneDAO.getModelGene(id);

		if(gene != null)
			return gene.getName();

		return null;

	}

	@Override
	public void insertModelGeneHasOrthology(Integer geneId, Integer orthologyId, Double score) {

		ModelGeneHasOrthology orth = new ModelGeneHasOrthology();

		ModelGeneHasOrthologyId id = new ModelGeneHasOrthologyId(orthologyId, geneId);

		orth.setId(id);
		
		if(score != null)
			orth.setSimilarity(score.floatValue());

		this.modelGeneHasOrthologyDAO.save(orth);
	}

	@Override
	public void updateModelGeneEndPositions(Integer geneId, String leftEndPosition, String rightEndPosition)  {

		ModelGene gene = this.modelgeneDAO.getModelGene(geneId);

		if(leftEndPosition != null)
			gene.setLeftEndPosition(leftEndPosition);

		if(rightEndPosition != null)
			gene.setRightEndPosition(rightEndPosition);

		this.modelgeneDAO.update(gene);
	}

	@Override
	public GeneContainer getModelGeneByQuery(String query)  {

		ModelGene gene = this.modelgeneDAO.findUniqueByAttribute("query", query);

		if(gene == null)
			return null;

		return buildGeneContainer(gene);
	}

	@Override
	public List<Integer> getModelGenesIDs(boolean encoded)  {

		return this.modelgeneDAO.getModelGenesIDs(encoded);

	}

	@Override
	public Integer countInitialMetabolicGenes() throws Exception {
		
		return this.modelgeneDAO.countInitialMetabolicGenes();
		
	}
	
	@Override
	public void removeAllFromModelGeneHasCompartment() throws Exception {
		
		this.genehascompartDAO.removeAllFromModelGeneHasCompartment();
		
	}
}
