package display;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.util.List;
import model.Node;
import model.Pipe;
import model.Units;

/**
 * 
 * Regulates the display package to ensure no unhandled exceptions are thrown as well as stores data
 * about the display package as a whole and enables the user to change these display wide values.
 * 
 * @author John Coleman
 *
 */
public class UserInterfaceRegulator {
  private static String generalMode;
  private static String subsetMode;
  private static String currentReply;
  private String selectedObjectType;
  private String selectedObjectName = null;
  private static Units systemUnits;
  private MainView graphicInterface;
  private Font displayFont;
  private boolean listNodeProperties;
  private boolean listPipeProperties;
  private boolean displayPipeProperties;
  private boolean displayNodeProperties;
  private String listNodeProperty;
  private String displayPipeProperty;
  private String displayNodeProperty;
  private String listPipeProperty;
  public static final int NODE_OFFSET = 30;
  public static final Color LIGHTGREY1 = new Color(179, 179, 179);
  public static final Color LIGHTGREY2 = new Color(204, 204, 204);
  public static final Color DARKGREY1 = new Color(64, 64, 64);
  public static final Color DARKGREY2 = new Color(48, 48, 48);
  public static final Color GREY = new Color(92, 92, 92);
  public static final Color WHITE1 = new Color(243, 243, 243);
  public static final Color BLUE1 = new Color(127, 167, 204);
  public static final Color LIGHTPURPLE1 = new Color(176, 178, 231);
  public static final Color GREEN1 = new Color(136, 215, 11);
  public static final Color RED1 = new Color(255, 54, 83);


  public UserInterfaceRegulator() {
    selectedObjectName = null;
    selectedObjectType = "None";
    generalMode = "Home";
    subsetMode = "none";
    currentReply = "";
    displayFont = new Font("SansSerif", Font.PLAIN, 12);
    graphicInterface = new MainView();
    graphicInterface.setVisible(true);

    listNodeProperties = true;
    listPipeProperties = true;
    displayNodeProperties = true;
    displayPipeProperties = true;
    listNodeProperty = "None";
    displayNodeProperty = "None";
    listPipeProperty = "None";
    displayPipeProperty = "None";
  }

  public Font getDisplayFont() {
    return displayFont;
  }

