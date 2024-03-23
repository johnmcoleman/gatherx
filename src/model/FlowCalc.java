package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import optimization.functionImplementation.ObjectiveFunctionNonLinear;
import optimization.functionImplementation.Options;
import solvers.NonlinearEquationSolver;

/**
 * The Flow Calculation class uses the global values, physical relationships, and node and pipe data
 * from the Gathering System class in which it is contained in order to perform numerical analysis
 * on this information.
 * 
 * @author John Coleman
 */
public class FlowCalc implements Serializable, Cloneable {
  private static final long serialVersionUID = 1396899015650879906L;
  private static final String[] pressureEquations =
      {"AmericanGasAssociation", "Mueller", "PanHandleA", "PanHandleB", "Weymouth"};
  private String pressureEquationMode;
  private String type;
  private PressureEquation currentEquation;

  // calculation metaparameters
  private int maxIterations;
  private double minFunctionValue;

  // global values in back end units
  private double gravity;
  private double basePressure;
  private double averageTemperature;
  private double baseTemperature;
  private double averageZ;
  private double friction;
  private double pressureGradient;
  private double efficiency;

  /**
   * Default new Flow Calculation constructor for when no information is availible.
   */
  public FlowCalc() {
    type = "Pressure";
    pressureEquationMode = "Weymouth";
    currentEquation = this.getPressureEquation("Weymouth");
    gravity = 0.7;
    basePressure = 14.7;
    averageTemperature = 530;
    baseTemperature = 520;
    averageZ = 0.9;
    friction = 0.1;
    pressureGradient = 20.0;
    efficiency = 1.0;
    maxIterations = 100;
    minFunctionValue = 0.01;
  }

  /**
   * Flow Calculation constructor for when only the type of calculation is known.
   * 
   * @param calculationType the parameter of the system for which the analysis is solving.
   */
  public FlowCalc(String calculationType) {
    type = calculationType;
    currentEquation = this.getPressureEquation("Weymouth");
    pressureEquationMode = "Weymouth";
    gravity = 0.7;
    basePressure = 14.7;
    averageTemperature = 530;
    baseTemperature = 520;
    averageZ = 0.9;
    friction = 0.1;
    efficiency = 1.0;
    maxIterations = 100;
    minFunctionValue = 0.01;
    if (calculationType.equals("Pipe Size")) {
      pressureGradient = 20.0;
    } else {
      pressureGradient = 0.0;
    }
  }

  /**
   * Flow Calculation constructor for when only the type of calculation and the pressure equation
   * are known.
   * 
   * @param calculationType the parameter of the system for which the analysis is solving.
   * @param pressureEquation the equation selected which the analysis will use.
   */
  public FlowCalc(String calculationType, String pressureEquation) {
    type = calculationType;
    currentEquation = this.getPressureEquation(pressureEquation);
    pressureEquationMode = pressureEquation;
    gravity = 0.7;
    basePressure = 14.7;
    averageTemperature = 530;
    baseTemperature = 520;
    averageZ = 0.9;
    friction = 0.1;
    efficiency = 1.0;
    maxIterations = 100;
    minFunctionValue = 0.01;
    if (calculationType.equals("Pipe Size")) {
      pressureGradient = 20.0;
    } else {
      pressureGradient = 0.0;
    }
  }

  @Override
  public Object clone() {
    try {
      return (FlowCalc) super.clone();
    } catch (CloneNotSupportedException e) {
      return new FlowCalc();
    }
  }

  public int getMaxIterations() {
    return maxIterations;
  }

  public void setMaxIterations(int maxIterations) {
    this.maxIterations = maxIterations;
  }

  public double getMinFunctionValue() {
    return minFunctionValue;
  }

  public void setMinFunctionValue(double minFunctionValue) {
    this.minFunctionValue = minFunctionValue;
  }

  public PressureEquation getCurrentEquation() {
    return this.currentEquation;
  }

  public void setCurrentEquation(PressureEquation currentEquation) {
    this.currentEquation = currentEquation;
  }

  public String getPressureEquationMode() {
    return pressureEquationMode;
  }

