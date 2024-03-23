package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import controller.GatherX;

public class DataPanelSaveListener implements ActionListener {
  private ArrayList<String> changedValues = new ArrayList<String>();
  private ArrayList<String> changedParamaters = new ArrayList<String>();
  private ArrayList<String> changedUnits = new ArrayList<String>();
  private ArrayList<JTextField> dataEntryTextFields;
  private HashMap<JTextField, JLabel> dataFieldToLabel;
  private HashMap<JLabel, JComboBox<String>> labelToUnit;

  /**
   * Updates node or pipe data in the model through the controller when save button is pressed.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    Boolean objectIsSelected = GatherX.getUiRegulator().getSelectedObjectType() != null;
    JButton saveBtn = GatherX.getUiRegulator().getGraphicInterface().getDataPanel().getSaveBtn();
    if (objectIsSelected) {
      if (e.getSource().equals(saveBtn)) {
        // clears arraylists from the last time the save button was clicked
        changedValues = new ArrayList<String>();
        changedParamaters = new ArrayList<String>();
        changedUnits = new ArrayList<String>();
        // initializes data text field and data label arrays
        dataEntryTextFields =
            GatherX.getUiRegulator().getGraphicInterface().getDataPanel().getDataTextFields();
        dataFieldToLabel =
            GatherX.getUiRegulator().getGraphicInterface().getDataPanel().getDataFieldToLabel();
        labelToUnit =
            GatherX.getUiRegulator().getGraphicInterface().getDataPanel().getLabelToUnit();
        // gather new values in arrays
        for (JTextField txtField : dataEntryTextFields) {
          String enteredText = txtField.getText();
          // only add entered text to what to update if it is not empty
          if (!(enteredText == null || enteredText.trim().isEmpty())) {
            changedValues.add(txtField.getText());
            changedParamaters.add(dataFieldToLabel.get(txtField).getText());
            changedUnits
                .add((String) labelToUnit.get(dataFieldToLabel.get(txtField)).getSelectedItem());
          }
        }
        GatherX.getUiRegulator().getGraphicInterface().getDataPanel()
            .setUpdatedDataPanelValues(changedParamaters, changedValues, changedUnits);
        GatherX.getUiRegulator().getGraphicInterface().getDataPanel().updateSelectedObjectData();
        GatherX.refreshInterface();
      }
    }
  }
}
