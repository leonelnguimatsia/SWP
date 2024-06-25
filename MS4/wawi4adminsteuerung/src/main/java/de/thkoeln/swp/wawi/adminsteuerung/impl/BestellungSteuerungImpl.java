/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetKlasse BestellungSteuerungImpl          *
 *                                                               *
 *****************************************************************/


package de.thkoeln.swp.wawi.adminsteuerung.impl;

import de.thkoeln.swp.wawi.datenhaltungapi.IBestellungAdmin;
import de.thkoeln.swp.wawi.steuerungapi.admin.IBestellungSteuerung;
import de.thkoeln.swp.wawi.steuerungapi.grenz.BestellungGrenz;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellung;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;

import javax.persistence.EntityManager;
import java.lang.module.FindException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BestellungSteuerungImpl implements IBestellungSteuerung {

    // Hält einen Logger um Fehler zu protokollieren
    private  static final Logger LOGGER = Logger.getLogger(BestellungSteuerungImpl.class.getName());

    // Hält den EntityManager zur Verbindung mit der Datenhaltung
    private EntityManager em;

    // Hält die Implementierung von IBestellungAdmin
    IBestellungAdmin iBestellungAdmin;

    /**
     * Konstruktor,ermittelt den EntityManager für die Verbindung zur Datenbank
     * und ermittelt Implementierungen für die Schnittestellen zur Datenhaltung
     */
    public BestellungSteuerungImpl(){

        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();
        boolean loadError = false;

        // Die Implementierung von IBestellungAdmin finden
        Iterator<IBestellungAdmin> bestellungAdminIterator = ServiceLoader.load(IBestellungAdmin.class).iterator();

        if(bestellungAdminIterator.hasNext()){

            iBestellungAdmin = bestellungAdminIterator.next();
            iBestellungAdmin.setEntityManager(em);

        }else{

            LOGGER.log(Level.SEVERE,"IBestellungAdmin Implementierung nicht gefunden");
            loadError = true;

        }
        if(loadError){

            LOGGER.log(Level.SEVERE,"Programm wird abgebrochen");
            throw new FindException("Es konnte keine IBestellungAdmin-Implementierung gefunden werden");

        }
    }

    /**
     * Erstellt eine Liste aller neuen Bestellungen als ArrayList der Grenzklasse
     * @return null, wenn keine neuen Bestellungen vorhanden sind oder keine Implementierung
     *         von IBestellungAdmin gefunden wurde, sonst die Liste der neuen Bestellungen
     */
    @Override
    public List<BestellungGrenz> getBestellungen() {

        if(iBestellungAdmin == null){

            return null;
        }

        List<Bestellung> bestellungList = iBestellungAdmin.getAlleBestellungen();

        if(bestellungList == null){

            return null;

        }

        //Wenn eine Liste an neuen Bestellungen erstellt wurde, umwandeln diese in eine ArrayList der GrenzKlasse
        Iterator<Bestellung> bestellungIterator = bestellungList.iterator();
        List<BestellungGrenz> bestellungGrenzList = new ArrayList<BestellungGrenz>();
        BestellungGrenz bestellungGrenz;

        while (bestellungIterator.hasNext()){

            bestellungGrenz = EntityGrenzConverter.toGrenz(bestellungIterator.next());
            bestellungGrenzList.add(bestellungGrenz);
        }

        return bestellungGrenzList;
    }

    /**
     * Erstellt eine Liste aller neuen Bestellungen einens Zeitraum mit localen Datum als ArrayList der Grenzklasse
     * @param localDateVon Vom Anfang
     * @param localDateBis Bis zum Ende
     * @return null, wenn keine neuen Bestellungen vorhanden sind oder keine Implementierung
     *         von IBestellungAdmin gefunden wurde, sonst die Liste der neuen Bestellungen
     */
    @Override
    public List<BestellungGrenz> getBestellungenVonBisDatum(LocalDate localDateVon, LocalDate localDateBis) {

        if(localDateVon == null || localDateBis == null){

            return null;

        }
        List<Bestellung> bestellungDatumList = iBestellungAdmin.getBestellungenByDatum(localDateVon,localDateBis);

        if(bestellungDatumList == null){

            return null;
        }

        //Wenn eine Liste an neuen Bestellungen erstellt wurde, umwandeln diese in eine ArrayList der GrenzKlasse
        Iterator<Bestellung> bestellungIterator = bestellungDatumList.iterator();
        List<BestellungGrenz> bestellungGrenzList = new ArrayList<BestellungGrenz>();
        BestellungGrenz bestellungGrenz;

        while (bestellungIterator.hasNext()){

            bestellungGrenz = EntityGrenzConverter.toGrenz(bestellungIterator.next());
            bestellungGrenzList.add(bestellungGrenz);

        }

        return bestellungGrenzList;
    }

    /**
     *  Ermittelt eine einzelne Bestellung anhand der ID
     * @param bestellungById die ID der Bestellung
     * @return null, wenn keine Implementierung von IBestellungAdmin gefunden wurde, wenn die ID ungültig ist
     *         oder wenn keine Bestellung gefunden wurde, sonst die Bestellung als Objekt der Grenzklasse.
     */
    @Override
    public BestellungGrenz getBestellungById(int bestellungById) {

        if(iBestellungAdmin.getBestellungById(bestellungById) == null){

            return null;

        }

        if(iBestellungAdmin == null){

            return null;
        }

        Bestellung bestellung = iBestellungAdmin.getBestellungById(bestellungById);

        if(bestellung == null){

            return null;
        }

        return EntityGrenzConverter.toGrenz(bestellung);
    }
}
