package pt.uminho.ceb.biosystems.merlin.gui.operations.modelTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.ei.aibench.workbench.Workbench;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot.UniProtAPI;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.CreateGenomeFile;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.EntrezFetch;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.EntrezService;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.EntrezServiceFactory;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.NcbiDatabases;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.GBFeature;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.GBQualifier;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.containers.GBSet;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.WorkspaceAIB;
import pt.uminho.ceb.biosystems.merlin.gui.datatypes.model.ModelGenesAIB;
import pt.uminho.ceb.biosystems.merlin.gui.utilities.AIBenchUtils;
import pt.uminho.ceb.biosystems.merlin.processes.WorkspaceProcesses;
import pt.uminho.ceb.biosystems.merlin.services.model.ModelGenesServices;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;



@Operation(name="Update Locus_tags",description="Update genes locus_tag")
public class UpdateLocusTags {

	private WorkspaceAIB workspace;
	private Long taxonomyID;
	private Boolean searchLocusOnline = false;

	/**
	 * 
	 * @param Search locustag in online databases.
	 */
	@Port(direction=Direction.INPUT, name="search locus tag online", advanced = true, order=1)
	public void setDatase(boolean searchLocusOnline) {
		this.searchLocusOnline = searchLocusOnline;
	}


	/**
	 * @param taxonomyID of the organism
	 * @throws Exception 
	 * @throws IOException 
	 */
	@Port(direction=Direction.INPUT, name="workspace", order=2)
	public void setDatabase(WorkspaceAIB workspace) throws Exception {


		//		if(!gbFileName.equals("") && Project.isFaaFiles(databaseName, taxonomyID)){

		this.taxonomyID = workspace.getTaxonomyID();
		this.workspace = workspace;


		List<String> sequence_ids = new ArrayList<String>(ModelGenesServices.getGeneIds(workspace.getName()).values());
		Map<String,String> locusTags = getLocusTagBySeqIds(sequence_ids, this.workspace.getName(), this.taxonomyID);

		if(!locusTags.isEmpty()) {
			for(Entry<String, String> entry : locusTags.entrySet()) {
				
				ModelGenesServices.updateGenesLocusByQuery(workspace.getName(), entry.getKey(), entry.getValue());
			}

		}

		AIBenchUtils.updateView(workspace.getName(), ModelGenesAIB.class);
		
		Workbench.getInstance().info("locus tag update complete!");
	}

	/**
	 * @param sequence_ids
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getLocusTagBySeqIds(List<String> idsToSearch, String databaseName, Long taxonomyID) throws Exception {

		Map<String,String> toUpdate = new HashMap<>();
		Set<String> idsToRemove =  new HashSet<>();

		//GenBank/RefSeq fasta file (.gbff) Search
		File genBankFile;
		if(!idsToSearch.isEmpty() && idsToSearch!=null && (genBankFile = WorkspaceProcesses.checkGenBankFile(databaseName, taxonomyID))!=null){

			Map<String, String> genBankLocusTags = CreateGenomeFile.getLocusTagFromGenBankFastFile(genBankFile);

			for(String seqID : idsToSearch){

				if(genBankLocusTags.containsKey(seqID)){
					toUpdate.put(seqID, genBankLocusTags.get(seqID));
					idsToRemove.add(seqID);
				}
			}

			idsToSearch.removeAll(idsToRemove);
			idsToRemove = new HashSet<>();
		}

		//UniProt Search
		if(this.searchLocusOnline && !idsToSearch.isEmpty() && idsToSearch!=null){

			AtomicBoolean cancel = new AtomicBoolean(false);
			Map<String,List<UniProtEntry>> uniProtEntries = UniProtAPI.getUniprotEntriesFromRefSeq(idsToSearch, cancel, 0);

			for(String seqID : uniProtEntries.keySet()){

				if(uniProtEntries.get(seqID)!=null && !uniProtEntries.get(seqID).isEmpty()){

					if(uniProtEntries.get(seqID).size()==1){

						UniProtEntry entry = uniProtEntries.get(seqID).get(0);

						toUpdate.put(seqID, UniProtAPI.getLocusTag(entry));
						idsToSearch.remove(seqID);
					}
				}
			}
		}

		//NCBI Search
		if(this.searchLocusOnline && !idsToSearch.isEmpty() && idsToSearch!=null){

			EntrezServiceFactory entrezServiceFactory = new EntrezServiceFactory("https://eutils.ncbi.nlm.nih.gov/entrez/eutils", false);
			EntrezService entrezService = entrezServiceFactory.build();

			for(String seqID : idsToSearch){

				if(seqID.matches("^[a-zA-Z]{2}_.+")){

					GBSet gbSet = entrezService.eFetch(NcbiDatabases.protein, seqID, "xml");
					List<GBFeature> features = gbSet.gBSeq.get(0).features;

					for(GBFeature feature : features) {

						if(feature.featureKey.equals("CDS")){

							for(GBQualifier qualifier : feature.qualifiers){

								if(qualifier.name.equals("locus_tag")){
									toUpdate.put(seqID, qualifier.value);
									idsToRemove.add(seqID);
								}
							}
						}
					}
				}
			}

			idsToSearch.removeAll(idsToRemove);
		}

		//NCBI E-utilities Search
		if(this.searchLocusOnline && !idsToSearch.isEmpty() && idsToSearch!=null){

			Set<String> subSet = new HashSet<>();
			EntrezFetch e = new EntrezFetch();

			int i = 0;
			for(String seqId : idsToSearch){

				if((i + 1) % 500 == 0){
					toUpdate.putAll(e.getLocusFromID(subSet,100));

					subSet = new HashSet<>();
				}

				if(seqId.matches("^[a-zA-Z]{3}\\d+\\.\\d{1}")){

					subSet.add(seqId);
					i++;
				}
			}

			if(!subSet.isEmpty() && subSet!=null)
				toUpdate.putAll(e.getLocusFromID(subSet,100));
		}

		//		System.out.println("toUpdate--->"+toUpdate);
		//		System.out.println("toUpdate.size--->"+toUpdate.size());
		//		System.out.println("idsToSearch--->"+idsToSearch);
		//		System.out.println("idsToSearch.size--->"+idsToSearch.size());

		return toUpdate;
	}

}
