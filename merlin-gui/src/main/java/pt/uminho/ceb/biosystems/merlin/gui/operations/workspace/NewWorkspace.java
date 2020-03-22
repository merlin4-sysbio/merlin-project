package pt.uminho.ceb.biosystems.merlin.gui.operations.workspace;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.NcbiAPI;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceEntity;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceTables;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceDatabaseAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTableAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationCompartmentsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationEnzymesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.WorkspaceAnnotationEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelGenesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelMetabolitesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelPathwaysAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelProteinsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelReactionsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.WorkspaceModelEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.regulation.WorkspaceRegulationEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.validation.WorkspaceValidationEntitiesAIB;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

/**
 * This is the class that creates a new project for the AIbench interface.
 * 
 *
 */
@Operation(name="workspace",description="create a new merlin workspace")
public class NewWorkspace {

	private String databaseName = null;
	private long taxonomyID;


	/**
	 * 
	 * @param db Name of the database to associate the project to.
	 */
	@Port(direction=Direction.INPUT,validateMethod ="validateDataBase", name="Database", order=6)
	public void setDb(String databaseName) {
		this.databaseName = databaseName;
	}


	@Port(direction=Direction.INPUT, name="TaxonomyID",order=11, validateMethod = "validateTaxonomy" )
	public void setTaxonomyID(long taxonomyID) {

		this.taxonomyID = taxonomyID;
	}

	/**
	 * @param taxonomyID
	 */
	public void validateTaxonomy(long taxonomyID) {

	}

	/**
	 * @param databaseName
	 */
	public void validateDataBase(String databaseName) {

		this.databaseName = databaseName;

	}

