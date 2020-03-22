package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.modelSeed;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;


public class ModelSeedReactionsDB {

	private static final String HTTP_FILE_URL = "https://raw.githubusercontent.com/ModelSEED/ModelSEEDDatabase/master/Biochemistry/reactions.tsv";

	private Map<String, String[]> modelSeedReactions;



	public ModelSeedReactionsDB(){

		this.modelSeedReactions = new HashMap<String,String[]>();

		readReactionsDBFile();
	}

	/**
	 * reader for modelSeed compounds database '.tsv' file 
	 * @return
	 */
	public void readReactionsDBFile() {

		String filePath = FileUtils.getConfFolderPath().concat("ModelSeedReactions.tsv");
		//		String httpFileUrl = "https://raw.githubusercontent.com/ModelSEED/ModelSEEDDatabase/master/Biochemistry/reactions.tsv";

		List<String> reactionsList = new ArrayList<>();

		try {
			if(new File(filePath).exists())
				reactionsList = FileUtils.readLines(filePath);
			else
				reactionsList = Utilities.getFileFromHttpUrl(HTTP_FILE_URL);
		} 

		catch (IOException e) {
			e.printStackTrace();
		} 

		parseReactionsFile(reactionsList);
	}


	/**
	 * @param compoundsList
	 */
	public void parseReactionsFile(List<String> reactionsList){

		//		for(String line : reactionsList){
		for(int i=1; i<reactionsList.size(); i++){ 

			//			ArrayList<String> reactionInfo = new ArrayList<>();

			String[] infoList = reactionsList.get(i).split("\t");
			String[] reactionInfo = new String[11];

			boolean isObsolete = infoList[18].equals("1");

			reactionInfo[0] = infoList[1]; //abbreviation
			reactionInfo[1] = infoList[2]; //enzyme name
			reactionInfo[2] = infoList[4]; //stoichiometry
			reactionInfo[3] = infoList[5]; //isTrasnport

			//reaction direction
			String direction = infoList[8];
			if(!direction.equals("<") && !direction.equals("=") && !direction.equals("<"))
				direction = infoList[9];
			reactionInfo[4] = direction; 

			//isReversible
			if(direction.equals("="))   
				reactionInfo[5] = Boolean.toString(true);
			else
				reactionInfo[5] = Boolean.toString(false);

			//equation
			//			String equation = infoList[7].replaceAll("[\\[\\(]\\s*\\d*\\s*[\\]\\)]", "").trim();
			//			String equation = infoList[7].replaceAll("\\s*[\\[\\(]\\s*\\d*\\w*[\\.\\=]*\\d*\\s*[\\]\\)]", "").trim();
			String equation = infoList[7].replaceAll("\\s*[\\[\\(]\\s*\\d*[\\.\\=]*\\d*\\s*[\\]\\)]", "").trim();
			equation = equation.replaceAll("\\s*[\\[\\(]\\s*\\w*[\\.\\=]+\\d*\\s*[\\]\\)]", "").trim();
			if(equation.contains(" <=> ") && !direction.equals("=")){
				if(direction.equals("<"))
					equation =  equation.replace(" <=> ", " <= ");
				else
					equation = equation.replace(" <=> ", " => ");
			}
			else if(equation.contains(" => ") && !direction.equals(">")){
				if(direction.equals("<"))
					equation =  equation.replace(" => ", " <= ");
				else
					equation = equation.replace(" => ", " <=> ");
			}
			else if(equation.contains(" <= ") && !direction.equals("<")){
				if(direction.equals(">"))
					equation =  equation.replace(" <= ", " => ");
				else
					equation = equation.replace(" <= ", " <=> ");
			}
			reactionInfo[6] = equation; 

			reactionInfo[7] = infoList[16]; //compounds in reaction
			reactionInfo[8] = infoList[19]; //linked reactions

			//Reaction spontaneity
			if(infoList[14]!=null && !infoList[14].equalsIgnoreCase("null")){
				Double gibbsEnergy = Double.parseDouble(infoList[14]);
				//				Double gibbsEnergyErr = Double.parseDouble(infoList[15]);
				boolean isSpontaneous;

				//				if((gibbsEnergy+gibbsEnergyErr)<0 && (gibbsEnergy-gibbsEnergyErr)<0)
				if(gibbsEnergy<0)
					isSpontaneous = true;
				else
					isSpontaneous = false;

				reactionInfo[9] = Boolean.toString(isSpontaneous);
			}
			
			reactionInfo[10] = Boolean.toString(isObsolete);




			//			reactionInfo.add(infoList[1]); //abbreviation
			//			reactionInfo.add(infoList[2]); //enzyme name
			//			reactionInfo.add(infoList[4]); //stoichiometry
			//			reactionInfo.add(infoList[5]); //isTrasnport
			//			reactionInfo.add(infoList[7]); //equation
			//			
			//			
			//			
			//			if(infoList[9].equals("="))   //isReversible
			//				reactionInfo.add(Boolean.toString(true));
			//			else
			//				reactionInfo.add(Boolean.toString(false));
			//			
			//			reactionInfo.add(infoList[9]); //direction
			//			reactionInfo.add(infoList[16]); //compounds in reaction
			//			reactionInfo.add(infoList[19]); //linked reactions


			this.modelSeedReactions.put(infoList[0], reactionInfo);

		}
	}


