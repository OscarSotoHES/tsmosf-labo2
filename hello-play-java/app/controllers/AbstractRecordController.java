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

public class AbstractRecordController<T extends IDataRecord> extends
		AbstractRecordWithCacheController<T> {

	protected AbstractRecordController(Class<T> entityClass) {
		super(entityClass);
	}
}
