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
import model.InventoryItem;
import utils.SpriteLoader;
import javafx.scene.input.*;

public class DashboardScreen {

    private Stage stage;
    private Player player;
    private Scene scene;
    private StackPane sceneRoot; // Changed to StackPane for modals

    // UI Components
    private Label coinsLabel, xpLabel, baitsLabel, warningLabel;
    private VBox sidePanel;
    private ImageView fishermanView;
    private Timeline idleAnimation;

    // State
    private String activePanelType = null;

    public DashboardScreen(Stage stage, Player player) {
        this.stage = stage;
        this.player = player;
        createDashboard();
    }

    private void createDashboard() {
        // Root must be StackPane for layering modals
        sceneRoot = new StackPane();
        
        // Main game layout
        AnchorPane gameLayer = new AnchorPane();
        gameLayer.getStyleClass().add("dashboard-root");

        // Background
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

        // Fisherman sprite
        fishermanView = new ImageView();
        fishermanView.setFitWidth(140);
        fishermanView.setFitHeight(140);
        fishermanView.setPreserveRatio(true);
        fishermanView.setSmooth(false);
        gameLayer.getChildren().add(fishermanView);
        AnchorPane.setTopAnchor(fishermanView, 407.0);
        AnchorPane.setLeftAnchor(fishermanView, 990.0);
        startIdleAnimation();

        // Bag button
        Button bagBtn = new Button("👜 Bag");
        bagBtn.getStyleClass().add("nav-btn");
        bagBtn.setOnAction(e -> togglePanel("inventory"));
        gameLayer.getChildren().add(bagBtn);
        AnchorPane.setTopAnchor(bagBtn, 80.0);
        AnchorPane.setLeftAnchor(bagBtn, 0.0);

        // Go Fishing button
        Button fishingBtn = new Button("🎣 GO FISHING");
        fishingBtn.getStyleClass().add("fishing-btn");
        fishingBtn.setOnAction(e -> handleGoFishing());
        gameLayer.getChildren().add(fishingBtn);
        AnchorPane.setTopAnchor(fishingBtn, 300.0);
        AnchorPane.setRightAnchor(fishingBtn, 10.0);

        // Warning label
        warningLabel = new Label();
        warningLabel.getStyleClass().add("fishing-warning");
        warningLabel.setVisible(false);
        warningLabel.setMaxWidth(300);
        warningLabel.setWrapText(true);
        gameLayer.getChildren().add(warningLabel);
        AnchorPane.setTopAnchor(warningLabel, 380.0);
        AnchorPane.setRightAnchor(warningLabel, 10.0);

        // Side panel
        sidePanel = new VBox(20);
        sidePanel.getStyleClass().add("side-panel");
        sidePanel.setPrefWidth(450);
        sidePanel.setMaxWidth(450);
        sidePanel.setVisible(false);
        sidePanel.setManaged(false);
        gameLayer.getChildren().add(sidePanel);
        AnchorPane.setTopAnchor(sidePanel, 50.0);
        AnchorPane.setLeftAnchor(sidePanel, 0.0);

        // Top bar
        HBox topBar = createTopBar();
        gameLayer.getChildren().add(topBar);
        AnchorPane.setTopAnchor(topBar, 0.0);
        AnchorPane.setLeftAnchor(topBar, 0.0);
        AnchorPane.setRightAnchor(topBar, 0.0);

        // Add game layer to scene root
        sceneRoot.getChildren().add(gameLayer);
        
        // Scene
        scene = new Scene(sceneRoot, 1280, 720);
        try {
            scene.getStylesheets().add(getClass().getResource("/stylesheet/styles.css").toExternalForm());
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

        Label sep1 = new Label("|"); sep1.getStyleClass().add("stat-separator");
        coinsLabel = new Label("💰 " + player.getCoins()); coinsLabel.getStyleClass().add("stat-label");
        Label sep2 = new Label("|"); sep2.getStyleClass().add("stat-separator");
        xpLabel = new Label("⭐ " + player.getXp()); xpLabel.getStyleClass().add("stat-label");
        Label sep3 = new Label("|"); sep3.getStyleClass().add("stat-separator");
        baitsLabel = new Label("🐛 " + player.getTotalBaits()); baitsLabel.getStyleClass().add("stat-label");

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
            warningLabel.setVisible(false);
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
            KeyFrame frame = new KeyFrame(Duration.millis(i * 200), e -> fishermanView.setImage(idleFrames[frameIndex]));
            idleAnimation.getKeyFrames().add(frame);
        }
        idleAnimation.play();
    }

