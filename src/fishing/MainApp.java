package fishing;

//MainApp.java

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
 public static final int WINDOW_WIDTH = 1280;
 public static final int WINDOW_HEIGHT = 720;
 public static final String APP_TITLE = "Fishing Adventure 🎣";
 
 @Override
 public void start(Stage primaryStage) {
     SceneManager.initialize(primaryStage);
     SceneManager.switchScene("welcome");
 }
 
 public static void main(String[] args) {
     launch(args);
 }
}