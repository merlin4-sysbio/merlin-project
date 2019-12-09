/**
 * 
 */
package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.uniprot;

import java.util.List;

import uk.ac.ebi.kraken.interfaces.uniprot.NcbiTaxon;

/**
 * @author ODias
 *
 */
public class TaxonomyContainer {

	private String speciesName;
	private List<NcbiTaxon> taxonomy;


	/**
	 * 
	 */
	public TaxonomyContainer() {

	}
	
	/**
	 * @param genus
	 */
	public TaxonomyContainer(String genus) {

		this.setSpeciesName(genus);		
	}
	
	public TaxonomyContainer(String genus, List<NcbiTaxon> taxonomy) {

		this.setSpeciesName(genus);		
		this.setTaxonomy(taxonomy);
	}

	/**
	 * @return the genus
	 */
	public String getSpeciesName() {
		return speciesName;
	}

	/**
	 * @param genus the genus to set
	 */
	public void setSpeciesName(String genus) {
		this.speciesName = genus;
	}

	/**
	 * @return the taxonomy
	 */
	public List<NcbiTaxon> getTaxonomy() {
		return taxonomy;
	}

	/**
	 * @param taxonomy the taxonomy to set
	 */
	public void setTaxonomy(List<NcbiTaxon> taxonomy) {
		this.taxonomy = taxonomy;
	}

	@Override
	public String toString() {
		return "TaxonomyContainer [genus=" + speciesName + ", taxonomy=" + taxonomy
				+ "]";
	}

}
