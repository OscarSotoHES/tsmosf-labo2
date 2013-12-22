package controllers;

import java.util.List;
import java.util.concurrent.*;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import models.IDataRecord;
import models.Lesson;
import models.Student;
import models.StudentLesson;

import play.cache.Cache;

public class AbstractRecordController<T extends IDataRecord> extends AbstractController {

	private Class<T> entityClass;
	protected AbstractRecordController(Class<T> entityClass){
		this.entityClass=entityClass;
		refreshCache();
	}
	
	public Class<T> entityClass(){
		return entityClass;
	}
	
	public String tableName(){
		return entityClass().getAnnotation(Table.class).name();
	}
	
	public List<T> list(){
		return (List<T>) Cache.get(entityName());
	}

	public List<T> list(String query, Object...args){
		return createQuery(query, args).getResultList();
	}
	
	public T get(final Long id){
		try
		{
			return Cache.getOrElse(entityName() + "_" + id, new Callable<T>() {
				 @Override
			        public T call() throws Exception {	
					return getEntityManager().find(entityClass, id);
			        }
			}, 10000);
		}
		catch(Exception ex)
		{
			System.out.println("Exception in get(" + id + ")" + ex);
			return null;
		}
	}

	public T get(String query, Object...args){
		return createQuery(query, args).getSingleResult();
	}
	
	public TypedQuery<T> createQuery(String query){
		return getEntityManager().createQuery(String.format(query, entityName()) , entityClass());
	}
	
	public TypedQuery<T> createQuery(String query, Object...args){
		TypedQuery<T> q = createQuery(query);
		for(int i=0; i<args.length; i++)
			q.setParameter(i, args[i]);
		return q;
	}

	public T updateIt(T argv){
		EntityManager em = getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try{
			tx.begin();
			argv=em.merge(argv);
			tx.commit();
			refreshCache(argv);
			return argv;
		}catch(Exception ex){
			if(tx.isActive())
				tx.rollback();
			throw ex;
		}
	}
	
	private void refreshCache() {
		Cache.set(entityName(), getEntityManager().createQuery(String.format("select t from %s as t ",
			entityName()) , entityClass()).getResultList());
	}
	
	private void refreshCache(T argv) {
		refreshCache();
		Cache.set(entityName() + "_" + argv.getId(), argv);
	}
	
	private void removeFromCache(T argv){
		removeFromCache(argv.getId());
	}

	private void removeFromCache(Long id){
	        String entityName = entityName();
                Cache.remove(entityName);
                Cache.remove(entityName + "_" + id);
	}

	public T createIt(T argv){
		EntityManager em = getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try{
			tx.begin();
			em.persist(argv);
			tx.commit();
			refreshCache(argv);
			return argv;
		}catch(Exception ex){
			if(tx.isActive())
				tx.rollback();
			throw ex;
		}
	}

	public T saveIt(T argv){
		if(argv.getId()!=null)
			return updateIt(argv);
		return createIt(argv);
	}

	public T deleteIt(T argv){
		removeFromCache(argv);
		EntityManager em = getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try{
			tx.begin();
			em.remove(argv);
			tx.commit();
			return argv;
		}catch(Exception ex){
			if(tx.isActive())
				tx.rollback();
			throw ex;
		}
		refreshCache();
	}

	public T deleteIt(Long argv){
		removeFromCache(argv);
		return deleteIt(get(argv));
	}

	public T refresh(T argv){
		EntityManager em = getEntityManager();
		if(em.contains(argv))
			em.refresh(argv);
		else
			argv=em.merge(argv);
		return argv;
	}

	public String entityName(){
		String s=entityClass().getAnnotation(Entity.class).name();
		if(s==null || s.isEmpty())
			s=entityClass().getSimpleName();
		return s;
	}

	public static <T extends IDataRecord> long nexId(List<T> items){
		long r=-1;
		for(T o:items)
			if(r<o.getId())
				r=o.getId();
		return r+1;
	}
	public static <T extends IDataRecord> T remove(Long id, List<T> items){
		T r=null;
		for(int i=0; i<items.size(); i++)
			if((r=items.get(i))!=null && r.getId()==id){
				items.remove(i);
				return r;
			}
		return null;
	}
	public static <T extends IDataRecord> int indexOf(Long id, List<T> items){
		T r=null;
		for(int i=0; i<items.size(); i++)
			if((r=items.get(i))!=null && r.getId()==id)
				return i;
		return -1;
	}
	public static <T extends IDataRecord> T find(Long id, List<T> items){
		T r=null;
		for(int i=0; i<items.size(); i++)
			if((r=items.get(i))!=null && r.getId()==id)
				return r;
		return null;
	}
	

    protected static void updateRelation(Iterable<StudentLesson> l, Student model){
    	if(l==null )
    		return;
		for(StudentLesson o:l)
			if(o!=null)
				o.setStudentId(model.getId());
    }
	

    protected static void updateRelation(List<StudentLesson> l, Lesson model){
    	if(l==null || l.isEmpty())
    		return;
		for(StudentLesson o:l)
			if(o!=null)
				o.setLessonId(model.getId());
    }
}
