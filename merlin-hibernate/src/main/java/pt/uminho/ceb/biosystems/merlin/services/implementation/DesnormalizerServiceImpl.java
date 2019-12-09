package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.core.datatypes.view.ModelDesnormalizedReactionPathwayAndCompartment;
import pt.uminho.ceb.biosystems.merlin.dao.interfaces.views.IDatabaseViewsDao;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IDesnormalizerService;

public class DesnormalizerServiceImpl implements IDesnormalizerService{

	private IDatabaseViewsDao databaseviewsDao;

	public DesnormalizerServiceImpl(IDatabaseViewsDao databaseviewsDao) {
		this.databaseviewsDao = databaseviewsDao;
	}
	
	@Override
	public List<ModelDesnormalizedReactionPathwayAndCompartment> getAllReactionsView(Boolean compartimentalized, Boolean encodedOnly) throws Exception {
		return this.databaseviewsDao.getAllReactionsView(compartimentalized, encodedOnly);
	}

}
