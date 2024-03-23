package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.misc.TransposeAlgs_DDRM;

/**
 * The Graph class contains a list of nodes and pipes which correspond to nodes and edges in
 * mathematical graphs respectively. Nodes and pipes can be identified by their unique names, and
 * their physical connections are found within the node and pipe objects themselves.
 * 
 * @author John Coleman
 * @author Nick Chen
 * @author Corey Tolbert
 *
 */
public class Graph implements Serializable {
  private static final long serialVersionUID = -7680407406672046728L;
  private String timestamp;
  private List<Node> nodes;
  private List<Pipe> pipes;

  /**
   * Constructs a default graph object
   */
  public Graph() {
    nodes = new ArrayList<>();
    pipes = new ArrayList<>();
  }

  /**
   * TODO
   */
  public Graph clone() throws CloneNotSupportedException {
    Graph g;
    try {
      g = (Graph) super.clone();

    } catch (CloneNotSupportedException e) {
      g = new Graph(); // may need to write in better catch
    }

    g.nodes = new ArrayList<>(this.nodes);
    g.pipes = new ArrayList<>(this.pipes);

    return g;
  }

  /**
   * Constructs a graph object based on information from the controller
   * 
   * @param nodes a list of nodes in the graph
   * @param pipes a list of pipes in the graph
   */
  public Graph(List<Node> nodes, List<Pipe> pipes) {
    this.nodes = nodes;
    this.pipes = pipes;
  }

  @Override
  public String toString() {
    if (nodes.size() + pipes.size() == 0) {
      return "Empty Graph";
    }
    StringBuilder sb = new StringBuilder();
    sb.append("Nodes:\n");
    for (Node n : nodes) {
      sb.append(n.toString());
      sb.append("\n");
    }
    sb.append("Pipes:\n");
    for (Pipe p : pipes) {
      sb.append(p.toString());
      sb.append("\n");
    }
    return sb.toString();
  }

  /*
   * Checks if the shallow values of two graph objects are equivalent to one another.
   * 
   * @param graph another instance of the graph class to which the current instance will be
   * compared.
   * 
   * @return whether or not all constants in the two graphs match
   */
  public boolean constantsEquals(Graph graph) {
    boolean equal = true;
    List<Node> comparisonNodes = graph.getNodes();
    List<Pipe> comparisonPipes = graph.getPipes();
    if (graph.getNumNodes() != this.getNumNodes() || graph.getNumPipes() != this.getNumPipes()) {
      equal = false;
    } else {
      for (int index = 0; index < graph.getNumNodes(); index++) {
        Node comparisonNode = comparisonNodes.get(index);
        if (!comparisonNode.constantsEquals(this.nodes.get(index), 1e-02)) {
          equal = false;
        }
      }
      for (int index = 0; index < graph.getNumPipes(); index++) {
        Pipe comparisonPipe = comparisonPipes.get(index);
        if (!comparisonPipe.constantsEquals(this.pipes.get(index), 1e-02)) {
          equal = false;
        }
      }
    }
    return equal;
  }

