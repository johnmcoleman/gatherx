package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Engine Regulator class checks a Graph object before a Gathering System attempts to execute a
 * Flow Calculation method on the Graph to screen for any values which might throw a runtime
 * exception.
 * 
 * @author John Coleman
 */
public class EngineRegulator implements Serializable, Cloneable {
  private static final long serialVersionUID = 1536538825153722183L;
  private String generalMode;
  private String subsetMode;
  private String pressureEquationMode = "Weymouth";

  /**
   * Finds all network related issues on the graph
   * 
   * @param graph the mapping and physical information representing a gas gathering system.
   * @return a list of strings where each string describes a specific network issue.
   */
  public ArrayList<String> checkGraph(Graph graph) {
    ArrayList<String> graphIssues = new ArrayList<>();
    ArrayList<String> nodeIssues = this.checkNodeValues(graph);
    List<String> pipeIssues = this.checkPipeValues(graph);
    // checks for loops and isolated clusters iff there is one outlet and no incorrect pipe
    // connections
    if (nodeIssues.contains("No outlet exists. Minimum one outlet required.")
        || nodeIssues.contains("No inlet exists. Minimum one inlet required.")
        || pipeIssues.contains(
            "Pipes connecting the same two nodes exist. Only one pipe can be placed between two nodes.")
        || pipeIssues.contains(
            "Pipe with the same node at both ends exists. Each pipe must have distinct end nodes.")) {
    } else {
      // check if unconnected nodes are present
      if (graph.isDisjoint()) {
        graphIssues.add(
            "Gathering system is not fully connected. Gathering sytem must be fully connected.");
      }
    }
    graphIssues.addAll(nodeIssues);
    graphIssues.addAll(pipeIssues);
    return graphIssues;
  }

  /**
   * Finds all issues with values contained inside nodes inside the graph.
   * 
   * @param graph the mapping and physical information representing a gas gathering system.
   * @return a list of strings where each string describes a specific node issue.
   */
  public ArrayList<String> checkNodeValues(Graph graph) {
    ArrayList<String> nodeIssues = new ArrayList<>();
    int numOutlets = 0;
    int numInlets = 0;
    // extract all data from every node and see if it contains errors
    for (Node currentNode : graph.getNodes()) {
      String name = currentNode.getNodeName();
      String type = currentNode.getType();
      double volumes = currentNode.getVolumes();
      double gatheredVolumes = currentNode.getGatheredVolumes();
      double totalVolumes = currentNode.getTotalVolumes();
      double pressure = currentNode.getInletPressure();
      // checks for incorrect pressure values
      if (Math.abs(pressure) < 2 * Double.MIN_VALUE || pressure < 0.0) {
        nodeIssues.add("Node " + name + ": Nodes pressures cannot be zero or negative.");
        currentNode.setState("failed");
      }
      // check for incorrect volumes
      if (volumes < 0.0 || gatheredVolumes < 0.0 || totalVolumes < 0.0) {
        nodeIssues.add("Node " + name + ": Inlets and outlets cannot have negative flow rates.");
        currentNode.setState("failed");
      }
      // counts total number of outlets
      if (type.equals("Outlet")) {
        numOutlets++;
      }
      // counts total number of inlets
      if (type.equals("Inlet")) {
        numInlets++;
      }
    }
    // checks for duplicate names in the graph
    Set<String> nameSet = new HashSet<>(graph.getNodeNames());
    if (nameSet.size() < graph.getNumNodes()) {
      nodeIssues.add("Nodes with duplicate names exist. Nodes must have unique names.");
    }
    // checks for no outlet
    if (numOutlets <= 0) {
      nodeIssues.add("No outlet exists. Minimum one outlet required.");
    }
    // checks for no inlet
    if (numInlets <= 0) {
      nodeIssues.add("No inlet exists. Minimum one inlet required.");
    }
    // limits total number of nodes
    if (graph.getNumNodes() >= 500) {
      nodeIssues.add("Too many nodes. Maximum of 500 nodes exceeded.");
    }
    return nodeIssues;
  }

