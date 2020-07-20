package pt.uminho.ceb.biosystems.merlin.core.containers.gpr;


import java.util.List;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.ModuleType;


public class ModuleCI {

	private String module;
	private ModuleType moduleType;
	private String definition;
	private String name;
	private String stoichiometry;
	private List<String> pathways;

	/**
	 * @param module
	 * @param moduleType
	 */
	public ModuleCI(String module, ModuleType moduleType) {
		super();
		this.module = module;
		this.moduleType = moduleType;
	}

	/**
	 * @param module
	 * @param moduleType
	 * @param definition
	 * @param name
	 * @param stoichiometry
	 * @param pathways
	 */
	public ModuleCI(String module, ModuleType moduleType, String definition,
			String name, String stoichiometry, List<String> pathways) {
		super();
		this.module = module;
		this.moduleType = moduleType;
		this.definition = definition;
		this.name = name;
		this.stoichiometry = stoichiometry;
		this.pathways = pathways;
	}

	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}
	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}
	/**
	 * @return the moduleType
	 */
	public ModuleType getModuleType() {
		return moduleType;
	}
	/**
	 * @param moduleType the moduleType to set
	 */
	public void setModuleType(ModuleType moduleType) {
		this.moduleType = moduleType;
	}
	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}
	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the stoichiometry
	 */
	public String getStoichiometry() {
		return stoichiometry;
	}
	/**
	 * @param stoichiometry the stoichiometry to set
	 */
	public void setStoichiometry(String stoichiometry) {
		this.stoichiometry = stoichiometry;
	}
	/**
	 * @return the pathways
	 */
	public List<String> getPathways() {
		return pathways;
	}
	/**
	 * @param pathways the pathways to set
	 */
	public void setPathways(List<String> pathways) {
		this.pathways = pathways;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ModuleCI ["
				+ (module != null ? "module=" + module + ", " : "")
				+ (moduleType != null ? "moduleType=" + moduleType + ", " : "")
				+ (definition != null ? "definition=" + definition + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (stoichiometry != null ? "stoichiometry=" + stoichiometry
						+ ", " : "")
				+ (pathways != null ? "pathways=" + pathways : "") + "]";
	}



}
