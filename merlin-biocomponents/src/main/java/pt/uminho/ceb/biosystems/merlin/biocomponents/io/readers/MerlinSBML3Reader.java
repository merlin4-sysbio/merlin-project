package pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.CVTerm.Qualifier;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.ext.fbc.FBCConstants;
import org.sbml.jsbml.ext.fbc.FBCModelPlugin;
import org.sbml.jsbml.ext.fbc.GeneProduct;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.GeneCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.InvalidBooleanRuleException;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.JSBMLLevel3Reader;


public class MerlinSBML3Reader extends JSBMLLevel3Reader{

	private static final long serialVersionUID = 1L;

	private Map<String,List<String[]>> genesProteins;
	private Map<String,Map<String,Set<String>>> reactionsProteins;
	private Map<String, Set<String>> proteins;


	
	public MerlinSBML3Reader(String filePath, String organismName) throws Exception {
		super(filePath, organismName);

		getReactionsEnzymes();
		buildReactionsProteinsInfo();
	}


	@Override
	public void readGenes(){

		FBCModelPlugin fbcModel = (FBCModelPlugin) super.document.getModel().getExtension(FBCConstants.namespaceURI);
		ListOf<GeneProduct> genesModel = fbcModel.getListOfGeneProducts();

		this.genesProteins = new HashMap<>();	//Map where the SBML gene ids are the keys, and the values are Lists of Arrays (pos 0: database name; pos 1:protein id)
		this.proteins = new HashMap<>();		//Map with sets of proteins external database ids, where the keys are the DB names

		for(int i=0; i< genesModel.size(); i++){

			GeneProduct gene = genesModel.get(i);
			//SGC 
			String geneID = gene.getId();
			String label = gene.getLabel().replace("__SBML_DOT__", ".");

			genes.put(geneID, new GeneCI(geneID, label));	

			Annotation annotation = gene.getAnnotation();

			for(CVTerm cvterm : annotation.getListOfCVTerms()){

				if(cvterm.getQualifier().equals(Qualifier.BQB_IS)){

					for(String resource : cvterm.getResources()){

						String entry = resource.replace("http://identifiers.org/", "");

						String[] splitedEntry = entry.split("/");

						//						String database = splitedEntry[0];
						//						String id = splitedEntry[1];

						List<String[]> proteins =  new ArrayList<>();
						proteins.add(splitedEntry);

						this.genesProteins.put(geneID, proteins);

						if(this.proteins.containsKey(splitedEntry[0])){
							this.proteins.get(splitedEntry[0]).add(splitedEntry[1]);
						}
						else{
							Set<String> proteinIds = new HashSet<>();
							proteinIds.add(splitedEntry[1]);
							this.proteins.put(splitedEntry[0], proteinIds);
						}
					}
				}
			}
		}
	}

	
	/**
	 * this method retrieve the ec-numbers for each reaction, adding them to the respective container (ReactionCI)
	 * 
	 * @throws InvalidBooleanRuleException
	 */
	private void getReactionsEnzymes() throws InvalidBooleanRuleException {

		ListOf<Reaction> sbmlreactions = super.document.getModel().getListOfReactions();
		for (int i = 0; i < sbmlreactions.size(); i++) {

			Reaction sbmlreaction = sbmlreactions.get(i);
			String reactionId = sbmlreaction.getId();

			if(reactionList.containsKey(reactionId)){
				
				Set<String> ec_numbers = new HashSet<>();
				Annotation annotation = sbmlreaction.getAnnotation();
				
				for(CVTerm cvterm : annotation.getListOfCVTerms()){
					
					if(cvterm.getQualifier().equals(Qualifier.BQB_IS)){
						
						for(String resource : cvterm.getResources()){
							
							if(resource.contains("ec-code"))
								ec_numbers.add(getDatabaseAndIdFromIdentifierURL(resource)[1]);
								
						}
					}
				}
				
				reactionList.get(reactionId).setEcNumbers(ec_numbers);
				
				if(ec_numbers.size()==1)
					reactionList.get(reactionId).setEc_number(ec_numbers.iterator().next());
			}
		}
	}


	/**
	 * This method fill a Map where the keys are the reactions IDs and the values are Maps with 
	 * reaction associated names of protein ids database sources (keys) and a set of associated proteins external database ids
	 * 
	 */
	private void buildReactionsProteinsInfo() {

		this.reactionsProteins = new HashMap<>();

		for(ReactionCI reaction : reactionList.values()){

			Map<String,Set<String>> proteinsInfo = new HashMap<>();

			for(String gene : reaction.getGenesIDs()){

				if(this.genesProteins.containsKey(gene)){

					for(String[] geneProtein : this.genesProteins.get(gene)){

						if(proteinsInfo.containsKey(geneProtein[0])){
							proteinsInfo.get(geneProtein[0]).add(geneProtein[1]);
						}
						else{
							Set<String> proteinExternalIDs = new HashSet<>();
							proteinExternalIDs.add(geneProtein[1]);
							proteinsInfo.put(geneProtein[0], proteinExternalIDs);
						}
					}
				}
			}

			this.reactionsProteins.put(reaction.getId(), proteinsInfo);
		}
	}
	
	
	/**
	 * @param url
	 * @return
	 */
	public static String[] getDatabaseAndIdFromIdentifierURL(String url){
		
		String entry = url.replace("http://identifiers.org/", "");

		String[] splitedEntry = entry.trim().split("/");
//		splitedEntry[0] -> database;
//		splitedEntry[1] -> id;

		return splitedEntry;
	}
	
	
	/**
	 * @return
	 */
	public Map<String,Map<String,Set<String>>> getReactionsProteinsInfo() {

		return this.reactionsProteins;
	}
	
	/**
	 * @return
	 */
	public Map<String, List<String[]>> getGenesProteinsInfo(){
		
		return this.genesProteins;
	}
	
	/**
	 * @return
	 */
	public Map<String, Set<String>> getProteinsByDatase(){
		
		return this.proteins;
	}

}