  /*
   * Checks if the names of the connected nodes and pipes of this graph object match with the
   * shallow values of another instance of the graph class.
   * 
   * @param graph another instance of the graph class to which the current instance will be
   * compared.
   * 
   * @return whether or not all connections in the two graphs match
   */
  public boolean connectionsEquals(Graph graph) {
    boolean equal = true;
    List<Node> comparisonNodes = graph.getNodes();
    List<Pipe> comparisonPipes = graph.getPipes();
    if (graph.getNumNodes() != this.getNumNodes() || graph.getNumPipes() != this.getNumPipes()) {
      equal = false;
    } else {
      for (int index = 0; index < graph.getNumNodes(); index++) {
        Node comparisonNode = comparisonNodes.get(index);
        Node currentNode = this.nodes.get(index);
        List<Node> comparisonNeighbors = comparisonNode.getNeighbors();
        List<Node> currentNodeNeighbors = currentNode.getNeighbors();
        if (comparisonNeighbors.size() != currentNodeNeighbors.size()) {
          equal = false;
        } else {
          for (int neighborIndex = 0; index < comparisonNeighbors.size(); index++) {
            Node comparisonNeighbor = comparisonNeighbors.get(neighborIndex);
            Node currentNodeNeighbor = currentNodeNeighbors.get(neighborIndex);
            String comparisonNeighborName = comparisonNeighbor.getNodeName();
            String currentNodeNeighborName = currentNodeNeighbor.getNodeName();
            if (!comparisonNeighborName.equals(currentNodeNeighborName)) {
              equal = false;
            }
          }
        }
      }
      for (int index = 0; index < graph.getNumPipes(); index++) {
        Pipe comparisonPipe = comparisonPipes.get(index);
        Pipe currentPipe = this.pipes.get(index);
        String comparisonPipeEndNodeName1 = comparisonPipe.getEnd1().getNodeName();
        String comparisonPipeEndNodeName2 = comparisonPipe.getEnd2().getNodeName();
        String currentPipeEndNodeName1 = currentPipe.getEnd1().getNodeName();
        String currentPipeEndNodeName2 = currentPipe.getEnd2().getNodeName();
        if (!comparisonPipeEndNodeName1.equals(currentPipeEndNodeName1)) {
          equal = false;
        }
        if (!comparisonPipeEndNodeName2.equals(currentPipeEndNodeName2)) {
          equal = false;
        }
      }
    }
    return equal;
  }

  public Graph getDeepClone() {
    int numNodes = this.getNumNodes();
    int numPipes = this.getNumPipes();
    ArrayList<Node> newNodeList = new ArrayList<Node>();
    ArrayList<Pipe> newPipeList = new ArrayList<Pipe>();
    ArrayList<Node> oldNodeList = new ArrayList<Node>(this.getNodes());
    ArrayList<Pipe> oldPipeList = new ArrayList<Pipe>(this.getPipes());
    ArrayList<String> oldNodeListNames = new ArrayList<String>(this.getNodeNames());
    ArrayList<String> oldPipeListNames = new ArrayList<String>(this.getPipeNames());

    // Transfers constants of all nodes
    for (int index = 0; index < numNodes; index++) {
      Node newNode = new Node();
      Node oldNode = oldNodeList.get(index);
      newNode.setAllConstants(oldNode);
      newNodeList.add(newNode);
    }
    // Transfers constants of all pipes
    for (int index = 0; index < numPipes; index++) {
      Pipe newPipe = new Pipe();
      Pipe oldPipe = oldPipeList.get(index);
      newPipe.setAllConstants(oldPipe);
      newPipeList.add(newPipe);
    }
    // Duplicates node connections
    for (int nodeIndex = 0; nodeIndex < numNodes; nodeIndex++) {
      List<Node> oldNeighbors = oldNodeList.get(nodeIndex).getNeighbors();
      if (!oldNeighbors.isEmpty()) {
        ArrayList<String> oldNeighborsNames = new ArrayList<String>();
        for (int neighborIndex = 0; neighborIndex < oldNeighbors.size(); neighborIndex++) {
          oldNeighborsNames.add(oldNeighbors.get(neighborIndex).getNodeName());
        }
        for (String name : oldNeighborsNames) {
          int oldIndex = oldNodeListNames.indexOf(name);
          newNodeList.get(nodeIndex).addNeighbor(newNodeList.get(oldIndex));
        }
      }
    }
    // Duplicates pipe to end node connections
    for (int index = 0; index < numPipes; index++) {
      Pipe oldPipe = oldPipeList.get(index);
      Node oldEndNode1 = oldPipe.getEnd1();
      Node oldEndNode2 = oldPipe.getEnd2();
      int newEnd1Index = oldNodeList.indexOf(oldEndNode1);
      int newEnd2Index = oldNodeList.indexOf(oldEndNode2);
      newPipeList.get(index).setEnd1(newNodeList.get(newEnd1Index));
      newPipeList.get(index).setEnd2(newNodeList.get(newEnd2Index));
    }
    Graph shallowClone = new Graph(newNodeList, newPipeList);
    return shallowClone;
  }

