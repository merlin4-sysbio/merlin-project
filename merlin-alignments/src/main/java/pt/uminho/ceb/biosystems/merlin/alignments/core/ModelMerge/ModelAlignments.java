package pt.uminho.ceb.biosystems.merlin.alignments.core.ModelMerge;

import java.util.concurrent.ConcurrentLinkedQueue;

import pt.uminho.ceb.biosystems.merlin.core.containers.alignment.AlignmentContainer;

/**
 * @author amaromorais
 *
 */
public interface ModelAlignments extends Runnable {
	
	/**
	 * 
	 */
	@Override
	public void run();
	
	/**
	 * 
	 */
	public void buildAlignmentCapsules();
	
	/**
	 * 
	 */
	public ConcurrentLinkedQueue<AlignmentContainer> getAlignmentsCapsules();

	
}
