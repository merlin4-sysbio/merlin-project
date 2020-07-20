package pt.uminho.ceb.biosystems.merlin.gui.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author ODias
 *
 */
public class IntegrationReport {

	/**
	 * Constructor
	 */
	public IntegrationReport(){
		if(new File("reports").exists())
		{
			if(!new File("reports").isDirectory())
			{
				new File("otherDataReports").mkdir();
				new File("reports").renameTo(new File("otherDataReports", "reports"));
				new File("reports").mkdir();
			}			
		}
		else
		{
			(new File("reports")).mkdir();
		}
	}

	/**
	 * @param database 
	 * @param oldLocusTag
	 */
	public void saveLocusTagReport(String database, Map<String, String> oldLocusTag, String path){
		// New locus Tags
		
		StringBuffer strInput=new StringBuffer();
		if(!oldLocusTag.isEmpty())
		{
			strInput.append("The following deprecated Locus Tags from the local database were updated to the new Locus Tags from the Homology database\n");
			strInput.append("Old  Locus Tag\tNew Locus Tag\n");
			for(String locus : oldLocusTag.keySet())
			{
				strInput.append(locus+"\t"+oldLocusTag.get(locus)+"\n");
			}
		}

		BufferedWriter out;
		try
		{
			if(!new File(path+ "/" + database).exists())
			{
				new File(path+ "/" + database).mkdir();
			}
			out = new BufferedWriter(new FileWriter(path+ "/" + database+"/LocusTagIntegrationReport.txt"));
			out.write(strInput.toString());
			out.close();
		}
		catch (IOException e) {e.printStackTrace();}

	}	

