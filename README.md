# 🎣 Fishing Game - Complete Project Plan
**CMSC 22 Final Project - JavaFX Game-Based Inventory System**

---

## 📋 Project Overview

### Theme: Fishing Adventure
A relaxing fishing game where players collect all 5 fish species (each with 3 rarity variants), manage their inventory, upgrade rods, purchase baits, and claim daily rewards.

### Core Objective
Catch all 5 fish species in Common, Rare, and Legendary rarities while managing limited inventory space (15 slots) and resources (coins, XP, baits).

---

## 🎮 Game Mechanics

### Starting Resources
- **Level**: Static (no leveling system)
- **XP**: 0 (used as currency, not for leveling)
- **Coins**: 50
- **Baits**: 20 Basic Worms
- **Rod**: Bamboo Rod (starter, cannot be sold)
- **Inventory Slots**: 15 (limited capacity)

---

## 🎣 Fishing System

### Fishing Mechanic (Timing-Based)
1. Player selects a bait and clicks "Cast Line"
2. Fisherman animation plays (idle → casting → fishing)
3. After 2-3 seconds, a **timing mini-game** appears:
   - Horizontal bar with a moving indicator (slides left-right)
   - Green "sweet spot" zone in the middle
   - Player must click when indicator is in the sweet spot
4. **Success**: Catch a fish based on bait + rod probabilities
5. **Failure**: Bait consumed, no fish caught, show "The fish got away!" message

### Sweet Spot Difficulty by Rod
- **Bamboo Rod**: Sweet spot = 15% of bar width
- **Wooden Rod**: Sweet spot = 20% of bar width
- **Steel Rod**: Sweet spot = 25% of bar width
- **Master Rod**: Sweet spot = 30% of bar width

### Fishing Process
1. One bait consumed per cast (regardless of success/failure)
2. On successful catch: Fish added to inventory (if space available)
3. On failure: Bait consumed, no reward
4. Inventory full? Show warning: "Inventory full! Sell fish to make space."

---

## 🐟 Fish System

### 5 Fish Species (Each with 3 Rarities)

| Fish Species | Common | Rare | Legendary |
|-------------|--------|------|-----------|
| **Salmon** | 10 coins / 5 XP | 30 coins / 15 XP | 100 coins / 50 XP |
| **Bass** | 8 coins / 5 XP | 25 coins / 15 XP | 80 coins / 50 XP |
| **Tuna** | 20 coins / 10 XP | 60 coins / 25 XP | 200 coins / 80 XP |
| **Marlin** | 25 coins / 12 XP | 75 coins / 30 XP | 250 coins / 100 XP |
| **Koi** | 15 coins / 8 XP | 50 coins / 20 XP | 180 coins / 70 XP |

### Rarity Visual Design
- **Common**: Gray/White border glow
- **Rare**: Blue/Purple border glow
- **Legendary**: Gold/Orange border glow

### Fish Sprites
- Use provided fish spritesheets
- Apply colored border/glow in JavaFX (CSS drop-shadow or border styling)

---

## 🪱 Bait System

### 4 Bait Types with Catch Probabilities

| Bait Type | Cost (per unit) | Common % | Rare % | Legendary % |
|-----------|----------------|----------|--------|-------------|
| **Basic Worm** | 3 coins | 60% | 30% | 10% |
| **Enhanced Bait** | 10 coins | 40% | 40% | 20% |
| **Rare Lure** | 25 coins | 20% | 50% | 30% |
| **Legendary Bait** | 60 coins | 5% | 45% | 50% |

### Bait Shop Bundles (Optional for better value)
- 10x Basic Worm: 25 coins (save 5 coins)
- 5x Enhanced Bait: 45 coins (save 5 coins)
- 3x Rare Lure: 70 coins (save 5 coins)

### Bait Inventory
- Baits stored separately from main inventory (unlimited bait storage)
- Display bait count in fishing screen UI

---

## 🎣 Rod System

### 4 Rod Tiers (Purchasable, Not Upgrades)

