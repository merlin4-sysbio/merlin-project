package pt.uminho.ceb.biosystems.merlin.processes.model.compartments;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ncbi.EntrezLink.KINGDOM;
import pt.uminho.ceb.biosystems.merlin.core.utilities.compartments.CompartmentsEnumerators.STAIN;
import pt.uminho.ceb.biosystems.merlin.core.utilities.compartments.CompartmentsUtilities;

/**
 * @author ODias
 *
 */
public class CompartmentsProcesses {

	private KINGDOM kingdom;
	private STAIN stain;
	private boolean hasCellWall=false;
	private String interiorCompartment;
	//private Integer interiorCompartmentID;
	private Set<Integer> ignoreCompartmentsID;
	private boolean processCompartmentsInitiated = false;
	private Map<Integer, String> compartmentsAbb_ids;
	private Map<String, Integer> idCompartmentAbbIdMap;
	private static final List<String> MEMBRANES_ABBREVIATIONS = Arrays.asList("cytmem", "cellw", "outme", "pla", "plas");

//	/**
//	 */
//	public CompartmentsProcesses() {
//
//	}

	/**
	 * @param interiorCompartment
	 */
	/**
	 * @param interiorCompartment
	 * @param compartmentsAbb_ids
	 * @param idCompartmentAbbIdMap
	 */
	public CompartmentsProcesses(String interiorCompartment, Map<Integer, String> compartmentsAbb_ids, Map<String, Integer> idCompartmentAbbIdMap) {

		this.interiorCompartment = interiorCompartment;
		this.compartmentsAbb_ids= compartmentsAbb_ids;
		this.idCompartmentAbbIdMap = idCompartmentAbbIdMap;
		this.ignoreCompartmentsID = new HashSet<>();
	}

	public Map<Integer, String> getCompartmentsAbb_ids() {
		return compartmentsAbb_ids;
	}

	public Map<String, Integer> getIdCompartmentAbbIdMap() {
		return idCompartmentAbbIdMap;
	}

	/**
	 * @param existingCompartments
	 */
	public CompartmentsProcesses(Set<String> existingCompartments) {

		this.initProcessCompartments(existingCompartments);
	}

	/**
	 * @param existingCompartments
	 */
	public void initProcessCompartments(Set<String> existingCompartments) {

		if(!this.isProcessCompartmentsInitiated()) {

			this.stain = STAIN.gram_positive;

			for(String compartment : existingCompartments) {

				if(compartment.equalsIgnoreCase("perip") || compartment.equalsIgnoreCase("periplasm") || compartment.equalsIgnoreCase("periplasmic")) {

					this.kingdom = KINGDOM.Bacteria;
					this.stain = STAIN.gram_negative;
				}

				if(compartment.equalsIgnoreCase("cellw") || compartment.equalsIgnoreCase("cellwall") ) {

					this.setHasCellWall(true);
				}

				if(compartment.equalsIgnoreCase("pla") || compartment.equalsIgnoreCase("plas")) {

					this.kingdom = KINGDOM.Eukaryota;
					this.stain = null;
				}
			}

			this.setProcessCompartmentsInitiated(true);
		}
	}


//	/**
//	 * @param metaboliteMap
//	 * @param compartment
//	 * @return
//	 * @throws Exception 
//	 */
//	public String processTransportCompartments(String localisation, String compartment) throws Exception {
//
//		try {
//
//			if (this.isProcessCompartmentsInitiated()) {
//
//				if (localisation.equalsIgnoreCase("out") || localisation.equalsIgnoreCase(CompartmentsUtilities.DEFAULT_MEMBRANE.toString()))					
//					return CompartmentsUtilities.getOutsideMembrane(compartment.toLowerCase(), this.stain);
//				else
//
//					return CompartmentsUtilities.getInsideMembrane(compartment.toLowerCase(), this.stain);
//			} else {
//
//				throw new Exception("Compartments processing not initiated!");
//			} 
//		} catch (Exception e) {
//
//			System.out.println(localisation+" "+compartment);
//			throw e;
//		}
//	}
	
	/**
	 * @param abb
	 * @return
	 */
	public boolean checkIfValidMembrane(String abb) {
		
		if(abb != null)
			return MEMBRANES_ABBREVIATIONS.contains(abb);
		return false;
	}

