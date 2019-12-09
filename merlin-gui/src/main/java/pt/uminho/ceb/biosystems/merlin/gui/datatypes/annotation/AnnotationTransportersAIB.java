
package pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.AnnotationTransporters;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTableAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.interfaces.IEntityAIB;

/**
 * @author ODias
 *
 */
@Datatype(structure= Structure.SIMPLE, namingMethod="getName",removable=true,removeMethod ="remove")
public class AnnotationTransportersAIB extends AnnotationTransporters implements IEntityAIB{
	
	private HashMap<String, String> names;

	/**
	 * @param table
	 * @param name
	 */
	public AnnotationTransportersAIB(WorkspaceTableAIB table, String name) {
		
		super(table, name);
	}
	
	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public String[][] getStats() {

		String[][] res = new String[3][];
		try {
			
			
//			Integer[] result = TransportersAPI.getStats(this.table.getName(), stmt);
//			
//			res[0] = new String[] {"number of metabolites", ""+result[0]};
//			res[1] = new String[] {"number of metabolites with no name associated", ""+ result[1]};
//			res[2] = new String[] {"number of compounds", ""+ result[2]};

		}
		catch(Exception e){e.printStackTrace();}
		return res;
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getData()
	 */
	public WorkspaceGenericDataTable getData() {
		
		this.names = new HashMap<String, String>();
		ArrayList<String> columnsNames = new ArrayList<String>();

		columnsNames.add("Info");
		columnsNames.add("Genes");
		columnsNames.add("TC family");
		columnsNames.add("Number of encoded proteins");

		WorkspaceGenericDataTable res = new WorkspaceGenericDataTable(columnsNames, "Transporters", "Transport proteins encoding genes") {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int col) {
				
				if (col==0) {
					
					return true;
				}
				else {
					
					return false;
				}
			}
		};

		try {
			
			ArrayList<String[]> result = new ArrayList<>();
			
			for(int i=0; i<result.size(); i++){
				String[] list = result.get(i);
				
				List<Object> ql = new ArrayList<Object>();

				ql.add("");
				ql.add(list[1]);
				ql.add(list[4]);
				ql.add(list[2]);
				ql.add(list[5]);
				res.addLine(ql, Integer.parseInt(list[0]));

				if( list[1]==(null))
					this.names.put(list[0], list[2]);
				else
					this.names.put(list[0], list[1]);
			}
		} 
		catch(Exception e) {
			
			e.printStackTrace();
		}

		return res;
	
	}
	
	public HashMap<Integer,Integer[]> getSearchData() {
		
		HashMap<Integer,Integer[]> res = new HashMap<Integer,Integer[]>();

		res.put(0, new Integer[]{0});
		res.put(1, new Integer[]{1});
		res.put(2, new Integer[]{2});
		res.put(3, new Integer[]{3});
		res.put(4, new Integer[]{4});

		return res;
	}
	
	public String[] getSearchDataIds() {
		
		String[] res = new String[]{"name" , "InChi", "formula",
		};

		return res;
	}
	
	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getRowInfo(java.lang.String)
	 */
	public WorkspaceDataTable[] getRowInfo(int identifier, boolean refresh) {
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#hasWindow()
	 */
	public boolean hasWindow() {
		
		return true;
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getName(java.lang.String)
	 */
	public String getGeneName(String id) {
		
		return this.names.get(id);
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getSingular()
	 */
	public String getSingular() {
		
		return "transport protein: ";
	}
}
