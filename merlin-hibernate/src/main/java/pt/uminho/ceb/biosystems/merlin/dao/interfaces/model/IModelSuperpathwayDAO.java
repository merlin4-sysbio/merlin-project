package pt.uminho.ceb.biosystems.merlin.dao.interfaces.model;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSuperpathway;

public interface IModelSuperpathwayDAO extends IGenericDao<ModelSuperpathway>{

	public void addSuperpathway(ModelSuperpathway superpathway);

	public void addSuperpathwayList(List<ModelSuperpathway> superpathwayList);

	public List<ModelSuperpathway> getAllSuperpathwayList();

	public ModelSuperpathway getSuperpathway(Integer id);

	public void removeSuperpathway(ModelSuperpathway superpathway);

	public void removeSuperpathwayList(List<ModelSuperpathway> superpathwayList);

	public void updateSuperpathwayList(List<ModelSuperpathway> superpathwayList);

	public void updateSuperpathway(ModelSuperpathway superpathway);

	public void insertModelPathwayIdAndSuperPathway(Integer id, Integer superID);

}



