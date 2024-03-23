package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import controller.GatherX;

/**
 * Checks which user interface tab on the top bar is clicked.
 * 
 * @author John Coleman
 *
 */
public class ButtonModeListener implements ActionListener {

  private static String generalMode;
  private static String subsetMode;
  private static ArrayList<JButton> graphingBtns = MainView.getGraphingButtons();
  private static ArrayList<JButton> analysisBtns = MainView.getAnalysisButtons();
  private static ArrayList<JButton> calculationBtns = MainView.getCalculationButtons();

  /**
   * Determines the tab and specific button clicked and relays mode information to UIRegulator
   * 
   * @Override
   */
  public void actionPerformed(ActionEvent e) {
    generalMode = "UNKNOWN";
    subsetMode = "UNKNOWN";
    for (JButton button : graphingBtns) {
      if (e.getSource().equals(button)) {
        generalMode = "Insert";
        subsetMode = button.getText();
      }
    }
    for (JButton button : analysisBtns) {
      if (e.getSource().equals(button)) {
        generalMode = "View";
        subsetMode = button.getText();
      }
    }
    for (JButton button : calculationBtns) {
      if (e.getSource().equals(button)) {
        generalMode = "Home";
        subsetMode = button.getText();
        if (button.getText().equals("New Calculation")) {
          GatherX.newCalculation("Pressure"); // needs to be updated
        }
        if (button.getText().equals("Pressure")) {
          GatherX.newCalculation(subsetMode);
        }
        if (button.getText().equals("Pipe Size")) {
          GatherX.newCalculation(subsetMode);
        }
        if (button.getText().equals("Efficiency")) {
          GatherX.newCalculation(subsetMode);
        }
      }
    }

    // set the selection mode
    UserInterfaceRegulator.setGeneralMode(generalMode);
    UserInterfaceRegulator.setSubMode(subsetMode);
    GatherX.getCurrentGatheringSystem();
  }
};
