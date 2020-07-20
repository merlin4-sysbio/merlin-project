package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.modelSeed;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;

public class ModelSeedPathwaysDB {
	
	private static final String HTTP_FILE_URL = "https://raw.githubusercontent.com/ModelSEED/ModelSEEDDatabase/master/Biochemistry/Pathways/KEGG.pathways";

	private Map<String, ArrayList<String>> keggPathwaysDB;
	//	private List<String> keggReactionsList;
	private Map<String, List<String[]>> pathways_hierarchy;
	private Map<String, List<String>> super_pathways;
	private Map<String, List<String>> reactionsPathways;


	public ModelSeedPathwaysDB(){

		this.keggPathwaysDB = new HashMap<String, ArrayList<String>>();
		//		this.keggReactionsList = new ArrayList<>();
		this.reactionsPathways = new HashMap<String, List<String>>();
		this.pathways_hierarchy = new HashMap<String, List<String[]>>();
		this.super_pathways = new HashMap<String,List<String>>();

		readPathwaysDBFiles();
	}


	/**
	 * reader for KEGG pathways database '.tsv' file 
	 * @return
	 */
	public void readPathwaysDBFiles() {

		String keggFilePath = FileUtils.getConfFolderPath().concat("KEGG_pathways.tsv");
		//		String modelSeedFilePath = FileUtils.getHomeFolderPath().concat("HopeScenarios.tsv");
//		String httpKeggFileUrl = "https://raw.githubusercontent.com/ModelSEED/ModelSEEDDatabase/master/Biochemistry/Pathways/KEGG.pathways";

		List<String> keggPathwaysList = new ArrayList<>();
		//		List<String> modelSeedPathwaysList = new ArrayList<>();

		try {
			//			modelSeedPathwaysList = FileUtils.readLines(modelSeedFilePath);
			if(new File(keggFilePath).exists())
				keggPathwaysList = FileUtils.readLines(keggFilePath);
			else
				keggPathwaysList = Utilities.getFileFromHttpUrl(HTTP_FILE_URL);
		} 

		catch (IOException e) {
			e.printStackTrace();
		} 

		parseKeggPathwaysFile(keggPathwaysList);
	}


	/**
	 * @param pathwayList
	 */
	public void parseKeggPathwaysFile(List<String> pathwayList){

		for(int line = 1 ; line < pathwayList.size(); line++){
			
			ArrayList<String> pathwayInfo = new ArrayList<>();

			String[] infoList = pathwayList.get(line).split("\t");

			Integer pos = 0;
			
			for(int i=2 ; i<infoList.length ; i++){
			
				pathwayInfo.add(infoList[i]);
				pos = i;
			}

			this.keggPathwaysDB.put(infoList[1].substring(3), pathwayInfo);
			
			if(pos>2){

				if(pos==4){
					
					String[] reactions = infoList[4].split("\\|");

					for(String reaction : reactions){

						if(!this.reactionsPathways.containsKey(reaction)){
							List<String> pathway = new ArrayList<>();
							pathway.add(infoList[1].substring(3));
							this.reactionsPathways.put(reaction, pathway);
						}
						else
							this.reactionsPathways.get(reaction).add(infoList[1].substring(3));
					}
				}

				String[] superPathways = infoList[3].split(";");
				
				String superPathway = superPathways[0].replaceAll("\"", "").trim();
				String intermediary = superPathways[1].replaceAll("\"", "").trim();
				
				if(!this.super_pathways.containsKey(superPathway)){

					List<String> superPathwayList = new ArrayList<>();
					superPathwayList.add(intermediary);
					this.super_pathways.put(superPathway, superPathwayList);
				}
				else
					this.super_pathways.get(superPathway).add(intermediary);
				
				
				String[] pathway = new String[2];
				pathway[0] = infoList[1].substring(3); 
				pathway[1] = infoList[2].replaceAll("\"", "").trim();

				if(!this.pathways_hierarchy.containsKey(intermediary)){

					List<String[]> pathways = new ArrayList<>();
					pathways.add(pathway);
					this.pathways_hierarchy.put(intermediary, pathways);
				}
				else
					this.pathways_hierarchy.get(intermediary).add(pathway);
			}

		}
	}


	/**
	 * verify the existence of an reactionID in KEGG pathways reactions list
	 * 
	 * @param reactionID
	 * @return boolean
	 */
	public boolean existsReactionIDinKeggPathway(String reactionID){

		if(this.reactionsPathways.containsKey(reactionID))
			return true;

		return false;
	}
	
	/**
	 * @return 
	 * @return
	 */
	public List<String> getReactionPathways(String reactionKeggID){
		
		return this.reactionsPathways.get(reactionKeggID);
	}

	/**
	 * method to get the all reactions for a given pathway
	 * @param pathwayID
	 * @return reactions list
	 */
	public String[] getPathwayReactions(String pathwayID){

		return this.keggPathwaysDB.get(pathwayID).get(2).split("\\|");
	}


	/**
	 * method to get pathway name
	 * @param pathwayID
	 * @return pathwayName
	 */
	public String getPathwayName(String pathwayID){
		
		return this.keggPathwaysDB.get(pathwayID).get(0).replace("\"", "");
	}
	
	
	/**
	 * retrieve all super pathways in Kegg_pathways '.tsv' file
	 * 
	 * @return Map<String, List<String>> superPathways
	 */
	public Map<String, List<String>> getSuperPathways(){
		
		return this.super_pathways;
	}
	
	
	/**
	 * @return
	 */
	public Map<String, List<String[]>> getPathwaysHierarchy(){
		
		return this.pathways_hierarchy;
	}

}
