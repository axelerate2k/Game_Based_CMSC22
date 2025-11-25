package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String username;
    private int coins;
    private int xp;
    private String equippedRod;
    private List<InventoryItem> inventory;
    private PlayerStats stats;
    private long lastRewardClaim;
    private InventoryItem selectedBait;
    
    public Player(String username) {
        this.username = username;
        this.coins = 50;
        this.xp = 0;
        this.equippedRod = "Bamboo Rod";
        this.inventory = new ArrayList<>();
        this.stats = new PlayerStats();
        this.lastRewardClaim = 0;
        this.selectedBait = null;
        
        // Add starter items
        inventory.add(new InventoryItem("Rod", "Bamboo Rod", 1));
        inventory.add(new InventoryItem("Bait", "Basic Worm", 20));
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public int getCoins() { return coins; }
    public void setCoins(int coins) { this.coins = coins; }
    public void addCoins(int amount) { this.coins += amount; }
    public void removeCoins(int amount) { this.coins -= amount; }
    
    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
    public void addXp(int amount) { this.xp += amount; }
    public void removeXp(int amount) { this.xp -= amount; }
    
    public String getEquippedRod() { return equippedRod; }
    public void setEquippedRod(String rod) { this.equippedRod = rod; }
    
    public List<InventoryItem> getInventory() { return inventory; }
    public void addToInventory(InventoryItem item) { inventory.add(item); }
    public void removeFromInventory(InventoryItem item) { inventory.remove(item); }
    
    public PlayerStats getStats() { return stats; }
    
    public long getLastRewardClaim() { return lastRewardClaim; }
    public void setLastRewardClaim(long timestamp) { this.lastRewardClaim = timestamp; }
    
    public InventoryItem getSelectedBait() { return selectedBait; }
    public void setSelectedBait(InventoryItem bait) { this.selectedBait = bait; }
    
    // Get total bait count
    public int getTotalBaits() {
        int total = 0;
        for (InventoryItem item : inventory) {
            if (item.getType().equals("Bait")) {
                total += item.getQuantity();
            }
        }
        return total;
    }
    
    // Get bait by name
    public InventoryItem getBait(String baitName) {
        for (InventoryItem item : inventory) {
            if (item.getType().equals("Bait") && item.getName().equals(baitName)) {
                return item;
            }
        }
        return null;
    }
    
    // Use one bait
    public boolean useBait() {
        if (selectedBait != null && selectedBait.getQuantity() > 0) {
            selectedBait.setQuantity(selectedBait.getQuantity() - 1);
            if (selectedBait.getQuantity() == 0) {
                inventory.remove(selectedBait);
                selectedBait = null;
            }
            return true;
        }
        return false;
    }
}

// Inner class for player statistics
class PlayerStats {
    private int totalFishCaught;
    private int totalMoneyEarned;
    private int totalXpEarned;
    private String bestCatch;
    private String bestAnglerfish;
    private String bestRedSalmon;
    private String bestSwordfish;
    private String bestOarfish;
    private String bestShark;
    
    public PlayerStats() {
        this.totalFishCaught = 0;
        this.totalMoneyEarned = 0;
        this.totalXpEarned = 0;
        this.bestCatch = "None";
        this.bestAnglerfish = "None";
        this.bestRedSalmon = "None";
        this.bestSwordfish = "None";
        this.bestOarfish = "None";
        this.bestShark = "None";
    }
    
    public int getTotalFishCaught() { return totalFishCaught; }
    public void incrementFishCaught() { this.totalFishCaught++; }
    
    public int getTotalMoneyEarned() { return totalMoneyEarned; }
    public void addMoneyEarned(int amount) { this.totalMoneyEarned += amount; }
    
    public int getTotalXpEarned() { return totalXpEarned; }
    public void addXpEarned(int amount) { this.totalXpEarned += amount; }
    
    public String getBestCatch() { return bestCatch; }
    public void setBestCatch(String bestCatch) { this.bestCatch = bestCatch; }
    
    // Getters and setters for best catches per species
    public String getBestAnglerfish() { return bestAnglerfish; }
    public void setBestAnglerfish(String rarity) { this.bestAnglerfish = rarity; }
    
    public String getBestRedSalmon() { return bestRedSalmon; }
    public void setBestRedSalmon(String rarity) { this.bestRedSalmon = rarity; }
    
    public String getBestSwordfish() { return bestSwordfish; }
    public void setBestSwordfish(String rarity) { this.bestSwordfish = rarity; }
    
    public String getBestOarfish() { return bestOarfish; }
    public void setBestOarfish(String rarity) { this.bestOarfish = rarity; }
    
    public String getBestShark() { return bestShark; }
    public void setBestShark(String rarity) { this.bestShark = rarity; }
}