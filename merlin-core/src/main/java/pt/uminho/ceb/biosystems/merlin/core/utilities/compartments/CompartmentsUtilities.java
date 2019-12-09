package pt.uminho.ceb.biosystems.merlin.core.utilities.compartments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.utilities.compartments.CompartmentsEnumerators.STAIN;

public class CompartmentsUtilities {

	public static String assignCorrectMembraneDefaultCompartment(boolean isEukaryote) {
		
		if(isEukaryote)
			return "PLAS";
		
		return "CYTMEM";
	}
		
		/**
		 * @param abbreviation
		 * @return
		 */
		public static String parseAbbreviation(String abbreviation) {

			if(abbreviation.equals("permem"))			
				return "peroxisomal_membrane";

			if(abbreviation.equals("ermem"))
				return "ER_membrane";

			if(abbreviation.equals("vacmem"))			
				return "vacuolar_membrane";

			if(abbreviation.equals("golmem"))
				return "golgi_membrane";

			if(abbreviation.equals("nucmem"))
				return "nuclear_membrane";

			if(abbreviation.equals("unkn"))
				return "unknown";

			if(abbreviation.equals("cytmem"))
				return "cytoplasmic_membrane";

			if(abbreviation.equals("perip"))
				return "periplasmic";

			if(abbreviation.equals("outme") || abbreviation.equals("outmem"))
				return "outer_membrane";
			
			if(abbreviation.equals("innmem"))
				return "inner_membrane";

			if(abbreviation.equals("cytop") || abbreviation.equals("cyto"))
				return "cytoplasmic";

			if(abbreviation.equals("pla") || abbreviation.equals("plas") || abbreviation.equals("plasmem"))
				return "plasma_membrane";

			else if(abbreviation.equals("gol") || abbreviation.equals("golg"))
				return "golgi_apparatus";

			else if(abbreviation.equals("vac") || abbreviation.equals("vacu"))
				return "vacuole";

			else if(abbreviation.equals("mit") || abbreviation.equals("mito"))
				return "mitochondria";

			else if(abbreviation.equals("ves") || abbreviation.equals("vesi"))
				return "vesicles_of_secretory_system";

			else if(abbreviation.equals("end") || abbreviation.equals("E.R.")) 
				return "endoplasmic_reticulum";

			else if(abbreviation.equals("---"))
				return "other";

			else if(abbreviation.equals("nuc") || abbreviation.equals("nucl"))
				return "nuclear";

			else if(abbreviation.equals("cyt") || abbreviation.equals("cyto"))
				return "cytosol";

			else if(abbreviation.equals("csk") || abbreviation.equals("cysk"))
				return "cytoskeleton";

			else if(abbreviation.equals("exc") || abbreviation.equals("extr"))
				return "extracellular";

			else if(abbreviation.equals("pox") || abbreviation.equals("pero"))
				return "peroxisome";

			else if(abbreviation.equals("mitmem"))			
				return "mitochondrion membrane";

			else if(abbreviation.equals("plast"))
				return "plastid";

			else if(abbreviation.equals("chlo"))
				return "chloroplast";

			else if(abbreviation.equals("chlomem"))
				return "chloroplast membrane";

			else if(abbreviation.equals("lyso"))
				return "lysosome";

			else if(abbreviation.equals("cellw"))
				return "cellwall";

			else if(abbreviation.contains("_")) {

				String compartment = "";
				String[] dual_compartment = abbreviation.split("_");

				for(int i = 0; i<dual_compartment.length;i++) {

					if(i!=0)
						compartment=compartment.concat("_");

					compartment=compartment.concat(CompartmentsUtilities.parseAbbreviation(dual_compartment[i]));
				}
				return compartment;
			}
			else {

				return abbreviation;

			}
		}

