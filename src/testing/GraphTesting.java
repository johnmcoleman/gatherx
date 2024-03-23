package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.ejml.EjmlUnitTests;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.misc.TransposeAlgs_DDRM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Graph;
import model.Node;
import model.Pipe; //

public class GraphTesting {

  private static Graph emptyGraph = new Graph();
  private static Node defaultNode = new Node();
  private static Node basicNode = new Node();

  private static Node node0;
  private static Node node1;
  private static Node node2;
  private static Node node3;
  private static Node node4;
  private static Node node0Copy;
  private static Node node1Copy;
  private static Node node2Copy;
  private static Node node3Copy;
  private static Node node4Copy;

  private static Node outlet0;
  private static Node outlet1;
  private static Node outlet2;
  private static Node outlet0Copy;

  private static Pipe pipe0;
  private static Pipe pipe1;
  private static Pipe pipe2;
  private static Pipe pipe3;
  private static Pipe pipe4;
  private static Pipe pipe5;
  private static Pipe pipe6;
  private static Pipe pipe7;
  private static Pipe pipe0Copy;

  private static Graph basicGraph, basicGraphCopy, disjointGraph, oneTGraph, uselessTGraph,
      multiOutletGraph, noOutletGraph, loopedGraph, noOutletLoopedGraph;

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

