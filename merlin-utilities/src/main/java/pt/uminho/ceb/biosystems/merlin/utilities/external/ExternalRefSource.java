package pt.uminho.ceb.biosystems.merlin.utilities.external;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ExternalRefSource {
	
	TCDB{
		@Override
		public String getURL(){
			return "http://www.tcdb.org/";
		}

		public String getName(){
			return "tcdb";
		}

		public String getIdentifierId(){

			return this.getMiriamId();
		}

		public String getMiriamId(){
			return "tcdb";
		}

		public ExternalRef getReference(String id){
			return null;
		}
	},
	NCBI_GENE{
		@Override
		public String getURL(){
			return "https://www.ncbi.nlm.nih.gov/gene/";
		}

		public String getName(){
			return "ncbi gene";
		}

		public String getIdentifierId(){

			return this.getMiriamId();
		}

		public String getMiriamId(){
			return "ncbigene";
		}

		public ExternalRef getReference(String id){
			return null;
		}
	},
	NCBI_PROTEIN{
		@Override
		public String getURL(){
			return "https://www.ncbi.nlm.nih.gov/protein/";
		}

		public String getName(){
			return "ncbi protein";
		}

		public String getIdentifierId(){

			return this.getMiriamId();
		}

		public String getMiriamId(){
			return "ncbiprotein";
		}

		public ExternalRef getReference(String id){
			return null;
		}
	},
	KEGG{
		@Override
		public String getURL(){
			return "http://www.genome.jp/";
		}

		public String getName(){
			return "kegg";
		}

		public String getIdentifierId(){

			return this.getMiriamId();
		}

		public String getMiriamId(){
			return "kegg";
		}

		public ExternalRef getReference(String id){
			return null;
		}
	},
	KEGG_PATHWAY{
		@Override
		public String getURL(){
			return "http://www.genome.jp/dbget-bin/show_pathway?";
		}

		public String getName(){
			return "kegg pathway";
		}

		public String getIdentifierId(){

			return this.getMiriamId();
		}

		public String getMiriamId(){
			return "kegg.pathway";
		}

		public ExternalRef getReference(String id){
			return null;
		}
	},

	KEGG_REACTION{
		@Override
		public String getURL(){
			return "http://www.genome.jp/dbget-bin/www_bget?";
		}

		public String getName(){
			return "kegg reaction";
		}

		public String getMiriamId(){
			return "kegg.reaction";
		}

		public String getIdentifierId(){

			return this.getMiriamId();
		}

//		public KeggReactionInformation getReference(String id) throws Exception{
//			return KeggAPI.getReactionByKeggId(id);
//		}

	},

	EC_Code{
		public String getURL(){
			return "";
		}

		public String getName(){
			return "ec-code";
		}

		public String getMiriamId(){
			return "ec-code";
		}
		public String getIdentifierId(){

			return this.getMiriamId();
		}

//		@Override
//		IExternalRef getReference(String id) throws Exception {
//			// TODO Auto-generated method stub
//			return null;
//		}
	},

	KEGG_CPD{
		@Override
		public String getURL(){
			return "http://www.genome.jp/dbget-bin/www_bget?";
		}

		public String getName(){
			return "kegg compound";
		}

		public String getMiriamId(){
			return "kegg.compound";
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}
		
		public MetaboliteExternalRef getReference(String id) throws Exception{
			
			return null;
//			return KeggAPI.getCompoundByKeggId(id);
		}

	},

	CHEBI{
		@Override
		public String getURL(){
			return "http://www.ebi.ac.uk/chebi/searchId.do?chebiId=";
		}

		public String getName(){
			return "chebi";
		}

		public String getMiriamId(){
			return "obo.chebi";
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}
		
//		public MetaboliteExternalRef getReference(String id) throws Exception{
//			return ChebiAPIInterface.getExternalReference(id);

//		}

	},

	PUBMED{
		@Override
		public String getURL(){
			return "";
		}

		public String getName(){
			return "pubmed";
		}

		public String getMiriamId(){
			return "pubmed";
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}
		
		public ExternalRef getReference(String id){
			return null;

		}

	},

	SGD{
		@Override
		public String getURL(){
			return "";
		}

		public String getName(){
			return "sgd";
		}

		public String getMiriamId(){
			return "sgd";
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}
		
		public ExternalRef getReference(String id){
			return null;
		}

	},

	ECO{
		@Override
		public String getURL(){
			return "";
		}

		public String getName(){
			return "evidence code";
		}

		public String getMiriamId(){
			return "ECO";
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}

//		@Override
//		IExternalRef getReference(String id) throws Exception {
//			// TODO Auto-generated method stub
//			return null;
//		}

	},

	UNIPROT{
		@Override
		public String getURL(){
			return "";
		}

		public String getName(){
			return "uniprot";
		}

		public String getMiriamId(){
			return "uniprot";
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}

//		@Override
//		IExternalRef getReference(String id) throws Exception {
//			// TODO Auto-generated method stub
//			return null;
//		}


	}, KEGG_ORTHOLOGY {

		@Override
		public String getURL() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		String getMiriamId() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}

//		@Override
//		IExternalRef getReference(String id) throws Exception {
//			// TODO Auto-generated method stub
//			return null;
//		}
	}, BRENDA {
		@Override
		public String getURL() {
			return "http://www.brenda-enzymes.org/";
		}

		@Override
		public String getName() {
			return "BRENDA";
		}

		@Override
		String getMiriamId() {
			return null;
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}

//		@Override
//		IExternalRef getReference(String id) throws Exception {
//			return null;
//		}
	}, CAS {
		@Override
		public String getURL() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getName() {
			return "CAS";
		}

		@Override
		String getMiriamId() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}

