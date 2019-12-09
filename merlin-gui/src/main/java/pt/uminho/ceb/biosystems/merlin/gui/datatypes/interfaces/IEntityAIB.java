package pt.uminho.ceb.biosystems.merlin.gui.datatypes.interfaces;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.Workspace;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;

/**
 * @author odias
 *
 */
public interface IEntityAIB {

	/**
	 * @param identifier
	 * @param refresh
	 * @return
	 * @throws Exception 
	 */
	public WorkspaceDataTable[] getRowInfo(int identifier, boolean refresh) throws Exception;

	public String getName();

	public Workspace getWorkspace();

	public WorkspaceGenericDataTable getMainTableData();

	public String getSingular();
}
