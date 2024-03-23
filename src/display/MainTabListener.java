package display;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * Sets the general mode based on the tabbed pane selection at the top of the main view.
 * 
 * @author John Coleman
 */
public class MainTabListener implements ChangeListener {

  @Override
  public void stateChanged(ChangeEvent e) {
    String generalMode = "UNKNOWN";
    JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
    int selectedIndex = tabbedPane.getSelectedIndex();
    switch (selectedIndex) {
      case 0:
        generalMode = "File";
        break;
      case 1:
        generalMode = "Home";
        break;
      case 2:
        generalMode = "Insert";
        break;
      case 3:
        generalMode = "View";
        break;
      default:
        break;
    }
    UserInterfaceRegulator.setGeneralMode(generalMode);
  }

}
