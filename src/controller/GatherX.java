package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import display.ConsolePanel;
import display.MainView;
import display.UserInterfaceRegulator;
import display.Verifier;
import model.FlowCalc;
import model.GatheringSystem;
import model.Graph;
import model.SaveLoad;
import model.UndoRedo;

/**
 * The GatherX class is the container for the front and back end of the application and dictates how
 * the user interface refreshes and how project files and undo and redo states are managed.
 * 
 * @author John Coleman
 * @author Nick Chen
 *
 */
public class GatherX {
  private static UserInterfaceRegulator uiRegulator;
  private static MessageDispatch dispatcher;
  private static GatheringSystem currentGatheringSystem;
  private static ArrayList<GatheringSystem> gatheringSystems = new ArrayList<GatheringSystem>();
  private static ArrayList<String> gatheringSystemNames = new ArrayList<String>();
  private static ArrayList<String> filePaths = new ArrayList<String>();
  private static ArrayList<UndoRedo> undoRedoControllers = new ArrayList<UndoRedo>();
  private static int nextSystemID;
  private static boolean makingNewSystem;
  private static boolean switchingGatheringSystem;
  // private static boolean makingNewCalculation;
  private static int indexOfCurrentSystem;
  public static Verifier verifier;
  // change undo limit here
  public final static int UNDO_LIMIT = 5;
  // debug mode variable
  public static final boolean DEBUG = true;

  /**
   * Checks to see if the launcher is running at any given point in time.
   */

  // this block checks to see if the launcher is running at any given point in time

