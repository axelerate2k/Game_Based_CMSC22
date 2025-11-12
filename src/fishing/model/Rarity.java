package fishing.model;

public enum Rarity {
    COMMON("Common", "#CCCCCC"),// Gray
    RARE("Rare", "#9C27B0"), // Purple
    LEGENDARY("Legendary", "#FFD700");// Gold
    
    private final String displayName;
    private final String color;// Hex color for UI
    
    Rarity(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColor() {
        return color;
    }
}