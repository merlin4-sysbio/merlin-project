package pt.uminho.ceb.biosystems.merlin.entities.model;
// Generated Jul 16, 2019 10:41:08 AM by Hibernate Tools 5.2.12.Final

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalInhibitor;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalSubstrateAffinity;
import pt.uminho.ceb.biosystems.merlin.entities.experimental.ExperimentalTurnoverNumber;

/**
 * ModelCompound generated by hbm2java
 */
@Entity
@Table(name = "model_compound")
public class ModelCompound implements java.io.Serializable {

	private int idcompound;
	private String name;
	private String inchi;
	private String externalIdentifier;
	private String entryType;
	private String formula;
	private String molecularWeight;
	private String neutralFormula;
	private Short charge;
	private String smiles;
	private Boolean hasBiologicalRoles;
	private Set<ExperimentalSubstrateAffinity> experimentalSubstrateAffinities = new HashSet<ExperimentalSubstrateAffinity>(
			0);
	private Set<ModelPathwayHasCompound> modelPathwayHasCompounds = new HashSet<ModelPathwayHasCompound>(0);
	private Set<ModelEnzymaticAlternativeCofactor> modelEnzymaticAlternativeCofactors = new HashSet<ModelEnzymaticAlternativeCofactor>(
			0);
	private Set<ModelSameAs> modelSameAsesForSimilarMetaboliteid = new HashSet<ModelSameAs>(0);
	private Set<ModelSameAs> modelSameAsesForMetaboliteid = new HashSet<ModelSameAs>(0);
	private Set<ExperimentalTurnoverNumber> experimentalTurnoverNumbers = new HashSet<ExperimentalTurnoverNumber>(0);
	private Set<ModelStoichiometry> modelStoichiometries = new HashSet<ModelStoichiometry>(0);
	private Set<ModelEffector> modelEffectors = new HashSet<ModelEffector>(0);
	private Set<ModelModulesHasCompound> modelModulesHasCompounds = new HashSet<ModelModulesHasCompound>(0);
	private Set<ModelEnzymaticCofactor> modelEnzymaticCofactors = new HashSet<ModelEnzymaticCofactor>(0);
	private Set<ModelSubstrateAffinity> modelSubstrateAffinities = new HashSet<ModelSubstrateAffinity>(0);
	private Set<ModelFunctionalParameter> modelFunctionalParameters = new HashSet<ModelFunctionalParameter>(0);
	private Set<ModelMetabolicRegulation> modelMetabolicRegulations = new HashSet<ModelMetabolicRegulation>(0);
	private Set<ExperimentalInhibitor> experimentalInhibitors = new HashSet<ExperimentalInhibitor>(0);

	public ModelCompound() {
	}

	public ModelCompound(int idcompound) {
		this.idcompound = idcompound;
	}

