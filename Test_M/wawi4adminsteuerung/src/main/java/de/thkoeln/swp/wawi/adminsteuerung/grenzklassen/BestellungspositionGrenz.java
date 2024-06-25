/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetsGrenzKlasse BestellungspositionGrenz   *
 *                                                               *
 *****************************************************************/

package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

public class BestellungspositionGrenz {

    private int bpid;
    private BestellungGrenz bestellung;
    private Integer produkt;
    private int anzahl;

    /**
     * Konstruktor Ohne Paramter
     */
    public BestellungspositionGrenz(){


    }

    /**
     * Konstruktor mit Parameter
     * @param bestellungspositionId
     * @param bestellung
     * @param produktGrenz
     * @param anzahl
     */
    public BestellungspositionGrenz(int bestellungspositionId, BestellungGrenz bestellung, Integer produktGrenz, int anzahl){

        this.bpid = bestellungspositionId;
        this.bestellung = bestellung;
        this.produkt = produktGrenz;
        this.anzahl = anzahl;

    }

    /**
     * Methode getBpid()
     * @return die Bestellungsposition-ID
     */
    public int getBpid() {

        return bpid;
    }

    /**
     * Methode setBpid() mit Parameter
     * @param bpid
     */
    public void setBpid(int bpid) {

        this.bpid = bpid;
    }

    /**
     * Methode getBestellung()
     * @return die Bestellung
     */
    public BestellungGrenz getBestellung() {

        return bestellung;
    }

    /**
     * Methode setBestellung() mit Parameter
     * @param bestellung
     */
    public void setBestellung(BestellungGrenz bestellung) {

        this.bestellung = bestellung;
    }

    /**
     * Methode getProdukt()
     * @return das Produkt
     */
    public Integer getProdukt() {

        return produkt;
    }

    /**
     * Methode setProdukt() mit Paramter
     * @param produkt
     */
    public void setProdukt(Integer produkt) {

        this.produkt = produkt;
    }

    /**
     * Methode getAnzahl()
     * @return die Anzahl der Bestellungsposition
     */
    public int getAnzahl() {

        return anzahl;
    }

    /**
     * Methode setAnzahl() mit Parameter
     * @param anzahl
     */
    public void setAnzahl(int anzahl) {

        this.anzahl = anzahl;
    }
}
