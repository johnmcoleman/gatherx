package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import controller.GatherX;

/**
 * Allows the user to make new gathering systems and tab between gathering systems.
 * 
 * @author John Coleman
 *
 */
public class SystemPanelListener implements ActionListener {
  private ArrayList<JButton> systemPanelBtns = new ArrayList<>();
  ArrayList<String> gatheringSystemNames = new ArrayList<>();

  @Override
  public void actionPerformed(ActionEvent e) {
    systemPanelBtns =
        GatherX.getUiRegulator().getGraphicInterface().getSystemPanel().getSystemPanelBtns();
    gatheringSystemNames = GatherX.getGatheringSystemNames();
    for (JButton button : systemPanelBtns) {
      if (e.getSource().equals(button)) {
        String btnText = button.getText();
        if (btnText.equals("+")) {
          GatherX.getUiRegulator().setSelectedObjectName(null);
          GatherX.getUiRegulator().setSelectedObjectType("none");
          GatherX.newGatheringSystem();
        }
        for (String name : gatheringSystemNames) {
          if (btnText.equals(name)) {
            GatherX.getUiRegulator().setSelectedObjectName(null);
            GatherX.getUiRegulator().setSelectedObjectType("none");
            GatherX.setCurrentGatheringSystem(name);
          }
        }
      }
    }
  }
}
