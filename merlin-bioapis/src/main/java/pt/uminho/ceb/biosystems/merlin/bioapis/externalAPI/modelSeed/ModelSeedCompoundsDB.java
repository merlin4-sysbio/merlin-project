package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.modelSeed;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.utilities.Utilities;
import pt.uminho.ceb.biosystems.merlin.utilities.io.FileUtils;


public class ModelSeedCompoundsDB {

	private static final String HTTP_FILE_URL = "https://raw.githubusercontent.com/ModelSEED/ModelSEEDDatabase/master/Biochemistry/compounds.tsv";

	private Map<String, String[]> compoundsDB;
	private Set<String> coreCompounds;


	public ModelSeedCompoundsDB(){

		this.compoundsDB = new HashMap<String, String[]>();
		this.coreCompounds = new HashSet<>();

		readCompoundsDBFile();
	}



	/**
	 * reader for modelSeed compounds database '.tsv' file 
	 * @return
	 */
	public void readCompoundsDBFile() {

		String filePath = FileUtils.getConfFolderPath().concat("ModelSeedCompounds.tsv");
		//		String httpFileUrl = "https://raw.githubusercontent.com/ModelSEED/ModelSEEDDatabase/master/Biochemistry/compounds.tsv";

		List<String> compoundsList = new ArrayList<>();

		try {
			if(new File(filePath).exists())
				compoundsList = FileUtils.readLines(filePath);
			else
				compoundsList = Utilities.getFileFromHttpUrl(HTTP_FILE_URL);
		} 

		catch (IOException e) {
			e.printStackTrace();
		} 
		parseCompoundsFile(compoundsList);
	}


	/**
	 * @param compoundsList
	 */
	public void parseCompoundsFile(List<String> compoundsList){

		for(String line : compoundsList){

			String[] compoundInfo = new String[9];
			String[] infoList = line.split("\t");

			//			for(int i=1 ; i<8 ; i++)
			//				compoundInfo.add(infoList[i]);

			boolean isObsolete = infoList[9].equals("1");


			compoundInfo[0] = infoList[1];	//abbreviation
			compoundInfo[1] = infoList[2];	//name
			compoundInfo[2] = infoList[3];	//formula
			compoundInfo[3] = infoList[4];	//mass
			compoundInfo[4] = infoList[6];	//inchi key
			compoundInfo[5] = infoList[7];	//charge
			//isCore
			if(infoList[8].equals("1")){
				compoundInfo[6] = "true";
				this.coreCompounds.add(infoList[0]);
			}
			else{
				compoundInfo[6] = "false";
			}

			compoundInfo[7] = infoList[10]; //linked compounds
			
			compoundInfo[8] = Boolean.toString(isObsolete);

			this.compoundsDB.put(infoList[0], compoundInfo);
			
		}

	}


	/**
	 * verify if a given compoundID is present in ModelSeed Compound tsv file
	 * 
	 * @param compoundID
	 * @return
	 */
	public boolean existsCompoundID(String compoundID){

		if(this.compoundsDB.containsKey(compoundID))
			return true;

		return false;
	}


	/**
	 * method to get compound name
	 * @param compoundID
	 * @return
	 */
	public String getCompoundName(String compoundID){

		return this.compoundsDB.get(compoundID)[1];
	}

	/**
	 * method to get compound abbreviation
	 * @param compoundID
	 * @return
	 */
	public String getCompoundAbbreviation(String compoundID){

		return this.compoundsDB.get(compoundID)[0];	

	}
	
	
	
	public boolean isObsolete(String compoundID){

		return Boolean.getBoolean(this.compoundsDB.get(compoundID)[8]);	

	}
	

	/**
	 * method to get compound formula
	 * @param compoundID
	 * @return
	 */
	public String getCompoundFormula(String compoundID){

		return this.compoundsDB.get(compoundID)[2];	
	}

	/**
	 * method to get compound charge
	 * @param compoundID
	 * @return
	 */
	public String getCompoundCharge(String compoundID){

		return this.compoundsDB.get(compoundID)[5];	
	}

	/**
	 * method to get compound molecular weight
	 * @param compoundID
	 * @return
	 */
	public String getCompoundMolecularWeight(String compoundID){

		return this.compoundsDB.get(compoundID)[3];
	}

	/**
	 * method to get compound Inchikey
	 * @param compoundID
	 * @return
	 */
	public String getCompoundInchikey(String compoundID){

		return this.compoundsDB.get(compoundID)[4];
	}

	/**
	 * @param compoundID
	 * @return
	 */
	public boolean isCoreCompound(String compoundID){

		return Boolean.parseBoolean(this.compoundsDB.get(compoundID)[6]);

	}

	/**
	 * @param compoundID
	 * @return
	 */
	public List<String> getLinkedCompounds(String compoundID){

		List<String> linkedCompounds = new ArrayList<>();

		if(this.compoundsDB.get(compoundID)[7]!=null && !this.compoundsDB.get(compoundID)[7].equals("null"))
			linkedCompounds = Arrays.asList(this.compoundsDB.get(compoundID)[7].split(";"));

		return linkedCompounds;
	}

	/**
	 * @return
	 */
	public Set<String> getCoreCompounds(){

		return this.coreCompounds;
	}
}
