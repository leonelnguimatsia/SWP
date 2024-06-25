module de.thkoeln.swp.wawi.bootloader {

    requires javafx.fxml;
    requires javafx.controls;

    requires de.thkoeln.swp.wawi.componentcontroller;

    requires de.thkoeln.swp.wawi.admingui;
    requires de.thkoeln.swp.wawi.kundegui;
    requires de.thkoeln.swp.wawi.sachbearbeitergui;
    requires de.thkoeln.swp.wawi.lagergui;

    requires de.thkoeln.swp.wawi.adminsteuerung;
    requires de.thkoeln.swp.wawi.kundesteuerung;
    requires de.thkoeln.swp.wawi.sachbearbeitersteuerung;
    requires de.thkoeln.swp.wawi.lagersteuerung;

    requires net.bytebuddy;
    requires org.slf4j;

    exports de.thkoeln.swp.wawi.bootloader;
    opens de.thkoeln.swp.wawi.bootloader to javafx.fxml;
}

