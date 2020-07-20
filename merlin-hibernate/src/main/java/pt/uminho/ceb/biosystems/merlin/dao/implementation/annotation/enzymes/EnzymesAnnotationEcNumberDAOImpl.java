package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationEcNumberDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationEcNumber;


public class EnzymesAnnotationEcNumberDAOImpl extends GenericDaoImpl<EnzymesAnnotationEcNumber> implements IEnzymesAnnotationEcNumberDAO {

	public EnzymesAnnotationEcNumberDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationEcNumber.class);
	
	}

	@Override
	public void addEnzymesAnnotationEcNumber(EnzymesAnnotationEcNumber EnzymesAnnotationEcNumber) {
		super.save(EnzymesAnnotationEcNumber);
		
	}

	@Override
	public void addEnzymesAnnotationEcNumberList(List<EnzymesAnnotationEcNumber> EnzymesAnnotationEcNumberList) {
		for (EnzymesAnnotationEcNumber ecnumber: EnzymesAnnotationEcNumberList) {
			this.addEnzymesAnnotationEcNumber(ecnumber);
		}
		
	}

	@Override
	public List<EnzymesAnnotationEcNumber> getAllEnzymesAnnotationEcNumber() {
		return super.findAll();
	}
	
	@Override
	public EnzymesAnnotationEcNumber getEnzymesAnnotationEcNumber(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationEcNumber(EnzymesAnnotationEcNumber EnzymesAnnotationEcNumber) {
		super.delete(EnzymesAnnotationEcNumber);
		
	}

	@Override
	public void removeEnzymesAnnotationEcNumberList(List<EnzymesAnnotationEcNumber> EnzymesAnnotationEcNumberList) {
		for (EnzymesAnnotationEcNumber ecnumber: EnzymesAnnotationEcNumberList) {
			this.removeEnzymesAnnotationEcNumber(ecnumber);
		}
	}

	@Override
	public void updateEnzymesAnnotationEcNumberList(List<EnzymesAnnotationEcNumber> EnzymesAnnotationEcNumberList) {
		for (EnzymesAnnotationEcNumber ecnumber: EnzymesAnnotationEcNumberList) {
			this.update(ecnumber);
		}
		
	}

	@Override
	public void updateEnzymesAnnotationEcNumber(EnzymesAnnotationEcNumber EnzymesAnnotationEcNumber) {
		super.update(EnzymesAnnotationEcNumber);
		
	}
	
	@Override
	public List<EnzymesAnnotationEcNumber> getAllEnzymesAnnotationEcNumberByEcNumber(String ec){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("ecNumber", ec);
		List<EnzymesAnnotationEcNumber> list =  this.findByAttributes(dic);

		return list;
	}

}
