package pt.uminho.ceb.biosystems.merlin.dao.interfaces.views;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.view.ModelDesnormalizedReactionPathwayAndCompartment;

public interface IDatabaseViewsDao {

	public List<ModelDesnormalizedReactionPathwayAndCompartment> getAllReactionsView(Boolean compartimentalized, Boolean encodedOnly);
	
}
