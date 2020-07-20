package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.util.List;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSuperpathwayDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSuperpathway;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSuperpathwayId;

public class ModelSuperpathwayDAOImpl extends GenericDaoImpl<ModelSuperpathway> implements IModelSuperpathwayDAO{

	public ModelSuperpathwayDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelSuperpathway.class);

	}

	@Override 
	public void addSuperpathway(ModelSuperpathway superpathway) {
		super.save(superpathway);

	}

	@Override 
	public void addSuperpathwayList(List<ModelSuperpathway> superpathwayList) {
		for (ModelSuperpathway superpathway: superpathwayList) {
			this.addSuperpathway(superpathway);
		}

	}

	@Override 
	public List<ModelSuperpathway> getAllSuperpathwayList() {
		return super.findAll();
	}

	@Override 
	public ModelSuperpathway getSuperpathway(Integer id) {
		return super.findById(id);
	}

	@Override 
	public void removeSuperpathway(ModelSuperpathway superpathway) {
		super.delete(superpathway);

	}

	@Override 
	public void removeSuperpathwayList(List<ModelSuperpathway> superpathwayList) {
		for (ModelSuperpathway superpathway: superpathwayList) {
			this.removeSuperpathway(superpathway);
		}

	}

	@Override 
	public void updateSuperpathwayList(List<ModelSuperpathway> superpathwayList) {
		for (ModelSuperpathway superpathway: superpathwayList) {
			this.update(superpathway);
		}

	}

	@Override 
	public void updateSuperpathway(ModelSuperpathway superpathway) {
		super.update(superpathway);

	}
	
	@Override
	public void insertModelPathwayIdAndSuperPathway(Integer id, Integer superID){
		ModelSuperpathway superPath = new ModelSuperpathway();
		
		superPath.setId(new ModelSuperpathwayId(id, superID));
		
		this.save(superPath);	
	}
}

