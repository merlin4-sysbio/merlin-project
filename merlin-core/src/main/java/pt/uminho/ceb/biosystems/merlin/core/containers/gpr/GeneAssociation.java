package pt.uminho.ceb.biosystems.merlin.core.containers.gpr;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GeneAssociation {

	private List<String> genes;
	private Map <String, String> locusTags;
	private Map<String, ModuleCI> modules;

//	public GeneAssociation (String module, String name) {
//
//		this.genes = new ArrayList<>();
//		
//	}
	
	/**
	 * @param mic
	 */
	public GeneAssociation (ModuleCI mic) {

		this.genes = new ArrayList<>();
		this.modules = new HashMap<>();
		this.locusTags = new HashMap<>();
		this.modules.put(mic.getModule(), mic);
	}

	/**
	 * @param gene
	 */
	public void addGene(String gene) {

		this.genes.add(gene);
	}
	
    /**
     * @param gene
     */
    public void addAllGenes(Set<String> genes) {
 
        this.genes.addAll(genes);
    }
	
	/**
	 * @param gene
	 */
	public void setLocusTag(String gene, String locus) {

		this.locusTags.put(gene, locus);
	}

	/**
	 * @return the genes
	 */
	public List<String> getGenes() {
		return genes;
	}

	/**
	 * @param genes the genes to set
	 */
	public void setGenes(List<String> genes) {
		this.genes = genes;
	}

	/**
	 * @return the modules
	 */
	public Map<String, ModuleCI> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(Map<String, ModuleCI> modules) {
		this.modules = modules;
	}

	/**
	 * @return the locusTags
	 */
	public Map <String, String> getLocusTags() {
		return locusTags;
	}

//	/**
//	 * @param locusTags the locusTags to set
//	 */
//	public void setLocusTags(Map <String, String> locusTags) {
//		this.locusTags = locusTags;
//	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GeneAssociation ["
				+ (genes != null ? "genes=" + genes + ", " : "")
				+ (modules != null ? "modules=" + modules : "") + "]";
	}

	
}
