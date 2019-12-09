package pt.uminho.ceb.biosystems.merlin.processes.model.compartments.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.compartments.AnnotationCompartmentsGenes;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.compartments.AnnotationCompartmentsLocTree;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.ICompartmentResult;
import pt.uminho.ceb.biosystems.merlin.core.utilities.compartments.CompartmentsUtilities;
import pt.uminho.ceb.biosystems.merlin.core.utilities.compartments.RetrieveRemoteResults;
import pt.uminho.ceb.biosystems.merlin.processes.model.compartments.CompartmentsInitializationProcesses;
import pt.uminho.ceb.biosystems.merlin.processes.model.compartments.interfaces.ICompartmentsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;

/**
 * @author Oscar Dias
 *
 */
public class ComparmentsImportLocTreeServices implements ICompartmentsServices {

	private static int normalization=100;
	private AtomicBoolean cancel;
	private boolean typePlant;
	private String databaseName;

	/**
	 * @param organismType
	 */
	public ComparmentsImportLocTreeServices(String databaseName) {

		this.cancel = new AtomicBoolean(false);
		this.databaseName = databaseName;
	}

	/**
	 * @return the cancel
	 */
	public AtomicBoolean isCancel() {
		return cancel;
	}

	/**
	 * @param cancel the cancel to set
	 */
	public void setCancel(AtomicBoolean cancel) {
		this.cancel = cancel;
	}


	public boolean isEukaryote() {

		return false;
	}

	public void setPlant(boolean typePlant){
		this.typePlant = typePlant;
	}
	
	public static String createAbbreviation(String localization) {
		
		String abbreviation = CompartmentsUtilities.getAbbreviation(localization);
		
		if(abbreviation.equals(localization) && localization.length() > 3) {
			
			abbreviation = localization.substring(0, 2);
			
			if(localization.matches(".*[Mm]embrane.*"))
				abbreviation.concat("mem");
		}
		
		return abbreviation;
	}

