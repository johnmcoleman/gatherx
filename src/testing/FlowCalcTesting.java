package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.ejml.EjmlUnitTests;
import org.ejml.data.DMatrixRMaj;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.FlowCalc;
import model.Graph;
import model.GraphMessage;
import model.Node;
import model.Pipe;

public class FlowCalcTesting {
  // Looped pressures and volumes test case
  private static Node node1;
  private static Node node2;
  private static Node node3;
  private static Node node4;
  private static Node node5;
  private static Node node6;
  private static Pipe pipe1;
  private static Pipe pipe2;
  private static Pipe pipe3;
  private static Pipe pipe4;
  private static Pipe pipe5;
  private static Pipe pipe6;
  private static List<Node> node1NeighborsList;
  private static List<Node> node2NeighborsList;
  private static List<Node> node3NeighborsList;
  private static List<Node> node4NeighborsList;
  private static List<Node> node5NeighborsList;
  private static List<Node> node6NeighborsList;
  private static ArrayList<Node> nodeList;
  private static ArrayList<Pipe> pipeList;
  private static Graph loopedGraph;
  DMatrixRMaj loopedGraphVariableConnectionMatrix;
  DMatrixRMaj loopedGraphVariableConnectionMatrixTransposed;
  DMatrixRMaj loopedGraphConstantConnectionMatrix;
  DMatrixRMaj loopedGraphConstantConnectionMatrixTransposed;

  // Looped pressures and volumes test case answer
  private static Node node7PressureVolumesAnswer;
  private static Node node8PressureVolumesAnswer;
  private static Node node9PressureVolumesAnswer;
  private static Node node10PressureVolumesAnswer;
  private static Node node11PressureVolumesAnswer;
  private static Node node12PressureVolumesAnswer;
  private static Pipe pipe7PressureVolumesAnswer;
  private static Pipe pipe8PressureVolumesAnswer;
  private static Pipe pipe9PressureVolumesAnswer;
  private static Pipe pipe10PressureVolumesAnswer;
  private static Pipe pipe11PressureVolumesAnswer;
  private static Pipe pipe12PressureVolumesAnswer;
  private static List<Node> node7NeighborsListPressureVolumesAnswer;
  private static List<Node> node8NeighborsListPressureVolumesAnswer;
  private static List<Node> node9NeighborsListPressureVolumesAnswer;
  private static List<Node> node10NeighborsListPressureVolumesAnswer;
  private static List<Node> node11NeighborsListPressureVolumesAnswer;
  private static List<Node> node12NeighborsListPressureVolumesAnswer;
  private static ArrayList<Node> nodeList1PressureVolumesAnswer;
  private static ArrayList<Pipe> pipeList1PressureVolumesAnswer;
  private static Graph loopedGraphPressureVolumesAnswer;

  // Looped pressures and efficiencies test case
  private static Node node1LoopedEfficiency;
  private static Node node2LoopedEfficiency;
  private static Node node3LoopedEfficiency;
  private static Node node4LoopedEfficiency;
  private static Node node5LoopedEfficiency;
  private static Node node6LoopedEfficiency;
  private static Pipe pipe1LoopedEfficiency;
  private static Pipe pipe2LoopedEfficiency;
  private static Pipe pipe3LoopedEfficiency;
  private static Pipe pipe4LoopedEfficiency;
  private static Pipe pipe5LoopedEfficiency;
  private static Pipe pipe6LoopedEfficiency;
  private static List<Node> node1NeighborsListLoopedEfficiency;
  private static List<Node> node2NeighborsListLoopedEfficiency;
  private static List<Node> node3NeighborsListLoopedEfficiency;
  private static List<Node> node4NeighborsListLoopedEfficiency;
  private static List<Node> node5NeighborsListLoopedEfficiency;
  private static List<Node> node6NeighborsListLoopedEfficiency;
  private static ArrayList<Node> nodeListLoopedEfficiency;
  private static ArrayList<Pipe> pipeListLoopedEfficiency;
  private static Graph loopedGraphEfficiency;

  // Looped pressures and efficiencies answer
  private static Node node1LoopedEfficiencyAnswer;
  private static Node node2LoopedEfficiencyAnswer;
  private static Node node3LoopedEfficiencyAnswer;
  private static Node node4LoopedEfficiencyAnswer;
  private static Node node5LoopedEfficiencyAnswer;
  private static Node node6LoopedEfficiencyAnswer;
  private static Pipe pipe1LoopedEfficiencyAnswer;
  private static Pipe pipe2LoopedEfficiencyAnswer;
  private static Pipe pipe3LoopedEfficiencyAnswer;
  private static Pipe pipe4LoopedEfficiencyAnswer;
  private static Pipe pipe5LoopedEfficiencyAnswer;
  private static Pipe pipe6LoopedEfficiencyAnswer;
  private static List<Node> node1NeighborsListLoopedEfficiencyAnswer;
  private static List<Node> node2NeighborsListLoopedEfficiencyAnswer;
  private static List<Node> node3NeighborsListLoopedEfficiencyAnswer;
  private static List<Node> node4NeighborsListLoopedEfficiencyAnswer;
  private static List<Node> node5NeighborsListLoopedEfficiencyAnswer;
  private static List<Node> node6NeighborsListLoopedEfficiencyAnswer;
  private static ArrayList<Node> nodeListLoopedEfficiencyAnswer;
  private static ArrayList<Pipe> pipeListLoopedEfficiencyAnswer;
  private static Graph loopedGraphEfficiencyAnswer;

  // Tree pressures and efficiencies test case
  private static Node node1Tree;
  private static Node node2Tree;
  private static Node node3Tree;
  private static Node node4Tree;
  private static Node node5Tree;
  private static Pipe pipe1Tree;
  private static Pipe pipe2Tree;
  private static Pipe pipe3Tree;
  private static Pipe pipe4Tree;
  private static List<Node> node1NeighborsListTree;
  private static List<Node> node2NeighborsListTree;
  private static List<Node> node3NeighborsListTree;
  private static List<Node> node4NeighborsListTree;
  private static List<Node> node5NeighborsListTree;
  private static ArrayList<Node> nodeListTree;
  private static ArrayList<Pipe> pipeListTree;
  private static Graph treeGraph;

  // Tree pressure calculation answer
  private static Node node1TreePressureAnswer;
  private static Node node2TreePressureAnswer;
  private static Node node3TreePressureAnswer;
  private static Node node4TreePressureAnswer;
  private static Node node5TreePressureAnswer;
  private static Pipe pipe1TreePressureAnswer;
  private static Pipe pipe2TreePressureAnswer;
  private static Pipe pipe3TreePressureAnswer;
  private static Pipe pipe4TreePressureAnswer;
  private static List<Node> node1NeighborsListTreePressureAnswer;
  private static List<Node> node2NeighborsListTreePressureAnswer;
  private static List<Node> node3NeighborsListTreePressureAnswer;
  private static List<Node> node4NeighborsListTreePressureAnswer;
  private static List<Node> node5NeighborsListTreePressureAnswer;
  private static ArrayList<Node> nodeListTreePressureAnswer;
  private static ArrayList<Pipe> pipeListTreePressureAnswer;
  private static Graph treeGraphPressureAnswer;

