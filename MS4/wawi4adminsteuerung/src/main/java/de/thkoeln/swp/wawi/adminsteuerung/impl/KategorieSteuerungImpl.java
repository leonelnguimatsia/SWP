/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetKlasse KategorieSteuerungImpl           *
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
import java.lang.module.FindException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KategorieSteuerungImpl implements IKategorieSteuerung {

    // Hält einen Logger um Fehler zu protokollieren
    private static final Logger LOGGER = Logger.getLogger(KategorieSteuerungImpl.class.getName());

    //Hält den EntitiyManager zur Verbindung mit der Datenhaltung
    private EntityManager em;

    //Hält die Implementierung von ICRUDKategorie
    private ICRUDKategorie icrudKategorie;

    /**
     *  Standardkonstrukor, lädt die Impementierung der ICRUDKategorie-Schnittstelle
     */
    public KategorieSteuerungImpl(){

        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();
        boolean loadError = false;
        Iterator<ICRUDKategorie> kategorieIterator = ServiceLoader.load(ICRUDKategorie.class).iterator();

        if(kategorieIterator.hasNext()){

            icrudKategorie = kategorieIterator.next();
            icrudKategorie.setEntityManager(em);

        }else{

            LOGGER.log(Level.SEVERE,"ICRUDKategorie Implementierung nicht gefunden");
            loadError = true;

        }

        if(loadError){

            LOGGER.log(Level.SEVERE,"Programm wird abgebrochen");
            throw new FindException("Es konnte keine ICRUDKategorie-Implementierung gefunden werden");

        }
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

        return EntityGrenzConverter.toGrenz(kategorieById);
    }


    /**
     *
     * @param  kategorieGrenz-Objekt eines kategorieGrenz der in die Datenbank eingefügt werden soll.
     * @return gibt bei erfolgreichem Einfügen{@code true} zurück, sonst {@code false}.
     */
    @Override
    public boolean addKategorie(KategorieGrenz kategorieGrenz) {

        Kategorie kategorieToAdd = EntityGrenzConverter.toEntity(kategorieGrenz);
        em.getTransaction().begin();
        boolean addKategorieResult = icrudKategorie.insertKategorie(kategorieToAdd);

        if(addKategorieResult){

            em.getTransaction().commit();

        }else {

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

        Kategorie kategorieToUpdate = EntityGrenzConverter.toEntity(kategorieGrenz);
        em.getTransaction().begin();
        boolean updateKategorieResult = icrudKategorie.updateKategorie(kategorieToUpdate);

        if(updateKategorieResult){

            em.getTransaction().commit();

        }else {

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

        if (kategorieToDelete.getProduktList() == null || kategorieToDelete.getProduktList().isEmpty()) {

            deleteKategorieResult = icrudKategorie.deleteKategorie(deleteKategorieId);

        }else {


            deleteKategorieResult = false;
        }

        if(deleteKategorieResult){

            em.getTransaction().commit();

        }else {

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

        List<Kategorie> kategorieList = icrudKategorie.getKategorieListe();

        List<KategorieGrenz> kategorieGrenzList = new ArrayList<KategorieGrenz>();

        for(Kategorie kategorie : kategorieList){

            kategorieGrenzList.add(EntityGrenzConverter.toGrenz(kategorie));

        }

        return kategorieGrenzList;
    }
}