  /**
   * Creates the A1 incidence matrix of the graph (nodes are rows w/outlets or inlets removed from
   * A, pipes are columns). Contains 0 for no connection between a pipe and a node, 1 for flow into
   * a node and negative 1 for flow out of a node.
   * 
   * @return the state variable incidence matrix of the graph
   */
  public DMatrixRMaj getVariableConnectionMatrix() {
    int numMassFunctions = this.getNumNodes() - this.getNumOutletNodes();
    int numEnergyFunctions = this.getNumPipes();
    List<Pipe> pipeList = this.getPipes();
    List<Node> nodeList = this.getNodes();
    DMatrixRMaj variableConnectionMatrix = new DMatrixRMaj(numMassFunctions, numEnergyFunctions);
    int column = 0;
    for (Pipe pipe : pipeList) {
      int row = 0;
      for (Node node : nodeList) {
        if (!node.getType().contentEquals("Outlet")) {
          String endNodeName1 = pipe.getEnd1().getNodeName();
          String endNodeName2 = pipe.getEnd2().getNodeName();
          String nodeName = node.getNodeName();
          if (nodeName.equals(endNodeName1)) {
            variableConnectionMatrix.set(row, column, 1d);
          } else if (nodeName.equals(endNodeName2)) {
            variableConnectionMatrix.set(row, column, -1d);
          }
          row++;
        }
      }
      column++;
    }
    return variableConnectionMatrix;
  }

  /**
   * Creates the transpose of the A1 incidence matrix of the graph.
   * 
   * @return the state variable incidence matrix of the graph transposed.
   */
  public DMatrixRMaj getVariableConnectionMatrixTransposed() {
    DMatrixRMaj variableConnectionMatrix = this.getVariableConnectionMatrix();
    DMatrixRMaj variableConnectionMatrixTransposed = new DMatrixRMaj(
        variableConnectionMatrix.getNumCols(), variableConnectionMatrix.getNumRows());
    TransposeAlgs_DDRM.standard(variableConnectionMatrix, variableConnectionMatrixTransposed);
    return variableConnectionMatrixTransposed;
  }

  /**
   * Creates the A2 incidence matrix of the graph (nodes are rows w/outlets or inlets removed from
   * A, pipes are columns). Contains 0 for no connection between a pipe and a node, 1 for flow into
   * a node and negative 1 for flow out of a node.
   * 
   * @return the constant variable incidence matrix of the graph
   */
  public DMatrixRMaj getConstantConnectionMatrix() {
    int numMassFunctions = this.getNumOutletNodes();
    int numEnergyFunctions = this.getNumPipes();
    List<Pipe> pipeList = this.getPipes();
    List<Node> nodeList = this.getNodes();
    DMatrixRMaj constantConnectionMatrix = new DMatrixRMaj(numMassFunctions, numEnergyFunctions);
    int column = 0;
    for (Pipe pipe : pipeList) {
      int row = 0;
      for (Node node : nodeList) {
        if (node.getType().contentEquals("Outlet")) {
          String endNodeName1 = pipe.getEnd1().getNodeName();
          String endNodeName2 = pipe.getEnd2().getNodeName();
          String nodeName = node.getNodeName();
          if (nodeName.equals(endNodeName1)) {
            constantConnectionMatrix.set(row, column, 1d);
          } else if (nodeName.equals(endNodeName2)) {
            constantConnectionMatrix.set(row, column, -1d);
          }
          row++;
        }
      }
      column++;
    }
    return constantConnectionMatrix;
  }

