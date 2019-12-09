package pt.uminho.ceb.biosystems.merlin.utilities.external;



public class ExternalRef implements IExternalRef{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String id;
	protected String name;
	protected ExternalRefSource externalRefSource;

	public ExternalRef(String id, String name,
			ExternalRefSource externalRefSource) {
		this.id = id;
		this.name = name;
		this.externalRefSource = externalRefSource;
	}
	
	public ExternalRef(ExternalRefSource externalRefSource) {
		this.externalRefSource = externalRefSource;
	}

	@Override
	public ExternalRefSource getExternalRefSource() {
		return externalRefSource;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getMiriamCode() {
		return externalRefSource.getMiriamCode(id);
	}

	@Override
	public String getIdentifierCode() {
		return externalRefSource.getIdentifierId(id);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getURLLink() {
		return externalRefSource.getUrlLink(id);
	}

	@Override
	public Boolean hasMiriamCode() {
		return externalRefSource.getMiriamId() != null;
	}

	@Override
	public boolean equals(Object o) {

		return ((ExternalRef) o).getId().equals(id)
				&& ((ExternalRef) o).getExternalRefSource().equals(
						externalRefSource);
	}

	public String toString() {

		return id;
	}

}
