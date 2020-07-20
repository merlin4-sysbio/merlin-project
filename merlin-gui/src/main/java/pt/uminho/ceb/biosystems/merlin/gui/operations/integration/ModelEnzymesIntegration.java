package pt.uminho.ceb.biosystems.merlin.gui.operations.integration;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.IIntegrateData;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.ExportType;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.IntegrationType;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SourceType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationEnzymesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.IntegrationConflictsGUI;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.AIBenchUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.IntegrationReport;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.annotation.AnnotationEnzymesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelEnzymesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelPathwaysServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.loaders.ModelDatabaseLoadingServices;

/**
 * @author ODias
 *
 * Integrate homology data with the data from the local database
 *
 */
@Operation(name="integrate homology data", description="integrate homology to database")
public class ModelEnzymesIntegration implements IIntegrateData {

	final static Logger logger = LoggerFactory.getLogger(ModelEnzymesIntegration.class);

	private static boolean INTEGRATE_FULL = true, INTEGRATE_PARTIAL = true, PROCESS_PROTEIN_NAMES = false;

	private AnnotationEnzymesAIB homologyDataContainer;
	private Map<String, String> homologyProduct;
	private Map<String, String> homologyName;
	private Map<String, Set<String>> homologyEnzymes;
	private Map<String, String> integratedProduct;
	private Map<String, String> integratedName;
	private Map<String, Set<String>> integratedEnzymes;
	private Map<String, String> productNames;
	private Map<String, String> geneNames;
	private Map<String, Set<String>> enzymes;	
	private boolean existshomologyInstance;
	private Map<String, String> newNameConflicts;
	private Map<String, String[]> nameConflictsDatabase, nameConflictsHomology;
	private Map<String, String> oldLocusNewTag;
	private IntegrationReport iReport;
	private Map<String,List<String>> allGeneNames;
	private Map<String,List<String>> existingECNumbers;
	private Map<String,Set<String>> allPathways;
	private Map<String,List<String>> allProteinNames;
	private Map<String,Set<String>> existsPathway;
	private Map<String, String> homologyLocusTags;
	private IntegrationType integrationNames;
	private IntegrationType integrationProducts;
	private IntegrationType integrationEnzymes;
	private String workspaceName;
	private File directory;

	private ExportType reportOrIntegration;

	@Port(direction=Direction.INPUT, name="select workspace", validateMethod = "checkProject", order=1)
	public void setProject(WorkspaceAIB project) {

	}

	/**
	 * @param project
	 */
	public void checkProject(WorkspaceAIB project) {

		if(project==null)
			throw new IllegalArgumentException("please select a workspace.");
		else
			this.workspaceName = project.getName();

		this.homologyDataContainer = (AnnotationEnzymesAIB) AIBenchUtils.getEntity(this.workspaceName, AnnotationEnzymesAIB.class);
	}

	@Port(direction=Direction.INPUT,  advanced = true, name="names integration",order=2)
	public void setNames(IntegrationType integrationNames){
		this.integrationNames = integrationNames;
	};

	@Port(direction=Direction.INPUT,  advanced = true, name="enzymes integration",order=3)
	public void setEnzymes(IntegrationType integrationEnzymes){
		this.integrationEnzymes = integrationEnzymes;
	};

	@Port(direction=Direction.INPUT, advanced = true, name="products integration", order=4)
	public void setProducts(IntegrationType integrationProducts){
		this.integrationProducts = integrationProducts;
	};


	@Port(direction=Direction.INPUT, name="report or integration", validateMethod = "checkOption", order=8)
	public void reportsOrIntegration(ExportType reportOrIntegration) {
		
		this.reportOrIntegration = reportOrIntegration;
	}
	
	public void checkOption(ExportType reportOrIntegration) {
		
		this.reportOrIntegration = reportOrIntegration;
	}
	
