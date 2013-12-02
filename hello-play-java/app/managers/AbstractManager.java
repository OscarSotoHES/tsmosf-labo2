package managers;

import java.util.List;

import models.IDataRecord;

public abstract class AbstractManager<T extends IDataRecord> {

	public abstract T createEmpty();
	public abstract T create(T argv);
	public abstract T get(Long id);
	public abstract T update(T argv);
	public abstract T delete(T argv);
	public abstract List<T> list();

}
