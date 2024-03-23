package controller;

import java.awt.Point;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.FlowCalc;
import model.GatheringSystem;
import model.Graph;
import model.Mueller;
import model.Node;
import model.PanHandleA;
import model.PanHandleB;
import model.Pipe;
import model.PressureEquation;
import model.Units;
import model.Weymouth;

/**
 * The Message Dispatch class handles the dispatch and receipt of messages from the display package
 * to the model package. It processes the string data and enumerated actions and then executes code
 * in both packages. In order to perform an action it generates an entirely new graph object and
 * then modifies it and passes it back to the controller.
 * 
 * @author John Coleman
 * @author Nick Chen
 *
 */
public class MessageDispatch {
  private DecimalFormat precision = new DecimalFormat("#.####");
  private int nextNodeID = 0;
  private int nextPipeID = 0;
  private boolean isModifyingFile;
  private boolean isModifyingUi;
  private boolean isFetchingData;
  private boolean isRunningCalculation;

  /**
   * Converts thrown exceptions into strings.
   * 
   * @param e the exception which occurred.
   * @return the thrown exception as a string
   */
  private String exceptionToString(Exception e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    String strace = sw.toString();
    return strace;
  }

  /**
   * Creates an instance of a particular extension of the pressure equation class for use in
   * calculations.
   * 
   * @param equationName the name of the pressure equation.
   * @return the pressure equation specified by name.
   */
  private PressureEquation getPressureEquation(String equationName) {
    switch (equationName) {
      case "Mueller":
        return new Mueller();
      case "Panhandle A":
        return new PanHandleA();
      case "Panhandle B":
        return new PanHandleB();
      case "Weymouth":
        return new Weymouth();
      default:
        throw new IllegalArgumentException("Pressure equation name invalid.");
    }
  }

  /**
   * Sets a parameter of the calculation panel when they are saved for the entire gathering system.
   * 
   * @param paramName the name of the parameter.
   * @param paramValue the numerical value of the parameter.
   * @param paramUnit the unit of the parameter.
   * @param calc the model object which executes calculations and maps to the calculation panel.
   * @return
   */
  private boolean setCalculationParameters(String paramName, String paramValue, String paramUnit,
      FlowCalc calc) {
    switch (paramName) {
      case "Efficiency":
        calc.setEfficiency(Double.valueOf(paramValue));
        // unitless
        return true;
      case "Specific Gravity":
        calc.setGravity(Double.valueOf(paramValue));
        // TODO needs units
        return true;
      case "Base Pressure":
        calc.setBasePressure(
            Units.convert(Double.valueOf(paramValue), paramUnit, "psia", "pressure"));
        return true;
      case "Average Temperature":
        calc.setAverageTemperature(
            Units.convert(Double.valueOf(paramValue), paramUnit, "R", "temperature"));
        return true;
      case "Base Temperature":
        calc.setBaseTemperature(
            Units.convert(Double.valueOf(paramValue), paramUnit, "R", "temperature"));
        return true;
      case "Z-Factor":
        calc.setAverageZ(Double.valueOf(paramValue));
        return true;
      case "Pressure Gradient":
        calc.setPressureGradient(Double.valueOf(paramValue));
        // TODO needs units
        return true;
      case "Friction Factor":
        calc.setFriction(Double.valueOf(paramValue));
        return true;
      default:
        return false;
    }
  }

  /**
   * Retrieves the specified node parameter and converts it from model units to the units currently
   * in use in the display.
   * 
   * @param node the node which the desired parameter is taken from.
   * @param paramName the name of the parameter
   * @param unitOut the unit currently in use in the display.
   * @return the value of the specified node parameter
   */
  private String fetchNodeParameter(Node node, String paramName, String unitOut) {
    switch (paramName) {
      case "Name":
        return node.getNodeName();
      case "Inlet Volumes":
        return "" + precision.format(Units.convert(node.getVolumes(), "scfd", unitOut, "volume"));
      case "Gathered Volumes":
        return ""
            + precision.format(Units.convert(node.getGatheredVolumes(), "scfd", unitOut, "volume"));
      case "Total Volumes":
        return ""
            + precision.format(Units.convert(node.getTotalVolumes(), "scfd", unitOut, "volume"));
      case "Inlet Pressure":
        return ""
            + precision.format(Units.convert(node.getInletPressure(), "psia", unitOut, "pressure"));
      case "Outlet Pressure":
        return "" + precision
            .format(Units.convert(node.getOutletPressure(), "psia", unitOut, "pressure"));
      case "Pressure Differential":
        return "" + precision
            .format(Units.convert(node.getPressureDifferential(), "psia", unitOut, "pressure"));
      case "location":
        return "" + node.getLocation().x + "," + node.getLocation().y;
      default:
        return "UNKNOWN";
    }
  }

  /**
   * Retrieves the unit of a physical attribute in use by a specified node.
   *
   * @param param the physical attribute.
   * @return the unit in use.
   */
  private String fetchNodeUnit(String param) {
    Units systemUnits = GatherX.getCurrentGatheringSystem().getUnits();
    switch (param) {
      case "Name":
        return "N/A";
      case "Inlet Volumes":
      case "Gathered Volumes":
      case "Total Volumes":
        return systemUnits.getNodeVolumeUnit();
      case "Inlet Pressure":
      case "Outlet Pressure":
      case "Pressure Differential":
        return systemUnits.getNodePressureUnit();
      default:
        return "UNKNOWN";
    }
  }

