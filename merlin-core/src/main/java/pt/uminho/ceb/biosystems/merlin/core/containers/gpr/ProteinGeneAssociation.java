package pt.uminho.ceb.biosystems.merlin.core.containers.gpr;

import java.util.ArrayList;
import java.util.List;


/**
 * @author ODias
 *
 */
public class ProteinGeneAssociation implements Comparable<ProteinGeneAssociation>{


	private String protein;
	private List<GeneAssociation> genes;


	/**
	 * @param protein
	 */
	public ProteinGeneAssociation(String protein) {
		super();
		this.setProtein(protein);
		this.genes = new ArrayList<>();
	}

	/**
	 * @param protein
	 * @param genes
	 */
	public ProteinGeneAssociation(String protein, List<GeneAssociation> genes) {
		super();
		this.setProtein(protein);
		this.setGenes(genes);
	}


	/**
	 * @param geneAssociation
	 */
	public void addGeneAssociation(GeneAssociation geneAssociation) {

		//if(!this.genes.contains(geneAssociation))
		int index = -1;
		for(int i = 0; i < this.getGenes().size(); i++) {

			if(this.getGenes().get(i).getGenes().equals(geneAssociation.getGenes())) {

				index=i;
				i=this.getGenes().size();
			}
		}

		if(index<0)
			this.genes.add(geneAssociation);
		else {
			
			this.getGenes().get(index).getModules().putAll(geneAssociation.getModules());
		}
	}

	/**
	 * @param geneAssociationList
	 */
	public void addAllGeneAssociation(List<GeneAssociation> geneAssociationList) {

		for(GeneAssociation geneAssociation : geneAssociationList) {

			this.addGeneAssociation(geneAssociation);
		}
	}

	/**
	 * @return the protein
	 */
	public String getProtein() {
		return protein;
	}

	/**
	 * @param protein the protein to set
	 */
	public void setProtein(String protein) {
		this.protein = protein;
	}

	/**
	 * @return the genes
	 */
	public List<GeneAssociation> getGenes() {
		return genes;
	}

	/**
	 * @param genes the genes to set
	 */
	public void setGenes(List<GeneAssociation> genes) {
		this.genes = genes;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProteinGeneAssociation ["
				+ (protein != null ? "protein=" + protein + ",\n\t" : "\n")
				+ (genes != null ? "genes=" + genes : "") + "]";
	}

	@Override
	public boolean equals(Object pga){

		if(ProteinGeneAssociation.class.isInstance(pga)) {
			ProteinGeneAssociation p = (ProteinGeneAssociation) pga;

			if(this.protein.equalsIgnoreCase(p.getProtein())){

				if(this.genes.equals(p.getGenes())) {

					return true;
				}
			}
		}

		return false;
	}

	@Override
	public int compareTo(ProteinGeneAssociation pga) {

		if(this.equals(pga))
			return 0;

		int nameDiff = this.protein.compareToIgnoreCase(pga.getProtein());

		//if(nameDiff != 0) 
		{

			return nameDiff;
		}

		//proteins are equal compara genes
		//return this.genes.compareTo(pga.getGenes());
	}
}
