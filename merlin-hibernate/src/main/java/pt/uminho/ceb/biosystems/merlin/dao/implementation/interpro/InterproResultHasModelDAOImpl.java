package pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproResultHasModelDAO;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasModel;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproResultHasModelId;


public class InterproResultHasModelDAOImpl extends GenericDaoImpl<InterproResultHasModel> implements I_InterproResultHasModelDAO{

	public InterproResultHasModelDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, InterproResultHasModel.class);
		
	}

	@Override
	public void addInterproResultHasModel(InterproResultHasModel InterproResultHasModel) {
		super.save(InterproResultHasModel);
		
	}

	@Override
	public void addInterproResultHasModelList(
			List<InterproResultHasModel> InterproResultHasModelList) {
		for (InterproResultHasModel InterproResultHasModel: InterproResultHasModelList) {
			this.addInterproResultHasModel(InterproResultHasModel);
		}
	}

	@Override
	public List<InterproResultHasModel> getAllInterproResultHasModel() {
		return super.findAll();
	}

	@Override
	public InterproResultHasModel getInterproResultHasModel(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeInterproResultHasModel(InterproResultHasModel InterproResultHasModel) {
		super.delete(InterproResultHasModel);
		
	}

	@Override
	public void removeInterproResultHasModelList(
			List<InterproResultHasModel> InterproResultHasModelList) {
		for (InterproResultHasModel InterproResultHasModel: InterproResultHasModelList) {
			this.removeInterproResultHasModel(InterproResultHasModel);
		}
	}

	@Override
	public void updateInterproResultHasModelList(
			List<InterproResultHasModel> InterproResultHasModelList) {
		for (InterproResultHasModel InterproResultHasModel: InterproResultHasModelList) {
			this.update(InterproResultHasModel);
		}
	}

	@Override
	public void updateInterproResultHasModel(InterproResultHasModel InterproResultHasModel) {
		super.update(InterproResultHasModel);
		
	}
	
	@Override
	public List<InterproResultHasModel> getAllInterproResultHasModelByResultIdAndModelAccession(int resultId, String modelAccession) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.resultId", resultId);
		map.put("id.modelAccession", modelAccession);
		List<InterproResultHasModel> res = this.findByAttributes(map);
		
		return res;
	}
	
	@Override
	public Integer insertInterproResultHasModel(String modelAccession, int resultId){
		InterproResultHasModel interpro = new InterproResultHasModel();
		InterproResultHasModelId id = new InterproResultHasModelId();
		id.setInterproModelAccession(modelAccession);
		id.setInterproResultId(resultId);
		interpro.setId(id);
		
		return (Integer) this.save(interpro);	
	}

}
