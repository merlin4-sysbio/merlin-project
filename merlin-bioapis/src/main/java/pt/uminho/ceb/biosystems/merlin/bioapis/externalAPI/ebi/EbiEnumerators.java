package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi;

public class EbiEnumerators {
	
	/**
	 * @author Oscar Dias
	 *
	 */
	public enum EbiTool {
		
		PHOBIUS,
		INTERPRO
	}
	
	public enum InterProOperation {
		// sequencesearch | parameters | parameterdetails | run | status | resulttypes | result
		// sequence search is directly through browser
		// parameterdetails followed by /$parameterId
		// resulttypes followed by /$jobID
		// result followed by /$jobID/$resultType
		
		sequencesearch,
		parameters,
		parameterdetails,
		run,
		status,
		resulttypes,
		result
	}

	public enum InterProDatabases{
		
		Coils				("Coils"),
		GENE3D				("Gene3d"),
		HAMAP				("HAMAP"),
		HMMPanther			("Panther"),
		HMMPfam				("pfamA"),
		Phobius				("Phobius"),
		HMMPIR				("PIRSF"),	
		FPrintScan			("PRINTS"),
		BLASTProDom			("ProDom"),
		PrositeProfiles		("ProfileScan"),
		PatternScan			("PrositePatterns"),
		SignalPHMM			("SignalP"),
		HMMSmart			("SMART"),
		SuperFamily			("SuperFamily"),
		HMMTigr				("TIGRFAM"),
		TMHMM				("TMHMM");
		
		private final String database;
		
		InterProDatabases(String database){
			
			this.database = database;
			
		}
		
		public String getDatabase(){
			
			return this.database;
			
		}
	}

}
