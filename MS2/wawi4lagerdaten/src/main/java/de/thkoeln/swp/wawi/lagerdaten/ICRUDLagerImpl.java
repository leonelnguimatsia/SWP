/*
Testklasse fuer die Implementierung von ICRUDLager
Autor: Cyntia Pola
Version: 0.1
Datum: 17.04.22
*/

package de.thkoeln.swp.wawi.lagerdaten;

import de.thkoeln.swp.wawi.datenhaltungapi.ICRUDLager;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Lager;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Lagerort;
import de.thkoeln.swp.wawi.wawidbmodel.exceptions.NoEntityManagerException;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ICRUDLagerImpl implements ICRUDLager {
    private static final Logger LOGGER =
            Logger.getLogger(ICRUDLager.class.getName());

    private EntityManager em;
    @Override
    public void setEntityManager(EntityManager em){this.em = em;}
    @Override
    public boolean insertLager(Lager lager){
        if (em==null)
            throw new NoEntityManagerException();
        if(lager== null||lager.getLagerid() !=null) {
            return false;
        }
        try{
            em.persist(lager);
        }
        catch (PersistenceException pe){
            LOGGER.log(Level.ALL, "insertLager", pe);
            pe.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateLager(Lager lager) {
        if(em == null){
            throw new NoEntityManagerException();
        }
        if (lager==null){
            return false;
        }
        Lager anotherLager = getLagerById(lager.getLagerid());
        if(anotherLager== null){
            return false;
        }
        em.merge(lager);
        return true;
    }

    @Override
    public Lager getLagerById(int lagerId) {
        if(em== null){
            throw new NoEntityManagerException();
        }
        Lager resultLagerId = em.find(Lager.class, lagerId);
        return resultLagerId;
    }

    @Override
    public List<Lager> getAlleLager() {
        if (em == null){
            throw new NoEntityManagerException();
        }

        List<Lager> lagerList = em.createNamedQuery("Lager.findAll").getResultList();

        return lagerList;
    }

    @Override
    public Lagerort getLagerortById(int lagerortId) {
        if(em== null){
            throw new NoEntityManagerException();
        }
        Lagerort resultLager = em.find(Lagerort.class, lagerortId);
        return resultLager;
    }

    @Override
    public List<Lagerort> getLagerortListe() {
        if (em == null){
            throw new NoEntityManagerException();
        }

        List<Lagerort> lagerortList= em.createNamedQuery("Lagerort.findAll").getResultList();

        return lagerortList;
    }

    @Override
    public boolean insertLagerort(Lagerort lagerort) {
        if (em==null)
            throw new NoEntityManagerException();
        if(lagerort==null||lagerort.getLgortid() !=null){return false;}
        try { em.persist(lagerort); }
        catch (PersistenceException pe){
            LOGGER.log(Level.ALL, "insertOrt", pe);
            pe.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateLagerort(Lagerort lagerort) {
      if(em == null){
          throw new NoEntityManagerException();
      }
      if (lagerort==null){
          return false;
      }
      Lagerort anotherLgort = getLagerortById(lagerort.getLgortid());
      if(anotherLgort== null){
          return false;
      }
      em.merge(lagerort);
      return true;
    }

    @Override
    public boolean deleteLagerort(int lgortId) {
        if (em == null) {
            throw new NoEntityManagerException();
        }

        Lagerort lagerort = getLagerortById(lgortId);

        if (lagerort == null) {
            return false;
        }

        em.remove(lagerort);

        return true;
    }


}
