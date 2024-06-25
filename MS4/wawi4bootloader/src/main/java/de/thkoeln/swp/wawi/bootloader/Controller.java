package de.thkoeln.swp.wawi.bootloader;

import de.thkoeln.swp.wawi.admingui.application.AdminApp;
import de.thkoeln.swp.wawi.lagergui.application.LagerApp;
import de.thkoeln.swp.wawi.kundegui.application.KundeApp;
import de.thkoeln.swp.wawi.sachbearbeitergui.application.SachbearbeiterApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import de.thkoeln.swp.wawi.componentcontroller.services.IActivateComponent;

import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Controller implements Initializable {
    @FXML
    ChoiceBox cbComponent;
    @FXML
    ImageView ampelAdmin;
    @FXML
    ImageView ampelKunde;
    @FXML
    ImageView ampelSach;
    @FXML
    ImageView ampelLager;
    @FXML
    Button bt_login;
    @FXML
    TextField logTextField;
    @FXML
    TextField tf_login;

    Image greenAmpel = new Image("greenlight24x24.png");
    Image yellowAmpel = new Image("yellowlight24x24.png");

    Logger logger = LoggerFactory.getLogger("de.thkoeln.swp.wawi.bootloader");


    private List<IActivateComponent> actList;

    private AdminApp adminApp;
    private LagerApp lagerApp;
    private KundeApp kundeApp;
    private SachbearbeiterApp sachbearbeiterApp;

    private String defaultTextfieldStyle;

    @FXML
    public void loginButtonClicked(){

        Stage stage = (Stage)bt_login.getScene().getWindow();

        logTextField.setStyle(defaultTextfieldStyle);
        String component = cbComponent.getValue().toString();
        logger.debug("loginButtonClicked:" + component);
        try {
            switch (component) {
                case "Admin":
                    if (actList.get(0).activateComponent(Integer.valueOf(tf_login.getText()))) {
                        adminApp = new AdminApp();
                        adminApp.start(stage);
                        ampelAdmin.setImage(greenAmpel);
                        logTextField.setText("Admin Login successful");
                        logger.debug("Admin Login Successful");
                    } else {
                        logTextField.setStyle("-fx-background-color: red;");
                        logTextField.setText("Admin Login failed");
                        ampelAdmin.setImage(yellowAmpel);
                        logger.debug("Admin Login failed");
                    }
                    break;

                case "Kunde":
                    if (actList.get(1).activateComponent(Integer.valueOf(tf_login.getText()))) {
                        kundeApp = new KundeApp();
                        kundeApp.start(stage);
                        ampelKunde.setImage(greenAmpel);
                        logTextField.setText("Kunde Login successful");
                        logger.debug("Kunde Login succesfull");
                    } else {
                        logTextField.setStyle("-fx-background-color: red;");
                        logTextField.setText("Kunde Login failed");
                        ampelKunde.setImage(yellowAmpel);
                        logger.debug("Kunde Login failed");
                    }
                    break;
                case "Sachbearbeiter":
                    if (actList.get(2).activateComponent(Integer.valueOf(tf_login.getText()))) {
                        sachbearbeiterApp = new SachbearbeiterApp();
                        sachbearbeiterApp.start(stage);
                        ampelSach.setImage(greenAmpel);
                        logTextField.setText("Sachbearbeiter Login successful");
                        logger.debug("Sachbearbeiter Login Successful");
                    } else {
                        logTextField.setStyle("-fx-background-color: red;");
                        logTextField.setText("Sachbearbeiter Login failed");
                        ampelSach.setImage(yellowAmpel);
                        logger.debug("Sachbearbeiter Login failed");
                    }
                    break;

                case "Lager":
                    if (actList.get(3).activateComponent(Integer.valueOf(tf_login.getText()))) {
                        lagerApp = new LagerApp();
                        lagerApp.start(stage);
                        ampelLager.setImage(greenAmpel);
                        logTextField.setText("Lager Login successful");
                        logger.debug("Lager Login successful");
                    } else {
                        logTextField.setStyle("-fx-background-color: red;");
                        logTextField.setText("Lager Login failed");
                        ampelLager.setImage(yellowAmpel);
                        logger.debug("Lager Login failed");
                    }
                    break;
            }
        } catch (UnsupportedOperationException e){
            logTextField.setStyle("-fx-background-color: red;");
            logTextField.setText("Not Yet Supported !!");
            logger.debug("Login: Not Yet Supported!");
        }
    }

    @FXML
    public void logoutButtonClicked(){
        String component = cbComponent.getValue().toString();
        logger.debug("logoutButtonClicked: "+component);
        try {
            switch (component) {
                case "Admin":
                    if (actList.get(0).deactivateComponent()) {
                        if (adminApp != null) adminApp.closeAdminApp();
                        ampelAdmin.setImage(yellowAmpel);
                        logTextField.setText("Admin Logout successful");
                        logger.debug("Admin Logout successful");
                    } else {
                        logTextField.setStyle("-fx-background-color: red;");
                        logTextField.setText("Admin was not logged in!");
                        logger.debug("Admin was not logged in");
                    }
                    break;

                case "Kunde":
                    if (actList.get(1).deactivateComponent()) {
                        if (kundeApp != null) kundeApp.closeKundeApp();
                        ampelKunde.setImage(yellowAmpel);
                        logTextField.setText("Kunde Logout successful");
                        logger.debug("Kunde Logout successful");
                    } else {
                        logTextField.setStyle("-fx-background-color: red;");
                        logTextField.setText("Kunde was not logged in!");
                        ampelKunde.setImage(yellowAmpel);
                        logger.debug("Kunde was not logged in");
                    }
                    break;
                case "Sachbearbeiter":
                    if (actList.get(2).deactivateComponent()) {
                        if (sachbearbeiterApp != null) sachbearbeiterApp.closeSachbearbeiterApp();
                        ampelSach.setImage(yellowAmpel);
                        logTextField.setText("Sachbearbeiter Logout successful");
                        logger.debug("Sachbearbeiter Logout successful");
                    } else {
                        logTextField.setStyle("-fx-background-color: red;");
                        logTextField.setText("Sachbearbeiter was not logged in!");
                        ampelSach.setImage(yellowAmpel);
                        logger.debug("Sachbearbeiter was not logged in");
                    }
                    break;

                case "Lager":
                    if (actList.get(3).deactivateComponent()) {
                        if (lagerApp != null) lagerApp.closeLagerApp();
                        ampelLager.setImage(yellowAmpel);
                        logTextField.setText("Lager Logout successful");
                        logger.debug("Lagerhalter Logout successful");
                    } else {
                        logTextField.setStyle("-fx-background-color: red;");
                        logTextField.setText("Lager was not logged in!");
                        ampelLager.setImage(yellowAmpel);
                        logger.debug("Lagerhalter was not logged in");
                    }
                    break;
            }
        } catch (UnsupportedOperationException e){
            logTextField.setStyle("-fx-background-color: red;");
            logTextField.setText("Not Yet Supported !!");
            logger.debug("Logout: Not Yet Supported");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.debug("initialize  Components");
        defaultTextfieldStyle = logTextField.getStyle();
        actList = new ArrayList<>();
        actList.add(new de.thkoeln.swp.wawi.adminsteuerung.impl.IActivateComponentImpl());
        actList.add(new de.thkoeln.swp.wawi.kundesteuerung.impl.IActivateComponentImpl());
        actList.add(new de.thkoeln.swp.wawi.sachbearbeitersteuerung.impl.IActivateComponentImpl());
        actList.add(new de.thkoeln.swp.wawi.lagersteuerung.impl.IActivateComponentImpl());
    }
}
