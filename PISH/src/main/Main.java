package main;

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
        
        showLoginScreen();
        
        primaryStage.setTitle("Fishing Adventure");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    private void showLoginScreen() {
        // Create root StackPane for layering
        StackPane root = new StackPane();
        
        // Background GIF
        ImageView bgImageView = new ImageView();
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/fishing_dock_login.gif"));
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
        VBox centerPanel = new VBox(20);
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setPadding(new Insets(40));
        centerPanel.setMaxWidth(400);
        centerPanel.setMaxHeight(500);
        centerPanel.getStyleClass().add("login-panel");
        
        // Title
        Label titleLabel = new Label("🎣 Fishing Adventure");
        titleLabel.getStyleClass().add("title-label");
        
        // Username field
        Label usernameLabel = new Label("Username:");
        usernameLabel.getStyleClass().add("field-label");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(300);
        usernameField.getStyleClass().add("input-field");
        
        // Password field
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
            new Region(), // Spacer
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
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS file: " + e.getMessage());
        }
        primaryStage.setScene(scene);
    }
    
    private void showDashboard(Player player) {
        DashboardScreen dashboard = new DashboardScreen(primaryStage, player);
        new DashboardScreen(primaryStage, player);

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