	/**
	 * 
	 * Method that returns the new project.
	 */
	/**
	 * @return
	 */
	@Port(direction=Direction.OUTPUT, name="workspace", order=15)
	public WorkspaceAIB getDataBase() {

		WorkspaceDatabaseAIB database = new WorkspaceDatabaseAIB(this.databaseName);

		try {

			WorkspaceTables existingTables = new WorkspaceTables();
			WorkspaceEntity gene = null;
			WorkspaceEntity protein = null;
			WorkspaceEntity enzyme = null;
			WorkspaceEntity reaction = null;
			WorkspaceEntity path = null;
			WorkspaceEntity metabolites = null;
			WorkspaceEntity annotationEnzymes = null;
			WorkspaceEntity annotationTransporters = null;
			WorkspaceEntity annotationCompartments = null;

//			for(int i=0;i<tables.length;i++) {

//				String[] meta = dbAccess.getMeta(tables[i], connection);
//				WorkspaceTableAIB table = new WorkspaceTableAIB(tables[i], this.databaseName);

//				if(table.getSize()!=null )
//					existingTables.addToList(table);

//				if(tables[i].equalsIgnoreCase("model_gene")) {

					gene = new ModelGenesAIB(new WorkspaceTableAIB("model_gene", this.databaseName), "genes");
//				}
//				else if(tables[i].equalsIgnoreCase("model_protein")) {

					protein = new ModelProteinsAIB(new WorkspaceTableAIB("model_protein", this.databaseName), "proteins");
//				}
//				else if(tables[i].equalsIgnoreCase("model_stoichiometry")) {

					metabolites = new ModelMetabolitesAIB(new WorkspaceTableAIB("model_stoichiometry", this.databaseName), "metabolites");
//				}
//				else if(tables[i].equalsIgnoreCase("model_reaction")) {

					reaction = new  ModelReactionsAIB(new WorkspaceTableAIB("model_reaction", this.databaseName), "reactions");
//				}
//				else if(tables[i].equalsIgnoreCase("model_pathway")) {

					path = new  ModelPathwaysAIB(new WorkspaceTableAIB("model_pathway", this.databaseName), "pathways");
//				}
//				else if(tables[i].equalsIgnoreCase("enzymes_annotation_geneHomology")) {

					annotationEnzymes = new AnnotationEnzymesAIB(new WorkspaceTableAIB("enzymes_annotation_geneHomology", this.databaseName), "enzymes");
//				}
				//					else if(tables[i].toLowerCase().equalsIgnoreCase("model_genes")) {
				//
				//						annotationTransporters = new AnnotationTransportersAIB(table, "transporters");
				//					}
//				else if(tables[i].toLowerCase().equalsIgnoreCase("compartments_annotation_reports")) {

					annotationCompartments = new AnnotationCompartmentsAIB(new WorkspaceTableAIB("compartments_annotation_reports", this.databaseName), "compartments");
//				}
//			}

			database.setTables(existingTables);

			ArrayList<WorkspaceEntity> entitiesList = new ArrayList<WorkspaceEntity>();
			
			

			if(gene!=null) {

				entitiesList.add(gene);
			}

			if(protein!=null) {

				entitiesList.add(protein);
			}

			if(metabolites!=null) {

				//				ArrayList<WorkspaceEntity> subs = new ArrayList<WorkspaceEntity>();
				//		if(enzirg!=null) subs.add(enzirg);
				//		if(ti!=null) subs.add(ti);

				//				if(compoundsReactions!=null)
				//					subs.add(compoundsReactions);
				//				if(compound!=null)
				//					subs.add(compound);

				//				if(subs!=null)
				//					repro.setSubenties(subs);

				entitiesList.add(metabolites);
			}

			if(reaction!=null)
				entitiesList.add(reaction);

			if(path!=null)
				entitiesList.add(path);

			WorkspaceModelEntitiesAIB entities = new WorkspaceModelEntitiesAIB();
			entities.setEntities(entitiesList);
			database.setEntities(entities);

			entitiesList = new ArrayList<>();
			if(annotationEnzymes!=null)
				entitiesList.add(annotationEnzymes);	

			//			if(annotationTransporters!=null)
			//				entitiesList.add(annotationTransporters);	

			if(annotationCompartments!=null)
				entitiesList.add(annotationCompartments);	

			WorkspaceAnnotationEntitiesAIB annotations = new WorkspaceAnnotationEntitiesAIB();
			annotations.setEntities(entitiesList);
			database.setEntities(annotations);

			entitiesList = new ArrayList<>();

			WorkspaceValidationEntitiesAIB validation = new WorkspaceValidationEntitiesAIB();
			
			validation.setEntities(entitiesList);

			database.setEntities(validation);
			
			entitiesList = new ArrayList<>();
			
			WorkspaceRegulationEntitiesAIB regulation = new WorkspaceRegulationEntitiesAIB();

			regulation.setEntities(entitiesList);
			
			database.setEntities(regulation);

			WorkspaceAIB project = new WorkspaceAIB(database);

//			ProjectAPI.isTransporterLoaded(connection);

			List<WorkspaceEntity> allentities = new ArrayList<WorkspaceEntity>();
			allentities.add(gene);
			allentities.add(protein);
			allentities.add(enzyme);
			allentities.add(reaction);
			allentities.add(path);
			allentities.add(metabolites);
			allentities.add(annotationEnzymes);
			allentities.add(annotationTransporters);
			allentities.add(annotationCompartments);

			for(WorkspaceEntity ent : allentities)
				if(ent != null)
					ent.setWorkspace(project);

			if(this.taxonomyID>0) {

				project.setTaxonomyID(this.taxonomyID);
				String[] orgData = ProjectServices.getOrganismData(this.databaseName, this.taxonomyID);

				if (orgData == null){
					orgData =  NcbiAPI.ncbiNewTaxID(this.taxonomyID);
					ProjectServices.updateOrganismData(this.databaseName, this.taxonomyID, orgData);
				}

				project.setOrganismName(orgData[0]);
				project.setOrganismLineage(orgData[1]);

			}

			project.setGenomeLocusTag(ModelGenesServices.getGenesLocusTagBySequenceId(this.databaseName));

			FileUtils.writeIdsConverterConfFile(project.getName());
			
			return project;
		}
		catch (IllegalArgumentException e) {

			Workbench.getInstance().error("an error occurred while retrieving the taxonomy information.\nplease verify the taxonomy identifier and your internet connection.");
			Workbench.getInstance().error(e.getMessage());
		} 
		catch (SQLException e) {

			e.printStackTrace();
			Workbench.getInstance().error("an error occurred while creating project, please try again.");
		} catch (Exception e) {

			e.printStackTrace();
			Workbench.getInstance().error("an error occurred while retrieving locus_tags from genomic.gbff.\nplease validate genomic.gbff and/or verify its existence.");
		}
		return null;

	}
	
		
}