| Rod Name | Buy Cost | Sell Value | Rarity Multiplier | Sweet Spot Size |
|----------|----------|------------|-------------------|-----------------|
| **Bamboo Rod** | FREE (starter) | Cannot sell | Base (1.0x) | 15% |
| **Wooden Rod** | 200 coins + 100 XP | 100 coins | +10% to Rare/Legendary | 20% |
| **Steel Rod** | 800 coins + 300 XP | 400 coins | +20% to Rare/Legendary | 25% |
| **Master Rod** | 2500 coins + 800 XP | 1200 coins | +35% to Rare/Legendary | 30% |

### Rod Mechanics
- Players can **own multiple rods** and switch between them
- Buying a rod costs **Coins + XP** (XP is spent as currency)
- Selling a rod returns **only coins** (50% of buy price)
- **Cannot sell Bamboo Rod** (starter rod)
- Rod multiplier affects final catch probabilities:
  - Example: Basic Worm (60% Common, 30% Rare, 10% Legendary) + Master Rod (+35% to Rare/Legendary)
  - Final: 60% → 55% Common, 30% → 35% Rare, 10% → 15% Legendary (redistributed proportionally)

### Rod Storage
- Rods take up inventory space (1 slot per rod)
- Active/equipped rod highlighted in inventory

---

## 🎒 Inventory System

### Inventory Details
- **Capacity**: 15 slots total
- **Slot contents**: Fish, Rods, Baits (baits have separate unlimited storage)
- **Visual Design**: GridPane with rounded rectangles (JavaFX CSS)
  - Each slot: Rounded box, item sprite, rarity border, quantity overlay
  - Empty slots: Grayed out rounded box

### Inventory Actions
1. **View Item**: Click item → Modal popup with:
   - Full description
   - Rarity and stats
   - Sell price (coins + XP gained)
   - "Sell" button (for fish)
   - "Equip" button (for rods)
   - "Close" button

2. **Manual Sorting**: Drag-and-drop items to reorder (JavaFX drag API)

3. **Auto-Sort Button**: Sorts inventory by:
   - First: Rods (by tier)
   - Second: Fish (by rarity: Legendary → Rare → Common)
   - Third: Empty slots at end

4. **Selling Fish**:
   - Click fish → Modal with "Sell" button
   - Gain coins + XP on sale
   - Fish removed from inventory (slot becomes empty)
   - Update player stats immediately

### Inventory Tab Layout
- **Single Tab**: "Inventory" (shows all items)
- Grid display: 5 columns × 3 rows = 15 slots
- Navigation buttons: "Sort Inventory" | "Back to Dashboard"

---

## 📖 Logbook System

### Fishing Statistics Tracked
1. **Total Fish Caught**: Count of all fish ever caught (lifetime)
2. **Total Money Earned**: Sum of all coins earned from selling fish
3. **Total XP Earned**: Sum of all XP earned from catching/selling fish
4. **Best Catch Overall**: Most expensive fish ever caught (name, rarity, value)
5. **Best Catch Per Species**: Highest rarity caught for each of the 5 fish species
6. **Collection Progress**: "Caught X/15 unique fish" (5 species × 3 rarities)

### Logbook UI
- Separate screen/tab: "Logbook"
- Display stats in clean, readable format
- Show fish sprites for best catches
- **Does NOT take inventory space** (separate from inventory)

---

## 🎁 Daily Reward System

### Reward Claiming
- **Demo Mode**: Claim every 5 seconds (configurable in code)
- **Real Mode**: Claim every 24 hours (configurable in code)
- **Cooldown Timer**: Display "Next reward in: XX:XX:XX" countdown

### Reward Types (Random with Probabilities)

| Rarity | Probability | Rewards |
|--------|-------------|---------|
| **Common** | 60% | 30 XP, 10 Basic Worms, OR 20 coins |
| **Rare** | 30% | 50 XP, 5 Enhanced Baits, OR 50 coins |
| **Legendary** | 10% | 100 XP, 3 Rare Lures, OR 100 coins OR 1 Legendary Fish |

### Reward UI
- Chest sprite with opening animation
- Click "Claim Reward" button
- Chest opens → Display reward (text + sprites)
- Confetti/sparkle effect on claim
- Cooldown timer starts immediately after claim

