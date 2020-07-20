package pt.uminho.ceb.biosystems.merlin.biocomponents.container;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.CVTerm.Qualifier;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.JSBML;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.ModifierSpeciesReference;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.xml.XMLNode;

import pt.uminho.ceb.biosystems.merlin.biocomponents.io.Enumerators.SBMLLevelVersion;



/**
 * @author ODias
 *
 */
public class SBML_Model {

	protected Model model;
	private int level;
	private int version;
	private Map<String, Set<String>> reactionReactants, reactionProducts;

	/**
	 * @param id
	 * @param filePath
	 * @param level
	 * @param version
	 */
	public SBML_Model(String id, SBMLLevelVersion levelAndVersion) {

		this.level=levelAndVersion.getLevel();
		this.version=levelAndVersion.getVersion();
		this.reactionReactants=new TreeMap<String, Set<String>>();
		this.reactionProducts=new TreeMap<String, Set<String>>();
		this.model = new Model(id, level, version);
		this.model.setName(id);
	}

	/**
	 * @param id
	 * @param name
	 */
	public void addCompartment(String id, String name, String outsideID) {

		Compartment compartment = this.model.createCompartment();
		compartment.setId(id.replace(" ", "_"));
		compartment.setName(name);
		compartment.setUnits("volume");
		compartment.setSize(1.00);

		if(outsideID!=null)
			compartment.setOutside(outsideID);

		if (!this.model.containsCompartment(id.replace(" ", "_")))
			this.model.addCompartment(compartment);
	}

	/**
	 * @param id
	 * @param name
	 * @param compartment
	 * @param string 

	public void addSpecies(String id, String name, String compartment){
		Species specie = new Species(this.level, this.version);
		specie.setId(id);
		specie.setName(name);
		specie.setCompartment(compartment);
		specie.setInitialAmount(1);
		this.sbmlModel.addSpecies(specie);
	}
	 */

	/**
	 * @param id
	 * @param name
	 * @param urn_id
	 * @param compartment
	 */
	public void addSpecies(String id, String name, String compartment, Annotation annotation) {

		Species species = this.model.createSpecies();
		species.setId(id);
		species.setName(name);
		species.setCompartment(compartment);
		species.setInitialAmount(1);

		species.setMetaId(id);
		species.setAnnotation(annotation);
	}

	/**
	 * @param id
	 * @param name
	 * @param urn_id
	 * @param reversibility
	 * @param xmlNote
	 * @param lower_bound
	 * @param upper_bound
	 * @param biomassEquation
	 * @throws XMLStreamException 
	 */
	public void addReaction(String id, String name, String urn_id, boolean reversibility, XMLNode xmlNote, double lower_bound , double upper_bound, boolean biomassEquation) {

		Reaction reaction = this.model.createReaction();
		reaction.setName(name);
		reaction.setId(id);
		reaction.setMetaId(id);
		reaction.setReversible(reversibility);

		if(xmlNote!=null)
			reaction.appendNotes(xmlNote);

		Annotation annotation = new Annotation();
		CVTerm cvTerm = new CVTerm(Qualifier.BQB_IS);
		annotation.addCVTerm(cvTerm);

		if(urn_id!=null && urn_id.matches("^R\\d{5}.*")) {

			String link_id = urn_id;

			if(link_id.contains("_"))
				link_id = urn_id.split("_")[0];

			cvTerm.addResource("urn:miriam:kegg.reaction:" + link_id);
			cvTerm.addResource("http://www.genome.jp/dbget-bin/www_bget?rn:"+link_id);
			reaction.setAnnotation(annotation);
		}

		ListOf<LocalParameter> listOfLocalParameters = new ListOf<LocalParameter>();
		LocalParameter parameter = new LocalParameter();
		parameter.setId("LOWER_BOUND");
		parameter.setValue(lower_bound);
		listOfLocalParameters.add(parameter);

		parameter = new LocalParameter();
		parameter.setId("OBJECTIVE_COEFFICIENT");
		parameter.setValue(0.000000);

		if(biomassEquation)
			parameter.setValue(1.000000);

		listOfLocalParameters.add(parameter);

		parameter = new LocalParameter();
		parameter.setId("UPPER_BOUND");
		parameter.setValue(upper_bound);
		listOfLocalParameters.add(parameter);

		KineticLaw kineticLaw = new KineticLaw();
		String math = "<math xmlns=\"http://www.w3.org/1998/Math/MathML\"><ci> FLUX_VALUE </ci></math>";
		
		ASTNode mathnode = JSBML.readMathMLFromString(math);
		kineticLaw.setMath(mathnode);
		kineticLaw.setListOfLocalParameters(listOfLocalParameters);
		reaction.setKineticLaw(kineticLaw);
	}

	/**
	 * @param enzymeID
	 * @param reactionID
	 */
	public void setReactionEnzyme(String enzymeID, String reactionID) {

		ModifierSpeciesReference msrEnzyme = new ModifierSpeciesReference(this.level, this.version);
		msrEnzyme.setSpecies(enzymeID);
		this.model.getReaction(reactionID).addModifier(msrEnzyme);
	}

	/**
	 * @param id
	 * @param reactionID
	 * @param stoichiometry
	 */
	public void setReactionCompound(String compoundID, String reactionID, double stoichiometry) {

		if(stoichiometry>0) {

			if(this.reactionProducts.containsKey(reactionID)) {

				if(this.reactionProducts.get(reactionID).contains(compoundID)) {

					// do nothing
					System.err.println(compoundID+"\t"+ reactionID);
				}
				else {

					Set<String> compoundsID = this.reactionProducts.get(reactionID);
					compoundsID.add(compoundID);
					this.reactionProducts.put(reactionID, compoundsID);

					SpeciesReference srCompound = this.model.getReaction(reactionID).createProduct(this.model.getSpecies(compoundID));
					srCompound.setStoichiometry(stoichiometry);
				}
			}
			else {

				Set<String> compoundsID = new HashSet<String>();
				compoundsID.add(compoundID);
				this.reactionProducts.put(reactionID, compoundsID);

				SpeciesReference srCompound = this.model.getReaction(reactionID).createProduct(this.model.getSpecies(compoundID));
				srCompound.setStoichiometry(stoichiometry);
			}
		}
		else {

			if(this.reactionReactants.containsKey(reactionID)) {

				if(this.reactionReactants.get(reactionID).contains(compoundID.toString())) {

					// do nothing
					System.err.println(compoundID+"\t"+ reactionID);
				}
				else {

					Set<String> compoundsID = this.reactionReactants.get(reactionID);
					compoundsID.add(compoundID);
					this.reactionReactants.put(reactionID, compoundsID);

					SpeciesReference srCompound = this.model.getReaction(reactionID).createReactant(this.model.getSpecies(compoundID));
					stoichiometry=stoichiometry*-1;
					srCompound.setStoichiometry(stoichiometry);
				}
			}
			else {

				Set<String> compoundsID = new HashSet<String>();
				compoundsID.add(compoundID);
				this.reactionReactants.put(reactionID, compoundsID);

				SpeciesReference srCompound = this.model.getReaction(reactionID).createReactant(this.model.getSpecies(compoundID));
				stoichiometry=stoichiometry*-1;
				srCompound.setStoichiometry(stoichiometry);				
			}
		}
	}

	/**
	 * get SBML document level
	 * 
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * set SBML document level
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * get SBML document version
	 * 
	 * @return
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * set SBML document version
	 * 
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(Model model) {
		this.model = model;
	}
}
