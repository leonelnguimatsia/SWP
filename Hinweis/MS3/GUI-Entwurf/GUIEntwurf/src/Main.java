import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Main extends Application {
    
    
    private static Main instance = null;
    
    public static Main getInstance() {
        assert instance != null : "JavaFX application was not started!";
        return instance;
    }
    
    
    private Map<String, Stage> stages = new HashMap<>();
    
    
    private Scene sceneFromFxml(URL fxml) {
        try {
            return new Scene(FXMLLoader.load(fxml), -1., -1., false, SceneAntialiasing.BALANCED);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create scene!", e);
        }
    }
    
    
    public void openProductAndCategories() {
        Stage pac = stages.get("productAndCategories");
        if (pac != null) {
            if (!pac.isShowing())
                pac.show();
            pac.requestFocus();
            return;
        }
        
        final Stage main = stages.get("main");
        Stage nw = new Stage();
        stages.put("productAndCategories", nw);
    
        nw.setScene(sceneFromFxml(Main.class.getResource("ProduktKategorie.fxml")));
        nw.setTitle("WAWI Admin: Produkte & Kategorien");
        nw.setOnCloseRequest(e -> {
            nw.close();
            if (!main.isShowing())
                main.show();
            main.requestFocus();
        });
        nw.setOnShown(e -> {
            if (main.isShowing() && !main.isIconified())
                main.setIconified(true);
        });

        nw.show();
    }
    
    public void openStock() {
        Stage stock = stages.get("stock");
        if (stock != null) {
            if (!stock.isShowing())
                stock.show();
            stock.requestFocus();
            return;
        }
        
        final Stage main = stages.get("main");
        Stage nw = new Stage();
        stages.put("stock", nw);
        
        nw.setScene(sceneFromFxml(Main.class.getResource("Lager.fxml")));
        nw.setTitle("WAWI Admin: Bestellungen & Lagerverwaltung");
        nw.setOnCloseRequest(e -> {
            nw.close();
            if (!main.isShowing())
                main.show();
            main.requestFocus();
        });
        nw.setOnShown(e -> {
            if (main.isShowing() && !main.isIconified())
                main.setIconified(true);
        });
    
        nw.show();
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        instance = this;
        stages.put("main", primaryStage);
        
        primaryStage.setScene(sceneFromFxml(Main.class.getResource("MainMenu.fxml")));
        primaryStage.setTitle("WAWI Admin: Hauptmenü");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
