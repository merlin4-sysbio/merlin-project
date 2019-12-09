package pt.uminho.ceb.biosystems.merlin.core.datatypes;

import java.sql.SQLException;

public class WorkspaceTable {

	private String name;
	private String[] columms;
	private String size;
	
	/**
	 * @param name
	 * @param columms
	 * @param results
	 */
	public WorkspaceTable(String name, String[] columms) {
		
		this.name = name;
		this.columms = columms;
	}
	
	/**
	 * @param name
	 * @param columms
	 * @param results
	 */
	public WorkspaceTable(String name) {
		
		this.name = name;
	}
	
	public String getName() {
		return name.toLowerCase();
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String[] getColumms() {
		return columms;
	}

	public void setColumms(String[] columms) {
		this.columms = columms;
	}

	public String toString() {
		return name;
	}

	public String getSize() throws SQLException {
		
			return this.size;
	}
}