### Reward Configuration (for easy demo adjustment)
```java
// In DailyRewardManager class
public static final long REWARD_COOLDOWN_MS = 5000; // 5 seconds for demo
// Change to 86400000 for 24 hours in production
```

---

## 🛒 Shop System

### Shop Screen Layout
Two sections: **Rod Shop** and **Bait Shop**

#### Rod Shop
- Display all 4 rods in grid format
- Each rod card shows:
  - Rod sprite/icon
  - Name and tier
  - Stats (sweet spot size, rarity multiplier)
  - Buy cost (coins + XP)
  - Sell value (coins only)
  - "Buy" button (disabled if already owned or insufficient funds)
  - "Owned" indicator (green checkmark)

#### Bait Shop
- Display all 4 bait types
- Each bait card shows:
  - Bait sprite
  - Name
  - Catch probabilities (Common/Rare/Legendary %)
  - Price per unit
  - Input field: "Quantity to buy"
  - "Buy" button
  - Display total cost below

### Shop Actions
1. **Buying Rods**:
   - Check if player has enough coins + XP
   - Deduct coins + XP from player
   - Add rod to inventory (if space available)
   - If inventory full: Show "Inventory full!" error

2. **Buying Baits**:
   - Input quantity → Calculate total cost
   - Check if player has enough coins
   - Deduct coins, add baits to bait inventory (unlimited storage)

3. **Selling Rods** (from Inventory):
   - Click rod in inventory → Modal with "Sell" button
   - Gain coins (50% of buy price)
   - Cannot sell Bamboo Rod

---

## 🏆 Leaderboard System

### Leaderboard Criteria
- **Metric**: Total Fish Caught (lifetime)
- **Display**: Top 10 players sorted by total fish count

### Leaderboard UI
- Screen shows:
  - Rank # | Player Name | Total Fish Caught
  - Highlight current player's rank (if in top 10)
  - If not in top 10: Show "Your rank: #XX" at bottom

### Data Storage
- All player accounts stored in `players.ser` file
- Leaderboard reads all player data and sorts by totalFishCaught
- Updates on player save/logout

---

## 🎨 UI/UX Design

### Screen Flow

```
Welcome Screen (Login/Register)
    ↓
Dashboard (Main Hub)
    ↓
├── Fishing Screen (Cast line, timing mini-game)
├── Inventory Screen (View/sell fish, equip rods, sort)
├── Logbook Screen (Stats and best catches)
├── Shop Screen (Buy rods and baits)
├── Daily Rewards Screen (Claim rewards with cooldown)
├── Leaderboard Screen (Top 10 players)
├── About Screen (App info and how to play)
└── Credits Screen (Team members + references)
```

### Dashboard Layout
- **Top Bar**: 
  - Player name | Coins: XXX 💰 | XP: XXX ⭐ | Baits: XX 🪱
- **Center**: 
  - Large "GO FISHING" button with fishing rod icon
- **Bottom Navigation**: 
  - [Inventory] [Shop] [Daily Reward] [Logbook] [Leaderboard] [About] [Logout]

### Fishing Screen
- **Background**: Serene lake/ocean scene (side view)
- **Animated Elements**:
  - Fisherman sprite: Idle → Casting → Fishing → Catching animations
  - Water ripples (simple JavaFX Circle fade animation)
- **UI Elements**:
  - Top: Current bait selector dropdown
  - Center: "Cast Line" button (large, prominent)
  - Timing bar appears after casting (horizontal bar with moving indicator)
  - Caught fish popup: Fish sprite + rarity glow + "Caught [Fish Name]!" + rewards

### Inventory Screen
- **Grid Layout**: 5 columns × 3 rows = 15 slots
- **Slot Design**: 
  - Rounded rectangle (JavaFX CSS: `-fx-background-radius: 10px`)
  - Border color based on item type (Fish: rarity color, Rod: brown, Empty: gray)
  - Item sprite centered in slot
  - Quantity badge (bottom-right corner) for stackable items
- **Actions**:
  - Click slot → Modal with item details
  - Drag slot → Swap with another slot
  - "Sort Inventory" button (top-right)

