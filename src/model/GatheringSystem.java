package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The GatheringSystem class represents a project file in its entirety, which includes all
 * information relevant to the model of a single gas gathering system as well as meta-parameters
 * needed to perform operations and analysis on the gas gathering system such as the currently
 * selected units, error regulating engine, and analysis engine.
 * 
 * @author John Coleman
 */
public class GatheringSystem implements Serializable, Cloneable {
  private static final long serialVersionUID = -8104064443511616199L;
  private String name;

  // Serializable components
  private Graph graph;
  private FlowCalc calculation;
  private Units units;
  private EngineRegulator safetyCheck;

  private List<Graph> graphTimeline;
  private int graphIndex;


  public GatheringSystem() {
    /**
     * Default Gathering System constructor.
     */
    graph = new Graph();
    calculation = new FlowCalc("Pressure", "Weymouth");
    units = new Units();
    safetyCheck = new EngineRegulator();
    graphIndex = 0;
    graphTimeline = new ArrayList<>();
    graphTimeline.add(graph);
  }

  /**
   * Gathering System constructor for the creation of new project files or loading saved project
   * files.
   * 
   * @param graph the mathematical graph and set of mappings carrying physical information about the
   *        gas gathering system
   * @param name the name of the project file and gas gathering system
   */
  public GatheringSystem(Graph graph, String name) {
    // set the new data from the new file
    this.graph = graph;
    this.name = name;
    graphIndex = 0;
    graphTimeline = new ArrayList<>();
    graphTimeline.add(graph);
  }

  @Override
  /**
   * TODO
   */
  public GatheringSystem clone() throws CloneNotSupportedException {
    GatheringSystem g = null;
    try {
      g = (GatheringSystem) super.clone();

    } catch (CloneNotSupportedException e) {
      g = new GatheringSystem(); // may need to write in better catch
    }
    g.calculation = (FlowCalc) this.calculation.clone();
    g.units = (Units) this.units.clone();
    g.safetyCheck = (EngineRegulator) this.safetyCheck.clone();
    g.graph = (Graph) this.graph.clone();
    graphIndex = 0;
    return g;
  }

  public void nextGraph() {
    graphIndex = (graphIndex + 1) % graphTimeline.size();
    graph = graphTimeline.get(graphIndex);
    System.out.println("Current Time Frame: " + String.valueOf(graphIndex));
  }

  public void prevGraph() {
    graphIndex = (graphIndex + graphTimeline.size() - 1) % graphTimeline.size();
    graph = graphTimeline.get(graphIndex);
    System.out.println("Current Time Frame: " + String.valueOf(graphIndex));
  }

  public void moveGraph(int newIndex) {
    graphIndex = newIndex % graphTimeline.size();
    graph = graphTimeline.get(graphIndex);
    System.out.println("Current Time Frame: " + String.valueOf(graphIndex));
  }

  /**
   * Runs a pressure calculation on the gathering system which determines the direction and amount
   * of gas volume running through each pipe as well as the pressure at every intlet and tee.
   * 
   * @param pressEq the type of pressure equation being used for the calculation.
   * @return the errors which were detected before and after the calculation ran.
   */
  public ArrayList<String> pressuresVolumesCalc(PressureEquation pressEq) {
    List<Graph> newTimeline = new ArrayList<>();
    ArrayList<String> allGraphIssues = new ArrayList<>();
    for (int index = 0; index < graphTimeline.size(); index++) {
      Graph g = graphTimeline.get(index);
      ArrayList<String> issues = safetyCheck.checkGraph(g);
      if (!issues.isEmpty()) {
        allGraphIssues.add("Time frame " + index + " is formatted incorrectly.");
        allGraphIssues.addAll(issues);
        g.setState("failed");
      }
    }
    if (!allGraphIssues.isEmpty()) {
      allGraphIssues.add(0, "The following time frames are improperly formmatted:");
    } else {
      boolean isAnyFrameFailed = false;
      for (int index = 0; index < graphTimeline.size(); index++) {
        Graph g = graphTimeline.get(index);
        GraphMessage message = this.getCalculation().pressureVolumesCalc(g);
        if (message.getState()) {
          Graph newGraph = message.getGraph();
          newGraph.setState("success");
          newTimeline.add(newGraph);
        } else {
          isAnyFrameFailed = true;
          g.setState("failed");
          newTimeline.add(g);
          allGraphIssues.add("Time frame " + index + " failed to converge.");
        }
      }
      graphTimeline = newTimeline;
      graph = graphTimeline.get(graphIndex);

      if (isAnyFrameFailed) {
        allGraphIssues.add(0, "The following time frame calculations failed to converge:");
      } else {
        System.out.println("Successful Pressure Calculation");
      }
    }

    return allGraphIssues;
  }



