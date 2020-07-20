package pt.uminho.ceb.biosystems.merlin.core.datatypes.model.compartments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.CompartmentContainer;
import pt.uminho.ceb.biosystems.merlin.core.interfaces.ICompartmentResult;
import pt.uminho.ceb.biosystems.merlin.core.utilities.compartments.CompartmentsUtilities;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


public class AnnotationCompartmentsPSort3 implements Serializable, ICompartmentResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer geneID;
	private String locusTag;
	private Map<String, CompartmentContainer> compartmentsInfo;
	private List<Pair<String, Double>> compartments;
	
	/**
	 * @param geneID
	 */
	public AnnotationCompartmentsPSort3(Integer geneID) {
		this.setGeneID(geneID);
		this.setCompartments(new ArrayList<Pair<String, Double>>());
		this.compartmentsInfo = new HashMap<String, CompartmentContainer>();
	}
	
	/**
	 * @param compartmentID
	 * @param score
	 */
	public void addCompartment(String compartmentAbbreviation, double score){
		this.compartments.add(new Pair<String, Double>(compartmentAbbreviation,score));
	}

	/**
	 * @return the compartments
	 */
	public List<Pair<String, Double>> getCompartments() {
		return compartments;
	}

	/**
	 * @param compartments the compartments to set
	 */
	public void setCompartments(List<Pair<String, Double>> compartments) {
		this.compartments = compartments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PSort3Result ["
				+ (this.geneID != null ? "geneID=" + this.geneID + ", " : "")
				+ (this.compartments != null ? "compartments=" + this.compartments : "")
				+ "]";
	}

	@Override
	public void setGeneLocusTag(String locus) {
		this.locusTag = locus;
		
	}

	@Override
	public String getGeneLocusTag() {
		return this.locusTag;
	}

	@Override
	public void setGeneID(Integer id) {
		this.geneID = id;
		
	}

	@Override
	public Integer getGeneID() {
		return this.geneID;
	}
	
	@Override
	public void addCompartmentInfo(String abb, String name) {
		
		String standardName = CompartmentsUtilities.parseAbbreviation(abb);
		
		if(!standardName.equals(abb))
			name = standardName;
		
		if(!this.compartmentsInfo.containsKey(abb))
			this.compartmentsInfo.put(abb, new CompartmentContainer(name, abb));
	}
	
	@Override
	public String getCompartmentNameByAbb(String abb) {
		
		if(this.compartmentsInfo.containsKey(abb))
			return this.compartmentsInfo.get(abb).getName();
		
		return abb;
	}
	
}