  /**
   * Sets a single node parameter and converts from display to model units.
   * 
   * @param node the specified node
   * @param param the node parameter to set
   * @param value the quantity of the parameter
   * @param unitIn the display unit
   * @return if the parameter set was successful.
   */
  private boolean setNodeParameter(Node node, String param, String value, String unitIn) {
    Units systemUnits = GatherX.getCurrentGatheringSystem().getUnits();
    node.setState("success");
    switch (param) {
      case "Name":
        node.setNodeName(value);
        return true;
      case "Inlet Volumes":
        node.setNodeVolumes(Units.convert(Double.parseDouble(value), unitIn, "scfd", "volume"));
        systemUnits.setNodeVolumeUnit(unitIn);
        return true;
      case "Total Volumes":
        return true;
      case "Outlet Pressure":
        // node.setOutletPressure(Units.convert(Double.valueOf(value), unitIn, "psia", "pressure"));
        // //set by inlet or pressure differential
        return true;
      case "Inlet Pressure":
        node.setInletPressure(Units.convert(Double.parseDouble(value), unitIn, "psia", "pressure"));
        return true;
      case "Pressure Differential":
        // TODO CAN'T SET PRESSURE DIFFERENTIAL, WHAT IS THE GOAL?
        // node.setPressureDifferential(
        // Units.convert(Double.valueOf(value), unitIn, "psia", "pressure"));
        // systemUnits.setNodeVolumeUnit(unitIn); //TODO needs its own unit set later probably
        return true;
      default:
        return false;
    }
  }

  /**
   * Sets a single pipe parameter and converts from display to model units.
   * 
   * @param pipe the specified pipe
   * @param param the physical attribute
   * @param unitOut the unit currently in use in the display.
   * @return the value of the specified pipe parameter.
   */
  private String fetchPipeParameter(Pipe pipe, String param, String unitOut) {
    switch (param) {
      case "Name":
        return pipe.getPipeName();
      case "Length":
        return "" + precision.format(Units.convert(pipe.getPipeLength(), "mi", unitOut, "length"));
      case "Inlet Pressure":
        return ""
            + precision.format(Units.convert(pipe.getInPressure(), "psia", unitOut, "pressure"));
      case "Outlet Pressure":
        return ""
            + precision.format(Units.convert(pipe.getOutPressure(), "psia", unitOut, "pressure"));
      case "Pressure Differential":
        return "" + precision
            .format(Units.convert(pipe.getPressureDifferential(), "psia", unitOut, "pressure"));
      case "Pressure Gradient":
        return "" + precision.format(pipe.getPressureGradient());
      // TODO pressure gradient has multiple units but currently only uses psi/mi
      case "Efficiency":
        return "" + precision.format(pipe.getEfficiency());
      case "Estimated Liquid Dropout":
        return "" + precision.format(pipe.getLiquidDropout());
      case "Average Velocity":
        return "" + precision.format(Math.abs(pipe.getVelocity()));
      case "Diameter":
        return ""
            + precision.format(Units.convert(pipe.getPipeDiameter(), "in", unitOut, "length"));
      case "Specific Gravity":
        return "" + precision.format(pipe.getGravity());
      case "Average Temperature":
        return "" + precision.format(Units.convert(pipe.getAvgTemp(), "R", unitOut, "temperature"));
      case "Base Temperature":
        return ""
            + precision.format(Units.convert(pipe.getBaseTemp(), "R", unitOut, "temperature"));
      case "Base Pressure":
        return ""
            + precision.format(Units.convert(pipe.getBasePressure(), "psia", unitOut, "pressure"));
      case "Z-Factor":
        return "" + precision.format(pipe.getZ());
      case "Flow Rate":
        return "" + precision
            .format(Math.abs(Units.convert(pipe.getPipeFlowRate(), "scfd", unitOut, "volume"))); // needs
      // unit
      // conversion
      default:
        return "UNKNOWN";
    }
  }

  /**
   * Retrieves the unit of a physical attribute in use by a specified pipe.
   * 
   * @param pipe the specified pipe.
   * @param value the variable to retrieve the unit of.
   * @return the unit in use.
   */
  private String fetchPipeUnit(Pipe pipe, String value) {
    Units systemUnits = GatherX.getCurrentGatheringSystem().getUnits();
    switch (value) {
      case "Name":
      case "Specific Gravity":
      case "Z-Factor":
      case "Friction Factor":
      case "Efficiency":
        return "N/A";
      case "Length":
        return systemUnits.getLengthUnit();
      case "Inlet Pressure":
      case "realInPressure":
        return systemUnits.getInletPressureUnit();
      case "Outlet Pressure":
      case "realOutPressure":
        return systemUnits.getOutletPressureUnit();
      case "Diameter":
        return systemUnits.getDiameterUnit();
      case "Average Temperature":
        return systemUnits.getAverageTemperatureUnit();
      case "Base Temperature":
        return systemUnits.getBaseTemperatureUnit();
      case "Base Pressure":
        return systemUnits.getBasePressureUnit();
      case "Flow Rate":
        return systemUnits.getNodeVolumeUnit();
      default:
        return "UNKNOWN";
    }
  }

