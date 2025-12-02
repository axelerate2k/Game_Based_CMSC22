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
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Player;
import model.InventoryItem;
import utils.SpriteLoader;

import java.util.List;

public class DashboardScreen {

    private Stage stage;
    private Player player;
    private Scene scene;
    private StackPane root;

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
        root = new StackPane();

        // --- Main Game Layout --- //
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
        AnchorPane.setLeftAnchor(fishermanView, 950.0);
        startIdleAnimation();

        // --- Shop button --- //
        Button shopBtn = new Button("🏪 Shop");
        shopBtn.getStyleClass().add("nav-btn");
        shopBtn.setOnAction(e -> togglePanel("shop"));
        gameLayer.getChildren().add(shopBtn);
        AnchorPane.setTopAnchor(shopBtn, 130.0); // under Bag button
        AnchorPane.setLeftAnchor(shopBtn, 0.0);


        // --- Bag button (Inventory) ---
        Button bagBtn = new Button("👜 Bag");
        bagBtn.getStyleClass().add("nav-btn");
        bagBtn.setOnAction(e -> togglePanel("inventory"));
        gameLayer.getChildren().add(bagBtn);
        AnchorPane.setTopAnchor(bagBtn, 80.0);
        AnchorPane.setLeftAnchor(bagBtn, 0.0);

        // --- Warning label below Go Fishing ---
        warningLabel = new Label();
        warningLabel.getStyleClass().add("fishing-warning");
        warningLabel.setVisible(false);
        warningLabel.setMaxWidth(300);
        warningLabel.setWrapText(true);
        gameLayer.getChildren().add(warningLabel);
        AnchorPane.setTopAnchor(warningLabel, 500.0);
        AnchorPane.setRightAnchor(warningLabel, 10.0);

        // --- Side panel ---
        sidePanel = new VBox(20);
        sidePanel.getStyleClass().add("side-panel");
        sidePanel.setPrefWidth(450);
        sidePanel.setMaxWidth(450);
        sidePanel.setVisible(false);
        sidePanel.setManaged(false);
        gameLayer.getChildren().add(sidePanel);
        AnchorPane.setTopAnchor(sidePanel, 50.0);
        AnchorPane.setLeftAnchor(sidePanel, 0.0);

        // --- Top stats bar ---
        HBox topBar = createTopBar();
        gameLayer.getChildren().add(topBar);
        AnchorPane.setTopAnchor(topBar, 0.0);
        AnchorPane.setLeftAnchor(topBar, 0.0);
        AnchorPane.setRightAnchor(topBar, 0.0);

        // --- Add game layer to root --- //
        root.getChildren().add(gameLayer);

