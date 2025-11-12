package fishing;

//SceneManager.java

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fishing.model.Player;

import java.io.IOException;

public class SceneManager {
 private static Stage primaryStage;
 private static Player currentPlayer;
 
 public static void initialize(Stage stage) {
     primaryStage = stage;
     primaryStage.setTitle(MainApp.APP_TITLE);
     primaryStage.setWidth(MainApp.WINDOW_WIDTH);
     primaryStage.setHeight(MainApp.WINDOW_HEIGHT);
     primaryStage.setResizable(false);
     primaryStage.centerOnScreen();
     primaryStage.show();
 }
 
 public static void switchScene(String sceneName) {
     try {
         String fxmlPath = "/fxml/" + sceneName + ".fxml";
         FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
         Parent root = loader.load();
         
         Scene scene = new Scene(root, MainApp.WINDOW_WIDTH, MainApp.WINDOW_HEIGHT);
         
         // Load CSS stylesheet
         String css = SceneManager.class.getResource("/css/style.css").toExternalForm();
         scene.getStylesheets().add(css);
         
         primaryStage.setScene(scene);
     } catch (IOException e) {
         System.err.println("Error loading scene: " + sceneName);
         e.printStackTrace();
     }
 }
 
 public static void setPlayer(Player player) {
     currentPlayer = player;
 }
 
 public static Player getPlayer() {
     return currentPlayer;
 }
 
 public static Stage getPrimaryStage() {
     return primaryStage;
 }
}