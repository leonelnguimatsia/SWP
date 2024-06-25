/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetKlasse SteuerungBestellungImpl          *
 *                                                               *
 *****************************************************************/

package de.thkoeln.swp.wawi.adminsteuerung.impl;

import de.thkoeln.swp.wawi.datenhaltungapi.IBestellungAdmin;
import de.thkoeln.swp.wawi.datenhaltungapi.ICRUDKunde;
import de.thkoeln.swp.wawi.steuerungapi.admin.IBestellungSteuerung;
import de.thkoeln.swp.wawi.steuerungapi.grenz.BestellungGrenz;
import de.thkoeln.swp.wawi.steuerungapi.grenz.BestellungspositionGrenz;
import de.thkoeln.swp.wawi.steuerungapi.grenz.KundeGrenz;
import de.thkoeln.swp.wawi.steuerungapi.grenz.ProduktGrenz;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellung;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Bestellungsposition;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Kunde;
import de.thkoeln.swp.wawi.wawidbmodel.entities.Produkt;
import de.thkoeln.swp.wawi.wawidbmodel.impl.IDatabaseImpl;
import de.thkoeln.swp.wawi.wawidbmodel.services.IDatabase;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SteuerungBestellungImpl implements IBestellungSteuerung {

    // Hält einen Logger um Fehler zu Protokollieren
    private static final Logger LOGGER = Logger.getLogger(SteuerungBestellungImpl.class.getName());

    //Hält den EntityManager zur Verbindung mit der Datenhaltung
    private EntityManager em;

    //Hält die Implementierung von IBestellungAdmin
    IBestellungAdmin iBestellungAdmin;

    //Hält die Implementierung von ICRUDKunde
    ICRUDKunde icrudKunde;

    /**
     * Konstruktor,ermittelt den EntityManager für die Verbindung zur Datenbank
     * und ermittelt Implementierungen für die Schnittestellen zur Datenhaltung
     */
    public SteuerungBestellungImpl(){

        IDatabase db = new IDatabaseImpl();
        em = db.getEntityManager();
        boolean loadError = false;

        //Die Implementierung von IBestellungAdmin finden
        Iterator<IBestellungAdmin> iteratorBestellungAdminIterator = ServiceLoader.load(IBestellungAdmin.class).iterator();
        if(iteratorBestellungAdminIterator.hasNext()){

            iBestellungAdmin = iteratorBestellungAdminIterator.next();
            iBestellungAdmin.setEntityManager(em);

        }else{

            LOGGER.log(Level.SEVERE,"IBestellungAdmi Implementierung nicht gefunden");
            loadError = true;
        }

        //die Implementierung von ICRUDKunde finden
        Iterator<ICRUDKunde> iteratorIcrudKunde = ServiceLoader.load(ICRUDKunde.class).iterator();
        if(iteratorIcrudKunde.hasNext()){

            icrudKunde = iteratorIcrudKunde.next();
            icrudKunde.setEntityManager(em);

        }else{

            LOGGER.log(Level.SEVERE,"ICRUDKunde Implementierung nicht gefunden");
            loadError = true;
        }
        if(loadError){

            LOGGER.log(Level.SEVERE, "Programm abgebrochen");
        }
    }


    /**
     *  Wandelt eine Bestellung in ein zugehöriges Objekt der GrenzKlasse um
     * @param bestellung die umwandelnde Bestellung
     * @return null wenn eine ungültige übergeben wurde, sonst ein Objekt der GrenzKlasse
     */
    BestellungGrenz bestellungToGrenz(Bestellung bestellung){

        if(bestellung == null){

            return null;
        }
        BestellungGrenz bestellungGrenz = new BestellungGrenz();

        bestellungGrenz.setBestellungId(bestellung.getBid());
        bestellungGrenz.setLieferadresse(bestellung.getLieferadresse());
        bestellungGrenz.setRechnungsadresse(bestellung.getRechnungsadresse());
        bestellungGrenz.setCreated(bestellung.getCreated());
        bestellungGrenz.setStatus(bestellung.getStatus());
        bestellungGrenz.setGesamtnetto(bestellung.getGesamtnetto());
        bestellungGrenz.setGesamtbrutto(bestellung.getGesamtbrutto());
        bestellungGrenz.setKundeGrenz(kundeToGrenz(bestellung.getKunde()));

        //Die Liste der Bestellungspositionen hizufügen
        List<Bestellungsposition> bestellungspositionList = bestellung.getBestellungspositionList();
        Iterator<Bestellungsposition> iteratorBestellungsposition = bestellungspositionList.listIterator();
        BestellungspositionGrenz bestellungspositionGrenz;
        List<BestellungspositionGrenz> bestellungspositionGrenzList = new ArrayList<BestellungspositionGrenz>();
        while (iteratorBestellungsposition.hasNext()){

            bestellungspositionGrenz = bestellungspositionToGrenz(iteratorBestellungsposition.next());
            bestellungspositionGrenz.setBestellungGrenz(bestellungGrenz);
            bestellungspositionGrenzList.add(bestellungspositionGrenz);
        }

        bestellungGrenz.setBestellungspositionGrenzList(bestellungspositionGrenzList);

        return bestellungGrenz;
    }

    /**
     *  Wandelt ein Kunde in ein zugehöriges Objekt der GrenzKlasse um
     * @param kunde die umwandelnde Kunde
     * @return null wenn eine ungültige übergeben wurde, sonst ein Objekt der GrenzKlasse
     */
    KundeGrenz kundeToGrenz(Kunde kunde){

        if(kunde == null){

            return null;
        }
        KundeGrenz kundeGrenz = new KundeGrenz();

        kundeGrenz.setKundeId(kunde.getKid());
        kundeGrenz.setName(kunde.getName());
        kundeGrenz.setVorname(kunde.getVorname());
        kundeGrenz.setAdresse(kunde.getAdresse());
        kundeGrenz.setCreated(kunde.getCreated());

        return kundeGrenz;
    }


    /**
     *  Wandelt eine bestellungsposition in ein zugehöriges Objekt der GrenzKlasse um
     * @param bestellungsposition die umwandelnde Bestellungsposition
     * @return null wenn eine ungültige übergeben wurde, sonst ein Objekt der GrenzKlasse
     */
    BestellungspositionGrenz bestellungspositionToGrenz(Bestellungsposition bestellungsposition){

        if(bestellungsposition == null){

            return null;
        }

        BestellungspositionGrenz bestellungspositionGrenz = new BestellungspositionGrenz();

        bestellungspositionGrenz.setBestellungspositionId(bestellungsposition.getBpid());
        bestellungspositionGrenz.setAnzahl(bestellungsposition.getAnzahl());
        bestellungspositionGrenz.setBestellungGrenz(bestellungToGrenz(bestellungsposition.getBestellung()));
        bestellungspositionGrenz.setProduktGrenz(produktToGrenz(bestellungsposition.getProdukt()));

        return bestellungspositionGrenz;

    }

    /**
     *  Wandelt ein Produkt in ein zugehöriges Objekt der GrenzKlasse um
     * @param produkt die umwandelnde Produkt
     * @return null wenn eine ungültige übergeben wurde, sonst ein Objekt der GrenzKlasse
     */
    ProduktGrenz produktToGrenz(Produkt produkt){

        if(produkt == null){

            return null;
        }

        ProduktGrenz produktGrenz = new ProduktGrenz();
        produktGrenz.setProduktId(produkt.getProdid());
        produktGrenz.setName(produkt.getName());
        produktGrenz.setBeschreibung(produkt.getBeschreibung());
        produktGrenz.setAngelegt(produkt.getAngelegt());
        produktGrenz.setStueckzahl(produkt.getStueckzahl());
        produktGrenz.setNettopreis(produkt.getNettopreis());
        produktGrenz.setMwstsatz(produkt.getMwstsatz());
        produktGrenz.setAktiv(produkt.getAktiv());

        return produktGrenz;

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
        Iterator<Bestellung> iteratorBestellung = bestellungList.iterator();
        List<BestellungGrenz> bestellungGrenzList = new ArrayList<BestellungGrenz>();
        BestellungGrenz bestellungGrenz;

        while (iteratorBestellung.hasNext()){

            bestellungGrenz = bestellungToGrenz(iteratorBestellung.next());
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

        if(localDateVon == null || localDateBis == null ){

            return null;
        }

        List<Bestellung> bestellungDatumList = iBestellungAdmin.getBestellungenByDatum(localDateVon,localDateBis);
        if(bestellungDatumList == null){

            return  null;
        }
        //Wenn eine Liste an neuen Bestellungen erstellt wurde, umwandeln diese in eine ArrayList der GrenzKlasse
        Iterator<Bestellung> iteratorBestellung = bestellungDatumList.iterator();
        List<BestellungGrenz> bestellungGrenzList = new ArrayList<BestellungGrenz>();
        BestellungGrenz bestellungGrenz;

        while (iteratorBestellung.hasNext()){

            bestellungGrenz = bestellungToGrenz(iteratorBestellung.next());
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

        return bestellungToGrenz(bestellung);
    }
}
