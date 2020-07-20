/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.processes.model.kegg;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot.UniProtAPI;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.KeggAPI;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;

/**
 * @author ODias
 *
 */
public class ValidateData {


	/**
	 * @throws ServiceException 
	 * @throws RemoteException 
	 * 
	 */
	public ValidateData(){

	}

	/**
	 * @param ecnumbers
	 * @throws Exception 
	 */
	public void kegg_ecnumber_validation(List<String> ecnumbers) throws Exception {
		while(!ecnumbers.isEmpty())
		{
			int i=0;
			String ecnumber = "ec:"+ecnumbers.remove(i);
			i++;
			while(i<99 && !ecnumbers.isEmpty())
			{
				ecnumber+=" ec:"+ecnumbers.remove(i);
				i++;
			}
			this.verifyECNumbers_kegg(ecnumber);
			List<String> temp = new ArrayList<String>(ecnumbers);
			ecnumbers=temp;
		}
	}

	/**
	 * @param uniprot_ids
	 */
	public void validate_organisms_by_uniprot_id(List<String> uniprot_ids){
		for(String uniprot_id : uniprot_ids)
		{
			this.getOrganism_id(uniprot_id);
		}
	}
	
	/**
	 * @param uniprot_ids
	 */
	public void validate_organisms_taxonomy_by_uniprot_id(List<String> uniprot_ids){
		for(String uniprot_id : uniprot_ids)
		{
			this.get_organism_taxonomy_id(uniprot_id);
		}
	}
	
	/**
	 * @param uniprot_ids
	 * @throws ServiceException 
	 */
	public void validate_organisms_by_locus_tag(List<String> loci) throws ServiceException{
		for(String locus : loci)
			this.getOrganism_id_locus(locus);
	}
	

	/**
	 * @param genes
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public void uniprot_genes_validation(List<String> genes) throws ServiceException {
		for(String gene:genes)
			verify_genes_uniprot(gene);
	}

	/**
	 * @param ecnumbers
	 * @throws Exception 
	 */
	private void verifyECNumbers_kegg(String ecnumbers) throws Exception{

		Map<String, List<String>> printer = KeggAPI.getEnzymeInfo(ecnumbers);

	}

	/**
	 * @param gene
	 * @throws ServiceException 
	 */
	private void verify_genes_uniprot(String gene) throws ServiceException{

		UniProtEntry entry = UniProtAPI.getEntry(gene,0);

		if(entry==null)
			entry = UniProtAPI.getEntryFromUniProtID(gene,0);
	}


	/**
	 * @param uniprot_id
	 */
	private void getOrganism_id(String uniprot_id){
		UniProtEntry entry = UniProtAPI.getEntryFromUniProtID(uniprot_id,0);
	}
	
	/**
	 * @param uniprot_id
	 */
	private void get_organism_taxonomy_id(String uniprot_id){
		UniProtEntry entry = UniProtAPI.getEntryFromUniProtID(uniprot_id,0);
		String[] taxon = UniProtAPI.getOrganismTaxa(entry);
	}
	
	/**
	 * @param uniprot_id
	 * @throws ServiceException 
	 */
	private void getOrganism_id_locus(String locus) throws ServiceException{
		UniProtEntry entry = UniProtAPI.getEntry(locus,0);
	}
	
	/**
	 * @param uniprot_id
	 * @throws ServiceException 
	 */
	public void get_organism_taxonomy_id_locus(String locus) throws ServiceException{
		UniProtEntry entry = UniProtAPI.getEntry(locus,0);
		String[] taxon = UniProtAPI.getOrganismTaxa(entry);
	}

}
