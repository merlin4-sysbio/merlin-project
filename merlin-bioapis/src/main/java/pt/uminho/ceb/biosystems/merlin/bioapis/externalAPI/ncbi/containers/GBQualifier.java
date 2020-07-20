package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="GBQualifier", strict=false)
public class GBQualifier {

	@Element(name="GBQualifier_name", required=false)
	public String name;
	
	@Element(name="GBQualifier_value", required=false)
	public String value;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GBQualifier [name=" + name + ", value=" + value + "]";
	}

	
	
}
