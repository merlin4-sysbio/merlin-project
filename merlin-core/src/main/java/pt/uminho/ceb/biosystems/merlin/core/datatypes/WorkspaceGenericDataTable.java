package pt.uminho.ceb.biosystems.merlin.core.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author oscar
 *
 */
public class WorkspaceGenericDataTable extends WorkspaceDataTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String windowName;
	protected List<String> metaboliteType;
	private List<Integer> identifiers;
	
	/**
	 * @param columnsNames
	 * @param name
	 * @param windowName
	 */
	public WorkspaceGenericDataTable(List<String> columnsNames, String name, String windowName) {
		
		super(columnsNames, name);
		this.windowName = windowName;
		this.identifiers = new ArrayList<>();
		this.metaboliteType = new ArrayList<>();
    }
    
    /**
     * @param line
     * @param id
     */
    public void addLine(ArrayList<Object> line,  int id) {
    	
    	super.addLine(line);
    	this.identifiers.add(id);
    }
    
    /**
     * @param line
     * @param id
     */
    public void addLine(List<Object> line, int id ) {
      	super.addLine(line);
    	this.identifiers.add(id);
	}
        
    /**
     * @param line
     * @param id
     */
    public void addLine(Object[] line,  int id) {
    	super.addLine(line);
    	this.identifiers.add(id);
    }
    
    /**
     * @param line
     * @param type
     * @param id
     */
    public void addLine(ArrayList<Object> line, String type, int id) {
    	super.addLine(line);
    	this.metaboliteType.add(type);
    	this.identifiers.add(id);
    }
    
    /**
     * @param line
     * @param type
     * @param id
     */
    public void addLine(List<Object> line, String type, int id) {
    	super.addLine(line);
    	this.metaboliteType.add(type);
    	this.identifiers.add(id);
    }
    
    
    /**
     * 
     * @param index
     * @param line
     * @param type
     * @param id
     */
    public void setLine(int index, List<Object> line, String type, int id) {
    	super.setLine(index, line);
    	this.metaboliteType.set(index, type);
    	this.identifiers.set(index, id);
    }
    
    /**
     * @param row
     * @return
     */
    public String getRowType(int row) {
    	
    	return metaboliteType.get(row);
    }
    
    /**
     * @return
     */
    public String getWindowName() {
    	
    	return windowName;
    }
    
    /**
     * @param row
     * @return
     */
    public int getRowID(int row) {
    	
    	return identifiers.get(row);
    }

	/**
	 * @return
	 */
	public List<Integer> getidentifiers() {
		return identifiers;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public int getRowFromID(int id) {

		for(int i =0; identifiers.size()>i; i++)
			if(this.identifiers.get(i) == (id))
				return i;
		
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GenericDataTable [windowName=" + windowName + ", identifiers=" + identifiers
				+ "]";
	}
	
	
}