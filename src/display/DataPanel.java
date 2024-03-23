package display;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import controller.Action;
import controller.GatherX;
import controller.Message;
import controller.MessageDispatch;


/**
 * The Data Panel class is the rightmost panel which displays and enables the user to modify data
 * from selected nodes and pipes.
 * 
 * @author John Coleman
 *
 */
public class DataPanel extends JPanel {

  // pipe data panel components
  private JTextField nameField;
  private JTextField lengthField;
  private JTextField diameterField;
  private JTextField flowRateField;
  private JTextField inletPressureField;
  private JTextField outletPressureField;
  private JTextField pressureDifferentialField;
  private JTextField efficiencyField;
  private JTextField dropoutField;
  private JTextField velocityField;
  private JTextField specificGravityField;
  private JTextField basePressureField;
  private JTextField avgTempField;
  private JTextField baseTempField;
  private JTextField zFactorField;
  private JTextField frictionFactorField;

  private JLabel nameLabel = new JLabel("Name");
  private JLabel lengthLabel = new JLabel("Length");
  private JLabel diameterLabel = new JLabel("Diameter");
  private JLabel flowRateLabel = new JLabel("Flow Rate");
  private JLabel inletPressureLabel = new JLabel("Inlet Pressure");
  private JLabel outletPressureLabel = new JLabel("Outlet Pressure");
  private JLabel pressureDifferentialLabel = new JLabel("Pressure Gradient");
  private JLabel efficiencyLabel = new JLabel("Efficiency");
  private JLabel dropoutLabel = new JLabel("Estimated Liquid Dropout");
  private JLabel velocityLabel = new JLabel("Average Velocity");
  private JLabel specificGravityLabel = new JLabel("Specific Gravity");
  private JLabel basePressureLabel = new JLabel("Base Pressure");
  private JLabel avgTempLabel = new JLabel("Average Temperature");
  private JLabel baseTempLabel = new JLabel("Base Temperature");
  private JLabel zLabel = new JLabel("Z-Factor");
  private JLabel frictionFactorLabel = new JLabel("Friction Factor");
  private JLabel titleLabel = new JLabel();

  private JComboBox<String> lengthUnits = new JComboBox<>();
  private JComboBox<String> diameterUnits = new JComboBox<>();
  private JComboBox<String> flowRateUnits = new JComboBox<>();
  private JComboBox<String> inletPressureUnits = new JComboBox<>();
  private JComboBox<String> outletPressureUnits = new JComboBox<>();
  private JComboBox<String> pressureDifferentialUnits = new JComboBox<>();
  private JComboBox<String> efficiencyUnits = new JComboBox<>();
  private JComboBox<String> dropoutUnits = new JComboBox<>();
  private JComboBox<String> velocityUnits = new JComboBox<>();
  private JComboBox<String> specificGravityUnits = new JComboBox<>();
  private JComboBox<String> basePressureUnits = new JComboBox<>();
  private JComboBox<String> averageTemperatureUnits = new JComboBox<>();
  private JComboBox<String> baseTemperatureUnits = new JComboBox<>();
  private JComboBox<String> zFactorUnits = new JComboBox<>();
  private JComboBox<String> frictionFactorUnits = new JComboBox<>();

  // node data panel components
  private JLabel nodeTotalVolumesLabel = new JLabel("Total Volumes");
  private JComboBox<String> nameUnits = new JComboBox<String>();
  private JComboBox<String> nodeTotalVolumesUnits = new JComboBox<String>();
  private JTextField nameTextField;
  private JTextField nodeTotalVolumesTextField;

  private JLabel nodeInletVolumes = new JLabel("Inlet Volumes");
  private JComboBox<String> nodeInletVolumesUnits = new JComboBox<String>();
  private JTextField nodeInletVolumesTextField;

  private JLabel nodeInletPressureLabel = new JLabel("Inlet Pressure");
  private JComboBox<String> nodeInletPressureUnits = new JComboBox<String>();
  private JTextField nodeInletPressureTextField;

