package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.datastructures;

import java.util.List;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.utilities.external.ExternalRefSource;
import pt.uminho.ceb.biosystems.merlin.utilities.external.IExternalRef;

public class KeggECNumberEntry implements IExternalRef {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final ExternalRefSource EC_CODE = ExternalRefSource.EC_Code;
	
	protected String id;
	protected List<String> names;
	protected Set<String> reactionIds;
	protected List<String> klass;
	protected String sysname;
	
	
	public KeggECNumberEntry(
			String id,
			List<String> names,
			List<String> klass,
			String sysname,
			Set<String> reactionIds) {
		this.id = id;
		this.names = names;
		this.klass = klass;
		this.reactionIds = reactionIds;
		this.sysname = sysname;
	}
	
	
	@Override
	public ExternalRefSource getExternalRefSource() {
		return EC_CODE;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return names.get(0);
	}
	
	public List<String> getNames() {
		return names;
	}
	
	public Set<String> getReactionIds() {
		return reactionIds;
	}

	@Override
	public String getURLLink() {
		return "http://www.kegg.jp/entry/ec:" + id;
	}

	@Override
	public Boolean hasMiriamCode() {
		return true;
	}

	@Override
	public String getMiriamCode() {
		return "urn:miriam:ec-code:" + id;
	}


	@Override
	public String getIdentifierCode() {
		// TODO Auto-generated method stub
		return ExternalRefSource.EC_Code.getIdentifierCode(id);
	}

}
