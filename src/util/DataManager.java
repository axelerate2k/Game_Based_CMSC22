package util;

import fishing.model.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Handles saving and loading player data using Java serialization.
*/
public class DataManager {
 private static final String DATA_DIR = "data";
 private static final String PLAYERS_FILE = DATA_DIR + "/players.ser";
 
 /**
  * Ensure data directory exists
  */
 static {
     File dataDir = new File(DATA_DIR);
     if (!dataDir.exists()) {
         dataDir.mkdirs();
     }
 }
 
 /**
  * Load all players from file
  * Returns a Map of username -> Player
  */
 @SuppressWarnings("unchecked")
 public static Map<String, Player> loadAllPlayers() {
     File file = new File(PLAYERS_FILE);
     
     if (!file.exists()) {
         return new HashMap<>();
     }
     
     try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
         return (Map<String, Player>) ois.readObject();
     } catch (IOException | ClassNotFoundException e) {
         System.err.println("Error loading players: " + e.getMessage());
         return new HashMap<>();
     }
 }
 
 /**
  * Save all players to file
  */
 public static void saveAllPlayers(Map<String, Player> players) {
     try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PLAYERS_FILE))) {
         oos.writeObject(players);
     } catch (IOException e) {
         System.err.println("Error saving players: " + e.getMessage());
         e.printStackTrace();
     }
 }
 
 /**
  * Save a single player (updates the players map)
  */
 public static void savePlayer(Player player) {
     Map<String, Player> players = loadAllPlayers();
     players.put(player.getUsername(), player);
     saveAllPlayers(players);
 }
 
 /**
  * Load a specific player by username
  */
 public static Player loadPlayer(String username) {
     Map<String, Player> players = loadAllPlayers();
     return players.get(username);
 }
 
 /**
  * Check if a player exists
  */
 public static boolean playerExists(String username) {
     Map<String, Player> players = loadAllPlayers();
     return players.containsKey(username);
 }
 
 /**
  * Register a new player (save to file)
  */
 public static boolean registerPlayer(String username, String password) {
     if (playerExists(username)) {
         return false; // Username already taken
     }
     
     Player newPlayer = new Player(username, password);
     savePlayer(newPlayer);
     return true;
 }
 
 /**
  * Authenticate a player (login)
  */
 public static Player authenticatePlayer(String username, String password) {
     Player player = loadPlayer(username);
     
     if (player == null) {
         return null; // Player not found
     }
     
     if (!player.verifyPassword(password)) {
         return null; // Wrong password
     }
     
     return player;
 }
 
 /**
  * Get all players sorted by total fish caught (for leaderboard)
  */
 public static List<Player> getLeaderboard() {
     Map<String, Player> players = loadAllPlayers();
     List<Player> playerList = new ArrayList<>(players.values());
     
     // Sort by total fish caught (descending)
     playerList.sort((p1, p2) -> 
         Integer.compare(p2.getStats().getTotalFishCaught(), 
                        p1.getStats().getTotalFishCaught())
     );
     
     return playerList;
 }
}