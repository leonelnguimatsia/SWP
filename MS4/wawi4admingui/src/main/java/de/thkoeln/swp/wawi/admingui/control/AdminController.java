package de.thkoeln.swp.wawi.admingui.control;


import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import static de.thkoeln.swp.wawi.admingui.util.Alerts.*;


public class AdminController {
    
    // UI elements
    
    @FXML private Button btOpenProductView;
    @FXML private Button btOpenWarehouseView;
    
    
    // init
    
    @FXML
    private void initialize() {
        final String t = "-fx-background-image: url('%s');";
        btOpenProductView.setStyle(String.format(t, getClass().getResource("/icons/btOpenProductViewIcon.png")));
        btOpenWarehouseView.setStyle(String.format(t, getClass().getResource("/icons/btOpenWarehouseViewIcon.png")));
    }
    
    
    // listeners
    
    @FXML
    private void openProductView() {
        AdminApp.getInstance().openProductView();
    }
    
    @FXML
    private void openWarehouseView() {
        AdminApp.getInstance().openWarehouseView();
    }
    
    @FXML
    private void logout() {
        if (agreed(showYesNoDialog(null, "Möchten Sie sich abmelden?", "Abmeldung Bestätigen",
                                   "SWP - WAWI - ADMIN")))
            AdminApp.getInstance().closeAdminApp();
    }
    
    @FXML
    private void exit() {
        if (agreed(showYesNoDialog(null, "Möchten Sie das Programm beenden?", "Programm Beenden",
                                   "SWP - WAWI - ADMIN")))
            AdminApp.getInstance().exit();
    }
    
}
