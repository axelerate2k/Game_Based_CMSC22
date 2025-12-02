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

    private String type; // "Rod", "Bait", "Fish"
    private String name;
    private int quantity;
    private String rarity; // For fish: "Common", "Rare", "Legendary"
    
    public InventoryItem(String type, String name, int quantity) {
        this.type = type;
        this.name = name;
        this.quantity = quantity;
        this.rarity = null;
    }
    
    public InventoryItem(String type, String name, int quantity, String rarity) {
        this.type = type;
        this.name = name;
        this.quantity = quantity;
        this.rarity = rarity;
    }
    
    public String getType() { return type; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getRarity() { return rarity; }
    
    // Get sell value (coins and XP)
    public int getSellCoins() {
        if (!type.equals("Fish")) return 0;
        
        // Fish sell values based on project spec
        switch (name) {
            case "Anglerfish":
                if (rarity.equals("Common")) return 10;
                if (rarity.equals("Rare")) return 30;
                if (rarity.equals("Legendary")) return 100;
                break;
            case "Red Salmon":
                if (rarity.equals("Common")) return 8;
                if (rarity.equals("Rare")) return 25;
                if (rarity.equals("Legendary")) return 80;
                break;
            case "Swordfish":
                if (rarity.equals("Common")) return 20;
                if (rarity.equals("Rare")) return 60;
                if (rarity.equals("Legendary")) return 200;
                break;
            case "Oarfish":
                if (rarity.equals("Common")) return 25;
                if (rarity.equals("Rare")) return 75;
                if (rarity.equals("Legendary")) return 250;
                break;
            case "Great White Shark":
                if (rarity.equals("Common")) return 15;
                if (rarity.equals("Rare")) return 50;
                if (rarity.equals("Legendary")) return 180;
                break;
        }
        return 0;
    }
    
    public int getSellXP() {
        if (!type.equals("Fish")) return 0;
        
        switch (name) {
            case "Anglerfish":
                if (rarity.equals("Common")) return 5;
                if (rarity.equals("Rare")) return 15;
                if (rarity.equals("Legendary")) return 50;
                break;
            case "Red Salmon":
                if (rarity.equals("Common")) return 5;
                if (rarity.equals("Rare")) return 15;
                if (rarity.equals("Legendary")) return 50;
                break;
            case "Swordfish":
                if (rarity.equals("Common")) return 10;
                if (rarity.equals("Rare")) return 25;
                if (rarity.equals("Legendary")) return 80;
                break;
            case "Oarfish":
                if (rarity.equals("Common")) return 12;
                if (rarity.equals("Rare")) return 30;
                if (rarity.equals("Legendary")) return 100;
                break;
            case "Great White Shark":
                if (rarity.equals("Common")) return 8;
                if (rarity.equals("Rare")) return 20;
                if (rarity.equals("Legendary")) return 70;
                break;
        }
        return 0;
    }
    
    // Get description for item details modal
    public String getDescription() {
        if (type.equals("Bait")) {
            switch (name) {
                case "Basic Worm": return "Common bait. 60% Common, 30% Rare, 10% Legendary";
                case "Enhanced Bait": return "Better bait. 40% Common, 40% Rare, 20% Legendary";
                case "Rare Lure": return "Rare bait. 20% Common, 50% Rare, 30% Legendary";
                case "Master Bait": return "Best bait. 5% Common, 45% Rare, 50% Legendary";
            }
        } else if (type.equals("Rod")) {
            switch (name) {
                case "Bamboo Rod": return "Starter rod. 15% sweet spot. Base catch rates.";
                case "Wooden Rod": return "Better rod. 20% sweet spot. +10% Rare/Legendary";
                case "Steel Rod": return "Great rod. 25% sweet spot. +20% Rare/Legendary";
                case "Master Rod": return "Best rod. 30% sweet spot. +35% Rare/Legendary";
            }
        } else if (type.equals("Fish")) {
            return rarity + " " + name + ". Caught from the sea!";
        }
        return "An item.";
    }
}

// Inner class for player statistics