  public static Timer timer = new Timer(4 * (int) (Math.pow(10, 3)), new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      Process p = null;
      try {
        if (System.getProperty("os.name").startsWith("Windows")) {
          p = Runtime.getRuntime().exec("tasklist");
          System.out.println("hello its workings");
        } else if (System.getProperty("os.name").startsWith("Mac")) {
          p = Runtime.getRuntime().exec("pgrep -fl Messages > /dev/null 2>&1");
          Runtime.getRuntime().exec("clear && printf '\\e[3J'");
        }

      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
      InputStream exitVal = p.getInputStream();
      Scanner s = new Scanner(exitVal).useDelimiter("\\A");
      String result = s.hasNext() ? s.next() : "";

      System.out.println("here" + result);
      if (!result.contains("gather-x")) {
        int confirm = JOptionPane.showOptionDialog(GatherX.getUiRegulator().getGraphicInterface(),
            "Launcher has exited. Please check that your license key is still valid, and that you are connected to the internet.",
            "GatherX ", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (confirm == JOptionPane.OK_OPTION) {
          // verifier.setDuration(true);
          int confirm2 =
              JOptionPane.showOptionDialog(GatherX.getUiRegulator().getGraphicInterface(),
                  "Would you like to save your current system before GatherX exits?",
                  "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                  null, null, null);
          if (confirm2 == JOptionPane.NO_OPTION) {
            // verifier.setDuration(true);
            System.exit(0);
          } else {
            saveGatheringSystem();
            System.exit(0);
          }
        }
      }

    }

  });

  /**
   * Default application start GatherX constructor.
   */
  public GatherX() {
    // starts the timer - this will check for the launcher's existence every so often
    timer.start();
    nextSystemID = 0;

    GatheringSystem blankGatheringSystem = new GatheringSystem();
    makingNewSystem = true;
    currentGatheringSystem = blankGatheringSystem;
    blankGatheringSystem.setName("New System.gtrx");
    gatheringSystems.add(blankGatheringSystem);
    gatheringSystemNames.add("New System.gtrx");
    undoRedoControllers.add(new UndoRedo(UNDO_LIMIT, "New System.gtrx"));
    filePaths.add(null);

    dispatcher = new MessageDispatch();
    uiRegulator = new UserInterfaceRegulator();
    uiRegulator.getGraphicInterface().setSystemPanel(gatheringSystemNames);
    indexOfCurrentSystem = 0;
    refreshInterface();
    makingNewSystem = false;
  }

  // TODO move into gatheringSystem class
  public static void newCalculation(String calculationType) {
    currentGatheringSystem.newCalculation(calculationType);
    GatherX.getUiRegulator().setSelectedObjectName(null);
    refreshInterface();
  }

  /**
   * Method that runs when a save state is triggered - i.e something that can be saved occurs. (does
   * this already exist?) Saves the state in the UndoRedo memory stack (stack size controlled by
   * UNDO_LIMIT) Triggered by the following events: - figure drawn on graph, save button in left
   * menu triggered, save button in right menu triggered (in progress)
   * 
   */
  public static void stateModified() {
    undoRedoControllers.get(indexOfCurrentSystem).change(currentGatheringSystem);
  }

  /**
   * When redo button is triggered in MenuPanelListener, currentGatheringSystem is undone (check for
   * empty stack is accounted for in UndoRedo Class)
   * 
   */
  public static void undo() {
    makingNewSystem = true;
    uiRegulator.setSelectedObjectName(null);
    GatheringSystem tempSys = currentGatheringSystem;
    int index = gatheringSystems.indexOf(tempSys);
    int indexName = gatheringSystemNames.indexOf(tempSys.getName());
    GatheringSystem newSys =
        undoRedoControllers.get(indexOfCurrentSystem).undo(currentGatheringSystem);
    currentGatheringSystem = newSys;
    gatheringSystems.set(index, newSys);
    gatheringSystemNames.set(indexName, newSys.getName());
    refreshInterface();
    makingNewSystem = false;
  }

  /**
   * When redo button is triggered in MenuPanelListener, currentGatheringSystem is redone (check for
   * empty stack is accounted for in UndoRedo Class)
   * 
   */
  public static void redo() {
    makingNewSystem = true;
    uiRegulator.setSelectedObjectName(null);
    GatheringSystem tempSys = currentGatheringSystem;
    int index = gatheringSystems.indexOf(tempSys);
    int indexName = gatheringSystemNames.indexOf(tempSys.getName());
    GatheringSystem newSys =
        undoRedoControllers.get(indexOfCurrentSystem).redo(currentGatheringSystem);
    currentGatheringSystem = newSys;
    gatheringSystems.set(index, newSys);
    gatheringSystemNames.set(indexName, newSys.getName());
    refreshInterface();
    makingNewSystem = false;
  }

  /**
   * Creates new blank uniquely named gathering system.
   */
  public static void newGatheringSystem() {
    makingNewSystem = true;
    // ensures unique new name is given
    String newName = "Gathering System " + nextSystemID + ".gtrx";
    while (gatheringSystemNames.contains(newName)
        || gatheringSystemNames.contains(newName.substring(0, newName.length() - 5))) {
      nextSystemID++;
      newName = "Gathering System " + nextSystemID + ".gtrx";
    }

    // creates new system and adds it to mappings
    GatheringSystem blankGatheringSystem = new GatheringSystem();
    blankGatheringSystem.setName(newName);
    gatheringSystems.add(blankGatheringSystem);
    gatheringSystemNames.add(newName);
    setCurrentGatheringSystem(blankGatheringSystem, newName);
    undoRedoControllers.add(new UndoRedo(UNDO_LIMIT, newName));
    filePaths.add(null);
    refreshInterface();
    makingNewSystem = false;
  }

  /**
   * Closes the selected gathering system
   */
  public static void closeGatheringSystem() {
    // closes selected gathering system, will close entire application if closing last system
    // selected
    switchingGatheringSystem = true;
    String systemName = currentGatheringSystem.getName();
    if (gatheringSystemNames.size() > 1) {
      if (gatheringSystemNames.contains(systemName)) {
        int index = gatheringSystemNames.indexOf(systemName);
        gatheringSystemNames.remove(index);
        gatheringSystems.remove(index);
        undoRedoControllers.remove(index);
        filePaths.remove(index);
        currentGatheringSystem = gatheringSystems.get(0);
        indexOfCurrentSystem = 0;
      }
    } else {
      int confirm = JOptionPane.showOptionDialog(GatherX.getUiRegulator().getGraphicInterface(),
          "Are You Sure You Want to Close GatherX? Unsaved Work Will Be Lost.", "Exit Confirmation",
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
      if (confirm == JOptionPane.YES_OPTION) {
        // verifier.setDuration(true);
        System.exit(0);
      }
    }
    refreshInterface();
    switchingGatheringSystem = false;
  }

  /**
   * Opens the gathering system saved at a given file path.
   * 
   * @param filePath the location of the saved gathering system.
   */
  public static void openGatheringSystem(String filePath) {
    // sets the view to display the selected file if already open
    if (filePaths.contains(filePath)) {
      currentGatheringSystem = gatheringSystems.get(filePaths.indexOf(filePath));
    } else {
      // loads the file
      GatheringSystem openedSystem = SaveLoad.loadFile(filePath);
      String openName = openedSystem.getName();
      if (gatheringSystemNames.contains(openName)
          || gatheringSystemNames.contains(openName.substring(0, openName.length() - 5))) {
        String[] warnings = {"The name (" + openName
            + ") is the same as another open file. Please select a different name or close out of ("
            + openName + ")."};
        for (String warning : warnings) {
          System.out.println(warning);
        }
      } else {
        // updates gatherX mappings
        currentGatheringSystem = openedSystem;
        gatheringSystems.add(openedSystem);
        gatheringSystemNames.add(openedSystem.getName());
        undoRedoControllers.add(new UndoRedo(UNDO_LIMIT, openedSystem.getName()));
        filePaths.add(filePath);
      }
    }
    refreshInterface();
  }

  /**
   * Opens the gathering system saved at a given file path.
   *
   * @param filePath the location of the saved gathering system.
   */
  public static void importGatheringSystem(String filePath) {
    // sets the view to display the selected file if already open
    makingNewSystem = true;
    if (filePaths.contains(filePath)) {
      currentGatheringSystem = gatheringSystems.get(filePaths.indexOf(filePath));
    } else {
      // loads the file
      GatheringSystem openedSystem = SaveLoad.importCSV(filePath);
      String openName = openedSystem.getName();
      if (gatheringSystemNames.contains(openName)
          || gatheringSystemNames.contains(openName.substring(0, openName.length() - 5))) {
        String[] warnings = {"The name (" + openName
            + ") is the same as another open file. Please select a different name or close out of ("
            + openName + ")."};
        for (String warning : warnings) {
          System.out.println(warning);
        }
      } else {
        // updates gatherX mappings
        currentGatheringSystem = openedSystem;
        gatheringSystems.add(openedSystem);
        gatheringSystemNames.add(openedSystem.getName());
        undoRedoControllers.add(new UndoRedo(UNDO_LIMIT, openedSystem.getName()));
        filePaths.add(filePath);
      }
    }
    refreshInterface();
    makingNewSystem = false;
  }

  /**
   * Saves the current system if there is a file path for it, otherwise forces user to set one.
   */
  public static void saveGatheringSystem() {
    int index = gatheringSystems.indexOf(currentGatheringSystem);
    if (filePaths.size() > 0) {
      if (filePaths.get(index) != null) {
        SaveLoad.saveFile(filePaths.get(index), currentGatheringSystem);
      } else {
        String filePath = MainView.createFileSaverWindow();
        dispatcher.dispatch(new Message(Action.SAVE_FILE_AS, filePath));
      }
    } else {
      String filePath = MainView.createFileSaverWindow();
      dispatcher.dispatch(new Message(Action.SAVE_FILE_AS, filePath));
    }
  }

  /**
   * Exports the file and sets the current system name to be the file name.
   *
   * @param filePath the location where the project is to be saved.
   */
  public static void exportGatheringSystem(String filePath) {
    String newSystemName = new File(filePath).getName();
    if (gatheringSystemNames.contains(newSystemName)
        || gatheringSystemNames.contains(newSystemName.substring(0, newSystemName.length() - 5))) {
      String[] warnings = {"The name (" + newSystemName
          + ") is the same as another open file. Please select a different name or close out of ("
          + newSystemName + ")."};
      for (String warning : warnings) {
        System.out.println(warning);
      }
    } else {
      currentGatheringSystem.setName(newSystemName);
      int index = gatheringSystems.indexOf(currentGatheringSystem);
      gatheringSystemNames.set(index, newSystemName);
      SaveLoad.exportCSV(filePath, currentGatheringSystem);
      filePaths.set(index, filePath);
      refreshInterface();
    }
  }

  /**
   * Saves the file and sets the current system name to be the file name.
   * 
   * @param filePath the location where the project is to be saved.
   */
  public static void saveAsGatheringSystem(String filePath) {
    String newSystemName = new File(filePath).getName();
    if (gatheringSystemNames.contains(newSystemName)
        || gatheringSystemNames.contains(newSystemName.substring(0, newSystemName.length() - 5))) {
      String[] warnings = {"The name (" + newSystemName
          + ") is the same as another open file. Please select a different name or close out of ("
          + newSystemName + ")."};
      for (String warning : warnings) {
        System.out.println(warning);
      }
    } else {
      currentGatheringSystem.setName(newSystemName);
      int index = gatheringSystems.indexOf(currentGatheringSystem);
      gatheringSystemNames.set(index, newSystemName);
      SaveLoad.saveFile(filePath, currentGatheringSystem);
      filePaths.set(index, filePath);
      refreshInterface();
    }
  }

  /**
   * Saves all open gathering systems in a folder.
   * 
   * @param filePath the location of the folder where all open systems will be saved
   */
  public static void saveAllGatheringSystems(String filePath) {
    for (int index = 0; index < gatheringSystems.size(); index++) {
      GatheringSystem gather = gatheringSystems.get(index);
      if (gather.getName().endsWith(".gtrx")) {
        SaveLoad.saveFile(filePath + "\\" + gather.getName(), gather);
        filePaths.set(index, filePath + "\\" + gather.getName());
      } else {
        SaveLoad.saveFile(filePath + "\\" + gather.getName() + ".gtrx", gather);
        filePaths.set(index, filePath + "\\" + gather.getName() + ".gtrx");
      }
    }
  }

  public static GatheringSystem getCurrentGatheringSystem() {
    return currentGatheringSystem;
  }

  /**
   * Set the active gathering system open on the user interface.
   * 
   * @param currentGatheringSystem the gathering system to be set active.
   * @param name the name of the gathering system.
   */
  public static void setCurrentGatheringSystem(GatheringSystem currentGatheringSystem,
      String name) {
    GatherX.currentGatheringSystem = currentGatheringSystem;
    indexOfCurrentSystem = gatheringSystemNames.indexOf(name);
  }

  /**
   * Set the active gathering system open on the user interface based on the system's name
   * 
   * @param name the name of the gathering system.
   */
  public static void setCurrentGatheringSystem(String name) {
    switchingGatheringSystem = true;
    int index = gatheringSystemNames.indexOf(name);

    // only set a new system to current if it is not the same as the old one
    if (!GatherX.currentGatheringSystem.equals(gatheringSystems.get(index))) {
      GatherX.currentGatheringSystem = gatheringSystems.get(index);
      indexOfCurrentSystem = index;
      refreshInterface();
    }
    switchingGatheringSystem = false;
  }

  public static UserInterfaceRegulator getUiRegulator() {
    return uiRegulator;
  }

  public static void setUiRegulator(UserInterfaceRegulator uiRegulator) {
    GatherX.uiRegulator = uiRegulator;
  }

  public static MessageDispatch getDispatcher() {
    return dispatcher;
  }

  public static void setDispatcher(MessageDispatch dispatcher) {
    GatherX.dispatcher = dispatcher;
  }

  public static ArrayList<String> getGatheringSystemNames() {
    return gatheringSystemNames;
  }

  public static void setGatheringSystemNames(ArrayList<String> gatheringSystemNames) {
    GatherX.gatheringSystemNames = gatheringSystemNames;
  }

  public static int getNextSystemID() {
    return nextSystemID;
  }

  public static void setNextSystemID(int nextSystemID) {
    GatherX.nextSystemID = nextSystemID;
  }

  /**
   * Refreshes the user interface to represent the state of the model or to respond to a user
   * action.
   */
  public static void refreshInterface() {
    // prints any thrown exceptions out to the console
    ConsolePanel.setConsoleText();
    // gathers all back end data and puts it into local variables
    String panelType = "";
    String selectedName = null;
    Graph currentGraph = GatherX.getCurrentGatheringSystem().getGraph();

    ArrayList<String> systemNames = GatherX.getGatheringSystemNames();
    boolean newSystem = makingNewSystem;
    boolean switchingSystem = switchingGatheringSystem;
    if (GatherX.getUiRegulator().getSelectedObjectName() != null && !newSystem
        && !switchingSystem) {
      if (GatherX.getUiRegulator().getSelectedObjectType().equals("Node")) {
        selectedName = GatherX.getUiRegulator().getSelectedObjectName();
        panelType = "Node," + currentGraph.getNode(selectedName).getType();
      } else if (GatherX.getUiRegulator().getSelectedObjectType().equals("Pipe")) {
        selectedName = GatherX.getUiRegulator().getSelectedObjectName();
        panelType = "Pipe,NA";
      }
    }
    String dataPanelType = panelType;
    FlowCalc calculation = currentGatheringSystem.getCalculation();
    boolean redosLeft = GatherX.undoRedoControllers.get(indexOfCurrentSystem).moreRedosAllowed();
    boolean undosLeft = GatherX.undoRedoControllers.get(indexOfCurrentSystem).moreUndosAllowed();
    SwingUtilities.invokeLater(() -> {
      // sets system panel
      uiRegulator.getGraphicInterface().setSystemPanel(systemNames);
      // sets id panel
      GatherX.uiRegulator.getGraphicInterface().setIdPanel(currentGraph);
      // sets data panel
      if (GatherX.getUiRegulator().getSelectedObjectName() != null && !newSystem
          && !switchingSystem) {
        GatherX.getUiRegulator().getGraphicInterface().setDataPanel(dataPanelType);
      } else {
        GatherX.uiRegulator.getGraphicInterface().setDataPanel();
      }
      GatherX.uiRegulator.getGraphicInterface().setCalculationPanel(calculation);
      // sets other panels
      GatherX.uiRegulator.getGraphicInterface().setSystemPanel(systemNames);
      GatherX.uiRegulator.setDisplayFont(GatherX.uiRegulator.getDisplayFont().getSize());
      // sets redo and undo buttons
      GatherX.uiRegulator.getGraphicInterface().setChangeButtons(redosLeft, undosLeft);
      // repaints interface
      GatherX.uiRegulator.getGraphicInterface().revalidate();
      GatherX.uiRegulator.getGraphicInterface().repaint();
    });
  }
}
