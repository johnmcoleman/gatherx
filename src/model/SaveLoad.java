package model;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Save Load class saves and loads individual project files which are instances of the Gathering
 * System class in various formats.
 * 
 * @author John Coleman
 * @author Corey Tolbert
 *
 */
public class SaveLoad {

  /**
   * Saves an instance of the Gathering System class as a project file.
   * 
   * @param filePath the location on the drive where the file will be saved.
   * @param systemToSave the gathering system object to be saved.
   */
  public static void saveFile(String filePath, GatheringSystem systemToSave) {
    try {
      FileOutputStream fileOut = new FileOutputStream(filePath);
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
      objectOut.writeObject(systemToSave);
      objectOut.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads an instance of the Gathering System class from a project file.
   * 
   * @param filePath the location on the drive where the file will be loaded from.
   * @return
   */
  public static GatheringSystem loadFile(String filePath) {
    GatheringSystem gatheringSystem = new GatheringSystem();
    try {
      FileInputStream saveFile = new FileInputStream(filePath);
      ObjectInputStream restore = new ObjectInputStream(saveFile);
      Object obj = restore.readObject();
      gatheringSystem = (GatheringSystem) obj;
      restore.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return gatheringSystem;
  }

  // Works at least somewhat
  public static GatheringSystem importCSV(String filePath) {
    GatheringSystem gatheringSystem = new GatheringSystem();
    try {
      BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
      String row = csvReader.readLine();
      String[] firstLine = row.split(",");
      Integer numNodes = Integer.parseInt(firstLine[2]);
      Integer numPipes = Integer.parseInt(firstLine[4]);
      csvReader.readLine(); // THROW AWAY LINE THAT CONTAINS WHAT EACH
      // COLUMN MEANS IN THE CSV
      List<Graph> graphTimeline = new ArrayList<>();
      int count = 0;
      boolean isLineEmpty = false;
      while ((row = csvReader.readLine()) != null && isLineEmpty == false) {
        count++;
        String[] data = row.split(",");
        // Breaks out of the loop if some of the data is missing
        if (data.length == 0 || data == null) {
          isLineEmpty = true;
        }
        for (int i = 0; i < data.length; i++) {
          if (data[i].contentEquals("")) {
            isLineEmpty = true;
          }
        }
        if (isLineEmpty) {
          break;
        }
        Graph slice = new Graph();
        List<Node> nodes = new ArrayList<>();
        List<Pipe> pipes = new ArrayList<>();
        Map<String, String> nodeToNeigh = new HashMap<>();
        slice.setTimestamp(data[0]);
        int index = 1;
        for (int i = 0; i < numNodes; i++) {
          nodes.add(nodeFromCSV(nodeToNeigh, Arrays.copyOfRange(data, index, index + 7)));
          index += 7;
        }
        updateNeighbors(nodes, nodeToNeigh);
        slice.setNodes(nodes);

        for (int i = 0; i < numPipes; i++) {
          pipes.add(pipeFromCSV(nodes, Arrays.copyOfRange(data, index, index + 16)));
          index += 16;
        }
        slice.setPipes(pipes);

        graphTimeline.add(slice);
      }
      gatheringSystem.setGraphTimeline(graphTimeline);
      gatheringSystem.setGraph(graphTimeline.get(0));
      gatheringSystem.setName(filePath);
      csvReader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gatheringSystem;
  }

  // TODO IMPLEMENT AND TEST
  public static void exportCSV(String filePath, GatheringSystem systemToSave) {
    int numNodes = systemToSave.getGraph().getNumNodes();
    int numPipes = systemToSave.getGraph().getNumPipes();
    List<Graph> graphTimeline = systemToSave.getGraphTimeline();
    if (graphTimeline == null) {
      graphTimeline = new ArrayList<>();
      graphTimeline.add(systemToSave.getGraph());
    }
    try {
      FileWriter csvWriter = new FileWriter(filePath, false);
      // Writes first line
      csvWriter.append("Time,NODES,");
      csvWriter.append(Integer.toString(numNodes));
      csvWriter.append(",PIPES,");
      csvWriter.append(Integer.toString(numPipes));
      csvWriter.append("\n");

      // Writes standard second line
      csvWriter.append("null");
      for (int i = 1; i <= numNodes; i++) {
        csvWriter.append(",Node");
        csvWriter.append(Integer.toString(i));
        csvWriter.append("Name,");
        csvWriter.append("Node");
        csvWriter.append(Integer.toString(i));
        csvWriter.append("Type,");
        csvWriter.append("Node");
        csvWriter.append(Integer.toString(i));
        csvWriter.append("Neighbors,");
        csvWriter.append("nodeVolumes(scfd),inletPressure(psia),location,state");
      }
      for (int i = 1; i <= numPipes; i++) {
        csvWriter.append(",Pipe");
        csvWriter.append(Integer.toString(i));
        csvWriter.append("Name,");
        csvWriter
            .append("nodeEnd1,nodeEnd2,length(mi),diameter(in),flowRate(scfd),inletPressure(psia)"
                + ",outletPressure(psia),pressureGradient(psi/mi),gravityS(air),pressureBasePB(psi)"
                + ",tempAvg(R),tempBase(R),zAvg,efficiency,state");
      }
      csvWriter.append("\n");

      for (Graph g : graphTimeline) {
        // Appends timestamp
        csvWriter.append(g.getTimestamp());
        for (Node node : g.getNodes()) {
          csvWriter.append(",");
          csvWriter.append(node.toString());
        }
        for (Pipe pipe : g.getPipes()) {
          csvWriter.append(",");
          csvWriter.append(pipe.toString());
        }
        csvWriter.append("\n");
      }
      csvWriter.flush();
      csvWriter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static Node nodeFromCSV(Map<String, String> nodeToNeighbors, String[] data) {
    String name = data[0];
    double nodeVol = Double.parseDouble(data[3]) * 1000d;
    double inPress = Double.parseDouble(data[4]) + 14.73d;
    // Handles Location
    String[] coord = data[5].split(";");
    int x = Integer.parseInt(coord[0].trim());
    int y = Integer.parseInt(coord[1].trim());

    Node node = new Node();
    // SETS EVERYTHING TO THE CURRENT NODE
    node.setNodeName(name);
    node.setNodeType(data[1]);
    nodeToNeighbors.put(name, data[2]);
    node.setNodeVolumes(nodeVol);
    node.setInletPressure(inPress);
    node.setLocation(new Point(x, y));
    node.setState(data[6]);

    return node;
  }

  private static void updateNeighbors(List<Node> nodeList, Map<String, String> nodeToNeighbors) {
    for (Node currNode : nodeList) {
      // Handles Neighbors, using the previously created node if it exists
      String neighs = nodeToNeighbors.get(currNode.getNodeName());
      String[] neighNames = neighs.split(";");
      List<Node> neighbors = new ArrayList<>();
      // Parses neighbors from their names
      for (String neighName : neighNames) {
        if (neighName.length() == 0) {
          continue;
        }
        String neighNameTrimmed = neighName.trim();
        for (Node n : nodeList) {
          if (n.getNodeName().equals(neighNameTrimmed)) {
            neighbors.add(n);
            break;
          }
        }
      }
      // Updates Neighbors
      currNode.setNeighbors(neighbors);
    }
  }

  private static Pipe pipeFromCSV(List<Node> nodeList, String[] data) {
    double length = Double.parseDouble(data[3]);
    double diam = Double.parseDouble(data[4]);
    double flow = Double.parseDouble(data[5]);
    double press1 = Double.parseDouble(data[6]);
    double press2 = Double.parseDouble(data[7]);
    double maxPG = Double.parseDouble(data[8]);
    double grav = Double.parseDouble(data[9]);
    double pressBase = Double.parseDouble(data[10]);
    double tempAvg = Double.parseDouble(data[11]);
    double tempBase = Double.parseDouble(data[12]);
    double zAvg = Double.parseDouble(data[13]);
    double eff = Double.parseDouble(data[14]);
    // Handles end nodes
    String end1Name = data[1];
    String end2Name = data[2];
    Node end1 = null;
    Node end2 = null;
    for (Node n : nodeList) {
      if (n.getNodeName().equals(end1Name)) {
        end1 = n;
      }
      if (n.getNodeName().equals(end2Name)) {
        end2 = n;
      }
    }

    Pipe pipe = new Pipe();
    // SETS EVERYTHING TO THE CURRENT PIPE
    pipe.setPipeName(data[0]);
    pipe.setEnd1(end1);
    pipe.setEnd2(end2);
    pipe.setPipeLength(length);
    pipe.setDiameter(diam);
    pipe.setFlowRate(flow);
    pipe.setInPressure(press1);
    pipe.setOutPressure(press2);
    pipe.setMaxPressureGradient(maxPG);
    pipe.setGravity(grav);
    pipe.setBasePressure(pressBase);
    pipe.setAvgTemp(tempAvg);
    pipe.setBaseTemp(tempBase);
    pipe.setZ(zAvg);
    pipe.setEfficiency(eff);
    pipe.setState(data[15]);

    return pipe;
  }
}
