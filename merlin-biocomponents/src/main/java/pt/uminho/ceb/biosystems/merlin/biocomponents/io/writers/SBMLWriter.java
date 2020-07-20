package pt.uminho.ceb.biosystems.merlin.biocomponents.io.writers;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.CVTerm.Qualifier;
import org.sbml.jsbml.CVTerm.Type;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import pt.uminho.ceb.biosystems.merlin.biocomponents.container.SBML_Model;
import pt.uminho.ceb.biosystems.merlin.biocomponents.io.Enumerators.SBMLLevelVersion;
import pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers.ContainerBuilder;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ReactionContainer;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelCompartmentServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelPathwaysServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.merlin.utilities.RulesParser;
import pt.uminho.ceb.biosystems.merlin.utilities.Utilities;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


/**
 * @author Oscar
 *
 */
public class SBMLWriter {

	private static final String MERLIN_NAME = "merlin - www.merlin-sysbio.org";
	private static final boolean concatenate = true;
	private boolean isCompartmentalisedModel;
	private Map<Integer, List<MetaboliteContainer>> reactionMetabolites;
	private Map<Integer, ReactionContainer> reactions;
	private Map<String, CompartmentContainer> compartments;
	private int compartmentCounter;
	private Map<String, String> compartmentID;
	private String  outsideID;
	private boolean generateFormulae;
	private String biomassEquationID;
	private Map<String,String> metabolites_formula;
	private SBMLLevelVersion levelAndVersion = SBMLLevelVersion.L2V1;
	private String filePath;
	private String sbmlFileID;

	private List<String> genes;
	private SBMLDocument sbmlDocument;
	private String workspaceName;
	private Map<String, String> reactionLabels;

	/**
	 * @param DatabaseAccess
	 * @param filePath
	 * @param sbmlFileID
	 * @param isCompartmentalisedModel
	 * @param generateFormulae
	 * @param biomassEquationID
	 * @param levelAndVersion
	 */
	public SBMLWriter(String workspaceName, String filePath, String sbmlFileID, boolean isCompartmentalisedModel,
			boolean generateFormulae, String biomassEquationID, SBMLLevelVersion levelAndVersion) {

		this.workspaceName = workspaceName;
		this.compartmentCounter = 1;
		this.compartmentID = new TreeMap<String, String>();
		this.isCompartmentalisedModel = isCompartmentalisedModel;
		this.compartments = new HashMap<String, CompartmentContainer>();
		this.reactionMetabolites = new HashMap<>();
		this.reactions = new HashMap<>();
		this.outsideID=null;
		this.generateFormulae = generateFormulae;
		this.metabolites_formula = new TreeMap<String, String>();		
		this.setBiomassEquationName(biomassEquationID);
		this.filePath = filePath;
		this.sbmlFileID = sbmlFileID;
		this.levelAndVersion = levelAndVersion;
		this.reactionLabels = new HashMap<>();

		this.genes = new ArrayList<>();
	}


	/**
	 * @throws Exception 
	 * @throws  
	 * 
	 */
	public void getDataFromDatabase() throws Exception {

		this.getReactions();
		this.getStoichiometry();
		
	}

	/**
	 * <p>This method converts the model data to the SBML</p>
	 * <p>native format and returns it as an SBML <code>String</code></p> 
	 * @param addAllNotes 
	 * 
	 * @return <code>String</code> representation of the SBML model.
	 * @throws Exception 
	 */
	public void toSBML(boolean addAllNotes) throws Exception{

		SBML_Model sbmlModel = new SBML_Model(sbmlFileID.replaceAll("[^a-zA-Z0-9]", ""), levelAndVersion );
		this.getCompartments(sbmlModel);
		this.buildModel(sbmlModel, addAllNotes);

		this.sbmlDocument = this.createSBMLDocument(sbmlModel, filePath);

		if(this.generateFormulae)
			this.generateFormulae(filePath);
	}