        // fisherman clickable
        fishermanView.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND));
        fishermanView.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT));
        fishermanView.setOnMouseClicked(event -> {
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

        else if (panelType.equals("shop")) {
            titleLabel.setText("🛒 Shop");
            sidePanel.getChildren().add(createShopGrid());
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
            startFishingAnimation();
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

                // --- FIX: Introduce effectively final variable for use in lambda ---
                final InventoryItem finalItem = item;
                // --- END FIX ---

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

    // ========== SHOP SYSTEM ==========

    private GridPane createShopGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        int rows = 3;
        int cols = 2;

        Image[] baitSprites = SpriteLoader.loadBaitSprites();
        Image[] rodSprites = SpriteLoader.loadRodSprites();
        Image[] fishSprites = SpriteLoader.loadFishSprites();

        // Sample shop items
        InventoryItem[] shopItems = new InventoryItem[] {
                new InventoryItem("Bait", "Basic Worm", 10),
                new InventoryItem("Rod", "Wooden Rod", 1),
                new InventoryItem("Bait", "Basic Worm", 10),
                new InventoryItem("Rod", "Wooden Rod", 1),
                new InventoryItem("Bait", "Basic Worm", 10),
                new InventoryItem("Rod", "Wooden Rod", 1)

        };

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                final int slotIndex = i * cols + j;

                StackPane slotPane = new StackPane();
                slotPane.setPrefSize(80, 80);

                Button slotBtn = new Button();
                slotBtn.setPrefSize(80, 80);

                InventoryItem item = slotIndex < shopItems.length ? shopItems[slotIndex] : null;

                // --- FIX: Introduce effectively final variable for use in lambda ---
                final InventoryItem finalItem = item;
                // --- END FIX ---

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

                } else {
                    slotBtn.getStyleClass().add("inventory-slot-empty");
                }

                slotPane.getChildren().add(slotBtn);

                // Quantity label for stackable items (baits)
                if (finalItem != null && finalItem.getType().equals("Bait") && finalItem.getQuantity() > 1) {
                    Label qtyLabel = new Label("×" + finalItem.getQuantity());
                    qtyLabel.getStyleClass().add("item-quantity-label");
                    qtyLabel.setMouseTransparent(true);
                    StackPane.setAlignment(qtyLabel, Pos.BOTTOM_RIGHT);
                    StackPane.setMargin(qtyLabel, new Insets(0, 5, 5, 0));
                    slotPane.getChildren().add(qtyLabel);
                }

                // Click event for buying
                slotBtn.setOnAction(e -> {
                    if (finalItem != null) {
                        System.out.println("Buying item: " + finalItem.getName());
                        // TODO: Implement purchase logic
                    }
                });

                grid.add(slotPane, j, i);
            }
        }

        return grid;
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

        if (item.getType().equals("Bait") || item.getType().equals("Fish")) {
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

        if (item.getType().equals("Rod") && !item.getName().equals(player.getEquippedRod())) {
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
                root.getChildren().remove(modalOverlay);
                // Refresh inventory to show equipped status
                if (activePanelType != null && activePanelType.equals("inventory")) {
                    togglePanel("inventory");
                    togglePanel("inventory");
                }
            });
            buttonBox.getChildren().add(equipBtn);
        }

        // Only allow selling non-equipped Rods and any Fish
        if (item.getType().equals("Fish") ||
                (item.getType().equals("Rod") && !item.getName().equals(player.getEquippedRod()))) {
            Button sellBtn = new Button("💰 Sell 1x"); // Changed label to sell 1x
            sellBtn.getStyleClass().addAll("modal-btn", "modal-btn-danger");
            sellBtn.setOnAction(e -> {
                handleSellItem(item, slotIndex);
                root.getChildren().remove(modalOverlay);
            });
            buttonBox.getChildren().add(sellBtn);
        }

        Button closeBtn = new Button("Close");
        closeBtn.getStyleClass().add("modal-btn");
        closeBtn.setOnAction(e -> root.getChildren().remove(modalOverlay));
        buttonBox.getChildren().add(closeBtn);

        modalPanel.getChildren().add(buttonBox);
        modalOverlay.getChildren().add(modalPanel);

        root.getChildren().add(modalOverlay);
        modalOverlay.toFront();

        modalOverlay.setOnMouseClicked(e -> {
            if (e.getTarget() == modalOverlay) {
                root.getChildren().remove(modalOverlay);
            }
        });
    }

    private String getRarityColor(String rarity) {
        switch (rarity.toLowerCase()) {
            case "common": return "#999999";
            case "rare": return "#4A90E2";
            case "legendary": return "#FFD700";
            default: return "#CCCCCC";
        }
    }

    /**
     * Handles selling by using the Player.changeItemQuantity or Player.removeFromInventory.
     * This ensures the player's internal List<InventoryItem> is correctly updated,
     * which is essential for saving/loading.
     */
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

    // ==================== ADDED METHODS ====================

    public void refreshActivePanel() {
        if (activePanelType != null) {
            // Re-render the panel
            String currentPanel = activePanelType;
            closePanel();
            togglePanel(currentPanel);
        }
    }

    public void updatePlayerStats() {
        updateStats();
        // updateStats already calls refreshActivePanel
    }

    // =======================================================

    public Scene getScene() {
        return scene;
    }
}
