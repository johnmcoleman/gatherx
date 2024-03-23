package display;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import controller.GatherX;

/**
 * Detects changes in the current selection of the pressure equation and uses the controller package
 * to update the application when the selection is changed.
 * 
 * @author John Coleman
 *
 */
public class PressureEquationSelectionListener implements ItemListener {
  @Override
  public void itemStateChanged(ItemEvent e) {
    JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
    String peq = (String) comboBox.getSelectedItem();
    GatherX.getCurrentGatheringSystem().getCalculation().setPressureEquationMode(peq);
    GatherX.refreshInterface();
  }
}