	/**
	 * @param sbmlModel
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws SBMLException
	 * @throws XMLStreamException
	 */
	public SBMLDocument createSBMLDocument(SBML_Model sbmlModel, String filePath) throws FileNotFoundException, SBMLException, XMLStreamException {

		SBMLDocument document = new SBMLDocument(levelAndVersion.getLevel(), levelAndVersion.getVersion());
		document.setModel(sbmlModel.getModel());
		document.addDeclaredNamespace("xmlns:html", "http://www.w3.org/1999/xhtml");

		org.sbml.jsbml.SBMLWriter sbmlwriter = new org.sbml.jsbml.SBMLWriter();
		sbmlwriter.setProgramName(MERLIN_NAME);
		String merlinVersion = Utilities.getMerlinVersion();

		if(merlinVersion!=null)
			sbmlwriter.setProgramVersion(merlinVersion);

		OutputStream out = new FileOutputStream(filePath);
		sbmlwriter.write(document, out);
		try {
			out.close();
		} 
		catch (IOException e) {

			e.printStackTrace();
		}
		return document;
	}

	/**
	 * @param link
	 * @param conditions
	 * @throws Exception 
	 * @throws IOException 
	 */
	private void getReactions() throws IOException, Exception {

		try {

			Map<Integer, ReactionContainer> result = ModelReactionsServices.getReactionsByReactionId(this.workspaceName, this.isCompartmentalisedModel);
			
			this.reactions.putAll(result);

			List<Pair<Integer, String>> result2 = ModelReactionsServices.getReactionHasEnzyme2(this.workspaceName, this.isCompartmentalisedModel);

			for(Pair<Integer, String> pair : result2){
				
				Integer reactionId = pair.getA();
				String ecNumber = pair.getB();

				if(this.reactions.containsKey(reactionId) && !ecNumber.contains(".-")) {

					ReactionContainer reactionContainer = this.reactions.get(reactionId);

					List<ProteinContainer> enzymeSet = new ArrayList<>();
					if(reactionContainer.getEnzymes()!=null)
						enzymeSet = reactionContainer.getEnzymes();

					if(ecNumber!=null) {

						enzymeSet.add(new ProteinContainer(ecNumber));
						reactionContainer.setEnzymes(enzymeSet);
					}
					reactionContainer = this.reactions.put(reactionId, reactionContainer);
				}
			}
		
			Map<Integer, List<PathwayContainer>> map = ModelPathwaysServices.getPathwaysByReaction(this.workspaceName);

			for(Integer reactionId : map.keySet()) {
			
					if(this.reactions.containsKey(reactionId)) {
	
						ReactionContainer reactionContainer = this.reactions.get(reactionId);
						
						for(PathwayContainer pathway : map.get(reactionId))
							reactionContainer.addPathway(pathway);
						
						reactionContainer = this.reactions.put(reactionId, reactionContainer);
					}
			}

			List<String[]> result3 = ModelReactionsServices.getReactionGenes(this.workspaceName);

			for(int i=0; i<result3.size(); i++){
				String[] list = result3.get(i);

				if(this.reactions.containsKey(Integer.parseInt(list[0])) && !list[3].contains(".-")) {

					ReactionContainer reactionContainer = this.reactions.get(Integer.parseInt(list[0]));

					String locus= list[2], geneName = null;

					if(list[1]!=null)
						geneName = list[1].replace(" ","").replace(",","_").replace("/","_").replace("\\","_").trim();//replace("-","_").trim();

					//					this.addGeneCI(locus, geneName);
					reactionContainer.addGene(locus, geneName);
					reactionContainer = this.reactions.put(Integer.parseInt(list[0]), reactionContainer);

				}
			}
		}
		catch (SQLException e) {

			e.printStackTrace();
		}
	}

