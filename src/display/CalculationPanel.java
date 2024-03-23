package display;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import controller.GatherX;
import model.FlowCalc;

/**
 * The second to the left panel which contains global values and allows the user to select and run a
 * calculation.
 * 
 * @author John Coleman
 *
 */
public class CalculationPanel extends JPanel {

  JTextField efficiencyTextField;
  JTextField gravityTextField;
  JTextField basePressureTextField;
  JTextField averageTemperatureTextField;
  JTextField baseTemperatureTextField;
  JTextField averageZTextField;
  JTextField roughnessTextField;
  JTextField maxIterationsField;
  JTextField errorThresholdField;

  JLabel efficiencyLabel = new JLabel("Efficiency");
  JLabel gravityLabel = new JLabel("Specific Gravity");
  JLabel basePressureLabel = new JLabel("Base Pressure");
  JLabel averageTemperatureLabel = new JLabel("Average Temperature");
  JLabel baseTemperatureLabel = new JLabel("Base Temperature");
  JLabel averageZLabel = new JLabel("Z-Factor");
  JLabel roughnessLabel = new JLabel("Friction Factor");
  JLabel maxIterationsLabel = new JLabel("Maximum Iterations");
  JLabel errorThresholdLabel = new JLabel("Error Thresohold");
  JLabel pressureCalcInfoLabel = new JLabel("Pressure Set Points: Outlets");
  JLabel efficiencyCalcInfoLabel = new JLabel("Efficiency Set Points: Outlets, Inlets");

  private final JComboBox<String> efficiencyUnits = new JComboBox<String>();
  private final JComboBox<String> specificGravityUnits = new JComboBox<String>();
  private final JComboBox<String> basePressureUnits = new JComboBox<String>();
  private final JComboBox<String> averageTemperatureUnits = new JComboBox<String>();
  private final JComboBox<String> baseTemperatureUnits = new JComboBox<String>();
  private final JComboBox<String> zFactorUnits = new JComboBox<String>();
  private final JComboBox<String> frictionFactorUnits = new JComboBox<String>();

  ArrayList<JTextField> globalValueTextFields = new ArrayList<JTextField>();
  ArrayList<JLabel> globalValueLabels = new ArrayList<JLabel>();
  ArrayList<JComboBox<String>> globalUnits = new ArrayList<JComboBox<String>>();

  boolean isEfficiency = false;

  JButton calculateBtn;
  JButton setGlobalValuesBtn;
  JLabel panelLabel;
  JLabel pressureEquationLabel;
  JComboBox<String> pressureEquationSelection;

  /**
   * Empty calculation panel constructor displays only a save button.
   */
  public CalculationPanel() {
    this.setLayout(new GridBagLayout());
    this.setVisible(true);
    GridBagConstraints constraint = new GridBagConstraints();
    constraint.gridx = 0;
    constraint.gridy = 10;
    constraint.weightx = 1;
    constraint.weighty = 1;
    constraint.gridwidth = 3;
    constraint.insets = new Insets(10, 10, 10, 10);
    constraint.fill = GridBagConstraints.NONE;

    pressureEquationSelection = new JComboBox<String>();
    pressureEquationSelection.addItem("Weymouth");
    pressureEquationSelection.addItem("Mueller");
    pressureEquationSelection.addItem("Panhandle A");
    pressureEquationSelection.addItem("Panhandle B");


    calculateBtn = new JButton("Calculate");
    this.add(calculateBtn, constraint);
  }

