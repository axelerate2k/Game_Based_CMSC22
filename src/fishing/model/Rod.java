package fishing.model;

public class Rod extends Item {
    private static final long serialVersionUID = 1L;
    
    private int tier;
    private double rarityMultiplier;
    private int sweetSpotSize; // Percentage (15-30%)
    private int buyCostCoins;
    private int buyCostXP;
    private int sellValue;
    
    public Rod(int tier, String name, String description, double rarityMultiplier, 
               int sweetSpotSize, int buyCostCoins, int buyCostXP, int sellValue) {
        super(name, description, "assets/sprites/rod_tier" + tier + ".png");
        this.tier = tier;
        this.rarityMultiplier = rarityMultiplier;
        this.sweetSpotSize = sweetSpotSize;
        this.buyCostCoins = buyCostCoins;
        this.buyCostXP = buyCostXP;
        this.sellValue = sellValue;
    }
    
    @Override
    public String getType() {
        return "Rod";
    }
    
    public int getTier() {
        return tier;
    }
    
    public double getRarityMultiplier() {
        return rarityMultiplier;
    }
    
    public int getSweetSpotSize() {
        return sweetSpotSize;
    }
    
    public int getBuyCostCoins() {
        return buyCostCoins;
    }
    
    public int getBuyCostXP() {
        return buyCostXP;
    }
    
    public int getSellValue() {
        return sellValue;
    }
    
    public boolean canBeSold() {
        return tier > 1; // Cannot sell Bamboo Rod (tier 1)
    }
    
    // Static factory methods for each rod tier
    public static Rod getBambooRod() {
        return new Rod(1, "Bamboo Rod", 
            "A simple bamboo rod. Good for beginners.",
            1.0, 15, 0, 0, 0);
    }
    
    public static Rod getWoodenRod() {
        return new Rod(2, "Wooden Rod",
            "A sturdy wooden rod. Slightly better catch rates.",
            1.1, 20, 200, 100, 100);
    }
    
    public static Rod getSteelRod() {
        return new Rod(3, "Steel Rod",
            "A professional steel rod. Significantly improves rare catches.",
            1.2, 25, 800, 300, 400);
    }
    
    public static Rod getMasterRod() {
        return new Rod(4, "Master Rod",
            "The ultimate fishing rod. Legendary catches are more common.",
            1.35, 30, 2500, 800, 1200);
    }
}
