package pt.uminho.ceb.biosystems.merlin.utilities;

public class SbmlUtilities {


	/**
	 * @param sbmlMetaboliteID
	 * @return
	 */
	public static String processSBMLGeneIdentifier(String sbmlGeneID) {
		
		String processedGeneID = sbmlGeneID;
		
		if(processedGeneID.matches("^[Gg]_.+"))
			processedGeneID = processedGeneID.replaceAll("^[Gg]_","");
		
		return processedGeneID;
	}
	
	/**
	 * @param sbmlMetaboliteID
	 * @return
	 */
	public static String processSBMLMetaboliteIdentifier(String sbmlMetaboliteID) {
		
		String processedMetID = sbmlMetaboliteID;
		
		while(processedMetID.matches("^[Mm]_.+"))
			processedMetID = processedMetID.replaceAll("^[Mm]_","");
		
		if(processedMetID.contains("_")){

			String[] metIDSplited = processedMetID.split("_");

			for(String splitedId : metIDSplited){

				if(splitedId.matches("cpd\\d{5}"))
					processedMetID = splitedId;
			}

			while(processedMetID.matches(".+[^_]_\\w{1}\\d*$"))
				processedMetID = processedMetID.substring(0, processedMetID.lastIndexOf("_"));
			
			//TODO
			// Quando se tratar do importer da BIGG tem de se mudar isto !!!
			if(processedMetID.contains("__"))
				processedMetID = processedMetID.split("__")[0];

		}
		return processedMetID;
	}
	
	/**
	 * @param sbmlReactionID
	 * @return
	 */
	public static String processSBMLReactionIdentifier(String sbmlReactionID) {
		
		String processedReactionID = sbmlReactionID;
		
		while(processedReactionID.matches("^[Rr]_.+"))
			processedReactionID = processedReactionID.replaceAll("^[Rr]_","");
		
		if(processedReactionID.contains("_")){

			String[] reactionIDSplited = processedReactionID.split("_");

			for(String splitedId : reactionIDSplited){

					if(splitedId.matches("rxn\\d{5}"))
						processedReactionID = splitedId;
				
					else if(splitedId.matches("R\\d{5}"))
						processedReactionID = splitedId;
					
			}

			while(processedReactionID.matches(".+[^_]_\\w{1}\\d*$"))
				processedReactionID = processedReactionID.substring(0, processedReactionID.lastIndexOf("_"));	

		}
		
		return processedReactionID;
	}

}