	/**
	 *  method for parsing compartmtents for metabolic reactions
	 * @param list
	 * @param ignoreList
	 * @return
	 * @throws Exception
	 */
	public Set<Integer> parseCompartments(List<Integer> list, List<String> ignoreList) throws Exception {
		
		Set<Integer> compartments = new HashSet<>();

		if (this.processCompartmentsInitiated) {
			
			try {

				for(int compartment: list) {
					
					String abb = compartmentsAbb_ids.get(compartment).toLowerCase();
					
					if(abb.equalsIgnoreCase("cytmem")) {

						compartments.add(idCompartmentAbbIdMap.get(interiorCompartment.toLowerCase()));

						if(stain.equals(STAIN.gram_negative))
							compartments.add(idCompartmentAbbIdMap.get("perip"));
						else
							if(!hasCellWall)
								compartments.add(idCompartmentAbbIdMap.get("extr"));
					}
					
					else if(ignoreList.contains(abb.toLowerCase())) {

						compartments.add(idCompartmentAbbIdMap.get(interiorCompartment.toLowerCase()));
						this.ignoreCompartmentsID.add(compartment);
					} 
					
					else if(abb.equalsIgnoreCase("cellw")) {

						compartments.add(idCompartmentAbbIdMap.get("extr"));
					}
					else if(abb.equalsIgnoreCase("outme")) {

						compartments.add(idCompartmentAbbIdMap.get("perip"));
						compartments.add(idCompartmentAbbIdMap.get("extr"));
					}
					else if(abb.equalsIgnoreCase("pla") || abb.equalsIgnoreCase("plas")) {

						compartments.add(idCompartmentAbbIdMap.get(interiorCompartment.toLowerCase()));
						compartments.add(idCompartmentAbbIdMap.get("extr"));
					} 
					else if (abb.contains("me")) {

						for(String newAbb : CompartmentsUtilities.getOutsideMembranes(abb, stain)) 
							compartments.add(idCompartmentAbbIdMap.get(newAbb.toLowerCase()));
					}
					else if(abb.equalsIgnoreCase("unkn")) {

						compartments.add(idCompartmentAbbIdMap.get(interiorCompartment.toLowerCase()));
					} 
				
					else {

						compartments.add(compartment);
					}
					
				}

//				System.out.println("compartments----->"+compartments);
				
				return compartments;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
		
			throw new Exception("Compartments processing not initiated!");
		}
		return compartments;
	}

	/**
	 * @param compartmentID
	 * @return
	 * @throws Exception 
	 */
	public String getOutside(String compartmentID) throws Exception {

		if(this.isProcessCompartmentsInitiated())
			return CompartmentsUtilities.getOutside(this.stain, compartmentID);
		else
			throw new Exception("Compartments processing not initiated!");
	}

	/**
	 * @return the kingdom
	 */
	public KINGDOM getKingdom() {
		return kingdom;
	}

	/**
	 * @param kingdom the kingdom to set
	 */
	public void setKingdom(KINGDOM kingdom) {
		this.kingdom = kingdom;
	}

	/**
	 * @return the stain
	 */
	public STAIN getStain() {
		return stain;
	}

	/**
	 * @param stain the stain to set
	 */
	public void setStain(STAIN stain) {
		this.stain = stain;
	}



	/**
	 * @return the interiorCompartment
	 */
	public String getInteriorCompartment() {
		return interiorCompartment;
	}

	/**
	 * @param interiorCompartment the interiorCompartment to set
	 */
	public void setInteriorCompartment(String interiorCompartment) {
		this.interiorCompartment = interiorCompartment;
	}



	/**
	 * @return the ignoreCompartmentsID
	 */
	public Set<Integer> getIgnoreCompartmentsID() {
		return ignoreCompartmentsID;
	}

	/**
	 * @param ignoreCompartmentsID the ignoreCompartmentsID to set
	 */
	public void setIgnoreCompartmentsID(Set<Integer> ignoreCompartmentsID) {
		this.ignoreCompartmentsID = ignoreCompartmentsID;
	}

	/**
	 * @return the hascellwall
	 */
	public boolean isHasCellWall() {
		return hasCellWall;
	}

	/**
	 * @param hascellwall the hascellwall to set
	 */
	public void setHasCellWall(boolean hascellwall) {
		this.hasCellWall = hascellwall;
	}


	/**
	 * @return the processCompartmentsInitiated
	 */
	public boolean isProcessCompartmentsInitiated() {
		return processCompartmentsInitiated;
	}


	/**
	 * @param processCompartmentsInitiated the processCompartmentsInitiated to set
	 */
	public void setProcessCompartmentsInitiated(boolean processCompartmentsInitiated) {

		this.processCompartmentsInitiated = processCompartmentsInitiated;
	}

	/**
	 * @return the interiorCompartmentID
	 */
	public Integer getInteriorCompartmentID() {
		
		return this.idCompartmentAbbIdMap.get(interiorCompartment.toLowerCase());
	}


}