    public void updateStats() {
        coinsLabel.setText("💰 " + player.getCoins());
        xpLabel.setText("⭐ " + player.getXp());
        baitsLabel.setText("🐛 " + player.getTotalBaits());
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

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                final int slotIndex = i * cols + j;
                
                StackPane slotPane = new StackPane();
                slotPane.setPrefSize(80, 80);
                
                Button slotBtn = new Button();
                slotBtn.setPrefSize(80, 80);
                
                InventoryItem item = player.getItemAt(slotIndex);
                
                if (item != null) {
                    slotBtn.getStyleClass().add("inventory-slot");
                    
                    if (item.getType().equals("Fish") && item.getRarity() != null) {
                        slotBtn.getStyleClass().add("rarity-" + item.getRarity().toLowerCase());
                    }
                    
                    Image sprite = getSpriteForItem(item, baitSprites, rodSprites, fishSprites);
                    if (sprite != null) {
                        ImageView iv = new ImageView(sprite);
                        iv.setFitWidth(56);
                        iv.setFitHeight(56);
                        iv.setPreserveRatio(true);
                        iv.setSmooth(false);
                        slotBtn.setGraphic(iv);
                    }
                    
                    if (item.equals(player.getSelectedBait())) {
                        slotBtn.getStyleClass().add("inventory-slot-selected");
                    }
                    
                } else {
                    slotBtn.getStyleClass().add("inventory-slot-empty");
                }
                
                // Add button first
                slotPane.getChildren().add(slotBtn);
                
                // Add quantity label ON TOP of button (if applicable)
                if (item != null && item.getType().equals("Bait") && item.getQuantity() > 1) {
                    Label qtyLabel = new Label("×" + item.getQuantity());
                    qtyLabel.getStyleClass().add("item-quantity-label");
                    qtyLabel.setMouseTransparent(true); // Let clicks pass through to button
                    StackPane.setAlignment(qtyLabel, Pos.BOTTOM_RIGHT);
                    StackPane.setMargin(qtyLabel, new Insets(0, 5, 5, 0));
                    slotPane.getChildren().add(qtyLabel); // Add AFTER button
                }
                
                slotBtn.setOnAction(e -> handleSlotClick(slotIndex, item));
                setupDragAndDrop(slotBtn, slotIndex, item);
                
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
            player.setSelectedSlotIndex(slotIndex);
            
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

    private String getRarityColor(String rarity) {
        switch (rarity.toLowerCase()) {
            case "common": return "#999999";
            case "rare": return "#4A90E2";
            case "legendary": return "#FFD700";
            default: return "#CCCCCC";
        }
    }

    private void handleSellItem(InventoryItem item, int slotIndex) {
        if (item.getType().equals("Fish")) {
            int coins = item.getSellCoins();
            int xp = item.getSellXP();
            player.addCoins(coins);
            player.addXp(xp);
            player.getStats().addMoneyEarned(coins);
            player.getStats().addXpEarned(xp);
            
            System.out.println("Sold " + item.getName() + " for " + coins + " coins + " + xp + " XP");
        } else if (item.getType().equals("Rod")) {
            int coins = item.getRodSellValue();
            player.addCoins(coins);
            System.out.println("Sold " + item.getName() + " for " + coins + " coins");
        }
        
        player.setItemAt(slotIndex, null);
        updateStats();
        
        if (activePanelType != null && activePanelType.equals("inventory")) {
            togglePanel("inventory");
            togglePanel("inventory");
        }
    }

    private void setupDragAndDrop(Button slotBtn, int slotIndex, InventoryItem item) {
        slotBtn.setOnDragDetected(e -> {
            if (item == null) return;
            
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
                
                player.swapItems(sourceIndex, targetIndex);
                
                if (player.getSelectedSlotIndex() == sourceIndex) {
                    player.setSelectedSlotIndex(targetIndex);
                } else if (player.getSelectedSlotIndex() == targetIndex) {
                    player.setSelectedSlotIndex(sourceIndex);
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

    public Scene getScene() {
        return scene;
    }
}