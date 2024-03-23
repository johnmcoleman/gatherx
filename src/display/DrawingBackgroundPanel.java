package display;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import javax.swing.JPanel;
import controller.GatherX;
import model.Graph;
import model.Node;
import model.Pipe;

public class DrawingBackgroundPanel extends JPanel {
  private static final long serialVersionUID = 6957506136464486182L;
  private String systemState;
  private JLabel timeFrameNumber;

  public DrawingBackgroundPanel() {
    setOpaque(true);
    setVisible(true);
  }

  /**
   * Draws the panel behind the drawing panel to give it a consistent size border with a color which
   * indicates the success or failure state of the currently selected graph.
   * 
   * @Override
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D graph = (Graphics2D) g;
    Graph currentGraph = GatherX.getCurrentGatheringSystem().getGraph();
    systemState = "neutral";
    for (Pipe currentPipe : currentGraph.getPipes()) {
      String pipeStatus = currentPipe.getState();
      switch (pipeStatus) {
        case "success":
          if (!systemState.equals("failed")) {
            systemState = "success";
          }
          break;
        case "failed":
          systemState = "failed";
          break;
        case "neutral":
          break;
        default:
      }
    }
    for (Node currentNode : currentGraph.getNodes()) {
      String nodeStatus = currentNode.getState();
      graph.setColor(UserInterfaceRegulator.LIGHTGREY2);
      switch (nodeStatus) {
        case "success":
          if (!systemState.equals("failed")) {
            systemState = "success";
          }
          break;
        case "failed":
          systemState = "failed";
          break;
        case "neutral":
          break;
        default:
      }
      switch (systemState) {
        case "success":
          this.setBackground(UserInterfaceRegulator.GREEN1);
          break;
        case "neutral":
          this.setBackground(UserInterfaceRegulator.LIGHTGREY2);
          break;
        case "failure":
          this.setBackground(UserInterfaceRegulator.RED1);
          break;
        default:
          this.setBackground(UserInterfaceRegulator.WHITE1);
          break;
      }
    }
  }
}
