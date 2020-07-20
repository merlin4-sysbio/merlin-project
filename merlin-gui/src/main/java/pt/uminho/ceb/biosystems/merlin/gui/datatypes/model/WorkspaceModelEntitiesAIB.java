package pt.uminho.ceb.biosystems.merlin.gui.datatypes.model;

import java.util.ArrayList;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceModelEntities;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceEntityAIB;

@Datatype(structure = Structure.LIST,namingMethod="getName")
public class WorkspaceModelEntitiesAIB extends WorkspaceModelEntities {

	private ArrayList<WorkspaceEntityAIB> entitiesGUIList;

	@ListElements
	public ArrayList<WorkspaceEntity> getEntitiesList() {
		return super.getEntitiesList();
	}
	
	@ListElements
	public ArrayList<WorkspaceEntityAIB> getEntitiesGUIList() {
		return this.entitiesGUIList;
	}
	
	public String getName() {
		return "model";
	}

}