package pt.uminho.ceb.biosystems.merlin.core.interfaces;

import java.util.List;

import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;


/**
 * @author Oscar Dias
 *
 */
public interface ICompartmentResult {

	public void setGeneLocusTag(String locus);
	
	public String getGeneLocusTag();
	
	public void setGeneID(Integer id);
	
	public Integer getGeneID();
	
	public void addCompartment(String compartmentAbbreviation, double score);

	public List<Pair<String, Double>> getCompartments();
	
	public void addCompartmentInfo(String abb, String name);
	
	public String getCompartmentNameByAbb(String abb);
			
}
