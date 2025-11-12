// FishingStats.java
package fishing.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Tracks player's fishing statistics for the logbook
 */
public class FishingStats implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int totalFishCaught;
    private int totalMoneyEarned;
    private int totalXPEarned;
    
    private Fish bestCatchOverall; // Most expensive fish ever caught
    
    // Track best rarity caught for each species
    private Map<FishSpecies, Rarity> bestCatchPerSpecies;
    
    // Track which unique fish have been caught (5 species × 3 rarities = 15)
    private boolean[][] caughtFish; // [species][rarity]
    
    public FishingStats() {
        this.totalFishCaught = 0;
        this.totalMoneyEarned = 0;
        this.totalXPEarned = 0;
        this.bestCatchOverall = null;
        this.bestCatchPerSpecies = new HashMap<>();
        
        // Initialize caught fish tracker (5 species, 3 rarities)
        this.caughtFish = new boolean[5][3];
    }
    
    /**
     * Record a fish catch
     */
    public void recordCatch(Fish fish) {
        totalFishCaught++;
        totalXPEarned += fish.getXpValue();
        
        // Mark fish as caught
        int speciesIndex = fish.getSpecies().ordinal();
        int rarityIndex = fish.getRarity().ordinal();
        caughtFish[speciesIndex][rarityIndex] = true;
        
        // Update best catch overall
        if (bestCatchOverall == null || fish.getCoinValue() > bestCatchOverall.getCoinValue()) {
            bestCatchOverall = fish;
        }
        
        // Update best catch per species
        FishSpecies species = fish.getSpecies();
        Rarity currentBest = bestCatchPerSpecies.get(species);
        
        if (currentBest == null || fish.getRarity().ordinal() > currentBest.ordinal()) {
            bestCatchPerSpecies.put(species, fish.getRarity());
        }
    }
    
    /**
     * Record a fish sale
     */
    public void recordSale(Fish fish) {
        totalMoneyEarned += fish.getCoinValue();
    }
    
    /**
     * Get collection progress (number of unique fish caught out of 15)
     */
    public int getCollectionProgress() {
        int count = 0;
        for (boolean[] speciesArray : caughtFish) {
            for (boolean caught : speciesArray) {
                if (caught) count++;
            }
        }
        return count;
    }
    
    /**
     * Check if a specific fish variant has been caught
     */
    public boolean hasCaught(FishSpecies species, Rarity rarity) {
        return caughtFish[species.ordinal()][rarity.ordinal()];
    }
    
    /**
     * Check if collection is complete (all 15 unique fish caught)
     */
    public boolean isCollectionComplete() {
        return getCollectionProgress() == 15;
    }
    
    // Getters
    public int getTotalFishCaught() {
        return totalFishCaught;
    }
    
    public int getTotalMoneyEarned() {
        return totalMoneyEarned;
    }
    
    public int getTotalXPEarned() {
        return totalXPEarned;
    }
    
    public Fish getBestCatchOverall() {
        return bestCatchOverall;
    }
    
    public Map<FishSpecies, Rarity> getBestCatchPerSpecies() {
        return new HashMap<>(bestCatchPerSpecies);
    }
    
    public boolean[][] getCaughtFish() {
        return caughtFish;
    }
}