  /**
   * Creates the transpose of the A2 incidence matrix of the graph.
   * 
   * @return the constant variable incidence matrix of the graph transposed.
   */
  public DMatrixRMaj getConstantConnectionMatrixTransposed() {
    DMatrixRMaj constantConnectionMatrix = this.getConstantConnectionMatrix();
    DMatrixRMaj constantConnectionMatrixTransposed = new DMatrixRMaj(
        constantConnectionMatrix.getNumCols(), constantConnectionMatrix.getNumRows());
    TransposeAlgs_DDRM.standard(constantConnectionMatrix, constantConnectionMatrixTransposed);
    return constantConnectionMatrixTransposed;
  }

  /**
   * Creates the A incidence matrix of the graph (nodes are rows, pipes are columns). Contains 0 for
   * no connection between a pipe and a node, 1 for flow into a node and negative 1 for flow out of
   * a node.
   * 
   * TODO Unused and untested
   * 
   * @return the incidence matrix of the graph
   */
  public DMatrixRMaj getConnectionMatrix() {
    List<Pipe> pipeList = this.getPipes();
    List<Node> nodeList = this.getNodes();
    DMatrixRMaj connectionMatrix = new DMatrixRMaj(this.getNumNodes(), this.getNumPipes());
    int row = 0;
    for (Pipe pipe : pipeList) {
      int column = 0;
      for (Node node : nodeList) {
        String endNodeName1 = pipe.getEnd1().getNodeName();
        String endNodeName2 = pipe.getEnd2().getNodeName();
        String nodeName = node.getNodeName();
        if (nodeName.equals(endNodeName1)) {
          connectionMatrix.set(row, column, 1d);
        } else if (nodeName.equals(endNodeName2)) {
          connectionMatrix.set(row, column, -1d);
        }
        row++;
      }
      column++;
    }
    return connectionMatrix;
  }

  /**
   * @param centerNode the node adjacent to all the nodes which will be returned
   * @return the neighbors of a given node, or null if there are no neighbors or a null node
   */
  public List<Node> getNeighbors(Node centerNode) {
    if (centerNode == null || !containsNode(centerNode)) {
      return null;
    }
    return centerNode.getNeighbors();
  }

  /**
   * @return number of nodes in the graph
   */
  public int getNumNodes() {
    return nodes.size();
  }

  /**
   * @return number of inlet nodes in the graph
   */
  public int getNumInletNodes() {
    int counter = 0;
    for (Node node : nodes) {
      if (node.getType().equals("Inlet")) {
        counter++;
      }
    }
    return counter;
  }

  /**
   * @return number of outlet nodes in the graph
   */
  public int getNumOutletNodes() {
    int counter = 0;
    for (Node node : nodes) {
      if (node.getType().equals("Outlet")) {
        counter++;
      }
    }
    return counter;
  }

  /**
   * @return number of pipes in the graph
   */
  public int getNumPipes() {
    return pipes.size();
  }

  /**
   * @return endnodes nodes in the graph with only one connection and are not an outlet
   */
  public List<Node> getEndNodes() {
    List<Node> endNodes = new ArrayList<>();
    // loop through all the nodes in the graph and add them to the list to be returned only if they
    // have 1 connection and are not a outlet
    for (Node n : nodes) {
      if (n.getNeighbors().size() == 1 && !(n.getType().equals("Outlet"))) {
        endNodes.add(n);
      }
    }
    return endNodes;
  }

  /**
   * Returns the pipe that connects the two end nodes.
   * 
   * @param end1 the first node connected to a pipe.
   * @param end2 the second node connected to a pipe.
   * @return the pipe in the graph with the two end nodes, or null if none exists
   */
  public Pipe getPipe(Node end1, Node end2) {
    // loop through all the pipes in the graph and check and see if they map to the given two end
    // nodes
    for (Pipe p : pipes) {
      // compare the ends of the current pipe to the two target end nodes
      Node test1 = p.getEnd1();
      Node test2 = p.getEnd2();
      Boolean eq1 = (test1.getNodeName().equals(end1.getNodeName())
          && test2.getNodeName().equals(end2.getNodeName()));
      Boolean eq2 = (test1.getNodeName().equals(end2.getNodeName())
          && test2.getNodeName().equals(end1.getNodeName()));
      if (eq1 || eq2) {
        return p;
      }
    }
    // return null if it doesn't exist
    return null;
  }