### Item Detail Modal (on click)
- Semi-transparent overlay (darken background)
- Centered modal box:
  - Large item sprite with rarity glow
  - Item name (bold, large font)
  - Description text
  - Stats: Sell value, XP gain, etc.
  - Action buttons: "Sell" | "Equip" (rods) | "Close"

### Shop Screen
- **Two Sections**: Tabs or split view
  - **Rod Shop**: 2×2 grid of rod cards
  - **Bait Shop**: 2×2 grid of bait cards
- **Card Design**: 
  - Background: Rounded rectangle with subtle shadow
  - Item sprite at top
  - Name and description
  - Price and stats
  - "Buy" button (green if affordable, gray if not)

### Daily Reward Screen
- **Center**: Large animated chest sprite
- **Timer Display**: "Next reward in: 00:00:05" (countdown)
- **Claim Button**: 
  - Enabled when timer = 0
  - Disabled with timer display when on cooldown
- **Reward Animation**: 
  - Chest opens (sprite change or Timeline animation)
  - Reward items fly out (TranslateTransition)
  - Text display: "You received: 30 XP + 10 Baits!"

### Logbook Screen
- **Stats Panel**: Clean list format
  - "Total Fish Caught: XXX"
  - "Total Money Earned: XXX coins"
  - "Total XP Earned: XXX"
  - "Best Catch: Legendary Marlin (250 coins)"
- **Collection Progress**: 
  - Grid showing all 15 fish variants (5 species × 3 rarities)
  - Grayed out if not yet caught
  - Colored with rarity glow if caught

### Login/Register Screen
- **Background**: Peaceful fishing dock/lakeside with sunrise
- **Animated Clouds**: PNG clouds moving slowly across sky (TranslateTransition loop)
- **Center Panel**: 
  - App logo/title: "Fishing Adventure" 🎣
  - Input fields: Username | Password
  - Buttons: [Login] [Register]
  - Error messages (red text): "Invalid credentials" or "Username taken"

---

## 🎵 Audio & Visual Effects

### Sound Effects (Extra Points)
- **Fishing**: 
  - Water splash on cast
  - Reeling sound during timing bar
  - Success chime on catch
  - Failure/splash on miss
- **UI**:
  - Coin clink on purchase/sell
  - Chest opening sound
  - Button click sounds
- **Use**: JavaFX MediaPlayer or AudioClip

### Visual Animations
- **Fisherman Sprite**: Use Timeline to cycle through spritesheet frames
  - Idle (loop): 4 frames at 200ms each
  - Casting: 3 frames at 150ms each
  - Fishing: 6 frames at 100ms each (loop)
  - Catching: 4 frames at 200ms each
- **Timing Bar**: 
  - TranslateTransition for moving indicator (2-second duration, loop indefinitely)
  - Green rectangle for sweet spot (static)
- **Fish Popup**: 
  - FadeTransition (fade in 0.5s)
  - ScaleTransition (pop effect 0.3s)
  - PauseTransition (hold 2s)
  - FadeTransition (fade out 0.5s)
- **Water Ripples**: 
  - Circle with StrokeTransition (expand outward)
  - FadeTransition (fade out as expands)
  - Loop infinitely at random intervals

---

## 💾 Data Persistence

### File Structure
```
/data
  ├── players.ser          # All player accounts (for leaderboard)
  └── current_player.ser   # Currently logged-in player data
```

### Serialization Strategy
- **Player class implements Serializable**
- **Save on**:
  - Manual save (from pause menu or dashboard)
  - Logout button
  - App exit (with confirmation modal)

### Save Confirmation Modal
- Triggered on "Exit" or "Logout"
- Modal popup:
  - "Would you like to save your progress?"
  - Buttons: [Yes] [No] [Cancel]
  - "Yes": Save data → Exit/Logout
  - "No": Exit/Logout without saving
  - "Cancel": Return to game

### Data Manager Class
```java
public class DataManager {
    public static void savePlayer(Player player);
    public static Player loadPlayer(String username);
    public static List<Player> loadAllPlayers(); // For leaderboard
    public static void saveAllPlayers(List<Player> players);
}
```

