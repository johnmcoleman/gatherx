package display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import controller.Action;
import controller.GatherX;
import controller.Message;
import controller.MessageDispatch;
import controller.Runner;
import model.Graph;
import model.Node;
import model.Pipe;
import model.Units;

/**
 * The Drawing Panel class creates the panel in the middle of the screen where the user can place
 * node and pipe elements which are visually represented as lines and points on a plane.
 * 
 * @author John Coleman
 *
 */
public class DrawingPanel extends JPanel
    implements MouseWheelListener, MouseListener, MouseMotionListener {

  private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();;

  private double zoomFactor = 1;
  private double prevZoomFactor = 1;
  private boolean zoomer;
  private boolean dragger;
  private boolean released;
  private double xOffset;
  private double yOffset;
  private int xDiff;
  private int yDiff;
  private Point startPoint;
  private DecimalFormat precision = new DecimalFormat("#.##");
  AffineTransform at = new AffineTransform();;

  private boolean isFirstClick = true;
  private MessageDispatch dispatcher = GatherX.getDispatcher();
  private String firstClickNodeName = null;
  private String endNodeNames = "";
  private String pipeLocation = "";
  private int nodeOffset = UserInterfaceRegulator.NODE_OFFSET;


  public DrawingPanel() {
    initComponent();
  }

  /**
   * Initializes the panel by loading resources and adding listeners.
   */
  private void initComponent() {
    setVisible(true);
    // load image files
    try {
      BufferedImage outlet = ImageIO.read(Runner.class.getResourceAsStream("/images/outlet.png"));
      images.add(outlet);
      BufferedImage inlet = ImageIO.read(Runner.class.getResourceAsStream("/images/inlet.png"));
      images.add(inlet);
      BufferedImage tee = ImageIO.read(Runner.class.getResourceAsStream("/images/tee.png"));
      images.add(tee);
      BufferedImage compressor =
          ImageIO.read(Runner.class.getResourceAsStream("/images/compressor.png"));
      images.add(compressor);
      BufferedImage controlValve =
          ImageIO.read(Runner.class.getResourceAsStream("/images/controlValve.png"));
      images.add(controlValve);
    } catch (IOException e) {
      if (GatherX.DEBUG) {
        System.out.println(e.getStackTrace());
      }
    }
    addMouseWheelListener(this);
    addMouseMotionListener(this);
    addMouseListener(this);
    Graph currentGraph = GatherX.getCurrentGatheringSystem().getGraph();
    if (!currentGraph.getNodes().isEmpty()) {
      Node centerNode = currentGraph.getNodes().get(0);
      Point start = centerNode.getLocation();
      xOffset = start.getX();
      yOffset = start.getY();
    } else {
      xOffset = 0;
      yOffset = 0;
    }
  }

  /**
   * Draws the panel with all the user placed elements from the model.
   * 
   * @Override
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D graph = (Graphics2D) g;

    // zooms and repositions the screen
    if (zoomer) {
      AffineTransform at = new AffineTransform();

      double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
      double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

      double zoomDiv = zoomFactor / prevZoomFactor;

      this.xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
      this.yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

      at.translate(xOffset, yOffset);
      at.scale(zoomFactor, zoomFactor);
      this.prevZoomFactor = zoomFactor;
      graph.transform(at);
      this.zoomer = false;
    } else if (this.dragger) {
      AffineTransform at = new AffineTransform();
      at.translate(xOffset + xDiff, yOffset + yDiff);
      at.scale(zoomFactor, zoomFactor);
      graph.transform(at);

      if (this.released) {
        this.xOffset += xDiff;
        this.yOffset += yDiff;
        this.dragger = false;
      }
    } else {
      AffineTransform at = new AffineTransform();
      at.translate(xOffset, yOffset);
      at.scale(zoomFactor, zoomFactor);
      graph.transform(at);
    }

    // gets all the information needed to draw the system
    String selectedObjectName = GatherX.getUiRegulator().getSelectedObjectName();
    String selectedObjectType = GatherX.getUiRegulator().getSelectedObjectType();
    Graph currentGraph = GatherX.getCurrentGatheringSystem().getGraph();

    // draws highlight of selected object
    if ((selectedObjectName != null)) {
      switch (selectedObjectType) {
        case "Pipe":
          Pipe pipe = currentGraph.getPipe(selectedObjectName);
          if (pipe != null) {
            Point node1Location = pipe.getEnd1().getLocation();
            Point node2Location = pipe.getEnd2().getLocation();
            graph.setColor(UserInterfaceRegulator.WHITE1);
            graph.setStroke(
                new BasicStroke((float) ((nodeOffset) * 1.7d * (1 / (zoomFactor * 0.3 + 1)))));
            graph.drawLine(node1Location.x, node1Location.y, node2Location.x, node2Location.y);
          }
          break;
        case "Node":
          Node currentNode = currentGraph.getNode(selectedObjectName);
          if (currentNode != null) {
            Point nodePoint = currentNode.getLocation();
            double radius = nodeOffset;
            Shape theCircle = new Ellipse2D.Double(nodePoint.x - radius, nodePoint.y - radius,
                2.0 * radius, 2.0 * radius);
            graph.setColor(UserInterfaceRegulator.WHITE1);
            graph.setStroke(
                new BasicStroke((float) (nodeOffset * 1.5 * (1 / (zoomFactor * 0.5 + 1)))));
            graph.draw(theCircle);
          }
          break;
        default:
          break;
      }
    }
    // draws pipes
    for (Pipe currentPipe : currentGraph.getPipes()) {
      Point node1Location = currentPipe.getEnd1().getLocation();
      Point node2Location = currentPipe.getEnd2().getLocation();
      // draws pipe property highlight
      String currentVisiblePipeProperty = GatherX.getUiRegulator().getDisplayPipeProperty();
      Color propertyColor = getColorFromProperty(currentVisiblePipeProperty, currentPipe);
      graph.setColor(propertyColor);
      graph.setStroke(new BasicStroke((float) (nodeOffset)));
      graph.drawLine(node1Location.x, node1Location.y, node2Location.x, node2Location.y);
      // draws connection line
      graph.setColor(UserInterfaceRegulator.GREY);
      graph.setStroke(new BasicStroke(nodeOffset / 2f));
      graph.drawLine(node1Location.x, node1Location.y, node2Location.x, node2Location.y);
      graph.setColor(UserInterfaceRegulator.LIGHTGREY2);
      graph.setStroke(new BasicStroke(nodeOffset / 4f));
      graph.drawLine(node1Location.x, node1Location.y, node2Location.x, node2Location.y);
      // draw flow direction indicator
      Point midpoint = new Point((node1Location.x + node2Location.x) / 2,
          (node1Location.y + node2Location.y) / 2);
      if (currentPipe.getFlowRate() < 0.0) {
        graph.setColor(UserInterfaceRegulator.WHITE1);
        graph.setStroke(new BasicStroke(nodeOffset / 2f));
        graph.drawLine(node1Location.x, node1Location.y, midpoint.x, midpoint.y);
      } else {
        graph.setColor(UserInterfaceRegulator.WHITE1);
        graph.setStroke(new BasicStroke(nodeOffset / 2f));
        graph.drawLine(node2Location.x, node2Location.y, midpoint.x, midpoint.y);
      }
    }
    // draws nodes
    for (Node currentNode : currentGraph.getNodes()) {
      String nodeType = currentNode.getType();
      Point nodePoint = currentNode.getLocation();
      // draws the calculation status ring
      double radius = nodeOffset;
      Shape theCircle = new Ellipse2D.Double(nodePoint.x - radius, nodePoint.y - radius,
          2.0 * radius, 2.0 * radius);
      graph.setColor(UserInterfaceRegulator.LIGHTGREY2);
      graph.setStroke(new BasicStroke(nodeOffset / 4f));
      graph.draw(theCircle);
      // draws the image file
      switch (nodeType) {
        case "Inlet":
          graph.drawImage(images.get(1), nodePoint.x - nodeOffset, nodePoint.y - nodeOffset, this);
          break;
        case "Tee":
          graph.drawImage(images.get(2), nodePoint.x - nodeOffset, nodePoint.y - nodeOffset, this);
          break;
        case "Outlet":
          graph.drawImage(images.get(0), nodePoint.x - nodeOffset, nodePoint.y - nodeOffset, this);
          break;
        case "Compressor":
          graph.drawImage(images.get(3), nodePoint.x - nodeOffset, nodePoint.y - nodeOffset, this);
          break;
        case "Control Valve":
          graph.drawImage(images.get(4), nodePoint.x - nodeOffset, nodePoint.y - nodeOffset, this);
          break;
      }
    }
    // draws node properties selected for display
    boolean isDisplayNodeProperties = GatherX.getUiRegulator().isDisplayNodeProperties();
    Units unitSystem = GatherX.getCurrentGatheringSystem().getUnits();
    if (isDisplayNodeProperties) {
      String currentVisibleProperty = GatherX.getUiRegulator().getDisplayNodeProperty();
      for (Node currentNode : currentGraph.getNodes()) {
        Point nodePoint = currentNode.getLocation();
        String nodeParameter = "";
        switch (currentVisibleProperty) {
          case "Inlet Pressure":
            double inletPressure = Units.convert(currentNode.getInletPressure(), "psia",
                unitSystem.getNodePressureUnit(), "pressure");
            nodeParameter = String.valueOf(precision.format(inletPressure)) + " "
                + unitSystem.getNodePressureUnit();
            break;
          case "Inlet Volumes":
            double inletVolumes = Units.convert(currentNode.getVolumes(), "scfd",
                unitSystem.getNodeVolumeUnit(), "volume");
            nodeParameter = String.valueOf(precision.format(inletVolumes)) + " "
                + unitSystem.getNodeVolumeUnit();
            break;
          default:
        }
        if (!nodeParameter.equals("")) {
          graph.setFont(GatherX.getUiRegulator().getDisplayFont());
          FontMetrics fm = g.getFontMetrics();
          Rectangle2D rect = fm.getStringBounds(nodeParameter, g);

          g.setColor(UserInterfaceRegulator.WHITE1);
          g.fillRect(nodePoint.x + nodeOffset, nodePoint.y - nodeOffset - fm.getAscent(),
              (int) rect.getWidth(), (int) rect.getHeight());
          graph.setColor(UserInterfaceRegulator.DARKGREY2);
          graph.drawString(nodeParameter, nodePoint.x + nodeOffset, nodePoint.y - nodeOffset);
        }
      }
    }
  }

  /**
   * Manages the zoom ratio of the panel based on mouse wheel scrolling.
   * 
   * @Override
   */
  public void mouseWheelMoved(MouseWheelEvent e) {

    zoomer = true;

    // zoom in
    if (e.getWheelRotation() < 0) {
      this.zoomFactor *= 1.1;
      this.repaint();
    }
    // zoom out
    if (e.getWheelRotation() > 0) {
      this.zoomFactor /= 1.1;
      this.repaint();
    }
    // cap zoom factor
    if (zoomFactor > 5.0) {
      this.zoomFactor = 5.0;
    } else if (zoomFactor < 0.2) {
      this.zoomFactor = 0.2;
    }
  }

  /**
   * Manages panning of the panel through mouse dragging.
   * 
   * @Override
   */
  public void mouseDragged(MouseEvent e) {
    Point curPoint = e.getLocationOnScreen();
    this.xDiff = curPoint.x - startPoint.x;
    this.yDiff = curPoint.y - startPoint.y;

    this.dragger = true;
    this.repaint();
  }

  @Override
  public void mouseMoved(MouseEvent e) {}

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  /**
   * Determines the location of objects placed and sends a message through the controller to the
   * model for updates.
   * 
   * @Override
   */
  public void mousePressed(MouseEvent e) {
    // zoom and pan information set
    this.released = false;
    this.startPoint = MouseInfo.getPointerInfo().getLocation();

    // set mouse point
    Point mousePoint = new Point(e.getX(), e.getY());
    Point trueLocation = new Point();

    // transform to true coordinates
    AffineTransform clickToTrueLocation = new AffineTransform();
    clickToTrueLocation.translate(xOffset, yOffset);
    clickToTrueLocation.scale(zoomFactor, zoomFactor);
    try {
      clickToTrueLocation.invert();
    } catch (NoninvertibleTransformException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    clickToTrueLocation.transform(mousePoint, trueLocation);

    String genMode = UserInterfaceRegulator.getGeneralMode();
    String subMode = UserInterfaceRegulator.getSubMode();

    Graph currentGraph = GatherX.getCurrentGatheringSystem().getGraph();

    // decides what message to send to controller and what visual information to update based on
    // current UI state
    switch (genMode) {
      case "Insert":
        // if placing a node determines node type and sends information to the controller
        boolean isInsertingNode = false;
        if (subMode.equals("Tee") || subMode.equals("Inlet") || subMode.equals("Outlet")
            || subMode.equals("Compressor") || subMode.equals("Control Valve")) {
          isInsertingNode = true;
        }
        if (isInsertingNode) {
          // determines mode and select appropriate node type
          String nodeType = UserInterfaceRegulator.getSubMode();
          String nodeData = nodeType + ":" + Integer.toString(trueLocation.x) + ","
              + Integer.toString(trueLocation.y);
          boolean tooClose = false;
          // only lets the user place a node if it is far enough away from another node
          for (Node currentNode : currentGraph.getNodes()) {
            // gets node data
            Point nodePoint = currentNode.getLocation();

            // checks if click was too close to another node
            if (Math.abs(nodePoint.getX() - trueLocation.getX()) <= nodeOffset * 2
                && Math.abs(nodePoint.getY() - trueLocation.getY()) <= nodeOffset * 2) {
              tooClose = true;
            }
          }
          if (!tooClose) {
            // sends information to the model through the controller
            Message nodeMessage = new Message(Action.NEW_NODE, nodeData);
            this.dispatcher.dispatch(nodeMessage);
            this.repaint();
          }
        }

        // if placing a pipe determines which nodes pipe is placed between and sends information to
        // the controller
        if (subMode.equals("Pipe")) {
          for (Node currentNode : currentGraph.getNodes()) {
            // gets node data
            Point nodePoint = currentNode.getLocation();
            String currentNodeName = currentNode.getNodeName();

            // checks if click was close enough to a node to select it
            if (Math.abs(nodePoint.getX() - trueLocation.getX()) <= 20
                && Math.abs(nodePoint.getY() - trueLocation.getY()) <= 20) {
              if (isFirstClick) {
                this.firstClickNodeName = currentNodeName;
                this.endNodeNames = currentNodeName;
                isFirstClick = false;
              } else if (!firstClickNodeName.equals(currentNodeName)) {
                this.pipeLocation = "0,0"; // outdated, pipe now treated as line segment
                this.endNodeNames = endNodeNames + "," + currentNodeName;
                this.isFirstClick = true;
                Message pipeMessage =
                    new Message(Action.NEW_PIPE, endNodeNames + ":" + pipeLocation);
                this.dispatcher.dispatch(pipeMessage);
                this.firstClickNodeName = null;
                this.repaint();
              }
            }
          }
        }
        // if deleting determines which node or pipe is selected to be deleted and sends information
        // to the controller
        if (subMode.equals("Delete")) {
          String selectedNodeName =
              UserInterfaceRegulator.clickedNode(currentGraph.getNodes(), trueLocation);
          String selectedPipeName =
              UserInterfaceRegulator.clickedPipe(currentGraph.getPipes(), trueLocation);
          // dispatches message if node was found to be close enough
          if (selectedNodeName != null) {
            Message deleteNodeMessage = new Message(Action.DELETE_NODE, selectedNodeName);
            dispatcher.dispatch(deleteNodeMessage);
          } else if (selectedPipeName != null) {
            Message deletePipeMessage = new Message(Action.DELETE_PIPE, selectedPipeName);
            dispatcher.dispatch(deletePipeMessage);
          }
          this.repaint();
        }
        // if selecting node or pipe
        if (subMode.equals("Select")) {
          this.selectObject(trueLocation, currentGraph.getNodes(), currentGraph.getPipes());
        }
        break;
      case "View":
        selectObject(trueLocation, currentGraph.getNodes(), currentGraph.getPipes());
        break;
      case "Home":
        selectObject(trueLocation, currentGraph.getNodes(), currentGraph.getPipes());
        break;
      case "File":
        selectObject(trueLocation, currentGraph.getNodes(), currentGraph.getPipes());
        break;
      default:
        // selectObject(trueLocation, currentGraph.getNodes(), currentGraph.getPipes());
        break;
    }
  }

  /**
   * Checks if click was close enough to a node to select it.
   * 
   * @param trueLocation the model package location of the click
   * @param nodes list of nodes
   * @param pipes list of pipes
   */
  public void selectObject(Point trueLocation, List<Node> nodes, List<Pipe> pipes) {
    String selectedNodeName = UserInterfaceRegulator.clickedNode(nodes, trueLocation);
    String selectedPipeName = UserInterfaceRegulator.clickedPipe(pipes, trueLocation);
    if (selectedNodeName != null) {
      GatherX.getUiRegulator().setSelectedObjectType("Node");
      GatherX.getUiRegulator().setSelectedObjectName(selectedNodeName);
      GatherX.refreshInterface();
    } else if (selectedPipeName != null) {
      GatherX.getUiRegulator().setSelectedObjectType("Pipe");
      GatherX.getUiRegulator().setSelectedObjectName(selectedPipeName);
      GatherX.refreshInterface();
    }
  }

  public Color getColorFromProperty(String property, Pipe pipe) {
    double min;
    double max;
    double value;
    double allowedError;
    Color lowColor;
    Color midColor;
    Color highColor;
    switch (property) {
      case "Efficiency":
        min = 0d;
        max = 1d;
        lowColor = new Color(255, 0, 0);
        midColor = new Color(255, 255, 0);
        highColor = new Color(0, 255, 0);
        allowedError = 1.20;
        value = pipe.getEfficiency();
        if (Math.abs(value - 1d) <= 1e-6) {
          return UserInterfaceRegulator.LIGHTGREY1;
        }
        break;
      case "Velocity":
        min = 0d;
        max = 40d;
        lowColor = new Color(0, 0, 255);
        midColor = new Color(255, 0, 255);
        highColor = new Color(255, 0, 0);
        allowedError = 1.25;
        value = Math.abs(pipe.getVelocity());
        break;
      case "Pressure Gradient":
        min = 0d;
        max = 30d;
        lowColor = new Color(0, 0, 255);
        midColor = new Color(255, 0, 255);
        highColor = new Color(255, 0, 0);
        allowedError = 1.5;
        value = Math.abs(pipe.getPressureGradient());
        break;
      default:
        min = 0d;
        max = 1d;
        lowColor = new Color(255, 0, 0);
        midColor = new Color(255, 255, 0);
        highColor = new Color(0, 255, 0);
        allowedError = 1.0;
        value = 2;
    }
    double fade = 2 * (value - min) / (max - min);
    int red;
    int green;
    int blue;
    Color color1 = lowColor;
    Color color2 = midColor;
    if (fade >= 1) {
      fade -= 1;
      color1 = midColor;
      color2 = highColor;
    }
    int diffRed = color2.getRed() - color1.getRed();
    int diffGreen = color2.getGreen() - color1.getGreen();
    int diffBlue = color2.getBlue() - color1.getBlue();
    red = (int) Math.floor(color1.getRed() + (diffRed * fade));
    green = (int) Math.floor(color1.getGreen() + (diffGreen * fade));
    blue = (int) Math.floor(color1.getBlue() + (diffBlue * fade));

    if (value >= (max) && value <= (max * allowedError)) {
      return highColor;
    } else if (value > (max * allowedError)) {
      return UserInterfaceRegulator.LIGHTGREY1;
    }
    try {
      Color color = new Color(red, green, blue);
      return color;
    } catch (IllegalArgumentException e) {
      return UserInterfaceRegulator.LIGHTGREY1;
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    released = true;
    repaint();
  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }
}