  // Tree efficiency calculation answer
  private static Node node1TreeEfficiencyAnswer;
  private static Node node2TreeEfficiencyAnswer;
  private static Node node3TreeEfficiencyAnswer;
  private static Node node4TreeEfficiencyAnswer;
  private static Node node5TreeEfficiencyAnswer;
  private static Pipe pipe1TreeEfficiencyAnswer;
  private static Pipe pipe2TreeEfficiencyAnswer;
  private static Pipe pipe3TreeEfficiencyAnswer;
  private static Pipe pipe4TreeEfficiencyAnswer;
  private static List<Node> node1NeighborsListTreeEfficiencyAnswer;
  private static List<Node> node2NeighborsListTreeEfficiencyAnswer;
  private static List<Node> node3NeighborsListTreeEfficiencyAnswer;
  private static List<Node> node4NeighborsListTreeEfficiencyAnswer;
  private static List<Node> node5NeighborsListTreeEfficiencyAnswer;
  private static ArrayList<Node> nodeListTreeEfficiencyAnswer;
  private static ArrayList<Pipe> pipeListTreeEfficiencyAnswer;
  private static Graph treeGraphEfficiencyAnswer;

  // Single inlet to outlet pressures and volumes test case
  private static Node node1Basic;
  private static Node node2Basic;
  private static Pipe pipe1Basic;
  private static List<Node> node1BasicNeighborsList;
  private static List<Node> node2BasicNeighborsList;
  private static ArrayList<Node> nodeListBasic;
  private static ArrayList<Pipe> pipeListBasic;
  private static Graph graphBasic;

