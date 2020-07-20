package pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation;

import java.util.ArrayList;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.ListElements;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceAnnotations;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;

/**
 * @author Oscar Dias
 *
 */
@Datatype(structure = Structure.LIST,namingMethod="getName")
public class WorkspaceAnnotationEntitiesAIB extends WorkspaceAnnotations {


	@ListElements
	public ArrayList<WorkspaceEntity> getEntitiesList() {
		return super.getEntitiesList();
	}

	public void setEntities(ArrayList<WorkspaceEntity> enties) {
		super.setEntities(enties);
		setChanged();
		notifyObservers();
	}

	public String getName() {
		return "annotation";
	}
}
