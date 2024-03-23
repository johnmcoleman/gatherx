package model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Node class represents a point in a gas gathering system which corresponds to a piece of
 * equipment or other point of interest such as inlets, outlets, compressors, and valves.
 * 
 * @author John Coleman
 */
public class Node implements Serializable {
  private static final long serialVersionUID = 3500139085667044837L;
  private String nodeName; // unique identifier
  private String type; // inlet, outlet, tee etc.

  private List<Node> neighbors;
  private List<Double> numericalProperties; // for making deep copies
  private List<String> stringProperties;

  private double nodeVolumes; // volumes gathered from this node (scfd)
  private double gatheredVolumes; // volumes that have been gathered leading up to this node (scfd)
  // private double totalVolumes; // volumes gathered at this node and leading up to this node
  // combined
  // (scfd)
  private double inletPressure; // inlet pressure at the junction (psia)
  private double outletPressure; // outlet pressure at the junction (psia)
  // TODO REMOVE PRESSURE DIFFERENTIAL
  // private double targPressureDiff; // target pressure change across junction if
  // valve/compressor (psi)
  private Point location; // true node location
  private String state; // determines if the node contains erroneous values

  /**
   * Constructs a default node object.
   */
  public Node() {
    nodeName = "default";
    type = "default";
    nodeVolumes = 0.0;
    gatheredVolumes = 0.0;
    inletPressure = 14.7;
    outletPressure = 14.7;
    location = new Point(0, 0);
    state = "neutral";
    neighbors = new ArrayList<>();
    setArrays();
  }

  /**
   * Constructs a node using information taken from an instance of the gathering system class.
   */
  public Node(String name, String nodeType, double flow, double gather, double inPress,
      double outPress, int xLoc, int yLoc, String status) {
    nodeName = name;
    type = nodeType;
    nodeVolumes = flow;
    gatheredVolumes = gather;
    inletPressure = inPress;
    outletPressure = inPress; // TODO Inlet and outlet pressure always equivalent
    location = new Point(xLoc, yLoc);
    state = status;
    neighbors = new ArrayList<>();
    setArrays();
  }

  /*
   * Instantiates the arrays which hold all the string and double properties.
   */
  public void setArrays() {
    numericalProperties = new ArrayList<Double>();
    numericalProperties.add(nodeVolumes);
    numericalProperties.add(gatheredVolumes);
    numericalProperties.add(inletPressure);
    numericalProperties.add(outletPressure);
    stringProperties = new ArrayList<String>();
    stringProperties.add(nodeName);
    stringProperties.add(state);
    stringProperties.add(type);
  }

  /*
   * Takes on all the data of another node WITHOUT information about its neighbors
   */
  public void setAllConstants(Node oldNode) {
    this.setNodeVolumes(oldNode.getVolumes());
    this.setGatheredVolumes(oldNode.getGatheredVolumes());
    this.setOutletPressure(oldNode.getOutletPressure());
    this.setInletPressure(oldNode.getInletPressure());
    this.setLocation(new Point(oldNode.getLocation().x, oldNode.getLocation().y));
    this.setNodeName(oldNode.getNodeName());
    this.setState(oldNode.getState());
    this.setNodeType(oldNode.getType());
    setArrays();
  }

