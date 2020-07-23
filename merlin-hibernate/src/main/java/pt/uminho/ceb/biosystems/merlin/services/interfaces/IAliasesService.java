package pt.uminho.ceb.biosystems.merlin.services.interfaces;

import java.util.List;

public interface IAliasesService {
	
	public List<String> getSynonyms(String class_, int entity) throws Exception;
	
	public boolean checkAliasExistence(String class_, Integer entity, String name) throws Exception;
	
	public List<String> getAliasClassTU(Integer id) throws Exception;
	
	public List<String> getAliasClassG(Integer id) throws Exception;
	
	public List<String> getAliasClassR(Integer id) throws Exception;
	
	public List<String[]> getAliasClassP(Integer id) throws Exception;
	
	public long countGenesSynonyms(String class_) throws Exception;
	
	public long countProteinsSynonyms(String class_) throws Exception;
	
	public boolean checkEntityFromAliases(String cl, int entity, String alias) throws Exception;
	
	public List<String> getAliasClassC(Integer id) throws Exception;

	public void insertNewModelAliasEntry(String cl, int entity, String alias) throws Exception;

	void updateModelAlias(int modelAliasId, String cl, int entity, String alias) throws Exception;

	void removeModelAlias(int modelAliasId) throws Exception;


}
