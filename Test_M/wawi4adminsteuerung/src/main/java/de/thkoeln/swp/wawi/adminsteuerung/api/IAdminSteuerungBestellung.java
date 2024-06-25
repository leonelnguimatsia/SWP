/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetsInterface IAdminSteuerungBestellung    *
 *                                                               *
 *****************************************************************/

package de.thkoeln.swp.wawi.adminsteuerung.api;

import de.thkoeln.swp.wawi.adminsteuerung.impl.SteuerungBestellungImpl;
import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.BestellungGrenz;

import java.time.LocalDate;
import java.util.List;


public interface IAdminSteuerungBestellung {

    /**
     *  Siehe {@link SteuerungBestellungImpl#getBestellungen()} getBestellungen()
     */

     public List<BestellungGrenz> getBestellungen();


    /**
     *  Siehe {@link SteuerungBestellungImpl#getBestellungenVonBisDatum(LocalDate,LocalDate)} getBestellungenVonBisDatum(LocalDate,LocalDate)
     */

    public List<BestellungGrenz> getBestellungenVonBisDatum(LocalDate localDateVon, LocalDate localDateBis);


    /**
     *  Siehe {@link SteuerungBestellungImpl#getBestellungById(int)} getBestellungById(int )
     */

    public BestellungGrenz getBestellungById(int bestellungById);

}