  /*
   * Sets up the graph objects needed to test the functions
   */
  @BeforeEach
  void setup() {
    /*
     * Instantiates node and pipe objects to input into the looped graph pressure and volume
     * calculation test case
     */
    node1 = new Node("Node1", "Inlet", 5000000.0, 0.0, 1000.0, 1000.0, 0, 0, "neutral");
    node2 = new Node("Node2", "Tee", 0.0, 0.0, 800.0, 800.0, 0, 0, "neutral");
    node3 = new Node("Node3", "Inlet", 2000000.0, 0.0, 700.0, 700.0, 0, 0, "neutral");
    node4 = new Node("Node4", "Outlet", 0.0, 0.0, 200.0, 200.0, 0, 0, "neutral");
    node5 = new Node("Node5", "Inlet", 3000000.0, 0.0, 600.0, 600.0, 0, 0, "neutral");
    node6 = new Node("Node6", "Inlet", 1000000.0, 0.0, 200.0, 200.0, 0, 0, "neutral");
    pipe1 = new Pipe("Pipe1", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);
    pipe2 = new Pipe("Pipe2", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);
    pipe3 = new Pipe("Pipe3", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);
    pipe4 = new Pipe("Pipe4", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);
    pipe5 = new Pipe("Pipe5", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);
    pipe6 = new Pipe("Pipe6", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);
    // Node 1 connections
    node1NeighborsList = new ArrayList<Node>();
    node1NeighborsList.add(node2);
    node1.setNeighbors(node1NeighborsList);
    // Node 2 connections
    node2NeighborsList = new ArrayList<Node>();
    node2NeighborsList.add(node1);
    node2NeighborsList.add(node3);
    node2NeighborsList.add(node5);
    node2.setNeighbors(node2NeighborsList);
    // Node 3 connections
    node3NeighborsList = new ArrayList<Node>();
    node3NeighborsList.add(node2);
    node3NeighborsList.add(node4);
    node3NeighborsList.add(node5);
    node3.setNeighbors(node3NeighborsList);
    // Node 4 connections
    node4NeighborsList = new ArrayList<Node>();
    node4NeighborsList.add(node3);
    node4.setNeighbors(node4NeighborsList);
    // Node 5 connections
    node5NeighborsList = new ArrayList<Node>();
    node5NeighborsList.add(node2);
    node5NeighborsList.add(node3);
    node5NeighborsList.add(node6);
    node5.setNeighbors(node5NeighborsList);
    // Node 6 connections
    node6NeighborsList = new ArrayList<Node>();
    node6NeighborsList.add(node5);
    node6.setNeighbors(node6NeighborsList);
    // Pipe 1 end connections
    pipe1.setEnd1(node1);
    pipe1.setEnd2(node2);
    // Pipe 2 end connections
    pipe2.setEnd1(node3);
    pipe2.setEnd2(node2);
    // Pipe 3 end connections
    pipe3.setEnd1(node2);
    pipe3.setEnd2(node5);
    // Pipe 4 end connections
    pipe4.setEnd1(node3);
    pipe4.setEnd2(node5);
    // Pipe 5 end connections
    pipe5.setEnd1(node5);
    pipe5.setEnd2(node6);
    // Pipe 6 end connections
    pipe6.setEnd1(node3);
    pipe6.setEnd2(node4);

    // Instantiates the graph
    nodeList = new ArrayList<Node>();
    nodeList.add(node1);
    nodeList.add(node2);
    nodeList.add(node3);
    nodeList.add(node4);
    nodeList.add(node5);
    nodeList.add(node6);
    pipeList = new ArrayList<Pipe>();
    pipeList.add(pipe1);
    pipeList.add(pipe2);
    pipeList.add(pipe3);
    pipeList.add(pipe4);
    pipeList.add(pipe5);
    pipeList.add(pipe6);
    loopedGraph = new Graph(nodeList, pipeList);

    loopedGraphVariableConnectionMatrix = loopedGraph.getVariableConnectionMatrix();
    loopedGraphVariableConnectionMatrixTransposed =
        loopedGraph.getVariableConnectionMatrixTransposed();
    loopedGraphConstantConnectionMatrix = loopedGraph.getConstantConnectionMatrix();
    loopedGraphConstantConnectionMatrixTransposed =
        loopedGraph.getConstantConnectionMatrixTransposed();

    /*
     * Instantiates node and pipe objects to the pressure and volumes answer graph object
     */
    node7PressureVolumesAnswer = new Node("Node1", "Inlet", 5000000.0, 0.0,
        1.0e+02 * 4.538021801280556, 1.0e+02 * 4.538021801280556, 0, 0, "success");
    node8PressureVolumesAnswer = new Node("Node2", "Tee", 0.0, 0.0, 1.0e+02 * 4.254425619315318,
        1.0e+02 * 4.254425619315318, 0, 0, "success");
    node9PressureVolumesAnswer = new Node("Node3", "Inlet", 2000000.0, 0.0,
        1.0e+02 * 4.008561072264924, 1.0e+02 * 4.008561072264924, 0, 0, "success");
    node10PressureVolumesAnswer =
        new Node("Node4", "Outlet", 0.0, 0.0, 200.0, 200.0, 0, 0, "success");
    node11PressureVolumesAnswer = new Node("Node5", "Inlet", 3000000.0, 0.0,
        1.0e+02 * 4.251646539166847, 1.0e+02 * 4.251646539166847, 0, 0, "success");
    node12PressureVolumesAnswer = new Node("Node6", "Inlet", 1000000.0, 0.0,
        1.0e+02 * 4.263359998258871, 1.0e+02 * 4.263359998258871, 0, 0, "success");
    pipe7PressureVolumesAnswer = new Pipe("Pipe1", 1.0e+07 * 0.500000000000000, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
    pipe8PressureVolumesAnswer = new Pipe("Pipe2", 1.0e+07 * -0.451316701949486, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
    pipe9PressureVolumesAnswer = new Pipe("Pipe3", 1.0e+07 * 0.048683298050514, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
    pipe10PressureVolumesAnswer = new Pipe("Pipe4", 1.0e+07 * -0.448683298050514, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
    pipe11PressureVolumesAnswer = new Pipe("Pipe5", 1.0e+07 * -0.100000000000000, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
    pipe12PressureVolumesAnswer = new Pipe("Pipe6", 1.0e+07 * 1.100000000000000, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
    // Node 7 connections
    node7NeighborsListPressureVolumesAnswer = new ArrayList<Node>();
    node7NeighborsListPressureVolumesAnswer.add(node8PressureVolumesAnswer);
    node7PressureVolumesAnswer.setNeighbors(node7NeighborsListPressureVolumesAnswer);
    // Node 8 connections
    node8NeighborsListPressureVolumesAnswer = new ArrayList<Node>();
    node8NeighborsListPressureVolumesAnswer.add(node7PressureVolumesAnswer);
    node8NeighborsListPressureVolumesAnswer.add(node9PressureVolumesAnswer);
    node8NeighborsListPressureVolumesAnswer.add(node11PressureVolumesAnswer);
    node8PressureVolumesAnswer.setNeighbors(node8NeighborsListPressureVolumesAnswer);
    // Node 9 connections
    node9NeighborsListPressureVolumesAnswer = new ArrayList<Node>();
    node9NeighborsListPressureVolumesAnswer.add(node8PressureVolumesAnswer);
    node9NeighborsListPressureVolumesAnswer.add(node10PressureVolumesAnswer);
    node9NeighborsListPressureVolumesAnswer.add(node11PressureVolumesAnswer);
    node9PressureVolumesAnswer.setNeighbors(node9NeighborsListPressureVolumesAnswer);
    // Node 10 connections
    node10NeighborsListPressureVolumesAnswer = new ArrayList<Node>();
    node10NeighborsListPressureVolumesAnswer.add(node9PressureVolumesAnswer);
    node10PressureVolumesAnswer.setNeighbors(node10NeighborsListPressureVolumesAnswer);
    // Node 11 connections
    node11NeighborsListPressureVolumesAnswer = new ArrayList<Node>();
    node11NeighborsListPressureVolumesAnswer.add(node8PressureVolumesAnswer);
    node11NeighborsListPressureVolumesAnswer.add(node9PressureVolumesAnswer);
    node11NeighborsListPressureVolumesAnswer.add(node12PressureVolumesAnswer);
    node11PressureVolumesAnswer.setNeighbors(node11NeighborsListPressureVolumesAnswer);
    // Node 12 connections
    node12NeighborsListPressureVolumesAnswer = new ArrayList<Node>();
    node12NeighborsListPressureVolumesAnswer.add(node11PressureVolumesAnswer);
    node12PressureVolumesAnswer.setNeighbors(node12NeighborsListPressureVolumesAnswer);
    // Pipe 7 end connections
    pipe7PressureVolumesAnswer.setEnd1(node7PressureVolumesAnswer);
    pipe7PressureVolumesAnswer.setEnd2(node8PressureVolumesAnswer);
    // Pipe 8 end connections
    pipe8PressureVolumesAnswer.setEnd1(node9PressureVolumesAnswer);
    pipe8PressureVolumesAnswer.setEnd2(node8PressureVolumesAnswer);
    // Pipe 9 end connections
    pipe9PressureVolumesAnswer.setEnd1(node8PressureVolumesAnswer);
    pipe9PressureVolumesAnswer.setEnd2(node11PressureVolumesAnswer);
    // Pipe 10 end connections
    pipe10PressureVolumesAnswer.setEnd1(node9PressureVolumesAnswer);
    pipe10PressureVolumesAnswer.setEnd2(node11PressureVolumesAnswer);
    // Pipe 11 end connections
    pipe11PressureVolumesAnswer.setEnd1(node11PressureVolumesAnswer);
    pipe11PressureVolumesAnswer.setEnd2(node12PressureVolumesAnswer);
    // Pipe 12 end connections
    pipe12PressureVolumesAnswer.setEnd1(node9PressureVolumesAnswer);
    pipe12PressureVolumesAnswer.setEnd2(node10PressureVolumesAnswer);

    // Instantiates the graph
    nodeList1PressureVolumesAnswer = new ArrayList<Node>();
    nodeList1PressureVolumesAnswer.add(node7PressureVolumesAnswer);
    nodeList1PressureVolumesAnswer.add(node8PressureVolumesAnswer);
    nodeList1PressureVolumesAnswer.add(node9PressureVolumesAnswer);
    nodeList1PressureVolumesAnswer.add(node10PressureVolumesAnswer);
    nodeList1PressureVolumesAnswer.add(node11PressureVolumesAnswer);
    nodeList1PressureVolumesAnswer.add(node12PressureVolumesAnswer);
    pipeList1PressureVolumesAnswer = new ArrayList<Pipe>();
    pipeList1PressureVolumesAnswer.add(pipe7PressureVolumesAnswer);
    pipeList1PressureVolumesAnswer.add(pipe8PressureVolumesAnswer);
    pipeList1PressureVolumesAnswer.add(pipe9PressureVolumesAnswer);
    pipeList1PressureVolumesAnswer.add(pipe10PressureVolumesAnswer);
    pipeList1PressureVolumesAnswer.add(pipe11PressureVolumesAnswer);
    pipeList1PressureVolumesAnswer.add(pipe12PressureVolumesAnswer);
    loopedGraphPressureVolumesAnswer =
        new Graph(nodeList1PressureVolumesAnswer, pipeList1PressureVolumesAnswer);

    /*
     * Instantiates node and pipe objects to the pressure and efficiency calculation looped test
     * case for use with each sub function of the calculation
     */
    node1LoopedEfficiency =
        new Node("Node1", "Inlet", 5000000.0, 0.0, Math.sqrt(2.059364186889763 * 1e+05 * 1.01),
            Math.sqrt(2.059364186889763 * 1e+05 * 1.01), 0, 0, "success");
    node2LoopedEfficiency = new Node("Node2", "Tee", 0.0, 0.0, 700.0, 700.0, 0, 0, "success");
    node3LoopedEfficiency =
        new Node("Node3", "Inlet", 2000000.0, 0.0, Math.sqrt(1.606856187007772 * 1e+05 * 1.02),
            Math.sqrt(1.606856187007772 * 1e+05 * 1.02), 0, 0, "success");
    node4LoopedEfficiency = new Node("Node4", "Outlet", 0.0, 0.0, 200.0, 200.0, 0, 0, "success");
    node5LoopedEfficiency =
        new Node("Node5", "Inlet", 3000000.0, 0.0, Math.sqrt(1.807649829400943 * 1e+05 * 1.01),
            Math.sqrt(1.807649829400943 * 1e+05 * 1.01), 0, 0, "success");
    node6LoopedEfficiency =
        new Node("Node6", "Inlet", 1000000.0, 0.0, Math.sqrt(1.817623847475387 * 1e+05),
            Math.sqrt(1.817623847475387 * 1e+05), 0, 0, "success");
    pipe1LoopedEfficiency = new Pipe("Pipe1", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1,
        1d, 1d, "success", 1d);
    pipe2LoopedEfficiency = new Pipe("Pipe2", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1,
        1d, 1d, "success", 1d);
    pipe3LoopedEfficiency = new Pipe("Pipe3", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1,
        1d, 1d, "success", 1d);
    pipe4LoopedEfficiency = new Pipe("Pipe4", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1,
        1d, 1d, "success", 1d);
    pipe5LoopedEfficiency = new Pipe("Pipe5", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1,
        1d, 1d, "success", 1d);
    pipe6LoopedEfficiency = new Pipe("Pipe6", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1,
        1d, 1d, "success", 1d);
    // Node 1 connections
    node1NeighborsListLoopedEfficiency = new ArrayList<Node>();
    node1NeighborsListLoopedEfficiency.add(node2LoopedEfficiency);
    node1LoopedEfficiency.setNeighbors(node1NeighborsListLoopedEfficiency);
    // Node 2 connections
    node2NeighborsListLoopedEfficiency = new ArrayList<Node>();
    node2NeighborsListLoopedEfficiency.add(node1LoopedEfficiency);
    node2NeighborsListLoopedEfficiency.add(node3LoopedEfficiency);
    node2NeighborsListLoopedEfficiency.add(node5LoopedEfficiency);
    node2LoopedEfficiency.setNeighbors(node2NeighborsListLoopedEfficiency);
    // Node 3 connections
    node3NeighborsListLoopedEfficiency = new ArrayList<Node>();
    node3NeighborsListLoopedEfficiency.add(node2LoopedEfficiency);
    node3NeighborsListLoopedEfficiency.add(node4LoopedEfficiency);
    node3NeighborsListLoopedEfficiency.add(node5LoopedEfficiency);
    node3LoopedEfficiency.setNeighbors(node3NeighborsListLoopedEfficiency);
    // Node 4 connections
    node4NeighborsListLoopedEfficiency = new ArrayList<Node>();
    node4NeighborsListLoopedEfficiency.add(node3LoopedEfficiency);
    node4LoopedEfficiency.setNeighbors(node4NeighborsListLoopedEfficiency);
    // Node 5 connections
    node5NeighborsListLoopedEfficiency = new ArrayList<Node>();
    node5NeighborsListLoopedEfficiency.add(node2LoopedEfficiency);
    node5NeighborsListLoopedEfficiency.add(node3LoopedEfficiency);
    node5NeighborsListLoopedEfficiency.add(node6LoopedEfficiency);
    node5LoopedEfficiency.setNeighbors(node5NeighborsListLoopedEfficiency);
    // Node 6 connections
    node6NeighborsListLoopedEfficiency = new ArrayList<Node>();
    node6NeighborsListLoopedEfficiency.add(node5LoopedEfficiency);
    node6LoopedEfficiency.setNeighbors(node6NeighborsListLoopedEfficiency);
    // Pipe 1 end connections
    pipe1LoopedEfficiency.setEnd1(node1LoopedEfficiency);
    pipe1LoopedEfficiency.setEnd2(node2LoopedEfficiency);
    // Pipe 2 end connections
    pipe2LoopedEfficiency.setEnd1(node3LoopedEfficiency);
    pipe2LoopedEfficiency.setEnd2(node2LoopedEfficiency);
    // Pipe 3 end connections
    pipe3LoopedEfficiency.setEnd1(node2LoopedEfficiency);
    pipe3LoopedEfficiency.setEnd2(node5LoopedEfficiency);
    // Pipe 4 end connections
    pipe4LoopedEfficiency.setEnd1(node3LoopedEfficiency);
    pipe4LoopedEfficiency.setEnd2(node5LoopedEfficiency);
    // Pipe 5 end connections
    pipe5LoopedEfficiency.setEnd1(node5LoopedEfficiency);
    pipe5LoopedEfficiency.setEnd2(node6LoopedEfficiency);
    // Pipe 6 end connections
    pipe6LoopedEfficiency.setEnd1(node3LoopedEfficiency);
    pipe6LoopedEfficiency.setEnd2(node4LoopedEfficiency);

    // Instantiates the graph and executes the test
    nodeListLoopedEfficiency = new ArrayList<Node>();
    nodeListLoopedEfficiency.add(node1LoopedEfficiency);
    nodeListLoopedEfficiency.add(node2LoopedEfficiency);
    nodeListLoopedEfficiency.add(node3LoopedEfficiency);
    nodeListLoopedEfficiency.add(node4LoopedEfficiency);
    nodeListLoopedEfficiency.add(node5LoopedEfficiency);
    nodeListLoopedEfficiency.add(node6LoopedEfficiency);
    pipeListLoopedEfficiency = new ArrayList<Pipe>();
    pipeListLoopedEfficiency.add(pipe1LoopedEfficiency);
    pipeListLoopedEfficiency.add(pipe2LoopedEfficiency);
    pipeListLoopedEfficiency.add(pipe3LoopedEfficiency);
    pipeListLoopedEfficiency.add(pipe4LoopedEfficiency);
    pipeListLoopedEfficiency.add(pipe5LoopedEfficiency);
    pipeListLoopedEfficiency.add(pipe6LoopedEfficiency);
    loopedGraphEfficiency = new Graph(nodeListLoopedEfficiency, pipeListLoopedEfficiency);

    /*
     * Answer to the looped graph after an efficiency calculation
     */
    node1LoopedEfficiencyAnswer = new Node("Node1", "Inlet", 5000000.0, 0.0,
        1.0e+02 * 4.089140942950780, 1.0e+02 * 4.089140942950780, 0, 0, "success");
    node2LoopedEfficiencyAnswer = new Node("Node2", "Tee", 0.0, 0.0, 1.0e+02 * 3.767110716370531,
        1.0e+02 * 3.767110716370531, 0, 0, "success");
    node3LoopedEfficiencyAnswer = new Node("Node3", "Inlet", 2000000.0, 0.0,
        1.0e+02 * 4.048448234506559, 1.0e+02 * 4.048448234506559, 0, 0, "success");
    node4LoopedEfficiencyAnswer =
        new Node("Node4", "Outlet", 0.0, 0.0, 200.0, 200.0, 0, 0, "success");
    node5LoopedEfficiencyAnswer = new Node("Node5", "Inlet", 3000000.0, 0.0,
        1.0e+02 * 3.765095636171905, 1.0e+02 * 3.765095636171905, 0, 0, "success");
    node6LoopedEfficiencyAnswer = new Node("Node6", "Inlet", 1000000.0, 0.0,
        1.0e+02 * 3.733075016150462, 1.0e+02 * 3.733075016150462, 0, 0, "success");
    pipe1LoopedEfficiencyAnswer = new Pipe("Pipe1", 1.0e+07 * 0.500000000000000, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 0.992770966113299, 1d, "success", 1d);
    pipe2LoopedEfficiencyAnswer = new Pipe("Pipe2", 1.0e+07 * -0.451316701949486, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 0.955280517515971, 1d, "success", 1d);
    pipe3LoopedEfficiencyAnswer = new Pipe("Pipe3", 1.0e+07 * 0.048683298050514, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 1.319464430055245, 1d, "success", 1d);
    pipe4LoopedEfficiencyAnswer = new Pipe("Pipe4", 1.0e+07 * -0.448683298050514, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 0.958248475237316, 1d, "success", 1d);
    pipe5LoopedEfficiencyAnswer = new Pipe("Pipe5", 1.0e+07 * -0.100000000000000, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 0.644529124569670, 1d, "success", 1d);
    pipe6LoopedEfficiencyAnswer = new Pipe("Pipe6", 1.0e+07 * 1.100000000000000, 1d, 4d, 1d, 1d,
        0.80, 14.7, 530d, 520d, 0.9, 0.1, 0.986945746727844, 1d, "success", 1d);
    // Node 1 connections
    node1NeighborsListLoopedEfficiencyAnswer = new ArrayList<Node>();
    node1NeighborsListLoopedEfficiencyAnswer.add(node2LoopedEfficiencyAnswer);
    node1LoopedEfficiencyAnswer.setNeighbors(node1NeighborsListLoopedEfficiencyAnswer);
    // Node 2 connections
    node2NeighborsListLoopedEfficiencyAnswer = new ArrayList<Node>();
    node2NeighborsListLoopedEfficiencyAnswer.add(node1LoopedEfficiencyAnswer);
    node2NeighborsListLoopedEfficiencyAnswer.add(node3LoopedEfficiencyAnswer);
    node2NeighborsListLoopedEfficiencyAnswer.add(node5LoopedEfficiencyAnswer);
    node2LoopedEfficiencyAnswer.setNeighbors(node2NeighborsListLoopedEfficiencyAnswer);
    // Node 3 connections
    node3NeighborsListLoopedEfficiencyAnswer = new ArrayList<Node>();
    node3NeighborsListLoopedEfficiencyAnswer.add(node2LoopedEfficiencyAnswer);
    node3NeighborsListLoopedEfficiencyAnswer.add(node4LoopedEfficiencyAnswer);
    node3NeighborsListLoopedEfficiencyAnswer.add(node5LoopedEfficiencyAnswer);
    node3LoopedEfficiencyAnswer.setNeighbors(node3NeighborsListLoopedEfficiencyAnswer);
    // Node 4 connections
    node4NeighborsListLoopedEfficiencyAnswer = new ArrayList<Node>();
    node4NeighborsListLoopedEfficiencyAnswer.add(node3LoopedEfficiencyAnswer);
    node4LoopedEfficiencyAnswer.setNeighbors(node4NeighborsListLoopedEfficiencyAnswer);
    // Node 5 connections
    node5NeighborsListLoopedEfficiencyAnswer = new ArrayList<Node>();
    node5NeighborsListLoopedEfficiencyAnswer.add(node2LoopedEfficiencyAnswer);
    node5NeighborsListLoopedEfficiencyAnswer.add(node3LoopedEfficiencyAnswer);
    node5NeighborsListLoopedEfficiencyAnswer.add(node6LoopedEfficiencyAnswer);
    node5LoopedEfficiencyAnswer.setNeighbors(node5NeighborsListLoopedEfficiencyAnswer);
    // Node 6 connections
    node6NeighborsListLoopedEfficiencyAnswer = new ArrayList<Node>();
    node6NeighborsListLoopedEfficiencyAnswer.add(node5LoopedEfficiencyAnswer);
    node6LoopedEfficiencyAnswer.setNeighbors(node6NeighborsListLoopedEfficiencyAnswer);
    // Pipe 1 end connections
    pipe1LoopedEfficiencyAnswer.setEnd1(node1LoopedEfficiencyAnswer);
    pipe1LoopedEfficiencyAnswer.setEnd2(node2LoopedEfficiencyAnswer);
    // Pipe 2 end connections
    pipe2LoopedEfficiencyAnswer.setEnd1(node3LoopedEfficiencyAnswer);
    pipe2LoopedEfficiencyAnswer.setEnd2(node2LoopedEfficiencyAnswer);
    // Pipe 3 end connections
    pipe3LoopedEfficiencyAnswer.setEnd1(node2LoopedEfficiencyAnswer);
    pipe3LoopedEfficiencyAnswer.setEnd2(node5LoopedEfficiencyAnswer);
    // Pipe 4 end connections
    pipe4LoopedEfficiencyAnswer.setEnd1(node3LoopedEfficiencyAnswer);
    pipe4LoopedEfficiencyAnswer.setEnd2(node5LoopedEfficiencyAnswer);
    // Pipe 5 end connections
    pipe5LoopedEfficiencyAnswer.setEnd1(node5LoopedEfficiencyAnswer);
    pipe5LoopedEfficiencyAnswer.setEnd2(node6LoopedEfficiencyAnswer);
    // Pipe 6 end connections
    pipe6LoopedEfficiencyAnswer.setEnd1(node3LoopedEfficiencyAnswer);
    pipe6LoopedEfficiencyAnswer.setEnd2(node4LoopedEfficiencyAnswer);

    // Instantiates the graph and executes the test
    nodeListLoopedEfficiencyAnswer = new ArrayList<Node>();
    nodeListLoopedEfficiencyAnswer.add(node1LoopedEfficiencyAnswer);
    nodeListLoopedEfficiencyAnswer.add(node2LoopedEfficiencyAnswer);
    nodeListLoopedEfficiencyAnswer.add(node3LoopedEfficiencyAnswer);
    nodeListLoopedEfficiencyAnswer.add(node4LoopedEfficiencyAnswer);
    nodeListLoopedEfficiencyAnswer.add(node5LoopedEfficiencyAnswer);
    nodeListLoopedEfficiencyAnswer.add(node6LoopedEfficiencyAnswer);
    pipeListLoopedEfficiencyAnswer = new ArrayList<Pipe>();
    pipeListLoopedEfficiencyAnswer.add(pipe1LoopedEfficiencyAnswer);
    pipeListLoopedEfficiencyAnswer.add(pipe2LoopedEfficiencyAnswer);
    pipeListLoopedEfficiencyAnswer.add(pipe3LoopedEfficiencyAnswer);
    pipeListLoopedEfficiencyAnswer.add(pipe4LoopedEfficiencyAnswer);
    pipeListLoopedEfficiencyAnswer.add(pipe5LoopedEfficiencyAnswer);
    pipeListLoopedEfficiencyAnswer.add(pipe6LoopedEfficiencyAnswer);
    loopedGraphEfficiencyAnswer =
        new Graph(nodeListLoopedEfficiencyAnswer, pipeListLoopedEfficiencyAnswer);

    /*
     * Instantiates node and pipe objects to the pressure and efficiency calculation tree test case
     * for use with each sub function of the calculation
     */
    node1Tree = new Node("Node1", "Outlet", 0, 0.0, 100.0, 100.0, 0, 0, "success");
    node2Tree = new Node("Node2", "Inlet", 2e6, 0.0, 163d, 163d, 0, 0, "success");
    node3Tree = new Node("Node3", "Inlet", 1e6, 0.0, 167d, 167d, 0, 0, "success");
    node4Tree = new Node("Node4", "Tee", 0.0, 0.0, 164.1823094, 164.1823094, 0, 0, "success");
    node5Tree = new Node("Node5", "Inlet", 1e6, 0.0, 170d, 170d, 0, 0, "success");
    pipe1Tree = new Pipe("Pipe1", 0.0, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);
    pipe2Tree = new Pipe("Pipe2", 0.0, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);
    pipe3Tree = new Pipe("Pipe3", 0.0, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);
    pipe4Tree = new Pipe("Pipe4", 0.0, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);

    // Node 1 connections
    node1NeighborsListTree = new ArrayList<Node>();
    node1NeighborsListTree.add(node2Tree);
    node1Tree.setNeighbors(node1NeighborsListTree);
    // Node 2 connections
    node2NeighborsListTree = new ArrayList<Node>();
    node2NeighborsListTree.add(node1Tree);
    node2NeighborsListTree.add(node3Tree);
    node2NeighborsListTree.add(node4Tree);
    node2Tree.setNeighbors(node2NeighborsListTree);
    // Node 3 connections
    node3NeighborsListTree = new ArrayList<Node>();
    node3NeighborsListTree.add(node2Tree);
    node3Tree.setNeighbors(node3NeighborsListTree);
    // Node 4 connections
    node4NeighborsListTree = new ArrayList<Node>();
    node4NeighborsListTree.add(node2Tree);
    node4NeighborsListTree.add(node5Tree);
    node4Tree.setNeighbors(node4NeighborsListTree);
    // Node 5 connections
    node5NeighborsListTree = new ArrayList<Node>();
    node5NeighborsListTree.add(node4Tree);
    node5Tree.setNeighbors(node5NeighborsListTree);
    // Pipe 1 end connections
    pipe1Tree.setEnd1(node5Tree);
    pipe1Tree.setEnd2(node4Tree);
    // Pipe 2 end connections
    pipe2Tree.setEnd1(node2Tree);
    pipe2Tree.setEnd2(node4Tree);
    // Pipe 3 end connections
    pipe3Tree.setEnd1(node2Tree);
    pipe3Tree.setEnd2(node3Tree);
    // Pipe 4 end connections
    pipe4Tree.setEnd1(node1Tree);
    pipe4Tree.setEnd2(node2Tree);

    // Instantiates the graph and executes the test
    nodeListTree = new ArrayList<Node>();
    nodeListTree.add(node1Tree);
    nodeListTree.add(node2Tree);
    nodeListTree.add(node3Tree);
    nodeListTree.add(node4Tree);
    nodeListTree.add(node5Tree);
    pipeListTree = new ArrayList<Pipe>();
    pipeListTree.add(pipe1Tree);
    pipeListTree.add(pipe2Tree);
    pipeListTree.add(pipe3Tree);
    pipeListTree.add(pipe4Tree);
    treeGraph = new Graph(nodeListTree, pipeListTree);

    /*
     * Answer to the tree graph after a pressure calculation
     */
    node1TreePressureAnswer = new Node("Node1", "Outlet", 0, 0.0, 100.0, 100.0, 0, 0, "success");
    node2TreePressureAnswer =
        new Node("Node2", "Inlet", 2e6, 0.0, 161.1161969, 161.1161969, 0, 0, "success");
    node3TreePressureAnswer =
        new Node("Node3", "Inlet", 1e6, 0.0, 164.1823094, 164.1823094, 0, 0, "success");
    node4TreePressureAnswer =
        new Node("Node4", "Tee", 0.0, 0.0, 164.1823094, 164.1823094, 0, 0, "success");
    node5TreePressureAnswer =
        new Node("Node5", "Inlet", 1e6, 0.0, 167.1922024, 167.1922024, 0, 0, "success");
    pipe1TreePressureAnswer = new Pipe("Pipe1", 1e6, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9,
        0.1, 1d, 1d, "success", 1d);
    pipe2TreePressureAnswer = new Pipe("Pipe2", -1e6, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9,
        0.1, 1d, 1d, "success", 1d);
    pipe3TreePressureAnswer = new Pipe("Pipe3", -1e6, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9,
        0.1, 1d, 1d, "success", 1d);
    pipe4TreePressureAnswer = new Pipe("Pipe4", -4e6, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9,
        0.1, 1d, 1d, "success", 1d);

    // Node 1 connections
    node1NeighborsListTreePressureAnswer = new ArrayList<Node>();
    node1NeighborsListTreePressureAnswer.add(node2TreePressureAnswer);
    node1TreePressureAnswer.setNeighbors(node1NeighborsListTreePressureAnswer);
    // Node 2 connections
    node2NeighborsListTreePressureAnswer = new ArrayList<Node>();
    node2NeighborsListTreePressureAnswer.add(node1TreePressureAnswer);
    node2NeighborsListTreePressureAnswer.add(node3TreePressureAnswer);
    node2NeighborsListTreePressureAnswer.add(node4TreePressureAnswer);
    node2TreePressureAnswer.setNeighbors(node2NeighborsListTreePressureAnswer);
    // Node 3 connections
    node3NeighborsListTreePressureAnswer = new ArrayList<Node>();
    node3NeighborsListTreePressureAnswer.add(node2TreePressureAnswer);
    node3TreePressureAnswer.setNeighbors(node3NeighborsListTreePressureAnswer);
    // Node 4 connections
    node4NeighborsListTreePressureAnswer = new ArrayList<Node>();
    node4NeighborsListTreePressureAnswer.add(node2TreePressureAnswer);
    node4NeighborsListTreePressureAnswer.add(node5TreePressureAnswer);
    node4TreePressureAnswer.setNeighbors(node4NeighborsListTreePressureAnswer);
    // Node 5 connections
    node5NeighborsListTreePressureAnswer = new ArrayList<Node>();
    node5NeighborsListTreePressureAnswer.add(node4TreePressureAnswer);
    node5TreePressureAnswer.setNeighbors(node5NeighborsListTreePressureAnswer);
    // Pipe 1 end connections
    pipe1TreePressureAnswer.setEnd1(node5TreePressureAnswer);
    pipe1TreePressureAnswer.setEnd2(node4TreePressureAnswer);
    // Pipe 2 end connections
    pipe2TreePressureAnswer.setEnd1(node2TreePressureAnswer);
    pipe2TreePressureAnswer.setEnd2(node4TreePressureAnswer);
    // Pipe 3 end connections
    pipe3TreePressureAnswer.setEnd1(node2TreePressureAnswer);
    pipe3TreePressureAnswer.setEnd2(node3TreePressureAnswer);
    // Pipe 4 end connections
    pipe4TreePressureAnswer.setEnd1(node1TreePressureAnswer);
    pipe4TreePressureAnswer.setEnd2(node2TreePressureAnswer);

    // Instantiates the graph and executes the test
    nodeListTreePressureAnswer = new ArrayList<Node>();
    nodeListTreePressureAnswer.add(node1TreePressureAnswer);
    nodeListTreePressureAnswer.add(node2TreePressureAnswer);
    nodeListTreePressureAnswer.add(node3TreePressureAnswer);
    nodeListTreePressureAnswer.add(node4TreePressureAnswer);
    nodeListTreePressureAnswer.add(node5TreePressureAnswer);
    pipeListTreePressureAnswer = new ArrayList<Pipe>();
    pipeListTreePressureAnswer.add(pipe1TreePressureAnswer);
    pipeListTreePressureAnswer.add(pipe2TreePressureAnswer);
    pipeListTreePressureAnswer.add(pipe3TreePressureAnswer);
    pipeListTreePressureAnswer.add(pipe4TreePressureAnswer);
    treeGraphPressureAnswer = new Graph(nodeListTreePressureAnswer, pipeListTreePressureAnswer);

    /*
     * Answer to the tree graph after an efficiency calculation
     */
    node1TreeEfficiencyAnswer = new Node("Node1", "Outlet", 0, 0.0, 100.0, 100.0, 0, 0, "success");
    node2TreeEfficiencyAnswer = new Node("Node2", "Inlet", 2e6, 0.0, 163d, 163d, 0, 0, "success");
    node3TreeEfficiencyAnswer = new Node("Node3", "Inlet", 1e6, 0.0, 167d, 167d, 0, 0, "success");
    node4TreeEfficiencyAnswer =
        new Node("Node4", "Tee", 0.0, 0.0, 164.1823094, 164.1823094, 0, 0, "success");
    node5TreeEfficiencyAnswer = new Node("Node5", "Inlet", 1e6, 0.0, 170d, 170d, 0, 0, "success");
    pipe1TreeEfficiencyAnswer = new Pipe("Pipe1", 1e6, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9,
        0.1, 0.7162556122440092, 1d, "success", 1d);
    pipe2TreeEfficiencyAnswer = new Pipe("Pipe2", -1e6, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9,
        0.1, 1.6057376620211643, 1d, "success", 1d);
    pipe3TreeEfficiencyAnswer = new Pipe("Pipe3", -1e6, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9,
        0.1, 0.8692568261924385, 1d, "success", 1d);
    pipe4TreeEfficiencyAnswer = new Pipe("Pipe4", -4e6, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9,
        0.1, 0.9814019527865486, 1d, "success", 1d);

    // Node 1 connections
    node1NeighborsListTreeEfficiencyAnswer = new ArrayList<Node>();
    node1NeighborsListTreeEfficiencyAnswer.add(node2TreeEfficiencyAnswer);
    node1TreeEfficiencyAnswer.setNeighbors(node1NeighborsListTreeEfficiencyAnswer);
    // Node 2 connections
    node2NeighborsListTreeEfficiencyAnswer = new ArrayList<Node>();
    node2NeighborsListTreeEfficiencyAnswer.add(node1TreeEfficiencyAnswer);
    node2NeighborsListTreeEfficiencyAnswer.add(node3TreeEfficiencyAnswer);
    node2NeighborsListTreeEfficiencyAnswer.add(node4TreeEfficiencyAnswer);
    node2TreeEfficiencyAnswer.setNeighbors(node2NeighborsListTreeEfficiencyAnswer);
    // Node 3 connections
    node3NeighborsListTreeEfficiencyAnswer = new ArrayList<Node>();
    node3NeighborsListTreeEfficiencyAnswer.add(node2TreeEfficiencyAnswer);
    node3TreeEfficiencyAnswer.setNeighbors(node3NeighborsListTreeEfficiencyAnswer);
    // Node 4 connections
    node4NeighborsListTreeEfficiencyAnswer = new ArrayList<Node>();
    node4NeighborsListTreeEfficiencyAnswer.add(node2TreeEfficiencyAnswer);
    node4NeighborsListTreeEfficiencyAnswer.add(node5TreeEfficiencyAnswer);
    node4TreeEfficiencyAnswer.setNeighbors(node4NeighborsListTreeEfficiencyAnswer);
    // Node 5 connections
    node5NeighborsListTreeEfficiencyAnswer = new ArrayList<Node>();
    node5NeighborsListTreeEfficiencyAnswer.add(node4TreeEfficiencyAnswer);
    node5TreeEfficiencyAnswer.setNeighbors(node5NeighborsListTreeEfficiencyAnswer);
    // Pipe 1 end connections
    pipe1TreeEfficiencyAnswer.setEnd1(node5TreeEfficiencyAnswer);
    pipe1TreeEfficiencyAnswer.setEnd2(node4TreeEfficiencyAnswer);
    // Pipe 2 end connections
    pipe2TreeEfficiencyAnswer.setEnd1(node2TreeEfficiencyAnswer);
    pipe2TreeEfficiencyAnswer.setEnd2(node4TreeEfficiencyAnswer);
    // Pipe 3 end connections
    pipe3TreeEfficiencyAnswer.setEnd1(node2TreeEfficiencyAnswer);
    pipe3TreeEfficiencyAnswer.setEnd2(node3TreeEfficiencyAnswer);
    // Pipe 4 end connections
    pipe4TreeEfficiencyAnswer.setEnd1(node1TreeEfficiencyAnswer);
    pipe4TreeEfficiencyAnswer.setEnd2(node2TreeEfficiencyAnswer);

    // Instantiates the graph and executes the test
    nodeListTreeEfficiencyAnswer = new ArrayList<Node>();
    nodeListTreeEfficiencyAnswer.add(node1TreeEfficiencyAnswer);
    nodeListTreeEfficiencyAnswer.add(node2TreeEfficiencyAnswer);
    nodeListTreeEfficiencyAnswer.add(node3TreeEfficiencyAnswer);
    nodeListTreeEfficiencyAnswer.add(node4TreeEfficiencyAnswer);
    nodeListTreeEfficiencyAnswer.add(node5TreeEfficiencyAnswer);
    pipeListTreeEfficiencyAnswer = new ArrayList<Pipe>();
    pipeListTreeEfficiencyAnswer.add(pipe1TreeEfficiencyAnswer);
    pipeListTreeEfficiencyAnswer.add(pipe2TreeEfficiencyAnswer);
    pipeListTreeEfficiencyAnswer.add(pipe3TreeEfficiencyAnswer);
    pipeListTreeEfficiencyAnswer.add(pipe4TreeEfficiencyAnswer);
    treeGraphEfficiencyAnswer =
        new Graph(nodeListTreeEfficiencyAnswer, pipeListTreeEfficiencyAnswer);

    /*
     * Instantiates node and pipe objects to input into the basic graph test case
     */
    node1Basic = new Node("Node1", "Inlet", 5000000.0, 0.0, 14.7, 14.7, 0, 0, "success");
    node2Basic = new Node("Node2", "Outlet", 0.0, 0.0, 14.7, 14.7, 0, 0, "success");
    pipe1Basic = new Pipe("Pipe1", 0d, 1d, 4d, 1d, 1d, 0.80, 14.7, 530d, 520d, 0.9, 0.1, 1d, 1d,
        "success", 1d);
    // Node 1 connections
    node1BasicNeighborsList = new ArrayList<Node>();
    node1BasicNeighborsList.add(node2Basic);
    node1Basic.setNeighbors(node1BasicNeighborsList);
    // Node 2 connections
    node2BasicNeighborsList = new ArrayList<Node>();
    node2BasicNeighborsList.add(node1Basic);
    node2Basic.setNeighbors(node2BasicNeighborsList);
    // Pipe 1 end connections
    pipe1Basic.setEnd1(node1Basic);
    pipe1Basic.setEnd2(node2Basic);

    // Instantiates the graph and executes the test
    nodeListBasic = new ArrayList<Node>();
    nodeListBasic.add(node1Basic);
    nodeListBasic.add(node2Basic);

    pipeListBasic = new ArrayList<Pipe>();
    pipeListBasic.add(pipe1Basic);
    graphBasic = new Graph(nodeListBasic, pipeListBasic);
  }

  /*
   * Checks if the pressures and volumes calculated by the solver correctly converge to a known
   * solution.
   */
  @Test
  void testPressureVolumesCalc() {
    FlowCalc flowCalcTree = new FlowCalc();
    GraphMessage treeMessage = flowCalcTree.pressureVolumesCalc(treeGraph);
    Graph calculatedTreeGraph = treeMessage.getGraph();
    assertEquals(true, calculatedTreeGraph.connectionsEquals(treeGraphPressureAnswer));
    assertEquals(true, calculatedTreeGraph.constantsEquals(treeGraphPressureAnswer));

    FlowCalc flowCalcLooped = new FlowCalc();
    GraphMessage loopedMessage = flowCalcLooped.pressureVolumesCalc(loopedGraph);
    Graph calculatedLoopGraph = loopedMessage.getGraph();
    assertEquals(true, calculatedLoopGraph.connectionsEquals(loopedGraphPressureVolumesAnswer));
    assertEquals(true, calculatedLoopGraph.constantsEquals(loopedGraphPressureVolumesAnswer));

    FlowCalc flowCalcBasic = new FlowCalc();
    flowCalcBasic.pressureVolumesCalc(graphBasic);
  }

  /*
   * Checks if the pressures and efficiencies calculated by the solver correctly converge to a known
   * solution. TODO change the graphs
   */
  @Test
  void testPressureEfficiencyCalc() {
    FlowCalc flowCalcTree = new FlowCalc();
    GraphMessage treeMessage = flowCalcTree.pressureEfficiencyCalc(treeGraph);
    Graph calculatedTreeGraph = treeMessage.getGraph();
    assertEquals(true, calculatedTreeGraph.connectionsEquals(treeGraphEfficiencyAnswer));
    assertEquals(true, calculatedTreeGraph.constantsEquals(treeGraphEfficiencyAnswer));

    FlowCalc flowCalcBasic = new FlowCalc();
    flowCalcBasic.pressureEfficiencyCalc(graphBasic);
  }

  /**
   * Tests state variable initial conditions vector generation.
   */
  @Test
  void testGetInitialConditionsVectorPressureVolumeCalc() {
    DMatrixRMaj variableConnectionMatrix = loopedGraph.getVariableConnectionMatrix();
    DMatrixRMaj initialConditionsVector = FlowCalc
        .getInitialConditionsVectorPressureVolumeCalc(loopedGraph, variableConnectionMatrix);
    double[][] testMatrix = {{Math.pow(1000d, 2)}, {Math.pow(600d, 2)}, {Math.pow(700d, 2)},
        {Math.pow(600d, 2)}, {Math.pow(200d, 2)}, {0.0}, {0.0}, {0.0}, {0.0}, {0.0}, {0.0}};
    DMatrixRMaj testInitialConditionsVector = new DMatrixRMaj(testMatrix);
    EjmlUnitTests.assertEquals(testInitialConditionsVector, initialConditionsVector, 1e-6);
  }

  /*
   * Checks if the graph returned is correct for a given vector solution
   */
  @Test
  void testGetGraphFromPressureAndFlowVector() {
    FlowCalc flowCalc = new FlowCalc();
    double[][] testMatrix = {{1.0e+05 * 2.059364186889763}, {1.0e+05 * 1.810013735028653},
        {1.0e+05 * 1.606856187007772}, {1.0e+05 * 1.807649829400943}, {1.0e+05 * 1.817623847475387},
        {1.0e+07 * 0.500000000000000}, {1.0e+07 * -0.451316701949486},
        {1.0e+07 * 0.048683298050514}, {1.0e+07 * -0.448683298050514},
        {1.0e+07 * -0.100000000000000}, {1.0e+07 * 1.100000000000000}};
    DMatrixRMaj functionVector = new DMatrixRMaj(testMatrix);
    Graph resultsGraph = flowCalc.getGraphFromPressureAndFlowVector(functionVector, loopedGraph);
    assertEquals(true, resultsGraph.constantsEquals(loopedGraphPressureVolumesAnswer));
    assertEquals(true, resultsGraph.connectionsEquals(loopedGraphPressureVolumesAnswer));
  }

  /*
   * Checks if the graph returned is correct for a given vector solution
   */
  @Test
  void testGetGraphFromPressureAndEfficiencyVector() {
    FlowCalc flowCalc = new FlowCalc();
    double[][] testMatrix = {{1.0e+05 * 1.672107365131640}, {1.0e+05 * 1.419112314939369},
        {1.0e+05 * 1.638993310747928}, {1.0e+05 * 1.417594514952072}, {1.0e+05 * 1.393584907620677},
        {0.992770966113299}, {0.955280517515971}, {1.319464430055246}, {0.958248475237316},
        {0.644529124569670}, {0.986945746727844}, {0.992770966113299}, {1.122739531178760},
        {1.000846732170868}, {0.644529124569670}};
    DMatrixRMaj functionVector = new DMatrixRMaj(testMatrix);
    Graph efficiencyResultsGraph = flowCalc.getGraphFromPressureAndEfficiencyVector(functionVector,
        loopedGraphPressureVolumesAnswer);
    assertEquals(true, efficiencyResultsGraph.connectionsEquals(loopedGraphEfficiencyAnswer));
    assertEquals(true, efficiencyResultsGraph.constantsEquals(loopedGraphEfficiencyAnswer));
  }

  /*
   * Checks if the results of all the portions of the efficiency functions are properly concatenated
   */
  @Test
  void testVectorConcatenation() {
    ArrayList<DMatrixRMaj> vectors = new ArrayList<DMatrixRMaj>();
    double[][] energyFunctionsSumTestValues = {{1d}, {2d}, {3d}, {4d}, {5d}, {6d}};
    DMatrixRMaj energyFunctionsSumTest = new DMatrixRMaj(energyFunctionsSumTestValues);
    double[][] massFunctionsSumTestValues = {{7d}, {8d}, {9d}, {10d}, {11d}};
    DMatrixRMaj massFunctionsSumTest = new DMatrixRMaj(massFunctionsSumTestValues);
    double[][] pressureErrorFunctionsSumTestValues = {{12d}, {13d}, {14d}, {15d}};
    DMatrixRMaj pressureErrorFunctionsSumTest =
        new DMatrixRMaj(pressureErrorFunctionsSumTestValues);
    double[][] finalVectorTestValues = {{1d}, {2d}, {3d}, {4d}, {5d}, {6d}, {7d}, {8d}, {9d}, {10d},
        {11d}, {12d}, {13d}, {14d}, {15d}};
    DMatrixRMaj finalVectorTest = new DMatrixRMaj(finalVectorTestValues);
    new DMatrixRMaj(pressureErrorFunctionsSumTestValues);
    vectors.add(energyFunctionsSumTest);
    vectors.add(massFunctionsSumTest);
    vectors.add(pressureErrorFunctionsSumTest);
    DMatrixRMaj finalVector = FlowCalc.vectorConcatenation(vectors);
    EjmlUnitTests.assertEquals(finalVector, finalVectorTest, 1e-06);
  }

  /*
   * Checks if the matrix returned from the mass function summation of the efficiency calculation is
   * correct for a given solution
   */
  @Test
  void testGetNodeTypeListIndices() {
    ArrayList<Integer> outletIndicies = new ArrayList<>();
    ArrayList<Integer> inletIndicies = new ArrayList<>();
    ArrayList<Integer> outletIndiciesTest = new ArrayList<>();
    outletIndiciesTest.add(null);
    outletIndiciesTest.add(null);
    outletIndiciesTest.add(null);
    outletIndiciesTest.add(3);
    outletIndiciesTest.add(null);
    outletIndiciesTest.add(null);
    ArrayList<Integer> inletIndiciesTest = new ArrayList<>();
    inletIndiciesTest.add(0);
    inletIndiciesTest.add(null);
    inletIndiciesTest.add(2);
    inletIndiciesTest.add(null);
    inletIndiciesTest.add(4);
    inletIndiciesTest.add(5);
    outletIndicies = FlowCalc.getNodeTypeListIndices("Outlet", nodeList);
    inletIndicies = FlowCalc.getNodeTypeListIndices("Inlet", nodeList);
    assertEquals(outletIndicies, outletIndiciesTest);
    assertEquals(inletIndicies, inletIndiciesTest);
  }
}
