package pt.uminho.ceb.biosystems.merlin.gui.operations.loaders.annotation.compartments_new;

import java.io.File;
import java.util.HashMap;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.ProgressHandler;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.AIBenchUtils;

@Operation(name="Load Reports",description="Load reports")
public class LoadReportsCompartmentalization {


	private String tool;
	private WorkspaceAIB workspace;
	private HashMap<String, String> operationsCompartments;
	private File file;
	private String link;
	

	@Port(direction=Direction.INPUT, name="Workspace",description="", order = 1)
	public void setWorkspace(String workspace) {

		this.workspace = AIBenchUtils.getProject(workspace);
	}


	@Port(direction=Direction.INPUT, name="Tool",description="", order = 2)
	public void setTool(String tool) {

		this.tool = tool;

		this.operationsCompartments = new HashMap<String,String>();
		this.operationsCompartments.put("LocTree3", "operations.LoadLocTreeReports.ID");
		this.operationsCompartments.put("WoLFPSORT", "operations.LoadWoLFPSORTReports.ID");
		this.operationsCompartments.put("PSortb3", "operations.LoadPSortReports.ID");

		if (tool.equals("PSortb3")) {

			FilePathPopUp filePath = new FilePathPopUp();

			File file = filePath.getFile();

			this.file = file;

			OperationDefinition<?> operation = Core.getInstance().getOperationById(
					this.operationsCompartments.get(this.tool));

			ParamSpec[] specs = new ParamSpec[]{ 
					new ParamSpec("File", File.class,this.file,null),
					new ParamSpec("workspace", WorkspaceAIB.class,workspace,null)
			};

			ProgressHandler handler = null;
			Core.getInstance().executeOperation(operation, handler, specs);
		}



		else {

			UrlPopUp url = new UrlPopUp();

			this.link=url.getLink();

			OperationDefinition<?> operation = Core.getInstance().getOperationById(
					this.operationsCompartments.get(this.tool));

			ParamSpec[] specs = new ParamSpec[]{ 
					new ParamSpec("Link", String.class,this.link,null),
					new ParamSpec("workspace", WorkspaceAIB.class,workspace,null)
			};

			ProgressHandler handler = null;
			Core.getInstance().executeOperation(operation, handler, specs);
		}

	}

}





