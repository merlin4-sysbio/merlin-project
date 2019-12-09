package pt.uminho.ceb.biosystems.merlin.core.utilities.compartments;

public class CompartmentsEnumerators {

	/**
	 * @author ODias
	 *
	 */


	public enum STAIN {

		gram_positive,
		gram_negative
	}
	
	
	public static enum OrganismType{
		__(""),
		animal ("animal"),
		plant ("plant"),
		fungi ("fungi");
		
		private String organismType;
		
		/**
		 * @param organismType
		 */
		private  OrganismType (String organismType) {

			this.organismType = organismType;
		}
		
		public String getOrganismType(){
			return this.organismType;
		}
	}
	
	public enum KINGDOM{
		//TODO Bacteria(Bacteria)
		Bacteria ("Bacteria"),
		Eukaryota ("Eukaryota"),
		Archaea ("Archaea"),
		Viruses ("Viruses"),
		Viroids ("Viroids"),
		All("All");
		
		private String kingdom;
		
		private  KINGDOM (String kingDom) {
			
			this.kingdom = kingDom;
		}
		
		public String getKingdom(){
			return this.kingdom;
		}
	}
	
	/**
	 * @author ODias
	 *
	 */
	public enum TransportType {
		
		symport(0),
		symport_out (1),
		antiport(0),
		//simple,
		complex(0),
		transport(2),
		influx(0),
		sensor (3),
		efflux(1);

		private int transport_type;
		
		private TransportType(int transport_type) {
			
			this.transport_type = transport_type;
		}

		public int getTransport(){
			return this.transport_type;
		}
	}

}