  @BeforeEach
  void setup() {
    /*
     * Looped graph case
     */
    node1LoopedEfficiency =
        new Node("Node1", "Inlet", 5000000.0, 0.0, Math.sqrt(1.655551846664990 * 1e+05 * 1.01),
            Math.sqrt(1.655551846664990 * 1e+05 * 1.01), 0, 0, "success");
    node2LoopedEfficiency = new Node("Node2", "Tee", 0.0, 0.0, 700.0, 700.0, 0, 0, "success");
    node3LoopedEfficiency =
        new Node("Node3", "Inlet", 2000000.0, 0.0, Math.sqrt(1.606856187007772 * 1e+05 * 1.02),
            Math.sqrt(1.606856187007772 * 1e+05 * 1.02), 0, 0, "success");
    node4LoopedEfficiency = new Node("Node4", "Outlet", 0.0, 0.0, 200.0, 200.0, 0, 0, "success");
    node5LoopedEfficiency =
        new Node("Node5", "Inlet", 3000000.0, 0.0, Math.sqrt(1.403558925695121 * 1e+05 * 1.01),
            Math.sqrt(1.403558925695121 * 1e+05 * 1.01), 0, 0, "success");
    node6LoopedEfficiency =
        new Node("Node6", "Inlet", 1000000.0, 0.0, Math.sqrt(0.995584907620677 * 1e+05),
            Math.sqrt(1.393584907620677 * 1e+05), 0, 0, "success");
    pipe1LoopedEfficiency = new Pipe("Pipe1", 1.0e+07 * 0.500000000000000, 1d, 4d, 1d, 1d, 0.80,
        14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
    pipe2LoopedEfficiency = new Pipe("Pipe2", 1.0e+07 * -0.448528137423857, 1d, 4d, 1d, 1d, 0.80,
        14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
    pipe3LoopedEfficiency = new Pipe("Pipe3", 1.0e+07 * 0.051471862576143, 1d, 4d, 1d, 1d, 0.80,
        14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
    pipe4LoopedEfficiency = new Pipe("Pipe4", 1.0e+07 * -0.451471862576143, 1d, 4d, 1d, 1d, 0.80,
        14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
    pipe5LoopedEfficiency = new Pipe("Pipe5", 1.0e+07 * -0.100000000000000, 1d, 4d, 1d, 1d, 0.80,
        14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
    pipe6LoopedEfficiency = new Pipe("Pipe6", 1.0e+07 * 1.100000000000000, 1d, 4d, 1d, 1d, 0.80,
        14.7, 530d, 520d, 0.9, 0.1, 1d, 1d, "success", 1d);
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

    List<Node> defaultNeigh = new ArrayList<>();
    defaultNeigh.add(defaultNode);
    basicNode.setNeighbors(defaultNeigh);

    /*
     * Tree graph case
     */
    node1Tree = new Node("Node1", "Outlet", 0, 0.0, 100.0, 100.0, 0, 0, "success");
    node2Tree = new Node("Node2", "Inlet", 2e6, 0.0, 200.0, 200.0, 0, 0, "success");
    node3Tree = new Node("Node3", "Inlet", 1e6, 0.0, 250.0, 250.0, 0, 0, "success");
    node4Tree = new Node("Node4", "Tee", 0.0, 0.0, 14.7, 14.7, 0, 0, "success");
    node5Tree = new Node("Node5", "Inlet", 1e6, 0.0, 300.0, 300.0, 0, 0, "success");
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


    // Initializes nodes and pipes
    node0 = new Node("Node 0", "Inlet", 1.893, 45, 889.6, 905.6, 1, 18, "neutral");
    node1 = new Node("Node 1", "Tee", 1.893, 45, 889.6, 905.6, 2, 14, "neutral");
    node2 = new Node("Node 2", "Inlet", 1.893, 45, 889.6, 905.6, -8, 10, "neutral");
    node3 = new Node("Node 3", "Inlet", 1.893, 45, 889.6, 905.6, 6, 12, "neutral");
    node4 = new Node("Node 4", "Tee", 1.893, 45, 889.6, 905.6, 5, -9, "neutral");
    node0Copy = new Node("Node 0", "Inlet", 1.893, 45, 889.6, 905.6, 1, 18, "neutral");
    node1Copy = new Node("Node 1", "Tee", 1.893, 45, 889.6, 905.6, 2, 14, "neutral");
    node2Copy = new Node("Node 2", "Inlet", 1.893, 45, 889.6, 905.6, -8, 10, "neutral");
    node3Copy = new Node("Node 3", "Inlet", 1.893, 45, 889.6, 905.6, 6, 12, "neutral");
    node4Copy = new Node("Node 4", "Tee", 1.893, 45, 889.6, 905.6, 5, -9, "neutral");
    outlet0 = new Node("Outlet 0", "Outlet", 1.893, 45, 889.6, 905.6, -8, 10, "neutral");
    outlet1 = new Node("Outlet 1", "Outlet", 1.893, 45, 889.6, 905.6, 6, 12, "neutral");
    outlet2 = new Node("Outlet 2", "Outlet", 1.893, 45, 889.6, 905.6, 5, -9, "neutral");
    outlet0Copy = new Node("Outlet 0", "Outlet", 1.893, 45, 889.6, 905.6, -8, 10, "neutral");
    pipe0 = new Pipe();
    pipe1 = new Pipe();
    pipe2 = new Pipe();
    pipe3 = new Pipe();
    pipe4 = new Pipe();
    pipe5 = new Pipe();
    pipe6 = new Pipe();
    pipe7 = new Pipe();
    pipe0Copy = new Pipe();
    // Sets up Pipes
    {
      pipe0.setEnd1(node0);
      pipe0.setEnd2(outlet0);

      pipe1.setEnd1(node1);
      pipe1.setEnd2(outlet1);

      pipe2.setEnd1(node1);
      pipe2.setEnd2(node2);

      pipe3.setEnd1(node1);
      pipe3.setEnd2(node3);

      pipe4.setEnd1(outlet1);
      pipe4.setEnd2(node2);

      pipe5.setEnd1(node2);
      pipe5.setEnd2(node3);

      pipe6.setEnd1(node2);
      pipe6.setEnd2(node4);

      pipe7.setEnd1(node2);
      pipe7.setEnd2(outlet2);

      pipe0Copy.setEnd1(node0Copy);
      pipe0Copy.setEnd2(outlet0Copy);
    }

    // Sets up the basicGraph
    {
      List<Pipe> basicPipes = new ArrayList<>();
      basicPipes.add(pipe0);
      List<Node> basicNeigh0 = new ArrayList<>();
      basicNeigh0.add(outlet0);
      List<Node> basicNeigh1 = new ArrayList<>();
      basicNeigh1.add(node0);
      node0.setNeighbors(basicNeigh0);
      outlet0.setNeighbors(basicNeigh1);
      List<Node> basicNodes = new ArrayList<>();
      basicNodes.add(node0);
      basicNodes.add(outlet0);
      basicGraph = new Graph(basicNodes, basicPipes);
    }

    // Sets up a deep copy of the basicGraph
    {
      List<Pipe> basicPipesCopy = new ArrayList<>();
      basicPipesCopy.add(pipe0Copy);
      List<Node> basicNeigh0Copy = new ArrayList<>();
      basicNeigh0Copy.add(outlet0Copy);
      List<Node> basicNeigh1Copy = new ArrayList<>();
      basicNeigh1Copy.add(node0Copy);
      node0Copy.setNeighbors(basicNeigh0Copy);
      outlet0Copy.setNeighbors(basicNeigh1Copy);
      List<Node> basicNodesCopy = new ArrayList<>();
      basicNodesCopy.add(node0Copy);
      basicNodesCopy.add(outlet0Copy);
      basicGraphCopy = new Graph(basicNodesCopy, basicPipesCopy);
    }

    // Sets up the disjointGraph
    {
      List<Pipe> disjointPipes = new ArrayList<>();
      disjointPipes.add(pipe0);
      List<Node> disjointNodes = new ArrayList<>();
      disjointNodes.add(node0);
      disjointNodes.add(node1);
      disjointNodes.add(outlet0);
      node1.setNeighbors(Collections.emptyList());
      disjointGraph = new Graph(disjointNodes, disjointPipes);
    }

    // Sets up the oneTGraph
    {
      List<Pipe> oneTPipes = new ArrayList<>();
      oneTPipes.add(pipe1);
      oneTPipes.add(pipe3);
      List<Node> neigh0 = new ArrayList<>();
      neigh0.add(node1);
      List<Node> neigh1 = new ArrayList<>();
      neigh1.add(node3);
      neigh1.add(outlet1);

      node3.setNeighbors(neigh0);
      node1.setNeighbors(neigh1);
      outlet1.setNeighbors(neigh0);
      List<Node> oneTNodes = new ArrayList<>();
      oneTNodes.add(node1);
      oneTNodes.add(node3);
      oneTNodes.add(outlet1);
      oneTGraph = new Graph(oneTNodes, oneTPipes);
    }

    // Sets up the uselessTGraph
    {
      List<Pipe> uselessTPipes = new ArrayList<>();
      uselessTPipes.add(pipe4);
      uselessTPipes.add(pipe6);
      List<Node> neigh0 = new ArrayList<>();
      neigh0.add(node2);
      List<Node> neigh1 = new ArrayList<>();
      neigh1.add(node4);
      neigh1.add(outlet1);

      node4.setNeighbors(neigh0);
      node2.setNeighbors(neigh1);
      outlet1.setNeighbors(neigh0);
      List<Node> uselessTNodes = new ArrayList<>();
      uselessTNodes.add(node4);
      uselessTNodes.add(node2);
      uselessTNodes.add(outlet1);
      uselessTGraph = new Graph(uselessTNodes, uselessTPipes);
    }

    // Sets up the multiOutletGraph
    {
      List<Pipe> multiOutletPipes = new ArrayList<>();
      multiOutletPipes.add(pipe4);
      multiOutletPipes.add(pipe7);
      List<Node> neigh0 = new ArrayList<>();
      neigh0.add(node2);
      List<Node> neigh1 = new ArrayList<>();
      neigh1.add(outlet2);
      neigh1.add(outlet1);

      outlet2.setNeighbors(neigh0);
      node2.setNeighbors(neigh1);
      outlet1.setNeighbors(neigh0);
      List<Node> multiOutletNodes = new ArrayList<>();
      multiOutletNodes.add(node2);
      multiOutletNodes.add(outlet2);
      multiOutletNodes.add(outlet1);
      multiOutletGraph = new Graph(multiOutletNodes, multiOutletPipes);
    }

    // Sets up the noOutletGraph
    {
      List<Pipe> noOutletPipes = new ArrayList<>();
      noOutletPipes.add(pipe3);
      noOutletPipes.add(pipe5);
      noOutletPipes.add(pipe6);
      List<Node> neigh0 = new ArrayList<>();
      neigh0.add(node3);
      List<Node> neigh1 = new ArrayList<>();
      neigh1.add(node2);
      neigh1.add(node1);
      List<Node> neigh2 = new ArrayList<>();
      neigh2.add(node4);
      neigh2.add(node3);
      List<Node> neigh3 = new ArrayList<>();
      neigh3.add(node2);

      node1.setNeighbors(neigh0);
      node2.setNeighbors(neigh2);
      node3.setNeighbors(neigh1);
      node4.setNeighbors(neigh3);
      List<Node> noOutletNodes = new ArrayList<>();
      noOutletNodes.add(node1);
      noOutletNodes.add(node2);
      noOutletNodes.add(node3);
      noOutletNodes.add(node4);
      noOutletGraph = new Graph(noOutletNodes, noOutletPipes);
    }

    // Sets up the loopedGraph
    {
      List<Pipe> loopedPipes = new ArrayList<>();
      loopedPipes.add(pipe1);
      loopedPipes.add(pipe2);
      loopedPipes.add(pipe4);
      List<Node> neigh0 = new ArrayList<>();
      neigh0.add(node2);
      neigh0.add(outlet1);
      List<Node> neigh1 = new ArrayList<>();
      neigh1.add(node2);
      neigh1.add(node1);
      List<Node> neigh2 = new ArrayList<>();
      neigh2.add(outlet1);
      neigh2.add(node1);

      node1.setNeighbors(neigh0);
      outlet1.setNeighbors(neigh1);
      node2.setNeighbors(neigh2);
      List<Node> loopedNodes = new ArrayList<>();
      loopedNodes.add(node2);
      loopedNodes.add(node1);
      loopedNodes.add(outlet1);
      loopedGraph = new Graph(loopedNodes, loopedPipes);
    }

    // Sets up the noOutletLoopedGraph
    {
      List<Pipe> noOutletLoopedPipes = new ArrayList<>();
      noOutletLoopedPipes.add(pipe3);
      noOutletLoopedPipes.add(pipe5);
      noOutletLoopedPipes.add(pipe6);
      noOutletLoopedPipes.add(pipe2);
      List<Node> neigh0 = new ArrayList<>();
      neigh0.add(node3);
      neigh0.add(node2);
      List<Node> neigh1 = new ArrayList<>();
      neigh1.add(node2);
      neigh1.add(node1);
      List<Node> neigh2 = new ArrayList<>();
      neigh2.add(node4);
      neigh2.add(node3);
      neigh2.add(node1);
      List<Node> neigh3 = new ArrayList<>();
      neigh3.add(node2);

      node1.setNeighbors(neigh0);
      node2.setNeighbors(neigh2);
      node3.setNeighbors(neigh1);
      node4.setNeighbors(neigh3);
      List<Node> noOutletLoopedNodes = new ArrayList<>();
      noOutletLoopedNodes.add(node1);
      noOutletLoopedNodes.add(node2);
      noOutletLoopedNodes.add(node3);
      noOutletLoopedNodes.add(node4);
      noOutletLoopedGraph = new Graph(noOutletLoopedNodes, noOutletLoopedPipes);
    }
  }

  /**
   * Tests that toString() works
   */
  @Test
  void testToString() {
    assertEquals(emptyGraph.toString(), "Empty Graph");

    assertEquals(basicGraph.toString(), "Nodes:\n" + node0.toString() + "\n" + outlet0.toString()
        + "\n" + "Pipes:\n" + pipe0.toString() + "\n");

    assertEquals(disjointGraph.toString(), "Nodes:\n" + node0.toString() + "\n" + node1.toString()
        + "\n" + outlet0.toString() + "\n" + "Pipes:\n" + pipe0.toString() + "\n");

    assertEquals(oneTGraph.toString(),
        "Nodes:\n" + node1.toString() + "\n" + node3.toString() + "\n" + outlet1.toString() + "\n"
            + "Pipes:\n" + pipe1.toString() + "\n" + pipe3.toString() + "\n");

    assertEquals(uselessTGraph.toString(),
        "Nodes:\n" + node4.toString() + "\n" + node2.toString() + "\n" + outlet1.toString() + "\n"
            + "Pipes:\n" + pipe4.toString() + "\n" + pipe6.toString() + "\n");

    assertEquals(uselessTGraph.toString(),
        "Nodes:\n" + node4.toString() + "\n" + node2.toString() + "\n" + outlet1.toString() + "\n"
            + "Pipes:\n" + pipe4.toString() + "\n" + pipe6.toString() + "\n");

    assertEquals(multiOutletGraph.toString(),
        "Nodes:\n" + node2.toString() + "\n" + outlet2.toString() + "\n" + outlet1.toString() + "\n"
            + "Pipes:\n" + pipe4.toString() + "\n" + pipe7.toString() + "\n");

    assertEquals(noOutletGraph.toString(),
        "Nodes:\n" + node1.toString() + "\n" + node2.toString() + "\n" + node3.toString() + "\n"
            + node4.toString() + "\n" + "Pipes:\n" + pipe3.toString() + "\n" + pipe5.toString()
            + "\n" + pipe6.toString() + "\n");

    assertEquals(loopedGraph.toString(),
        "Nodes:\n" + node2.toString() + "\n" + node1.toString() + "\n" + outlet1.toString() + "\n"
            + "Pipes:\n" + pipe1.toString() + "\n" + pipe2.toString() + "\n" + pipe4.toString()
            + "\n");

    assertEquals(noOutletLoopedGraph.toString(),
        "Nodes:\n" + node1.toString() + "\n" + node2.toString() + "\n" + node3.toString() + "\n"
            + node4.toString() + "\n" + "Pipes:\n" + pipe3.toString() + "\n" + pipe5.toString()
            + "\n" + pipe6.toString() + "\n" + pipe2.toString() + "\n");
  }

  /**
   * Tests that getNeighbors() works
   */
  @Test
  void testGetNeighbors() {
    assertNull(emptyGraph.getNeighbors(null));
    assertNull(emptyGraph.getNeighbors(defaultNode));
    assertNull(emptyGraph.getNeighbors(basicNode));

    List<Node> neigh0 = new ArrayList<>();
    neigh0.add(outlet0);
    assertEquals(disjointGraph.getNeighbors(node0), neigh0);
    assertNull(disjointGraph.getNeighbors(node4));

    List<Node> neigh4 = new ArrayList<>();
    neigh4.add(node2);
    assertEquals(uselessTGraph.getNeighbors(node4), neigh4);
    assertEquals(multiOutletGraph.getNeighbors(outlet2), neigh4);
  }

  /**
   * Tests that getEndNodes() works
   */
  @Test
  void testGetEndNodes() {
    assertEquals(emptyGraph.getEndNodes(), Collections.emptyList());
  }

  /**
   * Tests that getPipe() works
   */
  @Test
  void testGetPipe() {
    assertNull(emptyGraph.getPipe(basicNode, defaultNode));
    assertNull(emptyGraph.getPipe("default"));
  }

  /**
   * Tests that getNode() works
   */
  @Test
  void testGetNode() {
    assertNull(emptyGraph.getNode("default"));
  }

  /**
   * Tests that isDisjoint() works
   */
  @Test
  void testIsDisjoint() {
    assertFalse(emptyGraph.isDisjoint());
    assertFalse(loopedGraphEfficiency.isDisjoint());

    assertTrue(disjointGraph.isDisjoint());
  }

  /**
   * Tests that containsLoop() works
   */
  @Test
  void testContainsLoop() {
    assertFalse(emptyGraph.containsLoop(null));
    assertFalse(emptyGraph.containsLoop(defaultNode));
  }

  /**
   * Tests that getCenterNode() works
   */
  @Test
  void testGetCenterNode() {
    assertNull(emptyGraph.getCenterNode());
  }

  /**
   * Tests that getNodeNames() works
   */
  @Test
  void testGetNodeNames() {
    assertEquals(emptyGraph.getNodeNames(), Collections.emptyList());
  }

  /**
   * Tests that getPipeNames() works
   */
  @Test
  void testGetPipeNames() {
    assertEquals(emptyGraph.getPipeNames(), Collections.emptyList());
  }

  /*
   * Tests that a shallow copy is recognized as carrying the same simple data type fields with the
   * same values
   */
  @Test
  void testConstantEquals() {
    assertEquals(true, basicGraph.constantsEquals(basicGraphCopy));
    assertEquals(false, basicGraph.constantsEquals(loopedGraph));
  }

  /*
   * Tests that a shallow copy is recognized as carrying the connections between the nodes and pipes
   * as determined by their names which are assumed to be unique
   */
  @Test
  void testConnectionsEquals() {
    assertEquals(true, basicGraph.connectionsEquals(basicGraphCopy));
    assertEquals(false, basicGraph.connectionsEquals(loopedGraph));
    assertEquals(false, basicGraph.connectionsEquals(disjointGraph));
    assertEquals(false, multiOutletGraph.connectionsEquals(disjointGraph));
  }

  /*
   * Tests that the creation of a deep copy works.
   */
  @Test
  void testGetDeepClone() {
    Graph basicGraphClone = basicGraph.getDeepClone();
    assertEquals(true, basicGraph.connectionsEquals(basicGraphClone));
    assertEquals(true, basicGraph.constantsEquals(basicGraphClone));

    Graph loopedGraphEfficiencyClone = loopedGraphEfficiency.getDeepClone();
    assertEquals(true, loopedGraphEfficiency.connectionsEquals(loopedGraphEfficiencyClone));
    assertEquals(true, loopedGraphEfficiency.constantsEquals(loopedGraphEfficiencyClone));
  }

  /**
   * Tests that constant connection matrix construction works properly.
   */
  @Test
  void testGetConstantConnectionMatrix() {
    DMatrixRMaj loopedConstantConnectionMatrix =
        loopedGraphEfficiency.getConstantConnectionMatrix();
    double[][] loopedTestMatrix = {{0d, 0d, 0d, 0d, 0d, -1d}};
    DMatrixRMaj loopedTestConstantConnectionMatrix = new DMatrixRMaj(loopedTestMatrix);
    EjmlUnitTests.assertEquals(loopedTestConstantConnectionMatrix, loopedConstantConnectionMatrix,
        1e-6);

    DMatrixRMaj treeConstantConnectionMatrix = treeGraph.getConstantConnectionMatrix();
    double[][] treeTestMatrix = {{0d, 0d, 0d, 1d}};
    DMatrixRMaj treeTestConstantConnectionMatrix = new DMatrixRMaj(treeTestMatrix);
    EjmlUnitTests.assertEquals(treeTestConstantConnectionMatrix, treeConstantConnectionMatrix,
        1e-6);
  }

  /**
   * Tests state variable connection matrix construction.
   */
  @Test
  void testGetVariableConnectionMatrix() {
    DMatrixRMaj loopedVariableConnectionMatrix =
        loopedGraphEfficiency.getVariableConnectionMatrix();
    double[][] loopedTestMatrix = {{1d, 0d, 0d, 0d, 0d, 0d}, {-1d, -1d, 1d, 0d, 0d, 0d},
        {0d, 1d, 0d, 1d, 0d, 1d}, {0d, 0d, -1d, -1d, 1d, 0d}, {0d, 0d, 0d, 0d, -1d, 0d}};
    DMatrixRMaj loopedTestVariableConnectionMatrix = new DMatrixRMaj(loopedTestMatrix);
    EjmlUnitTests.assertEquals(loopedTestVariableConnectionMatrix, loopedVariableConnectionMatrix,
        1e-6);

    DMatrixRMaj treeVariableConnectionMatrix = treeGraph.getVariableConnectionMatrix();
    double[][] treeTestMatrix =
        {{0d, 1d, 1d, -1d}, {0d, 0d, -1d, 0d}, {-1d, -1d, 0d, 0d}, {1d, 0d, 0d, 0d}};
    DMatrixRMaj treeTestVariableConnectionMatrix = new DMatrixRMaj(treeTestMatrix);
    EjmlUnitTests.assertEquals(treeTestVariableConnectionMatrix, treeVariableConnectionMatrix,
        1e-6);
  }

  /**
   * Tests that constant connection matrix construction works properly.
   */
  @Test
  void testGetConstantConnectionMatrixTransposed() {
    DMatrixRMaj constantConnectionMatrixTransposed =
        loopedGraphEfficiency.getConstantConnectionMatrixTransposed();
    double[][] testMatrix = {{0d, 0d, 0d, 0d, 0d, -1d}};
    DMatrixRMaj testConstantConnectionMatrix = new DMatrixRMaj(testMatrix);
    DMatrixRMaj testConstantConnectionMatrixTransposed = new DMatrixRMaj(
        testConstantConnectionMatrix.getNumCols(), testConstantConnectionMatrix.getNumRows());
    TransposeAlgs_DDRM.standard(testConstantConnectionMatrix,
        testConstantConnectionMatrixTransposed);
    EjmlUnitTests.assertEquals(testConstantConnectionMatrixTransposed,
        constantConnectionMatrixTransposed, 1e-6);
  }

  /**
   * Tests state variable connection matrix construction.
   */
  @Test
  void testGetVariableConnectionMatrixTransposed() {
    DMatrixRMaj variableConnectionMatrixTransposed =
        loopedGraphEfficiency.getVariableConnectionMatrixTransposed();
    double[][] testMatrix = {{1d, 0d, 0d, 0d, 0d, 0d}, {-1d, -1d, 1d, 0d, 0d, 0d},
        {0d, 1d, 0d, 1d, 0d, 1d}, {0d, 0d, -1d, -1d, 1d, 0d}, {0d, 0d, 0d, 0d, -1d, 0d}};
    DMatrixRMaj testVariableConnectionMatrix = new DMatrixRMaj(testMatrix);
    DMatrixRMaj testConstantConnectionMatrixTransposed = new DMatrixRMaj(
        testVariableConnectionMatrix.getNumCols(), testVariableConnectionMatrix.getNumRows());
    TransposeAlgs_DDRM.standard(testVariableConnectionMatrix,
        testConstantConnectionMatrixTransposed);
    EjmlUnitTests.assertEquals(testConstantConnectionMatrixTransposed,
        variableConnectionMatrixTransposed, 1e-6);
  }
}
