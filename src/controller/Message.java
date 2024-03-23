package controller;

import display.ErrorFrame;

/**
 * Contains a message which consists of an action and associated string data from the user interface
 * to the back end model.
 * 
 * @author John Coleman
 *
 */
public class Message {
  private Action action;
  private String data;
  private String[] catagorizedData;

  /**
   * Default Message constructor for when there are no user specified parameters which must have
   * their text filtered.
   * 
   * @param action the action the user has specified.
   * @param data the necessary information to execute the action.
   */
  public Message(Action action, String data) {
    this.action = action;
    this.data = data;
    this.catagorizedData = data.split(":");
  }

  /**
   * Message constructor for when the specified parameters may need their text filtered for special
   * symbols used to delineate value separation.
   * 
   * @param action the action the user has specified.
   * @param catData the data segmented into substrings on the assumption that there are no special
   *        symbols used for separation of values which the user has entered.
   */
  public Message(Action action, String[] catData) {
    this.action = action;
    this.catagorizedData = catData;
    this.data = "";
    for (int index = 0; index < catData.length; index++) {
      this.data = this.data + catData[index];
    }
  }

  public Action getAction() {
    return action;
  }

  public String getData() {
    return data;
  }

  public String[] getCatagorizedData() {
    return catagorizedData;
  }

  /**
   * Send message information to the display package.
   * 
   * @param str message information
   */
  public void reply(String str) {
    GatherX.getUiRegulator().setCurrentReply(str);
  }

  /**
   * Display an issue with the execution of a message on the display.
   * 
   * @param currentReply the reply from the message dispatcher to the display package.
   * @param error if there was an exception executing the message from the display.
   */
  public void reply(String currentReply, boolean error) {
    if (!error) {
      ErrorFrame exceptionFrame = new ErrorFrame(currentReply);
    } else {
      String[] warnings = currentReply.split(":");
      for (String warning : warnings) {
        System.out.println(warning);
      }
    }
  }
}
