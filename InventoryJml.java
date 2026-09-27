// A bounded stock record for a single warehouse item.
// Verify with:
//   openjml -esc --progress --show-summary InventoryJml.java
public final class InventoryJml {

    //@ public invariant 0 <= capacity;
    //@ public invariant 0 <= reorderPoint && reorderPoint <= capacity;
    //@ public invariant 0 <= available && available <= capacity;

    /*@ spec_public @*/ private final int capacity;
    /*@ spec_public @*/ private final int reorderPoint;
    /*@ spec_public @*/ private int available;

    /*@ public normal_behavior
      @   requires capacity >= 0;
      @   requires reorderPoint >= 0;
      @   requires reorderPoint <= capacity;
      @   requires initialAvailable >= 0;
      @   requires initialAvailable <= capacity;
      @   assignable \everything;
      @   ensures this.capacity == capacity;
      @   ensures this.reorderPoint == reorderPoint;
      @   ensures available == initialAvailable;
      @*/
    public InventoryJml(int capacity, int reorderPoint, int initialAvailable) {
        this.capacity = capacity;
        this.reorderPoint = reorderPoint;
        this.available = initialAvailable;
    }

    /*@ public normal_behavior
      @   ensures \result == available;
      @   assignable \nothing;
      @*/
    public /*@ pure @*/ int available() {
        return available;
    }

    /*@ public normal_behavior
      @   ensures \result == capacity - available;
      @   ensures 0 <= \result && \result <= capacity;
      @   assignable \nothing;
      @*/
    public /*@ pure @*/ int freeCapacity() {
        return capacity - available;
    }

    /*@ public normal_behavior
      @   ensures \result == (available <= reorderPoint);
      @   assignable \nothing;
      @*/
    public /*@ pure @*/ boolean needsRestock() {
        return available <= reorderPoint;
    }

    /*@ public normal_behavior
      @   requires quantity >= 0;
      @   requires quantity <= available;
      @   assignable available;
      @   ensures available == \old(available) - quantity;
      @   ensures freeCapacity() == \old(freeCapacity()) + quantity;
      @   ensures \old(needsRestock()) ==> needsRestock();
      @*/
    public void reserve(int quantity) {
        available -= quantity;
    }

    /*@ public normal_behavior
      @   requires quantity >= 0;
      @   requires quantity <= capacity - available;
      @   assignable available;
      @   ensures available == \old(available) + quantity;
      @   ensures freeCapacity() == \old(freeCapacity()) - quantity;
      @   ensures \old(!needsRestock()) ==> !needsRestock();
      @*/
    public void restock(int quantity) {
        available += quantity;
    }
}