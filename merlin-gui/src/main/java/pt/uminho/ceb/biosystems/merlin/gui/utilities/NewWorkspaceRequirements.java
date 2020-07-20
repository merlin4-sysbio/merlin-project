package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.Compartments;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelCompartmentServices;

public class NewWorkspaceRequirements {
	
	/**
	 * @param databaseName
	 * @throws Exception
	 */
	public static void injectRequiredDataToNewWorkspace(String databaseName) throws Exception {
		
		CompartmentContainer container = ModelCompartmentServices.getCompartmentByAbbreviation(databaseName, Compartments.inside.getAbbreviation());
		
		if(container == null)
			ModelCompartmentServices.insertNameAndAbbreviation(databaseName, Compartments.inside.toString(), Compartments.inside.getAbbreviation());
		
		container = ModelCompartmentServices.getCompartmentByAbbreviation(databaseName, Compartments.outside.getAbbreviation());
		
		if(container == null)
			ModelCompartmentServices.insertNameAndAbbreviation(databaseName, Compartments.outside.toString(), Compartments.outside.getAbbreviation());
		
	}

}
