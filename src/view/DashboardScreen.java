package view;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.InventoryItem;
import model.Player;
import utils.SpriteLoader;

import java.util.List;
import java.util.Random;

public class DashboardScreen {

    private Stage stage;
    private Player player;
    private Scene scene;
    private StackPane sceneRoot; // Changed to StackPane for modals

    // UI Components
    private Label coinsLabel, xpLabel, baitsLabel, warningLabel, fishingLabel;
    private VBox sidePanel;
    private ImageView fishermanView;
    private Label usernameLabel; // Move from local variable to class-level
    private ProgressBar xpBar;

    // State
    private String activePanelType = null; // "inventory", "shop", "daily", "logbook"

    public DashboardScreen(Stage stage, Player player) {
        this.stage = stage;
        this.player = player;
        createDashboard();
    }

    private void createDashboard() {
        // --- Root Layout --- //
        sceneRoot = new StackPane();

        // Main game layout
        AnchorPane gameLayer = new AnchorPane();
        gameLayer.getStyleClass().add("dashboard-root");

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
        gameLayer.getChildren().add(bgView);
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
        gameLayer.getChildren().add(fishermanView);
        AnchorPane.setTopAnchor(fishermanView, 407.0);
        AnchorPane.setLeftAnchor(fishermanView, 920.0);
        startIdleAnimation();

        // Bag button
        Button bagBtn = new Button("👜 Bag");
        bagBtn.getStyleClass().add("nav-btn");
        bagBtn.setOnAction(e -> togglePanel("inventory"));
        gameLayer.getChildren().add(bagBtn);
        AnchorPane.setTopAnchor(bagBtn, 80.0);
        AnchorPane.setLeftAnchor(bagBtn, 0.0);

        // Shop button
        Button shopBtn = new Button("🏪 Shop");
        shopBtn.getStyleClass().add("nav-btn");
        shopBtn.setOnAction(e -> togglePanel("shop"));
        gameLayer.getChildren().add(shopBtn);
        AnchorPane.setTopAnchor(shopBtn, 130.0); // under Bag button
        AnchorPane.setLeftAnchor(shopBtn, 0.0);

        //Daily Rewards button
        Button dailyBtn = new Button("🎁 Daily");
        dailyBtn.getStyleClass().add("nav-btn");
        dailyBtn.setOnAction(e -> togglePanel("daily"));
        gameLayer.getChildren().add(dailyBtn);
        AnchorPane.setTopAnchor(dailyBtn, 180.0); // under Shop button
        AnchorPane.setLeftAnchor(dailyBtn, 0.0);


        // --- Warning label --- //
        warningLabel = new Label();
        warningLabel.getStyleClass().add("fishing-warning");
        warningLabel.setVisible(false);
        warningLabel.setMaxWidth(300);
        warningLabel.setWrapText(true);
        gameLayer.getChildren().add(warningLabel);
        AnchorPane.setTopAnchor(warningLabel, 500.0);
        AnchorPane.setRightAnchor(warningLabel, 10.0);

        // --- Fishing label --- //

        fishingLabel = new Label();
        fishingLabel.getStyleClass().add("fishing-success");
        fishingLabel.setVisible(false);
        fishingLabel.setMaxWidth(300);
        fishingLabel.setWrapText(true);
        gameLayer.getChildren().add(fishingLabel);
        AnchorPane.setTopAnchor(fishingLabel, 500.0);
        AnchorPane.setRightAnchor(fishingLabel, 10.0);

        // --- Side panel ---
        sidePanel = new VBox(20);
        sidePanel.getStyleClass().add("side-panel");
        sidePanel.setPrefWidth(450);
        sidePanel.setMaxWidth(450);
        sidePanel.setMaxHeight(620);
        sidePanel.setVisible(false);
        sidePanel.setManaged(false);
        gameLayer.getChildren().add(sidePanel);
        AnchorPane.setTopAnchor(sidePanel, 50.0);
        AnchorPane.setBottomAnchor(sidePanel, 50.0);
        AnchorPane.setLeftAnchor(sidePanel, 0.0);

        // --- Top stats bar ---
        HBox topBar = createTopBar();
        gameLayer.getChildren().add(topBar);
        AnchorPane.setTopAnchor(topBar, 0.0);
        AnchorPane.setLeftAnchor(topBar, 0.0);
        AnchorPane.setRightAnchor(topBar, 0.0);

        // --- Add game layer to root --- //
        sceneRoot.getChildren().add(gameLayer);

        // fisherman clickable
        fishermanView.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND));
        fishermanView.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT));
        fishermanView.setOnMouseClicked(event -> {
            handleGoFishing();
        });


        // --- Scene ---
        scene = new Scene(sceneRoot, 1280, 720);
        try {
            scene.getStylesheets().add(getClass().getResource("/stylesheet/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS: " + e.getMessage());
        }

        stage.setScene(scene);
    }


    // ---------------- Essential Methods ----------------

    private HBox createTopBar() {
        HBox topBar = new HBox(10);
        topBar.getStyleClass().add("dashboard-top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Username
        usernameLabel = new Label(player.getUsername());
        usernameLabel.getStyleClass().add("stat-label");


        Label sep1 = new Label("|");
        sep1.getStyleClass().add("stat-separator");

        // Level
        xpLabel = new Label("Level " + player.getLevel());
        xpLabel.getStyleClass().add("stat-label");

        // XP Bar
        xpBar = new ProgressBar();
        xpBar.setPrefWidth(150);
        xpBar.setPrefHeight(15);
        xpBar.setStyle("-fx-accent: #FFD700;");
        updateXpBar();

        // HBox for level + XP bar horizontally
        HBox levelBox = new HBox(5);
        levelBox.setAlignment(Pos.CENTER_LEFT);
        levelBox.getChildren().addAll(xpLabel, xpBar);

        Label sep2 = new Label("|");
        sep2.getStyleClass().add("stat-separator");

        // Coins
        coinsLabel = new Label("💰 " + player.getCoins());
        coinsLabel.getStyleClass().add("stat-label");

        Label sep3 = new Label("|");
        sep3.getStyleClass().add("stat-separator");

        // Baits
        baitsLabel = new Label("🐛 " + player.getTotalBaits());
        baitsLabel.getStyleClass().add("stat-label");

        // Build top bar
        topBar.getChildren().addAll(
                usernameLabel, sep1,
                levelBox, sep2,
                coinsLabel, sep3,
                baitsLabel
        );

        refreshUsernameLevel();
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
        } else if (panelType.equals("shop")) {
            titleLabel.setText("🛒 Shop");
            sidePanel.getChildren().add(createShopContent()); // Changed from createShopGrid()
        } else if (panelType.equals("daily")) {
            titleLabel.setText("🎁 Daily Reward");
            sidePanel.getChildren().add(createDailyRewardContent());
        }

        sidePanel.setVisible(true);
        sidePanel.setManaged(true);
        sidePanel.toFront();
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
            fishingLabel.setText("You Fished!");
            fishingLabel.setVisible(true);
            Timeline hideSuccess = new Timeline(new KeyFrame(Duration.seconds(2), e -> fishingLabel.setVisible(false)));
            startFishingAnimation();
            System.out.println("Starting fishing with: " + player.getSelectedBait().getName());
            player.useBait();
            hideSuccess.play();
            refreshActivePanel();
        }
    }

    private void startIdleAnimation() {
        Image[] idleFrames = SpriteLoader.loadFishermanIdle();
        if (idleFrames == null || idleFrames.length == 0) {
            System.err.println("Failed to load fisherman idle animation");
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

    private void startFishingAnimation() {
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
        baitsLabel.setText("🐛 " + player.getTotalBaits());
        updateXpBar();        // for xp bar

        // Ensure the panel is refreshed after a stat change (like selling)
        refreshActivePanel();
    }

    // ========== INVENTORY SYSTEM ==========

    private GridPane createInventoryGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        int rows = 3;
        int cols = 5;

        Image[] baitSprites = SpriteLoader.loadBaitSprites();
        Image[] rodSprites = SpriteLoader.loadRodSprites();
        Image[] fishSprites = SpriteLoader.loadFishSprites();

        List<InventoryItem> currentInventory = player.getInventory();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                final int slotIndex = i * cols + j;

                StackPane slotPane = new StackPane();
                slotPane.setPrefSize(80, 80);

                Button slotBtn = new Button();
                slotBtn.setPrefSize(80, 80);

                InventoryItem item = null;
                if (slotIndex < currentInventory.size()) {
                    item = currentInventory.get(slotIndex);
                }

                final InventoryItem finalItem = item;

                if (finalItem != null) {
                    slotBtn.getStyleClass().add("inventory-slot");

                    if (finalItem.getType().equals("Fish") && finalItem.getRarity() != null) {
                        slotBtn.getStyleClass().add("rarity-" + finalItem.getRarity().toLowerCase());
                    }

                    Image sprite = getSpriteForItem(finalItem, baitSprites, rodSprites, fishSprites);
                    if (sprite != null) {
                        ImageView iv = new ImageView(sprite);
                        iv.setFitWidth(56);
                        iv.setFitHeight(56);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(false);
                        slotBtn.setGraphic(iv);
                    }

                    if (finalItem.equals(player.getSelectedBait())) {
                        slotBtn.getStyleClass().add("inventory-slot-selected");
                    }

                } else {
                    slotBtn.getStyleClass().add("inventory-slot-empty");
                }

                // Add button first
                slotPane.getChildren().add(slotBtn);

                // Add quantity label ON TOP of button (if applicable)
                if (finalItem != null && (finalItem.getType().equals("Bait") || finalItem.getType().equals("Fish")) && finalItem.getQuantity() > 1) {
                    Label qtyLabel = new Label("×" + finalItem.getQuantity());
                    qtyLabel.getStyleClass().add("item-quantity-label");
                    qtyLabel.setMouseTransparent(true); // Let clicks pass through to button
                    StackPane.setAlignment(qtyLabel, Pos.BOTTOM_RIGHT);
                    StackPane.setMargin(qtyLabel, new Insets(0, 5, 5, 0));
                    slotPane.getChildren().add(qtyLabel); // Add AFTER button
                }

                // Use finalItem in event handlers
                slotBtn.setOnAction(e -> handleSlotClick(slotIndex, finalItem));
                setupDragAndDrop(slotBtn, slotIndex, finalItem);

                grid.add(slotPane, j, i);
            }
        }

        return grid;
    }

    private void setupDragAndDrop(Button slotBtn, int slotIndex, InventoryItem item) {
        if (item == null) return; // Prevent drag/drop setup on empty slots

        slotBtn.setOnDragDetected(e -> {

            Dragboard db = slotBtn.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(slotIndex));
            db.setContent(content);

            slotBtn.setOpacity(0.5);
            e.consume();
        });

        slotBtn.setOnDragOver(e -> {
            if (e.getGestureSource() != slotBtn && e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        slotBtn.setOnDragEntered(e -> {
            if (e.getGestureSource() != slotBtn && e.getDragboard().hasString()) {
                slotBtn.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 4;");
            }
            e.consume();
        });

        slotBtn.setOnDragExited(e -> {
            slotBtn.setStyle("");
            e.consume();
        });

        slotBtn.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                int sourceIndex = Integer.parseInt(db.getString());
                int targetIndex = slotIndex;

                // Assuming you have a way to swap elements in the Player's inventory list:
                List<InventoryItem> inventory = player.getInventory();
                if (sourceIndex < inventory.size() && targetIndex < inventory.size()) {
                    // Simple swap
                    InventoryItem item1 = inventory.get(sourceIndex);
                    InventoryItem item2 = inventory.get(targetIndex);
                    inventory.set(targetIndex, item1);
                    inventory.set(sourceIndex, item2);
                }

                if (activePanelType != null && activePanelType.equals("inventory")) {
                    togglePanel("inventory");
                    togglePanel("inventory");
                }

                success = true;
            }

            e.setDropCompleted(success);
            e.consume();
        });

        slotBtn.setOnDragDone(e -> {
            slotBtn.setOpacity(1.0);
            e.consume();
        });
    }

    // ========== SHOP SYSTEM ==========

    private VBox createShopContent() {
        VBox shopContainer = new VBox(15);
        shopContainer.setPadding(new Insets(10));

        // Tab buttons
        HBox tabBar = new HBox(10);
        tabBar.setAlignment(Pos.CENTER);

        Button rodsTabBtn = new Button("⚔️ Rods");
        Button baitsTabBtn = new Button("🪱 Baits");

        rodsTabBtn.getStyleClass().add("shop-tab-btn");
        baitsTabBtn.getStyleClass().add("shop-tab-btn");

        tabBar.getChildren().addAll(rodsTabBtn, baitsTabBtn);

        // Content area with ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(480); // Set a fixed viewport height
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox contentArea = new VBox(10);
        scrollPane.setContent(contentArea);

        // Show rods by default
        contentArea.getChildren().add(createRodsShop());
        rodsTabBtn.getStyleClass().add("shop-tab-active");

        // Tab switching
        rodsTabBtn.setOnAction(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(createRodsShop());
            rodsTabBtn.getStyleClass().add("shop-tab-active");
            baitsTabBtn.getStyleClass().remove("shop-tab-active");
        });

        baitsTabBtn.setOnAction(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(createBaitsShop());
            baitsTabBtn.getStyleClass().add("shop-tab-active");
            rodsTabBtn.getStyleClass().remove("shop-tab-active");
        });

        shopContainer.getChildren().addAll(tabBar, scrollPane);
        return shopContainer;
    }

    // ========== RODS SHOP ==========

    private VBox createRodsShop() {
        VBox rodsContainer = new VBox(10);
        rodsContainer.setPadding(new Insets(10));

        // Rod data: name, cost_coins, cost_xp, stats, description
        String[][] rodsData = {
                {"Bamboo Rod", "FREE", "0", "15% sweet spot | Base rates", "Your trusty starter rod. Cannot be sold."},
                {"Wooden Rod", "200", "100", "20% sweet spot | +10% Rare/Legendary", "A solid upgrade. Better catch rates!"},
                {"Steel Rod", "800", "300", "25% sweet spot | +20% Rare/Legendary", "Professional quality. Even better rates!"},
                {"Master Rod", "2500", "800", "30% sweet spot | +35% Rare/Legendary", "The ultimate fishing tool. Maximum power!"}
        };

        Image[] rodSprites = SpriteLoader.loadRodSprites();

        for (int i = 0; i < rodsData.length; i++) {
            HBox rodCard = createRodCard(rodsData[i], rodSprites[i]);
            rodsContainer.getChildren().add(rodCard);
        }

        return rodsContainer;
    }

    private HBox createRodCard(String[] data, Image sprite) {
        String name = data[0];
        int costCoins = data[1].equals("FREE") ? 0 : Integer.parseInt(data[1]);
        int costXp = Integer.parseInt(data[2]);
        String stats = data[3];
        String description = data[4];

        HBox card = new HBox(15);
        card.getStyleClass().add("shop-card");
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);

        // Rod sprite
        ImageView spriteView = new ImageView(sprite);
        spriteView.setFitWidth(64);
        spriteView.setFitHeight(64);
        spriteView.setPreserveRatio(true);
        spriteView.setSmooth(false);

        StackPane spritePane = new StackPane(spriteView);
        spritePane.setPrefSize(80, 80);
        spritePane.setStyle("-fx-border-color: #7c5636; -fx-border-width: 2; -fx-background-color: #2b1a0e; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Info section
        VBox infoBox = new VBox(5);

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("shop-item-name");

        Label statsLabel = new Label(stats);
        statsLabel.getStyleClass().add("shop-item-stats");

        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("shop-item-desc");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(200);

        infoBox.getChildren().addAll(nameLabel, statsLabel, descLabel);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Buy section
        VBox buyBox = new VBox(8);
        buyBox.setAlignment(Pos.CENTER_RIGHT);

        if (costCoins == 0) {
            Label freeLabel = new Label("STARTER ROD");
            freeLabel.setStyle("-fx-font-family: 'BoldPixels'; -fx-font-size: 14px; -fx-text-fill: #a1c45a; -fx-font-weight: bold;");
            buyBox.getChildren().add(freeLabel);
        } else {
            Label priceLabel = new Label("💰 " + costCoins + " coins");
            priceLabel.setStyle("-fx-font-family: 'BoldPixels'; -fx-font-size: 14px; -fx-text-fill: #f4d35e;");

            Label xpLabel = new Label("⭐ " + costXp + " XP");
            xpLabel.setStyle("-fx-font-family: 'BoldPixels'; -fx-font-size: 14px; -fx-text-fill: #f4d35e;");

            Button buyBtn = new Button("BUY");
            buyBtn.getStyleClass().addAll("modal-btn", "modal-btn-success");
            buyBtn.setMinWidth(100);

            // Check if player already owns this rod
            boolean alreadyOwned = playerOwnsRod(name);

            if (alreadyOwned) {
                buyBtn.setText("OWNED");
                buyBtn.setDisable(true);
                buyBtn.setStyle("-fx-opacity: 0.5;");
            } else {
                buyBtn.setOnAction(e -> handleBuyRod(name, costCoins, costXp, buyBtn));
            }

            buyBox.getChildren().addAll(priceLabel, xpLabel, buyBtn);
        }

        card.getChildren().addAll(spritePane, infoBox, spacer, buyBox);
        return card;
    }

    private boolean playerOwnsRod(String rodName) {
        for (InventoryItem item : player.getInventory()) {
            if (item != null && item.getType().equals("Rod") && item.getName().equals(rodName)) {
                return true;
            }
        }
        return false;
    }

    private void handleBuyRod(String rodName, int costCoins, int costXp, Button buyBtn) {
        // Check if player has enough resources
        if (player.getCoins() < costCoins) {
            showShopError("Not enough coins!");
            return;
        }

        if (player.getXp() < costXp) {
            showShopError("Not enough XP!");
            return;
        }

        // Check if inventory has space
        if (!hasEmptyInventorySlot()) {
            showShopError("Inventory full!");
            return;
        }

        // Deduct resources
        player.removeCoins(costCoins);
        player.removeXp(costXp);

        // Add rod to inventory
        InventoryItem newRod = new InventoryItem("Rod", rodName, 1);
        addToFirstEmptySlot(newRod);

        // Update UI
        updateStats();
        buyBtn.setText("OWNED");
        buyBtn.setDisable(true);
        buyBtn.setStyle("-fx-opacity: 0.5;");

        System.out.println("Purchased: " + rodName);
    }

    // ========== BAITS SHOP ==========

    private VBox createBaitsShop() {
        VBox baitsContainer = new VBox(10);
        baitsContainer.setPadding(new Insets(10));

        // Bait data: name, cost_per_unit, probabilities, description
        String[][] baitsData = {
                {"Basic Worm", "3", "60% Common | 30% Rare | 10% Legendary", "Basic fishing bait. Good for beginners."},
                {"Enhanced Bait", "10", "40% Common | 40% Rare | 20% Legendary", "Improved bait with balanced rates."},
                {"Rare Lure", "25", "20% Common | 50% Rare | 30% Legendary", "Premium lure attracting rare fish!"},
                {"Master Bait", "60", "5% Common | 45% Rare | 50% Legendary", "The ultimate bait. Legendary rates!"}
        };

        Image[] baitSprites = SpriteLoader.loadBaitSprites();

        for (int i = 0; i < baitsData.length; i++) {
            HBox baitCard = createBaitCard(baitsData[i], baitSprites[i]);
            baitsContainer.getChildren().add(baitCard);
        }

        return baitsContainer;
    }

    private HBox createBaitCard(String[] data, Image sprite) {
        String name = data[0];
        int costPerUnit = Integer.parseInt(data[1]);
        String probabilities = data[2];
        String description = data[3];

        HBox card = new HBox(15);
        card.getStyleClass().add("shop-card");
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);

        // Bait sprite
        ImageView spriteView = new ImageView(sprite);
        spriteView.setFitWidth(64);
        spriteView.setFitHeight(64);
        spriteView.setPreserveRatio(true);
        spriteView.setSmooth(false);

        StackPane spritePane = new StackPane(spriteView);
        spritePane.setPrefSize(80, 80);
        spritePane.setStyle("-fx-border-color: #7c5636; -fx-border-width: 2; -fx-background-color: #2b1a0e; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Info section
        VBox infoBox = new VBox(5);

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("shop-item-name");

        Label probLabel = new Label(probabilities);
        probLabel.getStyleClass().add("shop-item-stats");

        Label descLabel = new Label(description);
        descLabel.getStyleClass().add("shop-item-desc");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(200);

        infoBox.getChildren().addAll(nameLabel, probLabel, descLabel);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Buy section
        VBox buyBox = new VBox(8);
        buyBox.setAlignment(Pos.CENTER_RIGHT);

        Label priceLabel = new Label("💰 " + costPerUnit + " coins each");
        priceLabel.setStyle("-fx-font-family: 'BoldPixels'; -fx-font-size: 13px; -fx-text-fill: #f4d35e;");

        // Quantity selector
        HBox quantityBox = new HBox(5);
        quantityBox.setAlignment(Pos.CENTER);

        Button minusBtn = new Button("-");
        minusBtn.getStyleClass().add("quantity-btn");
        minusBtn.setPrefSize(30, 30);

        TextField quantityField = new TextField("1");
        quantityField.setPrefWidth(100);
        quantityField.setAlignment(Pos.CENTER);
        quantityField.setStyle("-fx-font-family: 'BoldPixels'; -fx-font-size: 14px; -fx-padding: 8 5 8 5;");

        Button plusBtn = new Button("+");
        plusBtn.getStyleClass().add("quantity-btn");
        plusBtn.setPrefSize(30, 30);

        quantityBox.getChildren().addAll(minusBtn, quantityField, plusBtn);

        // Quantity controls
        minusBtn.setOnAction(e -> {
            int current = Integer.parseInt(quantityField.getText());
            if (current > 1) quantityField.setText(String.valueOf(current - 1));
        });

        plusBtn.setOnAction(e -> {
            int current = Integer.parseInt(quantityField.getText());
            if (current < 999) quantityField.setText(String.valueOf(current + 1));
        });

        // Only allow numbers
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                quantityField.setText(oldVal);
            }
            if (newVal.isEmpty()) {
                quantityField.setText("1");
            }
        });

        Button buyBtn = new Button("BUY");
        buyBtn.getStyleClass().addAll("modal-btn", "modal-btn-success");
        buyBtn.setMinWidth(100);

        buyBtn.setOnAction(e -> {
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                handleBuyBait(name, costPerUnit, quantity);
            } catch (NumberFormatException ex) {
                showShopError("Invalid quantity!");
            }
        });

        buyBox.getChildren().addAll(priceLabel, quantityBox, buyBtn);

        card.getChildren().addAll(spritePane, infoBox, spacer, buyBox);
        return card;
    }

    private void handleBuyBait(String baitName, int costPerUnit, int quantity) {
        int totalCost = costPerUnit * quantity;

        // Check if player has enough coins
        if (player.getCoins() < totalCost) {
            showShopError("Not enough coins! Need " + totalCost + " coins.");
            return;
        }

        // Deduct coins
        player.removeCoins(totalCost);

        // Add bait to inventory (stack if exists)
        boolean added = false;
        for (InventoryItem item : player.getInventory()) {
            if (item != null && item.getType().equals("Bait") && item.getName().equals(baitName)) {
                item.setQuantity(item.getQuantity() + quantity);
                added = true;
                break;
            }
        }

        // If not found, add to empty slot
        if (!added) {
            if (!hasEmptyInventorySlot()) {
                showShopError("Inventory full!");
                player.addCoins(totalCost); // Refund
                return;
            }
            InventoryItem newBait = new InventoryItem("Bait", baitName, quantity);
            addToFirstEmptySlot(newBait);
        }

        // Update UI
        updateStats();

        System.out.println("Purchased: " + quantity + "x " + baitName + " for " + totalCost + " coins");
    }

    // ========== HELPER METHODS ==========

    private boolean hasEmptyInventorySlot() {
        for (int i = 0; i < 15; i++) {
            if (player.getItemAt(i) == null) {
                return true;
            }
        }
        return false;
    }

    private void addToFirstEmptySlot(InventoryItem item) {
        for (int i = 0; i < 15; i++) {
            if (player.getItemAt(i) == null) {
                player.setItemAt(i, item);
                return;
            }
        }
    }

    private void showShopError(String message) {
        // Show error in warning label temporarily
        warningLabel.setText("⚠️ " + message);
        warningLabel.setVisible(true);
        Timeline hideWarning = new Timeline(new KeyFrame(Duration.seconds(2), e -> warningLabel.setVisible(false)));
        hideWarning.play();
    }


    // ========== DAILY REWARDS SYSTEM ==========

    // Cooldown configuration (change these for testing)
    private static final long COOLDOWN_SECONDS = 10; // 10 seconds for demo
    // private static final long COOLDOWN_SECONDS = 86400; // 24 hours for production

    private VBox createDailyRewardContent() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.CENTER);

        // Check cooldown status
        long currentTime = System.currentTimeMillis() / 1000; // Convert to seconds
        long lastClaim = player.getLastRewardClaim();
        long timeSinceLastClaim = currentTime - lastClaim;
        boolean canClaim = timeSinceLastClaim >= COOLDOWN_SECONDS;

        // Chest image
        Image[] objectSprites = SpriteLoader.loadObjectSprites();
        Image chestSprite = objectSprites[0]; // Chest is index 0

        ImageView chestView = new ImageView(chestSprite);
        chestView.setFitWidth(120);
        chestView.setFitHeight(120);
        chestView.setPreserveRatio(true);
        chestView.setSmooth(false);

        StackPane chestPane = new StackPane(chestView);
        chestPane.setPrefSize(150, 150);
        chestPane.setStyle(
                "-fx-border-color: #f4d35e;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-color: #2b1a0e;" +
                        "-fx-background-radius: 15;"
        );

        // If can claim, add glow effect
        if (canClaim) {
            chestPane.setStyle(
                    chestPane.getStyle() +
                            "-fx-effect: dropshadow(three-pass-box, #f4d35e, 20, 0, 0, 0);"
            );
        }

        // Title
        Label titleLabel = new Label("Daily Treasure Chest");
        titleLabel.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f4d35e;" +
                        "-fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.6), 2, 0, 1, 1);"
        );

        // Status label (timer or ready)
        Label statusLabel = new Label();
        statusLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: white;"
        );

        statusLabel.setMaxWidth(350);
        statusLabel.setAlignment(Pos.CENTER);

        // Claim button
        Button claimButton = new Button("🎁 CLAIM REWARD");
        claimButton.getStyleClass().addAll("modal-btn", "modal-btn-success");
        claimButton.setMinWidth(200);
        claimButton.setPrefHeight(50);
        claimButton.setStyle(claimButton.getStyle() + "-fx-font-size: 16px;");

        if (canClaim) {
            statusLabel.setText("✨ Your daily reward is ready! ✨");
            statusLabel.setStyle(statusLabel.getStyle() + "-fx-text-fill: #a1c45a;");
            claimButton.setDisable(false);

            claimButton.setOnAction(e -> {
                claimDailyReward(container);
            });
        } else {
            long remainingSeconds = COOLDOWN_SECONDS - timeSinceLastClaim;
            statusLabel.setText("⏰ Next reward available in:\n" + formatTime(remainingSeconds));
            claimButton.setDisable(true);
            claimButton.setOpacity(0.5);

            // Start countdown timer
            startCountdownTimer(statusLabel, claimButton, remainingSeconds);
        }

        // Reward info section
        VBox rewardInfoBox = new VBox(10);
        rewardInfoBox.setAlignment(Pos.CENTER);
        rewardInfoBox.setStyle(
                "-fx-background-color: rgba(94, 62, 36, 0.5);" +
                        "-fx-border-color: #7c5636;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;"
        );

        Label infoTitle = new Label("Possible Rewards:");
        infoTitle.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f4d35e;"
        );

        Label commonInfo = new Label("⚪ Common (60%): 30 XP, 10 Basic Worms, or 20 coins");
        Label rareInfo = new Label("🔵 Rare (30%): 50 XP, 5 Enhanced Baits, or 50 coins");
        Label legendaryInfo = new Label("🟡 Legendary (10%): 100 XP, 3 Rare Lures, 100 coins, or Legendary Fish!");

        commonInfo.setStyle("-fx-font-family: 'BoldPixels'; -fx-font-size: 11px; -fx-text-fill: #d4a373;");
        rareInfo.setStyle("-fx-font-family: 'BoldPixels'; -fx-font-size: 11px; -fx-text-fill: #d4a373;");
        legendaryInfo.setStyle("-fx-font-family: 'BoldPixels'; -fx-font-size: 11px; -fx-text-fill: #d4a373;");

        commonInfo.setWrapText(true);
        rareInfo.setWrapText(true);
        legendaryInfo.setWrapText(true);

        commonInfo.setMaxWidth(350);
        rareInfo.setMaxWidth(350);
        legendaryInfo.setMaxWidth(350);

        rewardInfoBox.getChildren().addAll(infoTitle, commonInfo, rareInfo, legendaryInfo);

        container.getChildren().addAll(chestPane, titleLabel, statusLabel, claimButton, rewardInfoBox);

        return container;
    }

    private void startCountdownTimer(Label statusLabel, Button claimButton, long initialRemainingSeconds) {
        // Create a final array to hold the timeline reference
        final Timeline[] countdown = new Timeline[1];

        countdown[0] = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            long current = System.currentTimeMillis() / 1000;
            long lastClaim = player.getLastRewardClaim();
            long timeSince = current - lastClaim;
            long remaining = COOLDOWN_SECONDS - timeSince;

            if (remaining <= 0) {
                statusLabel.setText("✨ Your daily reward is ready! ✨");
                statusLabel.setStyle(
                        "-fx-font-family: 'BoldPixels';" +
                                "-fx-font-size: 14px;" +
                                "-fx-text-fill: #a1c45a;"  +
                                "-fx-text-alignment: center;"
                );
                claimButton.setDisable(false);
                claimButton.setOpacity(1.0);

                // Stop the countdown
                if (countdown[0] != null) {
                    countdown[0].stop();
                }

                // Refresh panel to show glow
                if (activePanelType != null && activePanelType.equals("daily")) {
                    togglePanel("daily");
                    togglePanel("daily");
                }
            } else {
                statusLabel.setText("⏰ Next reward available in: " + formatTime(remaining));
                System.out.println("Remaining seconds = " + remaining);
                System.out.println("Formatted time = " + formatTime(remaining));

            }
        }));
        countdown[0].setCycleCount(Timeline.INDEFINITE); // Changed to INDEFINITE
        countdown[0].play();
    }

    private String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%02d:%02d", minutes, secs);
        }
    }



    private void claimDailyReward(VBox container) {
        // Generate random reward
        DailyReward reward = generateRandomReward();

        // Update player last claim time
        player.setLastRewardClaim(System.currentTimeMillis() / 1000);

        // Apply reward to player
        applyReward(reward);

        // Show reward modal
        showRewardModal(reward);

        // Refresh daily reward screen
        if (activePanelType != null && activePanelType.equals("daily")) {
            togglePanel("daily");
            togglePanel("daily");
        }
    }

    private int getBaitIndex(String name) {
        switch (name) {
            case "Basic Worm":
                return 0;
            case "Enhanced Bait":
                return 1;
            case "Rare Lure":
                return 2;
            case "Master Bait":
                return 3;
            default:
                return -1;
        }
    }

    private int getFishIndex(String name) {
        switch (name) {
            case "Anglerfish":
                return 0;
            case "Red Salmon":
                return 1;
            case "Swordfish":
                return 2;
            case "Oarfish":
                return 3;
            case "Great White Shark":
                return 4;
            default:
                return -1;
        }
    }

    public void updateXpBar() {
        int xp = player.getXp();
        int nextLevelXp = player.getXpForNextLevel();
        if (nextLevelXp <= 0) {		//   // If max level reached, fill the xp bar
            xpBar.setProgress(1.0); // Max level
        } else {
            double progress = (double) xp / nextLevelXp;		// Otherwise, set progress as fraction of current xp / next level xp
            xpBar.setProgress(progress);
        }
    }

    public void updatePlayerStats() {
        updateStats();
        // updateStats already calls refreshActivePanel
    }

    public void refreshActivePanel() {
        if (activePanelType != null) {
            // Re-render the panel
            String currentPanel = activePanelType;
            closePanel();
            togglePanel(currentPanel);
        }
    }

    private Image getSpriteForItem(InventoryItem item, Image[] baits, Image[] rods, Image[] fish) {
        if (item.getType().equals("Bait")) {
            switch (item.getName()) {
                case "Basic Worm": return baits[0];
                case "Enhanced Bait": return baits[1];
                case "Rare Lure": return baits[2];
                case "Master Bait": return baits[3];
            }
        } else if (item.getType().equals("Rod")) {
            switch (item.getName()) {
                case "Bamboo Rod": return rods[0];
                case "Wooden Rod": return rods[1];
                case "Steel Rod": return rods[2];
                case "Master Rod": return rods[3];
            }
        } else if (item.getType().equals("Fish")) {
            switch (item.getName()) {
                case "Anglerfish": return fish[0];
                case "Red Salmon": return fish[1];
                case "Swordfish": return fish[2];
                case "Oarfish": return fish[3];
                case "Great White Shark": return fish[4];
            }
        }
        return null;
    }

    private void handleSlotClick(int slotIndex, InventoryItem item) {
        if (item == null) return;

        if (item.getType().equals("Bait")) {
            player.setSelectedBait(item);

            // Toggle panel twice to force a refresh (redraw the selection border)
            if (activePanelType != null && activePanelType.equals("inventory")) {
                togglePanel("inventory");
                togglePanel("inventory");
            }

            updateStats();
            System.out.println("Selected bait: " + item.getName());
        } else {
            showItemDetailsModal(item, slotIndex);
        }
    }

    private void showItemDetailsModal(InventoryItem item, int slotIndex) {
        StackPane modalOverlay = new StackPane();
        modalOverlay.getStyleClass().add("modal-overlay");

        VBox modalPanel = new VBox(20);
        modalPanel.getStyleClass().add("modal-panel");
        modalPanel.setMaxWidth(400);
        modalPanel.setAlignment(Pos.CENTER);

        Label titleLabel = new Label(item.getName());
        titleLabel.getStyleClass().add("modal-title");

        Image[] baits = SpriteLoader.loadBaitSprites();
        Image[] rods = SpriteLoader.loadRodSprites();
        Image[] fish = SpriteLoader.loadFishSprites();
        Image sprite = getSpriteForItem(item, baits, rods, fish);

        if (sprite != null) {
            ImageView spriteView = new ImageView(sprite);
            spriteView.setFitWidth(80);
            spriteView.setFitHeight(80);
            spriteView.setPreserveRatio(true);
            spriteView.setSmooth(false);

            StackPane spritePane = new StackPane(spriteView);
            spritePane.setPrefSize(100, 100);
            if (item.getType().equals("Fish") && item.getRarity() != null) {
                spritePane.setStyle(
                        "-fx-border-color: " + getRarityColor(item.getRarity()) + ";" +
                                "-fx-border-width: 3;" +
                                "-fx-border-radius: 10;" +
                                "-fx-background-radius: 10;"
                );
            }
            modalPanel.getChildren().add(spritePane);
        }

        String typeText = item.getType();
        if (item.getRarity() != null) {
            typeText += " - " + item.getRarity();
        }
        Label typeLabel = new Label(typeText);
        typeLabel.getStyleClass().add("modal-content-label");
        typeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label descLabel = new Label(item.getDescription());
        descLabel.getStyleClass().add("modal-content-label");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(350);
        descLabel.setAlignment(Pos.CENTER);

        if (item.getType().equals("Bait")) {
            Label qtyLabel = new Label("Quantity: " + item.getQuantity());
            qtyLabel.getStyleClass().add("modal-content-label");
            modalPanel.getChildren().add(qtyLabel);
        }

        if (item.getType().equals("Fish")) {
            Label sellLabel = new Label("💰 Sell: " + item.getSellCoins() + " coins + " + item.getSellXP() + " XP");
            sellLabel.getStyleClass().add("modal-content-label");
            sellLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2E7D32;");
            modalPanel.getChildren().add(sellLabel);
        }

        if (item.getType().equals("Rod") && !item.getName().equals("Bamboo Rod")) {
            Label sellLabel = new Label("💰 Sell: " + item.getRodSellValue() + " coins");
            sellLabel.getStyleClass().add("modal-content-label");
            sellLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #2E7D32;");
            modalPanel.getChildren().add(sellLabel);
        }

        modalPanel.getChildren().addAll(titleLabel, typeLabel, descLabel);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        if (item.getType().equals("Rod")) {
            Button equipBtn = new Button("⚡ Equip");
            equipBtn.getStyleClass().addAll("modal-btn", "modal-btn-success");
            equipBtn.setOnAction(e -> {
                player.setEquippedRod(item.getName());
                System.out.println("Equipped: " + item.getName());
                sceneRoot.getChildren().remove(modalOverlay);
            });
            buttonBox.getChildren().add(equipBtn);
        }

        if (item.getType().equals("Fish") ||
                (item.getType().equals("Rod") && !item.getName().equals("Bamboo Rod"))) {
            Button sellBtn = new Button("💰 Sell");
            sellBtn.getStyleClass().addAll("modal-btn", "modal-btn-danger");
            sellBtn.setOnAction(e -> {
                handleSellItem(item, slotIndex);
                sceneRoot.getChildren().remove(modalOverlay);
            });
            buttonBox.getChildren().add(sellBtn);
        }

        Button closeBtn = new Button("Close");
        closeBtn.getStyleClass().add("modal-btn");
        closeBtn.setOnAction(e -> sceneRoot.getChildren().remove(modalOverlay));
        buttonBox.getChildren().add(closeBtn);

        modalPanel.getChildren().add(buttonBox);
        modalOverlay.getChildren().add(modalPanel);

        sceneRoot.getChildren().add(modalOverlay);
        modalOverlay.toFront();

        modalOverlay.setOnMouseClicked(e -> {
            if (e.getTarget() == modalOverlay) {
                sceneRoot.getChildren().remove(modalOverlay);
            }
        });
    }

    public void refreshUsernameLevel() {
        usernameLabel.setText("👤 " + player.getUsername() + "  |  Level " + player.getLevel());
        xpLabel.setText("⭐ " + player.getXp());		//Update the xp label with the current xp value
        xpBar.setProgress(getXpProgress());		// Update the xp progress bar to reflect the player's progress toward the next level
    }

    private double getXpProgress() {
        int currentXp = player.getXp();
        int xpForNextLevel = player.getXpForNextLevel();		// Get the total xp required to reach the next level
        if (xpForNextLevel <= 0) return 1.0; // Max level reached
        return (double) currentXp / xpForNextLevel;		// Otherwise, return progress as a fraction of xp toward next level
    }


    private void handleSellItem(InventoryItem item, int slotIndex) {
        if (item.getType().equals("Fish")) {
            // Fish are stackable. The Model's changeItemQuantity handles reducing the count
            // and removing the item completely if the quantity reaches zero.
            if (player.changeItemQuantity(item.getName(), -1)) {
                int coins = item.getSellCoins();
                int xp = item.getSellXP();

                player.addCoins(coins);
                player.addXp(xp);
                player.getStats().addMoneyEarned(coins);
                player.getStats().addXpEarned(xp);

                refreshUsernameLevel();             // <--- update level label
                updateStats();                      // <--- update other labels (coins, XP, baits)

                System.out.println("Sold 1x " + item.getName() + " for " + coins + " coins + " + xp + " XP");
            } else {
                System.err.println("Sell failed: Could not reduce fish quantity.");
                return;
            }
        } else if (item.getType().equals("Rod")) {
            // Rods are non-stackable, remove the entire object

            // Safety check: Cannot sell equipped rod (UI button should prevent this, but check here too)
            if (item.getName().equals(player.getEquippedRod())) {
                System.err.println("Sell failed: Cannot sell equipped rod.");
                return;
            }

            int coins = item.getRodSellValue();
            player.removeFromInventory(item);
            player.addCoins(coins);

            System.out.println("Sold " + item.getName() + " for " + coins + " coins");
        }

        updateStats(); // Recalculates stats and refreshes panel

        // This is now redundant since updateStats calls refreshActivePanel, but keeping for safety
        if (activePanelType != null && activePanelType.equals("inventory")) {
            togglePanel("inventory");
            togglePanel("inventory");
        }
    }


    private String getRarityColor(String rarity) {
        switch (rarity.toLowerCase()) {
            case "common": return "#999999";
            case "rare": return "#4A90E2";
            case "legendary": return "#FFD700";
            default: return "#CCCCCC";
        }
    }



    // ========== DAILY REWARDS SYSTEM ==========


    private DailyReward generateRandomReward() {
        Random random = new Random();
        int roll = random.nextInt(100); // 0-99

        String rarity;
        String rewardType;
        int amount = 0;
        String itemName = null;

        // Determine rarity
        if (roll < 60) { // 60% Common
            rarity = "Common";
            int commonRoll = random.nextInt(3);
            switch (commonRoll) {
                case 0:
                    rewardType = "XP";
                    amount = 30;
                    break;
                case 1:
                    rewardType = "Bait";
                    itemName = "Basic Worm";
                    amount = 10;
                    break;
                default:
                    rewardType = "Coins";
                    amount = 20;
                    break;
            }
        } else if (roll < 90) { // 30% Rare
            rarity = "Rare";
            int rareRoll = random.nextInt(3);
            switch (rareRoll) {
                case 0:
                    rewardType = "XP";
                    amount = 50;
                    break;
                case 1:
                    rewardType = "Bait";
                    itemName = "Enhanced Bait";
                    amount = 5;
                    break;
                default:
                    rewardType = "Coins";
                    amount = 50;
                    break;
            }
        } else { // 10% Legendary
            rarity = "Legendary";
            int legendaryRoll = random.nextInt(4);
            switch (legendaryRoll) {
                case 0:
                    rewardType = "XP";
                    amount = 100;
                    break;
                case 1:
                    rewardType = "Bait";
                    itemName = "Rare Lure";
                    amount = 3;
                    break;
                case 2:
                    rewardType = "Coins";
                    amount = 100;
                    break;
                default:
                    rewardType = "Fish";
                    // Random legendary fish
                    String[] fishNames = {"Anglerfish", "Red Salmon", "Swordfish", "Oarfish", "Great White Shark"};
                    itemName = fishNames[random.nextInt(fishNames.length)];
                    amount = 1;
                    break;
            }
        }

        return new DailyReward(rarity, rewardType, itemName, amount);
    }

    private void applyReward(DailyReward reward) {
        switch (reward.type) {
            case "Coins":
                player.addCoins(reward.amount);
                break;

            case "XP":
                player.addXp(reward.amount);
                break;

            case "Bait":
                // Try to stack with existing bait
                boolean stacked = false;
                for (InventoryItem item : player.getInventory()) {
                    if (item != null && item.getType().equals("Bait") && item.getName().equals(reward.itemName)) {
                        item.setQuantity(item.getQuantity() + reward.amount);
                        stacked = true;
                        break;
                    }
                }

                // If not stacked, add to empty slot
                if (!stacked) {
                    if (hasEmptyInventorySlot()) {
                        InventoryItem newBait = new InventoryItem("Bait", reward.itemName, reward.amount);
                        addToFirstEmptySlot(newBait);
                    } else {
                        // Inventory full - convert to coins
                        player.addCoins(reward.amount * 5); // Compensate with coins
                    }
                }
                break;

            case "Fish":
                if (hasEmptyInventorySlot()) {
                    InventoryItem newFish = new InventoryItem("Fish", reward.itemName, 1, "Legendary");
                    addToFirstEmptySlot(newFish);
                } else {
                    // Inventory full - convert to coins
                    player.addCoins(100); // Compensate with 100 coins
                }
                break;
        }

        updateStats();
    }

    private void showRewardModal(DailyReward reward) {
        StackPane modalOverlay = new StackPane();
        modalOverlay.getStyleClass().add("modal-overlay");

        VBox modalPanel = new VBox(25);
        modalPanel.getStyleClass().add("modal-panel");
        modalPanel.setMaxWidth(450);
        modalPanel.setAlignment(Pos.CENTER);
        modalPanel.setPadding(new Insets(40));

        // Rarity banner
        Label rarityLabel = new Label("✨ " + reward.rarity.toUpperCase() + " REWARD! ✨");
        rarityLabel.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + getRarityTextColor(reward.rarity) + ";" +
                        "-fx-effect: dropshadow(three-pass-box, " + getRarityGlowColor(reward.rarity) + ", 15, 0, 0, 0);"
        );

        // Reward display
        VBox rewardDisplay = new VBox(15);
        rewardDisplay.setAlignment(Pos.CENTER);
        rewardDisplay.setStyle(
                "-fx-background-color: #2b1a0e;" +
                        "-fx-border-color: " + getRarityBorderColor(reward.rarity) + ";" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 25;" +
                        "-fx-effect: dropshadow(three-pass-box, " + getRarityGlowColor(reward.rarity) + ", 20, 0, 0, 0);"
        );

        // Reward icon/sprite
        if (reward.type.equals("Bait")) {
            Image[] baitSprites = SpriteLoader.loadBaitSprites();
            int index = getBaitIndex(reward.itemName);
            if (index >= 0) {
                ImageView sprite = new ImageView(baitSprites[index]);
                sprite.setFitWidth(100);
                sprite.setFitHeight(100);
                sprite.setPreserveRatio(true);
                sprite.setSmooth(false);
                rewardDisplay.getChildren().add(sprite);
            }
        } else if (reward.type.equals("Fish")) {
            Image[] fishSprites = SpriteLoader.loadFishSprites();
            int index = getFishIndex(reward.itemName);
            if (index >= 0) {
                ImageView sprite = new ImageView(fishSprites[index]);
                sprite.setFitWidth(100);
                sprite.setFitHeight(100);
                sprite.setPreserveRatio(true);
                sprite.setSmooth(false);
                rewardDisplay.getChildren().add(sprite);
            }
        } else {
            // Show emoji for coins/XP
            Label emojiLabel = new Label(reward.type.equals("Coins") ? "💰" : "⭐");
            emojiLabel.setStyle("-fx-font-size: 80px;");
            rewardDisplay.getChildren().add(emojiLabel);
        }

        // Reward text
        Label rewardText = new Label(getRewardText(reward));
        rewardText.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f4d35e;" +
                        "-fx-text-alignment: center;"
        );
        rewardText.setWrapText(true);
        rewardText.setMaxWidth(300);

        rewardDisplay.getChildren().add(rewardText);

        // Claim button
        Button okButton = new Button("AWESOME!");
        okButton.getStyleClass().addAll("modal-btn", "modal-btn-success");
        okButton.setMinWidth(150);
        okButton.setPrefHeight(45);
        okButton.setStyle(okButton.getStyle() + "-fx-font-size: 16px;");
        okButton.setOnAction(e -> sceneRoot.getChildren().remove(modalOverlay));

        modalPanel.getChildren().addAll(rarityLabel, rewardDisplay, okButton);
        modalOverlay.getChildren().add(modalPanel);

        sceneRoot.getChildren().add(modalOverlay);
        modalOverlay.toFront();

        // Click overlay to close
        modalOverlay.setOnMouseClicked(e -> {
            if (e.getTarget() == modalOverlay) {
                sceneRoot.getChildren().remove(modalOverlay);
            }
        });
    }
    private String getRarityTextColor(String rarity) {
        switch (rarity) {
            case "Common": return "#CCCCCC";
            case "Rare": return "#4A90E2";
            case "Legendary": return "#FFD700";
            default: return "#FFFFFF";
        }
    }
    private String getRarityBorderColor(String rarity) {
        switch (rarity) {
            case "Common": return "#999999";
            case "Rare": return "#4A90E2";
            case "Legendary": return "#FFD700";
            default: return "#CCCCCC";
        }
    }

    private String getRarityGlowColor(String rarity) {
        switch (rarity) {
            case "Common": return "rgba(153, 153, 153, 0.6)";
            case "Rare": return "rgba(74, 144, 226, 0.8)";
            case "Legendary": return "rgba(255, 215, 0, 0.9)";
            default: return "rgba(200, 200, 200, 0.5)";
        }
    }
    public Scene getScene() {
        return scene;
    }


    private String getRewardText(DailyReward reward) {
        switch (reward.type) {
            case "Coins":
                return "+" + reward.amount + " Coins!";
            case "XP":
                return "+" + reward.amount + " XP!";
            case "Bait":
                return reward.amount + "x " + reward.itemName + "!";
            case "Fish":
                return "Legendary " + reward.itemName + "!";
            default:
                return "Mystery Reward!";
        }
    }

    // Helper class for daily rewards
    private static class DailyReward {
        String rarity;    // "Common", "Rare", "Legendary"
        String type;      // "Coins", "XP", "Bait", "Fish"
        String itemName;  // For baits/fish
        int amount;       // Quantity

        DailyReward(String rarity, String type, String itemName, int amount) {
            this.rarity = rarity;
            this.type = type;
            this.itemName = itemName;
            this.amount = amount;
        }
    }
}
