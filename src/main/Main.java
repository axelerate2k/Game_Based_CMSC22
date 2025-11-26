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

import java.util.Map;

public class Main extends Application {
    
    private Stage primaryStage;
    private PlayerDataManager dataManager;
    
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


    private Scene makeWelcomeScene() {
        // Create root StackPane for layering
        StackPane root = new StackPane();
        
        // Background GIF
        ImageView bgImageView = new ImageView();
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/backgrounds/login/fishing_dock_login.gif"));
            bgImageView.setImage(bgImage);
            bgImageView.setFitWidth(1280);
            bgImageView.setFitHeight(720);
            bgImageView.setPreserveRatio(false);
        } catch (Exception e) {
            System.err.println("Could not load background image: " + e.getMessage());
            // Fallback to solid color background
            root.getStyleClass().add("login-background");
        }
        
        // Center panel with translucent background
        VBox centerPanel = new VBox(12); // tighter spacing (12 instead of 20)
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setMaxWidth(550);
        centerPanel.setMaxHeight(250);
        centerPanel.getStyleClass().add("login-panel");
        
        // Title
        Label titleLabel = new Label("🎣 Fishing Adventure");
        titleLabel.getStyleClass().add("title-label");
        
        // Username Label
        Label usernameLabel = new Label("Username:");
        usernameLabel.getStyleClass().add("field-label");

        // text field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(300);
        usernameField.getStyleClass().add("input-field");
        
        // Password Label
        Label passwordLabel = new Label("Password:");
        passwordLabel.getStyleClass().add("field-label");

        // text field
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
            // Removed Spacer
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
            
            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.getStyleClass().remove("success-label");
                errorLabel.getStyleClass().add("error-label");
                errorLabel.setText("Please fill in all fields!");
                return;
            }
            
            if (dataManager.validateLogin(username, password)) {
                errorLabel.getStyleClass().remove("error-label");
                errorLabel.getStyleClass().add("success-label");
                errorLabel.setText("Login successful!");
                
                // Load player data and transition to dashboard
                Map<String, String> playerData = dataManager.loadPlayerData(username);
                Player player = loadPlayerFromData(username, playerData);
                
                // Show dashboard after short delay
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(0.5));
                pause.setOnFinished(evt -> showDashboard(player));
                pause.play();
            } else {
                errorLabel.getStyleClass().remove("success-label");
                errorLabel.getStyleClass().add("error-label");
                errorLabel.setText("Invalid credentials!");
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
            
            if (username.length() < 3) {
                errorLabel.getStyleClass().remove("success-label");
                errorLabel.getStyleClass().add("error-label");
                errorLabel.setText("Username must be at least 3 characters!");
                return;
            }
            
            if (password.length() < 4) {
                errorLabel.getStyleClass().remove("success-label");
                errorLabel.getStyleClass().add("error-label");
                errorLabel.setText("Password must be at least 4 characters!");
                return;
            }
            
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

        // Load external CSS file
        try {
            scene.getStylesheets().add(getClass().getResource("/stylesheet/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS file: " + e.getMessage());
        }

        return scene;
    }

    private Scene makeSplashScene(Stage stage) {

        // Create the splash layout
        StackPane splashLayout = new StackPane();

        // Background image
        ImageView bg = new ImageView(new Image(getClass().getResourceAsStream("/backgrounds/splash/splashbg.png")));
        bg.setFitWidth(1280);
        bg.setFitHeight(720);
        bg.setPreserveRatio(false);

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
        footerBg.setPrefHeight(30); // height of the box
        footerBg.setMaxWidth(Double.MAX_VALUE);

        StackPane footerContainer = new StackPane(footerBg, footer);
        StackPane.setAlignment(footerContainer, Pos.BOTTOM_CENTER);


        // Add to layout
        splashLayout.getChildren().addAll(bg, overlay, logo, footerContainer);

        // Scene
        Scene splashScene = new Scene(splashLayout, 1280, 720);
        splashScene.getStylesheets().addAll(getClass().getResource("/stylesheet/styles.css").toExternalForm());

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


    private Player loadPlayerFromData(String username, Map<String, String> data) {
        Player player = new Player(username);

        if (data == null) return player;

        try {
            if (data.containsKey("coins"))
                player.setCoins(Integer.parseInt(data.get("coins")));

            if (data.containsKey("xp"))
                player.setXp(Integer.parseInt(data.get("xp")));

            if (data.containsKey("equippedRod"))
                player.setEquippedRod(data.get("equippedRod"));

      
        } catch (Exception e) {
            System.err.println("Error loading player data: " + e.getMessage());
        }

        return player;
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}