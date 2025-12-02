package main;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import data.PlayerDataManager;
import javafx.util.Duration;
import model.Player;
import view.DashboardScreen;

import java.net.URL; // Import for resource loading
import java.util.Map;

public class Main extends Application {
    
    private Stage primaryStage;
    private PlayerDataManager dataManager;
    private Player currentPlayer;
    
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.dataManager = new PlayerDataManager();

        Scene splash = makeSplashScene(primaryStage);
        primaryStage.setTitle("Fishda");
        primaryStage.setScene(splash);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    
    /**
     * Called when the application is closing, saving data if a player is loaded.
     */
    @Override
    public void stop() {
        
        // Check if a player object is currently loaded
        if (currentPlayer != null) { 
            // THIS MUST BE PRESENT TO RESOLVE THE ERROR:
            try {
                dataManager.saveGame(currentPlayer);
                System.out.println("💾 Game data auto-saved successfully on exit.");
            } catch (Exception e) {
                // Catching Exception covers the IOException thrown by the file operations
                System.err.println("❌ ERROR: Failed to auto-save game on exit!");
                e.printStackTrace();
            }
        }
    }
    
    private Scene makeWelcomeScene() {
        // Create root StackPane for layering
        StackPane root = new StackPane();
        
        // Background GIF
        ImageView bgImageView = new ImageView();
        try {
            // FIX: Use getResource() for image loading to simplify path handling
            URL bgUrl = getClass().getResource("/backgrounds/login/fishing_dock_login.gif");
            if (bgUrl != null) {
                Image bgImage = new Image(bgUrl.toExternalForm());
                bgImageView.setImage(bgImage);
                bgImageView.setFitWidth(1280);
                bgImageView.setFitHeight(720);
                bgImageView.setPreserveRatio(false);
            } else {
                 System.err.println("Could not find login background image.");
            }
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            root.getStyleClass().add("login-background"); // Fallback CSS style
        }
        
        // Center panel with translucent background
        VBox centerPanel = new VBox(12);
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setMaxWidth(550);
        centerPanel.setMaxHeight(250);
        centerPanel.getStyleClass().add("login-panel");
        
        // Title
        Label titleLabel = new Label("🎣 Fishing Adventure");
        titleLabel.getStyleClass().add("title-label");
        
        // Username Fields
        Label usernameLabel = new Label("Username:");
        usernameLabel.getStyleClass().add("field-label");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(300);
        usernameField.getStyleClass().add("input-field");
        
        // Password Fields
        Label passwordLabel = new Label("Password:");
        passwordLabel.getStyleClass().add("field-label");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(300);
        passwordField.getStyleClass().add("input-field");
        
        // Error label
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(300);
        errorLabel.setAlignment(Pos.CENTER);
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().addAll("btn", "btn-success");
        
        Button registerButton = new Button("Register");
        registerButton.getStyleClass().addAll("btn", "btn-info");
        
        buttonBox.getChildren().addAll(loginButton, registerButton);
        
        // Add all elements to center panel
        centerPanel.getChildren().addAll(
            titleLabel,
            usernameLabel,
            usernameField,
            passwordLabel,
            passwordField,
            errorLabel,
            buttonBox
        );
        
        // Event handlers
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            
            // Validation and Login Logic...
            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.getStyleClass().clear();
                errorLabel.getStyleClass().add("error-label");
                errorLabel.setText("Please fill in all fields!");
                return;
            }
            
