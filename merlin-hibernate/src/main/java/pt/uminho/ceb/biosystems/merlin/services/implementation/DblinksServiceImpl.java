package pt.uminho.ceb.biosystems.merlin.services.implementation;

import java.util.List;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.model.IModelDblinksDAO;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelDblinks;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelDblinksId;
import pt.uminho.ceb.biosystems.merlin.services.interfaces.IDblinksService;

public class DblinksServiceImpl implements IDblinksService{
	
	
		IModelDblinksDAO modelDblinksDAO;
	

		public DblinksServiceImpl(IModelDblinksDAO modelDblinksDAO) {
			this.modelDblinksDAO = modelDblinksDAO;
			
		}

		@Override
		public boolean checkInternalIdFromDblinks(String class_, int internal, String database) throws Exception {
			boolean exists = false;
			
			List<ModelDblinks> list = this.modelDblinksDAO.getAllModelDblinksByAttributes(class_, internal, database);
			
			if (list != null && list.size() > 0)
				exists = true;
			
			return exists;
		}
		
		@Override
		public void insertDbLinksEntry(String class_, Integer internalId, String database, String externalId) {
			
			ModelDblinks dbLinks = new ModelDblinks();
			
			ModelDblinksId id = new ModelDblinksId();
			
			
			id.setClass_(class_);
			id.setExternalDatabase(database);
			id.setInternalId(internalId);
			
			dbLinks.setId(id);
			dbLinks.setExternalId(externalId);
			
			this.modelDblinksDAO.addModelDblinks(dbLinks);
			
		}

}
