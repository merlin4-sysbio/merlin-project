package pt.uminho.ceb.biosystems.merlin.gui.datatypes.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.ModelGenes;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTableAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.interfaces.IEntityAIB;
import pt.uminho.ceb.biosystems.merlin.processes.model.ModelGenesProcesses;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;

@Datatype(structure = Structure.LIST,namingMethod="getName")
public class ModelGenesAIB extends ModelGenes implements IEntityAIB {

	private String workspaceName;

	/**
	 * @param dbt
	 * @param name
	 * @param ultimlyComplexComposedBy
	 */
	public ModelGenesAIB(WorkspaceTableAIB dbt, String name) {

		super(dbt, name);
		this.workspaceName = dbt.getWorkspaceName();
		this.setNamesIndex(new HashMap<>());
		this.setIdentifiers(new HashMap<>());
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public String[][] getStats() {

		try {
			if(super.getStats()==null) {

				double[] res = ModelGenesServices.getStats(this.workspaceName);

				super.setStats(ModelGenesProcesses.getStats(res));
			}

			return super.getStats();
		} 
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	public WorkspaceGenericDataTable getMainTableData(boolean encoded, boolean update) throws IOException, Exception {

		if(super.getMainTableData()==null || update) {

			WorkspaceGenericDataTable dataTable = ModelGenesProcesses.getMainTableData(this.workspaceName, encoded, this.getNamesIndex(), this.getIdentifiers());
			super.setMainTableData(dataTable);
		}

		return super.getMainTableData();
	}


	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getRowInfo(java.lang.String)
	 */
	public WorkspaceDataTable[] getRowInfo(int identifier, boolean refresh) throws Exception {

		if(super.getRowInfo()==null || refresh) {

			Map<String, List<List<String>>> dataList = ModelGenesServices.getRowInfo(this.workspaceName, identifier);
			super.setRowInfo(ModelGenesProcesses.getRowInfo(dataList));
		}

		return super.getRowInfo();
	}

	/**
	 * @param id
	 * @return
	 */
	public String getGeneName(int id) {

		return this.getNamesIndex().get(id);
	}

	/**
	 * @param selectedRow
	 * @return
	 * @throws Exception 
	 */
	public GeneContainer getGeneData(int selectedRow) throws Exception {

		if(super.getGeneData()==null) {

			super.setGeneData(ModelGenesServices.getGeneDataById(this.workspaceName, this.getIdentifiers().get(selectedRow)));
		}

		return super.getGeneData();
	}

	/**
	 * @param selectedRow
	 * @return
	 */
	public String[] getSubunits(int selectedRow, Boolean update) {

		try {
			if(super.getSubunits()==null || update) {

				String[][] dataList = ModelGenesServices.getSubunitsByGeneId(this.workspaceName, this.getIdentifiers().get(selectedRow));

				super.setSubunits(ModelGenesProcesses.getSubunits(dataList));
			}

			return super.getSubunits();
		}
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * @return
	 * @throws Exception 
	 */
	public String[][] getProteins(String databaseName) throws Exception {

		if(super.getProteins()==null) {

			String[][] res = ModelProteinsServices.getProteins(databaseName);

			super.setProteins(ModelGenesProcesses.getProteins(res));
		}

		return super.getProteins();
	} 

}