/*****************************************************************
 * Autor: Leonel Nguimatsia                                         *
 * @version: IntelliJ2021 JDK17                                     *
 * Hochschule: TH-Kloen                                             *
 * Ort: Deutz Technische Hochschule                                 *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de            *
 * Beschreibung: EntitaetsGrenzKlasse BestellungspositionUebersicht *
 *                                                                  *
 *****************************************************************/

package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

import java.math.BigDecimal;

public class BestellungspositionUebersicht {

     private Integer bpid;
     private Integer bid;
     private Integer pid;
     private Integer kaid;
     private Integer anzahl;
     private String  name;
     private BigDecimal preisNetto;


    /**
     * Konstruktor ohne Paramater
     */
    public BestellungspositionUebersicht(){


     }

    /**
     * Konstruktor mit Paramter
     * @param bpid
     * @param bid
     * @param pid
     * @param kaid
     * @param anzahl
     * @param name
     * @param preisNetto
     */
    public BestellungspositionUebersicht(Integer bpid, Integer bid, Integer pid, Integer kaid, Integer anzahl, String name, BigDecimal preisNetto) {

        this.bpid = bpid;
        this.bid = bid;
        this.pid = pid;
        this.kaid = kaid;
        this.anzahl = anzahl;
        this.name = name;
        this.preisNetto = preisNetto;
    }

    /**
     * Methode getBpid()
     * @return die Bestellungsposition-ID
     */
    public Integer getBpid() {

        return bpid;
    }

    /**
     * Methode setBpid() mit Paramter
     * @param bpid
     */
    public void setBpid(Integer bpid) {

        this.bpid = bpid;
    }

    /**
     * Methode getBid()
     * @return die Bestellung-ID
     */
    public Integer getBid() {

        return bid;
    }

    /**
     * Methode setBid() mit Parameter
     * @param bid
     */
    public void setBid(Integer bid) {

        this.bid = bid;
    }

    /**
     * Methode getPid()
     * @return die Produkt-ID
     */
    public Integer getPid() {

        return pid;
    }

    /**
     * Methode setPid() mit Parameter
     * @param pid
     */
    public void setPid(Integer pid) {

        this.pid = pid;
    }

    /**
     * Methode getKaid()
     * @return die Kategorie-ID
     */
    public Integer getKaid() {

        return kaid;
    }

    /**
     * Methode setKaid() mit Parameter
     * @param kaid
     */
    public void setKaid(Integer kaid) {

        this.kaid = kaid;
    }

    /**
     * Methode getAnzahl()
     * @return die Anzahl der Bestellungsposition
     */
    public Integer getAnzahl() {

        return anzahl;
    }

    /**
     * Methode setAnzahl() mit Paramter
     * @param anzahl
     */
    public void setAnzahl(Integer anzahl) {

        this.anzahl = anzahl;
    }

    /**
     * Methode getName()
     * @return der Name eines Kategories
     */
    public String getName() {

        return name;
    }

    /**
     * Methode setName() mit Paramter
     * @param name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Methode getPreisNetto()
     * @return der Netoo-Preis eines Produkts
     */
    public BigDecimal getPreisNetto() {

        return preisNetto;
    }

    /**
     * Methode setPreisNetto() mit Parameter
     * @param preisNetto
     */
    public void setPreisNetto(BigDecimal preisNetto) {

        this.preisNetto = preisNetto;
    }
}
