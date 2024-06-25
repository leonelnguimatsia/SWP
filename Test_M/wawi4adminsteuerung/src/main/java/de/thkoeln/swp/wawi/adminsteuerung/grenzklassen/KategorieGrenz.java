/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetsGrenzKlasse KategorieGrenz             *
 *                                                               *
 *****************************************************************/

package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;


public class KategorieGrenz {

    private int katid;
    private int parentkatid;
    private  String name;
    private String beschreibung;


    /**
     * Konstruktor ohne Parameter
     */
    public KategorieGrenz(){


    }

    /**
     * Konstruktor mit Paramter
     * @param kategorieId
     * @param parentkategorieId
     * @param name
     * @param beschreibung
     */
    public KategorieGrenz(int kategorieId, int parentkategorieId, String name, String beschreibung) {

        this.katid = kategorieId;
        this.parentkatid = parentkategorieId;
        this.name = name;
        this.beschreibung = beschreibung;
    }

    /**
     * Methode getKatid()
     * @return die´Kategorie-ID
     */
    public int getKatid() {

        return katid;
    }

    /**
     * Methode seKatid() mit Parameter
     * @param katid
     */
    public void setKatid(int katid) {

        this.katid = katid;
    }

    /**
     * Methode getParentkatid()
     * @return der Parent Kategrie-Id
     */
    public int getParentkatid() {

        return parentkatid;
    }

    /**
     * Methode setParentkatid() mit Paramter
     * @param parentkatid
     */
    public void setParentkatid(int parentkatid) {

        this.parentkatid = parentkatid;
    }

    /**
     * Methode getName()
     * @return der Name eines Kategories
     */
    public String getName() {

        return name;
    }

    /**
     * Methode setName() mnit Paramter
     * @param name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Methode getBeschreibung()
     * @return die beschreibung eines Kategories
     */
    public String getBeschreibung() {

        return beschreibung;
    }

    /**
     * Methode setBeschreibung() mit Parameter
     * @param beschreibung
     */
    public void setBeschreibung(String beschreibung) {

        this.beschreibung = beschreibung;
    }
}
