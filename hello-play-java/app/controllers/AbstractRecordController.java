package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import models.IDataRecord;

public class AbstractRecordController<T extends IDataRecord> extends AbstractController {


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
}
