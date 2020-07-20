/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.core.containers.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author ODias
 *
 */
public class PathwaysHierarchyContainer {

	private Map<String, List<String[]>> pathwaysHierarchy;
	private String superPathway;
	
	/**
	 * 
	 */
	public PathwaysHierarchyContainer(String superPathway) {
		this.setSuper_pathway(superPathway);
		this.setPathwaysHierarchy(new HashMap<String, List<String[]>>());
	}
	/**
	 * @param pathways_hierarchy the pathways_hierarchy to set
	 */
	public void setPathwaysHierarchy(Map<String, List<String[]>> pathwaysHierarchy) {
		this.pathwaysHierarchy = pathwaysHierarchy;
	}
	/**
	 * @return the pathways_hierarchy
	 */
	public Map<String, List<String[]>> getPathways_hierarchy() {
		return pathwaysHierarchy;
	}
	/**
	 * @param super_pathway the super_pathway to set
	 */
	public void setSuper_pathway(String superPathway) {
		this.superPathway = superPathway;
	}
	/**
	 * @return the super_pathway
	 */
	public String getSuper_pathway() {
		return superPathway;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		return "PathwaysHierarchyContainer ["
				+ (pathwaysHierarchy != null ? "pathways_hierarchy="
						+ toString(pathwaysHierarchy.entrySet(), maxLen)
						+ ", " : "")
				+ (superPathway != null ? "super_pathway=" + superPathway
						: "") + "]";
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
	
	
}