  /**
   * Finds all issues with values contained inside pipes inside the graph.
   * 
   * @param graph the mapping and physical information representing a gas gathering system.
   * @return a list of strings where each string describes a specific pipe issue.
   */
  public List<String> checkPipeValues(Graph graph) {
    List<String> pipeIssues = new ArrayList<>();
    List<Set<Node>> endNodes = new ArrayList<>();
    // extracts all the end nodes in the graph
    for (Pipe currentPipe : graph.getPipes()) {
      Set<Node> endNodePair = new HashSet<>();
      endNodePair.add(currentPipe.getEnd1());
      endNodePair.add(currentPipe.getEnd2());
      endNodes.add(endNodePair);
    }
    // extracts all data from every node and see if it contains errors
    for (Pipe currentPipe : graph.getPipes()) {
      String name = currentPipe.getPipeName();
      double length = currentPipe.getPipeLength();
      double diameter = currentPipe.getPipeDiameter();
      double flowRate = currentPipe.getPipeFlowRate();
      double inPress = currentPipe.getInPressure();
      double outPress = currentPipe.getOutPressure();
      double gravity = currentPipe.getGravity();
      double basePress = currentPipe.getBasePressure();
      double avgTemp = currentPipe.getAvgTemp();
      double baseTemp = currentPipe.getBaseTemp();
      double zValue = currentPipe.getZ();
      double efficiency = currentPipe.getEfficiency();
      // checks for incorrect pressure values
      if (inPress < 0.0 || outPress < 0.0) {
        pipeIssues
            .add("Pipe " + name + ": Negative pressure. Absolute pressure must be greater than 0.");
        currentPipe.setState("failed");
      }

      // checks for zero values or negatives in the gravity, length, temperature, z value, absolute
      // roughness, diameter, efficiency, and base pressure
      if (Math.abs(diameter) < 2 * Double.MIN_VALUE || diameter < 0.0) {
        pipeIssues.add("Pipe " + name
            + ": Pipe diameter negative or zero. Pipe diameter must be greater than 0.");
        currentPipe.setState("failed");
      }
      if (Math.abs(gravity) < 2 * Double.MIN_VALUE || gravity < 0.0) {
        pipeIssues
            .add("Pipe " + name + ": Gravity negative or zero. Gravity must be greater than 0.");
        currentPipe.setState("failed");
      }
      if (Math.abs(length) < 2 * Double.MIN_VALUE || length < 0.0) {
        pipeIssues.add(
            "Pipe " + name + ": Pipe length negative or zero. Pipe length must be greater than 0.");
        currentPipe.setState("failed");
      }
      if (Math.abs(zValue) < 2 * Double.MIN_VALUE || zValue < 0.0) {
        pipeIssues.add("Pipe " + name
            + ": Z coefficient negative or zero. Z coefficient must be greater than 0.");
        currentPipe.setState("failed");
      }
      if ((Math.abs(avgTemp) < 2 * Double.MIN_VALUE || avgTemp < 0.0)
          || Math.abs(baseTemp) < 2 * Double.MIN_VALUE || baseTemp < 0.0) {
        pipeIssues.add("Pipe " + name
            + ": Absolute temperature negative or zero. Absolute temperature must be greater than 0.");
        currentPipe.setState("failed");
      }
      if (Math.abs(basePress) < 2 * Double.MIN_VALUE || basePress < 0.0) {
        pipeIssues.add("Pipe " + name
            + ": Base pressure negative or zero. Base pressure must be greater than 0.");
        currentPipe.setState("failed");
      }
      if (Double.isNaN(efficiency) || Double.isInfinite(efficiency)) {
        pipeIssues.add("Pipe " + name + ": Efficiency is infinite or not a number.");
        currentPipe.setState("failed");
      } else if (Math.abs(efficiency) < 2 * Double.MIN_VALUE || efficiency < 0.0) {
        pipeIssues.add("Pipe " + name
            + ": Efficiency is negative. Efficiency must be greater than or equal to 0.");
        currentPipe.setState("failed");
      }
    }

    // checks for duplicate names in the graph
    Set<String> nameSet = new HashSet<>(graph.getPipeNames());
    if (nameSet.size() < graph.getNumPipes()) {
      pipeIssues.add("Pipes with duplicate names exist. Pipes must have unique names.");
    }
    // checks for pipes connected between the same nodes
    boolean pipeSelfConnect = false;
    Set<Set<Node>> endNodesSet = new HashSet<>();
    for (Set<Node> setPair : endNodes) {
      endNodesSet.add(setPair);
      if (setPair.size() < 2) {
        pipeSelfConnect = true;
      }
    }
    if (endNodesSet.size() < endNodes.size()) {
      pipeIssues.add(
          "Pipes connecting the same two nodes exist. Only one pipe can be placed between two nodes.");
    }
    if (pipeSelfConnect) {
      pipeIssues.add(
          "Pipe with the same node at both ends exists. Each pipe must have distinct end nodes.");
    }
    return pipeIssues;
  }

  @Override
  public Object clone() {
    try {
      return (EngineRegulator) super.clone();
    } catch (CloneNotSupportedException e) {
      return new EngineRegulator();
    }
  }

  public String getGeneralMode() {
    return generalMode;
  }

  public void setGeneralMode(String generalMode) {
    this.generalMode = generalMode;
  }

  public String getSubsetMode() {
    return subsetMode;
  }

  public void setSubsetMode(String subsetMode) {
    this.subsetMode = subsetMode;
  }

  public String getPressureEquationMode() {
    return pressureEquationMode;
  }

  public void setPressureEquationMode(String pressureEquationMode) {
    this.pressureEquationMode = pressureEquationMode;
  }
}
