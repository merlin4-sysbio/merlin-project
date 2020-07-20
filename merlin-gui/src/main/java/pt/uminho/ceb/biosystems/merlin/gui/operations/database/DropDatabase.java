package pt.uminho.ceb.biosystems.merlin.gui.operations.database;

import java.io.File;
import java.util.List;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.Workspace;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.services.DatabaseServices;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

@Operation(description="drop workspace", name="drop workspace", enabled = true)
public class DropDatabase{

	/**
	 * @param project
	 */
	@Port(name="workspace",description="select workspace",direction=Direction.INPUT,order=1)
	public void setProject(WorkspaceAIB project) throws Exception{		//after this process the files still remain in the h2Database folder, because connections are still up using old SQL queries.
																			//When all queries are integrated in hibernate, these connections will disappear and a complete delete of the DB will be possible.
		String name = project.getName();
		
		DatabaseServices.dropConnection(name);
		
		try {
			DatabaseServices.dropDatabase(name);
			
			File databaseFolder = new File(FileUtils.getWorkspaceFolderPath(project.getName()));
			FileUtils.deleteDirectory(databaseFolder);
			
			List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(Workspace.class);
			
			for (ClipboardItem item : cl) {
				
				String workspaceName = ((WorkspaceAIB)item.getUserData()).getName();
				
				if(name.equals(workspaceName)) {
					
					Core.getInstance().getClipboard().removeClipboardItem(item);
				}
			}
			
			Workbench.getInstance().info("database "+name+" successfuly droped.");
		} 
		catch (Exception e) {
			e.printStackTrace();
			Workbench.getInstance().error("there was an error when trying to drop "+name+"!!");
		}
		
	}
}
