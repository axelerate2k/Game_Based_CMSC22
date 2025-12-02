package view;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Player;
import model.InventoryItem;
import utils.SpriteLoader;

public class DashboardScreen {

    private Stage stage;
    private Player player;
    private Scene scene;

    // UI Components
    private Label coinsLabel, xpLabel, baitsLabel, warningLabel;
    private VBox sidePanel;
    private ImageView fishermanView;


    // State
    private String activePanelType = null; // "inventory", "shop", "daily", "logbook"

    public DashboardScreen(Stage stage, Player player) {
        this.stage = stage;
        this.player = player;
        createDashboard();
    }

    private void createDashboard() {
        // --- Root layout ---
        AnchorPane root = new AnchorPane();
        root.getStyleClass().add("dashboard-root");

        // --- Background ---
        ImageView bgView = new ImageView();
        try {
            Image bgImage = new Image(getClass().getResourceAsStream("/backgrounds/login/fishing_background.png"));
            bgView.setImage(bgImage);
            bgView.setFitWidth(1280);
            bgView.setFitHeight(720);
            bgView.setPreserveRatio(false);
        } catch (Exception e) {
            System.err.println("Could not load background: " + e.getMessage());
        }
        root.getChildren().add(bgView);
        AnchorPane.setTopAnchor(bgView, 0.0);
        AnchorPane.setLeftAnchor(bgView, 0.0);
        AnchorPane.setRightAnchor(bgView, 0.0);
        AnchorPane.setBottomAnchor(bgView, 0.0);

        // --- Fisherman sprite ---
        fishermanView = new ImageView();
        fishermanView.setFitWidth(140);
        fishermanView.setFitHeight(140);
        fishermanView.setPreserveRatio(true);
        fishermanView.setSmooth(false);
        fishermanView.getStyleClass().add("fisherman-sprite");
        root.getChildren().add(fishermanView);
        AnchorPane.setTopAnchor(fishermanView, 407.0);
        AnchorPane.setLeftAnchor(fishermanView, 950.0);
        startIdleAnimation();

        // --- Bag button (Inventory) ---
        Button bagBtn = new Button("👜 Bag");
        bagBtn.getStyleClass().add("bag-btn");
        bagBtn.setOnAction(e -> togglePanel("inventory"));
        root.getChildren().add(bagBtn);
        AnchorPane.setTopAnchor(bagBtn, 80.0);
        AnchorPane.setLeftAnchor(bagBtn, 0.0);

        // --- Warning label below Go Fishing ---
        warningLabel = new Label();
        warningLabel.getStyleClass().add("fishing-warning");
        warningLabel.setVisible(false);
        warningLabel.setMaxWidth(300);
        warningLabel.setWrapText(true);
        root.getChildren().add(warningLabel);
        AnchorPane.setTopAnchor(warningLabel, 500.0);
        AnchorPane.setRightAnchor(warningLabel, 10.0);

        // --- Side panel ---
        sidePanel = new VBox(20);
        sidePanel.getStyleClass().add("side-panel");
        sidePanel.setPrefWidth(450);
        sidePanel.setMaxWidth(450);
        sidePanel.setVisible(false);
        sidePanel.setManaged(false);
        root.getChildren().add(sidePanel);
        AnchorPane.setTopAnchor(sidePanel, 50.0);
        AnchorPane.setLeftAnchor(sidePanel, 0.0);

        // --- Top stats bar ---
        HBox topBar = createTopBar();
        root.getChildren().add(topBar);
        AnchorPane.setTopAnchor(topBar, 0.0);
        AnchorPane.setLeftAnchor(topBar, 0.0);
        AnchorPane.setRightAnchor(topBar, 0.0);

        // fisherman clickable
        fishermanView.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND));
        fishermanView.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT));
        fishermanView.setOnMouseClicked(event -> {
            startFishingAnimation();
            handleGoFishing();
        });


        // --- Scene ---
        scene = new Scene(root, 1280, 720);
        try {
            scene.getStylesheets().add(getClass().getResource("/stylesheet/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS: " + e.getMessage());
        }

        stage.setScene(scene);
    }


    // ---------------- Essential Methods ----------------

    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.getStyleClass().add("dashboard-top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label usernameLabel = new Label("👤 " + player.getUsername());
        usernameLabel.getStyleClass().add("stat-label");

        Label sep1 = new Label("|"); sep1.getStyleClass().add("stat-separator");
        coinsLabel = new Label("💰 " + player.getCoins()); coinsLabel.getStyleClass().add("stat-label");
        Label sep2 = new Label("|"); sep2.getStyleClass().add("stat-separator");
        xpLabel = new Label("⭐ " + player.getXp()); xpLabel.getStyleClass().add("stat-label");
        Label sep3 = new Label("|"); sep3.getStyleClass().add("stat-separator");
        baitsLabel = new Label("🪱 " + player.getTotalBaits()); baitsLabel.getStyleClass().add("stat-label");

        topBar.getChildren().addAll(usernameLabel, sep1, coinsLabel, sep2, xpLabel, sep3, baitsLabel);
        return topBar;
    }

    private void togglePanel(String panelType) {
        if (activePanelType != null && activePanelType.equals(panelType)) {
            closePanel();
            return;
        }

        activePanelType = panelType;
        sidePanel.getChildren().clear();

        Label titleLabel = new Label();
        titleLabel.getStyleClass().add("side-panel-title");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().add("side-panel-close-btn");
        closeBtn.setOnAction(e -> closePanel());
        header.getChildren().addAll(titleLabel, spacer, closeBtn);

        sidePanel.getChildren().add(header);

        if (panelType.equals("inventory")) {
            titleLabel.setText("📦 Inventory");
            sidePanel.getChildren().add(createInventoryGrid());
        }

        sidePanel.setVisible(true);
        sidePanel.setManaged(true);
        sidePanel.toFront(); // ensure panel is on top
    }

    private void closePanel() {
        activePanelType = null;
        sidePanel.setVisible(false);
        sidePanel.setManaged(false);
    }

    private void handleGoFishing() {
        if (player.getSelectedBait() == null) {
            warningLabel.setText("⚠️ Please select a bait from inventory!");
            warningLabel.setVisible(true);
            Timeline hideWarning = new Timeline(new KeyFrame(Duration.seconds(3), e -> warningLabel.setVisible(false)));
            hideWarning.play();
        } else {
            warningLabel.setVisible(false);
            System.out.println("Starting fishing with: " + player.getSelectedBait().getName());
        }
    }

    private void startIdleAnimation() {
        Image[] idleFrames = SpriteLoader.loadFishermanIdle();
        if (idleFrames == null || idleFrames.length == 0) {
            return;
        }
        // Frame delay in nanoseconds (200 ms per frame)
        long frameDelayNs = 200_000_000;

        // Track the current frame and last update time

        AnimationTimer idleAnimationTimer = new AnimationTimer() {
            long lastUpdate = 0;
            int currentFrame = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= frameDelayNs) {
                    fishermanView.setImage(idleFrames[currentFrame++]);

                    // Loop animation indefinitely
                    if (currentFrame >= idleFrames.length) {
                        currentFrame = 0;
                    }

                    lastUpdate = now;
                }
            }
        };

        idleAnimationTimer.start();
    }

    private void startFishingAnimation(){
        Image[] fishingFrames = SpriteLoader.loadFishermanFishing();
        if (fishingFrames == null || fishingFrames.length == 0) {
            return;
        }

        // Frame delay in milliseconds
        long frameDelayNs = 120_000_000; // 120 ms in nanoseconds

        AnimationTimer timer = new AnimationTimer() {
            long lastUpdate = 0;
            int currentFrame = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= frameDelayNs) {
                    fishermanView.setImage(fishingFrames[currentFrame++]);

                    // Stop when we reach the last frame
                    if (currentFrame >= fishingFrames.length) {
                        stop();
                    }

                    lastUpdate = now;
                }
            }
        };

        timer.start();
    }

    public void updateStats() {
        coinsLabel.setText("💰 " + player.getCoins());
        xpLabel.setText("⭐ " + player.getXp());
        baitsLabel.setText("🪱 " + player.getTotalBaits());
    }

    // ---------------- Inventory Grid ----------------

    private GridPane createInventoryGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        int rows = 3;
        int cols = 5;

        Image[] baitSprites = SpriteLoader.loadBaitSprites();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = i * cols + j;
                Button slotBtn = new Button();
                slotBtn.setPrefSize(64, 64);

                // NEW: Apply the "Epic" CSS class to every slot (filled or empty)
                slotBtn.getStyleClass().add("inventory-slot");

                if (index < player.getInventory().size()) {
                    InventoryItem item = player.getInventory().get(index);
                    Image img = null;
                    if (item.getType().equals("Bait")) {
                        switch (item.getName()) {
                            case "Basic Worm": img = baitSprites[0]; break;
                            case "Enhanced": img = baitSprites[1]; break;
                            case "Rare Lure": img = baitSprites[2]; break;
                            case "Master Bait": img = baitSprites[3]; break;
                        }
                    }
                    if (img != null) {
                        ImageView iv = new ImageView(img);
                        iv.setFitWidth(48);
                        iv.setFitHeight(48);
                        slotBtn.setGraphic(iv);
                    }

                    slotBtn.setOnAction(e -> {
                        if (item.getType().equals("Bait")) {
                            player.setSelectedBait(item);
                            updateStats();
                            System.out.println("Selected bait: " + item.getName());
                        }
                    });
                } 
                // OLD: Removed the 'else' block that had inline styles. 
                // The CSS class now handles the look of empty slots automatically.

                grid.add(slotBtn, j, i);
            }
        }

        return grid;
    }

    public Scene getScene() {
        return scene;
    }
}
