/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.core.containers.model;

import java.util.List;

/**
 * @author ODias
 *
 */
public interface EntityContainer {

	/**
	 * @param entry the entry to set
	 */
	public void setExternalIdentifier(String externalIdentifier);
	/**
	 * @return the entry
	 */
	public String getExternalIdentifier();
	/**
	 * @param dblinks the dblinks to set
	 */
	public void setDblinks(List<String> dblinks);
	/**
	 * @return the dblinks
	 */
	public List<String> getDblinks();
	/**
	 * @param names the names to set
	 */
	public void setNames(List<String> names);
	/**
	 * @return the names
	 */
	public List<String> getNames();
	
}