  private JLabel nodeOutletPressureLabel = new JLabel("Outlet Pressure");
  private JComboBox<String> nodeOutletPressureUnits = new JComboBox<String>();
  private JTextField nodeOutletPressureTextField;

  private JLabel nodePressureDifferentialLabel = new JLabel("Pressure Differential");
  private JComboBox<String> nodePressureDifferentialUnits = new JComboBox<String>();
  private JTextField nodePressureDifferentialTextField;

  private DataPanelSaveListener dataPanelEntryListen = new DataPanelSaveListener();

  private static MessageDispatch dispatcher = GatherX.getDispatcher();
  private ArrayList<JTextField> dataTextFields = new ArrayList<>();
  private ArrayList<JLabel> dataEntryLabels = new ArrayList<>();
  private ArrayList<JComboBox<String>> unitLabels = new ArrayList<>();
  private HashMap<JTextField, JLabel> dataFieldToLabel = new HashMap<>();
  private HashMap<JLabel, JTextField> labelToDataField = new HashMap<>();
  private HashMap<JComboBox<String>, JLabel> unitToLabel = new HashMap<>();
  private HashMap<JLabel, JComboBox<String>> labelToUnit = new HashMap<>();
  private ArrayList<String> newParamaters = new ArrayList<>();
  private ArrayList<String> newValues = new ArrayList<>();
  private ArrayList<String> newUnits = new ArrayList<>();
  private JButton saveBtn = new JButton("Save");

  /**
   * Constructs an empty data panel
   */
  public DataPanel() {

  }

  /**
   * Makes all data in a pipe or node just selected by the user visible in the data panel.
   */
  public void displaySelectedData() {
    // fetch all parameters for the object
    StringBuilder messageData = new StringBuilder();
    messageData.append(GatherX.getUiRegulator().getSelectedObjectName());
    messageData.append(":");
    for (int index = 0; index < dataTextFields.size(); index++) {
      // gets text of label corresponding to the text field
      messageData.append(dataFieldToLabel.get(dataTextFields.get(index)).getText());
      if (!(dataTextFields.size() - 1 == index)) {
        messageData.append(",");
      }
    }
    String selectedObjectType = GatherX.getUiRegulator().getSelectedObjectType();
    if (selectedObjectType.equals("Pipe")) {
      Message fetchPipeInfo = new Message(Action.FETCH_PIPE_DATA, messageData.toString());
      dispatcher.dispatch(fetchPipeInfo);
    }
    if (selectedObjectType.equals("Node")) {
      Message fetchNodeInfo = new Message(Action.FETCH_NODE_DATA, messageData.toString());
      dispatcher.dispatch(fetchNodeInfo);
    }
    // update all text fields with the current object parameters based on the reply
    String[] uiReply = UserInterfaceRegulator.getCurrentReply().split(":");
    String[] parameterValues = uiReply[1].split(",");
    String[] parameterUnits = uiReply[0].split(",");
    for (int index = 0; index < dataTextFields.size(); index++) {
      String currentText = parameterValues[index];
      dataTextFields.get(index).setText(currentText);
      unitLabels.get(index).setSelectedItem(parameterUnits[index]);
    }
    this.revalidate();
    this.repaint();
  }

  /**
   * Makes all the data in a pipe or node just saved by the user update to the new model package
   * data.
   * 
   * @param changedParamaters the new panel parameters.
   * @param changedValues the new panel quantities.
   * @param changedUnits the new panel unit selections.
   */
  public void setUpdatedDataPanelValues(ArrayList<String> changedParamaters,
      ArrayList<String> changedValues, ArrayList<String> changedUnits) {
    newParamaters = changedParamaters;
    newValues = changedValues;
    newUnits = changedUnits;
  }

