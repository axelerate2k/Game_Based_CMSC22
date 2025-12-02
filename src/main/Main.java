package main;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
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

        // Load fonts
        Font.loadFont(getClass().getResourceAsStream("/fonts/PixelOperator-Bold.ttf"), 20);
        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 18);
        Font.loadFont(getClass().getResourceAsStream("/fonts/PixelOperator.ttf"), 12);       // dialogue / inventory / body text
        Font.loadFont(getClass().getResourceAsStream("/fonts/dogicapixelbold.ttf"), 20);  // UI / labels
        Font.loadFont(getClass().getResourceAsStream("/fonts/BitPotion.ttf"), 28); // titles / splash


        this.primaryStage = stage;
        this.dataManager = new PlayerDataManager();

        Scene splash = makeSplashScene(primaryStage);
        primaryStage.setTitle("Fishing Adventure");
        primaryStage.setScene(splash);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    private Scene makeWelcomeScene() {

        // Layout
        VBox centerPanel;
        Label titleLabel;
        Label usernameLabel;
        TextField usernameField;
        Label passwordLabel;
        PasswordField passwordField;
        Label errorLabel;
        HBox buttonBox;
        Button loginButton;
        Button registerButton;

        final boolean[] isRegistering = {false}; // start in login mode

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
        centerPanel = new VBox(14);
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setMaxWidth(700);
        centerPanel.setMaxHeight(380);
        centerPanel.getStyleClass().add("login-panel");
        
        // Title
        titleLabel = new Label("Fishing Adventure");
        titleLabel.getStyleClass().add("title-label");
        titleLabel.setText(titleLabel.getText().toUpperCase());
        
        // Username Label
        usernameLabel = new Label("Username:");
        usernameLabel.getStyleClass().add("field-label");

        // text field
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(300);
        usernameField.getStyleClass().add("input-field");
        
        // Password Label
        passwordLabel = new Label("Password:");
        passwordLabel.getStyleClass().add("field-label");

        // text field
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(300);
        passwordField.getStyleClass().add("input-field");
        
        // Error label
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(300);
        errorLabel.setAlignment(Pos.CENTER);
        
        // Buttons
        buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        loginButton = new Button("Login");
        loginButton.getStyleClass().addAll("btn", "btn-login");
        
        registerButton = new Button("Register");
        registerButton.getStyleClass().addAll("btn", "btn-register");
        
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


        EventHandler<KeyEvent> enterHandler = event -> {
            if(event.getCode() == KeyCode.ENTER){
                if(isRegistering[0]){
                    handleRegister(isRegistering, usernameField, passwordField, errorLabel);
                } else {
                    handleLogin(usernameField, passwordField, errorLabel);
                }
            }
        };

        // Event handlers for enter key
        usernameField.setOnKeyPressed(enterHandler);
        passwordField.setOnKeyPressed(enterHandler);

        // Event handlers for mouse clicks
        loginButton.setOnMouseClicked(e -> {
            handleLogin(usernameField, passwordField, errorLabel);
        });
        
        registerButton.setOnMouseClicked(e -> {
           handleRegister(isRegistering, usernameField, passwordField, errorLabel);
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

    private void handleRegister(boolean[] isRegistering, TextField usernameField, PasswordField passwordField, Label errorLabel) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError(errorLabel,"Please fill in all fields!");
            return;
        }

        if (username.length() < 3) {
            showError(errorLabel,"Username must be at least 3 characters!");
            return;
        }

        if (password.length() < 4) {
            showError(errorLabel,"Password must be at least 4 characters!");
            return;
        }

        if (dataManager.registerUser(username, password)) {
            showSuccess(errorLabel,"Registration successful!");
            usernameField.clear();
            passwordField.clear();

            // switch back to login mode
            isRegistering[0] = false;

        } else {
            showError(errorLabel,"Username already taken!");
        }
    }

    private void showError(Label errorLabel, String message) {
        errorLabel.getStyleClass().remove("success-label");
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setText(message);
    }

    private void showSuccess(Label errorLabel, String message) {
        errorLabel.getStyleClass().remove("error-label");
        errorLabel.getStyleClass().add("success-label");
        errorLabel.setText(message);
    }

    private void handleLogin(TextField usernameField, PasswordField passwordField, Label errorLabel) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError(errorLabel,"Please fill in all fields!");
            return;
        }

        if (dataManager.validateLogin(username, password)) {
            showSuccess(errorLabel,"Login successful!");
            Player player = loadPlayerFromData(username, dataManager.loadPlayerData(username));

            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(e -> showDashboard(player));
            pause.play();
        } else {
            showError(errorLabel,"Invalid credentials!");
        }
    }


    private Scene makeSplashScene(Stage stage) {

        // Attach Logo
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/backgrounds/login/fishdaIcon.png")));

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
        logo.setText(logo.getText().toUpperCase());
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
        splashScene.getStylesheets().add(getClass().getResource("/stylesheet/styles.css").toExternalForm());

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