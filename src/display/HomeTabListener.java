package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import controller.GatherX;
import controller.MessageDispatch;

/**
 * Detects actions on the home tab and allows the user to perform the listed action when clicking a
 * button.
 * 
 * @author John Coleman
 *
 */
public class HomeTabListener implements ActionListener {
  private ArrayList<JButton> homeTabBtns = new ArrayList<JButton>();
  private String btnText = "";

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    homeTabBtns = GatherX.getUiRegulator().getGraphicInterface().getHomeBtns();
    MessageDispatch dispatcher = GatherX.getDispatcher();
    // sets which one of the view options is selected
    for (JButton button : homeTabBtns) {
      if (e.getSource().equals(button)) {
        btnText = button.getText();
      }
    }
    if (!btnText.contentEquals("")) {
      switch (btnText) {
        case "New Calculation":
          switch (command) {
            case "Pressure":
              GatherX.newCalculation(command);
              break;
            case "Efficiency":
              GatherX.newCalculation(command);
              break;
            default:
          }
          break;
        case "Undo":
          GatherX.undo();
          break;
        case "Redo":
          GatherX.redo();
          break;
        case "Text Size":
          if (!command.contentEquals("Text Size")) {
            if (Integer.valueOf(command) != null) {
              GatherX.getUiRegulator().setDisplayFont(Integer.valueOf(command));
            }
          }
          break;
        default:
      }
    }
  }
}

