package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologydataDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberList;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationGeneHomology;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologyData;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductList;



public class EnzymesAnnotationHomologyDataDAOImpl extends GenericDaoImpl<EnzymesAnnotationHomologyData> implements IEnzymesAnnotationHomologydataDAO {

	public EnzymesAnnotationHomologyDataDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationHomologyData.class);

	}

	@Override
	public void addEnzymesAnnotationHomologyData(EnzymesAnnotationHomologyData enzymesAnnotationHomologyData) {
		super.save(enzymesAnnotationHomologyData);

	}

	@Override
	public void addEnzymesAnnotationHomologyDataList(List<EnzymesAnnotationHomologyData> enzymesAnnotationHomologyDataList) {
		for (EnzymesAnnotationHomologyData enzymesAnnotationHomologyData: enzymesAnnotationHomologyDataList) {
			this.addEnzymesAnnotationHomologyData(enzymesAnnotationHomologyData);
		}
	}

	@Override
	public List<EnzymesAnnotationHomologyData> getAllEnzymesAnnotationHomologyData() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationHomologyData getEnzymesAnnotationHomologyData(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationHomologyData(EnzymesAnnotationHomologyData enzymesAnnotationHomologyData) {
		super.delete(enzymesAnnotationHomologyData);

	}

	@Override
	public void removeEnzymesAnnotationHomologyDataList(List<EnzymesAnnotationHomologyData> enzymesAnnotationHomologyDataList) {
		for (EnzymesAnnotationHomologyData enzymesAnnotationHomologyData: enzymesAnnotationHomologyDataList) {
			this.removeEnzymesAnnotationHomologyData(enzymesAnnotationHomologyData);
		}
	}

	@Override
	public void updateEnzymesAnnotationHomologyDataList(List<EnzymesAnnotationHomologyData> enzymesAnnotationHomologyDataList) {
		for (EnzymesAnnotationHomologyData enzymesAnnotationHomologyData: enzymesAnnotationHomologyDataList) {
			this.update(enzymesAnnotationHomologyData);
		}
	}

	@Override
	public void updateEnzymesAnnotationHomologyData(EnzymesAnnotationHomologyData enzymesAnnotationHomologyData) {
		super.update(enzymesAnnotationHomologyData);

	}

	@Override
	public List<EnzymesAnnotationHomologyData> getAllEnzymesAnnotationHomologyDataAttributes() {	
		List<EnzymesAnnotationHomologyData> list =  this.findAll();
		return list;
	}

	@Override
	public List<String[]> getEnzymesAnnotationHomologydataSKeyAndOtherNames(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationProductList> product = c.from(EnzymesAnnotationProductList.class);
		Join<EnzymesAnnotationProductList, EnzymesAnnotationHomologyData> join = product.join("enzymesAnnotationHomologyData",JoinType.LEFT);

		c.multiselect(join.get("enzymesAnnotationGeneHomology").get("SKey"), product.get("otherNames")); 

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		List<String[]> res = new ArrayList<String[]>();

		if(list.size() > 0) {
			for(Object[] result: list) {

				String[] aux = new String[2];

				aux[0] = result[0].toString();
				aux[1] = result[1].toString();

				res.add(aux);
			}

		}

		return res;
	}

	@Override
	public List<String[]> getEnzymesAnnotationHomologydataSKeyAndOtherEcNumbers(){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationEcNumberList> data = c.from(EnzymesAnnotationEcNumberList.class);
		Join<EnzymesAnnotationEcNumberList, EnzymesAnnotationHomologyData> join = data.join("enzymesAnnotationHomologyData",JoinType.LEFT);

		c.multiselect(join.get("enzymesAnnotationGeneHomology").get("SKey"), data.get("otherEcnumbers")); 

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		List<String[]> res = new ArrayList<String[]>();

		if(list.size() > 0) {
			for(Object[] result: list) {

				String[] aux = new String[2];

				aux[0] = result[0].toString();
				aux[1] = result[1].toString();

				res.add(aux);
			}

		}
		return res;
	}

	@Override
	public List<EnzymesAnnotationHomologyData> getAllEnzymesAnnotationHomologyDataBySKey(Integer skey) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("SKey", skey);
		List<EnzymesAnnotationHomologyData> list =  this.findByAttributes(dic);
		return list;
	}

	@Override
	public void updateEnzymesAnnotationHomologydataLocusTagByHomologySkey(String locus, Integer skey) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("SKey", skey);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("locusTag", locus);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public void updateEnzymesAnnotationHomologydataNameByHomologySkey(String name, Integer skey) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("SKey", skey);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("geneName", name);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public void updateEnzymesAnnotationHomologydataNotesAndProductByHomologySkey(String notes, String product, Integer skey) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("SKey", skey);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("product", product);
		updateAttributes.put("notes", notes);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public void updateEnzymesAnnotationHomologydataAttributesByHomologySkey(Integer homologyskey, String product, Integer skey,
			String locus, String ec) {
		Map<String, Serializable> filterAttributes = new HashMap<String, Serializable>();
		filterAttributes.put("SKey", skey);

		Map<String, Serializable> updateAttributes = new HashMap<String, Serializable>();
		updateAttributes.put("product", product);
		updateAttributes.put("enzymesAnnotationGeneHomology.SKey", homologyskey);
		updateAttributes.put("locusTag", locus);
		updateAttributes.put("ecNumber", ec);

		this.updateAttributesByAndFilterAttributes(updateAttributes, filterAttributes);
	}

	@Override
	public Boolean checkHomologyDataByGeneHomologySKey(Integer skey) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("SKey", skey);
		return this.checkByAttributes(dic);
	}

	@Override
	public Integer getHomologyDataIDByGeneHomologySKey(Integer skey) {
		EnzymesAnnotationHomologyData res = this.findUniqueByAttribute("SKey", skey);
		if(res != null)
			return res.getSKey();
		return null;
	}

	@Override
	public Integer insertHomologyData(EnzymesAnnotationGeneHomology geneHomology, String locusTag, String geneName,
			String product, String ecnumber, Boolean selected, String chromossome, String notes) {

		EnzymesAnnotationHomologyData homologyData = new EnzymesAnnotationHomologyData();
		homologyData.setChromosome(chromossome);
		homologyData.setEcNumber(ecnumber);

		homologyData.setEnzymesAnnotationGeneHomology(geneHomology);

		homologyData.setLocusTag(locusTag);
		homologyData.setGeneName(geneName);
		homologyData.setProduct(product);
		homologyData.setSelected(selected);
		homologyData.setNotes(notes);

		return (Integer) this.save(homologyData);

	}

	@Override
	public List<String[]> getHomologyProductsByGeneHomologySkey(Integer skey){ 
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<EnzymesAnnotationHomologyData> homologyData = c.from(EnzymesAnnotationHomologyData.class);
		Join<EnzymesAnnotationProductList, EnzymesAnnotationHomologyData> productList = homologyData.join("enzymesAnnotationHomologyData",JoinType.LEFT);

		c.multiselect(homologyData.get("product"), productList.get("otherNames")); 

		Predicate filter = cb.equal(homologyData.get("SKey"), skey);

		c.where(filter);

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> list = q.getResultList();

		List<String[]> res = new ArrayList<>();

		if(list.size() > 0) {
			
			for(Object[] result: list) {

				String[] aux = new String[2];

				aux[0] = result[0].toString(); // Product
				aux[1] = result[1].toString(); // Other ProductNames ??

				res.add(aux);
			}

		}

		return res;
	}


}
