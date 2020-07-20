package pt.uminho.ceb.biosystems.merlin.gui.datatypes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.MetaboliteContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.ModelMetabolites;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTableAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.interfaces.IEntityAIB;
import pt.uminho.ceb.biosystems.merlin.processes.model.ModelMetabolitesProcesses;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelMetabolitesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelReactionsServices;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

@Datatype(structure= Structure.LIST, namingMethod="getName",removable=true)//,removeMethod ="remove")
public class ModelMetabolitesAIB extends ModelMetabolites implements IEntityAIB {

	private String workspaceName;

	/**
	 * @param dbt
	 * @param name
	 */
	public ModelMetabolitesAIB(WorkspaceTableAIB tableGUI, String name) {
		
		super(tableGUI, name);
		workspaceName = tableGUI.getWorkspaceName();
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public String[][] getStats() {
		
		if(super.getStats() == null) {
			
			try {
				List<Pair<Integer, String>> data = ModelMetabolitesServices.getStats(workspaceName);
				String[][] stats = ModelMetabolitesProcesses.getStats(data);
				super.setStats(stats);
			} 
			catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}
		}
		
		return super.getStats();
	}

	/**
	 * @param selection
	 * @return
	 * @throws Exception 
	 */
	public WorkspaceGenericDataTable getMainTableData(int selection, boolean encoded, ArrayList<Integer> types) throws Exception {
		
//		if(super.getDataReagentProduct() == null) 
		{
			
			Map<Integer, String> typeMap = new HashMap<> ();
//			this.identifiers
//			this.namesIndex
			Map<Integer, List<Object>> data = ModelMetabolitesServices.getMainTableData(workspaceName, selection, encoded, types, typeMap);
			WorkspaceGenericDataTable dataTable = ModelMetabolitesProcesses.getMainTableData(this.name, data, typeMap);
			super.setDataReagentProduct(dataTable);
		}
		
		return super.getDataReagentProduct();
	}	

	/**
	 * @throws Exception 
	 *
	 */
	public WorkspaceDataTable[] getRowInfo(int metaboliteIdentifier, boolean refresh) throws Exception {
		
		if(super.getReaction() == null || refresh) {
			
			Map<String, List<ArrayList<String>>> data;
			try {
				data = ModelMetabolitesServices.getRowInfo(workspaceName, metaboliteIdentifier);
				WorkspaceDataTable[] dataTable = ModelMetabolitesProcesses.getRowInfo(data);
				super.setReaction(dataTable);
			} 
			catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}
			
		}
		
		return super.getReaction();
	}
	
	/* (non-Javadoc)
	 * @see pt.uminho.ceb.biosystems.merlin.core.datatypes.metabolic_regulatory.Entity#getName(java.lang.String)
	 */
	public String getName(String id) {
		
		return "metabolites";
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getSingular()
	 */
	public String getSingular() {
		
		return "metabolite: ";
	}
	
	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#hasWindow()
	 */
	public boolean hasWindow() {
		
		return true;
	}
	
	/**
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public int getMetaboliteOccurrence (String name) throws Exception {		

	if(super.getMetaboliteOccurrence() < 0)
			super.setMetaboliteOccurrence((int) ModelMetabolitesServices.getMetaboliteOccurrence(this.workspaceName, name));
		
		return super.getMetaboliteOccurrence();
	}
	
	/**
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public String getKeggIdOccurence (String name) throws Exception {
		
		//if(super.getKeggOccurence() == null) 			
			super.setKeggOccurence(ModelMetabolitesServices.getKeggIdOccurence(this.workspaceName, name));
		
		return super.getKeggOccurence();
	}
	
	
	public MetaboliteContainer getMetaboliteData (String metabolite) throws Exception {
		
//		if(super.getMetaboliteData() == null) 			
			super.setMetaboliteData(ModelMetabolitesServices.getModelCompoundByName(this.workspaceName, metabolite));
		
		return super.getMetaboliteData();
	}
	
	public List<String> getRelatedReactions(String name) {
		
		try {
			//if(super.getRelatedReactions() == null || super.getRelatedReactions().isEmpty()) 			
				super.setRelatedReactions(ModelReactionsServices.getRelatedReactions(this.workspaceName, name));
			
		} catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
		return super.getRelatedReactions();
	}

}
