package de.thkoeln.swp.wawi.lagerdaten;

import de.thkoeln.swp.wawi.datenhaltungapi.ICRUDLager;
import de.thkoeln.swp.wawi.datenhaltungapi.ILagerService;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Einlieferung;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Lagerort;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Lagerverkehr;
import de.thkoeln.swp.wawi.wawidbmodel.exceptions.NoEntityManagerException;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.util.List;

public class ILagerServiceImpl implements ILagerService {

    private EntityManager em;
    private static final Logger LOGGER = Logger.getLogger(ICRUDLager.class.getName());


    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public List<Einlieferung> getAlleEinlieferungen() {
        if (em == null){
            throw new NoEntityManagerException();
        }
        return em.createNamedQuery("Einlieferung.findAll", Einlieferung.class).getResultList() ;
    }

    @Override
    public List<Einlieferung> getEinlieferungenByDatum(LocalDate localDate, LocalDate localDate1) {

        if (em == null){
            throw new NoEntityManagerException();
        }

        List<Einlieferung> einlieferungList = em.createNamedQuery("Einlieferung.findAll", Einlieferung.class).getResultList();
        List<Einlieferung> intervallListe = new ArrayList<>();
        for(int j = 0; j < einlieferungList.size(); j++){
            Einlieferung e1 = einlieferungList.get(j) ;
            if(e1.getCreated().isBefore(localDate1 ) && e1.getCreated().isAfter(localDate)){

                intervallListe.add(einlieferungList.get(j));

            }
        }
        return intervallListe;
    }

    @Override
    public List<Lagerverkehr> getLagerverkehrByDatum(LocalDate localDate, LocalDate localDate1) {

        if (em == null){
            throw new NoEntityManagerException();
        }
        List<Lagerverkehr> lagerverkehrList = em.createNamedQuery("Lagerverkehr.findAll", Lagerverkehr.class).getResultList();
        List<Lagerverkehr> intervallListe = new ArrayList<>();
        for(int j = 0; j < lagerverkehrList.size(); j++){
            Lagerverkehr l1 = lagerverkehrList.get(j) ;
            if(l1.getCreated().isBefore(localDate1 ) && l1.getCreated().isAfter(localDate)){

                intervallListe.add(lagerverkehrList.get(j));

            }
        }
        return intervallListe;
    }

    @Override
    public List<Lagerverkehr> getGesamtLagerverkehr() {

        if (em == null){
            throw new NoEntityManagerException();
        }
        return em.createNamedQuery("Lagerverkehr.findAll", Lagerverkehr.class).getResultList();
    }

    @Override
    public Einlieferung getEinlieferungById(int i) {

        if (em == null){
            throw new NoEntityManagerException();
        }
          em.createNamedQuery("Einlieferung.findAll", Einlieferung.class);

        return  em.find(Einlieferung.class,i);
    }

    @Override
    public Lagerort getLagerortById(int i) {

        if (em == null){
            throw new NoEntityManagerException();
        }
        em.createNamedQuery("Lagerort.findAll", Lagerort.class);

        return em.find(Lagerort.class,i);
    }

    @Override
    public List<Lagerort> getLagerortListe() {

        if (em == null){
            throw new NoEntityManagerException();
        }
        return em.createNamedQuery("Lagerort.findAll", Lagerort.class).getResultList();
    }
}