            if (dataManager.validateLogin(username, password)) {
                errorLabel.getStyleClass().clear();
                errorLabel.getStyleClass().add("success-label");
                errorLabel.setText("Login successful!");
                
                Player player = dataManager.loadGame(username); 
                this.currentPlayer = player; 
                
                PauseTransition pause = new PauseTransition(Duration.seconds(0.5)); 
                pause.setOnFinished(evt -> showDashboard(player));
                pause.play();
            } else {
                errorLabel.getStyleClass().clear();
                errorLabel.getStyleClass().add("error-label");
                errorLabel.setText("Invalid username or password!");
                
                passwordField.clear(); 
                usernameField.requestFocus();
            }
        });
        
        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            
            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.getStyleClass().remove("success-label");
                errorLabel.getStyleClass().add("error-label");
                errorLabel.setText("Please fill in all fields!");
                return;
            }
            // ... (Your length checks remain here) ...
            
            if (dataManager.registerUser(username, password)) {
                errorLabel.getStyleClass().remove("error-label");
                errorLabel.getStyleClass().add("success-label");
                errorLabel.setText("Registration successful! You can now login.");
                usernameField.clear();
                passwordField.clear();
            } else {
                errorLabel.getStyleClass().remove("success-label");
                errorLabel.getStyleClass().add("error-label");
                errorLabel.setText("Username already taken!");
            }
        });
        
        // Add background and panel to root
        root.getChildren().addAll(bgImageView, centerPanel);

        Scene scene = new Scene(root, 1280, 720);

        // FIX: Standardize CSS loading to use the classpath root (/)
        try {
            URL cssUrl = getClass().getResource("/stylesheet/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("❌ ERROR: CSS file not found at /stylesheet/styles.css");
            }
        } catch (Exception e) {
            System.err.println("Could not load CSS file: " + e.getMessage());
        }

        return scene;
    }

    private Scene makeSplashScene(Stage stage) {

        StackPane splashLayout = new StackPane();

        // Background image
        // FIX: Standardize image loading
        ImageView bg = new ImageView();
        try {
            URL bgUrl = getClass().getResource("/backgrounds/splash/splashbg.png");
            if (bgUrl != null) {
                bg.setImage(new Image(bgUrl.toExternalForm()));
                bg.setFitWidth(1280);
                bg.setFitHeight(720);
                bg.setPreserveRatio(false);
            } else {
                System.err.println("Could not find splash background image.");
            }
        } catch (Exception e) {
             System.err.println("Error loading splash background: " + e.getMessage());
        }


        // Warm overlay
        Region overlay = new Region();
        overlay.setId("splash-overlay");
        overlay.setPrefSize(1280, 720);

        // Center logo
        Label logo = new Label("FISHDA");
        logo.setId("splash-logo");
        StackPane.setAlignment(logo, Pos.CENTER);

        // Footer text
        Label footer = new Label("© 2025 Charlie Kirk Studio");
        footer.setId("splash-footer");
        StackPane.setAlignment(footer, Pos.BOTTOM_CENTER);

        // footer background
        Region footerBg = new Region();
        footerBg.setPrefHeight(30);
        footerBg.setMaxWidth(Double.MAX_VALUE);

        StackPane footerContainer = new StackPane(footerBg, footer);
        StackPane.setAlignment(footerContainer, Pos.BOTTOM_CENTER);


        // Add to layout
        splashLayout.getChildren().addAll(bg, overlay, logo, footerContainer);

        // Scene
        Scene splashScene = new Scene(splashLayout, 1280, 720);
        
        // FIX: Standardize CSS loading for the splash scene too
        try {
            URL cssUrl = getClass().getResource("/stylesheet/styles.css");
            if (cssUrl != null) {
                splashScene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("❌ ERROR: CSS file not found at /stylesheet/styles.css");
            }
        } catch (Exception e) {
             System.err.println("Could not load CSS file for splash scene: " + e.getMessage());
        }

        // Fade in effect
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), splashLayout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Wait 2 seconds, then fade out
        PauseTransition wait = new PauseTransition(Duration.seconds(2));
        wait.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), splashLayout);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> stage.setScene(makeWelcomeScene()));
            fadeOut.play();
        });
        wait.play();
        return splashScene;
    }

    private void showDashboard(Player player) {
        DashboardScreen dashboard = new DashboardScreen(primaryStage, player);
        primaryStage.setScene(dashboard.getScene());
    }


    
    public static void main(String[] args) {
        launch(args);
    }
}