	/**
	 * @param link
	 * @param conditions
	 * @throws Exception 
	 */
	private void getStoichiometry() throws Exception {

			List<String[]> result = ModelReactionsServices.getStoichiometryInfo(this.workspaceName, this.isCompartmentalisedModel);

			for(int i=0; i<result.size(); i++){
				String[] list = result.get(i);

				if(this.reactions.containsKey(Integer.parseInt(list[1]))) {

					if(!list[3].contains("m") && !list[3].contains("n")) {

						List<MetaboliteContainer> metabolitesContainer = new ArrayList<MetaboliteContainer>();

						if(this.reactionMetabolites.containsKey(Integer.parseInt(list[1])))
							metabolitesContainer = this.reactionMetabolites.get(Integer.parseInt(list[1]));

						MetaboliteContainer metabolite = new MetaboliteContainer(Integer.parseInt(list[2]), list[4], list[5], 
								Double.parseDouble(list[3]),list[8], Integer.valueOf(list[9]), list[10], list[6]);
						
						metabolitesContainer.add(metabolite);

						this.reactionMetabolites.put(Integer.parseInt(list[1]), metabolitesContainer);
						
					}
					else {

						this.reactionMetabolites.remove(Integer.parseInt(list[1]));
						this.reactions.remove(Integer.parseInt(list[1]));
					}
				}
			}
	}


	/**
	 * @param link
	 * @param conditions
	 * @throws Exception 
	 */
	private void getCompartments(SBML_Model sbml) throws Exception {
		
		List<CompartmentContainer> result = ModelCompartmentServices.getCompartmentsInfo(this.workspaceName);

		for(CompartmentContainer container : result) {

			this.compartments.put(container.getCompartmentID() + "", container);

			if((container.getName().equalsIgnoreCase("extracellular") && this.isCompartmentalisedModel) || (container.getName().equalsIgnoreCase("outside") && !this.isCompartmentalisedModel))
				this.outsideID=this.getCompartmentID(container.getCompartmentID()+"", sbml);
		}
	}

	/**
	 * @param compartment
	 * @return
	 */
	private String getCompartmentID(String compartment, SBML_Model sbml) {

		if(!this.compartmentID.containsKey(compartment)) {

			String id = SBMLWriter.buildID("C_", this.compartmentCounter);
			sbml.addCompartment(id, compartment, this.outsideID);
			//sbml.addCompartment(id, this.compartments.get(compartment).getName(), this.outsideID);
			this.compartmentID.put(compartment, id);
			this.compartmentCounter++ ;

		}
		return this.compartmentID.get(compartment);
	}