	/**
	 * Method to parse a BufferedReader containing information about compartments.
	 * 
	 * @param in
	 * @return Map<String, CompartmentResult>
	 * @throws IOException
	 */
	public Map<String, ICompartmentResult> readLocTreeFile(BufferedReader in) throws Exception {

		Map<String, Integer> sequenceIds = ModelGenesServices.getGeneIDsByQuery(this.databaseName);

		Map<String, ICompartmentResult> compartmentLists = new HashMap<>();

		String inputLine;

		int protID = -1, scr = -1, loc = -1, geneOnto = -1, acc = -1, annType=-1;

		String proteinID = null, localization = null, 
				geneOntologyTerms = null, accuracy = null, annotationType = null;
		String score = null;

		int count = 0;

		boolean header = true, flag = false;

		AnnotationCompartmentsLocTree locTR = null;

		boolean oldParser = false;

		if((inputLine = in.readLine()) !=null){
			Document doc = Jsoup.parse(inputLine);
			String str = doc.body().text();

			if(str.contains("#")){
				oldParser = true;
				protID = 0;
				scr = 1;
				loc = 2;
				geneOnto = 3;
			}
		}

		while ((inputLine = in.readLine()) != null && !this.cancel.get() && !inputLine.contains("Mouse click")) {

			String str = inputLine;

			if(!oldParser){
				Document doc = Jsoup.parse(inputLine);
				str = doc.body().text();
			}

			if(oldParser && !str.contains("#")){

				String[] locT = str.split("\\t");

				String localizationString = locT[loc];

				if(!typePlant) {

					if (localizationString.equals("chloroplast") || 
							localizationString.equals("plastid"))
						localizationString = "mitochondrion";

					if (localizationString.equals("chloroplast membrane") || 
							localizationString.equals("plastid membrane"))
						localizationString = "mitochondrion membrane";
				}

				if(compartmentLists.containsKey(locT[protID])) {

					locTR = (AnnotationCompartmentsLocTree) compartmentLists.get(locT[protID]);
					String abbreviation = createAbbreviation(localizationString);
					locTR.addCompartment(abbreviation, Double.parseDouble(locT[scr]));
					locTR.addCompartmentInfo(abbreviation, localization);
				}
				else {

					locTR = new AnnotationCompartmentsLocTree(sequenceIds.get(locT[protID]), Double.parseDouble(locT[scr]), localizationString, locT[geneOnto]);


					if(acc>0 && annType>0) {

						locTR.setAnnotationType(locT[annType]);
						locTR.setExpectedAccuracy(locT[acc]);
					}
				}

				compartmentLists.put(locT[protID], locTR);

			}

			if (!flag && !oldParser){							//reads the order of the information

				if (str.contains("Details")){

					if (!header)
						flag = true;

					count = 0;
					header = false;
				}

				else if (str.contains("Protein ID"))
					protID = count;

				else if(str.contains("Score"))
					scr = count;

				else if(str.contains("Expected Accuracy"))
					acc = count;

				else if(str.contains("Localization Class"))
					loc = count;

				else if(str.contains("Gene Ontology Terms"))
					geneOnto = count;

				else if(str.contains("Annotation Type"))
					annType = count;

				count++;

				//				System.out.println(protID);
				//				System.out.println(scr);
				//				System.out.println(acc);
				//				System.out.println(loc);
				//				System.out.println(geneOnto);
				//				System.out.println(annType);
			}

			if (flag){

				//				System.out.println();
				//				System.out.println("NOVA");				
				//				System.out.println(str);
				//				System.out.println(!str.contains("Details"));
				//				System.out.println(!str.equals(""));
				//				System.out.println(count);

				if(!str.contains("Details") && str != null && !str.equals("")){

					if(count == protID)
						proteinID = str;

					if(count == scr) 
						score = str;
					//						score = Double.parseDouble(str);

					else if(count == acc) 
						accuracy = str;
					else if(count == loc){
						localization = str;

						if(!typePlant) {

							if (localization.equals("chloroplast") || 
									localization.equals("plastid"))
								localization = "mitochondrion";

							if (localization.equals("chloroplast membrane") || 
									localization.equals("plastid membrane"))
								localization = "mitochondrion membrane";
						}
					}

					else if(count == geneOnto) 
						geneOntologyTerms = str;
					else if(count == annType){
						if(str.contains(";")){
							geneOntologyTerms.concat(str);
							count --;
						}
						else{
							annotationType = str;
						}
					}

					count ++;

					if (count == 7){

						if(!proteinID.isEmpty() && !score.isEmpty() && !localization.isEmpty() 
								&& !annotationType.isEmpty() && !geneOntologyTerms.isEmpty() && !accuracy.isEmpty()){

							String abbreviation = createAbbreviation(localization);

							try {

								if(compartmentLists.containsKey(proteinID)) {
									locTR = (AnnotationCompartmentsLocTree) compartmentLists.get(proteinID);
									locTR.addCompartment(abbreviation, Double.parseDouble(score));
									locTR.addCompartmentInfo(abbreviation, localization);
								}
								else {

									locTR = new AnnotationCompartmentsLocTree(sequenceIds.get(proteinID), Double.parseDouble(score), abbreviation, geneOntologyTerms);
									locTR.setAnnotationType(annotationType);
									locTR.setExpectedAccuracy(accuracy);
									locTR.addCompartmentInfo(abbreviation, localization);
								}

								compartmentLists.put(proteinID, locTR);
							} catch (Exception e) {

							}
						}


						locTR = null;
						count = 1;
						proteinID = null;
						localization = null;
						geneOntologyTerms = null;
						accuracy = null;
						annotationType = null;

					}
				}
			}
		}

		// ###############################################	old parser


		//			if(str.startsWith("#")) {
		//
		//				if(str.toLowerCase().contains("Expected Accuracy".toLowerCase()) && str.toLowerCase().contains("Annotation Type".toLowerCase())) {
		//
		//					expectedAccuracy = 2;
		//					localization = 3;
		//					geneOntologyTerms = 4;
		//					annotationType = 5; 
		//				}
		//			} 
		//			else {
		//
		//				String[] locT = str.split("\\t");
		//
		//				LocTreeResult locTR = null;
		//
		//				String localizationString = locT[localization];
		//
		//				if(!typePlant) {
		//
		//					if (localizationString.equals("chloroplast") || 
		//							localizationString.equals("plastid"))
		//						localizationString = "mitochondrion";
		//
		//					if (localizationString.equals("chloroplast membrane") || 
		//							localizationString.equals("plastid membrane"))
		//						localizationString = "mitochondrion membrane";
		//				}
		//
		//				if(compartmentLists.containsKey(locT[proteinID])) {
		//
		//					locTR = (LocTreeResult) compartmentLists.get(locT[proteinID]);
		//					locTR.addCompartment(localizationString, new Double(locT[score]));
		//				}
		//				else {
		//
		//					locTR = new LocTreeResult(locT[proteinID], new Double(locT[score]), localizationString, locT[geneOntologyTerms]);
		//					
		//					
		//					if(expectedAccuracy>0 && annotationType>0) {
		//
		//						locTR.setAnnotationType(locT[annotationType]);
		//						locTR.setExpectedAccuracy(locT[expectedAccuracy]);
		//					}
		//				}
		//
		//				compartmentLists.put(locT[proteinID], locTR);
		//			}
		//		}

		in.close();

		return compartmentLists;
	}

