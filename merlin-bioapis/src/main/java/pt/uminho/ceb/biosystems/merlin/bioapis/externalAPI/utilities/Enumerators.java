package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.utilities;

import java.util.ArrayList;
import java.util.List;

import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;

public class Enumerators {

	public enum FileExtensions{


        //          ASSEMBLY_REPORT("assembly_report.txt"),
        //          ASSEMBLY_STATS("assembly_stats.txt"),
        PROTEIN_FAA("protein.faa")
        {
            
            public List<SequenceType> getSequenceType() {
                
                this.sequenceTypes.add(SequenceType.PROTEIN);
                return this.sequenceTypes;
            }
            
            @Override
            public String toString(){
                return "fasta file (protein.faa)";
            }
        }, 
        CDS_FROM_GENOMIC("cds_from_genomic.fna") {
            
            public List<SequenceType> getSequenceType() {
                
                this.sequenceTypes.add(SequenceType.CDS_DNA);
                
                return this.sequenceTypes;
            }
            
            @Override
            public String toString(){
                return "cds file (cds_from_genomic.fna)";
            }
        },
        //          FEATURE_TABLE("feature_table.txt"),
        GENOMIC_FNA("genomic.fna"){
            
            public List<SequenceType> getSequenceType() {
                
                this.sequenceTypes.add(SequenceType.GENOMIC_DNA);
                
                return this.sequenceTypes;
            }
            
            @Override
            public String toString(){
                return "genome file (genomic.fna)";
            }
        },
//      GENOMIC_GBFF("genomic.gbff"){
//          @Override
//          public String toString(){
//              return "genbank file ('.gbff')";
//          }
//      },
        //          GENOMIC_GFF("genomic.gff"),
        //          PROTEIN_GPFF("protein.gpff"),
        RNA_FROM_GENOMIC("rna_from_genomic.fna") {
            
            public List<SequenceType> getSequenceType() {
            
                this.sequenceTypes.add(SequenceType.RRNA);
                this.sequenceTypes.add(SequenceType.TRNA);
                return this.sequenceTypes;
            }
            
            @Override
            public String toString(){
                return "rna file (rna_from_genomic.fna)";
            }
        },
        //R_RNA_FROM_GENOMIC ("rrna_from_genomic.fna"),
        //T_RNA_FROM_GENOMIC ("trna_from_genomic.fna"),
        CUSTOM_GENBANK_FILE("customGenBankFile"){
            @Override
            public String toString(){
                return "custom genbank file ('.gbff'/'.gpff')";
            }
        };
        private String name;
        FileExtensions(String name){
            this.name = name;
        }
        
        protected List<SequenceType> sequenceTypes = new ArrayList<>();
        public String getName(){
            return name;
        }
        
        public String getExtension(){
            return name.substring(name.lastIndexOf("."));
        }
        
        @Override
        public String toString(){
            return null;
        }
        
        public List<SequenceType> getSequenceType() {
            
            return sequenceTypes;
        }

	}

	public enum TypeOfExport{

		PROTEIN_FAA("protein.faa"){
			@Override
			public String toString(){
				return "fasta file (protein.faa)";
			}
		}, 
		ALL_FILES(".mer");

		private String type;

		private TypeOfExport(String type){
			this.type = type;
		}

		public String extension(){
			return this.type;
		}
		
		@Override
		public String toString(){
			return "all files";
		}

	}

	public enum GenBankFiles{

		//		PROTEIN_GPFF("protein.gpff"), 
		GENOMIC_GBFF("genomic.gbff"),
		CUSTOM_FILE("customGenBankFile"){
			@Override
			public String toString(){
				return "custom genbank file ('.gbff'/'.gpff')";
			}
		};

		private String file;

		private GenBankFiles(String file){
			this.file = file;
		}

		public String extension(){
			return this.file;
		}
		
		@Override
		public String toString(){
			return "gb file downloaded by merlin ('genomic.gbff')";
		}

	}
	
}