### .ser File Explanation
- `.ser` = Serialized object file (binary format)
- Java's native serialization: ObjectOutputStream / ObjectInputStream
- Example:
```java
// Saving
FileOutputStream fileOut = new FileOutputStream("player.ser");
ObjectOutputStream out = new ObjectOutputStream(fileOut);
out.writeObject(playerObject);
out.close();

// Loading
FileInputStream fileIn = new FileInputStream("player.ser");
ObjectInputStream in = new ObjectInputStream(fileIn);
Player player = (Player) in.readObject();
in.close();
```

---

## 🏗️ Class Architecture

### Core Classes

#### 1. Player Class
```java
public class Player implements Serializable {
    private String username;
    private String password; // Hashed
    private int coins;
    private int xp;
    
    private Inventory inventory; // 15 slots
    private BaitInventory baitInventory; // Unlimited
    private Rod equippedRod;
    
    private FishingStats stats;
    private long lastRewardClaim; // Timestamp for cooldown
    
    // Methods
    public boolean purchaseRod(Rod rod);
    public boolean purchaseBait(Bait bait, int quantity);
    public void sellFish(Fish fish);
    public void sellRod(Rod rod);
    public void catchFish(Fish fish);
    public void equipRod(Rod rod);
    public Reward claimDailyReward();
}
```

#### 2. Inventory Class
```java
public class Inventory implements Serializable {
    private Item[] slots; // Fixed size: 15
    private int currentSize;
    
    public boolean addItem(Item item);
    public void removeItem(int slotIndex);
    public void swapItems(int index1, int index2);
    public void sortInventory(); // Rods → Fish (by rarity) → Empty
    public boolean isFull();
}
```

#### 3. Item (Abstract) & Subclasses
```java
public abstract class Item implements Serializable {
    protected String name;
    protected String description;
    protected String spritePath;
    
    public abstract String getType(); // "Fish", "Rod", "Bait"
}

public class Fish extends Item {
    private FishSpecies species; // SALMON, BASS, TUNA, MARLIN, KOI
    private Rarity rarity; // COMMON, RARE, LEGENDARY
    private int coinValue;
    private int xpValue;
    
    public int getSellValue() { return coinValue; }
    public int getXPValue() { return xpValue; }
}

public class Rod extends Item {
    private int tier; // 1-4
    private double rarityMultiplier; // 1.0, 1.1, 1.2, 1.35
    private int sweetSpotSize; // 15, 20, 25, 30 (percentage)
    private int buyCostCoins;
    private int buyCostXP;
    private int sellValue;
    
    public boolean canBeSold() { return tier > 1; } // Bamboo cannot be sold
}

public class Bait extends Item {
    private BaitType type; // BASIC, ENHANCED, RARE, LEGENDARY
    private int pricePerUnit;
    private double[] catchProbabilities; // [common%, rare%, legendary%]
    private int quantity;
}
```

#### 4. FishingStats Class
```java
public class FishingStats implements Serializable {
    private int totalFishCaught;
    private int totalMoneyEarned;
    private int totalXPEarned;
    
    private Fish bestCatchOverall; // Most expensive fish ever
    private Map<FishSpecies, Rarity> bestCatchPerSpecies;
    
    // Collection tracking
    private boolean[][] caughtFish; // 5 species × 3 rarities = 15 unique fish
    
    public void recordCatch(Fish fish);
    public int getCollectionProgress(); // Returns count of unique fish caught
}
```

#### 5. FishingGame Class
```java
public class FishingGame {
    private Player player;
    private Rod currentRod;
    private Bait selectedBait;
    
    public Fish performFishing(boolean hitSweetSpot) {
        if (!hitSweetSpot) return null; // Bait consumed, no catch
        
        // Calculate probabilities: bait base + rod multiplier
        double[] finalProbs = applyRodMultiplier(
            selectedBait.getCatchProbabilities(), 
            currentRod.getRarityMultiplier()
        );
        
        // Randomly select rarity based on probabilities
        Rarity rarity = rollRarity(finalProbs);
        
        // Randomly select fish species
        FishSpecies species = rollRandomSpecies();
        
        return new Fish(species, rarity);
    }
    
    private Rarity rollRarity(double[] probabilities);
    private FishSpecies rollRandomSpecies();
}
```

