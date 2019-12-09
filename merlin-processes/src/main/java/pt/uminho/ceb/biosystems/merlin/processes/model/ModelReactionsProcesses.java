package pt.uminho.ceb.biosystems.merlin.processes.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ModelContainer;
import pt.uminho.ceb.biosystems.merlin.core.containers.model.ProteinContainer;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.WorkspaceDataTable;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.auxiliary.ModelBlockedReactions;
import pt.uminho.ceb.biosystems.merlin.core.datatypes.model.datatables.ModelPathwayReactions;
import pt.uminho.ceb.biosystems.merlin.services.ProjectServices;
import pt.uminho.ceb.biosystems.mew.biocomponents.validation.chemestry.BalanceValidator;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

/**
 * @author ODias
 *
 */
public class ModelReactionsProcesses {

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getStats()
	 */
	public static String[][] getStats(List<Integer> values) {

		String[][] res = new String[16][];

		int i=0;

		res[i] = new String[] {"Total number of reactions in the model", ""+values.get(i)};
		i++;

		res[i] = new String[] {"Number of KEGG reactions in the model", ""+values.get(i)};
		i++;

		res[i] = new String[] {"Number of reactions inserted by HOMOLOGY in the model", ""+values.get(i)};
		i++;

		res[i] = new String[] {"Number of reactions from the TRANSPORTERS annotation tool in the model", ""+values.get(i)};
		i++;

		res[i] = new String[] {"Number of reactions inserted MANUALLY in the model", ""+values.get(i)};
		i++;

		res[i] = new String[] {"", ""};
		i++;

		res[i] = new String[] {"Number of reactions", ""+values.get(i)};
		i++;

		res[i] = new String[] {"Number of reversible reactions", ""+values.get(i)};
		i++;

		res[i] = new String[] {"Number of irreversible reactions", ""+values.get(i)};
		i++;

		res[i] = new String[] {"Number of reactions from KEGG", ""+values.get(i)};
		i++;

		res[i] = new String[] {"Number of reactions from the TRANSPORTERS annotation tool", ""+values.get(i)};
		i++;

		res[i] = new String[] {"	Number of reactions with no identifier associated", ""+values.get(i)};
		i++;

		res[i] = new String[] {"	Number of reactions with no equation associated", ""+values.get(i)};
		i++;

		res[i] = new String[] {"Number of reactions with no pathway associated", ""+values.get(i)};
		i++;

		res[i] = new String[] {"Average number of reactants by reaction",""+values.get(i)};
		i++;
		res[i] = new String[] {"Average number of products by reaction", ""+values.get(i)};

		return res;
	}

	/**
	 * @param encodedOnly
	 * @param capsule
	 * @param pathways
	 * @return
	 * @throws Exception 
	 */
	public static ModelPathwayReactions getMainTableData(String databaseName, boolean encodedOnly, ModelContainer capsule, Map<Integer, String> pathways) throws Exception {

		ModelPathwayReactions reactionsData=null;

		ArrayList<String> columnsNames = new ArrayList<String>();
		columnsNames.add("info");
		columnsNames.add("pathway name");
		columnsNames.add("identifier");
		columnsNames.add("equation");
		if(ProjectServices.isCompartmentalisedModel(databaseName))
			columnsNames.add("localization");
		else
			columnsNames.add("source");
		columnsNames.add("notes");
		columnsNames.add("reversible");
		columnsNames.add("in model");

		reactionsData = new ModelPathwayReactions(columnsNames, "reactions", pathways, encodedOnly) {

			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int col){
				if (col==0 || col>4) {

					return true;
				}
				else return false;
			}
		};

		Map<Integer, Pair<Integer, List<Object>>> data = capsule.getReactionsData();

