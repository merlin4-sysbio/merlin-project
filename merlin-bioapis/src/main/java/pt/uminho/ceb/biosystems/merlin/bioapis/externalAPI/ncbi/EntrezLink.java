package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.ELinkResult;

/**
 * @author ODias
 *
 */
public class EntrezLink {

	private EntrezService entrezService;

	/**
	 */
	public EntrezLink() {
		
		EntrezServiceFactory entrezServiceFactory = new EntrezServiceFactory("https://eutils.ncbi.nlm.nih.gov/entrez/eutils", false);
		this.entrezService = entrezServiceFactory.build();
	}


	/**
	 * @param list_of_ids
	 * @param originDB
	 * @param objectiveDB
	 * @param queryResponseConcatenationSize
	 * @return
	 */
	public List<List<String>> getLinksList(List<String> list_of_ids, NcbiDatabases originDB, NcbiDatabases objectiveDB, int queryResponseConcatenationSize){
		try
		{
			List<String> ids_link = new ArrayList<String>();
			
			List<String> listOfGenomeIDs = new ArrayList<String>();
			int index=0;
			String genome_ids="";
			
			for(int i=0; i<list_of_ids.size();i++) {
				
				if(index>0)
					genome_ids=genome_ids.concat(",");

				genome_ids=genome_ids.concat(list_of_ids.get(i));
				index++;
				
				if(index>999) {
					
					listOfGenomeIDs.add(genome_ids);// +"+AND+refseq[Filter]");
					genome_ids="";
					index=0;
				}
			}
			
			if(!genome_ids.isEmpty()) {
				
				listOfGenomeIDs.add(genome_ids);//+"+AND+refseq[Filter]");
			}

			for(int index_=0;index_<listOfGenomeIDs.size();index_++) {
				
				ELinkResult eLinkResult = entrezService.eLink(objectiveDB, originDB, listOfGenomeIDs.get(index_), "xml");
				
				for (int i = 0; i < eLinkResult.linkSet.size(); i++) {
					
					for (int j = 0; j < eLinkResult.linkSet.get(i).linkSetDb.size(); j++) {
						
						for (int k = 0; k <  eLinkResult.linkSet.get(i).linkSetDb.get(j).link.size(); k++) {
							
							String org =  eLinkResult.linkSet.get(i).linkSetDb.get(j).link.get(k).id;
							ids_link.add(org);
						}
					}
				}
			}

			List<List<String>> linkList = new ArrayList<List<String>>();
			List<String> links = new ArrayList<String>();
			index=0;
			for(int i=0; i<ids_link.size();i++)
			{
				links.add(ids_link.get(i));
				index++;
				if(index>queryResponseConcatenationSize)
				{
					linkList.add(links);
					links = new ArrayList<String>();
					index=0;
				}
			}
			if(!links.isEmpty())
			{
				linkList.add(links);
			}
			return linkList;
		}
		catch(Exception e) {
			
			e.printStackTrace();
			throw e;
		}
	}


	public enum KINGDOM{
		Bacteria,
		Eukaryota,
		Archaea,
		Viruses,
		Viroids,
		All
	}
}