//		@Override
//		IExternalRef getReference(String id) throws Exception {
//			// TODO Auto-generated method stub
//			return null;
//		}
	}, PUBCHEM {
		@Override
		public String getURL() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getName() {
			return "pubchem";
		}

		@Override
		String getMiriamId() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}

//		@Override
//		IExternalRef getReference(String id) throws Exception {
//			// TODO Auto-generated method stub
//			return null;
//		}
	}, METACYC_CPD {
		@Override
		public String getURL() {
			return "http://metacyc.org/META/NEW-IMAGE?object=";
		}

		@Override
		public String getName() {
			return "metacyc compound";
		}

		@Override
		String getMiriamId() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}

//		@Override
//		IExternalRef getReference(String id) throws Exception {
//			return new ExternalRef(id, null, this);
//		}
	}, SEED_CPD {
		@Override
		public String getURL() {
			return "http://seed-viewer.theseed.org/seedviewer.cgi?page=CompoundViewer&compound=";
		}

		@Override
		public String getName() {
			return "seed compound";
		}

		@Override
		String getMiriamId() {
			// TODO Auto-generated method stub
			return "seed.compound";
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}

//		@Override
//		IExternalRef getReference(String id) throws Exception {
//			return new ExternalRef(id, null, this);
//		}
	}, MODEL {
		@Override
		public String getURL() {
			return null;
		}

		@Override
		public String getName() {
			return "model";
		}

		@Override
		String getMiriamId() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public String getIdentifierId(){

			return this.getMiriamId();
		}

//		@Override
//		IExternalRef getReference(String id) throws Exception {
//			// TODO Auto-generated method stub
//			return null;
//		}
	} ;


	abstract public String getURL();
	abstract public String getName();
	abstract String getMiriamId();
	abstract String getIdentifierId();
//	abstract IExternalRef getReference(String id) throws Exception;


	public String getSourceId(String miriamCode){

		miriamCode = miriamCode.replaceAll("%3A", ":");
		String regExpString = "urn:miriam:"+getMiriamId()+":(.+)";

		Pattern pattern = Pattern.compile(regExpString);
		Matcher matcher = pattern.matcher(miriamCode);

		String id = null;
		if(matcher.find())
			id = matcher.group(1);
		return id;
	}

	public String getMiriamCode(String sourceId){
		if(this.getMiriamId() == null)
			return null;

		String miriamCode = "urn:miriam:" + getMiriamId() + ":" + sourceId;
		return miriamCode;
	}
	
	public String getIdentifierId(String identifierCode){

		identifierCode = identifierCode.replaceAll("%3A", ":");
		String regExpString = "http://identifiers.org/"+getIdentifierId()+"/(.+)";

		Pattern pattern = Pattern.compile(regExpString);
		Matcher matcher = pattern.matcher(identifierCode);

		String id = null;
		if(matcher.find())
			id = matcher.group(1);
		return id;
	}
	
	public String getIdentifierCode(String sourceId){
		if(this.getIdentifierId() == null)
			return null;

		String identifierCode = "http://identifiers.org/" + getIdentifierId() + "/" + sourceId;
		return identifierCode;
	}

	public String getUrlLink(String sourceId){
		return this.getURL() + sourceId;
	}

}
