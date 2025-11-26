package utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class SpriteLoader {
    
    /**
     * Extract a sprite from a spritesheet
     * @param spriteSheet The full spritesheet image
     * @param x X coordinate of top-left corner
     * @param y Y coordinate of top-left corner
     * @param width Width of the sprite
     * @param height Height of the sprite
     * @return Extracted sprite as Image
     */
    public static Image extractSprite(Image spriteSheet, int x, int y, int width, int height) {
        PixelReader reader = spriteSheet.getPixelReader();
        WritableImage sprite = new WritableImage(reader, x, y, width, height);
        return sprite;
    }
    
    /**
     * Extract multiple frames from a horizontal strip (for animations)
     * @param spriteSheet The full spritesheet
     * @param frameCount Number of frames
     * @param frameWidth Width of each frame
     * @param frameHeight Height of each frame
     * @param startY Y coordinate where frames start
     * @return Array of frame images
     */
    public static Image[] extractHorizontalFrames(Image spriteSheet, int frameCount, int frameWidth, int frameHeight, int startY) {
        Image[] frames = new Image[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = extractSprite(spriteSheet, i * frameWidth, startY, frameWidth, frameHeight);
        }
        return frames;
    }
    
    /**
     * Load fisherman idle animation frames
     * @return Array of 4 idle animation frames
     */
    public static Image[] loadFishermanIdle() {
        try {
            Image spriteSheet = new Image(SpriteLoader.class.getResourceAsStream("/sprites/Fisherman_idle.png"));
            return extractHorizontalFrames(spriteSheet, 4, 48, 48, 0);
        } catch (Exception e) {
            System.err.println("Could not load fisherman idle sprites: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Load fisherman fishing animation frames
     * @return Array of 4 fishing animation frames
     */
    public static Image[] loadFishermanFishing() {
        try {
            Image spriteSheet = new Image(SpriteLoader.class.getResourceAsStream("/sprites/Fisherman_fish.png"));
            return extractHorizontalFrames(spriteSheet, 4, 48, 48, 0);
        } catch (Exception e) {
            System.err.println("Could not load fisherman fishing sprites: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Load all fish sprites from fishes.png
     * @return Array of fish images [Anglerfish, Red Salmon, Swordfish, Oarfish, Great White Shark]
     */
    public static Image[] loadFishSprites() {
        try {
            Image spriteSheet = new Image(SpriteLoader.class.getResourceAsStream("/sprites/fishes.png"));
            Image[] fish = new Image[5];
            
            // Extract based on your coordinates
            fish[0] = extractSprite(spriteSheet, 192, 64, 32, 32);  // Anglerfish (Row 2, Col 6)
            fish[1] = extractSprite(spriteSheet, 32, 64, 32, 32);   // Red Salmon (Row 2, Col 1)
            fish[2] = extractSprite(spriteSheet, 224, 96, 32, 32);  // Swordfish (Row 3, Col 7)
            fish[3] = extractSprite(spriteSheet, 128, 160, 32, 32); // Oarfish (Row 5, Col 4)
            fish[4] = extractSprite(spriteSheet, 64, 192, 32, 32);  // Great White Shark (Row 6, Col 2)
            
            return fish;
        } catch (Exception e) {
            System.err.println("Could not load fish sprites: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Load all rod sprites from fishing_gear.png
     * @return Array of rod images [Bamboo, Wooden, Steel, Master]
     */
    public static Image[] loadRodSprites() {
        try {
            Image spriteSheet = new Image(SpriteLoader.class.getResourceAsStream("/sprites/fishing_gear.png"));
            Image[] rods = new Image[4];
            
            rods[0] = extractSprite(spriteSheet, 32, 0, 32, 32);  // Bamboo Rod (Row 0, Col 1)
            rods[1] = extractSprite(spriteSheet, 0, 0, 32, 32);   // Wooden Rod (Row 0, Col 0)
            rods[2] = extractSprite(spriteSheet, 64, 0, 32, 32);  // Steel Rod (Row 0, Col 2)
            rods[3] = extractSprite(spriteSheet, 96, 0, 32, 32);  // Master Rod (Row 0, Col 3)
            
            return rods;
        } catch (Exception e) {
            System.err.println("Could not load rod sprites: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Load all bait sprites from fishing_gear.png
     * @return Array of bait images [Basic Worm, Enhanced, Rare Lure, Master Bait]
     */
    public static Image[] loadBaitSprites() {
        try {
            Image spriteSheet = new Image(SpriteLoader.class.getResourceAsStream("/sprites/fishing_gear.png"));
            Image[] baits = new Image[4];
            
            baits[0] = extractSprite(spriteSheet, 64, 64, 32, 32);   // Basic Worm (Row 2, Col 2)
            baits[1] = extractSprite(spriteSheet, 96, 64, 32, 32);   // Enhanced Bait (Row 2, Col 3)
            baits[2] = extractSprite(spriteSheet, 128, 64, 32, 32);  // Rare Lure (Row 2, Col 4)
            baits[3] = extractSprite(spriteSheet, 160, 64, 32, 32);  // Master Bait (Row 2, Col 5)
            
            return baits;
        } catch (Exception e) {
            System.err.println("Could not load bait sprites: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Load coin and chest sprites from objects.png
     * @return Array [Chest, Coin]
     */
    public static Image[] loadObjectSprites() {
        try {
            Image spriteSheet = new Image(SpriteLoader.class.getResourceAsStream("/sprites/objects.png"));
            Image[] objects = new Image[2];
            
            objects[0] = extractSprite(spriteSheet, 32, 64, 32, 32);  // Chest (Row 2, Col 1)
            objects[1] = extractSprite(spriteSheet, 64, 64, 32, 32);  // Coin (Row 2, Col 2)
            
            return objects;
        } catch (Exception e) {
            System.err.println("Could not load object sprites: " + e.getMessage());
            return null;
        }
    }
}