package pt.uminho.ceb.biosystems.merlin.dao.implementation.views;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.view.ModelDesnormalizedReactionPathwayAndCompartment;
import pt.uminho.ceb.biosystems.merlin.dao.implementation.GenericDaoImpl;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.views.IDatabaseViewsDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathway;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelPathwayHasReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReaction;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelReactionLabels;

public class DatabaseViewsDaoImpl extends GenericDaoImpl<ModelDesnormalizedReactionPathwayAndCompartment> implements IDatabaseViewsDao{

	public DatabaseViewsDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, ModelDesnormalizedReactionPathwayAndCompartment.class);
		this.sessionFactory = sessionFactory;
	}


	@Override
	public List<ModelDesnormalizedReactionPathwayAndCompartment> getAllReactionsView(Boolean compartimentalized, Boolean encodedOnly) {

		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<ModelDesnormalizedReactionPathwayAndCompartment> c = cb.createQuery(ModelDesnormalizedReactionPathwayAndCompartment.class);
		Root<ModelReaction> data = c.from(ModelReaction.class);

		Join<ModelReaction, ModelPathwayHasReaction> join2 = data.join("modelPathwayHasReactions",JoinType.LEFT);
		Join<ModelPathwayHasReaction, ModelPathway> join3 = join2.join("modelPathway",JoinType.LEFT);
		Root<ModelReactionLabels> labels = c.from(ModelReactionLabels.class);

		List<Predicate> filters = new ArrayList<Predicate>();

		c.multiselect(this.getPath("idreaction", data), this.getPath("name", labels), this.getPath("equation", labels), this.getPath("idpathway", join3),
				this.getPath("name", join3), this.getPath("inModel", data), this.getPath("source", labels), this.getPath("isGeneric", labels),
				this.getPath("modelCompartment.idcompartment", data), this.getPath("notes", data), this.getPath("lowerBound", data), this.getPath("upperBound", data));

		if(encodedOnly) {
			filters.add(cb.equal(this.getPath("inModel", data), encodedOnly));
		}

		Predicate filter= cb.isNull(this.getPath("modelCompartment.idcompartment", data));

		if(compartimentalized) 
			filter = cb.isNotNull(this.getPath("modelCompartment.idcompartment", data));

		filters.add(filter);

		filters.add(cb.equal(data.get("modelReactionLabels").get("idreactionLabel"), labels.get("idreactionLabel")));

		Order[] orderList = {cb.asc( this.getPath("name", join3)), cb.asc(this.getPath("name", labels))}; 

		c.where(cb.and(filters.toArray(new Predicate[] {}))).orderBy(orderList);
		Query<ModelDesnormalizedReactionPathwayAndCompartment> q = super.sessionFactory.getCurrentSession().createQuery(c);
		return q.getResultList();

		//	 	Query query = this.getSessionFactory().getCurrentSession().createQuery(queryString);
		//	 	query.setParameter("encodedOnly", encodedOnly);
		//	 	query.setParameter("isOriginal", !compartimentalized);
		//	 	query.setResultTransformer( Transformers.aliasToBean(ModelDesnormalizedReactionPathwayAndCompartment.class));
		//	 	return query.getResultList();

		//		CriteriaBuilder cb = this.getSessionFactory().getCurrentSession().getCriteriaBuilder();
		//		CriteriaQuery<ModelDesnormalizedReactionPathwayAndCompartment> c = cb.createQuery(ModelDesnormalizedReactionPathwayAndCompartment.class);
		//		Root<ModelReaction> reaction = c.from(ModelReaction.class);
		//		Root<ModelPathwayHasReaction> pathWithReaction = c.from(ModelPathwayHasReaction.class);
		//		Root<ModelPathway> pathway = c.from(ModelPathway.class);
		//		
		//		if(!encodedOnly)
		//			c.where(
		//					cb.and(
		//							cb.equal(reaction.get("idreaction"),pathWithReaction.get("id").get("reactionIdreaction")),
		//							cb.or(
		//									cb.equal(pathway.get("idpathway"), pathWithReaction.get("id").get("pathwayIdpathway")),
		//									cb.isNull(pathway.get("idpathway"))
		//								),
		//							cb.equal(reaction.get("originalReaction"), (compartimentalized)?false:true)
		//						)
		//				);
		//		else
		//			c.where(
		//					cb.and(
		//							cb.equal(reaction.get("idreaction"),pathWithReaction.get("id").get("reactionIdreaction")),
		//							cb.or(
		//									cb.equal(pathway.get("idpathway"), pathWithReaction.get("id").get("pathwayIdpathway")),
		//									cb.isNull(pathway.get("idpathway"))
		//								),
		//							cb.equal(reaction.get("originalReaction"), (compartimentalized)?false:true),
		//							cb.equal(reaction.get("inModel"), true)
		//						)
		//				);
		////		Join<ModelReaction, ModelPathway> reactwithpath = reaction.join("modelPathways", JoinType.LEFT);
		////		reactwithpath.on(cb.or(
		////				cb.equal(, y),
		////				cb.isNull(reactwithpath.get("idpathway"))));
		//
		//		c.multiselect(reaction.get("idreaction").alias("idreaction"), 
		//				reaction.get("name").alias("reactionName"),
		//				reaction.get("equation").alias("reactionEquation"),
		//				reaction.get("reversible").alias("reactionReversible"),
		//				pathway.get("idpathway").alias("idpathway"),
		//				pathway.get("name").alias("pathwayName"),
		//				reaction.get("inModel").alias("reactionInModel"),
		//				reaction.get("source").alias("reactionSource"),
		//				reaction.get("isGeneric").alias("reactionIsGeneric"),
		//				reaction.get("originalReaction").alias("originalReaction"),
		//				reaction.get("modelCompartment").get("idcompartment").alias("compartmentId"),
		//				reaction.get("notes").alias("reactionNotes"));
		//		c.orderBy(cb.asc(pathway.get("name")), cb.asc(reaction.get("name")));
		//		Query<ModelDesnormalizedReactionPathwayAndCompartment> q = this.getSessionFactory().getCurrentSession().createQuery(c);
		//		return q.getResultList();
		/*	SELECT DISTINCT idreaction, model_reaction.name AS reaction_name, equation, reversible, model_pathway.idpathway, model_pathway.name AS pathway_name, model_reaction.inModel, isGeneric, model_reaction.source, originalReaction, compartment_idcompartment, notes
		FROM model_reaction
		LEFT OUTER JOIN model_pathway_has_reaction ON idreaction=model_pathway_has_reaction.reaction_idreaction
		LEFT OUTER JOIN model_pathway ON model_pathway.idpathway=model_pathway_has_reaction.pathway_idpathway
		WHERE model_pathway_has_reaction.pathway_idpathway=model_pathway.idpathway or idpathway IS NULL
		ORDER BY model_pathway.name, model_reaction.name ;*/
	}

}
