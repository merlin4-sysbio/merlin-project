/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.utilities;

import java.util.Comparator;
import java.util.Map;

/**
 * @author ODias
 *
 */
public class ValueComparator implements Comparator<Object>  {
	private Map<?, ?>base;
	
	
	public ValueComparator(Map<?, ?> base) {
		this.base = base;
	}

	public int compare(Object a, Object b) {
		if((Double)base.get(a) < (Double)base.get(b)) {
			return 1;
		} else if((Double)base.get(a) == (Double)base.get(b)) {
			return 0;
		} else {
			return -1;
		}
	}


}
