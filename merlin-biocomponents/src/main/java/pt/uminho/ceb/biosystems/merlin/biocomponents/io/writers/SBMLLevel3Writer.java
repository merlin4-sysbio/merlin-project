package pt.uminho.ceb.biosystems.merlin.biocomponents.io.writers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.CVTerm.Qualifier;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.Unit.Kind;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.fbc.And;
import org.sbml.jsbml.ext.fbc.Association;
import org.sbml.jsbml.ext.fbc.FBCConstants;
import org.sbml.jsbml.ext.fbc.FBCModelPlugin;
import org.sbml.jsbml.ext.fbc.FBCReactionPlugin;
import org.sbml.jsbml.ext.fbc.FBCSpeciesPlugin;
import org.sbml.jsbml.ext.fbc.FluxObjective;
import org.sbml.jsbml.ext.fbc.GeneProduct;
import org.sbml.jsbml.ext.fbc.GeneProductAssociation;
import org.sbml.jsbml.ext.fbc.GeneProductRef;
import org.sbml.jsbml.ext.fbc.Objective;
import org.sbml.jsbml.ext.fbc.Objective.Type;
import org.sbml.jsbml.ext.fbc.Or;
import org.sbml.jsbml.ext.groups.Group;
import org.sbml.jsbml.ext.groups.GroupsConstants;
import org.sbml.jsbml.ext.groups.GroupsModelPlugin;
import org.sbml.jsbml.ext.groups.Member;
import org.sbml.jsbml.text.parser.ParseException;
import org.sbml.jsbml.validator.SyntaxChecker;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.parsers.MathMLStaxParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.biocomponents.io.Enumerators.SBMLLevelVersion;
import pt.uminho.ceb.biosystems.merlin.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.utilities.external.ExternalRef;
import pt.uminho.ceb.biosystems.merlin.utilities.external.ExternalRefSource;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.CompartmentCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.GeneCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.MetaboliteCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionConstraintCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionTypeEnum;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.StoichiometryValueCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.exceptions.JSBMLWriterException;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.collection.CollectionUtils;

public class SBMLLevel3Writer {

	private String path;
	private Container container;
	private boolean palssonSpecific = false;
	//	private boolean writeMath = false;
	private boolean ignoreNotesField = false;
	private boolean ignoreCellDesignerAnnotations = false;
	private boolean writeUnits = true;
	private boolean needsToBeStandardized = false;
	private Map<String, ReactionConstraintCI> overrideConstraints;

	private String taxonomyID;
	private String modelID;
	private String pubmedID;
	private boolean writeObjectives;
	private SBMLDocument sbmlDocument;


	private SBMLLevelVersion levelAndVersion = SBMLLevelVersion.L3V2;
	private ArrayList<String> ignoredNamespaces = new ArrayList<String>();
	private HashMap<String, MetaboliteCI> new_metabolites_names_r = new HashMap<String, MetaboliteCI>();
	private HashMap<String, MetaboliteCI> new_metabolites_names_p = new HashMap<String, MetaboliteCI>();
	//	private Set<String> extraMetabolitesInfo;
	//	private Set<String> extraReactionsInfo;


	private static final double DEFAULT_BOUND_VALUE = 999999;
	private static final boolean PARAMETER_IS_CONSTANT = true;
	private static final boolean METABOLITE_CONSTANT = false;
	private static final boolean METABOLITE_HAS_ONLY_SUBSTANCE_UNITS = false;
	private static final double DEFAULT_INITIAL_AMOUNT = 1.0;
	private static final org.sbml.jsbml.ext.groups.Group.Kind GROUPS_KIND = Group.Kind.partonomy;
	private static final String MERLIN_NAME = "merlin - www.merlin-sysbio.org";
	//	private static final String EQUATION_PREFIX = "EQUATION: ";
	private Integer genesNumber = 1; 


	public static String CELLDESIGNER_NAMESPACE_PREFIX = "celldesigner";
	public static String CELLDESIGNER_NAMESPACE_URI = "http://www.sbml.org/2001/ns/celldesigner";
	
	final static Logger logger = LoggerFactory.getLogger(SBMLLevel3Writer.class);


	//	private XMLNamespaces spaces;

	public SBMLLevel3Writer(){}

	public SBMLLevel3Writer(String path, Container container, String taxonomyID, boolean writeObjectives) {
		this.overrideConstraints = new HashMap<String, ReactionConstraintCI>();
		this.path = path;
		this.container = container;
		
		if(!container.hasUnicIds()){
			this.container = container.clone();
			this.container.useUniqueIds();
		}
		this.taxonomyID = taxonomyID;
		this.writeObjectives = writeObjectives;

	}

	public SBMLLevel3Writer(String path,Container container, String taxonomyID, boolean writeObjectives, String modelID){
		this(path,container,taxonomyID,writeObjectives);
		this.modelID = modelID;
	}

	public SBMLLevel3Writer(String path,Container container, String taxonomyID, boolean writeObjectives, String modelID, boolean palssonSpecific, boolean writeUnits){
		this(path,container,taxonomyID,writeObjectives,modelID);
		this.palssonSpecific = palssonSpecific;
		this.writeUnits = writeUnits;
	}

	public SBMLLevel3Writer(String path,Container container, String taxonomyID, boolean writeObjectives, String modelID, boolean palssonSpecific,SBMLLevelVersion lv, boolean writeUnits){
		this(path,container,taxonomyID,writeObjectives,modelID,palssonSpecific, writeUnits);
		this.levelAndVersion = lv;
	}

	public SBMLLevel3Writer(String path,Container container, String taxonomyID, boolean writeObjectives, String modelID, boolean palssonSpecific,boolean writeMath,SBMLLevelVersion lv, boolean writeUnits){
		this(path,container,taxonomyID,writeObjectives,modelID,palssonSpecific, writeUnits);
		//		this.writeMath = writeMath;
		this.levelAndVersion = lv;
	}

