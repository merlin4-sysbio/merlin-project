package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.datatypes;

import java.util.concurrent.ConcurrentHashMap;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

public class NcbiData {

	private ConcurrentHashMap<String, String> locus_Tag;
	private ConcurrentHashMap<String, AbstractSequence<?>> sequences;

	/**
	 * @param locus_Tag
	 * @param sequences
	 */
	public NcbiData(ConcurrentHashMap<String, String> locus_Tag, ConcurrentHashMap<String, AbstractSequence<?>> sequences) {

		this.locus_Tag = locus_Tag;
		this.sequences = sequences;
	}
	
	/**
	 * @param name
	 * @param locus
	 */
	public void addLocusTag(String name, String locus){

		this.locus_Tag.put(name, locus);
	}

	/**
	 * @param name
	 * @param sequence
	 */
	public void addSequence(String name, ProteinSequence sequence){

		this.sequences.put(name, sequence);
	}
}
