/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetKlasse SteuerungKategorieImpl           *
 *                                                               *
 *****************************************************************/

package de.thkoeln.swp.wawi.adminsteuerung.impl;


import de.thkoeln.swp.wawi.datenhaltungapi.ICRUDKategorie;
import de.thkoeln.swp.wawi.steuerungapi.admin.IKategorieSteuerung;
import de.thkoeln.swp.wawi.steuerungapi.grenz.KategorieGrenz;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Kategorie;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SteuerungKategorieImpl implements IKategorieSteuerung {

    // Hält einen Logger um Fehler zu protokollieren
        private static final Logger LOGGER = Logger.getLogger(SteuerungKategorieImpl.class.getName());

    // Hält den EntityManager zur Verbindung mit der Datenhaltung
        private EntityManager em;

    // Hält die Implementierung von ICRUDKategorie
        private ICRUDKategorie icrudKategorie;

    /**
     *  Standardkonstrukor, lädt die Impementierung der ICRUDKategorie-Schnittstelle
      */
    public SteuerungKategorieImpl(){

        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();
        boolean loadError = false;
        Iterator<ICRUDKategorie> iterator = ServiceLoader.load(ICRUDKategorie.class).iterator();
        if(iterator.hasNext()){

            icrudKategorie = iterator.next();
            icrudKategorie.setEntityManager(em);

        } else{

            LOGGER.log(Level.SEVERE, "ICRUDKategorie Implementierung nicht gefunden");
            loadError = true;
        }
        if(loadError){

            LOGGER.log(Level.SEVERE, "Programm abgebrochen");
        }
    }

    /**
     *
     * @param  kategorie kategorie als Kategorie-Objekt welches in ein KategorieGrenz-Objekt umgewandelt werden soll
     * @return kategorieGrenz. Gibt ein kategorieGrenz-Objekt mit den gleichen Parameter wie das übergebene Kategorie-Objekt zurück.
     */
    KategorieGrenz kategorieToGrenz(Kategorie kategorie){

        KategorieGrenz kategorieGrenz = new KategorieGrenz();
        kategorieGrenz.setKategorieId(kategorie.getKatid());
        kategorieGrenz.setName(kategorie.getName());
        kategorieGrenz.setBeschreibung(kategorie.getBeschreibung());
        kategorieGrenz.setParentKategorieId(kategorie.getParentkatid());

        return kategorieGrenz;
    }

    /**
     *
     * @param kategorieGrenz kategorieGrenz als KategorieGrenz-Objekt welches in ein Kategorie-Objekt umgewandelt werden soll
     * @return kategorie. Gibt ein kategorie-Objekt mit den gleichen Parameter wie das übergebene KategorieGrenz-Objekt zurück.
     */
    Kategorie kategorieFromGrenz(KategorieGrenz kategorieGrenz){

        Kategorie kategorie = new Kategorie();
        kategorie.setKatid(kategorieGrenz.getKategorieId());
        kategorie.setParentkatid(kategorieGrenz.getParentKategorieId());
        kategorie.setName(kategorieGrenz.getName());
        kategorie.setBeschreibung(kategorieGrenz.getBeschreibung());

        return kategorie;
    }

    /**
     *
     * @param kategorieId ID eines Kategories
     * @return kategorieToGrenz(kategorieById).Gibt ein KategorieGrenz-Objekt des Kategorie mit der übergeben ID zurück.
     */
    @Override
    public KategorieGrenz getKategorieById(int kategorieId) {

        if(icrudKategorie.getKategorieById(kategorieId) == null){

            return null;
        }

        Kategorie kategorieById = icrudKategorie.getKategorieById(kategorieId);
        if(kategorieById == null){

            return null;
        }

        return kategorieToGrenz(kategorieById);

    }

    /**
     *
     * @param  kategorieGrenz-Objekt eines kategorieGrenz der in die Datenbank eingefügt werden soll.
     * @return gibt bei erfolgreichem Einfügen{@code true} zurück, sonst {@code false}.
     */
    @Override
    public boolean addKategorie(KategorieGrenz kategorieGrenz) {

        Kategorie kategorieToAdd = kategorieFromGrenz(kategorieGrenz);
        em.getTransaction().begin();
        boolean addKategorieResult = icrudKategorie.insertKategorie(kategorieToAdd);
        if(addKategorieResult){

            em.getTransaction().commit();

        } else{

            em.getTransaction().rollback();
        }

        return addKategorieResult;
    }

    /**
     *
     * @param kategorieGrenz bearbeitetes KategorienGrenz-Objekt eines Kategorie
     * @return gibt bei erfolgreichem Bearbeiten{@code true} zurück, sonst {@code false}.
     */
    @Override
    public boolean updateKategorie(KategorieGrenz kategorieGrenz) {

        em.getTransaction().begin();

        boolean updateKategorieResult = icrudKategorie.updateKategorie(kategorieFromGrenz(kategorieGrenz));

        if(updateKategorieResult){

            em.getTransaction().commit();

        }else{

            em.getTransaction().rollback();
        }

        return updateKategorieResult;
    }

    /**
     *
     * @param deleteKategorieId eines zu löschenden Kategorie
     * @return gibt bei erfolgreichem Löschen{@code true} zurück, sonst {@code false}.
     */
    @Override
    public boolean deleteKategorie(int deleteKategorieId) {

        em.getTransaction().begin();
        Kategorie kategorieToDelete = icrudKategorie.getKategorieById(deleteKategorieId);
        boolean deleteKategorieResult;
        if(kategorieToDelete.getProduktList().isEmpty()){

            deleteKategorieResult = icrudKategorie.deleteKategorie(deleteKategorieId);

        }else{

            deleteKategorieResult = false;
        }

        if(deleteKategorieResult){

            em.getTransaction().commit();

        }else{

            em.getTransaction().rollback();
        }

        return deleteKategorieResult;
    }


    /**
     *
     * @return Gibt eine Liste aller Kategorien als KategorienGrenz-Objekt zurück.
     */
    @Override
    public List<KategorieGrenz> getKategorien() {

        List<Kategorie> alleKategorien = icrudKategorie.getKategorieListe();

        List<KategorieGrenz> alleKategorienGrenz = new ArrayList<>();

        for( Kategorie kategorie : alleKategorien){

            alleKategorienGrenz.add(kategorieToGrenz(kategorie));
        }

        return alleKategorienGrenz;
    }




}
