package pt.uminho.ceb.biosystems.merlin.core.containers.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ModuleContainer implements EntityContainer{

	private String externalIdentifier;
	private List<String> orthologues;
	private String stoichiometry;
	private String name;
	private String definition;
	private String moduleHieralchicalClass;
	private String moduleType;
	private String reaction;
	private List<String> substrates;
	private	Map<String, String> pathways;


	/**
	 * @param entry
	 */
	public ModuleContainer(String externalIdentifier) {
		this.setExternalIdentifier(externalIdentifier);
	}

	/**
	 * @param moduleType the moduleType to set
	 */
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	/**
	 * @return the moduleType
	 */
	public String getModuleType() {
		return moduleType;
	}

	/**
	 * @param substrates the substrates to set
	 */
	public void setSubstrates(List<String> substrates) {
		this.substrates = substrates;
	}

	/**
	 * @return the substrates
	 */
	public List<String> getSubstrates() {
		return substrates;
	}

	/**
	 * @param pathways the pathways to set
	 */
	public void setPathways(Map<String, String> pathways) {
		this.pathways = pathways;
	}

	/**
	 * @return the pathways
	 */
	public Map<String, String> getPathways() {
		return pathways;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param entry the entry to set
	 */
	public void setExternalIdentifier(String externalIdentifier) {
		this.externalIdentifier = externalIdentifier;
	}

	/**
	 * @return the entry
	 */
	public String getExternalIdentifier() {
		return externalIdentifier;
	}

	@Override
	public List<String> getDblinks() {
		return null;
	}

	@Override
	public List<String> getNames() {
		return null;
	}

	@Override
	public void setDblinks(List<String> dblinks) {
	}

	@Override
	public void setNames(List<String> names) {
	}

	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * @param orthologues the orthologues to set
	 */
	public void setOrthologues(List<String> orthologues) {
		this.orthologues = orthologues;
	}

	/**
	 * @return the orthologues
	 */
	public List<String> getOrthologues() {
		return orthologues;
	}

	/**
	 * @param stoichiometry the stoichiometry to set
	 */
	public void setStoichiometry(String stoichiometry) {
		this.stoichiometry = stoichiometry;
	}

	/**
	 * @return the stoichiometry
	 */
	public String getStoichiometry() {
		return stoichiometry;
	}

	/**
	 * @param moduleHieralchicalClass the moduleHieralchicalClass to set
	 */
	public void setModuleHieralchicalClass(String moduleHieralchicalClass) {
		this.moduleHieralchicalClass = moduleHieralchicalClass;
	}

	/**
	 * @return the moduleHieralchicalClass
	 */
	public String getModuleHieralchicalClass() {
		return moduleHieralchicalClass;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		return "ModuleContainer ["
		+ (definition != null ? "definition=" + definition + ", " : "")
		+ (externalIdentifier != null ? "entry=" + externalIdentifier + ", " : "")
		+ (moduleHieralchicalClass != null ? "moduleHieralchicalClass="
				+ moduleHieralchicalClass + ", " : "")
				+ (moduleType != null ? "moduleType=" + moduleType + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (orthologues != null ? "orthologues="
						+ toString(orthologues, maxLen) + ", " : "")
							+ (pathways != null ? "pathways="
								+ toString(pathways.entrySet(), maxLen) + ", " : "")
								+ (stoichiometry != null ? "stoichiometry=" + stoichiometry + ", " : "")
										+ (substrates != null ? "substrates="
												+ toString(substrates, maxLen) : "") + "]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
		&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @return the reaction
	 */
	public String getReaction() {
		return reaction;
	}

	/**
	 * @param reaction the reaction to set
	 */
	public void setReaction(String reaction) {
		this.reaction = reaction;
	}
}