	/* (non-Javadoc)
	 * @see pt.uminho.sysbio.common.transporters.core.compartments.CompartmentsInterface#addGeneInformation(java.io.File)
	 */
	public Map<String, ICompartmentResult> addGeneInformation(File outFile) throws Exception {

		return null;
	}

	/**
	 * 
	 * 
	 * @param compartmentLists
	 * @return
	 * @throws Exception

				private Map<String, CompartmentResult> getLocusTags(Map<String, CompartmentResult> compartmentLists) throws Exception {

					Map<String, CompartmentResult> compartmentResults = new HashMap<>();

					if (!this.cancel.get()) {

						Map<String, String> idLocus = NcbiAPI.getNCBILocusTags(compartmentLists.keySet(), 500);

						for (String id : idLocus.keySet()) {

							CompartmentResult locTreeResult = compartmentLists.get(id);
							locTreeResult.setGeneID(idLocus.get(id));
							compartmentResults.put(idLocus.get(id), locTreeResult);
						}
					}
					return compartmentResults;
				}
	 */

	/**
	 * Get best compartments by gene.
	 * 
	 * @param threshold
	 * @param statement
	 * @param projectID
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<Integer, AnnotationCompartmentsGenes> getBestCompartmentsByGene(double threshold) throws Exception {

		return CompartmentsInitializationProcesses.getBestCompartmenForGene(this.databaseName, threshold, ComparmentsImportLocTreeServices.normalization);
	}

	/**
	 *  Load compartments from results.
	 * 
	 * @param results
	 * @param projectID
	 * @param statement
	 * @throws Exception
	 */
	public void loadCompartmentsInformation(Map<String, ICompartmentResult> results) throws Exception{
		for(ICompartmentResult locTreeResult : results.values())
			CompartmentsInitializationProcesses.loadData(this.databaseName, locTreeResult);

	}

	/* (non-Javadoc)
	 * @see pt.uminho.sysbio.common.transporters.core.compartments.CompartmentsInterface#getCompartments(java.lang.String)
	 */
	public boolean getCompartments(String string) {

		return true;
	}

	@Override
	public Map<String, ICompartmentResult> addGeneInformation(String link) throws Exception {

		BufferedReader data = RetrieveRemoteResults.retrieveDataFromURL(link);

		Map<String, ICompartmentResult> compartmentLists = this.readLocTreeFile(data); 

		return compartmentLists;
	}
}
