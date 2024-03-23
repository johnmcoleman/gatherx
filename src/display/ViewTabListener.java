package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import controller.GatherX;

/**
 * Detects actions on the view tab and allows the user to perform the listed action when clicking a
 * button.
 * 
 * @author John Coleman
 *
 */
public class ViewTabListener implements ActionListener {
  private static ArrayList<JButton> viewTabBtns = new ArrayList<JButton>();
  private String btnText = "";

  @Override
  public void actionPerformed(ActionEvent e) {
    viewTabBtns = GatherX.getUiRegulator().getGraphicInterface().getViewBtns();
    // sets which one of the view options is selected
    for (JButton button : viewTabBtns) {
      if (e.getSource().equals(button)) {
        btnText = button.getText();
      }
    }
    chooseProperty(e, btnText);
    GatherX.refreshInterface();
  }

  /**
   * Passes properties along to the user interface regulator based on which button was clicked.
   * 
   * @param e the even which triggered the view tab listener
   * @param btnText the text on the button which was clicked
   */
  private void chooseProperty(ActionEvent e, String btnText) {
    // sets the variable to be displayed using the view option
    String command = e.getActionCommand();
    if (!btnText.contentEquals("")) {
      switch (btnText) {
        case "<html><center>Display Node<br />Properties</center></html>":
          GatherX.getUiRegulator().setDisplayNodeProperty(command);
          break;
        case "<html><center>Display Pipe<br />Properties</center></html>":
          GatherX.getUiRegulator().setDisplayPipeProperty(command);
          break;
        case "<html><center>List Node<br />Properties</center></html>":
          GatherX.getUiRegulator().setListNodeProperty(command);
          break;
        case "<html><center>List Pipe<br />Properties</center></html>":
          GatherX.getUiRegulator().setListPipeProperty(command);
          break;
        default:
      }
    }
  }
}
