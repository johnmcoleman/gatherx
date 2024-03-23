package display;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import controller.GatherX;
import model.Graph;
import model.Node;
import model.Pipe;

/**
 * Detects actions on the pipe and node identification panel and allows the user to select pipes and
 * nodes from the panel.
 * 
 * @author John Coleman
 *
 */
public class IdPanelListener implements MouseListener {

  @Override
  public void mouseClicked(MouseEvent e) {
    Graph graph = GatherX.getCurrentGatheringSystem().getGraph();

    ArrayList<JLabel> idPipeLabels =
        GatherX.getUiRegulator().getGraphicInterface().getIdPanel().getPipeIdLabels();
    ArrayList<JLabel> idNodeLabels =
        GatherX.getUiRegulator().getGraphicInterface().getIdPanel().getNodeIdLabels();
    boolean selectedNode = false;
    boolean selectedPipe = false;
    JLabel currentLabel = new JLabel();
    for (JLabel label : idPipeLabels) {
      if (e.getSource().equals(label)) {
        currentLabel = label;
        selectedPipe = true;
      }
    }
    for (JLabel label : idNodeLabels) {
      if (e.getSource().equals(label)) {
        currentLabel = label;
        selectedNode = true;
      }
    }
    if (selectedPipe) {
      for (Pipe pipe : graph.getPipes()) {
        if (pipe.getPipeName().equals(currentLabel.getText())) {
          GatherX.getUiRegulator().setSelectedObjectType("Pipe");
          GatherX.getUiRegulator().setSelectedObjectName(pipe.getPipeName());
          String dataPanelType = "Pipe,NA";
          GatherX.refreshInterface();
          break;
        }
      }
    }
    if (selectedNode) {
      for (Node node : graph.getNodes()) {
        if (node.getNodeName().equals(currentLabel.getText())) {
          GatherX.getUiRegulator().setSelectedObjectType("Node");
          GatherX.getUiRegulator().setSelectedObjectName(node.getNodeName());
          String dataPanelType = "Node," + node.getType();
          GatherX.refreshInterface();
          break;
        }
      }
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub

  }

}