	/**
	 * @param addAllNotes 
	 * @throws SQLException 
	 * 
	 */
	public void buildModel(SBML_Model sbmlModel, boolean addAllNotes) throws SQLException {

		int reactionsCounter = 1 ;
		int metabolitesCounter = 1;
		Map<String,String> compoundCompartmentID = new TreeMap<>();

		for(int reaction_id : this.reactions.keySet()) {

			ReactionContainer reaction = this.reactions.get(reaction_id);
			//String name = reaction.getExternalIdentifier()+"__("+reaction.getEquation().replace(" ", "")+")";
			String name = reaction.getEquation().replace(" ", "");

			String rid = SBMLWriter.buildID("R_", reactionsCounter)/*+"_"+reaction.getName().replace(" ", "_").replace("\t", "_").replace("-", "_")*/;

			boolean biomassEquation = reaction.getExternalIdentifier().equalsIgnoreCase(this.getBiomassEquationID());

			double upper_bound = 999999;
			double lower_bound = 0;

			if(reaction.getLowerBound()!= null)
				lower_bound = reaction.getLowerBound();
			else 
				if(reaction.isReversible())
					lower_bound = -999999;

			if(reaction.getUpperBound()!= null)
				upper_bound = reaction.getUpperBound();


			if(reaction.getGenes()!= null)
				for(Pair<String,String> gene : reaction.getGenes())			
					if(!genes.contains(gene.getA()))
						genes.add(gene.getA());

			String geneRule = reaction.getGeneRule();

			//			System.out.println(reaction.getGeneRule());

			//			if(reaction.getGeneRule()!=null && !reaction.getGeneRule().isEmpty()){
			//				List<List<Pair<String,String>>> rule = ModelAPI.parseBooleanRule(reaction.getGeneRule(), stmt);
			//				geneRule = Utilities.parseRuleListToString(rule);
			//			}

			if(geneRule!= null && !geneRule.isEmpty())
				reaction.setGeneRule(geneRule);


			XMLNode reactionNote = SBMLWriter.getReactionNote(reaction.getGenes(), reaction.getPathways(), reaction.getEnzymes(), reaction.getNotes(), addAllNotes, reaction.getGeneRule(), this.levelAndVersion);

			this.reactionLabels.put(reaction.getExternalIdentifier(), SBMLWriter.buildID("",reactionsCounter).replace("_", ""));
			
			sbmlModel.addReaction(rid, name, reaction.getExternalIdentifier(), reaction.isReversible(), reactionNote, lower_bound, upper_bound, biomassEquation);
			reactionsCounter++ ;

//			if(this.reactions.get(reaction_id).getEnzymes() != null) {
//
//				for(EnzymeContainer enzyme : this.reactions.get(reaction_id).getEnzymes()) {
//					
//					String enzyme_surrogate = enzyme.getExternalIdentifier();
//					if(reaction.getLocalisation()!=null)
//						enzyme_surrogate = enzyme_surrogate.concat("_").concat(this.getCompartmentID(reaction.getLocalisation().getCompartmentID()+"", sbmlModel));
//					String eid;
//					if(enzymesID.containsKey(enzyme_surrogate)) {
//
//						eid = enzymesID.get(enzyme_surrogate);
//					}
//					else {
//
//						eid = SBMLWriter.buildID("E_", enzymesCounter);
//
//						sbmlModel.addSpecies(eid, enzyme.getExternalIdentifier(), this.getCompartmentID(reaction.getLocalisation().getCompartmentID()+"", sbmlModel), SBMLWriter.getAnnotation(enzyme.getExternalIdentifier(), ""));
//						enzymesID.put(enzyme_surrogate, eid);
//						enzymesCounter++;
//					}
//					sbmlModel.setReactionEnzyme(eid, rid);
//				}
//			}

			if(this.reactionMetabolites.get(reaction_id) != null) {

				for(MetaboliteContainer metabolite : this.reactionMetabolites.get(reaction_id)) {

					String metabolite_surrogate = metabolite.getMetaboliteID()+"".concat("_").concat(metabolite.getCompartment_name());
					String mid;
					if(compoundCompartmentID.containsKey(metabolite_surrogate)) {

						mid = compoundCompartmentID.get(metabolite_surrogate);
					}
					else {

						mid = SBMLWriter.buildID("M_", metabolitesCounter);
						compoundCompartmentID.put(metabolite_surrogate,mid);

						String sbmlName="";

						if(metabolite.getExternalIdentifier() != null) {

							sbmlName=sbmlName.concat(metabolite.getExternalIdentifier());
						}

						if(metabolite.getName() != null) {

							if(sbmlName!=null && !sbmlName.isEmpty())
								sbmlName=sbmlName.concat("_");

							sbmlName=sbmlName.concat(metabolite.getName());
						}

						if(metabolite.getFormula() != null) {

							if(sbmlName!=null && !sbmlName.isEmpty())
								sbmlName=sbmlName.concat("_");

							sbmlName=sbmlName.concat(metabolite.getFormula());
						}
						sbmlModel.addSpecies( mid, sbmlName, this.getCompartmentID(metabolite.getCompartmentID()+"", sbmlModel), SBMLWriter.getAnnotation(metabolite.getExternalIdentifier(), metabolite.getFormula()));

						metabolitesCounter++ ;

						metabolites_formula.put(mid, metabolite.getFormula());
					}

					sbmlModel.setReactionCompound(mid, rid, new Double(metabolite.getStoichiometric_coefficient()));

				}
			}
			else {

//				System.err.println(reaction_id +"\t" +reaction.getExternalIdentifier());
			}
		}

	}

