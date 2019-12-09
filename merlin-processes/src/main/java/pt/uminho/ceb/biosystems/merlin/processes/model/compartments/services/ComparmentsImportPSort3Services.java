/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.processes.model.compartments.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.compartments.AnnotationCompartmentsGenes;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.compartments.AnnotationCompartmentsPSort3;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.ICompartmentResult;
import pt.uminho.ceb.biosystems.merlin.processes.model.compartments.CompartmentsInitializationProcesses;
import pt.uminho.ceb.biosystems.merlin.processes.model.compartments.interfaces.ICompartmentsServices;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;

/**
 * @author ODias
 *
 */
public class ComparmentsImportPSort3Services implements ICompartmentsServices{

	private static int normalization=10;
	private AtomicBoolean cancel;
	private boolean typePlant;
	private String databaseName;


//	/**
//	 * @param conn
//	 * @param results
//	 * @param project_id
//	 */
//	public ReadPSort3(Connection conn, Map<String, CompartmentResult> results, int project_id) {
//		this.cancel = new AtomicBoolean(false);
//		this.loadCompartments = new LoadCompartments(conn);
//		this.results = results;
//		this.project_id = project_id;
//	}

	/**
	 * @param databaseName 
	 * 
	 */
	public ComparmentsImportPSort3Services(String databaseName) {

		this.cancel = new AtomicBoolean(false);
		this.databaseName = databaseName;

	}

	/**
	 * @param outFile
	 * @return
	 * @throws Exception 
	 */
	public Map<String, ICompartmentResult> addGeneInformation(File outFile) throws Exception {

		Map<String, ICompartmentResult> compartmentLists = this.readPSortFile(outFile);

		Map<String, ICompartmentResult> compartmentResults = compartmentLists;

		return compartmentResults;

	}


