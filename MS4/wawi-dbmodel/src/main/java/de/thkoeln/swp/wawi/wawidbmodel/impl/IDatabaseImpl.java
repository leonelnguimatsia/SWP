package de.thkoeln.swp.wawi.wawidbmodel.impl;

import de.thkoeln.swp.wawi.wawidbmodel.services.EntityManagerSingleton;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;

import javax.persistence.EntityManager;

public class IDatabaseImpl implements IDatabase {

	@Override
	public EntityManager getEntityManager() {
		return EntityManagerSingleton.getInstance().getEntityManager();
	}

	@Override
	public void useDevPU() {
		EntityManagerSingleton.getInstance().useDevPU();
	}

	@Override
	public void useProdPU() {
		EntityManagerSingleton.getInstance().useProdPU();
	}

	@Override
	public String getCurrentPU() {
		return EntityManagerSingleton.getInstance().getCurrentPU();
	}
}
