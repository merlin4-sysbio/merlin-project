package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.chebi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.ws.WebServiceException;

import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.KeggAPI;
import pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.kegg.datastructures.KeggCompoundER;
import pt.uminho.ceb.biosystems.merlin.utilities.external.ExternalRefSource;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;
import uk.ac.ebi.chebi.webapps.chebiWS.model.DataItem;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntity;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntityList;
import uk.ac.ebi.chebi.webapps.chebiWS.model.OntologyDataItem;
import uk.ac.ebi.chebi.webapps.chebiWS.model.SearchCategory;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;

public class ChebiAPIInterface {

	static private ChebiWebServiceClient chebiClient = new ChebiWebServiceClient();
	static private Map<String, ExternalRefSource> sourceDic = getsources();
	private static int maxNTries = 100;

	/**
	 * @param chebiId
	 * @return
	 */
	static public String getKeggIdRef(String chebiId) throws Exception{

		String keggId = null;
		List<DataItem> datalinks;
		datalinks = chebiClient.getCompleteEntity(chebiId).getDatabaseLinks();
		for ( DataItem dataItem : datalinks ) {
			if(dataItem.getType().equalsIgnoreCase("kegg compound accession")){
				keggId = dataItem.getData();
				break;
			}
		}
		return keggId;
	}