  /**
   * Sends message to update model package data for the selected object when the save button is
   * pressed on the panel.
   */
  public void updateSelectedObjectData() {
    // builds the message to the controller
    StringBuilder messageData = new StringBuilder();
    messageData.append(GatherX.getUiRegulator().getSelectedObjectName());
    messageData.append(":");
    for (int index = 0; index < newParamaters.size(); index++) {
      messageData.append(newParamaters.get(index));
      if (!(newParamaters.size() - 1 == index)) {
        messageData.append(",");
      }
    }
    messageData.append(":");
    for (int index = 0; index < newValues.size(); index++) {
      String currentValue = newValues.get(index);
      currentValue = currentValue.replaceAll("[$,]", "");
      messageData.append(currentValue);
      if (!(newValues.size() - 1 == index)) {
        messageData.append(",");
      }
    }
    messageData.append(":");
    for (int index = 0; index < newUnits.size(); index++) {
      messageData.append(newUnits.get(index));
      if (!(newUnits.size() - 1 == index)) {
        messageData.append(",");
      }
    }
    // dispatches two messages one that sends data to the model and another that fetches it to be
    // displayed
    MessageDispatch dispatcher = GatherX.getDispatcher();
    String selectedObjectType = GatherX.getUiRegulator().getSelectedObjectType();
    if (selectedObjectType.equals("Pipe")) {
      Message pipeDataSend = new Message(Action.SET_PIPE_DATA, messageData.toString());
      dispatcher.dispatchWithFilter(pipeDataSend, true);
    }
    if (selectedObjectType.equals("Node")) {
      Message nodeDataSend = new Message(Action.SET_NODE_DATA, messageData.toString());
      dispatcher.dispatchWithFilter(nodeDataSend, true);
    }
  }

