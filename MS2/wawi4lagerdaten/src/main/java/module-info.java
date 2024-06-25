
import de.thkoeln.swp.wawi.lagerdaten.ICRUDLagerImpl;

import de.thkoeln.swp.wawi.lagerdaten.ILagerServiceImpl;


module de.thkoeln.swp.wawi.lagerdaten {
    requires de.thkoeln.swp.wawi.datenhaltungapi;
    requires de.thkoeln.swp.wawi.wawidbmodel;
    requires java.logging;


    provides de.thkoeln.swp.wawi.datenhaltungapi.ICRUDLager with ICRUDLagerImpl;

    provides de.thkoeln.swp.wawi.datenhaltungapi.ILagerService with ILagerServiceImpl;
    provides de.thkoeln.swp.wawi.datenhaltungapi.ICRUDLieferant with de.thkoeln.swp.wawi.lagerdaten.ICRUDLieferantImpl;

}