  public void setPressureEquationMode(String pressureEquationMode) {
    this.pressureEquationMode = pressureEquationMode;
  }

  public static String[] getPressureEquations() {
    return pressureEquations;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public double getGravity() {
    return gravity;
  }

  public void setGravity(double gravity) {
    this.gravity = gravity;
  }

  public double getBasePressure() {
    return basePressure;
  }

  public void setBasePressure(double basePressure) {
    this.basePressure = basePressure;
  }

  public double getAverageTemperature() {
    return averageTemperature;
  }

  public void setAverageTemperature(double averageTemperature) {
    this.averageTemperature = averageTemperature;
  }

  public double getBaseTemperature() {
    return baseTemperature;
  }

  public void setBaseTemperature(double baseTemperature) {
    this.baseTemperature = baseTemperature;
  }

  public double getAverageZ() {
    return averageZ;
  }

  public void setAverageZ(double averageZ) {
    this.averageZ = averageZ;
  }

  public double getFriction() {
    return friction;
  }

  public void setFriction(double friction) {
    this.friction = friction;
  }

  public double getPressureGradient() {
    return pressureGradient;
  }

  public void setPressureGradient(double pressureDifferentialPerLength) {
    this.pressureGradient = pressureDifferentialPerLength;
  }

  public double getEfficiency() {
    return efficiency;
  }

  public void setEfficiency(double efficiency) {
    this.efficiency = efficiency;
  }

  private PressureEquation getPressureEquation(String str) {
    switch (str) {
      case "Mueller":
        return new Mueller();
      case "PanHandle A":
        return new PanHandleA();
      case "PanHandle B":
        return new PanHandleB();
      case "Weymouth":
      default:
        return new Weymouth();
    }
  }

  /**
   * Calculates volumes flowing through pipes and pressures at nodes based off of a single set
   * pressure node and all node flow rates.
   * 
   * TODO Make this method work for distribution systems, where inlet pressures are set points
   * rather than outlets
   * 
   * @param calcGraph the graph with the physical information about the gas gathering system on
   *        which the analysis will be performed.
   * @return a new instance of the graph object with the same information as calcGraph but with new
   *         calculated pressures and flow rates
   */
  public GraphMessage pressureVolumesCalc(Graph calcGraph) {
    // Determines formulation of the non-linear system of equations based on the graph
    List<Pipe> pipeList = calcGraph.getPipes();
    List<Node> nodeList = calcGraph.getNodes();
    int numMassFunctions = calcGraph.getNumNodes() - calcGraph.getNumOutletNodes();
    int numEnergyFunctions = calcGraph.getNumPipes();
    int numberOfVariables = numMassFunctions + numEnergyFunctions;
    ArrayList<Integer> excludedNodeIndices = new ArrayList<Integer>();
    for (int index = 0; index < nodeList.size(); index++) {
      if (nodeList.get(index).getType().equals("Outlet")) {
        excludedNodeIndices.add(index);
      }
    }
    // A1
    DMatrixRMaj variableConnectionMatrix = calcGraph.getVariableConnectionMatrix();
    // A1T
    DMatrixRMaj variableConnectionMatrixTransposed =
        calcGraph.getVariableConnectionMatrixTransposed();
    // A2T
    DMatrixRMaj constantConnectionMatrixTransposed =
        calcGraph.getConstantConnectionMatrixTransposed();

    // Initializes the options of the solver
    Options options = new Options(numberOfVariables); // TODO fails if number of variables > 999
    options.setAnalyticalJacobian(false);
    options.setAlgorithm(Options.TRUST_REGION);
    options.setMaxStep(1e16);
    options.setSaveIterationDetails(true);
    options.setAllTolerances(this.minFunctionValue);
    options.setMaxIterations(this.maxIterations);

    PressureEquation equation = this.getCurrentEquation();

    ObjectiveFunctionNonLinear objectiveFunction = new ObjectiveFunctionNonLinear() {
      @Override
      public DMatrixRMaj getF(DMatrixRMaj x) {
        // Initializes variables and constants in matrix form
        DMatrixRMaj functionVector = new DMatrixRMaj(numberOfVariables, 1); // F(p,q)
        DMatrixRMaj energyFunctionsSum = new DMatrixRMaj(numEnergyFunctions, 1); // F(p)
        DMatrixRMaj pressureStateVariables = CommonOps_DDRM.extract(x, 0, numMassFunctions, 0, 1); // p1
        DMatrixRMaj pressureConstants = new DMatrixRMaj(excludedNodeIndices.size(), 1); // p2
        double deltaPressureSign;
        int index = 0;
        for (Integer excludedIndex : excludedNodeIndices) {
          double knownPressure = nodeList.get(excludedIndex).getInletPressure();
          pressureConstants.add(index, 0, Math.pow(knownPressure, 2d));
          index++;
        }
        DMatrixRMaj massFunctionsSum = new DMatrixRMaj(numMassFunctions, 1); // F(q)
        DMatrixRMaj flowStateVariables =
            CommonOps_DDRM.extract(x, numMassFunctions, numberOfVariables, 0, 1); // q

        // F(p) = A1T*p1 + A2T*p2 - phi(q) = 0 (Conservation of Energy Functions) Term # L -> R
        DMatrixRMaj term1Energy =
            new DMatrixRMaj(variableConnectionMatrixTransposed.getNumRows(), 1);
        CommonOps_DDRM.mult(variableConnectionMatrixTransposed, pressureStateVariables,
            term1Energy); // A1T*p1
        DMatrixRMaj term2Energy =
            new DMatrixRMaj(constantConnectionMatrixTransposed.getNumRows(), 1);
        CommonOps_DDRM.mult(constantConnectionMatrixTransposed, pressureConstants, term2Energy); // A2T*p2
        DMatrixRMaj term3Energy =
            new DMatrixRMaj(variableConnectionMatrixTransposed.getNumRows(), 1);
        for (int row = 0; row < numEnergyFunctions; row++) { // -phi(q)
          Pipe currentPipe = pipeList.get(row);
          double flow = x.get(numMassFunctions + row);
          if (flow < 0) {
            deltaPressureSign = 1d;
          } else {
            deltaPressureSign = -1d;
          }
          term3Energy.add(row, 0, deltaPressureSign * equation.getFlowRateCoefficient(currentPipe)
              * Math.pow(flow, equation.getFlowExponent()));
        }
        DMatrixRMaj term1Plus2 =
            new DMatrixRMaj(variableConnectionMatrixTransposed.getNumRows(), 1);
        CommonOps_DDRM.add(term1Energy, term2Energy, term1Plus2);
        CommonOps_DDRM.add(term1Plus2, term3Energy, energyFunctionsSum);

        // F(q) A1*q - Q = 0 (Conservation of Mass Functions)
        DMatrixRMaj term1Mass = new DMatrixRMaj(numMassFunctions, 1);
        CommonOps_DDRM.mult(variableConnectionMatrix, flowStateVariables, term1Mass); // A1*q
        DMatrixRMaj term2Mass = new DMatrixRMaj(numMassFunctions, 1);
        int includedNodeCounter = 0;
        int nodeListIndex = 0;
        for (Node node : nodeList) { // - Q
          if (!excludedNodeIndices.contains(nodeListIndex)) {
            term2Mass.add(includedNodeCounter, 0, -1d * node.getVolumes());
            includedNodeCounter++;
          }
          nodeListIndex++;
        }
        CommonOps_DDRM.add(term1Mass, term2Mass, massFunctionsSum);

        // F(p,q) = [F(p),F(q)]
        for (int row = 0; row < numEnergyFunctions; row++) { // F(p)
          functionVector.add(row, 0, energyFunctionsSum.get(row));
        }
        for (int row = 0; row < numMassFunctions; row++) { // F(q)
          functionVector.add(row + numEnergyFunctions, 0, massFunctionsSum.get(row));
        }

        return functionVector;
      }

      @Override
      public DMatrixRMaj getJ(DMatrixRMaj x) {
        /*
         * TODO Calculate the analytical Jacobian matrix in order to improve performance
         */
        return null;
      }

    };

    DMatrixRMaj initialGuess =
        getInitialConditionsVectorPressureVolumeCalc(calcGraph, variableConnectionMatrix);

    // print out the results
    NonlinearEquationSolver nonlinearSolver =
        new NonlinearEquationSolver(objectiveFunction, options);
    nonlinearSolver.solve(new DMatrixRMaj(initialGuess));
    Graph graph = getGraphFromPressureAndFlowVector(nonlinearSolver.getX(), calcGraph);
    List<Pipe> pipeListCalced = graph.getPipes();
    DMatrixRMaj velocities = new DMatrixRMaj(pipeListCalced.size(), 1);
    int countyboy = 0;
    for (Pipe pipe : pipeListCalced) {
      velocities.add(countyboy, 0, pipe.getVelocity());
      countyboy++;
    }
    List<Node> nodeListCalced = graph.getNodes();
    DMatrixRMaj pressures = new DMatrixRMaj(nodeListCalced.size(), 1);
    countyboy = 0;
    for (Node node : nodeListCalced) {
      pressures.add(countyboy, 0, node.getInletPressure());
      countyboy++;
    }
    boolean state = true;
    String results = nonlinearSolver.getResults().toString();
    if (results.contains("failed") || results.contains("null")) {
      state = false;
    }
    GraphMessage message = new GraphMessage(graph, results, state);
    return message;
  }

  /**
   * Concatenates EJML matrices with only one column. eg. F = [F1,F2,F3] F1 energy functions, F2
   * mass functions, F3 pressure error functions
   * 
   * @param vectors the list of the matrices to be concatenated
   * @return a vector matrix concatenated in the order of the vectors in vectors
   */
  public static DMatrixRMaj vectorConcatenation(ArrayList<DMatrixRMaj> vectors) {
    int totalLength = 0;
    for (DMatrixRMaj vector : vectors) {
      totalLength += vector.getNumElements();
    }
    DMatrixRMaj finalVector = new DMatrixRMaj(totalLength, 1);
    int index = 0;
    for (DMatrixRMaj vector : vectors) {
      for (int row = 0; row < vector.getNumElements(); row++) {
        finalVector.add(row + index, 0, vector.get(row));
      }
      index += vector.getNumElements();
    }
    return finalVector;
  }

  /**
   * Locates the indices of a specific type of node within a list of nodes.
   * 
   * @param nodeType nodeType the type of the node eg "Tee"
   * @param nodeList nodeList the list of nodes which needs to have different type nodes located
   *        from.
   * @return an array list of the indices at which the node list contains a node of the type
   *         nodeType.
   */
  public static ArrayList<Integer> getNodeTypeListIndices(String nodeType, List<Node> nodeList) {
    ArrayList<Integer> nodeIndices = new ArrayList<Integer>();
    for (int index = 0; index < nodeList.size(); index++) {
      if (nodeList.get(index).getType().equals(nodeType)) {
        nodeIndices.add(index);
      } else {
        nodeIndices.add(null);
      }
    }
    return nodeIndices;
  }

  /**
   * Calculates the efficiencies of all the pipes which are between two nodes which have given
   * pressures such as inlets and outlets.
   * 
   * @param pressureCalcGraph the graph object for which to solve the calculation initial conditions
   *        are needed.
   * @param numVariables the number of state variables.
   * @param normalizingScalar the scalar which normalizes the efficiencies to the same order of
   *        magnitude as the pressures
   * @return the solution vector.
   */
  public GraphMessage pressureEfficiencyCalc(Graph originalGraph) {
    GraphMessage pressureMessage = this.pressureVolumesCalc(originalGraph);
    Graph pressureCalcGraph = pressureMessage.getGraph();
    int numMassFunctions = originalGraph.getNumNodes() - originalGraph.getNumOutletNodes();
    int numEnergyFunctions = originalGraph.getNumPipes();
    List<Integer> inlets = getNodeTypeListIndices("Inlet", originalGraph.getNodes());
    int numInlets = 0;
    for (int index = 0; index < inlets.size(); index++) {
      if (inlets.get(index) != null) {
        numInlets++;
      }
    }
    int numVariables = numMassFunctions + numEnergyFunctions + numInlets;
    DMatrixRMaj solutionVector = new DMatrixRMaj(numVariables, 1);
    List<Node> pressureCalcNodeList = pressureCalcGraph.getNodes();
    List<Node> originalNodeList = originalGraph.getNodes();
    ArrayList<Integer> teeIndices = getNodeTypeListIndices("Tee", pressureCalcNodeList);
    ArrayList<Integer> outletIndices = getNodeTypeListIndices("Outlet", pressureCalcNodeList);
    PressureEquation equation = this.getCurrentEquation();

    // Creates a graph from which flow rates for efficiency setting can be created
    Graph calculatedAndKnownPressures = pressureCalcGraph.getDeepClone();
    List<Node> calculatedAndKnownPressuresNodeList = calculatedAndKnownPressures.getNodes();
    List<Pipe> calculatedAndKnownPressuresPipeList = calculatedAndKnownPressures.getPipes();
    int nodeListIndex = 0;
    for (Node node : originalNodeList) {
      if (node.getType().equals("Inlet")) {
        Node calculatedAndKnownPressureNode =
            calculatedAndKnownPressuresNodeList.get(nodeListIndex);
        calculatedAndKnownPressureNode.setInletPressure(node.getInletPressure());
        calculatedAndKnownPressureNode.setOutletPressure(node.getOutletPressure());
      }
      nodeListIndex++;
    }

    int solutionVectorIndex = 0;
    // Carries over pressure (p) state variables to the solution vector
    nodeListIndex = 0;
    for (Node node : pressureCalcNodeList) {
      if (!node.getType().equals("Outlet")) {
        if (node.getType().equals("Inlet")) {
          solutionVector.set(solutionVectorIndex,
              Math.pow(originalNodeList.get(nodeListIndex).getInletPressure(), 2d));
          solutionVectorIndex++;
        } else {
          solutionVector.set(solutionVectorIndex, Math.pow(node.getInletPressure(), 2d));
          solutionVectorIndex++;
        }
      }
      nodeListIndex++;
    }
    // Calculates pipe efficiency (E) state variables
    for (Pipe pipe : calculatedAndKnownPressuresPipeList) {
      double efficiency = Math.abs(pipe.getFlowRate() / equation.getFlowRateMagnitude(pipe));
      solutionVector.set(solutionVectorIndex, efficiency);
      solutionVectorIndex++;
    }
    // Sets inlet efficiency (e) state variables
    int rowe = 0;
    for (Node node : pressureCalcNodeList) {
      if ((teeIndices.get(rowe) == null) && (outletIndices.get(rowe) == null)) {
        solutionVector.set(solutionVectorIndex, 1.0);
        solutionVectorIndex++;
      }
      rowe++;
    }
    Graph solution = getGraphFromPressureAndEfficiencyVector(solutionVector, pressureCalcGraph);
    boolean state = true;
    String results = pressureMessage.getMessage();
    if (results.contains("failed") || results.contains("null")) {
      state = false;
    }
    GraphMessage message = new GraphMessage(solution, results, state);
    return message;
  }

  /**
   * Finds the vector of initial conditions for the pressure and flow state variables of a graph.
   * 
   * @param graph the graph object for which to solve the calculation initial conditions are needed.
   * @param variableConnectionMatrix the connection matrix for the state variables of the graph.
   * @return the vector of initial conditions.
   */
  public static DMatrixRMaj getInitialConditionsVectorPressureVolumeCalc(Graph graph,
      DMatrixRMaj variableConnectionMatrix) {
    List<Node> nodeList = graph.getNodes();
    int numberOfVariables = graph.getNumNodes() - graph.getNumOutletNodes() + graph.getNumPipes();
    DMatrixRMaj initialGuess = new DMatrixRMaj(numberOfVariables, 1);
    /*
     * Sets initial pressures to the node's current pressure unless it is a node type where known
     * pressure data is not provided, in which case set it to the pressure of the nearest node with
     * known pressure data. p0 = [p10 , p20, p30 ..... pn0]
     */
    double minimumOutletPressure = 0.0;
    for (Node node : nodeList) {
      if (node.getType().equals("Outlet")) {
        double pressure = node.getInletPressure();
        if (minimumOutletPressure < pressure) {
          minimumOutletPressure = pressure;
        }
      }
    }
    int rowP = 0;
    for (Node startNode : nodeList) {
      if (startNode.getType().equals("Inlet")) {
        if (startNode.getInletPressure() < minimumOutletPressure) {
          startNode.setInletPressure(minimumOutletPressure + 1.0);
        }
        initialGuess.set(rowP, Math.pow(startNode.getInletPressure(), 2d));
        rowP++;
      } else if (startNode.getType().equals("Tee")) {
        Stack<Node> closedSet = new Stack<>();
        HashMap<Node, Integer> distance = new HashMap<>();

        // Initializes node distances
        for (Node node : nodeList) {
          distance.put(node, Integer.MAX_VALUE);
        }
        distance.replace(startNode, 0);
        closedSet.push(startNode);

        // Executes depth first search for the nearest node with known pressure data
        Iterator<Node> itr = closedSet.iterator();
        Boolean isknownPressureNodeFound = false;
        while (itr.hasNext() && !isknownPressureNodeFound) {
          Node currentNode = closedSet.pop();
          if (!currentNode.getType().equals("Tee")) {
            initialGuess.set(rowP, Math.pow(currentNode.getInletPressure(), 2d));
            isknownPressureNodeFound = true;
            rowP++;
          } else {
            List<Node> neighbors = currentNode.getNeighbors();
            for (Node neighbor : neighbors) {
              // Checks if node has been previously encountered and if it has been then ignores it
              if (distance.get(neighbor).equals(Integer.MAX_VALUE)) {
                distance.replace(neighbor, distance.get(currentNode) + 1);
                closedSet.push(neighbor);
              }
            }
          }
        }
      }
    }
    /*
     * Sets initial pipe flow rates to the average of the total flow in the system.
     * 
     * TODO implement q0 = (A^-1)*Q for better guess, will need more equations for the system.
     */
    DMatrixRMaj setFlowRatesQ = new DMatrixRMaj(variableConnectionMatrix.getNumCols(), 1);
    for (int row = 0; row < variableConnectionMatrix.getNumCols(); row++) {
      setFlowRatesQ.add(row, 0, nodeList.get(row).getVolumes());
    }
    DMatrixRMaj initialFlowRatesQ = new DMatrixRMaj(graph.getNumPipes(), 1);
    CommonOps_DDRM.fill(initialFlowRatesQ, 0);
    for (int index = variableConnectionMatrix.getNumRows(); index < numberOfVariables; index++) {
      initialGuess.add(index, 0,
          initialFlowRatesQ.get(index - variableConnectionMatrix.getNumRows()));
    }
    return initialGuess;
  }


  /**
   * Returns a new graph object of the same form as the old graph object with node and pipe
   * variables set to the state variables of the function vector.
   * 
   * @param functionVector the vector of state variables F[pressures (psia) ,flow rates (scfd)]
   * @param oldGraph oldGraph the graph object from which the function vector is the solution to
   * @param excludedNodeIndices the list of indices which are set points in the pressure calculation
   * @return newGraph the new instance of a graph object which takes the state variable properties
   *         of the vector solution from the old graph
   */
  public Graph getGraphFromPressureAndFlowVector(DMatrixRMaj functionVector, Graph oldGraph) {
    int numNodes = oldGraph.getNumNodes();
    int numPipes = oldGraph.getNumPipes();
    ArrayList<Integer> outletNodeIndices =
        FlowCalc.getNodeTypeListIndices("Outlet", oldGraph.getNodes());
    Graph newGraph = oldGraph.getDeepClone();
    List<Node> newNodeList = newGraph.getNodes();
    List<Pipe> newPipeList = newGraph.getPipes();

    // Assigns values from the calculated state variables to the graph
    int functionVectorIndex = 0;
    for (int nodeListIndex = 0; nodeListIndex < numNodes; nodeListIndex++) {
      Node newNode = newNodeList.get(nodeListIndex);
      if (!outletNodeIndices.contains(nodeListIndex)) {
        newNode.setInletPressure(Math.sqrt(functionVector.get(functionVectorIndex)));
        newNode.setOutletPressure(Math.sqrt(functionVector.get(functionVectorIndex)));
        if (functionVector.get(functionVectorIndex) < 0.0) {
          throw new ArithmeticException("Converged to Negative Pressures");
        }
        functionVectorIndex++;
      }
    }
    for (int pipeListIndex = 0; pipeListIndex < numPipes; pipeListIndex++) {
      Pipe newPipe = newPipeList.get(pipeListIndex);
      newPipe.setFlowRate(functionVector.get(functionVectorIndex));
      functionVectorIndex++;
    }
    // Sets all nodes and pipes to a successful calculation state
    for (Node node : newNodeList) {
      node.setState("success");
    }
    for (Pipe pipe : newPipeList) {
      pipe.setState("success");
    }
    newGraph.setNodes(newNodeList);
    newGraph.setPipes(newPipeList);
    return newGraph;
  }

  /**
   * Returns a new graph object of the same form as the old graph object with node and pipe
   * variables set to the state variables of the function vector.
   * 
   * @param functionVector the solution to a pressure based efficiency calculation
   * @param oldGraph the graph before the efficiency calculation was run
   * @return newGraph the new instance of a graph object which takes on the state variable
   *         properties of the vector solution from the old graph
   */
  public Graph getGraphFromPressureAndEfficiencyVector(DMatrixRMaj functionVector, Graph oldGraph) {
    int numNodes = oldGraph.getNumNodes();
    int numPipes = oldGraph.getNumPipes();
    ArrayList<Integer> outletNodeIndices =
        FlowCalc.getNodeTypeListIndices("Outlet", oldGraph.getNodes());
    Graph newGraph = oldGraph.getDeepClone();
    List<Node> newNodeList = newGraph.getNodes();
    List<Pipe> newPipeList = newGraph.getPipes();

    // Assigns values from the calculated state variables to the graph
    int functionVectorIndex = 0;
    for (int nodeListIndex = 0; nodeListIndex < numNodes; nodeListIndex++) {
      Node newNode = newNodeList.get(nodeListIndex);
      if (!outletNodeIndices.contains(nodeListIndex)) {
        newNode.setInletPressure(Math.sqrt(functionVector.get(functionVectorIndex)));
        newNode.setOutletPressure(Math.sqrt(functionVector.get(functionVectorIndex)));
        if (functionVector.get(functionVectorIndex) < 0.0) {
          throw new ArithmeticException("Converged to Negative Pressures");
        }
        functionVectorIndex++;
      }
    }
    for (int pipeListIndex = 0; pipeListIndex < numPipes; pipeListIndex++) {
      Pipe newPipe = newPipeList.get(pipeListIndex);
      newPipe.setEfficiency((functionVector.get(functionVectorIndex)));
      functionVectorIndex++;
    }
    // Sets all nodes and pipes to a successful calculation state
    for (Node node : newNodeList) {
      node.setState("success");
    }
    for (Pipe pipe : newPipeList) {
      pipe.setState("success");
    }
    newGraph.setNodes(newNodeList);
    newGraph.setPipes(newPipeList);
    return newGraph;
  }

  /**
   * Calculates the square root of every element in the matrix.
   * 
   * @param input the input matrix
   * @return the square root of every element in the input matrix
   */
  public static DMatrixRMaj elementSquareRoot(DMatrixRMaj input) {
    DMatrixRMaj output = new DMatrixRMaj(input.getNumRows(), input.getNumCols());
    for (int row = 0; row < input.getNumRows(); row++) {
      for (int col = 0; col < input.getNumCols(); col++) {
        output.set(row, col, Math.sqrt(input.get(row, col)));
      }
    }
    return output;
  }

  /**
   * Calculates the absolute value of every element in the matrix.
   * 
   * @param input the input matrix
   * @return the absolute value of every element in the input matrix
   */
  public static DMatrixRMaj elementAbsoluteValue(DMatrixRMaj input) {
    DMatrixRMaj output = new DMatrixRMaj(input.getNumRows(), input.getNumCols());
    for (int row = 0; row < input.getNumRows(); row++) {
      for (int col = 0; col < input.getNumCols(); col++) {
        output.set(row, col, Math.abs(input.get(row, col)));
      }
    }
    return output;
  }
}
