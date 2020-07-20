package pt.uminho.ceb.biosystems.merlin.dao.implementation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

import pt.uminho.ceb.biosystems.merlin.core.containers.model.GeneContainer;
import pt.uminho.ceb.biosystems.merlin.core.utilities.Enumerators.SequenceType;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelSequenceDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelSequence;

public class ModelSequenceDAOImpl extends GenericDaoImpl<ModelSequence> implements IModelSequenceDAO{

	public ModelSequenceDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelSequence.class);

	}

	@Override 
	public void addModelSequence(ModelSequence modelSequence) {
		super.save(modelSequence);

	}

	@Override 
	public void addModelSequenceList(List<ModelSequence> modelSequenceList) {
		for (ModelSequence modelSequence: modelSequenceList) {
			this.addModelSequence(modelSequence);
		}

	}

	@Override 
	public List<ModelSequence> getAllModelSequence() {
		return super.findAll();
	}

	@Override 
	public ModelSequence getModelSequence(Integer id) {
		return super.findById(id);
	}

	@Override 
	public void removeModelSequence(ModelSequence modelSequence) {
		super.delete(modelSequence);

	}

	@Override 
	public void removeModelSequenceList(List<ModelSequence> modelSequenceList) {
		for (ModelSequence modelSequence: modelSequenceList) {
			this.removeModelSequence(modelSequence);
		}

	}

	@Override 
	public void updateModelSequenceList(List<ModelSequence> modelSequenceList) {
		for (ModelSequence modelSequence: modelSequenceList) {
			this.update(modelSequence);
		}

	}

	@Override 
	public void updateModelSequence(ModelSequence modelSequence) {
		super.update(modelSequence);
	}

	@Override 
	public List<Integer> getModelSequenceGeneIdByGeneIdAndSequenceType(int geneid, SequenceType seqtype) {

		Map<String, Serializable> dic = new HashMap<>();
		List<Integer> res = new ArrayList<>();
		dic.put("modelGene.idgene", geneid);
		dic.put("sequenceType", seqtype.toString());

		List<ModelSequence> list =  this.findByAttributes(dic);
		if(list != null) {
			for (ModelSequence x : list) {
				res.add(x.getModelGene().getIdgene());
			}
		}
		return res;
	}

	@Override 
	public List<ModelSequence> getModelSequencesBySequenceType(SequenceType seqtype) {
		Map<String, Serializable> dic = new HashMap<>();
		dic.put("sequenceType", seqtype.toString());

		List<ModelSequence> resultList = this.findByAttributes(dic);

		return resultList;

		//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		//		CriteriaQuery<ModelSequence> c = cb.createQuery(ModelSequence.class);
		//		Root<ModelSequence> modelSequence = c.from(ModelSequence.class);
		//		Root<ModelGene> modelGene = c.from(ModelGene.class);
		//		
		//		c.select(modelSequence);
		//		
		//		Predicate filter1 = cb.equal(modelSequence.get("modelGene").get("idgene"), modelGene.get("idgene"));
		//	    c.where(filter1);
		//
		//		Query<ModelSequence> q = super.sessionFactory.getCurrentSession().createQuery(c);
		//		List<ModelSequence> resultList = q.getResultList();
		//
		//		return resultList;
	}

	@Override 
	public List<GeneContainer> getSequenceByGeneId(int idGene) {

		Map<String, Serializable> dic = new HashMap<>();
		dic.put("modelGene.idgene", idGene);

		List<ModelSequence> genes = this.findByAttributes(dic);

		List<GeneContainer> res = new ArrayList<>();

		if(genes != null) {

			for(ModelSequence gene : genes) {

				GeneContainer container = new GeneContainer(gene.getModelGene().getIdgene());
				
				container.setIdSequence(gene.getIdsequence());
				
				container.setSequenceType(gene.getSequenceType());
				
				if(gene.getSequenceType().equals(SequenceType.PROTEIN.toString())) 
					container.setAasequence(gene.getSequence());
				else
					container.setNtsequence(gene.getSequence());

				res.add(container);
			}
		}
		return res;
	}

	@Override
	public boolean checkGenomeSequencesByType(SequenceType type) {
		
		Map<String, Serializable> dic = new HashMap<>();
		dic.put("sequenceType", type.toString());

		List<ModelSequence> genes = this.findByAttributes(dic);
		
		if(genes != null && genes.size() > 0)
			return true;
		
		return false;
	}
	
	@Override
	public Integer countSequencesByType(SequenceType type) {
		
		Map<String, Serializable> dic = new HashMap<>();
		dic.put("sequenceType", type.toString());

		List<ModelSequence> genes = this.findByAttributes(dic);
		
		return genes.size();
	}
}
