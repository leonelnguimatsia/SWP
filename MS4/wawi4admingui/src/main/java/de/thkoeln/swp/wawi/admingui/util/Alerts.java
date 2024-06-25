package de.thkoeln.swp.wawi.admingui.util;


import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Window;

import java.util.Optional;


public final class Alerts {
    
    
    private Alerts() {}
    
    
    public static final ButtonType CANCEL = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
    public static final ButtonType CLOSE = new ButtonType("Schließen", ButtonData.CANCEL_CLOSE);
    public static final ButtonType UPDATE = new ButtonType("Aktualisieren", ButtonData.APPLY);
    public static final ButtonType REMOVE = new ButtonType("Entfernen", ButtonData.APPLY);
    public static final ButtonType APPLY = new ButtonType("Anwenden", ButtonData.APPLY);
    public static final ButtonType OK = new ButtonType("OK", ButtonData.OK_DONE);
    public static final ButtonType YES = new ButtonType("Ja", ButtonData.YES);
    public static final ButtonType NO = new ButtonType("Nein", ButtonData.NO);
    
    
    public static Optional<ButtonType> showAlert(Alert.AlertType type, Window owner, String msg, String head,
                                                 String title, ButtonType... buttons) {
        Alert alert = new Alert(type);
        if (owner != null)
            alert.initOwner(owner);
        if (msg != null)
            alert.setContentText(msg);
        if (head != null)
            alert.setHeaderText(head);
        if (title != null)
            alert.setTitle(title);
        if (buttons != null && buttons.length > 0) {
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(buttons);
        }
        return alert.showAndWait();
    }
    
    
    public static void showMessage(Window owner, String msg, String head, String title) {
        showAlert(AlertType.INFORMATION, owner, msg, head, title, OK);
    }
    
    public static void showWarning(Window owner, String msg, String head, String title) {
        showAlert(AlertType.WARNING, owner, msg, head, title, OK);
    }
    
    public static void showError(Window owner, String msg, String head, String title) {
        showAlert(AlertType.ERROR, owner, msg, head, title, OK);
    }
    
    
    public static Optional<ButtonType> showYesNoDialog(Window owner, String msg, String head, String title) {
        return showAlert(AlertType.CONFIRMATION, owner, msg, head, title, YES, NO);
    }
    
    public static Optional<ButtonType> showYesCancelDialog(Window owner, String msg, String head, String title) {
        return showAlert(AlertType.CONFIRMATION, owner, msg, head, title, YES, CANCEL);
    }
    
    public static Optional<ButtonType> showYesNoCancelDialog(Window owner, String msg, String head, String title) {
        return showAlert(AlertType.CONFIRMATION, owner, msg, head, title, YES, NO, CANCEL);
    }
    
    
    public static boolean agreed(Optional<?> o) {
        return o.isPresent() && (o.get() == YES || o.get() == OK || o.get() == APPLY ||
                                 o.get() == REMOVE || o.get() == UPDATE);
    }
    
    public static boolean denied(Optional<?> o) {
        return o.isPresent() && (o.get() == NO || o.get() == CANCEL);
    }
    
    public static boolean closed(Optional<?> o) {
        return o.isEmpty() || o.get() == CLOSE;
    }
    
    public static boolean deniedOrClosed(Optional<?> o) {
        return denied(o) || closed(o);
    }
    
}
