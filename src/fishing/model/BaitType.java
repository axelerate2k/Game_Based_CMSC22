package fishing.model;

public enum BaitType {
    BASIC_WORM("Basic Worm"),
    ENHANCED_BAIT("Enhanced Bait"),
    RARE_LURE("Rare Lure"),
    LEGENDARY_BAIT("Legendary Bait");
    
    private final String displayName;
    
    BaitType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}