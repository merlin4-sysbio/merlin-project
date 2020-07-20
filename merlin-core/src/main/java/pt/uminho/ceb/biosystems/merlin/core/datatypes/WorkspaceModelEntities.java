package pt.uminho.ceb.biosystems.merlin.core.datatypes;

import java.util.ArrayList;
import java.util.Observable;

public class WorkspaceModelEntities extends Observable {

	private ArrayList<WorkspaceEntity> entities = null;
	
	public ArrayList<WorkspaceEntity> getEntities() {
		return entities;
	}

	public WorkspaceModelEntities()
	{
		this.entities = new ArrayList<WorkspaceEntity>();
	}
	
	public ArrayList<WorkspaceEntity> getEntitiesList() {
		return entities;
	}

	public void setEntities(ArrayList<WorkspaceEntity> enties) {
		this.entities = enties;
		setChanged();
		notifyObservers();
	}
	
//	public String getName() {
//		return "regulator";
//	}
//	
	public void addEntity(WorkspaceEntity entitie) {
		
		this.entities.add(entitie);
		setChanged();
		notifyObservers();
	}
}