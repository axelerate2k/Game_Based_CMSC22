package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Player;
import utils.SpriteLoader;

public class DashboardScreen {
    
    private Stage stage;
    private Player player;
    private Scene scene;
    
    // UI Components
    private Label coinsLabel;
    private Label xpLabel;
    private Label baitsLabel;
    private Label warningLabel;
    private VBox sidePanel;
    private Button inventoryBtn, shopBtn, dailyBtn, logbookBtn, logoutBtn;
    private ImageView fishermanView;
    private Timeline idleAnimation;
    
    // State
    private String activePanelType = null; // null, "inventory", "shop", "daily", "logbook"
    
    public DashboardScreen(Stage stage, Player player) {
        this.stage = stage;
        this.player = player;
        createDashboard();
    }
  
    
    private void createDashboard() {
        // Root layout
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");
        
        // Background image
        StackPane centerStack = new StackPane();
        ImageView bgView = new ImageView();
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/fishing_background.png"));
            bgView.setImage(bgImage);
            bgView.setFitWidth(1280);
            bgView.setFitHeight(720);
            bgView.setPreserveRatio(false);
        } catch (Exception e) {
            System.err.println("Could not load background: " + e.getMessage());
        }
        
        // Fisherman sprite animation
        fishermanView = new ImageView();
        fishermanView.setFitWidth(140); // Scale up 2x (48 * 2)
        fishermanView.setFitHeight(140);
        fishermanView.setPreserveRatio(true);
        fishermanView.setSmooth(false); // Pixel art should not be smoothed
        StackPane.setAlignment(fishermanView, Pos.TOP_LEFT);
        StackPane.setMargin(fishermanView, new Insets(407, 0, 0, 990)); // Your coordinates
        
        // Start idle animation
        startIdleAnimation();
        
        // Go Fishing button
        VBox fishingArea = new VBox(15);
        fishingArea.setAlignment(Pos.CENTER);
        StackPane.setAlignment(fishingArea, Pos.TOP_CENTER);
        StackPane.setMargin(fishingArea, new Insets(0, 0, 0, 900));
        
        Button fishingBtn = new Button("🎣 GO FISHING");
        fishingBtn.getStyleClass().add("fishing-btn");
        
        warningLabel = new Label();
        warningLabel.getStyleClass().add("fishing-warning");
        warningLabel.setVisible(false);
        warningLabel.setMaxWidth(300);
        warningLabel.setAlignment(Pos.CENTER);
        warningLabel.setWrapText(true);
        
        fishingArea.getChildren().addAll(fishingBtn, warningLabel);
        
        // Fishing button action
        fishingBtn.setOnAction(e -> handleGoFishing());
        
        centerStack.getChildren().addAll(bgView, fishermanView, fishingArea);
        
        // Top bar
        HBox topBar = createTopBar();
        
        // Bottom navigation
        HBox bottomNav = createBottomNav();
        
        // Side panel (initially hidden)
        sidePanel = new VBox(20);
        sidePanel.getStyleClass().add("side-panel");
        sidePanel.setPrefWidth(450);
        sidePanel.setMaxWidth(450);
        sidePanel.setVisible(false);
        sidePanel.setManaged(false);
        
        // Layer everything
        StackPane mainStack = new StackPane();
        mainStack.getChildren().addAll(centerStack, sidePanel);
        StackPane.setAlignment(sidePanel, Pos.CENTER_LEFT);
        
        root.setTop(topBar);
        root.setCenter(mainStack);
        root.setBottom(bottomNav);
        
