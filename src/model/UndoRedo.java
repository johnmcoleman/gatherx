package model;

import java.util.Stack;


/**
 * This class holds the methods called in GatherX.java that undo and redo changes to gathering
 * systems. Stacks have a maximum limit of (limit). After the limit is passed, the stacks will
 * automatically remove extra data from the bottom to preserve space. The 'limit' variable can be
 * found in GatherX.java as the final int: 'UNDO_LIMIT'
 *
 * @author Nick Chen
 */
public class UndoRedo {
  private int limit;
  private Stack<GatheringSystem> memory;
  private Stack<GatheringSystem> redoMemory;
  private String systemName;

  /**
   * Constructs the UndoRedo Class.
   * 
   * 
   * @param limit how many changes this class keeps track of. systemName String of the gathering
   *        system's name, to which this undo/redo object corresponds
   */
  public UndoRedo(int limit, String systemName) {
    this.limit = limit;
    this.systemName = systemName;
    memory = new Stack<GatheringSystem>();
    redoMemory = new Stack<GatheringSystem>();
  }

  /**
   * If the memory stack has items, then return the last item and pop off stack, push it on the
   * redoMemory else, returns current GatheringSystem (as a backup) - undo and redo buttons (UI)
   * should be inactive if stacks are empty to reduce chance of error
   * 
   * @param currentSystem current gathering system in case the stack is empty (this is a terrible
   *        way to do this lol)
   * @return the new Gathering System after undo
   */
  public GatheringSystem undo(GatheringSystem currentSystem) {
    GatheringSystem toReturn = currentSystem;
    if (!memory.empty()) {
      toReturn = memory.pop();
      redoMemory.push(currentSystem);
      autoClear();
    }
    return toReturn;
  }

  /**
   * If the redoMemory stack has items, then return the last item and pop off stack, push it on the
   * undo memory else, returns current GatheringSystem (as a backup) - undo and redo buttons (UI)
   * should be inactive if stacks are empty to reduce chance of error
   * 
   * @param currentSystem current gathering system in case the stack is empty (this is a terrible
   *        way to do this lol)
   * @return the new Gathering System after redo
   */
  public GatheringSystem redo(GatheringSystem currentSystem) {
    GatheringSystem toReturn = currentSystem;
    if (!redoMemory.empty()) {
      toReturn = redoMemory.pop();
      memory.push(currentSystem);
      autoClear();
    }

    return toReturn;
  }

  /**
   * Clears the redoMemory stack and pushes a change to the undo memory stack
   * 
   * @param change the most recent copy of the current GatheringSystem to store
   */
  public void change(GatheringSystem change) {

    // **** need to implement a way to prevent the UndoRedo from adding changes if duplicate
    // if(memory.isEmpty() || memory.peek().equals(change)) {
    try {
      memory.push(change.clone());
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    redoMemory.clear();
    autoClear();
  }

  /**
   * Removes the bottom item of the undo memory stack if it is greater than limit
   */
  public void autoClear() {
    if (memory.size() > limit) {
      memory.remove(0);
    }
    if (redoMemory.size() > limit) {
      memory.remove(0);
    }
  }


  public void setSystemName(String systemName) {
    this.systemName = systemName;
  }

  public String getSystemName() {
    return this.systemName;
  }

  public boolean moreRedosAllowed() {
    return redoMemory.isEmpty();
  }

  public boolean moreUndosAllowed() {
    return memory.isEmpty();
  }

}
