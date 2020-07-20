package pt.uminho.ceb.biosystems.merlin.gui.datatypes.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.ModelPathways;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTableAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.interfaces.IEntityAIB;
import pt.uminho.ceb.biosystems.merlin.processes.model.ModelPathwaysProcesses;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelPathwaysServices;


/**
 * @author ODias
 *
 */
@Datatype(structure= Structure.SIMPLE, namingMethod="getName",removable=true,removeMethod ="remove")
public class ModelPathwaysAIB extends ModelPathways implements IEntityAIB {

	private String workspaceName;
	
	/**
	 * @param dbt
	 * @param name
	 */
	public ModelPathwaysAIB(WorkspaceTableAIB dbt, String name) {

		super(dbt, name);
		workspaceName = dbt.getWorkspaceName();
		this.namesIndex = new HashMap<>();
		this.identifiers = new HashMap<>();
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public String[][] getStats() {

		if(super.getStats()==null) {

			List<Integer> res = ModelPathwaysServices.getStats(this.workspaceName);

			super.setStats(ModelPathwaysProcesses.getStats(res));
		}

		return super.getStats();
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getData()
	 */
	public WorkspaceGenericDataTable getMainTableData() {

		if(super.getMainTableData()==null) {

			Map<Integer, List<Object>> res = ModelPathwaysServices.getMainTableData(workspaceName, this.namesIndex, this.identifiers);
			super.setMainTableData(ModelPathwaysProcesses.getMainTableData(res));
		}

		return super.getMainTableData();
	}


	/**
	 *
	 */
	public WorkspaceDataTable[] getRowInfo(int pathwayIdentifier, boolean refresh) {

		refresh = true;

		if(super.getRowInfo()==null || refresh) {

			try {
				Map<String, List<List<String>>> dataList = ModelPathwaysServices.getRowInfo(workspaceName, pathwayIdentifier);
				super.setRowInfo(ModelPathwaysProcesses.getRowInfo(dataList));
			} 
			catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}
		}

		return super.getRowInfo();
	}
}