  /**
   * Checks if the constants of this node and the other node are equivalent. DOES NOT CHECK FOR
   * NEIGHBORS.
   * 
   * @param oldNode the node to check if it is equivalent to this node
   * @param threshold the threshold within which double value error is permitted
   * @return if the nodes have equivalent constants
   */
  public boolean constantsEquals(Node oldNode, double threshold) {
    boolean equal = true;
    oldNode.setArrays();
    this.setArrays();
    List<Double> oldNodeNumProperties = oldNode.getNumericalProperties();
    List<Double> nodeNumProperties = this.getNumericalProperties();
    for (int index = 0; index < oldNodeNumProperties.size(); index++) {
      double oldConstant = oldNodeNumProperties.get(index);
      double constant = nodeNumProperties.get(index);
      if (!(Math.abs(oldConstant - constant) < threshold)) {
        equal = false;
      }
    }
    List<String> oldNodeStrProperties = oldNode.getStringProperties();
    List<String> nodeStrProperties = this.getStringProperties();
    for (int index = 0; index < oldNodeStrProperties.size(); index++) {
      String oldString = oldNodeStrProperties.get(index);
      String string = nodeStrProperties.get(index);
      if (!(string.contentEquals(oldString))) {
        equal = false;
      }
    }
    return equal;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getNodeName());
    sb.append(",");
    sb.append(this.getType());
    sb.append(",");
    // Handles neighbors
    for (Node neighbor : this.getNeighbors()) {
      sb.append(neighbor.getNodeName());
      sb.append(";");
    }
    sb.append(",");
    sb.append(this.getVolumes());
    sb.append(",");
    sb.append(this.getInletPressure());
    sb.append(",");
    // Handles Location
    Point point = this.getLocation();
    sb.append(point.x);
    sb.append(";");
    sb.append(point.y);
    sb.append(",");
    sb.append(this.getState());

    return sb.toString();
  }


  /**
   * Sets inlet pressure and outlet pressure based on the existing pressure differential at the
   * node.
   *
   * @param newPressure the new inlet pressure of the node.
   */
  public void setInletPressure(double newPressure) {
    double pressDiff = getPressureDifferential();
    switch (this.getType()) {
      case "Control Valve":
        inletPressure = newPressure;
        outletPressure = newPressure - pressDiff;
        break;
      case "Compressor":
        inletPressure = newPressure;
        outletPressure = newPressure + pressDiff;
        break;
      default:
        inletPressure = newPressure;
        outletPressure = newPressure;
    }
  }

  /**
   * Sets inlet pressure and outlet pressure based on the existing pressure differential at the
   * node.
   *
   * @param newPressure the new outlet pressure of the node.
   */
  public void setOutletPressure(double newPressure) {
    double pressDiff = getPressureDifferential();
    switch (this.getType()) {
      case "Control Valve":
        outletPressure = newPressure;
        inletPressure = newPressure + pressDiff;
        break;
      case "Compressor":
        outletPressure = newPressure;
        inletPressure = newPressure - pressDiff;
        break;
      default:
        outletPressure = newPressure;
        inletPressure = newPressure;
    }
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Point getLocation() {
    return location;
  }

  public void setLocation(Point location) {
    this.location = location;
  }

  public String getNodeName() {
    return nodeName;
  }

  public String getType() {
    return type;
  }

  public List<Node> getNeighbors() {
    return neighbors;
  }

  public void setNeighbors(List<Node> neighs) {
    this.neighbors = neighs;
  }

  public void addNeighbor(Node neigh) {
    this.neighbors.add(neigh);
  }

  public void deleteNeighbor(Node neigh) {
    this.neighbors.remove(neigh);
  }

  public double getVolumes() {
    return nodeVolumes;
  }

  public double getGatheredVolumes() {
    return gatheredVolumes;
  }

  public void setGatheredVolumes(double vol) {
    gatheredVolumes = vol;
  }

  public double getTotalVolumes() {
    return gatheredVolumes + nodeVolumes;
  }

  public double getInletPressure() {
    return inletPressure;
  }

  public void setNodeName(String name) {
    nodeName = name;
  }

  public void setNodeType(String typ) {
    type = typ;
  }

  public void setNodeVolumes(double vol) {
    nodeVolumes = vol;
  }

  /*
   * Add set amount to gathered volumes
   */
  public void addGatheredVolumes(double vol) {
    gatheredVolumes += vol;
  }

  public double getPressureDifferential() {
    return Math.abs(inletPressure - outletPressure);
  }

  public double getOutletPressure() {
    return outletPressure;
  }

  public List<Double> getNumericalProperties() {
    return numericalProperties;
  }

  public void setNumericalProperties(List<Double> numericalProperties) {
    this.numericalProperties = numericalProperties;
  }

  public List<String> getStringProperties() {
    return stringProperties;
  }

  public void setStringProperties(List<String> stringProperties) {
    this.stringProperties = stringProperties;
  }

}
