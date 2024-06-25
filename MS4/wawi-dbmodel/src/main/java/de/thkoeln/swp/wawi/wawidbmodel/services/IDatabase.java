package de.thkoeln.swp.wawi.wawidbmodel.services;

import javax.persistence.EntityManager;

public interface IDatabase {	
	public EntityManager getEntityManager();
	public void useDevPU();
	public void useProdPU();
	public String getCurrentPU();
}
