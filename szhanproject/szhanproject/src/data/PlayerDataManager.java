package data;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PlayerDataManager {
    private static final String DATA_FOLDER = "player_data";
    
    public PlayerDataManager() {
        // Create data folder if it doesn't exist
        try {
            Files.createDirectories(Paths.get(DATA_FOLDER));
        } catch (IOException e) {
            System.err.println("Could not create data folder: " + e.getMessage());
        }
    }
    
    public boolean registerUser(String username, String password) {
        File playerFile = new File(DATA_FOLDER + "/player_" + username + ".txt");
        
        if (playerFile.exists()) {
            return false; // Username already taken
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(playerFile))) {
            // Write initial player data
            writer.write("USERNAME:" + username + "\n");
            writer.write("PASSWORD:" + password + "\n");
            writer.write("COINS:50\n");
            writer.write("XP:0\n");
            writer.write("EQUIPPED_ROD:Bamboo Rod\n");
            writer.write("INVENTORY:\n");
            writer.write("  Rod,Bamboo Rod,1\n");
            writer.write("  Bait,Basic Worm,20\n");
            writer.write("STATS:\n");
            writer.write("  TOTAL_FISH_CAUGHT:0\n");
            writer.write("  TOTAL_MONEY_EARNED:0\n");
            writer.write("  TOTAL_XP_EARNED:0\n");
            writer.write("  BEST_CATCH:None\n");
            writer.write("  BEST_ANGLERFISH:None\n");
            writer.write("  BEST_RED_SALMON:None\n");
            writer.write("  BEST_SWORDFISH:None\n");
            writer.write("  BEST_OARFISH:None\n");
            writer.write("  BEST_SHARK:None\n");
            writer.write("LAST_REWARD_CLAIM:0\n");
            
            return true;
        } catch (IOException e) {
            System.err.println("Error creating player file: " + e.getMessage());
            return false;
        }
    }
    
    public boolean validateLogin(String username, String password) {
        File playerFile = new File(DATA_FOLDER + "/player_" + username + ".txt");
        
        if (!playerFile.exists()) {
            return false; // User doesn't exist
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(playerFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PASSWORD:")) {
                    String storedPassword = line.substring("PASSWORD:".length());
                    return storedPassword.equals(password);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading player file: " + e.getMessage());
        }
        
        return false;
    }
    
    public Map<String, String> loadPlayerData(String username) {
        Map<String, String> playerData = new HashMap<>();
        File playerFile = new File(DATA_FOLDER + "/player_" + username + ".txt");
        
        if (!playerFile.exists()) {
            return null;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(playerFile))) {
            String line;
            StringBuilder currentSection = new StringBuilder();
            String currentKey = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.contains(":") && !line.startsWith("  ")) {
                    // Save previous section if exists
                    if (currentKey != null) {
                        playerData.put(currentKey, currentSection.toString().trim());
                    }
                    
                    // Start new section
                    String[] parts = line.split(":", 2);
                    currentKey = parts[0];
                    currentSection = new StringBuilder(parts.length > 1 ? parts[1] : "");
                } else if (currentKey != null) {
                    currentSection.append("\n").append(line);
                }
            }
            
            // Save last section
            if (currentKey != null) {
                playerData.put(currentKey, currentSection.toString().trim());
            }
            
        } catch (IOException e) {
            System.err.println("Error loading player data: " + e.getMessage());
            return null;
        }
        
        return playerData;
    }
}