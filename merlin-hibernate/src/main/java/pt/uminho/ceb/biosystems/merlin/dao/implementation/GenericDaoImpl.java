package pt.uminho.ceb.biosystems.merlin.dao.implementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

//import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
//import org.hibernate.criterion.Disjunction;
//import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import pt.uminho.ceb.biosystems.merlin.dao.interfaces.IGenericDao;
import pt.uminho.ceb.biosystems.merlin.entities.model.ModelDblinks;

public class GenericDaoImpl<T> implements IGenericDao<T> {


	protected SessionFactory sessionFactory;
	private Class<T> klass;

	public GenericDaoImpl(SessionFactory sessionFactory, Class<T> klass) {
		this.sessionFactory = sessionFactory;
		this.klass = klass;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public T findById(Serializable id) {
		T T = (T) sessionFactory.getCurrentSession().get(klass, id);
		return T;
	}

	public List<T> findAll() {
		CriteriaQuery<T> c = sessionFactory.getCurrentSession().getCriteriaBuilder().createQuery(klass);
		Root<T> table = c.from(klass);
		c.select(table);
		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);
		return q.getResultList();
	}

	public Path<Object> getPath(String pathString, From<?,?> table) {
		String[] keys = pathString.split("\\.");

		Path<Object> path = table.get(keys[0]);
		for(Integer i=1; i<keys.length; i++) {
			path = path.get(keys[i]);
		}
		return path;
	}

	public List<T> findByAttributes(Map<String, Serializable> eqRestrictions) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(klass);

		Root<T> table = c.from(klass); 
		c.select(table); 

		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : eqRestrictions.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table),entry.getValue())); 

		Predicate p = cb.and(filters.toArray(new Predicate[] {})); 
		c.where(p);

		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);

		List<T> res = q.getResultList();

		return res;
	}
	
	@Override
	public List<T> findBySingleAttribute(String key, Object value) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(klass);

		Root<T> table = c.from(klass); 
		c.select(table); 

		Predicate filter = cb.equal(this.getPath(key, table), value); 

		Predicate p = cb.and(filter); 
		c.where(p);

		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);

		List<T> res = q.getResultList();

		return res;
	}
	
	@Override
	public T findUniqueByAttributes(Map<String, Serializable> eqRestrictions) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(klass);

		Root<T> table = c.from(klass); 
		c.select(table); 

		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : eqRestrictions.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table),entry.getValue())); 

		Predicate p = cb.and(filters.toArray(new Predicate[] {})); 
		c.where(p);

		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);
		
		try {
			return  q.getSingleResult();
		}catch(RuntimeException e) {
			return null;
		}
	}
	
	@Override
	public List<T> findByAttributesAndOrderByColumn(Map<String, Serializable> eqRestrictions, String column, boolean asc) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(klass);

		Root<T> table = c.from(klass); 
		c.select(table); 

		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : eqRestrictions.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table),entry.getValue())); 

		Predicate p = cb.and(filters.toArray(new Predicate[] {})); 
		
		Order[] orderList = {cb.desc(table.get(column))}; 
		
		if(asc)
			orderList = new Order[]{cb.asc(table.get(column))}; 
	
		c.where(p).orderBy(orderList);

		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);

		List<T> res = q.getResultList();

		return res;
	}


	@Override
	public Integer findByAttributesWithMaxInColumn(Map<String, Serializable> eqRestrictions, String maxColumn) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(klass);

		Root<T> table = c.from(klass); 
		c.select((Selection<? extends T>) cb.max(table.get(maxColumn)));

		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (String key : eqRestrictions.keySet()) 
			filters.add(cb.equal(this.getPath(key, table),eqRestrictions.get(key)));

