package pt.uminho.ceb.biosystems.merlin.core.datatypes.model.datatables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;

/**
 * @author ODias
 *
 */
public class ModelPathwayReactions extends WorkspaceDataTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected List<Integer> ids;
	protected List<Integer> pathway;
	protected Map <Integer, String> pathways;
	protected boolean encodedOnly;

	/**
	 * @param columnsNames
	 * @param name
	 * @param pathways
	 * @param encodedOnly
	 */
	public ModelPathwayReactions(ArrayList<String> columnsNames, String name, Map <Integer, String> pathways, boolean encodedOnly) {
		
		super(columnsNames, name);
		this.ids = new ArrayList<>();
		this.pathway = new ArrayList<>();
		this.pathways = pathways;
		this.encodedOnly=encodedOnly;
	}

	/**
	 * @param line
	 * @param id
	 * @param pathway
	 */
	public void addLine(List<Object> line, int id, int pathway){
		super.addLine(line);
		this.ids.add(id);
		this.pathway.add(pathway);
	}

	/**
	 * @param row
	 * @return
	 */
	public int getRowPathway(int row){
		return pathway.get(row);
	}

	/**
	 * @param row
	 * @return
	 */
	public int getRowId(int row){
		return ids.get(row);
	}

	/**
	 * @param path
	 * @return
	 */
	public WorkspaceGenericDataTable getReactionsData(int path, String pathwayName, boolean isCompartimentalizedModel) {
		
		try {
			
			ArrayList<String> columnsNames = new ArrayList<String>();
			columnsNames.add("info");
			columnsNames.add("pathway name");
			columnsNames.add("reaction name");
			columnsNames.add("equation");
			if(isCompartimentalizedModel)
				columnsNames.add("localization");
			else
				columnsNames.add("source");
			columnsNames.add("notes");
			columnsNames.add("reversible");
			columnsNames.add("in model");

			WorkspaceGenericDataTable qrt = new WorkspaceGenericDataTable(columnsNames, "reactions", pathwayName) {
				
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isCellEditable(int row, int col) {
					
					if (col==0 || col>4)						
						return true;
					else 
						return false;
				}
			};
			
			for(int i=0;i<pathway.size();i++) {
				
				if(pathway.get(i)==path)
					qrt.addLine((Object[])super.getRow(i), this.ids.get(i));
			}
			return qrt;
		}
		catch(Exception e) {
		
			e.printStackTrace();
		}

		return null;
	}
}
