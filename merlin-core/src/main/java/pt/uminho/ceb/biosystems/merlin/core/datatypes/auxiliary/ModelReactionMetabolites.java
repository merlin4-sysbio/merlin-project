package pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary;

public class ModelReactionMetabolites {

	
	private String name;
	private String externalIdentifier;
	private String internalIdentifier;
	private String formula;
	
	/**
	 * @param name
	 * @param externalIdentifier
	 * @param internalIdentifier
	 * @param formula
	 */
	public ModelReactionMetabolites(String name, String externalIdentifier, String internalIdentifier, String formula) {
		super();
		this.name = name;
		this.externalIdentifier = externalIdentifier;
		this.internalIdentifier = internalIdentifier;
		this.formula = formula;
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
	 * @return the externalIdentifier
	 */
	public String getExternalIdentifier() {
		return externalIdentifier;
	}

	/**
	 * @param externalIdentifier the externalIdentifier to set
	 */
	public void setExternalIdentifier(String externalIdentifier) {
		this.externalIdentifier = externalIdentifier;
	}

	/**
	 * @return the internalIdentifier
	 */
	public String getInternalIdentifier() {
		return internalIdentifier;
	}

	/**
	 * @param internalIdentifier the internalIdentifier to set
	 */
	public void setInternalIdentifier(String internalIdentifier) {
		this.internalIdentifier = internalIdentifier;
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

}