	/**
	 * @param directory
	 * @throws Exception 
	 */
	@Port(direction=Direction.INPUT,  advanced = true, allowNull = true, name="directory:",description="folder", validateMethod="checkDirectory", order=6)
	public void selectDirectory(File directory) throws Exception{

		this.homologyLocusTags = new TreeMap<String, String>();
		this.homologyName = new TreeMap<String, String>();
		this.homologyProduct = new TreeMap<String, String>();
		this.homologyEnzymes = new TreeMap<String, Set<String>>();
		this.integratedProduct = new TreeMap<String, String>();
		this.integratedEnzymes = new TreeMap<String, Set<String>>();
		this.integratedName = new TreeMap<String, String>();
		this.productNames = new TreeMap<String, String>();
		this.enzymes = new TreeMap<String, Set<String>>();
		this.geneNames = new TreeMap<String, String>();
		this.newNameConflicts = new TreeMap<String, String>();

		Map<Integer, String> queriesList = AnnotationEnzymesServices.getGeneHomologyQueriesBySKey(this.workspaceName);

		for(Integer row : homologyDataContainer.getIntegrationLocusList().keySet()) {

			String locusTag = homologyDataContainer.getIntegrationLocusList().get(row);
			int tableIndex = (homologyDataContainer.getKeys().get(row));
			String sequence_id = queriesList.get(tableIndex);
			this.homologyLocusTags.put(sequence_id, locusTag);				

			if(homologyDataContainer.getIntegrationNamesList().containsKey(row))
				this.homologyName.put(sequence_id,homologyDataContainer.getIntegrationNamesList().get(row));

			if(homologyDataContainer.getIntegrationProdItem().containsKey(row) && !homologyDataContainer.getIntegrationProdItem().get(row).isEmpty())
				this.homologyProduct.put(sequence_id,homologyDataContainer.getIntegrationProdItem().get(row));

			if(homologyDataContainer.getIntegrationEcItem().containsKey(row) && !homologyDataContainer.getIntegrationEcItem().get(row).isEmpty())
				this.homologyEnzymes.put(sequence_id, new HashSet<String>(Arrays.asList(homologyDataContainer.getIntegrationEcItem().get(row).split(", "))));
		}

		this.setExistshomologyInstance(true);
		this.iReport = new IntegrationReport();

		if(performIntegration()) {

			try {
				if(this.reportOrIntegration.equals(ExportType.INTEGRATION)) {

					loadLocalDatabase(PROCESS_PROTEIN_NAMES);
					generateReports();
					MerlinUtils.updateAllViews(homologyDataContainer.getWorkspace().getName());
				}
				else {

					//reports generated separately because there is some error when trying to produce them before the actual integration 
					//(some genes assigned with enzymes loose their assignment ex: KLLA0A00759g	[3.5.4.4] looses the enzymes after some operations- uncomment System.out.println() on IntegrateBLASTData)
					generateReports();
					Workbench.getInstance().info("reports generated!");
				}
			} 
			catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}
		}
		else {

			Workbench.getInstance().warn("an error occurred while performing the integration!");
		}
	}

	/**
	 * @param directory
	 */
	public void checkDirectory(File directory) {

		if(directory != null) {

			if(directory.toString().isEmpty()) {

				throw new IllegalArgumentException("please select a directory!");
			}
			else {

				if(directory.isDirectory())
					this.directory = directory;
				else
					this.directory = directory.getParentFile();	
			}
		}
	}

	/**
	 * Performs the data integration
	 */
	public boolean performIntegration() {

		Map<String,String> existingNames = new TreeMap<String, String>();
		Map<String, Set<String>> existingGeneNamesAlias = new TreeMap<String, Set<String>>();

		Map<String,String> existingProducts = new TreeMap<String, String>();
		Map<String, Set<String>> existingProductsAlias = new TreeMap<String, Set<String>>();

		this.existingECNumbers = new HashMap<String, List<String>>();
		this.existsPathway = new TreeMap<String, Set<String>>();
		this.allPathways = new TreeMap<String, Set<String>>();
		Set<String> enzymes  = new TreeSet<String>();

		try {

			existingGeneNamesAlias = ModelGenesServices.getGeneNamesAliases(this.workspaceName, "g");

			this.existingECNumbers = ModelEnzymesServices.getECNumbers(this.workspaceName);
			
			List<ProteinContainer> products = ModelProteinsServices.getProducts(this.workspaceName);
					
			Map<String, String> finalproducts= new HashMap<> ();
			
			for(ProteinContainer item : products) {
				finalproducts.put(item.getQuery(),item.getName());
			}
				
			existingProducts = finalproducts;

			existingProductsAlias = ModelGenesServices.getQueryAndAliasFromProducts(this.workspaceName);

			this.allPathways = ModelPathwaysServices.getAllPathways(this.workspaceName);

			this.existsPathway = ModelPathwaysServices.getEnzymesPathways(this.workspaceName);

			this.oldLocusNewTag = new TreeMap<String, String>();

		}
		catch (Exception e) {

			logger.error("Stack trace ",e);
			return false;
		}

		this.nameConflictsDatabase = new TreeMap<String, String[]>();
		this.nameConflictsHomology = new TreeMap<String, String[]>();

		//////////////////////////////////////////////////////////////////////////////Pre-process


		Set<String> oldList = new TreeSet<String>(existingGeneNamesAlias.keySet());

		for(String key: oldList) {

			if(this.oldLocusNewTag.containsKey(key)) {

				existingNames.put(this.oldLocusNewTag.get(key), existingNames.get(key));
				existingNames.remove(key);
			}
		}

		oldList = new TreeSet<String>(existingECNumbers.keySet());
		for(String key: oldList) {

			if(this.oldLocusNewTag.containsKey(key)) {

				existingECNumbers.put(this.oldLocusNewTag.get(key), existingECNumbers.get(key));
				existingECNumbers.remove(key);
			}
		}

		oldList = new TreeSet<String>(existingProducts.keySet());
		for(String key: oldList) {

			if(this.oldLocusNewTag.containsKey(key)) {

				existingProducts.put(this.oldLocusNewTag.get(key), existingProducts.get(key));
				existingProducts.remove(key);
			}
		}

		oldList = new TreeSet<String>(existingProductsAlias.keySet());
		for(String key: oldList) {

			if(this.oldLocusNewTag.containsKey(key)) {

				existingProductsAlias.put(this.oldLocusNewTag.get(key), existingProductsAlias.get(key));
				existingProductsAlias.remove(key);
			}
		}

		/////////////////////////////////////////////////////////////////////////////Genes

		this.compareGenes(existingNames, existingGeneNamesAlias);
		/////////////////////////////////////////////////////////////////////////////EnzymesContainer

		this.compareEnzymes(enzymes);

		/////////////////////////////////////////////////////////////////////////////PRODUCTS

		this.compareProteins(existingProducts, existingProductsAlias);

		return true;
	}

	/**
	 * @param enzymes
	 * @param integrationEnzymes
	 */
	private void compareEnzymes(Set<String> enzymes) {

		for(String key : this.homologyEnzymes.keySet()) {

			boolean exists=false;
			if(existingECNumbers.containsKey(key)) {

				exists=true;
			}

			switch (integrationEnzymes) 
			{
			case MERGE:
			{
				if(exists) {

					if(!this.homologyEnzymes.get(key).isEmpty()) {

						if(existingECNumbers.get(key).isEmpty()) {

							this.integratedEnzymes.put(key, this.homologyEnzymes.get(key));
							this.enzymes.put(key, this.homologyEnzymes.get(key));
						}
						else {

							enzymes = new TreeSet<String>(existingECNumbers.get(key));
							enzymes.addAll(this.homologyEnzymes.get(key));
							this.integratedEnzymes.put(key,enzymes);
							this.enzymes.put(key,enzymes);
						}
					}
				}
				else {

					this.integratedEnzymes.put(key, this.homologyEnzymes.get(key));
					this.enzymes.put(key, this.homologyEnzymes.get(key));
				}
				break;
			}
			case MODEL:
			{
				if(exists) {

					if(existingECNumbers.isEmpty() && !this.homologyEnzymes.get(key).isEmpty()) {

						this.integratedEnzymes.put(key, this.homologyEnzymes.get(key));
						this.enzymes.put(key, this.homologyEnzymes.get(key));
					}	
				}
				else {

					this.integratedEnzymes.put(key, this.homologyEnzymes.get(key));
					this.enzymes.put(key, this.homologyEnzymes.get(key));
				}
				break;
			}
			case ANNOTATION:
			{
				this.integratedEnzymes.put(key, homologyEnzymes.get(key));
				this.enzymes.put(key, homologyEnzymes.get(key));
			}
			}
		}
	}

	/**
	 * @param existingNames
	 * @param integrationNames
	 * @param existingGeneNamesAlias
	 */
	private void compareGenes(Map<String, String> existingNames, Map<String, Set<String>> existingGeneNamesAlias) {

		for(String key : this.homologyName.keySet()) {

			boolean exists=false;
			if(existingNames.containsKey(key)) {

				exists=true;
			}

			switch (integrationNames) 
			{

			case MERGE:
			{
				if(exists) {

					if(existingNames.get(key).isEmpty()) {

						if(!this.homologyName.get(key).isEmpty()) {

							this.integratedName.put(key, this.homologyName.get(key));
							this.geneNames.put(key, this.homologyName.get(key));
						}
						else {

							//do nothing
						}
					}
					else {

						if(!(existingNames.get(key).equals(this.homologyName.get(key))) && !this.homologyName.get(key).isEmpty()) {

							if(existingGeneNamesAlias.containsKey(key)) {

								if(!(existingGeneNamesAlias.containsKey(key) && existingGeneNamesAlias.get(key).contains(this.homologyName.get(key)))) {

									List<String> temp = new ArrayList<String>();
									temp.add(existingNames.get(key));

									if(existingGeneNamesAlias.get(key)!=null) {

										temp.addAll(existingGeneNamesAlias.get(key));
									}

									this.nameConflictsDatabase.put(key, temp.toArray(new String[temp.size()]));
									this.nameConflictsHomology.put(key, new String[]{this.homologyName.get(key)});
								}
								else {

								}
							}
							else {

								this.nameConflictsDatabase.put(key, new String[]{existingNames.get(key)});
								this.nameConflictsHomology.put(key, new String[]{this.homologyName.get(key)});
							}
						}
					}
				}
				else {

					this.integratedName.put(key, this.homologyName.get(key));
					this.geneNames.put(key, this.homologyName.get(key));
				}
				break;
			}
			case MODEL:
			{

				if(exists) {

					if(existingNames.get(key).isEmpty()) {

						this.integratedName.put(key, this.homologyName.get(key));
						this.geneNames.put(key, this.homologyName.get(key));
					}
				}
				else {

					this.integratedName.put(key, this.homologyName.get(key));
					this.geneNames.put(key, this.homologyName.get(key));
				}

				//				if(this.isEukaryote) {
				//
				//					if(this.existingChromosome.containsKey(key) && this.existingChromosome.get(key).equalsIgnoreCase(this.chromosome.get(key))) {
				//
				//						this.chromosome.put(key, this.existingChromosome.get(key));
				//					}
				//				}

				break;
			}
			case ANNOTATION:
			{
				this.integratedName.put(key, this.homologyName.get(key));
				this.geneNames.put(key, this.homologyName.get(key));
			}
			}
		}

		allGeneNames = new TreeMap<String, List<String>>();

		for(String locus: existingNames.keySet()) {

			List<String> temp = new ArrayList<String>();
			temp.add(existingNames.get(locus));

			if(existingGeneNamesAlias.containsKey(locus)) {

				temp.addAll(existingGeneNamesAlias.get(locus));
			}
			allGeneNames.put(locus,temp);
		}
	}

	/**
	 * @param existingProducts
	 * @param integrationProducts
	 * @param existingProductsAlias
	 */
	private void compareProteins(Map<String, String> existingProducts, Map<String, Set<String>> existingProductsAlias) {

		for(String key : this.homologyProduct.keySet()) {

			boolean exists=false; 

			if(existingProducts.containsKey(key)) {

				exists=true;
			}

			switch (integrationProducts) 
			{
			case MERGE:
			{
				if(exists && !existingProducts.get(key).isEmpty()) {

					if(existingProducts.get(key).equals("hypothetical protein")) {

						if(!this.homologyProduct.get(key).contains("hypothetical protein")) {

							this.integratedProduct.put(key, this.homologyProduct.get(key));
							this.productNames.put(key, this.homologyProduct.get(key));
						}
					}
					else {

						if(this.homologyProduct.get(key).contains(existingProducts.get(key))) {

							this.integratedProduct.put(key,existingProducts.get(key));
							this.productNames.put(key,existingProducts.get(key));
						}
						else {

							this.integratedProduct.put(key, this.homologyProduct.get(key));
							this.productNames.put(key, this.homologyProduct.get(key));
						}
					}
				}
				else {

					this.integratedProduct.put(key, this.homologyProduct.get(key));
					this.productNames.put(key, this.homologyProduct.get(key));
				}
				break;
			}

			case MODEL:
			{
				if(exists) {

					if(!existingProducts.containsKey(key)) {

						this.integratedProduct.put(key, this.homologyProduct.get(key));
						this.productNames.put(key, this.homologyProduct.get(key));
					}
				}
				else {

					this.integratedProduct.put(key, this.homologyProduct.get(key));
					this.productNames.put(key, this.homologyProduct.get(key));
				}
				break;
			}

			case ANNOTATION:
			{

				this.integratedProduct.put(key, this.homologyProduct.get(key));
				this.productNames.put(key, this.homologyProduct.get(key));
			}
			}
		}

		allProteinNames = new TreeMap<String, List<String>>();

		for(String locus: existingProducts.keySet()) {

			List<String> temp = new ArrayList<String>();
			temp.add(existingProducts.get(locus));
			if(existingProductsAlias.containsKey(locus)) {

				temp.addAll(existingProductsAlias.get(locus));
			}
			allProteinNames.put(locus,temp);
		}
	}

	/**
	 * 
	 */
	public void generateReports() {

		if(directory!=null) {

			String path = directory.getAbsolutePath();

			this.iReport.saveLocusTagReport(this.workspaceName,new TreeMap<String,String>(this.oldLocusNewTag), path);
			this.iReport.saveGeneNameReport(this.workspaceName, allGeneNames, new TreeMap<String,String>(this.homologyName),new TreeMap<String,String>(this.integratedName), path);
			this.iReport.saveEnzymesReport(this.workspaceName,existingECNumbers,new TreeMap<String,Set<String>>(this.homologyEnzymes), new TreeMap<String,Set<String>>(this.integratedEnzymes), path);
			this.iReport.saveProteinNamesReport(this.workspaceName, allProteinNames,new TreeMap<String,String>(this.homologyProduct), new TreeMap<String,String>(this.integratedProduct), path);
			this.iReport.saveGeneNamesConflicts(this.workspaceName,new TreeMap<String,String>(this.newNameConflicts), path);
			this.iReport.pathwaysIntegrationReport(this.workspaceName, allPathways, existsPathway, new TreeMap<String,Set<String>>(this.homologyEnzymes), path);
		}
	}

	/**
	 * Loads the local database with the integrated data 
	 * @throws Exception 
	 */
	public void loadLocalDatabase(boolean processProteinNames) throws Exception {

		if(nameConflictsHomology.size()>0) {

			Workbench.getInstance().warn(""+nameConflictsHomology.size()+" unsolved conflicts during the gene names integration!");
			this.newNameConflicts = new TreeMap<String, String>();
			IntegrationConflictsGUI inst = new IntegrationConflictsGUI(this, true);
			inst.setIconImage((new ImageIcon(getClass().getClassLoader().getResource("icons/merlin.png"))).getImage());
			inst.setVisible(true);		
			inst.setAlwaysOnTop(true);
		}
		else {

			Workbench.getInstance().info("no conflits found throughout the gene names integration!");
		}

		try {

			///////////////////////////////////////////////////////////////////////////////////////////
	//		logger.info("Pre-Processing Genes...");
			///////////////////////////////////////////////////////////////////////////////////////////

//			for(String oldLocusTag :this.oldLocusNewTag.keySet())
//				ModelAPI.updateLocusTag(oldLocusTag, this.oldLocusNewTag.get(oldLocusTag), statement);

			///////////////////////////////////////////////////////////////////////////////////////////
			logger.info("Processing Genes...");
			///////////////////////////////////////////////////////////////////////////////////////////

			Map<String, Integer> sequenceTOGeneID = new HashMap<>();

			for(String sequence_id : this.homologyLocusTags.keySet()) {

				int geneID = ModelDatabaseLoadingServices.loadGene(this.workspaceName, this.homologyLocusTags.get(sequence_id), sequence_id, this.geneNames.get(sequence_id), 
						null, null, null, SourceType.HOMOLOGY);
				sequenceTOGeneID.put(sequence_id, geneID);
			}

			///////////////////////////////////////////////////////////////////////////////////////////
			logger.info("Processing Enzymes...");
			///////////////////////////////////////////////////////////////////////////////////////////

			for(String sequence_id :this.enzymes.keySet()) {
				ModelEnzymesServices.loadEnzymeGetReactions(this.workspaceName,sequenceTOGeneID.get(sequence_id), this.enzymes.get(sequence_id),
						this.productNames.get(sequence_id), INTEGRATE_PARTIAL, INTEGRATE_FULL, processProteinNames,
						ProjectServices.isCompartmentalisedModel(workspaceName));
			}


			///////////////////////////////////////////////////////////////////////////////////////////	
			Workbench.getInstance().info("integration finished");
			///////////////////////////////////////////////////////////////////////////////////////////
		} 
		catch (SQLException e) {

			e.printStackTrace();
			Workbench.getInstance().error(e.getMessage());	
		}
	}





	/**
	 * @param existshomologyInstance
	 * 
	 * Whether there is or not a homology instance
	 * 
	 */
	public void setExistshomologyInstance(boolean existshomologyInstance) {
		this.existshomologyInstance = existshomologyInstance;
	}

	/**
	 * @return Boolean of the existence of the homology instance
	 */
	public boolean isExistshomologyInstance() {
		return existshomologyInstance;
	}

	public Map<String, String[]> getNameConflictsDatabase() {
		return nameConflictsDatabase;
	}

	public Map<String, String[]> getNameConflictsHomology() {
		return nameConflictsHomology;
	}
	/**
	 * @param newNameConflicts the newNameConflicts to set
	 */
	public void setNewNameConflicts(Map<String, String> newNameConflicts) {
		this.newNameConflicts = newNameConflicts;
	}


	/**
	 * Operation that assigns new updated locus tags to the local database.
	 * 
	 * @param statement
	 * @throws SQLException
	 */
	public void setNewLocusTags(Statement statement) throws SQLException {

		
		//		Set<String> databaseGenes = ModelAPI.getAllDatabaseGenes(statement);
		//
		//		List<String> oldLocusTagsDatabase = new ArrayList<String>(databaseGenes);
		//
		//		oldLocusTagsDatabase.removeAll(this.homologyLocusTags);
		//		List<String> oldLocusTagsHomology = new ArrayList<String>(this.homologyLocusTags);
		//		oldLocusTagsHomology.removeAll(databaseGenes);

		//		List<String> iteratorList = new ArrayList<String>(oldLocusTagsDatabase);
		//
		//		for(int index = 0 ; index<iteratorList.size() ; index++) {
		//
		//			if(oldLocusTagsHomology.isEmpty() || oldLocusTagsDatabase.isEmpty()) {
		//
		//				index = iteratorList.size();
		//			}
		//			else {
		//
		//				String oldLocusTag = iteratorList.get(index);
		//				UniProtEntry uniProtEntry = UniProtAPI.getUniProtEntryFromXRef(oldLocusTag,0);
		//				String newLocusTag = null;
		//
		//				if(uniProtEntry!=null && UniProtAPI.getLocusTags(uniProtEntry)!=null && 
		//						UniProtAPI.getLocusTags(uniProtEntry).size()>0) {
		//
		//					newLocusTag = UniProtAPI.getLocusTags(uniProtEntry).get(0);//.getValue();
		//
		//					if(!homologyLocusTags.contains(newLocusTag)) {
		//
		//						this.oldLocusNewTag.put(oldLocusTag,newLocusTag);
		//						oldLocusTagsDatabase.remove(oldLocusTag);
		//						oldLocusTagsHomology.remove(newLocusTag);
		//					}
		//				}
		//			}
		//		}

		//		Map<String, List<String>> alternativeLocusTag = this.getAllAlternativeLocusTag();
		//				this.oldLocusNewTag =  new TreeMap<String, String>();
		//				for(String locus:alternativeLocusTag.keySet())
		//				{
		//					if(!oldLocusTags.contains(locus)) // if the homology locus tag is not available in the local database
		//					{
		//						boolean found=false;
		//						for(int i =0 ;i< alternativeLocusTag.get(locus).size();i++) //for each alternative (ordered)
		//						{
		//							String alternative = alternativeLocusTag.get(locus).get(i);
		//		
		//							if(oldLocusTags.contains(alternative) && !found)// if the alternative is present in the local database && is the first available alternative
		//							{
		//								if(!alternativeLocusTag.containsKey(alternative)) // if the alternative is a homology locusTag it is not available
		//								{
		//									this.oldLocusNewTag.put(alternative,locus);
		//									found=true;
		//								}
		//							}
		//						}
		//					}
		//				}	
		//				System.out.println("SIZE:\t"+this.oldLocusNewTag.size());
		//		
		//				for(String key: this.oldLocusNewTag.keySet())
		//				{
		//					System.out.println("old: "+key+"\tnew: "+this.oldLocusNewTag.get(key));
		//				}

	}


}
