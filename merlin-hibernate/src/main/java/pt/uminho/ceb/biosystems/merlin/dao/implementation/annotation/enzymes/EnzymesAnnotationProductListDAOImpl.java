package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationProductListDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologyData;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationProductList;


public class EnzymesAnnotationProductListDAOImpl extends GenericDaoImpl<EnzymesAnnotationProductList> implements IEnzymesAnnotationProductListDAO {

	public EnzymesAnnotationProductListDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationProductList.class);
		
	}

	@Override
	public void addEnzymesAnnotationProductList(EnzymesAnnotationProductList EnzymesAnnotationProductList) {
		super.save(EnzymesAnnotationProductList);
	}

	@Override
	public void addEnzymesAnnotationProductList(List<EnzymesAnnotationProductList> EnzymesAnnotationProductListList) {
		for (EnzymesAnnotationProductList EnzymesAnnotationProductList: EnzymesAnnotationProductListList) {
			this.addEnzymesAnnotationProductList(EnzymesAnnotationProductList);
		}
	}

	@Override
	public List<EnzymesAnnotationProductList> getAllEnzymesAnnotationProductList() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationProductList getEnzymesAnnotationProductListInteger(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationProductList(EnzymesAnnotationProductList EnzymesAnnotationProductList) {
		super.delete(EnzymesAnnotationProductList);
		
	}

	@Override
	public void removeEnzymesAnnotationProductListList(List<EnzymesAnnotationProductList> EnzymesAnnotationProductListList) {
		for (EnzymesAnnotationProductList EnzymesAnnotationProductList: EnzymesAnnotationProductListList) {
			this.removeEnzymesAnnotationProductList(EnzymesAnnotationProductList);
		}
	}

	@Override
	public void updateEnzymesAnnotationProductListList(List<EnzymesAnnotationProductList> EnzymesAnnotationProductListList) {
		for (EnzymesAnnotationProductList EnzymesAnnotationProductList: EnzymesAnnotationProductListList) {
			this.update(EnzymesAnnotationProductList);
		}
	}

	@Override
	public void updateEnzymesAnnotationProductList(EnzymesAnnotationProductList EnzymesAnnotationProductList) {
		super.update(EnzymesAnnotationProductList);
		
	}

	@Override
	public List<EnzymesAnnotationProductList> getAllEnzymesAnnotationProductListBySKey(Integer skey) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("SKey", skey);
		List<EnzymesAnnotationProductList> list =  this.findByAttributes(dic);
		return list;
	}
	
	@Override
	public List<EnzymesAnnotationProductList> getAllEnzymesAnnotationProductListByHomologyDataSKey(Integer skey) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("enzymesAnnotationHomologydata.SKey", skey);
		List<EnzymesAnnotationProductList> list =  this.findByAttributes(dic);
		return list;
	}
	
	@Override
	public void removeEnzymesAnnotationProductListByHomologyDataSkey(Integer skey){
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("enzymesAnnotationHomologydata.SKey", skey);
		List<EnzymesAnnotationProductList> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			this.removeEnzymesAnnotationProductListList(res);;
		}
	}
	
	@Override
	public Integer insertEnzymesAnnotationProductListHomologyDataSkeyAndOtherNames(Integer skey, String otherNames){
		EnzymesAnnotationProductList product = new EnzymesAnnotationProductList();
		EnzymesAnnotationHomologyData data = new EnzymesAnnotationHomologyData();
		product.setOtherNames(otherNames);
		data.setSKey(skey);
		product.setEnzymesAnnotationHomologyData(data);

		return (Integer) this.save(product);	
	}

	@Override
	public Boolean checkEnzymesAnnotationProductListByHomologyDataSKey(Integer skey) {
		Map<String, Serializable> eqRestrictions = new HashMap<String, Serializable>();
		eqRestrictions.put("enzymesAnnotationHomologydata.SKey", skey);
		return this.checkByAttributes(eqRestrictions);
	}

}
