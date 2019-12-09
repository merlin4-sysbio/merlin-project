package pt.uminho.ceb.biosystems.merlin.core.datatypes;

import java.util.ArrayList;
import java.util.Observable;

/**
 * @author Oscar Dias
 *
 */
public class WorkspaceAnnotations extends Observable {


	/**
	 * 
	 */
	private ArrayList<WorkspaceEntity> annotations = null;

	/**
	 * 
	 */
	public WorkspaceAnnotations() {

		this.annotations = new ArrayList<WorkspaceEntity>();
	}


	public ArrayList<WorkspaceEntity> getEntities() {
		return annotations;
	}


	public ArrayList<WorkspaceEntity> getEntitiesList() {
		return annotations;
	}

	public void setEntities(ArrayList<WorkspaceEntity> enties) {
		this.annotations = enties;
		setChanged();
		notifyObservers();
	}

	public String getName() {
		return "annotation";
	}
}
