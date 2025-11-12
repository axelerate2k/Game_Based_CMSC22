package fishing.model;

public class Bait extends Item {
    private static final long serialVersionUID = 1L;
    
    private BaitType type;
    private int pricePerUnit;
    private double[] catchProbabilities; // [common%, rare%, legendary%]
    
    public Bait(BaitType type, String name, String description, int pricePerUnit,
                double commonProb, double rareProb, double legendaryProb) {
        super(name, description, "assets/sprites/bait_" + type.name().toLowerCase() + ".png");
        this.type = type;
        this.pricePerUnit = pricePerUnit;
        this.catchProbabilities = new double[]{commonProb, rareProb, legendaryProb};
    }
    
    @Override
    public String getType() {
        return "Bait";
    }
    
    public BaitType getBaitType() {
        return type;
    }
    
    public int getPricePerUnit() {
        return pricePerUnit;
    }
    
    public double[] getCatchProbabilities() {
        return catchProbabilities;
    }
    
    // Static factory methods for each bait type
    public static Bait getBasicWorm() {
        return new Bait(BaitType.BASIC_WORM, "Basic Worm",
            "Simple earthworm. Attracts common fish.",
            3, 0.60, 0.30, 0.10);
    }
    
    public static Bait getEnhancedBait() {
        return new Bait(BaitType.ENHANCED_BAIT, "Enhanced Bait",
            "Premium bait mix. Balanced catch rates.",
            10, 0.40, 0.40, 0.20);
    }
    
    public static Bait getRareLure() {
        return new Bait(BaitType.RARE_LURE, "Rare Lure",
            "Shiny lure that attracts rare fish.",
            25, 0.20, 0.50, 0.30);
    }
    
    public static Bait getLegendaryBait() {
        return new Bait(BaitType.LEGENDARY_BAIT, "Legendary Bait",
            "Exotic bait. High chance for legendary catches!",
            60, 0.05, 0.45, 0.50);
    }
}