  /**
   * Retrieves a node from the nodes list by name
   *
   * @param name a string representing the name of the node to retrieve
   * @return the node, or null, if the node is not in the list
   */
  public Node getNode(String name) {
    for (Node n : nodes) {
      if (n.getNodeName().equals(name)) {
        return n;
      }
    }
    return null;
  }

  /**
   * Retrieves a pipe from the pipes list by name
   *
   * @param name a string representing the name of the pipe to retrieve
   * @return the pipe, or null, if the pipe is not in the list
   */
  public Pipe getPipe(String name) {
    for (Pipe p : pipes) {
      if (p.getPipeName().equals(name)) {
        return p;
      }
    }
    return null;
  }

  /**
   * **DEPRECATED** Uses depth first search to return a mapping of each node to its parent node.
   * 
   * @param startNode the node at which the search algorithm assigns a distance of zero.
   * @return a mapping of nodes to their parent nodes.
   */
  public HashMap<Node, Node> dfsTrace(Node startNode) {
    Stack<Node> closedSet = new Stack<>();
    HashMap<Node, Integer> distance = new HashMap<>();
    HashMap<Node, Node> parent = new HashMap<>();

    // initialize unvisited nodes to maximum integer value
    for (Node n : nodes) {
      distance.put(n, Integer.MAX_VALUE);
    }

    // initialize start node, set its distance to one and push onto closed set
    distance.replace(startNode, 0);
    closedSet.push(startNode);

    // using iterator to iterate through stack with FIFO like a queue
    Iterator<Node> itr = closedSet.iterator();

    // while the closed set is not empty
    while (itr.hasNext()) {
      // pop a node off the top of the closed set and locate its neighbors and then assign distance
      // and parent mapping to neighbors
      Node currentNode = closedSet.pop();
      List<Node> neighbors = currentNode.getNeighbors();
      for (Node neighbor : neighbors) {
        // check if node has been previously encountered and if it has been then ignore it
        if (distance.get(neighbor).equals(Integer.MAX_VALUE)) {
          distance.replace(neighbor, distance.get(currentNode) + 1);
          parent.put(neighbor, currentNode);
          closedSet.push(neighbor);
        }
      }
    }
    return parent;
  }

  /**
   * Uses depth first search to return a mapping of each node to its distance.
   *
   * @param startNode the node at which the search algorithm assigns a distance of zero.
   * @return a mapping of nodes to their parent nodes.
   */
  private HashMap<Node, Integer> dfsTraceDistance(Node startNode) {
    Stack<Node> closedSet = new Stack<>();
    HashMap<Node, Integer> distance = new HashMap<>();

    // Checks that the start node is in the graph
    if (!containsNode(startNode)) {
      return null;
    }

    // initialize unvisited nodes to maximum integer value
    for (Node n : nodes) {
      distance.put(n, Integer.MAX_VALUE);
    }

    // initialize start node, set its distance to one and push onto closed set
    distance.replace(startNode, 0);
    closedSet.push(startNode);

    // using iterator to iterate through stack with FIFO like a queue
    Iterator<Node> itr = closedSet.iterator();

    // while the closed set is not empty
    while (itr.hasNext()) {
      // pop a node off the top of the closed set and locate its neighbors and then assign distance
      // and parent mapping to neighbors
      Node currentNode = closedSet.pop();
      List<Node> neighbors = currentNode.getNeighbors();
      for (Node neighbor : neighbors) {
        // check if node has been previously encountered and if it has been then ignore it
        if (distance.get(neighbor) == null) {
          return null;
        }
        if (distance.get(neighbor).equals(Integer.MAX_VALUE)) {
          distance.replace(neighbor, distance.get(currentNode) + 1);
          closedSet.push(neighbor);
        }
      }
    }
    return distance;
  }