	/**
	 * This method writes the container into a SBML file
	 * @throws Exception
	 */
	public void writeToFile() throws Exception {
		if(isPalssonSpecific())
			constructMetabolitesToInsert();
		try {
			toSBML(path);
		} catch (SBMLException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	public void setOverrideConstraints(
			Map<String, ReactionConstraintCI> overrideConstraints) {
		this.overrideConstraints = overrideConstraints;
	}


	/**
	 * This method constructs the metabolites to insert
	 */
	private void constructMetabolitesToInsert(){

		Set<String> drains = container.getDrains();

		Map<String, MetaboliteCI> metabolites = container.getMetabolites();
		
		for(String reaction : drains){
			Map<String, StoichiometryValueCI> reactants = container.getReaction(reaction).getReactants();
			Map<String, StoichiometryValueCI> products = container.getReaction(reaction).getProducts();
			if(reactants.size() > 0){
				generalStandard(reactants, metabolites, reaction, true);
			}
			else if(products.size() > 0){
				generalStandard(products, metabolites, reaction, false);
			}
		}
	}

	/**
	 * This method receives a map with some metabolites, and updates them with a new standardized name
	 * @param map The structure with the to update metabolites
	 * @param metabolites A structure with all the metabolites
	 * @param reaction The reaction ID
	 */
	private void generalStandard(Map<String, StoichiometryValueCI> map, Map<String, MetaboliteCI> metabolites, String reaction, boolean isReactant){
		MetaboliteCI new_metabolite = null;
		String new_metabolite_name;
		for (String name : map.keySet()){				//it will be just 1 reactant
			new_metabolite_name = name;
			if(underComp(name)) new_metabolite_name = name.replaceAll("_(" + CollectionUtils.join(container.getCompartments().keySet(), "|")+")_$", "");					//-2 to delete the "_e"
			if(needsToBeStandardized) new_metabolite_name = standardizeMetId(new_metabolite_name);
			new_metabolite_name += "_b";
			new_metabolite = metabolites.get(name).clone();
			new_metabolite.setId(new_metabolite_name);
			
			if(isReactant)
				new_metabolites_names_p.put(reaction, new_metabolite);
			else
				new_metabolites_names_r.put(reaction, new_metabolite);

		}
	}

	/**
	 * This method checks if a metaboliteID has a compartment and a '_' in its name
	 * @param name The metaboliteID
	 * @return
	 */
	private boolean underComp(String name) {
		String reg_exp = ".*(" + CollectionUtils.join(container.getCompartments().keySet(), "|")+")_$";
		if(name.matches(reg_exp)) return true;
		return false;
	}


	protected Model createModel() throws Exception{

		Model model = new Model(container.getModelName(), levelAndVersion.getLevel(), levelAndVersion.getVersion());

		ListOf<UnitDefinition> list_unitDef = new ListOf<UnitDefinition>(levelAndVersion.getLevel(), levelAndVersion.getVersion());
		ListOf<Unit> list_unit = new ListOf<Unit>(levelAndVersion.getLevel(), levelAndVersion.getVersion());
		UnitDefinition unit_def = null;

		FBCModelPlugin fbcModel = new FBCModelPlugin(model);
		fbcModel.setStrict(true);
		model.addPlugin(FBCConstants.namespaceURI, fbcModel);

		/**write model annotations*/
		writeModelAnnotations(model);

		/**write list of objectives*/
		if(writeObjectives)
			writeListOfObjectives(model);

		/**write unit definitions*/
		if(writeUnits)
			writeUnitDefinitions(list_unitDef, list_unit, unit_def, model);

		/** load all the compartments */
		loadCompartments(model);
		
		/** load all model groups (pathways in this case) */
		loadGroups(model);
		
		/** load all species */		
		loadSpecies(model);
		
		/** load all genes */		
		loadGeneProducts(model);

		/**  load the drains and define new metabolites for each one of them  */
		if(isPalssonSpecific())
			loadDrains(model);
		
		/** load all the reactions */
		loadReactions(model, unit_def);

		return model;
	}

	protected void saveToFile(Model model, String outputFile) throws FileNotFoundException, SBMLException, XMLStreamException{
		SBMLDocument document = new SBMLDocument(levelAndVersion.getLevel(), levelAndVersion.getVersion());
		Logger lS = LoggerFactory.getLogger(SBMLDocument.class);
		Logger lM = LoggerFactory.getLogger(MathMLStaxParser.class);
		lS.debug("logger ok");
		lM.debug("logger ok");
		//		spaces = new XMLNamespaces();
		//		spaces.add("http://www.w3.org/1999/xhtml", "html");
		//		spaces.add("http://www.sbml.org/sbml/level2");
		//		spaces.add("http://www.w3.org/1998/Math/MathML");
		////		document.addDeclaredNamespace(prefix, namespace);
		//		document.addNamespace("html", "xmlns", "http://www.w3.org/1999/xhtml");
		
		document.setModel(model);
		writeNotes(document, model);
		
		this.sbmlDocument=document;
		
//		int errorsWarnings = document.checkConsistency();

//		logger.info("SBML VALIDATION ERRORS/WARNINGS: "+errorsWarnings);

		SBMLWriter writer = new SBMLWriter();
		writer.setProgramName(MERLIN_NAME);
		String merlinVersion = Utilities.getMerlinVersion();
		if(merlinVersion!=null)
			writer.setProgramVersion(merlinVersion);

		OutputStream out = new FileOutputStream(outputFile);
		writer.write(document, out);
	}

	/**
	 * <p>This method converts the <code>InformationContainer</code> to the SBML</p>
	 * <p>native format and returns it as an SBML <code>String</code></p> 
	 * 
	 * @return <code>String</code> representation of the SBML model.
	 * @throws Exception 
	 */
	public void toSBML(String outputFile) throws Exception{
		Model model = createModel();
		saveToFile(model, outputFile);
	}

	/**
	 * This method writes the notes
	 * @param document The SBMLDocument object
	 * @param model The model
	 */
	public void writeNotes(SBMLDocument document, Model model){

		//		System.out.println(container.getNotes());
		//		standardizeMetId(idcontainer)
		//		if(container.getNotes() != null) document.setNotes(container.getNotes());
		//		document.setn
		//		document.addDeclaredNamespace("html", "html");
		String id = container.getModelName(); 
		String name = container.getModelName();
		if(id==null)
			id="ID";
		if(name==null)
			name="Model exported by merlin software";
		model.setId(id);
		model.setName(name);

	}


	/**
	 * This method writes the model annotations, such as taxonomyID or modelID of the organism
	 * @param model
	 */
	public void writeModelAnnotations(Model model) {

		List<CVTerm> resources = new ArrayList<>();

		if(this.taxonomyID != null) {

			CVTerm taxonomy = new CVTerm(Qualifier.BQB_HAS_TAXON, this.taxonomyID);
			resources.add(taxonomy);
		}

		if(this.modelID != null) {

			CVTerm modelId = new CVTerm(Qualifier.BQM_IS, this.modelID);
			resources.add(modelId);
		}

		if(this.pubmedID !=null) {

			CVTerm pubMedId = new CVTerm(Qualifier.BQM_IS_DESCRIBED_BY, this.pubmedID);
			resources.add(pubMedId);
		}

		Annotation modelAnnotation = new Annotation(resources);
		model.setAnnotation(modelAnnotation);
	}

	/**
	 * This method writes the unit definitions
	 * @param list_unitDef A list of unit definitios
	 * @param list_unit A list of units
	 * @param unit_def A UnitDefition object
	 * @param model The model
	 */
	public void writeUnitDefinitions(ListOf<UnitDefinition> list_unitDef, ListOf<Unit> list_unit, UnitDefinition unit_def, Model model){
		Unit unit_mole = new Unit(levelAndVersion.getLevel(), levelAndVersion.getVersion());
		unit_mole.setScale(-3);
		unit_mole.setKind(Kind.MOLE);
		unit_mole.setExponent(1.0);
		unit_mole.setMultiplier(1.0);

		Unit unit_gram = new Unit(levelAndVersion.getLevel(), levelAndVersion.getVersion());
		unit_gram.setKind(Kind.GRAM);
		unit_gram.setExponent(-1.0);
		unit_gram.setScale(0);
		unit_gram.setMultiplier(1.0);

		Unit unit_second = new Unit(levelAndVersion.getLevel(), levelAndVersion.getVersion());
		unit_second.setLevel(levelAndVersion.getLevel());
		unit_second.setMultiplier(3600.0);
		unit_second.setKind(Kind.SECOND);
		unit_second.setExponent(-1.0);
		unit_second.setScale(0);

		list_unit.add(unit_mole);
		list_unit.add(unit_gram);
		list_unit.add(unit_second);

		unit_def = new UnitDefinition("mmol_per_gDW_per_hr", "Millimoles per gram (dry weight) per hour", levelAndVersion.getLevel(), levelAndVersion.getVersion());
		unit_def.setListOfUnits(list_unit);
		list_unitDef.add(unit_def);

		//SUBSTANCE
		UnitDefinition unit_definition = new UnitDefinition("substance", "Millimoles per gram (dry weight)", levelAndVersion.getLevel(), levelAndVersion.getVersion());
		list_unit = new ListOf<Unit>(levelAndVersion.getLevel(), levelAndVersion.getVersion());

		Unit unit_mole2 = unit_mole.clone();
		Unit unit_gram2 = unit_gram.clone();
		list_unit.add(unit_mole2);
		list_unit.add(unit_gram2);

		unit_definition.setListOfUnits(list_unit);
		list_unitDef.add(unit_definition);

		//TIME
		unit_definition = new UnitDefinition("time", "Hour", levelAndVersion.getLevel(), levelAndVersion.getVersion());
		list_unit = new ListOf<Unit>(levelAndVersion.getLevel(), levelAndVersion.getVersion());

		Unit unit_time = unit_second.clone();
		unit_time.setExponent(1.0);
		list_unit.add(unit_time);

		unit_definition.setListOfUnits(list_unit);
		list_unitDef.add(unit_definition);

		/** set list of unit definitions */
		model.setListOfUnitDefinitions(list_unitDef);
	}

	/**
	 * This method writes the list of objectives (in this case the biomass)
	 * @param model The model
	 */
	public void writeListOfObjectives(Model model){

		FBCModelPlugin sbml3Objectives = (FBCModelPlugin) model.getPlugin("fbc");

		ReactionCI biomassReaction = container.getReaction(container.getBiomassId());
//		String[] biomassName = biomassReaction.getName().split("__");
//		String reactionID = standardizerReactId(biomassName[biomassName.length-1],biomassReaction.getType());
		
		String reactionID = standardizerReactId(biomassReaction.getId(),biomassReaction.getType());

		Objective biomassObjective = new Objective("biomass");
		biomassObjective.setType(Type.MAXIMIZE);

		FluxObjective biomass = new FluxObjective();
		biomass.setCoefficient(1.0);
		biomass.setReaction(reactionID);

		biomassObjective.addFluxObjective(biomass);
		sbml3Objectives.setActiveObjective(biomassObjective);

		try{
			sbml3Objectives.addObjective(biomassObjective);
		}catch(Exception e){
			exceptionsMap.put(biomass.getId(), e);
		}

		if(!exceptionsMap.isEmpty())
			throw new JSBMLWriterException(exceptionsMap);
	}

	/**
	 * This method loads the compartments
	 * @param model The model
	 */
	public void loadCompartments(Model model){

		ListOf<Compartment> sbml3Compartments = new ListOf<Compartment>(levelAndVersion.getLevel(), levelAndVersion.getVersion());

		for(CompartmentCI comp : container.getCompartments().values()){
			Compartment sbmlCompartment = new Compartment(comp.getId(), comp.getName(), levelAndVersion.getLevel(), levelAndVersion.getVersion());
			sbmlCompartment.setId(comp.getId());
			if(comp.getName()!= null) sbmlCompartment.setName(comp.getName());
			//			sbmlCompartment.setOutside(comp.getOutside());
			sbmlCompartment.setConstant(true);

			try{
				sbml3Compartments.add(sbmlCompartment);
			}catch(Exception e){
				exceptionsMap.put(comp.getId(), e);
			}
		}
		model.setListOfCompartments(sbml3Compartments);

		if(!exceptionsMap.isEmpty())
			throw new JSBMLWriterException(exceptionsMap);
	}

	/**
	 * This method loads the list of groups (pathways) of the model
	 * @param model The model
	 */
	public void loadGroups(Model model){

		GroupsModelPlugin modelGroupsPlugin = new GroupsModelPlugin(model);
		ListOf<Group> sbml3Groups = new ListOf<Group>(levelAndVersion.getLevel(), levelAndVersion.getVersion());

		Map<String, List<ReactionCI>> pathwaysReactionsMap = new HashMap<>();

		for(ReactionCI reaction : container.getReactions().values()){

			if(reaction.getSusbystems()!=null && !reaction.getSusbystems().isEmpty()){
				for(String pathwayInReaction : reaction.getSusbystems()){

					if(pathwaysReactionsMap.containsKey(pathwayInReaction))
						pathwaysReactionsMap.get(pathwayInReaction).add(reaction);

					else{
						List<ReactionCI> reactionsInPathway = new ArrayList<>();
						reactionsInPathway.add(reaction);
						pathwaysReactionsMap.put(pathwayInReaction, reactionsInPathway);
					}
				}
			}
		}
		int groupIDcounter = 1;

		for(String pathway : pathwaysReactionsMap.keySet()){
			Group group = new Group();
			group.setId("g".concat(Integer.toString(groupIDcounter)));
			group.setKind(GROUPS_KIND);
			group.setName(pathway);

			for(ReactionCI reaction : pathwaysReactionsMap.get(pathway)){
//				String[] reactionName = reaction.getName().split("__");
//				String reactionID = standardizerReactId(reactionName[reactionName.length-1],reaction.getType());
				String reactionID = standardizerReactId(reaction.getId(),reaction.getType()).replace("=", "").replace("<", "").replace(">", "");
				Member member = new Member();
				
				member.setIdRef(reactionID.replaceAll("[><']", "").replace("\\", ""));
				group.addMember(member);
			}
			try{
				sbml3Groups.add(group);
				groupIDcounter++;
			}catch(Exception e){
				exceptionsMap.put(group.getId(), e);
			}
		}
		modelGroupsPlugin.setListOfGroups(sbml3Groups);
		model.addPlugin(GroupsConstants.namespaceURI, modelGroupsPlugin);

		if(!exceptionsMap.isEmpty())
			throw new JSBMLWriterException(exceptionsMap);
	}

	/**
	 * This method loads the metabolites
	 * @param model The model
	 * @throws Exception 
	 */
	public void loadSpecies(Model model) throws Exception{

		verifyNeedsStandardize();

		ListOf<Species> smbl3species = new ListOf<Species>(levelAndVersion.getLevel(), levelAndVersion.getVersion());
		//		Set<String> metabolitesInDrains = container.getMetaboliteToDrain().keySet();
		
		for(CompartmentCI compCI : container.getCompartments().values()){

			for(String s : compCI.getMetabolitesInCompartmentID()){		
				
				MetaboliteCI metabolite = container.getMetabolite(s);

				Species species = new Species(levelAndVersion.getLevel(), levelAndVersion.getVersion());

				String sbmlName;// = standardizeMetId(species.getId());
				if(needsToBeStandardized) sbmlName = standardizeMetId(metabolite.getId());
				else sbmlName = metabolite.getId();
				
				species.setId(sbmlName);
				if(metabolite.getName().split("_").length>1)
					species.setName(metabolite.getName().split("_")[1]);
				species.setCompartment(compCI.getId());

				//				if(metabolitesInDrains.contains(sbmlName))
				//					species.setBoundaryCondition(true);
				//				else
				//					species.setBoundaryCondition(false);

				species.setBoundaryCondition(false);
				species.setHasOnlySubstanceUnits(METABOLITE_HAS_ONLY_SUBSTANCE_UNITS);
				species.setConstant(METABOLITE_CONSTANT);
				species.setInitialAmount(DEFAULT_INITIAL_AMOUNT);

				FBCSpeciesPlugin fbcMeta = new FBCSpeciesPlugin(species);

				if(metabolite.getFormula() != null && SyntaxChecker.isValidChemicalFormula(metabolite.getFormula()))
					fbcMeta.setChemicalFormula(metabolite.getFormula());
				fbcMeta.setCharge(metabolite.getCharge());

				species.addExtension(FBCConstants.namespaceURI_L3V1V1, fbcMeta);

				try{
					writeSpeciesAnnotation(metabolite, species);
					//					addMetaboliteNoteInformation(s, species);
					smbl3species.add(species);
				}catch(Exception e){
					exceptionsMap.put(metabolite.getId(), e);
				}
			}

			model.setListOfSpecies(smbl3species);

			if(!exceptionsMap.isEmpty())
				throw new JSBMLWriterException(exceptionsMap);
		}
	}


	/**
//	 * This is an auxiliary method that writes the metabolites' annotations
//	 * @param metabolite A MetaboliteCI object
//	 * @param sbmlSpecies A Species object
//	 */
	public void writeSpeciesAnnotation(MetaboliteCI metabolite, Species sbmlSpecies){

		//String speciesKeggID = metabolite.getName().split("_")[0];
		String speciesKeggID = metabolite.getId().split("_")[1];

		if(validateKeggID(speciesKeggID, true)) {

			Annotation annotation = writeAnnotation(speciesKeggID, Qualifier.BQB_IS, ExternalRefSource.KEGG_CPD);
			sbmlSpecies.setAnnotation(annotation);
		}

	}


	public void verifyNeedsStandardize(){
		for(MetaboliteCI species : container.getMetabolites().values()){
			needsToBeStandardized |= Character.isDigit(species.getId().charAt(0));
		}
	}


	/**
	 * This method load genes into the model
	 * @param The model
	 */
	public void loadGeneProducts(Model model){

		//		ListOf<GeneProduct> sbml3GeneProducts = new ListOf<GeneProduct>(levelAndVersion.getLevel(), levelAndVersion.getVersion());

		FBCModelPlugin fbcModel = (FBCModelPlugin) model.getPlugin("fbc");
		
		for(GeneCI gene : container.getGenes().values()){
			
			String geneProductID = standardGeneID(gene.getGeneId());
			
			//			if(!fbcModel.getGeneProduct(geneProductID)){

			GeneProduct sbmlGeneProduct = new GeneProduct(levelAndVersion.getLevel(), levelAndVersion.getVersion());

			sbmlGeneProduct.setId(geneProductID);
			sbmlGeneProduct.setLabel(geneProductID.replace("G_", ""));
//			String[] idSplit = gene.getGeneId().split("_");
			String geneName = gene.getGeneName();

			if(geneName!= null && !geneName.equals(""))
				sbmlGeneProduct.setName(gene.getGeneName());
//			else {
//				if (idSplit.length == 2 && !idSplit[1].startsWith("\\d"))
//					sbmlGeneProduct.setName(idSplit[0]);
//
//				else if(idSplit.length > 2){
//					Integer startLocus = -1;
//					Integer i = idSplit.length-1;
//
//					while(i>=0 && startLocus==-1){
//						if(!idSplit[i].startsWith("\\d"))
//							startLocus = i;
//						i--;	
//					}
//					if(startLocus > 0){
//						geneName = "";
//
//						for(int j=0; j<startLocus; j++)
//							geneName.concat(idSplit[j]);
//
//						sbmlGeneProduct.setName(geneName);
//					}
//				}
//			}

			sbmlGeneProduct.setMetaId(sbmlGeneProduct.getId());

			/** write the annotations */
			writeGeneProductsAnnotations(gene, sbmlGeneProduct);

			try{
				fbcModel.addGeneProduct(sbmlGeneProduct);
				genesNumber++;
				//				sbml3GeneProducts.add(sbmlGeneProdutct);
			}catch(Exception e){
				exceptionsMap.put(gene.getGeneId(), e);
			}
			//			}
		}
		//		fbcModel.setListOfGeneProducts(sbml3GeneProducts);
		model.addPlugin(FBCConstants.namespaceURI_L3V1V1, fbcModel);;

		if(!exceptionsMap.isEmpty())
			throw new JSBMLWriterException(exceptionsMap);
	}



	/**
	 * This method writes the annotation nodes for each GeneProduct in the ListOfGeneProducts
	 * @param gene
	 * @param sbmlGene
	 */
	public void writeGeneProductsAnnotations(GeneCI gene, GeneProduct sbmlGene){

		Map<Qualifier, Map<ExternalRefSource, List<String>>> annotationData = new HashMap<>();
		Qualifier[] qualifiers = new Qualifier[] {Qualifier.BQB_IS, Qualifier.BQB_ENCODES};//, Qualifier.BQB_IS_ENCODED_BY};
		Set<String> reactions = gene.getReactionIds();
		List<String> reactionKeggIds = new ArrayList<>();
		List<String> geneEcNumbers = new ArrayList<>();

		for(String idReaction : reactions) {

			ReactionCI reaction = container.getReaction(idReaction);
			//String keggID = reaction.getName().split("_")[0];
			String keggID = reaction.getId().split("_")[1];
			if(validateKeggID(keggID, false))
				reactionKeggIds.add(keggID);
			String ecNumbersString = reaction.getEc_number();
			String[] ecNumbers = null;
			if(ecNumbersString.length()>0)
				ecNumbers = ecNumbersString.substring(1, ecNumbersString.length()-1).split(",");

			if(ecNumbers != null && !Arrays.asList(ecNumbers).isEmpty())
				geneEcNumbers.addAll(Arrays.asList(ecNumbers));
		}
		for(Qualifier qualifier : qualifiers) {

			Map<ExternalRefSource,List<String>> resources = new HashMap<>();

			if(qualifier.equals(Qualifier.BQB_IS)){
				List<String> ncbiID = new ArrayList<>();
				ncbiID.add(standardGeneID(gene.getGeneId()).substring(2));
				resources.put(ExternalRefSource.NCBI_PROTEIN, ncbiID);
			}

			else if (qualifier.equals(Qualifier.BQB_ENCODES)){
				resources.put(ExternalRefSource.KEGG_REACTION, reactionKeggIds);
//				resources.put(ExternalRefSource.EC_Code, geneEcNumbers);

			}
			annotationData.put(qualifier, resources);
		}
		sbmlGene.setAnnotation(writeAnnotations(annotationData));
	}


	/**
	 * This method loads the drains and define new metabolites for each one of them
	 * @param model
	 * @throws Exception 
	 */
	public void loadDrains(Model model) throws Exception{
		
		if(container.getExternalCompartment() != null){
			
			String extcomp = container.getExternalCompartment().getId();

			//PRODUCTS
			for(MetaboliteCI newmet : new_metabolites_names_p.values()){
				Species sbmlSpecies = new Species(levelAndVersion.getLevel(), levelAndVersion.getVersion());
				sbmlSpecies.setId(newmet.getId());
				sbmlSpecies.setName(newmet.getName());
				sbmlSpecies.setCompartment(extcomp);
				sbmlSpecies.setBoundaryCondition(true);
				sbmlSpecies.setHasOnlySubstanceUnits(METABOLITE_HAS_ONLY_SUBSTANCE_UNITS);
				sbmlSpecies.setConstant(METABOLITE_CONSTANT);
				sbmlSpecies.setInitialAmount(DEFAULT_INITIAL_AMOUNT);

				if(model.getSpecies(newmet.getId())==null)
					model.addSpecies(sbmlSpecies);
			}

			//REACTANTS
			for(MetaboliteCI newmet : new_metabolites_names_r.values()){
				Species sbmlSpecies = new Species(levelAndVersion.getLevel(), levelAndVersion.getVersion());
				sbmlSpecies.setId(newmet.getId());
				sbmlSpecies.setName(newmet.getName());
				sbmlSpecies.setCompartment(extcomp);
				sbmlSpecies.setBoundaryCondition(true);
				sbmlSpecies.setHasOnlySubstanceUnits(METABOLITE_HAS_ONLY_SUBSTANCE_UNITS);
				sbmlSpecies.setConstant(METABOLITE_CONSTANT);
				sbmlSpecies.setInitialAmount(DEFAULT_INITIAL_AMOUNT);

				if(model.getSpecies(newmet.getId())==null)
					model.addSpecies(sbmlSpecies);
			}
		}
	}

	/**
	 * This method loads the reactions
	 * @param model The model
	 * @param unitDef A UnitDefinition object
	 * @throws XMLStreamException 
	 */
	public void loadReactions(Model model, UnitDefinition unitDef) throws XMLStreamException{

		ListOf<Parameter> listParameters = new ListOf<Parameter>(levelAndVersion.getLevel(), levelAndVersion.getVersion());

//		Parameter defaultLB = new Parameter("merlin_defaulLB");
//		defaultLB.setConstant(PARAMETER_CONSTANT);
//		defaultLB.setName("merlin_default_lowerBound");
//		defaultLB.setUnits(unitDef);
//		defaultLB.setValue(-DEFAULT_BOUND_VALUE);
//		listParameters.add(0, defaultLB);
//
//		Parameter boundZero = new Parameter("merlin_0_bound");
//		boundZero.setConstant(PARAMETER_CONSTANT );
//		boundZero.setName("merlin_0_bound");
//		boundZero.setUnits(unitDef);
//		boundZero.setValue(0);
//		listParameters.add(1, boundZero);
//
//		Parameter defaultUB = new Parameter("merlin_defaulUB");
//		defaultUB.setConstant(PARAMETER_CONSTANT);
//		defaultUB.setName("merlin_default_upperBound");
//		defaultUB.setUnits(unitDef);
//		defaultUB.setValue(DEFAULT_BOUND_VALUE);
//		listParameters.add(2, defaultUB);

		//		Map<String,Map<String,String>> reactionsExtraInfo = container.getReactionsExtraInfo();
		
		for(String rId : container.getReactions().keySet()){
			ReactionCI ogreaction = container.getReactions().get(rId);

			//			String reactionID = standardizerReactId(ogreaction.getId(), ogreaction.getType());
//			String[] reactionName = ogreaction.getName().split("__");
//			String reactionID = standardizerReactId(reactionName[reactionName.length-1],ogreaction.getType());
			String reactionID = standardizerReactId(ogreaction.getId(),ogreaction.getType());
			Reaction sbmlReaction = new Reaction(levelAndVersion.getLevel(), levelAndVersion.getVersion());
			sbmlReaction.setId(reactionID.replaceAll("[><']", "").replace("\\", ""));
//			sbmlReaction.setName(ogreaction.getName().substring(0, ogreaction.getName().lastIndexOf("__")));
			sbmlReaction.setName(ogreaction.getName());
			sbmlReaction.setReversible(ogreaction.isReversible());
			//			String[] splitName = ogreaction.getName().split("__");
			//			sbmlReaction.setMetaId(standardizerReactMetaID(reactionsExtraInfo.get(reactionName).get("MERLIN_ID")));
			//			sbmlReaction.setMetaId("R_".concat(splitName[splitName.length-1]));

			/** write bounds as palsson does*/
			writeBounds(ogreaction, sbmlReaction, listParameters, unitDef);

			//			/** write reaction equation as note*/
			//			String equation = ogreaction.getName().split("__")[1];
			//			if(equation!= null){
			//				XMLNode equationNote = new XMLNode(new XMLTriple("p", "", "html"));
			//				equationNote.addChild(new XMLNode(EQUATION_PREFIX.concat(equation.substring(1, equation.length()-1))));
			//				sbmlReaction.setNotes(equationNote);
			//			}

			/** write the annotations */
			writeReactionAnnotations(ogreaction, sbmlReaction);

			/**geneProductAssociation*/
			writeGeneProductAssociation(ogreaction,sbmlReaction);

			/**products*/
			writeProductsOrReactants(ogreaction, sbmlReaction, true, rId, model, new_metabolites_names_p);

			/**reactants*/
			writeProductsOrReactants(ogreaction, sbmlReaction, false, rId, model, new_metabolites_names_r);

			try{
				model.addReaction(sbmlReaction);
			}catch(Exception e){
				exceptionsMap.put(rId, e);
			}
		}
		model.setListOfParameters(listParameters);

		if(!exceptionsMap.isEmpty())
			throw new JSBMLWriterException(exceptionsMap);
	}

	Map<String, Exception> exceptionsMap = new HashMap<String, Exception>();


	/**
//	 * This is an auxiliary method that writes the reactions annotations
//	 * @param ogreaction A ReactionCI object
//	 * @param sbmlReaction A Reaction object
//	 */
	public void writeReactionAnnotations(ReactionCI ogreaction, Reaction sbmlReaction){

		String keggID = ogreaction.getId().split("_")[1];
		
		Map<Qualifier, Map<ExternalRefSource,List<String>>> annotationData = new HashMap<>();
		Map<ExternalRefSource,List<String>> annotationElement = new HashMap<>();

		if(validateKeggID(keggID, false)) {
			
			List<String> keggIDs = new ArrayList<>();
			keggIDs.add(keggID);
			annotationElement.put(ExternalRefSource.KEGG_REACTION, keggIDs);
			
			annotationData.put(Qualifier.BQB_IS, annotationElement);
		}
		
		String ecNumbersString = ogreaction.getEc_number();
		
		if(ecNumbersString.length()>0){
			
			annotationElement = new HashMap<>();

			String[] ecNumbers = ecNumbersString.substring(1, ecNumbersString.length()-1).split(",");
				
			annotationElement.put(ExternalRefSource.EC_Code, Arrays.asList(ecNumbers));
			annotationData.put(Qualifier.BQB_IS_RELATED_TO, annotationElement);
		}
		
		sbmlReaction.setAnnotation(writeAnnotations(annotationData));
		

		//		XMLNode rdfNode = null ;

		//		MiriamLink link = new MiriamLink();
		//		
		//		link.setAddress("http://www.ebi.ac.uk/miriamws/main/MiriamWebServices");
		//		
		//		
		//		link.convertURN("urn:lsid:ec-code.org:1.1.1.1");

	}

	/**
//	 * This is an auxiliary method that writes the reactions geneProductAssociations
//	 * @param ogreaction A ReactionCI object
//	 * @param sbmlReaction A Reaction object
//	 */
	public void writeGeneProductAssociation(ReactionCI reaction, Reaction sbmlReaction) {

		String geneRulesString = reaction.getGeneRuleString().trim();

		if(!geneRulesString.equals("")) {

			FBCReactionPlugin fbcPlugin = (FBCReactionPlugin) sbmlReaction.getPlugin("fbc");
			GeneProductAssociation geneProductAssociation = new GeneProductAssociation(levelAndVersion.getLevel(), levelAndVersion.getVersion());

			if(geneRulesString.contains(" or ") || geneRulesString.contains(" and ")) {

				geneRulesString = geneRulesString.trim().substring(1, geneRulesString.length()-1).trim();

				if(!geneRulesString.contains(" or ")){

					geneRulesString = geneRulesString.replace(")","").replace("(","").trim();
					And and = new And();
					String[] genes = geneRulesString.split(" and ");

					for(String gene : genes){
						GeneProductRef geneRef = new GeneProductRef();
						String geneID = standardGeneID(gene.trim());
						geneRef.setGeneProduct(geneID);
						and.addAssociation(geneRef);
					}
					geneProductAssociation.setAssociation(and);
				}
				else if(!geneRulesString.contains(" and ")){

					geneRulesString = geneRulesString.replace(")","").replace("(","").trim();
					Or or = new Or();
					String[] genes = geneRulesString.split(" or ");

					for(String gene : genes){
						GeneProductRef geneRef = new GeneProductRef();
						String geneID = standardGeneID(gene.trim());
						geneRef.setGeneProduct(geneID);
						or.addAssociation(geneRef);
					}
					geneProductAssociation.setAssociation(or);
				}
				else{
					Or or = new Or();
					String[] rules = geneRulesString.split(" or ");

					for(String rule : rules){

						Association association = null;

						if(!rule.contains(" and ") && !rule.contains(" or ")){
							GeneProductRef geneRef = new GeneProductRef();
							String geneID = standardGeneID(rule.replace(")","").replace("(","").trim());
							geneRef.setGeneProduct(geneID);
							association = geneRef;
						}
						else{	
							And and = new And();
							String[] genes = rule.trim().split(" and ");

							for(String gene : genes){
								GeneProductRef geneRef = new GeneProductRef();
								String geneID = standardGeneID(gene.replace(")","").replace("(","").trim());
								geneRef.setGeneProduct(geneID);
								and.addAssociation(geneRef);
							}
							association = and;
						}
						or.addAssociation(association);

					}
					geneProductAssociation.setAssociation(or);
				}
			}
			else {
				GeneProductRef geneRef = new GeneProductRef();
				String geneID = standardGeneID(geneRulesString);
				geneRef.setGeneProduct(geneID);
				geneProductAssociation.setAssociation(geneRef);
			}
			fbcPlugin.setGeneProductAssociation(geneProductAssociation);
		}
	}


	/**
	 * @param oldGeneID
	 * @return
	 */
	public String standardGeneID(String oldGeneID) {
		
//		String[] splited = oldGeneID.split("_");
//		String newGeneID = "";
//
//		if(splited.length>2){
//
//			if(splited[splited.length-1].startsWith("\\d"))
//				newGeneID = "G_".concat(splited[splited.length-2]).concat("_").concat(splited[splited.length-1]);
//
//			else
//				newGeneID = "G_".concat(splited[splited.length-1]);
//		}
//
//		else if(splited.length==2 && !splited[1].startsWith("\\d"))
//			newGeneID = "G_".concat(splited[1]);
//
//		else
//			newGeneID = "G_".concat(oldGeneID);
		
		String newGeneID = "G_".concat(oldGeneID);

		if(oldGeneID.split("\\.").length==2){
			newGeneID = "G_".concat(oldGeneID.substring(0, oldGeneID.indexOf(".")));
		}
		else if(oldGeneID.split("\\.").length>2){
			newGeneID = "G_".concat(String.format("%05d", genesNumber));
		}
		
		return newGeneID;
	}


	/**
	 * @param sourceID
	 * @param externalSource
	 * @return
	 */
	public Annotation writeAnnotation(String sourceID, Qualifier qualifier, ExternalRefSource externalSource){

		Annotation annotation = new Annotation();

		ExternalRef referenceSource = new ExternalRef(externalSource);

		String resource = referenceSource.getExternalRefSource().getIdentifierCode(sourceID);

		CVTerm bqmodel = new CVTerm(qualifier, resource);
		annotation.addCVTerm(bqmodel);

		return annotation;
	}


	/**
	 * @param sourceID
	 * @param externalSource
	 * @return
	 */
	public Annotation writeAnnotations(Map<Qualifier, Map<ExternalRefSource,List<String>>> annotationData){

		Annotation annotation = new Annotation();

		for(Qualifier qualifier : annotationData.keySet()) {

			CVTerm annotationNode = new CVTerm();
			annotationNode.setQualifier(qualifier);

			Map<ExternalRefSource,List<String>> resources = annotationData.get(qualifier);

			for(ExternalRefSource externalSource : resources.keySet()) {

				ExternalRef referenceSource = new ExternalRef(externalSource);

				for(String sourceID : resources.get(externalSource)) {

					String resource = referenceSource.getExternalRefSource().getIdentifierCode(sourceID.trim());
					annotationNode.addResource(resource);
				}
			}
			annotation.addCVTerm(annotationNode);
		}

		return annotation;
	}


	/**
	 * This is an auxiliary method that writes the bounds and geneRules
	 * @param ogreaction A ReactionCI object
	 * @param sbmlReaction A Reaction object
	 * @param unitDef A UnitDefinition object
	 */
	public void writeBounds(ReactionCI ogreaction, Reaction sbmlReaction, ListOf<Parameter> listParameters, UnitDefinition unitDef){

		if(listParameters.isEmpty()){
			
			Parameter defaultLB = new Parameter("merlin_defaulLB");
			defaultLB.setConstant(PARAMETER_IS_CONSTANT);
			defaultLB.setName("merlin_default_lowerBound");
			defaultLB.setUnits(unitDef);
			defaultLB.setValue(-DEFAULT_BOUND_VALUE);
			listParameters.add(0, defaultLB);

			Parameter boundZero = new Parameter("merlin_0_bound");
			boundZero.setConstant(PARAMETER_IS_CONSTANT );
			boundZero.setName("merlin_0_bound");
			boundZero.setUnits(unitDef);
			boundZero.setValue(0);
			listParameters.add(1, boundZero);

			Parameter defaultUB = new Parameter("merlin_defaulUB");
			defaultUB.setConstant(PARAMETER_IS_CONSTANT);
			defaultUB.setName("merlin_default_upperBound");
			defaultUB.setUnits(unitDef);
			defaultUB.setValue(DEFAULT_BOUND_VALUE);
			listParameters.add(2, defaultUB);
		}
		
		Parameter lowerP = new Parameter();
		Parameter upperP = new Parameter();
		lowerP.setConstant(PARAMETER_IS_CONSTANT);
		upperP.setConstant(PARAMETER_IS_CONSTANT);

		FBCReactionPlugin fbcBounds = new FBCReactionPlugin(sbmlReaction);

		if(overrideConstraints.get(ogreaction.getId()) != null) {
			Double lb = overrideConstraints.get(ogreaction.getId()).getLowerLimit();
			Double ub = overrideConstraints.get(ogreaction.getId()).getUpperLimit();

			//LOWER BOUND
			if(lb.equals(-DEFAULT_BOUND_VALUE) || lb<-9999)
				lowerP = listParameters.get(0).clone();
			else if(lb.equals((double) 0))
				lowerP = listParameters.get(1).clone();
			else
				lowerP.setValue(lb);

			//UPPER BOUND
			if(ub.equals(DEFAULT_BOUND_VALUE) || ub>9999)
				upperP = listParameters.get(2).clone();
			else if(ub.equals((double) 0))
				upperP = listParameters.get(1).clone();
			else
				upperP.setValue(ub);
		}
		else if(container.getDefaultEC().get(ogreaction.getId()) != null){
			Double lb = container.getDefaultEC().get(ogreaction.getId()).getLowerLimit();
			Double ub = container.getDefaultEC().get(ogreaction.getId()).getUpperLimit();

			//LOWER BOUND
			if(lb.equals(-DEFAULT_BOUND_VALUE) || lb<-9999)
				lowerP = listParameters.get(0).clone();
			else if(lb.equals((double) 0))
				lowerP = listParameters.get(1).clone();
			else
				lowerP.setValue(lb);

			//UPPER BOUND
			if(ub.equals(DEFAULT_BOUND_VALUE) || ub>9999)
				upperP = listParameters.get(2).clone();
			else if(ub.equals((double) 0))
				upperP = listParameters.get(1).clone();
			else
				upperP.setValue(ub);

		}
		else{

			if(ogreaction.getReversible())
				lowerP = listParameters.get(0).clone();
			else
				lowerP = listParameters.get(1).clone();

			upperP = listParameters.get(2).clone();
		}

		if(lowerP.getId()==null || lowerP.getId().equals("")){

			lowerP.setId(sbmlReaction.getId().concat("_lb"));
			lowerP.setName(sbmlReaction.getName().concat(" lower bound"));

			if(writeUnits){
				lowerP.setUnits(unitDef);
			}

			listParameters.add(lowerP);
		}
		if(upperP.getId()==null || upperP.getId().equals("")){

			upperP.setId(sbmlReaction.getId().concat("_ub"));
			upperP.setName(sbmlReaction.getName().concat(" upper bound"));

			if(writeUnits){
				upperP.setUnits(unitDef);
			}
			listParameters.add(upperP);
		}

		fbcBounds.setLowerFluxBound(lowerP);
		fbcBounds.setUpperFluxBound(upperP);

		sbmlReaction.addPlugin(FBCConstants.namespaceURI, fbcBounds);


		//			LocalParameter objectiveCoeff = new LocalParameter("OBJECTIVE_COEFFICIENT");
		//			objectiveCoeff.setName("");
		//			
		//			LocalParameter fluxValue = new LocalParameter("FLUX_VALUE");
		//			fluxValue.setName("");
		//			if(writeUnits) fluxValue.setUnits(unit_def);
		//		
		//			LocalParameter reducedCost = new LocalParameter("REDUCED_COST");
		//			reducedCost.setName("");

		//			KineticLaw law = new KineticLaw();	
		//			law.addLocalParameter(lowerP);
		//			law.addLocalParameter(upperP);				
		////			law.addLocalParameter(objectiveCoeff);
		////			law.addLocalParameter(fluxValue);
		////			law.addLocalParameter(reducedCost);
		//			String math = "<math xmlns=\"http://www.w3.org/1998/Math/MathML\"><ci> FLUX_VALUE </ci></math>";
		////			String math = "<math xmlns=\"http://www.w3.org/1998/Math/MathML\"></math>";
		//			ASTNode mathnode = JSBML.readMathMLFromString(math);			
		//			law.setMath(mathnode);				
		//			sbmlReaction.setKineticLaw(law);

	}

	/**
	 * This is an auxiliary method that writes the products or the reactants
	 * @param ogreaction A ReactionCI object
	 * @param sbmlreaction A Reaction object
	 * @param isProduct A boolean that tells if we are inserting a product (true) or a reactant (false)
	 * @param z The reactionID of the current reaction
	 * @param new_metabolites_names A structure with the names of the products or of the reactants
	 */
	public void writeProductsOrReactants(ReactionCI ogreaction, Reaction sbmlreaction, boolean isProduct, String z, Model model, HashMap<String, MetaboliteCI> new_metabolites_names){
		Map<String, StoichiometryValueCI> aux;
		if(isProduct) aux = ogreaction.getProducts();
		else aux = ogreaction.getReactants();

		for(String specie : aux.keySet()){
			SpeciesReference compound = new SpeciesReference(levelAndVersion.getLevel(), levelAndVersion.getVersion());
			String specieName = specie;
			if(needsToBeStandardized) specieName = standardizeMetId(specie);
			compound.setSpecies(specieName);
			Double stoichValue;
			if(isProduct){
				stoichValue = aux.get(specie).getStoichiometryValue();
//				if(stoichValue<0)
//					stoichValue = -stoichValue;
				compound.setStoichiometry(stoichValue);
				compound.setConstant(true);
				sbmlreaction.addProduct(compound);
			} else{
				stoichValue = aux.get(specie).getStoichiometryValue();
//				if(stoichValue>0)
//					stoichValue = -stoichValue;
				compound.setStoichiometry(stoichValue);
				compound.setConstant(true);
				sbmlreaction.addReactant(compound);
			}
		}

		if(new_metabolites_names.containsKey(z)){
			SpeciesReference compound = new SpeciesReference(levelAndVersion.getLevel(), levelAndVersion.getVersion());
			compound.setSpecies(new_metabolites_names.get(z).getId());
			compound.setStoichiometry(1.0);
			//			boolean constant = model.getSpecies(new_metabolites_names.get(z).getId()).isConstant();
			//			boolean constant = true;
			compound.setConstant(true);
			if(isProduct) sbmlreaction.addProduct(compound);
			else sbmlreaction.addReactant(compound);
		}
	}

	/**
	 * Method that standardizes a reactionID
	 * @param reaction The reactionID
	 * @param reactionTypeEnum The type of reaction
	 * @return The new reactionID
	 */
	private String standardizerReactId(String reaction,
			ReactionTypeEnum reactionTypeEnum) {
		
//		Integer id = Integer.parseInt(reaction);
//		String newName = String.format("%06d", id);
		
		String newName = reaction;

		if(!(reaction.startsWith("r_") || reaction.startsWith("R_")))
			newName = "R_" + reaction;

		newName = newName.replace("-", "_");
		newName = newName.replace("(", "_");
		newName = newName.replace(")", "_");
		newName = newName.replace(",", "_");
		newName = newName.replace(".", "_");
		newName = newName.replace("[", "_");
		newName = newName.replace("]", "_");
		newName = newName.replace(" ", "_");

		return newName;
	}

	/**
	 * Method that standardizes a metaboliteID
	 * @param id The metabolite ID
	 * @return The new metaboliteID
	 */
	private String standardizeMetId(String id) {

		if(!(id.startsWith("m_") || id.startsWith("M_")))
			id = "M_"+ id;
		id = id.replace("-", "_");
		id = id.replace("(", "_");
		id = id.replace(")", "_");
		id = id.replace(",", "_");
		id = id.replace(".", "_");
		id = id.replace("[", "_");
		id = id.replace("]", "_");
		id = id.replace(" ", "_");
		id = id.replace("=", "");
		id = id.replace(":", "_");
		id = id.replace("'", "_");
		return id;
	}

	/**
	 * method that verifies if a keggID is valid
	 * @param keggID
	 * @return
	 */
	public boolean validateKeggID(String keggID, boolean isCompound) {

		if(isCompound)
			return keggID.matches("[GDC]\\d{5}");
		else
			return keggID.matches("R\\d{5}");
	}

	/**
	 * This method validates a XMLNode object
	 * @param node A XMLNode object
	 * @return A boolean telling if the XMLNode is or not valid
	 */
	public boolean validateNode(XMLNode node){
		for(String prefix : ignoredNamespaces){
			String np = node.getPrefix();
			if(np.equalsIgnoreCase(prefix))
				return false;
		}
		return true;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the container
	 */
	public Container getContainer() {
		return container;
	}

	/**
	 * @param container the container to set
	 */
	public void setContainer(Container container) {
		this.container = container;
	}

	/**
	 * @return the taxonomyID
	 */
	public String getTaxonomyID() {
		return taxonomyID;
	}

	/**
	 * @param String the taxonomyID to set
	 */
	public void setTaxonomyID(String taxID) {
		this.taxonomyID = taxID;
	}

	/**
	 * @return the modelID
	 */
	public String getModelID() {
		return modelID;
	}

	/**
	 * @param String the modelID to set
	 */
	public void setModelID(String modelID) {
		this.modelID = modelID;
	}


	/**
	 * @return the pubmedID
	 */
	public String getPubmedID() {
		return pubmedID;
	}

	/**
	 * @param String the pubmedID to set
	 */
	public void setPubmedID(String pubmedID) {
		this.pubmedID = pubmedID;
	}

	/**
	 * @return A boolean telling if is palsoon specific (true) or not (false)
	 */
	public boolean isPalssonSpecific() {
		return palssonSpecific;
	}

	/**
	 * @param palssonSpecific A boolean to set if it is palsson specific (true) or not (false)
	 */
	public void setPalssonSpecific(boolean palssonSpecific) {
		this.palssonSpecific = palssonSpecific;
	}

	/**
	 * @return A boolean telling if it's supposed to ignore the notes field (true) or not (false)
	 */
	public boolean isIgnoreNotesField() {
		return ignoreNotesField;
	}

	/**
	 * @param ignoreNotesField A boolean to set if it is supposed to ingore the notes field (true) or not (false)
	 */
	public void setIgnoreNotesField(boolean ignoreNotesField) {
		this.ignoreNotesField = ignoreNotesField;
	}

	/**
	 * @return A boolean telling if it is supposed to ignore the CellDesigner annotations
	 */
	public boolean isIgnoreCellDesignerAnnotations() {
		return ignoreCellDesignerAnnotations;
	}

	/**
	 * @param ignoreCellDesignerAnnotations A boolean to set if it is supposed to ignore the CellDesigner annotations
	 */
	public void setIgnoreCellDesignerAnnotations(boolean ignoreCellDesignerAnnotations) {
		this.ignoreCellDesignerAnnotations = ignoreCellDesignerAnnotations;
		if(ignoreCellDesignerAnnotations)
			ignoredNamespaces.add(CELLDESIGNER_NAMESPACE_PREFIX);
	}
	
	/**
	 * @return
	 */
	public SBMLDocument getDocument() {
		
		return this.sbmlDocument;
	}

}
