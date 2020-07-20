package pt.uminho.ceb.biosystems.merlin.gui.datatypes;

import java.util.List;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.datatypes.annotation.Clipboard;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.Workspace;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceAnnotations;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.WorkspaceModelEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.regulation.WorkspaceRegulationEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.validation.WorkspaceValidationEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.services.DatabaseServices;

/**
 * @author Oscar Dias
 *
 */
@Datatype(structure = Structure.COMPLEX,namingMethod="getName",removable=true,removeMethod ="removeWorkspace")
public class WorkspaceAIB extends Workspace {

	/**
	 * 
	 */
	private WorkspaceDatabaseAIB database;

	/**
	 * @param database
	 * @param name
	 */
	public WorkspaceAIB(WorkspaceDatabaseAIB database) {

		super(database);
		this.database = database;
		this.workspaceName = database.getWorkspaceName();
		this.setTaxonomyID(-1);
	}

	@Clipboard(name="model",order=1)
	public WorkspaceModelEntitiesAIB getEntities() {
		return this.database.getEntities();
	}
	
	@Clipboard(name="annotation",order=2)
	public WorkspaceAnnotations getAnnotations() {
		return this.database.getAnnotations();
	}
	
	@Clipboard(name="regulation",order=3)
	public WorkspaceRegulationEntitiesAIB getRegulation() {
		return this.database.getRegulation();
	}
	
	@Clipboard(name="validation" ,order=4)
	public WorkspaceValidationEntitiesAIB getValidation() {
		return this.database.getValidation();
	}

	public void setDatabase(WorkspaceDatabaseAIB db) {
		this.database = db;
	}
	
	public WorkspaceDatabaseAIB getDatabase() {
		return database;
	}
	

	public String getName() {
		
		return super.getName();
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void removeWorkspace() throws Exception{

		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(WorkspaceAIB.class);
		ClipboardItem torem = null;
		for(ClipboardItem item : items){
			
			if(item.getUserData().equals(this)){
				DatabaseServices.dropConnection(this.getName());
				torem = item;
				break;
			}
		}
		Core.getInstance().getClipboard().removeClipboardItem(torem);
		System.gc();
		Workbench.getInstance().info("workspace " + this.getName() + " closed!");
	}
}
