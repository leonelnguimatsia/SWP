/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetsGrenzKlasseKundeGrenz                  *
 *                                                               *
 *****************************************************************/

package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

import java.time.LocalDate;

public class KundeGrenz {

    private int kid;
    private String name;
    private String vorname;
    private String adresse;
    private LocalDate created;

    /**
     * Konstruktor der KuneGrenz-Klasse Ohne Paramter
     */
    public KundeGrenz(){


    }

    /**
     * Konstruktor der Klasse KundeGrenz-Klasse mit Partameter
     * @param kundeId
     * @param kundeName
     * @param kundeVorname
     * @param kundeAdresse
     * @param cr
     */
    public KundeGrenz(int kundeId, String kundeName, String kundeVorname, String kundeAdresse, LocalDate cr){

        this.kid = kundeId;
        this.name = kundeName;
        this.vorname = kundeVorname;
        this.adresse = kundeAdresse;
        this.created = cr;
    }

    /**
     * Methode getKid()
     * @return Gibt die Kunde-ID
     */
    public int getKid() {

        return kid;
    }

    /**
     * Methode setkid() mit Paramter
     * @param kid
     */
    public void setKid(int kid) {

        this.kid = kid;
    }

    /**
     * Methode getName()
     * @return der Name eines Kundes
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
     * Methode getVorname()
     * @return der Vorname eines Kundes
     */
    public String getVorname() {

        return vorname;
    }

    /**
     * Methode setVorname() mit Parameter
     * @param vorname
     */
    public void setVorname(String vorname) {

        this.vorname = vorname;
    }

    /**
     * Methode  getAdresse()
     * @return die Adresse eines Kundes
     */
    public String getAdresse() {

        return adresse;
    }

    /**
     * Methode setAdresse() mit Parameter
     * @param adresse
     */
    public void setAdresse(String adresse) {

        this.adresse = adresse;
    }

    /**
     * Methode getCreated()
     * @return das erstellte Local Datum
     */
    public LocalDate getCreated() {

        return created;
    }

    /**
     * Methode setCreated() mit Paramter
     * @param created
     */
    public void setCreated(LocalDate created) {

        this.created = created;
    }
}
