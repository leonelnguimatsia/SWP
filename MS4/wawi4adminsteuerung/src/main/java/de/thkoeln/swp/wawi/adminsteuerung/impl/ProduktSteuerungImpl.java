/*****************************************************************
 * Autor: Cyntia Pola                                            *
 * @version: IntelliJ2021 JDK17                                  *
 * **************************************************************/
package de.thkoeln.swp.wawi.adminsteuerung.impl;

import de.thkoeln.swp.wawi.datenhaltungapi.IBestellungAdmin;
import de.thkoeln.swp.wawi.datenhaltungapi.ICRUDKategorie;
import de.thkoeln.swp.wawi.datenhaltungapi.ICRUDProdukt;
import de.thkoeln.swp.wawi.steuerungapi.admin.IProduktSteuerung;
import de.thkoeln.swp.wawi.steuerungapi.grenz.KategorieGrenz;
import de.thkoeln.swp.wawi.steuerungapi.grenz.ProduktGrenz;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellung;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellungsposition;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Kategorie;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Produkt;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProduktSteuerungImpl implements IProduktSteuerung {

    // Hält einen Logger um Fehler zu protokollieren
    private static final Logger LOGGER = Logger.getLogger(ProduktSteuerungImpl.class.getName());

    //Hält die Implementierung von ICRUDProdukt
    private ICRUDProdukt icrudProdukt;
    private IBestellungAdmin iBestellungAdmin;

    //Hält den EntitiyManager zur Verbindung mit der Datenhaltung
    private EntityManager em;


    /**
     *  Standardkonstrukor, lädt die Impementierung der ICRUDProdukt-Schnittstelle
     */
    public ProduktSteuerungImpl(){

        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();
        boolean loadError = false;
        Iterator<ICRUDProdukt> produktIterator = ServiceLoader.load(ICRUDProdukt.class).iterator();
        Iterator<IBestellungAdmin> bestellungAdminIterator = ServiceLoader.load(IBestellungAdmin.class).iterator();
        if(produktIterator.hasNext()){

            icrudProdukt = produktIterator.next();
            icrudProdukt.setEntityManager(em);
        }

        else {
            LOGGER.log(Level.SEVERE,"produktKaterie not found");
            loadError = true;
        }
        if(bestellungAdminIterator.hasNext()){

            iBestellungAdmin = bestellungAdminIterator.next();
            iBestellungAdmin.setEntityManager(em);
        }

        else {
            LOGGER.log(Level.SEVERE,"produktKaterie not found");
            loadError = true;
        }
        if(loadError){
            LOGGER.log(Level.SEVERE,"Program wird abgebrochen");
        }
    }


    /**
     * @return Gibt eine Liste aller Produkte mit deren Kategorie
     * als ProduktGrenz-Objekt zurück.
     */
    @Override
    public List<ProduktGrenz> getProdukte() {
        List<Produkt> produktListe = icrudProdukt.getProduktListe();

        List<ProduktGrenz> produktGrenzList = new ArrayList<ProduktGrenz>();

        for(Produkt produkt : produktListe){
            produktGrenzList.add(EntityGrenzConverter.toGrenz(produkt));
        }
        return produktGrenzList;
    }


    /**
     *
     * @param  produktGrenz-Objekt eines produktGrenz der in die Datenbank eingefügt werden soll.
     * @return gibt bei erfolgreichem Einfügen{@code true} zurück, sonst {@code false}.
     */
    @Override
    public boolean addProdukt(ProduktGrenz produktGrenz) {

        Produkt produktToAdd = EntityGrenzConverter.toEntity(produktGrenz);
        em.getTransaction().begin();
        boolean addProdukteResult = icrudProdukt.insertProdukt(produktToAdd);
        if(addProdukteResult){

            em.getTransaction().commit();

        }else{

            em.getTransaction().rollback();
        }
        return addProdukteResult;
    }

    /**
     *
     * @param produkteId eines zu löschenden Produkt
     * @return gibt bei erfolgreichem Löschen{@code true} zurück, sonst {@code false}.
     */
    @Override
    public boolean deleteProdukt(int produkteId) {

        em.getTransaction().begin();
        Produkt produktToDelete = icrudProdukt.getProduktById(produkteId);
        boolean deleteProdukteResult = true;

        for (Bestellung bestellung: iBestellungAdmin.getAlleBestellungen()) {
            for (Bestellungsposition bestellungsposition: bestellung.getBestellungspositionList()) {
                if (bestellungsposition.getProdukt().equals(produktToDelete)) {
                    deleteProdukteResult = false;
                    break;
                }
            }
        }

        if(deleteProdukteResult){
            icrudProdukt.deleteProdukt(produkteId);
            em.getTransaction().commit();

        }else{

            em.getTransaction().rollback();

        }
        return deleteProdukteResult;
    }

    /**
     *
     * @param produktGrenz bearbeitetes ProduktGrenz-Objekt eines Produkt
     * @return gibt bei erfolgreichem Bearbeiten{@code true} zurück, sonst {@code false}.
     */
    @Override
    public boolean updateProdukt(ProduktGrenz produktGrenz) {

        Produkt produktToUpdate = EntityGrenzConverter.toEntity(produktGrenz);
        em.getTransaction().begin();
        boolean updateProdukteResult = icrudProdukt.updateProdukt(produktToUpdate);
        if(updateProdukteResult){

            em.getTransaction().commit();

        }else{

            em.getTransaction().rollback();

        }

        return updateProdukteResult;
    }

    /**
     * @param stueckzahl aktiviere  bestimmte Produkte aus der Produktliste der Admin
     * @return true, wenn Produkte die oberhalb einer eingegebenen Stückzahl vorhanden sind.
     */
    @Override
    public boolean aktiviereProdukte(int stueckzahl) {
        List<Produkt> pl = icrudProdukt.getProduktListe();

        em.getTransaction().begin();
        for (Produkt p : pl) {
            if (p.getStueckzahl() >= stueckzahl) {
                p.setAktiv(true);
                if (!icrudProdukt.updateProdukt(p)) {
                    em.getTransaction().rollback();
                    return false;
                }
            }
        }
        em.getTransaction().commit();

        return true;

    }

    /**
     * @param stueckzahl deaktiviere  bestimmte Produkte aus der Produktliste der Admin
     * @return true, wenn Produkte die unterhalb deaktiviere einer eingegebenen Stückzahl vorhanden sind.
     */

    @Override
    public boolean deaktiviereProdukte(int stueckzahl) {
        List<Produkt> pl = icrudProdukt.getProduktListe();

        em.getTransaction().begin();
        for (Produkt p : pl) {
            if (p.getStueckzahl() <= stueckzahl) {
                p.setAktiv(false);
                if (!icrudProdukt.updateProdukt(p)) {
                    em.getTransaction().rollback();
                    return false;
                }
            }
        }
        em.getTransaction().commit();

        return true;
    }

}