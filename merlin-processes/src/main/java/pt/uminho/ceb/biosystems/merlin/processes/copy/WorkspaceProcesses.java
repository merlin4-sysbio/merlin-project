package pt.uminho.ceb.biosystems.merlin.processes.copy;

import java.io.File;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.CreateGenomeFile;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.Enumerators;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.Enumerators.FileExtensions;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities.Enumerators.GenBankFiles;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelSequenceServices;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

public class WorkspaceProcesses {

	
	public static String getWorkspaceTaxonomyFolderPath(String name, long taxonomyID) {
		
		return FileUtils.getWorkspaceTaxonomyFolderPath(name, taxonomyID);
	}
	
	

	
	/**
	 * verify if a database directory exists, if not creats it
	 * @param databaseName
	 * @return
	 */
	public static boolean checkFastaExistence(String databaseName, Long taxID, FileExtensions fileExtension) {
		
		File newPath = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxID) + fileExtension.getName());
		
		if (newPath.exists())
			return true;
		
		return false;
	}
	
	/**
	 * @param databaseName
	 * @param taxID
	 * @return
	 */
	public static boolean isFnaFiles(String databaseName, Long taxID) {
		
		return WorkspaceProcesses.checkFastaExistence(databaseName, taxID, FileExtensions.CDS_FROM_GENOMIC);
	}
	

	/**
	 * @param databaseName
	 * @param taxID
	 * @return
	 */
	public static boolean isFaaFiles(String databaseName, Long taxID) {
		
		return WorkspaceProcesses.checkFastaExistence(databaseName, taxID, FileExtensions.PROTEIN_FAA);
	}
	
	
	/**
	 * @param databaseName
	 * @param taxID
	 * @return
	 * @throws Exception 
	 */
	public static void createFaaFile(String databaseName, Long taxID) throws Exception {
		
		String filePath = FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, 
				taxID).concat(Enumerators.FileExtensions.PROTEIN_FAA.getName());
		
		if(! isFaaFiles(databaseName, taxID))
			CreateGenomeFile.buildFastaFile(filePath, ModelSequenceServices.getGenomeFromDatabase(databaseName, SequenceType.PROTEIN));
		
	}
	
	/**
	 * @param databaseName
	 * @param taxID
	 * @return
	 */
	public static File checkGenBankFile(String databaseName, Long taxID) {
		
		File newPath = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxID) + GenBankFiles.GENOMIC_GBFF.extension());

		if (newPath.exists())
			return newPath;
		
		newPath = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxID) + FileExtensions.CUSTOM_GENBANK_FILE.getName().concat(".gbff"));
		
		if (newPath.exists())
			return newPath;
		
		newPath = new File(FileUtils.getWorkspaceTaxonomyFolderPath(databaseName, taxID) + FileExtensions.CUSTOM_GENBANK_FILE.getName().concat(".gpff"));
		
		if (newPath.exists())
			return newPath;
		
		return null;
	}
	
}