  /**
   * Sets the font for the entire display to the default size and font.
   */
  public void setDisplayFont() {
    int fontSize = this.displayFont.getSize();
    this.displayFont = new Font("SansSerif", Font.PLAIN, fontSize);
    for (Component currentComponent : graphicInterface.getDataPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getIdPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getCalculationPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getHomePanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getViewPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getInsertPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getSystemPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getJMenuBar().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    graphicInterface.getModeSelectionPane().setFont(displayFont);
  }

  public void setDisplayFont(Font displayFont) {
    this.displayFont = displayFont;
  }

  /**
   * Sets the font size for the entire display.
   * 
   * @param fontSize the new font size.
   */
  public void setDisplayFont(int fontSize) {
    this.displayFont = new Font("SansSerif", Font.PLAIN, fontSize);
    for (Component currentComponent : graphicInterface.getDataPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getIdPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getCalculationPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getFilePanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getHomePanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getViewPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getInsertPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    for (Component currentComponent : graphicInterface.getSystemPanel().getComponents()) {
      currentComponent.setFont(displayFont);
    }
    /*
     * for(Component currentComponent : graphicInterface.getJMenuBar().getComponents()) {
     * currentComponent.setFont(displayFont); }
     */
    graphicInterface.getModeSelectionPane().setFont(displayFont);
  }

  public static Units getSystemUnits() {
    return systemUnits;
  }

  public static void setSystemUnits(Units systemUnits) {
    UserInterfaceRegulator.systemUnits = systemUnits;
  }

  public String getSelectedObjectType() {
    return selectedObjectType;
  }

  public void setSelectedObjectType(String selectedObjectType) {
    this.selectedObjectType = selectedObjectType;
  }

  public String getSelectedObjectName() {
    return selectedObjectName;
  }

  public void setSelectedObjectName(String selected) {
    try {
      this.selectedObjectName = selected;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getCurrentReply() {
    return currentReply;
  }

  public MainView getGraphicInterface() {
    return graphicInterface;
  }

  public void setGraphicInterface(MainView graphicInterface) {
    this.graphicInterface = graphicInterface;
  }

  public boolean isListNodeProperties() {
    return listNodeProperties;
  }

  public void setListNodeProperties(boolean listNodeProperties) {
    this.listNodeProperties = listNodeProperties;
  }

  public boolean isListPipeProperties() {
    return listPipeProperties;
  }

  public void setListPipeProperties(boolean listPipeProperties) {
    this.listPipeProperties = listPipeProperties;
  }

  public boolean isDisplayNodeProperties() {
    return displayNodeProperties;
  }

  public void setDisplayNodeProperties(boolean displayNodeProperties) {
    this.displayNodeProperties = displayNodeProperties;
  }

  public String getListNodeProperty() {
    return listNodeProperty;
  }

  public void setListNodeProperty(String listNodeProperty) {
    this.listNodeProperty = listNodeProperty;
  }

  public String getDisplayNodeProperty() {
    return displayNodeProperty;
  }

  public void setDisplayNodeProperty(String displayNodeProperty) {
    this.displayNodeProperty = displayNodeProperty;
  }

  public String getListPipeProperty() {
    return listPipeProperty;
  }

  public void setListPipeProperty(String listPipeProperty) {
    this.listPipeProperty = listPipeProperty;
  }

  /**
   * Captures the response from the controller class' latest message to the model package and
   * processes it for the display.
   * 
   * @param currentReply
   */
  public void setCurrentReply(String currentReply) {
    UserInterfaceRegulator.currentReply = currentReply;
    // update panel if user deletes currently selected node
    if (currentReply.length() > 11) {
      if (currentReply.startsWith("DELETE_NODE:")) {
        String[] sections = currentReply.split(" ");
        if (sections.length > 2) {
          String deletedNodeName = sections[2];
          if (selectedObjectType.equals("Node") && selectedObjectName == null) {
            this.graphicInterface.removeDataPanel();
          }
        }
      }
    }
    // update panel if user deletes currently selected pipe
    if (currentReply.length() > 11) {
      if (currentReply.startsWith("DELETE_PIPE:")) {
        String[] sections = currentReply.split(" ");
        if (sections.length > 2) {
          String deletedPipeName = sections[2];
          if (selectedObjectType.equals("Pipe") && selectedObjectName == null) {
            this.graphicInterface.removeDataPanel();
          }
        }
      }
    }
    // display end user entry problems that are not java runtime exceptions marked by the starting
    // phrase "Graph Errors:"
    if (currentReply.length() > 12) {
      if (currentReply.substring(0, 13).equals("Graph Errors@")) {
        String[] sections = currentReply.split("@");
        if (sections.length > 1) {
          String[] problems = sections[1].split(",");
          for (String problem : problems) {
            System.out.println(problem);
          }
        }
      }
    }
  }

  public static void setSubMode(String mode) {
    subsetMode = mode;
  }

  public static void setGeneralMode(String mode) {
    generalMode = mode;
  }

  public static String getSubMode() {
    return subsetMode;
  }

  public static String getGeneralMode() {
    return generalMode;
  }

  public boolean isDisplayPipeProperties() {
    return displayPipeProperties;
  }

  public void setDisplayPipeProperties(boolean displayPipeProperties) {
    this.displayPipeProperties = displayPipeProperties;
  }

  public String getDisplayPipeProperty() {
    return displayPipeProperty;
  }

  public void setDisplayPipeProperty(String displyaPipeProperty) {
    this.displayPipeProperty = displyaPipeProperty;
  }

  /**
   * Determines if a node was clicked based on mouse click proximity to the node's model package
   * location.
   * 
   * @param nodes a list of nodes
   * @param mousePoint the model package location of the mouse click.
   * @return the unique integer representing the selected node.
   */
  public static String clickedNode(List<Node> nodes, Point mousePoint) {
    String selectedNodeName = null;
    for (Node node : nodes) {
      Point nodePoint = node.getLocation();
      if (Math.abs(nodePoint.getX() - mousePoint.getX()) <= NODE_OFFSET
          && Math.abs(nodePoint.getY() - mousePoint.getY()) <= NODE_OFFSET) {
        selectedNodeName = node.getNodeName();
      }
    }
    return selectedNodeName;
  }

  /**
   * Determines if a pipe was clicked based on mouse click proximity to the model package line
   * segment location of the pipe.
   * 
   * @param pipes a list of pipes
   * @param mousePoint the model package location of the mouse click.
   * @return the unique integer representing the selected pipe.
   */
  public static String clickedPipe(List<Pipe> pipes, Point mousePoint) {
    String selectedPipeName = null;
    for (Pipe currentPipe : pipes) {
      DoublePoint endPoint1 = new DoublePoint(currentPipe.getEnd1().getLocation());
      DoublePoint endPoint2 = new DoublePoint(currentPipe.getEnd2().getLocation());
      DoublePoint mouseLoc = new DoublePoint(mousePoint);
      if (distToSegment(mouseLoc, endPoint1, endPoint2) < NODE_OFFSET) {
        selectedPipeName = currentPipe.getPipeName();
      }
    }
    return selectedPipeName;
  }

  public void refreshFrame() {
    graphicInterface.revalidate();
    graphicInterface.repaint();
  }

  /**
   * Finds the distance from a point to a line segment.
   * 
   * @param p the point.
   * @param v the first end point of the line segment.
   * @param w the second end point of the line segment.
   * @return
   */
  static double distToSegment(DoublePoint p, DoublePoint v, DoublePoint w) {
    return Math.sqrt(distToSegmentSquared(p, v, w));
  }

  static double sqr(double x) {
    return x * x;
  }

  static double dist2(DoublePoint v, DoublePoint w) {
    return sqr(v.x - w.x) + sqr(v.y - w.y);
  }

  static double distToSegmentSquared(DoublePoint p, DoublePoint v, DoublePoint w) {
    double l2 = dist2(v, w);
    if (l2 == 0)
      return dist2(p, v);
    double t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2;
    if (t < 0)
      return dist2(p, v);
    if (t > 1)
      return dist2(p, w);
    return dist2(p, new DoublePoint(v.x + t * (w.x - v.x), v.y + t * (w.y - v.y)));
  }

  static class DoublePoint {
    public double x;
    public double y;

    public DoublePoint(double x, double y) {
      this.x = x;
      this.y = y;
    }

    public DoublePoint(Point point) {
      this.x = Double.valueOf(point.x);
      this.y = Double.valueOf(point.y);
    }
  }
}
