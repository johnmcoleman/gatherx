package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import controller.Action;
import controller.GatherX;
import controller.Message;
import controller.MessageDispatch;

public class CalculationPanelListener implements ActionListener {
  ArrayList<JTextField> globalValueTextFields = new ArrayList<JTextField>();
  ArrayList<JLabel> globalValueLabels = new ArrayList<JLabel>();
  ArrayList<JComboBox<String>> globalUnits = new ArrayList<JComboBox<String>>();
  JTextField iter = new JTextField();
  JTextField error = new JTextField();

  /**
   * Checks the calculation panel to see if any values are changed or calculations are run by the
   * user.
   * 
   * @Override
   */
  public void actionPerformed(ActionEvent e) {
    // sets initial values
    JButton calcBtn =
        GatherX.getUiRegulator().getGraphicInterface().getCalculationPanel().getCalculateBtn();
    JButton saveBtn = GatherX.getUiRegulator().getGraphicInterface().getCalculationPanel()
        .getSetGlobalValuesBtn();
    globalValueTextFields = GatherX.getUiRegulator().getGraphicInterface().getCalculationPanel()
        .getGlobalValueTextFields();
    globalValueLabels =
        GatherX.getUiRegulator().getGraphicInterface().getCalculationPanel().getGlobalValueLabels();
    globalUnits =
        GatherX.getUiRegulator().getGraphicInterface().getCalculationPanel().getGlobalUnits();
    JComboBox<String> equationSelectionBox = GatherX.getUiRegulator().getGraphicInterface()
        .getCalculationPanel().getPressureEquationSelection();
    MessageDispatch dispatcher = GatherX.getDispatcher();
    if (e.getSource().equals(calcBtn)) {
      // runs the selected calculation
      String calculationType = GatherX.getCurrentGatheringSystem().getCalculation().getType();
      String equationSelection = (String) equationSelectionBox.getSelectedItem();
      switch (calculationType) {
        case "Pressure":
          setMetaParameters();
          Message message = new Message(Action.VOLUMES_AND_PRESSURES_CALC, equationSelection);
          dispatcher.dispatch(message);
          GatherX.getCurrentGatheringSystem().setCalculation("Pressure", equationSelection);
          break;
        case "Efficiency":
          setMetaParameters();
          message = new Message(Action.PRESSURES_AND_EFFICIENCIES_CALC, equationSelection);
          dispatcher.dispatch(message);
          GatherX.getCurrentGatheringSystem().setCalculation("Efficiency", equationSelection);
          break;
        default:

      }
    } else if (e.getSource().equals(saveBtn)) {
      // adds all parameter names, values and units to the message and sends it to the dispatcher
      StringBuilder parameterNames = new StringBuilder();
      StringBuilder parameterValues = new StringBuilder();
      StringBuilder parameterUnits = new StringBuilder();
      for (int index = 0; index < globalValueTextFields.size(); index++) {
        JTextField textField = globalValueTextFields.get(index);
        String enteredText = textField.getText();
        enteredText = enteredText.replaceAll("[$,]", "");
        // only add entered text to what to update if it is not empty
        if (!(enteredText == null || enteredText.trim().isEmpty())) {
          if (index != 0) {
            parameterNames.append(",");
            parameterValues.append(",");
            parameterUnits.append(",");
          }
          parameterNames.append(globalValueLabels.get(index).getText());
          parameterValues.append(enteredText);
          parameterUnits.append(globalUnits.get(index).getSelectedItem());
        }
      }

      String messageData = parameterNames.toString() + ":" + parameterValues.toString() + ":"
          + parameterUnits.toString();
      Message message = new Message(Action.SET_ALL_PIPE_DATA, messageData);
      GatherX.getDispatcher().dispatchWithFilter(message, true);
    }
  }

  private void setMetaParameters() {
    iter = GatherX.getUiRegulator().getGraphicInterface().getCalculationPanel()
        .getMaxIterationsField();
    error = GatherX.getUiRegulator().getGraphicInterface().getCalculationPanel()
        .getErrorThresholdField();
    GatherX.getCurrentGatheringSystem().getCalculation()
        .setMaxIterations(Integer.parseInt(iter.getText()));
    GatherX.getCurrentGatheringSystem().getCalculation()
        .setMinFunctionValue(Double.parseDouble(error.getText()));
  }
}
