package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AboutScreen {

    private Stage stage;
    private Scene scene;
    private Scene previousScene;

    public AboutScreen(Stage stage, Scene previousScene) {
        this.stage = stage;
        this.previousScene = previousScene;
        createAboutScreen();
    }

    private void createAboutScreen() {
        // Root container
        BorderPane root = new BorderPane();
        root.getStyleClass().add("dashboard-root");

        // Scrollable content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Main content container
        VBox contentBox = new VBox(25);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(40, 80, 40, 80));
        contentBox.setMaxWidth(900);
        contentBox.getStyleClass().add("about-content");

        // Title
        Label titleLabel = new Label("FISHDA");
        titleLabel.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 48px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f4d35e;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 6, 0, 2, 2);"
        );

        // Subtitle
        Label subtitleLabel = new Label("A Pixel Art Fishing Adventure");
        subtitleLabel.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 18px;" +
                        "-fx-text-fill: #d4a373;"
        );

        // Divider
        Region divider1 = createDivider();

        // About Section
        VBox aboutSection = createSection(
                "📖 About the Game",
                "Fishda is a relaxing pixel art fishing game where you catch various fish, " +
                        "upgrade your fishing equipment, and build your collection. Experience the joy " +
                        "of fishing in a charming retro-style world with simple mechanics and rewarding gameplay."
        );

        // How to Play Section
        VBox howToPlaySection = createSection(
                "🎮 How to Play",
                "• Click on the fisherman to start fishing\n" +
                        "• Select bait from your inventory (Bag button)\n" +
                        "• Purchase rods and baits from the Shop\n" +
                        "• Claim daily rewards for free items\n" +
                        "• Sell your caught fish for coins and XP\n" +
                        "• Level up to unlock better fishing opportunities\n" +
                        "• Drag and drop items to organize your inventory"
        );

        // Features Section
        VBox featuresSection = createSection(
                "✨ Features",
                "• Multiple fish rarities: Common, Rare, and Legendary\n" +
                        "• 4 unique fishing rods with different catch rates\n" +
                        "• 4 types of bait with varying probabilities\n" +
                        "• Daily reward system with random prizes\n" +
                        "• Level progression system (up to Level 30)\n" +
                        "• Inventory management with drag-and-drop\n" +
                        "• Beautiful pixel art graphics and animations"
        );

        // Divider
        Region divider2 = createDivider();

        // Development Team Title
        Label teamTitle = new Label("👥 Development Team");
        teamTitle.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f4d35e;"
        );

        // Team Box
        VBox teamBox = new VBox(10);
        teamBox.setAlignment(Pos.CENTER);
        teamBox.setStyle(
                "-fx-background-color: rgba(94, 62, 36, 0.5);" +
                        "-fx-border-color: #7c5636;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 20;"
        );

        String[] members = {
                "Brylle B. Pamulaklakin",
                "Frendzo Charles C. Pelagio",
                "Lance Axel B. Gasmen",
                "Szhan Wayne S. Timosan"
        };

        for (String member : members) {
            Label memberLabel = new Label("• " + member);
            memberLabel.setStyle(
                    "-fx-font-family: 'BoldPixels';" +
                            "-fx-font-size: 14px;" +
                            "-fx-text-fill: #f5e1b3;"
            );
            teamBox.getChildren().add(memberLabel);
        }

        // Divider
        Region divider3 = createDivider();

        // Credits Title
        Label creditsTitle = new Label("🎨 Asset Credits");
        creditsTitle.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f4d35e;"
        );

        // Credits Box
        VBox creditsBox = new VBox(15);
        creditsBox.setAlignment(Pos.CENTER);
        creditsBox.setStyle(
                "-fx-background-color: rgba(94, 62, 36, 0.5);" +
                        "-fx-border-color: #7c5636;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 20;"
        );

        Label credit1Title = new Label("Fishing Icons (Fish, Rods, Baits):");
        credit1Title.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f4d35e;"
        );

        Label credit1Link = new Label("https://happypotato100.itch.io/fishing-icon-pack");
        credit1Link.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 11px;" +
                        "-fx-text-fill: #4A90E2;" +
                        "-fx-underline: true;"
        );
        credit1Link.setWrapText(true);

        Label credit2Title = new Label("Fisherman Animation & Background Tilesets:");
        credit2Title.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f4d35e;"
        );

        Label credit2Link = new Label("https://free-game-assets.itch.io/free-fishing-pixel-art-pack");
        credit2Link.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 11px;" +
                        "-fx-text-fill: #4A90E2;" +
                        "-fx-underline: true;"
        );
        credit2Link.setWrapText(true);

        creditsBox.getChildren().addAll(
                credit1Title, credit1Link,
                new Label(" "),
                credit2Title, credit2Link
        );

        // Back Button
        Button backButton = new Button("← Back to Game");
        backButton.getStyleClass().addAll("modal-btn", "modal-btn-success");
        backButton.setMinWidth(200);
        backButton.setPrefHeight(50);
        backButton.setStyle(backButton.getStyle() + "-fx-font-size: 16px;");
        backButton.setOnAction(e -> stage.setScene(previousScene));

        // Copyright
        Label copyrightLabel = new Label("© 2025 Fishda Development Team");
        copyrightLabel.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 12px;" +
                        "-fx-text-fill: #999999;" +
                        "-fx-padding: 20 0 0 0;"
        );

        // Add all sections
        contentBox.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                divider1,
                aboutSection,
                howToPlaySection,
                featuresSection,
                divider2,
                teamTitle,
                teamBox,
                divider3,
                creditsTitle,
                creditsBox,
                backButton,
                copyrightLabel
        );


        HBox centerWrapper = new HBox(contentBox);
        centerWrapper.setAlignment(Pos.TOP_CENTER);

        scrollPane.setContent(centerWrapper);

        // CENTER THE WHOLE PAGE ON SCREEN
        root.setCenter(scrollPane);

        // Create scene
        scene = new Scene(root, 1280, 720);
        try {
            scene.getStylesheets().add(getClass().getResource("/stylesheet/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS: " + e.getMessage());
        }
    }

    private VBox createSection(String title, String content) {
        VBox section = new VBox(10);
        section.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #f4d35e;"
        );

        Label contentLabel = new Label(content);
        contentLabel.setStyle(
                "-fx-font-family: 'BoldPixels';" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #f5e1b3;" +
                        "-fx-line-spacing: 5px;"
        );
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(800);

        section.getChildren().addAll(titleLabel, contentLabel);
        return section;
    }

    private Region createDivider() {
        Region divider = new Region();
        divider.setPrefHeight(2);
        divider.setMaxWidth(600);
        divider.setStyle(
                "-fx-background-color: #7c5636;" +
                        "-fx-opacity: 0.5;"
        );
        return divider;
    }

    public Scene getScene() {
        return scene;
    }
}
