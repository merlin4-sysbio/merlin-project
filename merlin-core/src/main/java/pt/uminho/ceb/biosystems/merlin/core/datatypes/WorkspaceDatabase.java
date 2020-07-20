package pt.uminho.ceb.biosystems.merlin.core.datatypes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author adias
 *
 */
public class WorkspaceDatabase extends Observable {

	/**
	 * 
	 */
	private WorkspaceTables tables;
	private WorkspaceModelEntities entities;
	private TreeMap<String,LinkedList<String>> ultimlyComplexComposedBy;
	private LinkedList<String> orphenComplexs;
	private WorkspaceAnnotations annotations;

	/**
	 * @param databaseAccess
	 */
	public WorkspaceDatabase() {
		
		this.ultimlyComplexComposedBy = new TreeMap<String,LinkedList<String>>();
		this.orphenComplexs = new LinkedList<String>();
	}

	public WorkspaceTables getTables() {
		return tables;
	}

	public void setTables(WorkspaceTables dbt) {
		this.tables = dbt;
		setChanged();
		notifyObservers();
	}

	public WorkspaceModelEntities getEntities() {
		return entities;
	}

	public void setEntities(WorkspaceModelEntities entities) {
		this.entities = entities;
	}
	
	public WorkspaceAnnotations getAnnotations() {
		return annotations;
	}
	
	public void setEntities(WorkspaceAnnotations annotations) {

		this.annotations = annotations;		
	}
	
	public LinkedList<String> getOrphenComplexs() {
		return orphenComplexs;
	}

	public TreeMap<String, LinkedList<String>> getUltimlyComplexComposedBy() {
		return ultimlyComplexComposedBy;
	}

	public TreeMap<String,LinkedList<String>> getRestOfComplexs(HashMap<String,LinkedList<String>> proteinGenes, 
			HashMap<String,LinkedList<String>> complexComposedBy, 
			TreeMap<String,LinkedList<String>> ultimlyComplexComposedBy)
			{

		int intialUnkonen = complexComposedBy.size();

		Set<String> keys = complexComposedBy.keySet();

		LinkedList<String> found = new LinkedList<String>();

		for (Iterator<String> p_iter = keys.iterator(); p_iter.hasNext(); )
		{
			String key = (String)p_iter.next();

			LinkedList<String> subProts = complexComposedBy.get(key);

			boolean go = true;

			for(int i=0;i<subProts.size() && go;i++)
			{
				String sub = subProts.get(i);

				if(!ultimlyComplexComposedBy.containsKey(sub) && !proteinGenes.containsKey(sub)) go = false;
			}

			if(go) found.add(key);
		}

		for(int i=0;i<found.size();i++)
		{
			String key = found.get(i);

			LinkedList<String> subProts = complexComposedBy.get(key);

			complexComposedBy.remove(key);

			LinkedList<String> comps = new LinkedList<String>();

			for(int e=0;e<subProts.size();e++)
			{
				String sub = subProts.get(e);

				LinkedList<String> genesToAdd;

				if(ultimlyComplexComposedBy.containsKey(sub))
				{
					genesToAdd = ultimlyComplexComposedBy.get(sub);
				}
				else
				{
					genesToAdd = proteinGenes.get(sub);
				}

				for(int g=0;g<genesToAdd.size();g++)
				{
					comps.add(genesToAdd.get(g));
				}
			}

			ultimlyComplexComposedBy.put(key, comps);
		}

		if(complexComposedBy.isEmpty() || intialUnkonen==complexComposedBy.size())
		{

			if(intialUnkonen==complexComposedBy.size())
			{
				Set<String> kezs = complexComposedBy.keySet();

				for (Iterator<String> p_iter = kezs.iterator(); p_iter.hasNext(); )
				{
					String key = (String)p_iter.next();
					this.orphenComplexs.add(key);
				}
			}
			return ultimlyComplexComposedBy;
		}
		else
		{
			return getRestOfComplexs(proteinGenes, complexComposedBy, ultimlyComplexComposedBy);
		}

			}

	protected void addToList(String add, String key, HashMap<String,LinkedList<String>> h)
	{
		if(h.containsKey(key)) h.get(key).add(add);
		else
		{
			LinkedList<String> lis = new LinkedList<String>();
			lis.add(add);
			h.put(key, lis);
		}
	}

	protected void addToList(LinkedList<String> add, String key, HashMap<String,LinkedList<String>> h)
	{
		if(h.containsKey(key))
		{
			for(int i=0;i<add.size();i++)
			{
				h.get(key).add(add.get(i));
			}
		}
		else h.put(key, add);
	}
	
}
