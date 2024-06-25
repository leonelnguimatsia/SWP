module de.thkoeln.swp.wawi.admingui {

    requires de.thkoeln.swp.wawi.adminsteuerung;
    requires de.thkoeln.swp.wawi.steuerungapi;

    requires javafx.base;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    
    requires java.logging;

    exports de.thkoeln.swp.wawi.admingui.application;

    opens de.thkoeln.swp.wawi.admingui.control to javafx.fxml;
    opens de.thkoeln.swp.wawi.admingui.control.dialogs to javafx.fxml;
}
