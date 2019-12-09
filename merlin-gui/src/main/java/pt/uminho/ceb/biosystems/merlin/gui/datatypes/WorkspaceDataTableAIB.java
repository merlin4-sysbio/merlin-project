package pt.uminho.ceb.biosystems.merlin.gui.datatypes;


import java.util.List;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;

@Datatype(structure = Structure.SIMPLE, namingMethod="getName")
public class WorkspaceDataTableAIB extends WorkspaceDataTable {

	private static final long serialVersionUID = -1L;

	/**
	 * @param columnsNames
	 * @param name
	 */
	public WorkspaceDataTableAIB(List<String> columnsNames, String name) {
		
		super(columnsNames, name);
	}

	/**
	 * @param columnsNames
	 * @param name
	 */
	public WorkspaceDataTableAIB(String[] columnsNames, String name) {
		
		super(columnsNames, name);
	}
}
