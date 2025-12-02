package model;

public class PlayerStats {
    private int totalFishCaught;
    private int totalMoneyEarned;
    private int totalXpEarned;
    private String bestCatch;
    private String bestAnglerfish;
    private String bestRedSalmon;
    private String bestSwordfish;
    private String bestOarfish;
    private String bestShark;

    public PlayerStats() {
        this.totalFishCaught = 0;
        this.totalMoneyEarned = 0;
        this.totalXpEarned = 0;
        this.bestCatch = "None";
        this.bestAnglerfish = "None";
        this.bestRedSalmon = "None";
        this.bestSwordfish = "None";
        this.bestOarfish = "None";
        this.bestShark = "None";
    }

    // Total fish caught
    public int getTotalFishCaught() { return totalFishCaught; }
    public void incrementFishCaught() { this.totalFishCaught++; }
    public void setTotalFishCaught(int count) { this.totalFishCaught = count; }

    // Total money earned
    public int getTotalMoneyEarned() { return totalMoneyEarned; }
    public void addMoneyEarned(int amount) { this.totalMoneyEarned += amount; }
    public void setTotalMoneyEarned(int amount) { this.totalMoneyEarned = amount; }

    // Total XP earned
    public int getTotalXpEarned() { return totalXpEarned; }
    public void addXpEarned(int amount) { this.totalXpEarned += amount; }
    public void setTotalXpEarned(int amount) { this.totalXpEarned = amount; }

    // Best catch overall
    public String getBestCatch() { return bestCatch; }
    public void setBestCatch(String bestCatch) { this.bestCatch = bestCatch; }

    // Best catches per species
    public String getBestAnglerfish() { return bestAnglerfish; }
    public void setBestAnglerfish(String rarity) { this.bestAnglerfish = rarity; }

    public String getBestRedSalmon() { return bestRedSalmon; }
    public void setBestRedSalmon(String rarity) { this.bestRedSalmon = rarity; }

    public String getBestSwordfish() { return bestSwordfish; }
    public void setBestSwordfish(String rarity) { this.bestSwordfish = rarity; }

    public String getBestOarfish() { return bestOarfish; }
    public void setBestOarfish(String rarity) { this.bestOarfish = rarity; }

    public String getBestShark() { return bestShark; }
    public void setBestShark(String rarity) { this.bestShark = rarity; }
}