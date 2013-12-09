package models;

import java.io.Serializable;

public interface IDataRecord extends Serializable{
	public abstract Long getId();
	public abstract void setId(Long argv);
}