	/**
	 * verify if a given reactionID is present in ModelSeed Reactions tsv file
	 * 
	 * @param reactionID
	 * @return
	 */
	public boolean existsReactionID(String reactionID){

		if(this.modelSeedReactions.containsKey(reactionID))
			return true;

		return false;
	}

	/**
	 * method to get the reaction enzyme name
	 * @param reactionID
	 * @return enzyme name
	 */
	public String getEnzymeName(String reactionID){

		//		System.out.println(reactionID);
		//		System.out.println(modelSeedReactions.get(reactionID));
		//		System.out.println(modelSeedReactions.get(reactionID).get(1));

		return this.modelSeedReactions.get(reactionID)[1];
	}

	/**
	 * method to get reaction abbreviation
	 * @param reactionID
	 * @return reaction abbreviation
	 */
	public String getReactionAbbreviation(String reactionID){

		return this.modelSeedReactions.get(reactionID)[0];	

	}

	/**
	 * @param reactionID
	 * @return
	 */
	public String getReactionStoichiometry(String reactionID){

		return this.modelSeedReactions.get(reactionID)[2];	
	}

	/**
	 * @param reactionID
	 * @return
	 */
	public boolean isTransportReaction(String reactionID){

		return Boolean.parseBoolean(this.modelSeedReactions.get(reactionID)[3]);	
	}

	/**
	 * @param reactionID
	 * @return
	 */
	public String getReactionEquation(String reactionID){

		return this.modelSeedReactions.get(reactionID)[6];	
	}


	/**
	 * @param reactionID
	 * @return
	 */
	public boolean isReactionReversible(String reactionID){

		return Boolean.parseBoolean(this.modelSeedReactions.get(reactionID)[5]);	
	}


	/**
	 * @param reactionID
	 * @return
	 */
	public boolean isReactionSpontaneous(String reactionID){

		return Boolean.parseBoolean(this.modelSeedReactions.get(reactionID)[9]);	
	}


	/**
	 * @param reactionID
	 * @return
	 */
	public String getReactionDirection (String reactionID){

		return this.modelSeedReactions.get(reactionID)[4];	
	}


	/**
	 * @param reactionID
	 * @return
	 */
	public String[] getCompoundsInReaction(String reactionID){

		return this.modelSeedReactions.get(reactionID)[7].split(";");	
	}
	
	
	public boolean isObsolete(String reactionID){

		return Boolean.getBoolean(this.modelSeedReactions.get(reactionID)[10]);	
	}


	public Map<String, Double> getReactantsIdsAndStoichiometry(String reactionID) {

		String[] reactionIdsAndStoichiometry = this.getReactionStoichiometry(reactionID).split(";");

		Map<String, Double> res = new HashMap<String,Double>();

		for (String reaction : reactionIdsAndStoichiometry) {

			Double stoic = Double.valueOf(reaction.split(":")[0]);

			if (stoic < 0.0) {

				String reactantID = reaction.split(":")[1];

				res.put(reactantID, stoic);

			}

		}

		return res;
	}

	public Map<String, Double> getProductsIdsAndStoichiometry(String reactionID) {

		String[] productIdsAndStoichiometry = this.getReactionStoichiometry(reactionID).split(";");

		Map<String, Double> res = new HashMap<String,Double>();

		for (String reaction : productIdsAndStoichiometry) {

			Double stoic = Double.valueOf(reaction.split(":")[0]);

			if (stoic > 0.0) {

				String productID = reaction.split(":")[1];

				res.put(productID, stoic);

			}

		}

		return res;
	}
	/**
	 * @param reactionID
	 * @return
	 */
	public List<String> getLinkedReactions(String reactionID){

		List<String> linkedReactions = new ArrayList<>();

		if(this.modelSeedReactions.get(reactionID)[8]!=null && !this.modelSeedReactions.get(reactionID)[8].equals("null"))
			linkedReactions = Arrays.asList(this.modelSeedReactions.get(reactionID)[8].split(";"));

		return linkedReactions;	
	}
}
