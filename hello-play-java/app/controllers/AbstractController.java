package controllers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import play.mvc.Controller;

public abstract class AbstractController extends Controller {

	private EntityManagerFactory emf = null;
	private EntityManager em = null;
	
	public EntityManagerFactory createEntityManagerFactory(){
		return Persistence.createEntityManagerFactory("defaultPersistenceUnit");
	}
	public synchronized EntityManagerFactory getEntityManagerFactory(){
		if(emf!=null)
			return emf;
		emf=createEntityManagerFactory();
		return emf;
	}
	public EntityManager createEntityManager(){
		return getEntityManagerFactory().createEntityManager();
	}
	public synchronized EntityManager getEntityManager(){
		if(em!=null)
			return em;
		em=createEntityManager();
		return em;
	}

}
