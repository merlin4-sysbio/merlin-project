package pt.uminho.ceb.biosystems.merlin.gui.operations.workspace;

import java.io.File;
import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.Enumerators.TypeOfExport;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

/**
 * @author davidelagoa
 *
 */
@Operation(description="Export files.")
public class ExportFiles {

	private String extension, databaseName;
	private WorkspaceAIB project = null;
	private File directory;
	private Long taxID;

	@Port(direction=Direction.INPUT, name="Select Workspace", validateMethod = "checkProject", order=1)
	public void setProject(WorkspaceAIB project) {

	}

	/**
	 * @param project
	 */
	public void checkProject(WorkspaceAIB project) {

		if(project==null) {

			throw new IllegalArgumentException("Please select a workspace.");
		}
		else {
			this.databaseName = project.getName();
			this.taxID = project.getTaxonomyID();
			this.project = project;

		} 
	}

	@Port(direction=Direction.INPUT,validateMethod="",name="chose file", description="",order=2)
	public void setExtensions(TypeOfExport type) {

		this.extension = type.extension();

	}

	/**
	 * @param directory
	 * @throws IOException 
	 */
	@Port(direction=Direction.INPUT, name="Directory:",description="folder",validateMethod="checkDirectory",order=3)
	public void selectDirectory(File directory){

		String path;
		String name = "";

		try {
			if(extension.equals(TypeOfExport.ALL_FILES.extension())){

				path = FileUtils.getWorkspaceFolderPath(databaseName);

				name = databaseName + extension;

				if(FileUtils.existsPath(path)){

					FileUtils.createZipFile(path, this.directory.getAbsolutePath() + "/" + name, 1);


				}
				else
					throw new IllegalArgumentException("Error while exporting. The current workspace has no files set yet.");
			}
			else{

				path = FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxID) + extension;

				name = databaseName +"_" + taxID + "_" + this.extension;

				if(FileUtils.existsPath(path)){

					FileUtils.copyFile(path, this.directory.getAbsolutePath() + "/" + name);

				}
				else
					throw new IllegalArgumentException("Error while exporting. The chosen file doesn't exist for the current workspace.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error while exporting.");
		}

		Workbench.getInstance().info("File successfully exported with name: " + name);
	}

	/**
	 * @param directory
	 */
	public void checkDirectory(File directory) {

		if(directory == null || directory.toString().isEmpty()) {

			throw new IllegalArgumentException("Please select a directory!");
		}
		else {

			if(directory.isDirectory())
				this.directory = directory;
			else
				this.directory = directory.getParentFile();	
		}
	}

}
