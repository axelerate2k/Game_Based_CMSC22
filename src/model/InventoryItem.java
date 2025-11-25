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
}