  public CalculationPanel(FlowCalc currentCalculation) {
    this.setLayout(new GridBagLayout());
    this.setVisible(true);
    this.removeAll();
    String calculationType = currentCalculation.getType();
    String pressureEquation = currentCalculation.getPressureEquationMode();
    // determines calculation panel type
    if (calculationType.equals("Efficiency")) {
      isEfficiency = true;
    }

    FlowCalc newCalc = currentCalculation;
    int textFieldLength = 10;
    efficiencyTextField = new JTextField(textFieldLength);
    gravityTextField = new JTextField(textFieldLength);
    basePressureTextField = new JTextField(textFieldLength);
    averageTemperatureTextField = new JTextField(textFieldLength);
    baseTemperatureTextField = new JTextField(textFieldLength);
    averageZTextField = new JTextField(textFieldLength);
    roughnessTextField = new JTextField(textFieldLength);
    maxIterationsField = new JTextField(textFieldLength);
    errorThresholdField = new JTextField(textFieldLength);

    efficiencyTextField.setText(String.valueOf(newCalc.getEfficiency()));
    gravityTextField.setText(String.valueOf(newCalc.getGravity()));
    basePressureTextField.setText(String.valueOf(newCalc.getBasePressure()));
    averageTemperatureTextField.setText(String.valueOf(newCalc.getAverageTemperature()));
    baseTemperatureTextField.setText(String.valueOf(newCalc.getBaseTemperature()));
    averageZTextField.setText(String.valueOf(newCalc.getAverageZ()));
    roughnessTextField.setText(String.valueOf(newCalc.getFriction()));
    maxIterationsField.setText(String.valueOf(newCalc.getMaxIterations()));
    errorThresholdField.setText(String.valueOf(newCalc.getMinFunctionValue()));

    globalValueTextFields = new ArrayList<JTextField>();
    globalValueLabels = new ArrayList<JLabel>();
    globalUnits = new ArrayList<JComboBox<String>>();

    if (!isEfficiency) {
      globalValueTextFields.add(efficiencyTextField);
    }
    globalValueTextFields.add(gravityTextField);
    globalValueTextFields.add(basePressureTextField);
    globalValueTextFields.add(averageTemperatureTextField);
    globalValueTextFields.add(baseTemperatureTextField);
    globalValueTextFields.add(averageZTextField);

    if (!isEfficiency) {
      globalValueLabels.add(efficiencyLabel);
    }
    globalValueLabels.add(gravityLabel);
    globalValueLabels.add(basePressureLabel);
    globalValueLabels.add(averageTemperatureLabel);
    globalValueLabels.add(baseTemperatureLabel);
    globalValueLabels.add(averageZLabel);

    if (!isEfficiency) {
      globalUnits.add(efficiencyUnits);
    }
    globalUnits.add(specificGravityUnits);
    globalUnits.add(basePressureUnits);
    globalUnits.add(averageTemperatureUnits);
    globalUnits.add(baseTemperatureUnits);
    globalUnits.add(zFactorUnits);

    for (JComboBox<String> box : globalUnits) {
      box.removeAllItems();
    }

    CalculationPanelListener listen = new CalculationPanelListener();
    PressureEquationSelectionListener listenPressureEquation =
        new PressureEquationSelectionListener();

    // creates components on all calculation panels
    GridBagConstraints constraint = new GridBagConstraints();

    panelLabel = new JLabel(calculationType + " Calculation");
    constraint.gridy = 0;
    constraint.weightx = 0.5;
    constraint.weighty = 0.0;
    constraint.insets = new Insets(10, 10, 10, 10);
    constraint.fill = GridBagConstraints.NONE;
    constraint.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(panelLabel, constraint);

    // creates global value setting components
    specificGravityUnits.addItem("air");
    basePressureUnits.addItem("psi");
    averageTemperatureUnits.addItem("R");
    averageTemperatureUnits.addItem("F");
    averageTemperatureUnits.addItem("C");
    averageTemperatureUnits.addItem("K");
    baseTemperatureUnits.addItem("R");
    baseTemperatureUnits.addItem("F");
    baseTemperatureUnits.addItem("C");
    baseTemperatureUnits.addItem("K");
    zFactorUnits.addItem("N/A");
    frictionFactorUnits.addItem("N/A");
    efficiencyUnits.addItem("N/A");
    basePressureUnits
        .setSelectedItem(GatherX.getCurrentGatheringSystem().getUnits().getBasePressureUnit());
    averageTemperatureUnits.setSelectedItem(
        GatherX.getCurrentGatheringSystem().getUnits().getAverageTemperatureUnit());
    baseTemperatureUnits
        .setSelectedItem(GatherX.getCurrentGatheringSystem().getUnits().getBaseTemperatureUnit());

    constraint.gridx = 0;
    constraint.gridy = 1;
    constraint.fill = GridBagConstraints.NONE;

    // creates equation selection drop down
    constraint.gridy += 1;
    constraint.gridx = 0;
    constraint.gridwidth = 1;
    pressureEquationLabel = new JLabel("Equation");
    this.add(pressureEquationLabel, constraint);

    // creates pressure equation selection options
    pressureEquationSelection = new JComboBox<String>();
    pressureEquationSelection.addItem("Weymouth");
    pressureEquationSelection.addItem("Mueller");
    pressureEquationSelection.addItem("Panhandle A");
    pressureEquationSelection.addItem("Panhandle B");
    pressureEquationSelection.setSelectedItem(pressureEquation);
    pressureEquationSelection.addItemListener(listenPressureEquation);
    constraint.gridx = 1;
    constraint.gridwidth = 2;
    constraint.fill = GridBagConstraints.HORIZONTAL;
    this.add(pressureEquationSelection, constraint);

    // creates iteration number text field
    constraint.gridx = 0;
    constraint.gridy += 1;
    constraint.gridwidth = 1;
    constraint.fill = GridBagConstraints.NONE;
    this.add(maxIterationsLabel, constraint);

    constraint.gridx = 1;
    constraint.gridwidth = 1;
    this.add(maxIterationsField, constraint);

    // creates error threshold number text field
    constraint.gridx = 0;
    constraint.gridy += 1;
    constraint.gridwidth = 1;
    constraint.fill = GridBagConstraints.NONE;
    this.add(errorThresholdLabel, constraint);

    constraint.gridx = 1;
    constraint.gridwidth = 1;
    this.add(errorThresholdField, constraint);

    // creates set point information label
    constraint.gridx = 0;
    constraint.gridy += 1;
    constraint.gridwidth = 3;
    constraint.fill = GridBagConstraints.NONE;
    if (isEfficiency) {
      this.add(efficiencyCalcInfoLabel, constraint);
    } else {
      this.add(pressureCalcInfoLabel, constraint);
    }

    // creates calculation button
    constraint.gridx = 0;
    constraint.gridy += 1;
    constraint.weightx = 1.0;
    constraint.gridwidth = 3;
    constraint.fill = GridBagConstraints.HORIZONTAL;
    calculateBtn = new JButton("Calculate");
    calculateBtn.addActionListener(listen);
    this.add(calculateBtn, constraint);

    constraint.gridy += 1;
    constraint.gridwidth = 1;
    for (int index = 0; index < globalValueLabels.size(); index++) {
      constraint.gridx = 0;
      constraint.fill = GridBagConstraints.NONE;
      this.add(globalValueLabels.get(index), constraint);
      constraint.gridx = 1;
      constraint.fill = GridBagConstraints.HORIZONTAL;
      this.add(globalValueTextFields.get(index), constraint);
      constraint.gridx = 2;
      constraint.fill = GridBagConstraints.HORIZONTAL;
      this.add(globalUnits.get(index), constraint);
      constraint.gridy += 1;
    }

    setGlobalValuesBtn = new JButton("Save System Values");
    setGlobalValuesBtn.addActionListener(listen);
    constraint.gridx = 0;
    constraint.gridy += 1;
    constraint.weightx = 0.0;
    constraint.weighty = 1.0;
    constraint.fill = GridBagConstraints.HORIZONTAL;
    constraint.gridwidth = 3;
    this.add(setGlobalValuesBtn, constraint);
  }