//		filters.add(cb.equal(table.get(maxColumn), cb.max(table.get(maxColumn))));

		Predicate p = cb.and(filters.toArray(new Predicate[] {})); 
		c.having(p);

		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);

		return  (Integer) q.getSingleResult();
	}

	public List<T> findByAttributesWithPagniation(Map<String, Serializable> eqRestrictions, int index, int limit) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(klass);

		Root<T> table = c.from(klass); 
		c.select(table); 

		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : eqRestrictions.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table),entry.getValue())); 

		Predicate p = cb.and(filters.toArray(new Predicate[] {})); 
		c.where(p);

		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);
		
		q.setMaxResults(limit);
		q.setFirstResult(index);
		q.setFetchSize(limit);
		
		return q.getResultList();
	}

	public T findUniqueByAttribute(String attribute, Serializable value) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(klass);

		Root<T> table = c.from(klass); 
		c.select(table); 

		Predicate p = cb.and(cb.equal(this.getPath(attribute, table),value)); 
		c.where(p);

		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);
		
		q.setMaxResults(1);
		q.setFetchSize(1);
		
		try {
			return  q.getSingleResult();
		}catch(RuntimeException e) {
			return null;
		}
	}

	public List<T> findByOrAttributes(Map<String, Serializable> orRestrictions) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(klass);

		Root<T> table = c.from(klass); 
		c.select(table); 

		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : orRestrictions.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table),entry.getValue())); 

		Predicate p = cb.or(filters.toArray(new Predicate[] {}));
		c.where(p);
		
		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);
		
		return q.getResultList();
	}
	
	public Boolean checkIfIsFilled() {
		CriteriaQuery<T> c = sessionFactory.getCurrentSession().getCriteriaBuilder().createQuery(klass);
		
		Root<T> root = c.from(klass);
		c.select(root);
		
		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);
		
		q.setMaxResults(1);
		q.setFetchSize(1);
		
		List<T> result = q.list();
		return !result.isEmpty();
	}
	
	@Override
	public Boolean checkByAttributes(Map<String, Serializable> eqRestrictions) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(klass);

		Root<T> table = c.from(klass); 
		c.select(table); 

		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : eqRestrictions.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table),entry.getValue())); 

		Predicate p = cb.and(filters.toArray(new Predicate[] {})); 
		c.where(p);

		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);
		q.setMaxResults(1);
		q.setFetchSize(1);
		
		try {
			T result = q.getSingleResult();
			if(result != null)
				return true;
		}catch (RuntimeException e) {
			return false;
		}
		return false;
	}
	
	@Override
	public Boolean checkByOrAttributes(Map<String, Serializable> orRestrictions) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(klass);

		Root<T> table = c.from(klass); 
		c.select(table); 

		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : orRestrictions.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table),entry.getValue())); 

		Predicate p = cb.or(filters.toArray(new Predicate[] {}));
		c.where(p);
		
		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);
		
		q.setMaxResults(1);
		q.setFetchSize(1);
		
		try {
			T result = q.getSingleResult();
			if(result != null)
				return true;
		}catch (RuntimeException e) {
			return false;
		}
		return false;
	}

	@Override
	public Boolean checkUniqueByAttributes(Map<String, Serializable> eqRestrictions) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<T> c = cb.createQuery(klass);

		Root<T> table = c.from(klass); 
		c.select(table); 

		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : eqRestrictions.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table),entry.getValue())); 

		Predicate p = cb.and(filters.toArray(new Predicate[] {})); 
		c.where(p);

		Query<T> q = sessionFactory.getCurrentSession().createQuery(c);
		
		try {
			T result = q.getSingleResult();
			if(result != null)
				return true;
		}catch(RuntimeException e) {
			return false;
		}
		return false;
	}

	@Override
	public Long countAll() {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		
		Root<T> table = c.from(klass);
		c.multiselect(cb.count(table));
		
		Query<Long> q = sessionFactory.getCurrentSession().createQuery(c);
		
		return q.getSingleResult();
	}

	@Override
	public Long countByAttributes(Map<String, Serializable> eqRestrictions) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		
		Root<T> table = c.from(klass);
		c.multiselect(cb.count(table));
		
		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : eqRestrictions.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table),entry.getValue())); 

		Predicate p = cb.and(filters.toArray(new Predicate[] {})); 
		c.where(p);
		
		Query<Long> q = sessionFactory.getCurrentSession().createQuery(c);
		
		return q.getSingleResult();
	}

	@Override
	public Long countByOrAttributes(Map<String, Serializable> orRestrictions) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<Long> c = cb.createQuery(Long.class);
		
		Root<T> table = c.from(klass);
		c.multiselect(cb.count(table));
		
		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : orRestrictions.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table),entry.getValue())); 

		Predicate p = cb.or(filters.toArray(new Predicate[] {})); 
		c.where(p);
		
		Query<Long> q = sessionFactory.getCurrentSession().createQuery(c);
		
		return q.getSingleResult();
	}

	@Override
	public void flushSession() {
		sessionFactory.getCurrentSession().flush();

	}

	@Override
	public void clearSession() {
		sessionFactory.getCurrentSession().clear();

	}

	@Override
	public Serializable save(Object object) {
		return sessionFactory.getCurrentSession().save(object);

	}

	@Override
	public void update(Object object) {
		sessionFactory.getCurrentSession().update(object);

	}

	@Override
	public Integer updateAttributesByAndFilterAttributes(Map<String, Serializable> updateAttributes, Map<String, Serializable> filterAttributes) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaUpdate<T> c = cb.createCriteriaUpdate(klass);

		Root<T> table = c.from(klass);

		for (Map.Entry<String, Serializable> entry : updateAttributes.entrySet())
			c.set(this.getPath(entry.getKey(), table), entry.getValue());

		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : filterAttributes.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table), entry.getValue())); 

		Predicate p = cb.and(filters.toArray(new Predicate[] {})); 
		c.where(p);
		return sessionFactory.getCurrentSession().createQuery(c).executeUpdate();
	}

	@Override
	public Integer updateAttributesByOrFilterAttributes(Map<String, Serializable> updateAttributes, Map<String, Serializable> filterAttributes) {
		CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
		CriteriaUpdate<T> c = cb.createCriteriaUpdate(klass);

		Root<T> table = c.from(klass);

		for (Map.Entry<String, Serializable> entry : updateAttributes.entrySet())
			c.set(this.getPath(entry.getKey(), table), entry.getValue());

		List<Predicate> filters = new ArrayList<Predicate>(); 
		for (Map.Entry<String, Serializable> entry : filterAttributes.entrySet()) 
			filters.add(cb.equal(this.getPath(entry.getKey(), table), entry.getValue())); 

		Predicate p = cb.or(filters.toArray(new Predicate[] {}));  
		c.where(p);
		return sessionFactory.getCurrentSession().createQuery(c).executeUpdate();
	}

	@Override
	public void delete(Object object) {
		sessionFactory.getCurrentSession().delete(object);

	}

	@Override
	public void refresh(Object object) {
		sessionFactory.getCurrentSession().refresh(object);

	}

	@Override
	public void evict(Object object) {
		sessionFactory.getCurrentSession().evict(object);

	}

	@Override
	public T merge(Object object) {
		@SuppressWarnings("unchecked")
		T castedObj = (T) sessionFactory.getCurrentSession().merge(object);
		return castedObj;	
	}





}