		/**
		 * @param compartmentID
		 * @return
		 */
		public static String getAbbreviation(String compartmentID) {

			if(compartmentID.equals("peroxisome membrane") || compartmentID.equals("peroxisomal_membrane"))			
				return "permem";

			if(compartmentID.equals("mitochondrion membrane"))			
				return "mitmem";

			if(compartmentID.equals("endoplasmic reticulum membrane"))
				return "ermem";

			if(compartmentID.equals("vacuole membrane") || compartmentID.equals("vacuolar_membrane"))			
				return "vacmem";

			if(compartmentID.equals("golgi apparatus membrane") || compartmentID.equals("golgi_membrane"))
				return "golmem";

			if(compartmentID.equals("nucleus membrane") || compartmentID.equals("nuclear_membrane") )
				return "nucmem";

			if(compartmentID.equals("plastid"))
				return "plast";

			if(compartmentID.equals("unknown"))
				return "unkn";

			if(compartmentID.equals("plasma membrane"))
				return "plasmem";

			if(compartmentID.equals("periplasmic"))
				return "perip";

			if(compartmentID.equals("outer_membrane") || compartmentID.equals("outer membrane"))
				return "outmem";
			
			if(compartmentID.equals("inner_membrane") || compartmentID.equals("inner membrane"))
				return "innmem";

			if(compartmentID.equals("cytoplasm") || compartmentID.equals("cytoplasmic"))
				return "cytop";

			else if(compartmentID.equals("golgi apparatus") || compartmentID.equals("golgi"))
				return "golg";

			else if(compartmentID.equals("vacuole"))
				return "vac";

			else if(compartmentID.equals("mitochondrion") || compartmentID.equals("mitochondrial"))
				return "mito";

			else if(compartmentID.equals("vesicles_of_secretory_system"))
				return "ves";
			
			else if(compartmentID.equals("vesicles_of_secretory_system membrane"))
				return "vesmem";

			else if(compartmentID.equals("endoplasmic reticulum")) 
				return "E.R.";

			else if(compartmentID.equals("other"))
				return "---";

			else if(compartmentID.equals("nucleus"))
				return "nuc";

			else if(compartmentID.equals("cytoskeleton"))
				return "cysk";

			else if(compartmentID.equals("extracellular") || compartmentID.equals("secreted"))
				return "extr";

			else if(compartmentID.equals("peroxisome"))
				return "pox";

			else if(compartmentID.equals("chloroplast"))
				return "chlo";

			else if(compartmentID.equals("chloroplast membrane"))
				return "chlomem";
			
			else if(compartmentID.equals("lysosome membrane"))
				return "lysomem";

			else if(compartmentID.equals("lysosome"))
				return "lyso";

			else if(compartmentID.equals("cellwall"))
				return "cellw";

			else {

				return compartmentID;
			}
		}


		/**
		 * Get the outside of the membranes
		 * 
		 * @param stain
		 * @return
		 * @throws Exception
		 */
		public static Set<String> getOutsideMembranes(String compartmentID, STAIN stain) throws Exception {

			Set<String> list = new HashSet<String>();

			if(compartmentID.equalsIgnoreCase("outmem")) {

				list.add("EXTR");
				list.add("PERIP");
				return list;
			}

			list.add("cytop");
			list.add(CompartmentsUtilities.getOutsideMembrane(compartmentID, stain));

			return list;
		}

		/**
		 * @param compartment
		 * @return
		 */
		public static String getOutsideMembrane(String compartment, STAIN stain) {
			
			List<String> compartments = new ArrayList<String>(
				    Arrays.asList("unkn", "pla", "plas", "outme", "plasmem", "outmem", "cellw", "cellwall", "extr"));
			
			if(compartments.contains(compartment))
				return ("EXTR");
			
			else if(compartment.equalsIgnoreCase("cytmem")) {
				
				if (stain.equals(STAIN.gram_negative))
					return ("PERIP");
				else
					return ("EXTR");
			}

			return ("CYTOP");

		}
		