	/**
	 * @param existingGeneNames
	 * @param homologyGeneNames
	 * @param geneNames
	 */
	public void saveGeneNameReport( String database, Map<String, List<String>> existingGeneNames, Map<String, String> homologyGeneNames, Map<String, String> geneNames,  String path){
		// for each existing name find Homology name if exists and rename
		StringBuffer strInput=new StringBuffer(),strInput1=new StringBuffer(),strInput2=new StringBuffer(),strInput3=new StringBuffer();
		//for each integrated result
		if(!geneNames.isEmpty())
		{
			strInput.append("Genes names integration Results\n");
			strInput.append("Locus Tag\tGene Name\n");
			for(String name : geneNames.keySet()) {
				
				if(geneNames.get(name)!=null && !geneNames.get(name).isEmpty()) {
					
					strInput.append(name+"\t"+geneNames.get(name)+"\n");
				}
			}
		}
		strInput.append("\n\n\n\n\n");
		strInput1.append("\n\n\n\n\n");
		strInput2.append("\n\n\n\n\n");
		strInput3.append("\n\n\n\n\n");
		if(!existingGeneNames.isEmpty())
		{
			strInput.append("Identics\nLocus Tag\tExisting name\tHomology name\n");
			strInput1.append("Distinct Gene Names\nLocus Tag\tExisting Gene Names\tHomology'ed Gene Names\n");
			strInput2.append("Local Database Gene Names\nLocus Tag\tExisting Gene Names\tHomology'ed Gene Names\n");
			strInput3.append("Local Database Subsets\nLocus Tag\tExisting Proteins\tHomology'ed Proteins\n");
			for(String name : existingGeneNames.keySet())
			{
				//if(!existingGeneNames.get(name).isEmpty() || !existingGeneNames.get(name).get(0).isEmpty())
				if(existingGeneNames.containsKey(name) && !existingGeneNames.get(name).get(0).isEmpty())
				{
					if(homologyGeneNames.containsKey(name))
					{
						if(homologyGeneNames.get(name).equals(existingGeneNames.get(name).get(0)))
						{
							strInput.append(name+"\t"+existingGeneNames.get(name)+"\t"+homologyGeneNames.get(name)+"\n");
							homologyGeneNames.remove(name);
						}
						else
						{
							if(existingGeneNames.get(name).contains(homologyGeneNames.get(name)) && !homologyGeneNames.get(name).isEmpty())
							{
								strInput3.append(name+"\t"+existingGeneNames.get(name)+"\t"+homologyGeneNames.get(name)+"\n");
								homologyGeneNames.remove(name);
							}
							else
							{
								if(!homologyGeneNames.get(name).isEmpty())
								{
									strInput1.append(name+"\t"+existingGeneNames.get(name)+"\t"+homologyGeneNames.get(name)+"\n");
									homologyGeneNames.remove(name);
								}
								else
								{
									strInput2.append(name+"\t"+existingGeneNames.get(name)+"\n");
								}
							}
						}
					}
					else
					{
						strInput2.append(name+"\t"+existingGeneNames.get(name)+"\n");
					}
				}
			}
		}
		//for each remaining Homology namesS
		if(!homologyGeneNames.isEmpty())
		{
			strInput2.append("\n\nHomology Gene Names\nLocus Tag\tHomology'ed Proteins\n");
			for(String name : homologyGeneNames.keySet())
			{
				if(homologyGeneNames.get(name)!=null && !homologyGeneNames.get(name).isEmpty())
				{
					strInput2.append(name+"\t"+homologyGeneNames.get(name)+"\n");
				}
			}
		}
		strInput.append(strInput3).append(strInput1).append(strInput2);
		BufferedWriter out;
		try
		{
			if(!new File(path+ "/" + database).exists())
			{
				new File(path+ "/" + database).mkdir();
			}
			out = new BufferedWriter(new FileWriter(path+ "/" + database+"/GeneNameIntegrationReport.txt"));
			out.write(strInput.toString());
			out.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}	

	/**
	 * @param existingEnzymes
	 * @param homologyEnzymes
	 * @param enzymes
	 */
	public void saveEnzymesReport(String database, Map<String, List<String>> existingEnzymes , Map<String, Set<String>> homologyEnzymesData, Map<String, Set<String>> integratedEnzymes,  String path){
		//for each existing enzyme set and if exists Homology enzyme set
		
		Map<String,Set<String>> enzymes = new TreeMap<String ,Set<String>>();
		for(String lt:homologyEnzymesData.keySet())
			enzymes.put(lt, integratedEnzymes.get(lt));
		
		Map<String,Set<String>> homologyEnzymes = new TreeMap<String ,Set<String>>();
		for(String lt:homologyEnzymesData.keySet())
			homologyEnzymes.put(lt, homologyEnzymesData.get(lt));
		
		StringBuffer strInput=new StringBuffer(),strInput1=new StringBuffer(),strInput2=new StringBuffer(),strInput3=new StringBuffer(),strInput4=new StringBuffer();
		if(!enzymes.isEmpty()) {
			
			strInput.append("Enzymes integration results\n");
			strInput.append("Locus Tag\tEnzymes\n");
			for(String name : enzymes.keySet())
				strInput.append(name+"\t"+enzymes.get(name)+"\n");
		}
		strInput.append("\n\n\n\n\n");
		strInput1.append("\n\n\n\n\n");
		strInput2.append("\n\n\n\n\n");
		strInput3.append("\n\n\n\n\n");
		strInput4.append("\n\n\n\n\n");
		
		if(!existingEnzymes.isEmpty()) {
			
			strInput.append("Locus Tag\tExisting enzymes\tHomology enzymes\n");
			strInput1.append("Distinct enzymes\nLocus Tag\tExisting Proteins\tHomology'ed Proteins\n");
			strInput2.append("Local Database Enzymes\nLocus Tag\tExisting Proteins\tHomology'ed Proteins\n");
			strInput3.append("Local Database Subsets\nLocus Tag\tExisting Proteins\tHomology'ed Proteins\n");
			strInput4.append("Homology Subsets\nLocus Tag\tExisting Proteins\tHomology'ed Proteins\n");
			for(String name : existingEnzymes.keySet())
			{
				if(homologyEnzymes.containsKey(name))
				{
					if(homologyEnzymes.get(name).equals(existingEnzymes.get(name)))
					{
						strInput.append(name+"\t"+existingEnzymes.get(name)+"\t"+homologyEnzymes.get(name)+"\n");
						homologyEnzymes.remove(name);
					}
					else
					{
						if(existingEnzymes.get(name).containsAll(homologyEnzymes.get(name)))
						{
							strInput3.append(name+"\t"+existingEnzymes.get(name)+"\t"+homologyEnzymes.get(name)+"\n");
							homologyEnzymes.remove(name);
						}
						else
							if(!existingEnzymes.get(name).isEmpty() && homologyEnzymes.get(name).containsAll(existingEnzymes.get(name)))
							{
								strInput4.append(name+"\t"+existingEnzymes.get(name)+"\t"+homologyEnzymes.get(name)+"\n");
								homologyEnzymes.remove(name);
							}
							else
								if(!existingEnzymes.get(name).isEmpty())
								{
									strInput1.append(name+"\t"+existingEnzymes.get(name)+"\t"+homologyEnzymes.get(name)+"\n");
									homologyEnzymes.remove(name);
								}
					}
				}
				else
				{
					if(!existingEnzymes.get(name).isEmpty())
					{
						strInput2.append(name+"\t"+existingEnzymes.get(name)+"\n");
					}
				}
			}
		}
		// for each of the remaining enzymes set
		if(!homologyEnzymes.isEmpty())
		{
			strInput2.append("\n\nHomology Enzymes\nLocus Tag\tExisting Proteins\tHomology'ed Proteins\n");
			for(String name : homologyEnzymes.keySet())
			{
				strInput2.append(name+"\t\t"+homologyEnzymes.get(name)+"\n");
			}
		}//for each of the integrated enzyme sets
		strInput.append(strInput3).append(strInput4).append(strInput1).append(strInput2);

		BufferedWriter out;
		try
		{
			if(!new File(path+ "/" + database).exists())
			{
				new File(path+ "/" + database).mkdir();
			}
			out = new BufferedWriter(new FileWriter(path+ "/" + database+"/EnzymesIntegrationReport.txt"));
			out.write(strInput.toString());
			out.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}	

	/**
	 * @param existingProducts
	 * @param homologyProducts
	 * @param proteinNames
	 */
	public void saveProteinNamesReport(String database, Map<String, List<String>> existingProducts, Map<String, String> homologyProductsData, Map<String, String> proteinNames,  String path){
		
		Map<String,String> homologyProducts = new TreeMap<String, String>();
		for(String lt:homologyProductsData.keySet())
		{
			homologyProducts.put(lt, homologyProductsData.get(lt));
		}

		
		StringBuffer strInput=new StringBuffer(),strInput1=new StringBuffer(),strInput2=new StringBuffer(),strInput3=new StringBuffer();
		if(!proteinNames.isEmpty())
		{
			strInput.append("Protein names integration results\n");
			strInput.append("Locus Tag\tProtein Name\n");
			for(String name : proteinNames.keySet())
			{
				if(!proteinNames.get(name).isEmpty())
				{
					strInput.append(name+"\t"+proteinNames.get(name)+"\n");
				}
			}
		}
		strInput.append("\n\n\n\n\n");
		strInput1.append("\n\n\n\n\n");
		strInput2.append("\n\n\n\n\n");
		strInput3.append("\n\n\n\n\n");
		if(!existingProducts.isEmpty())
		{
			strInput.append("Locus Tag\tExisting Proteins\tHomology'ed Proteins\n");
			strInput1.append("Distinct Products\nLocus Tag\tExisting Proteins\tHomology'ed Proteins\n");
			strInput2.append("Local Only\nLocus Tag\tExisting Proteins\tHomology'ed Proteins\n");
			strInput3.append("Homology Only\nLocus Tag\tExisting Proteins\tHomology'ed Proteins\n");
			for(String name : existingProducts.keySet())
			{
				//if(!existingProducts.get(name).isEmpty() || !existingProducts.get(name).get(0).isEmpty())
				if(!existingProducts.get(name).get(0).isEmpty())
				{
					if(homologyProducts.containsKey(name))
					{
						if(homologyProducts.get(name).equals(existingProducts.get(name).get(0)))
						{
							strInput.append(name+"\t"+existingProducts.get(name)+"\t"+Arrays.asList(homologyProducts.get(name))+"\n");
							homologyProducts.remove(name);
						}
						else
						{
							boolean remove=false;
							for(String protein:Arrays.asList(homologyProducts.get(name)))
							{
								if(existingProducts.get(name).contains(protein) && !remove)
								{
									strInput.append(name+"\t"+existingProducts.get(name)+"\t"+Arrays.asList(homologyProducts.get(name))+"\n");
									remove=true;
								}
							}
							if(remove)
							{
								homologyProducts.remove(name);
							}
							else 
							{
								strInput1.append(name+"\t"+existingProducts.get(name)+"\t"+Arrays.asList(homologyProducts.get(name))+"\n");
								homologyProducts.remove(name);
							}
						}
					}
					else 
					{
						strInput2.append(name+"\t"+existingProducts.get(name)+"\n");
						homologyProducts.remove(name);
					}
				}
				else 
				{
					if(homologyProducts.containsKey(name))
					{
						strInput3.append(name+"\t"+Arrays.asList(homologyProducts.get(name))+"\n");
						homologyProducts.remove(name);
					}
				}
			}
		}
		//		if(!homologyProducts.isEmpty())
		//		{
		//			for(String name : homologyProducts.keySet())
		//			{
		//				if(!existingProducts.containsKey(name) && homologyProducts.get(name).length>0)
		//				{
		//					strInput3.append(name+"\t"+Arrays.asList(homologyProducts.get(name))+"\n");
		//				}
		//			}
		//		}
		strInput.append(strInput1).append(strInput2).append(strInput3);

		BufferedWriter out;
		try
		{
			if(!new File(path+ "/" + database).exists())
			{
				new File(path+ "/" + database).mkdir();
			}
			out = new BufferedWriter(new FileWriter(path+ "/" + database+"/ProteinNamesIntegrationReport.txt"));
			out.write(strInput.toString());
			out.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}

	/**
	 * @param newNameConflicts
	 */
	public void saveGeneNamesConflicts(String database, Map<String, String> newNameConflicts,  String path) {
		StringBuffer strInput=new StringBuffer();
		if(!newNameConflicts.isEmpty())
		{
			strInput.append("The following protein names were selected\n");
			strInput.append("Locus Tag\tProtein Name\n");
			for(String name : newNameConflicts.keySet())
			{
				strInput.append(name+"\t"+newNameConflicts.get(name)+"\n");
			}
		}
		BufferedWriter out;
		try
		{
			if(!new File(path+ "/" + database).exists())
			{
				new File(path+ "/" + database).mkdir();
			}
			out = new BufferedWriter(new FileWriter(path+ "/" + database+"/GeneNameConflictsReport.txt"));
			out.write(strInput.toString());
			out.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}

	/**
	 * @param newProductsConflicts
	 */
	public void saveProteinConflicts(String database, Map<String, String> newProductsConflicts,  String path) {
		StringBuffer strInput=new StringBuffer();
		if(!newProductsConflicts.isEmpty())
		{
			strInput.append("The following protein names were selected\n");
			strInput.append("Locus Tag\tProtein Name\n");
			for(String name : newProductsConflicts.keySet())
			{
				strInput.append(name+"\t"+newProductsConflicts.get(name)+"\n");
			}
		}
		BufferedWriter out;
		try
		{
			if(!new File(path+ "/" + database).exists())
			{
				new File(path+ "/" + database).mkdir();
			}
			out = new BufferedWriter(new FileWriter(path+ "/" + database+"/ProteinNameConflictsReport.txt"));
			out.write(strInput.toString());
			out.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}

	/**
	 * @param dsa
	 * @param homologyEnzyme
	 */
	public void pathwaysIntegrationReport(String database, Map<String,Set<String>> allPathways, Map<String,Set<String>> exists, Map<String,Set<String>> homologyEnzymesData, String dirPath) {
		
		Map<String,Set<String>> homologyEnzyme = new TreeMap<String ,Set<String>>();
		for(String lt:homologyEnzymesData.keySet())
		{
			homologyEnzyme.put(lt, homologyEnzymesData.get(lt));
		}
		//remove the existing enzymes from pathways that exists in the model
		for(String path: exists.keySet())
		{
			allPathways.get(path).removeAll(exists.get(path));
		}
		// remove the existing enzymes from the Homology enzymes
		for(String path: exists.keySet())
		{
			for(String locus: homologyEnzyme.keySet())
			{
				homologyEnzyme.get(locus).removeAll(exists.get(path));
			}
		}

		// map the enzymes to a list of locus
		Map<String,Set<String>> enzymesTOlocus =  new TreeMap<String,Set<String>>();
		Set<String>  locusList;
		for(String key: homologyEnzyme.keySet())
		{
			for(String enzyme: homologyEnzyme.get(key))
			{
				if(enzymesTOlocus.containsKey(enzyme))
				{
					locusList = enzymesTOlocus.get(enzyme);
					locusList.add(key);
					enzymesTOlocus.put(enzyme,locusList);
				}
				else
				{
					locusList =  new TreeSet<String>();
					locusList.add(key);
					enzymesTOlocus.put(enzyme,locusList);
				}
			}
		}

		// for each path of all the paths if the Homology enzymes exists add new enzyme to path model
		// Check if path is complete
		List<String> completePathways = new ArrayList<String>();
		Map<String,Set<String>> incompletePathways = new TreeMap<String,Set<String>>(allPathways);
		Map<String,Set<String>> newEnzymes =  new TreeMap<String,Set<String>>();
		Set<String> enzymesList;
		for(String pathway: allPathways.keySet())
		{
			Set<String>  enzymes = new TreeSet<String>(allPathways.get(pathway));
			for(String enz: enzymes)
			{
				if(enzymesTOlocus.containsKey(enz))
				{
					incompletePathways.get(pathway).remove(enz);
					if(incompletePathways.get(pathway).isEmpty()){completePathways.add(pathway);}

					if(newEnzymes.containsKey(pathway)){enzymesList = newEnzymes.get(pathway);}
					else{enzymesList =  new TreeSet<String>();}
					enzymesList.add(enz);
					newEnzymes.put(pathway,enzymesList);
				}
			}
		}

		StringBuffer strInput = new StringBuffer();

		if(!completePathways.isEmpty())
		{//complete pathways
			strInput.append("The following pathways are complete:\n\n");
			for(String path : completePathways)
			{
				strInput.append(path+"\n");
			}
			this.createFile(database, "/Complete_Pathways.xlsx", strInput, dirPath);
		}
		if(!incompletePathways.isEmpty())
		{//complete pathways
			strInput.append("The following pathways are incomplete:\n\n");
			for(String path : incompletePathways.keySet())
			{
				if(incompletePathways.containsKey(path))
				{
					strInput.append(path+"\n");
					for(String ecn : incompletePathways.get(path))
					{
						strInput.append("\t"+ecn+"\n");
					}
				}
			}
			this.createFile(database, "/Incomplete_Pathways.xlsx", strInput, dirPath);
		}

		//for each new enzyme in NEW path, add to file
		strInput = new StringBuffer();
		strInput.append("Pathway\tEnzyme\tLocusTags:\n\n");
		for(String path : newEnzymes.keySet())
		{
			if(!exists.containsKey(path))
			{
				strInput.append(path+"\n");
				for(String ecn : newEnzymes.get(path))
				{
					strInput.append("\t"+ecn+"\t"+enzymesTOlocus.get(ecn)+"\n");
				}
			}
		}
		this.createFile(database, "/New_Pathway.xlsx", strInput, dirPath);

		// for each new enzyme in existing path, add to file
		strInput = new StringBuffer();
		strInput.append("Pathway\tEnzyme\tLocusTags:\n\n");
		for(String path : newEnzymes.keySet())
		{
			if(exists.containsKey(path))
			{
				strInput.append(path+"\n");
				for(String ecn : newEnzymes.get(path))
				{
					strInput.append("\t"+ecn+"\t"+enzymesTOlocus.get(ecn)+"\n");
				}
			}
		}
		this.createFile(database, "/New_enzymes.xlsx", strInput, dirPath);

		// for each enzyme in each existing path, add to file			
		strInput = new StringBuffer();
		strInput.append("Pathway\tEnzyme:\n\n");
		for(String name : exists.keySet())
		{
			strInput.append(name+"\n");
			for(String ecn : exists.get(name))
			{
				strInput.append("\t"+ecn+"\n");
			}
		}
		this.createFile(database, "/Existing_enzymes.xlsx", strInput, dirPath);
	}


	/**
	 * @param database
	 * @param fileName
	 * @param strInput
	 */
	private void createFile(String database, String fileName, StringBuffer strInput,  String path){
		BufferedWriter out;
		try
		{
			if(!new File(path+ "/" + database).exists())
			{
				new File(path+ "/" + database).mkdir();
			}
			out = new BufferedWriter(new FileWriter(path+ "/" + database+fileName));
			out.write(strInput.toString());
			out.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}


}
