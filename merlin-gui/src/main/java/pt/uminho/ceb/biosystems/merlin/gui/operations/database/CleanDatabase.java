package pt.uminho.ceb.biosystems.merlin.gui.operations.database;

import java.io.IOException;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.ParamSpec;
import es.uvigo.ei.aibench.core.operation.OperationDefinition;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SchemaType;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.MerlinUtils;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.NewWorkspaceRequirements;
import pt.uminho.ceb.biosystems.merlin.services.DatabaseServices;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.annotation.AnnotationEnzymesServices;
import pt.uminho.ceb.biosystems.merlin.utilities.DatabaseFilesPaths;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

@Operation(description="clean workspace.", name="clean workspace.")
public class CleanDatabase {

	private WorkspaceAIB project;
	private boolean reloadData = false;		//DEBUG ME!!!!
	private boolean genesData;

	/**
	 * @param project
	 */
	@Port(name="workspace:",description="select workspace",direction=Direction.INPUT,order=1)
	public void setProject(WorkspaceAIB project){
		this.project = project;
	}

	/**
	 * @param schema
	 * @throws Exception 
	 * @throws IOException 
	 */
	@Port(name="select information",description="be carefull when selecting information to delete! ", defaultValue = "ignore", direction = Direction.INPUT, order=2)
	public void cleanSchema(SchemaType schema) throws IOException, Exception {

		boolean error = false;

		switch (schema)
		{
		case ALL_INFORMATION:
		{

			try {			
				DatabaseServices.dropConnection(this.project.getName());

				String path = DatabaseFilesPaths.getEnzymesAnnotationPath();
				DatabaseServices.executeDropTable(this.project.getName(), FileUtils.readLines(path));
				
				path = DatabaseFilesPaths.getCompartmentsAnnotationPath();
				DatabaseServices.executeDropTable(this.project.getName(), FileUtils.readLines(path));

				path = DatabaseFilesPaths.getModelPath(this.genesData);
				DatabaseServices.executeDropTable(this.project.getName(), FileUtils.readLines(path));

				path = DatabaseFilesPaths.getInterproAnnotationPath();
				DatabaseServices.executeDropTable(this.project.getName(), FileUtils.readLines(path));

				DatabaseServices.getDatabaseService(this.project.getName());
				
				if(this.reloadData)
					reloadMetabolicData();
				else
					NewWorkspaceRequirements.injectRequiredDataToNewWorkspace(this.project.getName());
				
				this.project.setInitialiseHomologyData(true);
				break;
			}
			catch (Exception e) {
				error = true;
				Workbench.getInstance().error(e);
				e.printStackTrace();
			} 	
		}
		case MODEL: {

			{
				try {
					int count = AnnotationEnzymesServices.countEntriesInGeneHomology(this.project.getName());

					if(count > 0 && this.genesData) {
						Workbench.getInstance().error("enzymes annotation contains data! please clean the enzymes annotation before cleaning the model or select 'clean all'!");
					}
					else {
						String path = DatabaseFilesPaths.getModelPath(this.genesData);

						DatabaseServices.dropConnection(this.project.getName());

						DatabaseServices.executeDropTable(this.project.getName(), FileUtils.readLines(path));

						DatabaseServices.getDatabaseService(this.project.getName());

						if(this.reloadData)
							reloadMetabolicData();

					}
				}
				catch (Exception e) {
					error = true;
					Workbench.getInstance().error(e);
					e.printStackTrace();
				} 				

				break;							

			}
		}
		case ENZYMES_ANNOTATION:
		{
			try {
				String path = DatabaseFilesPaths.getEnzymesAnnotationPath();

				DatabaseServices.dropConnection(this.project.getName());

				DatabaseServices.executeDropTable(this.project.getName(), FileUtils.readLines(path));

				DatabaseServices.getDatabaseService(this.project.getName());
			}
			catch (Exception e) {
				error = true;
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}

			this.project.setInitialiseHomologyData(true);
			break;
		}
		case COMPARTMENTS_ANNOTATION: {

			try {
				if(ProjectServices.isCompartmentalisedModel(this.project.getName())) {			
					Workbench.getInstance().warn("compartments already integrated in model. to remove compartments from model, all model information should be removed and the database re-loaded!");
				}
				else {
					String path = DatabaseFilesPaths.getCompartmentsAnnotationPath();

					DatabaseServices.dropConnection(this.project.getName());

					DatabaseServices.executeDropTable(this.project.getName(), FileUtils.readLines(path));

					DatabaseServices.getDatabaseService(this.project.getName());
				}
			} 
			catch (Exception e) {
				error = true;
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}

			break;
		}
		case INTERPRO_ANNOTATION:
			try {
				String path = DatabaseFilesPaths.getInterproAnnotationPath();

				DatabaseServices.dropConnection(this.project.getName());

				DatabaseServices.executeDropTable(this.project.getName(), FileUtils.readLines(path));

				DatabaseServices.getDatabaseService(this.project.getName());
			}
			catch (Exception e) {
				error = true;
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}

			break;
		default:
			break;
		}

		if(schema.equals(SchemaType.IGNORE)) {

			Workbench.getInstance().info("database cleaning ignored");
		}
		else {

			MerlinUtils.updateAllViews(project.getName());

			if(error)
				Workbench.getInstance().error("there was an error when trying to clean "+this.project.getName()+" workspace!!");
			else {
				
				if(this.reloadData)
					Workbench.getInstance().info("workspace "+ this.project.getName() +" successfuly cleaned, starting loading metabolic data.");
				else
					Workbench.getInstance().info("workspace "+ this.project.getName() +" successfuly cleaned.");
				
			}
		}

	}

	private void reloadMetabolicData() {

		if(this.reloadData) {
			ParamSpec[] paramsSpec = new ParamSpec[]{
					new ParamSpec("project", WorkspaceAIB.class, this.project, null)
			};

			for (@SuppressWarnings("rawtypes") OperationDefinition def : Core.getInstance().getOperations()){
				if (def.getID().equals("operations.LoaderWorkspaceDatabase.ID")){

					Workbench.getInstance().executeOperation(def, paramsSpec);
				}
			}
		}

	}

	/**
	 * @param project			this flag is throwing errors
	 */
	@Port(name="reload metabolic data",description="reload metabolic data",validateMethod="validateReload",defaultValue="false",direction=Direction.INPUT,order=3,advanced=true)
	public void setLoadMetabolicData(boolean reloadData) {
		this.reloadData = reloadData;
	}

	public void validateReload(boolean reloadData) {
		this.reloadData = reloadData;
	}

	/**
	 * @param project
	 */
	@Port(name="delete gene sequences",description="delete gene and sequences information, only used for 'model' or 'all' information",validateMethod="validateGenes",defaultValue="false",direction=Direction.INPUT,order=4,advanced=true)
	public void setGenesData(boolean genesData) {
		this.genesData = genesData;
	}

	public void validateGenes(boolean genesData) {
		this.genesData = genesData;
	}
}