  /**
   * Sets a specified pipe parameter and converts from display to model units.
   * 
   * @param pipe the pipe
   * @param param the specified pipe parameter
   * @param value the quantity of the parameter
   * @param unitIn the display unit.
   * @return if the parameter is successfully set.
   */
  private boolean setPipeParameter(Pipe pipe, String param, String value, String unitIn) {
    Units systemUnits = GatherX.getCurrentGatheringSystem().getUnits();
    pipe.setState("success");
    switch (param) {
      case "Name":
        pipe.setPipeName(value);
        return true;
      case "Length":
        pipe.setPipeLength(Units.convert(Double.valueOf(value), unitIn, "mi", "length"));
        systemUnits.setLengthUnit(unitIn);
        return true;
      case "Inlet Pressure":
        pipe.setInPressure(Units.convert(Double.valueOf(value), unitIn, "psia", "pressure"));
        systemUnits.setInletPressureUnit(unitIn);
        return true;
      case "Outlet Pressure":
        pipe.setOutPressure(Units.convert(Double.valueOf(value), unitIn, "psia", "pressure"));
        systemUnits.setOutletPressureUnit(unitIn);
        return true;
      case "Diameter":
        pipe.setDiameter(Units.convert(Double.valueOf(value), unitIn, "in", "length"));
        systemUnits.setDiameterUnit(unitIn);
        return true;
      case "Efficiency":
        pipe.setPipeEff(Double.valueOf(value));
        // unitless
        return true;
      case "Specific Gravity":
        pipe.setGravity(Double.valueOf(value));
        // unitless
        return true;
      case "Average Temperature":
        pipe.setAvgTemp(Units.convert(Double.valueOf(value), unitIn, "R", "temperature"));
        systemUnits.setAverageTemperatureUnit(unitIn);
        return true;
      case "Base Temperature":
        pipe.setBaseTemp(Units.convert(Double.valueOf(value), unitIn, "R", "temperature"));
        systemUnits.setBaseTemperatureUnit(unitIn);
        return true;
      case "Base Pressure":
        pipe.setBasePressure(Units.convert(Double.valueOf(value), unitIn, "psia", "pressure"));
        systemUnits.setBasePressureUnit(unitIn);
        return true;
      case "Z-Factor":
        pipe.setZ(Double.valueOf(value));
        // unitless
        return true;
      case "Flow Rate":
        // can't set
        return true;
      default:
        return true;
    }
  }

  /**
   * Sets all pipe parameters in the gathering system to a single unit and quantity.
   * 
   * @param param the specified pipe parameter
   * @param value the quantity of the parameter
   * @param unitIn the display unit.
   * @param pipes a list of the pipes
   * @return if the parameters are successfully set.
   */
  private boolean setAllPipeParameters(String param, String value, String unitIn,
      List<Pipe> pipes) {
    Units systemUnits = GatherX.getCurrentGatheringSystem().getUnits();
    switch (param) {
      case "Specific Gravity":
        GatherX.getCurrentGatheringSystem().getCalculation().setGravity(Double.parseDouble(value));
        for (Pipe pipe : pipes) {
          pipe.setGravity(Double.parseDouble(value));
        }
        return true;
      // unitless
      case "Average Temperature":
        GatherX.getCurrentGatheringSystem().getCalculation()
            .setAverageTemperature(Double.parseDouble(value));
        for (Pipe pipe : pipes) {
          pipe.setAvgTemp(Units.convert(Double.parseDouble(value), unitIn, "R", "temperature"));
          systemUnits.setAverageTemperatureUnit(unitIn);
        }
        return true;
      case "Base Temperature":
        GatherX.getCurrentGatheringSystem().getCalculation()
            .setBaseTemperature(Double.parseDouble(value));
        for (Pipe pipe : pipes) {
          pipe.setBaseTemp(Units.convert(Double.parseDouble(value), unitIn, "R", "temperature"));
          systemUnits.setBaseTemperatureUnit(unitIn);
        }
        return true;
      case "Base Pressure":
        GatherX.getCurrentGatheringSystem().getCalculation()
            .setBasePressure(Double.parseDouble(value));
        for (Pipe pipe : pipes) {
          pipe.setBasePressure(
              Units.convert(Double.parseDouble(value), unitIn, "psia", "pressure"));
          systemUnits.setBasePressureUnit(unitIn);
        }
        return true;
      case "Z-Factor":
        GatherX.getCurrentGatheringSystem().getCalculation().setAverageZ(Double.parseDouble(value));
        for (Pipe pipe : pipes) {
          pipe.setZ(Double.parseDouble(value));
        }
        // unitless
        return true;
      case "Pressure Gradient":
        GatherX.getCurrentGatheringSystem().getCalculation()
            .setPressureGradient(Double.parseDouble(value));
        for (Pipe pipe : pipes) {

          pipe.setMaxPressureGradient(Double.parseDouble(value));
          // TODO handle unit conversion
        }
        return true;
      case "Efficiency":
        GatherX.getCurrentGatheringSystem().getCalculation()
            .setEfficiency(Double.parseDouble(value));
        for (Pipe pipe : pipes) {
          pipe.setEfficiency(Double.parseDouble(value));
        }
        // unitless
        return true;
      default:
        return false;
    }
  }

