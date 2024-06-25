/*****************************************************************
 * Autor: Leonel Nguimatsia                                      *
 * @version: IntelliJ2021 JDK17                                  *
 * Hochschule: TH-Kloen                                          *
 * Ort: Deutz Technische Hochschule                              *
 * Webmail: leonel.nguimatsia_tsobguim@smail.th-koeln.de         *
 * Beschreibung: EntitaetsInterface IAdminSteuerungKategorie     *
 *                                                               *
 *****************************************************************/

package de.thkoeln.swp.wawi.adminsteuerung.api;

import de.thkoeln.swp.wawi.adminsteuerung.impl.SteuerungKategorieImpl;
import de.thkoeln.swp.wawi.adminsteuerung.grenzklassen.KategorieGrenz;

import java.util.List;

public interface IAdminSteuerungKategorie {


    /**
     * Siehe {@link SteuerungKategorieImpl#getKategorieById(int)} getKategorieById(int)
     */
     public KategorieGrenz getKategorieById(int kategorieId);


    /**
     * Siehe {@link SteuerungKategorieImpl#addKategorie}} addKategorie(KategorieGrenz)
     */
    public Boolean addKategorie(KategorieGrenz kategorieGrenz);


    /**
     * Siehe {@link SteuerungKategorieImpl#updateKategorie}} updateKategorie(KategorieGrenz)
     */
    public boolean updateKategorie(KategorieGrenz kategorieGrenz);


    /**
     * Siehe {@link SteuerungKategorieImpl#deleteKategorie(int)} deleteKategorie(int)
     */
     public boolean deleteKategorie(int deleteKategorieId);


    /**
     * Siehe {@link SteuerungKategorieImpl#getKategorien()} getKategorien()
     */
     public List<KategorieGrenz> getKategorien();

}
