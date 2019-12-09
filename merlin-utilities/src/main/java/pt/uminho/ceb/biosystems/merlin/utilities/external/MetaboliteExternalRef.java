package pt.uminho.ceb.biosystems.merlin.utilities.external;

public class MetaboliteExternalRef extends ExternalRef implements IMetaboliteExternalRef{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Integer charge;
	protected String formula;
	protected String mass;
	protected String smiles;
	protected String inchikey;
	
	public MetaboliteExternalRef(String id, String name,
			ExternalRefSource externalRefSource, Integer charge, String formula,
			String mass, String smiles, String inchikey) {
		super(id, name, externalRefSource);
		this.charge = charge;
		this.formula = formula;
		this.mass = mass;
		this.smiles = smiles;
		this.inchikey = inchikey;
	}

	@Override
	public Integer getCharge() {
		return charge;
	}

	@Override
	public String getFormula() {
		return formula;
	}

	@Override
	public String getMass() {
		return mass;
	}

	@Override
	public String getSmiles() {
		return smiles;
	}

	@Override
	public String getInchikey() {
		return inchikey;
	}

	
}