  /**
   * Creates a new instance of the gathering system class and replaces the currently held gathering
   * system in the gatherX controller class in order to update the graph accordingly and sends a
   * message to the user interface if the update attempt fails.
   * 
   * @param message the action and data from the user interface to be taken.
   */
  public void dispatch(Message message) {
    // gets currently selected system information
    GatheringSystem currentSystem = GatherX.getCurrentGatheringSystem();
    Graph currentGraph = currentSystem.getGraph();
    List<Node> currentNodes = currentGraph.getNodes();
    List<Pipe> currentPipes = currentGraph.getPipes();
    List<String> nodeNames = currentGraph.getNodeNames();
    List<String> pipeNames = currentGraph.getPipeNames();

    // Graph currentSystemGraph = currentSystem.getGraph();
    FlowCalc calcSheet = currentSystem.getCalculation();

    PressureEquation peq = currentSystem.getCalculation().getCurrentEquation();
    isModifyingFile = false;
    isModifyingUi = false;
    isFetchingData = false;
    isRunningCalculation = false;

    // log modified states for undo button
    if (message.getAction() == Action.NEW_NODE || message.getAction() == Action.DELETE_PIPE
        || message.getAction() == Action.NEW_PIPE || message.getAction() == Action.DELETE_NODE) {
      GatherX.stateModified();
    }
    switch (message.getAction()) {
      case ECHO:
        message.reply(message.getData());
        break;
      // message data is "nodetype:nodex,nodey"
      case NEW_NODE:
        isModifyingUi = true;
        String data = message.getData();
        String[] sections = data.split(":");
        String[] coordinates = sections[1].split(",");
        String nodeType = sections[0];
        Node node = new Node();
        Point location =
            new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
        node.setNodeType(nodeType);
        node.setLocation(location);

        String newNodeName = "node " + nextNodeID;
        while (nodeNames.contains(newNodeName)) {
          nextNodeID++;
          newNodeName = "node " + nextNodeID;
        }
        node.setNodeName(newNodeName);
        currentNodes.add(node);
        message.reply("NEW_NODE: " + Integer.toString(nextNodeID));
        nextNodeID++;
        break;
      // message data is "nodeName:paramName,paramName"
      // reply data is paramValue,paramValue:paramUnit,paramUnit
      case FETCH_NODE_DATA:
        isFetchingData = true;
        data = message.getData();
        sections = data.split(":");
        String nodeName = sections[0];
        node = currentGraph.getNode(nodeName);
        if (node == null) {
          message.reply("No such node.");
          break;
        }

        String[] desiredValues = sections[1].split(",");
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < desiredValues.length; i++) {
          if (i != 0) {
            ret.append(",");
          }
          ret.append(fetchNodeUnit(desiredValues[i]).replace(",", ""));
        }
        String[] desiredUnits = ret.toString().split(",");
        ret.append(":");
        for (int i = 0; i < desiredValues.length; i++) {
          if (i != 0) {
            ret.append(",");
          }
          ret.append(fetchNodeParameter(node, desiredValues[i], desiredUnits[i]).replace(",", ""));
        }
        message.reply(ret.toString());
        break;
      // message data is
      // "nodeName:paramNames,paramNames:paramValues,paramValues:paramUnits,paramUnits"
      case SET_NODE_DATA:
        data = message.getData();
        sections = data.split(":");
        if (sections.length != 4) {
          message.reply("SET_NODE_DATA: Improperly formatted request.");
          break;
        }
        nodeName = sections[0];
        node = currentGraph.getNode(nodeName);
        if (node == null) {
          message.reply("SET_NODE_DATA: No such node.");
          break;
        }

        String[] paramNames = sections[1].split(",");
        String[] paramValues = sections[2].split(",");
        String[] paramUnits = sections[3].split(",");
        if (paramNames.length != paramValues.length || paramNames.length != paramUnits.length) {
          message.reply("SET_NODE_DATA: Name/value/units mismatch.");
          break;
        }
        ret = new StringBuilder();
        for (int i = 0; i < paramValues.length; i++) {
          if (i != 0) {
            ret.append(",");
          }
          try {
            if (!setNodeParameter(node, paramNames[i], paramValues[i], paramUnits[i])) {
              ret.append("SET_NODE_DATA: Error " + paramNames[i]);
            }
          } catch (IllegalArgumentException e) {
            ret.append(e.getMessage().replace(",", ""));
          }
        }
        if (nodeName.equals(GatherX.getUiRegulator().getSelectedObjectName())) {
          int indexOfNodeName = Arrays.asList(paramNames).indexOf("Name");
          GatherX.getUiRegulator().setSelectedObjectName(paramValues[indexOfNodeName]);
        }
        message.reply(ret.toString());
        break;
      // message data is "nodeName"
      case DELETE_NODE:
        isModifyingUi = true;
        nodeName = message.getData();
        if (nodeName.equals(GatherX.getUiRegulator().getSelectedObjectName())
            && GatherX.getUiRegulator().getSelectedObjectType().equals("Node")) {
          GatherX.getUiRegulator().setSelectedObjectName(null);
        }
        node = currentGraph.getNode(nodeName);
        if (node != null) {
          currentNodes.remove(node);
          message.reply("DELETE_NODE: Success " + message.getData());
          // checks to make sure all pipes have two end nodes and deletes any if they do not
          List<Pipe> pipesToDelete = new ArrayList<>();
          for (Pipe p : currentPipes) {
            Node endNode1 = p.getEnd1();
            Node endNode2 = p.getEnd2();

            if (!(currentNodes.contains(endNode1) && currentNodes.contains(endNode2))) {
              pipesToDelete.add(p);
            }
          }
          for (Pipe pipe : pipesToDelete) {
            // deletes any pipes where two end nodes are not attached
            if (pipe.getPipeName().equals(GatherX.getUiRegulator().getSelectedObjectName())) {
              GatherX.getUiRegulator().setSelectedObjectName(null);
            }
            currentPipes.remove(pipe);
            message.reply("DELETE_PIPE: Success ");
          }
        } else {
          message.reply("DELETE_NODE: Node does not exist.", true);
        }
        break;
      // message data is "nodeName,nodeName:nodex,nodey"
      case NEW_PIPE:
        isModifyingUi = true;
        data = message.getData();
        String[] idCoordData = data.split(":");
        coordinates = idCoordData[1].split(",");
        location = new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
        sections = idCoordData[0].split(",");
        if (sections.length != 2) {
          message.reply("NEW_PIPE: Please give start and end node", true);
          break;
        }
        Node firstNode = currentGraph.getNode(sections[0]);
        Node secondNode = currentGraph.getNode(sections[1]);
        boolean exists = false;
        // Checks to make sure we don't have a pipe there already
        for (Pipe pipe : currentPipes) {
          Node end1 = pipe.getEnd1();
          Node end2 = pipe.getEnd2();
          if ((firstNode.equals(end1) && secondNode.equals(end2))
              || (firstNode.equals(end2) && secondNode.equals(end1))) {
            exists = true;
            break;
          }
        }
        if (exists) {
          message.reply("Pipe already exists between those two nodes");
        } else {
          Pipe pipe = new Pipe();
          String newPipeName = "pipe " + nextPipeID;
          while (pipeNames.contains(newPipeName)) {
            nextPipeID++;
            newPipeName = "pipe " + nextPipeID;
          }
          pipe.setPipeName("pipe " + nextPipeID);
          pipe.setEnd1(firstNode);
          pipe.setEnd2(secondNode);
          firstNode.addNeighbor(secondNode);
          secondNode.addNeighbor(firstNode);
          pipe.setLocation(location);
          currentPipes.add(pipe);
          message.reply("NEW_PIPE: " + newPipeName);
          nextPipeID++;
        }
        break;
      // message data is "pipeName"
      case DELETE_PIPE:
        String pipeName = message.getData();
        if (pipeName.equals(GatherX.getUiRegulator().getSelectedObjectName())
            && GatherX.getUiRegulator().getSelectedObjectType().equals("Pipe")) {
          GatherX.getUiRegulator().setSelectedObjectName(null);
        }
        isModifyingUi = true;
        Pipe pipe = currentGraph.getPipe(pipeName);
        if (pipe != null) {
          firstNode = pipe.getEnd1();
          secondNode = pipe.getEnd2();
          firstNode.deleteNeighbor(secondNode);
          secondNode.deleteNeighbor(firstNode);
          currentPipes.remove(pipe);
          message.reply("DELETE_PIPE: Success " + message.getData());
        } else {
          message.reply("DELETE_PIPE: Pipe does not exist.", true);
        }
        break;
      // message data is "pipeName:paramName,paramName"
      // reply is "paramValue,paramValue:paramUnit,paramUnit"
      case FETCH_PIPE_DATA:
        isFetchingData = true;
        data = message.getData();
        sections = data.split(":");
        pipeName = sections[0];
        pipe = currentGraph.getPipe(pipeName);
        if (pipe == null) {
          message.reply("FETCH_PIPE_DATA: No such pipe.");
          break;
        }
        desiredValues = sections[1].split(",");
        ret = new StringBuilder();
        for (int i = 0; i < desiredValues.length; i++) {
          if (i != 0) {
            ret.append(",");
          }
          ret.append(fetchPipeUnit(pipe, desiredValues[i]).replace(",", ""));
        }
        desiredUnits = ret.toString().split(",");
        ret.append(":");
        for (int i = 0; i < desiredValues.length; i++) {
          if (i != 0) {
            ret.append(",");
          }
          ret.append(fetchPipeParameter(pipe, desiredValues[i], desiredUnits[i]).replace(",", ""));
        }
        message.reply(ret.toString());
        break;
      // message data is
      // "pipeName:paramNames,paramNames:paramValues,paramValues:paramUnit,paramUnit:paramType,paramType"
      case SET_PIPE_DATA:
        data = message.getData();
        sections = data.split(":");
        if (sections.length != 4) {
          message.reply("SET_PIPE_DATA: Improperly formatted request.");
          break;
        }
        pipeName = sections[0];
        pipe = currentGraph.getPipe(pipeName);
        if (pipe == null) {
          message.reply("SET_PIPE_DATA: No such pipe: " + pipeName);
          break;
        }
        paramNames = sections[1].split(",");
        paramValues = sections[2].split(",");
        paramUnits = sections[3].split(",");
        if (paramNames.length != paramValues.length || paramNames.length != paramUnits.length) {
          message.reply("SET_PIPE_DATA: Name/value/units mismatch.");
          break;
        }
        ret = new StringBuilder();
        for (int i = 0; i < paramValues.length; i++) {
          try {
            if (!setPipeParameter(pipe, paramNames[i], paramValues[i], paramUnits[i])) {
              ret.append("SET_PIPE_DATA: Error");
            }
          } catch (IllegalArgumentException e) {
            ret.append(e.getMessage().replace(",", ""));
          }
        }
        if (pipeName.equals(GatherX.getUiRegulator().getSelectedObjectName())) {
          int indexOfPipeName = Arrays.asList(paramNames).indexOf("Name");
          GatherX.getUiRegulator().setSelectedObjectName(paramValues[indexOfPipeName]);
        }
        message.reply(ret.toString());
        break;
      // message data is
      // "paramNames,paramNames:paramValues,paramValues:paramUnit,paramUnit:paramType,paramType"
      case SET_ALL_PIPE_DATA:
        isModifyingUi = true;
        data = message.getData();
        sections = data.split(":");
        if (sections.length != 3) {
          message.reply("SET_ALL_PIPE_DATA: Improperly formatted request.");
          break;
        }
        paramNames = sections[0].split(",");
        paramValues = sections[1].split(",");
        paramUnits = sections[2].split(",");
        if (paramNames.length != paramValues.length || paramNames.length != paramUnits.length) {
          message.reply("SET_PIPE_DATA: Name/value/units mismatch.");
          break;
        }
        for (int index = 0; index < paramNames.length; index++) {
          this.setCalculationParameters(paramNames[index], paramValues[index], paramUnits[index],
              calcSheet);
        }
        for (int index = 0; index < paramNames.length; index++) {
          this.setAllPipeParameters(paramNames[index], paramValues[index], paramUnits[index],
              currentPipes);
        }
        break;
      case NEXT_GRAPH:
        isModifyingUi = true;
        isModifyingFile = true;
        currentSystem.nextGraph();
        message.reply(new StringBuilder().append(String.valueOf(currentSystem.getGraphIndex()))
            .append(String.valueOf(currentSystem.getGraphTimelineSize())).toString());
        break;
      case PREV_GRAPH:
        isModifyingUi = true;
        isModifyingFile = true;
        currentSystem.prevGraph();
        message.reply(new StringBuilder().append(String.valueOf(currentSystem.getGraphIndex()))
            .append(String.valueOf(currentSystem.getGraphTimelineSize()))
            .append(currentSystem.getGraph().toString()).toString());
        break;
      case MOVE_GRAPH:
        isModifyingUi = true;
        isModifyingFile = true;
        currentSystem.moveGraph(Integer.parseInt(message.getData()));
        message.reply(new StringBuilder().append(String.valueOf(currentSystem.getGraphIndex()))
            .append(String.valueOf(currentSystem.getGraphTimelineSize()))
            .append(currentSystem.getGraph().toString()).toString());
        break;
      // message data is "pressure equation"
      case VOLUMES_AND_PRESSURES_CALC:
        isModifyingUi = true;
        isModifyingFile = true;
        isRunningCalculation = true;
        String pressureEquationLabel = message.getData();
        try {
          peq = getPressureEquation(pressureEquationLabel);
          ArrayList<String> preventedIssues = currentSystem.pressuresVolumesCalc(peq);
          StringBuilder replyBuilder = new StringBuilder();
          for (int index = 0; index < preventedIssues.size(); index++) {
            if (index != 0) {
              replyBuilder.append(",");
            }
            replyBuilder.append(preventedIssues.get(index));
          }
          message.reply("Graph Errors@" + replyBuilder.toString());
        } catch (RuntimeException re) {
          message.reply(exceptionToString(re), true);
        }
        break;
      // message data is "pressure equation"
      case PRESSURES_AND_EFFICIENCIES_CALC:
        isModifyingUi = true;
        isModifyingFile = true;
        isRunningCalculation = true;
        pressureEquationLabel = message.getData();
        try {
          peq = getPressureEquation(pressureEquationLabel);
          ArrayList<String> preventedIssues = currentSystem.pressuresEfficienciesCalc(peq);
          StringBuilder replyBuilder = new StringBuilder();
          for (int index = 0; index < preventedIssues.size(); index++) {
            if (index != 0) {
              replyBuilder.append(",");
            }
            replyBuilder.append(preventedIssues.get(index));
          }
          message.reply("Graph Errors@" + replyBuilder.toString());
        } catch (RuntimeException re) {
          message.reply(exceptionToString(re), true);
        }
        break;
      // message data is ""
      case NEW_FILE:
        isModifyingFile = true;
        try {
          GatherX.newGatheringSystem();
          message.reply("NEW_FILE: Success");
        } catch (RuntimeException re) {
          message.reply(exceptionToString(re), true);
        }
        break;
      // message data is "filename"
      case OPEN_FILE:
        isModifyingFile = true;
        data = message.getData();
        String fileName = data;
        if (!fileName.contentEquals("")) {
          try {
            GatherX.openGatheringSystem(fileName);
            message.reply("LOAD_FILE: Success");
          } catch (RuntimeException re) {
            message.reply(exceptionToString(re), true);
          }
        }
        break;
      case IMPORT_CSV:
        isModifyingFile = true;
        data = message.getData();
        fileName = data;
        if (!fileName.contentEquals("")) {
          try {
            GatherX.importGatheringSystem(fileName);
            message.reply("IMPORT_CSV: Success");
          } catch (RuntimeException re) {
            message.reply(exceptionToString(re), true);
          }
        }
        break;
      case EXPORT_CSV:
        isModifyingFile = true;
        data = message.getData();
        fileName = data;
        if (!fileName.contentEquals("")) {
          try {
            GatherX.exportGatheringSystem(fileName);
            message.reply("EXPORT_CSV: Success");
          } catch (RuntimeException re) {
            message.reply(exceptionToString(re), true);
          }
        }
        break;
      case CLOSE_FILE:
        isModifyingFile = true;
        try {
          GatherX.closeGatheringSystem();
          message.reply("CLOSE_FILE: Success");
        } catch (RuntimeException re) {
          message.reply(exceptionToString(re), true);
        }
        break;
      case SAVE_FILE:
        isModifyingFile = true;
        try {
          GatherX.saveGatheringSystem();
          message.reply("SAVE_FILE: Success");
        } catch (RuntimeException re) {
          message.reply(exceptionToString(re), true);
        }
        break;
      // message data is "filename"
      case SAVE_FILE_AS:
        isModifyingFile = true;
        data = message.getData();
        fileName = data;
        if (!fileName.contentEquals("")) {
          try {
            GatherX.saveAsGatheringSystem(fileName);
            message.reply("SAVE_FILE_AS: Success");
          } catch (RuntimeException re) {
            message.reply(exceptionToString(re), true);
          }
        }
        break;
      case SAVE_ALL_FILES:
        isModifyingFile = true;
        data = message.getData();
        fileName = data;
        if (!fileName.contentEquals("")) {
          try {
            GatherX.saveAllGatheringSystems(fileName);
            message.reply("SAVE_FILE_AS: Success");
          } catch (RuntimeException re) {
            message.reply(exceptionToString(re), true);
          }
        }
        break;
      default:
        message.reply("UNKNOWN COMMAND", true);
        break;
    }
    // updates currently selected system information
    if (!isModifyingFile && !isFetchingData) {
      currentGraph.setNodes(currentNodes);
      currentGraph.setPipes(currentPipes);
      currentSystem.setGraph(currentGraph);
      currentSystem.setCalculation(calcSheet);
      GatherX.setCurrentGatheringSystem(currentSystem, currentSystem.getName());
    }
    if (isModifyingUi) {
      GatherX.refreshInterface();
    }
  }

  /**
   * Filters a message constructed using an array of strings for "," ":" and other symbols when
   * attempting to set node and pipe parameters from the display data panel.
   * 
   * @param message the action and data from the user interface to be taken.
   * @param bool TODO
   */
  public void dispatchWithFilter(Message message, boolean bool) {
    String[] sortedData = message.getCatagorizedData();
    GatheringSystem currentGatheringSystem = GatherX.getCurrentGatheringSystem();
    Graph currentGraph = currentGatheringSystem.getGraph();
    List<Node> currentNodes = currentGraph.getNodes();
    List<Pipe> currentPipes = currentGraph.getPipes();
    switch (message.getAction()) {
      case SET_NODE_DATA:
        if (sortedData.length != 4) {
          message.reply(
              "A colon has been entered in the data panel and must be removed before the data can be saved",
              bool);
          break;
        } else {
          String previousNodeName = sortedData[0];
          String[] paramNames = sortedData[1].split(",");
          String[] paramValues = sortedData[2].split(",");
          String[] paramUnits = sortedData[3].split(",");
          StringBuilder setNodeErrors = new StringBuilder("");
          if (paramNames.length != paramValues.length || paramNames.length != paramUnits.length) {
            message.reply("This program uses comma separated values. Please remove commas from "
                + "data entry fields.", bool);
            break;
          } else {
            String savedName = paramValues[0];
            if (!savedName.matches("^[\\w\\-\\s]+$")) {
              setNodeErrors.append("Node name (" + savedName
                  + ") is invalid. Only alphanumeric characters are accepted.");
              setNodeErrors.append(":");
            }
            if (paramValues[0].length() > 20) {
              setNodeErrors.append("Node name (" + savedName
                  + ") is invalid. Node names must be less than 21 characters long.");
              setNodeErrors.append(":");
            }
            for (Node node : currentNodes) {
              String nodeName = node.getNodeName();
              if (savedName.contentEquals(nodeName) && !savedName.contentEquals(previousNodeName)) {
                setNodeErrors.append("Node name (" + savedName + ") is the same as (" + nodeName
                    + ") and invalid. Node names must be unique.");
                setNodeErrors.append(":");
              }
            }
            for (int index = 1; index < paramValues.length; index++) {
              if (!paramValues[index].matches("^\\d*\\.?\\d+$")) {
                setNodeErrors.append("The value entered for " + paramNames[index]
                    + " is invalid. Only numbers without commas are accepted.");
                setNodeErrors.append(":");
              }
            }
            /*
             * Check if the inlet/outlet pressure set induces an negative pressure differential.
             * this value cannot be set directly, only calculated as the difference between an inlet
             * and outlet pressure, and as a result should already have bad characters filtered and
             * can be converted into a double and back into a string mid process.
             */
            if (setNodeErrors.toString().equals("")) {
              String nodeName = sortedData[0];
              Node node = currentGraph.getNode(nodeName);
              String nodeType = node.getType();
              double inletPressure = Double.parseDouble(paramValues[3]);
              double pressureDifferential = Double.parseDouble(paramValues[2]);
              double outletPressure = inletPressure;
              if (nodeType.equals("Control Valve")) {
                outletPressure = inletPressure - pressureDifferential;
              } else if (nodeType.equals("Compressor")) {
                outletPressure = inletPressure + pressureDifferential;
              }
              if (!String.valueOf(outletPressure).matches("^^\\d*\\.?\\d+$")) {
                setNodeErrors.append("The resulting outlet pressure (" + outletPressure
                    + ") is invalid. Node pressures must be 0 or positive.");
                setNodeErrors.append(":");
              }
            }
            if (setNodeErrors.toString().equals("")) {
              Message reformattedMessage = new Message(message.getAction(),
                  sortedData[0] + ":" + sortedData[1] + ":" + sortedData[2] + ":" + sortedData[3]);
              this.dispatch(reformattedMessage);
            } else {
              message.reply(setNodeErrors.toString(), bool);
            }
          }
        }
        break;
      case SET_PIPE_DATA:
        if (sortedData.length != 4) {
          message.reply(
              "A colon has been entered in the data panel and must be removed before the data can be saved",
              bool);
          break;
        } else {
          String previousPipeName = sortedData[0];
          String[] paramNames = sortedData[1].split(",");
          String[] paramValues = sortedData[2].split(",");
          String[] paramUnits = sortedData[3].split(",");
          StringBuilder setPipeErrors = new StringBuilder("");
          if (paramNames.length != paramValues.length || paramNames.length != paramUnits.length) {
            message.reply("This program uses comma separated values. Please remove commas from "
                + "data entry fields.", bool);
            break;
          } else {
            String savedName = paramValues[0];
            if (!savedName.matches("^[\\w\\-\\s]+$")) {
              setPipeErrors.append("Pipe name (" + paramValues[0]
                  + ") is invalid. Only alphanumeric characters are accepted.");
              setPipeErrors.append(":");
            }
            if (savedName.length() > 20) {
              setPipeErrors.append("Pipe name (" + paramValues[0]
                  + ") is invalid. Names must be less than 21 characters long.");
              setPipeErrors.append(":");
            }
            for (Pipe pipe : currentPipes) {
              String pipeName = pipe.getPipeName();
              if (savedName.contentEquals(pipeName) && !savedName.contentEquals(previousPipeName)) {
                setPipeErrors.append("Pipe name (" + savedName + ") is the same as (" + pipeName
                    + ") and invalid. Pipe names must be unique.");
                setPipeErrors.append(":");
              }
            }
            for (int index = 1; index < paramValues.length; index++) {
              if (!paramValues[index].matches("^\\d*\\.?\\d+$")) {
                setPipeErrors.append("Value " + paramNames[index]
                    + " is invalid. Only numbers without commas are accepted.");
                setPipeErrors.append(":");
              }
            }
            if (setPipeErrors.toString().equals("")) {
              Message reformattedMessage = new Message(message.getAction(),
                  sortedData[0] + ":" + sortedData[1] + ":" + sortedData[2] + ":" + sortedData[3]);
              this.dispatch(reformattedMessage);
            } else {
              message.reply(setPipeErrors.toString(), bool);
            }
          }
        }
        break;
      case SET_ALL_PIPE_DATA:
        if (sortedData.length != 3) {
          message.reply(
              "A colon has been entered in the calculation panel and must be removed before the data can be saved",
              bool);
          break;
        }
        String[] paramNames = sortedData[0].split(",");
        String[] paramValues = sortedData[1].split(",");
        String[] paramUnits = sortedData[2].split(",");
        StringBuilder setAllPipeErrors = new StringBuilder("");
        if (paramNames.length != paramValues.length || paramNames.length != paramUnits.length) {
          message.reply(
              "This program uses comma seperated values. Please remove commas from data entry fields.",
              bool);
          break;
        } else {
          for (int index = 1; index < paramValues.length; index++) {
            if (!paramValues[index].matches("^\\d*\\.?\\d+$")) {
              setAllPipeErrors.append("Value " + paramNames[index]
                  + " is invalid. Only numbers without commas are accepted.");
              setAllPipeErrors.append(":");
            }
          }
          if (setAllPipeErrors.toString().equals("")) {
            Message reformattedMessage = new Message(message.getAction(),
                sortedData[0] + ":" + sortedData[1] + ":" + sortedData[2]);
            this.dispatch(reformattedMessage);
          } else {
            message.reply(setAllPipeErrors.toString(), bool);
          }
        }
        break;
      default:
        message.reply("Improper filtered message dispatch request");
        break;
    }
  }

  public DecimalFormat getPrecision() {
    return precision;
  }

  public void setPrecision(DecimalFormat precision) {
    this.precision = precision;
  }

  public boolean isModifyingFile() {
    return isModifyingFile;
  }

  public void setModifyingFile(boolean isModifyingFile) {
    this.isModifyingFile = isModifyingFile;
  }

  public boolean isModifyingUi() {
    return isModifyingUi;
  }

  public void setModifyingUi(boolean isModifyingUi) {
    this.isModifyingUi = isModifyingUi;
  }

  public boolean isFetchingData() {
    return isFetchingData;
  }

  public void setFetchingData(boolean isFetchingData) {
    this.isFetchingData = isFetchingData;
  }

  public boolean isRunningCalculation() {
    return isRunningCalculation;
  }

  public void setRunningCalculation(boolean isRunningCalculation) {
    this.isRunningCalculation = isRunningCalculation;
  }
}
