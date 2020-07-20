package pt.uminho.ceb.biosystems.merlin.gui.datatypes.regulation;

import java.util.ArrayList;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceRegulationEntities;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceEntityAIB;

@Datatype(structure = Structure.LIST,namingMethod="getName")
public class WorkspaceRegulationEntitiesAIB extends WorkspaceRegulationEntities{
	
	private ArrayList<WorkspaceEntityAIB> entitiesGUIList;

	@ListElements
	public ArrayList<WorkspaceEntity> getEntitiesList() {
		return super.getEntitiesList();
	}
	
	@ListElements
	public ArrayList<WorkspaceEntityAIB> getEntitiesGUIList() {
		return this.entitiesGUIList;
	}
	
	public void setEntities(ArrayList<WorkspaceEntity> enties) {
		this.entities = enties;
		setChanged();
		notifyObservers();
	}	
	
	public String getName() {
		return "regulation";
	}

	public void addEntity(WorkspaceEntity regulator) {
		this.entities.add(regulator);
		setChanged();
		notifyObservers();
		
	}


}
