package de.thkoeln.swp.wawi.adminsteuerung.impl;


import de.thkoeln.swp.wawi.steuerungapi.grenz.*;
import de.thkoeln.swp.wawi.wawidbmodel.entities.*;

import java.util.*;
import java.util.logging.Logger;


/**
 * This class provides utility functions to convert entity objects into boundary objects and vice versa. <br/>
 *
 * @author Fabian Ullmann (WAWI4-Admin)
 * @since 16.06.2022
 */
final class EntityGrenzConverter {
    
    
    private static final Logger LOGGER = Logger.getLogger(EntityGrenzConverter.class.getName());
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private EntityGrenzConverter() {}
    
    
    static ProduktGrenz toGrenz(Produkt produkt) {
        if (produkt == null)
            return null;
        
        return new ProduktGrenz(produkt.getProdid(), produkt.getName(), produkt.getBeschreibung(), produkt.getAngelegt(),
                                produkt.getStueckzahl(), produkt.getNettopreis(), produkt.getMwstsatz(),
                                produkt.getAktiv(), toGrenz(produkt.getKategorie()), toGrenz(produkt.getLagerort()));
    }
    
    static KategorieGrenz toGrenz(Kategorie kategorie) {
        if (kategorie == null)
            return null;
        
        return new KategorieGrenz(kategorie.getKatid(), kategorie.getName(), kategorie.getBeschreibung(),
                                  kategorie.getParentkatid());
    }
    
    static LieferpositionGrenz toGrenz(Lieferposition lieferposition) {
        if (lieferposition == null)
            return null;
        
        return new LieferpositionGrenz(lieferposition.getLpid(), lieferposition.getAnzahl(),
                                       lieferposition.getKaufpreis(), null, toGrenz(lieferposition.getProdukt()),
                                       lieferposition.getProdukt().getName(), lieferposition.getProdukt().getBeschreibung(),
                                       lieferposition.getProdukt().getStueckzahl() + lieferposition.getAnzahl());
        
    }
    
    static EinlieferungGrenz toGrenz(Einlieferung einlieferung) {
        if (einlieferung == null)
            return null;
        
        final List<LieferpositionGrenz> llg = einlieferung.getLieferpositionList().stream()
                                                          .map(EntityGrenzConverter::toGrenz).toList();
        return new EinlieferungGrenz(einlieferung.getElfid(), einlieferung.getCreated(), einlieferung.getGesamtpreis(),
                                     null, llg, null);
    }
    
    static BestellungspositionGrenz toGrenz(Bestellungsposition bestellungsposition) {
        if (bestellungsposition == null)
            return null;
        
        return new BestellungspositionGrenz(bestellungsposition.getBpid(), bestellungsposition.getAnzahl(),
                                            null, toGrenz(bestellungsposition.getProdukt()),
                                            bestellungsposition.getProdukt().getName(),
                                            bestellungsposition.getProdukt().getBeschreibung());
    }
    
    static BestellungGrenz toGrenz(Bestellung bestellung) {
        if (bestellung == null)
            return null;
        
        final List<BestellungspositionGrenz> lbg = bestellung.getBestellungspositionList().stream()
                                                             .map(EntityGrenzConverter::toGrenz).toList();
        return new BestellungGrenz(bestellung.getBid(), bestellung.getLieferadresse(), bestellung.getRechnungsadresse(),
                                   bestellung.getCreated(), bestellung.getStatus(), bestellung.getGesamtnetto(),
                                   bestellung.getGesamtbrutto(), null, lbg);
    }
    
    static LagerverkehrGrenz toGrenz(Lagerverkehr lagerverkehr) {
        if (lagerverkehr == null)
            return null;
        
        return new LagerverkehrGrenz(lagerverkehr.getLgvid(), toGrenz(lagerverkehr.getEinlieferung()),
                                     toGrenz(lagerverkehr.getBestellung()), lagerverkehr.getCreated(),
                                     lagerverkehr.getStatus(), null);
    }
    
    static LagerortGrenz toGrenz(Lagerort lagerort) {
        if (lagerort == null)
            return null;
        
        return new LagerortGrenz(lagerort.getLgortid(), lagerort.getBezeichnung(), lagerort.getKapazitaet(),
                                 toGrenz(lagerort.getLager()));
    }
    
    static LagerGrenz toGrenz(Lager lager) {
        if (lager == null)
            return null;
        
        return new LagerGrenz(lager.getLagerid(), lager.getName(), lager.getAddresse());
    }
    
    
    static Kategorie toEntity(KategorieGrenz kategorie) {
        if (kategorie == null)
            return null;
        
        return new Kategorie(kategorie.getKategorieId(), kategorie.getParentKategorieId(), kategorie.getName(),
                             kategorie.getBeschreibung());
    }
    
    static Produkt toEntity(ProduktGrenz produkt) {
        if (produkt == null)
            return null;
        
        Produkt p = new Produkt(produkt.getProduktId(), produkt.getName(), produkt.getBeschreibung(),
                                produkt.getAngelegt(), produkt.getStueckzahl(), produkt.getNettopreis(),
                                produkt.getMwstsatz(), produkt.isAktiv());
        p.setKategorie(toEntity(produkt.getKategorieGrenz()));
        p.setLagerort(toEntity(produkt.getLagerortGrenz()));
        return p;
    }
    
    static Lagerort toEntity(LagerortGrenz lagerort) {
        if (lagerort == null)
            return null;
        
        Lagerort l = new Lagerort(lagerort.getLagerortId(), lagerort.getBezeichnung(), lagerort.getKapazitaet());
        l.setLager(toEntity(lagerort.getLagerGrenz()));
        return l;
    }
    
    static Lager toEntity(LagerGrenz lager) {
        if (lager == null)
            return null;
        
        return new Lager(lager.getLagerId(), lager.getName(), lager.getAdresse());
    }
    
    
}
