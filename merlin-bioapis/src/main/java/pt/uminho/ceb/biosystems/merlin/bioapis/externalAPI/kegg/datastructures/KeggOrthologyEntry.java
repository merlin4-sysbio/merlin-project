package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.datastructures;

import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.utilities.external.ExternalRef;
import pt.uminho.ceb.biosystems.merlin.utilities.external.ExternalRefSource;

public class KeggOrthologyEntry extends ExternalRef {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String id;
	protected List<String> names;
	protected String definition;
	protected List<String> pathways;
	protected List<String> modules;
	protected List<String> disease;
	protected List<String> brite;
	protected Map<String, List<String>> dbAssociations; //Other DBs
	protected List<String> genes;
	protected List<String> references;
	
	public KeggOrthologyEntry(String id, List<String> names, String definition,
			List<String> pathways, List<String> modules, List<String> disease,
			List<String> brite, Map<String, List<String>> dbAssociations,
			List<String> genes, List<String> references) {
		super(definition, definition, ExternalRefSource.KEGG_ORTHOLOGY);
		this.id = id;
		this.names = names;
		this.definition = definition;
		this.pathways = pathways;
		this.modules = modules;
		this.disease = disease;
		this.brite = brite;
		this.dbAssociations = dbAssociations;
		this.genes = genes;
		this.references = references;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public List<String> getPathways() {
		return pathways;
	}

	public void setPathways(List<String> pathways) {
		this.pathways = pathways;
	}

	public List<String> getModules() {
		return modules;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public List<String> getDisease() {
		return disease;
	}

	public void setDisease(List<String> disease) {
		this.disease = disease;
	}

	public List<String> getBrite() {
		return brite;
	}

	public void setBrite(List<String> brite) {
		this.brite = brite;
	}

	public Map<String, List<String>> getDbAssociations() {
		return dbAssociations;
	}

	public void setDbAssociations(Map<String, List<String>> dbAssociations) {
		this.dbAssociations = dbAssociations;
	}

	public List<String> getGenes() {
		return genes;
	}

	public void setGenes(List<String> genes) {
		this.genes = genes;
	}

	public List<String> getReferences() {
		return references;
	}

	public void setReferences(List<String> references) {
		this.references = references;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
