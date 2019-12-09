package pt.uminho.ceb.biosystems.merlin.gui.datatypes;

import java.util.List;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.interfaces.IEntityAIB;

/**
 * @author ODias
 *
 */
@Datatype(structure = Structure.LIST,namingMethod="getName")
public abstract class WorkspaceEntityAIB extends WorkspaceEntity implements IEntityAIB {

	private WorkspaceAIB projectGUI;
	private List<WorkspaceEntityAIB> subentities;

	public WorkspaceEntityAIB(WorkspaceTableAIB dbt, String name) {
		super(dbt, name);
	}

	/**
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public String[][] getStats(String name) throws Exception {

		
//		if(super.getStats()==null) {
//
//			List<Integer> values = EntityServices.getStats(name, connection);
//			String[][] data = EntityProcesses.getStats(values);
//			super.setStats(data);			
//		}
//		return super.getStats();		
		
		return null;
	}

	public List<WorkspaceEntityAIB> getSubentities() {
		return this.subentities;
	}

	public void setSubentities(List<WorkspaceEntityAIB> subentities) {
		this.subentities = subentities;
	}

	public String getName(String id)
	{
		return super.getName();
	}

	public String getSingular() {
		return super.getName();
	}

	public WorkspaceAIB getWorkspace() {

		return this.projectGUI;		
	}

	public void setProject(WorkspaceAIB project){

		this.projectGUI = project;
	}

}
