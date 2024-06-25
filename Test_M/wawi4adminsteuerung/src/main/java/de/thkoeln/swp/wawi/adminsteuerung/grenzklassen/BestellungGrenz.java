/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetsGrenzKlasse BestellungGrenz            *
 *                                                               *
 *****************************************************************/

package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BestellungGrenz {

    private int bid;
    private KundeGrenz kunde;
    private String lieferadresse;
    private String rechnungsadresse;
    private LocalDate created;
    private String status;
    private BigDecimal gesamtnetto;
    private BigDecimal gesamtbrutto;
    private List<BestellungspositionGrenz> bestellungspositionList;

    /**
     *  Konstruktor der Bestellung-Klasse Ohne Paramter
     */
    public BestellungGrenz(){

        this.bestellungspositionList = new ArrayList<BestellungspositionGrenz>();
    }

    /**
     * Konstruktor der Bestellung-Klasse mit Paramter
     * @param bestellungsId
     * @param grenzeFuerKunde
     * @param lf
     * @param rechnung
     * @param cr
     * @param state
     * @param netto
     * @param brutto
     */
    public BestellungGrenz(int bestellungsId,KundeGrenz grenzeFuerKunde,String lf,String rechnung,LocalDate cr,String state,BigDecimal netto,BigDecimal brutto ){


            this.bestellungspositionList = new ArrayList<BestellungspositionGrenz>();
            this.bid = bestellungsId;
            this.kunde = grenzeFuerKunde;
            this.lieferadresse = lf;
            this.rechnungsadresse = rechnung;
            this.created = cr;
            this.status = state;
            this.gesamtnetto = netto;
            this.gesamtbrutto = brutto;
    }

    /**
     * Methode getBid()
     * @return die bestellung-ID
     */
    public int getBid() {

        return bid;
    }

    /**
     * Methode setBid() mit Paremeter
     * @param bid
     */
    public void setBid(int bid) {

        this.bid = bid;
    }

    /**
     * Methode getKunde()
     * @return gibt den Kunde die Bestellung zurück
     */
    public KundeGrenz getKunde() {

        return kunde;
    }

    /**
     * Methode setKunde() mit Parameter
     * @param kunde
     */
    public void setKunde(KundeGrenz kunde) {

        this.kunde = kunde;
    }

    /**
     * Methode getLieferadresse()
     * @return die Adresse der Lieferung
     */
    public String getLieferadresse() {

        return lieferadresse;
    }

    /**
     * Methode setLieferadresse() mit parameter
     * @param lieferadresse
     */
    public void setLieferadresse(String lieferadresse) {

        this.lieferadresse = lieferadresse;
    }

    /**
     * Methode getRechnungsadresse()
     * @return die Adresse für die Rechnung
     */
    public String getRechnungsadresse() {

        return rechnungsadresse;
    }

    /**
     * Methode setRechnungsadresse mit Parameter
     * @param rechnungsadresse
     */
    public void setRechnungsadresse(String rechnungsadresse) {

        this.rechnungsadresse = rechnungsadresse;
    }

    /**
     * Methode getCreated()
     * @return das erstellte local Datum für die Bestellung
     */
    public LocalDate getCreated() {

        return created;
    }

    /**
     * Methode setCeated() mit Parameter
     * @param created
     */
    public void setCreated(LocalDate created) {

        this.created = created;
    }

    /**
     * Methode getStatus()
     * @return das status der Bestellung
     */
    public String getStatus() {

        return status;
    }

    /**
     * Methode setStatus() mit Parameter
     * @param status
     */
    public void setStatus(String status) {

        this.status = status;
    }

    /**
     * Methode getGesamtnetto()
     * @return das gesamte Netto der Bestellung
     */
    public BigDecimal getGesamtnetto() {

        return gesamtnetto;
    }

    /**
     * Methode setGesamtnetto() mit Parameter
     * @param gesamtnetto
     */
    public void setGesamtnetto(BigDecimal gesamtnetto) {

        this.gesamtnetto = gesamtnetto;
    }

    /**
     * Methode getGesamtbrutto()
     * @return das gesamte Brutto der Bestellung
     */
    public BigDecimal getGesamtbrutto() {

        return gesamtbrutto;
    }

    /**
     * Methode setGesamtbrutto() mit Parameter
     * @param gesamtbrutto
     */
    public void setGesamtbrutto(BigDecimal gesamtbrutto) {

        this.gesamtbrutto = gesamtbrutto;
    }

    /**
     * Methode getBestellungspositionList()
     * @return die List der Bestellungsposition
     */
    public List<BestellungspositionGrenz> getBestellungspositionList() {

        return bestellungspositionList;
    }

    /**
     * Methode setBestellungspositionList() mit Parameter
     * @param bestellungspositionList
     */
    public void setBestellungspositionList(List<BestellungspositionGrenz> bestellungspositionList) {

        this.bestellungspositionList = bestellungspositionList;
    }
}