	/**
	 * @param outFile
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private Map<String, ICompartmentResult> readPSortFile(File outFile) throws NumberFormatException, Exception {

		Map<String, ICompartmentResult> compartmentLists = new HashMap<>();
		
		Map<String, Integer> sequencesIds = ModelGenesServices.getGeneIDsByQuery(this.databaseName);

		BufferedReader in = new BufferedReader(new FileReader(outFile));
		String str;
		int cytoplasmicMembrane_Score_index = -1;
		int seqID_index = -1;
		int periplasmic_Score_index = -1;
		int outerMembrane_Score_index = -1;
		int extracellular_Score_index= -1;
		int cytoplasmic_Score_index= -1;
		int final_Localization_index=-1;
		int cellwall_score_index = -1;

		boolean firstLine=true;

		while ((str = in.readLine()) != null && !this.cancel.get()) {

			if(!str.isEmpty()){

				String[] line = str.split("\t");

				if(firstLine) {

					firstLine=false;
					for(int i=0;i<line.length;i++) {

						if(line[i].trim().equalsIgnoreCase("SeqID")){seqID_index=i;}
						if(line[i].trim().replace(" ","").equalsIgnoreCase("CytoplasmicMembrane_Score")){cytoplasmicMembrane_Score_index=i;}
						if(line[i].trim().equalsIgnoreCase("periplasmic_Score")){periplasmic_Score_index=i;}
						if(line[i].trim().equalsIgnoreCase("OuterMembrane_Score")){outerMembrane_Score_index=i;}
						if(line[i].trim().equalsIgnoreCase("Extracellular_Score")){extracellular_Score_index=i;}
						if(line[i].trim().equalsIgnoreCase("Cytoplasmic_Score")){cytoplasmic_Score_index=i;}
						if(line[i].trim().equalsIgnoreCase("Final_Localization")){final_Localization_index=i;}
						if(line[i].trim().equalsIgnoreCase("Cellwall_Score")){cellwall_score_index=i;}
					}

					if(final_Localization_index<0) {

						in.close();
						return null;
					}
				}
				else {
					
					
					//String locus_tag = line[seqID_index].split(" ")[0].split("\\|")[3];
					String locus_tag = line[seqID_index].split(" ")[0];
					
					AnnotationCompartmentsPSort3 pSort3Result = new AnnotationCompartmentsPSort3(sequencesIds.get(locus_tag));
					
					boolean unknown=true;

					Double.valueOf(line[cytoplasmicMembrane_Score_index]);
					if(Double.valueOf(line[cytoplasmicMembrane_Score_index])>3)
					{
						unknown=false;
					}

					if(periplasmic_Score_index>0 && Double.valueOf(line[periplasmic_Score_index])>3) {

						unknown=false;
					}

					if(outerMembrane_Score_index>0 && Double.valueOf(line[outerMembrane_Score_index])>3) {

						unknown=false;
					}

					if(cellwall_score_index>0 && Double.valueOf(line[cellwall_score_index])>3) {

						unknown=false;
					}

					if(Double.valueOf(line[extracellular_Score_index])>3) {

						unknown=false;
					}
					if(Double.valueOf(line[cytoplasmic_Score_index])>3)
					{
						unknown=false;
					}

					double score;
					if(line[final_Localization_index].trim().equalsIgnoreCase("Unknown")&&unknown) {

						score = 10;
						pSort3Result.addCompartment("unkn", score);	
						pSort3Result.addCompartmentInfo("unkn", "Unknown");
					}
					else {

						boolean maxFound=false, returnFinalLocalisation=false;
						score = Double.valueOf(line[cytoplasmicMembrane_Score_index]);
						pSort3Result.addCompartment("cytmem", score);
						pSort3Result.addCompartmentInfo("cytmem", "CytoplasmicMembrane");
						if(score==10){maxFound=true;}

						score = Double.valueOf(line[extracellular_Score_index]);
						pSort3Result.addCompartment("extr", score);
						pSort3Result.addCompartmentInfo("extr", "extracellular");
						if(score==10)
						{
							if(maxFound){returnFinalLocalisation=true;}
							else{maxFound=true;}
						}

						if(periplasmic_Score_index>0) {

							score = Double.valueOf(line[periplasmic_Score_index]);
							pSort3Result.addCompartment("perip", score);
							pSort3Result.addCompartmentInfo("perip", "Periplasmic");
							if(score==10) {

								if(maxFound){returnFinalLocalisation=true;}
								else{maxFound=true;}
							}
						}

						if(outerMembrane_Score_index>0) {

							score = Double.valueOf(line[outerMembrane_Score_index]);
							pSort3Result.addCompartment("outme", score);
							pSort3Result.addCompartmentInfo("outme", "OuterMembrane");
							if(score==10) {

								if(maxFound){returnFinalLocalisation=true;}
								else{maxFound=true;}
							}
						}

						if(cellwall_score_index>0) {

							score = Double.valueOf(line[cellwall_score_index]);
							pSort3Result.addCompartment("cellw", score);
							pSort3Result.addCompartmentInfo("cellw", "Cellwall");
							if(score==10) {

								if(maxFound){returnFinalLocalisation=true;}
								else{maxFound=true;}
							}
						}

						score = Double.valueOf(line[cytoplasmic_Score_index]);
						pSort3Result.addCompartment("cytop", score);
						pSort3Result.addCompartmentInfo("cytop", "Cytoplasmic");
						if(score==10)
						{
							if(maxFound){returnFinalLocalisation=true;}
							else{maxFound=true;}
						}

						if(returnFinalLocalisation) {

							pSort3Result = new AnnotationCompartmentsPSort3(sequencesIds.get(locus_tag));
							String out;
							if(line[final_Localization_index].trim().equalsIgnoreCase("Cytoplasmic")){out = "cytop";}
							else if(line[final_Localization_index].trim().equalsIgnoreCase("CytoplasmicMembrane")){out = "cytmem";}
							else if(line[final_Localization_index].trim().equalsIgnoreCase("Periplasmic")){out = "perip";}
							else if(line[final_Localization_index].trim().equalsIgnoreCase("OuterMembrane")){out = "outme";}
							else if(line[final_Localization_index].trim().equalsIgnoreCase("Cellwall")){out = "cellw";}
							else{out = "extr";}
							
							pSort3Result.addCompartment(out,10);
						}
					}
					
					compartmentLists.put(locus_tag, pSort3Result);
				}
			}
		}
		
		in.close();
		return compartmentLists;
	}

	@Override
	public boolean getCompartments(String string) {
		return true;
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

	@Override
	public boolean isEukaryote() {

		return false;
	}

	@Override
	public Map<String, ICompartmentResult> addGeneInformation(String link) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadCompartmentsInformation(Map<String, ICompartmentResult> results)
			throws Exception {
		
		for(ICompartmentResult pSORT3Result : results.values())
			CompartmentsInitializationProcesses.loadData(this.databaseName, pSORT3Result);
	}

	@Override
	public Map<Integer, AnnotationCompartmentsGenes> getBestCompartmentsByGene(double threshold)
			throws Exception {
		
		return CompartmentsInitializationProcesses.getBestCompartmenForGene(this.databaseName, threshold, ComparmentsImportPSort3Services.normalization);
	}

	@Override
	public void setPlant(boolean typePlant) {
		
		this.setTypePlant(typePlant);
	}

	/**
	 * @return the typePlant
	 */
	public boolean isTypePlant() {
		return typePlant;
	}

	/**
	 * @param typePlant the typePlant to set
	 */
	public void setTypePlant(boolean typePlant) {
		this.typePlant = typePlant;
	}
}