	/**
	 * @param filePath
	 */
	public void generateFormulae(String filePath){

		if(this.metabolites_formula.size()>0) {

			try {

				FileWriter fstream = new FileWriter(filePath.replace(".xml","")+"_formulae.txt");
				BufferedWriter out = new BufferedWriter(fstream);

				for(String mid:metabolites_formula.keySet()) {

					out.write(mid+"\t"+metabolites_formula.get(mid)+"\n");
				}
				out.close();
			}
			catch (Exception e){

				System.err.println("Error: " + e.getMessage());
			}
		}
	}

	/**
	 * @param type
	 * @param counter
	 * @return
	 */
	private static String buildID(String type, int counter) {

		if(counter<10 ){return type.concat("0000"+counter);}
		if(counter<100) {return type.concat("000"+counter);}
		if(counter<1000) {return type.concat("00"+counter);}
		if(counter<10000) {return type.concat("0"+counter);}

		return type.concat("_"+counter);
	}

	/**
	 * @param urn_id
	 * @param formula
	 * @return
	 */
	public static Annotation getAnnotation(String urn_id, String formula) {

		Annotation annotation = new Annotation();
		CVTerm cvTerm = new CVTerm(Type.BIOLOGICAL_QUALIFIER, Qualifier.BQB_IS);
		annotation.addCVTerm(cvTerm);

		if(urn_id!=null) {

			if(urn_id.startsWith("C")) {

				if(formula!=null) {

					cvTerm.addResource("FORMULA:" + formula.toUpperCase());
				}
				cvTerm.addResource("urn:miriam:kegg.compound:" + urn_id);
				//cvTerm.addResource("<url:element>http://www.genome.jp/dbget-bin/www_bget?gl:"+urn_id+ "</url:element>");
				//annotation.appendNoRDFAnnotation("http://www.genome.jp/dbget-bin/www_bget?gl:"+urn_id);
				cvTerm.addResource("http://www.genome.jp/dbget-bin/www_bget?cpd:"+urn_id);
			}

			else if(urn_id.startsWith("G")) {

				if(formula!=null) {

					cvTerm.addResource("FORMULA: " + formula.toUpperCase());
				}
				cvTerm.addResource("urn:miriam:kegg.glycan:" + urn_id);
				cvTerm.addResource("http://www.genome.jp/dbget-bin/www_bget?gl:"+urn_id);
			}

			else if(urn_id.startsWith("D")) {

				if(formula!=null) {

					cvTerm.addResource("FORMULA: " + formula.toUpperCase());
				}
				cvTerm.addResource("urn:miriam:kegg.drugs:" + urn_id);
				cvTerm.addResource("http://www.genome.jp/dbget-bin/www_bget?dr:"+urn_id);
			}

			else if (urn_id.contains("#")) {

				cvTerm.addResource("urn:miriam:tcdb:" + urn_id);
				cvTerm.addResource("http://www.tcdb.org/search/result.php?tc="+urn_id);
			}

			else if (urn_id.contains(".")) {

				cvTerm.addResource("urn:miriam:ec-code:" + urn_id);
				cvTerm.addResource("http://www.genome.jp/dbget-bin/www_bget?ec:"+urn_id);
			}
		}
		return annotation;

	}

