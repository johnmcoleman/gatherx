package display;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import controller.GatherX;
import model.Graph;
import model.Node;
import model.Pipe;
import model.Units;

/**
 * The leftmost panel on the display which shows the user the nodes and pipes and various data about
 * these elements for the currently selected gas gathering system project file.
 * 
 * @author John Coleman
 *
 */
public class IdPanel extends JPanel {

  private ArrayList<JLabel> idPipeLabels = new ArrayList<>();
  private ArrayList<JLabel> idNodeLabels = new ArrayList<>();
  private JLabel nodeTitleLabel = new JLabel("Nodes:");
  private JLabel pipeTitleLabel = new JLabel("Pipes:");
  private JLabel emptyLabel = new JLabel("");
  private DecimalFormat precision = new DecimalFormat("#.##");
  // private JTextField searchBar = new JTextField(); search bar in future versions
  private int xPos;
  private int yPos;

  public ArrayList<JLabel> getPipeIdLabels() {
    return idPipeLabels;
  }

  public void setPipeIdLabels(ArrayList<JLabel> idLabels) {
    this.idPipeLabels = idLabels;
  }

  public ArrayList<JLabel> getNodeIdLabels() {
    return idNodeLabels;
  }

  public void setNodeIdLabels(ArrayList<JLabel> idLabels) {
    this.idNodeLabels = idLabels;
  }

  /**
   * Constructs a blank panel and wipes prior elements.
   */
  public IdPanel() {
    // clear the panel
    this.removeAll();
    this.setLayout(new GridBagLayout());
    idPipeLabels.clear();
    idNodeLabels.clear();

    // note nothing on this constructor actually gets shown because the ui is immediately refreshed
  }

