/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.gui.views;

import javax.swing.JPanel;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.utilities.UpdateUI;

/**
 * @author ODias
 *
 */
public abstract class WorkspaceUpdatablePanel extends JPanel implements UpdateUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WorkspaceEntity entity;

	/**
	 * 
	 */
	public WorkspaceUpdatablePanel() {
		
	}
	
	/**
	 * @param entity
	 */
	public WorkspaceUpdatablePanel(WorkspaceEntity entity){
		
		this.setEntity(entity);
	}
	
	/* (non-Javadoc)
	 * @see merlin_utilities.UpdateUI#updateTableUI()
	 */
	@Override
	public abstract void updateTableUI();

	/* (non-Javadoc)
	 * @see merlin_utilities.UpdateUI#addListenersToGraphicalObjects()
	 */
	@Override
	public abstract void addListenersToGraphicalObjects();
	
	@Override
	public String getWorkspaceName() {

		return this.entity.getWorkspace().getName();
	}

	/**
	 * @return
	 */
	public WorkspaceEntity getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 */
	public void setEntity(WorkspaceEntity entity) {
		this.entity = entity;
	}

}
