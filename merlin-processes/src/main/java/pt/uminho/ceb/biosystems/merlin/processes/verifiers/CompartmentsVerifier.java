package pt.uminho.ceb.biosystems.merlin.processes.verifiers;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelCompartmentServices;

public class CompartmentsVerifier {

	/**
	 * @param compartment
	 * @throws Exception 
	 */
	public static CompartmentContainer checkExternalCompartment(String compartment, String databaseName) throws Exception {

		boolean isCompartimentalisedModel = ProjectServices.isCompartmentalisedModel(databaseName);

		if(compartment.equalsIgnoreCase("auto")) {

			List<CompartmentContainer> containers = ModelCompartmentServices.getCompartmentsInfo(databaseName);

			for(CompartmentContainer entry : containers){

				if(entry != null) {

					String compartmentName = entry.getName().toLowerCase();

					if(((compartmentName.equalsIgnoreCase("extracellular") || (compartmentName.contains("extra"))) && isCompartimentalisedModel) || 
							(compartmentName.equalsIgnoreCase("outside") && !isCompartimentalisedModel)) {
						return entry;
					}
				}
			}
		}
		return ModelCompartmentServices.getCompartmentByName(databaseName, compartment);
	}


	/**
	 * @param compartmentID
	 * @throws Exception 
	 */
	public static CompartmentContainer checkInteriorCompartment(String compartment, String databaseName) throws Exception {

		boolean isCompartimentalisedModel = ProjectServices.isCompartmentalisedModel(databaseName);

		if(compartment.equalsIgnoreCase("auto")) {

			List<CompartmentContainer> containers = ModelCompartmentServices.getCompartmentsInfo(databaseName);

			for(CompartmentContainer entry : containers){

				if(entry != null) {

					String compartmentName = entry.getName().toLowerCase();

					if(((compartmentName.equalsIgnoreCase("cytoplasmic") || (compartmentName.equalsIgnoreCase("cytosol"))) && isCompartimentalisedModel) || 
							(compartmentName.equalsIgnoreCase("inside") && !isCompartimentalisedModel)) {
						return entry;
					}
				}
			}
		}
		return ModelCompartmentServices.getCompartmentByName(databaseName, compartment);
	}

	/**
	 * @param compartment
	 * @param databaseName
	 * @return
	 * @throws Exception
	 */
	public static CompartmentContainer checkMembraneCompartment(String compartment, String databaseName, boolean isEukatyote) throws Exception {
		boolean isCompartimentalisedModel = ProjectServices.isCompartmentalisedModel(databaseName);

		if(compartment.equalsIgnoreCase("auto")) {

			List<CompartmentContainer> containers = ModelCompartmentServices.getCompartmentsInfo(databaseName);

			for(CompartmentContainer entry : containers){

				if(entry != null) {

					String compartmentName = entry.getName().toLowerCase();

					if (compartmentName.matches("[Pp]lasma.*[Mm]embrane") && isEukatyote && isCompartimentalisedModel)
						return entry;
					if (compartmentName.matches("[Cc]ytoplasm.*[Mm]embrane") && !isEukatyote && isCompartimentalisedModel)
						return entry;
				}
			}
		}
		
		return ModelCompartmentServices.getCompartmentByName(databaseName, compartment);
	}
}
