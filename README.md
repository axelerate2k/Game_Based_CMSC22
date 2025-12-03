# 🎣 Fishda - Pixel Art Fishing Adventure

A relaxing pixel art fishing game built with JavaFX where you catch various fish, upgrade your fishing equipment, and build your collection. Experience the joy of fishing in a charming retro-style world with simple mechanics and rewarding gameplay.
<img width="1909" height="1116" alt="image" src="https://github.com/user-attachments/assets/26ae576d-151c-4985-938a-2290b78b727b" />
<img width="1913" height="1115" alt="image" src="https://github.com/user-attachments/assets/d5e42d36-78d9-40b5-aa2d-7b2f9aad8fa8" />
<img width="1594" height="928" alt="image" src="https://github.com/user-attachments/assets/0e7e5a11-d928-43ee-a2b9-5eabdb097621" />




---

## 📋 Table of Contents

- [Features](#-features)
- [Requirements](#-requirements)
- [Installation & Setup](#-installation--setup)
- [How to Run](#-how-to-run)
- [How to Play](#-how-to-play)
- [Game Mechanics](#-game-mechanics)
- [Development Team](#-development-team)
- [Credits](#-credits)
- [License](#-license)

---

## ✨ Features

### 🎮 Core Gameplay
- **Interactive Fishing System**: Click on the fisherman sprite to cast your line and catch fish
- **Multiple Fish Rarities**: Catch Common, Rare, and Legendary fish with different probabilities
- **5 Unique Fish Species**: Anglerfish, Red Salmon, Swordfish, Oarfish, and Great White Shark
- **Animated Fisherman**: Smooth idle and fishing animations for immersive gameplay

### 🎒 Inventory Management
- **15-Slot Inventory System**: Organize your items with a visual grid-based inventory
- **Drag-and-Drop Support**: Easily rearrange items by dragging them between slots
- **Item Stacking**: Baits and fish automatically stack in the same slot
- **Visual Rarity Indicators**: Color-coded borders for Common (Gray), Rare (Blue), and Legendary (Gold) items
- **Quantity Labels**: See how many of each stackable item you have at a glance

### 🛒 Shop System
- **Fishing Rods Shop**: Purchase 4 different rods with increasing catch rates
  - Bamboo Rod (Starter - FREE)
  - Wooden Rod (200 coins, 100 XP)
  - Steel Rod (800 coins, 300 XP)
  - Master Rod (2500 coins, 800 XP)
  
- **Bait Shop**: Buy various baits with different catch probabilities
  - Basic Worm (3 coins each)
  - Enhanced Bait (10 coins each)
  - Rare Lure (25 coins each)
  - Master Bait (60 coins each)
  
- **Quantity Selector**: Choose how many baits to purchase at once

### 🎁 Daily Rewards System
- **24-Hour Cooldown**: Claim free rewards once per day
- **Random Rewards**: Get coins, XP, baits, or even legendary fish
- **Rarity-Based Rewards**:
  - Common (60%): 30 XP, 10 Basic Worms, or 20 coins
  - Rare (30%): 50 XP, 5 Enhanced Baits, or 50 coins
  - Legendary (10%): 100 XP, 3 Rare Lures, 100 coins, or Legendary Fish
- **Live Countdown Timer**: See exactly when your next reward is available

### 📊 Progression System
- **Level System**: Progress from Level 1 to Level 30
- **XP Bar**: Visual progress bar showing advancement to next level
- **Stat Tracking**: Monitor your coins, XP, and total baits
- **Player Statistics**: Track fish caught, money earned, and XP gained

### 🎨 User Interface
- **Login & Registration**: Secure account system with username and password
- **Splash Screen**: Polished intro with fade animations
- **Responsive Dashboard**: Clean, intuitive pixel art themed interface
- **Modal Windows**: Item details, sell confirmations, and reward displays
- **Side Panel Navigation**: Easy access to Bag, Shop, Daily Rewards, and About screens

### 💰 Economy System
- **Sell Fish**: Convert your catches into coins and XP
- **Sell Rods**: Get partial refund for unused fishing rods
- **Currency Management**: Earn coins through fishing and selling
- **XP Progression**: Level up by fishing and selling items

---

## 🔧 Requirements

- **Java Development Kit (JDK)**: Version 11 or higher
- **JavaFX SDK**: Version 11 or higher
- **IDE** (Recommended): IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **Operating System**: Windows, macOS, or Linux

---

## 📥 Installation & Setup

### 1. Clone or Download the Repository

```bash
git clone https://github.com/axelerate2k/Game_Based_CMSC22.git
cd Game_Based_CMSC22
```
**Or download the ZIP file and paste into Java Project

### 2. Install JavaFX

#### Option A: Download JavaFX SDK
1. Download JavaFX SDK from [openjfx.io](https://openjfx.io/)
2. Extract to a location on your computer (e.g., `C:\javafx-sdk-21`)

#### Option B: Use Maven/Gradle
Add JavaFX dependencies to your build file (if using Maven/Gradle)

### 3. Configure Your IDE

#### For IntelliJ IDEA:
1. Open the project in IntelliJ IDEA
2. Go to **File → Project Structure → Libraries**
3. Click **+** and add all `.jar` files from `javafx-sdk/lib`
4. Go to **Run → Edit Configurations**
5. Add VM options:
   ```
   --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
   ```

#### For Eclipse:
1. Right-click project → **Build Path → Configure Build Path**
2. Add External JARs from JavaFX SDK `lib` folder
3. In Run Configurations, add VM arguments:
   ```
   --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
   ```

#### For VS Code:
1. Install Java Extension Pack
2. Configure `launch.json` with JavaFX VM arguments
3. Ensure JavaFX libraries are in classpath

---

## 🚀 How to Run

### Method 1: Run from IDE

1. Open the project in your IDE
2. Navigate to `src/main/Main.java`
3. Right-click on the file and select **Run 'Main.main()'**
4. The game window should launch

### Method 2: Run from Command Line

```bash
# Navigate to the project directory
cd fishda-game

# Compile (adjust paths to your JavaFX installation)
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls -d bin src/main/*.java src/model/*.java src/view/*.java src/data/*.java src/utils/*.java

# Run
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls -cp bin main.Main
```

### Method 3: Create Executable JAR

1. Build a JAR file with your IDE or build tool
2. Include JavaFX runtime in the JAR or use jlink to create a custom runtime
3. Run with:
   ```bash
   java -jar fishda.jar
   ```

---

## 🎮 How to Play

### Getting Started

1. **Launch the Game**: Run the application and wait for the splash screen
2. **Login/Register**: Create a new account or log in with existing credentials
   - Username must be at least 3 characters
   - Password must be at least 4 characters
3. **Dashboard**: You'll arrive at the main fishing dock

### Basic Gameplay Loop

1. **Select Bait**: 
   - Click the **👜 Bag** button to open your inventory
   - Click on any bait to select it (it will show a green border)
   - You start with 10 Basic Worms

2. **Go Fishing**:
   - Click directly on the **fisherman sprite** to cast your line
   - Watch the fishing animation play
   - See what you caught in the notification

3. **Manage Inventory**:
   - Open your **Bag** to view all items
   - Click on fish or rods to see details and sell them
   - Drag and drop items to rearrange your inventory
   - Stack similar items together

4. **Shop for Upgrades**:
   - Click **🏪 Shop** to open the shop
   - Switch between **Rods** and **Baits** tabs
   - Purchase better equipment to catch rarer fish
   - Buy more baits when you run out

5. **Claim Daily Rewards**:
   - Click **🎁 Daily** to check your daily reward
   - Claim free rewards every 24 hours
   - Get coins, XP, baits, or legendary fish

6. **Level Up**:
   - Earn XP by catching and selling fish
   - Watch your level progress in the top bar
   - Reach Level 30 for maximum progression

### Tips & Strategies

- 💡 **Start Simple**: Use Basic Worms to catch common fish and build up coins
- 💡 **Upgrade Gradually**: Buy the Wooden Rod first for better catch rates
- 💡 **Save for Master Equipment**: The Master Rod and Master Bait give best results
- 💡 **Sell Strategically**: Keep legendary fish or sell them for maximum profit
- 💡 **Daily Rewards**: Log in every day for free resources
- 💡 **Organize Inventory**: Keep baits at the top, fish at the bottom

---

## 🎯 Game Mechanics

### Fishing System

#### Catch Probabilities

The probability of catching each rarity depends on your **Bait** and **Rod**:

**Base Probabilities by Bait:**
- Basic Worm: 60% Common, 30% Rare, 10% Legendary
- Enhanced Bait: 40% Common, 40% Rare, 20% Legendary
- Rare Lure: 20% Common, 50% Rare, 30% Legendary
- Master Bait: 5% Common, 45% Rare, 50% Legendary

**Rod Bonuses:**
- Bamboo Rod: No bonus
- Wooden Rod: +10% to Rare and Legendary
- Steel Rod: +15% to Rare and Legendary
- Master Rod: +20% to Rare and Legendary

### Economy & Pricing

#### Selling Prices

Fish sell for different amounts based on rarity:
- **Common Fish**: ~10-20 coins, ~5-10 XP
- **Rare Fish**: ~30-50 coins, ~15-25 XP
- **Legendary Fish**: ~80-120 coins, ~40-60 XP

Rods can be sold for 50% of their purchase price (except Bamboo Rod).

### Level Progression

- **Level Formula**: Level = (Total XP ÷ 50) + 1
- **Max Level**: 30
- **XP per Level**: 50 XP required for each level
- **Benefits**: Higher levels unlock satisfaction and bragging rights!

### Inventory System

- **Total Slots**: 15 slots
- **Stackable Items**: Baits and Fish (with same rarity)
- **Unique Items**: Rods (one per slot)
- **Starting Items**: 1 Bamboo Rod, 10 Basic Worms

---

## 👥 Development Team

This game was developed by:

- **Brylle B. Pamulaklakin**
- **Frendzo Charles Pelagio**
- **Lance Axel B. Gasmen**
- **Szhan Wayne S. Timosan**

**Course**: Object-Oriented Programming  
**Institution**: University of the Philippines Los Banos 
**Year**: 2025

---

## 🎨 Credits

### Art Assets

**Fishing Icons (Fish, Rods, Baits)**  
Created by: HappyPotato100  
Source: [https://happypotato100.itch.io/fishing-icon-pack](https://happypotato100.itch.io/fishing-icon-pack)

**Fisherman Animation & Background Tilesets**  
Created by: Free Game Assets  
Source: [https://free-game-assets.itch.io/free-fishing-pixel-art-pack](https://free-game-assets.itch.io/free-fishing-pixel-art-pack)

### Technologies Used

- **Java 11+**: Core programming language
- **JavaFX 11+**: GUI framework
- **CSS**: Custom styling for retro pixel art theme

---

## 📄 License

This project is created for educational purposes as part of an Object-Oriented Programming course.

All art assets are credited to their respective creators on itch.io. Please refer to the original asset pages for their specific licenses.

---

## 🐛 Known Issues

- Daily reward countdown is set to 10 seconds for testing (change `COOLDOWN_SECONDS` to 86400 for 24-hour production mode)
- Inventory may not refresh immediately after some actions (close and reopen panel if needed)

---

## 🔮 Future Enhancements

- [ ] Add sound effects and background music
- [ ] Implement fishing minigame with timing mechanics
- [ ] Add more fish species and equipment
- [ ] Create achievement system
- [ ] Add fish encyclopedia/collection log
- [ ] Implement leaderboards
- [ ] Add weather system affecting catch rates
- [ ] Create multiple fishing locations

---

## 📧 Contact

For questions, suggestions, or bug reports, please contact the development team through your course instructor.

---

**Happy Fishing! 🎣**

*May your lines be tight and your catches legendary!*
