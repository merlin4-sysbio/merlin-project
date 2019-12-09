package pt.uminho.ceb.biosystems.merlin.core.datatypes;

import java.util.ArrayList;
import java.util.Observable;


public class WorkspaceRegulationEntities extends Observable{
	
	
	protected ArrayList<WorkspaceEntity> entities = null;

	public ArrayList<WorkspaceEntity> getEntities() {
		return entities;
	}

	public WorkspaceRegulationEntities()
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
	
	public String getName() {
		return "regulation";
	}
	

}
