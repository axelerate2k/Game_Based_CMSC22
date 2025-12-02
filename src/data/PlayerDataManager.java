package data;

import model.InventoryItem;
import model.Player;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PlayerDataManager {

    private static final String DATA_FOLDER = "player_data";

    public PlayerDataManager() {
        // Ensure the player_data directory exists
        try {
            Files.createDirectories(Paths.get(DATA_FOLDER));
        } catch (IOException e) {
            System.err.println("Could not create data directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 1. SAVE: Writes all Player data, Stats, and Inventory state
    public void saveGame(Player player) {
        String filename = DATA_FOLDER + "/player_" + player.getUsername() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Save Core Stats
            writer.write("coins:" + player.getCoins());
            writer.newLine();
            writer.write("xp:" + player.getXp());
            writer.newLine();

            // Save PlayerStats
            writer.write("stats_fishCaught:" + player.getStats().getTotalFishCaught());
            writer.newLine();
            writer.write("stats_moneyEarned:" + player.getStats().getTotalMoneyEarned());
            writer.newLine();
            writer.write("stats_xpEarned:" + player.getStats().getTotalXpEarned());
            writer.newLine();

            writer.write("best_overall:" + player.getStats().getBestCatch());
            writer.newLine();
            writer.write("best_anglerfish:" + player.getStats().getBestAnglerfish());
            writer.newLine();
            writer.write("best_redsalmon:" + player.getStats().getBestRedSalmon());
            writer.newLine();
            writer.write("best_swordfish:" + player.getStats().getBestSwordfish());
            writer.newLine();
            writer.write("best_oarfish:" + player.getStats().getBestOarfish());
            writer.newLine();
            writer.write("best_shark:" + player.getStats().getBestShark());
            writer.newLine();

            // Save Inventory Items (Format: item:Name,Qty,Rarity)
            for (InventoryItem item : player.getInventory()) {
                if (item != null) {
                    // Use "none" as a placeholder for null rarity (for Rods/Bait)
                    String rarity = item.getRarity() != null ? item.getRarity() : "none";

                    writer.write("item:" + item.getName() + "," + item.getQuantity() + "," + rarity);
                    writer.newLine();
                }
            }

            System.out.println("Game saved for: " + player.getUsername());

        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    //  Reads the file and reconstructs the Player object
    public Player loadGame(String username) {
        String filename = DATA_FOLDER + "/player_" + username + ".txt";
        File file = new File(filename);

        // If no save file exists, return a fresh player
        if (!file.exists()) {
            return new Player(username);
        }

        Player player = new Player(username);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length < 2) continue;

                String key = parts[0].trim();
                String value = parts[1].trim();

                switch (key) {
                    // Core Player Fields
                    case "coins":
                        player.setCoins(Integer.parseInt(value));
                        break;
                    case "xp":
                        player.setXp(Integer.parseInt(value));
                        break;

                    // PlayerStats Fields (Core)
                    case "stats_fishCaught":
                        player.getStats().setTotalFishCaught(Integer.parseInt(value));
                        break;
                    case "stats_moneyEarned":
                        player.getStats().setTotalMoneyEarned(Integer.parseInt(value));
                        break;
                    case "stats_xpEarned":
                        player.getStats().setTotalXpEarned(Integer.parseInt(value));
                        break;

                    // PlayerStats Fields (Best Catches)
                    case "best_overall":
                        player.getStats().setBestCatch(value);
                        break;
                    case "best_anglerfish":
                        player.getStats().setBestAnglerfish(value);
                        break;
                    case "best_redsalmon":
                        player.getStats().setBestRedSalmon(value);
                        break;
                    case "best_swordfish":
                        player.getStats().setBestSwordfish(value);
                        break;
                    case "best_oarfish":
                        player.getStats().setBestOarfish(value);
                        break;
                    case "best_shark":
                        player.getStats().setBestShark(value);
                        break;

                    // Inventory Item Loading
                    case "item":
                        // Value format: "ItemName,Quantity,Rarity"
                        String[] itemParts = value.split(",");

                        if (itemParts.length >= 2) {
                            String itemName = itemParts[0];
                            int qty = Integer.parseInt(itemParts[1]);

                            // Check if rarity was saved (length 3) and if it's not the "none" placeholder
                            String itemRarity = (itemParts.length == 3 && !itemParts[2].equals("none")) ? itemParts[2] : null;

                            // Determining the necessary item Type based on the name
                            String itemType = determineType(itemName);

                            // Creating the InventoryItem using the correct constructor
                            InventoryItem item;

                            if ("Fish".equals(itemType)) {
                                // Fish needs all 4 arguments (Type, Name, Qty, Rarity)
                                item = new InventoryItem(itemType, itemName, qty, itemRarity);
                            } else {
                                // Rods/Bait use the 3-argument constructor (Type, Name, Qty)
                                item = new InventoryItem(itemType, itemName, qty);
                            }

                            player.addToInventory(item);
                        }
                        break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading save file: " + e.getMessage());
        }

        return player;
    }

    // Helper method to determine the Type (Rod, Bait, Fish) from the name
    private String determineType(String itemName) {
        // Use .contains() for robustness
        if (itemName.contains("Rod")) {
            return "Rod";
        }
        if (itemName.contains("Worm") || itemName.contains("Bait") || itemName.contains("Lure")) {
            return "Bait";
        }
        // Assuming everything else is a Fish
        return "Fish";
    }

    // Simple login validator (checks if file exists)
    public boolean validateLogin(String username, String password) {
        File file = new File(DATA_FOLDER + "/player_" + username + ".txt");
        return file.exists();
    }

    // Simple register
    public boolean registerUser(String username, String password) {
        File file = new File(DATA_FOLDER + "/player_" + username + ".txt");
        if (file.exists()) return false;

        // Create a new player with defaults and save immediately
        Player newPlayer = new Player(username);
        saveGame(newPlayer);
        return true;
    }
}