	public ModelCompound(int idcompound, String name, String inchi, String externalIdentifier, String entryType,
			String formula, String molecularWeight, String neutralFormula, Short charge, String smiles,
			Boolean hasBiologicalRoles, Set<ExperimentalSubstrateAffinity> experimentalSubstrateAffinities,
			Set<ModelPathwayHasCompound> modelPathwayHasCompounds,
			Set<ModelEnzymaticAlternativeCofactor> modelEnzymaticAlternativeCofactors,
			Set<ModelSameAs> modelSameAsesForSimilarMetaboliteid, Set<ModelSameAs> modelSameAsesForMetaboliteid,
			Set<ExperimentalTurnoverNumber> experimentalTurnoverNumbers, Set<ModelStoichiometry> modelStoichiometries,
			Set<ModelEffector> modelEffectors, Set<ModelModulesHasCompound> modelModulesHasCompounds,
			Set<ModelEnzymaticCofactor> modelEnzymaticCofactors, Set<ModelSubstrateAffinity> modelSubstrateAffinities,
			Set<ModelFunctionalParameter> modelFunctionalParameters,
			Set<ModelMetabolicRegulation> modelMetabolicRegulations,
			Set<ExperimentalInhibitor> experimentalInhibitors) {
		this.idcompound = idcompound;
		this.name = name;
		this.inchi = inchi;
		this.externalIdentifier = externalIdentifier;
		this.entryType = entryType;
		this.formula = formula;
		this.molecularWeight = molecularWeight;
		this.neutralFormula = neutralFormula;
		this.charge = charge;
		this.smiles = smiles;
		this.hasBiologicalRoles = hasBiologicalRoles;
		this.experimentalSubstrateAffinities = experimentalSubstrateAffinities;
		this.modelPathwayHasCompounds = modelPathwayHasCompounds;
		this.modelEnzymaticAlternativeCofactors = modelEnzymaticAlternativeCofactors;
		this.modelSameAsesForSimilarMetaboliteid = modelSameAsesForSimilarMetaboliteid;
		this.modelSameAsesForMetaboliteid = modelSameAsesForMetaboliteid;
		this.experimentalTurnoverNumbers = experimentalTurnoverNumbers;
		this.modelStoichiometries = modelStoichiometries;
		this.modelEffectors = modelEffectors;
		this.modelModulesHasCompounds = modelModulesHasCompounds;
		this.modelEnzymaticCofactors = modelEnzymaticCofactors;
		this.modelSubstrateAffinities = modelSubstrateAffinities;
		this.modelFunctionalParameters = modelFunctionalParameters;
		this.modelMetabolicRegulations = modelMetabolicRegulations;
		this.experimentalInhibitors = experimentalInhibitors;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="UseExistingIdOtherwiseGenerateUsingIdentity")
	@GenericGenerator(name = "UseExistingIdOtherwiseGenerateUsingIdentity", strategy = "pt.uminho.ceb.biosystems.merlin.auxiliary.UseExistingIdOtherwiseGenerateUsingIdentity")	
	@Column(name = "idcompound", unique = true, nullable = false)
	public int getIdcompound() {
		return this.idcompound;
	}

	public void setIdcompound(int idcompound) {
		this.idcompound = idcompound;
	}

