/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: Module-Info de.thkoeln.swp.wawi.adminsteuerung  *
 *                                                               *
 *****************************************************************/

module de.thkoeln.swp.wawi.adminsteuerung {

    exports de.thkoeln.swp.wawi.adminsteuerung.impl;

    uses de.thkoeln.swp.wawi.datenhaltungapi.ICRUDKategorie;
    uses de.thkoeln.swp.wawi.datenhaltungapi.IBestellungAdmin;

    requires de.thkoeln.swp.wawi.componentcontroller;
    requires de.thkoeln.swp.wawi.wawidbmodel;
    requires de.thkoeln.swp.wawi.datenhaltungapi;
    requires de.thkoeln.swp.wawi.steuerungapi;
    requires java.logging;


}