		for (int i=0; i<capsule.getReactionsOrder().size(); i++ ) {

			int reaction = capsule.getReactionsOrder().get(i);
			Pair<Integer, List<Object>> line = data.get(i);

			reactionsData.addLine(line.getB(), reaction, line.getA());
		}
		return reactionsData;
	}

	/**
	 * @return a list with all pathways, except the SuperPathways
	 */
	public static String[] getUpdatedPathways(List<String[]> availablePathways) {

		List<String> pathways = new ArrayList<String>();
		//		Map <String, Integer> pathID = new HashMap<String, Integer>();
		//		pathID.put("", 0);
		pathways.add("");
		Set<String> pathwaysSet=new HashSet<String>();

		for(int i = 0; i<availablePathways.size(); i++){
			String[] list = availablePathways.get(i);

			//			pathID.put(list[1], Integer.parseInt(list[0]));
			pathwaysSet.add(list[1]);					
		}

		pathways.addAll(pathwaysSet);
		java.util.Collections.sort(pathways);
		String[] paths = new String[pathways.size()+1];
		paths[0] = "All";

		for(int i=0;i<pathways.size();i++) {

			paths[i+1] = pathways.get(i);
		}
		return paths;
	}

	/**
	 * @return a list with all pathways, except the SuperPathways
	 */
	public static Map<Integer, Integer> getSelectedPathIndexID(List<String[]> availablePathways) {

		Map<Integer, Integer> selectedPathIndexID = new HashMap<>();
		List<String> pathways = new ArrayList<String>();
		pathways.add("");
		Map <String, Integer> pathID = new HashMap<String, Integer>();
		pathID.put("", 0);


		for(int i = 0; i<availablePathways.size(); i++){

			String[] list = availablePathways.get(i);

			pathID.put(list[1], Integer.parseInt(list[0]));
			pathways.add(list[1]);					
		}

		java.util.Collections.sort(pathways);
		String[] paths = new String[pathways.size()+1];
		paths[0] = "All";


		for(int i=0;i<pathways.size();i++)
			selectedPathIndexID.put(i+1, pathID.get(pathways.get(i)));		

		return selectedPathIndexID;
	}

	/**
	 * @return
	 */
	public static String[] getGenesModel(List<String> lls) {

		String[] res = new String[lls.size()+1];

		res[0] = "";

		for(int i=0;i<lls.size();i++)
			res[i+1] = lls.get(i);

		return res;
	}

	/**
	 * @return
	 */
	public static Map<String, Integer> getGenesModelMap(List<GeneContainer> genes) {

		Map<String, Integer> ret = new HashMap<>();

		for(GeneContainer gene : genes) {

			String locusTag = gene.getLocusTag();
			String name = gene.getName();

			if(locusTag != null && name != null && !name.trim().isEmpty())
				locusTag = locusTag.concat(" (").concat(name).concat(")");

			ret.put(locusTag, gene.getIdGene());
		}

		return ret;
	}

	/**
	 * @return
	 */
	public static String[] getEnzymesModel(List<ProteinContainer> lls) {

		String[] res = new String[lls.size()+1];

		res[0] = "";

		for(int i=0;i<lls.size();i++) {
			
			ProteinContainer container = lls.get(i);
			
			String s = container.getExternalIdentifier() + "___" + container.getName() + "___" + container.getIdProtein() ; // + "___" + container.getIdProtein() 
			
			res[i+1] = s;
			
		}
		return res;
	}

	/**
	 * @return list of all pathways, including superpathways
	 */
	public static String[] getPathways(List<String> lls) {

		String[] res = new String[lls.size()+1];

		res[0] = "";

		for(int i=0;i<lls.size();i++)
			res[i+1] = lls.get(i);

		return res;
	}

	/* (non-Javadoc)
	 * @see datatypes.metabolic_regulatory.Entity#getRowInfo(java.lang.String)
	 */
	public static WorkspaceDataTable[] getRowInfo(int id, String name,Pair<Map<String, String>, List<List<List<String>>>> data,
			BalanceValidator balanceValidator, ModelBlockedReactions blockedReactions, 
			Map<String, String> externalModelIdentifiers) {

		Map<String,String> metabolites = data.getA();
		List<List<List<String>>> resultsListsLists = data.getB();

		int size = 6;
		boolean geneRules = resultsListsLists.size()>size;
		WorkspaceDataTable[] results = new WorkspaceDataTable[size];

		if(balanceValidator != null && externalModelIdentifiers!=null)
			results = new WorkspaceDataTable[results.length+1];

		if(blockedReactions!= null)
			results = new WorkspaceDataTable[results.length+1];

		if(geneRules)
			results = new WorkspaceDataTable[results.length+1];

		int counter = 0;

		List<String> columnsNames = new ArrayList<String>();
		columnsNames.add("metabolite");
		columnsNames.add("formula");
		columnsNames.add("identifier");
		columnsNames.add("compartment");
		columnsNames.add("stoichiometric coefficient");
		results[counter++] = new WorkspaceDataTable(columnsNames, "reaction");

		columnsNames = new ArrayList<String>();
		columnsNames.add("identifier");
		columnsNames.add("proteins");
		columnsNames.add("in model");
		results[counter++] = new WorkspaceDataTable(columnsNames, "enzymes");

		columnsNames = new ArrayList<String>();
		columnsNames.add("property");
		columnsNames.add("values");
		results[counter++] = new WorkspaceDataTable(columnsNames, "properties");

		columnsNames = new ArrayList<String>();
		columnsNames.add("synonyms");
		results[counter++] = new WorkspaceDataTable(columnsNames, "synonyms");

		columnsNames = new ArrayList<String>();
		columnsNames.add("pathways");
		results[counter++] = new WorkspaceDataTable(columnsNames, "pathways");
		
		columnsNames = new ArrayList<String>();
		columnsNames.add("source");
		results[counter++] = new WorkspaceDataTable(columnsNames, "source");

		if(balanceValidator != null && externalModelIdentifiers!=null) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("stoichiometric balance");
			columnsNames.add("values");
			results[counter++] = new WorkspaceDataTable(columnsNames, "balance");
		}

		if(blockedReactions!= null) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("metabolite");
			columnsNames.add("identifier");
			columnsNames.add("dead end");
			results[counter++] = new WorkspaceDataTable(columnsNames, "gaps");
		}

		if(geneRules) {

			columnsNames = new ArrayList<String>();
			columnsNames.add("rules");
			results[counter++] = new WorkspaceDataTable(columnsNames, "GPRs");
		}

		counter = 0;

		int l = 0;

		while(l<size) {

			List<List<String>> resultsList = resultsListsLists.get(counter);

			for(int i=0; i<resultsList.size(); i++){

				ArrayList<String> list = (ArrayList<String>) resultsList.get(i);
				results[counter].addLine(list);
			}
			counter++;
			l++;
		}


		if(balanceValidator!=null && externalModelIdentifiers.containsKey(name)) {

			ArrayList<String> resultList = new ArrayList<String>();
			resultList.add("sum of reactants");
			resultList.add(balanceValidator.getSumOfReactantsToString(externalModelIdentifiers.get(name)));
			results[counter].addLine(resultList);
			resultList = new ArrayList<String>();
			resultList.add("sum of products");
			resultList.add(balanceValidator.getSumOfProductsToString(externalModelIdentifiers.get(name)));
			results[counter].addLine(resultList);
			resultList = new ArrayList<String>();
			resultList.add("balance");
			resultList.add(balanceValidator.getDifResultToString(externalModelIdentifiers.get(name)));
			results[counter].addLine(resultList);
			counter++;
		}

		if(blockedReactions!= null) {

			for(String kegg_id : metabolites.keySet()) {

				ArrayList<String> resultList = new ArrayList<String>();
				resultList.add(metabolites.get(kegg_id));
				resultList.add(kegg_id);
				resultList.add(blockedReactions.getCompounds().contains(kegg_id)+"");
				results[counter].addLine(resultList);
			}
			counter++;
		}
		
		if(geneRules) {
			
			Integer auxCounter = counter;
			
			if(balanceValidator!=null && externalModelIdentifiers.containsKey(name))
				counter--;
			
			if(blockedReactions!= null)
				counter--;
			
			List<List<String>> resultsList = resultsListsLists.get(counter);

			for(int i=0; i<resultsList.size(); i++) {
				
				ArrayList<String> list = (ArrayList<String>) resultsList.get(i);
				results[auxCounter].addLine(list);
			}
		}

		return results;
	}

	/**
	 * colorize pathways
	 * @param encoded 
	 */
	public static Map<Integer,Color> colorPaths(List<String[]> availablePathways){

		Map<Integer,Color> pathwayColors= new HashMap<Integer,Color>();
		String[] paths = getUpdatedPathways(availablePathways);

		List<Color> usedColors = new ArrayList<Color>(); 
		// no path reactions gets merlin logo color
		Color merlin = new Color(0, 128, 128);
		usedColors.add(merlin);
		pathwayColors.put(0, merlin);

		for(Integer path=1; path<paths.length; path++)			
			pathwayColors = ModelReactionsProcesses.newColor(pathwayColors, usedColors, generateColor(), path);

		return pathwayColors;
	}

	/**
	 * @param usedColors
	 * @param color
	 * @param path
	 * @return
	 */
	private static Map<Integer,Color> newColor(Map<Integer,Color> pathwayColors, List <Color> usedColors, Color color, Integer path) {

		if(usedColors.contains(color) || color.equals(new Color(0,0,0)) || color.equals(new Color(255,255,255))) {

			newColor(pathwayColors, usedColors, generateColor(), path);
		}
		else {

			usedColors.add(color);
			pathwayColors.put(path, color);
		}

		return pathwayColors;		
	}

	/**
	 * @return
	 */
	private static Color generateColor(){
		//		int red = new Random().nextInt(70);
		//		int red = new Random().nextInt(256);
		//		int green = new Random().nextInt(256);
		//		while(green<200){green = new Random().nextInt(225);}
		//		int blue = new Random().nextInt(256);
		//		while(blue<112){blue = new Random().nextInt(225);}

		Color mix = new Color(255, 255, 255);

		Random random = new Random();
		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);

		// mix the color
		if (mix != null) {
			red = (red + mix.getRed()) / 2;
			green = (green + mix.getGreen()) / 2;
			blue = (blue + mix.getBlue()) / 2;
		}

		return new Color(red, green, blue);
	}

	/**
	 * @return
	 */
	public static String[] getCompartments(List<String> cls) {

		String[] res = new String[cls.size()];

		for(int i=0;i<cls.size();i++)
			res[i] = cls.get(i);

		return res;
	}


}