	@Column(name = "name", length = 400)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "inchi", length = 1500)
	public String getInchi() {
		return this.inchi;
	}

	public void setInchi(String inchi) {
		this.inchi = inchi;
	}

	@Column(name = "external_identifier", length = 250)
	public String getExternalIdentifier() {
		return this.externalIdentifier;
	}

	public void setExternalIdentifier(String externalIdentifier) {
		this.externalIdentifier = externalIdentifier;
	}

	@Column(name = "entry_type", length = 9)
	public String getEntryType() {
		return this.entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	@Column(name = "formula", length = 65535, columnDefinition="Text")
	public String getFormula() {
		return this.formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	@Column(name = "molecular_weight", length = 100)
	public String getMolecularWeight() {
		return this.molecularWeight;
	}

	public void setMolecularWeight(String molecularWeight) {
		this.molecularWeight = molecularWeight;
	}

	@Column(name = "neutral_formula", length = 120)
	public String getNeutralFormula() {
		return this.neutralFormula;
	}

	public void setNeutralFormula(String neutralFormula) {
		this.neutralFormula = neutralFormula;
	}

	@Column(name = "charge")
	public Short getCharge() {
		return this.charge;
	}

	public void setCharge(Short charge) {
		this.charge = charge;
	}

	@Column(name = "smiles", length = 1200)
	public String getSmiles() {
		return this.smiles;
	}

	public void setSmiles(String smiles) {
		this.smiles = smiles;
	}

	@Column(name = "hasBiologicalRoles")
	public Boolean getHasBiologicalRoles() {
		return this.hasBiologicalRoles;
	}

	public void setHasBiologicalRoles(Boolean hasBiologicalRoles) {
		this.hasBiologicalRoles = hasBiologicalRoles;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ExperimentalSubstrateAffinity> getExperimentalSubstrateAffinities() {
		return this.experimentalSubstrateAffinities;
	}

	public void setExperimentalSubstrateAffinities(Set<ExperimentalSubstrateAffinity> experimentalSubstrateAffinities) {
		this.experimentalSubstrateAffinities = experimentalSubstrateAffinities;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelPathwayHasCompound> getModelPathwayHasCompounds() {
		return this.modelPathwayHasCompounds;
	}

	public void setModelPathwayHasCompounds(Set<ModelPathwayHasCompound> modelPathwayHasCompounds) {
		this.modelPathwayHasCompounds = modelPathwayHasCompounds;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelEnzymaticAlternativeCofactor> getModelEnzymaticAlternativeCofactors() {
		return this.modelEnzymaticAlternativeCofactors;
	}

	public void setModelEnzymaticAlternativeCofactors(
			Set<ModelEnzymaticAlternativeCofactor> modelEnzymaticAlternativeCofactors) {
		this.modelEnzymaticAlternativeCofactors = modelEnzymaticAlternativeCofactors;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompoundBySimilarMetaboliteid")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelSameAs> getModelSameAsesForSimilarMetaboliteid() {
		return this.modelSameAsesForSimilarMetaboliteid;
	}

	public void setModelSameAsesForSimilarMetaboliteid(Set<ModelSameAs> modelSameAsesForSimilarMetaboliteid) {
		this.modelSameAsesForSimilarMetaboliteid = modelSameAsesForSimilarMetaboliteid;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompoundByMetaboliteid")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelSameAs> getModelSameAsesForMetaboliteid() {
		return this.modelSameAsesForMetaboliteid;
	}

	public void setModelSameAsesForMetaboliteid(Set<ModelSameAs> modelSameAsesForMetaboliteid) {
		this.modelSameAsesForMetaboliteid = modelSameAsesForMetaboliteid;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ExperimentalTurnoverNumber> getExperimentalTurnoverNumbers() {
		return this.experimentalTurnoverNumbers;
	}

	public void setExperimentalTurnoverNumbers(Set<ExperimentalTurnoverNumber> experimentalTurnoverNumbers) {
		this.experimentalTurnoverNumbers = experimentalTurnoverNumbers;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelStoichiometry> getModelStoichiometries() {
		return this.modelStoichiometries;
	}

	public void setModelStoichiometries(Set<ModelStoichiometry> modelStoichiometries) {
		this.modelStoichiometries = modelStoichiometries;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelEffector> getModelEffectors() {
		return this.modelEffectors;
	}

	public void setModelEffectors(Set<ModelEffector> modelEffectors) {
		this.modelEffectors = modelEffectors;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelModulesHasCompound> getModelModulesHasCompounds() {
		return this.modelModulesHasCompounds;
	}

	public void setModelModulesHasCompounds(Set<ModelModulesHasCompound> modelModulesHasCompounds) {
		this.modelModulesHasCompounds = modelModulesHasCompounds;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelEnzymaticCofactor> getModelEnzymaticCofactors() {
		return this.modelEnzymaticCofactors;
	}

	public void setModelEnzymaticCofactors(Set<ModelEnzymaticCofactor> modelEnzymaticCofactors) {
		this.modelEnzymaticCofactors = modelEnzymaticCofactors;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelSubstrateAffinity> getModelSubstrateAffinities() {
		return this.modelSubstrateAffinities;
	}

	public void setModelSubstrateAffinities(Set<ModelSubstrateAffinity> modelSubstrateAffinities) {
		this.modelSubstrateAffinities = modelSubstrateAffinities;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelFunctionalParameter> getModelFunctionalParameters() {
		return this.modelFunctionalParameters;
	}

	public void setModelFunctionalParameters(Set<ModelFunctionalParameter> modelFunctionalParameters) {
		this.modelFunctionalParameters = modelFunctionalParameters;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ModelMetabolicRegulation> getModelMetabolicRegulations() {
		return this.modelMetabolicRegulations;
	}

	public void setModelMetabolicRegulations(Set<ModelMetabolicRegulation> modelMetabolicRegulations) {
		this.modelMetabolicRegulations = modelMetabolicRegulations;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "modelCompound")
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<ExperimentalInhibitor> getExperimentalInhibitors() {
		return this.experimentalInhibitors;
	}

	public void setExperimentalInhibitors(Set<ExperimentalInhibitor> experimentalInhibitors) {
		this.experimentalInhibitors = experimentalInhibitors;
	}

}
