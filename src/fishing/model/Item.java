package fishing.model;
import java.io.Serializable;

/**
 * Abstract base class for all items in the game
 */
public abstract class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String name;
    protected String description;
    protected String spritePath;
    
    public Item(String name, String description, String spritePath) {
        this.name = name;
        this.description = description;
        this.spritePath = spritePath;
    }
    
    public abstract String getType();
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getSpritePath() {
        return spritePath;
    }
    
    @Override
    public String toString() {
        return name;
    }
}