package model;

public class InventoryItem {
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
    
    // Get rod sell value
    public int getRodSellValue() {
        if (!type.equals("Rod")) return 0;
        
        switch (name) {
            case "Bamboo Rod": return 0; // Cannot sell
            case "Wooden Rod": return 100;
            case "Steel Rod": return 400;
            case "Master Rod": return 1200;
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