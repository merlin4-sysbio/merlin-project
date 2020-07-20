package pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.AnnotationCompartments;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.compartments.AnnotationCompartmentsGenes;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.ICompartmentResult;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTableAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.interfaces.IEntityAIB;
import pt.uminho.ceb.biosystems.merlin.processes.model.compartments.CompartmentsAnnotationProcesses;
import pt.uminho.ceb.biosystems.merlin.processes.model.compartments.services.CompartmentsAnnotationServices;
import pt.uminho.ceb.biosystems.merlin.services.annotation.AnnotationCompartmentsServices;

@Datatype(structure= Structure.SIMPLE, namingMethod="getName",removable=true,removeMethod ="remove")
public class AnnotationCompartmentsAIB extends AnnotationCompartments implements IEntityAIB {

	/**
	 * @param dbt
	 * @param name
	 */
	public AnnotationCompartmentsAIB(WorkspaceTableAIB dbt, String name) {

		super(dbt, name);
		this.namesIndex = new HashMap<>();
		this.identifiers = new HashMap<>();

	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getName()
	 */
	public String getName() {

		return "compartments";
	}


	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public String[][] getStats() {
		
		try {
			if(super.getStats() == null) {
				
				List<Integer> result = AnnotationCompartmentsServices.getStats(this.getWorkspace().getName());
				this.setStats(CompartmentsAnnotationProcesses.getStats(result));
			}
		} 
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}

		return this.stats;
	}

	/**
	 * @return
	 */
	public WorkspaceGenericDataTable getMainTableData(boolean update) {

		if(this.mainTableData == null || update) {
			
			Map<Integer, ArrayList<Object>> data = CompartmentsAnnotationServices.getMainTableData(this.getWorkspace().getName(), 
					this.getWorkspace().getTaxonomyID(), threshold, namesIndex, identifiers);

			this.mainTableData = CompartmentsAnnotationProcesses.getMainTableData(data);
		}

		return this.mainTableData;
	}


	/**
	 * @throws Exception 
	 *
	 */
	public WorkspaceDataTable[] getRowInfo(int id, boolean refresh) throws Exception {
		if(super.getRowInfo()==null || refresh) {
			
			Map<String, List<List<String>>> result = new HashMap<>();
					
			List<String[]> data = AnnotationCompartmentsServices.getCompartmentsAnnotationReportsHasCompartmentsNameAndScore(this.getWorkspace().getName(), id);
			
			List<List<String>> resultLists = new ArrayList<List<String>>();

			for(int i=0; i<data.size(); i++){
				String[] list = data.get(i);

				ArrayList<String> resultsList = new ArrayList<>();

				resultsList.add(list[0]);
				Double score = Double.parseDouble(list[1])/10;
				resultsList.add(score.toString());
				resultLists.add(resultsList);
			}

			result.put("compartments", resultLists);
			this.rowInfo = CompartmentsAnnotationProcesses.getRowInfo(result);
		}

		return super.getRowInfo();
	}

	/**
	 * @param projectLineage
	 * @param tool
	 * @param results
	 * @param statement
	 * @throws Exception
	 */
	public static void loadPredictions(String databaseName, String projectLineage, String tool,
			Map<String, ICompartmentResult> results) throws Exception {

		CompartmentsAnnotationServices.loadPredictions(databaseName, projectLineage, tool, results);
	}

	/**
	 * @param threshold
	 * @param statement
	 * @return
	 */
	public Map<Integer, AnnotationCompartmentsGenes> runCompartmentsInterface(double threshold){

		return CompartmentsAnnotationServices.runCompartmentsInterface(this.getWorkspace().getName(), threshold, this.getWorkspace().getTaxonomyID());
	}

	/**
	 * @param thold
	 */
	public void setThreshold(Double thold) {

		this.threshold = thold;
	}

	/**
	 * @param 
	 * @return
	 */
	public Double getThreshold() {

		return this.threshold;
	}

	/**
	 * @param id
	 * @return
	 */
	public String getGeneName(int id) {

		return this.namesIndex.get(id);
	}

}
