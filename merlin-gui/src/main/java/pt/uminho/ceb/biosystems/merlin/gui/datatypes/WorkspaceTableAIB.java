package pt.uminho.ceb.biosystems.merlin.gui.datatypes;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTable;

@Datatype(structure = Structure.COMPLEX,namingMethod="getName")
public class WorkspaceTableAIB extends WorkspaceTable  {

	private String workspaceName;

	/**
	 * @param name
	 * @param columms
	 * @param results
	 */
	public WorkspaceTableAIB(String name, String[] columms,
			String workspaceName) {

		super(name, columms);
		this.workspaceName = workspaceName;
	}
	
	/**
	 * @param name
	 * @param results
	 */
	public WorkspaceTableAIB(String name, String workspaceName) {

		super(name);
		this.workspaceName = workspaceName;
	}
	
	/**
	 * @return
	 */
	public String getWorkspaceName() {
		return this.workspaceName;
	}
}