		/**
		 * @param compartmentID
		 * @return
		 */
		public static String getInsideMembrane(String compartmentID, STAIN stain) {

			//pode acontecer entrar aqui um extr??
			
			//interior compartment default ou escolhido pelo user??
			
			//innmem??
			
			//ves
			
			if(compartmentID.equalsIgnoreCase("extr") || compartmentID.equalsIgnoreCase("outmem") || compartmentID.equalsIgnoreCase("outme")) {//perip???  //outmem??
				
				if (stain.equals(STAIN.gram_negative))
					return ("PERIP");
				else
					return ("CYTOP");
			}
			
			if(compartmentID.equalsIgnoreCase("unkn"))//perip???  //outmem??
				return ("CYTOP"); 
			
			else if(compartmentID.equalsIgnoreCase("cytop") || compartmentID.equalsIgnoreCase("cytmem") || 
					compartmentID.equalsIgnoreCase("cytomem") || compartmentID.equalsIgnoreCase("cytopmem") ||
					compartmentID.equalsIgnoreCase("cyto"))
				return ("CYTOP");

			else if(compartmentID.equalsIgnoreCase("pla") || compartmentID.equalsIgnoreCase("plas") || 
					compartmentID.equalsIgnoreCase("plasmem"))
				return ("CYTOP");		//return (interiorCompartment.toUpperCase());
			
			else if(compartmentID.equalsIgnoreCase("cellw") || compartmentID.equalsIgnoreCase("cellwall"))
				return ("CYTOP");		//return (interiorCompartment.toUpperCase());

			else if(compartmentID.equalsIgnoreCase("golmem") || compartmentID.equalsIgnoreCase("golgmem"))
				return ("GOLG");

			else if(compartmentID.equalsIgnoreCase("vacmem") || compartmentID.equalsIgnoreCase("vacumem"))
				return ("VAC");

			else if(compartmentID.equalsIgnoreCase("mitmem") || compartmentID.equalsIgnoreCase("mitomem"))
				return ("MITO");

			else if(compartmentID.equalsIgnoreCase("endeme") || compartmentID.equalsIgnoreCase("ermem"))
				return ("E.R.");

			else if(compartmentID.equalsIgnoreCase("nucmem") || compartmentID.equalsIgnoreCase("nuclmem"))
				return ("NUC");

			else if(compartmentID.equalsIgnoreCase("csk") || compartmentID.equalsIgnoreCase("cysk") ||
					compartmentID.equalsIgnoreCase("cskmem") || compartmentID.equalsIgnoreCase("cyskmem") )
				return ("CYSK"); 

			else if(compartmentID.equalsIgnoreCase("poxmem") || compartmentID.equalsIgnoreCase("permem"))
				return ("POX");
			
			else if(compartmentID.equalsIgnoreCase("lysomem"))
				return ("LYSO");
			
			else if(compartmentID.equalsIgnoreCase("vesmem"))
				return ("VES");
			
			else if(compartmentID.equalsIgnoreCase("chlomem"))
				return ("CHLO");

			return compartmentID;

		}

		/**
		 * Get the outside of the compartments (Stain in optional)
		 * 
		 * @param stain
		 * @return
		 * @throws Exception
		 */
		public static String getOutside(STAIN stain, String compartmentID) throws Exception {

			if(compartmentID.equalsIgnoreCase("extr"))
				return "";

			if(compartmentID.equalsIgnoreCase("unkn"))
				return "unknown";

			if(compartmentID.equalsIgnoreCase("cytmem"))
				if(stain.equals(STAIN.gram_negative))
					return "perip";
				else
					return "extr";

			if(compartmentID.equalsIgnoreCase("perip"))
				return "outmem";

			if(compartmentID.equalsIgnoreCase("cellw") && compartmentID.equalsIgnoreCase("cellwall"))
				return "extr";

			if(compartmentID.equalsIgnoreCase("outme") || compartmentID.equalsIgnoreCase("outmem"))
				return "extr";

			if(compartmentID.equalsIgnoreCase("cytop"))
				return "cytmem";

			if(compartmentID.equalsIgnoreCase("pla") || compartmentID.equalsIgnoreCase("plas") || compartmentID.equalsIgnoreCase("plasmem"))
				return "extr";

			else if(compartmentID.equalsIgnoreCase("gol") || compartmentID.equalsIgnoreCase("golg"))
				return "golmem";

			else if(compartmentID.equalsIgnoreCase("vac") || compartmentID.equalsIgnoreCase("vacu"))
				return "vacmem";

			else if(compartmentID.equalsIgnoreCase("mit") || compartmentID.equalsIgnoreCase("mito"))
				return "mitmem";

			else if(compartmentID.equalsIgnoreCase("end") || compartmentID.equalsIgnoreCase("E.R."))
				return "ermem";

			else if(compartmentID.equalsIgnoreCase("nuc") || compartmentID.equalsIgnoreCase("nucl"))
				return "nucmem";

			else if(compartmentID.equalsIgnoreCase("cyt") || compartmentID.equalsIgnoreCase("cyto"))
				return "plasmem";

			else if(compartmentID.equalsIgnoreCase("csk") || compartmentID.equalsIgnoreCase("cysk"))
				return "plasmem";

			else if(compartmentID.equalsIgnoreCase("pox") || compartmentID.equalsIgnoreCase("pero"))
				return "permem";

			else
				return "cytop";

		}
}
