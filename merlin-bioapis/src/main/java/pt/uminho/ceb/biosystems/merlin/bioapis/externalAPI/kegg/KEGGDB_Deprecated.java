package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg;


@Deprecated
public interface KEGGDB_Deprecated {
	/*
		Database		Name		Abbrev	kid				Remark
		KEGG PATHWAY	pathway		path	map number	
		KEGG BRITE		brite		br		br number	
		KEGG MODULE		module		md		M number	
		KEGG DISEASE	disease		ds		H number		Japanese version: disease_ja ds_ja
		KEGG DRUG		drug		dr		D number		Japanese version: drug_ja dr_ja
		KEGG ENVIRON	environ		ev		E number		Japanese version: environ_ja ev_ja
		KEGG ORTHOLOGY	orthology	ko		K number	
		KEGG GENOME		genome		genome	T number	
		KEGG GENOMES	genomes		gn		T number		Composite database: genome + egenome + mgenome
		KEGG GENES		genes		-		-				Composite database: consisting of KEGG organisms
		KEGG LIGAND		ligand		ligand	-				Composite database: compound + glycan + reaction + rpair + rclass + enzyme
		KEGG COMPOUND	compound	cpd		C number		Japanese version: compound_ja cpd_ja
		KEGG GLYCAN		glycan		gl		G number	
		KEGG REACTION	reaction	rn		R number	
		KEGG RPAIR		rpair		rp		RP number	
		KEGG RCLASS		rclass		rc		RC number	
		KEGG ENZYME		enzyme		ec		-	
	 */
	public static final String COMPOUND = "cpd";
	public static final String ENZYME =   "ec";
	public static final String REACTION = "rn";
	public static final String GLYCAN =   "gl";
	public static final String PATHWAY =  "path";
}
