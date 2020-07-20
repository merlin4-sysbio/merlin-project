package pt.uminho.ceb.biosystems.merlin.utilities;

import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

/**
 * @author davidelagoa
 *
 */
public class DatabaseFilesPaths {

	private static final String MANAGEMENT_FILES_DIRECTORY = FileUtils.getDatabaseManagementFolderPath();

	
	public static String getModelPath(boolean genesData){

		if(genesData)
			return MANAGEMENT_FILES_DIRECTORY +"model.txt";
		else
			return MANAGEMENT_FILES_DIRECTORY +"model_noGenesSequences.txt";
	}

	public static String getEnzymesAnnotationPath(){

		return MANAGEMENT_FILES_DIRECTORY +"enzymes_annotation.txt";
	}

	public static String getCompartmentsAnnotationPath(){

		return MANAGEMENT_FILES_DIRECTORY +"compartments_annotation.txt";
	}

	public static String getInterproAnnotationPath(){

		return MANAGEMENT_FILES_DIRECTORY +"interpro.txt";
	}

}