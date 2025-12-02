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
    private int selectedSlotIndex = -1; // Track selected bait slot for visual feedback
    
    public Player(String username) {
        this.username = username;
        this.coins = 50;
        this.xp = 0;
        this.equippedRod = "Bamboo Rod";
        this.inventory = new ArrayList<>();
        this.stats = new PlayerStats();
        this.lastRewardClaim = 0;
        this.selectedBait = null;
        
        // Add starter items - Fill exactly 15 slots for testing
        inventory.add(new InventoryItem("Rod", "Bamboo Rod", 1));
        inventory.add(new InventoryItem("Bait", "Basic Worm", 20));
        inventory.add(new InventoryItem("Bait", "Enhanced Bait", 10));
        inventory.add(new InventoryItem("Fish", "Anglerfish", 1, "Common"));
        inventory.add(new InventoryItem("Fish", "Red Salmon", 1, "Rare"));
        inventory.add(new InventoryItem("Fish", "Swordfish", 1, "Legendary"));
        inventory.add(new InventoryItem("Bait", "Rare Lure", 5));
        inventory.add(new InventoryItem("Rod", "Wooden Rod", 1));
        inventory.add(new InventoryItem("Fish", "Oarfish", 1, "Common"));
        // Leave some empty slots (inventory size = 15)
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
    
    public int getSelectedSlotIndex() { return selectedSlotIndex; }
    public void setSelectedSlotIndex(int index) { this.selectedSlotIndex = index; }
    
    // Get item at specific inventory slot
    public InventoryItem getItemAt(int index) {
        if (index >= 0 && index < inventory.size()) {
            return inventory.get(index);
        }
        return null;
    }
    
 // Add this method to your Player class
    public void addItem(InventoryItem item) { 
        // This is the method the PlayerDataManager needs to call
        // It should add the item to the list.
        inventory.add(item); 
    }
    
    // Swap two items in inventory
    public void swapItems(int index1, int index2) {
        if (index1 >= 0 && index1 < 15 && index2 >= 0 && index2 < 15) {
            // Ensure inventory list is large enough
            while (inventory.size() < 15) {
                inventory.add(null);
            }
            
            InventoryItem temp = inventory.get(index1);
            inventory.set(index1, inventory.get(index2));
            inventory.set(index2, temp);
        }
    }
    
    // Set item at specific slot
    public void setItemAt(int index, InventoryItem item) {
        if (index >= 0 && index < 15) {
            while (inventory.size() <= index) {
                inventory.add(null);
            }
            inventory.set(index, item);
        }
    }
    
    // Get total bait count
 // Get total bait count
    public int getTotalBaits() {
        int total = 0;
        for (InventoryItem item : inventory) {
            // FIX: MUST check if item is not null before calling any method on it
            if (item != null && "Bait".equals(item.getType())) {
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