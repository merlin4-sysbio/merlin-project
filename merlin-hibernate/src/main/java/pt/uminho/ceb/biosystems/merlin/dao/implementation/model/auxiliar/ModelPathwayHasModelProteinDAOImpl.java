package pt.uminho.ceb.biosystems.merlin.dao.implementation.model.auxiliar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.PathwayContainer;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelPathwayHasModelProteinDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelCompound;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathway;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasModelProteinId;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionHasModelProtein;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelStoichiometry;

public class ModelPathwayHasModelProteinDAOImpl extends GenericDaoImpl<ModelPathwayHasModelProtein> implements IModelPathwayHasModelProteinDAO {

	public ModelPathwayHasModelProteinDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelPathwayHasModelProtein.class);
		
	}
	
	@Override
	public void addModelPathwayHasModelProtein(ModelPathwayHasModelProtein modelPathwayHasEnzyme) {
		super.save(modelPathwayHasEnzyme);
		
	}

	@Override
	public void addModelPathwayHasModelProteinList(List<ModelPathwayHasModelProtein> modelPathwayHasEnzymeList) {
		for (ModelPathwayHasModelProtein modelPathwayHasEnzyme: modelPathwayHasEnzymeList) {
			this.addModelPathwayHasModelProtein(modelPathwayHasEnzyme);
		}
		
	}

	@Override
	public List<ModelPathwayHasModelProtein> getAllModelPathwayHasModelProtein() {
		return super.findAll();
	}

	@Override
	public ModelPathwayHasModelProtein getModelPathwayHasModelProtein(Integer id) {
		return super.findById(id);
	}

	@Override
	public void removeModelPathwayHasModelProtein(ModelPathwayHasModelProtein modelPathwayHasEnzyme) {
		super.delete(modelPathwayHasEnzyme);
		
	}

	@Override
	public void removeModelPathwayHasModelProteinList(List<ModelPathwayHasModelProtein> modelPathwayHasEnzymeList) {
		for (ModelPathwayHasModelProtein modelPathwayHasEnzyme: modelPathwayHasEnzymeList) {
			this.removeModelPathwayHasModelProtein(modelPathwayHasEnzyme);
		}
		
	}

	@Override
	public void updateModelPathwayHasModelProteinList(List<ModelPathwayHasModelProtein> modelPathwayHasEnzymeList) {
		for (ModelPathwayHasModelProtein modelPathwayHasEnzyme: modelPathwayHasEnzymeList) {
			this.update(modelPathwayHasEnzyme);
		}
		
	}

	@Override
	public void updateModelPathwayHasModelProtein(ModelPathwayHasModelProtein modelPathwayHasEnzyme) {
		super.update(modelPathwayHasEnzyme);
		
	}
	
	@Override
	public List<ModelPathwayHasModelProtein> getAllModelPathwayHasModelProteinByEcNumberAndPathwayIdAndProteinId(Integer pathId, Integer enzymeProteinIdprotein){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelPathwayIdpathway", pathId);
		dic.put("id.modelProteinIdprotein", enzymeProteinIdprotein);
		List<ModelPathwayHasModelProtein> list =  this.findByAttributes(dic);

		return list;
	}
	
	@Override
	public List<ModelPathwayHasModelProtein> getAllModelPathwayHasModelProteinByPathwayIdAndProteinId(Integer pathId, Integer enzymeProteinIdprotein){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelPathwayIdpathway", pathId);
		dic.put("id.modelProteinIdprotein", enzymeProteinIdprotein);
		List<ModelPathwayHasModelProtein> list =  this.findByAttributes(dic);

		return list;
	}
	
	@Override
	public List<ModelPathwayHasModelProtein> getAllModelPathwayHasModelProteinByEcNumber(Integer idProtein){
		Map<String, Serializable> dic = new HashMap<String, Serializable>();
		dic.put("id.modelProteinIdprotein", idProtein);
		List<ModelPathwayHasModelProtein> list =  this.findByAttributes(dic);

		return list;
	}
	
	@Override
	public List<Integer> getAllModelPathwayHasModelProteinIdByPathId(Integer id){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathwayHasModelProtein> c = cb.createQuery(ModelPathwayHasModelProtein.class);
		Root<ModelPathwayHasModelProtein> pathEnz = c.from(ModelPathwayHasModelProtein.class);
		
		c.select(pathEnz).distinct(true); 

	    Predicate filter1 = cb.equal(pathEnz.get("id").get("modelPathwayIdpathway"), id);
	    
	    c.where(cb.and(filter1));
	    
	    Query<ModelPathwayHasModelProtein> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelPathwayHasModelProtein> list = q.getResultList();

		List<Integer> res = new ArrayList<Integer>();
		
		if(list != null && list.size() > 0) {
			
			for (ModelPathwayHasModelProtein x : list) {
				res.add(x.getModelProtein().getIdprotein());
			}
		}
			
		return res;
	}
	
	@Override
	public ModelPathwayHasModelProteinId insertModelPathwayHasModelProtein(Integer pathId, Integer proteinId) {
		ModelPathwayHasModelProtein model = new ModelPathwayHasModelProtein();
		ModelPathwayHasModelProteinId id = new ModelPathwayHasModelProteinId();
		id.setModelProteinIdprotein(proteinId);
		id.setModelPathwayIdpathway(pathId);
		model.setId(id);
		
		return (ModelPathwayHasModelProteinId) this.save(model);
	}

	@Override
	public Map<Integer,PathwayContainer> getDistinctModelPathwayHasModelProteinCodeAndNameByAttributes(Integer proteinId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelPathway> c = cb.createQuery(ModelPathway.class);
		Root<ModelPathwayHasModelProtein> pathEnz = c.from(ModelPathwayHasModelProtein.class);
		Root<ModelPathway> path = c.from(ModelPathway.class);

	    c.select(path); 
	    Predicate filter1 = cb.equal(path.get("idpathway"), pathEnz.get("id").get("modelPathwayIdpathway"));
	    Predicate filter3 = cb.equal(pathEnz.get("id").get("modelProteinIdprotein"), proteinId);
	    c.where(cb.and(filter1, filter3));

	   
		Query<ModelPathway> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<ModelPathway> list = q.getResultList();
		Map<Integer,PathwayContainer> res = new HashMap<Integer,PathwayContainer>();
		if(list.size() > 0) {
			for(ModelPathway result: list) {
				
				PathwayContainer container = new PathwayContainer(result.getCode(), result.getName());
				
				res.put(proteinId,container);
			}
		}
		return res;
	}
		
	@Override
	public List<String[]> getModelPathwayHasModelProteinAttributesByPathwayId(Integer pathId, boolean isCompartimentalized){ //nao tem aux
		
 		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Object[]> c = cb.createQuery(Object[].class);
		Root<ModelPathwayHasModelProtein> pathEnz = c.from(ModelPathwayHasModelProtein.class);
		Root<ModelProtein> protein = c.from(ModelProtein.class);
		Root<ModelReactionHasModelProtein> reactionHasenzyme = c.from(ModelReactionHasModelProtein.class);
		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		Root<ModelStoichiometry> stoich = c.from(ModelStoichiometry.class);
		Root<ModelCompound> compound = c.from(ModelCompound.class);
		Root<ModelPathwayHasReaction> pathHasReaction = c.from(ModelPathwayHasReaction.class);
		Root<ModelReactionLabels> reactionLabels = c.from(ModelReactionLabels.class);
		
				
	    c.multiselect(protein.get("ecnumber"), protein.get("idprotein"),
	    		reaction.get("inModel"), reactionLabels.get("name"), protein.get("inModel"), protein.get("name"),
	    		compound.get("externalIdentifier")); 

	    Predicate filter1 = cb.equal(pathEnz.get("id").get("modelProteinIdprotein"), reactionHasenzyme.get("id").get("modelProteinIdprotein"));
	    
	    Predicate filter3 = cb.equal(reaction.get("idreaction"), reactionHasenzyme.get("id").get("modelReactionIdreaction"));
	    Predicate filter4 = cb.equal(reaction.get("idreaction"), stoich.get("modelReaction").get("idreaction"));
	    Predicate filter5 = cb.equal(compound.get("idcompound"), stoich.get("modelCompound").get("idcompound"));
	    Predicate filter6 = cb.equal(reaction.get("idreaction"), pathHasReaction.get("id").get("reactionIdreaction"));
	    Predicate filter11 = cb.equal(reaction.get("modelReactionLabels").get("idreactionLabel"), reactionLabels.get("idreactionLabel"));
	    
	    Predicate filter13 = cb.equal(protein.get("idprotein"), reactionHasenzyme.get("id").get("modelProteinIdprotein"));
	    
	    
	    Predicate filter9 = cb.equal(pathEnz.get("id").get("modelPathwayIdpathway"), pathId);
	    Predicate filter10 = cb.equal(pathHasReaction.get("id").get("pathwayIdpathway"), pathId);
	    
	    
	    Predicate filter12 = cb.isNull(reaction.get("modelCompartment").get("idcompartment"));
		if(isCompartimentalized) 
			filter12 = cb.isNotNull(reaction.get("modelCompartment").get("idcompartment"));
		
		
	    
	    c.where(cb.and(filter1, filter3, filter4, filter5, filter6, filter9, filter10, filter11, filter12, filter13));

		Query<Object[]> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Object[]> resultList = q.getResultList();
		
		List<String[]> parsedList = new ArrayList<String[]>();
		
		if(resultList != null && resultList.size() > 0) {
			
			for(Object[] item: resultList) {

				String[] list = new String[7];
				list[0] = String.valueOf(item[0]);
				list[1] = String.valueOf(item[1]);
				list[2] = String.valueOf(item[2]);
				list[3] = (String) item[3];
				list[4] = String.valueOf(item[4]);
				list[5] = (String) item[5];
				list[6] = (String) item[6];
				parsedList.add(list);
				
			}
		}
		return parsedList;
	}

	@Override
	public List<Integer> getModelPathwayHasModelProteinReactionIdByPathwayIdAndProteinId(Integer pathId, Integer proteinId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);
		Root<ModelPathwayHasModelProtein> pathEnz = c.from(ModelPathwayHasModelProtein.class);
		Root<ModelReactionHasModelProtein> reactEnz = c.from(ModelReactionHasModelProtein.class);
		Root<ModelPathwayHasReaction> pathReact = c.from(ModelPathwayHasReaction.class);
	
	    c.select(reactEnz.get("id").get("modelReactionIdreaction")); 
	    
	    Predicate filter1 = cb.equal(pathEnz.get("id").get("modelProteinIdprotein"), reactEnz.get("id").get("modelProteinIdprotein"));
	    Predicate filter3 = cb.equal(pathEnz.get("id").get("modelPathwayIdpathway"), pathReact.get("id").get("pathwayIdpathway"));
	    Predicate filter4 = cb.equal(reactEnz.get("id").get("modelReactionIdreaction"), pathReact.get("id").get("reactionIdreaction"));
	    Predicate filter6 = cb.equal(pathEnz.get("id").get("modelProteinIdprotein"), proteinId);
	    Predicate filter7 = cb.equal(pathEnz.get("id").get("modelPathwayIdpathway"), pathId);
	    
	    c.where(cb.and(filter1, filter3, filter4, filter6, filter7));

	   
		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer> list = q.getResultList();
		
		List<Integer> res_list = new ArrayList<Integer>();
		
		if(list != null && list.size() > 0) {
			
			for(Integer result: list) {
				
				res_list.add(result);
				
			}
			return res_list;
		}
		return null;
	}
			
	@Override
	public List<Integer> getModelPathwayHasModelProteinReactionIdByReactionIdAndProteinId(Integer reactId, Integer proteinId){
		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Integer> c = cb.createQuery(Integer.class);
		Root<ModelPathwayHasModelProtein> pathEnz = c.from(ModelPathwayHasModelProtein.class);
		Root<ModelReactionHasModelProtein> reactEnz = c.from(ModelReactionHasModelProtein.class);
		Root<ModelPathwayHasReaction> pathReact = c.from(ModelPathwayHasReaction.class);
	
	    c.select(reactEnz.get("id").get("modelReactionIdreaction")); 
	    Predicate filter1 = cb.equal(pathEnz.get("id").get("modelProteinIdprotein"), reactEnz.get("id").get("modelProteinIdprotein"));
	    Predicate filter3 = cb.equal(pathEnz.get("id").get("modelPathwayIdpathway"), pathReact.get("id").get("pathwayIdpathway"));
	    Predicate filter4 = cb.equal(reactEnz.get("id").get("modelReactionIdreaction"), pathReact.get("id").get("reactionIdreaction"));
	    Predicate filter6 = cb.equal(pathEnz.get("id").get("modelProteinIdprotein"), proteinId);
	    Predicate filter7 = cb.equal(reactEnz.get("id").get("modelReactionIdreaction"), reactId);
	    
	    c.where(cb.and(filter1, filter3, filter4, filter6, filter7));

		Query<Integer> q = super.sessionFactory.getCurrentSession().createQuery(c);
		List<Integer> list = q.getResultList();
		List<Integer> res_list = new ArrayList<Integer>();
		
		if(list != null && list.size() > 0) {
			for(Integer result: list) {
				res_list.add(result);
			}
		return res_list;
		}
		return null;
	}
	
	@Override
	public boolean deleteModelPathwayHasModelProteinByPathwayId(Integer pathId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.modelPathwayIdpathway", pathId);
	
		List<ModelPathwayHasModelProtein> list =  this.findByAttributes(map);
		this.removeModelPathwayHasModelProteinList(list);
		return true;
	}
	
	@Override
	public boolean deleteModelPathwayHasModelProteinByIdProtein(Integer proteinId) {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id.modelProteinIdprotein", proteinId);
	
		List<ModelPathwayHasModelProtein> list =  this.findByAttributes(map);
		this.removeModelPathwayHasModelProteinList(list);
		return true;
	}
			
}
