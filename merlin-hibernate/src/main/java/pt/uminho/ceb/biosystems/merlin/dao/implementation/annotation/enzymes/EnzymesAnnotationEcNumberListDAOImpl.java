package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberListDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumberList;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologyData;


public class EnzymesAnnotationEcNumberListDAOImpl extends GenericDaoImpl<EnzymesAnnotationEcNumberList> implements IEnzymesAnnotationEcNumberListDAO{

	public EnzymesAnnotationEcNumberListDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationEcNumberList.class);
	
	}

	@Override
	public void addEnzymesAnnotationEcNumberList(EnzymesAnnotationEcNumberList EnzymesAnnotationEcNumberList) {
		super.save(EnzymesAnnotationEcNumberList);
		
	}

	@Override
	public void addEnzymesAnnotationEcNumberListList(List<EnzymesAnnotationEcNumberList> EnzymesAnnotationEcNumberListList) {
		for (EnzymesAnnotationEcNumberList EnzymesAnnotationEcNumberList: EnzymesAnnotationEcNumberListList) {
			this.addEnzymesAnnotationEcNumberList(EnzymesAnnotationEcNumberList);
		}
	}

	@Override
	public List<EnzymesAnnotationEcNumberList> getAllEnzymesAnnotationEcNumberList() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationEcNumberList getEnzymesAnnotationEcNumberList(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationEcNumberList(EnzymesAnnotationEcNumberList EnzymesAnnotationEcNumberList) {
		super.delete(EnzymesAnnotationEcNumberList);
		
	}

	@Override
	public void removeEnzymesAnnotationEcNumberListList(List<EnzymesAnnotationEcNumberList> EnzymesAnnotationEcNumberListList) {
		for (EnzymesAnnotationEcNumberList EnzymesAnnotationEcNumberList: EnzymesAnnotationEcNumberListList) {
			this.removeEnzymesAnnotationEcNumberList(EnzymesAnnotationEcNumberList);
		}
	}

	@Override
	public void updateEnzymesAnnotationEcNumberListList(List<EnzymesAnnotationEcNumberList> EnzymesAnnotationEcNumberListList) {
		for (EnzymesAnnotationEcNumberList EnzymesAnnotationEcNumberList: EnzymesAnnotationEcNumberListList) {
			this.update(EnzymesAnnotationEcNumberList);
		}
	}

	@Override
	public void updateEnzymesAnnotationEcNumberList(EnzymesAnnotationEcNumberList EnzymesAnnotationEcNumberList) {
		super.update(EnzymesAnnotationEcNumberList);	
	}

	@Override
	public List<EnzymesAnnotationEcNumberList> getAllEnzymesAnnotationEcNumberListBySKey(Integer skey) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("SKey", skey);
		List<EnzymesAnnotationEcNumberList> list =  this.findByAttributes(dic);
		return list;
	}
	
	@Override
	public List<EnzymesAnnotationEcNumberList> getAllEnzymesAnnotationEcNumberListByHomologyDataSKey(Integer skey) {
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("enzymesAnnotationHomologydata.SKey", skey);
		List<EnzymesAnnotationEcNumberList> list =  this.findByAttributes(dic);
		return list;
	}
	
	@Override
	public void removeEnzymesAnnotationEcNumberListByHomologyDataSkey(Integer skey){
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("enzymesAnnotationHomologydata.SKey", skey);
		List<EnzymesAnnotationEcNumberList> res = this.findByAttributes(map);
		if (res!=null && res.size()>0) {
			this.removeEnzymesAnnotationEcNumberListList(res);;
		}
	}
	
	@Override
	public Integer insertEnzymesAnnotationEcNumberListSkeyAndOtherEcNumbers(Integer skey, String otherEcnumbers){
		EnzymesAnnotationEcNumberList ec = new EnzymesAnnotationEcNumberList();
		EnzymesAnnotationHomologyData data = new EnzymesAnnotationHomologyData();
		ec.setOtherEcnumbers(otherEcnumbers);
		data.setSKey(skey);
		ec.setEnzymesAnnotationHomologyData(data);

		return (Integer) this.save(ec);	
	}

	@Override
	public Boolean checkEcNumberHasenzymesAnnotationHomologydata(Integer homologyDataSkey) {
		Map<String, Serializable> eqRestrictions = new HashMap<String, Serializable>();
		eqRestrictions.put("enzymesAnnotationHomologyData.SKey", homologyDataSkey);
		return this.checkByAttributes(eqRestrictions);
	}

}

