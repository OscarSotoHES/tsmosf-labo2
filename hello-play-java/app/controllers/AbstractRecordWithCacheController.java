package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

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

public class AbstractRecordWithCacheController<T extends IDataRecord> extends
		AbstractController {

	private Class<T> entityClass;

	protected AbstractRecordWithCacheController(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public Class<T> entityClass() {
		return entityClass;
	}

	public String tableName() {
		return entityClass().getAnnotation(Table.class).name();
	}

	public List<T> list() {
		try {
			List<T> r;
			Callable<List<T>> call = new Callable<List<T>>() {
				@Override
				public List<T> call() throws Exception {
					return list(String.format("select t from %s as t ",
							entityName()));
				}
			};
			r = Cache.getOrElse(entityName(), call, 10000);
			if (r == null || r.isEmpty())
				r = call.call();
			return r;
		} catch (Exception ex) {
			System.out.println("Exception in list(): " + ex);
			return null;
		}
	}

	public List<T> list(String query, Object... args) {

		List<T> r = new ArrayList<T>();
		List<T> l = createQuery(query, args).getResultList();
		if (l == null || l.isEmpty())
			return r;
		for (T o : l)
			r.add(o);
		return r;
	}

	public T get(final Long id) {
		try {
			return Cache.getOrElse(entityName() + "_" + id, new Callable<T>() {
				@Override
				public T call() throws Exception {
					return getEntityManager().find(entityClass, id);
				}
			}, 10000);
		} catch (Exception ex) {
			System.out.println("Exception in get(" + id + ")" + ex);
			return null;
		}
	}

	public T get(String query, Object... args) {
		return createQuery(query, args).getSingleResult();
	}

	public synchronized TypedQuery<T> createQuery(String query) {
		return getEntityManager().createQuery(
				String.format(query, entityName()), entityClass());
	}

	public synchronized TypedQuery<T> createQuery(String query, Object... args) {
		TypedQuery<T> q = createQuery(query);
		for (int i = 0; i < args.length; i++)
			q.setParameter(i, args[i]);
		return q;
	}

	public synchronized T updateIt(T argv) {
		EntityManager em = getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			if (tx != null && tx.isActive() == false)
				tx.begin();
			argv = em.merge(argv);
			cleanRelations(em);
			if (tx != null && tx.isActive() == true)
				tx.commit();

			refreshCache(argv);
			return argv;
		} catch (Exception ex) {
			if (tx != null && tx.isActive() == true)
				tx.rollback();
			throw ex;
		}

	}

	private synchronized void refreshCache(T argv) {
		String entityName = entityName();
		Cache.remove(entityName);
		Cache.set(entityName + "_" + argv.getId(), argv);
	}

	private void removeFromCache(T argv) {
		removeFromCache(argv.getId());
	}

	private void removeFromCache(Long id) {
		String entityName = entityName();
		Cache.remove(entityName);
		Cache.remove(entityName + "_" + id);
	}

	public synchronized T createIt(T argv) {
		EntityManager em = getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			if (tx != null && tx.isActive() == false)
				tx.begin();
			em.persist(argv);
			if (tx != null && tx.isActive() == true)
				tx.commit();
			refreshCache(argv);
			return argv;
		} catch (Exception ex) {
			if (tx != null && tx.isActive() == true)
				tx.rollback();
			throw ex;
		}
	}

	public T saveIt(T argv) {
		if (argv.getId() != null)
			return updateIt(argv);
		return createIt(argv);
	}

	public synchronized T deleteIt(T argv) {
		EntityManager em = getEntityManager();
		EntityTransaction tx = em.getTransaction();
		removeFromCache(argv);
		try {
			if (tx != null && tx.isActive() == false)
				tx.begin();
			em.remove(argv);
			cleanRelations(em);
			if (tx!=null && tx.isActive()==true)
				tx.commit();
			return argv;
		} catch (Exception ex) {
			if (tx!=null && tx.isActive()==true)
				tx.rollback();
			throw ex;
		}
	}

	public T deleteIt(Long argv) {
		removeFromCache(argv);
		return deleteIt(get(argv));
	}

	public synchronized T refresh(T argv) {
		EntityManager em = getEntityManager();
		if (em.contains(argv))
			em.refresh(argv);
		else
			argv = em.merge(argv);
		return argv;
	}

	private void cleanRelations(EntityManager em) {
		em.createQuery(
				String.format(
						"delete from %s where studentId is null or lessonId is null",
						getEntityName(StudentLesson.class))).executeUpdate();
	}

	public static <C> String getEntityName(Class<C> clazz) {
		String s = clazz.getAnnotation(Entity.class).name();
		if (s == null || s.isEmpty())
			s = clazz.getSimpleName();
		return s;
	}

	public String entityName() {
		String s = entityClass().getAnnotation(Entity.class).name();
		if (s == null || s.isEmpty())
			s = entityClass().getSimpleName();
		return s;
	}

	public static <T extends IDataRecord> long nexId(List<T> items) {
		long r = -1;
		for (T o : items)
			if (r < o.getId())
				r = o.getId();
		return r + 1;
	}

	public static <T extends IDataRecord> T remove(Long id, List<T> items) {
		T r = null;
		for (int i = 0; i < items.size(); i++)
			if ((r = items.get(i)) != null && r.getId() == id) {
				items.remove(i);
				return r;
			}
		return null;
	}

	public static <T extends IDataRecord> int indexOf(Long id, List<T> items) {
		T r = null;
		for (int i = 0; i < items.size(); i++)
			if ((r = items.get(i)) != null && r.getId() == id)
				return i;
		return -1;
	}

	public static <T extends IDataRecord> T find(Long id, List<T> items) {
		T r = null;
		for (int i = 0; i < items.size(); i++)
			if ((r = items.get(i)) != null && r.getId() == id)
				return r;
		return null;
	}

	protected static void updateRelation(Iterable<StudentLesson> l,
			Student model) {
		if (l == null)
			return;
		for (StudentLesson o : l)
			if (o != null)
				o.setStudentId(model.getId());
	}

	protected static void updateRelation(Iterable<StudentLesson> l, Lesson model) {
		if (l == null)
			return;
		for (StudentLesson o : l)
			if (o != null)
				o.setLessonId(model.getId());
	}
}