	private static Map<String, ExternalRefSource> getsources() {

		Map<String, ExternalRefSource> ret = new HashMap<String, ExternalRefSource>();

		ret.put("KEGG COMPOUND accession", ExternalRefSource.KEGG_CPD);

		return ret;
	}
	/**
	 * @param chebiId
	 * @param charge
	 * @param sercheadCharges
	 * @return
	 * @throws Exception 
	 * @throws ChebiWebServiceFault_Exception
	 */
	static private ChebiER getMetabolite(String chebiId, int charge, List<Integer> sercheadCharges) throws Exception{
		Entity entity  = null;
			
		try {
			entity = chebiClient.getCompleteEntity(chebiId);
		} catch (ChebiWebServiceFault_Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		String chargeEntity = entity.getCharge();

		int chargeEnt = 0;
		if(chargeEntity!=null)
			chargeEnt = Integer.parseInt(chargeEntity.replace("+", ""));

		if(sercheadCharges.contains(chargeEnt))
			return convert(entity);

		sercheadCharges.add(chargeEnt);

		String chebiIdToReturn = null;
		String searchingOntology = null;

		if(charge < chargeEnt){
			searchingOntology = "is conjugate acid of";
		}else if(charge > chargeEnt){
			searchingOntology = "is conjugate base of";
		}

		if(searchingOntology!= null){
			for(OntologyDataItem ontology :entity.getOntologyParents()){
				if(ontology.getType().equalsIgnoreCase(searchingOntology))
					chebiIdToReturn = ontology.getChebiId();
			}

			if(chebiIdToReturn != null)
				return getMetabolite(chebiIdToReturn,charge,sercheadCharges);
		}

		return convert(entity);
	}


	/**
	 * @param metabolite_name
	 * @return
	 * @throws ChebiWebServiceFault_Exception 
	 */
	public static String getChebiEntityByName(String metabolite_name, double searchScore) throws Exception{
		SearchCategory searchCategory;
		if(searchScore==-1)
		{
			searchCategory=SearchCategory.ALL;
		}
		else
		{
			searchCategory=SearchCategory.ALL_NAMES;
		}
		LiteEntityList entities = chebiClient.getLiteEntity(metabolite_name, searchCategory, 10, StarsCategory.ALL);
		List<LiteEntity> resultList = entities.getListElement();
		for (LiteEntity liteEntity : resultList )
		{
			if(liteEntity.getChebiAsciiName().equalsIgnoreCase(metabolite_name))
			{
				return liteEntity.getChebiId();
			}
			if(liteEntity.getSearchScore()>searchScore)
			{
				if(searchScore==-1)
				{
					return liteEntity.getChebiId();
				}
				else
				{
					if(metabolite_name.equalsIgnoreCase(liteEntity.getChebiAsciiName()));
					{
						return liteEntity.getChebiId();
					}
				}
			}
			else
			{
				if(searchScore>=3)
				{
					return ChebiAPIInterface.getChebiEntityByName(metabolite_name, searchScore-1);
				}
			}				
		}
		return null;
	}

	/**
	 * @param metabolite_name
	 * @return
	 * @throws ChebiWebServiceFault_Exception 
	 */
	public static String getChebiEntityByExactName(String metabolite_name) throws Exception{
		//		try
		//		{
		SearchCategory searchCategory = SearchCategory.ALL_NAMES;
		LiteEntityList entities = chebiClient.getLiteEntity(metabolite_name, searchCategory, 100, StarsCategory.ALL);
		List<LiteEntity> resultList = entities.getListElement();
		for (int i = 0; i < resultList.size(); i++)
		{
			LiteEntity liteEntity = resultList.get(i);
			String chebiName = liteEntity.getChebiAsciiName() ;
			if(metabolite_name.trim().equalsIgnoreCase(chebiName))
			{
				i=resultList.size();
				return liteEntity.getChebiId();
			}
			Entity entity  = ChebiAPIInterface.chebiClient.getCompleteEntity(liteEntity.getChebiId());
			ChebiER chebi_data = convert(entity);
			for(String synonym: chebi_data.secondaryNames)
			{
				if(metabolite_name.trim().equalsIgnoreCase(synonym))
				{
					i=resultList.size();
					return liteEntity.getChebiId();
				}
			}
		}
		return null;
	}

	/**
	 * @param chebiId
	 * @return
	 * @throws ChebiWebServiceFault_Exception 
	 * @throws ChebiWebServiceFault_Exception 
	 */
	public static ChebiER getChildElements(String chebiId) throws Exception {

		Set<String> result=new TreeSet<String>();

		Entity entity  = ChebiAPIInterface.chebiClient.getCompleteEntity(chebiId);

		for(OntologyDataItem ontology :entity.getOntologyChildren()) {
			
			if(ontology.getType().equalsIgnoreCase("is a")) {
				
				result.add(ontology.getChebiId());
			}
			if(ontology.getType().equalsIgnoreCase("has role")) {
				
				result.add(ontology.getChebiId());
			}
		}
		String keggId = null;
		Set<String> temp = new TreeSet<String>();
		
		for ( DataItem dataItem : entity.getDatabaseLinks() ) {
			
			if(dataItem.getType().equalsIgnoreCase("kegg compound accession")) {
				
				if(keggId==null) {
					
					keggId = dataItem.getData();
				}
				
				temp.add(dataItem.getData());
			}
		}
		
		if(temp.size()>1) {
			
			for(String kegg:temp) {
				
				//if(kegg != null) { 
					
					try {
						KeggCompoundER keggCompoundER = KeggAPI.getCompoundByKeggId(kegg);
						
						if(keggCompoundER!=null) {
							
							keggId = keggCompoundER.getId();
							break;
						}
					}
					catch (Exception e) {
						
						e.printStackTrace();
					}
				//}
			}
		}
		ChebiER chebi_data = convert(entity);
		chebi_data.setKegg_id(keggId);
		chebi_data.setFunctional_children(result);

		return chebi_data;
	}

	/**
	 * @param chebiId
	 * @param trialNumber
	 * @return
	 */
	public static Set<String> getFunctionalParents(String chebiId, int trialNumber) {

		Set<String> result = new TreeSet<String>();
		
		Entity entity;
		
		try {
			
			entity = ChebiAPIInterface.chebiClient.getCompleteEntity(chebiId);
			
			for(OntologyDataItem ontology :entity.getOntologyParents()) {
				
				if(ontology.getType().equalsIgnoreCase("has functional parent")) {
					
					result.add(ontology.getChebiId());
				}
			}
		}
		catch (ChebiWebServiceFault_Exception e) {
			
			//e.printStackTrace();
			System.err.println(chebiId +"\t"+ e.getLocalizedMessage());
			
			if(trialNumber<maxNTries) {
				
				trialNumber=trialNumber+1;
				result.addAll(ChebiAPIInterface.getFunctionalParents(chebiId, trialNumber));
			}
		}
		catch (WebServiceException e) {

			System.err.println(chebiId +"\t"+ e.getLocalizedMessage());
			
			while(trialNumber<maxNTries) {
				
				trialNumber=trialNumber+1;
				result.addAll(ChebiAPIInterface.getFunctionalParents(chebiId, trialNumber));
			}
			
		}
		catch (NullPointerException e) {
			
			System.err.println(chebiId +"\t"+ e.getLocalizedMessage());
			
			while(trialNumber<maxNTries) {
				
				trialNumber=trialNumber+1;
				result.addAll(ChebiAPIInterface.getFunctionalParents(chebiId, trialNumber));
			}
		}
		catch (Exception e) {
			
			System.err.println(chebiId +"\t"+ e.getLocalizedMessage());
			
			while(trialNumber<maxNTries) {
				
				trialNumber=trialNumber+1;
				result.addAll(ChebiAPIInterface.getFunctionalParents(chebiId, trialNumber));
			}
		}

		return result;

	}

	/**
	 * @param chebiId
	 * @param charge
	 * @return
	 * @throws Exception 
	 * @throws ChebiWebServiceFault_Exception
	 */
	static public ChebiER getMetabolites(String chebiId, int charge) throws Exception{
		List<Integer> searchedCharges = new ArrayList<Integer>();
		return getMetabolite(chebiId,charge,searchedCharges);
	}

	/**
	 * @param searchingChebiId
	 * @return
	 * @throws Exception
	 */
	static public ChebiER getExternalReference(String searchingChebiId) throws Exception{


		Entity entity = chebiClient.getCompleteEntity(searchingChebiId);

		if(entity==null)
		{
			throw new Exception( "[ChebiWebServiceClient] - BD does not contain the id " + searchingChebiId);
		}

		return convert(entity);
	}

	//	static public ChebiER getCompleteInformation(ChebiER uncompleteEntity) throws Exception{
	//		
	//		return getExternalReference(uncompleteEntity.getIDReference());
	//	}

	//	static public List<SearchedExternalRef<ChebiER>> searchEntities(String searchKey) throws ChebiWebServiceFault_Exception{
	//		
	//		
	//		List<SearchedExternalRef<ChebiER>>  result = new ArrayList<SearchedExternalRef<ChebiER>>();
	//		LiteEntityList entities = chebiClient.getLiteEntity(searchKey, SearchCategory.ALL_NAMES, 50, StarsCategory.ALL);
	//		
	//		for(LiteEntity liteEntity: entities.getListElement()){
	//			Float score = liteEntity.getSearchScore();
	//			ChebiER ref = convert(liteEntity, null);
	//			SearchedExternalRef<ChebiER> searchedRef = new SearchedExternalRef<ChebiER>(ref, score, searchKey);
	//			
	//			result.add(searchedRef);
	//		}
	//		return result;
	//	}
	//	
	//	static public List<SearchedExternalRef<ChebiER>> searchEntities(List<String> searchKeys) throws ChebiWebServiceFault_Exception{
	//		
	//		
	//		List<SearchedExternalRef<ChebiER>>  result = new ArrayList<SearchedExternalRef<ChebiER>>();
	//		
	//		for(String searchKey: searchKeys){
	//			result.addAll(searchEntities(searchKey));
	//		}
	//		return result;
	//	}

	static private ChebiER convert(Entity entity){
		if(entity == null)
			return null;

		String chebiId = entity.getChebiId();


		List<String> secundaryNames = new ArrayList<String>();
		for(DataItem item :entity.getSynonyms())
			secundaryNames.add(item.getData());

				String formula = (entity.getFormulae()!=null && entity.getFormulae().size()>0)?entity.getFormulae().get(0).getData():null;

				int charge = 0;
				if(entity.getCharge() != null)
					charge = Integer.parseInt(entity.getCharge().replace("+", ""));

				Map<ExternalRefSource, String> otherBDLinks = new HashMap<ExternalRefSource, String>();

				List<DataItem> datalinks = entity.getDatabaseLinks();
				for (DataItem dataItem : datalinks) {

					String type = dataItem.getType();
					String data = dataItem.getData();
					ExternalRefSource souce = sourceDic.get(type);

					otherBDLinks.put(souce, data);

					// if(dataItem.getType().equalsIgnoreCase("kegg compound accession")){
					// keggId = dataItem.getData();
					// break;
					// }
				}

				ChebiER ret = new ChebiER(chebiId, entity.getChebiAsciiName(), charge,
						formula, entity.getMass(), entity.getSmiles(), entity
								.getInchiKey(), entity.getSecondaryChEBIIds(),
						secundaryNames, otherBDLinks);
				
				return ret;
	}

	//	static private ChebiER convert(LiteEntity entity, String miriamCode){
	//		String chebiId = entity.getChebiId();
	//		
	//		
	//		ChebiER ret = new ChebiER(chebiId, 
	//				entity.getChebiAsciiName(), 0, null, 
	//				null, null, null, null,null);
	//		
	//		return ret;
	//	}


}
