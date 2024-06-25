/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetsGrenzKlasse ProduktGrenz               *
 *                                                               *
 *****************************************************************/

package de.thkoeln.swp.wawi.adminsteuerung.grenzklassen;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProduktGrenz {

        private int prodid;
        private String name;
        private String beschreibung;
        private LocalDate angelegt;
        private Integer stueckzahl;
        private BigDecimal nettopreis;
        private Integer mwstsatz;
        private Integer kategorie;
        private Integer lagerort;

        /**
         * Kontruktor ohne Parameter
         */
        public ProduktGrenz(){

        }

        /**
         * Konstruktor mit Parameter
         * @param prodid
         * @param name
         * @param beschreibung
         * @param angelegt
         * @param stueckzahl
         * @param nettopreis
         * @param mwstsatz
         * @param kategorie
         * @param lagerort
         */
        public ProduktGrenz(int prodid, String name, String beschreibung, LocalDate angelegt, Integer stueckzahl,
                            BigDecimal nettopreis, Integer mwstsatz, Integer kategorie, Integer lagerort) {

                this.prodid = prodid;
                this.name = name;
                this.beschreibung = beschreibung;
                this.angelegt = angelegt;
                this.stueckzahl = stueckzahl;
                this.nettopreis = nettopreis;
                this.mwstsatz = mwstsatz;
                this.kategorie = kategorie;
                this.lagerort = lagerort;
        }

        /**
         * Methode getProdid()
         * @return das Produkt-Id eines Produkts
         */
        public int getProdid() {

                return prodid;
        }

        /**
         * Methode setProdid() mit Paramter
         * @param prodid
         */
        public void setProdid(int prodid) {

                this.prodid = prodid;
        }

        /**
         * Methode gerName()
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
         * Methode getBeschreibung()
         * @return  die beschreibung eines Kategories
         */
        public String getBeschreibung() {

                return beschreibung;
        }

        /**
         * Methode setBeschreibung mit Paramter
         * @param beschreibung
         */
        public void setBeschreibung(String beschreibung) {

                this.beschreibung = beschreibung;
        }

        /**
         * Methode getAngelegt()
         * @return das angelegte Produkt
         */
        public LocalDate getAngelegt() {

                return angelegt;
        }

        /**
         * Methode setAngelegt() mit Parameter
         * @param angelegt
         */
        public void setAngelegt(LocalDate angelegt) {

                this.angelegt = angelegt;
        }

        /**
         * Methode getstueckzahl()
         * @return die stueckzahl eines Produkts
         */
        public Integer getStueckzahl() {

                return stueckzahl;
        }

        /**
         * Methode setStueckzahl() mit Parameter
         * @param stueckzahl
         */
        public void setStueckzahl(Integer stueckzahl) {

                this.stueckzahl = stueckzahl;
        }

        /**
         * Methode getNettopreis()
         * @return der Netto-Preis eines Produkts
         */
        public BigDecimal getNettopreis() {

                return nettopreis;
        }

        /**
         * Methode setNettopreis() mit Parameter
         * @param nettopreis
         */
        public void setNettopreis(BigDecimal nettopreis) {

                this.nettopreis = nettopreis;
        }

        /**
         * Methode getMwstsatz()
         * @return der mwstsatz eines Produkts
         */
        public Integer getMwstsatz() {

                return mwstsatz;
        }

        /**
         * Methode setMwstsatz() mit Paramter
         * @param mwstsatz
         */
        public void setMwstsatz(Integer mwstsatz) {

                this.mwstsatz = mwstsatz;
        }

        /**
         * Methode getKategorie()
         * @return das Kategorie eines Produkt
         */
        public Integer getKategorie() {

                return kategorie;
        }

        /**
         * Methode setKategorie() mit Parameter
         * @param kategorie
         */
        public void setKategorie(Integer kategorie) {

                this.kategorie = kategorie;
        }

        /**
         * Methode getLagerort()
         * @return der Ort eines Lagers
         */
        public Integer getLagerort() {

                return lagerort;
        }

        /**
         * Methode setLagerort() mit Paramter
         * @param lagerort
         */
        public void setLagerort(Integer lagerort) {

                this.lagerort = lagerort;
        }
}


