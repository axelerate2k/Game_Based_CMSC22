package fishing.model;

public class Fish extends Item {
    private static final long serialVersionUID = 1L;
    
    private FishSpecies species;
    private Rarity rarity;
    private int coinValue;
    private int xpValue;
    
    public Fish(FishSpecies species, Rarity rarity) {
        super(
            rarity.name() + " " + species.name(),
            generateDescription(species, rarity),
            "assets/sprites/fish_" + species.name().toLowerCase() + ".png"
        );
        this.species = species;
        this.rarity = rarity;
        this.coinValue = calculateCoinValue(species, rarity);
        this.xpValue = calculateXPValue(species, rarity);
    }
    
    @Override
    public String getType() {
        return "Fish";
    }
    
    private static String generateDescription(FishSpecies species, Rarity rarity) {
        return "A " + rarity.name().toLowerCase() + " " + 
               species.getDisplayName() + ". " + species.getDescription();
    }
    
    /**
     * Calculate coin value based on fish species and rarity
     */
    private int calculateCoinValue(FishSpecies species, Rarity rarity) {
        int baseValue = species.getBaseValue();
        return switch (rarity) {
            case COMMON -> baseValue;
            case RARE -> baseValue * 3;
            case LEGENDARY -> baseValue * 10;
        };
    }
    
    /**
     * Calculate XP value based on fish species and rarity
     */
    private int calculateXPValue(FishSpecies species, Rarity rarity) {
        int baseXP = species.getBaseXP();
        return switch (rarity) {
            case COMMON -> baseXP;
            case RARE -> baseXP * 3;
            case LEGENDARY -> baseXP * 10;
        };
    }
    
    public FishSpecies getSpecies() {
        return species;
    }
    
    public Rarity getRarity() {
        return rarity;
    }
    
    public int getCoinValue() {
        return coinValue;
    }
    
    public int getXpValue() {
        return xpValue;
    }
    
    /**
     * Get CSS color for rarity border
     */
    public String getRarityColor() {
        return switch (rarity) {
            case COMMON -> "#CCCCCC"; // Gray
            case RARE -> "#9C27B0";   // Purple
            case LEGENDARY -> "#FFD700"; // Gold
        };
    }
}