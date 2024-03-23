package display;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import controller.GatherX;

public class SystemPanel extends JPanel {

  private ArrayList<JButton> systemPanelBtns = new ArrayList<JButton>();

  /**
   * Creates a default panel with an add button.
   */
  public SystemPanel() {
    this.setLayout(new GridBagLayout());
    this.setVisible(true);
    GridBagConstraints constraints = new GridBagConstraints();

    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.anchor = GridBagConstraints.EAST;
    JButton btnPlusOne = new JButton("+");
    systemPanelBtns.add(btnPlusOne);
    this.add(btnPlusOne, constraints);
  }

  /**
   * Creates a panel with an add system button as well as all the gathering systems which can be
   * toggled through.
   * 
   * @param graphNames the names of all gathering systems currently held in the controller package.
   */
  public SystemPanel(ArrayList<String> graphNames) {
    this.removeAll();
    this.setLayout(new GridBagLayout());
    this.setVisible(true);
    systemPanelBtns.clear();

    // creates constraints that orient all the buttons on the left hand side
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.NONE;
    constraints.weightx = 0;
    constraints.weighty = 1;
    constraints.gridx = 0;
    constraints.gridy = 0;
    JButton btnPlusOne = new JButton("+");
    systemPanelBtns.add(btnPlusOne);
    SystemPanelListener listener = new SystemPanelListener();
    btnPlusOne.addActionListener(listener);
    this.add(btnPlusOne, constraints);

    // creates a button for each gathering system currently loaded
    for (int index = 0; index < graphNames.size(); index++) {
      JButton systemBtn = new JButton(graphNames.get(index));
      if (systemBtn.getText().contentEquals(GatherX.getCurrentGatheringSystem().getName())) {
        systemBtn.setForeground(UserInterfaceRegulator.LIGHTGREY2);
        systemBtn.setBackground(UserInterfaceRegulator.DARKGREY2);
        systemBtn.setOpaque(true);
        systemBtn.setBorderPainted(false);
        systemBtn.setContentAreaFilled(false);
      }
      systemBtn.addActionListener(listener);
      if (index == graphNames.size() - 1) {
        constraints.weightx = 1;
      }
      constraints.gridx += 1;
      systemPanelBtns.add(systemBtn);
      this.add(systemBtn, constraints);
    }
  }

  public ArrayList<JButton> getSystemPanelBtns() {
    return systemPanelBtns;
  }

  public void setSystemPanelBtns(ArrayList<JButton> systemPanelBtns) {
    this.systemPanelBtns = systemPanelBtns;
  }

}