#### 6. Reward Class
```java
public class Reward implements Serializable {
    private int coins;
    private int xp;
    private List<Item> items; // Baits or bonus fish
    
    public static Reward generateRandomReward() {
        double roll = Math.random();
        if (roll < 0.6) return generateCommonReward();
        else if (roll < 0.9) return generateRareReward();
        else return generateLegendaryReward();
    }
}
```

#### 7. DataManager Class
```java
public class DataManager {
    private static final String PLAYERS_FILE = "data/players.ser";
    
    public static void savePlayer(Player player) throws IOException;
    public static Player loadPlayer(String username) throws IOException;
    public static List<Player> loadAllPlayers() throws IOException;
    public static boolean playerExists(String username);
}
```

### Enums
```java
public enum FishSpecies { SALMON, BASS, TUNA, MARLIN, KOI }
public enum Rarity { COMMON, RARE, LEGENDARY }
public enum BaitType { BASIC_WORM, ENHANCED_BAIT, RARE_LURE, LEGENDARY_BAIT }
```

---

## 📱 JavaFX Scenes & Controllers

### Scene Structure
```
MainApp.java (Application entry point)
  ├── WelcomeScene.fxml + WelcomeController.java
  ├── DashboardScene.fxml + DashboardController.java
  ├── FishingScene.fxml + FishingController.java
  ├── InventoryScene.fxml + InventoryController.java
  ├── ShopScene.fxml + ShopController.java
  ├── DailyRewardScene.fxml + DailyRewardController.java
  ├── LogbookScene.fxml + LogbookController.java
  ├── LeaderboardScene.fxml + LeaderboardController.java
  ├── AboutScene.fxml + AboutController.java
  └── CreditsScene.fxml + CreditsController.java
```

### Navigation Strategy
- **SceneManager** singleton class to handle scene switching
- Pass Player object between controllers via SceneManager

```java
public class SceneManager {
    private static Stage primaryStage;
    private static Player currentPlayer;
    
    public static void switchScene(String sceneName);
    public static void setPlayer(Player player);
    public static Player getPlayer();
}
```

---

## 🎯 Game Balance & Difficulty

### Progression Curve (10-20 min demo)
**Minute 0-5**: 
- Start with 20 Basic Worms, 50 coins, 0 XP
- Fish with Bamboo Rod → Catch mostly common fish
- Sell fish → Earn ~100-150 coins and ~50-80 XP

**Minute 5-10**:
- Buy Wooden Rod (200 coins + 100 XP) ✓
- Buy Enhanced Baits (10 coins each)
- Catch more rare fish → Sell for 200-300 coins

**Minute 10-15**:
- Buy Steel Rod (800 coins + 300 XP) ✓
- Buy Rare Lures (25 coins each)
- Start catching legendary fish consistently

**Minute 15-20**:
- Buy Master Rod (2500 coins + 800 XP) ✓
- Use Legendary Baits (60 coins each)
- Complete collection of all 15 unique fish
- Demonstrate leaderboard with multiple accounts

### Anti-Exploit Measures
1. **Limited Inventory (15 slots)**: Forces selling, prevents hoarding
2. **XP as Currency**: Cannot infinitely accumulate; must balance spending
3. **Bait Cost**: High-tier baits are expensive (60 coins for Legendary)
4. **Rod Costs Scale Exponentially**: 200 → 800 → 2500 coins
5. **Timing Mechanic**: Skill-based, not pure RNG (misses = wasted bait)
6. **No "Sell All" Button**: Deliberate action required to sell

---

## 📦 Assets Checklist

### ✅ Assets You Have
- [x] Fish spritesheets (5 species)
- [x] Chest sprite (for daily rewards)
- [x] Bait sprites (4 types)
- [x] Coin sprite
- [x] Fisherman spritesheet (idle + fishing animation)

### ❌ Assets You Need
- [ ] **Backgrounds**:
  - [ ] Login/Register screen (peaceful lakeside/dock with sunrise)
  - [ ] Fishing game screen (side view of lake/ocean)
  - [ ] Dashboard background (subtle water texture or solid color)
