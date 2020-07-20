package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.biojava.nbio.core.sequence.template.AbstractSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.datatypes.NcbiData;

/**
 * @author ODias
 *
 */
public class RunnableSequencesRetriever implements Runnable {


	private ConcurrentLinkedQueue<List<String>> queryArray;
	private String sourceDB;
	private ConcurrentHashMap<String,String> locus_Tag;
	private ConcurrentHashMap<String, AbstractSequence<?>> sequences;
	
	private static final Logger logger = LoggerFactory.getLogger(RunnableSequencesRetriever.class);

	/**
	 * @param queryArray
	 * @param locus_Tag
	 * @param sequences
	 * @param sourceDB
	 */
	public RunnableSequencesRetriever(ConcurrentLinkedQueue<List<String>> queryArray, ConcurrentHashMap<String,String> locus_Tag, ConcurrentHashMap<String, AbstractSequence<?>> sequences, String sourceDB) {
		
		this.queryArray = queryArray;
		this.sourceDB = sourceDB;
		this.locus_Tag = locus_Tag;
		this.sequences = sequences;
	}


	@Override
	public void run() {

		NcbiData ncbiData = new NcbiData(this.locus_Tag, this.sequences);

		while(this.queryArray.size()>0) {

			List<String> query = this.queryArray.poll();

			try {

				EntrezFetch entrezFetch = new EntrezFetch();
				ncbiData = entrezFetch.getSequences(query, this.sourceDB, ncbiData);
			}
			catch (RemoteException e){

				if(query.size()>50) {

					logger.info("Reducing concatenation size to:\t"+50);
					List<String> list = new ArrayList<String>();
					for(int i = 0; i<query.size();i++) {

						list.add(query.get(i));
						if(list.size()>49) {

							queryArray.offer(list);
							list = new ArrayList<String>();
						}
					}
					if(list.size()>0) {

						queryArray.offer(list);
					}
				}
				else {

					logger.info("Reducing concatenation size to:\t"+query.size()/2);
					int half = query.size()/2;
					int half_1 =(query.size()/2)+1;
					queryArray.offer(query.subList(0, half));
					queryArray.offer(query.subList(half_1,query.size()));
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
			System.gc();
		}
		
	}


}
