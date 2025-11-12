package fishing.model;
//Inventory.java


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* Manages player's main inventory with fixed slot capacity.
* Stores Fish and Rod items.
*/
public class Inventory implements Serializable {
 private static final long serialVersionUID = 1L;
 
 private Item[] slots;
 private int capacity;
 
 public Inventory(int capacity) {
     this.capacity = capacity;
     this.slots = new Item[capacity];
 }
 
 /**
  * Add item to first available slot
  */
 public boolean addItem(Item item) {
     for (int i = 0; i < capacity; i++) {
         if (slots[i] == null) {
             slots[i] = item;
             return true;
         }
     }
     return false; // Inventory full
 }
 
 /**
  * Remove specific item from inventory
  */
 public boolean removeItem(Item item) {
     for (int i = 0; i < capacity; i++) {
         if (slots[i] == item) {
             slots[i] = null;
             return true;
         }
     }
     return false;
 }
 
 /**
  * Remove item at specific slot index
  */
 public void removeItemAt(int slotIndex) {
     if (slotIndex >= 0 && slotIndex < capacity) {
         slots[slotIndex] = null;
     }
 }
 
 /**
  * Get item at specific slot
  */
 public Item getItemAt(int slotIndex) {
     if (slotIndex >= 0 && slotIndex < capacity) {
         return slots[slotIndex];
     }
     return null;
 }
 
 /**
  * Swap items between two slots
  */
 public void swapItems(int index1, int index2) {
     if (index1 >= 0 && index1 < capacity && index2 >= 0 && index2 < capacity) {
         Item temp = slots[index1];
         slots[index1] = slots[index2];
         slots[index2] = temp;
     }
 }
 
 /**
  * Check if inventory is full
  */
 public boolean isFull() {
     for (Item slot : slots) {
         if (slot == null) {
             return false;
         }
     }
     return true;
 }
 
 /**
  * Get number of items in inventory
  */
 public int getCurrentSize() {
     int count = 0;
     for (Item slot : slots) {
         if (slot != null) {
             count++;
         }
     }
     return count;
 }
 
 /**
  * Get all items (including nulls for empty slots)
  */
 public Item[] getSlots() {
     return slots;
 }
 
 /**
  * Get all non-null items
  */
 public List<Item> getItems() {
     List<Item> items = new ArrayList<>();
     for (Item item : slots) {
         if (item != null) {
             items.add(item);
         }
     }
     return items;
 }
 
 /**
  * Sort inventory: Rods first (by tier), then Fish (by rarity), then empty slots
  */
 public void sortInventory() {
     List<Item> items = getItems();
     
     // Sort items
     items.sort((item1, item2) -> {
         // Rods before Fish
         if (item1 instanceof Rod && item2 instanceof Fish) return -1;
         if (item1 instanceof Fish && item2 instanceof Rod) return 1;
         
         // Sort Rods by tier
         if (item1 instanceof Rod && item2 instanceof Rod) {
             return Integer.compare(((Rod) item1).getTier(), ((Rod) item2).getTier());
         }
         
         // Sort Fish by rarity (Legendary > Rare > Common)
         if (item1 instanceof Fish && item2 instanceof Fish) {
             return Integer.compare(
                 ((Fish) item2).getRarity().ordinal(),
                 ((Fish) item1).getRarity().ordinal()
             );
         }
         
         return 0;
     });
     
     // Clear slots and re-add sorted items
     Arrays.fill(slots, null);
     for (int i = 0; i < items.size(); i++) {
         slots[i] = items.get(i);
     }
 }
 
 public int getCapacity() {
     return capacity;
 }
}