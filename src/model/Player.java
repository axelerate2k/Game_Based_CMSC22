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
    
    // --- ITEM MANAGEMENT LOGIC ---

    /**
     * Helper method to find an InventoryItem object by its name.
     */
    public InventoryItem findItemByName(String name) {
        for (InventoryItem item : inventory) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Changes the quantity of an item. Used for selling/using stackable items (Fish/Bait).
     * @param itemName The name of the item (e.g., "Red Salmon").
     * @param quantityChange The amount to add (positive) or remove (negative).
     * @return True if the quantity was successfully changed, false otherwise.
     */
    public boolean changeItemQuantity(String itemName, int quantityChange) {
        InventoryItem item = findItemByName(itemName);

        if (item == null) {
            // Cannot reduce quantity of an item the player doesn't have
            return quantityChange > 0;
        }

        int currentQty = item.getQuantity();
        int newQty = currentQty + quantityChange;

        if (newQty < 0) {
            // Player doesn't have enough to fulfill the reduction
            return false;
        }

        if (newQty == 0) {
            // Remove the item entirely if the quantity hits zero
            inventory.remove(item);

            // If the item removed was the selected bait, deselect it.
            if (item == this.selectedBait) {
                this.selectedBait = null;
            }
            return true;
        }

        // Otherwise, just update the quantity
        item.setQuantity(newQty);
        return true;
    }

    // --- END ITEM MANAGEMENT LOGIC ---

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

    /**
     * Adds an item to the inventory. If an existing stackable item is found,
     * the quantity is added to the existing stack. Otherwise, the item is added.
     * Unique items (like Rods) will not be added if a matching item already exists.
     */
    public void addToInventory(InventoryItem item) {
        InventoryItem existingItem = findItemByName(item.getName());

        if (existingItem != null) {
            // Case 1: Item already exists. Only stack Bait and Fish.
            if (item.getType().equals("Bait") || item.getType().equals("Fish")) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            }
            // If the item is a unique equipment (like a Rod) and already exists,
            // we do nothing to prevent duplicates.
        } else {
            // Case 2: Item does not exist. Add it regardless of type.
             inventory.add(item);
        }
    }

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
    public int getTotalBaits() {
        int total = 0;
        for (InventoryItem item : inventory) {
            if (item.getType().equals("Bait")) {
                total += item.getQuantity();
            }
        }
        return total;
    }
    
    // Get bait by name - uses the robust findItemByName helper
    public InventoryItem getBait(String baitName) {
        return findItemByName(baitName);
    }
    
    // Use one bait - uses the new changeItemQuantity method
    public boolean useBait() {
        if (selectedBait != null && selectedBait.getQuantity() > 0) {
            // Delegate the quantity change logic to the new robust method
            return changeItemQuantity(selectedBait.getName(), -1);
        }
        return false;
    }
   
    
    public int getLevel() {
    	// Calculate level: integer division of XP by 50, plus 1 for starting at level 1
        int level = (this.xp / 50) + 1; // Each level requires +50 XP
        return Math.min(level, 30);     // Max level 30
    }

    public int getXpForNextLevel() {
        int level = getLevel();
        if (level >= 30) return -1; // Max level reached, no further XP needed
        return level * 50;		// Total XP required to reach the next level
    }

    public int getXpRemaining() {
        int next = getXpForNextLevel();
        if (next == -1) return 0;		// If max level, no XP remaining
        return next - this.xp;			// XP left to reach next level
    }

}
