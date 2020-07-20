package pt.uminho.ceb.biosystems.merlin.biocomponents.io;

public class Enumerators {
	
	public enum SBMLLevelVersion {

		L2V1{ // level 2 version 1

			public int getVersion(){
				return 1;
			}

			public int getLevel(){
				return 2;
			}

			@Override
			public String toString(){
				return "SBML Level 2 version 1";
			}
		}, 
		L2V2{ // level 2 version 2

			public int getVersion(){
				return 2;
			}

			public int getLevel(){
				return 2;
			}

			@Override
			public String toString(){
				return "SBML Level 2 version 2";
			}
		},
		L2V3{ //level 2 version 3

			public int getVersion(){
				return 3;
			}

			public int getLevel(){
				return 2;
			}

			@Override
			public String toString(){
				return "SBML Level 2 version 3";
			}
		},
		L2V4{ //level 2 version 4 //default

			public int getVersion(){
				return 4;
			}

			public int getLevel(){
				return 2;
			}

			@Override
			public String toString(){
				return "SBML Level 2 version 4";
			}
		},
		L3V1{ //level 3 version 1

			public int getVersion(){
				return 1;
			}

			@Override
			public String toString(){
				return "SBML Level 3 version 1";
			}
		}, 
		L3V2{ //level 3 version 1

			public int getVersion(){
				return 2;
			}

			@Override
			public String toString(){
				return "SBML Level 3 version 2";
			}
		};

		public int getLevel(){
			return 3;
		}

		public int getVersion(){
			return 2;
		}

		@Override
		public String toString(){
			return "SBML Level 3 version 2";
		}
	}
	
	public enum SBMLModelLevel{
		
		L2("Level 2"){
			@Override
			public String toString(){
				return this.getLevel();
			}
		},
		L3("Level 3");
		
		private String level;
		
		private SBMLModelLevel(String level){
			this.level = level;
		}
		
		public String getLevel(){
			return this.level;
		}
		
		@Override
		public String toString(){
			return L3.getLevel();
		}
		
	}

	
	public enum ModelSources{
		
		MODEL_SEED("modelSEED"),
		
//		BIGG("BIGG"){
//			@Override
//			public String toString(){
//				return "BIGG smbl model";
//			}
//		}
		OTHER("other"){
			@Override
			public String toString(){
				return "other smbl model source";
			}
		}
		;
		
		private String source;
		
		private ModelSources(String modelSource){
			this.source = modelSource;
		}
		
		public String sourceName(){
			return this.source;
		}
		
		@Override
		public String toString(){
			return "modelSEED smbl model";
		}
	}
	
	/**
	 * @author amaromorais
	 * available programs for models genomes alignment
	 */
	public static enum AlignmentMethod{	
		
		BLAST,
		SmithWaterman
	}
	
	

}
