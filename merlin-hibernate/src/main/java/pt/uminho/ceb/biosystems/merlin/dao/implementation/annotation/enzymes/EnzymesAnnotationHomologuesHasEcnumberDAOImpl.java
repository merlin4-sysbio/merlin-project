package pt.uminho.ceb.biosystems.merlin.dao.implementation.annotation.enzymes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.annotation.enzymes.IEnzymesAnnotationHomologuesHasEcNumberDAO;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologuesHasEcNumber;
import pt.uminho.ceb.biosystems.merlin.entities.annotation.enzymes.EnzymesAnnotationHomologuesHasEcNumberId;


public class EnzymesAnnotationHomologuesHasEcnumberDAOImpl extends GenericDaoImpl<EnzymesAnnotationHomologuesHasEcNumber> implements IEnzymesAnnotationHomologuesHasEcNumberDAO {

	public EnzymesAnnotationHomologuesHasEcnumberDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, EnzymesAnnotationHomologuesHasEcNumber.class);
		
	}

	@Override
	public void addEnzymesAnnotationHomologuesHasEcNumber(EnzymesAnnotationHomologuesHasEcNumber EnzymesAnnotationHomologuesHasEcNumber) {
		super.save(EnzymesAnnotationHomologuesHasEcNumber);
		
	}

	@Override
	public void addEnzymesAnnotationHomologuesHasEcNumber(List<EnzymesAnnotationHomologuesHasEcNumber> EnzymesAnnotationHomologuesHasEcNumberList) {
		for (EnzymesAnnotationHomologuesHasEcNumber EnzymesAnnotationHomologuesHasEcNumber: EnzymesAnnotationHomologuesHasEcNumberList) {
			this.addEnzymesAnnotationHomologuesHasEcNumber(EnzymesAnnotationHomologuesHasEcNumber);
		}
	}

	@Override
	public List<EnzymesAnnotationHomologuesHasEcNumber> getAllEnzymesAnnotationHomologuesHasEcNumber() {
		return super.findAll();
	}

	@Override
	public EnzymesAnnotationHomologuesHasEcNumber getEnzymesAnnotationHomologuesHasEcNumber(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeEnzymesAnnotationHomologuesHasEcNumber(EnzymesAnnotationHomologuesHasEcNumber EnzymesAnnotationHomologuesHasEcNumber) {
		super.delete(EnzymesAnnotationHomologuesHasEcNumber);
		
	}

	@Override
	public void removeEnzymesAnnotationHomologuesHasEcNumberList(List<EnzymesAnnotationHomologuesHasEcNumber> EnzymesAnnotationHomologuesHasEcNumberList) {
		for (EnzymesAnnotationHomologuesHasEcNumber EnzymesAnnotationHomologuesHasEcNumber: EnzymesAnnotationHomologuesHasEcNumberList) {
			this.removeEnzymesAnnotationHomologuesHasEcNumber(EnzymesAnnotationHomologuesHasEcNumber);
		}
		
	}

	@Override
	public void updateEnzymesAnnotationHomologuesHasEcNumberList(List<EnzymesAnnotationHomologuesHasEcNumber> EnzymesAnnotationHomologuesHasEcNumberList) {
		for (EnzymesAnnotationHomologuesHasEcNumber EnzymesAnnotationHomologuesHasEcNumber: EnzymesAnnotationHomologuesHasEcNumberList) {
			this.update(EnzymesAnnotationHomologuesHasEcNumber);
		}
	}

	@Override
	public void updateEnzymesAnnotationHomologuesHasEcNumber(EnzymesAnnotationHomologuesHasEcNumber EnzymesAnnotationHomologuesHasEcNumber) {
		super.update(EnzymesAnnotationHomologuesHasEcNumber);
		
	}

	@Override
	public List<EnzymesAnnotationHomologuesHasEcNumber> getAllEnzymesAnnotationHomologuesHasEcNumberByAttributes(Integer hom_skey, Integer ec_skey) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.enzymesAnnotationHomologuesSKey", hom_skey);
		map.put("id.ecNumberSKey", ec_skey);
		List<EnzymesAnnotationHomologuesHasEcNumber> res = this.findByAttributes(map);
		return res;
	}
	
	@Override
	public void insertEnzymesAnnotationHomologuesHasEcNumber(int homologues_s_key, int ecnumber_s_key){
		
		EnzymesAnnotationHomologuesHasEcNumber homHasEcNumber= new EnzymesAnnotationHomologuesHasEcNumber();
		EnzymesAnnotationHomologuesHasEcNumberId homHasEcNumberId= new EnzymesAnnotationHomologuesHasEcNumberId();

		homHasEcNumberId.setEnzymesAnnotationHomologuesSKey(homologues_s_key);
		homHasEcNumberId.setEcNumberSKey(ecnumber_s_key);
		homHasEcNumber.setId(homHasEcNumberId);
		
		this.save(homHasEcNumber);
		
				
	}

}
