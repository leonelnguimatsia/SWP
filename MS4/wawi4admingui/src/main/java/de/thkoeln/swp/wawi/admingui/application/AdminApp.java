package de.thkoeln.swp.wawi.admingui.application;


import de.thkoeln.swp.wawi.admingui.util.*;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;
import java.util.*;


public class AdminApp extends Application {
    
    
    private static AdminApp instance;
    
    public static AdminApp getInstance() {
        return instance;
    }
    
    
    private final Map<String, Stage> stages = new HashMap<>();
    private Stage currentStage;
    
    
    private Scene sceneFromFxml(String fxmlPath) {
        try {
            return new Scene(FXMLLoader.load(AdminApp.class.getResource(fxmlPath)),
                             -1, -1, false, SceneAntialiasing.BALANCED);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load scene!", e);
        }
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        instance = this;
        
        Stage admin = new Stage();
        stages.put("admin", admin);
        currentStage = admin;
        
        admin.setTitle("SWP - WAWI - Admin: Hauptmenü");
        admin.setScene(sceneFromFxml("/admin.fxml"));
        admin.show();
    }
    
    public void closeAdminApp() {
        stages.values().forEach(Stage::close);
    }
    
    public void exit() {
        Platform.exit();
    }
    
    
    public void hideMainMenu() {
        Stage admin = stages.get("admin");
        if (admin.isShowing())
            admin.setIconified(true);
    }
    
    public void showMainMenu() {
        Stage admin = stages.get("admin");
        admin.show();
        admin.setIconified(false);
        admin.requestFocus();
    }
    
    
    public synchronized Stage openProductView() {
        final Stage pv = stages.getOrDefault("productView", new Stage());
        currentStage = pv;
        
        if (stages.containsKey("productView")) {
            if (!pv.isShowing())
                pv.show();
            pv.requestFocus();
        } else {
            stages.put("productView", pv);
    
            pv.setScene(sceneFromFxml("/productView.fxml"));
            pv.setTitle("SWP - WAWI - Admin: Produkte & Kategorien");
            pv.setOnShown(e -> hideMainMenu());
            pv.setOnCloseRequest(e -> {
                pv.close();
                showMainMenu();
            });
            pv.show();
        }
        
        return pv;
    }
    
    public Stage getProductStage() {
        return getStage("productView");
    }
    
    
    public synchronized Stage openWarehouseView() {
        final Stage wv = stages.getOrDefault("warehouseView", new Stage());
        currentStage = wv;
        
        if (stages.containsKey("warehouseView")) {
            if (!wv.isShowing())
                wv.show();
            wv.requestFocus();
        } else {
            stages.put("warehouseView", wv);
        
            wv.setScene(sceneFromFxml("/warehouseView.fxml"));
            wv.setTitle("SWP - WAWI - Admin: Lager");
            wv.setOnShown(e -> hideMainMenu());
            wv.setOnCloseRequest(e -> {
                wv.close();
                showMainMenu();
            });
            wv.show();
        }
    
        return wv;
    }
    
    public Stage getWarehouseStage() {
        return getStage("warehouseView");
    }
    
    
    @SuppressWarnings("ClassEscapesDefinedScope")
    public synchronized Stage createStage(Stage parent, String fxmlPath, Parameter... params) {
        final String baseName = new File(fxmlPath).getName().replaceAll("\\.fxml$", "");
        
        String name = baseName;
        for (int i = 1; stages.containsKey(name); i++)
            name = baseName + i;
        
        Stage stage = new Stage();
        stages.put(name, stage);
        currentStage = stage;
        
        stage.setTitle("SWP - WAWI - Admin");
        stage.setScene(sceneFromFxml(fxmlPath));
        
        // init modality for dialogs
        if (parent == null)
            stage.initModality(Modality.NONE);
        else {
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parent);
        }
        
        // pass parameters
        Parameter[] p;
        if (params != null) {
            p = new Parameter[params.length + 2];
            System.arraycopy(params, 0, p, 2, params.length);
        } else
            p = new Parameter[2];
        p[0] = new Parameter("stage", stage);
        p[1] = new Parameter("stageName", name);
        stage.fireEvent(new DialogEvent(true, parent, p));
        
        return stage;
    }
    
    public Stage getStage(String name) {
        return stages.get(name);
    }
    
    public Stage getLatestStage() {
        return currentStage;
    }
    
    public void closeStage(String name) {
        Stage s = stages.remove(name);
        if (s != null && s.isShowing())
            s.close();
    }
    
    public void closeStage(Stage stage) {
        if (stages.values().remove(stage) && stage.isShowing())
            stage.close();
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
