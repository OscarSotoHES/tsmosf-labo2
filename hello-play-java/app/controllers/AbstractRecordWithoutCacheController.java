package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import models.IDataRecord;
import models.Lesson;
import models.LessonStudent;
import models.Student;
import models.StudentLesson;

public class AbstractRecordWithoutCacheController<T extends IDataRecord> extends AbstractController {

	private Class<T> entityClass;
	protected AbstractRecordWithoutCacheController(Class<T> entityClass){
		this.entityClass=entityClass;
	}
	public synchronized Class<T> entityClass(){
		return entityClass;
	}
	public String tableName(){
		return entityClass().getAnnotation(Table.class).name();
	}
	
	public List<T> list(){
		List<T> l = list(String.format("select t from %s as t ", entityName()));
		return l;
	}

	public List<T> list(String query, Object...args){
		
		EntityManager em = getEntityManager();
		TypedQuery<T> q = em.createQuery(query, entityClass());
		for(int i=0; i<args.length; i++)
			q.setParameter(i, args[i]);
		List<T> l=q.getResultList();
		ArrayList<T> r = new ArrayList<T>();
		r.addAll(l);
		return r;
	}
	public T get(final Long id){
		return getEntityManager().find(entityClass, id);
	}

	public T get(String query, Object...args){
		return createQuery(query, args).getSingleResult();
	}
	
	public synchronized TypedQuery<T> createQuery(String query){
		return getEntityManager().createQuery(String.format(query, entityName()) , entityClass());
	}
	
	public TypedQuery<T> createQuery(String query, Object...args){
		TypedQuery<T> q = createQuery(query);
		for(int i=0; i<args.length; i++)
			q.setParameter(i, args[i]);
		return q;
	}

	public synchronized T updateIt(T argv){
		EntityManager em = getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try{
			tx.begin();
			argv=em.merge(argv);
			cleanRelations(em);
			tx.commit();
			return argv;
		}catch(Exception ex){
			if(tx.isActive())
				tx.rollback();
			throw ex;
		}
		
	}
	
	private void cleanRelations(EntityManager em) {
		em.createQuery(String.format("delete from %s where studentId is null or lessonId is null", getEntityName(StudentLesson.class))).executeUpdate();
	}
	public synchronized T createIt(T argv){
		EntityManager em = getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try{
			tx.begin();
			em.persist(argv);
			tx.commit();
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

	public synchronized T deleteIt(T argv){
		EntityManager em = getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try{
			tx.begin();
			em.remove(argv);
			cleanRelations(em);
			tx.commit();
			return argv;
		}catch(Exception ex){
			if(tx.isActive())
				tx.rollback();
			throw ex;
		}
	}

	public T deleteIt(Long argv){
		return deleteIt(get(argv));
	}

	public synchronized T refresh(T argv){
		EntityManager em = getEntityManager();
		if(em.contains(argv))
			em.refresh(argv);
		else
			argv=em.merge(argv);
		return argv;
	}

	public String entityName(){

		return getEntityName(entityClass());
	}


	public static <C> String getEntityName(Class<C> clazz){
		String s=clazz.getAnnotation(Entity.class).name();
		if(s==null || s.isEmpty())
			s=clazz.getSimpleName();
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
	

    protected static void updateRelation(Iterable<LessonStudent> l, Lesson model){
    	if(l==null)
    		return;
		for(LessonStudent o:l)
			if(o!=null)
				o.setLessonId(model.getId());
    }
}
