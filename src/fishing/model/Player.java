package fishing.model;

//Player.java

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* Represents a player in the fishing game.
* Stores player credentials, resources, inventory, and statistics.
*/
public class Player implements Serializable {
 private static final long serialVersionUID = 1L;
 
 // Account information
 private String username;
 private String passwordHash;// Store hashed password for security
 
 // Resources
 private int coins;
 private int xp;
 
 // Inventories
 private Inventory inventory;
 private BaitInventory baitInventory;
 private Rod equippedRod;
 
 // Statistics
 private FishingStats stats;
 
 // Daily reward tracking
 private LocalDateTime lastRewardClaim;
 
 /**
  * Constructor for new player (registration)
  */
 public Player(String username, String password) {
     this.username = username;
     this.passwordHash = hashPassword(password);
     
     // Starting resources
     this.coins = 50;
     this.xp = 0;
     
     // Initialize inventories
     this.inventory = new Inventory(15);// 15 slots
     this.baitInventory = new BaitInventory();
     
     // Starting equipment
     this.equippedRod = Rod.getBambooRod(); / Starter rod
     this.inventory.addItem(this.equippedRod);
     
     // Starting baits (20 Basic Worms)
     this.baitInventory.addBait(Bait.getBasicWorm(), 20);
     
     // Initialize stats
     this.stats = new FishingStats();
     
     // No reward claimed yet
     this.lastRewardClaim = null;
 }
 
 // Getters and Setters
 public String getUsername() {
     return username;
 }
 
 public int getCoins() {
     return coins;
 }
 
 public void setCoins(int coins) {
     this.coins = Math.max(0, coins); // Prevent negative coins
 }
 
 public void addCoins(int amount) {
     this.coins += amount;
 }
 
 public void deductCoins(int amount) {
     this.coins = Math.max(0, this.coins - amount);
 }
 
 public int getXp() {
     return xp;
 }
 
 public void setXp(int xp) {
     this.xp = Math.max(0, xp); // Prevent negative XP
 }
 
 public void addXp(int amount) {
     this.xp += amount;
 }
 
 public void deductXp(int amount) {
     this.xp = Math.max(0, this.xp - amount);
 }
 
 public Inventory getInventory() {
     return inventory;
 }
 
 public BaitInventory getBaitInventory() {
     return baitInventory;
 }
 
 public Rod getEquippedRod() {
     return equippedRod;
 }
 
 public void equipRod(Rod rod) {
     this.equippedRod = rod;
 }
 
 public FishingStats getStats() {
     return stats;
 }
 
 public LocalDateTime getLastRewardClaim() {
     return lastRewardClaim;
 }
 
 public void setLastRewardClaim(LocalDateTime time) {
     this.lastRewardClaim = time;
 }
 
 /**
  * Verify password for login
  */
 public boolean verifyPassword(String password) {
     return this.passwordHash.equals(hashPassword(password));
 }
 
 /**
  * Simple password hashing (use stronger hashing in production)
  */
 private String hashPassword(String password) {
     // For simplicity, using basic hash. In production, use BCrypt or similar
     return Integer.toString(password.hashCode());
 }
 
 /**
  * Purchase a rod from shop
  */
 public boolean purchaseRod(Rod rod) {
     // Check if player has enough resources
     if (this.coins < rod.getBuyCostCoins() || this.xp < rod.getBuyCostXP()) {
         return false;
     }
     
     // Check if inventory has space
     if (this.inventory.isFull()) {
         return false;
     }
     
     // Deduct resources
     deductCoins(rod.getBuyCostCoins());
     deductXp(rod.getBuyCostXP());
     
     // Add rod to inventory
     return this.inventory.addItem(rod);
 }
 
 /**
  * Sell a rod from inventory
  */
 public boolean sellRod(Rod rod) {
     // Cannot sell starter rod
     if (!rod.canBeSold()) {
         return false;
     }
     
     // Remove from inventory
     if (this.inventory.removeItem(rod)) {
         addCoins(rod.getSellValue());
         
         // If sold equipped rod, equip bamboo rod
         if (this.equippedRod == rod) {
             this.equippedRod = findBambooRod();
         }
         return true;
     }
     return false;
 }
 
 /**
  * Find Bamboo Rod in inventory (always exists)
  */
 private Rod findBambooRod() {
     for (Item item : inventory.getItems()) {
         if (item instanceof Rod && ((Rod) item).getTier() == 1) {
             return (Rod) item;
         }
     }
     return Rod.getBambooRod(); // Fallback
 }
 
 /**
  * Purchase baits from shop
  */
 public boolean purchaseBait(Bait bait, int quantity) {
     int totalCost = bait.getPricePerUnit() * quantity;
     
     if (this.coins < totalCost) {
         return false;
     }
     
     deductCoins(totalCost);
     baitInventory.addBait(bait, quantity);
     return true;
 }
 
 /**
  * Catch a fish and add to inventory
  */
 public boolean catchFish(Fish fish) {
     if (this.inventory.isFull()) {
         return false;
     }
     
     // Add fish to inventory
     boolean added = this.inventory.addItem(fish);
     
     if (added) {
         // Update statistics
         stats.recordCatch(fish);
         addXp(fish.getXpValue());
     }
     
     return added;
 }
 
 /**
  * Sell a fish from inventory
  */
 public boolean sellFish(Fish fish) {
     if (this.inventory.removeItem(fish)) {
         addCoins(fish.getCoinValue());
         addXp(fish.getXpValue());
         stats.recordSale(fish);
         return true;
     }
     return false;
 }
 
 @Override
 public String toString() {
     return "Player{" +
             "username='" + username + '\'' +
             ", coins=" + coins +
             ", xp=" + xp +
             '}';
 }
}