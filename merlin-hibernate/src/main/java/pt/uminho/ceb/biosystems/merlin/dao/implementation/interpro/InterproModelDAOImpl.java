package pt.uminho.ceb.biosystems.merlin.dao.implementation.interpro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.interpro.I_InterproModelDAO;
import pt.uminho.ceb.biosystems.merlin.entities.interpro.InterproModel;

public class InterproModelDAOImpl extends GenericDaoImpl<InterproModel> implements I_InterproModelDAO{

	public InterproModelDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, InterproModel.class);
		
	}

	@Override
	public void addInterproModel(InterproModel interproInterproModel) {
		super.save(interproInterproModel);
		
	}

	@Override
	public void addInterproModelList(List<InterproModel> interproInterproModelList) {
		for (InterproModel interproInterproModel: interproInterproModelList) {
			this.addInterproModel(interproInterproModel);
		}
	}

	@Override
	public List<InterproModel> getAllInterproModel() {
		return super.findAll();
	}

	@Override
	public InterproModel getInterproModel(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeInterproModel(InterproModel interproInterproModel) {
		super.delete(interproInterproModel);
		
	}

	@Override
	public void removeInterproModelList(List<InterproModel> interproInterproModelList) {
		for (InterproModel interproInterproModel: interproInterproModelList) {
			this.removeInterproModel(interproInterproModel);
		}
	}

	@Override
	public void updateInterproModel(List<InterproModel> interproInterproModelList) {
		for (InterproModel interproInterproModel: interproInterproModelList) {
			this.update(interproInterproModel);
		}
	}

	@Override
	public void updateInterproModel(InterproModel interproInterproModel) {
		super.update(interproInterproModel);
	}	
	

	@Override
	public List<String> getInterproModelAccessionByAccession(String accession) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("accession", accession);
		List<InterproModel> res = this.findByAttributes(map);
		List<String> result = new ArrayList<String>();
		if (res!=null && res.size()>0) {
			result.add(res.get(0).getAccession());
		}
		return null;
	}

	@Override
	public Integer insertInterproModelData(String accession, String description, String name){
		InterproModel model = new InterproModel();
		model.setAccession(accession);
		model.setDescription(description);
		model.setName(name);

		return (Integer) this.save(model);	
	}
	
}