  public JButton getCalculateBtn() {
    return calculateBtn;
  }

  public void setCalculateBtn(JButton calculateBtn) {
    this.calculateBtn = calculateBtn;
  }

  public JButton getSetGlobalValuesBtn() {
    return setGlobalValuesBtn;
  }

  public void setSetGlobalValuesBtn(JButton setGlobalValuesBtn) {
    this.setGlobalValuesBtn = setGlobalValuesBtn;
  }

  public ArrayList<JTextField> getGlobalValueTextFields() {
    return globalValueTextFields;
  }

  public void setGlobalValueTextFields(ArrayList<JTextField> globalValueTextFields) {
    this.globalValueTextFields = globalValueTextFields;
  }

  public ArrayList<JLabel> getGlobalValueLabels() {
    return globalValueLabels;
  }

  public void setGlobalValueLabels(ArrayList<JLabel> globalValueLabels) {
    this.globalValueLabels = globalValueLabels;
  }

  public ArrayList<JComboBox<String>> getGlobalUnits() {
    return globalUnits;
  }

  public void setGlobalUnits(ArrayList<JComboBox<String>> globalUnits) {
    this.globalUnits = globalUnits;
  }

  public JComboBox<String> getPressureEquationSelection() {
    return pressureEquationSelection;
  }

  public void setPressureEquationSelection(JComboBox<String> pressureEquationSelection) {
    this.pressureEquationSelection = pressureEquationSelection;
  }

  public JTextField getMaxIterationsField() {
    return maxIterationsField;
  }

  public void setMaxIterationsField(JTextField maxIterationsField) {
    this.maxIterationsField = maxIterationsField;
  }

  public JTextField getErrorThresholdField() {
    return errorThresholdField;
  }

  public void setErrorThresholdField(JTextField errorThresholdField) {
    this.errorThresholdField = errorThresholdField;
  }

}