- [ ] **UI Elements**:
  - [ ] Cloud PNGs (2-3 variations for animated background)
  - [ ] Rod sprites/icons (4 rods) - *OR use simple icon + text*
  - [ ] XP icon/badge
  - [ ] Button backgrounds (rounded rectangles can be CSS)
- [ ] **Optional**:
  - [ ] Sound effects (water splash, coins, chest opening)
  - [ ] Inventory slot background texture

### Asset Search Suggestions
- **Keywords**: "pixel art fishing background", "cartoon lake scene", "anime fishing dock", "8-bit clouds PNG"
- **Free Resources**: 
  - OpenGameArt.org
  - Itch.io (free game assets)
  - Kenney.nl (free game assets)
  - Freepik (with attribution)

---

## ⏱️ Development Timeline

### Week 11 (Nov 10-14): Milestone 1 - 20% Complete ✅
**Goals**: Login system, basic Player class, data persistence

**Tasks**:
- [x] Welcome screen UI (login/register with animated GIF)
- [x] Player class with username, password, coins, XP, inventories
- [x] DataManager for serialization (save/load players)
- [x] Dashboard with player stats display
- [x] All model classes (Item hierarchy, Inventory, BaitInventory, FishingStats)
- [x] Enums (FishSpecies, Rarity, BaitType)
- [x] Save/logout confirmation system
- [x] Test: Create account → Save → Load on next run

**Deliverables**: ✅ Working login/register system with data persistence

**Files Created**:
- `MainApp.java`, `SceneManager.java`
- `WelcomeController.java`, `DashboardController.java`
- `Player.java`, `Inventory.java`, `BaitInventory.java`, `FishingStats.java`
- `Item.java`, `Fish.java`, `Rod.java`, `Bait.java`
- `FishSpecies.java`, `Rarity.java`, `BaitType.java`
- `DataManager.java`
- `welcome.fxml`, `dashboard.fxml`, `style.css`

---

### Week 12 (Nov 17-21): Milestone 2 - 50% Complete
**Goals**: Core fishing mechanic, inventory system, shop basics

**Tasks**:
- [ ] **Fishing Screen**:
  - [ ] Implement timing mini-game (horizontal bar + sweet spot)
  - [ ] FishingGame class with probability calculations
  - [ ] Fish generation based on bait + rod
  - [ ] Fisherman animation (spritesheet Timeline)
  - [ ] Caught fish popup with animation
- [ ] **Inventory System**:
  - [ ] 15-slot grid UI (rounded rectangles)
  - [ ] Item detail modal on click
  - [ ] Sell fish functionality
  - [ ] Manual drag-and-drop sorting
  - [ ] Auto-sort button
- [ ] **Shop Screen** (basic):
  - [ ] Display rods and baits
  - [ ] Buy baits functionality
  - [ ] Buy rods functionality (with XP cost)
- [ ] **Classes**: Item hierarchy (Fish, Rod, Bait), Inventory class

**Deliverables**: Playable fishing loop (fish → inventory → sell → buy baits/rods)

---

### Week 13 (Nov 24-28): Milestone 3 - 80% Complete
**Goals**: Polish all features, add rewards, logbook, leaderboard

**Tasks**:
- [ ] **Daily Rewards**:
  - [ ] Reward screen with chest animation
  - [ ] Cooldown timer (5 seconds for demo)
  - [ ] Random reward generation
- [ ] **Logbook**:
  - [ ] FishingStats tracking
  - [ ] Logbook UI with stats display
  - [ ] Best catch tracking
  - [ ] Collection progress (X/15 fish)
- [ ] **Leaderboard**:
  - [ ] Load all players from players.ser
  - [ ] Sort by total fish caught
  - [ ] Display top 10
- [ ] **Sound Effects**:
  - [ ] Add fishing, coin, chest sounds
  - [ ] Toggle mute button
- [ ] **UI Polish**:
  - [ ] Add rarity glow effects (CSS)
  - [ ] Smooth transitions between screens
  - [ ] Consistent styling (colors, fonts)
  - [ ] About and Credits screens
- [ ] **Testing**:
  - [ ] Test full game loop (20-minute playthrough)
  - [ ] Balance tuning
