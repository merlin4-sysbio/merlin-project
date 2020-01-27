package pt.uminho.ceb.biosystems.merlin.services.interfaces;

public interface IDblinksService {
	
	public boolean checkInternalIdFromDblinks(String cl, int internal, String database) throws Exception;

	void insertDbLinksEntry(String class_, Integer internalId, String database, String externalId) throws Exception;
	
}