  /**
   * Checks for any unvisited nodes by the depth first search algorithm to determine if the graph
   * has any unconnected nodes.
   *
   * @return if the graph contains any nodes unconnected to the center node, or false if the start
   *         node is not in the graph
   */
  public boolean isDisjoint() {
    if (pipes.size() == 0) {
      return true;
    }
    Node startNode = nodes.get(0);
    HashMap<Node, Integer> distance = dfsTraceDistance(startNode);
    if (distance == null) {
      return true;
    }
    // checks all the nodes to see which are not visited
    for (Integer dist : distance.values()) {
      if (dist == Integer.MAX_VALUE) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks for any unvisited nodes by breadth first search algorithm to determine if the graph has
   * any unconnected nodes or loops.
   * 
   * @param startNode the node at which the search starts.
   * @return if the graph has any unconnected nodes or loops.
   */
  public boolean containsLoop(Node startNode) {
    // initialize mappings and stack
    Stack<Node> closedSet = new Stack<>();
    HashMap<Node, Integer> distance = new HashMap<>();
    HashMap<Node, Node> parent = new HashMap<>();

    // Checks that the start node is in the graph
    if (!containsNode(startNode)) {
      return false;
    }

    // initialize unvisited nodes to maximum integer value
    for (Node n : nodes) {
      distance.put(n, Integer.MAX_VALUE);
    }

    // initialize start node, set its distance to one and push onto closed set
    distance.replace(startNode, 0);
    closedSet.push(startNode);

    // using iterator to iterate through stack with FIFO like a queue
    Iterator<Node> itr = closedSet.iterator();

    // while the closed set is not empty
    while (itr.hasNext()) {
      // pop a node off the top of the closed set and locate its neighbors and then assign distance
      // and parent mapping to neighbors
      Node currentNode = closedSet.pop();
      List<Node> neighbors = currentNode.getNeighbors();
      for (Node neighbor : neighbors) {
        // check if node has been previously encountered and if it has been then ignore it
        if (distance.get(neighbor).equals(Integer.MAX_VALUE)) {
          distance.replace(neighbor, distance.get(currentNode) + 1);
          parent.put(neighbor, currentNode);
          closedSet.push(neighbor);
        } else if (!parent.get(currentNode).equals(neighbor)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Identifies the central node or outlet of the graph. TODO Deprecated once multiple outlets are
   * enabled.
   * 
   * @return the central node of the graph assigned distance zero, or null if there is no Outlet
   */
  public Node getCenterNode() {
    for (Node n : nodes) {
      if (n.getType().equals("Outlet")) {
        return n;
      }
    }
    return null;
  }

  /**
   * Checks if the graph has no pipes or nodes.
   * 
   * @return if the graph has no pipes or nodes.
   */
  public boolean isEmpty() {
    return nodes.size() == 0 || pipes.size() == 0;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public List<Node> getNodes() {
    return nodes;
  }

  public void setNodes(List<Node> nodes) {
    this.nodes = nodes;
  }

  public List<Pipe> getPipes() {
    return pipes;
  }

  public void setPipes(List<Pipe> pipes) {
    this.pipes = pipes;
  }

  public List<String> getNodeNames() {
    List<String> nodeNames = new ArrayList<>();
    for (Node node : nodes) {
      nodeNames.add(node.getNodeName());
    }
    return nodeNames;
  }

  public List<String> getPipeNames() {
    List<String> pipeNames = new ArrayList<>();
    for (Pipe pipe : pipes) {
      pipeNames.add(pipe.getPipeName());
    }
    return pipeNames;
  }

  private boolean containsNode(Node node) {
    for (Node n : nodes) {
      if (n == node) {
        return true;
      }
    }
    return false;
  }

  public void setState(String state) {
    for (Pipe pipe : pipes) {
      pipe.setState(state);
    }
    for (Node node : nodes) {
      node.setState(state);
    }
  }
}