  /**
   * Constructs a panel with nodes and pipes and their properties listed based on the current
   * project file.
   * 
   * @param graph the current graph in the gathering system
   */
  public IdPanel(Graph graph) {
    // clears the panel
    this.removeAll();
    this.setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = new Insets(10, 10, 10, 10);
    constraints.anchor = GridBagConstraints.NORTH;
    constraints.fill = GridBagConstraints.NONE;
    idPipeLabels.clear();
    idNodeLabels.clear();

    // sorts nodes and pipes
    List<String> pipeNameList = graph.getPipeNames();
    List<String> nodeNameList = graph.getNodeNames();
    List<Node> nodeList = graph.getNodes();
    List<Pipe> pipeList = graph.getPipes();
    List<Node> sortedNodeList = new ArrayList<>();
    List<Pipe> sortedPipeList = new ArrayList<>();
    Collections.sort(pipeNameList);
    Collections.sort(nodeNameList);
    for (int index = 0; index < pipeNameList.size(); index++) {
      String pipeName = pipeNameList.get(index);
      for (Pipe pipe : pipeList) {
        if (pipe.getPipeName().contentEquals(pipeName)) {
          sortedPipeList.add(pipe);
        }
      }
    }
    for (int index = 0; index < nodeNameList.size(); index++) {
      String nodeName = nodeNameList.get(index);
      for (Node node : nodeList) {
        if (node.getNodeName().contentEquals(nodeName)) {
          sortedNodeList.add(node);
        }
      }
    }
    nodeList = sortedNodeList;
    pipeList = sortedPipeList;

    boolean isListNodeProperties = GatherX.getUiRegulator().isListNodeProperties();
    boolean isListPipeProperties = GatherX.getUiRegulator().isListPipeProperties();
    Units unitSystem = GatherX.getCurrentGatheringSystem().getUnits();

    // adds node labels
    xPos = 0;
    yPos = 0;
    constraints.gridx = xPos;
    constraints.gridy = yPos;
    if (nodeList.size() == 0) {
      constraints.weighty = 1;
    } else {
      constraints.weighty = 0;
    }
    this.add(nodeTitleLabel, constraints);
    yPos = 1;
    for (int index = 0; index < nodeList.size(); index++) {
      JLabel label = new JLabel(nodeList.get(index).getNodeName());
      if (index == nodeList.size() - 1 && pipeList.size() <= nodeList.size()) {
        constraints.weighty = 1;
      }
      idNodeLabels.add(label);
      constraints.gridx = xPos;
      constraints.gridy = yPos;
      this.add(label, constraints);
      yPos++;
    }

    // adds selected node property
    if (isListNodeProperties) {
      String listParameter = GatherX.getUiRegulator().getListNodeProperty();
      xPos += 1;
      yPos = 0;
      constraints.gridx = xPos;
      constraints.gridy = yPos;
      if (nodeList.size() == 0) {
        constraints.weighty = 1;
      } else {
        constraints.weighty = 0;
      }
      this.add(emptyLabel, constraints);
      yPos = 1;
      for (int index = 0; index < nodeList.size(); index++) {
        String labelText = "";
        Node currentNode = nodeList.get(index);
        switch (listParameter) {
          case "Inlet Pressure":
            labelText = String.valueOf(precision
                .format(Units.convert(currentNode.getInletPressure(), "psia",
                    unitSystem.getNodePressureUnit(), "pressure"))
                + " " + unitSystem.getNodePressureUnit());
            break;
          case "Inlet Volumes":
            labelText = String.valueOf(precision
                .format(Units.convert(currentNode.getVolumes(), "scfd",
                    unitSystem.getNodeVolumeUnit(), "volume"))
                + " " + unitSystem.getNodeVolumeUnit());
            break;
          default:
        }
        JLabel label = new JLabel(labelText);
        if (index == pipeList.size() - 1 && pipeList.size() >= nodeList.size()) {
          constraints.weighty = 1;
        }
        idNodeLabels.add(label);
        constraints.gridx = xPos;
        constraints.gridy = yPos;
        this.add(label, constraints);
        yPos++;
      }
    }

    // adds pipe labels
    xPos += 1;
    yPos = 0;
    constraints.gridx = xPos;
    constraints.gridy = yPos;
    if (nodeList.size() == 0) {
      constraints.weighty = 1;
    } else {
      constraints.weighty = 0;
    }
    this.add(pipeTitleLabel, constraints);
    yPos = 1;
    for (int index = 0; index < pipeNameList.size(); index++) {
      JLabel label = new JLabel(pipeNameList.get(index));
      if (index == pipeList.size() - 1 && pipeList.size() >= nodeList.size()) {
        constraints.weighty = 1;
      }
      idPipeLabels.add(label);
      constraints.gridx = xPos;
      constraints.gridy = yPos;
      this.add(label, constraints);
      yPos++;
    }

    // adds selected pipe property
    if (isListPipeProperties) {
      String listParameter = GatherX.getUiRegulator().getListPipeProperty();
      xPos += 1;
      yPos = 0;
      constraints.gridx = xPos;
      constraints.gridy = yPos;
      if (nodeList.size() == 0) {
        constraints.weighty = 1;
      } else {
        constraints.weighty = 0;
      }
      this.add(emptyLabel, constraints);
      yPos = 1;
      for (int index = 0; index < pipeList.size(); index++) {
        String labelText = "";
        Pipe currentPipe = pipeList.get(index);
        switch (listParameter) {
          case "Volumes":
            labelText = String.valueOf(precision
                .format(Units.convert(Math.abs(currentPipe.getPipeFlowRate()), "scfd",
                    unitSystem.getNodeVolumeUnit(), "volume"))
                + " " + unitSystem.getNodeVolumeUnit());
            break;
          case "Efficiency":
            labelText = String.valueOf(precision.format(currentPipe.getEfficiency()));
            break;
          case "Pressure Gradient":
            labelText =
                String.valueOf(precision.format(currentPipe.getMaxPressureGradient()) + " psi/mi");
            break;
          case "Velocity":
            labelText =
                String.valueOf(precision.format(Math.abs(currentPipe.getVelocity())) + " ft/s");
            break;
          default:
        }
        JLabel label = new JLabel(labelText);
        if (index == pipeList.size() - 1 && pipeList.size() >= nodeList.size()) {
          constraints.weighty = 1;
        }
        idPipeLabels.add(label);
        constraints.gridx = xPos;
        constraints.gridy = yPos;
        this.add(label, constraints);
        yPos++;
      }
    }

    // add action listener to all labels
    MouseListener labelListener = new IdPanelListener();
    for (JLabel label : idPipeLabels) {
      label.addMouseListener(labelListener);
    }
    for (JLabel label : idNodeLabels) {
      label.addMouseListener(labelListener);
    }
  }
}
