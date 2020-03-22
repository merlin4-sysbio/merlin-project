package pt.uminho.ceb.biosystems.merlin.dao.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;

public interface IGenericDao<T> {
	
		public SessionFactory getSessionFactory();
	
		public T findById(Serializable id);

		public List<T> findAll();

		public List<T> findByAttributes(Map<String, Serializable> eqRestrictions);
		
		public Integer findByAttributesWithMaxInColumn(Map<String, Serializable> eqRestrictions, String maxColumn);
		
		public List<T> findByAttributesWithPagniation(Map<String, Serializable> eqRestrictions, int index, int limit);

		public T findUniqueByAttribute(String attribute, Serializable value);

		public List<T> findByOrAttributes(Map<String, Serializable> orRestrictions);
		
		public T findUniqueByAttributes(Map<String, Serializable> eqRestrictions);
		
		public Boolean checkIfIsFilled();
		
		public Boolean checkByAttributes(Map<String, Serializable> eqRestrictions);
		
		public Boolean checkByOrAttributes(Map<String, Serializable> orRestrictions);
		
		public Boolean checkUniqueByAttributes(Map<String, Serializable> eqRestrictions);
		
		public Long countAll();
		
		public Long countByAttributes(Map<String, Serializable> eqRestrictions);
		
		public Long countByOrAttributes(Map<String, Serializable> orRestrictions);
		
		public void flushSession();
		
		public void clearSession();

		public Serializable save(Object object);

		public void update(Object object);
		
		public Integer updateAttributesByAndFilterAttributes(Map<String, Serializable> updateAttributes, Map<String, Serializable> filterAttributes);
		
		public Integer updateAttributesByOrFilterAttributes(Map<String, Serializable> updateAttributes, Map<String, Serializable> filterAttributes);

		public void delete(Object object);

		public void refresh(Object object);

		public void evict(Object object);
		
		public T merge(Object object);

		public List<T> findByAttributesAndOrderByColumn(Map<String, Serializable> eqRestrictions, String column, boolean asc);

		public List<T> findBySingleAttribute(String key, Object value);



}


