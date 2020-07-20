/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.workbench.MainWindow;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceEntityAIB;
import pt.uminho.ceb.biosystems.merlin.gui.views.WorkspaceUpdatablePanel;

/**
 * @author ODias
 *
 */
public class AIBenchUtils {


	/**
	 * Get project object from name
	 * 
	 * @param projectName
	 * @return
	 */
	public static WorkspaceAIB getProject(String projectName){



		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(WorkspaceAIB.class);

		for(int i=0; i<cl.size(); i++) {

			WorkspaceAIB project = (WorkspaceAIB) cl.get(i).getUserData();

			if(project.getName().equals(projectName))
				return project;

		}
		return null;
	}

	/**
	 * Get project object from name
	 * 
	 * @param projectName
	 * @return
	 */
	public static List<WorkspaceAIB> getAllProjects(){

		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(WorkspaceAIB.class);

		List<WorkspaceAIB> all = new ArrayList<>();

		for(int i=0; i<cl.size(); i++) 
			all.add((WorkspaceAIB) cl.get(i).getUserData());

		return all;
	}

	/**
	 * retrieves all open projects(workspaces) in clipboard
	 * 
	 * @return 
	 */
	public static List<WorkspaceAIB> getAllClipboardProjects(){

		List<ClipboardItem> cl = Core.getInstance().getClipboard().getAllItems();
		List<WorkspaceAIB> projects = new ArrayList<>();

		for(int i=0; i<cl.size(); i++) {

			WorkspaceEntityAIB entity = (WorkspaceEntityAIB) cl.get(i).getUserData();
			projects.add(entity.getWorkspace());
		}

		return projects;

	}


	/**
	 * @return
	 */
	public static List<String> getProjectNames() {

		List <String> projectNames = new ArrayList<String>();

		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(WorkspaceAIB.class);

		for(int i=0; i<cl.size(); i++) {

			ClipboardItem item = cl.get(i);
			projectNames.add(item.getName());
		}

		return projectNames;
	}


	/**
	 * @param projectName
	 * @param datatype
	 */
	public static void updateView(String projectName, Class<?> datatype) {

		ClipboardItem item = null;
		
		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(datatype);
		
		for(int i=0; i<cl.size(); i++) {

			if(datatype.equals(cl.get(i).getUserData().getClass())) {
				
				if( cl.get(i).getName().equals(projectName)) {

					item = cl.get(i);
				}
				else {
					
					if(cl.get(i).getUserData().getClass().getSuperclass().equals(WorkspaceEntityAIB.class) ||
							cl.get(i).getUserData().getClass().getSuperclass().getSuperclass().equals(WorkspaceEntityAIB.class.getSuperclass()) ||
							cl.get(i).getUserData().getClass().equals(WorkspaceEntityAIB.class)) {

						WorkspaceEntity entity = (WorkspaceEntity) cl.get(i).getUserData();

						if(entity.getWorkspace().getName().equals(projectName)){
							
							item = cl.get(i);
							entity.resetDataStuctures();
						}
					}
				}
			}
			else {

				WorkspaceEntityAIB entity = (WorkspaceEntityAIB) cl.get(i).getUserData();

				if(entity.getWorkspace().getName().equals(projectName)) {
					
					item = cl.get(i);
					entity.resetDataStuctures();
				}
					
			}
		}
		
		if(item!=null) {

			MainWindow window = (MainWindow) Workbench.getInstance().getMainFrame();

			List<Component> list = window.getDataViews(item);

			for(Component component : list) {

				if(component instanceof WorkspaceUpdatablePanel){

					WorkspaceUpdatablePanel view = (WorkspaceUpdatablePanel) component;

					if(view.getWorkspaceName().equals(projectName))
						view.updateTableUI();
				}
			}
		}
	}

	/**
	 * Get entity for project
	 * 
	 * @param projectName
	 * @param datatype
	 * @return
	 */
	public static WorkspaceEntity getEntity(String projectName, Class<?> datatype){

		List<ClipboardItem> cl = Core.getInstance().getClipboard().getItemsByClass(datatype);

		for(int i=0; i<cl.size(); i++) {

			WorkspaceEntity entity = (WorkspaceEntity) cl.get(i).getUserData();
//			WorkspaceEntity entityRet = (WorkspaceEntity) cl.get(i).getUserData();

			if(datatype.equals(entity.getClass())) {

				if(entity.getWorkspace().getName().equals(projectName))
					return entity;
			}
		}
		return null;
	}

}
