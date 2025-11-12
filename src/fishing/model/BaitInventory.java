// BaitInventory.java
package fishing.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages player's bait inventory (unlimited storage, quantity-based)
 */
public class BaitInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Maps BaitType to quantity owned
    private Map<BaitType, Integer> baits;
    
    public BaitInventory() {
        this.baits = new HashMap<>();
        
        // Initialize all bait types with 0 quantity
        for (BaitType type : BaitType.values()) {
            baits.put(type, 0);
        }
    }
    
    /**
     * Add baits to inventory
     */
    public void addBait(Bait bait, int quantity) {
        BaitType type = bait.getBaitType();
        int currentQuantity = baits.getOrDefault(type, 0);
        baits.put(type, currentQuantity + quantity);
    }
    
    /**
     * Remove baits from inventory (e.g., when fishing)
     */
    public boolean removeBait(Bait bait, int quantity) {
        BaitType type = bait.getBaitType();
        int currentQuantity = baits.getOrDefault(type, 0);
        
        if (currentQuantity < quantity) {
            return false; // Not enough baits
        }
        
        baits.put(type, currentQuantity - quantity);
        return true;
    }
    
    /**
     * Get quantity of specific bait type
     */
    public int getBaitQuantity(BaitType type) {
        return baits.getOrDefault(type, 0);
    }
    
    /**
     * Check if player has at least 1 of any bait
     */
    public boolean hasAnyBait() {
        return baits.values().stream().anyMatch(qty -> qty > 0);
    }
    
    /**
     * Check if player has specific bait
     */
    public boolean hasBait(BaitType type) {
        return baits.getOrDefault(type, 0) > 0;
    }
    
    /**
     * Get total bait count across all types
     */
    public int getTotalBaitCount() {
        return baits.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Get all baits with their quantities
     */
    public Map<BaitType, Integer> getAllBaits() {
        return new HashMap<>(baits);
    }
}