        // Create scene with CSS
        scene = new Scene(root, 1280, 720);
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS: " + e.getMessage());
        }
        
        stage.setScene(scene);
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.getStyleClass().add("dashboard-top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);
        
        Label usernameLabel = new Label("👤 " + player.getUsername());
        usernameLabel.getStyleClass().add("stat-label");
        
        Label sep1 = new Label("|");
        sep1.getStyleClass().add("stat-separator");
        
        coinsLabel = new Label("💰 " + player.getCoins());
        coinsLabel.getStyleClass().add("stat-label");
        
        Label sep2 = new Label("|");
        sep2.getStyleClass().add("stat-separator");
        
        xpLabel = new Label("⭐ " + player.getXp());
        xpLabel.getStyleClass().add("stat-label");
        
        Label sep3 = new Label("|");
        sep3.getStyleClass().add("stat-separator");
        
        baitsLabel = new Label("🪱 " + player.getTotalBaits());
        baitsLabel.getStyleClass().add("stat-label");
        
        topBar.getChildren().addAll(usernameLabel, sep1, coinsLabel, sep2, xpLabel, sep3, baitsLabel);
        
        return topBar;
    }
    
    private HBox createBottomNav() {
        HBox bottomNav = new HBox(10);
        bottomNav.getStyleClass().add("bottom-nav");
        bottomNav.setAlignment(Pos.CENTER);
        
        inventoryBtn = new Button("📦 Inventory");
        inventoryBtn.getStyleClass().add("nav-btn");
        inventoryBtn.setOnAction(e -> togglePanel("inventory"));
        
        shopBtn = new Button("🛒 Shop");
        shopBtn.getStyleClass().add("nav-btn");
        shopBtn.setOnAction(e -> togglePanel("shop"));
        
        dailyBtn = new Button("🎁 Daily Reward");
        dailyBtn.getStyleClass().add("nav-btn");
        dailyBtn.setOnAction(e -> togglePanel("daily"));
        
        logbookBtn = new Button("📖 Logbook");
        logbookBtn.getStyleClass().add("nav-btn");
        logbookBtn.setOnAction(e -> togglePanel("logbook"));
        
        logoutBtn = new Button("🚪 Logout");
        logoutBtn.getStyleClass().addAll("nav-btn");
        logoutBtn.setOnAction(e -> handleLogout());
        
        bottomNav.getChildren().addAll(inventoryBtn, shopBtn, dailyBtn, logbookBtn, logoutBtn);
        
        return bottomNav;
    }
    
    private void togglePanel(String panelType) {
        // If same panel, close it
        if (activePanelType != null && activePanelType.equals(panelType)) {
            closePanel();
            return;
        }
        
        // Update active panel
        activePanelType = panelType;
        
        // Reset all button styles
        inventoryBtn.getStyleClass().remove("nav-btn-active");
        shopBtn.getStyleClass().remove("nav-btn-active");
        dailyBtn.getStyleClass().remove("nav-btn-active");
        logbookBtn.getStyleClass().remove("nav-btn-active");
        
        // Show panel with appropriate content
        sidePanel.getChildren().clear();
        
        // Panel header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label();
        titleLabel.getStyleClass().add("side-panel-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().add("side-panel-close-btn");
        closeBtn.setOnAction(e -> closePanel());
        header.getChildren().addAll(titleLabel, spacer, closeBtn);
        
        sidePanel.getChildren().add(header);
        
        // Panel content based on type
        switch (panelType) {
            case "inventory":
                titleLabel.setText("📦 Inventory");
                inventoryBtn.getStyleClass().add("nav-btn-active");
                sidePanel.getChildren().add(createInventoryContent());
                break;
            case "shop":
                titleLabel.setText("🛒 Shop");
                shopBtn.getStyleClass().add("nav-btn-active");
                sidePanel.getChildren().add(createShopContent());
                break;
            case "daily":
                titleLabel.setText("🎁 Daily Reward");
                dailyBtn.getStyleClass().add("nav-btn-active");
                sidePanel.getChildren().add(createDailyContent());
                break;
            case "logbook":
                titleLabel.setText("📖 Logbook");
                logbookBtn.getStyleClass().add("nav-btn-active");
                sidePanel.getChildren().add(createLogbookContent());
                break;
        }
        
        sidePanel.setVisible(true);
        sidePanel.setManaged(true);
    }
    
    private void closePanel() {
        activePanelType = null;
        sidePanel.setVisible(false);
        sidePanel.setManaged(false);
        
        // Reset all button styles
        inventoryBtn.getStyleClass().remove("nav-btn-active");
        shopBtn.getStyleClass().remove("nav-btn-active");
        dailyBtn.getStyleClass().remove("nav-btn-active");
        logbookBtn.getStyleClass().remove("nav-btn-active");
    }
    
    private VBox createInventoryContent() {
        VBox content = new VBox(15);
        content.setAlignment(Pos.TOP_CENTER);
        
        Label placeholder = new Label("Inventory content will go here\n(Grid of 15 slots, 5x3)");
        placeholder.setStyle("-fx-text-fill: #3D2817; -fx-font-size: 14px;");
        
        content.getChildren().add(placeholder);
        
        return content;
    }
    
    private VBox createShopContent() {
        VBox content = new VBox(15);
        content.setAlignment(Pos.TOP_CENTER);
        
        Label placeholder = new Label("Shop content will go here\n(Rods and Baits for purchase)");
        placeholder.setStyle("-fx-text-fill: #3D2817; -fx-font-size: 14px;");
        
        content.getChildren().add(placeholder);
        
        return content;
    }
    
    private VBox createDailyContent() {
        VBox content = new VBox(15);
        content.setAlignment(Pos.TOP_CENTER);
        
        Label placeholder = new Label("Daily Reward content will go here\n(Chest with claim button)");
        placeholder.setStyle("-fx-text-fill: #3D2817; -fx-font-size: 14px;");
        
        content.getChildren().add(placeholder);
        
        return content;
    }
    
    private VBox createLogbookContent() {
        VBox content = new VBox(15);
        content.setAlignment(Pos.TOP_CENTER);
        
        Label placeholder = new Label("Logbook content will go here\n(Statistics and best catches)");
        placeholder.setStyle("-fx-text-fill: #3D2817; -fx-font-size: 14px;");
        
        content.getChildren().add(placeholder);
        
        return content;
    }
    
    private void handleGoFishing() {
        if (player.getSelectedBait() == null) {
            warningLabel.setText("⚠️ Please select a bait from inventory!");
            warningLabel.setVisible(true);
            
            // Hide warning after 3 seconds
            Timeline hideWarning = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
                warningLabel.setVisible(false);
            }));
            hideWarning.play();
        } else {
            warningLabel.setVisible(false);
            // TODO: Start fishing mini-game
            System.out.println("Starting fishing with: " + player.getSelectedBait().getName());
        }
    }
    
    private void startIdleAnimation() {
        Image[] idleFrames = SpriteLoader.loadFishermanIdle();
        if (idleFrames == null || idleFrames.length == 0) {
            System.err.println("Failed to load fisherman idle animation");
            return;
        }
        
        idleAnimation = new Timeline();
        idleAnimation.setCycleCount(Animation.INDEFINITE);
        
        for (int i = 0; i < idleFrames.length; i++) {
            final int frameIndex = i;
            KeyFrame frame = new KeyFrame(
                Duration.millis(i * 200), // 200ms per frame = 5 FPS
                e -> fishermanView.setImage(idleFrames[frameIndex])
            );
            idleAnimation.getKeyFrames().add(frame);
        }
        
        idleAnimation.play();
    }
    
    private void handleLogout() {
        // Stop animation
        if (idleAnimation != null) {
            idleAnimation.stop();
        }
        
        // TODO: Save player data
        System.out.println("Logging out...");
        
        // Return to login screen
        // This will be implemented when we update Main.java
    }
    
    public void updateStats() {
        coinsLabel.setText("💰 " + player.getCoins());
        xpLabel.setText("⭐ " + player.getXp());
        baitsLabel.setText("🪱 " + player.getTotalBaits());
    }
    
    public Scene getScene() {
        return scene;
    }
}