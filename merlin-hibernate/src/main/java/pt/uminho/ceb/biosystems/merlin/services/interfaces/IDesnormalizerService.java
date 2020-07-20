package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.view.ModelDesnormalizedReactionPathwayAndCompartment;

public interface IDesnormalizerService {

	public List<ModelDesnormalizedReactionPathwayAndCompartment> getAllReactionsView(Boolean compartimentalized, Boolean encodedOnly) throws Exception;
}