  /**
   * Removes all elements from the panel then adds new elements for a pipe selected by the user.
   */
  public void setDataPanel() {
    this.clearMaintainedObjects();
    this.removeAll();
    this.setVisible(false);

    // creates new text fields
    nameField = new JTextField();
    lengthField = new JTextField();
    diameterField = new JTextField();
    flowRateField = new JTextField();
    inletPressureField = new JTextField();
    outletPressureField = new JTextField();
    pressureDifferentialField = new JTextField();
    efficiencyField = new JTextField();
    dropoutField = new JTextField();
    velocityField = new JTextField();
    specificGravityField = new JTextField();
    basePressureField = new JTextField();
    avgTempField = new JTextField();
    baseTempField = new JTextField();
    zFactorField = new JTextField();
    frictionFactorField = new JTextField();

    String equationMode =
        GatherX.getCurrentGatheringSystem().getCalculation().getPressureEquationMode();
    this.setLayout(new GridBagLayout());
    GridBagConstraints constraint = new GridBagConstraints();
    constraint.weightx = 1;
    constraint.weighty = 0;
    constraint.insets = new Insets(10, 10, 10, 10);
    constraint.anchor = GridBagConstraints.NORTH;
    constraint.fill = GridBagConstraints.HORIZONTAL;

    // add the labels
    constraint.gridx = 0;
    constraint.gridy = 0;
    this.add(nameLabel, constraint);
    dataEntryLabels.add(nameLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(lengthLabel, constraint);
    dataEntryLabels.add(lengthLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(diameterLabel, constraint);
    dataEntryLabels.add(diameterLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(flowRateLabel, constraint);
    dataEntryLabels.add(flowRateLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(inletPressureLabel, constraint);
    dataEntryLabels.add(inletPressureLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(outletPressureLabel, constraint);
    dataEntryLabels.add(outletPressureLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(pressureDifferentialLabel, constraint);
    dataEntryLabels.add(pressureDifferentialLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(efficiencyLabel, constraint);
    dataEntryLabels.add(efficiencyLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(dropoutLabel, constraint);
    dataEntryLabels.add(dropoutLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(velocityLabel, constraint);
    dataEntryLabels.add(velocityLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(specificGravityLabel, constraint);
    dataEntryLabels.add(specificGravityLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(basePressureLabel, constraint);
    dataEntryLabels.add(basePressureLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(avgTempLabel, constraint);
    dataEntryLabels.add(avgTempLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(baseTempLabel, constraint);
    dataEntryLabels.add(baseTempLabel);

    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(zLabel, constraint);
    dataEntryLabels.add(zLabel);

    if (equationMode.equals("American Gas Association")) {
      constraint.gridx = 0;
      constraint.gridy += 1;
      this.add(frictionFactorLabel, constraint);
      dataEntryLabels.add(frictionFactorLabel);
    }

    // add the text fields
    constraint.gridx = 1;
    constraint.gridy = 0;
    this.add(nameField, constraint);
    nameField.setColumns(20); // Adjusted to make the whole column wider
    dataTextFields.add(nameField);

    constraint.gridy += 1;
    this.add(lengthField, constraint);
    lengthField.setColumns(10);
    dataTextFields.add(lengthField);

    constraint.gridy += 1;
    this.add(diameterField, constraint);
    diameterField.setColumns(10);
    dataTextFields.add(diameterField);

    constraint.gridy += 1;
    this.add(flowRateField, constraint);
    flowRateField.setColumns(10);
    flowRateField.setEditable(false);
    dataTextFields.add(flowRateField);

    constraint.gridy += 1;
    this.add(inletPressureField, constraint);
    inletPressureField.setColumns(10);
    inletPressureField.setEditable(false);
    dataTextFields.add(inletPressureField);

    constraint.gridy += 1;
    this.add(outletPressureField, constraint);
    outletPressureField.setColumns(10);
    outletPressureField.setEditable(false);
    dataTextFields.add(outletPressureField);

    constraint.gridy += 1;
    this.add(pressureDifferentialField, constraint);
    pressureDifferentialField.setColumns(10);
    pressureDifferentialField.setEditable(false);
    dataTextFields.add(pressureDifferentialField);

    constraint.gridy += 1;
    this.add(efficiencyField, constraint);
    efficiencyField.setColumns(10);
    dataTextFields.add(efficiencyField);

    constraint.gridy += 1;
    this.add(dropoutField, constraint);
    dropoutField.setColumns(10);
    dropoutField.setEditable(false);
    dataTextFields.add(dropoutField);

    constraint.gridy += 1;
    this.add(velocityField, constraint);
    velocityField.setColumns(10);
    velocityField.setEditable(false);
    dataTextFields.add(velocityField);

    constraint.gridy += 1;
    this.add(specificGravityField, constraint);
    specificGravityField.setColumns(10);
    dataTextFields.add(specificGravityField);

    constraint.gridy += 1;
    this.add(basePressureField, constraint);
    basePressureField.setColumns(10);
    dataTextFields.add(basePressureField);

    constraint.gridy += 1;
    this.add(avgTempField, constraint);
    avgTempField.setColumns(10);
    dataTextFields.add(avgTempField);

    constraint.gridy += 1;
    this.add(baseTempField, constraint);
    baseTempField.setColumns(10);
    dataTextFields.add(baseTempField);

    constraint.gridy += 1;
    this.add(zFactorField, constraint);
    zFactorField.setColumns(10);
    dataTextFields.add(zFactorField);

    // friction factor only needed for AGA equation
    if (equationMode.equals("American Gas Association")) {
      constraint.gridx = 1;
      constraint.gridy += 1;
      this.add(frictionFactorField, constraint);
      frictionFactorField.setColumns(10);
      dataTextFields.add(frictionFactorField);
    }

    // unit choice fields
    constraint.gridx = 2;
    constraint.gridy = 0;
    this.add(nameUnits, constraint);
    unitLabels.add(nameUnits);
    nameUnits.addItem("N/A");

    constraint.gridy += 1;
    this.add(lengthUnits, constraint);
    unitLabels.add(lengthUnits);
    lengthUnits.addItem("mi");
    lengthUnits.addItem("ft");

    constraint.gridy += 1;
    this.add(diameterUnits, constraint);
    unitLabels.add(diameterUnits);
    diameterUnits.addItem("in");

    constraint.gridy += 1;
    this.add(flowRateUnits, constraint);
    unitLabels.add(flowRateUnits);
    flowRateUnits.addItem("scfd");
    flowRateUnits.addItem("mscfd");
    flowRateUnits.addItem("mmscfd");

    constraint.gridy += 1;
    this.add(inletPressureUnits, constraint);
    unitLabels.add(inletPressureUnits);
    inletPressureUnits.addItem("psia");

    constraint.gridy += 1;
    this.add(outletPressureUnits, constraint);
    unitLabels.add(outletPressureUnits);
    outletPressureUnits.addItem("psia");

    constraint.gridy += 1;
    this.add(pressureDifferentialUnits, constraint);
    unitLabels.add(pressureDifferentialUnits);
    pressureDifferentialUnits.addItem("psi/mi");

    constraint.gridy += 1;
    this.add(efficiencyUnits, constraint);
    unitLabels.add(efficiencyUnits);
    efficiencyUnits.addItem("N/A");

    constraint.gridy += 1;
    this.add(dropoutUnits, constraint);
    unitLabels.add(dropoutUnits);
    dropoutUnits.addItem("ft^3");

    constraint.gridy += 1;
    this.add(velocityUnits, constraint);
    unitLabels.add(velocityUnits);
    velocityUnits.addItem("ft/s");

    constraint.gridy += 1;
    this.add(specificGravityUnits, constraint);
    unitLabels.add(specificGravityUnits);
    specificGravityUnits.addItem("air");

    constraint.gridy += 1;
    this.add(basePressureUnits, constraint);
    unitLabels.add(basePressureUnits);
    basePressureUnits.addItem("psia");

    constraint.gridy += 1;
    this.add(averageTemperatureUnits, constraint);
    unitLabels.add(averageTemperatureUnits);
    averageTemperatureUnits.addItem("F");
    averageTemperatureUnits.addItem("R");
    averageTemperatureUnits.addItem("C");
    averageTemperatureUnits.addItem("K");

    constraint.gridy += 1;
    this.add(baseTemperatureUnits, constraint);
    unitLabels.add(baseTemperatureUnits);
    baseTemperatureUnits.addItem("F");
    baseTemperatureUnits.addItem("R");
    baseTemperatureUnits.addItem("C");
    baseTemperatureUnits.addItem("K");

    constraint.gridy += 1;
    this.add(zFactorUnits, constraint);
    unitLabels.add(zFactorUnits);
    zFactorUnits.addItem("N/A");

    // friction factor only needed for AGA equation
    if (equationMode.equals("American Gas Association")) {
      constraint.gridy += 1;
      this.add(frictionFactorUnits, constraint);
      unitLabels.add(frictionFactorUnits);
      frictionFactorUnits.addItem("N/A");
    }

    // add the save button
    constraint.gridx = 0;
    constraint.gridy += 1;
    constraint.weighty = 1;
    constraint.gridwidth = 3;
    saveBtn.addActionListener(dataPanelEntryListen);
    saveBtn.setForeground(UserInterfaceRegulator.DARKGREY2);
    saveBtn.setBackground(UserInterfaceRegulator.LIGHTGREY2);
    saveBtn.setBorderPainted(false);
    saveBtn.setOpaque(true);
    this.add(saveBtn, constraint);

    // add object type
    constraint.gridx = 0;
    constraint.gridy += 1;
    constraint.weighty = 0;
    constraint.gridwidth = 3;
    titleLabel.setText("Selected Object Type - Pipe");
    this.add(titleLabel, constraint);

    // iterates through all labels, units, and textfields and maps them to each other
    for (int index = 0; index < dataTextFields.size(); index++) {
      dataFieldToLabel.put(dataTextFields.get(index), dataEntryLabels.get(index));
      unitToLabel.put(unitLabels.get(index), dataEntryLabels.get(index));
      labelToUnit.put(dataEntryLabels.get(index), unitLabels.get(index));
      labelToDataField.put(dataEntryLabels.get(index), dataTextFields.get(index));
    }

    // sets all the text fields to display the current data
    this.setVisible(true);
    displaySelectedData();
  }

  /**
   * Removes all elements from the panel then adds new elements for a node selected by the user.
   * 
   * @param nodeType the type of node selected which determines which elements are displayed.
   */
  public void setDataPanel(String nodeType) {
    this.clearMaintainedObjects();
    this.setBackground(UserInterfaceRegulator.DARKGREY1);
    this.removeAll();

    // creates textfields
    nameTextField = new JTextField();
    nodeTotalVolumesTextField = new JTextField();
    nodeInletVolumesTextField = new JTextField();
    nodeInletPressureTextField = new JTextField();
    nodeOutletPressureTextField = new JTextField();
    nodePressureDifferentialTextField = new JTextField();

    this.setLayout(new GridBagLayout());
    GridBagConstraints constraint = new GridBagConstraints();
    constraint.weightx = 1;
    constraint.weighty = 0;
    constraint.insets = new Insets(10, 10, 10, 10);
    constraint.anchor = GridBagConstraints.NORTH;
    constraint.fill = GridBagConstraints.HORIZONTAL;

    // create node data panel for tees
    if (nodeType.equals("Tee") || nodeType.equals("Inlet") || nodeType.equals("Outlet")) {
      // name and total volumes
      constraint.gridx = 0;
      constraint.gridy = 0;
      this.add(nameLabel, constraint);
      dataEntryLabels.add(nameLabel);

      constraint.gridx = 2;
      constraint.gridy = 0;
      nameUnits.addItem("N/A");
      this.add(nameUnits, constraint);
      unitLabels.add(nameUnits);

      constraint.gridx = 1;
      constraint.gridy = 0;
      this.add(nameTextField, constraint);
      nameTextField.setColumns(10);
      dataTextFields.add(nameTextField);
    }

    // pressure differential must be first item in the list because outlet and inlet pressure depend
    // on it
    dataEntryLabels.add(nodePressureDifferentialLabel);
    unitLabels.add(nodePressureDifferentialUnits);
    dataTextFields.add(nodePressureDifferentialTextField);

    // inlet pressure
    constraint.gridx = 0;
    constraint.gridy += 1;
    this.add(nodeInletPressureLabel, constraint);
    dataEntryLabels.add(nodeInletPressureLabel);

    constraint.gridx = 2;
    nodeInletPressureUnits.addItem("psia");
    this.add(nodeInletPressureUnits, constraint);
    unitLabels.add(nodeInletPressureUnits);

    constraint.gridx = 1;
    this.add(nodeInletPressureTextField, constraint);
    nodeInletPressureTextField.setColumns(10);
    dataTextFields.add(nodeInletPressureTextField);

    // create node data panel for inlets
    if (nodeType.equals("Inlet") || nodeType.equals("Outlet")) {
      // inlets need volume gathered at them
      constraint.gridx = 0;
      constraint.gridy += 1;
      this.add(nodeInletVolumes, constraint);
      dataEntryLabels.add(nodeInletVolumes);

      // volumetric flow rate unit
      constraint.gridx = 2;
      nodeInletVolumesUnits.addItem("scfd");
      nodeInletVolumesUnits.addItem("mscfd");
      nodeInletVolumesUnits.addItem("mmscfd");
      this.add(nodeInletVolumesUnits, constraint);
      unitLabels.add(nodeInletVolumesUnits);

      constraint.gridx = 1;
      this.add(nodeInletVolumesTextField, constraint);
      nodeInletVolumesTextField.setColumns(10);
      dataTextFields.add(nodeInletVolumesTextField);
    }

    // iterates through all labels, units, and textfields and maps them to each other
    for (int index = 0; index < dataTextFields.size(); index++) {
      dataFieldToLabel.put(dataTextFields.get(index), dataEntryLabels.get(index));
      unitToLabel.put(unitLabels.get(index), dataEntryLabels.get(index));
      labelToUnit.put(dataEntryLabels.get(index), unitLabels.get(index));
      labelToDataField.put(dataEntryLabels.get(index), dataTextFields.get(index));
    }

    // add the save button
    constraint.gridx = 0;
    constraint.gridy += 1;
    constraint.weighty = 1;
    constraint.gridwidth = 3;
    saveBtn.addActionListener(dataPanelEntryListen);
    saveBtn.setForeground(UserInterfaceRegulator.DARKGREY2);
    saveBtn.setBackground(UserInterfaceRegulator.LIGHTGREY1);
    this.add(saveBtn, constraint);

    constraint.gridx = 0;
    constraint.gridy += 1;
    constraint.weighty = 0;
    constraint.gridwidth = 3;
    titleLabel.setText("Selected Object Type - " + nodeType);
    this.add(titleLabel, constraint);

    // sets all the text fields to display the current data
    displaySelectedData();
  }

  /**
   * Clears objects within objects which have been removed from the panel when the panel is set for
   * a new selected pipe or node.
   */
  private void clearMaintainedObjects() {
    // clears save buttons of actionlisteners
    for (ActionListener actionListener : saveBtn.getActionListeners()) {
      saveBtn.removeActionListener(actionListener);
    }

    // clear jcomboboxes of entries
    for (Map.Entry<JLabel, JComboBox<String>> entry : labelToUnit.entrySet()) {
      entry.getValue().removeAllItems();
    }

    // refresh all objects that contained labels and fields from the old panel
    dataTextFields = new ArrayList<JTextField>();
    dataEntryLabels = new ArrayList<JLabel>();
    unitLabels = new ArrayList<JComboBox<String>>();
    dataFieldToLabel = new HashMap<JTextField, JLabel>();
    labelToDataField = new HashMap<JLabel, JTextField>();
    unitToLabel = new HashMap<JComboBox<String>, JLabel>();
    labelToUnit = new HashMap<JLabel, JComboBox<String>>();
    newParamaters = new ArrayList<String>();
    newValues = new ArrayList<String>();
    newUnits = new ArrayList<String>();
  }

  public void initDataPanel() {
    this.clearMaintainedObjects();
    this.removeAll();
  }

  public JButton getSaveBtn() {
    return saveBtn;
  }

  public void setSaveBtn(JButton saveBtn) {
    this.saveBtn = saveBtn;
  }

  public ArrayList<JTextField> getDataTextFields() {
    return dataTextFields;
  }

  public void setDataTextFields(ArrayList<JTextField> txtFields) {
    dataTextFields = txtFields;
  }

  public ArrayList<JLabel> getDataEntryLabels() {
    return dataEntryLabels;
  }

  public void setDataEntryLabels(ArrayList<JLabel> pipeLbls) {
    this.dataEntryLabels = pipeLbls;
  }

  public HashMap<JTextField, JLabel> getDataFieldToLabel() {
    return dataFieldToLabel;
  }

  public void setDataFieldToLabel(HashMap<JTextField, JLabel> pipeFieldToLabel) {
    this.dataFieldToLabel = pipeFieldToLabel;
  }

  public ArrayList<JComboBox<String>> getUnitLabels() {
    return unitLabels;
  }

  public void setUnitLabels(ArrayList<JComboBox<String>> unitLabels) {
    this.unitLabels = unitLabels;
  }

  public HashMap<JLabel, JTextField> getLabelToDataField() {
    return labelToDataField;
  }

  public void setLabelToDataField(HashMap<JLabel, JTextField> labelToDataField) {
    this.labelToDataField = labelToDataField;
  }

  public HashMap<JComboBox<String>, JLabel> getUnitToLabel() {
    return unitToLabel;
  }

  public void setUnitToLabel(HashMap<JComboBox<String>, JLabel> unitToLabel) {
    this.unitToLabel = unitToLabel;
  }

  public HashMap<JLabel, JComboBox<String>> getLabelToUnit() {
    return labelToUnit;
  }

  public void setLabelToUnit(HashMap<JLabel, JComboBox<String>> labelToUnit) {
    this.labelToUnit = labelToUnit;
  }
}
