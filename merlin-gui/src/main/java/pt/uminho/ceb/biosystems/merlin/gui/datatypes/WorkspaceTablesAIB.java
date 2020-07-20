package pt.uminho.ceb.biosystems.merlin.gui.datatypes;

import java.util.ArrayList;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTables;

@Datatype(structure = Structure.LIST, namingMethod="getName")
public class WorkspaceTablesAIB extends WorkspaceTables {

	private static final long serialVersionUID = -1L;
	private ArrayList<WorkspaceTableAIB> list = null;

	public WorkspaceTablesAIB() {
		list = new ArrayList<WorkspaceTableAIB>();
	}

	@ListElements
	public ArrayList<WorkspaceTableAIB> getList() {
		return list;
	}
	
}
