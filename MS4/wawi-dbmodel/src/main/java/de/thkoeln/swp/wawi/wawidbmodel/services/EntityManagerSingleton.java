package de.thkoeln.swp.wawi.wawidbmodel.services;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.sql.SQLException;
import org.h2.tools.Server;

public class EntityManagerSingleton {
	private static EntityManagerSingleton instance;
	private EntityManager emDev;
	private EntityManager emProd;

	private EntityManager emH2;
	private String pu = "WAWIDBPRODPU";
	
	private EntityManagerSingleton() {
		emDev = Persistence.createEntityManagerFactory("WAWIDBDEVPU").createEntityManager();
		emProd = Persistence.createEntityManagerFactory("WAWIDBPRODPU").createEntityManager();
		emH2  = Persistence.createEntityManagerFactory("H2").createEntityManager();
	}
	
	public static EntityManagerSingleton getInstance() {
		if (instance == null) {
			instance = new EntityManagerSingleton();
		}
		try {
			Server.createTcpServer().start();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return instance;
	}
	
	public EntityManager getEntityManager() {
		if (pu.equals("WAWIDBDEVPU")) {
			return emDev;
		}
		else if (pu.equals("WAWIDBPRODPU")){
			return emProd;
		}
		else if (pu.equals("H2")) {
			return emH2;
		}
		else return null;

	}
	
	public void useDevPU() {
		this.pu = "WAWIDBDEVPU";
	}
		
	public void useProdPU() {
		this.pu = "WAWIDBPRODPU";
	}

	public void useH2PU() {
		this.pu = "H2";
	}
		
	public String getCurrentPU() {
		return pu;
	}
}
