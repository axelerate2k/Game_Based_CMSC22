package fishing.model;

public enum FishSpecies {
    SALMON("Salmon", "A popular freshwater fish with pink flesh.", 10, 5),
    BASS("Bass", "A common game fish found in lakes and rivers.", 8, 5),
    TUNA("Tuna", "A large ocean fish prized for its meat.", 20, 10),
    MARLIN("Marlin", "A powerful billfish known for its fighting spirit.", 25, 12),
    KOI("Koi", "An ornamental carp with beautiful colors.", 15, 8);
    
    private final String displayName;
    private final String description;
    private final int baseValue; // Base coin value for common rarity
    private final int baseXP;    // Base XP for common rarity
    
    FishSpecies(String displayName, String description, int baseValue, int baseXP) {
        this.displayName = displayName;
        this.description = description;
        this.baseValue = baseValue;
        this.baseXP = baseXP;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getBaseValue() {
        return baseValue;
    }
    
    public int getBaseXP() {
        return baseXP;
    }
}