	/**
	 * @param genes
	 * @param proteins
	 * @param pathways
	 * @param proteinClass
	 * @param notes
	 * @param addAllNotes
	 * @param geneRule
	 * @param levelAndVersion
	 * @return
	 */
	public static XMLNode getReactionNote(Set<Pair<String,String>> genes, // Set<String> proteins, 
			List<PathwayContainer> pathways, List<ProteinContainer> proteinClass, String notes, boolean addAllNotes, String geneRule, SBMLLevelVersion levelAndVersion) {

		XMLNode note = new XMLNode(new XMLTriple("notes"));
		note.addNamespace("html");

		String genesNotes = RulesParser.processReactionGenes(genes, concatenate);
		String proteinsNotes = "";//ContainerBuilder.processReactionProteins(proteins);
		String pathwaysNotes = ContainerBuilder.processReactionPathways(pathways);
		String enzymesNotes = ContainerBuilder.processReactionProteinClass(proteinClass);

		Set<String> notesList = ContainerBuilder.processReactionNotes(notes);

		String newGeneRule = geneRule;
		if(newGeneRule!= null && !levelAndVersion.equals(SBMLLevelVersion.L3V1))			
			newGeneRule = newGeneRule.replaceAll(" \\(", "_").replaceAll("\\)", "");

		//		System.out.println(newGeneRule);

		Set<XMLNode> gener = ContainerBuilder.getGeneRules(genesNotes, notesList, addAllNotes, newGeneRule);
		Set<XMLNode> proteinr = ContainerBuilder.getProteinRules(proteinsNotes, notesList, addAllNotes);
		Set<XMLNode> proteinc = ContainerBuilder.getPathwaysRules(pathwaysNotes, notesList, addAllNotes);
		Set<XMLNode> sub = ContainerBuilder.getProteinClassRules(enzymesNotes, notesList, addAllNotes);

		for(XMLNode x : gener)
			note.addChild(x);

		for(XMLNode x : proteinr)
			note.addChild(x);

		for(XMLNode x : proteinc)
			note.addChild(x);

		for(XMLNode x : sub)
			note.addChild(x);

		return note;		
	}


	/**
	 * @param notes
	 * @return
	 */
	public static Set<String> processReactionNotes(String notes) {

		Set<String> reactionNotes = new HashSet<>();
		//<html:p>GENE_ASSOCIATION: </html:p>
		//<html:p>PROTEIN_ASSOCIATION: "</html:p>
		//<html:p>SUBSYSTEM: S_Tyrosine__Tryptophan__and_Phenylalanine_Metabolism</html:p>
		//<html:p>PROTEIN_CLASS: 1.13.11.27</html:p>
		//<html:p>GENE_ASSOCIATION: ( YOL096C  and  YDR204W  and  YML110C  and  YGR255C  and  YOR125C  and  YGL119W  and  YLR201C )</html:p>
		//<html:p>PROTEIN_ASSOCIATION: ( Coq3-m and Coq4-m and Coq5-m and Coq6-m and Coq7-m and Coq8-m and Coq9-m )"</html:p>
		//<html:p>SUBSYSTEM: S_Quinone_Biosynthesis</html:p>
		//<html:p>PROTEIN_CLASS: </html:p>


		if(notes!=null && !notes.trim().isEmpty())
			if(notes.contains("|"))
				for(String note : notes.split(" \\| "))
					reactionNotes.add(note.replace(",","_").replace(" ","_").replace(":_",": ").replace("_AND_"," and ").replace("_OR_"," or ").trim());	 //.replace(")","").replace("(","_")

		return reactionNotes; 
	}


	/**
	 * @return the biomassEquationID
	 */
	public String getBiomassEquationID() {
		return biomassEquationID;
	}

	/**
	 * @param biomassEquationID the biomassEquationID to set
	 */
	public void setBiomassEquationName(String biomassEquationID) {
		this.biomassEquationID = biomassEquationID;
	}


	/**
	 * @return
	 */
	public SBMLDocument getDocument() {

		return this.sbmlDocument;
	}

	public Map<String, String> getReactionLabels() {
		return reactionLabels;
	}


	public void setReactionLabels(Map<String, String> reactionLabels) {
		this.reactionLabels = reactionLabels;
	}
}
