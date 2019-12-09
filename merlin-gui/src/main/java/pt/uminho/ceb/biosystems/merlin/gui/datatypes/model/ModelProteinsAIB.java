package pt.uminho.ceb.biosystems.merlin.gui.datatypes.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.ModelProteins;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTableAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.interfaces.IEntityAIB;
import pt.uminho.ceb.biosystems.merlin.processes.model.ModelProteinsProcesses;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;

@Datatype(structure = Structure.LIST,namingMethod="getName")
public class ModelProteinsAIB extends ModelProteins implements IEntityAIB {

	private boolean existsGenes;
	private String workspaceName;

	/**
	 * @param dbt
	 * @param name
	 */
	public ModelProteinsAIB(WorkspaceTableAIB dbt, String name) {

		super(dbt, name);
		this.workspaceName = dbt.getWorkspaceName();
		this.namesIndex = new HashMap<>();
		this.identifiers = new HashMap<>();
	}

	/* (non-Javadoc)
	 * @see pt.uminho.ceb.biosystems.merlin.core.datatypes.model.ProteinsModel#getStats()
	 */
	public String[][] getStats() {
		
		try {
			if(super.getStats()==null) {
				
				List<Integer> values = ModelProteinsServices.getStats(this.workspaceName);
				String[][] data = ModelProteinsProcesses.getStats(values);
				super.setStats(data);			
			}
		}
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
		return super.getStats();		
	}

	/**
	 * @param encoded
	 * @param update
	 * @return
	 */
	public WorkspaceGenericDataTable getAllProteins(boolean encoded, boolean update) {
		
		try {
			
			if(super.getAllProteins() == null || update) {
				
				Map<String,Integer> proteins = new HashMap<>();
				
				List<String[]> result = ModelProteinsServices.getMainTableData(workspaceName, encoded, proteins);
				WorkspaceGenericDataTable allProteins = ModelProteinsProcesses.getMainTableData(proteins, result, this.namesIndex, this.identifiers);
				
				super.setAllProteins(allProteins);			
			}
		} 
		catch (Exception e) {
			
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}
		
		return super.getAllProteins();
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getRowInfo(java.lang.String)
	 */
	public WorkspaceDataTable[] getRowInfo(int proteinIdentifier, boolean refresh) {
		
		
		if(super.getRowInfo() == null || refresh) {
			
			List<List<String[]>> result = ModelProteinsServices.getRowInfo(workspaceName, proteinIdentifier);
			
			WorkspaceDataTable[] allProteins = ModelProteinsProcesses.getRowInfo(result);
			
			super.setRowInfo(allProteins);			
		}
		
		return super.getRowInfo();
	}

	/**
	 * @param selectedRow
	 * @return
	 * @throws Exception 
	 */
	public String[] getProteinData(int selectedRow, boolean refresh) throws Exception {

		if(super.getProteinData() == null || refresh) {
			
			int identifierProtein = this.getIdentifiers().get(selectedRow);
			
			ProteinContainer res = null;
			String[] data = new String[12];

			try {

				res = ModelProteinsServices.getProteinData(this.workspaceName, identifierProtein);

				data[0] = String.valueOf(res.getIdProtein());
				data[1] = res.getName();
				data[2] = res.getClass_();
				data[3] = res.getInchi();
				if(res.getMolecularWeight() != null)
					data[4] = String.valueOf(res.getMolecularWeight());
				if(res.getMolecularWeightExp() != null)
					data[5] = String.valueOf(res.getMolecularWeightExp());
				if(res.getMolecularWeightKd() != null)
					data[6] = String.valueOf(res.getMolecularWeightKd());
				if(res.getMolecularWeightSeq() != null)
					data[7] = String.valueOf(res.getMolecularWeightSeq());
				if(res.getPi() != null)
					data[8] = String.valueOf(res.getPi());
				if(res.getExternalIdentifier() != null)
					data[9]= res.getExternalIdentifier()+";";
				if(res.getInModel() != null)
					data[10]= res.getInModel()+";";
				data[11] = String.valueOf(res.getSource());
			}
			catch (Exception ex) {

				ex.printStackTrace();
			}
			
			super.setProteinData(data);			
		}
		
		return super.getProteinData();

	}

	/**
	 * @param selectedRow
	 * @return
	 * @throws Exception 
	 */
	public String[] getSynonyms(int selectedRow, boolean refresh) throws Exception {

		if(super.getSynonyms() == null || refresh) {
			
			int identifierProtein = this.getIdentifiers().get(selectedRow);
			
			List<String> synonyms = ModelProteinsServices.getSynonyms(workspaceName, identifierProtein);
			
			String[][] data = new String[synonyms.size()][1];
			
			for(int i = 0; i < synonyms.size(); i++)
				data[i][0] = synonyms.get(i);
			
			String[] result = ModelProteinsProcesses.getSynonyms(data);
			
			super.setSynonyms(result);			
		}
		
		return super.getSynonyms();
	}
	
	/**
	 * @param id
	 * @return
	 */
	public String getProteinName(int identifier) {

		return this.getNameFromIndex(identifier);
	}


	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getName()
	 */
	public String getName() {

		return "proteins";
	}

	/**
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 */
	public boolean existGenes() throws IOException, Exception {

		if(!this.existsGenes)				
			this.existsGenes = ModelGenesServices.existGenes(this.workspaceName);
		
		return this.existsGenes;
	}

}
