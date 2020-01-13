package pt.uminho.ceb.biosystems.merlin.biocomponents.container;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.CompartmentCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.GeneCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.MetaboliteCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.StoichiometryValueCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.interfaces.IContainerBuilder;

public class Container extends pt.uminho.ceb.biosystems.mew.biocomponents.container.Container {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Container(IContainerBuilder builder, boolean throwerros) throws IOException {
		populateContainer(builder);
		verifyDepBetweenClass(throwerros);
	}

	public Container(IContainerBuilder builder) throws IOException {
		this(builder, true);
	}


	public void verifyDepBetweenClass(boolean throwerros) throws IOException {

		for (CompartmentCI comp : getCompartments().values())
			comp.setMetabolitesInCompartmentID(new HashSet<String>());

		for (MetaboliteCI met : getMetabolites().values())
			met.setReactionsId(new HashSet<String>());


		Set<String> reactionsToDelete = new HashSet<String>();
		for(ReactionCI rci: getReactions().values()){
			if(rci.getMetaboliteSetIds().size() == 0)
				reactionsToDelete.add(rci.getId());
		}
		
		
		//this block exists to expedite the removal of reactions from the map.
		//remove and removeAll methods could take up to one hour for large models.
		{
			Set<String> existingReactions = new HashSet<>(reactions.keySet());
			existingReactions.removeAll(reactionsToDelete);
			Map<String, ReactionCI> newReactionsMap = new HashMap<>(); 
			for(String r : existingReactions)
				newReactionsMap.put(r, reactions.get(r));

			reactions = newReactionsMap;
		}

		Set<String> genesToRemove = new HashSet<String>();
		genesToRemove.addAll(genes.keySet());

		for (String rid : this.getReactions().keySet()) {
			ReactionCI reaction = this.getReactions().get(rid);
			verifyStoiDep(reaction.getProducts(), rid, throwerros);
			verifyStoiDep(reaction.getReactants(), rid, throwerros);

			for (String g : reaction.getGenesIDs()) {
				if (genesToRemove.contains(g))
					genesToRemove.remove(g);

				GeneCI gene = genes.get(g);
				if (gene == null) {
					gene = new GeneCI(g, "");
					genes.put(g, gene);
				}
				gene.addReactionId(rid);
			}
		}
		
		//this block exists to expedite the removal of genes from the map.
		//remove and removeAll methods could take up to one hour for large models.
		{
			Set<String> existingGenes = new HashSet<>(genes.keySet());
			existingGenes.removeAll(genesToRemove);
			Map<String, GeneCI> newGenesMap = new HashMap<>(); 
			for(String g : existingGenes)
				newGenesMap.put(g, genes.get(g));

			genes = newGenesMap;
		}

		Set<String> metaboliteToRemove = new HashSet<String>();
		for (MetaboliteCI met : metabolites.values())
			if (met.getReactionsId().size() == 0) {
				metaboliteToRemove.add(met.getId());
			}

		if (metaboliteToRemove.size() > 0)
			System.out.println("Metabolites To remove: " + metaboliteToRemove.size() + "/" + metabolites.size());

		for (String id : metaboliteToRemove)
			_removeMetabolite(id);

		clearInfoElements();
	}

	private void verifyStoiDep(Map<String, StoichiometryValueCI> stoi, String reactionId, boolean throwProblem) throws IOException {
		for (StoichiometryValueCI val : stoi.values()) {
			String metaboliteId = val.getMetaboliteId();
			String compartmentId = val.getCompartmentId();
			MetaboliteCI m = metabolites.get(metaboliteId);
			CompartmentCI c = compartments.get(compartmentId);

			if(throwProblem){
				if (m == null)
					throw new IOException("Metabolite " + metaboliteId + " present in reaction " + reactionId
							+ " was not declared");
				if (c == null)
					throw new IOException("Compartment " + compartmentId + " present in reaction " + reactionId
							+ "was not declared");
			}
			if(m!=null)m.getReactionsId().add(reactionId);
			if(c!=null)c.getMetabolitesInCompartmentID().add(metaboliteId);
		}

	}


}
