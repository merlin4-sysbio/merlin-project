/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot;

import uk.ac.ebi.kraken.interfaces.uniprot.NcbiTaxon;

/**
 * @author ODias
 *
 */
public class MyNcbiTaxon implements NcbiTaxon, Comparable<MyNcbiTaxon>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;
	private String value;
	
	/**
	 * 
	 */
	public MyNcbiTaxon() {

	}
	
	/**
	 * @param value
	 */
	public MyNcbiTaxon(String value) {

		this.value = value;
	}

	@Override
	public String getValue() {

		return this.value;
	}

	@Override
	public void setValue(String value) {

		this.value = value;
	}

	@Override
	public String toString() {
		
		return "MyNcbiTaxon [value=" + value + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
		
			return false;
		
		if (getClass() != obj.getClass())
		
			return false;
		
		MyNcbiTaxon other = (MyNcbiTaxon) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	} 

	@Override
	public int compareTo(MyNcbiTaxon o) {
		
		return this.getValue().compareTo(o.getValue());
	}
	
	
}
