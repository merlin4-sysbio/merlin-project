package pt.uminho.ceb.biosystems.merlin.processes.model.compartments.services;

import java.sql.SQLException;
import java.util.List;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelCompartmentServices;

public class CompartmentsIntegrationServices {


	/**
	 * @param interiorCompartment
	 * @param statement
	 * @return
	 * @throws Exception 
	 */
	public static String autoSetInteriorCompartment(String databaseName) throws Exception{

		String interiorCompartment = null;
		
		try {

//			Map<String, String> res = AnnotationCompartmentsServices.getNameAndAbbreviation(databaseName);
//			
//			for (String x : res.keySet()) {
//				String value = res.get(x);
//				if (value.equalsIgnoreCase("cyto")) 
//					interiorCompartment = "cyto";	
//				
//				else if (value.equalsIgnoreCase("cytop")) 
//					interiorCompartment = "cytop";
//			}
			
			List<CompartmentContainer> res = ModelCompartmentServices.getCompartmentsInfo(databaseName);

			for (CompartmentContainer x : res) {
				String abb = x.getAbbreviation();
				if (abb.equalsIgnoreCase("cyto")) 
					interiorCompartment = "cyto";	
				
				else if (abb.equalsIgnoreCase("cytop")) 
					interiorCompartment = "cytop";
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		return interiorCompartment;
	}


	/**
	 * @param compartment
	 * @throws SQLException
	 */
	public static int getCompartmentID(String databaseName, String compartment) {

		Integer compartmentID = null;

		try {

			String abbreviation;

			if(compartment.length()>3) {

				abbreviation=compartment.substring(0,3).toUpperCase();
			}
			else {

				abbreviation=compartment.toUpperCase().concat("_");

				while(abbreviation.length()<4)
					abbreviation=abbreviation.concat("_");
			}

			abbreviation=abbreviation.toUpperCase();
			
			compartmentID = ModelCompartmentServices.getCompartmentIdByNameAndAbbreviation(databaseName, compartment, abbreviation);
			
			if(compartmentID == null)
				compartmentID = ModelCompartmentServices.insertNameAndAbbreviation(databaseName, compartment, abbreviation);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return compartmentID;

	}
	
}
