/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.awt.Rectangle;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.JViewport;

import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot.TaxonomyContainer;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.NcbiAPI;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceEntityAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationCompartmentsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationEnzymesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation.AnnotationTransportersAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelGenesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelMetabolitesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelProteinsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelReactionsAIB;
import pt.uminho.ceb.biosystems.merlin.gui.jpanels.CustomGUI;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;

/**
 * @author Oscar
 *
 */
public class MerlinUtils {


	/**
	 * @param projectName
	 */
	public static void updateAllViews(String projectName) {

		MerlinUtils.updateEntity(projectName);
		MerlinUtils.updateProteinView(projectName);
		MerlinUtils.updateGeneView(projectName);
		MerlinUtils.updateAnnotationViews(projectName);
		MerlinUtils.updateMetabolicViews(projectName);
		MerlinUtils.updateProjectView(projectName);
	}



	/**
	 * @param projectName
	 */
	public static void updateMetabolicViews(String projectName) {

		MerlinUtils.updateReactionsView(projectName);
		MerlinUtils.updateProteinView(projectName);
		MerlinUtils.updateReagentProductsView(projectName);
	}

	/**
	 * @param projectName
	 */
	public static void updateEnzymesAnnotationView(String projectName) {

		AIBenchUtils.updateView(projectName, AnnotationEnzymesAIB.class);
	}


	/**
	 * @param projectName
	 */
	public static void updateCompartmentsAnnotationView(String projectName) {

		AIBenchUtils.updateView(projectName, AnnotationCompartmentsAIB.class);
	}

	/**
	 * @param projectName
	 */
	public static void updateAnnotationViews(String projectName) {

		AIBenchUtils.updateView(projectName, AnnotationEnzymesAIB.class);
		AIBenchUtils.updateView(projectName, AnnotationTransportersAIB.class);
		AIBenchUtils.updateView(projectName, AnnotationCompartmentsAIB.class);
	}

	/**
	 * @param projectName
	 */
	public static void updateTransportersAnnotationView(String projectName) {

		AIBenchUtils.updateView(projectName, AnnotationTransportersAIB.class);
	}

	/**
	 * @param projectName
	 */
	public static void updateProjectView(String projectName) {

		AIBenchUtils.updateView(projectName, WorkspaceAIB.class);
	}
	/**
	 * @param projectName
	 */
	public static void updateGeneView(String projectName) {

		AIBenchUtils.updateView(projectName, ModelGenesAIB.class);
	}

	/**
	 * @param projectName
	 */
	public static void updateProteinView(String projectName) {

		AIBenchUtils.updateView(projectName, ModelProteinsAIB.class);
	}


//	/**
//	 * @param projectName
//	 */
//	public static void updateEnzymeView(String projectName) {
//
//		AIBenchUtils.updateView(projectName, EnzymesModelGUI.class);
//	}

	/**
	 * @param projectName
	 */
	public static void updateReactionsView(String projectName) {

		AIBenchUtils.updateView(projectName, ModelReactionsAIB.class);
	}

	/**
	 * @param projectName
	 */
	public static void updateReagentProductsView(String projectName) {

		AIBenchUtils.updateView(projectName, ModelMetabolitesAIB.class);
	}


	/**
	 * @param projectName
	 */
	public static void updateEntity(String projectName) {

		AIBenchUtils.updateView(projectName, WorkspaceEntityAIB.class);
	}

	/**
	 * @param remoteExceptionTrials
	 * @return
	 * @throws Exception
	 */
	public static int getOrganismTaxonomyCount(long taxonomy_id) throws Exception {

		TaxonomyContainer result = NcbiAPI.getTaxonomyFromNCBI(taxonomy_id, 3);
		return result.getTaxonomy().size()+1;
	}

	/**
	 * 
	 * Scroll to center of table
	 * 
	 * 
	 * @param table
	 * @param rowIndex
	 * @param vColIndex
	 */
	public static void scrollToCenter(JTable table, int rowIndex, int vColIndex) {
		if (!(table.getParent() instanceof JViewport)) {
			return;
		}
		JViewport viewport = (JViewport) table.getParent();
		Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
		Rectangle viewRect = viewport.getViewRect();
		rect.setLocation(rect.x - viewRect.x, rect.y - viewRect.y);

		int centerX = (viewRect.width - rect.width) / 2;
		int centerY = (viewRect.height - rect.height) / 2;
		if (rect.x < centerX) {
			centerX = -centerX;
		}
		if (rect.y < centerY) {
			centerY = -centerY;
		}
		rect.translate(centerX, centerY);
		viewport.scrollRectToVisible(rect);
	}


	/**
	 * @param map
	 * @param msqlmt
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, String> parseGeneLocusTag(String databaseName, Map<String, String> map) throws Exception {

		Set<String> res = new HashSet<>();

		List<GeneContainer> containers = ModelGenesServices.getAllGeneData(databaseName);
		
		for(GeneContainer container : containers)
			res.add(container.getLocusTag());

		for(String id : map.keySet()) {

			String locus = map.get(id);

			if(!res.contains(locus))
				for(String newLocus : res)
					if(newLocus.replace("_", "").equalsIgnoreCase(locus.replace("_", ""))) {

						map.put(id, newLocus);
					}
		}
		return map;
	}

	/**
	 * @param unAnnotated
	 * @param initialSize
	 * @param location
	 * @return
	 */
	public static boolean unAnnotatedTransporters(float unAnnotated, float initialSize, String location){

		float percentage = (unAnnotated/initialSize)*100;
		int i =CustomGUI.stopQuestion("TCDB Unannoted transporters",
				percentage+"% of the TCDB genes identified as homolgues of the genome being studied are currently not annotated. Do you wish to continue?",
				new String[]{"Continue", "Abort", "Info"});

		String slash = "/", newSlash = "/";

		if(System.getProperty("os.name").toLowerCase().contains("windows"))
			newSlash = "\\";

		if(i<2) {

			switch (i)
			{
			case 0:return true;
			case 1: return false;
			default:return true;
			}
		}
		else {

			Workbench.getInstance().info("This genome has homology with "+Math.round(initialSize)+" TCDB genes. However, "+Math.round(unAnnotated)+" of the "+Math.round(initialSize)+" genes\n" +
					"are not annotated in merlin's current database.\n" +
					"If you continue, merlin will generate transport reactions with the current database.\n" +
					"However, if you want to generate transport reactions for the whole genome, you\n" +
					"may annotate the missing TCDB entries, filling in the following columns:" +
					"\n\t-direction\n\t-metabolite\n\t-reversibility\n\t-reacting_metabolites\n\t-equation\n" +
					"in the file located at :\n"+location.replace("plugins_bin\\merlin_core","").replace("plugins_bin/merlin_core","").replace("/../..","").replace(slash,newSlash)+
					"\naccording to the example.\n" +
					"Afterwards go to Transporters> New Transporters Loading and select the file.\n" +
					"Please send your file to support@merlin-sysbio.org prior to submission, to check your data.");
			return unAnnotatedTransporters( unAnnotated, initialSize, location);
		}
	}
}
