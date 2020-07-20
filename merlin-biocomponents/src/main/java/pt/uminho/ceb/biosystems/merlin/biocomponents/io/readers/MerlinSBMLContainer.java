package pt.uminho.ceb.biosystems.merlin.biocomponents.io.readers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot.UniProtAPI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.CompartmentCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.GeneCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.MetaboliteCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionConstraintCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.StoichiometryValueCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.interfaces.IContainerBuilder;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Field;

public class MerlinSBMLContainer extends Container{

	private static final long serialVersionUID = 1L;
	private Map<String,Map<String,Set<String>>> reactionsProteinsInfo;
	private Map<String,List<Pair<String,Set<String>>>> genesProteins;

	
	public MerlinSBMLContainer(IContainerBuilder reader, boolean throwerros) throws IOException {
		super(reader, throwerros);
		this.verifyReactionsReversibility();
	}
	
	public MerlinSBMLContainer(IContainerBuilder reader) throws IOException {
		this(reader, true);
	}
	
	public MerlinSBMLContainer(MerlinSBML3Reader sbml3Reader) throws IOException {
		this(sbml3Reader, true);
		
		this.reactionsProteinsInfo = sbml3Reader.getReactionsProteinsInfo();
	}
		
	
	/**
	 * 
	 */
	private void verifyReactionsReversibility() {
		
		boolean isReversible;
		
		;
		
		for(String reac : this.reactions.keySet()){
			
			ReactionConstraintCI bounds = this.defaultEC.get(reac);
			
			if(bounds.getLowerLimit()<0 && bounds.getUpperLimit()>0)
				isReversible = true;
			else
				isReversible = false;
			
			this.reactions.get(reac).setReversible(isReversible);
		}
	}
	
	//Method copied from MEW Container and corrected
	@Override
	public Set<String> getAllEc_Numbers() {
		
		Set<String> ret = new TreeSet<String>();
		
		for (ReactionCI reac : reactions.values()) {
			
			String ec_number = reac.getEc_number();
			if (!ec_number.equals(""))
				ret.add(ec_number);
		}
		return ret;
	}
	
	//Method copied from MEW Container and corrected
	@Override
	public Map<String, Set<String>> getECNumbers(){
		
		Map<String, Set<String>> ret = new HashMap<String, Set<String>>();

		for(ReactionCI reac: reactions.values()){

//			String ec_number = reac.getEc_number().trim();			

			if(reac.getEcNumbers()!=null){

				for(String ec_number : reac.getEcNumbers()){
					if(!ec_number.equals("")){

						Set<String> reactions = ret.get(ec_number);
						if(reactions == null)
							reactions = new TreeSet<String>(); 
						reactions.add(reac.getId());
						ret.put(ec_number, reactions);
					}
				}
			}
		}
		return ret;
	}
	
	
	/**
	 * @param genesProteinsInfo
	 * @return
	 */
	private Map<String,List<Pair<String,Set<String>>>> processGenesProteinsInfo(Map<String,List<String[]>> genesProteinsInfo){
		
		Map<String,List<Pair<String,Set<String>>>> res = new HashMap<>();
		
		for(String geneID : genesProteinsInfo.keySet()){
			
			List<Pair<String,Set<String>>> proteins = new ArrayList<>();
			
			for(String[] proteinInfo : genesProteinsInfo.get(geneID)){
				
				if(proteinInfo[0].equals("uniprot")){
					
					String uniprotID = proteinInfo[1];

					UniProtEntry uniprot = UniProtAPI.getEntryFromUniProtID(uniprotID, 0);

					if(uniprot!=null){
						
						Set<String> ec_numbers = new HashSet<>();
						
						List<String> ecList = UniProtAPI.getECnumbers(uniprot);
						
						System.out.println("geneID: "+geneID+"\tproteinInfo: "+Arrays.asList(proteinInfo)+"\tEcNumbers: "+ecList);
						
						ec_numbers.addAll(ecList);

						String proteinName = "";

						for(Field field : uniprot.getProteinDescription().getRecommendedName().getFields()){

							if(field.getType().getValue().equals("Full"))
								proteinName = field.getValue();
						}
						
						Pair<String,Set<String>> protein = new Pair<String, Set<String>>(proteinName, ec_numbers);
						proteins.add(protein);

					}
				}
				
			}
			
			res.put(geneID, proteins);
		}
		return res;
	}
	
	
	/**
	 * @return
	 */
	public Map<String,Map<String,Set<String>>> getReactionsProteinsInfo(){
		
		if(this.reactionsProteinsInfo == null)
			this.reactionsProteinsInfo = new HashMap<>();
		
		return this.reactionsProteinsInfo;
	}

	/**
	 * @return the genesProteins
	 */
	public Map<String,List<Pair<String,Set<String>>>> getGenesProteins() {
		return genesProteins;
	}

//	/**
//	 * @return the proteinsByDatabase
//	 */
//	public Map<String, Set<String>> getProteinsByDatabase() {
//		return proteinsByDatabase;
//	}

}