  /**
   * Runs an efficiency calculation which finds the estimated pressure of every tee and the
   * efficiency of every pipe.
   * 
   * @param pressEq the type of pressure equation being used for the calculation.
   * @return the errors which were detected before and after the calculation ran.
   */
  public ArrayList<String> pressuresEfficienciesCalc(PressureEquation pressEq) {
    List<Graph> newTimeline = new ArrayList<>();
    ArrayList<String> allGraphIssues = new ArrayList<>();
    for (int index = 0; index < graphTimeline.size(); index++) {
      Graph g = graphTimeline.get(index);
      ArrayList<String> issues = safetyCheck.checkGraph(g);
      if (!issues.isEmpty()) {
        allGraphIssues.add("Time frame " + index + " is formatted incorrectly.");
        allGraphIssues.addAll(issues);
        g.setState("failed");
      }
    }
    if (!allGraphIssues.isEmpty()) {
      allGraphIssues.add(0, "The following time frames are improperly formmatted:");
    } else {
      boolean isAnyFrameFailed = false;
      for (int index = 0; index < graphTimeline.size(); index++) {
        Graph g = graphTimeline.get(index);
        GraphMessage message = this.getCalculation().pressureEfficiencyCalc(g);
        if (message.getState()) {
          Graph newGraph = message.getGraph();
          newGraph.setState("success");
          newTimeline.add(newGraph);
        } else {
          isAnyFrameFailed = true;
          g.setState("failed");
          newTimeline.add(g);
          allGraphIssues.add("Time frame " + index + " failed to converge.");
        }
      }
      graphTimeline = newTimeline;
      graph = graphTimeline.get(graphIndex);

      if (isAnyFrameFailed) {
        allGraphIssues.add(0, "The following time frame calculations failed to converge:");
      } else {
        System.out.println("Successful Efficiency Calculation");
      }
    }

    return allGraphIssues;
  }


  public Graph getGraph() {
    return graph;
  }

  public void setGraph(Graph currGraph) {
    this.graph = currGraph;
  }

  public void setGraphTimeline(List<Graph> gTl) {
    this.graphTimeline = gTl;
  }

  public List<Graph> getGraphTimeline() {
    return graphTimeline;
  }

  public int getGraphTimelineSize() {
    return getGraphTimeline().size();
  }

  public int getGraphIndex() {
    return graphIndex;
  }

  public FlowCalc getCalculation() {
    return calculation;
  }

  public void setCalculation(FlowCalc calculation) {
    this.calculation = calculation;
  }

  public void setCalculation(String calculationType, String pressureEquation) {
    this.calculation = new FlowCalc(calculationType, pressureEquation);
  }

  public void newCalculation(String calculationType) {
    calculation = new FlowCalc(calculationType);
  }

  public Units getUnits() {
    return units;
  }

  public void setUnits(Units units) {
    this.units = units;
  }

  public EngineRegulator getSafetyCheck() {
    return safetyCheck;
  }

  public void setSafetyCheck(EngineRegulator safetyCheck) {
    this.safetyCheck = safetyCheck;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
