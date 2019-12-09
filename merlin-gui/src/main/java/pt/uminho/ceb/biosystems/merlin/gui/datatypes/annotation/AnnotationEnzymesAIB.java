package pt.uminho.ceb.biosystems.merlin.gui.datatypes.annotation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import es.uvigo.ei.aibench.core.Core;
import es.uvigo.ei.aibench.core.clipboard.ClipboardItem;
import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.ei.aibench.core.datatypes.annotation.Structure;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceGenericDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.AnnotationEnzymes;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.enzymes.AnnotationEnzymesRowInfo;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceTableAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.interfaces.IEntityAIB;
import pt.uminho.ceb.biosystems.merlin.processes.annotation.AnnotationEnzymesProcesses;
import pt.uminho.ceb.biosystems.merlin.services.annotation.AnnotationEnzymesServices;

/**
 * @author ODias
 *
 */
@Datatype(structure= Structure.SIMPLE, namingMethod="getName",removable=true,removeMethod ="remove")
public class AnnotationEnzymesAIB extends AnnotationEnzymes implements IEntityAIB {

	private String workspaceName;

	/**
	 * @param table
	 * @param name
	 */
	public AnnotationEnzymesAIB(WorkspaceTableAIB table, String name) {

		super(table, name);
		this.workspaceName = table.getWorkspaceName();

		try {
			this.configureHomologySearchType();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @throws Exception 
	 * 
	 */
	public void configureHomologySearchType() throws Exception {

		if(this.getHomologySearchType()==null)
			this.setHomologySearchType(AnnotationEnzymesServices.getHomologySearchType(workspaceName));
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public String[][] getStats(){

		if(super.getStats() == null) {

			try {
				List<Integer> result = AnnotationEnzymesServices.getStats(workspaceName);
				this.setStats(AnnotationEnzymesProcesses.getStats(result));
			} 
			catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}
		}

		return this.stats;
	}

	/**
	 * @param database
	 * @param useCache
	 * @return
	 * @throws Exception 
	 */
	public WorkspaceGenericDataTable getAllGenes(String database, boolean useCache) throws Exception {

		this.configureHomologySearchType();

		if(!useCache || this.getEnzymeFinalScore().size() == 0 || this.mainTableData == null) {

			try {
				AnnotationEnzymes annotationEnzymes = AnnotationEnzymesServices.getMainTableData(workspaceName, this);
				WorkspaceGenericDataTable mainTableData = AnnotationEnzymesProcesses.getMainTableData(annotationEnzymes, database);
				super.setMainTableData(mainTableData);
			} catch (Exception e) {
				Workbench.getInstance().error(e);
				e.printStackTrace();
			}


		}

		return super.getMainTableData();
	}


	/**
	 * @param id
	 * @return
	 */
	public String getGeneLocus(Integer id) {

		return this.getInitialLocus().get(id);
	}


	/* (non-Javadoc)
	 * @see pt.uminho.ceb.biosystems.merlin.core.datatypes.annotation.EnzymesAnnotation#getRowInfo(int)
	 */
	public WorkspaceDataTable[] getRowInfo(int enzymesAnnotationIdentifier, boolean refresh) throws Exception {

		if(super.getRowInfo()==null || refresh) {

			AnnotationEnzymesRowInfo enzymesAnnotationRowInfo = AnnotationEnzymesServices.getRowInfo(workspaceName, enzymesAnnotationIdentifier);
			super.setRowInfo(AnnotationEnzymesProcesses.getRowInfo(enzymesAnnotationRowInfo));
		}
		return super.getRowInfo();			
	}


	/**
	 * @param id
	 * @return
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public WorkspaceDataTable getECBlastSelectionPane(String query//String locus
			) throws Exception {

		List<String[]> ecData = AnnotationEnzymesServices.getECData(workspaceName, query);
		String title = "EC number(s) Selection";
		return AnnotationEnzymesProcesses.getSelectionPane(title, this.getEnzymeFrequencyScore(), this.getEnzymeTaxonomyScore(), this.getEnzymeFinalScore(), ecData);
	}


	/**
	 * @param id
	 * @return
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public WorkspaceDataTable getProductSelectionPane(String query) throws Exception {

		List<String[]> productData = AnnotationEnzymesServices.getProductData(workspaceName, query);
		String title = "product selection";
		return AnnotationEnzymesProcesses.getSelectionPane(title, this.getProductFrequencyScore(), this.getProductTaxonomyScore(), this.getProductFinalScore(), productData);
	}

	/**
	 * @param selectedItem
	 * @param row
	 * @return
	 */
	public String getECPercentage(String selectedItem, int row) {

		return AnnotationEnzymesProcesses.getECPercentage(selectedItem, row, this);
	}

	/**
	 * @param selectedItem
	 * @param row
	 * @return
	 */
	public String getProductPercentage(String selectedItem, int row) {

		return AnnotationEnzymesProcesses.getProductPercentage(selectedItem, row, this);
	}

	/**
	 * @return
	 * @throws Exception 
	 */
	public Map<String, List<String>> getUniprotECnumbers() throws Exception {

		//if(this.getUniProtECnumbers()==null)		
			this.setUniProtECnumbers(AnnotationEnzymesServices.getUniprotECnumbers(workspaceName));

		return this.getUniProtECnumbers();
	}

	/**
	 * Get previously committed scorer parameter values
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public AnnotationEnzymes getCommitedScorerData(String blastDatabase) throws Exception{

		return AnnotationEnzymesServices.getCommitedScorerData(workspaceName, this, blastDatabase);
	}

	/**
	 * Get previously committed Homology Data
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public AnnotationEnzymes getCommittedHomologyData() throws Exception {

		return AnnotationEnzymesServices.getCommittedHomologyData(workspaceName, this);
	}


	/**
	 * 
	 * @param blastDatabase
	 * @return
	 * @throws Exception 
	 * @throws IOException
	 */
	public void commitToDatabase(String blastDatabase) throws Exception { 

		AnnotationEnzymesServices.commitToDatabase(workspaceName, blastDatabase, this);

	}


	/**
	 * Get rows for genes with InterPro entries.
	 * 
	 * @return
	 */
	public List<Integer> getInterProRows() {


		try {
			if(super.getInterProRows()==null) {

				List<String> interProGenes = AnnotationEnzymesServices.getInterProGenes(this.workspaceName);

				Map<String, Integer> queryKeys = AnnotationEnzymesServices.getQueryKeys(this.workspaceName);

				this.setInterProRows(AnnotationEnzymesProcesses.getInterProRows(interProGenes, queryKeys, this));
			}
		} 
		catch (Exception e) {
			Workbench.getInstance().error(e);
			e.printStackTrace();
		}

		return super.getInterProRows();
	}


	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#		()
	 */
	public String getName() {

		return "enzymes";
	}


	/**
	 * Remove the Enzymes annotation instance from AIBench 
	 */
	public void remove() {

		List<ClipboardItem> items = Core.getInstance().getClipboard().getItemsByClass(AnnotationEnzymesAIB.class);
		ClipboardItem torem = null;
		for(ClipboardItem item : items){
			if(item.getUserData().equals(this)){
				torem = item;
				break;
			}
		}
		Core.getInstance().getClipboard().removeClipboardItem(torem);
		System.gc();
	}

}
