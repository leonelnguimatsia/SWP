import de.thkoeln.swp.wawi.datenhaltungapi.*;

module de.thkoeln.swp.wawi.adminsteuerung {
    exports de.thkoeln.swp.wawi.adminsteuerung.impl;

    requires de.thkoeln.swp.wawi.componentcontroller;
    requires de.thkoeln.swp.wawi.wawidbmodel;
    requires de.thkoeln.swp.wawi.datenhaltungapi;
    requires de.thkoeln.swp.wawi.steuerungapi;

    requires java.logging;
    
    uses ICRUDProdukt;
    uses ICRUDKategorie;
    uses ILagerService;
    uses IBestellungAdmin;
}