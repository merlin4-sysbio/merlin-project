package pt.uminho.ceb.biosystems.merlin.gui.datatypes;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDatabase;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.WorkspaceAnnotationEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.WorkspaceModelEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.regulation.WorkspaceRegulationEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.validation.WorkspaceValidationEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelProteinsServices;

/**
 * @author adias
 *
 */
@Datatype(structure = Structure.COMPLEX, namingMethod="getName")
public class WorkspaceDatabaseAIB extends WorkspaceDatabase {

	private String workspaceName;
	private WorkspaceModelEntitiesAIB entities;
	private WorkspaceAnnotationEntitiesAIB annotations;
	private WorkspaceValidationEntitiesAIB validation;
	private WorkspaceRegulationEntitiesAIB regulation;

	/**
	 * @param databaseAccess
	 * @throws SQLException 
	 */
	public WorkspaceDatabaseAIB(String workspaceName) {
		
		super();
		try {
			this.workspaceName = workspaceName;
			this.getComplex();
		}
		catch (Exception e) {
			Workbench.getInstance().error(e);
//			e.printStackTrace();
		}
	}
	
	public WorkspaceModelEntitiesAIB getEntities() {
		return entities;
	}
	
	public void setEntities(WorkspaceModelEntitiesAIB entities) {
		this.entities = entities;
	}
	
	public WorkspaceAnnotationEntitiesAIB getAnnotations() {
		return annotations;
	}
	
	public void setEntities(WorkspaceAnnotationEntitiesAIB annotations) {

		this.annotations = annotations;		
	}
	
	public WorkspaceValidationEntitiesAIB getValidation() {
		return validation;
	}
	
	public void setEntities(WorkspaceValidationEntitiesAIB validation) {
		this.validation = validation;		
	}
	
	public WorkspaceRegulationEntitiesAIB getRegulation() {
		return regulation;
	}
	
	public void setEntities(WorkspaceRegulationEntitiesAIB regulation) {
		this.regulation = regulation;		
	}

	public void setTables(WorkspaceTablesAIB dbt) {
		super.setTables(dbt);
		setChanged();
		notifyObservers();
	}

	protected void getComplex() throws Exception {
		
		HashMap<String,String[]> complexCodingGeneData = new HashMap<String,String[]>();

		HashMap<String,LinkedList<String>> complexComposedBy = new HashMap<String,LinkedList<String>>();

		HashMap<String,LinkedList<String>> proteinGenes = new HashMap<String,LinkedList<String>>();

		List<String[]> result = ModelProteinsServices.getAllFromProteinComposition(workspaceName);

		if(result != null && result.size() > 0) {
			
				for(int i=0; i<result.size(); i++) {
					
					String[] list = result.get(i);
					addToList(list[1], list[0], complexComposedBy);
				}

				result = ModelProteinsServices.getProteinComposition(this.workspaceName);

				for(int i=0; i<result.size(); i++) {
					
					String[] list = result.get(i);
					if(!complexCodingGeneData.containsKey(list[0])) {
						
						complexCodingGeneData.put(
								list[0], 
								new String[]{list[0], list[1], list[2]}
								);
					}
					//addToList(rs.getString(1), rs.getString(4), proteinGenes);
					addToList(list[0], "", proteinGenes);
				}
				getRestOfComplexs(proteinGenes, complexComposedBy, super.getUltimlyComplexComposedBy());
		}
	}

	/**
	 * @return the workspaceName
	 */
	public String getWorkspaceName() {
		return workspaceName;
	}

	/**
	 * @param workspaceName the workspaceName to set
	 */
	public void setWorkspaceName(String workspaceName) {
		this.workspaceName = workspaceName;
	}


}
