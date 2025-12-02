package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String username;
    private int coins;
    private int xp;
    private String equippedRod;
    private List<InventoryItem> inventory;
    private PlayerStats stats; // Assumes model.PlayerStats is a separate file
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
            // Item already exists. Only stack Bait and Fish.
            if (item.getType().equals("Bait") || item.getType().equals("Fish")) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            }
            // If the item is a unique equipment (like a Rod) and already exists,
            // we do nothing to prevent duplicates.
        } else {
            // Item does not exist. Add it regardless of type.
            inventory.add(item);
        }
    }

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

    public void addItem(InventoryItem item) {
        this.inventory.add(item);
    }
}